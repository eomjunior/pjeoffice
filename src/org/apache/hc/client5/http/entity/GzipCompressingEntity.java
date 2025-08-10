/*    */ package org.apache.hc.client5.http.entity;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.util.zip.GZIPOutputStream;
/*    */ import org.apache.hc.core5.http.HttpEntity;
/*    */ import org.apache.hc.core5.http.io.entity.HttpEntityWrapper;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GzipCompressingEntity
/*    */   extends HttpEntityWrapper
/*    */ {
/*    */   private static final String GZIP_CODEC = "gzip";
/*    */   
/*    */   public GzipCompressingEntity(HttpEntity entity) {
/* 49 */     super(entity);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getContentEncoding() {
/* 54 */     return "gzip";
/*    */   }
/*    */ 
/*    */   
/*    */   public long getContentLength() {
/* 59 */     return -1L;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isChunked() {
/* 65 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getContent() throws IOException {
/* 70 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeTo(OutputStream outStream) throws IOException {
/* 75 */     Args.notNull(outStream, "Output stream");
/* 76 */     GZIPOutputStream gzip = new GZIPOutputStream(outStream);
/* 77 */     super.writeTo(gzip);
/*    */ 
/*    */     
/* 80 */     gzip.close();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/GzipCompressingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */