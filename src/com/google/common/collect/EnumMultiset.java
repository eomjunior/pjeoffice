/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.ObjIntConsumer;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ @J2ktIncompatible
/*     */ public final class EnumMultiset<E extends Enum<E>>
/*     */   extends AbstractMultiset<E>
/*     */   implements Serializable
/*     */ {
/*     */   private transient Class<E> type;
/*     */   private transient E[] enumConstants;
/*     */   private transient int[] counts;
/*     */   private transient int distinctElements;
/*     */   private transient long size;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <E extends Enum<E>> EnumMultiset<E> create(Class<E> type) {
/*  55 */     return new EnumMultiset<>(type);
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
/*     */   public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> elements) {
/*  67 */     Iterator<E> iterator = elements.iterator();
/*  68 */     Preconditions.checkArgument(iterator.hasNext(), "EnumMultiset constructor passed empty Iterable");
/*  69 */     EnumMultiset<E> multiset = new EnumMultiset<>(((Enum<E>)iterator.next()).getDeclaringClass());
/*  70 */     Iterables.addAll(multiset, elements);
/*  71 */     return multiset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> elements, Class<E> type) {
/*  81 */     EnumMultiset<E> result = create(type);
/*  82 */     Iterables.addAll(result, elements);
/*  83 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EnumMultiset(Class<E> type) {
/*  94 */     this.type = type;
/*  95 */     Preconditions.checkArgument(type.isEnum());
/*  96 */     this.enumConstants = type.getEnumConstants();
/*  97 */     this.counts = new int[this.enumConstants.length];
/*     */   }
/*     */   
/*     */   private boolean isActuallyE(@CheckForNull Object o) {
/* 101 */     if (o instanceof Enum) {
/* 102 */       Enum<?> e = (Enum)o;
/* 103 */       int index = e.ordinal();
/* 104 */       return (index < this.enumConstants.length && this.enumConstants[index] == e);
/*     */     } 
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkIsE(Object element) {
/* 114 */     Preconditions.checkNotNull(element);
/* 115 */     if (!isActuallyE(element)) {
/* 116 */       throw new ClassCastException("Expected an " + this.type + " but got " + element);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   int distinctElements() {
/* 122 */     return this.distinctElements;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 127 */     return Ints.saturatedCast(this.size);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int count(@CheckForNull Object element) {
/* 133 */     if (element == null || !isActuallyE(element)) {
/* 134 */       return 0;
/*     */     }
/* 136 */     Enum<?> e = (Enum)element;
/* 137 */     return this.counts[e.ordinal()];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int add(E element, int occurrences) {
/* 144 */     checkIsE(element);
/* 145 */     CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 146 */     if (occurrences == 0) {
/* 147 */       return count(element);
/*     */     }
/* 149 */     int index = element.ordinal();
/* 150 */     int oldCount = this.counts[index];
/* 151 */     long newCount = oldCount + occurrences;
/* 152 */     Preconditions.checkArgument((newCount <= 2147483647L), "too many occurrences: %s", newCount);
/* 153 */     this.counts[index] = (int)newCount;
/* 154 */     if (oldCount == 0) {
/* 155 */       this.distinctElements++;
/*     */     }
/* 157 */     this.size += occurrences;
/* 158 */     return oldCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int remove(@CheckForNull Object element, int occurrences) {
/* 166 */     if (element == null || !isActuallyE(element)) {
/* 167 */       return 0;
/*     */     }
/* 169 */     Enum<?> e = (Enum)element;
/* 170 */     CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/* 171 */     if (occurrences == 0) {
/* 172 */       return count(element);
/*     */     }
/* 174 */     int index = e.ordinal();
/* 175 */     int oldCount = this.counts[index];
/* 176 */     if (oldCount == 0)
/* 177 */       return 0; 
/* 178 */     if (oldCount <= occurrences) {
/* 179 */       this.counts[index] = 0;
/* 180 */       this.distinctElements--;
/* 181 */       this.size -= oldCount;
/*     */     } else {
/* 183 */       this.counts[index] = oldCount - occurrences;
/* 184 */       this.size -= occurrences;
/*     */     } 
/* 186 */     return oldCount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int setCount(E element, int count) {
/* 193 */     checkIsE(element);
/* 194 */     CollectPreconditions.checkNonnegative(count, "count");
/* 195 */     int index = element.ordinal();
/* 196 */     int oldCount = this.counts[index];
/* 197 */     this.counts[index] = count;
/* 198 */     this.size += (count - oldCount);
/* 199 */     if (oldCount == 0 && count > 0) {
/* 200 */       this.distinctElements++;
/* 201 */     } else if (oldCount > 0 && count == 0) {
/* 202 */       this.distinctElements--;
/*     */     } 
/* 204 */     return oldCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 209 */     Arrays.fill(this.counts, 0);
/* 210 */     this.size = 0L;
/* 211 */     this.distinctElements = 0;
/*     */   }
/*     */   
/*     */   abstract class Itr<T> implements Iterator<T> {
/* 215 */     int index = 0;
/* 216 */     int toRemove = -1;
/*     */ 
/*     */     
/*     */     abstract T output(int param1Int);
/*     */     
/*     */     public boolean hasNext() {
/* 222 */       for (; this.index < EnumMultiset.this.enumConstants.length; this.index++) {
/* 223 */         if (EnumMultiset.this.counts[this.index] > 0) {
/* 224 */           return true;
/*     */         }
/*     */       } 
/* 227 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 232 */       if (!hasNext()) {
/* 233 */         throw new NoSuchElementException();
/*     */       }
/* 235 */       T result = output(this.index);
/* 236 */       this.toRemove = this.index;
/* 237 */       this.index++;
/* 238 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 243 */       CollectPreconditions.checkRemove((this.toRemove >= 0));
/* 244 */       if (EnumMultiset.this.counts[this.toRemove] > 0) {
/* 245 */         EnumMultiset.this.distinctElements--;
/* 246 */         EnumMultiset.this.size -= EnumMultiset.this.counts[this.toRemove];
/* 247 */         EnumMultiset.this.counts[this.toRemove] = 0;
/*     */       } 
/* 249 */       this.toRemove = -1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<E> elementIterator() {
/* 255 */     return new Itr<E>()
/*     */       {
/*     */         E output(int index) {
/* 258 */           return (E)EnumMultiset.this.enumConstants[index];
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Multiset.Entry<E>> entryIterator() {
/* 265 */     return new Itr<Multiset.Entry<E>>()
/*     */       {
/*     */         Multiset.Entry<E> output(final int index) {
/* 268 */           return new Multisets.AbstractEntry<E>()
/*     */             {
/*     */               public E getElement() {
/* 271 */                 return (E)EnumMultiset.this.enumConstants[index];
/*     */               }
/*     */ 
/*     */               
/*     */               public int getCount() {
/* 276 */                 return EnumMultiset.this.counts[index];
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEachEntry(ObjIntConsumer<? super E> action) {
/* 285 */     Preconditions.checkNotNull(action);
/* 286 */     for (int i = 0; i < this.enumConstants.length; i++) {
/* 287 */       if (this.counts[i] > 0) {
/* 288 */         action.accept(this.enumConstants[i], this.counts[i]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 295 */     return Multisets.iteratorImpl(this);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 300 */     stream.defaultWriteObject();
/* 301 */     stream.writeObject(this.type);
/* 302 */     Serialization.writeMultiset(this, stream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 311 */     stream.defaultReadObject();
/*     */     
/* 313 */     Class<E> localType = (Class<E>)Objects.<Object>requireNonNull(stream.readObject());
/* 314 */     this.type = localType;
/* 315 */     this.enumConstants = this.type.getEnumConstants();
/* 316 */     this.counts = new int[this.enumConstants.length];
/* 317 */     Serialization.populateMultiset(this, stream);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/EnumMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */