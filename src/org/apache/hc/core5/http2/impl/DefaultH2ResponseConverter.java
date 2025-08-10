/*     */ package org.apache.hc.core5.http2.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.message.BasicHeader;
/*     */ import org.apache.hc.core5.http.message.BasicHttpResponse;
/*     */ import org.apache.hc.core5.http2.H2MessageConverter;
/*     */ import org.apache.hc.core5.util.TextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultH2ResponseConverter
/*     */   implements H2MessageConverter<HttpResponse>
/*     */ {
/*  53 */   public static final DefaultH2ResponseConverter INSTANCE = new DefaultH2ResponseConverter();
/*     */   
/*     */   public HttpResponse convert(List<Header> headers) throws HttpException {
/*     */     int statusCode;
/*  57 */     String statusText = null;
/*  58 */     List<Header> messageHeaders = new ArrayList<>();
/*     */     
/*  60 */     for (int i = 0; i < headers.size(); i++) {
/*  61 */       Header header = headers.get(i);
/*  62 */       String name = header.getName();
/*  63 */       String value = header.getValue();
/*     */       
/*  65 */       for (int n = 0; n < name.length(); n++) {
/*  66 */         char ch = name.charAt(n);
/*  67 */         if (Character.isAlphabetic(ch) && !Character.isLowerCase(ch)) {
/*  68 */           throw new ProtocolException("Header name '%s' is invalid (header name contains uppercase characters)", new Object[] { name });
/*     */         }
/*     */       } 
/*     */       
/*  72 */       if (name.startsWith(":")) {
/*  73 */         if (!messageHeaders.isEmpty()) {
/*  74 */           throw new ProtocolException("Invalid sequence of headers (pseudo-headers must precede message headers)");
/*     */         }
/*  76 */         if (name.equals(":status")) {
/*  77 */           if (statusText != null) {
/*  78 */             throw new ProtocolException("Multiple '%s' response headers are illegal", new Object[] { name });
/*     */           }
/*  80 */           statusText = value;
/*     */         } else {
/*  82 */           throw new ProtocolException("Unsupported response header '%s'", new Object[] { name });
/*     */         } 
/*     */       } else {
/*  85 */         if (name.equalsIgnoreCase("Connection") || name.equalsIgnoreCase("Keep-Alive") || name
/*  86 */           .equalsIgnoreCase("Transfer-Encoding") || name.equalsIgnoreCase("Upgrade")) {
/*  87 */           throw new ProtocolException("Header '%s: %s' is illegal for HTTP/2 messages", new Object[] { header.getName(), header.getValue() });
/*     */         }
/*  89 */         messageHeaders.add(header);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  94 */     if (statusText == null) {
/*  95 */       throw new ProtocolException("Mandatory response header '%s' not found", new Object[] { ":status" });
/*     */     }
/*     */     
/*     */     try {
/*  99 */       statusCode = Integer.parseInt(statusText);
/* 100 */     } catch (NumberFormatException ex) {
/* 101 */       throw new ProtocolException("Invalid response status: " + statusText);
/*     */     } 
/* 103 */     BasicHttpResponse basicHttpResponse = new BasicHttpResponse(statusCode, null);
/* 104 */     basicHttpResponse.setVersion((ProtocolVersion)HttpVersion.HTTP_2);
/* 105 */     for (int j = 0; j < messageHeaders.size(); j++) {
/* 106 */       basicHttpResponse.addHeader(messageHeaders.get(j));
/*     */     }
/* 108 */     return (HttpResponse)basicHttpResponse;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Header> convert(HttpResponse message) throws HttpException {
/* 113 */     int code = message.getCode();
/* 114 */     if (code < 100 || code >= 600) {
/* 115 */       throw new ProtocolException("Response status %s is invalid", new Object[] { Integer.valueOf(code) });
/*     */     }
/* 117 */     List<Header> headers = new ArrayList<>();
/* 118 */     headers.add(new BasicHeader(":status", Integer.toString(code), false));
/*     */     
/* 120 */     for (Iterator<Header> it = message.headerIterator(); it.hasNext(); ) {
/* 121 */       Header header = it.next();
/* 122 */       String name = header.getName();
/* 123 */       String value = header.getValue();
/* 124 */       if (name.startsWith(":")) {
/* 125 */         throw new ProtocolException("Header name '%s' is invalid", new Object[] { name });
/*     */       }
/* 127 */       if (name.equalsIgnoreCase("Connection") || name.equalsIgnoreCase("Keep-Alive") || name
/* 128 */         .equalsIgnoreCase("Transfer-Encoding") || name.equalsIgnoreCase("Upgrade")) {
/* 129 */         throw new ProtocolException("Header '%s: %s' is illegal for HTTP/2 messages", new Object[] { header.getName(), header.getValue() });
/*     */       }
/* 131 */       headers.add(new BasicHeader(TextUtils.toLowerCase(name), value));
/*     */     } 
/* 133 */     return headers;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/DefaultH2ResponseConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */