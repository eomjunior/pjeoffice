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
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ final class Absent<T>
/*    */   extends Optional<T>
/*    */ {
/* 28 */   static final Absent<Object> INSTANCE = new Absent();
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   static <T> Optional<T> withType() {
/* 32 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPresent() {
/* 39 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public T get() {
/* 44 */     throw new IllegalStateException("Optional.get() cannot be called on an absent value");
/*    */   }
/*    */ 
/*    */   
/*    */   public T or(T defaultValue) {
/* 49 */     return Preconditions.checkNotNull(defaultValue, "use Optional.orNull() instead of Optional.or(null)");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<T> or(Optional<? extends T> secondChoice) {
/* 55 */     return (Optional<T>)Preconditions.<Optional<? extends T>>checkNotNull(secondChoice);
/*    */   }
/*    */ 
/*    */   
/*    */   public T or(Supplier<? extends T> supplier) {
/* 60 */     return Preconditions.checkNotNull(supplier
/* 61 */         .get(), "use Optional.orNull() instead of a Supplier that returns null");
/*    */   }
/*    */ 
/*    */   
/*    */   @CheckForNull
/*    */   public T orNull() {
/* 67 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<T> asSet() {
/* 72 */     return Collections.emptySet();
/*    */   }
/*    */ 
/*    */   
/*    */   public <V> Optional<V> transform(Function<? super T, V> function) {
/* 77 */     Preconditions.checkNotNull(function);
/* 78 */     return Optional.absent();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(@CheckForNull Object object) {
/* 83 */     return (object == this);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 88 */     return 2040732332;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 93 */     return "Optional.absent()";
/*    */   }
/*    */   
/*    */   private Object readResolve() {
/* 97 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Absent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */