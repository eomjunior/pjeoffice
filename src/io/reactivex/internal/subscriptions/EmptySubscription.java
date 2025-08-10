/*     */ package io.reactivex.internal.subscriptions;
/*     */ 
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
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
/*     */ public enum EmptySubscription
/*     */   implements QueueSubscription<Object>
/*     */ {
/*  26 */   INSTANCE;
/*     */ 
/*     */   
/*     */   public void request(long n) {
/*  30 */     SubscriptionHelper.validate(n);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancel() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  40 */     return "EmptySubscription";
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
/*     */   public static void error(Throwable e, Subscriber<?> s) {
/*  54 */     s.onSubscribe((Subscription)INSTANCE);
/*  55 */     s.onError(e);
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
/*     */   public static void complete(Subscriber<?> s) {
/*  68 */     s.onSubscribe((Subscription)INSTANCE);
/*  69 */     s.onComplete();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object poll() {
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  80 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int requestFusion(int mode) {
/*  90 */     return mode & 0x2;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offer(Object value) {
/*  95 */     throw new UnsupportedOperationException("Should not be called!");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offer(Object v1, Object v2) {
/* 100 */     throw new UnsupportedOperationException("Should not be called!");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscriptions/EmptySubscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */