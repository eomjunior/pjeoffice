/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ final class Platform
/*     */ {
/*     */   static <K, V> Map<K, V> newHashMapWithExpectedSize(int expectedSize) {
/*  39 */     return Maps.newHashMapWithExpectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> Map<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
/*  48 */     return Maps.newLinkedHashMapWithExpectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> Set<E> newHashSetWithExpectedSize(int expectedSize) {
/*  53 */     return Sets.newHashSetWithExpectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> Set<E> newConcurrentHashSet() {
/*  58 */     return ConcurrentHashMap.newKeySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> Set<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
/*  66 */     return Sets.newLinkedHashSetWithExpectedSize(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> Map<K, V> preservesInsertionOrderOnPutsMap() {
/*  75 */     return Maps.newLinkedHashMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> Set<E> preservesInsertionOrderOnAddsSet() {
/*  83 */     return CompactHashSet.create();
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
/*     */   static <T> T[] newArray(T[] reference, int length) {
/*  99 */     T[] empty = (reference.length == 0) ? reference : Arrays.<T>copyOf(reference, 0);
/* 100 */     return Arrays.copyOf(empty, length);
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
/*     */   static <T> T[] copy(Object[] source, int from, int to, T[] arrayOfType) {
/* 114 */     return Arrays.copyOfRange(source, from, to, (Class)arrayOfType.getClass());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   static MapMaker tryWeakKeys(MapMaker mapMaker) {
/* 124 */     return mapMaker.weakKeys();
/*     */   }
/*     */   
/*     */   static <E extends Enum<E>> Class<E> getDeclaringClassOrObjectForJ2cl(E e) {
/* 128 */     return e.getDeclaringClass();
/*     */   }
/*     */   
/*     */   static int reduceIterationsIfGwt(int iterations) {
/* 132 */     return iterations;
/*     */   }
/*     */   
/*     */   static int reduceExponentIfGwt(int exponent) {
/* 136 */     return exponent;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Platform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */