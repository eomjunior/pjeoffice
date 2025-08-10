/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Iterator;
/*     */ import javax.annotation.CheckForNull;
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
/*     */ final class MultiInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private Iterator<? extends ByteSource> it;
/*     */   @CheckForNull
/*     */   private InputStream in;
/*     */   
/*     */   public MultiInputStream(Iterator<? extends ByteSource> it) throws IOException {
/*  47 */     this.it = (Iterator<? extends ByteSource>)Preconditions.checkNotNull(it);
/*  48 */     advance();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  53 */     if (this.in != null) {
/*     */       try {
/*  55 */         this.in.close();
/*     */       } finally {
/*  57 */         this.in = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void advance() throws IOException {
/*  64 */     close();
/*  65 */     if (this.it.hasNext()) {
/*  66 */       this.in = ((ByteSource)this.it.next()).openStream();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/*  72 */     if (this.in == null) {
/*  73 */       return 0;
/*     */     }
/*  75 */     return this.in.available();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/*  80 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  85 */     while (this.in != null) {
/*  86 */       int result = this.in.read();
/*  87 */       if (result != -1) {
/*  88 */         return result;
/*     */       }
/*  90 */       advance();
/*     */     } 
/*  92 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  97 */     Preconditions.checkNotNull(b);
/*  98 */     while (this.in != null) {
/*  99 */       int result = this.in.read(b, off, len);
/* 100 */       if (result != -1) {
/* 101 */         return result;
/*     */       }
/* 103 */       advance();
/*     */     } 
/* 105 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 110 */     if (this.in == null || n <= 0L) {
/* 111 */       return 0L;
/*     */     }
/* 113 */     long result = this.in.skip(n);
/* 114 */     if (result != 0L) {
/* 115 */       return result;
/*     */     }
/* 117 */     if (read() == -1) {
/* 118 */       return 0L;
/*     */     }
/* 120 */     return 1L + this.in.skip(n - 1L);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/MultiInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */