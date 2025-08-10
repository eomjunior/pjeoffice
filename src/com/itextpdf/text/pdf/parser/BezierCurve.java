/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import com.itextpdf.awt.geom.Point2D;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BezierCurve
/*     */   implements Shape
/*     */ {
/*  62 */   public static double curveCollinearityEpsilon = 1.0E-30D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public static double distanceToleranceSquare = 0.025D;
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
/*  84 */   public static double distanceToleranceManhattan = 0.4D;
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<Point2D> controlPoints;
/*     */ 
/*     */ 
/*     */   
/*     */   public BezierCurve(List<Point2D> controlPoints) {
/*  93 */     this.controlPoints = new ArrayList<Point2D>(controlPoints);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Point2D> getBasePoints() {
/* 100 */     return this.controlPoints;
/*     */   }
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
/*     */   public List<Point2D> getPiecewiseLinearApproximation() {
/* 113 */     List<Point2D> points = new ArrayList<Point2D>();
/* 114 */     points.add(this.controlPoints.get(0));
/*     */     
/* 116 */     recursiveApproximation(((Point2D)this.controlPoints.get(0)).getX(), ((Point2D)this.controlPoints.get(0)).getY(), ((Point2D)this.controlPoints
/* 117 */         .get(1)).getX(), ((Point2D)this.controlPoints.get(1)).getY(), ((Point2D)this.controlPoints
/* 118 */         .get(2)).getX(), ((Point2D)this.controlPoints.get(2)).getY(), ((Point2D)this.controlPoints
/* 119 */         .get(3)).getX(), ((Point2D)this.controlPoints.get(3)).getY(), points);
/*     */     
/* 121 */     points.add(this.controlPoints.get(this.controlPoints.size() - 1));
/* 122 */     return points;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void recursiveApproximation(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, List<Point2D> points) {
/* 129 */     double x12 = (x1 + x2) / 2.0D;
/* 130 */     double y12 = (y1 + y2) / 2.0D;
/* 131 */     double x23 = (x2 + x3) / 2.0D;
/* 132 */     double y23 = (y2 + y3) / 2.0D;
/* 133 */     double x34 = (x3 + x4) / 2.0D;
/* 134 */     double y34 = (y3 + y4) / 2.0D;
/* 135 */     double x123 = (x12 + x23) / 2.0D;
/* 136 */     double y123 = (y12 + y23) / 2.0D;
/* 137 */     double x234 = (x23 + x34) / 2.0D;
/* 138 */     double y234 = (y23 + y34) / 2.0D;
/* 139 */     double x1234 = (x123 + x234) / 2.0D;
/* 140 */     double y1234 = (y123 + y234) / 2.0D;
/*     */     
/* 142 */     double dx = x4 - x1;
/* 143 */     double dy = y4 - y1;
/*     */ 
/*     */ 
/*     */     
/* 147 */     double d2 = Math.abs((x2 - x4) * dy - (y2 - y4) * dx);
/*     */ 
/*     */     
/* 150 */     double d3 = Math.abs((x3 - x4) * dy - (y3 - y4) * dx);
/*     */ 
/*     */ 
/*     */     
/* 154 */     if (d2 > curveCollinearityEpsilon || d3 > curveCollinearityEpsilon) {
/*     */ 
/*     */       
/* 157 */       if ((d2 + d3) * (d2 + d3) <= distanceToleranceSquare * (dx * dx + dy * dy)) {
/* 158 */         points.add(new Point2D.Double(x1234, y1234));
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/* 163 */     } else if (Math.abs(x1 + x3 - x2 - x2) + Math.abs(y1 + y3 - y2 - y2) + 
/* 164 */       Math.abs(x2 + x4 - x3 - x3) + Math.abs(y2 + y4 - y3 - y3) <= distanceToleranceManhattan) {
/* 165 */       points.add(new Point2D.Double(x1234, y1234));
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 170 */     recursiveApproximation(x1, y1, x12, y12, x123, y123, x1234, y1234, points);
/* 171 */     recursiveApproximation(x1234, y1234, x234, y234, x34, y34, x4, y4, points);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/BezierCurve.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */