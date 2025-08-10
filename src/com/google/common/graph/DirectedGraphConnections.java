/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ final class DirectedGraphConnections<N, V>
/*     */   implements GraphConnections<N, V>
/*     */ {
/*     */   private static final class PredAndSucc
/*     */   {
/*     */     private final Object successorValue;
/*     */     
/*     */     PredAndSucc(Object successorValue) {
/*  63 */       this.successorValue = successorValue;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class NodeConnection<N>
/*     */   {
/*     */     final N node;
/*     */ 
/*     */ 
/*     */     
/*     */     NodeConnection(N node) {
/*  77 */       this.node = (N)Preconditions.checkNotNull(node);
/*     */     }
/*     */     
/*     */     static final class Pred<N> extends NodeConnection<N> {
/*     */       Pred(N node) {
/*  82 */         super(node);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean equals(@CheckForNull Object that) {
/*  87 */         if (that instanceof Pred) {
/*  88 */           return this.node.equals(((Pred)that).node);
/*     */         }
/*  90 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public int hashCode() {
/*  97 */         return Pred.class.hashCode() + this.node.hashCode();
/*     */       }
/*     */     }
/*     */     
/*     */     static final class Succ<N> extends NodeConnection<N> {
/*     */       Succ(N node) {
/* 103 */         super(node);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean equals(@CheckForNull Object that) {
/* 108 */         if (that instanceof Succ) {
/* 109 */           return this.node.equals(((Succ)that).node);
/*     */         }
/* 111 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public int hashCode()
/*     */       {
/* 118 */         return Succ.class.hashCode() + this.node.hashCode(); } } } static final class Pred<N> extends NodeConnection<N> { Pred(N node) { super(node); } public boolean equals(@CheckForNull Object that) { if (that instanceof Pred) return this.node.equals(((Pred)that).node);  return false; } public int hashCode() { return Pred.class.hashCode() + this.node.hashCode(); } } static final class Succ<N> extends NodeConnection<N> { public int hashCode() { return Succ.class.hashCode() + this.node.hashCode(); } Succ(N node) { super(node); } public boolean equals(@CheckForNull Object that) {
/*     */       if (that instanceof Succ)
/*     */         return this.node.equals(((Succ)that).node); 
/*     */       return false;
/*     */     } }
/* 123 */    private static final Object PRED = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<N, Object> adjacentNodeValues;
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   private final List<NodeConnection<N>> orderedNodeConnections;
/*     */ 
/*     */ 
/*     */   
/*     */   private int predecessorCount;
/*     */ 
/*     */ 
/*     */   
/*     */   private int successorCount;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DirectedGraphConnections(Map<N, Object> adjacentNodeValues, @CheckForNull List<NodeConnection<N>> orderedNodeConnections, int predecessorCount, int successorCount) {
/* 147 */     this.adjacentNodeValues = (Map<N, Object>)Preconditions.checkNotNull(adjacentNodeValues);
/* 148 */     this.orderedNodeConnections = orderedNodeConnections;
/* 149 */     this.predecessorCount = Graphs.checkNonNegative(predecessorCount);
/* 150 */     this.successorCount = Graphs.checkNonNegative(successorCount);
/* 151 */     Preconditions.checkState((predecessorCount <= adjacentNodeValues
/* 152 */         .size() && successorCount <= adjacentNodeValues
/* 153 */         .size()));
/*     */   }
/*     */   
/*     */   static <N, V> DirectedGraphConnections<N, V> of(ElementOrder<N> incidentEdgeOrder) {
/*     */     List<NodeConnection<N>> orderedNodeConnections;
/* 158 */     int initialCapacity = 4;
/*     */ 
/*     */     
/* 161 */     switch (incidentEdgeOrder.type()) {
/*     */       case UNORDERED:
/* 163 */         orderedNodeConnections = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 172 */         return new DirectedGraphConnections<>(new HashMap<>(initialCapacity, 1.0F), orderedNodeConnections, 0, 0);case STABLE: orderedNodeConnections = new ArrayList<>(); return new DirectedGraphConnections<>(new HashMap<>(initialCapacity, 1.0F), orderedNodeConnections, 0, 0);
/*     */     } 
/*     */     throw new AssertionError(incidentEdgeOrder.type());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <N, V> DirectedGraphConnections<N, V> ofImmutable(N thisNode, Iterable<EndpointPair<N>> incidentEdges, Function<N, V> successorNodeToValueFn) {
/* 181 */     Preconditions.checkNotNull(thisNode);
/* 182 */     Preconditions.checkNotNull(successorNodeToValueFn);
/*     */     
/* 184 */     Map<N, Object> adjacentNodeValues = new HashMap<>();
/*     */     
/* 186 */     ImmutableList.Builder<NodeConnection<N>> orderedNodeConnectionsBuilder = ImmutableList.builder();
/* 187 */     int predecessorCount = 0;
/* 188 */     int successorCount = 0;
/*     */     
/* 190 */     for (EndpointPair<N> incidentEdge : incidentEdges) {
/* 191 */       if (incidentEdge.nodeU().equals(thisNode) && incidentEdge.nodeV().equals(thisNode)) {
/*     */ 
/*     */         
/* 194 */         adjacentNodeValues.put(thisNode, new PredAndSucc(successorNodeToValueFn.apply(thisNode)));
/*     */         
/* 196 */         orderedNodeConnectionsBuilder.add(new NodeConnection.Pred<>(thisNode));
/* 197 */         orderedNodeConnectionsBuilder.add(new NodeConnection.Succ<>(thisNode));
/* 198 */         predecessorCount++;
/* 199 */         successorCount++; continue;
/* 200 */       }  if (incidentEdge.nodeV().equals(thisNode)) {
/* 201 */         N predecessor = incidentEdge.nodeU();
/*     */         
/* 203 */         Object object = adjacentNodeValues.put(predecessor, PRED);
/* 204 */         if (object != null) {
/* 205 */           adjacentNodeValues.put(predecessor, new PredAndSucc(object));
/*     */         }
/*     */         
/* 208 */         orderedNodeConnectionsBuilder.add(new NodeConnection.Pred<>(predecessor));
/* 209 */         predecessorCount++; continue;
/*     */       } 
/* 211 */       Preconditions.checkArgument(incidentEdge.nodeU().equals(thisNode));
/*     */       
/* 213 */       N successor = incidentEdge.nodeV();
/* 214 */       V value = (V)successorNodeToValueFn.apply(successor);
/*     */       
/* 216 */       Object existingValue = adjacentNodeValues.put(successor, value);
/* 217 */       if (existingValue != null) {
/* 218 */         Preconditions.checkArgument((existingValue == PRED));
/* 219 */         adjacentNodeValues.put(successor, new PredAndSucc(value));
/*     */       } 
/*     */       
/* 222 */       orderedNodeConnectionsBuilder.add(new NodeConnection.Succ<>(successor));
/* 223 */       successorCount++;
/*     */     } 
/*     */ 
/*     */     
/* 227 */     return new DirectedGraphConnections<>(adjacentNodeValues, (List<NodeConnection<N>>)orderedNodeConnectionsBuilder
/*     */         
/* 229 */         .build(), predecessorCount, successorCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes() {
/* 236 */     if (this.orderedNodeConnections == null) {
/* 237 */       return Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
/*     */     }
/* 239 */     return new AbstractSet<N>()
/*     */       {
/*     */         public UnmodifiableIterator<N> iterator() {
/* 242 */           final Iterator<DirectedGraphConnections.NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
/* 243 */           final Set<N> seenNodes = new HashSet<>();
/* 244 */           return (UnmodifiableIterator<N>)new AbstractIterator<N>(this)
/*     */             {
/*     */               @CheckForNull
/*     */               protected N computeNext() {
/* 248 */                 while (nodeConnections.hasNext()) {
/* 249 */                   DirectedGraphConnections.NodeConnection<N> nodeConnection = nodeConnections.next();
/* 250 */                   boolean added = seenNodes.add(nodeConnection.node);
/* 251 */                   if (added) {
/* 252 */                     return nodeConnection.node;
/*     */                   }
/*     */                 } 
/* 255 */                 return (N)endOfData();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 262 */           return DirectedGraphConnections.this.adjacentNodeValues.size();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(@CheckForNull Object obj) {
/* 267 */           return DirectedGraphConnections.this.adjacentNodeValues.containsKey(obj);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<N> predecessors() {
/* 275 */     return new AbstractSet<N>()
/*     */       {
/*     */         public UnmodifiableIterator<N> iterator() {
/* 278 */           if (DirectedGraphConnections.this.orderedNodeConnections == null) {
/* 279 */             final Iterator<Map.Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
/* 280 */             return (UnmodifiableIterator<N>)new AbstractIterator<N>(this)
/*     */               {
/*     */                 @CheckForNull
/*     */                 protected N computeNext() {
/* 284 */                   while (entries.hasNext()) {
/* 285 */                     Map.Entry<N, Object> entry = entries.next();
/* 286 */                     if (DirectedGraphConnections.isPredecessor(entry.getValue())) {
/* 287 */                       return entry.getKey();
/*     */                     }
/*     */                   } 
/* 290 */                   return (N)endOfData();
/*     */                 }
/*     */               };
/*     */           } 
/* 294 */           final Iterator<DirectedGraphConnections.NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
/* 295 */           return (UnmodifiableIterator<N>)new AbstractIterator<N>(this)
/*     */             {
/*     */               @CheckForNull
/*     */               protected N computeNext() {
/* 299 */                 while (nodeConnections.hasNext()) {
/* 300 */                   DirectedGraphConnections.NodeConnection<N> nodeConnection = nodeConnections.next();
/* 301 */                   if (nodeConnection instanceof DirectedGraphConnections.NodeConnection.Pred) {
/* 302 */                     return nodeConnection.node;
/*     */                   }
/*     */                 } 
/* 305 */                 return (N)endOfData();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public int size() {
/* 313 */           return DirectedGraphConnections.this.predecessorCount;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(@CheckForNull Object obj) {
/* 318 */           return DirectedGraphConnections.isPredecessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors() {
/* 325 */     return new AbstractSet<N>()
/*     */       {
/*     */         public UnmodifiableIterator<N> iterator() {
/* 328 */           if (DirectedGraphConnections.this.orderedNodeConnections == null) {
/* 329 */             final Iterator<Map.Entry<N, Object>> entries = DirectedGraphConnections.this.adjacentNodeValues.entrySet().iterator();
/* 330 */             return (UnmodifiableIterator<N>)new AbstractIterator<N>(this)
/*     */               {
/*     */                 @CheckForNull
/*     */                 protected N computeNext() {
/* 334 */                   while (entries.hasNext()) {
/* 335 */                     Map.Entry<N, Object> entry = entries.next();
/* 336 */                     if (DirectedGraphConnections.isSuccessor(entry.getValue())) {
/* 337 */                       return entry.getKey();
/*     */                     }
/*     */                   } 
/* 340 */                   return (N)endOfData();
/*     */                 }
/*     */               };
/*     */           } 
/* 344 */           final Iterator<DirectedGraphConnections.NodeConnection<N>> nodeConnections = DirectedGraphConnections.this.orderedNodeConnections.iterator();
/* 345 */           return (UnmodifiableIterator<N>)new AbstractIterator<N>(this)
/*     */             {
/*     */               @CheckForNull
/*     */               protected N computeNext() {
/* 349 */                 while (nodeConnections.hasNext()) {
/* 350 */                   DirectedGraphConnections.NodeConnection<N> nodeConnection = nodeConnections.next();
/* 351 */                   if (nodeConnection instanceof DirectedGraphConnections.NodeConnection.Succ) {
/* 352 */                     return nodeConnection.node;
/*     */                   }
/*     */                 } 
/* 355 */                 return (N)endOfData();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public int size() {
/* 363 */           return DirectedGraphConnections.this.successorCount;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(@CheckForNull Object obj) {
/* 368 */           return DirectedGraphConnections.isSuccessor(DirectedGraphConnections.this.adjacentNodeValues.get(obj));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Iterator<EndpointPair<N>> incidentEdgeIterator(N thisNode) {
/*     */     final Iterator<EndpointPair<N>> resultWithDoubleSelfLoop;
/* 375 */     Preconditions.checkNotNull(thisNode);
/*     */ 
/*     */     
/* 378 */     if (this.orderedNodeConnections == null) {
/*     */       
/* 380 */       resultWithDoubleSelfLoop = Iterators.concat(
/* 381 */           Iterators.transform(
/* 382 */             predecessors().iterator(), predecessor -> EndpointPair.ordered(predecessor, thisNode)), 
/*     */           
/* 384 */           Iterators.transform(
/* 385 */             successors().iterator(), successor -> EndpointPair.ordered(thisNode, successor)));
/*     */     }
/*     */     else {
/*     */       
/* 389 */       resultWithDoubleSelfLoop = Iterators.transform(this.orderedNodeConnections
/* 390 */           .iterator(), connection -> (connection instanceof NodeConnection.Succ) ? EndpointPair.ordered(thisNode, connection.node) : EndpointPair.ordered(connection.node, (N)thisNode));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 400 */     final AtomicBoolean alreadySeenSelfLoop = new AtomicBoolean(false);
/* 401 */     return (Iterator<EndpointPair<N>>)new AbstractIterator<EndpointPair<N>>(this)
/*     */       {
/*     */         @CheckForNull
/*     */         protected EndpointPair<N> computeNext() {
/* 405 */           while (resultWithDoubleSelfLoop.hasNext()) {
/* 406 */             EndpointPair<N> edge = resultWithDoubleSelfLoop.next();
/* 407 */             if (edge.nodeU().equals(edge.nodeV())) {
/* 408 */               if (!alreadySeenSelfLoop.getAndSet(true))
/* 409 */                 return edge; 
/*     */               continue;
/*     */             } 
/* 412 */             return edge;
/*     */           } 
/*     */           
/* 415 */           return (EndpointPair<N>)endOfData();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V value(N node) {
/* 424 */     Preconditions.checkNotNull(node);
/* 425 */     Object value = this.adjacentNodeValues.get(node);
/* 426 */     if (value == PRED) {
/* 427 */       return null;
/*     */     }
/* 429 */     if (value instanceof PredAndSucc) {
/* 430 */       return (V)((PredAndSucc)value).successorValue;
/*     */     }
/* 432 */     return (V)value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removePredecessor(N node) {
/*     */     boolean removedPredecessor;
/* 438 */     Preconditions.checkNotNull(node);
/*     */     
/* 440 */     Object previousValue = this.adjacentNodeValues.get(node);
/*     */ 
/*     */     
/* 443 */     if (previousValue == PRED) {
/* 444 */       this.adjacentNodeValues.remove(node);
/* 445 */       removedPredecessor = true;
/* 446 */     } else if (previousValue instanceof PredAndSucc) {
/* 447 */       this.adjacentNodeValues.put(node, ((PredAndSucc)previousValue).successorValue);
/* 448 */       removedPredecessor = true;
/*     */     } else {
/* 450 */       removedPredecessor = false;
/*     */     } 
/*     */     
/* 453 */     if (removedPredecessor) {
/* 454 */       Graphs.checkNonNegative(--this.predecessorCount);
/*     */       
/* 456 */       if (this.orderedNodeConnections != null) {
/* 457 */         this.orderedNodeConnections.remove(new NodeConnection.Pred<>(node));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V removeSuccessor(Object node) {
/*     */     Object removedValue;
/* 466 */     Preconditions.checkNotNull(node);
/* 467 */     Object previousValue = this.adjacentNodeValues.get(node);
/*     */ 
/*     */     
/* 470 */     if (previousValue == null || previousValue == PRED) {
/* 471 */       removedValue = null;
/* 472 */     } else if (previousValue instanceof PredAndSucc) {
/* 473 */       this.adjacentNodeValues.put((N)node, PRED);
/* 474 */       removedValue = ((PredAndSucc)previousValue).successorValue;
/*     */     } else {
/* 476 */       this.adjacentNodeValues.remove(node);
/* 477 */       removedValue = previousValue;
/*     */     } 
/*     */     
/* 480 */     if (removedValue != null) {
/* 481 */       Graphs.checkNonNegative(--this.successorCount);
/*     */       
/* 483 */       if (this.orderedNodeConnections != null) {
/* 484 */         this.orderedNodeConnections.remove(new NodeConnection.Succ(node));
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 495 */     return (removedValue == null) ? null : (V)removedValue;
/*     */   }
/*     */   
/*     */   public void addPredecessor(N node, V unused) {
/*     */     boolean addedPredecessor;
/* 500 */     Object previousValue = this.adjacentNodeValues.put(node, PRED);
/*     */ 
/*     */     
/* 503 */     if (previousValue == null) {
/* 504 */       addedPredecessor = true;
/* 505 */     } else if (previousValue instanceof PredAndSucc) {
/*     */       
/* 507 */       this.adjacentNodeValues.put(node, previousValue);
/* 508 */       addedPredecessor = false;
/* 509 */     } else if (previousValue != PRED) {
/*     */       
/* 511 */       this.adjacentNodeValues.put(node, new PredAndSucc(previousValue));
/* 512 */       addedPredecessor = true;
/*     */     } else {
/* 514 */       addedPredecessor = false;
/*     */     } 
/*     */     
/* 517 */     if (addedPredecessor) {
/* 518 */       Graphs.checkPositive(++this.predecessorCount);
/*     */       
/* 520 */       if (this.orderedNodeConnections != null) {
/* 521 */         this.orderedNodeConnections.add(new NodeConnection.Pred<>(node));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V addSuccessor(N node, V value) {
/* 530 */     Object previousSuccessor, previousValue = this.adjacentNodeValues.put(node, value);
/*     */ 
/*     */     
/* 533 */     if (previousValue == null) {
/* 534 */       previousSuccessor = null;
/* 535 */     } else if (previousValue instanceof PredAndSucc) {
/* 536 */       this.adjacentNodeValues.put(node, new PredAndSucc(value));
/* 537 */       previousSuccessor = ((PredAndSucc)previousValue).successorValue;
/* 538 */     } else if (previousValue == PRED) {
/* 539 */       this.adjacentNodeValues.put(node, new PredAndSucc(value));
/* 540 */       previousSuccessor = null;
/*     */     } else {
/* 542 */       previousSuccessor = previousValue;
/*     */     } 
/*     */     
/* 545 */     if (previousSuccessor == null) {
/* 546 */       Graphs.checkPositive(++this.successorCount);
/*     */       
/* 548 */       if (this.orderedNodeConnections != null) {
/* 549 */         this.orderedNodeConnections.add(new NodeConnection.Succ<>(node));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 554 */     return (previousSuccessor == null) ? null : (V)previousSuccessor;
/*     */   }
/*     */   
/*     */   private static boolean isPredecessor(@CheckForNull Object value) {
/* 558 */     return (value == PRED || value instanceof PredAndSucc);
/*     */   }
/*     */   
/*     */   private static boolean isSuccessor(@CheckForNull Object value) {
/* 562 */     return (value != PRED && value != null);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/DirectedGraphConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */