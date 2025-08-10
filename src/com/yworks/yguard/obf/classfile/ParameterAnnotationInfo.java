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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParameterAnnotationInfo
/*    */ {
/*    */   private int u2annotationCount;
/*    */   private AnnotationInfo[] annotations;
/*    */   
/*    */   public static ParameterAnnotationInfo create(DataInput din) throws IOException {
/* 33 */     if (din == null) throw new NullPointerException("DataInput cannot be null!"); 
/* 34 */     ParameterAnnotationInfo an = new ParameterAnnotationInfo();
/* 35 */     an.read(din);
/* 36 */     return an;
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
/*    */   
/*    */   protected AnnotationInfo[] getAnnotations() {
/* 49 */     return this.annotations;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/* 58 */     for (int i = 0; i < this.u2annotationCount; i++) {
/* 59 */       this.annotations[i].markUtf8RefsInInfo(pool);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void read(DataInput din) throws IOException {
/* 66 */     this.u2annotationCount = din.readUnsignedShort();
/* 67 */     this.annotations = new AnnotationInfo[this.u2annotationCount];
/* 68 */     for (int i = 0; i < this.u2annotationCount; i++)
/*    */     {
/* 70 */       this.annotations[i] = AnnotationInfo.create(din);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(DataOutput dout) throws IOException {
/* 82 */     dout.writeShort(this.u2annotationCount);
/* 83 */     for (int i = 0; i < this.u2annotationCount; i++)
/*    */     {
/* 85 */       this.annotations[i].write(dout);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ParameterAnnotationInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */