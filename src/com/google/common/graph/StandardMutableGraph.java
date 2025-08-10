/*    */ package com.google.common.graph;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ final class StandardMutableGraph<N>
/*    */   extends ForwardingGraph<N>
/*    */   implements MutableGraph<N>
/*    */ {
/*    */   private final MutableValueGraph<N, GraphConstants.Presence> backingValueGraph;
/*    */   
/*    */   StandardMutableGraph(AbstractGraphBuilder<? super N> builder) {
/* 37 */     this.backingValueGraph = new StandardMutableValueGraph<>(builder);
/*    */   }
/*    */ 
/*    */   
/*    */   BaseGraph<N> delegate() {
/* 42 */     return this.backingValueGraph;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean addNode(N node) {
/* 47 */     return this.backingValueGraph.addNode(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean putEdge(N nodeU, N nodeV) {
/* 52 */     return (this.backingValueGraph.putEdgeValue(nodeU, nodeV, GraphConstants.Presence.EDGE_EXISTS) == null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean putEdge(EndpointPair<N> endpoints) {
/* 57 */     validateEndpoints(endpoints);
/* 58 */     return putEdge(endpoints.nodeU(), endpoints.nodeV());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeNode(N node) {
/* 63 */     return this.backingValueGraph.removeNode(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeEdge(N nodeU, N nodeV) {
/* 68 */     return (this.backingValueGraph.removeEdge(nodeU, nodeV) != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeEdge(EndpointPair<N> endpoints) {
/* 73 */     validateEndpoints(endpoints);
/* 74 */     return removeEdge(endpoints.nodeU(), endpoints.nodeV());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/StandardMutableGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */