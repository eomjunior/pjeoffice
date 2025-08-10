/*    */ package org.apache.hc.core5.http.impl;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.http.MessageHeaders;
/*    */ import org.apache.hc.core5.http.message.MessageSupport;
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
/*    */ @Internal
/*    */ public class IncomingEntityDetails
/*    */   implements EntityDetails
/*    */ {
/*    */   private final MessageHeaders message;
/*    */   private final long contentLength;
/*    */   
/*    */   public IncomingEntityDetails(MessageHeaders message, long contentLength) {
/* 53 */     this.message = (MessageHeaders)Args.notNull(message, "Message");
/* 54 */     this.contentLength = contentLength;
/*    */   }
/*    */   
/*    */   public IncomingEntityDetails(MessageHeaders message) {
/* 58 */     this(message, -1L);
/*    */   }
/*    */ 
/*    */   
/*    */   public long getContentLength() {
/* 63 */     return this.contentLength;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getContentType() {
/* 68 */     Header h = this.message.getFirstHeader("Content-Type");
/* 69 */     return (h != null) ? h.getValue() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getContentEncoding() {
/* 74 */     Header h = this.message.getFirstHeader("Content-Encoding");
/* 75 */     return (h != null) ? h.getValue() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isChunked() {
/* 80 */     return (this.contentLength < 0L);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<String> getTrailerNames() {
/* 85 */     Header h = this.message.getFirstHeader("Trailer");
/* 86 */     if (h == null) {
/* 87 */       return Collections.emptySet();
/*    */     }
/* 89 */     return MessageSupport.parseTokens(h);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/IncomingEntityDetails.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */