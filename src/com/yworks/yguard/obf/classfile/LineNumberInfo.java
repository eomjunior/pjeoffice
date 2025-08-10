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
/*     */ public class LineNumberInfo
/*     */ {
/*     */   private int u2startpc;
/*     */   private int u2lineNumber;
/*     */   
/*     */   public LineNumberInfo(int startPC, int lineNumber) {
/*  33 */     setLineNumber(lineNumber);
/*  34 */     setStartPC(startPC);
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
/*     */   public static LineNumberInfo create(DataInput din) throws IOException {
/*  48 */     LineNumberInfo lni = new LineNumberInfo();
/*  49 */     lni.read(din);
/*  50 */     return lni;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineNumber(int number) {
/*  59 */     this.u2lineNumber = number;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/*  68 */     return this.u2lineNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStartPC() {
/*  77 */     return this.u2startpc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartPC(int startPc) {
/*  86 */     this.u2startpc = startPc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineNumberInfo() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void read(DataInput din) throws IOException {
/*  98 */     this.u2startpc = din.readUnsignedShort();
/*  99 */     this.u2lineNumber = din.readUnsignedShort();
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
/* 110 */     dout.writeShort(this.u2startpc);
/* 111 */     dout.writeShort(this.u2lineNumber);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/LineNumberInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */