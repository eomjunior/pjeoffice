/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpRequestFactory;
/*     */ import org.apache.hc.core5.http.MessageConstraintException;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
/*     */ import org.apache.hc.core5.http.RequestHeaderFieldsTooLargeException;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.message.LineParser;
/*     */ import org.apache.hc.core5.http.message.RequestLine;
/*     */ import org.apache.hc.core5.http.nio.SessionInputBuffer;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ public class DefaultHttpRequestParser<T extends HttpRequest>
/*     */   extends AbstractMessageParser<T>
/*     */ {
/*     */   private final HttpRequestFactory<T> requestFactory;
/*     */   
/*     */   public DefaultHttpRequestParser(HttpRequestFactory<T> requestFactory, LineParser parser, Http1Config http1Config) {
/*  68 */     super(parser, http1Config);
/*  69 */     this.requestFactory = (HttpRequestFactory<T>)Args.notNull(requestFactory, "Request factory");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpRequestParser(HttpRequestFactory<T> requestFactory, Http1Config http1Config) {
/*  76 */     this(requestFactory, null, http1Config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpRequestParser(HttpRequestFactory<T> requestFactory) {
/*  83 */     this(requestFactory, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public T parse(SessionInputBuffer sessionBuffer, boolean endOfStream) throws IOException, HttpException {
/*     */     try {
/*  89 */       return super.parse(sessionBuffer, endOfStream);
/*  90 */     } catch (MessageConstraintException ex) {
/*  91 */       throw new RequestHeaderFieldsTooLargeException(ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected T createMessage(CharArrayBuffer buffer) throws HttpException {
/*  97 */     RequestLine requestLine = getLineParser().parseRequestLine(buffer);
/*  98 */     HttpRequest httpRequest = this.requestFactory.newHttpRequest(requestLine.getMethod(), requestLine.getUri());
/*  99 */     httpRequest.setVersion(requestLine.getProtocolVersion());
/* 100 */     return (T)httpRequest;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/DefaultHttpRequestParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */