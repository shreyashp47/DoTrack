package com.shreyash.dotrack.data.repository;

import com.shreyash.dotrack.data.local.dao.TaskDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineDispatcher;

@ScopeMetadata
@QualifierMetadata("com.shreyash.dotrack.core.di.IoDispatcher")
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
public final class TaskRepositoryImpl_Factory implements Factory<TaskRepositoryImpl> {
  private final Provider<TaskDao> taskDaoProvider;

  private final Provider<CoroutineDispatcher> ioDispatcherProvider;

  public TaskRepositoryImpl_Factory(Provider<TaskDao> taskDaoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    this.taskDaoProvider = taskDaoProvider;
    this.ioDispatcherProvider = ioDispatcherProvider;
  }

  @Override
  public TaskRepositoryImpl get() {
    return newInstance(taskDaoProvider.get(), ioDispatcherProvider.get());
  }

  public static TaskRepositoryImpl_Factory create(Provider<TaskDao> taskDaoProvider,
      Provider<CoroutineDispatcher> ioDispatcherProvider) {
    return new TaskRepositoryImpl_Factory(taskDaoProvider, ioDispatcherProvider);
  }

  public static TaskRepositoryImpl newInstance(TaskDao taskDao, CoroutineDispatcher ioDispatcher) {
    return new TaskRepositoryImpl(taskDao, ioDispatcher);
  }
}
