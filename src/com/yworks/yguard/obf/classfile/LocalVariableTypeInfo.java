/*     */ package com.yworks.yguard.obf.classfile;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
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
/*     */ public class LocalVariableTypeInfo
/*     */ {
/*     */   private int u2startpc;
/*     */   private int u2length;
/*     */   private int u2nameIndex;
/*     */   private int u2signatureIndex;
/*     */   private int u2index;
/*     */   
/*     */   public static LocalVariableTypeInfo create(DataInput din) throws IOException {
/*  42 */     if (din == null) throw new NullPointerException("DataInput cannot be null!"); 
/*  43 */     LocalVariableTypeInfo lvi = new LocalVariableTypeInfo();
/*  44 */     lvi.read(din);
/*  45 */     return lvi;
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
/*     */   protected int getNameIndex() {
/*  57 */     return this.u2nameIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setNameIndex(int index) {
/*  64 */     this.u2nameIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getSignatureIndex() {
/*  71 */     return this.u2signatureIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setSignatureIndex(int index) {
/*  78 */     this.u2signatureIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void markUtf8Refs(ConstantPool pool) {
/*  87 */     pool.incRefCount(this.u2nameIndex);
/*  88 */     pool.incRefCount(this.u2signatureIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   private void read(DataInput din) throws IOException {
/*  93 */     this.u2startpc = din.readUnsignedShort();
/*  94 */     this.u2length = din.readUnsignedShort();
/*  95 */     this.u2nameIndex = din.readUnsignedShort();
/*  96 */     this.u2signatureIndex = din.readUnsignedShort();
/*  97 */     this.u2index = din.readUnsignedShort();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(DataOutput dout) throws IOException {
/* 108 */     dout.writeShort(this.u2startpc);
/* 109 */     dout.writeShort(this.u2length);
/* 110 */     dout.writeShort(this.u2nameIndex);
/* 111 */     dout.writeShort(this.u2signatureIndex);
/* 112 */     dout.writeShort(this.u2index);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/LocalVariableTypeInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */