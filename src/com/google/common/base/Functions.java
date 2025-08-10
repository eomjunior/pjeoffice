/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class Functions
/*     */ {
/*     */   public static Function<Object, String> toStringFunction() {
/*  63 */     return ToStringFunction.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum ToStringFunction
/*     */     implements Function<Object, String> {
/*  68 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public String apply(Object o) {
/*  72 */       Preconditions.checkNotNull(o);
/*  73 */       return o.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  78 */       return "Functions.toStringFunction()";
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
/*     */   public static <E> Function<E, E> identity() {
/*  91 */     return IdentityFunction.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum IdentityFunction
/*     */     implements Function<Object, Object> {
/*  96 */     INSTANCE;
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Object apply(@CheckForNull Object o) {
/* 101 */       return o;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 106 */       return "Functions.identity()";
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
/*     */   public static <K, V> Function<K, V> forMap(Map<K, V> map) {
/* 125 */     return new FunctionForMapNoDefault<>(map);
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
/*     */   public static <K, V> Function<K, V> forMap(Map<K, ? extends V> map, @ParametricNullness V defaultValue) {
/* 143 */     return new ForMapWithDefault<>(map, defaultValue);
/*     */   }
/*     */   
/*     */   private static class FunctionForMapNoDefault<K, V>
/*     */     implements Function<K, V>, Serializable {
/*     */     final Map<K, V> map;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     FunctionForMapNoDefault(Map<K, V> map) {
/* 152 */       this.map = Preconditions.<Map<K, V>>checkNotNull(map);
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public V apply(@ParametricNullness K key) {
/* 158 */       V result = this.map.get(key);
/* 159 */       Preconditions.checkArgument((result != null || this.map.containsKey(key)), "Key '%s' not present in map", key);
/*     */       
/* 161 */       return NullnessCasts.uncheckedCastNullableTToT(result);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object o) {
/* 166 */       if (o instanceof FunctionForMapNoDefault) {
/* 167 */         FunctionForMapNoDefault<?, ?> that = (FunctionForMapNoDefault<?, ?>)o;
/* 168 */         return this.map.equals(that.map);
/*     */       } 
/* 170 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 175 */       return this.map.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 180 */       return "Functions.forMap(" + this.map + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ForMapWithDefault<K, V>
/*     */     implements Function<K, V>, Serializable {
/*     */     final Map<K, ? extends V> map;
/*     */     @ParametricNullness
/*     */     final V defaultValue;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ForMapWithDefault(Map<K, ? extends V> map, @ParametricNullness V defaultValue) {
/* 192 */       this.map = Preconditions.<Map<K, ? extends V>>checkNotNull(map);
/* 193 */       this.defaultValue = defaultValue;
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public V apply(@ParametricNullness K key) {
/* 199 */       V result = this.map.get(key);
/*     */       
/* 201 */       return (result != null || this.map.containsKey(key)) ? 
/* 202 */         NullnessCasts.<V>uncheckedCastNullableTToT(result) : 
/* 203 */         this.defaultValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object o) {
/* 208 */       if (o instanceof ForMapWithDefault) {
/* 209 */         ForMapWithDefault<?, ?> that = (ForMapWithDefault<?, ?>)o;
/* 210 */         return (this.map.equals(that.map) && Objects.equal(this.defaultValue, that.defaultValue));
/*     */       } 
/* 212 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 217 */       return Objects.hashCode(new Object[] { this.map, this.defaultValue });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 223 */       return "Functions.forMap(" + this.map + ", defaultValue=" + this.defaultValue + ")";
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
/*     */   public static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, ? extends B> f) {
/* 243 */     return new FunctionComposition<>(g, f);
/*     */   }
/*     */   
/*     */   private static class FunctionComposition<A, B, C>
/*     */     implements Function<A, C>, Serializable {
/*     */     private final Function<B, C> g;
/*     */     private final Function<A, ? extends B> f;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public FunctionComposition(Function<B, C> g, Function<A, ? extends B> f) {
/* 253 */       this.g = Preconditions.<Function<B, C>>checkNotNull(g);
/* 254 */       this.f = Preconditions.<Function<A, ? extends B>>checkNotNull(f);
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public C apply(@ParametricNullness A a) {
/* 260 */       return this.g.apply(this.f.apply(a));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 265 */       if (obj instanceof FunctionComposition) {
/* 266 */         FunctionComposition<?, ?, ?> that = (FunctionComposition<?, ?, ?>)obj;
/* 267 */         return (this.f.equals(that.f) && this.g.equals(that.g));
/*     */       } 
/* 269 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 274 */       return this.f.hashCode() ^ this.g.hashCode();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 280 */       return this.g + "(" + this.f + ")";
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
/*     */   public static <T> Function<T, Boolean> forPredicate(Predicate<T> predicate) {
/* 296 */     return new PredicateFunction<>(predicate);
/*     */   }
/*     */   
/*     */   private static class PredicateFunction<T>
/*     */     implements Function<T, Boolean>, Serializable {
/*     */     private final Predicate<T> predicate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private PredicateFunction(Predicate<T> predicate) {
/* 305 */       this.predicate = Preconditions.<Predicate<T>>checkNotNull(predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public Boolean apply(@ParametricNullness T t) {
/* 310 */       return Boolean.valueOf(this.predicate.apply(t));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 315 */       if (obj instanceof PredicateFunction) {
/* 316 */         PredicateFunction<?> that = (PredicateFunction)obj;
/* 317 */         return this.predicate.equals(that.predicate);
/*     */       } 
/* 319 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 324 */       return this.predicate.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 329 */       return "Functions.forPredicate(" + this.predicate + ")";
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
/*     */   public static <E> Function<Object, E> constant(@ParametricNullness E value) {
/* 345 */     return new ConstantFunction<>(value);
/*     */   }
/*     */   
/*     */   private static class ConstantFunction<E> implements Function<Object, E>, Serializable {
/*     */     @ParametricNullness
/*     */     private final E value;
/*     */     
/*     */     public ConstantFunction(@ParametricNullness E value) {
/* 353 */       this.value = value;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     @ParametricNullness
/*     */     public E apply(@CheckForNull Object from) {
/* 359 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 364 */       if (obj instanceof ConstantFunction) {
/* 365 */         ConstantFunction<?> that = (ConstantFunction)obj;
/* 366 */         return Objects.equal(this.value, that.value);
/*     */       } 
/* 368 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 373 */       return (this.value == null) ? 0 : this.value.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 378 */       return "Functions.constant(" + this.value + ")";
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
/*     */   public static <F, T> Function<F, T> forSupplier(Supplier<T> supplier) {
/* 393 */     return new SupplierFunction<>(supplier);
/*     */   }
/*     */   
/*     */   private static class SupplierFunction<F, T>
/*     */     implements Function<F, T>, Serializable
/*     */   {
/*     */     private final Supplier<T> supplier;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     private SupplierFunction(Supplier<T> supplier) {
/* 403 */       this.supplier = Preconditions.<Supplier<T>>checkNotNull(supplier);
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public T apply(@ParametricNullness F input) {
/* 409 */       return this.supplier.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 414 */       if (obj instanceof SupplierFunction) {
/* 415 */         SupplierFunction<?, ?> that = (SupplierFunction<?, ?>)obj;
/* 416 */         return this.supplier.equals(that.supplier);
/*     */       } 
/* 418 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 423 */       return this.supplier.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 428 */       return "Functions.forSupplier(" + this.supplier + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Functions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */