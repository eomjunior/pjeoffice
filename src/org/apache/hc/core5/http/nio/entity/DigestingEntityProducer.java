/*     */ package org.apache.hc.core5.http.nio.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.message.BasicHeader;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ 
/*     */ 
/*     */ public class DigestingEntityProducer
/*     */   implements AsyncEntityProducer
/*     */ {
/*     */   private final AsyncEntityProducer wrapped;
/*     */   private final MessageDigest digester;
/*     */   private volatile byte[] digest;
/*     */   
/*     */   public DigestingEntityProducer(String algo, AsyncEntityProducer wrapped) {
/*  61 */     this.wrapped = (AsyncEntityProducer)Args.notNull(wrapped, "Entity consumer");
/*     */     try {
/*  63 */       this.digester = MessageDigest.getInstance(algo);
/*  64 */     } catch (NoSuchAlgorithmException ex) {
/*  65 */       throw new IllegalArgumentException("Unsupported digest algorithm: " + algo);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  71 */     return this.wrapped.isRepeatable();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/*  76 */     return this.wrapped.getContentLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentType() {
/*  81 */     return this.wrapped.getContentType();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/*  86 */     return this.wrapped.getContentEncoding();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/*  91 */     return this.wrapped.isChunked();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getTrailerNames() {
/*  96 */     Set<String> allNames = new LinkedHashSet<>();
/*  97 */     Set<String> names = this.wrapped.getTrailerNames();
/*  98 */     if (names != null) {
/*  99 */       allNames.addAll(names);
/*     */     }
/* 101 */     allNames.add("digest-algo");
/* 102 */     allNames.add("digest");
/* 103 */     return allNames;
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/* 108 */     return this.wrapped.available();
/*     */   }
/*     */ 
/*     */   
/*     */   public void produce(final DataStreamChannel channel) throws IOException {
/* 113 */     this.wrapped.produce(new DataStreamChannel()
/*     */         {
/*     */           public void requestOutput()
/*     */           {
/* 117 */             channel.requestOutput();
/*     */           }
/*     */ 
/*     */           
/*     */           public int write(ByteBuffer src) throws IOException {
/* 122 */             ByteBuffer dup = src.duplicate();
/* 123 */             int writtenBytes = channel.write(src);
/* 124 */             if (writtenBytes > 0) {
/* 125 */               dup.limit(dup.position() + writtenBytes);
/* 126 */               DigestingEntityProducer.this.digester.update(dup);
/*     */             } 
/* 128 */             return writtenBytes;
/*     */           }
/*     */ 
/*     */           
/*     */           public void endStream(List<? extends Header> trailers) throws IOException {
/* 133 */             DigestingEntityProducer.this.digest = DigestingEntityProducer.this.digester.digest();
/* 134 */             List<Header> allTrailers = new ArrayList<>();
/* 135 */             if (trailers != null) {
/* 136 */               allTrailers.addAll(trailers);
/*     */             }
/* 138 */             allTrailers.add(new BasicHeader("digest-algo", DigestingEntityProducer.this.digester.getAlgorithm()));
/* 139 */             allTrailers.add(new BasicHeader("digest", TextUtils.toHexString(DigestingEntityProducer.this.digest)));
/* 140 */             channel.endStream(allTrailers);
/*     */           }
/*     */ 
/*     */           
/*     */           public void endStream() throws IOException {
/* 145 */             endStream(null);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/* 153 */     this.wrapped.failed(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 158 */     this.wrapped.releaseResources();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getDigest() {
/* 167 */     return this.digest;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/DigestingEntityProducer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */