/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ public abstract class ForwardingSortedSet<E>
/*     */   extends ForwardingSet<E>
/*     */   implements SortedSet<E>
/*     */ {
/*     */   @CheckForNull
/*     */   public Comparator<? super E> comparator() {
/*  70 */     return delegate().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   public E first() {
/*  76 */     return delegate().first();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<E> headSet(@ParametricNullness E toElement) {
/*  81 */     return delegate().headSet(toElement);
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   public E last() {
/*  87 */     return delegate().last();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<E> subSet(@ParametricNullness E fromElement, @ParametricNullness E toElement) {
/*  92 */     return delegate().subSet(fromElement, toElement);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedSet<E> tailSet(@ParametricNullness E fromElement) {
/*  97 */     return delegate().tailSet(fromElement);
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
/*     */   protected boolean standardContains(@CheckForNull Object object) {
/*     */     try {
/* 112 */       SortedSet<Object> self = (SortedSet)this;
/* 113 */       Object ceiling = self.tailSet(object).first();
/* 114 */       return (ForwardingSortedMap.unsafeCompare(comparator(), ceiling, object) == 0);
/* 115 */     } catch (ClassCastException|java.util.NoSuchElementException|NullPointerException e) {
/* 116 */       return false;
/*     */     } 
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
/*     */   protected boolean standardRemove(@CheckForNull Object object) {
/*     */     try {
/* 132 */       SortedSet<Object> self = (SortedSet)this;
/* 133 */       Iterator<?> iterator = self.tailSet(object).iterator();
/* 134 */       if (iterator.hasNext()) {
/* 135 */         Object ceiling = iterator.next();
/* 136 */         if (ForwardingSortedMap.unsafeCompare(comparator(), ceiling, object) == 0) {
/* 137 */           iterator.remove();
/* 138 */           return true;
/*     */         } 
/*     */       } 
/* 141 */     } catch (ClassCastException|NullPointerException e) {
/* 142 */       return false;
/*     */     } 
/* 144 */     return false;
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
/*     */   protected SortedSet<E> standardSubSet(@ParametricNullness E fromElement, @ParametricNullness E toElement) {
/* 156 */     return tailSet(fromElement).headSet(toElement);
/*     */   }
/*     */   
/*     */   protected abstract SortedSet<E> delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ForwardingSortedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */