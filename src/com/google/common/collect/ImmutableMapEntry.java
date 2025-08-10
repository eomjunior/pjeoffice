/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import javax.annotation.CheckForNull;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ class ImmutableMapEntry<K, V>
/*     */   extends ImmutableEntry<K, V>
/*     */ {
/*     */   static <K, V> ImmutableMapEntry<K, V>[] createEntryArray(int size) {
/*  49 */     return (ImmutableMapEntry<K, V>[])new ImmutableMapEntry[size];
/*     */   }
/*     */   
/*     */   ImmutableMapEntry(K key, V value) {
/*  53 */     super(key, value);
/*  54 */     CollectPreconditions.checkEntryNotNull(key, value);
/*     */   }
/*     */   
/*     */   ImmutableMapEntry(ImmutableMapEntry<K, V> contents) {
/*  58 */     super(contents.getKey(), contents.getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   ImmutableMapEntry<K, V> getNextInKeyBucket() {
/*  64 */     return null;
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   ImmutableMapEntry<K, V> getNextInValueBucket() {
/*  69 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isReusable() {
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class NonTerminalImmutableMapEntry<K, V>
/*     */     extends ImmutableMapEntry<K, V>
/*     */   {
/*     */     @CheckForNull
/*     */     private final transient ImmutableMapEntry<K, V> nextInKeyBucket;
/*     */ 
/*     */ 
/*     */     
/*     */     NonTerminalImmutableMapEntry(K key, V value, @CheckForNull ImmutableMapEntry<K, V> nextInKeyBucket) {
/*  91 */       super(key, value);
/*  92 */       this.nextInKeyBucket = nextInKeyBucket;
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     final ImmutableMapEntry<K, V> getNextInKeyBucket() {
/*  98 */       return this.nextInKeyBucket;
/*     */     }
/*     */ 
/*     */     
/*     */     final boolean isReusable() {
/* 103 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class NonTerminalImmutableBiMapEntry<K, V>
/*     */     extends NonTerminalImmutableMapEntry<K, V>
/*     */   {
/*     */     @CheckForNull
/*     */     private final transient ImmutableMapEntry<K, V> nextInValueBucket;
/*     */ 
/*     */     
/*     */     NonTerminalImmutableBiMapEntry(K key, V value, @CheckForNull ImmutableMapEntry<K, V> nextInKeyBucket, @CheckForNull ImmutableMapEntry<K, V> nextInValueBucket) {
/* 116 */       super(key, value, nextInKeyBucket);
/* 117 */       this.nextInValueBucket = nextInValueBucket;
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     ImmutableMapEntry<K, V> getNextInValueBucket() {
/* 123 */       return this.nextInValueBucket;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableMapEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */