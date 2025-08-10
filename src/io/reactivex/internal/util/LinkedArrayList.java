/*     */ package io.reactivex.internal.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinkedArrayList
/*     */ {
/*     */   final int capacityHint;
/*     */   Object[] head;
/*     */   Object[] tail;
/*     */   volatile int size;
/*     */   int indexInTail;
/*     */   
/*     */   public LinkedArrayList(int capacityHint) {
/*  46 */     this.capacityHint = capacityHint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Object o) {
/*  54 */     if (this.size == 0) {
/*  55 */       this.head = new Object[this.capacityHint + 1];
/*  56 */       this.tail = this.head;
/*  57 */       this.head[0] = o;
/*  58 */       this.indexInTail = 1;
/*  59 */       this.size = 1;
/*     */     
/*     */     }
/*  62 */     else if (this.indexInTail == this.capacityHint) {
/*  63 */       Object[] t = new Object[this.capacityHint + 1];
/*  64 */       t[0] = o;
/*  65 */       this.tail[this.capacityHint] = t;
/*  66 */       this.tail = t;
/*  67 */       this.indexInTail = 1;
/*  68 */       this.size++;
/*     */     } else {
/*  70 */       this.tail[this.indexInTail] = o;
/*  71 */       this.indexInTail++;
/*  72 */       this.size++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] head() {
/*  80 */     return this.head;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  88 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  93 */     int cap = this.capacityHint;
/*  94 */     int s = this.size;
/*  95 */     List<Object> list = new ArrayList(s + 1);
/*     */     
/*  97 */     Object[] h = head();
/*  98 */     int j = 0;
/*  99 */     int k = 0;
/* 100 */     while (j < s) {
/* 101 */       list.add(h[k]);
/* 102 */       j++;
/* 103 */       if (++k == cap) {
/* 104 */         k = 0;
/* 105 */         h = (Object[])h[cap];
/*     */       } 
/*     */     } 
/*     */     
/* 109 */     return list.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/LinkedArrayList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */