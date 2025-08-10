/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Predicates
/*     */ {
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> alwaysTrue() {
/*  53 */     return ObjectPredicate.ALWAYS_TRUE.withNarrowedType();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> alwaysFalse() {
/*  59 */     return ObjectPredicate.ALWAYS_FALSE.withNarrowedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> isNull() {
/*  68 */     return ObjectPredicate.IS_NULL.withNarrowedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Predicate<T> notNull() {
/*  77 */     return ObjectPredicate.NOT_NULL.withNarrowedType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> not(Predicate<T> predicate) {
/*  85 */     return new NotPredicate<>(predicate);
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
/*     */   public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> components) {
/*  97 */     return new AndPredicate<>(defensiveCopy(components));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public static <T> Predicate<T> and(Predicate<? super T>... components) {
/* 109 */     return new AndPredicate<>(defensiveCopy(components));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> and(Predicate<? super T> first, Predicate<? super T> second) {
/* 119 */     return new AndPredicate<>(asList(Preconditions.<Predicate>checkNotNull(first), Preconditions.<Predicate>checkNotNull(second)));
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
/*     */   public static <T> Predicate<T> or(Iterable<? extends Predicate<? super T>> components) {
/* 131 */     return new OrPredicate<>(defensiveCopy(components));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public static <T> Predicate<T> or(Predicate<? super T>... components) {
/* 143 */     return new OrPredicate<>(defensiveCopy(components));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> or(Predicate<? super T> first, Predicate<? super T> second) {
/* 153 */     return new OrPredicate<>(asList(Preconditions.<Predicate>checkNotNull(first), Preconditions.<Predicate>checkNotNull(second)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> equalTo(@ParametricNullness T target) {
/* 161 */     return (target == null) ? 
/* 162 */       isNull() : (
/* 163 */       new IsEqualToPredicate(target)).<T>withNarrowedType();
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
/*     */   @GwtIncompatible
/*     */   public static <T> Predicate<T> instanceOf(Class<?> clazz) {
/* 181 */     return new InstanceOfPredicate<>(clazz);
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static Predicate<Class<?>> subtypeOf(Class<?> clazz) {
/* 201 */     return new SubtypeOfPredicate(clazz);
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
/*     */   public static <T> Predicate<T> in(Collection<? extends T> target) {
/* 216 */     return new InPredicate<>(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A, B> Predicate<A> compose(Predicate<B> predicate, Function<A, ? extends B> function) {
/* 227 */     return new CompositionPredicate<>(predicate, function);
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static Predicate<CharSequence> containsPattern(String pattern) {
/* 241 */     return new ContainsPatternFromStringPredicate(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible("java.util.regex.Pattern")
/*     */   public static Predicate<CharSequence> contains(Pattern pattern) {
/* 254 */     return new ContainsPatternPredicate(new JdkPattern(pattern));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   enum ObjectPredicate
/*     */     implements Predicate<Object>
/*     */   {
/* 262 */     ALWAYS_TRUE
/*     */     {
/*     */       public boolean apply(@CheckForNull Object o) {
/* 265 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 270 */         return "Predicates.alwaysTrue()";
/*     */       }
/*     */     },
/*     */     
/* 274 */     ALWAYS_FALSE
/*     */     {
/*     */       public boolean apply(@CheckForNull Object o) {
/* 277 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 282 */         return "Predicates.alwaysFalse()";
/*     */       }
/*     */     },
/*     */     
/* 286 */     IS_NULL
/*     */     {
/*     */       public boolean apply(@CheckForNull Object o) {
/* 289 */         return (o == null);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 294 */         return "Predicates.isNull()";
/*     */       }
/*     */     },
/*     */     
/* 298 */     NOT_NULL
/*     */     {
/*     */       public boolean apply(@CheckForNull Object o) {
/* 301 */         return (o != null);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 306 */         return "Predicates.notNull()";
/*     */       }
/*     */     };
/*     */ 
/*     */     
/*     */     <T> Predicate<T> withNarrowedType() {
/* 312 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class NotPredicate<T>
/*     */     implements Predicate<T>, Serializable {
/*     */     final Predicate<T> predicate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     NotPredicate(Predicate<T> predicate) {
/* 322 */       this.predicate = Preconditions.<Predicate<T>>checkNotNull(predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(@ParametricNullness T t) {
/* 327 */       return !this.predicate.apply(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 332 */       return this.predicate.hashCode() ^ 0xFFFFFFFF;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 337 */       if (obj instanceof NotPredicate) {
/* 338 */         NotPredicate<?> that = (NotPredicate)obj;
/* 339 */         return this.predicate.equals(that.predicate);
/*     */       } 
/* 341 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 346 */       return "Predicates.not(" + this.predicate + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class AndPredicate<T>
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final List<? extends Predicate<? super T>> components;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private AndPredicate(List<? extends Predicate<? super T>> components) {
/* 358 */       this.components = components;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean apply(@ParametricNullness T t) {
/* 364 */       for (int i = 0; i < this.components.size(); i++) {
/* 365 */         if (!((Predicate<T>)this.components.get(i)).apply(t)) {
/* 366 */           return false;
/*     */         }
/*     */       } 
/* 369 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 375 */       return this.components.hashCode() + 306654252;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 380 */       if (obj instanceof AndPredicate) {
/* 381 */         AndPredicate<?> that = (AndPredicate)obj;
/* 382 */         return this.components.equals(that.components);
/*     */       } 
/* 384 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 389 */       return Predicates.toStringHelper("and", this.components);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class OrPredicate<T>
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final List<? extends Predicate<? super T>> components;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private OrPredicate(List<? extends Predicate<? super T>> components) {
/* 401 */       this.components = components;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean apply(@ParametricNullness T t) {
/* 407 */       for (int i = 0; i < this.components.size(); i++) {
/* 408 */         if (((Predicate<T>)this.components.get(i)).apply(t)) {
/* 409 */           return true;
/*     */         }
/*     */       } 
/* 412 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 418 */       return this.components.hashCode() + 87855567;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 423 */       if (obj instanceof OrPredicate) {
/* 424 */         OrPredicate<?> that = (OrPredicate)obj;
/* 425 */         return this.components.equals(that.components);
/*     */       } 
/* 427 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 432 */       return Predicates.toStringHelper("or", this.components);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String toStringHelper(String methodName, Iterable<?> components) {
/* 439 */     StringBuilder builder = (new StringBuilder("Predicates.")).append(methodName).append('(');
/* 440 */     boolean first = true;
/* 441 */     for (Object o : components) {
/* 442 */       if (!first) {
/* 443 */         builder.append(',');
/*     */       }
/* 445 */       builder.append(o);
/* 446 */       first = false;
/*     */     } 
/* 448 */     return builder.append(')').toString();
/*     */   }
/*     */   
/*     */   private static class IsEqualToPredicate implements Predicate<Object>, Serializable {
/*     */     private final Object target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private IsEqualToPredicate(Object target) {
/* 456 */       this.target = target;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(@CheckForNull Object o) {
/* 461 */       return this.target.equals(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 466 */       return this.target.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 471 */       if (obj instanceof IsEqualToPredicate) {
/* 472 */         IsEqualToPredicate that = (IsEqualToPredicate)obj;
/* 473 */         return this.target.equals(that.target);
/*     */       } 
/* 475 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 480 */       return "Predicates.equalTo(" + this.target + ")";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     <T> Predicate<T> withNarrowedType() {
/* 487 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class InstanceOfPredicate<T>
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     @J2ktIncompatible
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private InstanceOfPredicate(Class<?> clazz) {
/* 500 */       this.clazz = Preconditions.<Class<?>>checkNotNull(clazz);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(@ParametricNullness T o) {
/* 505 */       return this.clazz.isInstance(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 510 */       return this.clazz.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 515 */       if (obj instanceof InstanceOfPredicate) {
/* 516 */         InstanceOfPredicate<?> that = (InstanceOfPredicate)obj;
/* 517 */         return (this.clazz == that.clazz);
/*     */       } 
/* 519 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 524 */       return "Predicates.instanceOf(" + this.clazz.getName() + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   private static class SubtypeOfPredicate
/*     */     implements Predicate<Class<?>>, Serializable
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private SubtypeOfPredicate(Class<?> clazz) {
/* 539 */       this.clazz = Preconditions.<Class<?>>checkNotNull(clazz);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(Class<?> input) {
/* 544 */       return this.clazz.isAssignableFrom(input);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 549 */       return this.clazz.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 554 */       if (obj instanceof SubtypeOfPredicate) {
/* 555 */         SubtypeOfPredicate that = (SubtypeOfPredicate)obj;
/* 556 */         return (this.clazz == that.clazz);
/*     */       } 
/* 558 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 563 */       return "Predicates.subtypeOf(" + this.clazz.getName() + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class InPredicate<T>
/*     */     implements Predicate<T>, Serializable
/*     */   {
/*     */     private final Collection<?> target;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private InPredicate(Collection<?> target) {
/* 575 */       this.target = Preconditions.<Collection>checkNotNull(target);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(@ParametricNullness T t) {
/*     */       try {
/* 581 */         return this.target.contains(t);
/* 582 */       } catch (NullPointerException|ClassCastException e) {
/* 583 */         return false;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 589 */       if (obj instanceof InPredicate) {
/* 590 */         InPredicate<?> that = (InPredicate)obj;
/* 591 */         return this.target.equals(that.target);
/*     */       } 
/* 593 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 598 */       return this.target.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 603 */       return "Predicates.in(" + this.target + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class CompositionPredicate<A, B>
/*     */     implements Predicate<A>, Serializable
/*     */   {
/*     */     final Predicate<B> p;
/*     */     final Function<A, ? extends B> f;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private CompositionPredicate(Predicate<B> p, Function<A, ? extends B> f) {
/* 616 */       this.p = Preconditions.<Predicate<B>>checkNotNull(p);
/* 617 */       this.f = Preconditions.<Function<A, ? extends B>>checkNotNull(f);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(@ParametricNullness A a) {
/* 622 */       return this.p.apply(this.f.apply(a));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 627 */       if (obj instanceof CompositionPredicate) {
/* 628 */         CompositionPredicate<?, ?> that = (CompositionPredicate<?, ?>)obj;
/* 629 */         return (this.f.equals(that.f) && this.p.equals(that.p));
/*     */       } 
/* 631 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 636 */       return this.f.hashCode() ^ this.p.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 642 */       return this.p + "(" + this.f + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   private static class ContainsPatternPredicate
/*     */     implements Predicate<CharSequence>, Serializable
/*     */   {
/*     */     final CommonPattern pattern;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ContainsPatternPredicate(CommonPattern pattern) {
/* 657 */       this.pattern = Preconditions.<CommonPattern>checkNotNull(pattern);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(CharSequence t) {
/* 662 */       return this.pattern.matcher(t).find();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 670 */       return Objects.hashCode(new Object[] { this.pattern.pattern(), Integer.valueOf(this.pattern.flags()) });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 675 */       if (obj instanceof ContainsPatternPredicate) {
/* 676 */         ContainsPatternPredicate that = (ContainsPatternPredicate)obj;
/*     */ 
/*     */ 
/*     */         
/* 680 */         return (Objects.equal(this.pattern.pattern(), that.pattern.pattern()) && this.pattern
/* 681 */           .flags() == that.pattern.flags());
/*     */       } 
/* 683 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 692 */       String patternString = MoreObjects.toStringHelper(this.pattern).add("pattern", this.pattern.pattern()).add("pattern.flags", this.pattern.flags()).toString();
/* 693 */       return "Predicates.contains(" + patternString + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   private static class ContainsPatternFromStringPredicate
/*     */     extends ContainsPatternPredicate
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     ContainsPatternFromStringPredicate(String string) {
/* 707 */       super(Platform.compilePattern(string));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 712 */       return "Predicates.containsPattern(" + this.pattern.pattern() + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> List<Predicate<? super T>> asList(Predicate<? super T> first, Predicate<? super T> second) {
/* 721 */     return Arrays.asList((Predicate<? super T>[])new Predicate[] { first, second });
/*     */   }
/*     */   
/*     */   private static <T> List<T> defensiveCopy(T... array) {
/* 725 */     return defensiveCopy(Arrays.asList(array));
/*     */   }
/*     */   
/*     */   static <T> List<T> defensiveCopy(Iterable<T> iterable) {
/* 729 */     ArrayList<T> list = new ArrayList<>();
/* 730 */     for (T element : iterable) {
/* 731 */       list.add(Preconditions.checkNotNull(element));
/*     */     }
/* 733 */     return list;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Predicates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */