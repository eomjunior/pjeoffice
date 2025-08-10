/*    */ package org.apache.hc.core5.http.impl.io;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.HttpResponse;
/*    */ import org.apache.hc.core5.http.HttpResponseFactory;
/*    */ import org.apache.hc.core5.http.ReasonPhraseCatalog;
/*    */ import org.apache.hc.core5.http.impl.EnglishReasonPhraseCatalog;
/*    */ import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*    */ public class DefaultClassicHttpResponseFactory
/*    */   implements HttpResponseFactory<ClassicHttpResponse>
/*    */ {
/* 47 */   public static final DefaultClassicHttpResponseFactory INSTANCE = new DefaultClassicHttpResponseFactory();
/*    */ 
/*    */ 
/*    */   
/*    */   private final ReasonPhraseCatalog reasonCatalog;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultClassicHttpResponseFactory(ReasonPhraseCatalog catalog) {
/* 57 */     this.reasonCatalog = (ReasonPhraseCatalog)Args.notNull(catalog, "Reason phrase catalog");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultClassicHttpResponseFactory() {
/* 65 */     this((ReasonPhraseCatalog)EnglishReasonPhraseCatalog.INSTANCE);
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassicHttpResponse newHttpResponse(int status, String reasonPhrase) {
/* 70 */     return (ClassicHttpResponse)new BasicClassicHttpResponse(status, reasonPhrase);
/*    */   }
/*    */ 
/*    */   
/*    */   public ClassicHttpResponse newHttpResponse(int status) {
/* 75 */     return (ClassicHttpResponse)new BasicClassicHttpResponse(status, this.reasonCatalog, null);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/DefaultClassicHttpResponseFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */