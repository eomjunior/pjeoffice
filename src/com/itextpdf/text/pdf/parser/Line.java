/*    */ package com.itextpdf.text.pdf.parser;
/*    */ 
/*    */ import com.itextpdf.awt.geom.Point2D;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class Line
/*    */   implements Shape
/*    */ {
/*    */   private final Point2D p1;
/*    */   private final Point2D p2;
/*    */   
/*    */   public Line() {
/* 65 */     this(0.0F, 0.0F, 0.0F, 0.0F);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Line(float x1, float y1, float x2, float y2) {
/* 72 */     this.p1 = (Point2D)new Point2D.Float(x1, y1);
/* 73 */     this.p2 = (Point2D)new Point2D.Float(x2, y2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Line(Point2D p1, Point2D p2) {
/* 80 */     this((float)p1.getX(), (float)p1.getY(), (float)p2.getX(), (float)p2.getY());
/*    */   }
/*    */   
/*    */   public List<Point2D> getBasePoints() {
/* 84 */     List<Point2D> basePoints = new ArrayList<Point2D>(2);
/* 85 */     basePoints.add(this.p1);
/* 86 */     basePoints.add(this.p2);
/*    */     
/* 88 */     return basePoints;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/Line.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */