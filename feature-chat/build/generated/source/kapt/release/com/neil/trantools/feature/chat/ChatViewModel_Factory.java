package com.neil.trantools.feature.chat;

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
public final class ChatViewModel_Factory implements Factory<ChatViewModel> {
  private final Provider<Application> applicationProvider;

  private ChatViewModel_Factory(Provider<Application> applicationProvider) {
    this.applicationProvider = applicationProvider;
  }

  @Override
  public ChatViewModel get() {
    return newInstance(applicationProvider.get());
  }

  public static ChatViewModel_Factory create(Provider<Application> applicationProvider) {
    return new ChatViewModel_Factory(applicationProvider);
  }

  public static ChatViewModel newInstance(Application application) {
    return new ChatViewModel(application);
  }
}
