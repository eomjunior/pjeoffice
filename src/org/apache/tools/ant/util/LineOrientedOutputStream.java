/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
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
/*     */ 
/*     */ public abstract class LineOrientedOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private static final int INITIAL_SIZE = 132;
/*     */   private static final int CR = 13;
/*     */   private static final int LF = 10;
/*  42 */   private ByteArrayOutputStream buffer = new ByteArrayOutputStream(132);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean skip = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void write(int cc) throws IOException {
/*  55 */     byte c = (byte)cc;
/*  56 */     if (c == 10 || c == 13) {
/*  57 */       if (!this.skip) {
/*  58 */         processBuffer();
/*     */       }
/*     */     } else {
/*  61 */       this.buffer.write(cc);
/*     */     } 
/*  63 */     this.skip = (c == 13);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processBuffer() throws IOException {
/*     */     try {
/*  81 */       processLine(this.buffer.toByteArray());
/*     */     } finally {
/*  83 */       this.buffer.reset();
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
/*     */   
/*     */   protected abstract void processLine(String paramString) throws IOException;
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
/*     */   protected void processLine(byte[] line) throws IOException {
/* 109 */     processLine(new String(line));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 118 */     if (this.buffer.size() > 0) {
/* 119 */       processBuffer();
/*     */     }
/* 121 */     super.close();
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
/*     */   
/*     */   public final void write(byte[] b, int off, int len) throws IOException {
/* 136 */     int offset = off;
/* 137 */     int blockStartOffset = offset;
/* 138 */     int remaining = len;
/* 139 */     while (remaining > 0) {
/* 140 */       while (remaining > 0 && b[offset] != 10 && b[offset] != 13) {
/* 141 */         offset++;
/* 142 */         remaining--;
/*     */       } 
/*     */       
/* 145 */       int blockLength = offset - blockStartOffset;
/* 146 */       if (blockLength > 0) {
/* 147 */         this.buffer.write(b, blockStartOffset, blockLength);
/*     */       }
/* 149 */       while (remaining > 0 && (b[offset] == 10 || b[offset] == 13)) {
/* 150 */         write(b[offset]);
/* 151 */         offset++;
/* 152 */         remaining--;
/*     */       } 
/* 154 */       blockStartOffset = offset;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/LineOrientedOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */