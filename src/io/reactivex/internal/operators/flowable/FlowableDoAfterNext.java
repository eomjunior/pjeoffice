/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.functions.Consumer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowableDoAfterNext<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Consumer<? super T> onAfterNext;
/*     */   
/*     */   public FlowableDoAfterNext(Flowable<T> source, Consumer<? super T> onAfterNext) {
/*  35 */     super(source);
/*  36 */     this.onAfterNext = onAfterNext;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  41 */     if (s instanceof ConditionalSubscriber) {
/*  42 */       this.source.subscribe((FlowableSubscriber)new DoAfterConditionalSubscriber<T>((ConditionalSubscriber<? super T>)s, this.onAfterNext));
/*     */     } else {
/*  44 */       this.source.subscribe((FlowableSubscriber)new DoAfterSubscriber<T>(s, this.onAfterNext));
/*     */     } 
/*     */   }
/*     */   
/*     */   static final class DoAfterSubscriber<T>
/*     */     extends BasicFuseableSubscriber<T, T> {
/*     */     final Consumer<? super T> onAfterNext;
/*     */     
/*     */     DoAfterSubscriber(Subscriber<? super T> actual, Consumer<? super T> onAfterNext) {
/*  53 */       super(actual);
/*  54 */       this.onAfterNext = onAfterNext;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  59 */       if (this.done) {
/*     */         return;
/*     */       }
/*  62 */       this.downstream.onNext(t);
/*     */       
/*  64 */       if (this.sourceMode == 0) {
/*     */         try {
/*  66 */           this.onAfterNext.accept(t);
/*  67 */         } catch (Throwable ex) {
/*  68 */           fail(ex);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/*  75 */       return transitiveBoundaryFusion(mode);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/*  81 */       T v = (T)this.qs.poll();
/*  82 */       if (v != null) {
/*  83 */         this.onAfterNext.accept(v);
/*     */       }
/*  85 */       return v;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class DoAfterConditionalSubscriber<T>
/*     */     extends BasicFuseableConditionalSubscriber<T, T> {
/*     */     final Consumer<? super T> onAfterNext;
/*     */     
/*     */     DoAfterConditionalSubscriber(ConditionalSubscriber<? super T> actual, Consumer<? super T> onAfterNext) {
/*  94 */       super(actual);
/*  95 */       this.onAfterNext = onAfterNext;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 100 */       this.downstream.onNext(t);
/*     */       
/* 102 */       if (this.sourceMode == 0) {
/*     */         try {
/* 104 */           this.onAfterNext.accept(t);
/* 105 */         } catch (Throwable ex) {
/* 106 */           fail(ex);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnNext(T t) {
/* 113 */       boolean b = this.downstream.tryOnNext(t);
/*     */       try {
/* 115 */         this.onAfterNext.accept(t);
/* 116 */       } catch (Throwable ex) {
/* 117 */         fail(ex);
/*     */       } 
/* 119 */       return b;
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 124 */       return transitiveBoundaryFusion(mode);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/* 130 */       T v = (T)this.qs.poll();
/* 131 */       if (v != null) {
/* 132 */         this.onAfterNext.accept(v);
/*     */       }
/* 134 */       return v;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableDoAfterNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */