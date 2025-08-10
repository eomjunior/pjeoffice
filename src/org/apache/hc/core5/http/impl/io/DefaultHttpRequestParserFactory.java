/*    */ package org.apache.hc.core5.http.impl.io;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*    */ import org.apache.hc.core5.http.HttpRequestFactory;
/*    */ import org.apache.hc.core5.http.config.Http1Config;
/*    */ import org.apache.hc.core5.http.io.HttpMessageParser;
/*    */ import org.apache.hc.core5.http.io.HttpMessageParserFactory;
/*    */ import org.apache.hc.core5.http.message.LazyLineParser;
/*    */ import org.apache.hc.core5.http.message.LineParser;
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
/*    */   implements HttpMessageParserFactory<ClassicHttpRequest>
/*    */ {
/* 48 */   public static final DefaultHttpRequestParserFactory INSTANCE = new DefaultHttpRequestParserFactory();
/*    */   
/*    */   private final LineParser lineParser;
/*    */   
/*    */   private final HttpRequestFactory<ClassicHttpRequest> requestFactory;
/*    */ 
/*    */   
/*    */   public DefaultHttpRequestParserFactory(LineParser lineParser, HttpRequestFactory<ClassicHttpRequest> requestFactory) {
/* 56 */     this.lineParser = (lineParser != null) ? lineParser : (LineParser)LazyLineParser.INSTANCE;
/* 57 */     this.requestFactory = (requestFactory != null) ? requestFactory : DefaultClassicHttpRequestFactory.INSTANCE;
/*    */   }
/*    */   
/*    */   public DefaultHttpRequestParserFactory() {
/* 61 */     this(null, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpMessageParser<ClassicHttpRequest> create(Http1Config http1Config) {
/* 66 */     return new DefaultHttpRequestParser(this.lineParser, this.requestFactory, http1Config);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/DefaultHttpRequestParserFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */