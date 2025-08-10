/*    */ package io.reactivex.observers;
/*    */ 
/*    */ import io.reactivex.MaybeObserver;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class DisposableMaybeObserver<T>
/*    */   implements MaybeObserver<T>, Disposable
/*    */ {
/* 65 */   final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
/*    */ 
/*    */   
/*    */   public final void onSubscribe(@NonNull Disposable d) {
/* 69 */     if (EndConsumerHelper.setOnce(this.upstream, d, getClass())) {
/* 70 */       onStart();
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
/* 82 */     return (this.upstream.get() == DisposableHelper.DISPOSED);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void dispose() {
/* 87 */     DisposableHelper.dispose(this.upstream);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/observers/DisposableMaybeObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */