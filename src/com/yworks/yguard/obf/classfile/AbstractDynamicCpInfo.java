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
/*    */ public abstract class AbstractDynamicCpInfo
/*    */   extends CpInfo
/*    */ {
/*    */   private int u2bootstrapMethodAttrIndex;
/*    */   private int u2nameAndTypeIndex;
/*    */   
/*    */   protected AbstractDynamicCpInfo(int tag) {
/* 30 */     super(tag);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 35 */     this.u2bootstrapMethodAttrIndex = din.readUnsignedShort();
/* 36 */     this.u2nameAndTypeIndex = din.readUnsignedShort();
/*    */   }
/*    */   
/*    */   protected void writeInfo(DataOutput dout) throws IOException {
/* 40 */     dout.writeShort(this.u2bootstrapMethodAttrIndex);
/* 41 */     dout.writeShort(this.u2nameAndTypeIndex);
/*    */   }
/*    */   
/*    */   protected void markNTRefs(ConstantPool pool) {
/* 45 */     pool.incRefCount(this.u2nameAndTypeIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getBootstrapMethodAttrIndex() {
/* 54 */     return this.u2bootstrapMethodAttrIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNameAndTypeIndex() {
/* 63 */     return this.u2nameAndTypeIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setNameAndTypeIndex(int index) {
/* 72 */     this.u2nameAndTypeIndex = index;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/AbstractDynamicCpInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */