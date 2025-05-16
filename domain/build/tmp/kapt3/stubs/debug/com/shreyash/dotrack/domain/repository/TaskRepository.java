package com.shreyash.dotrack.domain.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J6\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\b\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000bH\u00a6@\u00a2\u0006\u0002\u0010\fJ\u001c\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u000e\u001a\u00020\u0006H\u00a6@\u00a2\u0006\u0002\u0010\u000fJ\u001c\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u000e\u001a\u00020\u0006H\u00a6@\u00a2\u0006\u0002\u0010\u000fJ\u001c\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00030\u00122\u0006\u0010\u000e\u001a\u00020\u0006H&J\u001a\u0010\u0014\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00150\u00030\u0012H&J\u001c\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u000e\u001a\u00020\u0006H\u00a6@\u00a2\u0006\u0002\u0010\u000fJ\u001c\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0018\u001a\u00020\u0013H\u00a6@\u00a2\u0006\u0002\u0010\u0019\u00a8\u0006\u001a"}, d2 = {"Lcom/shreyash/dotrack/domain/repository/TaskRepository;", "", "addTask", "Lcom/shreyash/dotrack/core/util/Result;", "", "title", "", "description", "dueDate", "Ljava/time/LocalDateTime;", "priority", "Lcom/shreyash/dotrack/domain/model/Priority;", "(Ljava/lang/String;Ljava/lang/String;Ljava/time/LocalDateTime;Lcom/shreyash/dotrack/domain/model/Priority;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "completeTask", "id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteTask", "getTaskById", "Lkotlinx/coroutines/flow/Flow;", "Lcom/shreyash/dotrack/domain/model/Task;", "getTasks", "", "uncompleteTask", "updateTask", "task", "(Lcom/shreyash/dotrack/domain/model/Task;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "domain_debug"})
public abstract interface TaskRepository {
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.shreyash.dotrack.core.util.Result<java.util.List<com.shreyash.dotrack.domain.model.Task>>> getTasks();
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.shreyash.dotrack.core.util.Result<com.shreyash.dotrack.domain.model.Task>> getTaskById(@org.jetbrains.annotations.NotNull()
    java.lang.String id);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object addTask(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.Nullable()
    java.time.LocalDateTime dueDate, @org.jetbrains.annotations.NotNull()
    com.shreyash.dotrack.domain.model.Priority priority, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.shreyash.dotrack.core.util.Result<kotlin.Unit>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateTask(@org.jetbrains.annotations.NotNull()
    com.shreyash.dotrack.domain.model.Task task, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.shreyash.dotrack.core.util.Result<kotlin.Unit>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteTask(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.shreyash.dotrack.core.util.Result<kotlin.Unit>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object completeTask(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.shreyash.dotrack.core.util.Result<kotlin.Unit>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object uncompleteTask(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.shreyash.dotrack.core.util.Result<kotlin.Unit>> $completion);
}