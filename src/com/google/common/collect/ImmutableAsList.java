/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import java.io.InvalidObjectException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ abstract class ImmutableAsList<E>
/*    */   extends ImmutableList<E>
/*    */ {
/*    */   abstract ImmutableCollection<E> delegateCollection();
/*    */   
/*    */   public boolean contains(@CheckForNull Object target) {
/* 44 */     return delegateCollection().contains(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 49 */     return delegateCollection().size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 54 */     return delegateCollection().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 59 */     return delegateCollection().isPartialView();
/*    */   }
/*    */   
/*    */   @GwtIncompatible
/*    */   @J2ktIncompatible
/*    */   static class SerializedForm implements Serializable {
/*    */     final ImmutableCollection<?> collection;
/*    */     private static final long serialVersionUID = 0L;
/*    */     
/*    */     SerializedForm(ImmutableCollection<?> collection) {
/* 69 */       this.collection = collection;
/*    */     }
/*    */     
/*    */     Object readResolve() {
/* 73 */       return this.collection.asList();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/*    */   @J2ktIncompatible
/*    */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 82 */     throw new InvalidObjectException("Use SerializedForm");
/*    */   }
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/*    */   @J2ktIncompatible
/*    */   Object writeReplace() {
/* 89 */     return new SerializedForm(delegateCollection());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableAsList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */