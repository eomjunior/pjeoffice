/*    */ package io.reactivex.internal.util;
/*    */ 
/*    */ import io.reactivex.functions.BiFunction;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Comparator;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MergerBiFunction<T>
/*    */   implements BiFunction<List<T>, List<T>, List<T>>
/*    */ {
/*    */   final Comparator<? super T> comparator;
/*    */   
/*    */   public MergerBiFunction(Comparator<? super T> comparator) {
/* 29 */     this.comparator = comparator;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<T> apply(List<T> a, List<T> b) throws Exception {
/* 34 */     int n = a.size() + b.size();
/* 35 */     if (n == 0) {
/* 36 */       return new ArrayList<T>();
/*    */     }
/* 38 */     List<T> both = new ArrayList<T>(n);
/*    */     
/* 40 */     Iterator<T> at = a.iterator();
/* 41 */     Iterator<T> bt = b.iterator();
/*    */     
/* 43 */     E e1 = at.hasNext() ? (E)at.next() : null;
/* 44 */     E e2 = bt.hasNext() ? (E)bt.next() : null;
/*    */     
/* 46 */     while (e1 != null && e2 != null) {
/* 47 */       if (this.comparator.compare((T)e1, (T)e2) < 0) {
/* 48 */         both.add((T)e1);
/* 49 */         e1 = at.hasNext() ? (E)at.next() : null; continue;
/*    */       } 
/* 51 */       both.add((T)e2);
/* 52 */       e2 = bt.hasNext() ? (E)bt.next() : null;
/*    */     } 
/*    */ 
/*    */     
/* 56 */     if (e1 != null) {
/* 57 */       both.add((T)e1);
/* 58 */       while (at.hasNext()) {
/* 59 */         both.add(at.next());
/*    */       }
/*    */     } else {
/* 62 */       both.add((T)e2);
/* 63 */       while (bt.hasNext()) {
/* 64 */         both.add(bt.next());
/*    */       }
/*    */     } 
/*    */     
/* 68 */     return both;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/MergerBiFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */