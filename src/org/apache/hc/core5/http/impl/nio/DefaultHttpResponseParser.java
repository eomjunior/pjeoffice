/*    */ package org.apache.hc.core5.http.impl.nio;
/*    */ 
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpMessage;
/*    */ import org.apache.hc.core5.http.HttpResponse;
/*    */ import org.apache.hc.core5.http.HttpResponseFactory;
/*    */ import org.apache.hc.core5.http.config.Http1Config;
/*    */ import org.apache.hc.core5.http.message.LineParser;
/*    */ import org.apache.hc.core5.http.message.StatusLine;
/*    */ import org.apache.hc.core5.util.Args;
/*    */ import org.apache.hc.core5.util.CharArrayBuffer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultHttpResponseParser<T extends HttpResponse>
/*    */   extends AbstractMessageParser<T>
/*    */ {
/*    */   private final HttpResponseFactory<T> responseFactory;
/*    */   
/*    */   public DefaultHttpResponseParser(HttpResponseFactory<T> responseFactory, LineParser parser, Http1Config http1Config) {
/* 63 */     super(parser, http1Config);
/* 64 */     this.responseFactory = (HttpResponseFactory<T>)Args.notNull(responseFactory, "Response factory");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultHttpResponseParser(HttpResponseFactory<T> responseFactory, Http1Config http1Config) {
/* 71 */     this(responseFactory, null, http1Config);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultHttpResponseParser(HttpResponseFactory<T> responseFactory) {
/* 78 */     this(responseFactory, null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected T createMessage(CharArrayBuffer buffer) throws HttpException {
/* 83 */     StatusLine statusLine = getLineParser().parseStatusLine(buffer);
/* 84 */     HttpResponse httpResponse = this.responseFactory.newHttpResponse(statusLine.getStatusCode(), statusLine.getReasonPhrase());
/* 85 */     httpResponse.setVersion(statusLine.getProtocolVersion());
/* 86 */     return (T)httpResponse;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/DefaultHttpResponseParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */