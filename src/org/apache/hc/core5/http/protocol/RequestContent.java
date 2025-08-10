/*     */ package org.apache.hc.core5.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.message.MessageSupport;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class RequestContent
/*     */   implements HttpRequestInterceptor
/*     */ {
/*  64 */   public static final HttpRequestInterceptor INSTANCE = new RequestContent();
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean overwrite;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestContent() {
/*  74 */     this(false);
/*     */   }
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
/*     */   public RequestContent(boolean overwrite) {
/*  90 */     this.overwrite = overwrite;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/*  96 */     Args.notNull(request, "HTTP request");
/*  97 */     String method = request.getMethod();
/*  98 */     if (Method.TRACE.isSame(method) && entity != null) {
/*  99 */       throw new ProtocolException("TRACE request may not enclose an entity");
/*     */     }
/* 101 */     if (this.overwrite) {
/* 102 */       request.removeHeaders("Transfer-Encoding");
/* 103 */       request.removeHeaders("Content-Length");
/*     */     } else {
/* 105 */       if (request.containsHeader("Transfer-Encoding")) {
/* 106 */         throw new ProtocolException("Transfer-encoding header already present");
/*     */       }
/* 108 */       if (request.containsHeader("Content-Length")) {
/* 109 */         throw new ProtocolException("Content-Length header already present");
/*     */       }
/*     */     } 
/* 112 */     if (entity != null) {
/* 113 */       ProtocolVersion ver = context.getProtocolVersion();
/*     */       
/* 115 */       if (entity.isChunked() || entity.getContentLength() < 0L) {
/* 116 */         if (ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/* 117 */           throw new ProtocolException("Chunked transfer encoding not allowed for " + ver);
/*     */         }
/*     */         
/* 120 */         request.addHeader("Transfer-Encoding", "chunked");
/* 121 */         MessageSupport.addTrailerHeader((HttpMessage)request, entity);
/*     */       } else {
/* 123 */         request.addHeader("Content-Length", Long.toString(entity.getContentLength()));
/*     */       } 
/* 125 */       MessageSupport.addContentTypeHeader((HttpMessage)request, entity);
/* 126 */       MessageSupport.addContentEncodingHeader((HttpMessage)request, entity);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/RequestContent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */