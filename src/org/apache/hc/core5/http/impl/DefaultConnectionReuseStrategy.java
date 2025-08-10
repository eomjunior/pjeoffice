/*     */ package org.apache.hc.core5.http.impl;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.message.BasicTokenIterator;
/*     */ import org.apache.hc.core5.http.message.MessageSupport;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class DefaultConnectionReuseStrategy
/*     */   implements ConnectionReuseStrategy
/*     */ {
/*  71 */   public static final DefaultConnectionReuseStrategy INSTANCE = new DefaultConnectionReuseStrategy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean keepAlive(HttpRequest request, HttpResponse response, HttpContext context) {
/*  81 */     Args.notNull(response, "HTTP response");
/*     */     
/*  83 */     if (request != null) {
/*  84 */       BasicTokenIterator<String> basicTokenIterator = new BasicTokenIterator(request.headerIterator("Connection"));
/*  85 */       while (basicTokenIterator.hasNext()) {
/*  86 */         String token = basicTokenIterator.next();
/*  87 */         if ("close".equalsIgnoreCase(token)) {
/*  88 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  96 */     if (response.getCode() == 204) {
/*  97 */       Header clh = response.getFirstHeader("Content-Length");
/*  98 */       if (clh != null) {
/*     */         try {
/* 100 */           long contentLen = Long.parseLong(clh.getValue());
/* 101 */           if (contentLen > 0L) {
/* 102 */             return false;
/*     */           }
/* 104 */         } catch (NumberFormatException numberFormatException) {}
/*     */       }
/*     */ 
/*     */       
/* 108 */       if (response.containsHeader("Transfer-Encoding")) {
/* 109 */         return false;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 115 */     Header teh = response.getFirstHeader("Transfer-Encoding");
/* 116 */     if (teh != null) {
/* 117 */       if (!"chunked".equalsIgnoreCase(teh.getValue())) {
/* 118 */         return false;
/*     */       }
/*     */     } else {
/* 121 */       String method = (request != null) ? request.getMethod() : null;
/* 122 */       if (MessageSupport.canResponseHaveBody(method, response) && response
/* 123 */         .countHeaders("Content-Length") != 1) {
/* 124 */         return false;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     Iterator<Header> headerIterator = response.headerIterator("Connection");
/* 132 */     if (!headerIterator.hasNext()) {
/* 133 */       headerIterator = response.headerIterator("Proxy-Connection");
/*     */     }
/*     */     
/* 136 */     ProtocolVersion ver = (response.getVersion() != null) ? response.getVersion() : context.getProtocolVersion();
/* 137 */     if (headerIterator.hasNext()) {
/* 138 */       if (ver.greaterEquals((ProtocolVersion)HttpVersion.HTTP_1_1)) {
/* 139 */         BasicTokenIterator<String> basicTokenIterator1 = new BasicTokenIterator(headerIterator);
/* 140 */         while (basicTokenIterator1.hasNext()) {
/* 141 */           String token = basicTokenIterator1.next();
/* 142 */           if ("close".equalsIgnoreCase(token)) {
/* 143 */             return false;
/*     */           }
/*     */         } 
/* 146 */         return true;
/*     */       } 
/* 148 */       BasicTokenIterator<String> basicTokenIterator = new BasicTokenIterator(headerIterator);
/* 149 */       while (basicTokenIterator.hasNext()) {
/* 150 */         String token = basicTokenIterator.next();
/* 151 */         if ("keep-alive".equalsIgnoreCase(token)) {
/* 152 */           return true;
/*     */         }
/*     */       } 
/* 155 */       return false;
/*     */     } 
/* 157 */     return ver.greaterEquals((ProtocolVersion)HttpVersion.HTTP_1_1);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/DefaultConnectionReuseStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */