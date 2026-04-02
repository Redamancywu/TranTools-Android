package com.neil.trantools.feature.gems;

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
public final class GemsViewModel_Factory implements Factory<GemsViewModel> {
  private final Provider<Application> applicationProvider;

  private GemsViewModel_Factory(Provider<Application> applicationProvider) {
    this.applicationProvider = applicationProvider;
  }

  @Override
  public GemsViewModel get() {
    return newInstance(applicationProvider.get());
  }

  public static GemsViewModel_Factory create(Provider<Application> applicationProvider) {
    return new GemsViewModel_Factory(applicationProvider);
  }

  public static GemsViewModel newInstance(Application application) {
    return new GemsViewModel(application);
  }
}
