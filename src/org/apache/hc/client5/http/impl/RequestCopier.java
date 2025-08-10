/*    */ package org.apache.hc.client5.http.impl;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.http.HttpMessage;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.message.BasicHttpRequest;
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
/*    */ @Deprecated
/*    */ public final class RequestCopier
/*    */   implements MessageCopier<HttpRequest>
/*    */ {
/* 45 */   public static final RequestCopier INSTANCE = new RequestCopier();
/*    */ 
/*    */   
/*    */   public HttpRequest copy(HttpRequest original) {
/* 49 */     if (original == null) {
/* 50 */       return null;
/*    */     }
/* 52 */     BasicHttpRequest copy = new BasicHttpRequest(original.getMethod(), null, original.getPath());
/* 53 */     copy.setScheme(original.getScheme());
/* 54 */     copy.setAuthority(original.getAuthority());
/* 55 */     copy.setVersion(original.getVersion());
/* 56 */     for (Iterator<Header> it = original.headerIterator(); it.hasNext();) {
/* 57 */       copy.addHeader(it.next());
/*    */     }
/* 59 */     return (HttpRequest)copy;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/RequestCopier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */