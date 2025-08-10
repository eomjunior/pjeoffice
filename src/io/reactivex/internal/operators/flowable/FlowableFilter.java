/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.functions.Predicate;
/*     */ import io.reactivex.internal.fuseable.ConditionalSubscriber;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
/*     */ import io.reactivex.internal.subscribers.BasicFuseableSubscriber;
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
/*     */ public final class FlowableFilter<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Predicate<? super T> predicate;
/*     */   
/*     */   public FlowableFilter(Flowable<T> source, Predicate<? super T> predicate) {
/*  27 */     super(source);
/*  28 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  33 */     if (s instanceof ConditionalSubscriber) {
/*  34 */       this.source.subscribe((FlowableSubscriber)new FilterConditionalSubscriber<T>((ConditionalSubscriber<? super T>)s, this.predicate));
/*     */     } else {
/*     */       
/*  37 */       this.source.subscribe((FlowableSubscriber)new FilterSubscriber<T>(s, this.predicate));
/*     */     } 
/*     */   }
/*     */   
/*     */   static final class FilterSubscriber<T>
/*     */     extends BasicFuseableSubscriber<T, T> implements ConditionalSubscriber<T> {
/*     */     final Predicate<? super T> filter;
/*     */     
/*     */     FilterSubscriber(Subscriber<? super T> actual, Predicate<? super T> filter) {
/*  46 */       super(actual);
/*  47 */       this.filter = filter;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  52 */       if (!tryOnNext(t)) {
/*  53 */         this.upstream.request(1L);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean tryOnNext(T t) {
/*     */       boolean b;
/*  59 */       if (this.done) {
/*  60 */         return false;
/*     */       }
/*  62 */       if (this.sourceMode != 0) {
/*  63 */         this.downstream.onNext(null);
/*  64 */         return true;
/*     */       } 
/*     */       
/*     */       try {
/*  68 */         b = this.filter.test(t);
/*  69 */       } catch (Throwable e) {
/*  70 */         fail(e);
/*  71 */         return true;
/*     */       } 
/*  73 */       if (b) {
/*  74 */         this.downstream.onNext(t);
/*     */       }
/*  76 */       return b;
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/*  81 */       return transitiveBoundaryFusion(mode);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/*  87 */       QueueSubscription<T> qs = this.qs;
/*  88 */       Predicate<? super T> f = this.filter;
/*     */       
/*     */       while (true) {
/*  91 */         T t = (T)qs.poll();
/*  92 */         if (t == null) {
/*  93 */           return null;
/*     */         }
/*     */         
/*  96 */         if (f.test(t)) {
/*  97 */           return t;
/*     */         }
/*     */         
/* 100 */         if (this.sourceMode == 2)
/* 101 */           qs.request(1L); 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class FilterConditionalSubscriber<T>
/*     */     extends BasicFuseableConditionalSubscriber<T, T> {
/*     */     final Predicate<? super T> filter;
/*     */     
/*     */     FilterConditionalSubscriber(ConditionalSubscriber<? super T> actual, Predicate<? super T> filter) {
/* 111 */       super(actual);
/* 112 */       this.filter = filter;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 117 */       if (!tryOnNext(t)) {
/* 118 */         this.upstream.request(1L);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean tryOnNext(T t) {
/*     */       boolean b;
/* 124 */       if (this.done) {
/* 125 */         return false;
/*     */       }
/*     */       
/* 128 */       if (this.sourceMode != 0) {
/* 129 */         return this.downstream.tryOnNext(null);
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 134 */         b = this.filter.test(t);
/* 135 */       } catch (Throwable e) {
/* 136 */         fail(e);
/* 137 */         return true;
/*     */       } 
/* 139 */       return (b && this.downstream.tryOnNext(t));
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 144 */       return transitiveBoundaryFusion(mode);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/* 150 */       QueueSubscription<T> qs = this.qs;
/* 151 */       Predicate<? super T> f = this.filter;
/*     */       
/*     */       while (true) {
/* 154 */         T t = (T)qs.poll();
/* 155 */         if (t == null) {
/* 156 */           return null;
/*     */         }
/*     */         
/* 159 */         if (f.test(t)) {
/* 160 */           return t;
/*     */         }
/*     */         
/* 163 */         if (this.sourceMode == 2)
/* 164 */           qs.request(1L); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */