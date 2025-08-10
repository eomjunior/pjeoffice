/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import java.util.Spliterator;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class ImmutableEnumMap<K extends Enum<K>, V>
/*     */   extends ImmutableMap.IteratorBasedImmutableMap<K, V>
/*     */ {
/*     */   private final transient EnumMap<K, V> delegate;
/*     */   
/*     */   static <K extends Enum<K>, V> ImmutableMap<K, V> asImmutable(EnumMap<K, V> map) {
/*     */     Map.Entry<K, V> entry;
/*  42 */     switch (map.size()) {
/*     */       case 0:
/*  44 */         return ImmutableMap.of();
/*     */       case 1:
/*  46 */         entry = Iterables.<Map.Entry<K, V>>getOnlyElement(map.entrySet());
/*  47 */         return ImmutableMap.of(entry.getKey(), entry.getValue());
/*     */     } 
/*  49 */     return new ImmutableEnumMap<>(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableEnumMap(EnumMap<K, V> delegate) {
/*  56 */     this.delegate = delegate;
/*  57 */     Preconditions.checkArgument(!delegate.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<K> keyIterator() {
/*  62 */     return Iterators.unmodifiableIterator(this.delegate.keySet().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<K> keySpliterator() {
/*  67 */     return this.delegate.keySet().spliterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  72 */     return this.delegate.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(@CheckForNull Object key) {
/*  77 */     return this.delegate.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V get(@CheckForNull Object key) {
/*  83 */     return this.delegate.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object<K, V> object) {
/*  88 */     if (object == this) {
/*  89 */       return true;
/*     */     }
/*  91 */     if (object instanceof ImmutableEnumMap) {
/*  92 */       object = (Object<K, V>)((ImmutableEnumMap)object).delegate;
/*     */     }
/*  94 */     return this.delegate.equals(object);
/*     */   }
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
/*  99 */     return Maps.unmodifiableEntryIterator(this.delegate.entrySet().iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 104 */     return CollectSpliterators.map(this.delegate.entrySet().spliterator(), Maps::unmodifiableEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 109 */     this.delegate.forEach(action);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   Object writeReplace() {
/* 121 */     return new EnumSerializedForm<>(this.delegate);
/*     */   }
/*     */   
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 126 */     throw new InvalidObjectException("Use EnumSerializedForm");
/*     */   }
/*     */   
/*     */   @J2ktIncompatible
/*     */   private static class EnumSerializedForm<K extends Enum<K>, V>
/*     */     implements Serializable
/*     */   {
/*     */     final EnumMap<K, V> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EnumSerializedForm(EnumMap<K, V> delegate) {
/* 137 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 141 */       return new ImmutableEnumMap<>(this.delegate);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableEnumMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */