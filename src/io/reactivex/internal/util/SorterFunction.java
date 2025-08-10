/*    */ package io.reactivex.internal.util;
/*    */ 
/*    */ import io.reactivex.functions.Function;
/*    */ import java.util.Collections;
/*    */ import java.util.Comparator;
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
/*    */ public final class SorterFunction<T>
/*    */   implements Function<List<T>, List<T>>
/*    */ {
/*    */   final Comparator<? super T> comparator;
/*    */   
/*    */   public SorterFunction(Comparator<? super T> comparator) {
/* 25 */     this.comparator = comparator;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<T> apply(List<T> t) throws Exception {
/* 30 */     Collections.sort(t, this.comparator);
/* 31 */     return t;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/SorterFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */