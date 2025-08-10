/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class LittleEndianDataOutputStream
/*     */   extends FilterOutputStream
/*     */   implements DataOutput
/*     */ {
/*     */   public LittleEndianDataOutputStream(OutputStream out) {
/*  49 */     super(new DataOutputStream((OutputStream)Preconditions.checkNotNull(out)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  55 */     this.out.write(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeBoolean(boolean v) throws IOException {
/*  60 */     ((DataOutputStream)this.out).writeBoolean(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeByte(int v) throws IOException {
/*  65 */     ((DataOutputStream)this.out).writeByte(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void writeBytes(String s) throws IOException {
/*  75 */     ((DataOutputStream)this.out).writeBytes(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeChar(int v) throws IOException {
/*  86 */     writeShort(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeChars(String s) throws IOException {
/*  97 */     for (int i = 0; i < s.length(); i++) {
/*  98 */       writeChar(s.charAt(i));
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
/*     */   public void writeDouble(double v) throws IOException {
/* 110 */     writeLong(Double.doubleToLongBits(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeFloat(float v) throws IOException {
/* 121 */     writeInt(Float.floatToIntBits(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeInt(int v) throws IOException {
/* 132 */     this.out.write(0xFF & v);
/* 133 */     this.out.write(0xFF & v >> 8);
/* 134 */     this.out.write(0xFF & v >> 16);
/* 135 */     this.out.write(0xFF & v >> 24);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeLong(long v) throws IOException {
/* 146 */     byte[] bytes = Longs.toByteArray(Long.reverseBytes(v));
/* 147 */     write(bytes, 0, bytes.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeShort(int v) throws IOException {
/* 158 */     this.out.write(0xFF & v);
/* 159 */     this.out.write(0xFF & v >> 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeUTF(String str) throws IOException {
/* 164 */     ((DataOutputStream)this.out).writeUTF(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 172 */     this.out.close();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/LittleEndianDataOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */