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
/*    */ public class AnnotationInfo
/*    */ {
/*    */   protected int u2typeIndex;
/*    */   private ElementValuePairInfo[] elementValuePairs;
/*    */   
/*    */   public static AnnotationInfo create(DataInput din) throws IOException {
/* 33 */     AnnotationInfo an = new AnnotationInfo();
/* 34 */     an.read(din);
/* 35 */     return an;
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
/*    */   public ElementValuePairInfo[] getElementValuePairs() {
/* 48 */     return this.elementValuePairs;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/* 57 */     pool.getCpEntry(this.u2typeIndex).incRefCount();
/* 58 */     int u2elementCount = this.elementValuePairs.length;
/* 59 */     for (int i = 0; i < u2elementCount; i++) {
/* 60 */       this.elementValuePairs[i].markUtf8RefsInInfo(pool);
/*    */     }
/*    */   }
/*    */   
/*    */   void read(DataInput din) throws IOException {
/* 65 */     this.u2typeIndex = din.readUnsignedShort();
/* 66 */     int u2elementCount = din.readUnsignedShort();
/* 67 */     this.elementValuePairs = new ElementValuePairInfo[u2elementCount];
/* 68 */     for (int i = 0; i < u2elementCount; i++) {
/* 69 */       this.elementValuePairs[i] = ElementValuePairInfo.create(din);
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
/* 81 */     dout.writeShort(this.u2typeIndex);
/* 82 */     int u2elementCount = this.elementValuePairs.length;
/* 83 */     dout.writeShort(u2elementCount);
/* 84 */     for (int i = 0; i < u2elementCount; i++)
/* 85 */       this.elementValuePairs[i].write(dout); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/AnnotationInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */