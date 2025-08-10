/*     */ package com.itextpdf.text.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.FileChannel;
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
/*     */ class MappedChannelRandomAccessSource
/*     */   implements RandomAccessSource
/*     */ {
/*     */   private final FileChannel channel;
/*     */   private final long offset;
/*     */   private final long length;
/*     */   private ByteBufferRandomAccessSource source;
/*     */   
/*     */   public MappedChannelRandomAccessSource(FileChannel channel, long offset, long length) {
/*  81 */     if (offset < 0L)
/*  82 */       throw new IllegalArgumentException(offset + " is negative"); 
/*  83 */     if (length <= 0L) {
/*  84 */       throw new IllegalArgumentException(length + " is zero or negative");
/*     */     }
/*  86 */     this.channel = channel;
/*  87 */     this.offset = offset;
/*  88 */     this.length = length;
/*  89 */     this.source = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void open() throws IOException {
/*  97 */     if (this.source != null) {
/*     */       return;
/*     */     }
/* 100 */     if (!this.channel.isOpen()) {
/* 101 */       throw new IllegalStateException("Channel is closed");
/*     */     }
/*     */     try {
/* 104 */       this.source = new ByteBufferRandomAccessSource(this.channel.map(FileChannel.MapMode.READ_ONLY, this.offset, this.length));
/* 105 */     } catch (IOException e) {
/* 106 */       if (exceptionIsMapFailureException(e))
/* 107 */         throw new MapFailedException(e); 
/* 108 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean exceptionIsMapFailureException(IOException e) {
/* 121 */     if (e.getMessage() != null && e.getMessage().indexOf("Map failed") >= 0) {
/* 122 */       return true;
/*     */     }
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int get(long position) throws IOException {
/* 131 */     if (this.source == null)
/* 132 */       throw new IOException("RandomAccessSource not opened"); 
/* 133 */     return this.source.get(position);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int get(long position, byte[] bytes, int off, int len) throws IOException {
/* 140 */     if (this.source == null)
/* 141 */       throw new IOException("RandomAccessSource not opened"); 
/* 142 */     return this.source.get(position, bytes, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long length() {
/* 149 */     return this.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 156 */     if (this.source == null)
/*     */       return; 
/* 158 */     this.source.close();
/* 159 */     this.source = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 164 */     return getClass().getName() + " (" + this.offset + ", " + this.length + ")";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/io/MappedChannelRandomAccessSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */