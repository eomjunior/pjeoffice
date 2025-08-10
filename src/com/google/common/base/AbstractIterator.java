/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ abstract class AbstractIterator<T>
/*    */   implements Iterator<T>
/*    */ {
/*    */   @CheckForNull
/*    */   private T next;
/*    */   
/*    */   @CheckForNull
/*    */   protected abstract T computeNext();
/*    */   
/* 34 */   private State state = State.NOT_READY;
/*    */ 
/*    */   
/*    */   private enum State
/*    */   {
/* 39 */     READY,
/* 40 */     NOT_READY,
/* 41 */     DONE,
/* 42 */     FAILED;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @CheckForNull
/*    */   @CanIgnoreReturnValue
/*    */   protected final T endOfData() {
/* 53 */     this.state = State.DONE;
/* 54 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean hasNext() {
/* 59 */     Preconditions.checkState((this.state != State.FAILED));
/* 60 */     switch (this.state) {
/*    */       case DONE:
/* 62 */         return false;
/*    */       case READY:
/* 64 */         return true;
/*    */     } 
/*    */     
/* 67 */     return tryToComputeNext();
/*    */   }
/*    */   
/*    */   private boolean tryToComputeNext() {
/* 71 */     this.state = State.FAILED;
/* 72 */     this.next = computeNext();
/* 73 */     if (this.state != State.DONE) {
/* 74 */       this.state = State.READY;
/* 75 */       return true;
/*    */     } 
/* 77 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   @ParametricNullness
/*    */   public final T next() {
/* 83 */     if (!hasNext()) {
/* 84 */       throw new NoSuchElementException();
/*    */     }
/* 86 */     this.state = State.NOT_READY;
/*    */     
/* 88 */     T result = NullnessCasts.uncheckedCastNullableTToT(this.next);
/* 89 */     this.next = null;
/* 90 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void remove() {
/* 95 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/AbstractIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */