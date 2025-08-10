/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Functions;
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
/*     */ @Immutable(containerOf = {"N"})
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @Beta
/*     */ public class ImmutableGraph<N>
/*     */   extends ForwardingGraph<N>
/*     */ {
/*     */   private final BaseGraph<N> backingGraph;
/*     */   
/*     */   ImmutableGraph(BaseGraph<N> backingGraph) {
/*  54 */     this.backingGraph = backingGraph;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <N> ImmutableGraph<N> copyOf(Graph<N> graph) {
/*  59 */     return (graph instanceof ImmutableGraph) ? 
/*  60 */       (ImmutableGraph<N>)graph : 
/*  61 */       new ImmutableGraph<>(new StandardValueGraph<>(
/*     */           
/*  63 */           GraphBuilder.from(graph), (Map<N, GraphConnections<N, ?>>)getNodeConnections(graph), graph.edges().size()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <N> ImmutableGraph<N> copyOf(ImmutableGraph<N> graph) {
/*  73 */     return (ImmutableGraph<N>)Preconditions.checkNotNull(graph);
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> incidentEdgeOrder() {
/*  78 */     return ElementOrder.stable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N> ImmutableMap<N, GraphConnections<N, GraphConstants.Presence>> getNodeConnections(Graph<N> graph) {
/*  86 */     ImmutableMap.Builder<N, GraphConnections<N, GraphConstants.Presence>> nodeConnections = ImmutableMap.builder();
/*  87 */     for (N node : graph.nodes()) {
/*  88 */       nodeConnections.put(node, connectionsOf(graph, node));
/*     */     }
/*  90 */     return nodeConnections.buildOrThrow();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N> GraphConnections<N, GraphConstants.Presence> connectionsOf(Graph<N> graph, N node) {
/*  96 */     Function<N, GraphConstants.Presence> edgeValueFn = Functions.constant(GraphConstants.Presence.EDGE_EXISTS);
/*  97 */     return graph.isDirected() ? 
/*  98 */       DirectedGraphConnections.<N, GraphConstants.Presence>ofImmutable(node, graph.incidentEdges(node), edgeValueFn) : 
/*  99 */       UndirectedGraphConnections.<N, GraphConstants.Presence>ofImmutable(
/* 100 */         Maps.asMap(graph.adjacentNodes(node), edgeValueFn));
/*     */   }
/*     */ 
/*     */   
/*     */   BaseGraph<N> delegate() {
/* 105 */     return this.backingGraph;
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
/*     */   public static class Builder<N>
/*     */   {
/*     */     private final MutableGraph<N> mutableGraph;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder(GraphBuilder<N> graphBuilder) {
/* 136 */       this.mutableGraph = graphBuilder.copy().incidentEdgeOrder(ElementOrder.stable()).build();
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
/*     */     public Builder<N> addNode(N node) {
/* 148 */       this.mutableGraph.addNode(node);
/* 149 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<N> putEdge(N nodeU, N nodeV) {
/* 167 */       this.mutableGraph.putEdge(nodeU, nodeV);
/* 168 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<N> putEdge(EndpointPair<N> endpoints) {
/* 190 */       this.mutableGraph.putEdge(endpoints);
/* 191 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableGraph<N> build() {
/* 198 */       return ImmutableGraph.copyOf(this.mutableGraph);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/ImmutableGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */