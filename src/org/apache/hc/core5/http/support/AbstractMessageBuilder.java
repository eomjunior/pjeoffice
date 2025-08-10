/*     */ package org.apache.hc.core5.http.support;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.message.BasicHeader;
/*     */ import org.apache.hc.core5.http.message.HeaderGroup;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMessageBuilder<T>
/*     */ {
/*     */   private ProtocolVersion version;
/*     */   private HeaderGroup headerGroup;
/*     */   
/*     */   protected void digest(HttpMessage message) {
/*  52 */     if (message == null) {
/*     */       return;
/*     */     }
/*  55 */     setVersion(message.getVersion());
/*  56 */     setHeaders(message.headerIterator());
/*     */   }
/*     */   
/*     */   public ProtocolVersion getVersion() {
/*  60 */     return this.version;
/*     */   }
/*     */   
/*     */   public AbstractMessageBuilder<T> setVersion(ProtocolVersion version) {
/*  64 */     this.version = version;
/*  65 */     return this;
/*     */   }
/*     */   
/*     */   public Header[] getHeaders() {
/*  69 */     return (this.headerGroup != null) ? this.headerGroup.getHeaders() : null;
/*     */   }
/*     */   
/*     */   public Header[] getHeaders(String name) {
/*  73 */     return (this.headerGroup != null) ? this.headerGroup.getHeaders(name) : null;
/*     */   }
/*     */   
/*     */   public AbstractMessageBuilder<T> setHeaders(Header... headers) {
/*  77 */     if (this.headerGroup == null) {
/*  78 */       this.headerGroup = new HeaderGroup();
/*     */     }
/*  80 */     this.headerGroup.setHeaders(headers);
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public AbstractMessageBuilder<T> setHeaders(Iterator<Header> it) {
/*  85 */     if (this.headerGroup == null) {
/*  86 */       this.headerGroup = new HeaderGroup();
/*     */     } else {
/*  88 */       this.headerGroup.clear();
/*     */     } 
/*  90 */     while (it.hasNext()) {
/*  91 */       this.headerGroup.addHeader(it.next());
/*     */     }
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public Header[] getFirstHeaders() {
/*  97 */     return (this.headerGroup != null) ? this.headerGroup.getHeaders() : null;
/*     */   }
/*     */   
/*     */   public Header getFirstHeader(String name) {
/* 101 */     return (this.headerGroup != null) ? this.headerGroup.getFirstHeader(name) : null;
/*     */   }
/*     */   
/*     */   public Header getLastHeader(String name) {
/* 105 */     return (this.headerGroup != null) ? this.headerGroup.getLastHeader(name) : null;
/*     */   }
/*     */   
/*     */   public AbstractMessageBuilder<T> addHeader(Header header) {
/* 109 */     if (this.headerGroup == null) {
/* 110 */       this.headerGroup = new HeaderGroup();
/*     */     }
/* 112 */     this.headerGroup.addHeader(header);
/* 113 */     return this;
/*     */   }
/*     */   
/*     */   public AbstractMessageBuilder<T> addHeader(String name, String value) {
/* 117 */     if (this.headerGroup == null) {
/* 118 */       this.headerGroup = new HeaderGroup();
/*     */     }
/* 120 */     this.headerGroup.addHeader((Header)new BasicHeader(name, value));
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public AbstractMessageBuilder<T> removeHeader(Header header) {
/* 125 */     if (this.headerGroup == null) {
/* 126 */       this.headerGroup = new HeaderGroup();
/*     */     }
/* 128 */     this.headerGroup.removeHeader(header);
/* 129 */     return this;
/*     */   }
/*     */   
/*     */   public AbstractMessageBuilder<T> removeHeaders(String name) {
/* 133 */     if (name == null || this.headerGroup == null) {
/* 134 */       return this;
/*     */     }
/* 136 */     for (Iterator<Header> i = this.headerGroup.headerIterator(); i.hasNext(); ) {
/* 137 */       Header header = i.next();
/* 138 */       if (name.equalsIgnoreCase(header.getName())) {
/* 139 */         i.remove();
/*     */       }
/*     */     } 
/* 142 */     return this;
/*     */   }
/*     */   
/*     */   public AbstractMessageBuilder<T> setHeader(Header header) {
/* 146 */     if (this.headerGroup == null) {
/* 147 */       this.headerGroup = new HeaderGroup();
/*     */     }
/* 149 */     this.headerGroup.setHeader(header);
/* 150 */     return this;
/*     */   }
/*     */   
/*     */   public AbstractMessageBuilder<T> setHeader(String name, String value) {
/* 154 */     if (this.headerGroup == null) {
/* 155 */       this.headerGroup = new HeaderGroup();
/*     */     }
/* 157 */     this.headerGroup.setHeader((Header)new BasicHeader(name, value));
/* 158 */     return this;
/*     */   }
/*     */   
/*     */   protected abstract T build();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/support/AbstractMessageBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */