/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.functions.BiPredicate;
/*     */ import io.reactivex.functions.Function;
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
/*     */ public final class FlowableDistinctUntilChanged<T, K>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Function<? super T, K> keySelector;
/*     */   final BiPredicate<? super K, ? super K> comparer;
/*     */   
/*     */   public FlowableDistinctUntilChanged(Flowable<T> source, Function<? super T, K> keySelector, BiPredicate<? super K, ? super K> comparer) {
/*  31 */     super(source);
/*  32 */     this.keySelector = keySelector;
/*  33 */     this.comparer = comparer;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  38 */     if (s instanceof ConditionalSubscriber) {
/*  39 */       ConditionalSubscriber<? super T> cs = (ConditionalSubscriber<? super T>)s;
/*  40 */       this.source.subscribe((FlowableSubscriber)new DistinctUntilChangedConditionalSubscriber<T, K>(cs, this.keySelector, this.comparer));
/*     */     } else {
/*  42 */       this.source.subscribe((FlowableSubscriber)new DistinctUntilChangedSubscriber<T, K>(s, this.keySelector, this.comparer));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DistinctUntilChangedSubscriber<T, K>
/*     */     extends BasicFuseableSubscriber<T, T>
/*     */     implements ConditionalSubscriber<T>
/*     */   {
/*     */     final Function<? super T, K> keySelector;
/*     */     
/*     */     final BiPredicate<? super K, ? super K> comparer;
/*     */     
/*     */     K last;
/*     */     
/*     */     boolean hasValue;
/*     */     
/*     */     DistinctUntilChangedSubscriber(Subscriber<? super T> actual, Function<? super T, K> keySelector, BiPredicate<? super K, ? super K> comparer) {
/*  60 */       super(actual);
/*  61 */       this.keySelector = keySelector;
/*  62 */       this.comparer = comparer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  67 */       if (!tryOnNext(t)) {
/*  68 */         this.upstream.request(1L);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnNext(T t) {
/*  74 */       if (this.done) {
/*  75 */         return false;
/*     */       }
/*  77 */       if (this.sourceMode != 0) {
/*  78 */         this.downstream.onNext(t);
/*  79 */         return true;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  85 */         K key = (K)this.keySelector.apply(t);
/*  86 */         if (this.hasValue) {
/*  87 */           boolean equal = this.comparer.test(this.last, key);
/*  88 */           this.last = key;
/*  89 */           if (equal) {
/*  90 */             return false;
/*     */           }
/*     */         } else {
/*  93 */           this.hasValue = true;
/*  94 */           this.last = key;
/*     */         } 
/*  96 */       } catch (Throwable ex) {
/*  97 */         fail(ex);
/*  98 */         return true;
/*     */       } 
/*     */       
/* 101 */       this.downstream.onNext(t);
/* 102 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 107 */       return transitiveBoundaryFusion(mode);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/*     */       while (true) {
/* 114 */         T v = (T)this.qs.poll();
/* 115 */         if (v == null) {
/* 116 */           return null;
/*     */         }
/* 118 */         K key = (K)this.keySelector.apply(v);
/* 119 */         if (!this.hasValue) {
/* 120 */           this.hasValue = true;
/* 121 */           this.last = key;
/* 122 */           return v;
/*     */         } 
/*     */         
/* 125 */         if (!this.comparer.test(this.last, key)) {
/* 126 */           this.last = key;
/* 127 */           return v;
/*     */         } 
/* 129 */         this.last = key;
/* 130 */         if (this.sourceMode != 1) {
/* 131 */           this.upstream.request(1L);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DistinctUntilChangedConditionalSubscriber<T, K>
/*     */     extends BasicFuseableConditionalSubscriber<T, T>
/*     */   {
/*     */     final Function<? super T, K> keySelector;
/*     */     
/*     */     final BiPredicate<? super K, ? super K> comparer;
/*     */     
/*     */     K last;
/*     */     
/*     */     boolean hasValue;
/*     */ 
/*     */     
/*     */     DistinctUntilChangedConditionalSubscriber(ConditionalSubscriber<? super T> actual, Function<? super T, K> keySelector, BiPredicate<? super K, ? super K> comparer) {
/* 151 */       super(actual);
/* 152 */       this.keySelector = keySelector;
/* 153 */       this.comparer = comparer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 158 */       if (!tryOnNext(t)) {
/* 159 */         this.upstream.request(1L);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnNext(T t) {
/* 165 */       if (this.done) {
/* 166 */         return false;
/*     */       }
/* 168 */       if (this.sourceMode != 0) {
/* 169 */         return this.downstream.tryOnNext(t);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 175 */         K key = (K)this.keySelector.apply(t);
/* 176 */         if (this.hasValue) {
/* 177 */           boolean equal = this.comparer.test(this.last, key);
/* 178 */           this.last = key;
/* 179 */           if (equal) {
/* 180 */             return false;
/*     */           }
/*     */         } else {
/* 183 */           this.hasValue = true;
/* 184 */           this.last = key;
/*     */         } 
/* 186 */       } catch (Throwable ex) {
/* 187 */         fail(ex);
/* 188 */         return true;
/*     */       } 
/*     */       
/* 191 */       this.downstream.onNext(t);
/* 192 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 197 */       return transitiveBoundaryFusion(mode);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/*     */       while (true) {
/* 204 */         T v = (T)this.qs.poll();
/* 205 */         if (v == null) {
/* 206 */           return null;
/*     */         }
/* 208 */         K key = (K)this.keySelector.apply(v);
/* 209 */         if (!this.hasValue) {
/* 210 */           this.hasValue = true;
/* 211 */           this.last = key;
/* 212 */           return v;
/*     */         } 
/*     */         
/* 215 */         if (!this.comparer.test(this.last, key)) {
/* 216 */           this.last = key;
/* 217 */           return v;
/*     */         } 
/* 219 */         this.last = key;
/* 220 */         if (this.sourceMode != 1)
/* 221 */           this.upstream.request(1L); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableDistinctUntilChanged.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */