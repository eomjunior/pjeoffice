/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.functions.BiPredicate;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.observers.BasicFuseableObserver;
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
/*     */ public final class ObservableDistinctUntilChanged<T, K>
/*     */   extends AbstractObservableWithUpstream<T, T>
/*     */ {
/*     */   final Function<? super T, K> keySelector;
/*     */   final BiPredicate<? super K, ? super K> comparer;
/*     */   
/*     */   public ObservableDistinctUntilChanged(ObservableSource<T> source, Function<? super T, K> keySelector, BiPredicate<? super K, ? super K> comparer) {
/*  28 */     super(source);
/*  29 */     this.keySelector = keySelector;
/*  30 */     this.comparer = comparer;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*  35 */     this.source.subscribe((Observer)new DistinctUntilChangedObserver<T, K>(observer, this.keySelector, this.comparer));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DistinctUntilChangedObserver<T, K>
/*     */     extends BasicFuseableObserver<T, T>
/*     */   {
/*     */     final Function<? super T, K> keySelector;
/*     */     
/*     */     final BiPredicate<? super K, ? super K> comparer;
/*     */     
/*     */     K last;
/*     */     
/*     */     boolean hasValue;
/*     */     
/*     */     DistinctUntilChangedObserver(Observer<? super T> actual, Function<? super T, K> keySelector, BiPredicate<? super K, ? super K> comparer) {
/*  51 */       super(actual);
/*  52 */       this.keySelector = keySelector;
/*  53 */       this.comparer = comparer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  58 */       if (this.done) {
/*     */         return;
/*     */       }
/*  61 */       if (this.sourceMode != 0) {
/*  62 */         this.downstream.onNext(t);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/*  69 */         K key = (K)this.keySelector.apply(t);
/*  70 */         if (this.hasValue) {
/*  71 */           boolean equal = this.comparer.test(this.last, key);
/*  72 */           this.last = key;
/*  73 */           if (equal) {
/*     */             return;
/*     */           }
/*     */         } else {
/*  77 */           this.hasValue = true;
/*  78 */           this.last = key;
/*     */         } 
/*  80 */       } catch (Throwable ex) {
/*  81 */         fail(ex);
/*     */         
/*     */         return;
/*     */       } 
/*  85 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/*  90 */       return transitiveBoundaryFusion(mode);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/*     */       while (true) {
/*  97 */         T v = (T)this.qd.poll();
/*  98 */         if (v == null) {
/*  99 */           return null;
/*     */         }
/* 101 */         K key = (K)this.keySelector.apply(v);
/* 102 */         if (!this.hasValue) {
/* 103 */           this.hasValue = true;
/* 104 */           this.last = key;
/* 105 */           return v;
/*     */         } 
/*     */         
/* 108 */         if (!this.comparer.test(this.last, key)) {
/* 109 */           this.last = key;
/* 110 */           return v;
/*     */         } 
/* 112 */         this.last = key;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableDistinctUntilChanged.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */