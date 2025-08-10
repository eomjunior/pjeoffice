/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.Objects;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ final class CharSequenceReader
/*     */   extends Reader
/*     */ {
/*     */   @CheckForNull
/*     */   private CharSequence seq;
/*     */   private int pos;
/*     */   private int mark;
/*     */   
/*     */   public CharSequenceReader(CharSequence seq) {
/*  47 */     this.seq = (CharSequence)Preconditions.checkNotNull(seq);
/*     */   }
/*     */   
/*     */   private void checkOpen() throws IOException {
/*  51 */     if (this.seq == null) {
/*  52 */       throw new IOException("reader closed");
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasRemaining() {
/*  57 */     return (remaining() > 0);
/*     */   }
/*     */   
/*     */   private int remaining() {
/*  61 */     Objects.requireNonNull(this.seq);
/*  62 */     return this.seq.length() - this.pos;
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
/*     */ 
/*     */   
/*     */   public synchronized int read(CharBuffer target) throws IOException {
/*  79 */     Preconditions.checkNotNull(target);
/*  80 */     checkOpen();
/*  81 */     Objects.requireNonNull(this.seq);
/*  82 */     if (!hasRemaining()) {
/*  83 */       return -1;
/*     */     }
/*  85 */     int charsToRead = Math.min(target.remaining(), remaining());
/*  86 */     for (int i = 0; i < charsToRead; i++) {
/*  87 */       target.put(this.seq.charAt(this.pos++));
/*     */     }
/*  89 */     return charsToRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int read() throws IOException {
/*  94 */     checkOpen();
/*  95 */     Objects.requireNonNull(this.seq);
/*  96 */     return hasRemaining() ? this.seq.charAt(this.pos++) : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int read(char[] cbuf, int off, int len) throws IOException {
/* 101 */     Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
/* 102 */     checkOpen();
/* 103 */     Objects.requireNonNull(this.seq);
/* 104 */     if (!hasRemaining()) {
/* 105 */       return -1;
/*     */     }
/* 107 */     int charsToRead = Math.min(len, remaining());
/* 108 */     for (int i = 0; i < charsToRead; i++) {
/* 109 */       cbuf[off + i] = this.seq.charAt(this.pos++);
/*     */     }
/* 111 */     return charsToRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized long skip(long n) throws IOException {
/* 116 */     Preconditions.checkArgument((n >= 0L), "n (%s) may not be negative", n);
/* 117 */     checkOpen();
/* 118 */     int charsToSkip = (int)Math.min(remaining(), n);
/* 119 */     this.pos += charsToSkip;
/* 120 */     return charsToSkip;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean ready() throws IOException {
/* 125 */     checkOpen();
/* 126 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 131 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void mark(int readAheadLimit) throws IOException {
/* 136 */     Preconditions.checkArgument((readAheadLimit >= 0), "readAheadLimit (%s) may not be negative", readAheadLimit);
/* 137 */     checkOpen();
/* 138 */     this.mark = this.pos;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/* 143 */     checkOpen();
/* 144 */     this.pos = this.mark;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() throws IOException {
/* 149 */     this.seq = null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/CharSequenceReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */