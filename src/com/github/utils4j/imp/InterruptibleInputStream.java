/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InterruptedIOException;
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
/*     */ public class InterruptibleInputStream
/*     */   extends InputStream
/*     */ {
/*     */   protected final InputStream in;
/*     */   protected int sleepTime;
/*     */   
/*     */   public InterruptibleInputStream(InputStream in) {
/*  40 */     this(in, 1000);
/*     */   }
/*     */   
/*     */   public InterruptibleInputStream(InputStream in, int sleepTime) {
/*  44 */     this.in = in;
/*  45 */     this.sleepTime = sleepTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  50 */     while (!Thread.interrupted()) {
/*  51 */       if (this.in.available() > 0) {
/*  52 */         return this.in.read();
/*     */       }
/*     */       try {
/*  55 */         Thread.sleep(this.sleepTime);
/*  56 */       } catch (InterruptedException e) {
/*     */         break;
/*     */       } 
/*     */     } 
/*  60 */     Thread.currentThread().interrupt();
/*  61 */     throw new InterruptedIOException("Thread interrupted while reading");
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  66 */     if (b == null)
/*  67 */       throw new NullPointerException(); 
/*  68 */     if (off < 0 || len < 0 || len > b.length - off)
/*  69 */       throw new IndexOutOfBoundsException(); 
/*  70 */     if (len == 0) {
/*  71 */       return 0;
/*     */     }
/*  73 */     int c = -1;
/*  74 */     while (!Thread.interrupted()) {
/*  75 */       if (this.in.available() > 0) {
/*  76 */         c = this.in.read();
/*     */         break;
/*     */       } 
/*     */       try {
/*  80 */         Thread.sleep(this.sleepTime);
/*  81 */       } catch (InterruptedException e) {
/*  82 */         Thread.currentThread().interrupt();
/*  83 */         throw new InterruptedIOException("Thread interrupted while reading");
/*     */       } 
/*     */     } 
/*     */     
/*  87 */     if (c == -1) {
/*  88 */       return -1;
/*     */     }
/*  90 */     b[off] = (byte)c;
/*     */     
/*  92 */     int i = 1;
/*     */     try {
/*  94 */       for (; i < len; i++) {
/*  95 */         c = -1;
/*  96 */         if (this.in.available() > 0) {
/*  97 */           c = this.in.read();
/*     */         }
/*  99 */         if (c == -1) {
/*     */           break;
/*     */         }
/* 102 */         b[off + i] = (byte)c;
/*     */       } 
/* 104 */     } catch (IOException iOException) {}
/*     */     
/* 106 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 111 */     return this.in.available();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 116 */     this.in.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void mark(int readlimit) {
/* 121 */     this.in.mark(readlimit);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/* 126 */     this.in.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 131 */     return this.in.markSupported();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/InterruptibleInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */