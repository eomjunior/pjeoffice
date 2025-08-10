/*     */ package com.yworks.yguard.obf.classfile;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnclosingMethodAttrInfo
/*     */   extends AttrInfo
/*     */ {
/*     */   private int u2classIndex;
/*     */   private int u2nameAndTypeIndex;
/*     */   
/*     */   protected EnclosingMethodAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/*  41 */     super(cf, attrNameIndex, attrLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getAttrName() {
/*  48 */     return "EnclosingMethod";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getClassIndex() {
/*  56 */     return this.u2classIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setClassIndex(int index) {
/*  63 */     this.u2classIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getNameAndTypeIndex() {
/*  70 */     return this.u2nameAndTypeIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setNameAndTypeIndex(int index) {
/*  77 */     this.u2nameAndTypeIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/*  88 */     if (this.u2nameAndTypeIndex > 0) {
/*  89 */       NameAndTypeCpInfo ntcpi = (NameAndTypeCpInfo)pool.getCpEntry(this.u2nameAndTypeIndex);
/*  90 */       ntcpi.incRefCount();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readInfo(DataInput din) throws IOException {
/* 104 */     this.u2classIndex = din.readUnsignedShort();
/* 105 */     this.u2nameAndTypeIndex = din.readUnsignedShort();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeInfo(DataOutput dout) throws IOException {
/* 111 */     dout.writeShort(this.u2classIndex);
/* 112 */     dout.writeShort(this.u2nameAndTypeIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dump(PrintWriter pw, ClassFile cf, int index) {
/* 124 */     pw.println("  EnclosingMethod ");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/EnclosingMethodAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */