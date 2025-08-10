/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IToken;
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
/*    */ public abstract class MSCAPIPTokenAccessor<T extends IToken>
/*    */   extends TokenAccessor<T>
/*    */ {
/*    */   protected MSCAPIPTokenAccessor(AuthStrategy strategy) {
/* 34 */     super(strategy, new MSCAPIDeviceManager());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/MSCAPIPTokenAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */