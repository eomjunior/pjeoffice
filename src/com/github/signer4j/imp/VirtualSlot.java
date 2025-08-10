/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ abstract class VirtualSlot
/*    */   extends AbstractSlot
/*    */ {
/*    */   private String name;
/*    */   
/*    */   public VirtualSlot(long number, String name) {
/* 37 */     super(number);
/* 38 */     this.name = Args.requireText(name, "name is null").trim();
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getDescription() {
/* 43 */     return "Virtual " + this.name + " Slot";
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getManufacturer() {
/* 48 */     return "https://github.com/l3onardo-oliv3ira";
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getHardwareVersion() {
/* 53 */     return "Universal";
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getFirmewareVersion() {
/* 58 */     return "1.0";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/VirtualSlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */