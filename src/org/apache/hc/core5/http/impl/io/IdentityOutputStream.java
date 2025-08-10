/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.hc.core5.http.StreamClosedException;
/*     */ import org.apache.hc.core5.http.io.SessionOutputBuffer;
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
/*     */ public class IdentityOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final SessionOutputBuffer buffer;
/*     */   private final OutputStream outputStream;
/*     */   private boolean closed;
/*     */   
/*     */   public IdentityOutputStream(SessionOutputBuffer buffer, OutputStream outputStream) {
/*  65 */     this.buffer = (SessionOutputBuffer)Args.notNull(buffer, "Session output buffer");
/*  66 */     this.outputStream = (OutputStream)Args.notNull(outputStream, "Output stream");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  75 */     if (!this.closed) {
/*  76 */       this.closed = true;
/*  77 */       this.buffer.flush(this.outputStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*  83 */     this.buffer.flush(this.outputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  88 */     if (this.closed) {
/*  89 */       throw new StreamClosedException();
/*     */     }
/*  91 */     this.buffer.write(b, off, len, this.outputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/*  96 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 101 */     if (this.closed) {
/* 102 */       throw new StreamClosedException();
/*     */     }
/* 104 */     this.buffer.write(b, this.outputStream);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/IdentityOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */