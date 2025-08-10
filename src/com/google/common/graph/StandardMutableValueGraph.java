/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Objects;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ final class StandardMutableValueGraph<N, V>
/*     */   extends StandardValueGraph<N, V>
/*     */   implements MutableValueGraph<N, V>
/*     */ {
/*     */   private final ElementOrder<N> incidentEdgeOrder;
/*     */   
/*     */   StandardMutableValueGraph(AbstractGraphBuilder<? super N> builder) {
/*  52 */     super(builder);
/*  53 */     this.incidentEdgeOrder = builder.incidentEdgeOrder.cast();
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> incidentEdgeOrder() {
/*  58 */     return this.incidentEdgeOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addNode(N node) {
/*  64 */     Preconditions.checkNotNull(node, "node");
/*     */     
/*  66 */     if (containsNode(node)) {
/*  67 */       return false;
/*     */     }
/*     */     
/*  70 */     addNodeInternal(node);
/*  71 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private GraphConnections<N, V> addNodeInternal(N node) {
/*  81 */     GraphConnections<N, V> connections = newConnections();
/*  82 */     Preconditions.checkState((this.nodeConnections.put(node, connections) == null));
/*  83 */     return connections;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V putEdgeValue(N nodeU, N nodeV, V value) {
/*  90 */     Preconditions.checkNotNull(nodeU, "nodeU");
/*  91 */     Preconditions.checkNotNull(nodeV, "nodeV");
/*  92 */     Preconditions.checkNotNull(value, "value");
/*     */     
/*  94 */     if (!allowsSelfLoops()) {
/*  95 */       Preconditions.checkArgument(!nodeU.equals(nodeV), "Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph that allows self-loops, call allowsSelfLoops(true) on the Builder.", nodeU);
/*     */     }
/*     */     
/*  98 */     GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
/*  99 */     if (connectionsU == null) {
/* 100 */       connectionsU = addNodeInternal(nodeU);
/*     */     }
/* 102 */     V previousValue = connectionsU.addSuccessor(nodeV, value);
/* 103 */     GraphConnections<N, V> connectionsV = this.nodeConnections.get(nodeV);
/* 104 */     if (connectionsV == null) {
/* 105 */       connectionsV = addNodeInternal(nodeV);
/*     */     }
/* 107 */     connectionsV.addPredecessor(nodeU, value);
/* 108 */     if (previousValue == null) {
/* 109 */       Graphs.checkPositive(++this.edgeCount);
/*     */     }
/* 111 */     return previousValue;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V putEdgeValue(EndpointPair<N> endpoints, V value) {
/* 118 */     validateEndpoints(endpoints);
/* 119 */     return putEdgeValue(endpoints.nodeU(), endpoints.nodeV(), value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeNode(N node) {
/* 125 */     Preconditions.checkNotNull(node, "node");
/*     */     
/* 127 */     GraphConnections<N, V> connections = this.nodeConnections.get(node);
/* 128 */     if (connections == null) {
/* 129 */       return false;
/*     */     }
/*     */     
/* 132 */     if (allowsSelfLoops())
/*     */     {
/* 134 */       if (connections.removeSuccessor(node) != null) {
/* 135 */         connections.removePredecessor(node);
/* 136 */         this.edgeCount--;
/*     */       } 
/*     */     }
/*     */     UnmodifiableIterator<N> unmodifiableIterator;
/* 140 */     for (unmodifiableIterator = ImmutableList.copyOf(connections.successors()).iterator(); unmodifiableIterator.hasNext(); ) { N successor = unmodifiableIterator.next();
/*     */       
/* 142 */       ((GraphConnections)Objects.<GraphConnections>requireNonNull(this.nodeConnections.getWithoutCaching(successor))).removePredecessor(node);
/* 143 */       Objects.requireNonNull(connections.removeSuccessor(successor));
/* 144 */       this.edgeCount--; }
/*     */     
/* 146 */     if (isDirected())
/*     */     {
/*     */       
/* 149 */       for (unmodifiableIterator = ImmutableList.copyOf(connections.predecessors()).iterator(); unmodifiableIterator.hasNext(); ) { N predecessor = unmodifiableIterator.next();
/*     */         
/* 151 */         Preconditions.checkState(
/* 152 */             (((GraphConnections)Objects.<GraphConnections>requireNonNull(this.nodeConnections.getWithoutCaching(predecessor))).removeSuccessor(node) != null));
/*     */         
/* 154 */         connections.removePredecessor(predecessor);
/* 155 */         this.edgeCount--; }
/*     */     
/*     */     }
/* 158 */     this.nodeConnections.remove(node);
/* 159 */     Graphs.checkNonNegative(this.edgeCount);
/* 160 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V removeEdge(N nodeU, N nodeV) {
/* 167 */     Preconditions.checkNotNull(nodeU, "nodeU");
/* 168 */     Preconditions.checkNotNull(nodeV, "nodeV");
/*     */     
/* 170 */     GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
/* 171 */     GraphConnections<N, V> connectionsV = this.nodeConnections.get(nodeV);
/* 172 */     if (connectionsU == null || connectionsV == null) {
/* 173 */       return null;
/*     */     }
/*     */     
/* 176 */     V previousValue = connectionsU.removeSuccessor(nodeV);
/* 177 */     if (previousValue != null) {
/* 178 */       connectionsV.removePredecessor(nodeU);
/* 179 */       Graphs.checkNonNegative(--this.edgeCount);
/*     */     } 
/* 181 */     return previousValue;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public V removeEdge(EndpointPair<N> endpoints) {
/* 188 */     validateEndpoints(endpoints);
/* 189 */     return removeEdge(endpoints.nodeU(), endpoints.nodeV());
/*     */   }
/*     */   
/*     */   private GraphConnections<N, V> newConnections() {
/* 193 */     return isDirected() ? 
/* 194 */       DirectedGraphConnections.<N, V>of(this.incidentEdgeOrder) : 
/* 195 */       UndirectedGraphConnections.<N, V>of(this.incidentEdgeOrder);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/StandardMutableValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */