/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.ConditionalSubscriber;
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
/*     */ 
/*     */ public final class FlowableMap<T, U>
/*     */   extends AbstractFlowableWithUpstream<T, U>
/*     */ {
/*     */   final Function<? super T, ? extends U> mapper;
/*     */   
/*     */   public FlowableMap(Flowable<T> source, Function<? super T, ? extends U> mapper) {
/*  28 */     super(source);
/*  29 */     this.mapper = mapper;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super U> s) {
/*  34 */     if (s instanceof ConditionalSubscriber) {
/*  35 */       this.source.subscribe((FlowableSubscriber)new MapConditionalSubscriber<T, U>((ConditionalSubscriber<? super U>)s, this.mapper));
/*     */     } else {
/*  37 */       this.source.subscribe((FlowableSubscriber)new MapSubscriber<T, U>(s, this.mapper));
/*     */     } 
/*     */   }
/*     */   
/*     */   static final class MapSubscriber<T, U> extends BasicFuseableSubscriber<T, U> {
/*     */     final Function<? super T, ? extends U> mapper;
/*     */     
/*     */     MapSubscriber(Subscriber<? super U> actual, Function<? super T, ? extends U> mapper) {
/*  45 */       super(actual);
/*  46 */       this.mapper = mapper;
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       U v;
/*  51 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*  55 */       if (this.sourceMode != 0) {
/*  56 */         this.downstream.onNext(null);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/*  63 */         v = (U)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper function returned a null value.");
/*  64 */       } catch (Throwable ex) {
/*  65 */         fail(ex);
/*     */         return;
/*     */       } 
/*  68 */       this.downstream.onNext(v);
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/*  73 */       return transitiveBoundaryFusion(mode);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public U poll() throws Exception {
/*  79 */       T t = (T)this.qs.poll();
/*  80 */       return (t != null) ? (U)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper function returned a null value.") : null;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class MapConditionalSubscriber<T, U> extends BasicFuseableConditionalSubscriber<T, U> {
/*     */     final Function<? super T, ? extends U> mapper;
/*     */     
/*     */     MapConditionalSubscriber(ConditionalSubscriber<? super U> actual, Function<? super T, ? extends U> function) {
/*  88 */       super(actual);
/*  89 */       this.mapper = function;
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       U v;
/*  94 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*  98 */       if (this.sourceMode != 0) {
/*  99 */         this.downstream.onNext(null);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 106 */         v = (U)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper function returned a null value.");
/* 107 */       } catch (Throwable ex) {
/* 108 */         fail(ex);
/*     */         return;
/*     */       } 
/* 111 */       this.downstream.onNext(v);
/*     */     }
/*     */     
/*     */     public boolean tryOnNext(T t) {
/*     */       U v;
/* 116 */       if (this.done) {
/* 117 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 123 */         v = (U)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper function returned a null value.");
/* 124 */       } catch (Throwable ex) {
/* 125 */         fail(ex);
/* 126 */         return true;
/*     */       } 
/* 128 */       return this.downstream.tryOnNext(v);
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 133 */       return transitiveBoundaryFusion(mode);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public U poll() throws Exception {
/* 139 */       T t = (T)this.qs.poll();
/* 140 */       return (t != null) ? (U)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper function returned a null value.") : null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */