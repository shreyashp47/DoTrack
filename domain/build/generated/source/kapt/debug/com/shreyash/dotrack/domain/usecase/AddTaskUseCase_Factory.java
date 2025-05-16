package com.shreyash.dotrack.domain.usecase;

import com.shreyash.dotrack.domain.repository.TaskRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class AddTaskUseCase_Factory implements Factory<AddTaskUseCase> {
  private final Provider<TaskRepository> taskRepositoryProvider;

  public AddTaskUseCase_Factory(Provider<TaskRepository> taskRepositoryProvider) {
    this.taskRepositoryProvider = taskRepositoryProvider;
  }

  @Override
  public AddTaskUseCase get() {
    return newInstance(taskRepositoryProvider.get());
  }

  public static AddTaskUseCase_Factory create(Provider<TaskRepository> taskRepositoryProvider) {
    return new AddTaskUseCase_Factory(taskRepositoryProvider);
  }

  public static AddTaskUseCase newInstance(TaskRepository taskRepository) {
    return new AddTaskUseCase(taskRepository);
  }
}
