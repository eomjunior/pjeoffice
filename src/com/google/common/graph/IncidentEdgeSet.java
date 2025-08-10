/*    */ package com.google.common.graph;
/*    */ 
/*    */ import java.util.AbstractSet;
/*    */ import java.util.Set;
/*    */ import javax.annotation.CheckForNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ abstract class IncidentEdgeSet<N>
/*    */   extends AbstractSet<EndpointPair<N>>
/*    */ {
/*    */   final N node;
/*    */   final BaseGraph<N> graph;
/*    */   
/*    */   IncidentEdgeSet(BaseGraph<N> graph, N node) {
/* 33 */     this.graph = graph;
/* 34 */     this.node = node;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(@CheckForNull Object o) {
/* 39 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 44 */     if (this.graph.isDirected()) {
/* 45 */       return this.graph.inDegree(this.node) + this.graph
/* 46 */         .outDegree(this.node) - (
/* 47 */         this.graph.successors(this.node).contains(this.node) ? 1 : 0);
/*    */     }
/* 49 */     return this.graph.adjacentNodes(this.node).size();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean contains(@CheckForNull Object obj) {
/* 55 */     if (!(obj instanceof EndpointPair)) {
/* 56 */       return false;
/*    */     }
/* 58 */     EndpointPair<?> endpointPair = (EndpointPair)obj;
/*    */     
/* 60 */     if (this.graph.isDirected()) {
/* 61 */       if (!endpointPair.isOrdered()) {
/* 62 */         return false;
/*    */       }
/*    */       
/* 65 */       Object source = endpointPair.source();
/* 66 */       Object target = endpointPair.target();
/* 67 */       return ((this.node.equals(source) && this.graph.successors(this.node).contains(target)) || (this.node
/* 68 */         .equals(target) && this.graph.predecessors(this.node).contains(source)));
/*    */     } 
/* 70 */     if (endpointPair.isOrdered()) {
/* 71 */       return false;
/*    */     }
/* 73 */     Set<N> adjacent = this.graph.adjacentNodes(this.node);
/* 74 */     Object nodeU = endpointPair.nodeU();
/* 75 */     Object nodeV = endpointPair.nodeV();
/*    */     
/* 77 */     return ((this.node.equals(nodeV) && adjacent.contains(nodeU)) || (this.node
/* 78 */       .equals(nodeU) && adjacent.contains(nodeV)));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/IncidentEdgeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */