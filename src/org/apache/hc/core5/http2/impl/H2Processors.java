/*    */ package org.apache.hc.core5.http2.impl;
/*    */ 
/*    */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*    */ import org.apache.hc.core5.http.HttpResponseInterceptor;
/*    */ import org.apache.hc.core5.http.impl.HttpProcessors;
/*    */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*    */ import org.apache.hc.core5.http.protocol.HttpProcessorBuilder;
/*    */ import org.apache.hc.core5.http.protocol.RequestExpectContinue;
/*    */ import org.apache.hc.core5.http.protocol.RequestUserAgent;
/*    */ import org.apache.hc.core5.http.protocol.ResponseDate;
/*    */ import org.apache.hc.core5.http.protocol.ResponseServer;
/*    */ import org.apache.hc.core5.http2.protocol.H2RequestConnControl;
/*    */ import org.apache.hc.core5.http2.protocol.H2RequestContent;
/*    */ import org.apache.hc.core5.http2.protocol.H2RequestTargetHost;
/*    */ import org.apache.hc.core5.http2.protocol.H2RequestValidateHost;
/*    */ import org.apache.hc.core5.http2.protocol.H2ResponseConnControl;
/*    */ import org.apache.hc.core5.http2.protocol.H2ResponseContent;
/*    */ import org.apache.hc.core5.util.TextUtils;
/*    */ import org.apache.hc.core5.util.VersionInfo;
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
/*    */ public final class H2Processors
/*    */ {
/*    */   private static final String SOFTWARE = "Apache-HttpCore";
/*    */   
/*    */   public static HttpProcessorBuilder customServer(String serverInfo) {
/* 53 */     return HttpProcessorBuilder.create()
/* 54 */       .addAll(new HttpResponseInterceptor[] {
/*    */           
/* 56 */           (HttpResponseInterceptor)new ResponseDate(), (HttpResponseInterceptor)new ResponseServer(!TextUtils.isBlank(serverInfo) ? serverInfo : 
/* 57 */             VersionInfo.getSoftwareInfo("Apache-HttpCore", "org.apache.hc.core5", H2Processors.class)), (HttpResponseInterceptor)H2ResponseContent.INSTANCE, (HttpResponseInterceptor)H2ResponseConnControl.INSTANCE
/*    */ 
/*    */         
/* 60 */         }).addAll(new HttpRequestInterceptor[] { (HttpRequestInterceptor)H2RequestValidateHost.INSTANCE });
/*    */   }
/*    */ 
/*    */   
/*    */   public static HttpProcessor server(String serverInfo) {
/* 65 */     return customServer(serverInfo).build();
/*    */   }
/*    */   
/*    */   public static HttpProcessor server() {
/* 69 */     return customServer(null).build();
/*    */   }
/*    */   
/*    */   public static HttpProcessorBuilder customClient(String agentInfo) {
/* 73 */     return HttpProcessorBuilder.create()
/* 74 */       .addAll(new HttpRequestInterceptor[] {
/*    */ 
/*    */ 
/*    */           
/* 78 */           (HttpRequestInterceptor)H2RequestContent.INSTANCE, (HttpRequestInterceptor)H2RequestTargetHost.INSTANCE, (HttpRequestInterceptor)H2RequestConnControl.INSTANCE, (HttpRequestInterceptor)new RequestUserAgent(!TextUtils.isBlank(agentInfo) ? agentInfo : 
/* 79 */             VersionInfo.getSoftwareInfo("Apache-HttpCore", "org.apache.hc.core5", HttpProcessors.class)), (HttpRequestInterceptor)RequestExpectContinue.INSTANCE
/*    */         });
/*    */   }
/*    */   
/*    */   public static HttpProcessor client(String agentInfo) {
/* 84 */     return customClient(agentInfo).build();
/*    */   }
/*    */   
/*    */   public static HttpProcessor client() {
/* 88 */     return customClient(null).build();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/H2Processors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */