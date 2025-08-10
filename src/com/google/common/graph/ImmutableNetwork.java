/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.util.Map;
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
/*     */ @Immutable(containerOf = {"N", "E"})
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @Beta
/*     */ public final class ImmutableNetwork<N, E>
/*     */   extends StandardNetwork<N, E>
/*     */ {
/*     */   private ImmutableNetwork(Network<N, E> network) {
/*  53 */     super(
/*  54 */         NetworkBuilder.from(network), getNodeConnections(network), getEdgeToReferenceNode(network));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <N, E> ImmutableNetwork<N, E> copyOf(Network<N, E> network) {
/*  59 */     return (network instanceof ImmutableNetwork) ? 
/*  60 */       (ImmutableNetwork<N, E>)network : 
/*  61 */       new ImmutableNetwork<>(network);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <N, E> ImmutableNetwork<N, E> copyOf(ImmutableNetwork<N, E> network) {
/*  71 */     return (ImmutableNetwork<N, E>)Preconditions.checkNotNull(network);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableGraph<N> asGraph() {
/*  76 */     return new ImmutableGraph<>(super.asGraph());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N, E> Map<N, NetworkConnections<N, E>> getNodeConnections(Network<N, E> network) {
/*  83 */     ImmutableMap.Builder<N, NetworkConnections<N, E>> nodeConnections = ImmutableMap.builder();
/*  84 */     for (N node : network.nodes()) {
/*  85 */       nodeConnections.put(node, connectionsOf(network, node));
/*     */     }
/*  87 */     return (Map<N, NetworkConnections<N, E>>)nodeConnections.buildOrThrow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N, E> Map<E, N> getEdgeToReferenceNode(Network<N, E> network) {
/*  94 */     ImmutableMap.Builder<E, N> edgeToReferenceNode = ImmutableMap.builder();
/*  95 */     for (E edge : network.edges()) {
/*  96 */       edgeToReferenceNode.put(edge, network.incidentNodes(edge).nodeU());
/*     */     }
/*  98 */     return (Map<E, N>)edgeToReferenceNode.buildOrThrow();
/*     */   }
/*     */   
/*     */   private static <N, E> NetworkConnections<N, E> connectionsOf(Network<N, E> network, N node) {
/* 102 */     if (network.isDirected()) {
/* 103 */       Map<E, N> inEdgeMap = Maps.asMap(network.inEdges(node), sourceNodeFn(network));
/* 104 */       Map<E, N> outEdgeMap = Maps.asMap(network.outEdges(node), targetNodeFn(network));
/* 105 */       int selfLoopCount = network.edgesConnecting(node, node).size();
/* 106 */       return network.allowsParallelEdges() ? 
/* 107 */         DirectedMultiNetworkConnections.<N, E>ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount) : 
/* 108 */         DirectedNetworkConnections.<N, E>ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount);
/*     */     } 
/*     */     
/* 111 */     Map<E, N> incidentEdgeMap = Maps.asMap(network.incidentEdges(node), adjacentNodeFn(network, node));
/* 112 */     return network.allowsParallelEdges() ? 
/* 113 */       UndirectedMultiNetworkConnections.<N, E>ofImmutable(incidentEdgeMap) : 
/* 114 */       UndirectedNetworkConnections.<N, E>ofImmutable(incidentEdgeMap);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <N, E> Function<E, N> sourceNodeFn(Network<N, E> network) {
/* 119 */     return edge -> network.incidentNodes(edge).source();
/*     */   }
/*     */   
/*     */   private static <N, E> Function<E, N> targetNodeFn(Network<N, E> network) {
/* 123 */     return edge -> network.incidentNodes(edge).target();
/*     */   }
/*     */   
/*     */   private static <N, E> Function<E, N> adjacentNodeFn(Network<N, E> network, N node) {
/* 127 */     return edge -> network.incidentNodes(edge).adjacentNode(node);
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
/*     */   public static class Builder<N, E>
/*     */   {
/*     */     private final MutableNetwork<N, E> mutableNetwork;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder(NetworkBuilder<N, E> networkBuilder) {
/* 158 */       this.mutableNetwork = networkBuilder.build();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<N, E> addNode(N node) {
/* 170 */       this.mutableNetwork.addNode(node);
/* 171 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<N, E> addEdge(N nodeU, N nodeV, E edge) {
/* 197 */       this.mutableNetwork.addEdge(nodeU, nodeV, edge);
/* 198 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<N, E> addEdge(EndpointPair<N> endpoints, E edge) {
/* 228 */       this.mutableNetwork.addEdge(endpoints, edge);
/* 229 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableNetwork<N, E> build() {
/* 237 */       return ImmutableNetwork.copyOf(this.mutableNetwork);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/ImmutableNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */