/*    */ package io.reactivex.disposables;
/*    */ 
/*    */ import java.util.concurrent.Future;
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
/*    */ final class FutureDisposable
/*    */   extends AtomicReference<Future<?>>
/*    */   implements Disposable
/*    */ {
/*    */   private static final long serialVersionUID = 6545242830671168775L;
/*    */   private final boolean allowInterrupt;
/*    */   
/*    */   FutureDisposable(Future<?> run, boolean allowInterrupt) {
/* 28 */     super(run);
/* 29 */     this.allowInterrupt = allowInterrupt;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDisposed() {
/* 34 */     Future<?> f = get();
/* 35 */     return (f == null || f.isDone());
/*    */   }
/*    */ 
/*    */   
/*    */   public void dispose() {
/* 40 */     Future<?> f = getAndSet(null);
/* 41 */     if (f != null)
/* 42 */       f.cancel(this.allowInterrupt); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/disposables/FutureDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */