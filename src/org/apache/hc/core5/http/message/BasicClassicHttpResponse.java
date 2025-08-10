/*    */ package org.apache.hc.core5.http.message;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ import java.io.IOException;
/*    */ import java.util.Locale;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.HttpEntity;
/*    */ import org.apache.hc.core5.http.ReasonPhraseCatalog;
/*    */ import org.apache.hc.core5.io.Closer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BasicClassicHttpResponse
/*    */   extends BasicHttpResponse
/*    */   implements ClassicHttpResponse
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private HttpEntity entity;
/*    */   
/*    */   public BasicClassicHttpResponse(int code, ReasonPhraseCatalog catalog, Locale locale) {
/* 59 */     super(code, catalog, locale);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BasicClassicHttpResponse(int code, String reasonPhrase) {
/* 69 */     super(code, reasonPhrase);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BasicClassicHttpResponse(int code) {
/* 78 */     super(code);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpEntity getEntity() {
/* 83 */     return this.entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setEntity(HttpEntity entity) {
/* 88 */     this.entity = entity;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 93 */     Closer.close((Closeable)this.entity);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BasicClassicHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */