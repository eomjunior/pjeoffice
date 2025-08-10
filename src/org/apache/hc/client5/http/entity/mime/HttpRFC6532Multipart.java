/*    */ package org.apache.hc.client5.http.entity.mime;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.StandardCharsets;
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
/*    */ class HttpRFC6532Multipart
/*    */   extends AbstractMultipartFormat
/*    */ {
/*    */   private final List<MultipartPart> parts;
/*    */   
/*    */   public HttpRFC6532Multipart(Charset charset, String boundary, List<MultipartPart> parts) {
/* 44 */     super(charset, boundary);
/* 45 */     this.parts = parts;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<MultipartPart> getParts() {
/* 50 */     return this.parts;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void formatMultipartHeader(MultipartPart part, OutputStream out) throws IOException {
/* 59 */     Header header = part.getHeader();
/* 60 */     for (MimeField field : header)
/* 61 */       writeField(field, StandardCharsets.UTF_8, out); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/HttpRFC6532Multipart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */