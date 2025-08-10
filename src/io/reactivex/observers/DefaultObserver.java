/*    */ package io.reactivex.observers;
/*    */ 
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.annotations.NonNull;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.util.EndConsumerHelper;
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
/*    */ public abstract class DefaultObserver<T>
/*    */   implements Observer<T>
/*    */ {
/*    */   private Disposable upstream;
/*    */   
/*    */   public final void onSubscribe(@NonNull Disposable d) {
/* 70 */     if (EndConsumerHelper.validate(this.upstream, d, getClass())) {
/* 71 */       this.upstream = d;
/* 72 */       onStart();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void cancel() {
/* 80 */     Disposable upstream = this.upstream;
/* 81 */     this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 82 */     upstream.dispose();
/*    */   }
/*    */   
/*    */   protected void onStart() {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/observers/DefaultObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */