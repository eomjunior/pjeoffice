/*     */ package com.itextpdf.text.pdf.parser.clipper;
/*     */ 
/*     */ import java.util.ArrayList;
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
/*     */ public class Paths
/*     */   extends ArrayList<Path>
/*     */ {
/*     */   private static final long serialVersionUID = 1910552127810480852L;
/*     */   
/*     */   public static Paths closedPathsFromPolyTree(PolyTree polytree) {
/*  88 */     Paths result = new Paths();
/*     */     
/*  90 */     result.addPolyNode(polytree, PolyNode.NodeType.CLOSED);
/*  91 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Paths makePolyTreeToPaths(PolyTree polytree) {
/*  96 */     Paths result = new Paths();
/*     */     
/*  98 */     result.addPolyNode(polytree, PolyNode.NodeType.ANY);
/*  99 */     return result;
/*     */   }
/*     */   
/*     */   public static Paths openPathsFromPolyTree(PolyTree polytree) {
/* 103 */     Paths result = new Paths();
/*     */     
/* 105 */     for (PolyNode c : polytree.getChilds()) {
/* 106 */       if (c.isOpen()) {
/* 107 */         result.add(c.getPolygon());
/*     */       }
/*     */     } 
/* 110 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Paths() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Paths(int initialCapacity) {
/* 123 */     super(initialCapacity);
/*     */   }
/*     */   
/*     */   public void addPolyNode(PolyNode polynode, PolyNode.NodeType nt) {
/* 127 */     boolean match = true;
/* 128 */     switch (nt) {
/*     */       case OPEN:
/*     */         return;
/*     */       case CLOSED:
/* 132 */         match = !polynode.isOpen();
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 138 */     if (polynode.getPolygon().size() > 0 && match) {
/* 139 */       add(polynode.getPolygon());
/*     */     }
/* 141 */     for (PolyNode pn : polynode.getChilds()) {
/* 142 */       addPolyNode(pn, nt);
/*     */     }
/*     */   }
/*     */   
/*     */   public Paths cleanPolygons() {
/* 147 */     return cleanPolygons(1.415D);
/*     */   }
/*     */   
/*     */   public Paths cleanPolygons(double distance) {
/* 151 */     Paths result = new Paths(size());
/* 152 */     for (int i = 0; i < size(); i++) {
/* 153 */       result.add(get(i).cleanPolygon(distance));
/*     */     }
/* 155 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public LongRect getBounds() {
/* 160 */     int i = 0;
/* 161 */     int cnt = size();
/* 162 */     LongRect result = new LongRect();
/* 163 */     while (i < cnt && get(i).isEmpty()) {
/* 164 */       i++;
/*     */     }
/* 166 */     if (i == cnt) {
/* 167 */       return result;
/*     */     }
/*     */     
/* 170 */     result.left = get(i).get(0).getX();
/* 171 */     result.right = result.left;
/* 172 */     result.top = get(i).get(0).getY();
/* 173 */     result.bottom = result.top;
/* 174 */     for (; i < cnt; i++) {
/* 175 */       for (int j = 0; j < get(i).size(); j++) {
/* 176 */         if (get(i).get(j).getX() < result.left) {
/* 177 */           result.left = get(i).get(j).getX();
/*     */         }
/* 179 */         else if (get(i).get(j).getX() > result.right) {
/* 180 */           result.right = get(i).get(j).getX();
/*     */         } 
/* 182 */         if (get(i).get(j).getY() < result.top) {
/* 183 */           result.top = get(i).get(j).getY();
/*     */         }
/* 185 */         else if (get(i).get(j).getY() > result.bottom) {
/* 186 */           result.bottom = get(i).get(j).getY();
/*     */         } 
/*     */       } 
/*     */     } 
/* 190 */     return result;
/*     */   }
/*     */   
/*     */   public void reversePaths() {
/* 194 */     for (Path poly : this)
/* 195 */       poly.reverse(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/clipper/Paths.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */