/*    */ package com.yworks.yshrink.model;
/*    */ 
/*    */ import java.io.File;
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
/*    */ public class FieldDescriptor
/*    */   extends AbstractDescriptor
/*    */ {
/*    */   private String desc;
/*    */   private String name;
/*    */   
/*    */   protected FieldDescriptor(String desc, String name, int access, File sourceJar) {
/* 24 */     super(access, sourceJar);
/* 25 */     this.desc = desc;
/* 26 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDesc() {
/* 35 */     return this.desc;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 44 */     return this.name;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 48 */     return "FieldDescriptor{name='" + this.name + '\'' + ", type='" + this.desc + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/model/FieldDescriptor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */