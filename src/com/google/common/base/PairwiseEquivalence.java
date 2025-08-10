/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
/*    */ import java.util.Iterator;
/*    */ import javax.annotation.CheckForNull;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class PairwiseEquivalence<E, T extends E>
/*    */   extends Equivalence<Iterable<T>>
/*    */   implements Serializable
/*    */ {
/*    */   final Equivalence<E> elementEquivalence;
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   PairwiseEquivalence(Equivalence<E> elementEquivalence) {
/* 30 */     this.elementEquivalence = Preconditions.<Equivalence<E>>checkNotNull(elementEquivalence);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean doEquivalent(Iterable<T> iterableA, Iterable<T> iterableB) {
/* 35 */     Iterator<T> iteratorA = iterableA.iterator();
/* 36 */     Iterator<T> iteratorB = iterableB.iterator();
/*    */     
/* 38 */     while (iteratorA.hasNext() && iteratorB.hasNext()) {
/* 39 */       if (!this.elementEquivalence.equivalent((E)iteratorA.next(), (E)iteratorB.next())) {
/* 40 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 44 */     return (!iteratorA.hasNext() && !iteratorB.hasNext());
/*    */   }
/*    */ 
/*    */   
/*    */   protected int doHash(Iterable<T> iterable) {
/* 49 */     int hash = 78721;
/* 50 */     for (T element : iterable) {
/* 51 */       hash = hash * 24943 + this.elementEquivalence.hash((E)element);
/*    */     }
/* 53 */     return hash;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(@CheckForNull Object object) {
/* 58 */     if (object instanceof PairwiseEquivalence) {
/*    */       
/* 60 */       PairwiseEquivalence<Object, Object> that = (PairwiseEquivalence<Object, Object>)object;
/* 61 */       return this.elementEquivalence.equals(that.elementEquivalence);
/*    */     } 
/*    */     
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 69 */     return this.elementEquivalence.hashCode() ^ 0x46A3EB07;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 74 */     return this.elementEquivalence + ".pairwise()";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/PairwiseEquivalence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */