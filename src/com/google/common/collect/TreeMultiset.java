/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.J2ktIncompatible;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Comparator;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.function.ObjIntConsumer;
/*      */ import javax.annotation.CheckForNull;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @ElementTypesAreNonnullByDefault
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class TreeMultiset<E>
/*      */   extends AbstractSortedMultiset<E>
/*      */   implements Serializable
/*      */ {
/*      */   private final transient Reference<AvlNode<E>> rootReference;
/*      */   private final transient GeneralRange<E> range;
/*      */   private final transient AvlNode<E> header;
/*      */   @GwtIncompatible
/*      */   @J2ktIncompatible
/*      */   private static final long serialVersionUID = 1L;
/*      */   
/*      */   public static <E extends Comparable> TreeMultiset<E> create() {
/*   79 */     return new TreeMultiset<>(Ordering.natural());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E> TreeMultiset<E> create(@CheckForNull Comparator<? super E> comparator) {
/*   96 */     return (comparator == null) ? 
/*   97 */       new TreeMultiset<>(Ordering.natural()) : 
/*   98 */       new TreeMultiset<>(comparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <E extends Comparable> TreeMultiset<E> create(Iterable<? extends E> elements) {
/*  111 */     TreeMultiset<E> multiset = create();
/*  112 */     Iterables.addAll(multiset, elements);
/*  113 */     return multiset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TreeMultiset(Reference<AvlNode<E>> rootReference, GeneralRange<E> range, AvlNode<E> endLink) {
/*  121 */     super(range.comparator());
/*  122 */     this.rootReference = rootReference;
/*  123 */     this.range = range;
/*  124 */     this.header = endLink;
/*      */   }
/*      */   
/*      */   TreeMultiset(Comparator<? super E> comparator) {
/*  128 */     super(comparator);
/*  129 */     this.range = GeneralRange.all(comparator);
/*  130 */     this.header = new AvlNode<>();
/*  131 */     successor(this.header, this.header);
/*  132 */     this.rootReference = new Reference<>();
/*      */   }
/*      */   
/*      */   private enum Aggregate
/*      */   {
/*  137 */     SIZE
/*      */     {
/*      */       int nodeAggregate(TreeMultiset.AvlNode<?> node) {
/*  140 */         return node.elemCount;
/*      */       }
/*      */ 
/*      */       
/*      */       long treeAggregate(@CheckForNull TreeMultiset.AvlNode<?> root) {
/*  145 */         return (root == null) ? 0L : root.totalCount;
/*      */       }
/*      */     },
/*  148 */     DISTINCT
/*      */     {
/*      */       int nodeAggregate(TreeMultiset.AvlNode<?> node) {
/*  151 */         return 1;
/*      */       }
/*      */ 
/*      */       
/*      */       long treeAggregate(@CheckForNull TreeMultiset.AvlNode<?> root) {
/*  156 */         return (root == null) ? 0L : root.distinctElements;
/*      */       }
/*      */     };
/*      */     
/*      */     abstract int nodeAggregate(TreeMultiset.AvlNode<?> param1AvlNode);
/*      */     
/*      */     abstract long treeAggregate(@CheckForNull TreeMultiset.AvlNode<?> param1AvlNode);
/*      */   }
/*      */   
/*      */   private long aggregateForEntries(Aggregate aggr) {
/*  166 */     AvlNode<E> root = this.rootReference.get();
/*  167 */     long total = aggr.treeAggregate(root);
/*  168 */     if (this.range.hasLowerBound()) {
/*  169 */       total -= aggregateBelowRange(aggr, root);
/*      */     }
/*  171 */     if (this.range.hasUpperBound()) {
/*  172 */       total -= aggregateAboveRange(aggr, root);
/*      */     }
/*  174 */     return total;
/*      */   }
/*      */   
/*      */   private long aggregateBelowRange(Aggregate aggr, @CheckForNull AvlNode<E> node) {
/*  178 */     if (node == null) {
/*  179 */       return 0L;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  184 */     int cmp = comparator().compare(NullnessCasts.uncheckedCastNullableTToT(this.range.getLowerEndpoint()), node.getElement());
/*  185 */     if (cmp < 0)
/*  186 */       return aggregateBelowRange(aggr, node.left); 
/*  187 */     if (cmp == 0) {
/*  188 */       switch (this.range.getLowerBoundType()) {
/*      */         case OPEN:
/*  190 */           return aggr.nodeAggregate(node) + aggr.treeAggregate(node.left);
/*      */         case CLOSED:
/*  192 */           return aggr.treeAggregate(node.left);
/*      */       } 
/*  194 */       throw new AssertionError();
/*      */     } 
/*      */     
/*  197 */     return aggr.treeAggregate(node.left) + aggr
/*  198 */       .nodeAggregate(node) + 
/*  199 */       aggregateBelowRange(aggr, node.right);
/*      */   }
/*      */ 
/*      */   
/*      */   private long aggregateAboveRange(Aggregate aggr, @CheckForNull AvlNode<E> node) {
/*  204 */     if (node == null) {
/*  205 */       return 0L;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  210 */     int cmp = comparator().compare(NullnessCasts.uncheckedCastNullableTToT(this.range.getUpperEndpoint()), node.getElement());
/*  211 */     if (cmp > 0)
/*  212 */       return aggregateAboveRange(aggr, node.right); 
/*  213 */     if (cmp == 0) {
/*  214 */       switch (this.range.getUpperBoundType()) {
/*      */         case OPEN:
/*  216 */           return aggr.nodeAggregate(node) + aggr.treeAggregate(node.right);
/*      */         case CLOSED:
/*  218 */           return aggr.treeAggregate(node.right);
/*      */       } 
/*  220 */       throw new AssertionError();
/*      */     } 
/*      */     
/*  223 */     return aggr.treeAggregate(node.right) + aggr
/*  224 */       .nodeAggregate(node) + 
/*  225 */       aggregateAboveRange(aggr, node.left);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  231 */     return Ints.saturatedCast(aggregateForEntries(Aggregate.SIZE));
/*      */   }
/*      */ 
/*      */   
/*      */   int distinctElements() {
/*  236 */     return Ints.saturatedCast(aggregateForEntries(Aggregate.DISTINCT));
/*      */   }
/*      */   
/*      */   static int distinctElements(@CheckForNull AvlNode<?> node) {
/*  240 */     return (node == null) ? 0 : node.distinctElements;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int count(@CheckForNull Object element) {
/*      */     try {
/*  247 */       E e = (E)element;
/*  248 */       AvlNode<E> root = this.rootReference.get();
/*  249 */       if (!this.range.contains(e) || root == null) {
/*  250 */         return 0;
/*      */       }
/*  252 */       return root.count(comparator(), e);
/*  253 */     } catch (ClassCastException|NullPointerException e) {
/*  254 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public int add(@ParametricNullness E element, int occurrences) {
/*  261 */     CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/*  262 */     if (occurrences == 0) {
/*  263 */       return count(element);
/*      */     }
/*  265 */     Preconditions.checkArgument(this.range.contains(element));
/*  266 */     AvlNode<E> root = this.rootReference.get();
/*  267 */     if (root == null) {
/*  268 */       int unused = comparator().compare(element, element);
/*  269 */       AvlNode<E> avlNode = new AvlNode<>(element, occurrences);
/*  270 */       successor(this.header, avlNode, this.header);
/*  271 */       this.rootReference.checkAndSet(root, avlNode);
/*  272 */       return 0;
/*      */     } 
/*  274 */     int[] result = new int[1];
/*  275 */     AvlNode<E> newRoot = root.add(comparator(), element, occurrences, result);
/*  276 */     this.rootReference.checkAndSet(root, newRoot);
/*  277 */     return result[0];
/*      */   }
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public int remove(@CheckForNull Object element, int occurrences) {
/*      */     AvlNode<E> newRoot;
/*  283 */     CollectPreconditions.checkNonnegative(occurrences, "occurrences");
/*  284 */     if (occurrences == 0) {
/*  285 */       return count(element);
/*      */     }
/*  287 */     AvlNode<E> root = this.rootReference.get();
/*  288 */     int[] result = new int[1];
/*      */ 
/*      */     
/*      */     try {
/*  292 */       E e = (E)element;
/*  293 */       if (!this.range.contains(e) || root == null) {
/*  294 */         return 0;
/*      */       }
/*  296 */       newRoot = root.remove(comparator(), e, occurrences, result);
/*  297 */     } catch (ClassCastException|NullPointerException e) {
/*  298 */       return 0;
/*      */     } 
/*  300 */     this.rootReference.checkAndSet(root, newRoot);
/*  301 */     return result[0];
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public int setCount(@ParametricNullness E element, int count) {
/*  307 */     CollectPreconditions.checkNonnegative(count, "count");
/*  308 */     if (!this.range.contains(element)) {
/*  309 */       Preconditions.checkArgument((count == 0));
/*  310 */       return 0;
/*      */     } 
/*      */     
/*  313 */     AvlNode<E> root = this.rootReference.get();
/*  314 */     if (root == null) {
/*  315 */       if (count > 0) {
/*  316 */         add(element, count);
/*      */       }
/*  318 */       return 0;
/*      */     } 
/*  320 */     int[] result = new int[1];
/*  321 */     AvlNode<E> newRoot = root.setCount(comparator(), element, count, result);
/*  322 */     this.rootReference.checkAndSet(root, newRoot);
/*  323 */     return result[0];
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean setCount(@ParametricNullness E element, int oldCount, int newCount) {
/*  329 */     CollectPreconditions.checkNonnegative(newCount, "newCount");
/*  330 */     CollectPreconditions.checkNonnegative(oldCount, "oldCount");
/*  331 */     Preconditions.checkArgument(this.range.contains(element));
/*      */     
/*  333 */     AvlNode<E> root = this.rootReference.get();
/*  334 */     if (root == null) {
/*  335 */       if (oldCount == 0) {
/*  336 */         if (newCount > 0) {
/*  337 */           add(element, newCount);
/*      */         }
/*  339 */         return true;
/*      */       } 
/*  341 */       return false;
/*      */     } 
/*      */     
/*  344 */     int[] result = new int[1];
/*  345 */     AvlNode<E> newRoot = root.setCount(comparator(), element, oldCount, newCount, result);
/*  346 */     this.rootReference.checkAndSet(root, newRoot);
/*  347 */     return (result[0] == oldCount);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/*  352 */     if (!this.range.hasLowerBound() && !this.range.hasUpperBound()) {
/*      */       
/*  354 */       for (AvlNode<E> current = this.header.succ(); current != this.header; ) {
/*  355 */         AvlNode<E> next = current.succ();
/*      */         
/*  357 */         current.elemCount = 0;
/*      */         
/*  359 */         current.left = null;
/*  360 */         current.right = null;
/*  361 */         current.pred = null;
/*  362 */         current.succ = null;
/*      */         
/*  364 */         current = next;
/*      */       } 
/*  366 */       successor(this.header, this.header);
/*  367 */       this.rootReference.clear();
/*      */     } else {
/*      */       
/*  370 */       Iterators.clear(entryIterator());
/*      */     } 
/*      */   }
/*      */   
/*      */   private Multiset.Entry<E> wrapEntry(final AvlNode<E> baseEntry) {
/*  375 */     return new Multisets.AbstractEntry<E>()
/*      */       {
/*      */         @ParametricNullness
/*      */         public E getElement() {
/*  379 */           return baseEntry.getElement();
/*      */         }
/*      */ 
/*      */         
/*      */         public int getCount() {
/*  384 */           int result = baseEntry.getCount();
/*  385 */           if (result == 0) {
/*  386 */             return TreeMultiset.this.count(getElement());
/*      */           }
/*  388 */           return result;
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   private AvlNode<E> firstNode() {
/*  397 */     AvlNode<E> node, root = this.rootReference.get();
/*  398 */     if (root == null) {
/*  399 */       return null;
/*      */     }
/*      */     
/*  402 */     if (this.range.hasLowerBound()) {
/*      */       
/*  404 */       E endpoint = NullnessCasts.uncheckedCastNullableTToT(this.range.getLowerEndpoint());
/*  405 */       node = root.ceiling(comparator(), endpoint);
/*  406 */       if (node == null) {
/*  407 */         return null;
/*      */       }
/*  409 */       if (this.range.getLowerBoundType() == BoundType.OPEN && 
/*  410 */         comparator().compare(endpoint, node.getElement()) == 0) {
/*  411 */         node = node.succ();
/*      */       }
/*      */     } else {
/*  414 */       node = this.header.succ();
/*      */     } 
/*  416 */     return (node == this.header || !this.range.contains(node.getElement())) ? null : node;
/*      */   }
/*      */   
/*      */   @CheckForNull
/*      */   private AvlNode<E> lastNode() {
/*  421 */     AvlNode<E> node, root = this.rootReference.get();
/*  422 */     if (root == null) {
/*  423 */       return null;
/*      */     }
/*      */     
/*  426 */     if (this.range.hasUpperBound()) {
/*      */       
/*  428 */       E endpoint = NullnessCasts.uncheckedCastNullableTToT(this.range.getUpperEndpoint());
/*  429 */       node = root.floor(comparator(), endpoint);
/*  430 */       if (node == null) {
/*  431 */         return null;
/*      */       }
/*  433 */       if (this.range.getUpperBoundType() == BoundType.OPEN && 
/*  434 */         comparator().compare(endpoint, node.getElement()) == 0) {
/*  435 */         node = node.pred();
/*      */       }
/*      */     } else {
/*  438 */       node = this.header.pred();
/*      */     } 
/*  440 */     return (node == this.header || !this.range.contains(node.getElement())) ? null : node;
/*      */   }
/*      */ 
/*      */   
/*      */   Iterator<E> elementIterator() {
/*  445 */     return Multisets.elementIterator(entryIterator());
/*      */   }
/*      */ 
/*      */   
/*      */   Iterator<Multiset.Entry<E>> entryIterator() {
/*  450 */     return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>() { @CheckForNull
/*  451 */         TreeMultiset.AvlNode<E> current = TreeMultiset.this.firstNode();
/*      */         @CheckForNull
/*      */         Multiset.Entry<E> prevEntry;
/*      */         
/*      */         public boolean hasNext() {
/*  456 */           if (this.current == null)
/*  457 */             return false; 
/*  458 */           if (TreeMultiset.this.range.tooHigh(this.current.getElement())) {
/*  459 */             this.current = null;
/*  460 */             return false;
/*      */           } 
/*  462 */           return true;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public Multiset.Entry<E> next() {
/*  468 */           if (!hasNext()) {
/*  469 */             throw new NoSuchElementException();
/*      */           }
/*      */           
/*  472 */           Multiset.Entry<E> result = TreeMultiset.this.wrapEntry(Objects.<TreeMultiset.AvlNode>requireNonNull(this.current));
/*  473 */           this.prevEntry = result;
/*  474 */           if (this.current.succ() == TreeMultiset.this.header) {
/*  475 */             this.current = null;
/*      */           } else {
/*  477 */             this.current = this.current.succ();
/*      */           } 
/*  479 */           return result;
/*      */         }
/*      */ 
/*      */         
/*      */         public void remove() {
/*  484 */           Preconditions.checkState((this.prevEntry != null), "no calls to next() since the last call to remove()");
/*  485 */           TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
/*  486 */           this.prevEntry = null;
/*      */         } }
/*      */       ;
/*      */   }
/*      */ 
/*      */   
/*      */   Iterator<Multiset.Entry<E>> descendingEntryIterator() {
/*  493 */     return (Iterator)new Iterator<Multiset.Entry<Multiset.Entry<E>>>() { @CheckForNull
/*  494 */         TreeMultiset.AvlNode<E> current = TreeMultiset.this.lastNode(); @CheckForNull
/*  495 */         Multiset.Entry<E> prevEntry = null;
/*      */ 
/*      */         
/*      */         public boolean hasNext() {
/*  499 */           if (this.current == null)
/*  500 */             return false; 
/*  501 */           if (TreeMultiset.this.range.tooLow(this.current.getElement())) {
/*  502 */             this.current = null;
/*  503 */             return false;
/*      */           } 
/*  505 */           return true;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public Multiset.Entry<E> next() {
/*  511 */           if (!hasNext()) {
/*  512 */             throw new NoSuchElementException();
/*      */           }
/*      */           
/*  515 */           Objects.requireNonNull(this.current);
/*  516 */           Multiset.Entry<E> result = TreeMultiset.this.wrapEntry(this.current);
/*  517 */           this.prevEntry = result;
/*  518 */           if (this.current.pred() == TreeMultiset.this.header) {
/*  519 */             this.current = null;
/*      */           } else {
/*  521 */             this.current = this.current.pred();
/*      */           } 
/*  523 */           return result;
/*      */         }
/*      */ 
/*      */         
/*      */         public void remove() {
/*  528 */           Preconditions.checkState((this.prevEntry != null), "no calls to next() since the last call to remove()");
/*  529 */           TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
/*  530 */           this.prevEntry = null;
/*      */         } }
/*      */       ;
/*      */   }
/*      */ 
/*      */   
/*      */   public void forEachEntry(ObjIntConsumer<? super E> action) {
/*  537 */     Preconditions.checkNotNull(action);
/*  538 */     AvlNode<E> node = firstNode();
/*  539 */     for (; node != this.header && node != null && !this.range.tooHigh(node.getElement()); 
/*  540 */       node = node.succ()) {
/*  541 */       action.accept(node.getElement(), node.getCount());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public Iterator<E> iterator() {
/*  547 */     return Multisets.iteratorImpl(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public SortedMultiset<E> headMultiset(@ParametricNullness E upperBound, BoundType boundType) {
/*  552 */     return new TreeMultiset(this.rootReference, this.range
/*      */         
/*  554 */         .intersect(GeneralRange.upTo(comparator(), upperBound, boundType)), this.header);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public SortedMultiset<E> tailMultiset(@ParametricNullness E lowerBound, BoundType boundType) {
/*  560 */     return new TreeMultiset(this.rootReference, this.range
/*      */         
/*  562 */         .intersect(GeneralRange.downTo(comparator(), lowerBound, boundType)), this.header);
/*      */   }
/*      */   
/*      */   private static final class Reference<T> {
/*      */     @CheckForNull
/*      */     private T value;
/*      */     
/*      */     @CheckForNull
/*      */     public T get() {
/*  571 */       return this.value;
/*      */     }
/*      */     private Reference() {}
/*      */     public void checkAndSet(@CheckForNull T expected, @CheckForNull T newValue) {
/*  575 */       if (this.value != expected) {
/*  576 */         throw new ConcurrentModificationException();
/*      */       }
/*  578 */       this.value = newValue;
/*      */     }
/*      */     
/*      */     void clear() {
/*  582 */       this.value = null;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class AvlNode<E>
/*      */   {
/*      */     @CheckForNull
/*      */     private final E elem;
/*      */ 
/*      */     
/*      */     private int elemCount;
/*      */ 
/*      */     
/*      */     private int distinctElements;
/*      */ 
/*      */     
/*      */     private long totalCount;
/*      */ 
/*      */     
/*      */     private int height;
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     private AvlNode<E> left;
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     private AvlNode<E> right;
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     private AvlNode<E> pred;
/*      */     
/*      */     @CheckForNull
/*      */     private AvlNode<E> succ;
/*      */ 
/*      */     
/*      */     AvlNode(@ParametricNullness E elem, int elemCount) {
/*  622 */       Preconditions.checkArgument((elemCount > 0));
/*  623 */       this.elem = elem;
/*  624 */       this.elemCount = elemCount;
/*  625 */       this.totalCount = elemCount;
/*  626 */       this.distinctElements = 1;
/*  627 */       this.height = 1;
/*  628 */       this.left = null;
/*  629 */       this.right = null;
/*      */     }
/*      */ 
/*      */     
/*      */     AvlNode() {
/*  634 */       this.elem = null;
/*  635 */       this.elemCount = 1;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private AvlNode<E> pred() {
/*  641 */       return Objects.<AvlNode<E>>requireNonNull(this.pred);
/*      */     }
/*      */     
/*      */     private AvlNode<E> succ() {
/*  645 */       return Objects.<AvlNode<E>>requireNonNull(this.succ);
/*      */     }
/*      */     
/*      */     int count(Comparator<? super E> comparator, @ParametricNullness E e) {
/*  649 */       int cmp = comparator.compare(e, getElement());
/*  650 */       if (cmp < 0)
/*  651 */         return (this.left == null) ? 0 : this.left.count(comparator, e); 
/*  652 */       if (cmp > 0) {
/*  653 */         return (this.right == null) ? 0 : this.right.count(comparator, e);
/*      */       }
/*  655 */       return this.elemCount;
/*      */     }
/*      */ 
/*      */     
/*      */     private AvlNode<E> addRightChild(@ParametricNullness E e, int count) {
/*  660 */       this.right = new AvlNode(e, count);
/*  661 */       TreeMultiset.successor(this, this.right, succ());
/*  662 */       this.height = Math.max(2, this.height);
/*  663 */       this.distinctElements++;
/*  664 */       this.totalCount += count;
/*  665 */       return this;
/*      */     }
/*      */     
/*      */     private AvlNode<E> addLeftChild(@ParametricNullness E e, int count) {
/*  669 */       this.left = new AvlNode(e, count);
/*  670 */       TreeMultiset.successor(pred(), this.left, this);
/*  671 */       this.height = Math.max(2, this.height);
/*  672 */       this.distinctElements++;
/*  673 */       this.totalCount += count;
/*  674 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     AvlNode<E> add(Comparator<? super E> comparator, @ParametricNullness E e, int count, int[] result) {
/*  683 */       int cmp = comparator.compare(e, getElement());
/*  684 */       if (cmp < 0) {
/*  685 */         AvlNode<E> initLeft = this.left;
/*  686 */         if (initLeft == null) {
/*  687 */           result[0] = 0;
/*  688 */           return addLeftChild(e, count);
/*      */         } 
/*  690 */         int initHeight = initLeft.height;
/*      */         
/*  692 */         this.left = initLeft.add(comparator, e, count, result);
/*  693 */         if (result[0] == 0) {
/*  694 */           this.distinctElements++;
/*      */         }
/*  696 */         this.totalCount += count;
/*  697 */         return (this.left.height == initHeight) ? this : rebalance();
/*  698 */       }  if (cmp > 0) {
/*  699 */         AvlNode<E> initRight = this.right;
/*  700 */         if (initRight == null) {
/*  701 */           result[0] = 0;
/*  702 */           return addRightChild(e, count);
/*      */         } 
/*  704 */         int initHeight = initRight.height;
/*      */         
/*  706 */         this.right = initRight.add(comparator, e, count, result);
/*  707 */         if (result[0] == 0) {
/*  708 */           this.distinctElements++;
/*      */         }
/*  710 */         this.totalCount += count;
/*  711 */         return (this.right.height == initHeight) ? this : rebalance();
/*      */       } 
/*      */ 
/*      */       
/*  715 */       result[0] = this.elemCount;
/*  716 */       long resultCount = this.elemCount + count;
/*  717 */       Preconditions.checkArgument((resultCount <= 2147483647L));
/*  718 */       this.elemCount += count;
/*  719 */       this.totalCount += count;
/*  720 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     AvlNode<E> remove(Comparator<? super E> comparator, @ParametricNullness E e, int count, int[] result) {
/*  726 */       int cmp = comparator.compare(e, getElement());
/*  727 */       if (cmp < 0) {
/*  728 */         AvlNode<E> initLeft = this.left;
/*  729 */         if (initLeft == null) {
/*  730 */           result[0] = 0;
/*  731 */           return this;
/*      */         } 
/*      */         
/*  734 */         this.left = initLeft.remove(comparator, e, count, result);
/*      */         
/*  736 */         if (result[0] > 0) {
/*  737 */           if (count >= result[0]) {
/*  738 */             this.distinctElements--;
/*  739 */             this.totalCount -= result[0];
/*      */           } else {
/*  741 */             this.totalCount -= count;
/*      */           } 
/*      */         }
/*  744 */         return (result[0] == 0) ? this : rebalance();
/*  745 */       }  if (cmp > 0) {
/*  746 */         AvlNode<E> initRight = this.right;
/*  747 */         if (initRight == null) {
/*  748 */           result[0] = 0;
/*  749 */           return this;
/*      */         } 
/*      */         
/*  752 */         this.right = initRight.remove(comparator, e, count, result);
/*      */         
/*  754 */         if (result[0] > 0) {
/*  755 */           if (count >= result[0]) {
/*  756 */             this.distinctElements--;
/*  757 */             this.totalCount -= result[0];
/*      */           } else {
/*  759 */             this.totalCount -= count;
/*      */           } 
/*      */         }
/*  762 */         return rebalance();
/*      */       } 
/*      */ 
/*      */       
/*  766 */       result[0] = this.elemCount;
/*  767 */       if (count >= this.elemCount) {
/*  768 */         return deleteMe();
/*      */       }
/*  770 */       this.elemCount -= count;
/*  771 */       this.totalCount -= count;
/*  772 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     AvlNode<E> setCount(Comparator<? super E> comparator, @ParametricNullness E e, int count, int[] result) {
/*  779 */       int cmp = comparator.compare(e, getElement());
/*  780 */       if (cmp < 0) {
/*  781 */         AvlNode<E> initLeft = this.left;
/*  782 */         if (initLeft == null) {
/*  783 */           result[0] = 0;
/*  784 */           return (count > 0) ? addLeftChild(e, count) : this;
/*      */         } 
/*      */         
/*  787 */         this.left = initLeft.setCount(comparator, e, count, result);
/*      */         
/*  789 */         if (count == 0 && result[0] != 0) {
/*  790 */           this.distinctElements--;
/*  791 */         } else if (count > 0 && result[0] == 0) {
/*  792 */           this.distinctElements++;
/*      */         } 
/*      */         
/*  795 */         this.totalCount += (count - result[0]);
/*  796 */         return rebalance();
/*  797 */       }  if (cmp > 0) {
/*  798 */         AvlNode<E> initRight = this.right;
/*  799 */         if (initRight == null) {
/*  800 */           result[0] = 0;
/*  801 */           return (count > 0) ? addRightChild(e, count) : this;
/*      */         } 
/*      */         
/*  804 */         this.right = initRight.setCount(comparator, e, count, result);
/*      */         
/*  806 */         if (count == 0 && result[0] != 0) {
/*  807 */           this.distinctElements--;
/*  808 */         } else if (count > 0 && result[0] == 0) {
/*  809 */           this.distinctElements++;
/*      */         } 
/*      */         
/*  812 */         this.totalCount += (count - result[0]);
/*  813 */         return rebalance();
/*      */       } 
/*      */ 
/*      */       
/*  817 */       result[0] = this.elemCount;
/*  818 */       if (count == 0) {
/*  819 */         return deleteMe();
/*      */       }
/*  821 */       this.totalCount += (count - this.elemCount);
/*  822 */       this.elemCount = count;
/*  823 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     AvlNode<E> setCount(Comparator<? super E> comparator, @ParametricNullness E e, int expectedCount, int newCount, int[] result) {
/*  833 */       int cmp = comparator.compare(e, getElement());
/*  834 */       if (cmp < 0) {
/*  835 */         AvlNode<E> initLeft = this.left;
/*  836 */         if (initLeft == null) {
/*  837 */           result[0] = 0;
/*  838 */           if (expectedCount == 0 && newCount > 0) {
/*  839 */             return addLeftChild(e, newCount);
/*      */           }
/*  841 */           return this;
/*      */         } 
/*      */         
/*  844 */         this.left = initLeft.setCount(comparator, e, expectedCount, newCount, result);
/*      */         
/*  846 */         if (result[0] == expectedCount) {
/*  847 */           if (newCount == 0 && result[0] != 0) {
/*  848 */             this.distinctElements--;
/*  849 */           } else if (newCount > 0 && result[0] == 0) {
/*  850 */             this.distinctElements++;
/*      */           } 
/*  852 */           this.totalCount += (newCount - result[0]);
/*      */         } 
/*  854 */         return rebalance();
/*  855 */       }  if (cmp > 0) {
/*  856 */         AvlNode<E> initRight = this.right;
/*  857 */         if (initRight == null) {
/*  858 */           result[0] = 0;
/*  859 */           if (expectedCount == 0 && newCount > 0) {
/*  860 */             return addRightChild(e, newCount);
/*      */           }
/*  862 */           return this;
/*      */         } 
/*      */         
/*  865 */         this.right = initRight.setCount(comparator, e, expectedCount, newCount, result);
/*      */         
/*  867 */         if (result[0] == expectedCount) {
/*  868 */           if (newCount == 0 && result[0] != 0) {
/*  869 */             this.distinctElements--;
/*  870 */           } else if (newCount > 0 && result[0] == 0) {
/*  871 */             this.distinctElements++;
/*      */           } 
/*  873 */           this.totalCount += (newCount - result[0]);
/*      */         } 
/*  875 */         return rebalance();
/*      */       } 
/*      */ 
/*      */       
/*  879 */       result[0] = this.elemCount;
/*  880 */       if (expectedCount == this.elemCount) {
/*  881 */         if (newCount == 0) {
/*  882 */           return deleteMe();
/*      */         }
/*  884 */         this.totalCount += (newCount - this.elemCount);
/*  885 */         this.elemCount = newCount;
/*      */       } 
/*  887 */       return this;
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     private AvlNode<E> deleteMe() {
/*  892 */       int oldElemCount = this.elemCount;
/*  893 */       this.elemCount = 0;
/*  894 */       TreeMultiset.successor(pred(), succ());
/*  895 */       if (this.left == null)
/*  896 */         return this.right; 
/*  897 */       if (this.right == null)
/*  898 */         return this.left; 
/*  899 */       if (this.left.height >= this.right.height) {
/*  900 */         AvlNode<E> avlNode = pred();
/*      */         
/*  902 */         avlNode.left = this.left.removeMax(avlNode);
/*  903 */         avlNode.right = this.right;
/*  904 */         this.distinctElements--;
/*  905 */         this.totalCount -= oldElemCount;
/*  906 */         return avlNode.rebalance();
/*      */       } 
/*  908 */       AvlNode<E> newTop = succ();
/*  909 */       newTop.right = this.right.removeMin(newTop);
/*  910 */       newTop.left = this.left;
/*  911 */       this.distinctElements--;
/*  912 */       this.totalCount -= oldElemCount;
/*  913 */       return newTop.rebalance();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     private AvlNode<E> removeMin(AvlNode<E> node) {
/*  920 */       if (this.left == null) {
/*  921 */         return this.right;
/*      */       }
/*  923 */       this.left = this.left.removeMin(node);
/*  924 */       this.distinctElements--;
/*  925 */       this.totalCount -= node.elemCount;
/*  926 */       return rebalance();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     private AvlNode<E> removeMax(AvlNode<E> node) {
/*  933 */       if (this.right == null) {
/*  934 */         return this.left;
/*      */       }
/*  936 */       this.right = this.right.removeMax(node);
/*  937 */       this.distinctElements--;
/*  938 */       this.totalCount -= node.elemCount;
/*  939 */       return rebalance();
/*      */     }
/*      */ 
/*      */     
/*      */     private void recomputeMultiset() {
/*  944 */       this
/*  945 */         .distinctElements = 1 + TreeMultiset.distinctElements(this.left) + TreeMultiset.distinctElements(this.right);
/*  946 */       this.totalCount = this.elemCount + totalCount(this.left) + totalCount(this.right);
/*      */     }
/*      */     
/*      */     private void recomputeHeight() {
/*  950 */       this.height = 1 + Math.max(height(this.left), height(this.right));
/*      */     }
/*      */     
/*      */     private void recompute() {
/*  954 */       recomputeMultiset();
/*  955 */       recomputeHeight();
/*      */     }
/*      */     
/*      */     private AvlNode<E> rebalance() {
/*  959 */       switch (balanceFactor()) {
/*      */         
/*      */         case -2:
/*  962 */           Objects.requireNonNull(this.right);
/*  963 */           if (this.right.balanceFactor() > 0) {
/*  964 */             this.right = this.right.rotateRight();
/*      */           }
/*  966 */           return rotateLeft();
/*      */         
/*      */         case 2:
/*  969 */           Objects.requireNonNull(this.left);
/*  970 */           if (this.left.balanceFactor() < 0) {
/*  971 */             this.left = this.left.rotateLeft();
/*      */           }
/*  973 */           return rotateRight();
/*      */       } 
/*  975 */       recomputeHeight();
/*  976 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     private int balanceFactor() {
/*  981 */       return height(this.left) - height(this.right);
/*      */     }
/*      */     
/*      */     private AvlNode<E> rotateLeft() {
/*  985 */       Preconditions.checkState((this.right != null));
/*  986 */       AvlNode<E> newTop = this.right;
/*  987 */       this.right = newTop.left;
/*  988 */       newTop.left = this;
/*  989 */       newTop.totalCount = this.totalCount;
/*  990 */       newTop.distinctElements = this.distinctElements;
/*  991 */       recompute();
/*  992 */       newTop.recomputeHeight();
/*  993 */       return newTop;
/*      */     }
/*      */     
/*      */     private AvlNode<E> rotateRight() {
/*  997 */       Preconditions.checkState((this.left != null));
/*  998 */       AvlNode<E> newTop = this.left;
/*  999 */       this.left = newTop.right;
/* 1000 */       newTop.right = this;
/* 1001 */       newTop.totalCount = this.totalCount;
/* 1002 */       newTop.distinctElements = this.distinctElements;
/* 1003 */       recompute();
/* 1004 */       newTop.recomputeHeight();
/* 1005 */       return newTop;
/*      */     }
/*      */     
/*      */     private static long totalCount(@CheckForNull AvlNode<?> node) {
/* 1009 */       return (node == null) ? 0L : node.totalCount;
/*      */     }
/*      */     
/*      */     private static int height(@CheckForNull AvlNode<?> node) {
/* 1013 */       return (node == null) ? 0 : node.height;
/*      */     }
/*      */     
/*      */     @CheckForNull
/*      */     private AvlNode<E> ceiling(Comparator<? super E> comparator, @ParametricNullness E e) {
/* 1018 */       int cmp = comparator.compare(e, getElement());
/* 1019 */       if (cmp < 0)
/* 1020 */         return (this.left == null) ? this : (AvlNode<E>)MoreObjects.firstNonNull(this.left.ceiling(comparator, e), this); 
/* 1021 */       if (cmp == 0) {
/* 1022 */         return this;
/*      */       }
/* 1024 */       return (this.right == null) ? null : this.right.ceiling(comparator, e);
/*      */     }
/*      */ 
/*      */     
/*      */     @CheckForNull
/*      */     private AvlNode<E> floor(Comparator<? super E> comparator, @ParametricNullness E e) {
/* 1030 */       int cmp = comparator.compare(e, getElement());
/* 1031 */       if (cmp > 0)
/* 1032 */         return (this.right == null) ? this : (AvlNode<E>)MoreObjects.firstNonNull(this.right.floor(comparator, e), this); 
/* 1033 */       if (cmp == 0) {
/* 1034 */         return this;
/*      */       }
/* 1036 */       return (this.left == null) ? null : this.left.floor(comparator, e);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @ParametricNullness
/*      */     E getElement() {
/* 1043 */       return NullnessCasts.uncheckedCastNullableTToT(this.elem);
/*      */     }
/*      */     
/*      */     int getCount() {
/* 1047 */       return this.elemCount;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1052 */       return Multisets.<E>immutableEntry(getElement(), getCount()).toString();
/*      */     }
/*      */   }
/*      */   
/*      */   private static <T> void successor(AvlNode<T> a, AvlNode<T> b) {
/* 1057 */     a.succ = b;
/* 1058 */     b.pred = a;
/*      */   }
/*      */ 
/*      */   
/*      */   private static <T> void successor(AvlNode<T> a, AvlNode<T> b, AvlNode<T> c) {
/* 1063 */     successor(a, b);
/* 1064 */     successor(b, c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 1080 */     stream.defaultWriteObject();
/* 1081 */     stream.writeObject(elementSet().comparator());
/* 1082 */     Serialization.writeMultiset(this, stream);
/*      */   }
/*      */   
/*      */   @J2ktIncompatible
/*      */   @GwtIncompatible
/*      */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 1088 */     stream.defaultReadObject();
/*      */ 
/*      */     
/* 1091 */     Comparator<? super E> comparator = (Comparator<? super E>)Objects.<Object>requireNonNull(stream.readObject());
/* 1092 */     Serialization.<AbstractSortedMultiset>getFieldSetter(AbstractSortedMultiset.class, "comparator").set(this, comparator);
/* 1093 */     Serialization.<TreeMultiset<E>>getFieldSetter((Class)TreeMultiset.class, "range")
/* 1094 */       .set(this, GeneralRange.all(comparator));
/* 1095 */     Serialization.<TreeMultiset<E>>getFieldSetter((Class)TreeMultiset.class, "rootReference")
/* 1096 */       .set(this, new Reference());
/* 1097 */     AvlNode<E> header = new AvlNode<>();
/* 1098 */     Serialization.<TreeMultiset<E>>getFieldSetter((Class)TreeMultiset.class, "header").set(this, header);
/* 1099 */     successor(header, header);
/* 1100 */     Serialization.populateMultiset(this, stream);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/TreeMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */