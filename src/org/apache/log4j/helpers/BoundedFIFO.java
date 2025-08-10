/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BoundedFIFO
/*     */ {
/*     */   LoggingEvent[] buf;
/*  35 */   int numElements = 0;
/*  36 */   int first = 0;
/*  37 */   int next = 0;
/*     */ 
/*     */   
/*     */   int maxSize;
/*     */ 
/*     */   
/*     */   public BoundedFIFO(int maxSize) {
/*  44 */     if (maxSize < 1) {
/*  45 */       throw new IllegalArgumentException("The maxSize argument (" + maxSize + ") is not a positive integer.");
/*     */     }
/*  47 */     this.maxSize = maxSize;
/*  48 */     this.buf = new LoggingEvent[maxSize];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggingEvent get() {
/*  56 */     if (this.numElements == 0) {
/*  57 */       return null;
/*     */     }
/*  59 */     LoggingEvent r = this.buf[this.first];
/*  60 */     this.buf[this.first] = null;
/*     */     
/*  62 */     if (++this.first == this.maxSize) {
/*  63 */       this.first = 0;
/*     */     }
/*  65 */     this.numElements--;
/*  66 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(LoggingEvent o) {
/*  75 */     if (this.numElements != this.maxSize) {
/*  76 */       this.buf[this.next] = o;
/*  77 */       if (++this.next == this.maxSize) {
/*  78 */         this.next = 0;
/*     */       }
/*  80 */       this.numElements++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxSize() {
/*  88 */     return this.maxSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFull() {
/*  96 */     return (this.numElements == this.maxSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 104 */     return this.numElements;
/*     */   }
/*     */   
/*     */   int min(int a, int b) {
/* 108 */     return (a < b) ? a : b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void resize(int newSize) {
/* 118 */     if (newSize == this.maxSize) {
/*     */       return;
/*     */     }
/* 121 */     LoggingEvent[] tmp = new LoggingEvent[newSize];
/*     */ 
/*     */     
/* 124 */     int len1 = this.maxSize - this.first;
/*     */ 
/*     */     
/* 127 */     len1 = min(len1, newSize);
/*     */ 
/*     */ 
/*     */     
/* 131 */     len1 = min(len1, this.numElements);
/*     */ 
/*     */ 
/*     */     
/* 135 */     System.arraycopy(this.buf, this.first, tmp, 0, len1);
/*     */ 
/*     */     
/* 138 */     int len2 = 0;
/* 139 */     if (len1 < this.numElements && len1 < newSize) {
/* 140 */       len2 = this.numElements - len1;
/* 141 */       len2 = min(len2, newSize - len1);
/* 142 */       System.arraycopy(this.buf, 0, tmp, len1, len2);
/*     */     } 
/*     */     
/* 145 */     this.buf = tmp;
/* 146 */     this.maxSize = newSize;
/* 147 */     this.first = 0;
/* 148 */     this.numElements = len1 + len2;
/* 149 */     this.next = this.numElements;
/* 150 */     if (this.next == this.maxSize) {
/* 151 */       this.next = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean wasEmpty() {
/* 160 */     return (this.numElements == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean wasFull() {
/* 168 */     return (this.numElements + 1 == this.maxSize);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/BoundedFIFO.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */