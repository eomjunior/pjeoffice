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
/*     */ import java.util.HashMap;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ @J2ktIncompatible
/*     */ public final class EnumHashBiMap<K extends Enum<K>, V>
/*     */   extends AbstractBiMap<K, V>
/*     */ {
/*     */   transient Class<K> keyTypeOrObjectUnderJ2cl;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Class<K> keyType) {
/*  60 */     return new EnumHashBiMap<>(keyType);
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
/*     */   public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Map<K, ? extends V> map) {
/*  75 */     EnumHashBiMap<K, V> bimap = create(EnumBiMap.inferKeyTypeOrObjectUnderJ2cl(map));
/*  76 */     bimap.putAll(map);
/*  77 */     return bimap;
/*     */   }
/*     */   
/*     */   private EnumHashBiMap(Class<K> keyType) {
/*  81 */     super(new EnumMap<>(keyType), new HashMap<>());
/*     */     
/*  83 */     this.keyTypeOrObjectUnderJ2cl = keyType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   K checkKey(K key) {
/*  90 */     return (K)Preconditions.checkNotNull(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V put(K key, @ParametricNullness V value) {
/*  99 */     return super.put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V forcePut(K key, @ParametricNullness V value) {
/* 108 */     return super.forcePut(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public Class<K> keyType() {
/* 114 */     return this.keyTypeOrObjectUnderJ2cl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 123 */     stream.defaultWriteObject();
/* 124 */     stream.writeObject(this.keyTypeOrObjectUnderJ2cl);
/* 125 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 131 */     stream.defaultReadObject();
/* 132 */     this.keyTypeOrObjectUnderJ2cl = (Class<K>)Objects.<Object>requireNonNull(stream.readObject());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     setDelegates(new EnumMap<>(this.keyTypeOrObjectUnderJ2cl), new HashMap<>());
/* 139 */     Serialization.populateMap(this, stream);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/EnumHashBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */