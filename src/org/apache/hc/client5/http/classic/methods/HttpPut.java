/*    */ package org.apache.hc.client5.http.classic.methods;
/*    */ 
/*    */ import java.net.URI;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpPut
/*    */   extends HttpUriRequestBase
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public static final String METHOD_NAME = "PUT";
/*    */   
/*    */   public HttpPut(URI uri) {
/* 49 */     super("PUT", uri);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpPut(String uri) {
/* 59 */     this(URI.create(uri));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/classic/methods/HttpPut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */