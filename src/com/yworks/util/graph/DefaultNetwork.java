/*     */ package com.yworks.util.graph;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class DefaultNetwork
/*     */   implements Network {
/*   9 */   private final List<Node> nodes = new ArrayList<>();
/*  10 */   private final List<Edge> edges = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public Object createNode() {
/*  14 */     Node node = new Node();
/*  15 */     this.nodes.add(node);
/*  16 */     return node;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object createEdge(Object source, Object target) {
/*  21 */     Node src = (Node)source;
/*  22 */     Node tgt = (Node)target;
/*  23 */     Edge edge = new Edge(src, tgt);
/*  24 */     if (src.getOutEdges().size() > 0) ((Edge)src.getOutEdges().get(src.getOutEdges().size() - 1)).setNextOutEdge(edge); 
/*  25 */     src.addOutEdge(edge);
/*  26 */     if (tgt.getInEdges().size() > 0) ((Edge)tgt.getInEdges().get(tgt.getInEdges().size() - 1)).setNextInEdge(edge); 
/*  27 */     tgt.addInEdge(edge);
/*  28 */     this.edges.add(edge);
/*  29 */     return edge;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getSource(Object edge) {
/*  34 */     return ((Edge)edge).getSource();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getTarget(Object edge) {
/*  39 */     return ((Edge)edge).getTarget();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator nodes() {
/*  44 */     return this.nodes.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer nodesSize() {
/*  49 */     return Integer.valueOf(this.nodes.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator edges() {
/*  54 */     return this.edges.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator inEdges(Object node) {
/*  59 */     return ((Node)node).getInEdges().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator outEdges(Object node) {
/*  64 */     return ((Node)node).getOutEdges().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object firstInEdge(Object node) {
/*  69 */     Node n = (Node)node;
/*  70 */     return (n.getInEdges().size() > 0) ? n.getInEdges().get(0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object firstOutEdge(Object node) {
/*  75 */     Node n = (Node)node;
/*  76 */     return (n.getOutEdges().size() > 0) ? n.getOutEdges().get(0) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object nextInEdge(Object edge) {
/*  81 */     return ((Edge)edge).getNextInEdge();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object nextOutEdge(Object edge) {
/*  86 */     return ((Edge)edge).getNextOutEdge();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator edgesConnecting(Object source, Object target) {
/*  91 */     Node src = (Node)source;
/*  92 */     Node tgt = (Node)target;
/*  93 */     List<Edge> edgesConnecting = new ArrayList<>(src.getOutEdges().size());
/*  94 */     for (Edge e : src.getOutEdges()) {
/*  95 */       if (e.getTarget() == tgt) {
/*  96 */         edgesConnecting.add(e);
/*     */       }
/*     */     } 
/*  99 */     return edgesConnecting.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object opposite(Object edge, Object node) {
/* 104 */     if (getSource(edge).equals(node)) {
/* 105 */       return getTarget(edge);
/*     */     }
/* 107 */     return getSource(edge);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/graph/DefaultNetwork.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */