/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @Beta
/*     */ public final class Graphs
/*     */ {
/*     */   public static <N> boolean hasCycle(Graph<N> graph) {
/*  61 */     int numEdges = graph.edges().size();
/*  62 */     if (numEdges == 0) {
/*  63 */       return false;
/*     */     }
/*  65 */     if (!graph.isDirected() && numEdges >= graph.nodes().size()) {
/*  66 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  70 */     Map<Object, NodeVisitState> visitedNodes = Maps.newHashMapWithExpectedSize(graph.nodes().size());
/*  71 */     for (N node : graph.nodes()) {
/*  72 */       if (subgraphHasCycle(graph, visitedNodes, node, null)) {
/*  73 */         return true;
/*     */       }
/*     */     } 
/*  76 */     return false;
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
/*     */   public static boolean hasCycle(Network<?, ?> network) {
/*  89 */     if (!network.isDirected() && network
/*  90 */       .allowsParallelEdges() && network
/*  91 */       .edges().size() > network.asGraph().edges().size()) {
/*  92 */       return true;
/*     */     }
/*  94 */     return hasCycle(network.asGraph());
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
/*     */   private static <N> boolean subgraphHasCycle(Graph<N> graph, Map<Object, NodeVisitState> visitedNodes, N node, @CheckForNull N previousNode) {
/* 107 */     NodeVisitState state = visitedNodes.get(node);
/* 108 */     if (state == NodeVisitState.COMPLETE) {
/* 109 */       return false;
/*     */     }
/* 111 */     if (state == NodeVisitState.PENDING) {
/* 112 */       return true;
/*     */     }
/*     */     
/* 115 */     visitedNodes.put(node, NodeVisitState.PENDING);
/* 116 */     for (N nextNode : graph.successors(node)) {
/* 117 */       if (canTraverseWithoutReusingEdge(graph, nextNode, previousNode) && 
/* 118 */         subgraphHasCycle(graph, visitedNodes, nextNode, node)) {
/* 119 */         return true;
/*     */       }
/*     */     } 
/* 122 */     visitedNodes.put(node, NodeVisitState.COMPLETE);
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean canTraverseWithoutReusingEdge(Graph<?> graph, Object nextNode, @CheckForNull Object previousNode) {
/* 134 */     if (graph.isDirected() || !Objects.equal(previousNode, nextNode)) {
/* 135 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 139 */     return false;
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
/*     */   public static <N> Graph<N> transitiveClosure(Graph<N> graph) {
/* 153 */     MutableGraph<N> transitiveClosure = GraphBuilder.<N>from(graph).allowsSelfLoops(true).build();
/*     */ 
/*     */ 
/*     */     
/* 157 */     if (graph.isDirected()) {
/*     */       
/* 159 */       for (N node : graph.nodes()) {
/* 160 */         for (N reachableNode : reachableNodes(graph, node)) {
/* 161 */           transitiveClosure.putEdge(node, reachableNode);
/*     */         }
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 167 */       Set<N> visitedNodes = new HashSet<>();
/* 168 */       for (N node : graph.nodes()) {
/* 169 */         if (!visitedNodes.contains(node)) {
/* 170 */           Set<N> reachableNodes = reachableNodes(graph, node);
/* 171 */           visitedNodes.addAll(reachableNodes);
/* 172 */           int pairwiseMatch = 1;
/* 173 */           for (N nodeU : reachableNodes) {
/* 174 */             for (N nodeV : Iterables.limit(reachableNodes, pairwiseMatch++)) {
/* 175 */               transitiveClosure.putEdge(nodeU, nodeV);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 182 */     return transitiveClosure;
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
/*     */   public static <N> Set<N> reachableNodes(Graph<N> graph, N node) {
/* 197 */     Preconditions.checkArgument(graph.nodes().contains(node), "Node %s is not an element of this graph.", node);
/* 198 */     return (Set<N>)ImmutableSet.copyOf(Traverser.<N>forGraph(graph).breadthFirst(node));
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
/*     */   public static <N> Graph<N> transpose(Graph<N> graph) {
/* 210 */     if (!graph.isDirected()) {
/* 211 */       return graph;
/*     */     }
/*     */     
/* 214 */     if (graph instanceof TransposedGraph) {
/* 215 */       return ((TransposedGraph)graph).graph;
/*     */     }
/*     */     
/* 218 */     return new TransposedGraph<>(graph);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, V> ValueGraph<N, V> transpose(ValueGraph<N, V> graph) {
/* 226 */     if (!graph.isDirected()) {
/* 227 */       return graph;
/*     */     }
/*     */     
/* 230 */     if (graph instanceof TransposedValueGraph) {
/* 231 */       return ((TransposedValueGraph)graph).graph;
/*     */     }
/*     */     
/* 234 */     return new TransposedValueGraph<>(graph);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, E> Network<N, E> transpose(Network<N, E> network) {
/* 242 */     if (!network.isDirected()) {
/* 243 */       return network;
/*     */     }
/*     */     
/* 246 */     if (network instanceof TransposedNetwork) {
/* 247 */       return ((TransposedNetwork)network).network;
/*     */     }
/*     */     
/* 250 */     return new TransposedNetwork<>(network);
/*     */   }
/*     */   
/*     */   static <N> EndpointPair<N> transpose(EndpointPair<N> endpoints) {
/* 254 */     if (endpoints.isOrdered()) {
/* 255 */       return EndpointPair.ordered(endpoints.target(), endpoints.source());
/*     */     }
/* 257 */     return endpoints;
/*     */   }
/*     */   
/*     */   private static class TransposedGraph<N>
/*     */     extends ForwardingGraph<N>
/*     */   {
/*     */     private final Graph<N> graph;
/*     */     
/*     */     TransposedGraph(Graph<N> graph) {
/* 266 */       this.graph = graph;
/*     */     }
/*     */ 
/*     */     
/*     */     Graph<N> delegate() {
/* 271 */       return this.graph;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> predecessors(N node) {
/* 276 */       return delegate().successors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> successors(N node) {
/* 281 */       return delegate().predecessors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<EndpointPair<N>> incidentEdges(N node) {
/* 286 */       return new IncidentEdgeSet<N>(this, node)
/*     */         {
/*     */           public Iterator<EndpointPair<N>> iterator() {
/* 289 */             return Iterators.transform(Graphs.TransposedGraph.this
/* 290 */                 .delegate().incidentEdges(this.node).iterator(), edge -> EndpointPair.of(Graphs.TransposedGraph.this.delegate(), edge.nodeV(), edge.nodeU()));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int inDegree(N node) {
/* 298 */       return delegate().outDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int outDegree(N node) {
/* 303 */       return delegate().inDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 308 */       return delegate().hasEdgeConnecting(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 313 */       return delegate().hasEdgeConnecting(Graphs.transpose(endpoints));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TransposedValueGraph<N, V>
/*     */     extends ForwardingValueGraph<N, V>
/*     */   {
/*     */     private final ValueGraph<N, V> graph;
/*     */     
/*     */     TransposedValueGraph(ValueGraph<N, V> graph) {
/* 323 */       this.graph = graph;
/*     */     }
/*     */ 
/*     */     
/*     */     ValueGraph<N, V> delegate() {
/* 328 */       return this.graph;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> predecessors(N node) {
/* 333 */       return delegate().successors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> successors(N node) {
/* 338 */       return delegate().predecessors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int inDegree(N node) {
/* 343 */       return delegate().outDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int outDegree(N node) {
/* 348 */       return delegate().inDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 353 */       return delegate().hasEdgeConnecting(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 358 */       return delegate().hasEdgeConnecting(Graphs.transpose(endpoints));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<V> edgeValue(N nodeU, N nodeV) {
/* 363 */       return delegate().edgeValue(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<V> edgeValue(EndpointPair<N> endpoints) {
/* 368 */       return delegate().edgeValue(Graphs.transpose(endpoints));
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V edgeValueOrDefault(N nodeU, N nodeV, @CheckForNull V defaultValue) {
/* 374 */       return delegate().edgeValueOrDefault(nodeV, nodeU, defaultValue);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public V edgeValueOrDefault(EndpointPair<N> endpoints, @CheckForNull V defaultValue) {
/* 380 */       return delegate().edgeValueOrDefault(Graphs.transpose(endpoints), defaultValue);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TransposedNetwork<N, E> extends ForwardingNetwork<N, E> {
/*     */     private final Network<N, E> network;
/*     */     
/*     */     TransposedNetwork(Network<N, E> network) {
/* 388 */       this.network = network;
/*     */     }
/*     */ 
/*     */     
/*     */     Network<N, E> delegate() {
/* 393 */       return this.network;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> predecessors(N node) {
/* 398 */       return delegate().successors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> successors(N node) {
/* 403 */       return delegate().predecessors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int inDegree(N node) {
/* 408 */       return delegate().outDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public int outDegree(N node) {
/* 413 */       return delegate().inDegree(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> inEdges(N node) {
/* 418 */       return delegate().outEdges(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> outEdges(N node) {
/* 423 */       return delegate().inEdges(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public EndpointPair<N> incidentNodes(E edge) {
/* 428 */       EndpointPair<N> endpointPair = delegate().incidentNodes(edge);
/* 429 */       return EndpointPair.of(this.network, endpointPair.nodeV(), endpointPair.nodeU());
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> edgesConnecting(N nodeU, N nodeV) {
/* 434 */       return delegate().edgesConnecting(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> edgesConnecting(EndpointPair<N> endpoints) {
/* 439 */       return delegate().edgesConnecting(Graphs.transpose(endpoints));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<E> edgeConnecting(N nodeU, N nodeV) {
/* 444 */       return delegate().edgeConnecting(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<E> edgeConnecting(EndpointPair<N> endpoints) {
/* 449 */       return delegate().edgeConnecting(Graphs.transpose(endpoints));
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public E edgeConnectingOrNull(N nodeU, N nodeV) {
/* 455 */       return delegate().edgeConnectingOrNull(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public E edgeConnectingOrNull(EndpointPair<N> endpoints) {
/* 461 */       return delegate().edgeConnectingOrNull(Graphs.transpose(endpoints));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 466 */       return delegate().hasEdgeConnecting(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 471 */       return delegate().hasEdgeConnecting(Graphs.transpose(endpoints));
/*     */     }
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
/*     */   public static <N> MutableGraph<N> inducedSubgraph(Graph<N> graph, Iterable<? extends N> nodes) {
/* 488 */     MutableGraph<N> subgraph = (nodes instanceof Collection) ? GraphBuilder.<N>from(graph).expectedNodeCount(((Collection)nodes).size()).build() : GraphBuilder.<N>from(graph).build();
/* 489 */     for (N node : nodes) {
/* 490 */       subgraph.addNode(node);
/*     */     }
/* 492 */     for (N node : subgraph.nodes()) {
/* 493 */       for (N successorNode : graph.successors(node)) {
/* 494 */         if (subgraph.nodes().contains(successorNode)) {
/* 495 */           subgraph.putEdge(node, successorNode);
/*     */         }
/*     */       } 
/*     */     } 
/* 499 */     return subgraph;
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
/*     */   public static <N, V> MutableValueGraph<N, V> inducedSubgraph(ValueGraph<N, V> graph, Iterable<? extends N> nodes) {
/* 515 */     MutableValueGraph<N, V> subgraph = (nodes instanceof Collection) ? ValueGraphBuilder.<N, V>from(graph).expectedNodeCount(((Collection)nodes).size()).build() : ValueGraphBuilder.<N, V>from(graph).build();
/* 516 */     for (N node : nodes) {
/* 517 */       subgraph.addNode(node);
/*     */     }
/* 519 */     for (N node : subgraph.nodes()) {
/* 520 */       for (N successorNode : graph.successors(node)) {
/* 521 */         if (subgraph.nodes().contains(successorNode))
/*     */         {
/* 523 */           subgraph.putEdgeValue(node, successorNode, 
/*     */ 
/*     */               
/* 526 */               Objects.requireNonNull(graph.edgeValueOrDefault(node, successorNode, null)));
/*     */         }
/*     */       } 
/*     */     } 
/* 530 */     return subgraph;
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
/*     */   public static <N, E> MutableNetwork<N, E> inducedSubgraph(Network<N, E> network, Iterable<? extends N> nodes) {
/* 546 */     MutableNetwork<N, E> subgraph = (nodes instanceof Collection) ? NetworkBuilder.<N, E>from(network).expectedNodeCount(((Collection)nodes).size()).build() : NetworkBuilder.<N, E>from(network).build();
/* 547 */     for (N node : nodes) {
/* 548 */       subgraph.addNode(node);
/*     */     }
/* 550 */     for (N node : subgraph.nodes()) {
/* 551 */       for (E edge : network.outEdges(node)) {
/* 552 */         N successorNode = network.incidentNodes(edge).adjacentNode(node);
/* 553 */         if (subgraph.nodes().contains(successorNode)) {
/* 554 */           subgraph.addEdge(node, successorNode, edge);
/*     */         }
/*     */       } 
/*     */     } 
/* 558 */     return subgraph;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <N> MutableGraph<N> copyOf(Graph<N> graph) {
/* 563 */     MutableGraph<N> copy = GraphBuilder.<N>from(graph).expectedNodeCount(graph.nodes().size()).build();
/* 564 */     for (N node : graph.nodes()) {
/* 565 */       copy.addNode(node);
/*     */     }
/* 567 */     for (EndpointPair<N> edge : graph.edges()) {
/* 568 */       copy.putEdge(edge.nodeU(), edge.nodeV());
/*     */     }
/* 570 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, V> MutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph) {
/* 576 */     MutableValueGraph<N, V> copy = ValueGraphBuilder.<N, V>from(graph).expectedNodeCount(graph.nodes().size()).build();
/* 577 */     for (N node : graph.nodes()) {
/* 578 */       copy.addNode(node);
/*     */     }
/* 580 */     for (EndpointPair<N> edge : graph.edges())
/*     */     {
/* 582 */       copy.putEdgeValue(edge
/* 583 */           .nodeU(), edge
/* 584 */           .nodeV(), 
/* 585 */           Objects.requireNonNull(graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), null)));
/*     */     }
/* 587 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, E> MutableNetwork<N, E> copyOf(Network<N, E> network) {
/* 596 */     MutableNetwork<N, E> copy = NetworkBuilder.<N, E>from(network).expectedNodeCount(network.nodes().size()).expectedEdgeCount(network.edges().size()).build();
/* 597 */     for (N node : network.nodes()) {
/* 598 */       copy.addNode(node);
/*     */     }
/* 600 */     for (E edge : network.edges()) {
/* 601 */       EndpointPair<N> endpointPair = network.incidentNodes(edge);
/* 602 */       copy.addEdge(endpointPair.nodeU(), endpointPair.nodeV(), edge);
/*     */     } 
/* 604 */     return copy;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static int checkNonNegative(int value) {
/* 609 */     Preconditions.checkArgument((value >= 0), "Not true that %s is non-negative.", value);
/* 610 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long checkNonNegative(long value) {
/* 615 */     Preconditions.checkArgument((value >= 0L), "Not true that %s is non-negative.", value);
/* 616 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static int checkPositive(int value) {
/* 621 */     Preconditions.checkArgument((value > 0), "Not true that %s is positive.", value);
/* 622 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long checkPositive(long value) {
/* 627 */     Preconditions.checkArgument((value > 0L), "Not true that %s is positive.", value);
/* 628 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum NodeVisitState
/*     */   {
/* 637 */     PENDING,
/* 638 */     COMPLETE;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/Graphs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */