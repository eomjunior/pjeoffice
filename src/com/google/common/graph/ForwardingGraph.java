/*     */ package com.google.common.graph;
/*     */ 
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ abstract class ForwardingGraph<N>
/*     */   extends AbstractGraph<N>
/*     */ {
/*     */   public Set<N> nodes() {
/*  34 */     return delegate().nodes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long edgeCount() {
/*  43 */     return delegate().edges().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirected() {
/*  48 */     return delegate().isDirected();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowsSelfLoops() {
/*  53 */     return delegate().allowsSelfLoops();
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> nodeOrder() {
/*  58 */     return delegate().nodeOrder();
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> incidentEdgeOrder() {
/*  63 */     return delegate().incidentEdgeOrder();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes(N node) {
/*  68 */     return delegate().adjacentNodes(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> predecessors(N node) {
/*  73 */     return delegate().predecessors(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors(N node) {
/*  78 */     return delegate().successors(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<EndpointPair<N>> incidentEdges(N node) {
/*  83 */     return delegate().incidentEdges(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int degree(N node) {
/*  88 */     return delegate().degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int inDegree(N node) {
/*  93 */     return delegate().inDegree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int outDegree(N node) {
/*  98 */     return delegate().outDegree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 103 */     return delegate().hasEdgeConnecting(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 108 */     return delegate().hasEdgeConnecting(endpoints);
/*     */   }
/*     */   
/*     */   abstract BaseGraph<N> delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/ForwardingGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */