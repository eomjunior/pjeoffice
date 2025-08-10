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
/*    */ public class RuntimeVisibleAnnotationsAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private int u2AnnotationCount;
/*    */   private AnnotationInfo[] annotations;
/*    */   
/*    */   public RuntimeVisibleAnnotationsAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 22 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 27 */     return "RuntimeVisibleAnnotations";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AnnotationInfo[] getAnnotations() {
/* 36 */     return this.annotations;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClassFile getOwner() {
/* 45 */     return this.owner;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getU2TypeIndex(int annotationIndex) {
/* 55 */     return (this.annotations[annotationIndex]).u2typeIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 60 */     dout.writeShort(this.u2AnnotationCount);
/* 61 */     for (int i = 0; i < this.u2AnnotationCount; i++) {
/* 62 */       this.annotations[i].write(dout);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 68 */     this.u2AnnotationCount = din.readUnsignedShort();
/* 69 */     this.annotations = new AnnotationInfo[this.u2AnnotationCount];
/* 70 */     for (int i = 0; i < this.u2AnnotationCount; i++) {
/* 71 */       this.annotations[i] = AnnotationInfo.create(din);
/*    */     }
/*    */   }
/*    */   
/*    */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/* 76 */     for (int i = 0; i < this.u2AnnotationCount; i++)
/* 77 */       this.annotations[i].markUtf8RefsInInfo(pool); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/RuntimeVisibleAnnotationsAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */