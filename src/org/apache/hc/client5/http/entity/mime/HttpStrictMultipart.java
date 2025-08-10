/*    */ package org.apache.hc.client5.http.entity.mime;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.List;
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
/*    */ class HttpStrictMultipart
/*    */   extends AbstractMultipartFormat
/*    */ {
/*    */   private final List<MultipartPart> parts;
/*    */   
/*    */   public HttpStrictMultipart(Charset charset, String boundary, List<MultipartPart> parts) {
/* 43 */     super(charset, boundary);
/* 44 */     this.parts = parts;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<MultipartPart> getParts() {
/* 49 */     return this.parts;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void formatMultipartHeader(MultipartPart part, OutputStream out) throws IOException {
/* 58 */     Header header = part.getHeader();
/* 59 */     for (MimeField field : header)
/* 60 */       writeField(field, out); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/HttpStrictMultipart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */