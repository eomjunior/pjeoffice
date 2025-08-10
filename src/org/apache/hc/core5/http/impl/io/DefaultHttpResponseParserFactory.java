/*    */ package org.apache.hc.core5.http.impl.io;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.HttpResponseFactory;
/*    */ import org.apache.hc.core5.http.config.Http1Config;
/*    */ import org.apache.hc.core5.http.io.HttpMessageParser;
/*    */ import org.apache.hc.core5.http.io.HttpMessageParserFactory;
/*    */ import org.apache.hc.core5.http.message.LazyLaxLineParser;
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
/*    */ public class DefaultHttpResponseParserFactory
/*    */   implements HttpMessageParserFactory<ClassicHttpResponse>
/*    */ {
/* 48 */   public static final DefaultHttpResponseParserFactory INSTANCE = new DefaultHttpResponseParserFactory();
/*    */   
/*    */   private final LineParser lineParser;
/*    */   
/*    */   private final HttpResponseFactory<ClassicHttpResponse> responseFactory;
/*    */ 
/*    */   
/*    */   public DefaultHttpResponseParserFactory(LineParser lineParser, HttpResponseFactory<ClassicHttpResponse> responseFactory) {
/* 56 */     this.lineParser = (lineParser != null) ? lineParser : (LineParser)LazyLaxLineParser.INSTANCE;
/* 57 */     this.responseFactory = (responseFactory != null) ? responseFactory : DefaultClassicHttpResponseFactory.INSTANCE;
/*    */   }
/*    */   
/*    */   public DefaultHttpResponseParserFactory() {
/* 61 */     this(null, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpMessageParser<ClassicHttpResponse> create(Http1Config http1Config) {
/* 66 */     return new DefaultHttpResponseParser(this.lineParser, this.responseFactory, http1Config);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/DefaultHttpResponseParserFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */