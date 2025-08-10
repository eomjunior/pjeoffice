/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ abstract class AbstractDirectedNetworkConnections<N, E>
/*     */   implements NetworkConnections<N, E>
/*     */ {
/*     */   final Map<E, N> inEdgeMap;
/*     */   final Map<E, N> outEdgeMap;
/*     */   private int selfLoopCount;
/*     */   
/*     */   AbstractDirectedNetworkConnections(Map<E, N> inEdgeMap, Map<E, N> outEdgeMap, int selfLoopCount) {
/*  54 */     this.inEdgeMap = (Map<E, N>)Preconditions.checkNotNull(inEdgeMap);
/*  55 */     this.outEdgeMap = (Map<E, N>)Preconditions.checkNotNull(outEdgeMap);
/*  56 */     this.selfLoopCount = Graphs.checkNonNegative(selfLoopCount);
/*  57 */     Preconditions.checkState((selfLoopCount <= inEdgeMap.size() && selfLoopCount <= outEdgeMap.size()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes() {
/*  62 */     return (Set<N>)Sets.union(predecessors(), successors());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> incidentEdges() {
/*  67 */     return new AbstractSet<E>()
/*     */       {
/*     */ 
/*     */         
/*     */         public UnmodifiableIterator<E> iterator()
/*     */         {
/*  73 */           Iterable<E> incidentEdges = (AbstractDirectedNetworkConnections.this.selfLoopCount == 0) ? Iterables.concat(AbstractDirectedNetworkConnections.this.inEdgeMap.keySet(), AbstractDirectedNetworkConnections.this.outEdgeMap.keySet()) : (Iterable<E>)Sets.union(AbstractDirectedNetworkConnections.this.inEdgeMap.keySet(), AbstractDirectedNetworkConnections.this.outEdgeMap.keySet());
/*  74 */           return Iterators.unmodifiableIterator(incidentEdges.iterator());
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/*  79 */           return IntMath.saturatedAdd(AbstractDirectedNetworkConnections.this.inEdgeMap.size(), AbstractDirectedNetworkConnections.this.outEdgeMap.size() - AbstractDirectedNetworkConnections.this.selfLoopCount);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(@CheckForNull Object obj) {
/*  84 */           return (AbstractDirectedNetworkConnections.this.inEdgeMap.containsKey(obj) || AbstractDirectedNetworkConnections.this.outEdgeMap.containsKey(obj));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> inEdges() {
/*  91 */     return Collections.unmodifiableSet(this.inEdgeMap.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> outEdges() {
/*  96 */     return Collections.unmodifiableSet(this.outEdgeMap.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public N adjacentNode(E edge) {
/* 104 */     return Objects.requireNonNull(this.outEdgeMap.get(edge));
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeInEdge(E edge, boolean isSelfLoop) {
/* 109 */     if (isSelfLoop) {
/* 110 */       Graphs.checkNonNegative(--this.selfLoopCount);
/*     */     }
/* 112 */     N previousNode = this.inEdgeMap.remove(edge);
/*     */     
/* 114 */     return Objects.requireNonNull(previousNode);
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeOutEdge(E edge) {
/* 119 */     N previousNode = this.outEdgeMap.remove(edge);
/*     */     
/* 121 */     return Objects.requireNonNull(previousNode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInEdge(E edge, N node, boolean isSelfLoop) {
/* 126 */     Preconditions.checkNotNull(edge);
/* 127 */     Preconditions.checkNotNull(node);
/*     */     
/* 129 */     if (isSelfLoop) {
/* 130 */       Graphs.checkPositive(++this.selfLoopCount);
/*     */     }
/* 132 */     N previousNode = this.inEdgeMap.put(edge, node);
/* 133 */     Preconditions.checkState((previousNode == null));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addOutEdge(E edge, N node) {
/* 138 */     Preconditions.checkNotNull(edge);
/* 139 */     Preconditions.checkNotNull(node);
/*     */     
/* 141 */     N previousNode = this.outEdgeMap.put(edge, node);
/* 142 */     Preconditions.checkState((previousNode == null));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/AbstractDirectedNetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */