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
/*    */ 
/*    */ public class NameAndTypeCpInfo
/*    */   extends CpInfo
/*    */   implements Cloneable
/*    */ {
/*    */   private int u2nameIndex;
/*    */   private int u2descriptorIndex;
/*    */   
/*    */   protected NameAndTypeCpInfo() {
/* 36 */     super(12);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object clone() {
/* 43 */     NameAndTypeCpInfo cloneInfo = new NameAndTypeCpInfo();
/* 44 */     cloneInfo.u2nameIndex = this.u2nameIndex;
/* 45 */     cloneInfo.u2descriptorIndex = this.u2descriptorIndex;
/* 46 */     cloneInfo.resetRefCount();
/* 47 */     return cloneInfo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getNameIndex() {
/* 55 */     return this.u2nameIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setNameIndex(int index) {
/* 62 */     this.u2nameIndex = index;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getDescriptorIndex() {
/* 69 */     return this.u2descriptorIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setDescriptorIndex(int index) {
/* 76 */     this.u2descriptorIndex = index;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void markUtf8Refs(ConstantPool pool) {
/* 81 */     pool.incRefCount(this.u2nameIndex);
/* 82 */     pool.incRefCount(this.u2descriptorIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 88 */     this.u2nameIndex = din.readUnsignedShort();
/* 89 */     this.u2descriptorIndex = din.readUnsignedShort();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void writeInfo(DataOutput dout) throws IOException {
/* 95 */     dout.writeShort(this.u2nameIndex);
/* 96 */     dout.writeShort(this.u2descriptorIndex);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/NameAndTypeCpInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */