/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.logging.Level;
/*     */ import javax.annotation.CheckForNull;
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
/*     */ @GwtCompatible
/*     */ class ImmediateFuture<V>
/*     */   implements ListenableFuture<V>
/*     */ {
/*  33 */   static final ListenableFuture<?> NULL = new ImmediateFuture(null);
/*     */   
/*  35 */   private static final LazyLogger log = new LazyLogger(ImmediateFuture.class);
/*     */   @ParametricNullness
/*     */   private final V value;
/*     */   
/*     */   ImmediateFuture(@ParametricNullness V value) {
/*  40 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addListener(Runnable listener, Executor executor) {
/*  46 */     Preconditions.checkNotNull(listener, "Runnable was null.");
/*  47 */     Preconditions.checkNotNull(executor, "Executor was null.");
/*     */     try {
/*  49 */       executor.execute(listener);
/*  50 */     } catch (Exception e) {
/*     */ 
/*     */       
/*  53 */       log.get()
/*  54 */         .log(Level.SEVERE, "RuntimeException while executing runnable " + listener + " with executor " + executor, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  66 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   public V get() {
/*  73 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   public V get(long timeout, TimeUnit unit) throws ExecutionException {
/*  79 */     Preconditions.checkNotNull(unit);
/*  80 */     return get();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  90 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  96 */     return super.toString() + "[status=SUCCESS, result=[" + this.value + "]]";
/*     */   }
/*     */   
/*     */   static final class ImmediateFailedFuture<V> extends AbstractFuture.TrustedFuture<V> {
/*     */     ImmediateFailedFuture(Throwable thrown) {
/* 101 */       setException(thrown);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ImmediateCancelledFuture<V>
/*     */     extends AbstractFuture.TrustedFuture<V> {
/*     */     @CheckForNull
/* 108 */     static final ImmediateCancelledFuture<Object> INSTANCE = AbstractFuture.GENERATE_CANCELLATION_CAUSES ? null : new ImmediateCancelledFuture();
/*     */     
/*     */     ImmediateCancelledFuture() {
/* 111 */       cancel(false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/ImmediateFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */