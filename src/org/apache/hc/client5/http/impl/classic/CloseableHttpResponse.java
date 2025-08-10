/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import org.apache.hc.client5.http.classic.ExecRuntime;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
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
/*     */ public final class CloseableHttpResponse
/*     */   implements ClassicHttpResponse
/*     */ {
/*     */   private final ClassicHttpResponse response;
/*     */   private final ExecRuntime execRuntime;
/*     */   
/*     */   static CloseableHttpResponse adapt(ClassicHttpResponse response) {
/*  53 */     if (response == null) {
/*  54 */       return null;
/*     */     }
/*  56 */     return (response instanceof CloseableHttpResponse) ? (CloseableHttpResponse)response : new CloseableHttpResponse(response, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   CloseableHttpResponse(ClassicHttpResponse response, ExecRuntime execRuntime) {
/*  62 */     this.response = (ClassicHttpResponse)Args.notNull(response, "Response");
/*  63 */     this.execRuntime = execRuntime;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCode() {
/*  68 */     return this.response.getCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpEntity getEntity() {
/*  73 */     return this.response.getEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsHeader(String name) {
/*  78 */     return this.response.containsHeader(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVersion(ProtocolVersion version) {
/*  83 */     this.response.setVersion(version);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCode(int code) {
/*  88 */     this.response.setCode(code);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getReasonPhrase() {
/*  93 */     return this.response.getReasonPhrase();
/*     */   }
/*     */ 
/*     */   
/*     */   public int countHeaders(String name) {
/*  98 */     return this.response.countHeaders(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEntity(HttpEntity entity) {
/* 103 */     this.response.setEntity(entity);
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getVersion() {
/* 108 */     return this.response.getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReasonPhrase(String reason) {
/* 113 */     this.response.setReasonPhrase(reason);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header[] getHeaders(String name) {
/* 118 */     return this.response.getHeaders(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addHeader(Header header) {
/* 123 */     this.response.addHeader(header);
/*     */   }
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 128 */     return this.response.getLocale();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addHeader(String name, Object value) {
/* 133 */     this.response.addHeader(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLocale(Locale loc) {
/* 138 */     this.response.setLocale(loc);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getHeader(String name) throws ProtocolException {
/* 143 */     return this.response.getHeader(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeader(Header header) {
/* 148 */     this.response.setHeader(header);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getFirstHeader(String name) {
/* 153 */     return this.response.getFirstHeader(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeader(String name, Object value) {
/* 158 */     this.response.setHeader(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeaders(Header... headers) {
/* 163 */     this.response.setHeaders(headers);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeHeader(Header header) {
/* 168 */     return this.response.removeHeader(header);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeHeaders(String name) {
/* 173 */     return this.response.removeHeaders(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getLastHeader(String name) {
/* 178 */     return this.response.getLastHeader(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header[] getHeaders() {
/* 183 */     return this.response.getHeaders();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Header> headerIterator() {
/* 188 */     return this.response.headerIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Header> headerIterator(String name) {
/* 193 */     return this.response.headerIterator(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 198 */     if (this.execRuntime != null) {
/*     */       try {
/* 200 */         this.response.close();
/* 201 */         this.execRuntime.disconnectEndpoint();
/*     */       } finally {
/* 203 */         this.execRuntime.discardEndpoint();
/*     */       } 
/*     */     } else {
/* 206 */       this.response.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 212 */     return this.response.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/CloseableHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */