/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collections;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ final class Present<T>
/*    */   extends Optional<T>
/*    */ {
/*    */   private final T reference;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   Present(T reference) {
/* 31 */     this.reference = reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPresent() {
/* 36 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public T get() {
/* 41 */     return this.reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public T or(T defaultValue) {
/* 46 */     Preconditions.checkNotNull(defaultValue, "use Optional.orNull() instead of Optional.or(null)");
/* 47 */     return this.reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<T> or(Optional<? extends T> secondChoice) {
/* 52 */     Preconditions.checkNotNull(secondChoice);
/* 53 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public T or(Supplier<? extends T> supplier) {
/* 58 */     Preconditions.checkNotNull(supplier);
/* 59 */     return this.reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public T orNull() {
/* 64 */     return this.reference;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<T> asSet() {
/* 69 */     return Collections.singleton(this.reference);
/*    */   }
/*    */ 
/*    */   
/*    */   public <V> Optional<V> transform(Function<? super T, V> function) {
/* 74 */     return new Present(
/* 75 */         Preconditions.checkNotNull((T)function
/* 76 */           .apply(this.reference), "the Function passed to Optional.transform() must not return null."));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(@CheckForNull Object object) {
/* 82 */     if (object instanceof Present) {
/* 83 */       Present<?> other = (Present)object;
/* 84 */       return this.reference.equals(other.reference);
/*    */     } 
/* 86 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 91 */     return 1502476572 + this.reference.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 96 */     return "Optional.of(" + this.reference + ")";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Present.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */