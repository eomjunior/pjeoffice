/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class StandardMutableNetwork<N, E>
/*     */   extends StandardNetwork<N, E>
/*     */   implements MutableNetwork<N, E>
/*     */ {
/*     */   StandardMutableNetwork(NetworkBuilder<? super N, ? super E> builder) {
/*  49 */     super(builder);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addNode(N node) {
/*  55 */     Preconditions.checkNotNull(node, "node");
/*     */     
/*  57 */     if (containsNode(node)) {
/*  58 */       return false;
/*     */     }
/*     */     
/*  61 */     addNodeInternal(node);
/*  62 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private NetworkConnections<N, E> addNodeInternal(N node) {
/*  72 */     NetworkConnections<N, E> connections = newConnections();
/*  73 */     Preconditions.checkState((this.nodeConnections.put(node, connections) == null));
/*  74 */     return connections;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addEdge(N nodeU, N nodeV, E edge) {
/*  80 */     Preconditions.checkNotNull(nodeU, "nodeU");
/*  81 */     Preconditions.checkNotNull(nodeV, "nodeV");
/*  82 */     Preconditions.checkNotNull(edge, "edge");
/*     */     
/*  84 */     if (containsEdge(edge)) {
/*  85 */       EndpointPair<N> existingIncidentNodes = incidentNodes(edge);
/*  86 */       EndpointPair<N> newIncidentNodes = EndpointPair.of(this, nodeU, nodeV);
/*  87 */       Preconditions.checkArgument(existingIncidentNodes
/*  88 */           .equals(newIncidentNodes), "Edge %s already exists between the following nodes: %s, so it cannot be reused to connect the following nodes: %s.", edge, existingIncidentNodes, newIncidentNodes);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  93 */       return false;
/*     */     } 
/*  95 */     NetworkConnections<N, E> connectionsU = this.nodeConnections.get(nodeU);
/*  96 */     if (!allowsParallelEdges()) {
/*  97 */       Preconditions.checkArgument((connectionsU == null || 
/*  98 */           !connectionsU.successors().contains(nodeV)), "Nodes %s and %s are already connected by a different edge. To construct a graph that allows parallel edges, call allowsParallelEdges(true) on the Builder.", nodeU, nodeV);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 103 */     boolean isSelfLoop = nodeU.equals(nodeV);
/* 104 */     if (!allowsSelfLoops()) {
/* 105 */       Preconditions.checkArgument(!isSelfLoop, "Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph that allows self-loops, call allowsSelfLoops(true) on the Builder.", nodeU);
/*     */     }
/*     */     
/* 108 */     if (connectionsU == null) {
/* 109 */       connectionsU = addNodeInternal(nodeU);
/*     */     }
/* 111 */     connectionsU.addOutEdge(edge, nodeV);
/* 112 */     NetworkConnections<N, E> connectionsV = this.nodeConnections.get(nodeV);
/* 113 */     if (connectionsV == null) {
/* 114 */       connectionsV = addNodeInternal(nodeV);
/*     */     }
/* 116 */     connectionsV.addInEdge(edge, nodeU, isSelfLoop);
/* 117 */     this.edgeToReferenceNode.put(edge, nodeU);
/* 118 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addEdge(EndpointPair<N> endpoints, E edge) {
/* 124 */     validateEndpoints(endpoints);
/* 125 */     return addEdge(endpoints.nodeU(), endpoints.nodeV(), edge);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeNode(N node) {
/* 131 */     Preconditions.checkNotNull(node, "node");
/*     */     
/* 133 */     NetworkConnections<N, E> connections = this.nodeConnections.get(node);
/* 134 */     if (connections == null) {
/* 135 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 140 */     for (UnmodifiableIterator<E> unmodifiableIterator = ImmutableList.copyOf(connections.incidentEdges()).iterator(); unmodifiableIterator.hasNext(); ) { E edge = unmodifiableIterator.next();
/* 141 */       removeEdge(edge); }
/*     */     
/* 143 */     this.nodeConnections.remove(node);
/* 144 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeEdge(E edge) {
/* 150 */     Preconditions.checkNotNull(edge, "edge");
/*     */     
/* 152 */     N nodeU = this.edgeToReferenceNode.get(edge);
/* 153 */     if (nodeU == null) {
/* 154 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 158 */     NetworkConnections<N, E> connectionsU = Objects.<NetworkConnections<N, E>>requireNonNull(this.nodeConnections.get(nodeU));
/* 159 */     N nodeV = connectionsU.adjacentNode(edge);
/* 160 */     NetworkConnections<N, E> connectionsV = Objects.<NetworkConnections<N, E>>requireNonNull(this.nodeConnections.get(nodeV));
/* 161 */     connectionsU.removeOutEdge(edge);
/* 162 */     connectionsV.removeInEdge(edge, (allowsSelfLoops() && nodeU.equals(nodeV)));
/* 163 */     this.edgeToReferenceNode.remove(edge);
/* 164 */     return true;
/*     */   }
/*     */   
/*     */   private NetworkConnections<N, E> newConnections() {
/* 168 */     return isDirected() ? (
/* 169 */       allowsParallelEdges() ? 
/* 170 */       DirectedMultiNetworkConnections.<N, E>of() : 
/* 171 */       DirectedNetworkConnections.<N, E>of()) : (
/* 172 */       allowsParallelEdges() ? 
/* 173 */       UndirectedMultiNetworkConnections.<N, E>of() : 
/* 174 */       UndirectedNetworkConnections.<N, E>of());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/StandardMutableNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */