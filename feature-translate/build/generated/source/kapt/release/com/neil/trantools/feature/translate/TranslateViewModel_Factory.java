package com.neil.trantools.feature.translate;

import android.app.Application;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
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
public final class TranslateViewModel_Factory implements Factory<TranslateViewModel> {
  private final Provider<Application> applicationProvider;

  private TranslateViewModel_Factory(Provider<Application> applicationProvider) {
    this.applicationProvider = applicationProvider;
  }

  @Override
  public TranslateViewModel get() {
    return newInstance(applicationProvider.get());
  }

  public static TranslateViewModel_Factory create(Provider<Application> applicationProvider) {
    return new TranslateViewModel_Factory(applicationProvider);
  }

  public static TranslateViewModel newInstance(Application application) {
    return new TranslateViewModel(application);
  }
}
