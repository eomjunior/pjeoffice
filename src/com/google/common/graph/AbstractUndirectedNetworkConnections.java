/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collections;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ abstract class AbstractUndirectedNetworkConnections<N, E>
/*     */   implements NetworkConnections<N, E>
/*     */ {
/*     */   final Map<E, N> incidentEdgeMap;
/*     */   
/*     */   AbstractUndirectedNetworkConnections(Map<E, N> incidentEdgeMap) {
/*  41 */     this.incidentEdgeMap = (Map<E, N>)Preconditions.checkNotNull(incidentEdgeMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> predecessors() {
/*  46 */     return adjacentNodes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors() {
/*  51 */     return adjacentNodes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> incidentEdges() {
/*  56 */     return Collections.unmodifiableSet(this.incidentEdgeMap.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> inEdges() {
/*  61 */     return incidentEdges();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> outEdges() {
/*  66 */     return incidentEdges();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public N adjacentNode(E edge) {
/*  72 */     return Objects.requireNonNull(this.incidentEdgeMap.get(edge));
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public N removeInEdge(E edge, boolean isSelfLoop) {
/*  78 */     if (!isSelfLoop) {
/*  79 */       return removeOutEdge(edge);
/*     */     }
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeOutEdge(E edge) {
/*  86 */     N previousNode = this.incidentEdgeMap.remove(edge);
/*     */     
/*  88 */     return Objects.requireNonNull(previousNode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInEdge(E edge, N node, boolean isSelfLoop) {
/*  93 */     if (!isSelfLoop) {
/*  94 */       addOutEdge(edge, node);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addOutEdge(E edge, N node) {
/* 100 */     N previousNode = this.incidentEdgeMap.put(edge, node);
/* 101 */     Preconditions.checkState((previousNode == null));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/AbstractUndirectedNetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */