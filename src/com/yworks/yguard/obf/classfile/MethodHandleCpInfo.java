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
/*    */ public class MethodHandleCpInfo
/*    */   extends CpInfo
/*    */ {
/*    */   private int u1referenceKind;
/*    */   private int u2referenceIndex;
/*    */   
/*    */   protected MethodHandleCpInfo() {
/* 28 */     super(15);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 33 */     this.u1referenceKind = din.readUnsignedByte();
/* 34 */     this.u2referenceIndex = din.readUnsignedShort();
/*    */   }
/*    */   
/*    */   protected void writeInfo(DataOutput dout) throws IOException {
/* 38 */     dout.writeByte(this.u1referenceKind);
/* 39 */     dout.writeShort(this.u2referenceIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getReferenceKind() {
/* 48 */     return this.u1referenceKind;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getReferenceIndex() {
/* 57 */     return this.u2referenceIndex;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/MethodHandleCpInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */