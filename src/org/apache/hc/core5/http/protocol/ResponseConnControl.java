/*     */ package org.apache.hc.core5.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HeaderElement;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.HttpResponseInterceptor;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
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
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class ResponseConnControl
/*     */   implements HttpResponseInterceptor
/*     */ {
/*     */   public void process(HttpResponse response, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/*  67 */     Args.notNull(response, "HTTP response");
/*  68 */     Args.notNull(context, "HTTP context");
/*     */ 
/*     */     
/*  71 */     int status = response.getCode();
/*  72 */     if (status == 400 || status == 408 || status == 411 || status == 413 || status == 414 || status == 503 || status == 501) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  79 */       response.setHeader("Connection", "close");
/*     */       return;
/*     */     } 
/*  82 */     if (!response.containsHeader("Connection")) {
/*     */ 
/*     */       
/*  85 */       ProtocolVersion ver = context.getProtocolVersion();
/*  86 */       if (entity != null && entity.getContentLength() < 0L && ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/*  87 */         response.setHeader("Connection", "close");
/*     */       } else {
/*  89 */         HttpCoreContext coreContext = HttpCoreContext.adapt(context);
/*  90 */         HttpRequest request = coreContext.getRequest();
/*  91 */         boolean closeRequested = false;
/*  92 */         boolean keepAliveRequested = false;
/*  93 */         if (request != null) {
/*  94 */           Iterator<HeaderElement> it = MessageSupport.iterate((MessageHeaders)request, "Connection");
/*  95 */           while (it.hasNext()) {
/*  96 */             HeaderElement he = it.next();
/*  97 */             if (he.getName().equalsIgnoreCase("close")) {
/*  98 */               closeRequested = true; break;
/*     */             } 
/* 100 */             if (he.getName().equalsIgnoreCase("keep-alive")) {
/* 101 */               keepAliveRequested = true;
/*     */             }
/*     */           } 
/*     */         } 
/* 105 */         if (closeRequested) {
/* 106 */           response.addHeader("Connection", "close");
/*     */         }
/* 108 */         else if (response.containsHeader("Upgrade")) {
/* 109 */           response.addHeader("Connection", "upgrade");
/*     */         }
/* 111 */         else if (keepAliveRequested) {
/* 112 */           response.addHeader("Connection", "keep-alive");
/*     */         }
/* 114 */         else if (ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/* 115 */           response.addHeader("Connection", "close");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/ResponseConnControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */