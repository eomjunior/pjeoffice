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
/*    */ public class FieldInfo
/*    */   extends ClassItemInfo
/*    */ {
/*    */   public static FieldInfo create(DataInput din, ClassFile cf) throws IOException {
/* 37 */     if (din == null) throw new NullPointerException("No input stream was provided."); 
/* 38 */     FieldInfo fi = new FieldInfo(cf);
/* 39 */     fi.read(din);
/* 40 */     return fi;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected FieldInfo(ClassFile cf) {
/* 50 */     super(cf);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/FieldInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */