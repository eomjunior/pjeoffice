/*    */ package io.reactivex.internal.util;
/*    */ 
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.functions.Consumer;
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
/*    */ public final class ConnectConsumer
/*    */   implements Consumer<Disposable>
/*    */ {
/*    */   public Disposable disposable;
/*    */   
/*    */   public void accept(Disposable t) throws Exception {
/* 27 */     this.disposable = t;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/ConnectConsumer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */