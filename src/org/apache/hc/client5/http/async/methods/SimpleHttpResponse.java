/*     */ package org.apache.hc.client5.http.async.methods;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.message.BasicHttpResponse;
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
/*     */ public final class SimpleHttpResponse
/*     */   extends BasicHttpResponse
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private SimpleBody body;
/*     */   
/*     */   public SimpleHttpResponse(int code) {
/*  58 */     super(code);
/*     */   }
/*     */   
/*     */   public SimpleHttpResponse(int code, String reasonPhrase) {
/*  62 */     super(code, reasonPhrase);
/*     */   }
/*     */   
/*     */   public static SimpleHttpResponse copy(HttpResponse original) {
/*  66 */     Args.notNull(original, "HTTP response");
/*  67 */     SimpleHttpResponse copy = new SimpleHttpResponse(original.getCode());
/*  68 */     copy.setVersion(original.getVersion());
/*  69 */     for (Iterator<Header> it = original.headerIterator(); it.hasNext();) {
/*  70 */       copy.addHeader(it.next());
/*     */     }
/*  72 */     return copy;
/*     */   }
/*     */   
/*     */   public static SimpleHttpResponse create(int code) {
/*  76 */     return new SimpleHttpResponse(code);
/*     */   }
/*     */   
/*     */   public static SimpleHttpResponse create(int code, String content, ContentType contentType) {
/*  80 */     SimpleHttpResponse response = new SimpleHttpResponse(code);
/*  81 */     if (content != null) {
/*  82 */       response.setBody(content, contentType);
/*     */     }
/*  84 */     return response;
/*     */   }
/*     */   
/*     */   public static SimpleHttpResponse create(int code, String content) {
/*  88 */     return create(code, content, ContentType.TEXT_PLAIN);
/*     */   }
/*     */   
/*     */   public static SimpleHttpResponse create(int code, byte[] content, ContentType contentType) {
/*  92 */     SimpleHttpResponse response = new SimpleHttpResponse(code);
/*  93 */     if (content != null) {
/*  94 */       response.setBody(content, contentType);
/*     */     }
/*  96 */     return response;
/*     */   }
/*     */   
/*     */   public static SimpleHttpResponse create(int code, byte[] content) {
/* 100 */     return create(code, content, ContentType.TEXT_PLAIN);
/*     */   }
/*     */   
/*     */   public void setBody(SimpleBody body) {
/* 104 */     this.body = body;
/*     */   }
/*     */   
/*     */   public void setBody(byte[] bodyBytes, ContentType contentType) {
/* 108 */     this.body = SimpleBody.create(bodyBytes, contentType);
/*     */   }
/*     */   
/*     */   public void setBody(String bodyText, ContentType contentType) {
/* 112 */     this.body = SimpleBody.create(bodyText, contentType);
/*     */   }
/*     */   
/*     */   public SimpleBody getBody() {
/* 116 */     return this.body;
/*     */   }
/*     */   
/*     */   public ContentType getContentType() {
/* 120 */     return (this.body != null) ? this.body.getContentType() : null;
/*     */   }
/*     */   
/*     */   public String getBodyText() {
/* 124 */     return (this.body != null) ? this.body.getBodyText() : null;
/*     */   }
/*     */   
/*     */   public byte[] getBodyBytes() {
/* 128 */     return (this.body != null) ? this.body.getBodyBytes() : null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/SimpleHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */