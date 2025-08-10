/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.NoSuchElementException;
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
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public abstract class AbstractIterator<T>
/*     */   extends UnmodifiableIterator<T>
/*     */ {
/*     */   @CheckForNull
/*     */   private T next;
/*  68 */   private State state = State.NOT_READY;
/*     */   
/*     */   @CheckForNull
/*     */   protected abstract T computeNext();
/*     */   
/*     */   private enum State
/*     */   {
/*  75 */     READY,
/*     */ 
/*     */     
/*  78 */     NOT_READY,
/*     */ 
/*     */     
/*  81 */     DONE,
/*     */ 
/*     */     
/*  84 */     FAILED;
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
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   protected final T endOfData() {
/* 126 */     this.state = State.DONE;
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasNext() {
/* 132 */     Preconditions.checkState((this.state != State.FAILED));
/* 133 */     switch (this.state) {
/*     */       case DONE:
/* 135 */         return false;
/*     */       case READY:
/* 137 */         return true;
/*     */     } 
/*     */     
/* 140 */     return tryToComputeNext();
/*     */   }
/*     */   
/*     */   private boolean tryToComputeNext() {
/* 144 */     this.state = State.FAILED;
/* 145 */     this.next = computeNext();
/* 146 */     if (this.state != State.DONE) {
/* 147 */       this.state = State.READY;
/* 148 */       return true;
/*     */     } 
/* 150 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public final T next() {
/* 157 */     if (!hasNext()) {
/* 158 */       throw new NoSuchElementException();
/*     */     }
/* 160 */     this.state = State.NOT_READY;
/*     */     
/* 162 */     T result = NullnessCasts.uncheckedCastNullableTToT(this.next);
/* 163 */     this.next = null;
/* 164 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   public final T peek() {
/* 176 */     if (!hasNext()) {
/* 177 */       throw new NoSuchElementException();
/*     */     }
/*     */     
/* 180 */     return NullnessCasts.uncheckedCastNullableTToT(this.next);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/AbstractIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */