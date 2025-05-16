package com.shreyash.dotrack.data.di;

import android.content.Context;
import com.shreyash.dotrack.data.local.TaskDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
    "cast"
})
public final class DatabaseModule_ProvideTaskDatabaseFactory implements Factory<TaskDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideTaskDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public TaskDatabase get() {
    return provideTaskDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideTaskDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideTaskDatabaseFactory(contextProvider);
  }

  public static TaskDatabase provideTaskDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTaskDatabase(context));
  }
}
