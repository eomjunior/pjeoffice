/*     */ package com.itextpdf.text.pdf.parser.clipper;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public class PolyNode
/*     */ {
/*     */   private PolyNode parent;
/*     */   
/*     */   enum NodeType
/*     */   {
/*  56 */     ANY, OPEN, CLOSED;
/*     */   }
/*     */ 
/*     */   
/*  60 */   private final Path polygon = new Path();
/*     */   private int index;
/*     */   private Clipper.JoinType joinType;
/*     */   private Clipper.EndType endType;
/*  64 */   protected final List<PolyNode> childs = new ArrayList<PolyNode>();
/*     */   private boolean isOpen;
/*     */   
/*     */   public void addChild(PolyNode child) {
/*  68 */     int cnt = this.childs.size();
/*  69 */     this.childs.add(child);
/*  70 */     child.parent = this;
/*  71 */     child.index = cnt;
/*     */   }
/*     */   
/*     */   public int getChildCount() {
/*  75 */     return this.childs.size();
/*     */   }
/*     */   
/*     */   public List<PolyNode> getChilds() {
/*  79 */     return Collections.unmodifiableList(this.childs);
/*     */   }
/*     */   
/*     */   public List<Point.LongPoint> getContour() {
/*  83 */     return this.polygon;
/*     */   }
/*     */   
/*     */   public Clipper.EndType getEndType() {
/*  87 */     return this.endType;
/*     */   }
/*     */   
/*     */   public Clipper.JoinType getJoinType() {
/*  91 */     return this.joinType;
/*     */   }
/*     */   
/*     */   public PolyNode getNext() {
/*  95 */     if (!this.childs.isEmpty()) {
/*  96 */       return this.childs.get(0);
/*     */     }
/*     */     
/*  99 */     return getNextSiblingUp();
/*     */   }
/*     */ 
/*     */   
/*     */   private PolyNode getNextSiblingUp() {
/* 104 */     if (this.parent == null) {
/* 105 */       return null;
/*     */     }
/* 107 */     if (this.index == this.parent.childs.size() - 1) {
/* 108 */       return this.parent.getNextSiblingUp();
/*     */     }
/*     */     
/* 111 */     return this.parent.childs.get(this.index + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public PolyNode getParent() {
/* 116 */     return this.parent;
/*     */   }
/*     */   
/*     */   public Path getPolygon() {
/* 120 */     return this.polygon;
/*     */   }
/*     */   
/*     */   public boolean isHole() {
/* 124 */     return isHoleNode();
/*     */   }
/*     */   
/*     */   private boolean isHoleNode() {
/* 128 */     boolean result = true;
/* 129 */     PolyNode node = this.parent;
/* 130 */     while (node != null) {
/* 131 */       result = !result;
/* 132 */       node = node.parent;
/*     */     } 
/* 134 */     return result;
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 138 */     return this.isOpen;
/*     */   }
/*     */   
/*     */   public void setEndType(Clipper.EndType value) {
/* 142 */     this.endType = value;
/*     */   }
/*     */   
/*     */   public void setJoinType(Clipper.JoinType value) {
/* 146 */     this.joinType = value;
/*     */   }
/*     */   
/*     */   public void setOpen(boolean isOpen) {
/* 150 */     this.isOpen = isOpen;
/*     */   }
/*     */   
/*     */   public void setParent(PolyNode n) {
/* 154 */     this.parent = n;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/clipper/PolyNode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */