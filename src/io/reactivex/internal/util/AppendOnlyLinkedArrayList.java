/*     */ package io.reactivex.internal.util;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.functions.BiPredicate;
/*     */ import io.reactivex.functions.Predicate;
/*     */ import org.reactivestreams.Subscriber;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AppendOnlyLinkedArrayList<T>
/*     */ {
/*     */   final int capacity;
/*     */   final Object[] head;
/*     */   Object[] tail;
/*     */   int offset;
/*     */   
/*     */   public AppendOnlyLinkedArrayList(int capacity) {
/*  37 */     this.capacity = capacity;
/*  38 */     this.head = new Object[capacity + 1];
/*  39 */     this.tail = this.head;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(T value) {
/*  48 */     int c = this.capacity;
/*  49 */     int o = this.offset;
/*  50 */     if (o == c) {
/*  51 */       Object[] next = new Object[c + 1];
/*  52 */       this.tail[c] = next;
/*  53 */       this.tail = next;
/*  54 */       o = 0;
/*     */     } 
/*  56 */     this.tail[o] = value;
/*  57 */     this.offset = o + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFirst(T value) {
/*  65 */     this.head[0] = value;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEachWhile(NonThrowingPredicate<? super T> consumer) {
/*  85 */     Object[] a = this.head;
/*  86 */     int c = this.capacity;
/*  87 */     while (a != null) {
/*  88 */       for (int i = 0; i < c; i++) {
/*  89 */         Object o = a[i];
/*  90 */         if (o == null) {
/*     */           break;
/*     */         }
/*  93 */         if (consumer.test((T)o)) {
/*     */           return;
/*     */         }
/*     */       } 
/*  97 */       a = (Object[])a[c];
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
/*     */   public <U> boolean accept(Subscriber<? super U> subscriber) {
/* 110 */     Object[] a = this.head;
/* 111 */     int c = this.capacity;
/* 112 */     while (a != null) {
/* 113 */       for (int i = 0; i < c; i++) {
/* 114 */         Object o = a[i];
/* 115 */         if (o == null) {
/*     */           break;
/*     */         }
/*     */         
/* 119 */         if (NotificationLite.acceptFull(o, subscriber)) {
/* 120 */           return true;
/*     */         }
/*     */       } 
/* 123 */       a = (Object[])a[c];
/*     */     } 
/* 125 */     return false;
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
/*     */   public <U> boolean accept(Observer<? super U> observer) {
/* 137 */     Object[] a = this.head;
/* 138 */     int c = this.capacity;
/* 139 */     while (a != null) {
/* 140 */       for (int i = 0; i < c; i++) {
/* 141 */         Object o = a[i];
/* 142 */         if (o == null) {
/*     */           break;
/*     */         }
/*     */         
/* 146 */         if (NotificationLite.acceptFull(o, observer)) {
/* 147 */           return true;
/*     */         }
/*     */       } 
/* 150 */       a = (Object[])a[c];
/*     */     } 
/* 152 */     return false;
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
/*     */   
/*     */   public <S> void forEachWhile(S state, BiPredicate<? super S, ? super T> consumer) throws Exception {
/* 165 */     Object[] a = this.head;
/* 166 */     int c = this.capacity;
/*     */     while (true) {
/* 168 */       for (int i = 0; i < c; i++) {
/* 169 */         Object o = a[i];
/* 170 */         if (o == null) {
/*     */           return;
/*     */         }
/* 173 */         if (consumer.test(state, o)) {
/*     */           return;
/*     */         }
/*     */       } 
/* 177 */       a = (Object[])a[c];
/*     */     } 
/*     */   }
/*     */   
/*     */   public static interface NonThrowingPredicate<T> extends Predicate<T> {
/*     */     boolean test(T param1T);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/AppendOnlyLinkedArrayList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */