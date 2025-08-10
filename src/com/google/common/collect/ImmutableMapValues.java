/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ final class ImmutableMapValues<K, V>
/*     */   extends ImmutableCollection<V>
/*     */ {
/*     */   private final ImmutableMap<K, V> map;
/*     */   
/*     */   ImmutableMapValues(ImmutableMap<K, V> map) {
/*  42 */     this.map = map;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  47 */     return this.map.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<V> iterator() {
/*  52 */     return new UnmodifiableIterator<V>() {
/*  53 */         final UnmodifiableIterator<Map.Entry<K, V>> entryItr = ImmutableMapValues.this.map.entrySet().iterator();
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/*  57 */           return this.entryItr.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public V next() {
/*  62 */           return (V)((Map.Entry)this.entryItr.next()).getValue();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<V> spliterator() {
/*  69 */     return CollectSpliterators.map(this.map.entrySet().spliterator(), Map.Entry::getValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object object) {
/*  74 */     return (object != null && Iterators.contains(iterator(), object));
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/*  79 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableList<V> asList() {
/*  84 */     final ImmutableList<Map.Entry<K, V>> entryList = this.map.entrySet().asList();
/*  85 */     return new ImmutableAsList<V>()
/*     */       {
/*     */         public V get(int index) {
/*  88 */           return (V)((Map.Entry)entryList.get(index)).getValue();
/*     */         }
/*     */ 
/*     */         
/*     */         ImmutableCollection<V> delegateCollection() {
/*  93 */           return ImmutableMapValues.this;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         @J2ktIncompatible
/*     */         @GwtIncompatible
/*     */         Object writeReplace() {
/* 102 */           return super.writeReplace();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public void forEach(Consumer<? super V> action) {
/* 110 */     Preconditions.checkNotNull(action);
/* 111 */     this.map.forEach((k, v) -> action.accept(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 120 */     return super.writeReplace();
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private static class SerializedForm<V>
/*     */     implements Serializable {
/*     */     final ImmutableMap<?, V> map;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(ImmutableMap<?, V> map) {
/* 131 */       this.map = map;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 135 */       return this.map.values();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableMapValues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */