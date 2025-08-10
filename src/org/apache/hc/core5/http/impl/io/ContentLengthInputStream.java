/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.StreamClosedException;
/*     */ import org.apache.hc.core5.http.io.SessionInputBuffer;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ public class ContentLengthInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private static final int BUFFER_SIZE = 2048;
/*     */   private final SessionInputBuffer buffer;
/*     */   private final InputStream inputStream;
/*     */   private final long contentLength;
/*     */   private long pos;
/*     */   private boolean closed;
/*     */   
/*     */   public ContentLengthInputStream(SessionInputBuffer buffer, InputStream inputStream, long contentLength) {
/*  83 */     this.buffer = (SessionInputBuffer)Args.notNull(buffer, "Session input buffer");
/*  84 */     this.inputStream = (InputStream)Args.notNull(inputStream, "Input stream");
/*  85 */     this.contentLength = Args.notNegative(contentLength, "Content length");
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
/*     */   public void close() throws IOException {
/*  97 */     if (!this.closed) {
/*     */       try {
/*  99 */         if (this.pos < this.contentLength) {
/* 100 */           byte[] buffer = new byte[2048];
/* 101 */           while (read(buffer) >= 0);
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 108 */         this.closed = true;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 115 */     int len = this.buffer.length();
/* 116 */     return Math.min(len, (int)(this.contentLength - this.pos));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 127 */     if (this.closed) {
/* 128 */       throw new StreamClosedException();
/*     */     }
/*     */     
/* 131 */     if (this.pos >= this.contentLength) {
/* 132 */       return -1;
/*     */     }
/* 134 */     int b = this.buffer.read(this.inputStream);
/* 135 */     if (b == -1) {
/* 136 */       if (this.pos < this.contentLength)
/* 137 */         throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: %d; received: %d)", new Object[] {
/*     */               
/* 139 */               Long.valueOf(this.contentLength), Long.valueOf(this.pos)
/*     */             }); 
/*     */     } else {
/* 142 */       this.pos++;
/*     */     } 
/* 144 */     return b;
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
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 161 */     if (this.closed) {
/* 162 */       throw new StreamClosedException();
/*     */     }
/*     */     
/* 165 */     if (this.pos >= this.contentLength) {
/* 166 */       return -1;
/*     */     }
/*     */     
/* 169 */     int chunk = len;
/* 170 */     if (this.pos + len > this.contentLength) {
/* 171 */       chunk = (int)(this.contentLength - this.pos);
/*     */     }
/* 173 */     int count = this.buffer.read(b, off, chunk, this.inputStream);
/* 174 */     if (count == -1 && this.pos < this.contentLength)
/* 175 */       throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: %d; received: %d)", new Object[] {
/*     */             
/* 177 */             Long.valueOf(this.contentLength), Long.valueOf(this.pos)
/*     */           }); 
/* 179 */     if (count > 0) {
/* 180 */       this.pos += count;
/*     */     }
/* 182 */     return count;
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
/*     */   public int read(byte[] b) throws IOException {
/* 195 */     return read(b, 0, b.length);
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
/*     */   public long skip(long n) throws IOException {
/* 208 */     if (n <= 0L) {
/* 209 */       return 0L;
/*     */     }
/* 211 */     byte[] buffer = new byte[2048];
/*     */ 
/*     */     
/* 214 */     long remaining = Math.min(n, this.contentLength - this.pos);
/*     */     
/* 216 */     long count = 0L;
/* 217 */     while (remaining > 0L) {
/* 218 */       int readLen = read(buffer, 0, (int)Math.min(2048L, remaining));
/* 219 */       if (readLen == -1) {
/*     */         break;
/*     */       }
/* 222 */       count += readLen;
/* 223 */       remaining -= readLen;
/*     */     } 
/* 225 */     return count;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/ContentLengthInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */