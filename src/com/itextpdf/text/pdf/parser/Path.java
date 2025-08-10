/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import com.itextpdf.awt.geom.Point2D;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ public class Path
/*     */ {
/*     */   private static final String START_PATH_ERR_MSG = "Path shall start with \"re\" or \"m\" operator";
/*  64 */   private List<Subpath> subpaths = new ArrayList<Subpath>();
/*     */   
/*     */   private Point2D currentPoint;
/*     */   
/*     */   public Path() {}
/*     */   
/*     */   public Path(List<? extends Subpath> subpaths) {
/*  71 */     addSubpaths(subpaths);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Subpath> getSubpaths() {
/*  78 */     return this.subpaths;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSubpath(Subpath subpath) {
/*  87 */     this.subpaths.add(subpath);
/*  88 */     this.currentPoint = subpath.getLastPoint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSubpaths(List<? extends Subpath> subpaths) {
/*  97 */     if (subpaths.size() > 0) {
/*  98 */       this.subpaths.addAll(subpaths);
/*  99 */       this.currentPoint = ((Subpath)this.subpaths.get(subpaths.size() - 1)).getLastPoint();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Point2D getCurrentPoint() {
/* 109 */     return this.currentPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void moveTo(float x, float y) {
/* 116 */     this.currentPoint = (Point2D)new Point2D.Float(x, y);
/* 117 */     Subpath lastSubpath = getLastSubpath();
/*     */     
/* 119 */     if (lastSubpath != null && lastSubpath.isSinglePointOpen()) {
/* 120 */       lastSubpath.setStartPoint(this.currentPoint);
/*     */     } else {
/* 122 */       this.subpaths.add(new Subpath(this.currentPoint));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void lineTo(float x, float y) {
/* 130 */     if (this.currentPoint == null) {
/* 131 */       throw new RuntimeException("Path shall start with \"re\" or \"m\" operator");
/*     */     }
/*     */     
/* 134 */     Point2D.Float float_ = new Point2D.Float(x, y);
/* 135 */     getLastSubpath().addSegment(new Line(this.currentPoint, (Point2D)float_));
/* 136 */     this.currentPoint = (Point2D)float_;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
/* 144 */     if (this.currentPoint == null) {
/* 145 */       throw new RuntimeException("Path shall start with \"re\" or \"m\" operator");
/*     */     }
/*     */     
/* 148 */     Point2D.Float float_1 = new Point2D.Float(x1, y1);
/* 149 */     Point2D.Float float_2 = new Point2D.Float(x2, y2);
/* 150 */     Point2D.Float float_3 = new Point2D.Float(x3, y3);
/*     */     
/* 152 */     List<Point2D> controlPoints = new ArrayList<Point2D>(Arrays.asList(new Point2D[] { this.currentPoint, (Point2D)float_1, (Point2D)float_2, (Point2D)float_3 }));
/* 153 */     getLastSubpath().addSegment(new BezierCurve(controlPoints));
/*     */     
/* 155 */     this.currentPoint = (Point2D)float_3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void curveTo(float x2, float y2, float x3, float y3) {
/* 164 */     if (this.currentPoint == null) {
/* 165 */       throw new RuntimeException("Path shall start with \"re\" or \"m\" operator");
/*     */     }
/*     */     
/* 168 */     curveTo((float)this.currentPoint.getX(), (float)this.currentPoint.getY(), x2, y2, x3, y3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void curveFromTo(float x1, float y1, float x3, float y3) {
/* 177 */     if (this.currentPoint == null) {
/* 178 */       throw new RuntimeException("Path shall start with \"re\" or \"m\" operator");
/*     */     }
/*     */     
/* 181 */     curveTo(x1, y1, x3, y3, x3, y3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rectangle(float x, float y, float w, float h) {
/* 188 */     moveTo(x, y);
/* 189 */     lineTo(x + w, y);
/* 190 */     lineTo(x + w, y + h);
/* 191 */     lineTo(x, y + h);
/* 192 */     closeSubpath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeSubpath() {
/* 199 */     Subpath lastSubpath = getLastSubpath();
/* 200 */     lastSubpath.setClosed(true);
/*     */     
/* 202 */     Point2D startPoint = lastSubpath.getStartPoint();
/* 203 */     moveTo((float)startPoint.getX(), (float)startPoint.getY());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void closeAllSubpaths() {
/* 210 */     for (Subpath subpath : this.subpaths) {
/* 211 */       subpath.setClosed(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Integer> replaceCloseWithLine() {
/* 222 */     List<Integer> modifiedSubpathsIndices = new ArrayList<Integer>();
/* 223 */     int i = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     for (Subpath subpath : this.subpaths) {
/* 230 */       if (subpath.isClosed()) {
/* 231 */         subpath.setClosed(false);
/* 232 */         subpath.addSegment(new Line(subpath.getLastPoint(), subpath.getStartPoint()));
/* 233 */         modifiedSubpathsIndices.add(Integer.valueOf(i));
/*     */       } 
/*     */       
/* 236 */       i++;
/*     */     } 
/*     */     
/* 239 */     return modifiedSubpathsIndices;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 246 */     return (this.subpaths.size() == 0);
/*     */   }
/*     */   
/*     */   private Subpath getLastSubpath() {
/* 250 */     return (this.subpaths.size() > 0) ? this.subpaths.get(this.subpaths.size() - 1) : null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/Path.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */