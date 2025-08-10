/*     */ package io.reactivex.schedulers;
/*     */ 
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Timed<T>
/*     */ {
/*     */   final T value;
/*     */   final long time;
/*     */   final TimeUnit unit;
/*     */   
/*     */   public Timed(@NonNull T value, long time, @NonNull TimeUnit unit) {
/*  39 */     this.value = value;
/*  40 */     this.time = time;
/*  41 */     this.unit = (TimeUnit)ObjectHelper.requireNonNull(unit, "unit is null");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public T value() {
/*  50 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NonNull
/*     */   public TimeUnit unit() {
/*  59 */     return this.unit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long time() {
/*  67 */     return this.time;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long time(@NonNull TimeUnit unit) {
/*  76 */     return unit.convert(this.time, this.unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/*  81 */     if (other instanceof Timed) {
/*  82 */       Timed<?> o = (Timed)other;
/*  83 */       return (ObjectHelper.equals(this.value, o.value) && this.time == o.time && 
/*     */         
/*  85 */         ObjectHelper.equals(this.unit, o.unit));
/*     */     } 
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  92 */     int h = (this.value != null) ? this.value.hashCode() : 0;
/*  93 */     h = h * 31 + (int)(this.time >>> 31L ^ this.time);
/*  94 */     h = h * 31 + this.unit.hashCode();
/*  95 */     return h;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     return "Timed[time=" + this.time + ", unit=" + this.unit + ", value=" + this.value + "]";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/schedulers/Timed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */