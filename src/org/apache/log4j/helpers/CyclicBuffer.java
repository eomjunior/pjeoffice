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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CyclicBuffer
/*     */ {
/*     */   LoggingEvent[] ea;
/*     */   int first;
/*     */   int last;
/*     */   int numElems;
/*     */   int maxSize;
/*     */   
/*     */   public CyclicBuffer(int maxSize) throws IllegalArgumentException {
/*  51 */     if (maxSize < 1) {
/*  52 */       throw new IllegalArgumentException("The maxSize argument (" + maxSize + ") is not a positive integer.");
/*     */     }
/*  54 */     this.maxSize = maxSize;
/*  55 */     this.ea = new LoggingEvent[maxSize];
/*  56 */     this.first = 0;
/*  57 */     this.last = 0;
/*  58 */     this.numElems = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(LoggingEvent event) {
/*  66 */     this.ea[this.last] = event;
/*  67 */     if (++this.last == this.maxSize) {
/*  68 */       this.last = 0;
/*     */     }
/*  70 */     if (this.numElems < this.maxSize) {
/*  71 */       this.numElems++;
/*  72 */     } else if (++this.first == this.maxSize) {
/*  73 */       this.first = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggingEvent get(int i) {
/*  84 */     if (i < 0 || i >= this.numElems) {
/*  85 */       return null;
/*     */     }
/*  87 */     return this.ea[(this.first + i) % this.maxSize];
/*     */   }
/*     */   
/*     */   public int getMaxSize() {
/*  91 */     return this.maxSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoggingEvent get() {
/*  99 */     LoggingEvent r = null;
/*     */     
/* 101 */     this.numElems--;
/* 102 */     r = this.ea[this.first];
/* 103 */     this.ea[this.first] = null;
/* 104 */     if (this.numElems > 0 && ++this.first == this.maxSize) {
/* 105 */       this.first = 0;
/*     */     }
/* 107 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 115 */     return this.numElems;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resize(int newSize) {
/* 124 */     if (newSize < 0) {
/* 125 */       throw new IllegalArgumentException("Negative array size [" + newSize + "] not allowed.");
/*     */     }
/* 127 */     if (newSize == this.numElems) {
/*     */       return;
/*     */     }
/* 130 */     LoggingEvent[] temp = new LoggingEvent[newSize];
/*     */     
/* 132 */     int loopLen = (newSize < this.numElems) ? newSize : this.numElems;
/*     */     
/* 134 */     for (int i = 0; i < loopLen; i++) {
/* 135 */       temp[i] = this.ea[this.first];
/* 136 */       this.ea[this.first] = null;
/* 137 */       if (++this.first == this.numElems)
/* 138 */         this.first = 0; 
/*     */     } 
/* 140 */     this.ea = temp;
/* 141 */     this.first = 0;
/* 142 */     this.numElems = loopLen;
/* 143 */     this.maxSize = newSize;
/* 144 */     if (loopLen == newSize) {
/* 145 */       this.last = 0;
/*     */     } else {
/* 147 */       this.last = loopLen;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/CyclicBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */