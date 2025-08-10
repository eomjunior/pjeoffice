/*    */ package com.yworks.yguard.obf.classfile;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
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
/*    */ public class AnnotationDefaultAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   protected ElementValueInfo elementValue;
/*    */   
/*    */   public AnnotationDefaultAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 24 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 29 */     return "AnnotationDefault";
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 34 */     this.elementValue.write(dout);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 41 */     this.elementValue = ElementValueInfo.create(din);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/* 46 */     this.elementValue.markUtf8RefsInInfo(pool);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/AnnotationDefaultAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */