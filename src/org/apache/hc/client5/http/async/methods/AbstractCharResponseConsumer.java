/*     */ package org.apache.hc.client5.http.async.methods;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
/*     */ import org.apache.hc.core5.http.nio.entity.AbstractCharDataConsumer;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCharResponseConsumer<T>
/*     */   extends AbstractCharDataConsumer
/*     */   implements AsyncResponseConsumer<T>
/*     */ {
/*     */   private volatile FutureCallback<T> resultCallback;
/*     */   
/*     */   protected abstract void start(HttpResponse paramHttpResponse, ContentType paramContentType) throws HttpException, IOException;
/*     */   
/*     */   protected abstract T buildResult() throws IOException;
/*     */   
/*     */   public void informationResponse(HttpResponse response, HttpContext context) throws HttpException, IOException {}
/*     */   
/*     */   public final void consumeResponse(HttpResponse response, EntityDetails entityDetails, HttpContext context, FutureCallback<T> resultCallback) throws HttpException, IOException {
/*  83 */     this.resultCallback = resultCallback;
/*  84 */     if (entityDetails != null) {
/*     */       ContentType contentType;
/*     */       try {
/*  87 */         contentType = ContentType.parse(entityDetails.getContentType());
/*  88 */       } catch (UnsupportedCharsetException ex) {
/*  89 */         throw new UnsupportedEncodingException(ex.getMessage());
/*     */       } 
/*  91 */       Charset charset = (contentType != null) ? contentType.getCharset() : null;
/*  92 */       if (charset == null) {
/*  93 */         charset = StandardCharsets.US_ASCII;
/*     */       }
/*  95 */       setCharset(charset);
/*  96 */       start(response, (contentType != null) ? contentType : ContentType.DEFAULT_TEXT);
/*     */     } else {
/*  98 */       start(response, (ContentType)null);
/*  99 */       completed();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void completed() throws IOException {
/* 105 */     this.resultCallback.completed(buildResult());
/*     */   }
/*     */   
/*     */   public void failed(Exception cause) {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/AbstractCharResponseConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */