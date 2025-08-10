/*     */ package org.apache.hc.core5.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.HttpResponseInterceptor;
/*     */ import org.apache.hc.core5.http.HttpVersion;
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
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class ResponseContent
/*     */   implements HttpResponseInterceptor
/*     */ {
/*     */   private final boolean overwrite;
/*     */   
/*     */   public ResponseContent() {
/*  68 */     this(false);
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
/*     */   public ResponseContent(boolean overwrite) {
/*  84 */     this.overwrite = overwrite;
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
/*     */   public void process(HttpResponse response, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/*  97 */     Args.notNull(response, "HTTP response");
/*  98 */     if (this.overwrite) {
/*  99 */       response.removeHeaders("Transfer-Encoding");
/* 100 */       response.removeHeaders("Content-Length");
/*     */     } else {
/* 102 */       if (response.containsHeader("Transfer-Encoding")) {
/* 103 */         throw new ProtocolException("Transfer-encoding header already present");
/*     */       }
/* 105 */       if (response.containsHeader("Content-Length")) {
/* 106 */         throw new ProtocolException("Content-Length header already present");
/*     */       }
/*     */     } 
/* 109 */     ProtocolVersion ver = context.getProtocolVersion();
/* 110 */     if (entity != null) {
/* 111 */       long len = entity.getContentLength();
/* 112 */       if (len >= 0L && !entity.isChunked()) {
/* 113 */         response.addHeader("Content-Length", Long.toString(entity.getContentLength()));
/* 114 */       } else if (ver.greaterEquals((ProtocolVersion)HttpVersion.HTTP_1_1)) {
/* 115 */         response.addHeader("Transfer-Encoding", "chunked");
/* 116 */         MessageSupport.addTrailerHeader((HttpMessage)response, entity);
/*     */       } 
/* 118 */       MessageSupport.addContentTypeHeader((HttpMessage)response, entity);
/* 119 */       MessageSupport.addContentEncodingHeader((HttpMessage)response, entity);
/*     */     } else {
/* 121 */       int status = response.getCode();
/* 122 */       if (status != 204 && status != 304)
/* 123 */         response.addHeader("Content-Length", "0"); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/ResponseContent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */