/*    */ package org.apache.hc.core5.http.impl.nio;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpRequestFactory;
/*    */ import org.apache.hc.core5.http.config.Http1Config;
/*    */ import org.apache.hc.core5.http.message.LazyLineParser;
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
/*    */ public class DefaultHttpRequestParserFactory
/*    */   implements NHttpMessageParserFactory<HttpRequest>
/*    */ {
/* 48 */   public static final DefaultHttpRequestParserFactory INSTANCE = new DefaultHttpRequestParserFactory();
/*    */ 
/*    */   
/*    */   private final Http1Config http1Config;
/*    */   
/*    */   private final LineParser lineParser;
/*    */   
/*    */   private final HttpRequestFactory<HttpRequest> requestFactory;
/*    */ 
/*    */   
/*    */   public DefaultHttpRequestParserFactory(Http1Config http1Config, HttpRequestFactory<HttpRequest> requestFactory, LineParser lineParser) {
/* 59 */     this.http1Config = (http1Config != null) ? http1Config : Http1Config.DEFAULT;
/* 60 */     this.requestFactory = (requestFactory != null) ? requestFactory : DefaultHttpRequestFactory.INSTANCE;
/* 61 */     this.lineParser = (lineParser != null) ? lineParser : (LineParser)LazyLineParser.INSTANCE;
/*    */   }
/*    */   
/*    */   public DefaultHttpRequestParserFactory(Http1Config http1Config) {
/* 65 */     this(http1Config, null, null);
/*    */   }
/*    */   
/*    */   public DefaultHttpRequestParserFactory() {
/* 69 */     this(null);
/*    */   }
/*    */ 
/*    */   
/*    */   public NHttpMessageParser<HttpRequest> create() {
/* 74 */     return new DefaultHttpRequestParser<>(this.requestFactory, this.lineParser, this.http1Config);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/DefaultHttpRequestParserFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */