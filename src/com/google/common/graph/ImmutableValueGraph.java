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
/*     */ @Immutable(containerOf = {"N", "V"})
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @Beta
/*     */ public final class ImmutableValueGraph<N, V>
/*     */   extends StandardValueGraph<N, V>
/*     */ {
/*     */   private ImmutableValueGraph(ValueGraph<N, V> graph) {
/*  51 */     super(ValueGraphBuilder.from(graph), (Map<N, GraphConnections<N, V>>)getNodeConnections(graph), graph.edges().size());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <N, V> ImmutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph) {
/*  56 */     return (graph instanceof ImmutableValueGraph) ? 
/*  57 */       (ImmutableValueGraph<N, V>)graph : 
/*  58 */       new ImmutableValueGraph<>(graph);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <N, V> ImmutableValueGraph<N, V> copyOf(ImmutableValueGraph<N, V> graph) {
/*  68 */     return (ImmutableValueGraph<N, V>)Preconditions.checkNotNull(graph);
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> incidentEdgeOrder() {
/*  73 */     return ElementOrder.stable();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableGraph<N> asGraph() {
/*  78 */     return new ImmutableGraph<>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N, V> ImmutableMap<N, GraphConnections<N, V>> getNodeConnections(ValueGraph<N, V> graph) {
/*  86 */     ImmutableMap.Builder<N, GraphConnections<N, V>> nodeConnections = ImmutableMap.builder();
/*  87 */     for (N node : graph.nodes()) {
/*  88 */       nodeConnections.put(node, connectionsOf(graph, node));
/*     */     }
/*  90 */     return nodeConnections.buildOrThrow();
/*     */   }
/*     */   
/*     */   private static <N, V> GraphConnections<N, V> connectionsOf(ValueGraph<N, V> graph, N node) {
/*  94 */     Function<N, V> successorNodeToValueFn = successorNode -> Objects.requireNonNull(graph.edgeValueOrDefault(node, successorNode, null));
/*     */ 
/*     */ 
/*     */     
/*  98 */     return graph.isDirected() ? 
/*  99 */       DirectedGraphConnections.<N, V>ofImmutable(node, graph
/* 100 */         .incidentEdges(node), successorNodeToValueFn) : 
/* 101 */       UndirectedGraphConnections.<N, V>ofImmutable(
/* 102 */         Maps.asMap(graph.adjacentNodes(node), successorNodeToValueFn));
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
/*     */   public static class Builder<N, V>
/*     */   {
/*     */     private final MutableValueGraph<N, V> mutableValueGraph;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder(ValueGraphBuilder<N, V> graphBuilder) {
/* 133 */       this
/* 134 */         .mutableValueGraph = graphBuilder.copy().incidentEdgeOrder(ElementOrder.stable()).build();
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
/*     */     public Builder<N, V> addNode(N node) {
/* 146 */       this.mutableValueGraph.addNode(node);
/* 147 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<N, V> putEdgeValue(N nodeU, N nodeV, V value) {
/* 168 */       this.mutableValueGraph.putEdgeValue(nodeU, nodeV, value);
/* 169 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<N, V> putEdgeValue(EndpointPair<N> endpoints, V value) {
/* 193 */       this.mutableValueGraph.putEdgeValue(endpoints, value);
/* 194 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableValueGraph<N, V> build() {
/* 202 */       return ImmutableValueGraph.copyOf(this.mutableValueGraph);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/ImmutableValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */