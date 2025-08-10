/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.annotations.Nullable;
/*    */ import io.reactivex.functions.Predicate;
/*    */ import io.reactivex.internal.observers.BasicFuseableObserver;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ObservableFilter<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   final Predicate<? super T> predicate;
/*    */   
/*    */   public ObservableFilter(ObservableSource<T> source, Predicate<? super T> predicate) {
/* 24 */     super(source);
/* 25 */     this.predicate = predicate;
/*    */   }
/*    */ 
/*    */   
/*    */   public void subscribeActual(Observer<? super T> observer) {
/* 30 */     this.source.subscribe((Observer)new FilterObserver<T>(observer, this.predicate));
/*    */   }
/*    */   
/*    */   static final class FilterObserver<T> extends BasicFuseableObserver<T, T> {
/*    */     final Predicate<? super T> filter;
/*    */     
/*    */     FilterObserver(Observer<? super T> actual, Predicate<? super T> filter) {
/* 37 */       super(actual);
/* 38 */       this.filter = filter;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 43 */       if (this.sourceMode == 0) {
/*    */         boolean b;
/*    */         try {
/* 46 */           b = this.filter.test(t);
/* 47 */         } catch (Throwable e) {
/* 48 */           fail(e);
/*    */           return;
/*    */         } 
/* 51 */         if (b) {
/* 52 */           this.downstream.onNext(t);
/*    */         }
/*    */       } else {
/* 55 */         this.downstream.onNext(null);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public int requestFusion(int mode) {
/* 61 */       return transitiveBoundaryFusion(mode);
/*    */     }
/*    */     
/*    */     @Nullable
/*    */     public T poll() throws Exception {
/*    */       T v;
/*    */       do {
/* 68 */         v = (T)this.qd.poll();
/* 69 */       } while (v != null && !this.filter.test(v));
/* 70 */       return v;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */