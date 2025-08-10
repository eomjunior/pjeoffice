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
/*     */ public class RecordAttrInfo
/*     */   extends AttrInfo
/*     */ {
/*     */   private static final int COMPONENTS_COUNT_FIELD_SIZE = 2;
/*     */   private RecordComponent[] components;
/*     */   private boolean attrInfoLengthDirty;
/*     */   
/*     */   RecordAttrInfo(ClassFile cf, int attrNameIndex, int attrLength) {
/*  31 */     super(cf, attrNameIndex, attrLength);
/*  32 */     this.components = new RecordComponent[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getAttrName() {
/*  40 */     return "Record";
/*     */   }
/*     */   
/*     */   RecordComponent[] getComponents() {
/*  44 */     return this.components;
/*     */   }
/*     */   
/*     */   protected int getAttrInfoLength() {
/*  48 */     if (this.attrInfoLengthDirty) {
/*  49 */       int newAttrLength = 2;
/*  50 */       for (int i = 0, m = this.components.length; i < m; i++) {
/*  51 */         newAttrLength += componentLength(this.components[i]);
/*     */       }
/*  53 */       this.u4attrLength = newAttrLength;
/*  54 */       this.attrInfoLengthDirty = false;
/*     */     } 
/*  56 */     return this.u4attrLength;
/*     */   }
/*     */   
/*     */   protected void trimAttrsExcept(String[] keepAttrs) {
/*  60 */     boolean dirty = false;
/*  61 */     for (int i = 0, n = this.components.length; i < n; i++) {
/*  62 */       dirty |= this.components[i].trimAttrsExcept(keepAttrs);
/*     */     }
/*  64 */     this.attrInfoLengthDirty = dirty;
/*     */   }
/*     */   
/*     */   protected void markUtf8RefsInInfo(ConstantPool pool) {
/*  68 */     for (int i = 0, n = this.components.length; i < n; i++) {
/*  69 */       this.components[i].markUtf8Refs(pool);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readInfo(DataInput din) throws IOException {
/*  77 */     int u2ComponentsCount = din.readUnsignedShort();
/*  78 */     this.components = new RecordComponent[u2ComponentsCount];
/*  79 */     for (int i = 0; i < u2ComponentsCount; i++) {
/*  80 */       this.components[i] = RecordComponent.read(din, this.owner);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeInfo(DataOutput dout) throws IOException {
/*  88 */     int u2ComponentsCount = this.components.length;
/*  89 */     dout.writeShort(u2ComponentsCount);
/*  90 */     for (int i = 0; i < u2ComponentsCount; i++) {
/*  91 */       this.components[i].write(dout);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int componentLength(RecordComponent c) {
/*  99 */     int length = 6;
/* 100 */     AttrInfo[] attributes = c.getAttributes();
/* 101 */     for (int j = 0, n = attributes.length; j < n; j++) {
/* 102 */       length += 6;
/* 103 */       length += attributes[j].getAttrInfoLength();
/*     */     } 
/* 105 */     return length;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/RecordAttrInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */