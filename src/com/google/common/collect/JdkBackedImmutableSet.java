/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import java.util.Set;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible(serializable = true)
/*    */ final class JdkBackedImmutableSet<E>
/*    */   extends IndexedImmutableSet<E>
/*    */ {
/*    */   private final Set<?> delegate;
/*    */   private final ImmutableList<E> delegateList;
/*    */   
/*    */   JdkBackedImmutableSet(Set<?> delegate, ImmutableList<E> delegateList) {
/* 37 */     this.delegate = delegate;
/* 38 */     this.delegateList = delegateList;
/*    */   }
/*    */ 
/*    */   
/*    */   E get(int index) {
/* 43 */     return this.delegateList.get(index);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(@CheckForNull Object object) {
/* 48 */     return this.delegate.contains(object);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 53 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 58 */     return this.delegateList.size();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @J2ktIncompatible
/*    */   @GwtIncompatible
/*    */   Object writeReplace() {
/* 67 */     return super.writeReplace();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/JdkBackedImmutableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */