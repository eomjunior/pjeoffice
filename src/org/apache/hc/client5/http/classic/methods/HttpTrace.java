/*    */ package org.apache.hc.client5.http.classic.methods;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.apache.hc.core5.http.HttpEntity;
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
/*    */ public class HttpTrace
/*    */   extends HttpUriRequestBase
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public static final String METHOD_NAME = "TRACE";
/*    */   
/*    */   public HttpTrace(URI uri) {
/* 52 */     super("TRACE", uri);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpTrace(String uri) {
/* 62 */     this(URI.create(uri));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setEntity(HttpEntity entity) {
/* 67 */     throw new IllegalStateException("TRACE requests may not include an entity.");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/classic/methods/HttpTrace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */