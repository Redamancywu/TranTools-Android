package com.neil.trantools.feature.voice;

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
public final class VoiceViewModel_Factory implements Factory<VoiceViewModel> {
  private final Provider<Application> applicationProvider;

  private VoiceViewModel_Factory(Provider<Application> applicationProvider) {
    this.applicationProvider = applicationProvider;
  }

  @Override
  public VoiceViewModel get() {
    return newInstance(applicationProvider.get());
  }

  public static VoiceViewModel_Factory create(Provider<Application> applicationProvider) {
    return new VoiceViewModel_Factory(applicationProvider);
  }

  public static VoiceViewModel newInstance(Application application) {
    return new VoiceViewModel(application);
  }
}
