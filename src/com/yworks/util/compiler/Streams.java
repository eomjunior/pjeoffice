/*     */ package com.yworks.util.compiler;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.CharBuffer;
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
/*     */ class Streams
/*     */ {
/*     */   static OutputStream newGuard(OutputStream os) {
/*  24 */     return new Guard(os);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Reader newTail(Reader r, int[] tail) {
/*  35 */     return new Tail(r, tail);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Guard
/*     */     extends OutputStream
/*     */   {
/*     */     private final OutputStream os;
/*     */ 
/*     */ 
/*     */     
/*     */     Guard(OutputStream os) {
/*  48 */       this.os = os;
/*     */     }
/*     */     
/*     */     public void write(int b) throws IOException {
/*  52 */       this.os.write(b);
/*     */     }
/*     */     
/*     */     public void write(byte[] b) throws IOException {
/*  56 */       this.os.write(b);
/*     */     }
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {
/*  60 */       this.os.write(b, off, len);
/*     */     }
/*     */     
/*     */     public void flush() throws IOException {
/*  64 */       this.os.flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/*  69 */       this.os.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Tail
/*     */     extends Reader
/*     */   {
/*     */     private final Reader r;
/*     */     
/*     */     private final int[] tail;
/*     */ 
/*     */     
/*     */     Tail(Reader r, int[] tail) {
/*  84 */       this.r = r;
/*  85 */       this.tail = tail;
/*     */     }
/*     */     
/*     */     public int read(CharBuffer target) throws IOException {
/*  89 */       int read = this.r.read(target);
/*  90 */       if (read > -1) {
/*  91 */         this.tail[0] = target.charAt(read - 1);
/*     */       }
/*  93 */       return read;
/*     */     }
/*     */     
/*     */     public int read() throws IOException {
/*  97 */       this.tail[0] = this.r.read(); return this.r.read();
/*     */     }
/*     */     
/*     */     public int read(char[] cbuf) throws IOException {
/* 101 */       int read = this.r.read(cbuf);
/* 102 */       if (read > -1) {
/* 103 */         this.tail[0] = cbuf[read - 1];
/*     */       }
/* 105 */       return read;
/*     */     }
/*     */     
/*     */     public int read(char[] cbuf, int off, int len) throws IOException {
/* 109 */       int read = this.r.read(cbuf, off, len);
/* 110 */       if (read > -1) {
/* 111 */         this.tail[0] = cbuf[off + read - 1];
/*     */       }
/* 113 */       return read;
/*     */     }
/*     */     
/*     */     public long skip(long n) throws IOException {
/* 117 */       return this.r.skip(n);
/*     */     }
/*     */     
/*     */     public boolean ready() throws IOException {
/* 121 */       return this.r.ready();
/*     */     }
/*     */     
/*     */     public boolean markSupported() {
/* 125 */       return this.r.markSupported();
/*     */     }
/*     */     
/*     */     public void mark(int readAheadLimit) throws IOException {
/* 129 */       this.r.mark(readAheadLimit);
/*     */     }
/*     */     
/*     */     public void reset() throws IOException {
/* 133 */       this.r.reset();
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/* 137 */       this.r.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/compiler/Streams.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */