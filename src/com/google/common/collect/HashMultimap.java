/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public final class HashMultimap<K, V>
/*     */   extends HashMultimapGwtSerializationDependencies<K, V>
/*     */ {
/*     */   private static final int DEFAULT_VALUES_PER_KEY = 2;
/*     */   @VisibleForTesting
/*  58 */   transient int expectedValuesPerKey = 2;
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashMultimap<K, V> create() {
/*  68 */     return new HashMultimap<>();
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
/*     */   public static <K, V> HashMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) {
/*  85 */     return new HashMultimap<>(expectedKeys, expectedValuesPerKey);
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
/*     */   public static <K, V> HashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 100 */     return new HashMultimap<>(multimap);
/*     */   }
/*     */   
/*     */   private HashMultimap() {
/* 104 */     this(12, 2);
/*     */   }
/*     */   
/*     */   private HashMultimap(int expectedKeys, int expectedValuesPerKey) {
/* 108 */     super(Platform.newHashMapWithExpectedSize(expectedKeys));
/* 109 */     Preconditions.checkArgument((expectedValuesPerKey >= 0));
/* 110 */     this.expectedValuesPerKey = expectedValuesPerKey;
/*     */   }
/*     */   
/*     */   private HashMultimap(Multimap<? extends K, ? extends V> multimap) {
/* 114 */     super(Platform.newHashMapWithExpectedSize(multimap.keySet().size()));
/* 115 */     putAll(multimap);
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
/*     */   Set<V> createCollection() {
/* 127 */     return Platform.newHashSetWithExpectedSize(this.expectedValuesPerKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 137 */     stream.defaultWriteObject();
/* 138 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 144 */     stream.defaultReadObject();
/* 145 */     this.expectedValuesPerKey = 2;
/* 146 */     int distinctKeys = Serialization.readCount(stream);
/* 147 */     Map<K, Collection<V>> map = Platform.newHashMapWithExpectedSize(12);
/* 148 */     setMap(map);
/* 149 */     Serialization.populateMultimap(this, stream, distinctKeys);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/HashMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */