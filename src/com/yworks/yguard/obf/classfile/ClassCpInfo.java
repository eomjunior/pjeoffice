/*    */ package com.yworks.yguard.obf.classfile;
/*    */ 
/*    */ import java.io.PrintWriter;
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
/*    */ public class ClassCpInfo
/*    */   extends AbstractTypeCpInfo
/*    */ {
/*    */   protected ClassCpInfo() {
/* 33 */     super(7);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void dump(PrintWriter pw, ClassFile cf) {
/* 45 */     pw.println("  Class: " + ((Utf8CpInfo)cf.getCpEntry(this.u2nameIndex)).getString());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ClassCpInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */