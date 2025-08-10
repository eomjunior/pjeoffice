/*     */ package org.apache.hc.core5.http.nio.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
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
/*     */ public class DigestingEntityConsumer<T>
/*     */   implements AsyncEntityConsumer<T>
/*     */ {
/*     */   private final AsyncEntityConsumer<T> wrapped;
/*     */   private final List<Header> trailers;
/*     */   private final MessageDigest digester;
/*     */   private volatile byte[] digest;
/*     */   
/*     */   public DigestingEntityConsumer(String algo, AsyncEntityConsumer<T> wrapped) throws NoSuchAlgorithmException {
/*  62 */     this.wrapped = (AsyncEntityConsumer<T>)Args.notNull(wrapped, "Entity consumer");
/*  63 */     this.trailers = new ArrayList<>();
/*  64 */     this.digester = MessageDigest.getInstance(algo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void streamStart(EntityDetails entityDetails, FutureCallback<T> resultCallback) throws IOException, HttpException {
/*  71 */     this.wrapped.streamStart(entityDetails, resultCallback);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/*  76 */     this.wrapped.updateCapacity(capacityChannel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void consume(ByteBuffer src) throws IOException {
/*  81 */     src.mark();
/*  82 */     this.digester.update(src);
/*  83 */     src.reset();
/*  84 */     this.wrapped.consume(src);
/*     */   }
/*     */ 
/*     */   
/*     */   public void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/*  89 */     if (trailers != null) {
/*  90 */       this.trailers.addAll(trailers);
/*     */     }
/*  92 */     this.digest = this.digester.digest();
/*  93 */     this.wrapped.streamEnd(trailers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/*  98 */     this.wrapped.failed(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public T getContent() {
/* 103 */     return (T)this.wrapped.getContent();
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 108 */     this.wrapped.releaseResources();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Header> getTrailers() {
/* 117 */     return (this.trailers != null) ? new ArrayList<>(this.trailers) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getDigest() {
/* 126 */     return this.digest;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/DigestingEntityConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */