/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.DoNotCall;
/*    */ import java.util.ListIterator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ public abstract class UnmodifiableListIterator<E>
/*    */   extends UnmodifiableIterator<E>
/*    */   implements ListIterator<E>
/*    */ {
/*    */   @Deprecated
/*    */   @DoNotCall("Always throws UnsupportedOperationException")
/*    */   public final void add(@ParametricNullness E e) {
/* 47 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   @DoNotCall("Always throws UnsupportedOperationException")
/*    */   public final void set(@ParametricNullness E e) {
/* 60 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/UnmodifiableListIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */