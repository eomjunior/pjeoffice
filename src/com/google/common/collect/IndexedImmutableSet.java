/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Iterator;
/*    */ import java.util.Spliterator;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible(emulated = true)
/*    */ abstract class IndexedImmutableSet<E>
/*    */   extends ImmutableSet.CachingAsList<E>
/*    */ {
/*    */   public UnmodifiableIterator<E> iterator() {
/* 35 */     return asList().iterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public Spliterator<E> spliterator() {
/* 40 */     return CollectSpliterators.indexed(size(), 1297, this::get);
/*    */   }
/*    */ 
/*    */   
/*    */   public void forEach(Consumer<? super E> consumer) {
/* 45 */     Preconditions.checkNotNull(consumer);
/* 46 */     int n = size();
/* 47 */     for (int i = 0; i < n; i++) {
/* 48 */       consumer.accept(get(i));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/*    */   int copyIntoArray(Object[] dst, int offset) {
/* 55 */     return asList().copyIntoArray(dst, offset);
/*    */   }
/*    */ 
/*    */   
/*    */   ImmutableList<E> createAsList() {
/* 60 */     return new ImmutableAsList<E>()
/*    */       {
/*    */         public E get(int index) {
/* 63 */           return IndexedImmutableSet.this.get(index);
/*    */         }
/*    */ 
/*    */         
/*    */         boolean isPartialView() {
/* 68 */           return IndexedImmutableSet.this.isPartialView();
/*    */         }
/*    */ 
/*    */         
/*    */         public int size() {
/* 73 */           return IndexedImmutableSet.this.size();
/*    */         }
/*    */ 
/*    */         
/*    */         ImmutableCollection<E> delegateCollection() {
/* 78 */           return IndexedImmutableSet.this;
/*    */         }
/*    */ 
/*    */ 
/*    */ 
/*    */         
/*    */         @J2ktIncompatible
/*    */         @GwtIncompatible
/*    */         Object writeReplace() {
/* 87 */           return super.writeReplace();
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @J2ktIncompatible
/*    */   @GwtIncompatible
/*    */   Object writeReplace() {
/* 98 */     return super.writeReplace();
/*    */   }
/*    */   
/*    */   abstract E get(int paramInt);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/IndexedImmutableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */