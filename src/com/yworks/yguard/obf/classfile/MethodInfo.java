/*    */ package com.yworks.yguard.obf.classfile;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.IOException;
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
/*    */ public class MethodInfo
/*    */   extends ClassItemInfo
/*    */ {
/*    */   public static MethodInfo create(DataInput din, ClassFile cf) throws IOException {
/* 37 */     if (din == null) throw new NullPointerException("No input stream was provided."); 
/* 38 */     MethodInfo mi = new MethodInfo(cf);
/* 39 */     mi.read(din);
/* 40 */     return mi;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected MethodInfo(ClassFile cf) {
/* 49 */     super(cf);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/MethodInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */