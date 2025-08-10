/*    */ package io.reactivex.internal.operators.single;
/*    */ 
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.SingleSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.observers.ResumeSingleObserver;
/*    */ import java.util.concurrent.atomic.AtomicReference;
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
/*    */ public final class SingleDelayWithCompletable<T>
/*    */   extends Single<T>
/*    */ {
/*    */   final SingleSource<T> source;
/*    */   final CompletableSource other;
/*    */   
/*    */   public SingleDelayWithCompletable(SingleSource<T> source, CompletableSource other) {
/* 30 */     this.source = source;
/* 31 */     this.other = other;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super T> observer) {
/* 36 */     this.other.subscribe(new OtherObserver<T>(observer, this.source));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class OtherObserver<T>
/*    */     extends AtomicReference<Disposable>
/*    */     implements CompletableObserver, Disposable
/*    */   {
/*    */     private static final long serialVersionUID = -8565274649390031272L;
/*    */     
/*    */     final SingleObserver<? super T> downstream;
/*    */     final SingleSource<T> source;
/*    */     
/*    */     OtherObserver(SingleObserver<? super T> actual, SingleSource<T> source) {
/* 50 */       this.downstream = actual;
/* 51 */       this.source = source;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 56 */       if (DisposableHelper.setOnce(this, d))
/*    */       {
/* 58 */         this.downstream.onSubscribe(this);
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable e) {
/* 64 */       this.downstream.onError(e);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 69 */       this.source.subscribe((SingleObserver)new ResumeSingleObserver(this, this.downstream));
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 74 */       DisposableHelper.dispose(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 79 */       return DisposableHelper.isDisposed(get());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleDelayWithCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */