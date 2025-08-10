/*    */ package com.yworks.yshrink.model;
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
/*    */ public class Invocation
/*    */ {
/*    */   private int opcode;
/*    */   private String type;
/*    */   private String name;
/*    */   private String desc;
/*    */   
/*    */   public Invocation(int opcode, String type, String name, String desc) {
/* 24 */     this.opcode = opcode;
/* 25 */     this.type = type;
/* 26 */     this.name = name;
/* 27 */     this.desc = desc;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOpcode() {
/* 36 */     return this.opcode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getType() {
/* 45 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 54 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDesc() {
/* 63 */     return this.desc;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/model/Invocation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */