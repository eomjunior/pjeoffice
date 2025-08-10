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
/*    */ 
/*    */ public class HttpHead
/*    */   extends HttpUriRequestBase
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public static final String METHOD_NAME = "HEAD";
/*    */   
/*    */   public HttpHead(URI uri) {
/* 50 */     super("HEAD", uri);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpHead(String uri) {
/* 58 */     this(URI.create(uri));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/classic/methods/HttpHead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */