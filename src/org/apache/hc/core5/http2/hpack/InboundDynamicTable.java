/*     */ package org.apache.hc.core5.http2.hpack;
/*     */ 
/*     */ import org.apache.hc.core5.http.Header;
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
/*     */ final class InboundDynamicTable
/*     */ {
/*     */   private final StaticTable staticTable;
/*     */   private final FifoBuffer headers;
/*     */   private int maxSize;
/*     */   private int currentSize;
/*     */   
/*     */   InboundDynamicTable(StaticTable staticTable) {
/*  43 */     this.staticTable = staticTable;
/*  44 */     this.headers = new FifoBuffer(256);
/*  45 */     this.maxSize = Integer.MAX_VALUE;
/*  46 */     this.currentSize = 0;
/*     */   }
/*     */   
/*     */   InboundDynamicTable() {
/*  50 */     this(StaticTable.INSTANCE);
/*     */   }
/*     */   
/*     */   public int getMaxSize() {
/*  54 */     return this.maxSize;
/*     */   }
/*     */   
/*     */   public void setMaxSize(int maxSize) {
/*  58 */     this.maxSize = maxSize;
/*  59 */     evict();
/*     */   }
/*     */   
/*     */   public int getCurrentSize() {
/*  63 */     return this.currentSize;
/*     */   }
/*     */   
/*     */   int staticLength() {
/*  67 */     return this.staticTable.length();
/*     */   }
/*     */   
/*     */   int dynamicLength() {
/*  71 */     return this.headers.size();
/*     */   }
/*     */   
/*     */   Header getDynamicEntry(int index) {
/*  75 */     return this.headers.get(index);
/*     */   }
/*     */   
/*     */   public int length() {
/*  79 */     return this.staticTable.length() + this.headers.size();
/*     */   }
/*     */   
/*     */   public HPackHeader getHeader(int index) {
/*  83 */     int length = length();
/*  84 */     Args.check((index >= 1), "index %s cannot be less than 1", Integer.valueOf(index));
/*  85 */     Args.check((index <= length), "length %s cannot be greater than index %s", new Object[] { Integer.valueOf(length), Integer.valueOf(index) });
/*  86 */     return (index <= this.staticTable.length()) ? this.staticTable
/*  87 */       .get(index) : this.headers
/*  88 */       .get(index - this.staticTable.length() - 1);
/*     */   }
/*     */   
/*     */   public void add(HPackHeader header) {
/*  92 */     int entrySize = header.getTotalSize();
/*  93 */     if (entrySize > this.maxSize) {
/*  94 */       clear();
/*     */       return;
/*     */     } 
/*  97 */     this.headers.addFirst(header);
/*  98 */     this.currentSize += entrySize;
/*  99 */     evict();
/*     */   }
/*     */   
/*     */   private void clear() {
/* 103 */     this.currentSize = 0;
/* 104 */     this.headers.clear();
/*     */   }
/*     */   
/*     */   private void evict() {
/* 108 */     while (this.currentSize > this.maxSize) {
/* 109 */       HPackHeader header = this.headers.removeLast();
/* 110 */       if (header != null) {
/* 111 */         this.currentSize -= header.getTotalSize(); continue;
/*     */       } 
/* 113 */       Asserts.check((this.currentSize == 0), "Current table size must be zero");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/hpack/InboundDynamicTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */