/*     */ package org.apache.hc.core5.http.nio.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
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
/*     */ public final class PathEntityProducer
/*     */   implements AsyncEntityProducer
/*     */ {
/*     */   private static final int BUFFER_SIZE = 8192;
/*     */   private final Path file;
/*     */   private final OpenOption[] openOptions;
/*     */   private final ByteBuffer byteBuffer;
/*     */   private final long length;
/*     */   private final ContentType contentType;
/*     */   private final boolean chunked;
/*     */   private final AtomicReference<Exception> exception;
/*     */   private final AtomicReference<SeekableByteChannel> channelRef;
/*     */   private boolean eof;
/*     */   
/*     */   public PathEntityProducer(Path file, ContentType contentType, boolean chunked, OpenOption... openOptions) throws IOException {
/*  65 */     this(file, 8192, contentType, chunked, openOptions);
/*     */   }
/*     */ 
/*     */   
/*     */   public PathEntityProducer(Path file, ContentType contentType, OpenOption... openOptions) throws IOException {
/*  70 */     this(file, contentType, false, openOptions);
/*     */   }
/*     */ 
/*     */   
/*     */   public PathEntityProducer(Path file, int bufferSize, ContentType contentType, boolean chunked, OpenOption... openOptions) throws IOException {
/*  75 */     this.file = (Path)Args.notNull(file, "file");
/*  76 */     this.openOptions = openOptions;
/*  77 */     this.length = Files.size(file);
/*  78 */     this.byteBuffer = ByteBuffer.allocate(bufferSize);
/*  79 */     this.contentType = contentType;
/*  80 */     this.chunked = chunked;
/*  81 */     this.channelRef = new AtomicReference<>();
/*  82 */     this.exception = new AtomicReference<>();
/*     */   }
/*     */   
/*     */   public PathEntityProducer(Path file, OpenOption... openOptions) throws IOException {
/*  86 */     this(file, ContentType.APPLICATION_OCTET_STREAM, openOptions);
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/*  91 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/*  96 */     if (this.exception.compareAndSet(null, cause)) {
/*  97 */       releaseResources();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 108 */     return this.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 113 */     return (this.contentType != null) ? this.contentType.toString() : null;
/*     */   }
/*     */   
/*     */   public Exception getException() {
/* 117 */     return this.exception.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getTrailerNames() {
/* 122 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/* 127 */     return this.chunked;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/* 132 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void produce(DataStreamChannel dataStreamChannel) throws IOException {
/* 137 */     SeekableByteChannel seekableByteChannel = this.channelRef.get();
/* 138 */     if (seekableByteChannel == null) {
/* 139 */       seekableByteChannel = Files.newByteChannel(this.file, this.openOptions);
/* 140 */       Asserts.check((this.channelRef.getAndSet(seekableByteChannel) == null), "Illegal producer state");
/*     */     } 
/* 142 */     if (!this.eof) {
/* 143 */       int bytesRead = seekableByteChannel.read(this.byteBuffer);
/* 144 */       if (bytesRead < 0) {
/* 145 */         this.eof = true;
/*     */       }
/*     */     } 
/* 148 */     if (this.byteBuffer.position() > 0) {
/* 149 */       this.byteBuffer.flip();
/* 150 */       dataStreamChannel.write(this.byteBuffer);
/* 151 */       this.byteBuffer.compact();
/*     */     } 
/* 153 */     if (this.eof && this.byteBuffer.position() == 0) {
/* 154 */       dataStreamChannel.endStream();
/* 155 */       releaseResources();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 161 */     this.eof = false;
/* 162 */     Closer.closeQuietly(this.channelRef.getAndSet(null));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/PathEntityProducer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */