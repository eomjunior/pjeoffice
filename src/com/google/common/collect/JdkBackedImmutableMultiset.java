/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.util.Collection;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ final class JdkBackedImmutableMultiset<E>
/*     */   extends ImmutableMultiset<E>
/*     */ {
/*     */   private final Map<E, Integer> delegateMap;
/*     */   private final ImmutableList<Multiset.Entry<E>> entries;
/*     */   private final long size;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   private transient ImmutableSet<E> elementSet;
/*     */   
/*     */   static <E> ImmutableMultiset<E> create(Collection<? extends Multiset.Entry<? extends E>> entries) {
/*  43 */     Multiset.Entry[] arrayOfEntry = entries.<Multiset.Entry>toArray(new Multiset.Entry[0]);
/*  44 */     Map<E, Integer> delegateMap = Maps.newHashMapWithExpectedSize(arrayOfEntry.length);
/*  45 */     long size = 0L;
/*  46 */     for (int i = 0; i < arrayOfEntry.length; i++) {
/*  47 */       Multiset.Entry<E> entry = arrayOfEntry[i];
/*  48 */       int count = entry.getCount();
/*  49 */       size += count;
/*  50 */       E element = (E)Preconditions.checkNotNull(entry.getElement());
/*  51 */       delegateMap.put(element, Integer.valueOf(count));
/*  52 */       if (!(entry instanceof Multisets.ImmutableEntry)) {
/*  53 */         arrayOfEntry[i] = Multisets.immutableEntry(element, count);
/*     */       }
/*     */     } 
/*  56 */     return new JdkBackedImmutableMultiset<>(delegateMap, 
/*  57 */         ImmutableList.asImmutableList((Object[])arrayOfEntry), size);
/*     */   }
/*     */ 
/*     */   
/*     */   private JdkBackedImmutableMultiset(Map<E, Integer> delegateMap, ImmutableList<Multiset.Entry<E>> entries, long size) {
/*  62 */     this.delegateMap = delegateMap;
/*  63 */     this.entries = entries;
/*  64 */     this.size = size;
/*     */   }
/*     */ 
/*     */   
/*     */   public int count(@CheckForNull Object element) {
/*  69 */     return ((Integer)this.delegateMap.getOrDefault(element, Integer.valueOf(0))).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<E> elementSet() {
/*  76 */     ImmutableSet<E> result = this.elementSet;
/*  77 */     return (result == null) ? (this.elementSet = new ImmutableMultiset.ElementSet<>(this.entries, this)) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   Multiset.Entry<E> getEntry(int index) {
/*  82 */     return this.entries.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  92 */     return Ints.saturatedCast(this.size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 101 */     return super.writeReplace();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/JdkBackedImmutableMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */