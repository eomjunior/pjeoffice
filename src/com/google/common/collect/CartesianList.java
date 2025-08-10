/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.util.AbstractList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.RandomAccess;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ final class CartesianList<E>
/*     */   extends AbstractList<List<E>>
/*     */   implements RandomAccess
/*     */ {
/*     */   private final transient ImmutableList<List<E>> axes;
/*     */   private final transient int[] axesSizeProduct;
/*     */   
/*     */   static <E> List<List<E>> create(List<? extends List<? extends E>> lists) {
/*  42 */     ImmutableList.Builder<List<E>> axesBuilder = new ImmutableList.Builder<>(lists.size());
/*  43 */     for (List<? extends E> list : lists) {
/*  44 */       List<E> copy = ImmutableList.copyOf(list);
/*  45 */       if (copy.isEmpty()) {
/*  46 */         return ImmutableList.of();
/*     */       }
/*  48 */       axesBuilder.add(copy);
/*     */     } 
/*  50 */     return new CartesianList<>(axesBuilder.build());
/*     */   }
/*     */   
/*     */   CartesianList(ImmutableList<List<E>> axes) {
/*  54 */     this.axes = axes;
/*  55 */     int[] axesSizeProduct = new int[axes.size() + 1];
/*  56 */     axesSizeProduct[axes.size()] = 1;
/*     */     try {
/*  58 */       for (int i = axes.size() - 1; i >= 0; i--) {
/*  59 */         axesSizeProduct[i] = IntMath.checkedMultiply(axesSizeProduct[i + 1], ((List)axes.get(i)).size());
/*     */       }
/*  61 */     } catch (ArithmeticException e) {
/*  62 */       throw new IllegalArgumentException("Cartesian product too large; must have size at most Integer.MAX_VALUE");
/*     */     } 
/*     */     
/*  65 */     this.axesSizeProduct = axesSizeProduct;
/*     */   }
/*     */   
/*     */   private int getAxisIndexForProductIndex(int index, int axis) {
/*  69 */     return index / this.axesSizeProduct[axis + 1] % ((List)this.axes.get(axis)).size();
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(@CheckForNull Object o) {
/*  74 */     if (!(o instanceof List)) {
/*  75 */       return -1;
/*     */     }
/*  77 */     List<?> list = (List)o;
/*  78 */     if (list.size() != this.axes.size()) {
/*  79 */       return -1;
/*     */     }
/*  81 */     ListIterator<?> itr = list.listIterator();
/*  82 */     int computedIndex = 0;
/*  83 */     while (itr.hasNext()) {
/*  84 */       int axisIndex = itr.nextIndex();
/*  85 */       int elemIndex = ((List)this.axes.get(axisIndex)).indexOf(itr.next());
/*  86 */       if (elemIndex == -1) {
/*  87 */         return -1;
/*     */       }
/*  89 */       computedIndex += elemIndex * this.axesSizeProduct[axisIndex + 1];
/*     */     } 
/*  91 */     return computedIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(@CheckForNull Object o) {
/*  96 */     if (!(o instanceof List)) {
/*  97 */       return -1;
/*     */     }
/*  99 */     List<?> list = (List)o;
/* 100 */     if (list.size() != this.axes.size()) {
/* 101 */       return -1;
/*     */     }
/* 103 */     ListIterator<?> itr = list.listIterator();
/* 104 */     int computedIndex = 0;
/* 105 */     while (itr.hasNext()) {
/* 106 */       int axisIndex = itr.nextIndex();
/* 107 */       int elemIndex = ((List)this.axes.get(axisIndex)).lastIndexOf(itr.next());
/* 108 */       if (elemIndex == -1) {
/* 109 */         return -1;
/*     */       }
/* 111 */       computedIndex += elemIndex * this.axesSizeProduct[axisIndex + 1];
/*     */     } 
/* 113 */     return computedIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableList<E> get(final int index) {
/* 118 */     Preconditions.checkElementIndex(index, size());
/* 119 */     return new ImmutableList<E>()
/*     */       {
/*     */         public int size()
/*     */         {
/* 123 */           return CartesianList.this.axes.size();
/*     */         }
/*     */ 
/*     */         
/*     */         public E get(int axis) {
/* 128 */           Preconditions.checkElementIndex(axis, size());
/* 129 */           int axisIndex = CartesianList.this.getAxisIndexForProductIndex(index, axis);
/* 130 */           return ((List<E>)CartesianList.this.axes.get(axis)).get(axisIndex);
/*     */         }
/*     */ 
/*     */         
/*     */         boolean isPartialView() {
/* 135 */           return true;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         @J2ktIncompatible
/*     */         @GwtIncompatible
/*     */         Object writeReplace() {
/* 144 */           return super.writeReplace();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 151 */     return this.axesSizeProduct[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object object) {
/* 156 */     if (!(object instanceof List)) {
/* 157 */       return false;
/*     */     }
/* 159 */     List<?> list = (List)object;
/* 160 */     if (list.size() != this.axes.size()) {
/* 161 */       return false;
/*     */     }
/* 163 */     int i = 0;
/* 164 */     for (Object o : list) {
/* 165 */       if (!((List)this.axes.get(i)).contains(o)) {
/* 166 */         return false;
/*     */       }
/* 168 */       i++;
/*     */     } 
/* 170 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/CartesianList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */