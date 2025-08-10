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
/*     */ public class ModuleAttrInfo
/*     */   extends AttrInfo
/*     */ {
/*     */   private int u2moduleNameIndex;
/*     */   private int u2moduleFlags;
/*     */   private int u2moduleVersionIndex;
/*     */   private ModuleRequires[] requires;
/*     */   private ModuleExports[] exports;
/*     */   private ModuleOpens[] opens;
/*     */   private int[] u2usesIndex;
/*     */   private ModuleProvides[] provides;
/*     */   
/*     */   ModuleAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/*  43 */     super(cf, attrNameIndex, attrLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getModuleNameIndex() {
/*  53 */     return this.u2moduleNameIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setModuleNameIndex(int idx) {
/*  62 */     this.u2moduleNameIndex = idx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getAttrName() {
/*  69 */     return "Module";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/*  76 */     if (this.u2moduleVersionIndex > 0) {
/*  77 */       pool.incRefCount(this.u2moduleVersionIndex);
/*     */     }
/*     */     
/*  80 */     for (int i = 0; i < this.requires.length; i++) {
/*  81 */       pool.incRefCount((this.requires[i]).u2requiresVersionIndex);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readInfo(DataInput din) throws IOException {
/*  89 */     this.u2moduleNameIndex = din.readUnsignedShort();
/*  90 */     this.u2moduleFlags = din.readUnsignedShort();
/*  91 */     this.u2moduleVersionIndex = din.readUnsignedShort();
/*     */     
/*  93 */     int u2requiresCount = din.readUnsignedShort();
/*  94 */     this.requires = new ModuleRequires[u2requiresCount];
/*  95 */     for (int i = 0; i < u2requiresCount; i++) {
/*  96 */       this.requires[i] = ModuleRequires.read(din);
/*     */     }
/*     */     
/*  99 */     int u2exportsCount = din.readUnsignedShort();
/* 100 */     this.exports = new ModuleExports[u2exportsCount];
/* 101 */     for (int j = 0; j < u2exportsCount; j++) {
/* 102 */       this.exports[j] = ModuleExports.read(din);
/*     */     }
/*     */     
/* 105 */     int u2opensCount = din.readUnsignedShort();
/* 106 */     this.opens = new ModuleOpens[u2opensCount];
/* 107 */     for (int k = 0; k < u2opensCount; k++) {
/* 108 */       this.opens[k] = ModuleOpens.read(din);
/*     */     }
/*     */     
/* 111 */     int u2usesCount = din.readUnsignedShort();
/* 112 */     this.u2usesIndex = new int[u2usesCount];
/* 113 */     for (int m = 0; m < u2usesCount; m++) {
/* 114 */       this.u2usesIndex[m] = din.readUnsignedShort();
/*     */     }
/*     */     
/* 117 */     int u2providesCount = din.readUnsignedShort();
/* 118 */     this.provides = new ModuleProvides[u2providesCount];
/* 119 */     for (int n = 0; n < u2providesCount; n++) {
/* 120 */       this.provides[n] = ModuleProvides.read(din);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeInfo(DataOutput dout) throws IOException {
/* 128 */     dout.writeShort(this.u2moduleNameIndex);
/* 129 */     dout.writeShort(this.u2moduleFlags);
/* 130 */     dout.writeShort(this.u2moduleVersionIndex);
/*     */     
/* 132 */     int u2requiresCount = this.requires.length;
/* 133 */     dout.writeShort(u2requiresCount);
/* 134 */     for (int i = 0; i < u2requiresCount; i++) {
/* 135 */       this.requires[i].write(dout);
/*     */     }
/*     */     
/* 138 */     int u2exportsCount = this.exports.length;
/* 139 */     dout.writeShort(u2exportsCount);
/* 140 */     for (int j = 0; j < u2exportsCount; j++) {
/* 141 */       this.exports[j].write(dout);
/*     */     }
/*     */     
/* 144 */     int u2opensCount = this.opens.length;
/* 145 */     dout.writeShort(u2opensCount);
/* 146 */     for (int k = 0; k < u2opensCount; k++) {
/* 147 */       this.opens[k].write(dout);
/*     */     }
/*     */     
/* 150 */     int u2usesCount = this.u2usesIndex.length;
/* 151 */     dout.writeShort(u2usesCount);
/* 152 */     for (int m = 0; m < u2usesCount; m++) {
/* 153 */       dout.writeShort(this.u2usesIndex[m]);
/*     */     }
/*     */     
/* 156 */     int u2providesCount = this.provides.length;
/* 157 */     dout.writeShort(u2providesCount);
/* 158 */     for (int n = 0; n < u2providesCount; n++)
/* 159 */       this.provides[n].write(dout); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ModuleAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */