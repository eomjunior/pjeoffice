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
/*     */ abstract class ForwardingNetwork<N, E>
/*     */   extends AbstractNetwork<N, E>
/*     */ {
/*     */   public Set<N> nodes() {
/*  37 */     return delegate().nodes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edges() {
/*  42 */     return delegate().edges();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirected() {
/*  47 */     return delegate().isDirected();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowsParallelEdges() {
/*  52 */     return delegate().allowsParallelEdges();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowsSelfLoops() {
/*  57 */     return delegate().allowsSelfLoops();
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> nodeOrder() {
/*  62 */     return delegate().nodeOrder();
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<E> edgeOrder() {
/*  67 */     return delegate().edgeOrder();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes(N node) {
/*  72 */     return delegate().adjacentNodes(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> predecessors(N node) {
/*  77 */     return delegate().predecessors(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors(N node) {
/*  82 */     return delegate().successors(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> incidentEdges(N node) {
/*  87 */     return delegate().incidentEdges(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> inEdges(N node) {
/*  92 */     return delegate().inEdges(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> outEdges(N node) {
/*  97 */     return delegate().outEdges(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public EndpointPair<N> incidentNodes(E edge) {
/* 102 */     return delegate().incidentNodes(edge);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> adjacentEdges(E edge) {
/* 107 */     return delegate().adjacentEdges(edge);
/*     */   }
/*     */ 
/*     */   
/*     */   public int degree(N node) {
/* 112 */     return delegate().degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int inDegree(N node) {
/* 117 */     return delegate().inDegree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int outDegree(N node) {
/* 122 */     return delegate().outDegree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(N nodeU, N nodeV) {
/* 127 */     return delegate().edgesConnecting(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(EndpointPair<N> endpoints) {
/* 132 */     return delegate().edgesConnecting(endpoints);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<E> edgeConnecting(N nodeU, N nodeV) {
/* 137 */     return delegate().edgeConnecting(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<E> edgeConnecting(EndpointPair<N> endpoints) {
/* 142 */     return delegate().edgeConnecting(endpoints);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E edgeConnectingOrNull(N nodeU, N nodeV) {
/* 148 */     return delegate().edgeConnectingOrNull(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public E edgeConnectingOrNull(EndpointPair<N> endpoints) {
/* 154 */     return delegate().edgeConnectingOrNull(endpoints);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 159 */     return delegate().hasEdgeConnecting(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 164 */     return delegate().hasEdgeConnecting(endpoints);
/*     */   }
/*     */   
/*     */   abstract Network<N, E> delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/ForwardingNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */