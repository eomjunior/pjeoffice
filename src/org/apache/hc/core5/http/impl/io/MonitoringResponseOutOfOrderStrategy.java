/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.io.HttpClientConnection;
/*     */ import org.apache.hc.core5.http.io.ResponseOutOfOrderStrategy;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Timeout;
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
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public final class MonitoringResponseOutOfOrderStrategy
/*     */   implements ResponseOutOfOrderStrategy
/*     */ {
/*     */   private static final int DEFAULT_CHUNK_SIZE = 8192;
/*  54 */   public static final MonitoringResponseOutOfOrderStrategy INSTANCE = new MonitoringResponseOutOfOrderStrategy();
/*     */ 
/*     */   
/*     */   private final long chunkSize;
/*     */   
/*     */   private final long maxChunksToCheck;
/*     */ 
/*     */   
/*     */   public MonitoringResponseOutOfOrderStrategy() {
/*  63 */     this(8192L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MonitoringResponseOutOfOrderStrategy(long chunkSize) {
/*  72 */     this(chunkSize, Long.MAX_VALUE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MonitoringResponseOutOfOrderStrategy(long chunkSize, long maxChunksToCheck) {
/*  83 */     this.chunkSize = Args.positive(chunkSize, "chunkSize");
/*  84 */     this.maxChunksToCheck = Args.positive(maxChunksToCheck, "maxChunksToCheck");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEarlyResponseDetected(ClassicHttpRequest request, HttpClientConnection connection, InputStream inputStream, long totalBytesSent, long nextWriteSize) throws IOException {
/*  94 */     if (nextWriteStartsNewChunk(totalBytesSent, nextWriteSize)) {
/*  95 */       boolean ssl = (connection.getSSLSession() != null);
/*  96 */       return ssl ? connection.isDataAvailable(Timeout.ONE_MILLISECOND) : ((inputStream.available() > 0));
/*     */     } 
/*  98 */     return false;
/*     */   }
/*     */   
/*     */   private boolean nextWriteStartsNewChunk(long totalBytesSent, long nextWriteSize) {
/* 102 */     long currentChunkIndex = Math.min(totalBytesSent / this.chunkSize, this.maxChunksToCheck);
/* 103 */     long newChunkIndex = Math.min((totalBytesSent + nextWriteSize) / this.chunkSize, this.maxChunksToCheck);
/* 104 */     return (currentChunkIndex < newChunkIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 109 */     return "DefaultResponseOutOfOrderStrategy{chunkSize=" + this.chunkSize + ", maxChunksToCheck=" + this.maxChunksToCheck + '}';
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/MonitoringResponseOutOfOrderStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */