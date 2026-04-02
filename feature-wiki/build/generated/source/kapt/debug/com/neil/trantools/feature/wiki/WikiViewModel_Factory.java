package com.neil.trantools.feature.wiki;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class WikiViewModel_Factory implements Factory<WikiViewModel> {
  private final Provider<Context> appContextProvider;

  private WikiViewModel_Factory(Provider<Context> appContextProvider) {
    this.appContextProvider = appContextProvider;
  }

  @Override
  public WikiViewModel get() {
    return newInstance(appContextProvider.get());
  }

  public static WikiViewModel_Factory create(Provider<Context> appContextProvider) {
    return new WikiViewModel_Factory(appContextProvider);
  }

  public static WikiViewModel newInstance(Context appContext) {
    return new WikiViewModel(appContext);
  }
}
