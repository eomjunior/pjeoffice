/*     */ package org.apache.hc.core5.http.impl;
/*     */ 
/*     */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*     */ import org.apache.hc.core5.http.HttpResponseInterceptor;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessorBuilder;
/*     */ import org.apache.hc.core5.http.protocol.RequestConnControl;
/*     */ import org.apache.hc.core5.http.protocol.RequestContent;
/*     */ import org.apache.hc.core5.http.protocol.RequestExpectContinue;
/*     */ import org.apache.hc.core5.http.protocol.RequestTargetHost;
/*     */ import org.apache.hc.core5.http.protocol.RequestUserAgent;
/*     */ import org.apache.hc.core5.http.protocol.RequestValidateHost;
/*     */ import org.apache.hc.core5.http.protocol.ResponseConnControl;
/*     */ import org.apache.hc.core5.http.protocol.ResponseContent;
/*     */ import org.apache.hc.core5.http.protocol.ResponseDate;
/*     */ import org.apache.hc.core5.http.protocol.ResponseServer;
/*     */ import org.apache.hc.core5.util.TextUtils;
/*     */ import org.apache.hc.core5.util.VersionInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HttpProcessors
/*     */ {
/*     */   private static final String SOFTWARE = "Apache-HttpCore";
/*     */   
/*     */   public static HttpProcessorBuilder customServer(String serverInfo) {
/*  61 */     return HttpProcessorBuilder.create()
/*  62 */       .addAll(new HttpResponseInterceptor[] {
/*     */           
/*  64 */           (HttpResponseInterceptor)new ResponseDate(), (HttpResponseInterceptor)new ResponseServer(!TextUtils.isBlank(serverInfo) ? serverInfo : 
/*  65 */             VersionInfo.getSoftwareInfo("Apache-HttpCore", "org.apache.hc.core5", HttpProcessors.class)), (HttpResponseInterceptor)new ResponseContent(), (HttpResponseInterceptor)new ResponseConnControl()
/*     */ 
/*     */         
/*  68 */         }).addAll(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestValidateHost() });
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
/*     */   public static HttpProcessor server(String serverInfo) {
/*  80 */     return customServer(serverInfo).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpProcessor server() {
/*  90 */     return customServer(null).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpProcessorBuilder customClient(String agentInfo) {
/* 101 */     return HttpProcessorBuilder.create()
/* 102 */       .addAll(new HttpRequestInterceptor[] {
/*     */ 
/*     */ 
/*     */           
/* 106 */           RequestContent.INSTANCE, RequestTargetHost.INSTANCE, RequestConnControl.INSTANCE, (HttpRequestInterceptor)new RequestUserAgent(!TextUtils.isBlank(agentInfo) ? agentInfo : 
/* 107 */             VersionInfo.getSoftwareInfo("Apache-HttpCore", "org.apache.hc.core5", HttpProcessors.class)), (HttpRequestInterceptor)RequestExpectContinue.INSTANCE
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpProcessor client(String agentInfo) {
/* 119 */     return customClient(agentInfo).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpProcessor client() {
/* 129 */     return customClient(null).build();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/HttpProcessors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */