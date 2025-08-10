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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ final class DirectedMultiNetworkConnections<N, E>
/*     */   extends AbstractDirectedNetworkConnections<N, E>
/*     */ {
/*     */   @CheckForNull
/*     */   @LazyInit
/*     */   private transient Reference<Multiset<N>> predecessorsReference;
/*     */   @CheckForNull
/*     */   @LazyInit
/*     */   private transient Reference<Multiset<N>> successorsReference;
/*     */   
/*     */   private DirectedMultiNetworkConnections(Map<E, N> inEdges, Map<E, N> outEdges, int selfLoopCount) {
/*  47 */     super(inEdges, outEdges, selfLoopCount);
/*     */   }
/*     */   
/*     */   static <N, E> DirectedMultiNetworkConnections<N, E> of() {
/*  51 */     return new DirectedMultiNetworkConnections<>(new HashMap<>(2, 1.0F), new HashMap<>(2, 1.0F), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <N, E> DirectedMultiNetworkConnections<N, E> ofImmutable(Map<E, N> inEdges, Map<E, N> outEdges, int selfLoopCount) {
/*  59 */     return new DirectedMultiNetworkConnections<>(
/*  60 */         (Map<E, N>)ImmutableMap.copyOf(inEdges), (Map<E, N>)ImmutableMap.copyOf(outEdges), selfLoopCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<N> predecessors() {
/*  67 */     return Collections.unmodifiableSet(predecessorsMultiset().elementSet());
/*     */   }
/*     */   private Multiset<N> predecessorsMultiset() {
/*     */     HashMultiset hashMultiset;
/*  71 */     Multiset<N> predecessors = getReference(this.predecessorsReference);
/*  72 */     if (predecessors == null) {
/*  73 */       hashMultiset = HashMultiset.create(this.inEdgeMap.values());
/*  74 */       this.predecessorsReference = new SoftReference(hashMultiset);
/*     */     } 
/*  76 */     return (Multiset<N>)hashMultiset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<N> successors() {
/*  83 */     return Collections.unmodifiableSet(successorsMultiset().elementSet());
/*     */   }
/*     */   private Multiset<N> successorsMultiset() {
/*     */     HashMultiset hashMultiset;
/*  87 */     Multiset<N> successors = getReference(this.successorsReference);
/*  88 */     if (successors == null) {
/*  89 */       hashMultiset = HashMultiset.create(this.outEdgeMap.values());
/*  90 */       this.successorsReference = new SoftReference(hashMultiset);
/*     */     } 
/*  92 */     return (Multiset<N>)hashMultiset;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(final N node) {
/*  97 */     return new MultiEdgesConnecting<E>(this.outEdgeMap, node)
/*     */       {
/*     */         public int size() {
/* 100 */           return DirectedMultiNetworkConnections.this.successorsMultiset().count(node);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeInEdge(E edge, boolean isSelfLoop) {
/* 107 */     N node = super.removeInEdge(edge, isSelfLoop);
/* 108 */     Multiset<N> predecessors = getReference(this.predecessorsReference);
/* 109 */     if (predecessors != null) {
/* 110 */       Preconditions.checkState(predecessors.remove(node));
/*     */     }
/* 112 */     return node;
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeOutEdge(E edge) {
/* 117 */     N node = super.removeOutEdge(edge);
/* 118 */     Multiset<N> successors = getReference(this.successorsReference);
/* 119 */     if (successors != null) {
/* 120 */       Preconditions.checkState(successors.remove(node));
/*     */     }
/* 122 */     return node;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInEdge(E edge, N node, boolean isSelfLoop) {
/* 127 */     super.addInEdge(edge, node, isSelfLoop);
/* 128 */     Multiset<N> predecessors = getReference(this.predecessorsReference);
/* 129 */     if (predecessors != null) {
/* 130 */       Preconditions.checkState(predecessors.add(node));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addOutEdge(E edge, N node) {
/* 136 */     super.addOutEdge(edge, node);
/* 137 */     Multiset<N> successors = getReference(this.successorsReference);
/* 138 */     if (successors != null) {
/* 139 */       Preconditions.checkState(successors.add(node));
/*     */     }
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private static <T> T getReference(@CheckForNull Reference<T> reference) {
/* 145 */     return (reference == null) ? null : reference.get();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/DirectedMultiNetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */