/*    */ package org.apache.hc.core5.http.impl;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.apache.hc.core5.http.ContentType;
/*    */ import org.apache.hc.core5.http.EntityDetails;
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
/*    */ public final class BasicEntityDetails
/*    */   implements EntityDetails
/*    */ {
/*    */   private final long len;
/*    */   private final ContentType contentType;
/*    */   
/*    */   public BasicEntityDetails(long len, ContentType contentType) {
/* 46 */     this.len = len;
/* 47 */     this.contentType = contentType;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getContentLength() {
/* 52 */     return this.len;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getContentType() {
/* 57 */     return (this.contentType != null) ? this.contentType.toString() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getContentEncoding() {
/* 62 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isChunked() {
/* 67 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<String> getTrailerNames() {
/* 72 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/BasicEntityDetails.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */