/*     */ package com.itextpdf.awt.geom;
/*     */ 
/*     */ import com.itextpdf.awt.geom.gl.Crossing;
/*     */ import com.itextpdf.awt.geom.misc.Messages;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class QuadCurve2D
/*     */   implements Shape, Cloneable
/*     */ {
/*     */   public abstract double getX1();
/*     */   
/*     */   public abstract double getY1();
/*     */   
/*     */   public abstract Point2D getP1();
/*     */   
/*     */   public abstract double getCtrlX();
/*     */   
/*     */   public abstract double getCtrlY();
/*     */   
/*     */   public abstract Point2D getCtrlPt();
/*     */   
/*     */   public abstract double getX2();
/*     */   
/*     */   public abstract double getY2();
/*     */   
/*     */   public abstract Point2D getP2();
/*     */   
/*     */   public abstract void setCurve(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6);
/*     */   
/*     */   public static class Float
/*     */     extends QuadCurve2D
/*     */   {
/*     */     public float x1;
/*     */     public float y1;
/*     */     public float ctrlx;
/*     */     public float ctrly;
/*     */     public float x2;
/*     */     public float y2;
/*     */     
/*     */     public Float() {}
/*     */     
/*     */     public Float(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
/*  46 */       setCurve(x1, y1, ctrlx, ctrly, x2, y2);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX1() {
/*  51 */       return this.x1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY1() {
/*  56 */       return this.y1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getCtrlX() {
/*  61 */       return this.ctrlx;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getCtrlY() {
/*  66 */       return this.ctrly;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX2() {
/*  71 */       return this.x2;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY2() {
/*  76 */       return this.y2;
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getP1() {
/*  81 */       return new Point2D.Float(this.x1, this.y1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getCtrlPt() {
/*  86 */       return new Point2D.Float(this.ctrlx, this.ctrly);
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getP2() {
/*  91 */       return new Point2D.Float(this.x2, this.y2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCurve(double x1, double y1, double ctrlx, double ctrly, double x2, double y2) {
/*  96 */       this.x1 = (float)x1;
/*  97 */       this.y1 = (float)y1;
/*  98 */       this.ctrlx = (float)ctrlx;
/*  99 */       this.ctrly = (float)ctrly;
/* 100 */       this.x2 = (float)x2;
/* 101 */       this.y2 = (float)y2;
/*     */     }
/*     */     
/*     */     public void setCurve(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
/* 105 */       this.x1 = x1;
/* 106 */       this.y1 = y1;
/* 107 */       this.ctrlx = ctrlx;
/* 108 */       this.ctrly = ctrly;
/* 109 */       this.x2 = x2;
/* 110 */       this.y2 = y2;
/*     */     }
/*     */     
/*     */     public Rectangle2D getBounds2D() {
/* 114 */       float rx0 = Math.min(Math.min(this.x1, this.x2), this.ctrlx);
/* 115 */       float ry0 = Math.min(Math.min(this.y1, this.y2), this.ctrly);
/* 116 */       float rx1 = Math.max(Math.max(this.x1, this.x2), this.ctrlx);
/* 117 */       float ry1 = Math.max(Math.max(this.y1, this.y2), this.ctrly);
/* 118 */       return new Rectangle2D.Float(rx0, ry0, rx1 - rx0, ry1 - ry0);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Double
/*     */     extends QuadCurve2D
/*     */   {
/*     */     public double x1;
/*     */     public double y1;
/*     */     public double ctrlx;
/*     */     public double ctrly;
/*     */     public double x2;
/*     */     public double y2;
/*     */     
/*     */     public Double() {}
/*     */     
/*     */     public Double(double x1, double y1, double ctrlx, double ctrly, double x2, double y2) {
/* 135 */       setCurve(x1, y1, ctrlx, ctrly, x2, y2);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX1() {
/* 140 */       return this.x1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY1() {
/* 145 */       return this.y1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getCtrlX() {
/* 150 */       return this.ctrlx;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getCtrlY() {
/* 155 */       return this.ctrly;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX2() {
/* 160 */       return this.x2;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY2() {
/* 165 */       return this.y2;
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getP1() {
/* 170 */       return new Point2D.Double(this.x1, this.y1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getCtrlPt() {
/* 175 */       return new Point2D.Double(this.ctrlx, this.ctrly);
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getP2() {
/* 180 */       return new Point2D.Double(this.x2, this.y2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCurve(double x1, double y1, double ctrlx, double ctrly, double x2, double y2) {
/* 185 */       this.x1 = x1;
/* 186 */       this.y1 = y1;
/* 187 */       this.ctrlx = ctrlx;
/* 188 */       this.ctrly = ctrly;
/* 189 */       this.x2 = x2;
/* 190 */       this.y2 = y2;
/*     */     }
/*     */     
/*     */     public Rectangle2D getBounds2D() {
/* 194 */       double rx0 = Math.min(Math.min(this.x1, this.x2), this.ctrlx);
/* 195 */       double ry0 = Math.min(Math.min(this.y1, this.y2), this.ctrly);
/* 196 */       double rx1 = Math.max(Math.max(this.x1, this.x2), this.ctrlx);
/* 197 */       double ry1 = Math.max(Math.max(this.y1, this.y2), this.ctrly);
/* 198 */       return new Rectangle2D.Double(rx0, ry0, rx1 - rx0, ry1 - ry0);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class Iterator
/*     */     implements PathIterator
/*     */   {
/*     */     QuadCurve2D c;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     AffineTransform t;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int index;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator(QuadCurve2D q, AffineTransform t) {
/* 228 */       this.c = q;
/* 229 */       this.t = t;
/*     */     }
/*     */     
/*     */     public int getWindingRule() {
/* 233 */       return 1;
/*     */     }
/*     */     
/*     */     public boolean isDone() {
/* 237 */       return (this.index > 1);
/*     */     }
/*     */     
/*     */     public void next() {
/* 241 */       this.index++;
/*     */     } public int currentSegment(double[] coords) {
/*     */       int type;
/*     */       int count;
/* 245 */       if (isDone())
/*     */       {
/* 247 */         throw new NoSuchElementException(Messages.getString("awt.4B"));
/*     */       }
/*     */ 
/*     */       
/* 251 */       if (this.index == 0) {
/* 252 */         type = 0;
/* 253 */         coords[0] = this.c.getX1();
/* 254 */         coords[1] = this.c.getY1();
/* 255 */         count = 1;
/*     */       } else {
/* 257 */         type = 2;
/* 258 */         coords[0] = this.c.getCtrlX();
/* 259 */         coords[1] = this.c.getCtrlY();
/* 260 */         coords[2] = this.c.getX2();
/* 261 */         coords[3] = this.c.getY2();
/* 262 */         count = 2;
/*     */       } 
/* 264 */       if (this.t != null) {
/* 265 */         this.t.transform(coords, 0, coords, 0, count);
/*     */       }
/* 267 */       return type;
/*     */     } public int currentSegment(float[] coords) {
/*     */       int type;
/*     */       int count;
/* 271 */       if (isDone())
/*     */       {
/* 273 */         throw new NoSuchElementException(Messages.getString("awt.4B"));
/*     */       }
/*     */ 
/*     */       
/* 277 */       if (this.index == 0) {
/* 278 */         type = 0;
/* 279 */         coords[0] = (float)this.c.getX1();
/* 280 */         coords[1] = (float)this.c.getY1();
/* 281 */         count = 1;
/*     */       } else {
/* 283 */         type = 2;
/* 284 */         coords[0] = (float)this.c.getCtrlX();
/* 285 */         coords[1] = (float)this.c.getCtrlY();
/* 286 */         coords[2] = (float)this.c.getX2();
/* 287 */         coords[3] = (float)this.c.getY2();
/* 288 */         count = 2;
/*     */       } 
/* 290 */       if (this.t != null) {
/* 291 */         this.t.transform(coords, 0, coords, 0, count);
/*     */       }
/* 293 */       return type;
/*     */     }
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
/*     */   public void setCurve(Point2D p1, Point2D cp, Point2D p2) {
/* 322 */     setCurve(p1.getX(), p1.getY(), cp.getX(), cp.getY(), p2.getX(), p2.getY());
/*     */   }
/*     */   
/*     */   public void setCurve(double[] coords, int offset) {
/* 326 */     setCurve(coords[offset + 0], coords[offset + 1], coords[offset + 2], coords[offset + 3], coords[offset + 4], coords[offset + 5]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurve(Point2D[] points, int offset) {
/* 333 */     setCurve(points[offset + 0]
/* 334 */         .getX(), points[offset + 0].getY(), points[offset + 1]
/* 335 */         .getX(), points[offset + 1].getY(), points[offset + 2]
/* 336 */         .getX(), points[offset + 2].getY());
/*     */   }
/*     */   
/*     */   public void setCurve(QuadCurve2D curve) {
/* 340 */     setCurve(curve
/* 341 */         .getX1(), curve.getY1(), curve
/* 342 */         .getCtrlX(), curve.getCtrlY(), curve
/* 343 */         .getX2(), curve.getY2());
/*     */   }
/*     */   
/*     */   public double getFlatnessSq() {
/* 347 */     return Line2D.ptSegDistSq(
/* 348 */         getX1(), getY1(), 
/* 349 */         getX2(), getY2(), 
/* 350 */         getCtrlX(), getCtrlY());
/*     */   }
/*     */   
/*     */   public static double getFlatnessSq(double x1, double y1, double ctrlx, double ctrly, double x2, double y2) {
/* 354 */     return Line2D.ptSegDistSq(x1, y1, x2, y2, ctrlx, ctrly);
/*     */   }
/*     */   
/*     */   public static double getFlatnessSq(double[] coords, int offset) {
/* 358 */     return Line2D.ptSegDistSq(coords[offset + 0], coords[offset + 1], coords[offset + 4], coords[offset + 5], coords[offset + 2], coords[offset + 3]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getFlatness() {
/* 365 */     return Line2D.ptSegDist(getX1(), getY1(), getX2(), getY2(), getCtrlX(), getCtrlY());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static double getFlatness(double x1, double y1, double ctrlx, double ctrly, double x2, double y2) {
/* 371 */     return Line2D.ptSegDist(x1, y1, x2, y2, ctrlx, ctrly);
/*     */   }
/*     */   
/*     */   public static double getFlatness(double[] coords, int offset) {
/* 375 */     return Line2D.ptSegDist(coords[offset + 0], coords[offset + 1], coords[offset + 4], coords[offset + 5], coords[offset + 2], coords[offset + 3]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void subdivide(QuadCurve2D left, QuadCurve2D right) {
/* 382 */     subdivide(this, left, right);
/*     */   }
/*     */   
/*     */   public static void subdivide(QuadCurve2D src, QuadCurve2D left, QuadCurve2D right) {
/* 386 */     double x1 = src.getX1();
/* 387 */     double y1 = src.getY1();
/* 388 */     double cx = src.getCtrlX();
/* 389 */     double cy = src.getCtrlY();
/* 390 */     double x2 = src.getX2();
/* 391 */     double y2 = src.getY2();
/* 392 */     double cx1 = (x1 + cx) / 2.0D;
/* 393 */     double cy1 = (y1 + cy) / 2.0D;
/* 394 */     double cx2 = (x2 + cx) / 2.0D;
/* 395 */     double cy2 = (y2 + cy) / 2.0D;
/* 396 */     cx = (cx1 + cx2) / 2.0D;
/* 397 */     cy = (cy1 + cy2) / 2.0D;
/* 398 */     if (left != null) {
/* 399 */       left.setCurve(x1, y1, cx1, cy1, cx, cy);
/*     */     }
/* 401 */     if (right != null) {
/* 402 */       right.setCurve(cx, cy, cx2, cy2, x2, y2);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void subdivide(double[] src, int srcoff, double[] left, int leftOff, double[] right, int rightOff) {
/* 409 */     double x1 = src[srcoff + 0];
/* 410 */     double y1 = src[srcoff + 1];
/* 411 */     double cx = src[srcoff + 2];
/* 412 */     double cy = src[srcoff + 3];
/* 413 */     double x2 = src[srcoff + 4];
/* 414 */     double y2 = src[srcoff + 5];
/* 415 */     double cx1 = (x1 + cx) / 2.0D;
/* 416 */     double cy1 = (y1 + cy) / 2.0D;
/* 417 */     double cx2 = (x2 + cx) / 2.0D;
/* 418 */     double cy2 = (y2 + cy) / 2.0D;
/* 419 */     cx = (cx1 + cx2) / 2.0D;
/* 420 */     cy = (cy1 + cy2) / 2.0D;
/* 421 */     if (left != null) {
/* 422 */       left[leftOff + 0] = x1;
/* 423 */       left[leftOff + 1] = y1;
/* 424 */       left[leftOff + 2] = cx1;
/* 425 */       left[leftOff + 3] = cy1;
/* 426 */       left[leftOff + 4] = cx;
/* 427 */       left[leftOff + 5] = cy;
/*     */     } 
/* 429 */     if (right != null) {
/* 430 */       right[rightOff + 0] = cx;
/* 431 */       right[rightOff + 1] = cy;
/* 432 */       right[rightOff + 2] = cx2;
/* 433 */       right[rightOff + 3] = cy2;
/* 434 */       right[rightOff + 4] = x2;
/* 435 */       right[rightOff + 5] = y2;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int solveQuadratic(double[] eqn) {
/* 440 */     return solveQuadratic(eqn, eqn);
/*     */   }
/*     */   
/*     */   public static int solveQuadratic(double[] eqn, double[] res) {
/* 444 */     return Crossing.solveQuad(eqn, res);
/*     */   }
/*     */   
/*     */   public boolean contains(double px, double py) {
/* 448 */     return Crossing.isInsideEvenOdd(Crossing.crossShape(this, px, py));
/*     */   }
/*     */   
/*     */   public boolean contains(double rx, double ry, double rw, double rh) {
/* 452 */     int cross = Crossing.intersectShape(this, rx, ry, rw, rh);
/* 453 */     return (cross != 255 && Crossing.isInsideEvenOdd(cross));
/*     */   }
/*     */   
/*     */   public boolean intersects(double rx, double ry, double rw, double rh) {
/* 457 */     int cross = Crossing.intersectShape(this, rx, ry, rw, rh);
/* 458 */     return (cross == 255 || Crossing.isInsideEvenOdd(cross));
/*     */   }
/*     */   
/*     */   public boolean contains(Point2D p) {
/* 462 */     return contains(p.getX(), p.getY());
/*     */   }
/*     */   
/*     */   public boolean intersects(Rectangle2D r) {
/* 466 */     return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
/*     */   }
/*     */   
/*     */   public boolean contains(Rectangle2D r) {
/* 470 */     return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
/*     */   }
/*     */   
/*     */   public Rectangle getBounds() {
/* 474 */     return getBounds2D().getBounds();
/*     */   }
/*     */   
/*     */   public PathIterator getPathIterator(AffineTransform t) {
/* 478 */     return new Iterator(this, t);
/*     */   }
/*     */   
/*     */   public PathIterator getPathIterator(AffineTransform t, double flatness) {
/* 482 */     return new FlatteningPathIterator(getPathIterator(t), flatness);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 488 */       return super.clone();
/* 489 */     } catch (CloneNotSupportedException e) {
/* 490 */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/QuadCurve2D.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */