/*    */ package org.apache.hc.core5.http.nio.entity;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.nio.charset.UnsupportedCharsetException;
/*    */ import org.apache.hc.core5.concurrent.FutureCallback;
/*    */ import org.apache.hc.core5.http.ContentType;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
/*    */ import org.apache.hc.core5.util.Args;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractBinAsyncEntityConsumer<T>
/*    */   extends AbstractBinDataConsumer
/*    */   implements AsyncEntityConsumer<T>
/*    */ {
/*    */   private volatile FutureCallback<T> resultCallback;
/*    */   private volatile T content;
/*    */   
/*    */   protected abstract void streamStart(ContentType paramContentType) throws HttpException, IOException;
/*    */   
/*    */   protected abstract T generateContent() throws IOException;
/*    */   
/*    */   public final void streamStart(EntityDetails entityDetails, FutureCallback<T> resultCallback) throws IOException, HttpException {
/* 70 */     Args.notNull(resultCallback, "Result callback");
/* 71 */     this.resultCallback = resultCallback;
/*    */     try {
/* 73 */       ContentType contentType = (entityDetails != null) ? ContentType.parse(entityDetails.getContentType()) : null;
/* 74 */       streamStart(contentType);
/* 75 */     } catch (UnsupportedCharsetException ex) {
/* 76 */       throw new UnsupportedEncodingException(ex.getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected final void completed() throws IOException {
/* 82 */     this.content = generateContent();
/* 83 */     if (this.resultCallback != null) {
/* 84 */       this.resultCallback.completed(this.content);
/*    */     }
/* 86 */     releaseResources();
/*    */   }
/*    */ 
/*    */   
/*    */   public final void failed(Exception cause) {
/* 91 */     if (this.resultCallback != null) {
/* 92 */       this.resultCallback.failed(cause);
/*    */     }
/* 94 */     releaseResources();
/*    */   }
/*    */ 
/*    */   
/*    */   public final T getContent() {
/* 99 */     return this.content;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/AbstractBinAsyncEntityConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */