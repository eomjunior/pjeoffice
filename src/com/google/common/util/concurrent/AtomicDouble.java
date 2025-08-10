/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.ReflectionSupport;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.atomic.AtomicLongFieldUpdater;
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
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ @J2ktIncompatible
/*     */ @ReflectionSupport(ReflectionSupport.Level.FULL)
/*     */ public class AtomicDouble
/*     */   extends Number
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   private volatile transient long value;
/*  68 */   private static final AtomicLongFieldUpdater<AtomicDouble> updater = AtomicLongFieldUpdater.newUpdater(AtomicDouble.class, "value");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicDouble(double initialValue) {
/*  76 */     this.value = Double.doubleToRawLongBits(initialValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicDouble() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double get() {
/*  90 */     return Double.longBitsToDouble(this.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void set(double newValue) {
/*  99 */     long next = Double.doubleToRawLongBits(newValue);
/* 100 */     this.value = next;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void lazySet(double newValue) {
/* 109 */     long next = Double.doubleToRawLongBits(newValue);
/* 110 */     updater.lazySet(this, next);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getAndSet(double newValue) {
/* 120 */     long next = Double.doubleToRawLongBits(newValue);
/* 121 */     return Double.longBitsToDouble(updater.getAndSet(this, next));
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
/*     */   public final boolean compareAndSet(double expect, double update) {
/* 134 */     return updater.compareAndSet(this, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
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
/*     */   public final boolean weakCompareAndSet(double expect, double update) {
/* 151 */     return updater.weakCompareAndSet(this, 
/* 152 */         Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final double getAndAdd(double delta) {
/* 163 */     return getAndAccumulate(delta, Double::sum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final double addAndGet(double delta) {
/* 174 */     return accumulateAndGet(delta, Double::sum);
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
/*     */   public final double getAndAccumulate(double x, DoubleBinaryOperator accumulatorFunction) {
/* 188 */     Preconditions.checkNotNull(accumulatorFunction);
/* 189 */     return getAndUpdate(oldValue -> accumulatorFunction.applyAsDouble(oldValue, x));
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
/*     */   public final double accumulateAndGet(double x, DoubleBinaryOperator accumulatorFunction) {
/* 203 */     Preconditions.checkNotNull(accumulatorFunction);
/* 204 */     return updateAndGet(oldValue -> accumulatorFunction.applyAsDouble(oldValue, x));
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
/*     */   public final double getAndUpdate(DoubleUnaryOperator updateFunction) {
/*     */     while (true) {
/* 217 */       long current = this.value;
/* 218 */       double currentVal = Double.longBitsToDouble(current);
/* 219 */       double nextVal = updateFunction.applyAsDouble(currentVal);
/* 220 */       long next = Double.doubleToRawLongBits(nextVal);
/* 221 */       if (updater.compareAndSet(this, current, next)) {
/* 222 */         return currentVal;
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
/*     */   @CanIgnoreReturnValue
/*     */   public final double updateAndGet(DoubleUnaryOperator updateFunction) {
/*     */     while (true) {
/* 237 */       long current = this.value;
/* 238 */       double currentVal = Double.longBitsToDouble(current);
/* 239 */       double nextVal = updateFunction.applyAsDouble(currentVal);
/* 240 */       long next = Double.doubleToRawLongBits(nextVal);
/* 241 */       if (updater.compareAndSet(this, current, next)) {
/* 242 */         return nextVal;
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
/* 254 */     return Double.toString(get());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 263 */     return (int)get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 272 */     return (long)get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 281 */     return (float)get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 287 */     return get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 296 */     s.defaultWriteObject();
/*     */     
/* 298 */     s.writeDouble(get());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 304 */     s.defaultReadObject();
/*     */     
/* 306 */     set(s.readDouble());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/AtomicDouble.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */