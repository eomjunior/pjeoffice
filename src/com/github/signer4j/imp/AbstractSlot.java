/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IDevice;
/*    */ import com.github.signer4j.ISlot;
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
/*    */ abstract class AbstractSlot
/*    */   implements ISlot
/*    */ {
/*    */   private final long number;
/*    */   protected DefaultDevice device;
/*    */   
/*    */   protected AbstractSlot(long number) {
/* 40 */     this.number = number;
/*    */   }
/*    */ 
/*    */   
/*    */   public final long getNumber() {
/* 45 */     return this.number;
/*    */   }
/*    */ 
/*    */   
/*    */   public final IDevice toDevice() {
/* 50 */     return this.device;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getSerial() {
/* 55 */     return getToken().getSerial();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     return getClass().getSimpleName() + " [description=" + getDescription() + ", manufacturerId=" + 
/* 61 */       getManufacturer() + ", hardwareVersion=" + getHardwareVersion() + ", firmewareVersion=" + 
/* 62 */       getFirmewareVersion() + ", number=" + 
/* 63 */       getNumber() + "]";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/AbstractSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */