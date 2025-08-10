/*     */ package org.apache.hc.core5.http.nio.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.message.BasicHttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncDataConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncFilterChain;
/*     */ import org.apache.hc.core5.http.nio.AsyncFilterHandler;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.entity.AsyncEntityProducers;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.net.URIAuthority;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public abstract class AbstractAsyncServerAuthFilter<T>
/*     */   implements AsyncFilterHandler
/*     */ {
/*     */   private final boolean respondImmediately;
/*     */   
/*     */   protected AbstractAsyncServerAuthFilter(boolean respondImmediately) {
/*  67 */     this.respondImmediately = respondImmediately;
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
/*     */   protected abstract T parseChallengeResponse(String paramString, HttpContext paramHttpContext) throws HttpException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean authenticate(T paramT, URIAuthority paramURIAuthority, String paramString, HttpContext paramHttpContext);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract String generateChallenge(T paramT, URIAuthority paramURIAuthority, String paramString, HttpContext paramHttpContext);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AsyncEntityProducer generateResponseContent(HttpResponse unauthorized) {
/* 113 */     return AsyncEntityProducers.create("Unauthorized");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncDataConsumer handle(HttpRequest request, EntityDetails entityDetails, HttpContext context, final AsyncFilterChain.ResponseTrigger responseTrigger, AsyncFilterChain chain) throws HttpException, IOException {
/* 123 */     Header h = request.getFirstHeader("Authorization");
/* 124 */     T challengeResponse = (h != null) ? parseChallengeResponse(h.getValue(), context) : null;
/*     */     
/* 126 */     URIAuthority authority = request.getAuthority();
/* 127 */     String requestUri = request.getRequestUri();
/*     */     
/* 129 */     boolean authenticated = authenticate(challengeResponse, authority, requestUri, context);
/* 130 */     Header expect = request.getFirstHeader("Expect");
/* 131 */     boolean expectContinue = (expect != null && "100-continue".equalsIgnoreCase(expect.getValue()));
/*     */     
/* 133 */     if (authenticated) {
/* 134 */       if (expectContinue) {
/* 135 */         responseTrigger.sendInformation((HttpResponse)new BasicClassicHttpResponse(100));
/*     */       }
/* 137 */       return chain.proceed(request, entityDetails, context, responseTrigger);
/*     */     } 
/* 139 */     final BasicHttpResponse unauthorized = new BasicHttpResponse(401);
/* 140 */     basicHttpResponse.addHeader("WWW-Authenticate", generateChallenge(challengeResponse, authority, requestUri, context));
/* 141 */     final AsyncEntityProducer responseContentProducer = generateResponseContent((HttpResponse)basicHttpResponse);
/* 142 */     if (this.respondImmediately || expectContinue || entityDetails == null) {
/* 143 */       responseTrigger.submitResponse((HttpResponse)basicHttpResponse, responseContentProducer);
/* 144 */       return null;
/*     */     } 
/* 146 */     return new AsyncDataConsumer()
/*     */       {
/*     */         public void updateCapacity(CapacityChannel capacityChannel) throws IOException
/*     */         {
/* 150 */           capacityChannel.update(2147483647);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void consume(ByteBuffer src) throws IOException {}
/*     */ 
/*     */         
/*     */         public void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 159 */           responseTrigger.submitResponse(unauthorized, responseContentProducer);
/*     */         }
/*     */ 
/*     */         
/*     */         public void releaseResources() {
/* 164 */           if (responseContentProducer != null)
/* 165 */             responseContentProducer.releaseResources(); 
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/AbstractAsyncServerAuthFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */