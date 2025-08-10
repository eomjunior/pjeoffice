/*     */ package org.apache.hc.core5.http.io.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpEntity;
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
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public final class NullEntity
/*     */   implements HttpEntity
/*     */ {
/*  52 */   public static final NullEntity INSTANCE = new NullEntity();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  58 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException, UnsupportedOperationException {
/*  63 */     return EmptyInputStream.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outStream) throws IOException {}
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/*  71 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Supplier<List<? extends Header>> getTrailers() {
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {}
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/*  84 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentType() {
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getTrailerNames() {
/* 104 */     return Collections.emptySet();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/entity/NullEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */