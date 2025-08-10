/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.HttpRequestFactory;
/*     */ import org.apache.hc.core5.http.MessageConstraintException;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
/*     */ import org.apache.hc.core5.http.RequestHeaderFieldsTooLargeException;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.io.SessionInputBuffer;
/*     */ import org.apache.hc.core5.http.message.LineParser;
/*     */ import org.apache.hc.core5.http.message.RequestLine;
/*     */ import org.apache.hc.core5.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultHttpRequestParser
/*     */   extends AbstractMessageParser<ClassicHttpRequest>
/*     */ {
/*     */   private final HttpRequestFactory<ClassicHttpRequest> requestFactory;
/*     */   
/*     */   public DefaultHttpRequestParser(LineParser lineParser, HttpRequestFactory<ClassicHttpRequest> requestFactory, Http1Config http1Config) {
/*  70 */     super(lineParser, http1Config);
/*  71 */     this.requestFactory = (requestFactory != null) ? requestFactory : DefaultClassicHttpRequestFactory.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpRequestParser(Http1Config http1Config) {
/*  78 */     this(null, null, http1Config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpRequestParser() {
/*  85 */     this(Http1Config.DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassicHttpRequest parse(SessionInputBuffer buffer, InputStream inputStream) throws IOException, HttpException {
/*     */     try {
/*  92 */       return super.parse(buffer, inputStream);
/*  93 */     } catch (MessageConstraintException ex) {
/*  94 */       throw new RequestHeaderFieldsTooLargeException(ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClassicHttpRequest createMessage(CharArrayBuffer buffer) throws IOException, HttpException {
/* 100 */     RequestLine requestLine = getLineParser().parseRequestLine(buffer);
/* 101 */     ClassicHttpRequest request = (ClassicHttpRequest)this.requestFactory.newHttpRequest(requestLine.getMethod(), requestLine.getUri());
/* 102 */     request.setVersion(requestLine.getProtocolVersion());
/* 103 */     return request;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/DefaultHttpRequestParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */