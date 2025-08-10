/*    */ package org.apache.hc.client5.http.impl.io;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.HttpResponseFactory;
/*    */ import org.apache.hc.core5.http.config.Http1Config;
/*    */ import org.apache.hc.core5.http.impl.io.DefaultClassicHttpResponseFactory;
/*    */ import org.apache.hc.core5.http.io.HttpMessageParser;
/*    */ import org.apache.hc.core5.http.io.HttpMessageParserFactory;
/*    */ import org.apache.hc.core5.http.message.BasicLineParser;
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
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class DefaultHttpResponseParserFactory
/*    */   implements HttpMessageParserFactory<ClassicHttpResponse>
/*    */ {
/* 49 */   public static final DefaultHttpResponseParserFactory INSTANCE = new DefaultHttpResponseParserFactory();
/*    */ 
/*    */   
/*    */   private final LineParser lineParser;
/*    */   
/*    */   private final HttpResponseFactory<ClassicHttpResponse> responseFactory;
/*    */ 
/*    */   
/*    */   public DefaultHttpResponseParserFactory(LineParser lineParser, HttpResponseFactory<ClassicHttpResponse> responseFactory) {
/* 58 */     this.lineParser = (lineParser != null) ? lineParser : (LineParser)BasicLineParser.INSTANCE;
/* 59 */     this.responseFactory = (responseFactory != null) ? responseFactory : (HttpResponseFactory<ClassicHttpResponse>)DefaultClassicHttpResponseFactory.INSTANCE;
/*    */   }
/*    */   
/*    */   public DefaultHttpResponseParserFactory(HttpResponseFactory<ClassicHttpResponse> responseFactory) {
/* 63 */     this(null, responseFactory);
/*    */   }
/*    */   
/*    */   public DefaultHttpResponseParserFactory() {
/* 67 */     this(null, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpMessageParser<ClassicHttpResponse> create(Http1Config h1Config) {
/* 72 */     return (HttpMessageParser<ClassicHttpResponse>)new LenientHttpResponseParser(this.lineParser, this.responseFactory, h1Config);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/io/DefaultHttpResponseParserFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */