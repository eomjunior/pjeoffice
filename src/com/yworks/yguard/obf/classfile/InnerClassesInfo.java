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
/*     */ public class InnerClassesInfo
/*     */ {
/*     */   private int u2innerClassInfoIndex;
/*     */   private int u2outerClassInfoIndex;
/*     */   private int u2innerNameIndex;
/*     */   private int u2innerClassAccessFlags;
/*     */   
/*     */   public static InnerClassesInfo create(DataInput din) throws IOException {
/*  40 */     InnerClassesInfo ici = new InnerClassesInfo();
/*  41 */     ici.read(din);
/*  42 */     return ici;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getModifiers() {
/*  51 */     int mods = 0;
/*  52 */     if ((this.u2innerClassAccessFlags & 0x1) == 1) mods |= 0x1; 
/*  53 */     if ((this.u2innerClassAccessFlags & 0x2) == 2) mods |= 0x2; 
/*  54 */     if ((this.u2innerClassAccessFlags & 0x4) == 4) mods |= 0x4; 
/*  55 */     if ((this.u2innerClassAccessFlags & 0x8) == 8) mods |= 0x8; 
/*  56 */     if ((this.u2innerClassAccessFlags & 0x10) == 16) mods |= 0x10; 
/*  57 */     if ((this.u2innerClassAccessFlags & 0x200) == 512) mods |= 0x200; 
/*  58 */     if ((this.u2innerClassAccessFlags & 0x400) == 1024) mods |= 0x400; 
/*  59 */     return mods;
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
/*     */   protected int getInnerClassIndex() {
/*  71 */     return this.u2innerClassInfoIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getInnerNameIndex() {
/*  78 */     return this.u2innerNameIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInnerNameIndex(int index) {
/*  85 */     this.u2innerNameIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void markUtf8Refs(ConstantPool pool) {
/*  95 */     if (this.u2innerNameIndex != 0)
/*     */     {
/*  97 */       pool.incRefCount(this.u2innerNameIndex);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void read(DataInput din) throws IOException {
/* 103 */     this.u2innerClassInfoIndex = din.readUnsignedShort();
/* 104 */     this.u2outerClassInfoIndex = din.readUnsignedShort();
/* 105 */     this.u2innerNameIndex = din.readUnsignedShort();
/* 106 */     this.u2innerClassAccessFlags = din.readUnsignedShort();
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
/* 117 */     dout.writeShort(this.u2innerClassInfoIndex);
/* 118 */     dout.writeShort(this.u2outerClassInfoIndex);
/* 119 */     dout.writeShort(this.u2innerNameIndex);
/* 120 */     dout.writeShort(this.u2innerClassAccessFlags);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/InnerClassesInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */