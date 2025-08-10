/*    */ package io.reactivex.internal.disposables;
/*    */ 
/*    */ import io.reactivex.disposables.Disposable;
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
/*    */ public final class SequentialDisposable
/*    */   extends AtomicReference<Disposable>
/*    */   implements Disposable
/*    */ {
/*    */   private static final long serialVersionUID = -754898800686245608L;
/*    */   
/*    */   public SequentialDisposable() {}
/*    */   
/*    */   public SequentialDisposable(Disposable initial) {
/* 45 */     lazySet(initial);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean update(Disposable next) {
/* 56 */     return DisposableHelper.set(this, next);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean replace(Disposable next) {
/* 67 */     return DisposableHelper.replace(this, next);
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 72 */     DisposableHelper.dispose(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 77 */     return DisposableHelper.isDisposed(get());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/disposables/SequentialDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */