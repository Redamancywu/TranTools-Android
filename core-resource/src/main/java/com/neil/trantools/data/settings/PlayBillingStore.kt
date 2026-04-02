package com.neil.trantools.data.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

object PlayBillingStore {
    private const val productId = "trantools_pro_monthly"
    private const val productType = BillingClient.ProductType.SUBS
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val initialized = AtomicBoolean(false)
    private val _subscriptionState = MutableStateFlow<LocalSubscriptionState?>(null)
    private var billingClient: BillingClient? = null
    private var productDetailsCache: ProductDetails? = null
    private var applicationContext: Context? = null

    fun observeState(): StateFlow<LocalSubscriptionState?> = _subscriptionState.asStateFlow()
    fun currentState(): LocalSubscriptionState? = _subscriptionState.value

    fun init(context: Context) {
        if (!initialized.compareAndSet(false, true)) return
        val appContext = context.applicationContext
        applicationContext = appContext
        val listener = PurchasesUpdatedListener { result, purchases ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                handlePurchases(purchases.orEmpty())
            }
        }
        billingClient = BillingClient.newBuilder(appContext)
            .setListener(listener)
            .enablePendingPurchases()
            .build()
        connectAndRefresh()
    }

    fun launchSubscribe(activity: Activity) {
        connectAndRefresh()
        val client = billingClient ?: return
        val details = productDetailsCache
        if (details != null) {
            launchBillingFlow(activity, client, details)
            return
        }

        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(productId)
            .setProductType(productType)
            .build()

        client.queryProductDetailsAsync(
            QueryProductDetailsParams.newBuilder().setProductList(listOf(product)).build()
        ) { result, detailsList ->
            if (result.responseCode != BillingClient.BillingResponseCode.OK) return@queryProductDetailsAsync
            val detailsFound = detailsList.firstOrNull() ?: return@queryProductDetailsAsync
            productDetailsCache = detailsFound
            launchBillingFlow(activity, client, detailsFound)
        }
    }

    fun restorePurchases() {
        connectAndRefresh()
    }

    fun openManageSubscriptions(context: Context) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/account/subscriptions")
        )
        context.startActivity(intent)
    }

    private fun connectAndRefresh() {
        val client = billingClient ?: return
        if (client.isReady) {
            queryExistingPurchases(client)
            return
        }
        client.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() = Unit

            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryExistingPurchases(client)
                }
            }
        })
    }

    private fun queryExistingPurchases(client: BillingClient) {
        client.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(productType).build()
        ) { result, purchases ->
            if (result.responseCode != BillingClient.BillingResponseCode.OK) return@queryPurchasesAsync
            handlePurchases(purchases)
        }
    }

    private fun handlePurchases(purchases: List<Purchase>) {
        val active = purchases.any { purchase ->
            purchase.products.contains(productId) && purchase.purchaseState == Purchase.PurchaseState.PURCHASED
        }

        if (active) {
            purchases.forEach { purchase ->
                if (!purchase.isAcknowledged) {
                    acknowledgePurchase(purchase.purchaseToken)
                }
            }
            _subscriptionState.value = LocalSubscriptionState.Pro
            syncSubscriptionStore(LocalSubscriptionState.Pro)
        } else {
            _subscriptionState.value = LocalSubscriptionState.Free
            syncSubscriptionStore(LocalSubscriptionState.Free)
        }
    }

    private fun syncSubscriptionStore(state: LocalSubscriptionState) {
        val context = applicationContext ?: return
        scope.launch {
            SubscriptionStore.set(context, state)
        }
    }

    private fun acknowledgePurchase(token: String) {
        val client = billingClient ?: return
        scope.launch {
            client.acknowledgePurchase(
                AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(token)
                    .build()
            ) { _ -> Unit }
        }
    }

    private fun launchBillingFlow(
        activity: Activity,
        client: BillingClient,
        productDetails: ProductDetails,
    ) {
        val offerToken = productDetails.subscriptionOfferDetails
            ?.firstOrNull()
            ?.offerToken
            ?: return

        val params = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .setOfferToken(offerToken)
            .build()

        client.launchBillingFlow(
            activity,
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(params))
                .build()
        )
    }
}
