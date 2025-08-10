/*     */ package org.apache.hc.client5.http.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.io.HttpClientResponseHandler;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
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
/*     */ public interface HttpClient
/*     */ {
/*     */   @Deprecated
/*     */   HttpResponse execute(ClassicHttpRequest paramClassicHttpRequest) throws IOException;
/*     */   
/*     */   @Deprecated
/*     */   HttpResponse execute(ClassicHttpRequest paramClassicHttpRequest, HttpContext paramHttpContext) throws IOException;
/*     */   
/*     */   @Deprecated
/*     */   ClassicHttpResponse execute(HttpHost paramHttpHost, ClassicHttpRequest paramClassicHttpRequest) throws IOException;
/*     */   
/*     */   @Deprecated
/*     */   HttpResponse execute(HttpHost paramHttpHost, ClassicHttpRequest paramClassicHttpRequest, HttpContext paramHttpContext) throws IOException;
/*     */   
/*     */   default ClassicHttpResponse executeOpen(HttpHost target, ClassicHttpRequest request, HttpContext context) throws IOException {
/* 183 */     return (ClassicHttpResponse)execute(target, request, context);
/*     */   }
/*     */   
/*     */   <T> T execute(ClassicHttpRequest paramClassicHttpRequest, HttpClientResponseHandler<? extends T> paramHttpClientResponseHandler) throws IOException;
/*     */   
/*     */   <T> T execute(ClassicHttpRequest paramClassicHttpRequest, HttpContext paramHttpContext, HttpClientResponseHandler<? extends T> paramHttpClientResponseHandler) throws IOException;
/*     */   
/*     */   <T> T execute(HttpHost paramHttpHost, ClassicHttpRequest paramClassicHttpRequest, HttpClientResponseHandler<? extends T> paramHttpClientResponseHandler) throws IOException;
/*     */   
/*     */   <T> T execute(HttpHost paramHttpHost, ClassicHttpRequest paramClassicHttpRequest, HttpContext paramHttpContext, HttpClientResponseHandler<? extends T> paramHttpClientResponseHandler) throws IOException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/classic/HttpClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */