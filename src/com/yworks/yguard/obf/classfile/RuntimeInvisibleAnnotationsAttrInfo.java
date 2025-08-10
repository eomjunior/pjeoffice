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
/*    */ 
/*    */ public class RuntimeInvisibleAnnotationsAttrInfo
/*    */   extends RuntimeVisibleAnnotationsAttrInfo
/*    */ {
/*    */   public RuntimeInvisibleAnnotationsAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 19 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 24 */     return "RuntimeInvisibleAnnotations";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/RuntimeInvisibleAnnotationsAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */