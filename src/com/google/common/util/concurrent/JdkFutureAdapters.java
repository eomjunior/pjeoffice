/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class JdkFutureAdapters
/*     */ {
/*     */   public static <V> ListenableFuture<V> listenInPoolThread(Future<V> future) {
/*  59 */     if (future instanceof ListenableFuture) {
/*  60 */       return (ListenableFuture<V>)future;
/*     */     }
/*  62 */     return new ListenableFutureAdapter<>(future);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <V> ListenableFuture<V> listenInPoolThread(Future<V> future, Executor executor) {
/*  87 */     Preconditions.checkNotNull(executor);
/*  88 */     if (future instanceof ListenableFuture) {
/*  89 */       return (ListenableFuture<V>)future;
/*     */     }
/*  91 */     return new ListenableFutureAdapter<>(future, executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ListenableFutureAdapter<V>
/*     */     extends ForwardingFuture<V>
/*     */     implements ListenableFuture<V>
/*     */   {
/* 106 */     private static final ThreadFactory threadFactory = (new ThreadFactoryBuilder())
/*     */       
/* 108 */       .setDaemon(true)
/* 109 */       .setNameFormat("ListenableFutureAdapter-thread-%d")
/* 110 */       .build();
/*     */     
/* 112 */     private static final Executor defaultAdapterExecutor = Executors.newCachedThreadPool(threadFactory);
/*     */ 
/*     */     
/*     */     private final Executor adapterExecutor;
/*     */     
/* 117 */     private final ExecutionList executionList = new ExecutionList();
/*     */ 
/*     */ 
/*     */     
/* 121 */     private final AtomicBoolean hasListeners = new AtomicBoolean(false);
/*     */     
/*     */     private final Future<V> delegate;
/*     */ 
/*     */     
/*     */     ListenableFutureAdapter(Future<V> delegate) {
/* 127 */       this(delegate, defaultAdapterExecutor);
/*     */     }
/*     */     
/*     */     ListenableFutureAdapter(Future<V> delegate, Executor adapterExecutor) {
/* 131 */       this.delegate = (Future<V>)Preconditions.checkNotNull(delegate);
/* 132 */       this.adapterExecutor = (Executor)Preconditions.checkNotNull(adapterExecutor);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Future<V> delegate() {
/* 137 */       return this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addListener(Runnable listener, Executor exec) {
/* 142 */       this.executionList.add(listener, exec);
/*     */ 
/*     */ 
/*     */       
/* 146 */       if (this.hasListeners.compareAndSet(false, true)) {
/* 147 */         if (this.delegate.isDone()) {
/*     */ 
/*     */           
/* 150 */           this.executionList.execute();
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 155 */         this.adapterExecutor.execute(() -> {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               try {
/*     */ 
/*     */                 
/*     */                 Uninterruptibles.getUninterruptibly(this.delegate);
/* 164 */               } catch (Throwable throwable) {}
/*     */               this.executionList.execute();
/*     */             });
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/JdkFutureAdapters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */