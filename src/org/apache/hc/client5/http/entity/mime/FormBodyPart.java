/*    */ package org.apache.hc.client5.http.entity.mime;
/*    */ 
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
/*    */ public class FormBodyPart
/*    */   extends MultipartPart
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   FormBodyPart(String name, ContentBody body, Header header) {
/* 44 */     super(body, header);
/* 45 */     Args.notNull(name, "Name");
/* 46 */     Args.notNull(body, "Body");
/* 47 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 51 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addField(String name, String value) {
/* 56 */     Args.notNull(name, "Field name");
/* 57 */     super.addField(name, value);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/FormBodyPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */