/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public final class FlowableElementAt<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final long index;
/*     */   final T defaultValue;
/*     */   final boolean errorOnFewer;
/*     */   
/*     */   public FlowableElementAt(Flowable<T> source, long index, T defaultValue, boolean errorOnFewer) {
/*  30 */     super(source);
/*  31 */     this.index = index;
/*  32 */     this.defaultValue = defaultValue;
/*  33 */     this.errorOnFewer = errorOnFewer;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  38 */     this.source.subscribe(new ElementAtSubscriber<T>(s, this.index, this.defaultValue, this.errorOnFewer));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ElementAtSubscriber<T>
/*     */     extends DeferredScalarSubscription<T>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = 4066607327284737757L;
/*     */     
/*     */     final long index;
/*     */     final T defaultValue;
/*     */     final boolean errorOnFewer;
/*     */     Subscription upstream;
/*     */     long count;
/*     */     boolean done;
/*     */     
/*     */     ElementAtSubscriber(Subscriber<? super T> actual, long index, T defaultValue, boolean errorOnFewer) {
/*  56 */       super(actual);
/*  57 */       this.index = index;
/*  58 */       this.defaultValue = defaultValue;
/*  59 */       this.errorOnFewer = errorOnFewer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  64 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  65 */         this.upstream = s;
/*  66 */         this.downstream.onSubscribe((Subscription)this);
/*  67 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  73 */       if (this.done) {
/*     */         return;
/*     */       }
/*  76 */       long c = this.count;
/*  77 */       if (c == this.index) {
/*  78 */         this.done = true;
/*  79 */         this.upstream.cancel();
/*  80 */         complete(t);
/*     */         return;
/*     */       } 
/*  83 */       this.count = c + 1L;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  88 */       if (this.done) {
/*  89 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  92 */       this.done = true;
/*  93 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  98 */       if (!this.done) {
/*  99 */         this.done = true;
/* 100 */         T v = this.defaultValue;
/* 101 */         if (v == null) {
/* 102 */           if (this.errorOnFewer) {
/* 103 */             this.downstream.onError(new NoSuchElementException());
/*     */           } else {
/* 105 */             this.downstream.onComplete();
/*     */           } 
/*     */         } else {
/* 108 */           complete(v);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 115 */       super.cancel();
/* 116 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableElementAt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */