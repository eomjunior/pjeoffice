/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.ImmutableLongArray;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.atomic.AtomicLongArray;
/*     */ import java.util.function.DoubleBinaryOperator;
/*     */ import java.util.function.DoubleUnaryOperator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtIncompatible
/*     */ @J2ktIncompatible
/*     */ public class AtomicDoubleArray
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   private transient AtomicLongArray longs;
/*     */   
/*     */   public AtomicDoubleArray(int length) {
/*  66 */     this.longs = new AtomicLongArray(length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicDoubleArray(double[] array) {
/*  77 */     int len = array.length;
/*  78 */     long[] longArray = new long[len];
/*  79 */     for (int i = 0; i < len; i++) {
/*  80 */       longArray[i] = Double.doubleToRawLongBits(array[i]);
/*     */     }
/*  82 */     this.longs = new AtomicLongArray(longArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int length() {
/*  91 */     return this.longs.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double get(int i) {
/* 101 */     return Double.longBitsToDouble(this.longs.get(i));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void set(int i, double newValue) {
/* 111 */     long next = Double.doubleToRawLongBits(newValue);
/* 112 */     this.longs.set(i, next);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void lazySet(int i, double newValue) {
/* 122 */     long next = Double.doubleToRawLongBits(newValue);
/* 123 */     this.longs.lazySet(i, next);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getAndSet(int i, double newValue) {
/* 134 */     long next = Double.doubleToRawLongBits(newValue);
/* 135 */     return Double.longBitsToDouble(this.longs.getAndSet(i, next));
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
/*     */   public final boolean compareAndSet(int i, double expect, double update) {
/* 149 */     return this.longs.compareAndSet(i, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
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
/*     */   public final boolean weakCompareAndSet(int i, double expect, double update) {
/* 167 */     return this.longs.weakCompareAndSet(i, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final double getAndAdd(int i, double delta) {
/* 179 */     return getAndAccumulate(i, delta, Double::sum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public double addAndGet(int i, double delta) {
/* 191 */     return accumulateAndGet(i, delta, Double::sum);
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
/*     */   @CanIgnoreReturnValue
/*     */   public final double getAndAccumulate(int i, double x, DoubleBinaryOperator accumulatorFunction) {
/* 206 */     Preconditions.checkNotNull(accumulatorFunction);
/* 207 */     return getAndUpdate(i, oldValue -> accumulatorFunction.applyAsDouble(oldValue, x));
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
/*     */   @CanIgnoreReturnValue
/*     */   public final double accumulateAndGet(int i, double x, DoubleBinaryOperator accumulatorFunction) {
/* 222 */     Preconditions.checkNotNull(accumulatorFunction);
/* 223 */     return updateAndGet(i, oldValue -> accumulatorFunction.applyAsDouble(oldValue, x));
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
/*     */   @CanIgnoreReturnValue
/*     */   public final double getAndUpdate(int i, DoubleUnaryOperator updaterFunction) {
/*     */     while (true) {
/* 238 */       long current = this.longs.get(i);
/* 239 */       double currentVal = Double.longBitsToDouble(current);
/* 240 */       double nextVal = updaterFunction.applyAsDouble(currentVal);
/* 241 */       long next = Double.doubleToRawLongBits(nextVal);
/* 242 */       if (this.longs.compareAndSet(i, current, next)) {
/* 243 */         return currentVal;
/*     */       }
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
/*     */   @CanIgnoreReturnValue
/*     */   public final double updateAndGet(int i, DoubleUnaryOperator updaterFunction) {
/*     */     while (true) {
/* 260 */       long current = this.longs.get(i);
/* 261 */       double currentVal = Double.longBitsToDouble(current);
/* 262 */       double nextVal = updaterFunction.applyAsDouble(currentVal);
/* 263 */       long next = Double.doubleToRawLongBits(nextVal);
/* 264 */       if (this.longs.compareAndSet(i, current, next)) {
/* 265 */         return nextVal;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 277 */     int iMax = length() - 1;
/* 278 */     if (iMax == -1) {
/* 279 */       return "[]";
/*     */     }
/*     */ 
/*     */     
/* 283 */     StringBuilder b = new StringBuilder(19 * (iMax + 1));
/* 284 */     b.append('[');
/* 285 */     for (int i = 0;; i++) {
/* 286 */       b.append(Double.longBitsToDouble(this.longs.get(i)));
/* 287 */       if (i == iMax) {
/* 288 */         return b.append(']').toString();
/*     */       }
/* 290 */       b.append(',').append(' ');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 301 */     s.defaultWriteObject();
/*     */ 
/*     */     
/* 304 */     int length = length();
/* 305 */     s.writeInt(length);
/*     */ 
/*     */     
/* 308 */     for (int i = 0; i < length; i++) {
/* 309 */       s.writeDouble(get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 316 */     s.defaultReadObject();
/*     */     
/* 318 */     int length = s.readInt();
/* 319 */     ImmutableLongArray.Builder builder = ImmutableLongArray.builder();
/* 320 */     for (int i = 0; i < length; i++) {
/* 321 */       builder.add(Double.doubleToRawLongBits(s.readDouble()));
/*     */     }
/* 323 */     this.longs = new AtomicLongArray(builder.build().toArray());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/AtomicDoubleArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */