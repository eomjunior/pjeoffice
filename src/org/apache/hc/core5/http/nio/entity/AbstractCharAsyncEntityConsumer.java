/*     */ package org.apache.hc.core5.http.nio.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
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
/*     */ public abstract class AbstractCharAsyncEntityConsumer<T>
/*     */   extends AbstractCharDataConsumer
/*     */   implements AsyncEntityConsumer<T>
/*     */ {
/*     */   private volatile FutureCallback<T> resultCallback;
/*     */   private volatile T content;
/*     */   
/*     */   protected AbstractCharAsyncEntityConsumer(int bufSize, CharCodingConfig charCodingConfig) {
/*  54 */     super(bufSize, charCodingConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractCharAsyncEntityConsumer() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void streamStart(ContentType paramContentType) throws HttpException, IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract T generateContent() throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void streamStart(EntityDetails entityDetails, FutureCallback<T> resultCallback) throws IOException, HttpException {
/*  78 */     Args.notNull(resultCallback, "Result callback");
/*  79 */     this.resultCallback = resultCallback;
/*     */     try {
/*  81 */       ContentType contentType = (entityDetails != null) ? ContentType.parse(entityDetails.getContentType()) : null;
/*  82 */       setCharset(ContentType.getCharset(contentType, null));
/*  83 */       streamStart(contentType);
/*  84 */     } catch (UnsupportedCharsetException ex) {
/*  85 */       throw new UnsupportedEncodingException(ex.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void completed() throws IOException {
/*  91 */     this.content = generateContent();
/*  92 */     if (this.resultCallback != null) {
/*  93 */       this.resultCallback.completed(this.content);
/*     */     }
/*  95 */     releaseResources();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void failed(Exception cause) {
/* 100 */     if (this.resultCallback != null) {
/* 101 */       this.resultCallback.failed(cause);
/*     */     }
/* 103 */     releaseResources();
/*     */   }
/*     */ 
/*     */   
/*     */   public final T getContent() {
/* 108 */     return this.content;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/AbstractCharAsyncEntityConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */