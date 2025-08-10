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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class LegacyMultipart
/*    */   extends AbstractMultipartFormat
/*    */ {
/*    */   private final List<MultipartPart> parts;
/*    */   
/*    */   public LegacyMultipart(Charset charset, String boundary, List<MultipartPart> parts) {
/* 49 */     super(charset, boundary);
/* 50 */     this.parts = parts;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<MultipartPart> getParts() {
/* 55 */     return this.parts;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void formatMultipartHeader(MultipartPart part, OutputStream out) throws IOException {
/* 67 */     Header header = part.getHeader();
/* 68 */     MimeField cd = header.getField("Content-Disposition");
/* 69 */     if (cd != null) {
/* 70 */       writeField(cd, this.charset, out);
/*    */     }
/* 72 */     String filename = part.getBody().getFilename();
/* 73 */     if (filename != null) {
/* 74 */       MimeField ct = header.getField("Content-Type");
/* 75 */       writeField(ct, this.charset, out);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/LegacyMultipart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */