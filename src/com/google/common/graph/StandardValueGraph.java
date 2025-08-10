/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ class StandardValueGraph<N, V>
/*     */   extends AbstractValueGraph<N, V>
/*     */ {
/*     */   private final boolean isDirected;
/*     */   private final boolean allowsSelfLoops;
/*     */   private final ElementOrder<N> nodeOrder;
/*     */   final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
/*     */   long edgeCount;
/*     */   
/*     */   StandardValueGraph(AbstractGraphBuilder<? super N> builder) {
/*  58 */     this(builder, builder.nodeOrder
/*     */         
/*  60 */         .createMap(((Integer)builder.expectedNodeCount
/*  61 */           .or(Integer.valueOf(10))).intValue()), 0L);
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
/*     */   StandardValueGraph(AbstractGraphBuilder<? super N> builder, Map<N, GraphConnections<N, V>> nodeConnections, long edgeCount) {
/*  73 */     this.isDirected = builder.directed;
/*  74 */     this.allowsSelfLoops = builder.allowsSelfLoops;
/*  75 */     this.nodeOrder = builder.nodeOrder.cast();
/*     */     
/*  77 */     this
/*     */ 
/*     */       
/*  80 */       .nodeConnections = (nodeConnections instanceof java.util.TreeMap) ? new MapRetrievalCache<>(nodeConnections) : new MapIteratorCache<>(nodeConnections);
/*  81 */     this.edgeCount = Graphs.checkNonNegative(edgeCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> nodes() {
/*  86 */     return this.nodeConnections.unmodifiableKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirected() {
/*  91 */     return this.isDirected;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowsSelfLoops() {
/*  96 */     return this.allowsSelfLoops;
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> nodeOrder() {
/* 101 */     return this.nodeOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes(N node) {
/* 106 */     return checkedConnections(node).adjacentNodes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> predecessors(N node) {
/* 111 */     return checkedConnections(node).predecessors();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors(N node) {
/* 116 */     return checkedConnections(node).successors();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<EndpointPair<N>> incidentEdges(N node) {
/* 121 */     final GraphConnections<N, V> connections = checkedConnections(node);
/*     */     
/* 123 */     return new IncidentEdgeSet<N>(this, this, node)
/*     */       {
/*     */         public Iterator<EndpointPair<N>> iterator() {
/* 126 */           return connections.incidentEdgeIterator(this.node);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 133 */     return hasEdgeConnectingInternal((N)Preconditions.checkNotNull(nodeU), (N)Preconditions.checkNotNull(nodeV));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 138 */     Preconditions.checkNotNull(endpoints);
/* 139 */     return (isOrderingCompatible(endpoints) && 
/* 140 */       hasEdgeConnectingInternal(endpoints.nodeU(), endpoints.nodeV()));
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V edgeValueOrDefault(N nodeU, N nodeV, @CheckForNull V defaultValue) {
/* 146 */     return edgeValueOrDefaultInternal((N)Preconditions.checkNotNull(nodeU), (N)Preconditions.checkNotNull(nodeV), defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V edgeValueOrDefault(EndpointPair<N> endpoints, @CheckForNull V defaultValue) {
/* 152 */     validateEndpoints(endpoints);
/* 153 */     return edgeValueOrDefaultInternal(endpoints.nodeU(), endpoints.nodeV(), defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   protected long edgeCount() {
/* 158 */     return this.edgeCount;
/*     */   }
/*     */   
/*     */   private final GraphConnections<N, V> checkedConnections(N node) {
/* 162 */     GraphConnections<N, V> connections = this.nodeConnections.get(node);
/* 163 */     if (connections == null) {
/* 164 */       Preconditions.checkNotNull(node);
/* 165 */       throw new IllegalArgumentException("Node " + node + " is not an element of this graph.");
/*     */     } 
/* 167 */     return connections;
/*     */   }
/*     */   
/*     */   final boolean containsNode(@CheckForNull N node) {
/* 171 */     return this.nodeConnections.containsKey(node);
/*     */   }
/*     */   
/*     */   private final boolean hasEdgeConnectingInternal(N nodeU, N nodeV) {
/* 175 */     GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
/* 176 */     return (connectionsU != null && connectionsU.successors().contains(nodeV));
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private final V edgeValueOrDefaultInternal(N nodeU, N nodeV, @CheckForNull V defaultValue) {
/* 181 */     GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
/* 182 */     V value = (connectionsU == null) ? null : connectionsU.value(nodeV);
/*     */     
/* 184 */     if (value == null) {
/* 185 */       return defaultValue;
/*     */     }
/* 187 */     return value;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/StandardValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */