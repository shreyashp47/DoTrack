package com.shreyash.dotrack.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J6\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0011J\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\u0013\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u0014J\u001c\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\u0013\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u0014J\u001c\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\b0\u00172\u0006\u0010\u0013\u001a\u00020\u000bH\u0016J\u001a\u0010\u0019\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u001a0\b0\u0017H\u0016J\u001c\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\u0013\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\u0014J\u001c\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\u001d\u001a\u00020\u0018H\u0096@\u00a2\u0006\u0002\u0010\u001eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/shreyash/dotrack/data/repository/TaskRepositoryImpl;", "Lcom/shreyash/dotrack/domain/repository/TaskRepository;", "taskDao", "Lcom/shreyash/dotrack/data/local/dao/TaskDao;", "ioDispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "(Lcom/shreyash/dotrack/data/local/dao/TaskDao;Lkotlinx/coroutines/CoroutineDispatcher;)V", "addTask", "Lcom/shreyash/dotrack/core/util/Result;", "", "title", "", "description", "dueDate", "Ljava/time/LocalDateTime;", "priority", "Lcom/shreyash/dotrack/domain/model/Priority;", "(Ljava/lang/String;Ljava/lang/String;Ljava/time/LocalDateTime;Lcom/shreyash/dotrack/domain/model/Priority;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "completeTask", "id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteTask", "getTaskById", "Lkotlinx/coroutines/flow/Flow;", "Lcom/shreyash/dotrack/domain/model/Task;", "getTasks", "", "uncompleteTask", "updateTask", "task", "(Lcom/shreyash/dotrack/domain/model/Task;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "data_debug"})
public final class TaskRepositoryImpl implements com.shreyash.dotrack.domain.repository.TaskRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.shreyash.dotrack.data.local.dao.TaskDao taskDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineDispatcher ioDispatcher = null;
    
    @javax.inject.Inject()
    public TaskRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.shreyash.dotrack.data.local.dao.TaskDao taskDao, @com.shreyash.dotrack.core.di.IoDispatcher()
    @org.jetbrains.annotations.NotNull()
    kotlinx.coroutines.CoroutineDispatcher ioDispatcher) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.shreyash.dotrack.core.util.Result<java.util.List<com.shreyash.dotrack.domain.model.Task>>> getTasks() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.shreyash.dotrack.core.util.Result<com.shreyash.dotrack.domain.model.Task>> getTaskById(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object addTask(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime dueDate, @org.jetbrains.annotations.NotNull()
    com.shreyash.dotrack.domain.model.Priority priority, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.shreyash.dotrack.core.util.Result<kotlin.Unit>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateTask(@org.jetbrains.annotations.NotNull()
    com.shreyash.dotrack.domain.model.Task task, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.shreyash.dotrack.core.util.Result<kotlin.Unit>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deleteTask(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.shreyash.dotrack.core.util.Result<kotlin.Unit>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object completeTask(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.shreyash.dotrack.core.util.Result<kotlin.Unit>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object uncompleteTask(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.shreyash.dotrack.core.util.Result<kotlin.Unit>> $completion) {
        return null;
    }
}