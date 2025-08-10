/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class AbstractMapBasedMultiset<E>
/*     */   extends AbstractMultiset<E>
/*     */   implements Serializable
/*     */ {
/*     */   private transient Map<E, Count> backingMap;
/*     */   private transient long size;
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private static final long serialVersionUID = -2250766705698539974L;
/*     */   
/*     */   protected AbstractMapBasedMultiset(Map<E, Count> backingMap) {
/*  66 */     Preconditions.checkArgument(backingMap.isEmpty());
/*  67 */     this.backingMap = backingMap;
/*     */   }
/*     */ 
/*     */   
/*     */   void setBackingMap(Map<E, Count> backingMap) {
/*  72 */     this.backingMap = backingMap;
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
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/*  86 */     return super.entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<E> elementIterator() {
/*  91 */     final Iterator<Map.Entry<E, Count>> backingEntries = this.backingMap.entrySet().iterator();
/*  92 */     return new Iterator<E>() {
/*     */         @CheckForNull
/*     */         Map.Entry<E, Count> toRemove;
/*     */         
/*     */         public boolean hasNext() {
/*  97 */           return backingEntries.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         @ParametricNullness
/*     */         public E next() {
/* 103 */           Map.Entry<E, Count> mapEntry = backingEntries.next();
/* 104 */           this.toRemove = mapEntry;
/* 105 */           return mapEntry.getKey();
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 110 */           Preconditions.checkState((this.toRemove != null), "no calls to next() since the last call to remove()");
/* 111 */           AbstractMapBasedMultiset.this.size -= ((Count)this.toRemove.getValue()).getAndSet(0);
/* 112 */           backingEntries.remove();
/* 113 */           this.toRemove = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Multiset.Entry<E>> entryIterator() {
/* 120 */     final Iterator<Map.Entry<E, Count>> backingEntries = this.backingMap.entrySet().iterator();
/* 121 */     return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>() {
/*     */         @CheckForNull
/*     */         Map.Entry<E, Count> toRemove;
/*     */         
/*     */         public boolean hasNext() {
/* 126 */           return backingEntries.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public Multiset.Entry<E> next() {
/* 131 */           final Map.Entry<E, Count> mapEntry = backingEntries.next();
/* 132 */           this.toRemove = mapEntry;
/* 133 */           return new Multisets.AbstractEntry<E>()
/*     */             {
/*     */               @ParametricNullness
/*     */               public E getElement() {
/* 137 */                 return (E)mapEntry.getKey();
/*     */               }
/*     */ 
/*     */               
/*     */               public int getCount() {
/* 142 */                 Count count = (Count)mapEntry.getValue();
/* 143 */                 if (count == null || count.get() == 0) {
/* 144 */                   Count frequency = (Count)AbstractMapBasedMultiset.this.backingMap.get(getElement());
/* 145 */                   if (frequency != null) {
/* 146 */                     return frequency.get();
/*     */                   }
/*     */                 } 
/* 149 */                 return (count == null) ? 0 : count.get();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 156 */           Preconditions.checkState((this.toRemove != null), "no calls to next() since the last call to remove()");
/* 157 */           AbstractMapBasedMultiset.this.size -= ((Count)this.toRemove.getValue()).getAndSet(0);
/* 158 */           backingEntries.remove();
/* 159 */           this.toRemove = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEachEntry(ObjIntConsumer<? super E> action) {
/* 166 */     Preconditions.checkNotNull(action);
/* 167 */     this.backingMap.forEach((element, count) -> action.accept(element, count.get()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 172 */     for (Count frequency : this.backingMap.values()) {
/* 173 */       frequency.set(0);
/*     */     }
/* 175 */     this.backingMap.clear();
/* 176 */     this.size = 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   int distinctElements() {
/* 181 */     return this.backingMap.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 188 */     return Ints.saturatedCast(this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 193 */     return new MapBasedMultisetIterator();
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
/*     */   private class MapBasedMultisetIterator
/*     */     implements Iterator<E>
/*     */   {
/* 208 */     final Iterator<Map.Entry<E, Count>> entryIterator = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();
/*     */     @CheckForNull
/*     */     Map.Entry<E, Count> currentEntry;
/*     */     
/*     */     public boolean hasNext() {
/* 213 */       return (this.occurrencesLeft > 0 || this.entryIterator.hasNext());
/*     */     }
/*     */     int occurrencesLeft; boolean canRemove;
/*     */     
/*     */     @ParametricNullness
/*     */     public E next() {
/* 219 */       if (this.occurrencesLeft == 0) {
/* 220 */         this.currentEntry = this.entryIterator.next();
/* 221 */         this.occurrencesLeft = ((Count)this.currentEntry.getValue()).get();
/*     */       } 
/* 223 */       this.occurrencesLeft--;
/* 224 */       this.canRemove = true;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 229 */       return (E)((Map.Entry)Objects.<Map.Entry>requireNonNull(this.currentEntry)).getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 234 */       CollectPreconditions.checkRemove(this.canRemove);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 239 */       int frequency = ((Count)((Map.Entry)Objects.<Map.Entry>requireNonNull(this.currentEntry)).getValue()).get();
/* 240 */       if (frequency <= 0) {
/* 241 */         throw new ConcurrentModificationException();
/*     */       }
/* 243 */       if (((Count)this.currentEntry.getValue()).addAndGet(-1) == 0) {
/* 244 */         this.entryIterator.remove();
/*     */       }
/* 246 */       AbstractMapBasedMultiset.this.size--;
/* 247 */       this.canRemove = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int count(@CheckForNull Object element) {
/* 253 */     Count frequency = Maps.<Count>safeGet(this.backingMap, element);
/* 254 */     return (frequency == null) ? 0 : frequency.get();
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
/*     */   @CanIgnoreReturnValue
/*     */   public int add(@ParametricNullness E element, int occurrences) {
/*     */     int oldCount;
/* 268 */     if (occurrences == 0) {
/* 269 */       return count(element);
/*     */     }
/* 271 */     Preconditions.checkArgument((occurrences > 0), "occurrences cannot be negative: %s", occurrences);
/* 272 */     Count frequency = this.backingMap.get(element);
/*     */     
/* 274 */     if (frequency == null) {
/* 275 */       oldCount = 0;
/* 276 */       this.backingMap.put(element, new Count(occurrences));
/*     */     } else {
/* 278 */       oldCount = frequency.get();
/* 279 */       long newCount = oldCount + occurrences;
/* 280 */       Preconditions.checkArgument((newCount <= 2147483647L), "too many occurrences: %s", newCount);
/* 281 */       frequency.add(occurrences);
/*     */     } 
/* 283 */     this.size += occurrences;
/* 284 */     return oldCount;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int remove(@CheckForNull Object element, int occurrences) {
/*     */     int numberRemoved;
/* 290 */     if (occurrences == 0) {
/* 291 */       return count(element);
/*     */     }
/* 293 */     Preconditions.checkArgument((occurrences > 0), "occurrences cannot be negative: %s", occurrences);
/* 294 */     Count frequency = this.backingMap.get(element);
/* 295 */     if (frequency == null) {
/* 296 */       return 0;
/*     */     }
/*     */     
/* 299 */     int oldCount = frequency.get();
/*     */ 
/*     */     
/* 302 */     if (oldCount > occurrences) {
/* 303 */       numberRemoved = occurrences;
/*     */     } else {
/* 305 */       numberRemoved = oldCount;
/* 306 */       this.backingMap.remove(element);
/*     */     } 
/*     */     
/* 309 */     frequency.add(-numberRemoved);
/* 310 */     this.size -= numberRemoved;
/* 311 */     return oldCount;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int setCount(@ParametricNullness E element, int count) {
/*     */     int oldCount;
/* 318 */     CollectPreconditions.checkNonnegative(count, "count");
/*     */ 
/*     */ 
/*     */     
/* 322 */     if (count == 0) {
/* 323 */       Count existingCounter = this.backingMap.remove(element);
/* 324 */       oldCount = getAndSet(existingCounter, count);
/*     */     } else {
/* 326 */       Count existingCounter = this.backingMap.get(element);
/* 327 */       oldCount = getAndSet(existingCounter, count);
/*     */       
/* 329 */       if (existingCounter == null) {
/* 330 */         this.backingMap.put(element, new Count(count));
/*     */       }
/*     */     } 
/*     */     
/* 334 */     this.size += (count - oldCount);
/* 335 */     return oldCount;
/*     */   }
/*     */   
/*     */   private static int getAndSet(@CheckForNull Count i, int count) {
/* 339 */     if (i == null) {
/* 340 */       return 0;
/*     */     }
/*     */     
/* 343 */     return i.getAndSet(count);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void readObjectNoData() throws ObjectStreamException {
/* 350 */     throw new InvalidObjectException("Stream data required");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/AbstractMapBasedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */