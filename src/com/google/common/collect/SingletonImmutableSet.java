/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import com.google.common.base.Preconditions;
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
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ final class SingletonImmutableSet<E>
/*    */   extends ImmutableSet<E>
/*    */ {
/*    */   final transient E element;
/*    */   
/*    */   SingletonImmutableSet(E element) {
/* 42 */     this.element = (E)Preconditions.checkNotNull(element);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 47 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(@CheckForNull Object target) {
/* 52 */     return this.element.equals(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public UnmodifiableIterator<E> iterator() {
/* 57 */     return Iterators.singletonIterator(this.element);
/*    */   }
/*    */ 
/*    */   
/*    */   public ImmutableList<E> asList() {
/* 62 */     return ImmutableList.of(this.element);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 67 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   int copyIntoArray(Object[] dst, int offset) {
/* 72 */     dst[offset] = this.element;
/* 73 */     return offset + 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int hashCode() {
/* 78 */     return this.element.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 83 */     return '[' + this.element.toString() + ']';
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @J2ktIncompatible
/*    */   @GwtIncompatible
/*    */   Object writeReplace() {
/* 92 */     return super.writeReplace();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/SingletonImmutableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */