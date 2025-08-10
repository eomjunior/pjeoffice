/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CheckReturnValue;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import com.google.errorprone.annotations.InlineMe;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
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
/*     */ public abstract class Converter<A, B>
/*     */   implements Function<A, B>
/*     */ {
/*     */   private final boolean handleNullAutomatically;
/*     */   @LazyInit
/*     */   @CheckForNull
/*     */   @RetainedWith
/*     */   private transient Converter<B, A> reverse;
/*     */   
/*     */   protected Converter() {
/* 151 */     this(true);
/*     */   }
/*     */ 
/*     */   
/*     */   Converter(boolean handleNullAutomatically) {
/* 156 */     this.handleNullAutomatically = handleNullAutomatically;
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
/*     */   @ForOverride
/*     */   protected abstract B doForward(A paramA);
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
/*     */   protected abstract A doBackward(B paramB);
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
/*     */   public final B convert(@CheckForNull A a) {
/* 195 */     return correctedDoForward(a);
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   B correctedDoForward(@CheckForNull A a) {
/* 200 */     if (this.handleNullAutomatically)
/*     */     {
/* 202 */       return (a == null) ? null : Preconditions.<B>checkNotNull(doForward(a));
/*     */     }
/* 204 */     return unsafeDoForward(a);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   A correctedDoBackward(@CheckForNull B b) {
/* 210 */     if (this.handleNullAutomatically)
/*     */     {
/* 212 */       return (b == null) ? null : Preconditions.<A>checkNotNull(doBackward(b));
/*     */     }
/* 214 */     return unsafeDoBackward(b);
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
/*     */   @CheckForNull
/*     */   private B unsafeDoForward(@CheckForNull A a) {
/* 246 */     return doForward(NullnessCasts.uncheckedCastNullableTToT(a));
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private A unsafeDoBackward(@CheckForNull B b) {
/* 251 */     return doBackward(NullnessCasts.uncheckedCastNullableTToT(b));
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
/*     */   public Iterable<B> convertAll(final Iterable<? extends A> fromIterable) {
/* 273 */     Preconditions.checkNotNull(fromIterable, "fromIterable");
/* 274 */     return new Iterable<B>()
/*     */       {
/*     */         public Iterator<B> iterator() {
/* 277 */           return new Iterator<B>() {
/* 278 */               private final Iterator<? extends A> fromIterator = fromIterable.iterator();
/*     */ 
/*     */               
/*     */               public boolean hasNext() {
/* 282 */                 return this.fromIterator.hasNext();
/*     */               }
/*     */ 
/*     */               
/*     */               public B next() {
/* 287 */                 return (B)Converter.this.convert(this.fromIterator.next());
/*     */               }
/*     */ 
/*     */               
/*     */               public void remove() {
/* 292 */                 this.fromIterator.remove();
/*     */               }
/*     */             };
/*     */         }
/*     */       };
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
/*     */   @CheckReturnValue
/*     */   public Converter<B, A> reverse() {
/* 309 */     Converter<B, A> result = this.reverse;
/* 310 */     return (result == null) ? (this.reverse = new ReverseConverter<>(this)) : result;
/*     */   }
/*     */   
/*     */   private static final class ReverseConverter<A, B> extends Converter<B, A> implements Serializable {
/*     */     final Converter<A, B> original;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ReverseConverter(Converter<A, B> original) {
/* 318 */       this.original = original;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected A doForward(B b) {
/* 330 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     protected B doBackward(A a) {
/* 335 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     A correctedDoForward(@CheckForNull B b) {
/* 341 */       return this.original.correctedDoBackward(b);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     B correctedDoBackward(@CheckForNull A a) {
/* 347 */       return this.original.correctedDoForward(a);
/*     */     }
/*     */ 
/*     */     
/*     */     public Converter<A, B> reverse() {
/* 352 */       return this.original;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object object) {
/* 357 */       if (object instanceof ReverseConverter) {
/* 358 */         ReverseConverter<?, ?> that = (ReverseConverter<?, ?>)object;
/* 359 */         return this.original.equals(that.original);
/*     */       } 
/* 361 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 366 */       return this.original.hashCode() ^ 0xFFFFFFFF;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 371 */       return this.original + ".reverse()";
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
/*     */   public final <C> Converter<A, C> andThen(Converter<B, C> secondConverter) {
/* 385 */     return doAndThen(secondConverter);
/*     */   }
/*     */ 
/*     */   
/*     */   <C> Converter<A, C> doAndThen(Converter<B, C> secondConverter) {
/* 390 */     return new ConverterComposition<>(this, Preconditions.<Converter<B, C>>checkNotNull(secondConverter));
/*     */   }
/*     */   
/*     */   private static final class ConverterComposition<A, B, C> extends Converter<A, C> implements Serializable {
/*     */     final Converter<A, B> first;
/*     */     final Converter<B, C> second;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ConverterComposition(Converter<A, B> first, Converter<B, C> second) {
/* 399 */       this.first = first;
/* 400 */       this.second = second;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected C doForward(A a) {
/* 412 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     protected A doBackward(C c) {
/* 417 */       throw new AssertionError();
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     C correctedDoForward(@CheckForNull A a) {
/* 423 */       return this.second.correctedDoForward(this.first.correctedDoForward(a));
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     A correctedDoBackward(@CheckForNull C c) {
/* 429 */       return this.first.correctedDoBackward(this.second.correctedDoBackward(c));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object object) {
/* 434 */       if (object instanceof ConverterComposition) {
/* 435 */         ConverterComposition<?, ?, ?> that = (ConverterComposition<?, ?, ?>)object;
/* 436 */         return (this.first.equals(that.first) && this.second.equals(that.second));
/*     */       } 
/* 438 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 443 */       return 31 * this.first.hashCode() + this.second.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 448 */       return this.first + ".andThen(" + this.second + ")";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @InlineMe(replacement = "this.convert(a)")
/*     */   public final B apply(A a) {
/* 479 */     return convert(a);
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
/*     */   public boolean equals(@CheckForNull Object object) {
/* 495 */     return super.equals(object);
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
/*     */   public static <A, B> Converter<A, B> from(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
/* 517 */     return new FunctionBasedConverter<>(forwardFunction, backwardFunction);
/*     */   }
/*     */   
/*     */   private static final class FunctionBasedConverter<A, B>
/*     */     extends Converter<A, B>
/*     */     implements Serializable
/*     */   {
/*     */     private final Function<? super A, ? extends B> forwardFunction;
/*     */     private final Function<? super B, ? extends A> backwardFunction;
/*     */     
/*     */     private FunctionBasedConverter(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction) {
/* 528 */       this.forwardFunction = Preconditions.<Function<? super A, ? extends B>>checkNotNull(forwardFunction);
/* 529 */       this.backwardFunction = Preconditions.<Function<? super B, ? extends A>>checkNotNull(backwardFunction);
/*     */     }
/*     */ 
/*     */     
/*     */     protected B doForward(A a) {
/* 534 */       return this.forwardFunction.apply(a);
/*     */     }
/*     */ 
/*     */     
/*     */     protected A doBackward(B b) {
/* 539 */       return this.backwardFunction.apply(b);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object object) {
/* 544 */       if (object instanceof FunctionBasedConverter) {
/* 545 */         FunctionBasedConverter<?, ?> that = (FunctionBasedConverter<?, ?>)object;
/* 546 */         return (this.forwardFunction.equals(that.forwardFunction) && this.backwardFunction
/* 547 */           .equals(that.backwardFunction));
/*     */       } 
/* 549 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 554 */       return this.forwardFunction.hashCode() * 31 + this.backwardFunction.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 559 */       return "Converter.from(" + this.forwardFunction + ", " + this.backwardFunction + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Converter<T, T> identity() {
/* 566 */     return (IdentityConverter)IdentityConverter.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class IdentityConverter<T>
/*     */     extends Converter<T, T>
/*     */     implements Serializable
/*     */   {
/* 574 */     static final Converter<?, ?> INSTANCE = new IdentityConverter();
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     protected T doForward(T t) {
/* 578 */       return t;
/*     */     }
/*     */ 
/*     */     
/*     */     protected T doBackward(T t) {
/* 583 */       return t;
/*     */     }
/*     */ 
/*     */     
/*     */     public IdentityConverter<T> reverse() {
/* 588 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     <S> Converter<T, S> doAndThen(Converter<T, S> otherConverter) {
/* 593 */       return Preconditions.<Converter<T, S>>checkNotNull(otherConverter, "otherConverter");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 603 */       return "Converter.identity()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 607 */       return INSTANCE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Converter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */