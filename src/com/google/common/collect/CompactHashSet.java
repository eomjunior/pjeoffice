/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ class CompactHashSet<E>
/*     */   extends AbstractSet<E>
/*     */   implements Serializable
/*     */ {
/*     */   @VisibleForTesting
/*     */   static final double HASH_FLOODING_FPP = 0.001D;
/*     */   private static final int MAX_HASH_BUCKET_LENGTH = 9;
/*     */   @CheckForNull
/*     */   private transient Object table;
/*     */   @CheckForNull
/*     */   private transient int[] entries;
/*     */   @CheckForNull
/*     */   @VisibleForTesting
/*     */   transient Object[] elements;
/*     */   private transient int metadata;
/*     */   private transient int size;
/*     */   
/*     */   public static <E> CompactHashSet<E> create() {
/*  86 */     return new CompactHashSet<>();
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
/*     */   public static <E> CompactHashSet<E> create(Collection<? extends E> collection) {
/*  98 */     CompactHashSet<E> set = createWithExpectedSize(collection.size());
/*  99 */     set.addAll(collection);
/* 100 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public static <E> CompactHashSet<E> create(E... elements) {
/* 112 */     CompactHashSet<E> set = createWithExpectedSize(elements.length);
/* 113 */     Collections.addAll(set, elements);
/* 114 */     return set;
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
/*     */   public static <E> CompactHashSet<E> createWithExpectedSize(int expectedSize) {
/* 128 */     return new CompactHashSet<>(expectedSize);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CompactHashSet() {
/* 206 */     init(3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CompactHashSet(int expectedSize) {
/* 215 */     init(expectedSize);
/*     */   }
/*     */ 
/*     */   
/*     */   void init(int expectedSize) {
/* 220 */     Preconditions.checkArgument((expectedSize >= 0), "Expected size must be >= 0");
/*     */ 
/*     */     
/* 223 */     this.metadata = Ints.constrainToRange(expectedSize, 1, 1073741823);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   boolean needsAllocArrays() {
/* 229 */     return (this.table == null);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   int allocArrays() {
/* 235 */     Preconditions.checkState(needsAllocArrays(), "Arrays already allocated");
/*     */     
/* 237 */     int expectedSize = this.metadata;
/* 238 */     int buckets = CompactHashing.tableSize(expectedSize);
/* 239 */     this.table = CompactHashing.createTable(buckets);
/* 240 */     setHashTableMask(buckets - 1);
/*     */     
/* 242 */     this.entries = new int[expectedSize];
/* 243 */     this.elements = new Object[expectedSize];
/*     */     
/* 245 */     return expectedSize;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @VisibleForTesting
/*     */   Set<E> delegateOrNull() {
/* 252 */     if (this.table instanceof Set) {
/* 253 */       return (Set<E>)this.table;
/*     */     }
/* 255 */     return null;
/*     */   }
/*     */   
/*     */   private Set<E> createHashFloodingResistantDelegate(int tableSize) {
/* 259 */     return new LinkedHashSet<>(tableSize, 1.0F);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   @CanIgnoreReturnValue
/*     */   Set<E> convertToHashFloodingResistantImplementation() {
/* 265 */     Set<E> newDelegate = createHashFloodingResistantDelegate(hashTableMask() + 1);
/* 266 */     for (int i = firstEntryIndex(); i >= 0; i = getSuccessor(i)) {
/* 267 */       newDelegate.add(element(i));
/*     */     }
/* 269 */     this.table = newDelegate;
/* 270 */     this.entries = null;
/* 271 */     this.elements = null;
/* 272 */     incrementModCount();
/* 273 */     return newDelegate;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   boolean isUsingHashFloodingResistance() {
/* 278 */     return (delegateOrNull() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setHashTableMask(int mask) {
/* 283 */     int hashTableBits = 32 - Integer.numberOfLeadingZeros(mask);
/* 284 */     this
/* 285 */       .metadata = CompactHashing.maskCombine(this.metadata, hashTableBits, 31);
/*     */   }
/*     */ 
/*     */   
/*     */   private int hashTableMask() {
/* 290 */     return (1 << (this.metadata & 0x1F)) - 1;
/*     */   }
/*     */   
/*     */   void incrementModCount() {
/* 294 */     this.metadata += 32;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean add(@ParametricNullness E object) {
/* 300 */     if (needsAllocArrays()) {
/* 301 */       allocArrays();
/*     */     }
/* 303 */     Set<E> delegate = delegateOrNull();
/* 304 */     if (delegate != null) {
/* 305 */       return delegate.add(object);
/*     */     }
/* 307 */     int[] entries = requireEntries();
/* 308 */     Object[] elements = requireElements();
/*     */     
/* 310 */     int newEntryIndex = this.size;
/* 311 */     int newSize = newEntryIndex + 1;
/* 312 */     int hash = Hashing.smearedHash(object);
/* 313 */     int mask = hashTableMask();
/* 314 */     int tableIndex = hash & mask;
/* 315 */     int next = CompactHashing.tableGet(requireTable(), tableIndex);
/* 316 */     if (next == 0) {
/* 317 */       if (newSize > mask) {
/*     */         
/* 319 */         mask = resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
/*     */       } else {
/* 321 */         CompactHashing.tableSet(requireTable(), tableIndex, newEntryIndex + 1);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 326 */       int entryIndex, entry, hashPrefix = CompactHashing.getHashPrefix(hash, mask);
/* 327 */       int bucketLength = 0;
/*     */       do {
/* 329 */         entryIndex = next - 1;
/* 330 */         entry = entries[entryIndex];
/* 331 */         if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && 
/* 332 */           Objects.equal(object, elements[entryIndex])) {
/* 333 */           return false;
/*     */         }
/* 335 */         next = CompactHashing.getNext(entry, mask);
/* 336 */         bucketLength++;
/* 337 */       } while (next != 0);
/*     */       
/* 339 */       if (bucketLength >= 9) {
/* 340 */         return convertToHashFloodingResistantImplementation().add(object);
/*     */       }
/*     */       
/* 343 */       if (newSize > mask) {
/*     */         
/* 345 */         mask = resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
/*     */       } else {
/* 347 */         entries[entryIndex] = CompactHashing.maskCombine(entry, newEntryIndex + 1, mask);
/*     */       } 
/*     */     } 
/* 350 */     resizeMeMaybe(newSize);
/* 351 */     insertEntry(newEntryIndex, object, hash, mask);
/* 352 */     this.size = newSize;
/* 353 */     incrementModCount();
/* 354 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void insertEntry(int entryIndex, @ParametricNullness E object, int hash, int mask) {
/* 361 */     setEntry(entryIndex, CompactHashing.maskCombine(hash, 0, mask));
/* 362 */     setElement(entryIndex, object);
/*     */   }
/*     */ 
/*     */   
/*     */   private void resizeMeMaybe(int newSize) {
/* 367 */     int entriesSize = (requireEntries()).length;
/* 368 */     if (newSize > entriesSize) {
/*     */ 
/*     */       
/* 371 */       int newCapacity = Math.min(1073741823, entriesSize + Math.max(1, entriesSize >>> 1) | 0x1);
/* 372 */       if (newCapacity != entriesSize) {
/* 373 */         resizeEntries(newCapacity);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void resizeEntries(int newCapacity) {
/* 383 */     this.entries = Arrays.copyOf(requireEntries(), newCapacity);
/* 384 */     this.elements = Arrays.copyOf(requireElements(), newCapacity);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private int resizeTable(int oldMask, int newCapacity, int targetHash, int targetEntryIndex) {
/* 389 */     Object newTable = CompactHashing.createTable(newCapacity);
/* 390 */     int newMask = newCapacity - 1;
/*     */     
/* 392 */     if (targetEntryIndex != 0)
/*     */     {
/* 394 */       CompactHashing.tableSet(newTable, targetHash & newMask, targetEntryIndex + 1);
/*     */     }
/*     */     
/* 397 */     Object oldTable = requireTable();
/* 398 */     int[] entries = requireEntries();
/*     */ 
/*     */     
/* 401 */     for (int oldTableIndex = 0; oldTableIndex <= oldMask; oldTableIndex++) {
/* 402 */       int oldNext = CompactHashing.tableGet(oldTable, oldTableIndex);
/* 403 */       while (oldNext != 0) {
/* 404 */         int entryIndex = oldNext - 1;
/* 405 */         int oldEntry = entries[entryIndex];
/*     */ 
/*     */         
/* 408 */         int hash = CompactHashing.getHashPrefix(oldEntry, oldMask) | oldTableIndex;
/*     */         
/* 410 */         int newTableIndex = hash & newMask;
/* 411 */         int newNext = CompactHashing.tableGet(newTable, newTableIndex);
/* 412 */         CompactHashing.tableSet(newTable, newTableIndex, oldNext);
/* 413 */         entries[entryIndex] = CompactHashing.maskCombine(hash, newNext, newMask);
/*     */         
/* 415 */         oldNext = CompactHashing.getNext(oldEntry, oldMask);
/*     */       } 
/*     */     } 
/*     */     
/* 419 */     this.table = newTable;
/* 420 */     setHashTableMask(newMask);
/* 421 */     return newMask;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object object) {
/* 426 */     if (needsAllocArrays()) {
/* 427 */       return false;
/*     */     }
/* 429 */     Set<E> delegate = delegateOrNull();
/* 430 */     if (delegate != null) {
/* 431 */       return delegate.contains(object);
/*     */     }
/* 433 */     int hash = Hashing.smearedHash(object);
/* 434 */     int mask = hashTableMask();
/* 435 */     int next = CompactHashing.tableGet(requireTable(), hash & mask);
/* 436 */     if (next == 0) {
/* 437 */       return false;
/*     */     }
/* 439 */     int hashPrefix = CompactHashing.getHashPrefix(hash, mask);
/*     */     while (true) {
/* 441 */       int entryIndex = next - 1;
/* 442 */       int entry = entry(entryIndex);
/* 443 */       if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && 
/* 444 */         Objects.equal(object, element(entryIndex))) {
/* 445 */         return true;
/*     */       }
/* 447 */       next = CompactHashing.getNext(entry, mask);
/* 448 */       if (next == 0)
/* 449 */         return false; 
/*     */     } 
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(@CheckForNull Object object) {
/* 455 */     if (needsAllocArrays()) {
/* 456 */       return false;
/*     */     }
/* 458 */     Set<E> delegate = delegateOrNull();
/* 459 */     if (delegate != null) {
/* 460 */       return delegate.remove(object);
/*     */     }
/* 462 */     int mask = hashTableMask();
/*     */     
/* 464 */     int index = CompactHashing.remove(object, null, mask, 
/*     */ 
/*     */ 
/*     */         
/* 468 */         requireTable(), 
/* 469 */         requireEntries(), 
/* 470 */         requireElements(), null);
/*     */     
/* 472 */     if (index == -1) {
/* 473 */       return false;
/*     */     }
/*     */     
/* 476 */     moveLastEntry(index, mask);
/* 477 */     this.size--;
/* 478 */     incrementModCount();
/*     */     
/* 480 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void moveLastEntry(int dstIndex, int mask) {
/* 487 */     Object table = requireTable();
/* 488 */     int[] entries = requireEntries();
/* 489 */     Object[] elements = requireElements();
/* 490 */     int srcIndex = size() - 1;
/* 491 */     if (dstIndex < srcIndex) {
/*     */       
/* 493 */       Object object = elements[srcIndex];
/* 494 */       elements[dstIndex] = object;
/* 495 */       elements[srcIndex] = null;
/*     */ 
/*     */       
/* 498 */       entries[dstIndex] = entries[srcIndex];
/* 499 */       entries[srcIndex] = 0;
/*     */ 
/*     */       
/* 502 */       int tableIndex = Hashing.smearedHash(object) & mask;
/* 503 */       int next = CompactHashing.tableGet(table, tableIndex);
/* 504 */       int srcNext = srcIndex + 1;
/* 505 */       if (next == srcNext) {
/*     */         
/* 507 */         CompactHashing.tableSet(table, tableIndex, dstIndex + 1);
/*     */       } else {
/*     */         int entryIndex, entry;
/*     */ 
/*     */         
/*     */         do {
/* 513 */           entryIndex = next - 1;
/* 514 */           entry = entries[entryIndex];
/* 515 */           next = CompactHashing.getNext(entry, mask);
/* 516 */         } while (next != srcNext);
/*     */         
/* 518 */         entries[entryIndex] = CompactHashing.maskCombine(entry, dstIndex + 1, mask);
/*     */       } 
/*     */     } else {
/* 521 */       elements[dstIndex] = null;
/* 522 */       entries[dstIndex] = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   int firstEntryIndex() {
/* 527 */     return isEmpty() ? -1 : 0;
/*     */   }
/*     */   
/*     */   int getSuccessor(int entryIndex) {
/* 531 */     return (entryIndex + 1 < this.size) ? (entryIndex + 1) : -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
/* 540 */     return indexBeforeRemove - 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 545 */     Set<E> delegate = delegateOrNull();
/* 546 */     if (delegate != null) {
/* 547 */       return delegate.iterator();
/*     */     }
/* 549 */     return new Iterator<E>() {
/* 550 */         int expectedMetadata = CompactHashSet.this.metadata;
/* 551 */         int currentIndex = CompactHashSet.this.firstEntryIndex();
/* 552 */         int indexToRemove = -1;
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 556 */           return (this.currentIndex >= 0);
/*     */         }
/*     */ 
/*     */         
/*     */         @ParametricNullness
/*     */         public E next() {
/* 562 */           checkForConcurrentModification();
/* 563 */           if (!hasNext()) {
/* 564 */             throw new NoSuchElementException();
/*     */           }
/* 566 */           this.indexToRemove = this.currentIndex;
/* 567 */           E result = CompactHashSet.this.element(this.currentIndex);
/* 568 */           this.currentIndex = CompactHashSet.this.getSuccessor(this.currentIndex);
/* 569 */           return result;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 574 */           checkForConcurrentModification();
/* 575 */           CollectPreconditions.checkRemove((this.indexToRemove >= 0));
/* 576 */           incrementExpectedModCount();
/* 577 */           CompactHashSet.this.remove(CompactHashSet.this.element(this.indexToRemove));
/* 578 */           this.currentIndex = CompactHashSet.this.adjustAfterRemove(this.currentIndex, this.indexToRemove);
/* 579 */           this.indexToRemove = -1;
/*     */         }
/*     */         
/*     */         void incrementExpectedModCount() {
/* 583 */           this.expectedMetadata += 32;
/*     */         }
/*     */         
/*     */         private void checkForConcurrentModification() {
/* 587 */           if (CompactHashSet.this.metadata != this.expectedMetadata) {
/* 588 */             throw new ConcurrentModificationException();
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 596 */     if (needsAllocArrays()) {
/* 597 */       return Spliterators.spliterator(new Object[0], 17);
/*     */     }
/* 599 */     Set<E> delegate = delegateOrNull();
/* 600 */     return (delegate != null) ? 
/* 601 */       delegate.spliterator() : 
/* 602 */       Spliterators.<E>spliterator(
/* 603 */         requireElements(), 0, this.size, 17);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super E> action) {
/* 608 */     Preconditions.checkNotNull(action);
/* 609 */     Set<E> delegate = delegateOrNull();
/* 610 */     if (delegate != null) {
/* 611 */       delegate.forEach(action);
/*     */     } else {
/* 613 */       for (int i = firstEntryIndex(); i >= 0; i = getSuccessor(i)) {
/* 614 */         action.accept(element(i));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 621 */     Set<E> delegate = delegateOrNull();
/* 622 */     return (delegate != null) ? delegate.size() : this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 627 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 632 */     if (needsAllocArrays()) {
/* 633 */       return new Object[0];
/*     */     }
/* 635 */     Set<E> delegate = delegateOrNull();
/* 636 */     return (delegate != null) ? delegate.toArray() : Arrays.<Object>copyOf(requireElements(), this.size);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T[] toArray(T[] a) {
/* 643 */     if (needsAllocArrays()) {
/* 644 */       if (a.length > 0) {
/* 645 */         a[0] = null;
/*     */       }
/* 647 */       return a;
/*     */     } 
/* 649 */     Set<E> delegate = delegateOrNull();
/* 650 */     return (delegate != null) ? 
/* 651 */       delegate.<T>toArray(a) : 
/* 652 */       ObjectArrays.<T>toArrayImpl(requireElements(), 0, this.size, a);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trimToSize() {
/* 660 */     if (needsAllocArrays()) {
/*     */       return;
/*     */     }
/* 663 */     Set<E> delegate = delegateOrNull();
/* 664 */     if (delegate != null) {
/* 665 */       Set<E> newDelegate = createHashFloodingResistantDelegate(size());
/* 666 */       newDelegate.addAll(delegate);
/* 667 */       this.table = newDelegate;
/*     */       return;
/*     */     } 
/* 670 */     int size = this.size;
/* 671 */     if (size < (requireEntries()).length) {
/* 672 */       resizeEntries(size);
/*     */     }
/* 674 */     int minimumTableSize = CompactHashing.tableSize(size);
/* 675 */     int mask = hashTableMask();
/* 676 */     if (minimumTableSize < mask) {
/* 677 */       resizeTable(mask, minimumTableSize, 0, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 683 */     if (needsAllocArrays()) {
/*     */       return;
/*     */     }
/* 686 */     incrementModCount();
/* 687 */     Set<E> delegate = delegateOrNull();
/* 688 */     if (delegate != null) {
/* 689 */       this
/* 690 */         .metadata = Ints.constrainToRange(size(), 3, 1073741823);
/* 691 */       delegate.clear();
/* 692 */       this.table = null;
/* 693 */       this.size = 0;
/*     */     } else {
/* 695 */       Arrays.fill(requireElements(), 0, this.size, (Object)null);
/* 696 */       CompactHashing.tableClear(requireTable());
/* 697 */       Arrays.fill(requireEntries(), 0, this.size, 0);
/* 698 */       this.size = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   @J2ktIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 704 */     stream.defaultWriteObject();
/* 705 */     stream.writeInt(size());
/* 706 */     for (E e : this) {
/* 707 */       stream.writeObject(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 714 */     stream.defaultReadObject();
/* 715 */     int elementCount = stream.readInt();
/* 716 */     if (elementCount < 0) {
/* 717 */       throw new InvalidObjectException("Invalid size: " + elementCount);
/*     */     }
/* 719 */     init(elementCount);
/* 720 */     for (int i = 0; i < elementCount; i++) {
/* 721 */       E element = (E)stream.readObject();
/* 722 */       add(element);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object requireTable() {
/* 732 */     return Objects.requireNonNull(this.table);
/*     */   }
/*     */   
/*     */   private int[] requireEntries() {
/* 736 */     return Objects.<int[]>requireNonNull(this.entries);
/*     */   }
/*     */   
/*     */   private Object[] requireElements() {
/* 740 */     return Objects.<Object[]>requireNonNull(this.elements);
/*     */   }
/*     */ 
/*     */   
/*     */   private E element(int i) {
/* 745 */     return (E)requireElements()[i];
/*     */   }
/*     */   
/*     */   private int entry(int i) {
/* 749 */     return requireEntries()[i];
/*     */   }
/*     */   
/*     */   private void setElement(int i, E value) {
/* 753 */     requireElements()[i] = value;
/*     */   }
/*     */   
/*     */   private void setEntry(int i, int value) {
/* 757 */     requireEntries()[i] = value;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/CompactHashSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */