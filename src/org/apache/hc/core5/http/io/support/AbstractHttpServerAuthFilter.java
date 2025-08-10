/*     */ package org.apache.hc.core5.http.io.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.io.HttpFilterChain;
/*     */ import org.apache.hc.core5.http.io.HttpFilterHandler;
/*     */ import org.apache.hc.core5.http.io.entity.EntityUtils;
/*     */ import org.apache.hc.core5.http.io.entity.StringEntity;
/*     */ import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
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
/*     */ public abstract class AbstractHttpServerAuthFilter<T>
/*     */   implements HttpFilterHandler
/*     */ {
/*     */   private final boolean respondImmediately;
/*     */   
/*     */   protected AbstractHttpServerAuthFilter(boolean respondImmediately) {
/*  63 */     this.respondImmediately = respondImmediately;
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
/*     */   protected HttpEntity generateResponseContent(HttpResponse unauthorized) {
/* 109 */     return (HttpEntity)new StringEntity("Unauthorized");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void handle(ClassicHttpRequest request, HttpFilterChain.ResponseTrigger responseTrigger, HttpContext context, HttpFilterChain chain) throws HttpException, IOException {
/* 118 */     Header h = request.getFirstHeader("Authorization");
/* 119 */     T challengeResponse = (h != null) ? parseChallengeResponse(h.getValue(), context) : null;
/*     */     
/* 121 */     URIAuthority authority = request.getAuthority();
/* 122 */     String requestUri = request.getRequestUri();
/*     */     
/* 124 */     boolean authenticated = authenticate(challengeResponse, authority, requestUri, context);
/* 125 */     Header expect = request.getFirstHeader("Expect");
/* 126 */     boolean expectContinue = (expect != null && "100-continue".equalsIgnoreCase(expect.getValue()));
/*     */     
/* 128 */     if (authenticated) {
/* 129 */       if (expectContinue) {
/* 130 */         responseTrigger.sendInformation((ClassicHttpResponse)new BasicClassicHttpResponse(100));
/*     */       }
/* 132 */       chain.proceed(request, responseTrigger, context);
/*     */     } else {
/* 134 */       BasicClassicHttpResponse basicClassicHttpResponse = new BasicClassicHttpResponse(401);
/* 135 */       basicClassicHttpResponse.addHeader("WWW-Authenticate", generateChallenge(challengeResponse, authority, requestUri, context));
/* 136 */       HttpEntity responseContent = generateResponseContent((HttpResponse)basicClassicHttpResponse);
/* 137 */       basicClassicHttpResponse.setEntity(responseContent);
/* 138 */       if (this.respondImmediately || expectContinue || request.getEntity() == null) {
/*     */         
/* 140 */         responseTrigger.submitResponse((ClassicHttpResponse)basicClassicHttpResponse);
/*     */         
/* 142 */         EntityUtils.consume(request.getEntity());
/*     */       } else {
/*     */         
/* 145 */         EntityUtils.consume(request.getEntity());
/*     */         
/* 147 */         responseTrigger.submitResponse((ClassicHttpResponse)basicClassicHttpResponse);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/support/AbstractHttpServerAuthFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */