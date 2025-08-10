/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.CompositeDisposable;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
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
/*    */ 
/*    */ public final class SingleEquals<T>
/*    */   extends Single<Boolean>
/*    */ {
/*    */   final SingleSource<? extends T> first;
/*    */   final SingleSource<? extends T> second;
/*    */   
/*    */   public SingleEquals(SingleSource<? extends T> first, SingleSource<? extends T> second) {
/* 29 */     this.first = first;
/* 30 */     this.second = second;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super Boolean> observer) {
/* 36 */     AtomicInteger count = new AtomicInteger();
/* 37 */     Object[] values = { null, null };
/*    */     
/* 39 */     CompositeDisposable set = new CompositeDisposable();
/* 40 */     observer.onSubscribe((Disposable)set);
/*    */     
/* 42 */     this.first.subscribe(new InnerObserver(0, set, values, observer, count));
/* 43 */     this.second.subscribe(new InnerObserver(1, set, values, observer, count));
/*    */   }
/*    */   
/*    */   static class InnerObserver<T> implements SingleObserver<T> {
/*    */     final int index;
/*    */     final CompositeDisposable set;
/*    */     final Object[] values;
/*    */     final SingleObserver<? super Boolean> downstream;
/*    */     final AtomicInteger count;
/*    */     
/*    */     InnerObserver(int index, CompositeDisposable set, Object[] values, SingleObserver<? super Boolean> observer, AtomicInteger count) {
/* 54 */       this.index = index;
/* 55 */       this.set = set;
/* 56 */       this.values = values;
/* 57 */       this.downstream = observer;
/* 58 */       this.count = count;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 63 */       this.set.add(d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSuccess(T value) {
/* 68 */       this.values[this.index] = value;
/*    */       
/* 70 */       if (this.count.incrementAndGet() == 2) {
/* 71 */         this.downstream.onSuccess(Boolean.valueOf(ObjectHelper.equals(this.values[0], this.values[1])));
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/*    */       while (true) {
/* 78 */         int state = this.count.get();
/* 79 */         if (state >= 2) {
/* 80 */           RxJavaPlugins.onError(e);
/*    */           return;
/*    */         } 
/* 83 */         if (this.count.compareAndSet(state, 2)) {
/* 84 */           this.set.dispose();
/* 85 */           this.downstream.onError(e);
/*    */           return;
/*    */         } 
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleEquals.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */