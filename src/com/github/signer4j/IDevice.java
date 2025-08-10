/*    */ package com.github.signer4j;
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
/*    */ public interface IDevice
/*    */   extends IGadget
/*    */ {
/*    */   Type getType();
/*    */   
/*    */   String getDriver();
/*    */   
/*    */   ISlot getSlot();
/*    */   
/*    */   ICertificates getCertificates();
/*    */   
/*    */   public enum Type
/*    */   {
/* 33 */     A1, A3, VIRTUAL;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IDevice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */