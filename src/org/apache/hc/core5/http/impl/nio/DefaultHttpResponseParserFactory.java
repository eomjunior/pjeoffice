/*    */ package org.apache.hc.core5.http.impl.nio;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.HttpResponse;
/*    */ import org.apache.hc.core5.http.HttpResponseFactory;
/*    */ import org.apache.hc.core5.http.config.Http1Config;
/*    */ import org.apache.hc.core5.http.message.LazyLaxLineParser;
/*    */ import org.apache.hc.core5.http.message.LineParser;
/*    */ import org.apache.hc.core5.http.nio.NHttpMessageParser;
/*    */ import org.apache.hc.core5.http.nio.NHttpMessageParserFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ public class DefaultHttpResponseParserFactory
/*    */   implements NHttpMessageParserFactory<HttpResponse>
/*    */ {
/* 48 */   public static final DefaultHttpResponseParserFactory INSTANCE = new DefaultHttpResponseParserFactory();
/*    */ 
/*    */   
/*    */   private final Http1Config http1Config;
/*    */   
/*    */   private final HttpResponseFactory<HttpResponse> responseFactory;
/*    */   
/*    */   private final LineParser lineParser;
/*    */ 
/*    */   
/*    */   public DefaultHttpResponseParserFactory(Http1Config http1Config, HttpResponseFactory<HttpResponse> responseFactory, LineParser lineParser) {
/* 59 */     this.http1Config = (http1Config != null) ? http1Config : Http1Config.DEFAULT;
/* 60 */     this.responseFactory = (responseFactory != null) ? responseFactory : DefaultHttpResponseFactory.INSTANCE;
/* 61 */     this.lineParser = (lineParser != null) ? lineParser : (LineParser)LazyLaxLineParser.INSTANCE;
/*    */   }
/*    */   
/*    */   public DefaultHttpResponseParserFactory(Http1Config http1Config) {
/* 65 */     this(http1Config, null, null);
/*    */   }
/*    */   
/*    */   public DefaultHttpResponseParserFactory() {
/* 69 */     this(null);
/*    */   }
/*    */ 
/*    */   
/*    */   public NHttpMessageParser<HttpResponse> create() {
/* 74 */     return new DefaultHttpResponseParser<>(this.responseFactory, this.lineParser, this.http1Config);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/DefaultHttpResponseParserFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */