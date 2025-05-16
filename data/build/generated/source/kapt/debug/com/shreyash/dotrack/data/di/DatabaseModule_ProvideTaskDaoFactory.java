package com.shreyash.dotrack.data.di;

import com.shreyash.dotrack.data.local.TaskDatabase;
import com.shreyash.dotrack.data.local.dao.TaskDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "cast"
})
public final class DatabaseModule_ProvideTaskDaoFactory implements Factory<TaskDao> {
  private final Provider<TaskDatabase> databaseProvider;

  public DatabaseModule_ProvideTaskDaoFactory(Provider<TaskDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TaskDao get() {
    return provideTaskDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideTaskDaoFactory create(
      Provider<TaskDatabase> databaseProvider) {
    return new DatabaseModule_ProvideTaskDaoFactory(databaseProvider);
  }

  public static TaskDao provideTaskDao(TaskDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTaskDao(database));
  }
}
