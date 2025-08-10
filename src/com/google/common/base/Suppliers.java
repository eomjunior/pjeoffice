/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class Suppliers
/*     */ {
/*     */   public static <F, T> Supplier<T> compose(Function<? super F, T> function, Supplier<F> supplier) {
/*  50 */     return new SupplierComposition<>(function, supplier);
/*     */   }
/*     */   
/*     */   private static class SupplierComposition<F, T> implements Supplier<T>, Serializable {
/*     */     final Function<? super F, T> function;
/*     */     final Supplier<F> supplier;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SupplierComposition(Function<? super F, T> function, Supplier<F> supplier) {
/*  59 */       this.function = Preconditions.<Function<? super F, T>>checkNotNull(function);
/*  60 */       this.supplier = Preconditions.<Supplier<F>>checkNotNull(supplier);
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public T get() {
/*  66 */       return this.function.apply(this.supplier.get());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/*  71 */       if (obj instanceof SupplierComposition) {
/*  72 */         SupplierComposition<?, ?> that = (SupplierComposition<?, ?>)obj;
/*  73 */         return (this.function.equals(that.function) && this.supplier.equals(that.supplier));
/*     */       } 
/*  75 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  80 */       return Objects.hashCode(new Object[] { this.function, this.supplier });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  85 */       return "Suppliers.compose(" + this.function + ", " + this.supplier + ")";
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
/*     */   public static <T> Supplier<T> memoize(Supplier<T> delegate) {
/* 108 */     if (delegate instanceof NonSerializableMemoizingSupplier || delegate instanceof MemoizingSupplier)
/*     */     {
/* 110 */       return delegate;
/*     */     }
/* 112 */     return (delegate instanceof Serializable) ? 
/* 113 */       new MemoizingSupplier<>(delegate) : 
/* 114 */       new NonSerializableMemoizingSupplier<>(delegate);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static class MemoizingSupplier<T> implements Supplier<T>, Serializable {
/*     */     final Supplier<T> delegate;
/*     */     volatile transient boolean initialized;
/*     */     @CheckForNull
/*     */     transient T value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     MemoizingSupplier(Supplier<T> delegate) {
/* 126 */       this.delegate = Preconditions.<Supplier<T>>checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public T get() {
/* 133 */       if (!this.initialized) {
/* 134 */         synchronized (this) {
/* 135 */           if (!this.initialized) {
/* 136 */             T t = this.delegate.get();
/* 137 */             this.value = t;
/* 138 */             this.initialized = true;
/* 139 */             return t;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 144 */       return NullnessCasts.uncheckedCastNullableTToT(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 149 */       return "Suppliers.memoize(" + (
/* 150 */         this.initialized ? ("<supplier that returned " + this.value + ">") : (String)this.delegate) + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class NonSerializableMemoizingSupplier<T>
/*     */     implements Supplier<T>
/*     */   {
/*     */     private static final Supplier<Void> SUCCESSFULLY_COMPUTED = () -> {
/*     */         throw new IllegalStateException();
/*     */       };
/*     */     
/*     */     private volatile Supplier<T> delegate;
/*     */     
/*     */     @CheckForNull
/*     */     private T value;
/*     */ 
/*     */     
/*     */     NonSerializableMemoizingSupplier(Supplier<T> delegate) {
/* 170 */       this.delegate = Preconditions.<Supplier<T>>checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public T get() {
/* 178 */       if (this.delegate != SUCCESSFULLY_COMPUTED) {
/* 179 */         synchronized (this) {
/* 180 */           if (this.delegate != SUCCESSFULLY_COMPUTED) {
/* 181 */             T t = this.delegate.get();
/* 182 */             this.value = t;
/* 183 */             this.delegate = (Supplier)SUCCESSFULLY_COMPUTED;
/* 184 */             return t;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 189 */       return NullnessCasts.uncheckedCastNullableTToT(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 194 */       Supplier<T> delegate = this.delegate;
/* 195 */       return "Suppliers.memoize(" + (
/* 196 */         (delegate == SUCCESSFULLY_COMPUTED) ? (
/* 197 */         "<supplier that returned " + this.value + ">") : 
/* 198 */         (String)delegate) + ")";
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
/*     */   public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> delegate, long duration, TimeUnit unit) {
/* 227 */     return new ExpiringMemoizingSupplier<>(delegate, duration, unit);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static class ExpiringMemoizingSupplier<T>
/*     */     implements Supplier<T>, Serializable {
/*     */     final Supplier<T> delegate;
/*     */     final long durationNanos;
/*     */     @CheckForNull
/*     */     volatile transient T value;
/*     */     volatile transient long expirationNanos;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ExpiringMemoizingSupplier(Supplier<T> delegate, long duration, TimeUnit unit) {
/* 241 */       this.delegate = Preconditions.<Supplier<T>>checkNotNull(delegate);
/* 242 */       this.durationNanos = unit.toNanos(duration);
/* 243 */       Preconditions.checkArgument((duration > 0L), "duration (%s %s) must be > 0", duration, unit);
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
/*     */     @ParametricNullness
/*     */     public T get() {
/* 256 */       long nanos = this.expirationNanos;
/* 257 */       long now = System.nanoTime();
/* 258 */       if (nanos == 0L || now - nanos >= 0L) {
/* 259 */         synchronized (this) {
/* 260 */           if (nanos == this.expirationNanos) {
/* 261 */             T t = this.delegate.get();
/* 262 */             this.value = t;
/* 263 */             nanos = now + this.durationNanos;
/*     */ 
/*     */             
/* 266 */             this.expirationNanos = (nanos == 0L) ? 1L : nanos;
/* 267 */             return t;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 272 */       return NullnessCasts.uncheckedCastNullableTToT(this.value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 279 */       return "Suppliers.memoizeWithExpiration(" + this.delegate + ", " + this.durationNanos + ", NANOS)";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Supplier<T> ofInstance(@ParametricNullness T instance) {
/* 288 */     return new SupplierOfInstance<>(instance);
/*     */   }
/*     */   
/*     */   private static class SupplierOfInstance<T> implements Supplier<T>, Serializable {
/*     */     @ParametricNullness
/*     */     final T instance;
/*     */     
/*     */     SupplierOfInstance(@ParametricNullness T instance) {
/* 296 */       this.instance = instance;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     @ParametricNullness
/*     */     public T get() {
/* 302 */       return this.instance;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 307 */       if (obj instanceof SupplierOfInstance) {
/* 308 */         SupplierOfInstance<?> that = (SupplierOfInstance)obj;
/* 309 */         return Objects.equal(this.instance, that.instance);
/*     */       } 
/* 311 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 316 */       return Objects.hashCode(new Object[] { this.instance });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 321 */       return "Suppliers.ofInstance(" + this.instance + ")";
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
/*     */   public static <T> Supplier<T> synchronizedSupplier(Supplier<T> delegate) {
/* 333 */     return new ThreadSafeSupplier<>(delegate);
/*     */   }
/*     */   
/*     */   private static class ThreadSafeSupplier<T> implements Supplier<T>, Serializable {
/*     */     final Supplier<T> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ThreadSafeSupplier(Supplier<T> delegate) {
/* 341 */       this.delegate = Preconditions.<Supplier<T>>checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public T get() {
/* 347 */       synchronized (this.delegate) {
/* 348 */         return this.delegate.get();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 354 */       return "Suppliers.synchronizedSupplier(" + this.delegate + ")";
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
/*     */   public static <T> Function<Supplier<T>, T> supplierFunction() {
/* 370 */     SupplierFunction<T> sf = SupplierFunctionImpl.INSTANCE;
/* 371 */     return sf;
/*     */   }
/*     */   
/*     */   private static interface SupplierFunction<T> extends Function<Supplier<T>, T> {}
/*     */   
/*     */   private enum SupplierFunctionImpl implements SupplierFunction<Object> {
/* 377 */     INSTANCE;
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Object apply(Supplier<Object> input) {
/* 383 */       return input.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 388 */       return "Suppliers.supplierFunction()";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Suppliers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */