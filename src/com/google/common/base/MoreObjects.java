/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.OptionalLong;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class MoreObjects
/*     */ {
/*     */   public static <T> T firstNonNull(@CheckForNull T first, @CheckForNull T second) {
/*  65 */     if (first != null) {
/*  66 */       return first;
/*     */     }
/*  68 */     if (second != null) {
/*  69 */       return second;
/*     */     }
/*  71 */     throw new NullPointerException("Both parameters are null");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ToStringHelper toStringHelper(Object self) {
/* 115 */     return new ToStringHelper(self.getClass().getSimpleName());
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
/*     */   public static ToStringHelper toStringHelper(Class<?> clazz) {
/* 129 */     return new ToStringHelper(clazz.getSimpleName());
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
/*     */   public static ToStringHelper toStringHelper(String className) {
/* 141 */     return new ToStringHelper(className);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ToStringHelper
/*     */   {
/*     */     private final String className;
/*     */ 
/*     */     
/* 152 */     private final ValueHolder holderHead = new ValueHolder();
/* 153 */     private ValueHolder holderTail = this.holderHead;
/*     */     
/*     */     private boolean omitNullValues = false;
/*     */     private boolean omitEmptyValues = false;
/*     */     
/*     */     private ToStringHelper(String className) {
/* 159 */       this.className = Preconditions.<String>checkNotNull(className);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper omitNullValues() {
/* 171 */       this.omitNullValues = true;
/* 172 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, @CheckForNull Object value) {
/* 182 */       return addHolder(name, value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, boolean value) {
/* 192 */       return addUnconditionalHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, char value) {
/* 202 */       return addUnconditionalHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, double value) {
/* 212 */       return addUnconditionalHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, float value) {
/* 222 */       return addUnconditionalHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, int value) {
/* 232 */       return addUnconditionalHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper add(String name, long value) {
/* 242 */       return addUnconditionalHolder(name, String.valueOf(value));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(@CheckForNull Object value) {
/* 253 */       return addHolder(value);
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
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(boolean value) {
/* 266 */       return addUnconditionalHolder(String.valueOf(value));
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
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(char value) {
/* 279 */       return addUnconditionalHolder(String.valueOf(value));
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
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(double value) {
/* 292 */       return addUnconditionalHolder(String.valueOf(value));
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
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(float value) {
/* 305 */       return addUnconditionalHolder(String.valueOf(value));
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
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(int value) {
/* 318 */       return addUnconditionalHolder(String.valueOf(value));
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
/*     */     @CanIgnoreReturnValue
/*     */     public ToStringHelper addValue(long value) {
/* 331 */       return addUnconditionalHolder(String.valueOf(value));
/*     */     }
/*     */ 
/*     */     
/*     */     private static boolean isEmpty(Object value) {
/* 336 */       if (value instanceof CharSequence)
/* 337 */         return (((CharSequence)value).length() == 0); 
/* 338 */       if (value instanceof Collection)
/* 339 */         return ((Collection)value).isEmpty(); 
/* 340 */       if (value instanceof Map)
/* 341 */         return ((Map)value).isEmpty(); 
/* 342 */       if (value instanceof Optional)
/* 343 */         return !((Optional)value).isPresent(); 
/* 344 */       if (value instanceof OptionalInt)
/* 345 */         return !((OptionalInt)value).isPresent(); 
/* 346 */       if (value instanceof OptionalLong)
/* 347 */         return !((OptionalLong)value).isPresent(); 
/* 348 */       if (value instanceof OptionalDouble)
/* 349 */         return !((OptionalDouble)value).isPresent(); 
/* 350 */       if (value instanceof Optional)
/* 351 */         return !((Optional)value).isPresent(); 
/* 352 */       if (value.getClass().isArray()) {
/* 353 */         return (Array.getLength(value) == 0);
/*     */       }
/* 355 */       return false;
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
/*     */ 
/*     */     
/*     */     public String toString() {
/* 369 */       boolean omitNullValuesSnapshot = this.omitNullValues;
/* 370 */       boolean omitEmptyValuesSnapshot = this.omitEmptyValues;
/* 371 */       String nextSeparator = "";
/* 372 */       StringBuilder builder = (new StringBuilder(32)).append(this.className).append('{');
/* 373 */       ValueHolder valueHolder = this.holderHead.next;
/* 374 */       for (; valueHolder != null; 
/* 375 */         valueHolder = valueHolder.next) {
/* 376 */         Object value = valueHolder.value;
/* 377 */         if (valueHolder instanceof UnconditionalValueHolder || ((value == null) ? !omitNullValuesSnapshot : (!omitEmptyValuesSnapshot || 
/*     */ 
/*     */           
/* 380 */           !isEmpty(value)))) {
/* 381 */           builder.append(nextSeparator);
/* 382 */           nextSeparator = ", ";
/*     */           
/* 384 */           if (valueHolder.name != null) {
/* 385 */             builder.append(valueHolder.name).append('=');
/*     */           }
/* 387 */           if (value != null && value.getClass().isArray()) {
/* 388 */             Object[] objectArray = { value };
/* 389 */             String arrayString = Arrays.deepToString(objectArray);
/* 390 */             builder.append(arrayString, 1, arrayString.length() - 1);
/*     */           } else {
/* 392 */             builder.append(value);
/*     */           } 
/*     */         } 
/*     */       } 
/* 396 */       return builder.append('}').toString();
/*     */     }
/*     */     
/*     */     private ValueHolder addHolder() {
/* 400 */       ValueHolder valueHolder = new ValueHolder();
/* 401 */       this.holderTail = this.holderTail.next = valueHolder;
/* 402 */       return valueHolder;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     private ToStringHelper addHolder(@CheckForNull Object value) {
/* 407 */       ValueHolder valueHolder = addHolder();
/* 408 */       valueHolder.value = value;
/* 409 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     private ToStringHelper addHolder(String name, @CheckForNull Object value) {
/* 414 */       ValueHolder valueHolder = addHolder();
/* 415 */       valueHolder.value = value;
/* 416 */       valueHolder.name = Preconditions.<String>checkNotNull(name);
/* 417 */       return this;
/*     */     }
/*     */     
/*     */     private UnconditionalValueHolder addUnconditionalHolder() {
/* 421 */       UnconditionalValueHolder valueHolder = new UnconditionalValueHolder();
/* 422 */       this.holderTail = this.holderTail.next = valueHolder;
/* 423 */       return valueHolder;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     private ToStringHelper addUnconditionalHolder(Object value) {
/* 428 */       UnconditionalValueHolder valueHolder = addUnconditionalHolder();
/* 429 */       valueHolder.value = value;
/* 430 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     private ToStringHelper addUnconditionalHolder(String name, Object value) {
/* 435 */       UnconditionalValueHolder valueHolder = addUnconditionalHolder();
/* 436 */       valueHolder.value = value;
/* 437 */       valueHolder.name = Preconditions.<String>checkNotNull(name);
/* 438 */       return this;
/*     */     }
/*     */     
/*     */     static class ValueHolder {
/*     */       @CheckForNull
/*     */       String name;
/*     */       @CheckForNull
/*     */       Object value;
/*     */       @CheckForNull
/*     */       ValueHolder next;
/*     */     }
/*     */     
/*     */     private static final class UnconditionalValueHolder extends ValueHolder {
/*     */       private UnconditionalValueHolder() {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/MoreObjects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */