/*    */ package io.reactivex.observers;
/*    */ 
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.annotations.NonNull;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.util.EndConsumerHelper;
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
/*    */ public abstract class DisposableCompletableObserver
/*    */   implements CompletableObserver, Disposable
/*    */ {
/* 56 */   final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
/*    */ 
/*    */   
/*    */   public final void onSubscribe(@NonNull Disposable d) {
/* 60 */     if (EndConsumerHelper.setOnce(this.upstream, d, getClass())) {
/* 61 */       onStart();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onStart() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean isDisposed() {
/* 73 */     return (this.upstream.get() == DisposableHelper.DISPOSED);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void dispose() {
/* 78 */     DisposableHelper.dispose(this.upstream);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/observers/DisposableCompletableObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */