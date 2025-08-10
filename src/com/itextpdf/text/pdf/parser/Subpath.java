/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import com.itextpdf.awt.geom.Point2D;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class Subpath
/*     */ {
/*     */   private Point2D startPoint;
/*  61 */   private List<Shape> segments = new ArrayList<Shape>();
/*     */ 
/*     */   
/*     */   private boolean closed;
/*     */ 
/*     */ 
/*     */   
/*     */   public Subpath() {}
/*     */ 
/*     */   
/*     */   public Subpath(Subpath subpath) {
/*  72 */     this.startPoint = subpath.startPoint;
/*  73 */     this.segments.addAll(subpath.getSegments());
/*  74 */     this.closed = subpath.closed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Subpath(Point2D startPoint) {
/*  81 */     this((float)startPoint.getX(), (float)startPoint.getY());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Subpath(float startPointX, float startPointY) {
/*  88 */     this.startPoint = (Point2D)new Point2D.Float(startPointX, startPointY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartPoint(Point2D startPoint) {
/*  96 */     setStartPoint((float)startPoint.getX(), (float)startPoint.getY());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartPoint(float x, float y) {
/* 105 */     this.startPoint = (Point2D)new Point2D.Float(x, y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Point2D getStartPoint() {
/* 112 */     return this.startPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Point2D getLastPoint() {
/* 119 */     Point2D lastPoint = this.startPoint;
/*     */     
/* 121 */     if (this.segments.size() > 0 && !this.closed) {
/* 122 */       Shape shape = this.segments.get(this.segments.size() - 1);
/* 123 */       lastPoint = shape.getBasePoints().get(shape.getBasePoints().size() - 1);
/*     */     } 
/*     */     
/* 126 */     return lastPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSegment(Shape segment) {
/* 135 */     if (this.closed) {
/*     */       return;
/*     */     }
/*     */     
/* 139 */     if (isSinglePointOpen()) {
/* 140 */       this.startPoint = segment.getBasePoints().get(0);
/*     */     }
/*     */     
/* 143 */     this.segments.add(segment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Shape> getSegments() {
/* 151 */     return this.segments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 159 */     return (this.startPoint == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSinglePointOpen() {
/* 167 */     return (this.segments.size() == 0 && !this.closed);
/*     */   }
/*     */   
/*     */   public boolean isSinglePointClosed() {
/* 171 */     return (this.segments.size() == 0 && this.closed);
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
/*     */   public boolean isClosed() {
/* 183 */     return this.closed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClosed(boolean closed) {
/* 190 */     this.closed = closed;
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
/*     */   public boolean isDegenerate() {
/* 202 */     if (this.segments.size() > 0 && this.closed) {
/* 203 */       return false;
/*     */     }
/*     */     
/* 206 */     for (Shape segment : this.segments) {
/* 207 */       Set<Point2D> points = new HashSet<Point2D>(segment.getBasePoints());
/*     */ 
/*     */       
/* 210 */       if (points.size() != 1) {
/* 211 */         return false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 216 */     return (this.segments.size() > 0 || this.closed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Point2D> getPiecewiseLinearApproximation() {
/* 225 */     List<Point2D> result = new ArrayList<Point2D>();
/*     */     
/* 227 */     if (this.segments.size() == 0) {
/* 228 */       return result;
/*     */     }
/*     */     
/* 231 */     if (this.segments.get(0) instanceof BezierCurve) {
/* 232 */       result.addAll(((BezierCurve)this.segments.get(0)).getPiecewiseLinearApproximation());
/*     */     } else {
/* 234 */       result.addAll(((Shape)this.segments.get(0)).getBasePoints());
/*     */     } 
/*     */     
/* 237 */     for (int i = 1; i < this.segments.size(); i++) {
/*     */       List<Point2D> segApprox;
/*     */       
/* 240 */       if (this.segments.get(i) instanceof BezierCurve) {
/* 241 */         segApprox = ((BezierCurve)this.segments.get(i)).getPiecewiseLinearApproximation();
/* 242 */         segApprox = segApprox.subList(1, segApprox.size());
/*     */       } else {
/* 244 */         segApprox = ((Shape)this.segments.get(i)).getBasePoints();
/* 245 */         segApprox = segApprox.subList(1, segApprox.size());
/*     */       } 
/*     */       
/* 248 */       result.addAll(segApprox);
/*     */     } 
/*     */     
/* 251 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/Subpath.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */