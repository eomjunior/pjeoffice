/*     */ package com.itextpdf.text.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.BufferUnderflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
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
/*     */ class ByteBufferRandomAccessSource
/*     */   implements RandomAccessSource
/*     */ {
/*     */   private final ByteBuffer byteBuffer;
/*     */   
/*     */   public ByteBufferRandomAccessSource(ByteBuffer byteBuffer) {
/*  68 */     this.byteBuffer = byteBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int get(long position) throws IOException {
/*  78 */     if (position > 2147483647L) {
/*  79 */       throw new IllegalArgumentException("Position must be less than Integer.MAX_VALUE");
/*     */     }
/*     */     
/*     */     try {
/*  83 */       if (position >= this.byteBuffer.limit()) {
/*  84 */         return -1;
/*     */       }
/*  86 */       byte b = this.byteBuffer.get((int)position);
/*     */       
/*  88 */       int n = b & 0xFF;
/*     */       
/*  90 */       return n;
/*  91 */     } catch (BufferUnderflowException e) {
/*  92 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int get(long position, byte[] bytes, int off, int len) throws IOException {
/* 103 */     if (position > 2147483647L) {
/* 104 */       throw new IllegalArgumentException("Position must be less than Integer.MAX_VALUE");
/*     */     }
/* 106 */     if (position >= this.byteBuffer.limit()) {
/* 107 */       return -1;
/*     */     }
/* 109 */     this.byteBuffer.position((int)position);
/* 110 */     int bytesFromThisBuffer = Math.min(len, this.byteBuffer.remaining());
/* 111 */     this.byteBuffer.get(bytes, off, bytesFromThisBuffer);
/*     */     
/* 113 */     return bytesFromThisBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long length() {
/* 122 */     return this.byteBuffer.limit();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 130 */     clean(this.byteBuffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean clean(final ByteBuffer buffer) {
/* 139 */     if (buffer == null || !buffer.isDirect()) {
/* 140 */       return false;
/*     */     }
/* 142 */     Boolean b = AccessController.<Boolean>doPrivileged(new PrivilegedAction<Boolean>() {
/*     */           public Boolean run() {
/* 144 */             Boolean success = Boolean.FALSE;
/*     */             try {
/* 146 */               Method getCleanerMethod = buffer.getClass().getMethod("cleaner", (Class[])null);
/* 147 */               getCleanerMethod.setAccessible(true);
/* 148 */               Object cleaner = getCleanerMethod.invoke(buffer, (Object[])null);
/* 149 */               Method clean = cleaner.getClass().getMethod("clean", (Class[])null);
/* 150 */               clean.invoke(cleaner, (Object[])null);
/* 151 */               success = Boolean.TRUE;
/* 152 */             } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */             
/* 156 */             return success;
/*     */           }
/*     */         });
/*     */     
/* 160 */     return b.booleanValue();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/io/ByteBufferRandomAccessSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */