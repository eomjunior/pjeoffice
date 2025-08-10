/*    */ package org.apache.hc.core5.http.impl.nio;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.HttpResponse;
/*    */ import org.apache.hc.core5.http.HttpResponseFactory;
/*    */ import org.apache.hc.core5.http.ReasonPhraseCatalog;
/*    */ import org.apache.hc.core5.http.impl.EnglishReasonPhraseCatalog;
/*    */ import org.apache.hc.core5.http.message.BasicHttpResponse;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*    */ public class DefaultHttpResponseFactory
/*    */   implements HttpResponseFactory<HttpResponse>
/*    */ {
/* 47 */   public static final DefaultHttpResponseFactory INSTANCE = new DefaultHttpResponseFactory();
/*    */ 
/*    */ 
/*    */   
/*    */   private final ReasonPhraseCatalog reasonCatalog;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultHttpResponseFactory(ReasonPhraseCatalog catalog) {
/* 57 */     this.reasonCatalog = (ReasonPhraseCatalog)Args.notNull(catalog, "Reason phrase catalog");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultHttpResponseFactory() {
/* 65 */     this((ReasonPhraseCatalog)EnglishReasonPhraseCatalog.INSTANCE);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpResponse newHttpResponse(int status, String reasonPhrase) {
/* 70 */     return (HttpResponse)new BasicHttpResponse(status, reasonPhrase);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpResponse newHttpResponse(int status) {
/* 75 */     return (HttpResponse)new BasicHttpResponse(status, this.reasonCatalog, null);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/DefaultHttpResponseFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */