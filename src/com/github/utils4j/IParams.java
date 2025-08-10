/*    */ package com.github.utils4j;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Supplier;
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
/*    */ public interface IParams
/*    */ {
/*    */   IParam get(String paramString);
/*    */   
/*    */   default boolean isPresent(String key) {
/* 37 */     return get(key).isPresent();
/*    */   }
/*    */   
/*    */   default <T> T getValue(String key) {
/* 41 */     return get(key).get();
/*    */   }
/*    */   
/*    */   default <T> T orElse(String key, T value) {
/* 45 */     return get(key).orElse(value);
/*    */   }
/*    */   
/*    */   default <T> T orElseGet(String key, Supplier<? extends T> other) {
/* 49 */     return get(key).orElseGet(other);
/*    */   }
/*    */   
/*    */   default <T> void ifPresent(String key, Consumer<T> consumer) {
/* 53 */     get(key).ifPresent(consumer);
/*    */   }
/*    */   
/*    */   default <X extends Throwable, T> T orElseThrow(String key, Supplier<? extends X> supplier) throws X {
/* 57 */     return get(key).orElseThrow(supplier);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/IParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */