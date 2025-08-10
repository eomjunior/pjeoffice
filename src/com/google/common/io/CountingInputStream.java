/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @J2ktIncompatible
/*    */ @GwtIncompatible
/*    */ public final class CountingInputStream
/*    */   extends FilterInputStream
/*    */ {
/*    */   private long count;
/* 37 */   private long mark = -1L;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CountingInputStream(InputStream in) {
/* 45 */     super((InputStream)Preconditions.checkNotNull(in));
/*    */   }
/*    */ 
/*    */   
/*    */   public long getCount() {
/* 50 */     return this.count;
/*    */   }
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 55 */     int result = this.in.read();
/* 56 */     if (result != -1) {
/* 57 */       this.count++;
/*    */     }
/* 59 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/* 64 */     int result = this.in.read(b, off, len);
/* 65 */     if (result != -1) {
/* 66 */       this.count += result;
/*    */     }
/* 68 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public long skip(long n) throws IOException {
/* 73 */     long result = this.in.skip(n);
/* 74 */     this.count += result;
/* 75 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void mark(int readlimit) {
/* 80 */     this.in.mark(readlimit);
/* 81 */     this.mark = this.count;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void reset() throws IOException {
/* 87 */     if (!this.in.markSupported()) {
/* 88 */       throw new IOException("Mark not supported");
/*    */     }
/* 90 */     if (this.mark == -1L) {
/* 91 */       throw new IOException("Mark not set");
/*    */     }
/*    */     
/* 94 */     this.in.reset();
/* 95 */     this.count = this.mark;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/CountingInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */