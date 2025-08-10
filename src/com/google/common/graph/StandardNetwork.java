/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class StandardNetwork<N, E>
/*     */   extends AbstractNetwork<N, E>
/*     */ {
/*     */   private final boolean isDirected;
/*     */   private final boolean allowsParallelEdges;
/*     */   private final boolean allowsSelfLoops;
/*     */   private final ElementOrder<N> nodeOrder;
/*     */   private final ElementOrder<E> edgeOrder;
/*     */   final MapIteratorCache<N, NetworkConnections<N, E>> nodeConnections;
/*     */   final MapIteratorCache<E, N> edgeToReferenceNode;
/*     */   
/*     */   StandardNetwork(NetworkBuilder<? super N, ? super E> builder) {
/*  67 */     this(builder, builder.nodeOrder
/*     */         
/*  69 */         .createMap(((Integer)builder.expectedNodeCount
/*  70 */           .or(Integer.valueOf(10))).intValue()), builder.edgeOrder
/*  71 */         .createMap(((Integer)builder.expectedEdgeCount.or(Integer.valueOf(20))).intValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   StandardNetwork(NetworkBuilder<? super N, ? super E> builder, Map<N, NetworkConnections<N, E>> nodeConnections, Map<E, N> edgeToReferenceNode) {
/*  82 */     this.isDirected = builder.directed;
/*  83 */     this.allowsParallelEdges = builder.allowsParallelEdges;
/*  84 */     this.allowsSelfLoops = builder.allowsSelfLoops;
/*  85 */     this.nodeOrder = builder.nodeOrder.cast();
/*  86 */     this.edgeOrder = builder.edgeOrder.cast();
/*     */ 
/*     */     
/*  89 */     this
/*     */ 
/*     */       
/*  92 */       .nodeConnections = (nodeConnections instanceof java.util.TreeMap) ? new MapRetrievalCache<>(nodeConnections) : new MapIteratorCache<>(nodeConnections);
/*  93 */     this.edgeToReferenceNode = new MapIteratorCache<>(edgeToReferenceNode);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> nodes() {
/*  98 */     return this.nodeConnections.unmodifiableKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edges() {
/* 103 */     return this.edgeToReferenceNode.unmodifiableKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirected() {
/* 108 */     return this.isDirected;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowsParallelEdges() {
/* 113 */     return this.allowsParallelEdges;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowsSelfLoops() {
/* 118 */     return this.allowsSelfLoops;
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> nodeOrder() {
/* 123 */     return this.nodeOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<E> edgeOrder() {
/* 128 */     return this.edgeOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> incidentEdges(N node) {
/* 133 */     return checkedConnections(node).incidentEdges();
/*     */   }
/*     */ 
/*     */   
/*     */   public EndpointPair<N> incidentNodes(E edge) {
/* 138 */     N nodeU = checkedReferenceNode(edge);
/*     */     
/* 140 */     N nodeV = ((NetworkConnections<N, E>)Objects.<NetworkConnections<N, E>>requireNonNull(this.nodeConnections.get(nodeU))).adjacentNode(edge);
/* 141 */     return EndpointPair.of(this, nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes(N node) {
/* 146 */     return checkedConnections(node).adjacentNodes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(N nodeU, N nodeV) {
/* 151 */     NetworkConnections<N, E> connectionsU = checkedConnections(nodeU);
/* 152 */     if (!this.allowsSelfLoops && nodeU == nodeV) {
/* 153 */       return (Set<E>)ImmutableSet.of();
/*     */     }
/* 155 */     Preconditions.checkArgument(containsNode(nodeV), "Node %s is not an element of this graph.", nodeV);
/* 156 */     return connectionsU.edgesConnecting(nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> inEdges(N node) {
/* 161 */     return checkedConnections(node).inEdges();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> outEdges(N node) {
/* 166 */     return checkedConnections(node).outEdges();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> predecessors(N node) {
/* 171 */     return checkedConnections(node).predecessors();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors(N node) {
/* 176 */     return checkedConnections(node).successors();
/*     */   }
/*     */   
/*     */   final NetworkConnections<N, E> checkedConnections(N node) {
/* 180 */     NetworkConnections<N, E> connections = this.nodeConnections.get(node);
/* 181 */     if (connections == null) {
/* 182 */       Preconditions.checkNotNull(node);
/* 183 */       throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", new Object[] { node }));
/*     */     } 
/* 185 */     return connections;
/*     */   }
/*     */   
/*     */   final N checkedReferenceNode(E edge) {
/* 189 */     N referenceNode = this.edgeToReferenceNode.get(edge);
/* 190 */     if (referenceNode == null) {
/* 191 */       Preconditions.checkNotNull(edge);
/* 192 */       throw new IllegalArgumentException(String.format("Edge %s is not an element of this graph.", new Object[] { edge }));
/*     */     } 
/* 194 */     return referenceNode;
/*     */   }
/*     */   
/*     */   final boolean containsNode(N node) {
/* 198 */     return this.nodeConnections.containsKey(node);
/*     */   }
/*     */   
/*     */   final boolean containsEdge(E edge) {
/* 202 */     return this.edgeToReferenceNode.containsKey(edge);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/StandardNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */