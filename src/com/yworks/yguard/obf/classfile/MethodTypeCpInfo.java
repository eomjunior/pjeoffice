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
/*    */ public class MethodTypeCpInfo
/*    */   extends CpInfo
/*    */ {
/*    */   private int u2descriptorIndex;
/*    */   
/*    */   protected MethodTypeCpInfo() {
/* 27 */     super(16);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 32 */     this.u2descriptorIndex = din.readUnsignedShort();
/*    */   }
/*    */   
/*    */   protected void writeInfo(DataOutput dout) throws IOException {
/* 36 */     dout.writeShort(this.u2descriptorIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getU2descriptorIndex() {
/* 45 */     return this.u2descriptorIndex;
/*    */   }
/*    */   
/*    */   protected void markUtf8Refs(ConstantPool pool) {
/* 49 */     pool.incRefCount(this.u2descriptorIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setU2descriptorIndex(int u2descriptorIndex) {
/* 58 */     this.u2descriptorIndex = u2descriptorIndex;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/MethodTypeCpInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */