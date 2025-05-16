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
public final class GetTasksUseCase_Factory implements Factory<GetTasksUseCase> {
  private final Provider<TaskRepository> taskRepositoryProvider;

  public GetTasksUseCase_Factory(Provider<TaskRepository> taskRepositoryProvider) {
    this.taskRepositoryProvider = taskRepositoryProvider;
  }

  @Override
  public GetTasksUseCase get() {
    return newInstance(taskRepositoryProvider.get());
  }

  public static GetTasksUseCase_Factory create(Provider<TaskRepository> taskRepositoryProvider) {
    return new GetTasksUseCase_Factory(taskRepositoryProvider);
  }

  public static GetTasksUseCase newInstance(TaskRepository taskRepository) {
    return new GetTasksUseCase(taskRepository);
  }
}
