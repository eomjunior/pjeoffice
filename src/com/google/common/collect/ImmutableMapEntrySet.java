/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class ImmutableMapEntrySet<K, V>
/*     */   extends ImmutableSet.CachingAsList<Map.Entry<K, V>>
/*     */ {
/*     */   abstract ImmutableMap<K, V> map();
/*     */   
/*     */   static final class RegularEntrySet<K, V>
/*     */     extends ImmutableMapEntrySet<K, V>
/*     */   {
/*     */     private final transient ImmutableMap<K, V> map;
/*     */     private final transient ImmutableList<Map.Entry<K, V>> entries;
/*     */     
/*     */     RegularEntrySet(ImmutableMap<K, V> map, Map.Entry<K, V>[] entries) {
/*  45 */       this(map, ImmutableList.asImmutableList((Object[])entries));
/*     */     }
/*     */     
/*     */     RegularEntrySet(ImmutableMap<K, V> map, ImmutableList<Map.Entry<K, V>> entries) {
/*  49 */       this.map = map;
/*  50 */       this.entries = entries;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<K, V> map() {
/*  55 */       return this.map;
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible("not used in GWT")
/*     */     int copyIntoArray(Object[] dst, int offset) {
/*  61 */       return this.entries.copyIntoArray(dst, offset);
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/*  66 */       return this.entries.iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<Map.Entry<K, V>> spliterator() {
/*  71 */       return this.entries.spliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super Map.Entry<K, V>> action) {
/*  76 */       this.entries.forEach(action);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableList<Map.Entry<K, V>> createAsList() {
/*  81 */       return new RegularImmutableAsList<>(this, this.entries);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @J2ktIncompatible
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/*  90 */       return super.writeReplace();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 100 */     return map().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object object) {
/* 105 */     if (object instanceof Map.Entry) {
/* 106 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 107 */       V value = map().get(entry.getKey());
/* 108 */       return (value != null && value.equals(entry.getValue()));
/*     */     } 
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 115 */     return map().isPartialView();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   boolean isHashCodeFast() {
/* 121 */     return map().isHashCodeFast();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 126 */     return map().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   Object writeReplace() {
/* 133 */     return new EntrySetSerializedForm<>(map());
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 139 */     throw new InvalidObjectException("Use EntrySetSerializedForm");
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private static class EntrySetSerializedForm<K, V> implements Serializable {
/*     */     final ImmutableMap<K, V> map;
/*     */     
/*     */     EntrySetSerializedForm(ImmutableMap<K, V> map) {
/* 148 */       this.map = map;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     Object readResolve() {
/* 152 */       return this.map.entrySet();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableMapEntrySet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */