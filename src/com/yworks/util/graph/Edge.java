/*    */ package com.yworks.util.graph;
/*    */ 
/*    */ class Edge
/*    */ {
/*    */   private final Node source;
/*    */   private final Node target;
/*    */   private Edge nextInEdge;
/*  8 */   private Edge nextOutEdge = null;
/*    */   
/*    */   public Edge(Node src, Node tgt) {
/* 11 */     this.source = src;
/* 12 */     this.target = tgt;
/*    */   }
/*    */   
/*    */   public Node getSource() {
/* 16 */     return this.source;
/*    */   }
/*    */   
/*    */   public Node getTarget() {
/* 20 */     return this.target;
/*    */   }
/*    */   
/*    */   public Edge getNextInEdge() {
/* 24 */     return this.nextInEdge;
/*    */   }
/*    */   
/*    */   public Edge getNextOutEdge() {
/* 28 */     return this.nextOutEdge;
/*    */   }
/*    */   
/*    */   public void setNextInEdge(Edge nextInEdge) {
/* 32 */     this.nextInEdge = nextInEdge;
/*    */   }
/*    */   
/*    */   public void setNextOutEdge(Edge nextOutEdge) {
/* 36 */     this.nextOutEdge = nextOutEdge;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/graph/Edge.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */