/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSequentialList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public class LinkedListMultimap<K, V>
/*     */   extends AbstractMultimap<K, V>
/*     */   implements ListMultimap<K, V>, Serializable
/*     */ {
/*     */   @CheckForNull
/*     */   private transient Node<K, V> head;
/*     */   @CheckForNull
/*     */   private transient Node<K, V> tail;
/*     */   private transient Map<K, KeyList<K, V>> keyToKeyList;
/*     */   private transient int size;
/*     */   private transient int modCount;
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   static final class Node<K, V>
/*     */     extends AbstractMapEntry<K, V>
/*     */   {
/*     */     @ParametricNullness
/*     */     final K key;
/*     */     @ParametricNullness
/*     */     V value;
/*     */     @CheckForNull
/*     */     Node<K, V> next;
/*     */     @CheckForNull
/*     */     Node<K, V> previous;
/*     */     @CheckForNull
/*     */     Node<K, V> nextSibling;
/*     */     @CheckForNull
/*     */     Node<K, V> previousSibling;
/*     */     
/*     */     Node(@ParametricNullness K key, @ParametricNullness V value) {
/* 120 */       this.key = key;
/* 121 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public K getKey() {
/* 127 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public V getValue() {
/* 133 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public V setValue(@ParametricNullness V newValue) {
/* 139 */       V result = this.value;
/* 140 */       this.value = newValue;
/* 141 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class KeyList<K, V> {
/*     */     LinkedListMultimap.Node<K, V> head;
/*     */     LinkedListMultimap.Node<K, V> tail;
/*     */     int count;
/*     */     
/*     */     KeyList(LinkedListMultimap.Node<K, V> firstNode) {
/* 151 */       this.head = firstNode;
/* 152 */       this.tail = firstNode;
/* 153 */       firstNode.previousSibling = null;
/* 154 */       firstNode.nextSibling = null;
/* 155 */       this.count = 1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> LinkedListMultimap<K, V> create() {
/* 174 */     return new LinkedListMultimap<>();
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
/*     */   public static <K, V> LinkedListMultimap<K, V> create(int expectedKeys) {
/* 186 */     return new LinkedListMultimap<>(expectedKeys);
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
/*     */   public static <K, V> LinkedListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 198 */     return new LinkedListMultimap<>(multimap);
/*     */   }
/*     */   
/*     */   LinkedListMultimap() {
/* 202 */     this(12);
/*     */   }
/*     */   
/*     */   private LinkedListMultimap(int expectedKeys) {
/* 206 */     this.keyToKeyList = Platform.newHashMapWithExpectedSize(expectedKeys);
/*     */   }
/*     */   
/*     */   private LinkedListMultimap(Multimap<? extends K, ? extends V> multimap) {
/* 210 */     this(multimap.keySet().size());
/* 211 */     putAll(multimap);
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
/*     */   private Node<K, V> addNode(@ParametricNullness K key, @ParametricNullness V value, @CheckForNull Node<K, V> nextSibling) {
/* 224 */     Node<K, V> node = new Node<>(key, value);
/* 225 */     if (this.head == null) {
/* 226 */       this.head = this.tail = node;
/* 227 */       this.keyToKeyList.put(key, new KeyList<>(node));
/* 228 */       this.modCount++;
/* 229 */     } else if (nextSibling == null) {
/*     */       
/* 231 */       ((Node)Objects.requireNonNull((T)this.tail)).next = node;
/* 232 */       node.previous = this.tail;
/* 233 */       this.tail = node;
/* 234 */       KeyList<K, V> keyList = this.keyToKeyList.get(key);
/* 235 */       if (keyList == null) {
/* 236 */         this.keyToKeyList.put(key, keyList = new KeyList<>(node));
/* 237 */         this.modCount++;
/*     */       } else {
/* 239 */         keyList.count++;
/* 240 */         Node<K, V> keyTail = keyList.tail;
/* 241 */         keyTail.nextSibling = node;
/* 242 */         node.previousSibling = keyTail;
/* 243 */         keyList.tail = node;
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 251 */       KeyList<K, V> keyList = Objects.<KeyList<K, V>>requireNonNull(this.keyToKeyList.get(key));
/* 252 */       keyList.count++;
/* 253 */       node.previous = nextSibling.previous;
/* 254 */       node.previousSibling = nextSibling.previousSibling;
/* 255 */       node.next = nextSibling;
/* 256 */       node.nextSibling = nextSibling;
/* 257 */       if (nextSibling.previousSibling == null) {
/* 258 */         keyList.head = node;
/*     */       } else {
/* 260 */         nextSibling.previousSibling.nextSibling = node;
/*     */       } 
/* 262 */       if (nextSibling.previous == null) {
/* 263 */         this.head = node;
/*     */       } else {
/* 265 */         nextSibling.previous.next = node;
/*     */       } 
/* 267 */       nextSibling.previous = node;
/* 268 */       nextSibling.previousSibling = node;
/*     */     } 
/* 270 */     this.size++;
/* 271 */     return node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeNode(Node<K, V> node) {
/* 279 */     if (node.previous != null) {
/* 280 */       node.previous.next = node.next;
/*     */     } else {
/* 282 */       this.head = node.next;
/*     */     } 
/* 284 */     if (node.next != null) {
/* 285 */       node.next.previous = node.previous;
/*     */     } else {
/* 287 */       this.tail = node.previous;
/*     */     } 
/* 289 */     if (node.previousSibling == null && node.nextSibling == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 295 */       KeyList<K, V> keyList = Objects.<KeyList<K, V>>requireNonNull(this.keyToKeyList.remove(node.key));
/* 296 */       keyList.count = 0;
/* 297 */       this.modCount++;
/*     */     } else {
/*     */       
/* 300 */       KeyList<K, V> keyList = Objects.<KeyList<K, V>>requireNonNull(this.keyToKeyList.get(node.key));
/* 301 */       keyList.count--;
/*     */       
/* 303 */       if (node.previousSibling == null) {
/*     */         
/* 305 */         keyList.head = Objects.<Node<K, V>>requireNonNull(node.nextSibling);
/*     */       } else {
/* 307 */         node.previousSibling.nextSibling = node.nextSibling;
/*     */       } 
/*     */       
/* 310 */       if (node.nextSibling == null) {
/*     */         
/* 312 */         keyList.tail = Objects.<Node<K, V>>requireNonNull(node.previousSibling);
/*     */       } else {
/* 314 */         node.nextSibling.previousSibling = node.previousSibling;
/*     */       } 
/*     */     } 
/* 317 */     this.size--;
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeAllNodes(@ParametricNullness K key) {
/* 322 */     Iterators.clear(new ValueForKeyIterator(key));
/*     */   }
/*     */   private class NodeIterator implements ListIterator<Map.Entry<K, V>> { int nextIndex;
/*     */     @CheckForNull
/*     */     LinkedListMultimap.Node<K, V> next;
/*     */     @CheckForNull
/*     */     LinkedListMultimap.Node<K, V> current;
/*     */     @CheckForNull
/*     */     LinkedListMultimap.Node<K, V> previous;
/* 331 */     int expectedModCount = LinkedListMultimap.this.modCount;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void checkForConcurrentModification() {
/* 352 */       if (LinkedListMultimap.this.modCount != this.expectedModCount) {
/* 353 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 359 */       checkForConcurrentModification();
/* 360 */       return (this.next != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public LinkedListMultimap.Node<K, V> next() {
/* 366 */       checkForConcurrentModification();
/* 367 */       if (this.next == null) {
/* 368 */         throw new NoSuchElementException();
/*     */       }
/* 370 */       this.previous = this.current = this.next;
/* 371 */       this.next = this.next.next;
/* 372 */       this.nextIndex++;
/* 373 */       return this.current;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 378 */       checkForConcurrentModification();
/* 379 */       Preconditions.checkState((this.current != null), "no calls to next() since the last call to remove()");
/* 380 */       if (this.current != this.next) {
/* 381 */         this.previous = this.current.previous;
/* 382 */         this.nextIndex--;
/*     */       } else {
/* 384 */         this.next = this.current.next;
/*     */       } 
/* 386 */       LinkedListMultimap.this.removeNode(this.current);
/* 387 */       this.current = null;
/* 388 */       this.expectedModCount = LinkedListMultimap.this.modCount;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasPrevious() {
/* 393 */       checkForConcurrentModification();
/* 394 */       return (this.previous != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public LinkedListMultimap.Node<K, V> previous() {
/* 400 */       checkForConcurrentModification();
/* 401 */       if (this.previous == null) {
/* 402 */         throw new NoSuchElementException();
/*     */       }
/* 404 */       this.next = this.current = this.previous;
/* 405 */       this.previous = this.previous.previous;
/* 406 */       this.nextIndex--;
/* 407 */       return this.current;
/*     */     }
/*     */ 
/*     */     
/*     */     public int nextIndex() {
/* 412 */       return this.nextIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public int previousIndex() {
/* 417 */       return this.nextIndex - 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(Map.Entry<K, V> e) {
/* 422 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(Map.Entry<K, V> e) {
/* 427 */       throw new UnsupportedOperationException(); }
/*     */     NodeIterator(int index) { int size = LinkedListMultimap.this.size(); Preconditions.checkPositionIndex(index, size); if (index >= size / 2) { this.previous = LinkedListMultimap.this.tail; this.nextIndex = size; while (index++ < size)
/*     */           previous();  } else { this.next = LinkedListMultimap.this.head; while (index-- > 0)
/*     */           next();  }
/* 431 */        this.current = null; } void setValue(@ParametricNullness V value) { Preconditions.checkState((this.current != null));
/* 432 */       this.current.value = value; }
/*     */      }
/*     */ 
/*     */   
/*     */   private class DistinctKeyIterator
/*     */     implements Iterator<K> {
/* 438 */     final Set<K> seenKeys = Sets.newHashSetWithExpectedSize(LinkedListMultimap.this.keySet().size()); @CheckForNull
/* 439 */     LinkedListMultimap.Node<K, V> next = LinkedListMultimap.this.head; @CheckForNull
/*     */     LinkedListMultimap.Node<K, V> current;
/* 441 */     int expectedModCount = LinkedListMultimap.this.modCount;
/*     */     
/*     */     private void checkForConcurrentModification() {
/* 444 */       if (LinkedListMultimap.this.modCount != this.expectedModCount) {
/* 445 */         throw new ConcurrentModificationException();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 451 */       checkForConcurrentModification();
/* 452 */       return (this.next != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     public K next() {
/* 458 */       checkForConcurrentModification();
/* 459 */       if (this.next == null) {
/* 460 */         throw new NoSuchElementException();
/*     */       }
/* 462 */       this.current = this.next;
/* 463 */       this.seenKeys.add(this.current.key);
/*     */       do {
/* 465 */         this.next = this.next.next;
/* 466 */       } while (this.next != null && !this.seenKeys.add(this.next.key));
/* 467 */       return this.current.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 472 */       checkForConcurrentModification();
/* 473 */       Preconditions.checkState((this.current != null), "no calls to next() since the last call to remove()");
/* 474 */       LinkedListMultimap.this.removeAllNodes(this.current.key);
/* 475 */       this.current = null;
/* 476 */       this.expectedModCount = LinkedListMultimap.this.modCount;
/*     */     }
/*     */     private DistinctKeyIterator() {} }
/*     */   private class ValueForKeyIterator implements ListIterator<V> { @ParametricNullness
/*     */     final K key;
/*     */     int nextIndex;
/*     */     @CheckForNull
/*     */     LinkedListMultimap.Node<K, V> next;
/*     */     @CheckForNull
/*     */     LinkedListMultimap.Node<K, V> current;
/*     */     @CheckForNull
/*     */     LinkedListMultimap.Node<K, V> previous;
/*     */     
/*     */     ValueForKeyIterator(K key) {
/* 490 */       this.key = key;
/* 491 */       LinkedListMultimap.KeyList<K, V> keyList = (LinkedListMultimap.KeyList<K, V>)LinkedListMultimap.this.keyToKeyList.get(key);
/* 492 */       this.next = (keyList == null) ? null : keyList.head;
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
/*     */     public ValueForKeyIterator(K key, int index) {
/* 504 */       LinkedListMultimap.KeyList<K, V> keyList = (LinkedListMultimap.KeyList<K, V>)LinkedListMultimap.this.keyToKeyList.get(key);
/* 505 */       int size = (keyList == null) ? 0 : keyList.count;
/* 506 */       Preconditions.checkPositionIndex(index, size);
/* 507 */       if (index >= size / 2) {
/* 508 */         this.previous = (keyList == null) ? null : keyList.tail;
/* 509 */         this.nextIndex = size;
/* 510 */         while (index++ < size) {
/* 511 */           previous();
/*     */         }
/*     */       } else {
/* 514 */         this.next = (keyList == null) ? null : keyList.head;
/* 515 */         while (index-- > 0) {
/* 516 */           next();
/*     */         }
/*     */       } 
/* 519 */       this.key = key;
/* 520 */       this.current = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 525 */       return (this.next != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     @CanIgnoreReturnValue
/*     */     public V next() {
/* 532 */       if (this.next == null) {
/* 533 */         throw new NoSuchElementException();
/*     */       }
/* 535 */       this.previous = this.current = this.next;
/* 536 */       this.next = this.next.nextSibling;
/* 537 */       this.nextIndex++;
/* 538 */       return this.current.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasPrevious() {
/* 543 */       return (this.previous != null);
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     @CanIgnoreReturnValue
/*     */     public V previous() {
/* 550 */       if (this.previous == null) {
/* 551 */         throw new NoSuchElementException();
/*     */       }
/* 553 */       this.next = this.current = this.previous;
/* 554 */       this.previous = this.previous.previousSibling;
/* 555 */       this.nextIndex--;
/* 556 */       return this.current.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public int nextIndex() {
/* 561 */       return this.nextIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public int previousIndex() {
/* 566 */       return this.nextIndex - 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 571 */       Preconditions.checkState((this.current != null), "no calls to next() since the last call to remove()");
/* 572 */       if (this.current != this.next) {
/* 573 */         this.previous = this.current.previousSibling;
/* 574 */         this.nextIndex--;
/*     */       } else {
/* 576 */         this.next = this.current.nextSibling;
/*     */       } 
/* 578 */       LinkedListMultimap.this.removeNode(this.current);
/* 579 */       this.current = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(@ParametricNullness V value) {
/* 584 */       Preconditions.checkState((this.current != null));
/* 585 */       this.current.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(@ParametricNullness V value) {
/* 590 */       this.previous = LinkedListMultimap.this.addNode(this.key, value, this.next);
/* 591 */       this.nextIndex++;
/* 592 */       this.current = null;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 600 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 605 */     return (this.head == null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(@CheckForNull Object key) {
/* 610 */     return this.keyToKeyList.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@CheckForNull Object value) {
/* 615 */     return values().contains(value);
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
/*     */   public boolean put(@ParametricNullness K key, @ParametricNullness V value) {
/* 630 */     addNode(key, value, null);
/* 631 */     return true;
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
/*     */   @CanIgnoreReturnValue
/*     */   public List<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
/* 647 */     List<V> oldValues = getCopy(key);
/* 648 */     ListIterator<V> keyValues = new ValueForKeyIterator(key);
/* 649 */     Iterator<? extends V> newValues = values.iterator();
/*     */ 
/*     */     
/* 652 */     while (keyValues.hasNext() && newValues.hasNext()) {
/* 653 */       keyValues.next();
/* 654 */       keyValues.set(newValues.next());
/*     */     } 
/*     */ 
/*     */     
/* 658 */     while (keyValues.hasNext()) {
/* 659 */       keyValues.next();
/* 660 */       keyValues.remove();
/*     */     } 
/*     */ 
/*     */     
/* 664 */     while (newValues.hasNext()) {
/* 665 */       keyValues.add(newValues.next());
/*     */     }
/*     */     
/* 668 */     return oldValues;
/*     */   }
/*     */   
/*     */   private List<V> getCopy(@ParametricNullness K key) {
/* 672 */     return Collections.unmodifiableList(Lists.newArrayList(new ValueForKeyIterator(key)));
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
/*     */   @CanIgnoreReturnValue
/*     */   public List<V> removeAll(@CheckForNull Object key) {
/* 689 */     K castKey = (K)key;
/* 690 */     List<V> oldValues = getCopy(castKey);
/* 691 */     removeAllNodes(castKey);
/* 692 */     return oldValues;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 697 */     this.head = null;
/* 698 */     this.tail = null;
/* 699 */     this.keyToKeyList.clear();
/* 700 */     this.size = 0;
/* 701 */     this.modCount++;
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
/*     */   public List<V> get(@ParametricNullness final K key) {
/* 717 */     return new AbstractSequentialList<V>()
/*     */       {
/*     */         public int size() {
/* 720 */           LinkedListMultimap.KeyList<K, V> keyList = (LinkedListMultimap.KeyList<K, V>)LinkedListMultimap.this.keyToKeyList.get(key);
/* 721 */           return (keyList == null) ? 0 : keyList.count;
/*     */         }
/*     */ 
/*     */         
/*     */         public ListIterator<V> listIterator(int index) {
/* 726 */           return new LinkedListMultimap.ValueForKeyIterator((K)key, index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Set<K> createKeySet() {
/*     */     class KeySetImpl
/*     */       extends Sets.ImprovedAbstractSet<K>
/*     */     {
/*     */       public int size() {
/* 737 */         return LinkedListMultimap.this.keyToKeyList.size();
/*     */       }
/*     */ 
/*     */       
/*     */       public Iterator<K> iterator() {
/* 742 */         return new LinkedListMultimap.DistinctKeyIterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(@CheckForNull Object key) {
/* 747 */         return LinkedListMultimap.this.containsKey(key);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(@CheckForNull Object o) {
/* 752 */         return !LinkedListMultimap.this.removeAll(o).isEmpty();
/*     */       }
/*     */     };
/* 755 */     return new KeySetImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   Multiset<K> createKeys() {
/* 760 */     return new Multimaps.Keys<>(this);
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
/*     */   public List<V> values() {
/* 773 */     return (List<V>)super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   List<V> createValues() {
/*     */     class ValuesImpl
/*     */       extends AbstractSequentialList<V>
/*     */     {
/*     */       public int size() {
/* 782 */         return LinkedListMultimap.this.size;
/*     */       }
/*     */ 
/*     */       
/*     */       public ListIterator<V> listIterator(int index) {
/* 787 */         final LinkedListMultimap<K, V>.NodeIterator nodeItr = new LinkedListMultimap.NodeIterator(index);
/* 788 */         return new TransformedListIterator<Map.Entry<K, V>, V>(this, nodeItr)
/*     */           {
/*     */             @ParametricNullness
/*     */             V transform(Map.Entry<K, V> entry) {
/* 792 */               return entry.getValue();
/*     */             }
/*     */ 
/*     */             
/*     */             public void set(@ParametricNullness V value) {
/* 797 */               nodeItr.setValue(value);
/*     */             }
/*     */           };
/*     */       }
/*     */     };
/* 802 */     return new ValuesImpl();
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
/*     */   public List<Map.Entry<K, V>> entries() {
/* 823 */     return (List<Map.Entry<K, V>>)super.entries();
/*     */   }
/*     */ 
/*     */   
/*     */   List<Map.Entry<K, V>> createEntries() {
/*     */     class EntriesImpl
/*     */       extends AbstractSequentialList<Map.Entry<K, V>>
/*     */     {
/*     */       public int size() {
/* 832 */         return LinkedListMultimap.this.size;
/*     */       }
/*     */ 
/*     */       
/*     */       public ListIterator<Map.Entry<K, V>> listIterator(int index) {
/* 837 */         return new LinkedListMultimap.NodeIterator(index);
/*     */       }
/*     */ 
/*     */       
/*     */       public void forEach(Consumer<? super Map.Entry<K, V>> action) {
/* 842 */         Preconditions.checkNotNull(action);
/* 843 */         for (LinkedListMultimap.Node<K, V> node = LinkedListMultimap.this.head; node != null; node = node.next) {
/* 844 */           action.accept(node);
/*     */         }
/*     */       }
/*     */     };
/* 848 */     return new EntriesImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 853 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   Map<K, Collection<V>> createAsMap() {
/* 858 */     return new Multimaps.AsMap<>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 869 */     stream.defaultWriteObject();
/* 870 */     stream.writeInt(size());
/* 871 */     for (Map.Entry<K, V> entry : entries()) {
/* 872 */       stream.writeObject(entry.getKey());
/* 873 */       stream.writeObject(entry.getValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 880 */     stream.defaultReadObject();
/* 881 */     this.keyToKeyList = Maps.newLinkedHashMap();
/* 882 */     int size = stream.readInt();
/* 883 */     for (int i = 0; i < size; i++) {
/*     */       
/* 885 */       K key = (K)stream.readObject();
/*     */       
/* 887 */       V value = (V)stream.readObject();
/* 888 */       put(key, value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/LinkedListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */