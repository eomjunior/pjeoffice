/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class SortedLists
/*     */ {
/*     */   enum KeyPresentBehavior
/*     */   {
/*  50 */     ANY_PRESENT
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*     */       <E> int resultIndex(Comparator<? super E> comparator, @ParametricNullness E key, List<? extends E> list, int foundIndex)
/*     */       {
/*  57 */         return foundIndex;
/*     */       }
/*     */     },
/*     */     
/*  61 */     LAST_PRESENT
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       <E> int resultIndex(Comparator<? super E> comparator, @ParametricNullness E key, List<? extends E> list, int foundIndex)
/*     */       {
/*  70 */         int lower = foundIndex;
/*  71 */         int upper = list.size() - 1;
/*     */         
/*  73 */         while (lower < upper) {
/*  74 */           int middle = lower + upper + 1 >>> 1;
/*  75 */           int c = comparator.compare(list.get(middle), key);
/*  76 */           if (c > 0) {
/*  77 */             upper = middle - 1; continue;
/*     */           } 
/*  79 */           lower = middle;
/*     */         } 
/*     */         
/*  82 */         return lower;
/*     */       }
/*     */     },
/*     */     
/*  86 */     FIRST_PRESENT
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       <E> int resultIndex(Comparator<? super E> comparator, @ParametricNullness E key, List<? extends E> list, int foundIndex)
/*     */       {
/*  95 */         int lower = 0;
/*  96 */         int upper = foundIndex;
/*     */ 
/*     */         
/*  99 */         while (lower < upper) {
/* 100 */           int middle = lower + upper >>> 1;
/* 101 */           int c = comparator.compare(list.get(middle), key);
/* 102 */           if (c < 0) {
/* 103 */             lower = middle + 1; continue;
/*     */           } 
/* 105 */           upper = middle;
/*     */         } 
/*     */         
/* 108 */         return lower;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     FIRST_AFTER
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*     */       public <E> int resultIndex(Comparator<? super E> comparator, @ParametricNullness E key, List<? extends E> list, int foundIndex)
/*     */       {
/* 122 */         return LAST_PRESENT.<E>resultIndex(comparator, key, list, foundIndex) + 1;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     LAST_BEFORE
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*     */       public <E> int resultIndex(Comparator<? super E> comparator, @ParametricNullness E key, List<? extends E> list, int foundIndex)
/*     */       {
/* 136 */         return FIRST_PRESENT.<E>resultIndex(comparator, key, list, foundIndex) - 1;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract <E> int resultIndex(Comparator<? super E> param1Comparator, @ParametricNullness E param1E, List<? extends E> param1List, int param1Int);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   enum KeyAbsentBehavior
/*     */   {
/* 156 */     NEXT_LOWER
/*     */     {
/*     */       int resultIndex(int higherIndex) {
/* 159 */         return higherIndex - 1;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     NEXT_HIGHER
/*     */     {
/*     */       public int resultIndex(int higherIndex) {
/* 169 */         return higherIndex;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 184 */     INVERTED_INSERTION_INDEX
/*     */     {
/*     */       public int resultIndex(int higherIndex) {
/* 187 */         return higherIndex ^ 0xFFFFFFFF;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract int resultIndex(int param1Int);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable> int binarySearch(List<? extends E> list, E e, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 206 */     Preconditions.checkNotNull(e);
/* 207 */     return binarySearch(list, e, Ordering.natural(), presentBehavior, absentBehavior);
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
/*     */   public static <E, K extends Comparable> int binarySearch(List<E> list, Function<? super E, K> keyFunction, K key, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 222 */     Preconditions.checkNotNull(key);
/* 223 */     return binarySearch(list, keyFunction, key, 
/* 224 */         Ordering.natural(), presentBehavior, absentBehavior);
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
/*     */   public static <E, K> int binarySearch(List<E> list, Function<? super E, K> keyFunction, @ParametricNullness K key, Comparator<? super K> keyComparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 241 */     return binarySearch(
/* 242 */         Lists.transform(list, keyFunction), key, keyComparator, presentBehavior, absentBehavior);
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
/*     */   public static <E> int binarySearch(List<? extends E> list, @ParametricNullness E key, Comparator<? super E> comparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior) {
/* 274 */     Preconditions.checkNotNull(comparator);
/* 275 */     Preconditions.checkNotNull(list);
/* 276 */     Preconditions.checkNotNull(presentBehavior);
/* 277 */     Preconditions.checkNotNull(absentBehavior);
/* 278 */     if (!(list instanceof java.util.RandomAccess)) {
/* 279 */       list = Lists.newArrayList(list);
/*     */     }
/*     */ 
/*     */     
/* 283 */     int lower = 0;
/* 284 */     int upper = list.size() - 1;
/*     */     
/* 286 */     while (lower <= upper) {
/* 287 */       int middle = lower + upper >>> 1;
/* 288 */       int c = comparator.compare(key, list.get(middle));
/* 289 */       if (c < 0) {
/* 290 */         upper = middle - 1; continue;
/* 291 */       }  if (c > 0) {
/* 292 */         lower = middle + 1; continue;
/*     */       } 
/* 294 */       return lower + presentBehavior
/* 295 */         .<E>resultIndex(comparator, key, list
/* 296 */           .subList(lower, upper + 1), middle - lower);
/*     */     } 
/*     */     
/* 299 */     return absentBehavior.resultIndex(lower);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/SortedLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */