/*     */ package org.apache.hc.core5.http.nio.entity;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.io.Closer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FileEntityProducer
/*     */   implements AsyncEntityProducer
/*     */ {
/*     */   private final File file;
/*     */   private final ByteBuffer byteBuffer;
/*     */   private final long length;
/*     */   private final ContentType contentType;
/*     */   private final boolean chunked;
/*     */   private final AtomicReference<Exception> exception;
/*     */   private final AtomicReference<RandomAccessFile> accessFileRef;
/*     */   private boolean eof;
/*     */   
/*     */   public FileEntityProducer(File file, int bufferSize, ContentType contentType, boolean chunked) {
/*  61 */     this.file = (File)Args.notNull(file, "File");
/*  62 */     this.length = file.length();
/*  63 */     this.byteBuffer = ByteBuffer.allocate(bufferSize);
/*  64 */     this.contentType = contentType;
/*  65 */     this.chunked = chunked;
/*  66 */     this.accessFileRef = new AtomicReference<>();
/*  67 */     this.exception = new AtomicReference<>();
/*     */   }
/*     */   
/*     */   public FileEntityProducer(File file, ContentType contentType, boolean chunked) {
/*  71 */     this(file, 8192, contentType, chunked);
/*     */   }
/*     */   
/*     */   public FileEntityProducer(File file, ContentType contentType) {
/*  75 */     this(file, contentType, false);
/*     */   }
/*     */   
/*     */   public FileEntityProducer(File file) {
/*  79 */     this(file, ContentType.APPLICATION_OCTET_STREAM);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentType() {
/*  89 */     return (this.contentType != null) ? this.contentType.toString() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/*  94 */     return this.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/*  99 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/* 104 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/* 109 */     return this.chunked;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getTrailerNames() {
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void produce(DataStreamChannel channel) throws IOException {
/* 120 */     RandomAccessFile accessFile = this.accessFileRef.get();
/* 121 */     if (accessFile == null) {
/* 122 */       accessFile = new RandomAccessFile(this.file, "r");
/* 123 */       Asserts.check((this.accessFileRef.getAndSet(accessFile) == null), "Illegal producer state");
/*     */     } 
/* 125 */     if (!this.eof) {
/* 126 */       int bytesRead = accessFile.getChannel().read(this.byteBuffer);
/* 127 */       if (bytesRead < 0) {
/* 128 */         this.eof = true;
/*     */       }
/*     */     } 
/* 131 */     if (this.byteBuffer.position() > 0) {
/* 132 */       this.byteBuffer.flip();
/* 133 */       channel.write(this.byteBuffer);
/* 134 */       this.byteBuffer.compact();
/*     */     } 
/* 136 */     if (this.eof && this.byteBuffer.position() == 0) {
/* 137 */       channel.endStream();
/* 138 */       releaseResources();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/* 144 */     if (this.exception.compareAndSet(null, cause)) {
/* 145 */       releaseResources();
/*     */     }
/*     */   }
/*     */   
/*     */   public Exception getException() {
/* 150 */     return this.exception.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 155 */     this.eof = false;
/* 156 */     Closer.closeQuietly(this.accessFileRef.getAndSet(null));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/FileEntityProducer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */