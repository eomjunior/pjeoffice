/*    */ package com.yworks.yguard.obf.classfile;
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
/*    */ public class RuntimeInvisibleTypeAnnotationsAttrInfo
/*    */   extends RuntimeVisibleTypeAnnotationsAttrInfo
/*    */ {
/*    */   public RuntimeInvisibleTypeAnnotationsAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 18 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */   
/*    */   protected String getAttrName() {
/* 22 */     return "RuntimeInvisibleTypeAnnotations";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/RuntimeInvisibleTypeAnnotationsAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */