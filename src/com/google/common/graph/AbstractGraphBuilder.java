/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.base.Optional;
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
/*    */ abstract class AbstractGraphBuilder<N>
/*    */ {
/*    */   final boolean directed;
/*    */   boolean allowsSelfLoops = false;
/* 30 */   ElementOrder<N> nodeOrder = ElementOrder.insertion();
/* 31 */   ElementOrder<N> incidentEdgeOrder = ElementOrder.unordered();
/*    */   
/* 33 */   Optional<Integer> expectedNodeCount = Optional.absent();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   AbstractGraphBuilder(boolean directed) {
/* 42 */     this.directed = directed;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/AbstractGraphBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */