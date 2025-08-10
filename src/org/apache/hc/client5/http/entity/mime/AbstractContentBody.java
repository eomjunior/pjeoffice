/*    */ package org.apache.hc.client5.http.entity.mime;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import org.apache.hc.core5.http.ContentType;
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
/*    */ public abstract class AbstractContentBody
/*    */   implements ContentBody
/*    */ {
/*    */   private final ContentType contentType;
/*    */   
/*    */   public AbstractContentBody(ContentType contentType) {
/* 48 */     Args.notNull(contentType, "Content type");
/* 49 */     this.contentType = contentType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ContentType getContentType() {
/* 56 */     return this.contentType;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMimeType() {
/* 61 */     return this.contentType.getMimeType();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMediaType() {
/* 66 */     String mimeType = this.contentType.getMimeType();
/* 67 */     int i = mimeType.indexOf('/');
/* 68 */     if (i != -1) {
/* 69 */       return mimeType.substring(0, i);
/*    */     }
/* 71 */     return mimeType;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSubType() {
/* 76 */     String mimeType = this.contentType.getMimeType();
/* 77 */     int i = mimeType.indexOf('/');
/* 78 */     if (i != -1) {
/* 79 */       return mimeType.substring(i + 1);
/*    */     }
/* 81 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCharset() {
/* 86 */     Charset charset = this.contentType.getCharset();
/* 87 */     return (charset != null) ? charset.name() : null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/AbstractContentBody.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */