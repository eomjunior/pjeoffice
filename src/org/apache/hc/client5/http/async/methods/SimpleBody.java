/*     */ package org.apache.hc.client5.http.async.methods;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.hc.core5.http.ContentType;
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
/*     */ public final class SimpleBody
/*     */ {
/*     */   private final byte[] bodyAsBytes;
/*     */   private final String bodyAsText;
/*     */   private final ContentType contentType;
/*     */   
/*     */   SimpleBody(byte[] bodyAsBytes, String bodyAsText, ContentType contentType) {
/*  48 */     this.bodyAsBytes = bodyAsBytes;
/*  49 */     this.bodyAsText = bodyAsText;
/*  50 */     this.contentType = contentType;
/*     */   }
/*     */   
/*     */   static SimpleBody create(String body, ContentType contentType) {
/*  54 */     Args.notNull(body, "Body");
/*  55 */     if (body.length() > 2048) {
/*  56 */       return new SimpleBody(null, body, contentType);
/*     */     }
/*  58 */     Charset charset = ((contentType != null) ? contentType : ContentType.DEFAULT_TEXT).getCharset();
/*  59 */     byte[] bytes = body.getBytes((charset != null) ? charset : StandardCharsets.US_ASCII);
/*  60 */     return new SimpleBody(bytes, null, contentType);
/*     */   }
/*     */   
/*     */   static SimpleBody create(byte[] body, ContentType contentType) {
/*  64 */     Args.notNull(body, "Body");
/*  65 */     return new SimpleBody(body, null, contentType);
/*     */   }
/*     */   
/*     */   public ContentType getContentType() {
/*  69 */     return this.contentType;
/*     */   }
/*     */   
/*     */   public byte[] getBodyBytes() {
/*  73 */     if (this.bodyAsBytes != null)
/*  74 */       return this.bodyAsBytes; 
/*  75 */     if (this.bodyAsText != null) {
/*  76 */       Charset charset = ((this.contentType != null) ? this.contentType : ContentType.DEFAULT_TEXT).getCharset();
/*  77 */       return this.bodyAsText.getBytes((charset != null) ? charset : StandardCharsets.US_ASCII);
/*     */     } 
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBodyText() {
/*  84 */     if (this.bodyAsBytes != null) {
/*  85 */       Charset charset = ((this.contentType != null) ? this.contentType : ContentType.DEFAULT_TEXT).getCharset();
/*  86 */       return new String(this.bodyAsBytes, (charset != null) ? charset : StandardCharsets.US_ASCII);
/*     */     } 
/*  88 */     return this.bodyAsText;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isText() {
/*  93 */     return (this.bodyAsText != null);
/*     */   }
/*     */   
/*     */   public boolean isBytes() {
/*  97 */     return (this.bodyAsBytes != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 102 */     return "SimpleBody{content length=" + ((this.bodyAsBytes != null) ? (String)Integer.valueOf(this.bodyAsBytes.length) : "chunked") + ", content type=" + this.contentType + "}";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/SimpleBody.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */