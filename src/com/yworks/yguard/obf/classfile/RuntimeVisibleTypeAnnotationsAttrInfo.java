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
/*    */ public class RuntimeVisibleTypeAnnotationsAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private int u2AnnotationCount;
/*    */   private TypeAnnotationInfo[] annotations;
/*    */   
/*    */   public RuntimeVisibleTypeAnnotationsAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 20 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */   
/*    */   protected String getAttrName() {
/* 24 */     return "RuntimeVisibleTypeAnnotations";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeAnnotationInfo[] getAnnotations() {
/* 33 */     return this.annotations;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClassFile getOwner() {
/* 42 */     return this.owner;
/*    */   }
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 46 */     dout.writeShort(this.u2AnnotationCount);
/* 47 */     for (int i = 0; i < this.u2AnnotationCount; i++) {
/* 48 */       this.annotations[i].write(dout);
/*    */     }
/*    */   }
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 53 */     this.u2AnnotationCount = din.readUnsignedShort();
/* 54 */     this.annotations = new TypeAnnotationInfo[this.u2AnnotationCount];
/* 55 */     for (int i = 0; i < this.u2AnnotationCount; i++) {
/* 56 */       this.annotations[i] = TypeAnnotationInfo.create(din);
/*    */     }
/*    */   }
/*    */   
/*    */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/* 61 */     for (int i = 0; i < this.u2AnnotationCount; i++)
/* 62 */       this.annotations[i].markUtf8RefsInInfo(pool); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/RuntimeVisibleTypeAnnotationsAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */