/*    */ package com.yworks.yguard.obf.classfile;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintWriter;
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
/*    */ 
/*    */ public abstract class RefCpInfo
/*    */   extends CpInfo
/*    */ {
/*    */   private int u2classIndex;
/*    */   private int u2nameAndTypeIndex;
/*    */   
/*    */   protected RefCpInfo(int tag) {
/* 37 */     super(tag);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getClassIndex() {
/* 46 */     return this.u2classIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getNameAndTypeIndex() {
/* 53 */     return this.u2nameAndTypeIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setNameAndTypeIndex(int index) {
/* 60 */     this.u2nameAndTypeIndex = index;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void markNTRefs(ConstantPool pool) {
/* 65 */     pool.incRefCount(this.u2nameAndTypeIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readInfo(DataInput din) throws IOException {
/* 71 */     this.u2classIndex = din.readUnsignedShort();
/* 72 */     this.u2nameAndTypeIndex = din.readUnsignedShort();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void writeInfo(DataOutput dout) throws IOException {
/* 78 */     dout.writeShort(this.u2classIndex);
/* 79 */     dout.writeShort(this.u2nameAndTypeIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void dump(PrintWriter pw, ClassFile cf, int index) {
/* 85 */     pw.println("  Ref " + Integer.toString(index) + ": " + ((Utf8CpInfo)cf.getCpEntry(((ClassCpInfo)cf.getCpEntry(this.u2classIndex)).getNameIndex())).getString() + " " + ((Utf8CpInfo)cf
/* 86 */         .getCpEntry(((NameAndTypeCpInfo)cf.getCpEntry(this.u2nameAndTypeIndex)).getNameIndex())).getString() + " " + ((Utf8CpInfo)cf
/* 87 */         .getCpEntry(((NameAndTypeCpInfo)cf.getCpEntry(this.u2nameAndTypeIndex)).getDescriptorIndex())).getString());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/RefCpInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */