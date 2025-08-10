/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ abstract class AbstractBaseGraph<N>
/*     */   implements BaseGraph<N>
/*     */ {
/*     */   protected long edgeCount() {
/*  52 */     long degreeSum = 0L;
/*  53 */     for (N node : nodes()) {
/*  54 */       degreeSum += degree(node);
/*     */     }
/*     */     
/*  57 */     Preconditions.checkState(((degreeSum & 0x1L) == 0L));
/*  58 */     return degreeSum >>> 1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<EndpointPair<N>> edges() {
/*  67 */     return new AbstractSet<EndpointPair<N>>()
/*     */       {
/*     */         public UnmodifiableIterator<EndpointPair<N>> iterator() {
/*  70 */           return (UnmodifiableIterator)EndpointPairIterator.of(AbstractBaseGraph.this);
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/*  75 */           return Ints.saturatedCast(AbstractBaseGraph.this.edgeCount());
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean remove(@CheckForNull Object o) {
/*  80 */           throw new UnsupportedOperationException();
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public boolean contains(@CheckForNull Object obj) {
/*  89 */           if (!(obj instanceof EndpointPair)) {
/*  90 */             return false;
/*     */           }
/*  92 */           EndpointPair<?> endpointPair = (EndpointPair)obj;
/*  93 */           return (AbstractBaseGraph.this.isOrderingCompatible(endpointPair) && AbstractBaseGraph.this
/*  94 */             .nodes().contains(endpointPair.nodeU()) && AbstractBaseGraph.this
/*  95 */             .successors(endpointPair.nodeU()).contains(endpointPair.nodeV()));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> incidentEdgeOrder() {
/* 102 */     return ElementOrder.unordered();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<EndpointPair<N>> incidentEdges(N node) {
/* 107 */     Preconditions.checkNotNull(node);
/* 108 */     Preconditions.checkArgument(nodes().contains(node), "Node %s is not an element of this graph.", node);
/* 109 */     return new IncidentEdgeSet<N>(this, this, node)
/*     */       {
/*     */         public UnmodifiableIterator<EndpointPair<N>> iterator() {
/* 112 */           if (this.graph.isDirected()) {
/* 113 */             return Iterators.unmodifiableIterator(
/* 114 */                 Iterators.concat(
/* 115 */                   Iterators.transform(this.graph
/* 116 */                     .predecessors(this.node).iterator(), predecessor -> EndpointPair.ordered(predecessor, this.node)), 
/*     */                   
/* 118 */                   Iterators.transform(
/*     */                     
/* 120 */                     (Iterator)Sets.difference(this.graph.successors(this.node), (Set)ImmutableSet.of(this.node)).iterator(), successor -> EndpointPair.ordered(this.node, (N)successor))));
/*     */           }
/*     */           
/* 123 */           return Iterators.unmodifiableIterator(
/* 124 */               Iterators.transform(this.graph
/* 125 */                 .adjacentNodes(this.node).iterator(), adjacentNode -> EndpointPair.unordered(this.node, (N)adjacentNode)));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int degree(N node) {
/* 134 */     if (isDirected()) {
/* 135 */       return IntMath.saturatedAdd(predecessors(node).size(), successors(node).size());
/*     */     }
/* 137 */     Set<N> neighbors = adjacentNodes(node);
/* 138 */     int selfLoopCount = (allowsSelfLoops() && neighbors.contains(node)) ? 1 : 0;
/* 139 */     return IntMath.saturatedAdd(neighbors.size(), selfLoopCount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int inDegree(N node) {
/* 145 */     return isDirected() ? predecessors(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int outDegree(N node) {
/* 150 */     return isDirected() ? successors(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(N nodeU, N nodeV) {
/* 155 */     Preconditions.checkNotNull(nodeU);
/* 156 */     Preconditions.checkNotNull(nodeV);
/* 157 */     return (nodes().contains(nodeU) && successors(nodeU).contains(nodeV));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEdgeConnecting(EndpointPair<N> endpoints) {
/* 162 */     Preconditions.checkNotNull(endpoints);
/* 163 */     if (!isOrderingCompatible(endpoints)) {
/* 164 */       return false;
/*     */     }
/* 166 */     N nodeU = endpoints.nodeU();
/* 167 */     N nodeV = endpoints.nodeV();
/* 168 */     return (nodes().contains(nodeU) && successors(nodeU).contains(nodeV));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void validateEndpoints(EndpointPair<?> endpoints) {
/* 176 */     Preconditions.checkNotNull(endpoints);
/* 177 */     Preconditions.checkArgument(isOrderingCompatible(endpoints), "Mismatch: endpoints' ordering is not compatible with directionality of the graph");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean isOrderingCompatible(EndpointPair<?> endpoints) {
/* 185 */     return (endpoints.isOrdered() == isDirected());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/AbstractBaseGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */