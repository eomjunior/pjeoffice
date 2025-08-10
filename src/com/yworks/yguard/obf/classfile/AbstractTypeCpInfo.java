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
/*    */ public abstract class AbstractTypeCpInfo
/*    */   extends CpInfo
/*    */ {
/*    */   int u2nameIndex;
/*    */   
/*    */   protected AbstractTypeCpInfo(int tag) {
/* 32 */     super(tag);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getNameIndex() {
/* 42 */     return this.u2nameIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setNameIndex(int index) {
/* 51 */     this.u2nameIndex = index;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void markUtf8Refs(ConstantPool pool) {
/* 56 */     pool.incRefCount(this.u2nameIndex);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 61 */     this.u2nameIndex = din.readUnsignedShort();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void writeInfo(DataOutput dout) throws IOException {
/* 66 */     dout.writeShort(this.u2nameIndex);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/AbstractTypeCpInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */