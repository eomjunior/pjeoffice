/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Closeable;
/*     */ import java.io.Flushable;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
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
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ class AppendableWriter
/*     */   extends Writer
/*     */ {
/*     */   private final Appendable target;
/*     */   private boolean closed;
/*     */   
/*     */   AppendableWriter(Appendable target) {
/*  48 */     this.target = (Appendable)Preconditions.checkNotNull(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/*  57 */     checkNotClosed();
/*     */ 
/*     */     
/*  60 */     this.target.append(new String(cbuf, off, len));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int c) throws IOException {
/*  69 */     checkNotClosed();
/*  70 */     this.target.append((char)c);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(String str) throws IOException {
/*  75 */     Preconditions.checkNotNull(str);
/*  76 */     checkNotClosed();
/*  77 */     this.target.append(str);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(String str, int off, int len) throws IOException {
/*  82 */     Preconditions.checkNotNull(str);
/*  83 */     checkNotClosed();
/*     */     
/*  85 */     this.target.append(str, off, off + len);
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*  90 */     checkNotClosed();
/*  91 */     if (this.target instanceof Flushable) {
/*  92 */       ((Flushable)this.target).flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  98 */     this.closed = true;
/*  99 */     if (this.target instanceof Closeable) {
/* 100 */       ((Closeable)this.target).close();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Writer append(char c) throws IOException {
/* 106 */     checkNotClosed();
/* 107 */     this.target.append(c);
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Writer append(@CheckForNull CharSequence charSeq) throws IOException {
/* 113 */     checkNotClosed();
/* 114 */     this.target.append(charSeq);
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Writer append(@CheckForNull CharSequence charSeq, int start, int end) throws IOException {
/* 120 */     checkNotClosed();
/* 121 */     this.target.append(charSeq, start, end);
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   private void checkNotClosed() throws IOException {
/* 126 */     if (this.closed)
/* 127 */       throw new IOException("Cannot write to a closed writer."); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/AppendableWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */