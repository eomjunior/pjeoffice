/*    */ package io.reactivex.disposables;
/*    */ 
/*    */ import io.reactivex.annotations.NonNull;
/*    */ import io.reactivex.functions.Action;
/*    */ import io.reactivex.internal.util.ExceptionHelper;
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
/*    */ final class ActionDisposable
/*    */   extends ReferenceDisposable<Action>
/*    */ {
/*    */   private static final long serialVersionUID = -8219729196779211169L;
/*    */   
/*    */   ActionDisposable(Action value) {
/* 27 */     super(value);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onDisposed(@NonNull Action value) {
/*    */     try {
/* 33 */       value.run();
/* 34 */     } catch (Throwable ex) {
/* 35 */       throw ExceptionHelper.wrapOrThrow(ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/disposables/ActionDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */