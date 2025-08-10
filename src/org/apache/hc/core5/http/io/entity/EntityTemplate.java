/*    */ package org.apache.hc.core5.http.io.entity;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.ContentType;
/*    */ import org.apache.hc.core5.io.IOCallback;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*    */ public final class EntityTemplate
/*    */   extends AbstractHttpEntity
/*    */ {
/*    */   private final long contentLength;
/*    */   private final IOCallback<OutputStream> callback;
/*    */   
/*    */   public EntityTemplate(long contentLength, ContentType contentType, String contentEncoding, IOCallback<OutputStream> callback) {
/* 57 */     super(contentType, contentEncoding);
/* 58 */     this.contentLength = contentLength;
/* 59 */     this.callback = (IOCallback<OutputStream>)Args.notNull(callback, "I/O callback");
/*    */   }
/*    */ 
/*    */   
/*    */   public long getContentLength() {
/* 64 */     return this.contentLength;
/*    */   }
/*    */ 
/*    */   
/*    */   public InputStream getContent() throws IOException {
/* 69 */     ByteArrayOutputStream buf = new ByteArrayOutputStream();
/* 70 */     writeTo(buf);
/* 71 */     return new ByteArrayInputStream(buf.toByteArray());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isRepeatable() {
/* 76 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeTo(OutputStream outStream) throws IOException {
/* 81 */     Args.notNull(outStream, "Output stream");
/* 82 */     this.callback.execute(outStream);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isStreaming() {
/* 87 */     return false;
/*    */   }
/*    */   
/*    */   public void close() throws IOException {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/entity/EntityTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */