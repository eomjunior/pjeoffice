/*    */ package io.reactivex.internal.observers;
/*    */ 
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
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
/*    */ public final class ResumeSingleObserver<T>
/*    */   implements SingleObserver<T>
/*    */ {
/*    */   final AtomicReference<Disposable> parent;
/*    */   final SingleObserver<? super T> downstream;
/*    */   
/*    */   public ResumeSingleObserver(AtomicReference<Disposable> parent, SingleObserver<? super T> downstream) {
/* 35 */     this.parent = parent;
/* 36 */     this.downstream = downstream;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSubscribe(Disposable d) {
/* 41 */     DisposableHelper.replace(this.parent, d);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSuccess(T value) {
/* 46 */     this.downstream.onSuccess(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable e) {
/* 51 */     this.downstream.onError(e);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/ResumeSingleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */