/*     */ package org.apache.hc.core5.http2.hpack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class FifoBuffer
/*     */ {
/*     */   private HPackHeader[] array;
/*     */   private int head;
/*     */   private int tail;
/*     */   
/*     */   FifoBuffer(int initialCapacity) {
/*  39 */     this.array = new HPackHeader[initialCapacity];
/*  40 */     this.head = 0;
/*  41 */     this.tail = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private void expand() {
/*  46 */     int newcapacity = this.array.length + 1 << 1;
/*  47 */     if (newcapacity < 0) {
/*  48 */       newcapacity = Integer.MAX_VALUE;
/*     */     }
/*  50 */     HPackHeader[] arrayOfHPackHeader1 = this.array;
/*  51 */     int len = arrayOfHPackHeader1.length;
/*  52 */     HPackHeader[] newArray = new HPackHeader[newcapacity];
/*  53 */     System.arraycopy(arrayOfHPackHeader1, this.head, newArray, 0, len - this.head);
/*  54 */     System.arraycopy(arrayOfHPackHeader1, 0, newArray, len - this.head, this.head);
/*  55 */     this.array = newArray;
/*  56 */     this.head = len;
/*  57 */     this.tail = 0;
/*     */   }
/*     */   
/*     */   public void clear() {
/*  61 */     this.head = 0;
/*  62 */     this.tail = 0;
/*     */   }
/*     */   
/*     */   public void addFirst(HPackHeader header) {
/*  66 */     this.array[this.head++] = header;
/*  67 */     if (this.head == this.array.length) {
/*  68 */       this.head = 0;
/*     */     }
/*  70 */     if (this.head == this.tail) {
/*  71 */       expand();
/*     */     }
/*     */   }
/*     */   
/*     */   public HPackHeader get(int index) {
/*  76 */     int i = this.head - index - 1;
/*  77 */     if (i < 0) {
/*  78 */       i = this.array.length + i;
/*     */     }
/*  80 */     return this.array[i];
/*     */   }
/*     */   
/*     */   public HPackHeader getFirst() {
/*  84 */     return this.array[(this.head > 0) ? (this.head - 1) : (this.array.length - 1)];
/*     */   }
/*     */   
/*     */   public HPackHeader getLast() {
/*  88 */     return this.array[this.tail];
/*     */   }
/*     */   
/*     */   public HPackHeader removeLast() {
/*  92 */     HPackHeader header = this.array[this.tail];
/*  93 */     if (header != null) {
/*  94 */       this.array[this.tail++] = null;
/*  95 */       if (this.tail == this.array.length) {
/*  96 */         this.tail = 0;
/*     */       }
/*     */     } 
/*  99 */     return header;
/*     */   }
/*     */   
/*     */   public int capacity() {
/* 103 */     return this.array.length;
/*     */   }
/*     */   
/*     */   public int size() {
/* 107 */     int i = this.head - this.tail;
/* 108 */     if (i < 0) {
/* 109 */       i = this.array.length + i;
/*     */     }
/* 111 */     return i;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/hpack/FifoBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */