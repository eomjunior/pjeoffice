/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ @J2ktIncompatible
/*     */ public final class EnumBiMap<K extends Enum<K>, V extends Enum<V>>
/*     */   extends AbstractBiMap<K, V>
/*     */ {
/*     */   transient Class<K> keyTypeOrObjectUnderJ2cl;
/*     */   transient Class<V> valueTypeOrObjectUnderJ2cl;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Class<K> keyType, Class<V> valueType) {
/*  72 */     return new EnumBiMap<>(keyType, valueType);
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
/*     */   public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Map<K, V> map) {
/*  86 */     EnumBiMap<K, V> bimap = create(inferKeyTypeOrObjectUnderJ2cl(map), inferValueTypeOrObjectUnderJ2cl(map));
/*  87 */     bimap.putAll(map);
/*  88 */     return bimap;
/*     */   }
/*     */   
/*     */   private EnumBiMap(Class<K> keyTypeOrObjectUnderJ2cl, Class<V> valueTypeOrObjectUnderJ2cl) {
/*  92 */     super(new EnumMap<>(keyTypeOrObjectUnderJ2cl), (Map)new EnumMap<>(valueTypeOrObjectUnderJ2cl));
/*     */     
/*  94 */     this.keyTypeOrObjectUnderJ2cl = keyTypeOrObjectUnderJ2cl;
/*  95 */     this.valueTypeOrObjectUnderJ2cl = valueTypeOrObjectUnderJ2cl;
/*     */   }
/*     */   
/*     */   static <K extends Enum<K>> Class<K> inferKeyTypeOrObjectUnderJ2cl(Map<K, ?> map) {
/*  99 */     if (map instanceof EnumBiMap) {
/* 100 */       return ((EnumBiMap)map).keyTypeOrObjectUnderJ2cl;
/*     */     }
/* 102 */     if (map instanceof EnumHashBiMap) {
/* 103 */       return ((EnumHashBiMap)map).keyTypeOrObjectUnderJ2cl;
/*     */     }
/* 105 */     Preconditions.checkArgument(!map.isEmpty());
/* 106 */     return Platform.getDeclaringClassOrObjectForJ2cl((K)map.keySet().iterator().next());
/*     */   }
/*     */   
/*     */   private static <V extends Enum<V>> Class<V> inferValueTypeOrObjectUnderJ2cl(Map<?, V> map) {
/* 110 */     if (map instanceof EnumBiMap) {
/* 111 */       return ((EnumBiMap)map).valueTypeOrObjectUnderJ2cl;
/*     */     }
/* 113 */     Preconditions.checkArgument(!map.isEmpty());
/* 114 */     return Platform.getDeclaringClassOrObjectForJ2cl((V)map.values().iterator().next());
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public Class<K> keyType() {
/* 120 */     return this.keyTypeOrObjectUnderJ2cl;
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public Class<V> valueType() {
/* 126 */     return this.valueTypeOrObjectUnderJ2cl;
/*     */   }
/*     */ 
/*     */   
/*     */   K checkKey(K key) {
/* 131 */     return (K)Preconditions.checkNotNull(key);
/*     */   }
/*     */ 
/*     */   
/*     */   V checkValue(V value) {
/* 136 */     return (V)Preconditions.checkNotNull(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 145 */     stream.defaultWriteObject();
/* 146 */     stream.writeObject(this.keyTypeOrObjectUnderJ2cl);
/* 147 */     stream.writeObject(this.valueTypeOrObjectUnderJ2cl);
/* 148 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 154 */     stream.defaultReadObject();
/* 155 */     this.keyTypeOrObjectUnderJ2cl = (Class<K>)Objects.<Object>requireNonNull(stream.readObject());
/* 156 */     this.valueTypeOrObjectUnderJ2cl = (Class<V>)Objects.<Object>requireNonNull(stream.readObject());
/* 157 */     setDelegates(new EnumMap<>(this.keyTypeOrObjectUnderJ2cl), (Map)new EnumMap<>(this.valueTypeOrObjectUnderJ2cl));
/*     */     
/* 159 */     Serialization.populateMap(this, stream);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/EnumBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */