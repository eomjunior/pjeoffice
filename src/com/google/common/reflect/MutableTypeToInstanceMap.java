/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ForwardingMap;
/*     */ import com.google.common.collect.ForwardingMapEntry;
/*     */ import com.google.common.collect.ForwardingSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotCall;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ public final class MutableTypeToInstanceMap<B>
/*     */   extends ForwardingMap<TypeToken<? extends B>, B>
/*     */   implements TypeToInstanceMap<B>
/*     */ {
/*  43 */   private final Map<TypeToken<? extends B>, B> backingMap = Maps.newHashMap();
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public <T extends B> T getInstance(Class<T> type) {
/*  48 */     return trustedGet(TypeToken.of(type));
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public <T extends B> T getInstance(TypeToken<T> type) {
/*  54 */     return trustedGet(type.rejectTypeVariables());
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public <T extends B> T putInstance(Class<T> type, @ParametricNullness T value) {
/*  61 */     return trustedPut(TypeToken.of(type), value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public <T extends B> T putInstance(TypeToken<T> type, @ParametricNullness T value) {
/*  68 */     return trustedPut(type.rejectTypeVariables(), value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public B put(TypeToken<? extends B> key, @ParametricNullness B value) {
/*  83 */     throw new UnsupportedOperationException("Please use putInstance() instead.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public void putAll(Map<? extends TypeToken<? extends B>, ? extends B> map) {
/*  96 */     throw new UnsupportedOperationException("Please use putInstance() instead.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<TypeToken<? extends B>, B>> entrySet() {
/* 101 */     return UnmodifiableEntry.transformEntries(super.entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<TypeToken<? extends B>, B> delegate() {
/* 106 */     return this.backingMap;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   private <T extends B> T trustedPut(TypeToken<T> type, @ParametricNullness T value) {
/* 112 */     return (T)this.backingMap.put(type, (B)value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   private <T extends B> T trustedGet(TypeToken<T> type) {
/* 118 */     return (T)this.backingMap.get(type);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class UnmodifiableEntry<K, V>
/*     */     extends ForwardingMapEntry<K, V>
/*     */   {
/*     */     private final Map.Entry<K, V> delegate;
/*     */     
/*     */     static <K, V> Set<Map.Entry<K, V>> transformEntries(final Set<Map.Entry<K, V>> entries) {
/* 128 */       return (Set<Map.Entry<K, V>>)new ForwardingSet<Map.Entry<K, V>>()
/*     */         {
/*     */           protected Set<Map.Entry<K, V>> delegate() {
/* 131 */             return entries;
/*     */           }
/*     */ 
/*     */           
/*     */           public Iterator<Map.Entry<K, V>> iterator() {
/* 136 */             return MutableTypeToInstanceMap.UnmodifiableEntry.transformEntries(super.iterator());
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public Object[] toArray() {
/* 148 */             Object[] result = standardToArray();
/* 149 */             return result;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public <T> T[] toArray(T[] array) {
/* 155 */             return (T[])standardToArray((Object[])array);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     private static <K, V> Iterator<Map.Entry<K, V>> transformEntries(Iterator<Map.Entry<K, V>> entries) {
/* 162 */       return Iterators.transform(entries, UnmodifiableEntry::new);
/*     */     }
/*     */     
/*     */     private UnmodifiableEntry(Map.Entry<K, V> delegate) {
/* 166 */       this.delegate = (Map.Entry<K, V>)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Map.Entry<K, V> delegate() {
/* 171 */       return this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public V setValue(@ParametricNullness V value) {
/* 177 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/reflect/MutableTypeToInstanceMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */