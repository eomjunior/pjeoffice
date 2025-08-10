/*     */ package com.google.common.graph;
/*     */ 
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ abstract class ForwardingValueGraph<N, V>
/*     */   extends AbstractValueGraph<N, V>
/*     */ {
/*     */   public Set<N> nodes() {
/*  37 */     return delegate().nodes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long edgeCount() {
/*  46 */     return delegate().edges().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirected() {
/*  51 */     return delegate().isDirected();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowsSelfLoops() {
/*  56 */     return delegate().allowsSelfLoops();
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> nodeOrder() {
/*  61 */     return delegate().nodeOrder();
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> incidentEdgeOrder() {
/*  66 */     return delegate().incidentEdgeOrder();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes(N node) {
/*  71 */     return delegate().adjacentNodes(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> predecessors(N node) {
/*  76 */     return delegate().predecessors(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors(N node) {
/*  81 */     return delegate().successors(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int degree(N node) {
/*  86 */     return delegate().degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int inDegree(N node) {
/*  91 */     return delegate().inDegree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int outDegree(N node) {
/*  96 */     return delegate().outDegree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 101 */     return delegate().hasEdgeConnecting(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 106 */     return delegate().hasEdgeConnecting(endpoints);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<V> edgeValue(N nodeU, N nodeV) {
/* 111 */     return delegate().edgeValue(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<V> edgeValue(EndpointPair<N> endpoints) {
/* 116 */     return delegate().edgeValue(endpoints);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V edgeValueOrDefault(N nodeU, N nodeV, @CheckForNull V defaultValue) {
/* 122 */     return delegate().edgeValueOrDefault(nodeU, nodeV, defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public V edgeValueOrDefault(EndpointPair<N> endpoints, @CheckForNull V defaultValue) {
/* 128 */     return delegate().edgeValueOrDefault(endpoints, defaultValue);
/*     */   }
/*     */   
/*     */   abstract ValueGraph<N, V> delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/ForwardingValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */