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
/*     */ import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
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
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public class HttpServerExpectationFilter
/*     */   implements HttpFilterHandler
/*     */ {
/*     */   protected boolean verify(ClassicHttpRequest request, HttpContext context) throws HttpException {
/*  65 */     return true;
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
/*     */   protected HttpEntity generateResponseContent(HttpResponse expectationFailed) throws HttpException {
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void handle(ClassicHttpRequest request, HttpFilterChain.ResponseTrigger responseTrigger, HttpContext context, HttpFilterChain chain) throws HttpException, IOException {
/*  86 */     Header expect = request.getFirstHeader("Expect");
/*  87 */     boolean expectContinue = (expect != null && "100-continue".equalsIgnoreCase(expect.getValue()));
/*  88 */     if (expectContinue) {
/*  89 */       boolean verified = verify(request, context);
/*  90 */       if (verified) {
/*  91 */         responseTrigger.sendInformation((ClassicHttpResponse)new BasicClassicHttpResponse(100));
/*     */       } else {
/*  93 */         BasicClassicHttpResponse basicClassicHttpResponse = new BasicClassicHttpResponse(417);
/*  94 */         HttpEntity responseContent = generateResponseContent((HttpResponse)basicClassicHttpResponse);
/*  95 */         basicClassicHttpResponse.setEntity(responseContent);
/*  96 */         responseTrigger.submitResponse((ClassicHttpResponse)basicClassicHttpResponse);
/*     */         return;
/*     */       } 
/*     */     } 
/* 100 */     chain.proceed(request, responseTrigger, context);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/support/HttpServerExpectationFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */