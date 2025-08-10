/*     */ package org.apache.hc.core5.http2.impl;
/*     */ 
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.message.BasicHeader;
/*     */ import org.apache.hc.core5.http.message.BasicHttpRequest;
/*     */ import org.apache.hc.core5.http2.H2MessageConverter;
/*     */ import org.apache.hc.core5.net.URIAuthority;
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
/*     */ public final class DefaultH2RequestConverter
/*     */   implements H2MessageConverter<HttpRequest>
/*     */ {
/*  56 */   public static final DefaultH2RequestConverter INSTANCE = new DefaultH2RequestConverter();
/*     */ 
/*     */   
/*     */   public HttpRequest convert(List<Header> headers) throws HttpException {
/*  60 */     String method = null;
/*  61 */     String scheme = null;
/*  62 */     String authority = null;
/*  63 */     String path = null;
/*  64 */     List<Header> messageHeaders = new ArrayList<>();
/*     */     
/*  66 */     for (int i = 0; i < headers.size(); i++) {
/*  67 */       Header header = headers.get(i);
/*  68 */       String name = header.getName();
/*  69 */       String value = header.getValue();
/*     */       
/*  71 */       for (int n = 0; n < name.length(); n++) {
/*  72 */         char ch = name.charAt(n);
/*  73 */         if (Character.isAlphabetic(ch) && !Character.isLowerCase(ch)) {
/*  74 */           throw new ProtocolException("Header name '%s' is invalid (header name contains uppercase characters)", new Object[] { name });
/*     */         }
/*     */       } 
/*     */       
/*  78 */       if (name.startsWith(":")) {
/*  79 */         if (!messageHeaders.isEmpty()) {
/*  80 */           throw new ProtocolException("Invalid sequence of headers (pseudo-headers must precede message headers)");
/*     */         }
/*     */         
/*  83 */         switch (name) {
/*     */           case ":method":
/*  85 */             if (method != null) {
/*  86 */               throw new ProtocolException("Multiple '%s' request headers are illegal", new Object[] { name });
/*     */             }
/*  88 */             method = value;
/*     */             break;
/*     */           case ":scheme":
/*  91 */             if (scheme != null) {
/*  92 */               throw new ProtocolException("Multiple '%s' request headers are illegal", new Object[] { name });
/*     */             }
/*  94 */             scheme = value;
/*     */             break;
/*     */           case ":path":
/*  97 */             if (path != null) {
/*  98 */               throw new ProtocolException("Multiple '%s' request headers are illegal", new Object[] { name });
/*     */             }
/* 100 */             path = value;
/*     */             break;
/*     */           case ":authority":
/* 103 */             authority = value;
/*     */             break;
/*     */           default:
/* 106 */             throw new ProtocolException("Unsupported request header '%s'", new Object[] { name });
/*     */         } 
/*     */       } else {
/* 109 */         if (name.equalsIgnoreCase("Connection") || name.equalsIgnoreCase("Keep-Alive") || name
/* 110 */           .equalsIgnoreCase("Proxy-Connection") || name.equalsIgnoreCase("Transfer-Encoding") || name
/* 111 */           .equalsIgnoreCase("Host") || name.equalsIgnoreCase("Upgrade")) {
/* 112 */           throw new ProtocolException("Header '%s: %s' is illegal for HTTP/2 messages", new Object[] { header.getName(), header.getValue() });
/*     */         }
/* 114 */         if (name.equalsIgnoreCase("TE") && !value.equalsIgnoreCase("trailers")) {
/* 115 */           throw new ProtocolException("Header '%s: %s' is illegal for HTTP/2 messages", new Object[] { header.getName(), header.getValue() });
/*     */         }
/* 117 */         messageHeaders.add(header);
/*     */       } 
/*     */     } 
/* 120 */     if (method == null) {
/* 121 */       throw new ProtocolException("Mandatory request header '%s' not found", new Object[] { ":method" });
/*     */     }
/* 123 */     if (Method.CONNECT.isSame(method)) {
/* 124 */       if (authority == null) {
/* 125 */         throw new ProtocolException("Header '%s' is mandatory for CONNECT request", new Object[] { ":authority" });
/*     */       }
/* 127 */       if (scheme != null) {
/* 128 */         throw new ProtocolException("Header '%s' must not be set for CONNECT request", new Object[] { ":scheme" });
/*     */       }
/* 130 */       if (path != null) {
/* 131 */         throw new ProtocolException("Header '%s' must not be set for CONNECT request", new Object[] { ":path" });
/*     */       }
/*     */     } else {
/* 134 */       if (scheme == null) {
/* 135 */         throw new ProtocolException("Mandatory request header '%s' not found", new Object[] { ":scheme" });
/*     */       }
/* 137 */       if (path == null) {
/* 138 */         throw new ProtocolException("Mandatory request header '%s' not found", new Object[] { ":path" });
/*     */       }
/*     */     } 
/*     */     
/* 142 */     BasicHttpRequest basicHttpRequest = new BasicHttpRequest(method, path);
/* 143 */     basicHttpRequest.setVersion((ProtocolVersion)HttpVersion.HTTP_2);
/* 144 */     basicHttpRequest.setScheme(scheme);
/*     */     try {
/* 146 */       basicHttpRequest.setAuthority(URIAuthority.create(authority));
/* 147 */     } catch (URISyntaxException ex) {
/* 148 */       throw new ProtocolException(ex.getMessage(), ex);
/*     */     } 
/* 150 */     basicHttpRequest.setPath(path);
/* 151 */     for (int j = 0; j < messageHeaders.size(); j++) {
/* 152 */       basicHttpRequest.addHeader(messageHeaders.get(j));
/*     */     }
/* 154 */     return (HttpRequest)basicHttpRequest;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Header> convert(HttpRequest message) throws HttpException {
/* 159 */     if (TextUtils.isBlank(message.getMethod())) {
/* 160 */       throw new ProtocolException("Request method is empty");
/*     */     }
/* 162 */     boolean optionMethod = Method.CONNECT.name().equalsIgnoreCase(message.getMethod());
/* 163 */     if (optionMethod) {
/* 164 */       if (message.getAuthority() == null) {
/* 165 */         throw new ProtocolException("CONNECT request authority is not set");
/*     */       }
/* 167 */       if (message.getPath() != null) {
/* 168 */         throw new ProtocolException("CONNECT request path must be null");
/*     */       }
/*     */     } else {
/* 171 */       if (TextUtils.isBlank(message.getScheme())) {
/* 172 */         throw new ProtocolException("Request scheme is not set");
/*     */       }
/* 174 */       if (TextUtils.isBlank(message.getPath())) {
/* 175 */         throw new ProtocolException("Request path is not set");
/*     */       }
/*     */     } 
/* 178 */     List<Header> headers = new ArrayList<>();
/* 179 */     headers.add(new BasicHeader(":method", message.getMethod(), false));
/* 180 */     if (optionMethod) {
/* 181 */       headers.add(new BasicHeader(":authority", message.getAuthority(), false));
/*     */     } else {
/* 183 */       headers.add(new BasicHeader(":scheme", message.getScheme(), false));
/* 184 */       if (message.getAuthority() != null) {
/* 185 */         headers.add(new BasicHeader(":authority", message.getAuthority(), false));
/*     */       }
/* 187 */       headers.add(new BasicHeader(":path", message.getPath(), false));
/*     */     } 
/*     */     
/* 190 */     for (Iterator<Header> it = message.headerIterator(); it.hasNext(); ) {
/* 191 */       Header header = it.next();
/* 192 */       String name = header.getName();
/* 193 */       String value = header.getValue();
/* 194 */       if (name.startsWith(":")) {
/* 195 */         throw new ProtocolException("Header name '%s' is invalid", new Object[] { name });
/*     */       }
/* 197 */       if (name.equalsIgnoreCase("Connection") || name.equalsIgnoreCase("Keep-Alive") || name
/* 198 */         .equalsIgnoreCase("Proxy-Connection") || name.equalsIgnoreCase("Transfer-Encoding") || name
/* 199 */         .equalsIgnoreCase("Host") || name.equalsIgnoreCase("Upgrade")) {
/* 200 */         throw new ProtocolException("Header '%s: %s' is illegal for HTTP/2 messages", new Object[] { header.getName(), header.getValue() });
/*     */       }
/* 202 */       if (name.equalsIgnoreCase("TE") && !value.equalsIgnoreCase("trailers")) {
/* 203 */         throw new ProtocolException("Header '%s: %s' is illegal for HTTP/2 messages", new Object[] { header.getName(), header.getValue() });
/*     */       }
/* 205 */       headers.add(new BasicHeader(TextUtils.toLowerCase(name), value));
/*     */     } 
/*     */     
/* 208 */     return headers;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/DefaultH2RequestConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */