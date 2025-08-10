/*    */ package org.apache.hc.client5.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import org.apache.hc.client5.http.impl.Wire;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class LoggingOutputStream
/*    */   extends OutputStream
/*    */ {
/*    */   private final OutputStream out;
/*    */   private final Wire wire;
/*    */   
/*    */   public LoggingOutputStream(OutputStream out, Wire wire) {
/* 42 */     this.out = out;
/* 43 */     this.wire = wire;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int b) throws IOException {
/*    */     try {
/* 49 */       this.out.write(b);
/* 50 */       this.wire.output(b);
/* 51 */     } catch (IOException ex) {
/* 52 */       this.wire.output("[write] I/O error: " + ex.getMessage());
/* 53 */       throw ex;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b) throws IOException {
/*    */     try {
/* 60 */       this.wire.output(b);
/* 61 */       this.out.write(b);
/* 62 */     } catch (IOException ex) {
/* 63 */       this.wire.output("[write] I/O error: " + ex.getMessage());
/* 64 */       throw ex;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b, int off, int len) throws IOException {
/*    */     try {
/* 71 */       this.wire.output(b, off, len);
/* 72 */       this.out.write(b, off, len);
/* 73 */     } catch (IOException ex) {
/* 74 */       this.wire.output("[write] I/O error: " + ex.getMessage());
/* 75 */       throw ex;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void flush() throws IOException {
/*    */     try {
/* 82 */       this.out.flush();
/* 83 */     } catch (IOException ex) {
/* 84 */       this.wire.output("[flush] I/O error: " + ex.getMessage());
/* 85 */       throw ex;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/*    */     try {
/* 92 */       this.out.close();
/* 93 */     } catch (IOException ex) {
/* 94 */       this.wire.output("[close] I/O error: " + ex.getMessage());
/* 95 */       throw ex;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/io/LoggingOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */