package com.shreyash.dotrack.domain.usecase;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J6\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\b\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u000eH\u0086B\u00a2\u0006\u0002\u0010\u000fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/shreyash/dotrack/domain/usecase/AddTaskUseCase;", "", "taskRepository", "Lcom/shreyash/dotrack/domain/repository/TaskRepository;", "(Lcom/shreyash/dotrack/domain/repository/TaskRepository;)V", "invoke", "Lcom/shreyash/dotrack/core/util/Result;", "", "title", "", "description", "dueDate", "Ljava/time/LocalDateTime;", "priority", "Lcom/shreyash/dotrack/domain/model/Priority;", "(Ljava/lang/String;Ljava/lang/String;Ljava/time/LocalDateTime;Lcom/shreyash/dotrack/domain/model/Priority;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "domain_debug"})
public final class AddTaskUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.shreyash.dotrack.domain.repository.TaskRepository taskRepository = null;
    
    @javax.inject.Inject()
    public AddTaskUseCase(@org.jetbrains.annotations.NotNull()
    com.shreyash.dotrack.domain.repository.TaskRepository taskRepository) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object invoke(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime dueDate, @org.jetbrains.annotations.NotNull()
    com.shreyash.dotrack.domain.model.Priority priority, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.shreyash.dotrack.core.util.Result<kotlin.Unit>> $completion) {
        return null;
    }
}