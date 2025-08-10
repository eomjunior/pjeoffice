/*    */ package com.yworks.util.graph;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ class Node
/*    */ {
/* 10 */   private final List<Edge> inEdges = new ArrayList<>();
/* 11 */   private final List<Edge> outEdges = new ArrayList<>();
/*    */   
/*    */   public List<Edge> getInEdges() {
/* 14 */     return this.inEdges;
/*    */   }
/*    */   
/*    */   public List<Edge> getOutEdges() {
/* 18 */     return this.outEdges;
/*    */   }
/*    */   
/*    */   public void addInEdge(Object edge) {
/* 22 */     this.inEdges.add((Edge)edge);
/*    */   }
/*    */   
/*    */   public void addOutEdge(Object edge) {
/* 26 */     this.outEdges.add((Edge)edge);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/graph/Node.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */