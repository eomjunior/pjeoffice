/*     */ package org.apache.hc.core5.http2.hpack;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ final class OutboundDynamicTable
/*     */ {
/*     */   private final StaticTable staticTable;
/*     */   private final FifoLinkedList headers;
/*     */   private final Map<String, LinkedList<HPackEntry>> mapByName;
/*     */   private int maxSize;
/*     */   private int currentSize;
/*     */   
/*     */   OutboundDynamicTable(StaticTable staticTable) {
/*  49 */     this.staticTable = staticTable;
/*  50 */     this.headers = new FifoLinkedList();
/*  51 */     this.mapByName = new HashMap<>();
/*  52 */     this.maxSize = Integer.MAX_VALUE;
/*  53 */     this.currentSize = 0;
/*     */   }
/*     */   
/*     */   OutboundDynamicTable() {
/*  57 */     this(StaticTable.INSTANCE);
/*     */   }
/*     */   
/*     */   public int getMaxSize() {
/*  61 */     return this.maxSize;
/*     */   }
/*     */   
/*     */   public void setMaxSize(int maxSize) {
/*  65 */     this.maxSize = maxSize;
/*  66 */     evict();
/*     */   }
/*     */   
/*     */   public int getCurrentSize() {
/*  70 */     return this.currentSize;
/*     */   }
/*     */   
/*     */   int staticLength() {
/*  74 */     return this.staticTable.length();
/*     */   }
/*     */   
/*     */   int dynamicLength() {
/*  78 */     return this.headers.size();
/*     */   }
/*     */   
/*     */   Header getDynamicEntry(int index) {
/*  82 */     return this.headers.get(index);
/*     */   }
/*     */   
/*     */   public int length() {
/*  86 */     return this.staticTable.length() + this.headers.size();
/*     */   }
/*     */   
/*     */   public Header getHeader(int index) {
/*  90 */     int length = length();
/*  91 */     Args.check((index >= 1), "index %s cannot be less then 1", Integer.valueOf(index));
/*  92 */     Args.check((index <= length), "index %s cannot be greater then length %s ", new Object[] { Integer.valueOf(index), Integer.valueOf(length) });
/*  93 */     return (index <= this.staticTable.length()) ? this.staticTable
/*  94 */       .get(index) : this.headers
/*  95 */       .get(index - this.staticTable.length() - 1);
/*     */   }
/*     */   
/*     */   public void add(HPackHeader header) {
/*  99 */     int entrySize = header.getTotalSize();
/* 100 */     if (entrySize > this.maxSize) {
/* 101 */       clear();
/* 102 */       this.mapByName.clear();
/*     */       return;
/*     */     } 
/* 105 */     String key = header.getName();
/* 106 */     FifoLinkedList.InternalNode node = this.headers.addFirst(header);
/* 107 */     LinkedList<HPackEntry> nodes = this.mapByName.computeIfAbsent(key, k -> new LinkedList());
/* 108 */     nodes.addFirst(node);
/* 109 */     this.currentSize += entrySize;
/* 110 */     evict();
/*     */   }
/*     */   
/*     */   private void clear() {
/* 114 */     this.currentSize = 0;
/* 115 */     this.headers.clear();
/*     */   }
/*     */   
/*     */   public List<HPackEntry> getByName(String key) {
/* 119 */     return this.mapByName.get(key);
/*     */   }
/*     */   
/*     */   private void evict() {
/* 123 */     while (this.currentSize > this.maxSize) {
/* 124 */       FifoLinkedList.InternalNode node = this.headers.removeLast();
/* 125 */       if (node != null) {
/* 126 */         HPackHeader header = node.getHeader();
/* 127 */         this.currentSize -= header.getTotalSize();
/*     */         
/* 129 */         String key = header.getName();
/* 130 */         LinkedList<HPackEntry> nodes = this.mapByName.get(key);
/* 131 */         if (nodes != null)
/* 132 */           nodes.remove(node); 
/*     */         continue;
/*     */       } 
/* 135 */       Asserts.check((this.currentSize == 0), "Current table size must be zero");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/hpack/OutboundDynamicTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */