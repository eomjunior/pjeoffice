/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.DoNotMock;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
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
/*     */ @DoNotMock("Call forGraph or forTree, passing a lambda or a Graph with the desired edges (built with GraphBuilder)")
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @Beta
/*     */ public abstract class Traverser<N>
/*     */ {
/*     */   private final SuccessorsFunction<N> successorFunction;
/*     */   
/*     */   private Traverser(SuccessorsFunction<N> successorFunction) {
/*  72 */     this.successorFunction = (SuccessorsFunction<N>)Preconditions.checkNotNull(successorFunction);
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
/*     */   public static <N> Traverser<N> forGraph(final SuccessorsFunction<N> graph) {
/* 100 */     return new Traverser<N>(graph)
/*     */       {
/*     */         Traverser.Traversal<N> newTraversal() {
/* 103 */           return Traverser.Traversal.inGraph(graph);
/*     */         }
/*     */       };
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
/*     */   public static <N> Traverser<N> forTree(final SuccessorsFunction<N> tree) {
/* 182 */     if (tree instanceof BaseGraph) {
/* 183 */       Preconditions.checkArgument(((BaseGraph)tree).isDirected(), "Undirected graphs can never be trees.");
/*     */     }
/* 185 */     if (tree instanceof Network) {
/* 186 */       Preconditions.checkArgument(((Network)tree).isDirected(), "Undirected networks can never be trees.");
/*     */     }
/* 188 */     return new Traverser<N>(tree)
/*     */       {
/*     */         Traverser.Traversal<N> newTraversal() {
/* 191 */           return Traverser.Traversal.inTree(tree);
/*     */         }
/*     */       };
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
/*     */   public final Iterable<N> breadthFirst(N startNode) {
/* 228 */     return breadthFirst((Iterable<? extends N>)ImmutableSet.of(startNode));
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
/*     */   public final Iterable<N> breadthFirst(Iterable<? extends N> startNodes) {
/* 242 */     final ImmutableSet<N> validated = validate(startNodes);
/* 243 */     return new Iterable<N>()
/*     */       {
/*     */         public Iterator<N> iterator() {
/* 246 */           return Traverser.this.newTraversal().breadthFirst((Iterator<? extends N>)validated.iterator());
/*     */         }
/*     */       };
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
/*     */   public final Iterable<N> depthFirstPreOrder(N startNode) {
/* 283 */     return depthFirstPreOrder((Iterable<? extends N>)ImmutableSet.of(startNode));
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
/*     */   public final Iterable<N> depthFirstPreOrder(Iterable<? extends N> startNodes) {
/* 297 */     final ImmutableSet<N> validated = validate(startNodes);
/* 298 */     return new Iterable<N>()
/*     */       {
/*     */         public Iterator<N> iterator() {
/* 301 */           return Traverser.this.newTraversal().preOrder((Iterator<? extends N>)validated.iterator());
/*     */         }
/*     */       };
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
/*     */   public final Iterable<N> depthFirstPostOrder(N startNode) {
/* 338 */     return depthFirstPostOrder((Iterable<? extends N>)ImmutableSet.of(startNode));
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
/*     */   public final Iterable<N> depthFirstPostOrder(Iterable<? extends N> startNodes) {
/* 352 */     final ImmutableSet<N> validated = validate(startNodes);
/* 353 */     return new Iterable<N>()
/*     */       {
/*     */         public Iterator<N> iterator() {
/* 356 */           return Traverser.this.newTraversal().postOrder((Iterator<? extends N>)validated.iterator());
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableSet<N> validate(Iterable<? extends N> startNodes) {
/* 365 */     ImmutableSet<N> copy = ImmutableSet.copyOf(startNodes);
/* 366 */     for (UnmodifiableIterator<N> unmodifiableIterator = copy.iterator(); unmodifiableIterator.hasNext(); ) { N node = unmodifiableIterator.next();
/* 367 */       this.successorFunction.successors(node); }
/*     */     
/* 369 */     return copy;
/*     */   }
/*     */ 
/*     */   
/*     */   abstract Traversal<N> newTraversal();
/*     */ 
/*     */   
/*     */   private static abstract class Traversal<N>
/*     */   {
/*     */     final SuccessorsFunction<N> successorFunction;
/*     */     
/*     */     Traversal(SuccessorsFunction<N> successorFunction) {
/* 381 */       this.successorFunction = successorFunction;
/*     */     }
/*     */     
/*     */     static <N> Traversal<N> inGraph(SuccessorsFunction<N> graph) {
/* 385 */       final Set<N> visited = new HashSet<>();
/* 386 */       return new Traversal<N>(graph)
/*     */         {
/*     */           @CheckForNull
/*     */           N visitNext(Deque<Iterator<? extends N>> horizon) {
/* 390 */             Iterator<? extends N> top = horizon.getFirst();
/* 391 */             while (top.hasNext()) {
/* 392 */               N element = top.next();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 401 */               Objects.requireNonNull(element);
/* 402 */               if (visited.add(element)) {
/* 403 */                 return element;
/*     */               }
/*     */             } 
/* 406 */             horizon.removeFirst();
/* 407 */             return null;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     static <N> Traversal<N> inTree(SuccessorsFunction<N> tree) {
/* 413 */       return new Traversal<N>(tree)
/*     */         {
/*     */           @CheckForNull
/*     */           N visitNext(Deque<Iterator<? extends N>> horizon) {
/* 417 */             Iterator<? extends N> top = horizon.getFirst();
/* 418 */             if (top.hasNext()) {
/* 419 */               return (N)Preconditions.checkNotNull(top.next());
/*     */             }
/* 421 */             horizon.removeFirst();
/* 422 */             return null;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     final Iterator<N> breadthFirst(Iterator<? extends N> startNodes) {
/* 428 */       return topDown(startNodes, Traverser.InsertionOrder.BACK);
/*     */     }
/*     */     
/*     */     final Iterator<N> preOrder(Iterator<? extends N> startNodes) {
/* 432 */       return topDown(startNodes, Traverser.InsertionOrder.FRONT);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Iterator<N> topDown(Iterator<? extends N> startNodes, final Traverser.InsertionOrder order) {
/* 442 */       final Deque<Iterator<? extends N>> horizon = new ArrayDeque<>();
/* 443 */       horizon.add(startNodes);
/* 444 */       return (Iterator<N>)new AbstractIterator<N>()
/*     */         {
/*     */           @CheckForNull
/*     */           protected N computeNext() {
/*     */             while (true) {
/* 449 */               N next = Traverser.Traversal.this.visitNext(horizon);
/* 450 */               if (next != null) {
/* 451 */                 Iterator<? extends N> successors = Traverser.Traversal.this.successorFunction.successors(next).iterator();
/* 452 */                 if (successors.hasNext())
/*     */                 {
/*     */                   
/* 455 */                   order.insertInto(horizon, successors);
/*     */                 }
/* 457 */                 return next;
/*     */               } 
/* 459 */               if (horizon.isEmpty())
/* 460 */                 return (N)endOfData(); 
/*     */             } 
/*     */           }
/*     */         };
/*     */     }
/*     */     final Iterator<N> postOrder(Iterator<? extends N> startNodes) {
/* 466 */       final Deque<N> ancestorStack = new ArrayDeque<>();
/* 467 */       final Deque<Iterator<? extends N>> horizon = new ArrayDeque<>();
/* 468 */       horizon.add(startNodes);
/* 469 */       return (Iterator<N>)new AbstractIterator<N>()
/*     */         {
/*     */           @CheckForNull
/*     */           protected N computeNext() {
/* 473 */             for (N next = Traverser.Traversal.this.visitNext(horizon); next != null; next = Traverser.Traversal.this.visitNext(horizon)) {
/* 474 */               Iterator<? extends N> successors = Traverser.Traversal.this.successorFunction.successors(next).iterator();
/* 475 */               if (!successors.hasNext()) {
/* 476 */                 return next;
/*     */               }
/* 478 */               horizon.addFirst(successors);
/* 479 */               ancestorStack.push(next);
/*     */             } 
/*     */             
/* 482 */             if (!ancestorStack.isEmpty()) {
/* 483 */               return ancestorStack.pop();
/*     */             }
/* 485 */             return (N)endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     abstract N visitNext(Deque<Iterator<? extends N>> param1Deque);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum InsertionOrder
/*     */   {
/* 506 */     FRONT
/*     */     {
/*     */       <T> void insertInto(Deque<T> deque, T value) {
/* 509 */         deque.addFirst(value);
/*     */       }
/*     */     },
/* 512 */     BACK
/*     */     {
/*     */       <T> void insertInto(Deque<T> deque, T value) {
/* 515 */         deque.addLast(value);
/*     */       }
/*     */     };
/*     */     
/*     */     abstract <T> void insertInto(Deque<T> param1Deque, T param1T);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/Traverser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */