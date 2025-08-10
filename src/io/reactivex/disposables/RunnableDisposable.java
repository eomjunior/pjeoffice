/*    */ package io.reactivex.disposables;
/*    */ 
/*    */ import io.reactivex.annotations.NonNull;
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
/*    */ final class RunnableDisposable
/*    */   extends ReferenceDisposable<Runnable>
/*    */ {
/*    */   private static final long serialVersionUID = -8219729196779211169L;
/*    */   
/*    */   RunnableDisposable(Runnable value) {
/* 25 */     super(value);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onDisposed(@NonNull Runnable value) {
/* 30 */     value.run();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 35 */     return "RunnableDisposable(disposed=" + isDisposed() + ", " + get() + ")";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/disposables/RunnableDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */