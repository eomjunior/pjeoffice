/*     */ package io.reactivex.internal.subscriptions;
/*     */ 
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.reactivestreams.Subscriber;
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
/*     */ public final class ScalarSubscription<T>
/*     */   extends AtomicInteger
/*     */   implements QueueSubscription<T>
/*     */ {
/*     */   private static final long serialVersionUID = -3830916580126663321L;
/*     */   final T value;
/*     */   final Subscriber<? super T> subscriber;
/*     */   static final int NO_REQUEST = 0;
/*     */   static final int REQUESTED = 1;
/*     */   static final int CANCELLED = 2;
/*     */   
/*     */   public ScalarSubscription(Subscriber<? super T> subscriber, T value) {
/*  43 */     this.subscriber = subscriber;
/*  44 */     this.value = value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void request(long n) {
/*  49 */     if (!SubscriptionHelper.validate(n)) {
/*     */       return;
/*     */     }
/*  52 */     if (compareAndSet(0, 1)) {
/*  53 */       Subscriber<? super T> s = this.subscriber;
/*     */       
/*  55 */       s.onNext(this.value);
/*  56 */       if (get() != 2) {
/*  57 */         s.onComplete();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancel() {
/*  65 */     lazySet(2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  73 */     return (get() == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offer(T e) {
/*  78 */     throw new UnsupportedOperationException("Should not be called!");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offer(T v1, T v2) {
/*  83 */     throw new UnsupportedOperationException("Should not be called!");
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T poll() {
/*  89 */     if (get() == 0) {
/*  90 */       lazySet(1);
/*  91 */       return this.value;
/*     */     } 
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  98 */     return (get() != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 103 */     lazySet(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int requestFusion(int mode) {
/* 108 */     return mode & 0x1;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscriptions/ScalarSubscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */