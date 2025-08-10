/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import java.io.Serializable;
/*     */ import java.util.function.BiPredicate;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public abstract class Equivalence<T>
/*     */   implements BiPredicate<T, T>
/*     */ {
/*     */   public final boolean equivalent(@CheckForNull T a, @CheckForNull T b) {
/*  66 */     if (a == b) {
/*  67 */       return true;
/*     */     }
/*  69 */     if (a == null || b == null) {
/*  70 */       return false;
/*     */     }
/*  72 */     return doEquivalent(a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final boolean test(@CheckForNull T t, @CheckForNull T u) {
/*  83 */     return equivalent(t, u);
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
/*     */   @ForOverride
/*     */   protected abstract boolean doEquivalent(T paramT1, T paramT2);
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
/*     */   public final int hash(@CheckForNull T t) {
/* 116 */     if (t == null) {
/* 117 */       return 0;
/*     */     }
/* 119 */     return doHash(t);
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
/*     */   @ForOverride
/*     */   protected abstract int doHash(T paramT);
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
/*     */   public final <F> Equivalence<F> onResultOf(Function<? super F, ? extends T> function) {
/* 158 */     return new FunctionalEquivalence<>(function, this);
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
/*     */   public final <S extends T> Wrapper<S> wrap(@ParametricNullness S reference) {
/* 172 */     return new Wrapper<>(this, reference);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Wrapper<T>
/*     */     implements Serializable
/*     */   {
/*     */     private final Equivalence<? super T> equivalence;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     private final T reference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Wrapper(Equivalence<? super T> equivalence, @ParametricNullness T reference) {
/* 209 */       this.equivalence = Preconditions.<Equivalence<? super T>>checkNotNull(equivalence);
/* 210 */       this.reference = reference;
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public T get() {
/* 216 */       return this.reference;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 226 */       if (obj == this) {
/* 227 */         return true;
/*     */       }
/* 229 */       if (obj instanceof Wrapper) {
/* 230 */         Wrapper<?> that = (Wrapper)obj;
/*     */         
/* 232 */         if (this.equivalence.equals(that.equivalence)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 238 */           Equivalence<Object> equivalence = (Equivalence)this.equivalence;
/* 239 */           return equivalence.equivalent(this.reference, that.reference);
/*     */         } 
/*     */       } 
/* 242 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 248 */       return this.equivalence.hash(this.reference);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 257 */       return this.equivalence + ".wrap(" + this.reference + ")";
/*     */     }
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public final <S extends T> Equivalence<Iterable<S>> pairwise() {
/* 280 */     return new PairwiseEquivalence<>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Predicate<T> equivalentTo(@CheckForNull T target) {
/* 290 */     return new EquivalentToPredicate<>(this, target);
/*     */   }
/*     */   
/*     */   private static final class EquivalentToPredicate<T> implements Predicate<T>, Serializable {
/*     */     private final Equivalence<T> equivalence;
/*     */     @CheckForNull
/*     */     private final T target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EquivalentToPredicate(Equivalence<T> equivalence, @CheckForNull T target) {
/* 300 */       this.equivalence = Preconditions.<Equivalence<T>>checkNotNull(equivalence);
/* 301 */       this.target = target;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(@CheckForNull T input) {
/* 306 */       return this.equivalence.equivalent(input, this.target);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 311 */       if (this == obj) {
/* 312 */         return true;
/*     */       }
/* 314 */       if (obj instanceof EquivalentToPredicate) {
/* 315 */         EquivalentToPredicate<?> that = (EquivalentToPredicate)obj;
/* 316 */         return (this.equivalence.equals(that.equivalence) && Objects.equal(this.target, that.target));
/*     */       } 
/* 318 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 323 */       return Objects.hashCode(new Object[] { this.equivalence, this.target });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 328 */       return this.equivalence + ".equivalentTo(" + this.target + ")";
/*     */     }
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
/*     */   public static Equivalence<Object> equals() {
/* 345 */     return Equals.INSTANCE;
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
/*     */   public static Equivalence<Object> identity() {
/* 357 */     return Identity.INSTANCE;
/*     */   }
/*     */   
/*     */   static final class Equals
/*     */     extends Equivalence<Object> implements Serializable {
/* 362 */     static final Equals INSTANCE = new Equals();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected boolean doEquivalent(Object a, Object b) {
/* 366 */       return a.equals(b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int doHash(Object o) {
/* 371 */       return o.hashCode();
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 375 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Identity
/*     */     extends Equivalence<Object>
/*     */     implements Serializable
/*     */   {
/* 383 */     static final Identity INSTANCE = new Identity();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected boolean doEquivalent(Object a, Object b) {
/* 387 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected int doHash(Object o) {
/* 392 */       return System.identityHashCode(o);
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 396 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Equivalence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */