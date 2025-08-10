/*    */ package org.apache.hc.client5.http.entity.mime;
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
/*    */ public class MultipartPart
/*    */ {
/*    */   private final Header header;
/*    */   private final ContentBody body;
/*    */   
/*    */   MultipartPart(ContentBody body, Header header) {
/* 44 */     this.body = body;
/* 45 */     this.header = (header != null) ? header : new Header();
/*    */   }
/*    */   
/*    */   public ContentBody getBody() {
/* 49 */     return this.body;
/*    */   }
/*    */   
/*    */   public Header getHeader() {
/* 53 */     return this.header;
/*    */   }
/*    */   
/*    */   void addField(String name, String value) {
/* 57 */     addField(new MimeField(name, value));
/*    */   }
/*    */   
/*    */   void addField(MimeField field) {
/* 61 */     this.header.addField(field);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/MultipartPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */