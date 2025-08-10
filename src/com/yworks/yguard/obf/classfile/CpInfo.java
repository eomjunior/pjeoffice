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
/*     */ public abstract class CpInfo
/*     */   implements ClassConstants
/*     */ {
/*     */   private int u1tag;
/*     */   private byte[] info;
/*  30 */   protected int refCount = 0;
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
/*     */   public static CpInfo create(DataInput din) throws IOException {
/*  44 */     if (din == null) throw new NullPointerException("No input stream was provided.");
/*     */ 
/*     */     
/*  47 */     CpInfo ci = null;
/*  48 */     switch (din.readUnsignedByte()) {
/*     */       case 1:
/*  50 */         ci = new Utf8CpInfo();
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
/*  69 */         ci.readInfo(din);
/*  70 */         return ci;case 3: ci = new IntegerCpInfo(); ci.readInfo(din); return ci;case 4: ci = new FloatCpInfo(); ci.readInfo(din); return ci;case 5: ci = new LongCpInfo(); ci.readInfo(din); return ci;case 6: ci = new DoubleCpInfo(); ci.readInfo(din); return ci;case 7: ci = new ClassCpInfo(); ci.readInfo(din); return ci;case 8: ci = new StringCpInfo(); ci.readInfo(din); return ci;case 9: ci = new FieldrefCpInfo(); ci.readInfo(din); return ci;case 10: ci = new MethodrefCpInfo(); ci.readInfo(din); return ci;case 11: ci = new InterfaceMethodrefCpInfo(); ci.readInfo(din); return ci;case 12: ci = new NameAndTypeCpInfo(); ci.readInfo(din); return ci;case 15: ci = new MethodHandleCpInfo(); ci.readInfo(din); return ci;case 16: ci = new MethodTypeCpInfo(); ci.readInfo(din); return ci;case 17: ci = new DynamicCpInfo(); ci.readInfo(din); return ci;case 18: ci = new InvokeDynamicCpInfo(); ci.readInfo(din); return ci;case 19: ci = new ModuleCpInfo(); ci.readInfo(din); return ci;case 20: ci = new PackageCpInfo(); ci.readInfo(din); return ci;
/*     */     } 
/*     */     throw new IOException("Unknown tag type in constant pool.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CpInfo(int tag) {
/*  81 */     this.u1tag = tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void readInfo(DataInput paramDataInput) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void markUtf8Refs(ConstantPool pool) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void markNTRefs(ConstantPool pool) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(DataOutput dout) throws IOException {
/* 115 */     if (dout == null) throw new IOException("No output stream was provided."); 
/* 116 */     dout.writeByte(this.u1tag);
/* 117 */     writeInfo(dout);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void writeInfo(DataOutput paramDataOutput) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRefCount() {
/* 133 */     return this.refCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public void incRefCount() {
/* 138 */     this.refCount++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decRefCount() {
/* 145 */     if (this.refCount == 0) {
/* 146 */       throw new IllegalStateException("Illegal to decrement reference count that is already zero.");
/*     */     }
/* 148 */     this.refCount--;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetRefCount() {
/* 154 */     this.refCount = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dump(PrintWriter pw, ClassFile cf, int index) {
/* 164 */     pw.println(getClass().getName());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/CpInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */