/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ import java.util.Spliterator;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class ImmutableSortedAsList<E>
/*     */   extends RegularImmutableAsList<E>
/*     */   implements SortedIterable<E>
/*     */ {
/*     */   ImmutableSortedAsList(ImmutableSortedSet<E> backingSet, ImmutableList<E> backingList) {
/*  36 */     super(backingSet, backingList);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> delegateCollection() {
/*  41 */     return (ImmutableSortedSet<E>)super.delegateCollection();
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/*  46 */     return delegateCollection().comparator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public int indexOf(@CheckForNull Object target) {
/*  55 */     int index = delegateCollection().indexOf(target);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     return (index >= 0 && get(index).equals(target)) ? index : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public int lastIndexOf(@CheckForNull Object target) {
/*  68 */     return indexOf(target);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object target) {
/*  74 */     return (indexOf(target) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
/*  85 */     ImmutableList<E> parentSubList = super.subListUnchecked(fromIndex, toIndex);
/*  86 */     return (new RegularImmutableSortedSet(parentSubList, comparator())).asList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/*  94 */     Objects.requireNonNull(delegateList()); return CollectSpliterators.indexed(size(), 1301, delegateList()::get, 
/*  95 */         comparator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 104 */     return super.writeReplace();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableSortedAsList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */