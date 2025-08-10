/*     */ package org.apache.hc.client5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.hc.client5.http.impl.Wire;
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
/*     */ class LoggingInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private final InputStream in;
/*     */   private final Wire wire;
/*     */   
/*     */   public LoggingInputStream(InputStream in, Wire wire) {
/*  42 */     this.in = in;
/*  43 */     this.wire = wire;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*     */     try {
/*  49 */       int b = this.in.read();
/*  50 */       if (b == -1) {
/*  51 */         this.wire.input("end of stream");
/*     */       } else {
/*  53 */         this.wire.input(b);
/*     */       } 
/*  55 */       return b;
/*  56 */     } catch (IOException ex) {
/*  57 */       this.wire.input("[read] I/O error: " + ex.getMessage());
/*  58 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/*     */     try {
/*  65 */       int bytesRead = this.in.read(b);
/*  66 */       if (bytesRead == -1) {
/*  67 */         this.wire.input("end of stream");
/*  68 */       } else if (bytesRead > 0) {
/*  69 */         this.wire.input(b, 0, bytesRead);
/*     */       } 
/*  71 */       return bytesRead;
/*  72 */     } catch (IOException ex) {
/*  73 */       this.wire.input("[read] I/O error: " + ex.getMessage());
/*  74 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*     */     try {
/*  81 */       int bytesRead = this.in.read(b, off, len);
/*  82 */       if (bytesRead == -1) {
/*  83 */         this.wire.input("end of stream");
/*  84 */       } else if (bytesRead > 0) {
/*  85 */         this.wire.input(b, off, bytesRead);
/*     */       } 
/*  87 */       return bytesRead;
/*  88 */     } catch (IOException ex) {
/*  89 */       this.wire.input("[read] I/O error: " + ex.getMessage());
/*  90 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/*     */     try {
/*  97 */       return super.skip(n);
/*  98 */     } catch (IOException ex) {
/*  99 */       this.wire.input("[skip] I/O error: " + ex.getMessage());
/* 100 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/*     */     try {
/* 107 */       return this.in.available();
/* 108 */     } catch (IOException ex) {
/* 109 */       this.wire.input("[available] I/O error : " + ex.getMessage());
/* 110 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mark(int readlimit) {
/* 116 */     super.mark(readlimit);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() throws IOException {
/* 121 */     super.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 132 */       this.in.close();
/* 133 */     } catch (IOException ex) {
/* 134 */       this.wire.input("[close] I/O error: " + ex.getMessage());
/* 135 */       throw ex;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/io/LoggingInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */