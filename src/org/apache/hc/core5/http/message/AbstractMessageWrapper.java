/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpMessage;
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
/*     */ public abstract class AbstractMessageWrapper
/*     */   implements HttpMessage
/*     */ {
/*     */   private final HttpMessage message;
/*     */   
/*     */   public AbstractMessageWrapper(HttpMessage message) {
/*  46 */     this.message = (HttpMessage)Args.notNull(message, "Message");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVersion(ProtocolVersion version) {
/*  51 */     this.message.setVersion(version);
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getVersion() {
/*  56 */     return this.message.getVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addHeader(Header header) {
/*  61 */     this.message.addHeader(header);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addHeader(String name, Object value) {
/*  66 */     this.message.addHeader(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeader(Header header) {
/*  71 */     this.message.setHeader(header);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeader(String name, Object value) {
/*  76 */     this.message.setHeader(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeaders(Header... headers) {
/*  81 */     this.message.setHeaders(headers);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeHeader(Header header) {
/*  86 */     return this.message.removeHeader(header);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeHeaders(String name) {
/*  91 */     return this.message.removeHeaders(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsHeader(String name) {
/*  96 */     return this.message.containsHeader(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public int countHeaders(String name) {
/* 101 */     return this.message.countHeaders(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header[] getHeaders(String name) {
/* 106 */     return this.message.getHeaders(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getHeader(String name) throws ProtocolException {
/* 111 */     return this.message.getHeader(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getFirstHeader(String name) {
/* 116 */     return this.message.getFirstHeader(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getLastHeader(String name) {
/* 121 */     return this.message.getLastHeader(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Header[] getHeaders() {
/* 126 */     return this.message.getHeaders();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Header> headerIterator() {
/* 131 */     return this.message.headerIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Header> headerIterator(String name) {
/* 136 */     return this.message.headerIterator(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 141 */     return this.message.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/AbstractMessageWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */