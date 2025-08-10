/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import com.github.utils4j.IKeyValue;
/*    */ import java.util.function.Supplier;
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
/*    */ 
/*    */ 
/*    */ public final class Pair<K, V>
/*    */   implements IKeyValue<K, V>
/*    */ {
/*    */   private final K key;
/*    */   private final V value;
/*    */   
/*    */   public static <K, V> Pair<K, V> of(K key, V value) {
/* 42 */     return new Pair<>(key, value);
/*    */   }
/*    */   
/*    */   public static <K, V> Pair<K, V> of(K k, Supplier<V> v) {
/* 46 */     Args.requireNonNull(v, "key supplier is null");
/* 47 */     return of(k, v.get());
/*    */   }
/*    */   
/*    */   public static <K, V> Pair<K, V> of(Supplier<K> k, V v) {
/* 51 */     Args.requireNonNull(k, "key supplier is null");
/* 52 */     return of(k.get(), v);
/*    */   }
/*    */   
/*    */   public static <K, V> Pair<K, V> of(Supplier<K> k, Supplier<V> v) {
/* 56 */     Args.requireNonNull(k, "key supplier is null");
/* 57 */     Args.requireNonNull(v, "value supplier is null");
/* 58 */     return of(k.get(), v.get());
/*    */   }
/*    */   
/*    */   private Pair(K key, V value) {
/* 62 */     this.key = key;
/* 63 */     this.value = value;
/*    */   }
/*    */   
/*    */   public Stream<V> valueStream() {
/* 67 */     return Stream.of(getValue());
/*    */   }
/*    */   
/*    */   public Stream<K> keyStream() {
/* 71 */     return Stream.of(getKey());
/*    */   }
/*    */ 
/*    */   
/*    */   public K getKey() {
/* 76 */     return this.key;
/*    */   }
/*    */ 
/*    */   
/*    */   public V getValue() {
/* 81 */     return this.value;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Pair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */