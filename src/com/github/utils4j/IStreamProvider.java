/*    */ package com.github.utils4j;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
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
/*    */ public interface IStreamProvider<T>
/*    */ {
/*    */   Stream<T> stream();
/*    */   
/*    */   default Stream<T> filter(Predicate<T> filter) {
/* 37 */     return stream().filter(filter);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/IStreamProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */