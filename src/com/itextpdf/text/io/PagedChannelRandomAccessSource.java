/*     */ package com.itextpdf.text.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PagedChannelRandomAccessSource
/*     */   extends GroupedRandomAccessSource
/*     */   implements RandomAccessSource
/*     */ {
/*     */   public static final int DEFAULT_TOTAL_BUFSIZE = 67108864;
/*     */   public static final int DEFAULT_MAX_OPEN_BUFFERS = 16;
/*     */   private final int bufferSize;
/*     */   private final FileChannel channel;
/*     */   private final MRU<RandomAccessSource> mru;
/*     */   
/*     */   public PagedChannelRandomAccessSource(FileChannel channel) throws IOException {
/*  84 */     this(channel, 67108864, 16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PagedChannelRandomAccessSource(FileChannel channel, int totalBufferSize, int maxOpenBuffers) throws IOException {
/*  94 */     super(buildSources(channel, totalBufferSize / maxOpenBuffers));
/*  95 */     this.channel = channel;
/*  96 */     this.bufferSize = totalBufferSize / maxOpenBuffers;
/*  97 */     this.mru = new MRU<RandomAccessSource>(maxOpenBuffers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static RandomAccessSource[] buildSources(FileChannel channel, int bufferSize) throws IOException {
/* 108 */     long size = channel.size();
/* 109 */     if (size <= 0L) {
/* 110 */       throw new IOException("File size must be greater than zero");
/*     */     }
/* 112 */     int bufferCount = (int)(size / bufferSize) + ((size % bufferSize == 0L) ? 0 : 1);
/*     */     
/* 114 */     MappedChannelRandomAccessSource[] sources = new MappedChannelRandomAccessSource[bufferCount];
/* 115 */     for (int i = 0; i < bufferCount; i++) {
/* 116 */       long pageOffset = i * bufferSize;
/* 117 */       long pageLength = Math.min(size - pageOffset, bufferSize);
/* 118 */       sources[i] = new MappedChannelRandomAccessSource(channel, pageOffset, pageLength);
/*     */     } 
/* 120 */     return (RandomAccessSource[])sources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getStartingSourceIndex(long offset) {
/* 129 */     return (int)(offset / this.bufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sourceReleased(RandomAccessSource source) throws IOException {
/* 138 */     RandomAccessSource old = this.mru.enqueue(source);
/* 139 */     if (old != null) {
/* 140 */       old.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sourceInUse(RandomAccessSource source) throws IOException {
/* 149 */     ((MappedChannelRandomAccessSource)source).open();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 158 */     super.close();
/* 159 */     this.channel.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MRU<E>
/*     */   {
/*     */     private final int limit;
/*     */ 
/*     */ 
/*     */     
/* 171 */     private LinkedList<E> queue = new LinkedList<E>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MRU(int limit) {
/* 178 */       this.limit = limit;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public E enqueue(E newElement) {
/* 188 */       if (this.queue.size() > 0 && this.queue.getFirst() == newElement) {
/* 189 */         return null;
/*     */       }
/* 191 */       for (Iterator<E> it = this.queue.iterator(); it.hasNext(); ) {
/* 192 */         E element = it.next();
/* 193 */         if (newElement == element) {
/* 194 */           it.remove();
/* 195 */           this.queue.addFirst(newElement);
/* 196 */           return null;
/*     */         } 
/*     */       } 
/* 199 */       this.queue.addFirst(newElement);
/*     */       
/* 201 */       if (this.queue.size() > this.limit) {
/* 202 */         return this.queue.removeLast();
/*     */       }
/* 204 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/io/PagedChannelRandomAccessSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */