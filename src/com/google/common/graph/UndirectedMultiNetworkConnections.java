/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.HashMultiset;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Multiset;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ final class UndirectedMultiNetworkConnections<N, E>
/*     */   extends AbstractUndirectedNetworkConnections<N, E>
/*     */ {
/*     */   @CheckForNull
/*     */   @LazyInit
/*     */   private transient Reference<Multiset<N>> adjacentNodesReference;
/*     */   
/*     */   private UndirectedMultiNetworkConnections(Map<E, N> incidentEdges) {
/*  47 */     super(incidentEdges);
/*     */   }
/*     */   
/*     */   static <N, E> UndirectedMultiNetworkConnections<N, E> of() {
/*  51 */     return new UndirectedMultiNetworkConnections<>(new HashMap<>(2, 1.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   static <N, E> UndirectedMultiNetworkConnections<N, E> ofImmutable(Map<E, N> incidentEdges) {
/*  56 */     return new UndirectedMultiNetworkConnections<>((Map<E, N>)ImmutableMap.copyOf(incidentEdges));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes() {
/*  63 */     return Collections.unmodifiableSet(adjacentNodesMultiset().elementSet());
/*     */   }
/*     */   private Multiset<N> adjacentNodesMultiset() {
/*     */     HashMultiset hashMultiset;
/*  67 */     Multiset<N> adjacentNodes = getReference(this.adjacentNodesReference);
/*  68 */     if (adjacentNodes == null) {
/*  69 */       hashMultiset = HashMultiset.create(this.incidentEdgeMap.values());
/*  70 */       this.adjacentNodesReference = new SoftReference(hashMultiset);
/*     */     } 
/*  72 */     return (Multiset<N>)hashMultiset;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(final N node) {
/*  77 */     return new MultiEdgesConnecting<E>(this.incidentEdgeMap, node)
/*     */       {
/*     */         public int size() {
/*  80 */           return UndirectedMultiNetworkConnections.this.adjacentNodesMultiset().count(node);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public N removeInEdge(E edge, boolean isSelfLoop) {
/*  88 */     if (!isSelfLoop) {
/*  89 */       return removeOutEdge(edge);
/*     */     }
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeOutEdge(E edge) {
/*  96 */     N node = super.removeOutEdge(edge);
/*  97 */     Multiset<N> adjacentNodes = getReference(this.adjacentNodesReference);
/*  98 */     if (adjacentNodes != null) {
/*  99 */       Preconditions.checkState(adjacentNodes.remove(node));
/*     */     }
/* 101 */     return node;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInEdge(E edge, N node, boolean isSelfLoop) {
/* 106 */     if (!isSelfLoop) {
/* 107 */       addOutEdge(edge, node);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addOutEdge(E edge, N node) {
/* 113 */     super.addOutEdge(edge, node);
/* 114 */     Multiset<N> adjacentNodes = getReference(this.adjacentNodesReference);
/* 115 */     if (adjacentNodes != null) {
/* 116 */       Preconditions.checkState(adjacentNodes.add(node));
/*     */     }
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private static <T> T getReference(@CheckForNull Reference<T> reference) {
/* 122 */     return (reference == null) ? null : reference.get();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/UndirectedMultiNetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */