/*    */ package io.reactivex.internal.disposables;
/*    */ 
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Cancellable;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
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
/*    */ public final class CancellableDisposable
/*    */   extends AtomicReference<Cancellable>
/*    */   implements Disposable
/*    */ {
/*    */   private static final long serialVersionUID = 5718521705281392066L;
/*    */   
/*    */   public CancellableDisposable(Cancellable cancellable) {
/* 34 */     super(cancellable);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 39 */     return (get() == null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 44 */     if (get() != null) {
/* 45 */       Cancellable c = getAndSet(null);
/* 46 */       if (c != null)
/*    */         try {
/* 48 */           c.cancel();
/* 49 */         } catch (Exception ex) {
/* 50 */           Exceptions.throwIfFatal(ex);
/* 51 */           RxJavaPlugins.onError(ex);
/*    */         }  
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/disposables/CancellableDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */