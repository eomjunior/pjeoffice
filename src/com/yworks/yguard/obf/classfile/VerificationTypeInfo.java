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
/*     */ public class VerificationTypeInfo
/*     */ {
/*     */   private int u2_cPoolIndex;
/*     */   private int u2_offset;
/*     */   private int u1_tag;
/*     */   
/*     */   public static VerificationTypeInfo create(DataInput din) throws IOException {
/*  26 */     if (din == null) throw new NullPointerException("DataInput cannot be null!"); 
/*  27 */     VerificationTypeInfo vti = new VerificationTypeInfo();
/*  28 */     vti.read(din);
/*  29 */     return vti;
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
/*     */   
/*     */   protected void markUtf8Refs(ConstantPool pool) {
/*  43 */     switch (this.u1_tag) {
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */         return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     throw new IllegalArgumentException("Unkown tag " + this.u1_tag);
/*     */   }
/*     */ 
/*     */   
/*     */   private void read(DataInput din) throws IOException {
/*  68 */     this.u2_cPoolIndex = -1;
/*  69 */     this.u1_tag = din.readUnsignedByte();
/*  70 */     switch (this.u1_tag) {
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 7:
/*  86 */         this.u2_cPoolIndex = din.readUnsignedShort();
/*     */       
/*     */       case 8:
/*  89 */         this.u2_offset = din.readUnsignedShort();
/*     */     } 
/*     */     
/*  92 */     throw new IllegalArgumentException("Unkown tag " + this.u1_tag);
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
/* 103 */     dout.writeByte(this.u1_tag);
/* 104 */     switch (this.u1_tag) {
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 7:
/* 120 */         dout.writeShort(this.u2_cPoolIndex);
/*     */       
/*     */       case 8:
/* 123 */         dout.writeShort(this.u2_offset);
/*     */     } 
/*     */     
/* 126 */     throw new IllegalArgumentException("Unkown tag " + this.u1_tag);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/VerificationTypeInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */