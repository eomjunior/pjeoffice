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
/*    */ public class RuntimeVisibleParameterAnnotationsAttrInfo
/*    */   extends AttrInfo
/*    */ {
/*    */   private int u1parameterCount;
/*    */   private ParameterAnnotationInfo[] annotations;
/*    */   
/*    */   public RuntimeVisibleParameterAnnotationsAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/* 22 */     super(cf, attrNameIndex, attrLength);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ParameterAnnotationInfo[] getParameterAnnotations() {
/* 31 */     return this.annotations;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getAttrName() {
/* 36 */     return "RuntimeVisibleParameterAnnotations";
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeInfo(DataOutput dout) throws IOException {
/* 41 */     dout.writeByte(this.u1parameterCount);
/* 42 */     for (int i = 0; i < this.u1parameterCount; i++) {
/* 43 */       this.annotations[i].write(dout);
/*    */     }
/*    */   }
/*    */   
/*    */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/* 48 */     for (int i = 0; i < this.u1parameterCount; i++) {
/* 49 */       this.annotations[i].markUtf8RefsInInfo(pool);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 56 */     this.u1parameterCount = din.readUnsignedByte();
/* 57 */     this.annotations = new ParameterAnnotationInfo[this.u1parameterCount];
/* 58 */     for (int i = 0; i < this.u1parameterCount; i++)
/* 59 */       this.annotations[i] = ParameterAnnotationInfo.create(din); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/RuntimeVisibleParameterAnnotationsAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */