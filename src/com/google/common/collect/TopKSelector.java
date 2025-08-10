/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ final class TopKSelector<T>
/*     */ {
/*     */   private final int k;
/*     */   private final Comparator<? super T> comparator;
/*     */   private final T[] buffer;
/*     */   private int bufferSize;
/*     */   @CheckForNull
/*     */   private T threshold;
/*     */   
/*     */   public static <T extends Comparable<? super T>> TopKSelector<T> least(int k) {
/*  69 */     return least(k, Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> TopKSelector<T> least(int k, Comparator<? super T> comparator) {
/*  80 */     return new TopKSelector<>(comparator, k);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Comparable<? super T>> TopKSelector<T> greatest(int k) {
/*  91 */     return greatest(k, Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> TopKSelector<T> greatest(int k, Comparator<? super T> comparator) {
/* 102 */     return new TopKSelector<>(Ordering.<T>from(comparator).reverse(), k);
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
/*     */   private TopKSelector(Comparator<? super T> comparator, int k) {
/* 123 */     this.comparator = (Comparator<? super T>)Preconditions.checkNotNull(comparator, "comparator");
/* 124 */     this.k = k;
/* 125 */     Preconditions.checkArgument((k >= 0), "k (%s) must be >= 0", k);
/* 126 */     Preconditions.checkArgument((k <= 1073741823), "k (%s) must be <= Integer.MAX_VALUE / 2", k);
/* 127 */     this.buffer = (T[])new Object[IntMath.checkedMultiply(k, 2)];
/* 128 */     this.bufferSize = 0;
/* 129 */     this.threshold = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void offer(@ParametricNullness T elem) {
/* 137 */     if (this.k == 0)
/*     */       return; 
/* 139 */     if (this.bufferSize == 0) {
/* 140 */       this.buffer[0] = elem;
/* 141 */       this.threshold = elem;
/* 142 */       this.bufferSize = 1;
/* 143 */     } else if (this.bufferSize < this.k) {
/* 144 */       this.buffer[this.bufferSize++] = elem;
/*     */       
/* 146 */       if (this.comparator.compare(elem, NullnessCasts.uncheckedCastNullableTToT(this.threshold)) > 0) {
/* 147 */         this.threshold = elem;
/*     */       }
/*     */     }
/* 150 */     else if (this.comparator.compare(elem, NullnessCasts.uncheckedCastNullableTToT(this.threshold)) < 0) {
/*     */       
/* 152 */       this.buffer[this.bufferSize++] = elem;
/* 153 */       if (this.bufferSize == 2 * this.k) {
/* 154 */         trim();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void trim() {
/* 164 */     int left = 0;
/* 165 */     int right = 2 * this.k - 1;
/*     */     
/* 167 */     int minThresholdPosition = 0;
/*     */ 
/*     */ 
/*     */     
/* 171 */     int iterations = 0;
/* 172 */     int maxIterations = IntMath.log2(right - left, RoundingMode.CEILING) * 3;
/* 173 */     while (left < right) {
/* 174 */       int pivotIndex = left + right + 1 >>> 1;
/*     */       
/* 176 */       int pivotNewIndex = partition(left, right, pivotIndex);
/*     */       
/* 178 */       if (pivotNewIndex > this.k) {
/* 179 */         right = pivotNewIndex - 1;
/* 180 */       } else if (pivotNewIndex < this.k) {
/* 181 */         left = Math.max(pivotNewIndex, left + 1);
/* 182 */         minThresholdPosition = pivotNewIndex;
/*     */       } else {
/*     */         break;
/*     */       } 
/* 186 */       iterations++;
/* 187 */       if (iterations >= maxIterations) {
/*     */         
/* 189 */         T[] castBuffer = this.buffer;
/*     */         
/* 191 */         Arrays.sort(castBuffer, left, right + 1, this.comparator);
/*     */         break;
/*     */       } 
/*     */     } 
/* 195 */     this.bufferSize = this.k;
/*     */     
/* 197 */     this.threshold = NullnessCasts.uncheckedCastNullableTToT(this.buffer[minThresholdPosition]);
/* 198 */     for (int i = minThresholdPosition + 1; i < this.k; i++) {
/* 199 */       if (this.comparator.compare(
/* 200 */           NullnessCasts.uncheckedCastNullableTToT(this.buffer[i]), NullnessCasts.uncheckedCastNullableTToT(this.threshold)) > 0)
/*     */       {
/* 202 */         this.threshold = this.buffer[i];
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
/*     */   private int partition(int left, int right, int pivotIndex) {
/* 214 */     T pivotValue = NullnessCasts.uncheckedCastNullableTToT(this.buffer[pivotIndex]);
/* 215 */     this.buffer[pivotIndex] = this.buffer[right];
/*     */     
/* 217 */     int pivotNewIndex = left;
/* 218 */     for (int i = left; i < right; i++) {
/* 219 */       if (this.comparator.compare(NullnessCasts.uncheckedCastNullableTToT(this.buffer[i]), pivotValue) < 0) {
/* 220 */         swap(pivotNewIndex, i);
/* 221 */         pivotNewIndex++;
/*     */       } 
/*     */     } 
/* 224 */     this.buffer[right] = this.buffer[pivotNewIndex];
/* 225 */     this.buffer[pivotNewIndex] = pivotValue;
/* 226 */     return pivotNewIndex;
/*     */   }
/*     */   
/*     */   private void swap(int i, int j) {
/* 230 */     T tmp = this.buffer[i];
/* 231 */     this.buffer[i] = this.buffer[j];
/* 232 */     this.buffer[j] = tmp;
/*     */   }
/*     */   
/*     */   TopKSelector<T> combine(TopKSelector<T> other) {
/* 236 */     for (int i = 0; i < other.bufferSize; i++) {
/* 237 */       offer(NullnessCasts.uncheckedCastNullableTToT(other.buffer[i]));
/*     */     }
/* 239 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void offerAll(Iterable<? extends T> elements) {
/* 250 */     offerAll(elements.iterator());
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
/*     */   public void offerAll(Iterator<? extends T> elements) {
/* 262 */     while (elements.hasNext()) {
/* 263 */       offer(elements.next());
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
/*     */   public List<T> topK() {
/* 277 */     T[] castBuffer = this.buffer;
/* 278 */     Arrays.sort(castBuffer, 0, this.bufferSize, this.comparator);
/* 279 */     if (this.bufferSize > this.k) {
/* 280 */       Arrays.fill((Object[])this.buffer, this.k, this.buffer.length, (Object)null);
/* 281 */       this.bufferSize = this.k;
/* 282 */       this.threshold = this.buffer[this.k - 1];
/*     */     } 
/*     */     
/* 285 */     T[] topK = Arrays.copyOf(castBuffer, this.bufferSize);
/*     */     
/* 287 */     return Collections.unmodifiableList(Arrays.asList(topK));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/TopKSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */