/*     */ package com.itextpdf.text.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GroupedRandomAccessSource
/*     */   implements RandomAccessSource
/*     */ {
/*     */   private final SourceEntry[] sources;
/*     */   private SourceEntry currentSourceEntry;
/*     */   private final long size;
/*     */   
/*     */   public GroupedRandomAccessSource(RandomAccessSource[] sources) throws IOException {
/*  73 */     this.sources = new SourceEntry[sources.length];
/*     */     
/*  75 */     long totalSize = 0L;
/*  76 */     for (int i = 0; i < sources.length; i++) {
/*  77 */       this.sources[i] = new SourceEntry(i, sources[i], totalSize);
/*  78 */       totalSize += sources[i].length();
/*     */     } 
/*  80 */     this.size = totalSize;
/*  81 */     this.currentSourceEntry = this.sources[sources.length - 1];
/*  82 */     sourceInUse(this.currentSourceEntry.source);
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
/*     */   protected int getStartingSourceIndex(long offset) {
/*  94 */     if (offset >= this.currentSourceEntry.firstByte) {
/*  95 */       return this.currentSourceEntry.index;
/*     */     }
/*  97 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SourceEntry getSourceEntryForOffset(long offset) throws IOException {
/* 108 */     if (offset >= this.size) {
/* 109 */       return null;
/*     */     }
/* 111 */     if (offset >= this.currentSourceEntry.firstByte && offset <= this.currentSourceEntry.lastByte) {
/* 112 */       return this.currentSourceEntry;
/*     */     }
/*     */     
/* 115 */     sourceReleased(this.currentSourceEntry.source);
/*     */     
/* 117 */     int startAt = getStartingSourceIndex(offset);
/*     */     
/* 119 */     for (int i = startAt; i < this.sources.length; i++) {
/* 120 */       if (offset >= (this.sources[i]).firstByte && offset <= (this.sources[i]).lastByte) {
/* 121 */         this.currentSourceEntry = this.sources[i];
/* 122 */         sourceInUse(this.currentSourceEntry.source);
/* 123 */         return this.currentSourceEntry;
/*     */       } 
/*     */     } 
/*     */     
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sourceReleased(RandomAccessSource source) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sourceInUse(RandomAccessSource source) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int get(long position) throws IOException {
/* 155 */     SourceEntry entry = getSourceEntryForOffset(position);
/*     */     
/* 157 */     if (entry == null) {
/* 158 */       return -1;
/*     */     }
/* 160 */     return entry.source.get(entry.offsetN(position));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int get(long position, byte[] bytes, int off, int len) throws IOException {
/* 168 */     SourceEntry entry = getSourceEntryForOffset(position);
/*     */     
/* 170 */     if (entry == null) {
/* 171 */       return -1;
/*     */     }
/* 173 */     long offN = entry.offsetN(position);
/*     */     
/* 175 */     int remaining = len;
/*     */     
/* 177 */     while (remaining > 0 && 
/* 178 */       entry != null) {
/*     */       
/* 180 */       if (offN > entry.source.length()) {
/*     */         break;
/*     */       }
/* 183 */       int count = entry.source.get(offN, bytes, off, remaining);
/* 184 */       if (count == -1) {
/*     */         break;
/*     */       }
/* 187 */       off += count;
/* 188 */       position += count;
/* 189 */       remaining -= count;
/*     */       
/* 191 */       offN = 0L;
/* 192 */       entry = getSourceEntryForOffset(position);
/*     */     } 
/* 194 */     return (remaining == len) ? -1 : (len - remaining);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long length() {
/* 202 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 210 */     for (SourceEntry entry : this.sources) {
/* 211 */       entry.source.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SourceEntry
/*     */   {
/*     */     final RandomAccessSource source;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final long firstByte;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final long lastByte;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final int index;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SourceEntry(int index, RandomAccessSource source, long offset) {
/* 243 */       this.index = index;
/* 244 */       this.source = source;
/* 245 */       this.firstByte = offset;
/* 246 */       this.lastByte = offset + source.length() - 1L;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long offsetN(long absoluteOffset) {
/* 255 */       return absoluteOffset - this.firstByte;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/io/GroupedRandomAccessSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */