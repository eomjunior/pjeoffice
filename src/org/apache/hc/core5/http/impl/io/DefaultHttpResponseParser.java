/*    */ package org.apache.hc.core5.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpMessage;
/*    */ import org.apache.hc.core5.http.HttpResponseFactory;
/*    */ import org.apache.hc.core5.http.config.Http1Config;
/*    */ import org.apache.hc.core5.http.message.LineParser;
/*    */ import org.apache.hc.core5.http.message.StatusLine;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultHttpResponseParser
/*    */   extends AbstractMessageParser<ClassicHttpResponse>
/*    */ {
/*    */   private final HttpResponseFactory<ClassicHttpResponse> responseFactory;
/*    */   
/*    */   public DefaultHttpResponseParser(LineParser lineParser, HttpResponseFactory<ClassicHttpResponse> responseFactory, Http1Config http1Config) {
/* 66 */     super(lineParser, http1Config);
/* 67 */     this.responseFactory = (responseFactory != null) ? responseFactory : DefaultClassicHttpResponseFactory.INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultHttpResponseParser(Http1Config http1Config) {
/* 74 */     this(null, null, http1Config);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultHttpResponseParser() {
/* 81 */     this(Http1Config.DEFAULT);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ClassicHttpResponse createMessage(CharArrayBuffer buffer) throws IOException, HttpException {
/* 86 */     StatusLine statusline = getLineParser().parseStatusLine(buffer);
/* 87 */     ClassicHttpResponse response = (ClassicHttpResponse)this.responseFactory.newHttpResponse(statusline.getStatusCode(), statusline.getReasonPhrase());
/* 88 */     response.setVersion(statusline.getProtocolVersion());
/* 89 */     return response;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/DefaultHttpResponseParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */