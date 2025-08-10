/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.collect.Maps;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @Beta
/*     */ public abstract class AbstractValueGraph<N, V>
/*     */   extends AbstractBaseGraph<N>
/*     */   implements ValueGraph<N, V>
/*     */ {
/*     */   public Graph<N> asGraph() {
/*  47 */     return new AbstractGraph<N>()
/*     */       {
/*     */         public Set<N> nodes() {
/*  50 */           return AbstractValueGraph.this.nodes();
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<EndpointPair<N>> edges() {
/*  55 */           return AbstractValueGraph.this.edges();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isDirected() {
/*  60 */           return AbstractValueGraph.this.isDirected();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean allowsSelfLoops() {
/*  65 */           return AbstractValueGraph.this.allowsSelfLoops();
/*     */         }
/*     */ 
/*     */         
/*     */         public ElementOrder<N> nodeOrder() {
/*  70 */           return AbstractValueGraph.this.nodeOrder();
/*     */         }
/*     */ 
/*     */         
/*     */         public ElementOrder<N> incidentEdgeOrder() {
/*  75 */           return AbstractValueGraph.this.incidentEdgeOrder();
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> adjacentNodes(N node) {
/*  80 */           return AbstractValueGraph.this.adjacentNodes(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> predecessors(N node) {
/*  85 */           return AbstractValueGraph.this.predecessors(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> successors(N node) {
/*  90 */           return AbstractValueGraph.this.successors(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public int degree(N node) {
/*  95 */           return AbstractValueGraph.this.degree(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public int inDegree(N node) {
/* 100 */           return AbstractValueGraph.this.inDegree(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public int outDegree(N node) {
/* 105 */           return AbstractValueGraph.this.outDegree(node);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<V> edgeValue(N nodeU, N nodeV) {
/* 112 */     return Optional.ofNullable(edgeValueOrDefault(nodeU, nodeV, null));
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<V> edgeValue(EndpointPair<N> endpoints) {
/* 117 */     return Optional.ofNullable(edgeValueOrDefault(endpoints, null));
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(@CheckForNull Object obj) {
/* 122 */     if (obj == this) {
/* 123 */       return true;
/*     */     }
/* 125 */     if (!(obj instanceof ValueGraph)) {
/* 126 */       return false;
/*     */     }
/* 128 */     ValueGraph<?, ?> other = (ValueGraph<?, ?>)obj;
/*     */     
/* 130 */     return (isDirected() == other.isDirected() && 
/* 131 */       nodes().equals(other.nodes()) && 
/* 132 */       edgeValueMap(this).equals(edgeValueMap(other)));
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 137 */     return edgeValueMap(this).hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 143 */     return "isDirected: " + 
/* 144 */       isDirected() + ", allowsSelfLoops: " + 
/*     */       
/* 146 */       allowsSelfLoops() + ", nodes: " + 
/*     */       
/* 148 */       nodes() + ", edges: " + 
/*     */       
/* 150 */       edgeValueMap(this);
/*     */   }
/*     */   
/*     */   private static <N, V> Map<EndpointPair<N>, V> edgeValueMap(ValueGraph<N, V> graph) {
/* 154 */     return Maps.asMap(graph
/* 155 */         .edges(), edge -> Objects.requireNonNull(graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), null)));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/AbstractValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */