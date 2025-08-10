/*     */ package com.itextpdf.awt.geom;
/*     */ 
/*     */ import com.itextpdf.awt.geom.gl.Crossing;
/*     */ import com.itextpdf.awt.geom.misc.Messages;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public abstract class CubicCurve2D
/*     */   implements Shape, Cloneable {
/*     */   public abstract double getX1();
/*     */   
/*     */   public abstract double getY1();
/*     */   
/*     */   public abstract Point2D getP1();
/*     */   
/*     */   public abstract double getCtrlX1();
/*     */   
/*     */   public abstract double getCtrlY1();
/*     */   
/*     */   public abstract Point2D getCtrlP1();
/*     */   
/*     */   public abstract double getCtrlX2();
/*     */   
/*     */   public abstract double getCtrlY2();
/*     */   
/*     */   public abstract Point2D getCtrlP2();
/*     */   
/*     */   public abstract double getX2();
/*     */   
/*     */   public abstract double getY2();
/*     */   
/*     */   public abstract Point2D getP2();
/*     */   
/*     */   public abstract void setCurve(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8);
/*     */   
/*     */   public static class Float extends CubicCurve2D {
/*     */     public float x1;
/*     */     public float y1;
/*     */     public float ctrlx1;
/*     */     public float ctrly1;
/*     */     public float ctrlx2;
/*     */     public float ctrly2;
/*     */     public float x2;
/*     */     public float y2;
/*     */     
/*     */     public Float() {}
/*     */     
/*     */     public Float(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
/*  48 */       setCurve(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX1() {
/*  53 */       return this.x1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY1() {
/*  58 */       return this.y1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getCtrlX1() {
/*  63 */       return this.ctrlx1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getCtrlY1() {
/*  68 */       return this.ctrly1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getCtrlX2() {
/*  73 */       return this.ctrlx2;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getCtrlY2() {
/*  78 */       return this.ctrly2;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX2() {
/*  83 */       return this.x2;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY2() {
/*  88 */       return this.y2;
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getP1() {
/*  93 */       return new Point2D.Float(this.x1, this.y1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getCtrlP1() {
/*  98 */       return new Point2D.Float(this.ctrlx1, this.ctrly1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getCtrlP2() {
/* 103 */       return new Point2D.Float(this.ctrlx2, this.ctrly2);
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getP2() {
/* 108 */       return new Point2D.Float(this.x2, this.y2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setCurve(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2) {
/* 115 */       this.x1 = (float)x1;
/* 116 */       this.y1 = (float)y1;
/* 117 */       this.ctrlx1 = (float)ctrlx1;
/* 118 */       this.ctrly1 = (float)ctrly1;
/* 119 */       this.ctrlx2 = (float)ctrlx2;
/* 120 */       this.ctrly2 = (float)ctrly2;
/* 121 */       this.x2 = (float)x2;
/* 122 */       this.y2 = (float)y2;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setCurve(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
/* 128 */       this.x1 = x1;
/* 129 */       this.y1 = y1;
/* 130 */       this.ctrlx1 = ctrlx1;
/* 131 */       this.ctrly1 = ctrly1;
/* 132 */       this.ctrlx2 = ctrlx2;
/* 133 */       this.ctrly2 = ctrly2;
/* 134 */       this.x2 = x2;
/* 135 */       this.y2 = y2;
/*     */     }
/*     */     
/*     */     public Rectangle2D getBounds2D() {
/* 139 */       float rx1 = Math.min(Math.min(this.x1, this.x2), Math.min(this.ctrlx1, this.ctrlx2));
/* 140 */       float ry1 = Math.min(Math.min(this.y1, this.y2), Math.min(this.ctrly1, this.ctrly2));
/* 141 */       float rx2 = Math.max(Math.max(this.x1, this.x2), Math.max(this.ctrlx1, this.ctrlx2));
/* 142 */       float ry2 = Math.max(Math.max(this.y1, this.y2), Math.max(this.ctrly1, this.ctrly2));
/* 143 */       return new Rectangle2D.Float(rx1, ry1, rx2 - rx1, ry2 - ry1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Double
/*     */     extends CubicCurve2D
/*     */   {
/*     */     public double x1;
/*     */     public double y1;
/*     */     public double ctrlx1;
/*     */     public double ctrly1;
/*     */     public double ctrlx2;
/*     */     public double ctrly2;
/*     */     public double x2;
/*     */     public double y2;
/*     */     
/*     */     public Double() {}
/*     */     
/*     */     public Double(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2) {
/* 163 */       setCurve(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX1() {
/* 168 */       return this.x1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY1() {
/* 173 */       return this.y1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getCtrlX1() {
/* 178 */       return this.ctrlx1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getCtrlY1() {
/* 183 */       return this.ctrly1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getCtrlX2() {
/* 188 */       return this.ctrlx2;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getCtrlY2() {
/* 193 */       return this.ctrly2;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX2() {
/* 198 */       return this.x2;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY2() {
/* 203 */       return this.y2;
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getP1() {
/* 208 */       return new Point2D.Double(this.x1, this.y1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getCtrlP1() {
/* 213 */       return new Point2D.Double(this.ctrlx1, this.ctrly1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getCtrlP2() {
/* 218 */       return new Point2D.Double(this.ctrlx2, this.ctrly2);
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getP2() {
/* 223 */       return new Point2D.Double(this.x2, this.y2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setCurve(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2) {
/* 230 */       this.x1 = x1;
/* 231 */       this.y1 = y1;
/* 232 */       this.ctrlx1 = ctrlx1;
/* 233 */       this.ctrly1 = ctrly1;
/* 234 */       this.ctrlx2 = ctrlx2;
/* 235 */       this.ctrly2 = ctrly2;
/* 236 */       this.x2 = x2;
/* 237 */       this.y2 = y2;
/*     */     }
/*     */     
/*     */     public Rectangle2D getBounds2D() {
/* 241 */       double rx1 = Math.min(Math.min(this.x1, this.x2), Math.min(this.ctrlx1, this.ctrlx2));
/* 242 */       double ry1 = Math.min(Math.min(this.y1, this.y2), Math.min(this.ctrly1, this.ctrly2));
/* 243 */       double rx2 = Math.max(Math.max(this.x1, this.x2), Math.max(this.ctrlx1, this.ctrlx2));
/* 244 */       double ry2 = Math.max(Math.max(this.y1, this.y2), Math.max(this.ctrly1, this.ctrly2));
/* 245 */       return new Rectangle2D.Double(rx1, ry1, rx2 - rx1, ry2 - ry1);
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
/*     */     CubicCurve2D c;
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
/*     */     Iterator(CubicCurve2D c, AffineTransform t) {
/* 275 */       this.c = c;
/* 276 */       this.t = t;
/*     */     }
/*     */     
/*     */     public int getWindingRule() {
/* 280 */       return 1;
/*     */     }
/*     */     
/*     */     public boolean isDone() {
/* 284 */       return (this.index > 1);
/*     */     }
/*     */     
/*     */     public void next() {
/* 288 */       this.index++;
/*     */     } public int currentSegment(double[] coords) {
/*     */       int type;
/*     */       int count;
/* 292 */       if (isDone()) {
/* 293 */         throw new NoSuchElementException(Messages.getString("awt.4B"));
/*     */       }
/*     */ 
/*     */       
/* 297 */       if (this.index == 0) {
/* 298 */         type = 0;
/* 299 */         coords[0] = this.c.getX1();
/* 300 */         coords[1] = this.c.getY1();
/* 301 */         count = 1;
/*     */       } else {
/* 303 */         type = 3;
/* 304 */         coords[0] = this.c.getCtrlX1();
/* 305 */         coords[1] = this.c.getCtrlY1();
/* 306 */         coords[2] = this.c.getCtrlX2();
/* 307 */         coords[3] = this.c.getCtrlY2();
/* 308 */         coords[4] = this.c.getX2();
/* 309 */         coords[5] = this.c.getY2();
/* 310 */         count = 3;
/*     */       } 
/* 312 */       if (this.t != null) {
/* 313 */         this.t.transform(coords, 0, coords, 0, count);
/*     */       }
/* 315 */       return type;
/*     */     } public int currentSegment(float[] coords) {
/*     */       int type;
/*     */       int count;
/* 319 */       if (isDone()) {
/* 320 */         throw new NoSuchElementException(Messages.getString("awt.4B"));
/*     */       }
/*     */ 
/*     */       
/* 324 */       if (this.index == 0) {
/* 325 */         type = 0;
/* 326 */         coords[0] = (float)this.c.getX1();
/* 327 */         coords[1] = (float)this.c.getY1();
/* 328 */         count = 1;
/*     */       } else {
/* 330 */         type = 3;
/* 331 */         coords[0] = (float)this.c.getCtrlX1();
/* 332 */         coords[1] = (float)this.c.getCtrlY1();
/* 333 */         coords[2] = (float)this.c.getCtrlX2();
/* 334 */         coords[3] = (float)this.c.getCtrlY2();
/* 335 */         coords[4] = (float)this.c.getX2();
/* 336 */         coords[5] = (float)this.c.getY2();
/* 337 */         count = 3;
/*     */       } 
/* 339 */       if (this.t != null) {
/* 340 */         this.t.transform(coords, 0, coords, 0, count);
/*     */       }
/* 342 */       return type;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurve(Point2D p1, Point2D cp1, Point2D cp2, Point2D p2) {
/* 378 */     setCurve(p1
/* 379 */         .getX(), p1.getY(), cp1
/* 380 */         .getX(), cp1.getY(), cp2
/* 381 */         .getX(), cp2.getY(), p2
/* 382 */         .getX(), p2.getY());
/*     */   }
/*     */   
/*     */   public void setCurve(double[] coords, int offset) {
/* 386 */     setCurve(coords[offset + 0], coords[offset + 1], coords[offset + 2], coords[offset + 3], coords[offset + 4], coords[offset + 5], coords[offset + 6], coords[offset + 7]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurve(Point2D[] points, int offset) {
/* 394 */     setCurve(points[offset + 0]
/* 395 */         .getX(), points[offset + 0].getY(), points[offset + 1]
/* 396 */         .getX(), points[offset + 1].getY(), points[offset + 2]
/* 397 */         .getX(), points[offset + 2].getY(), points[offset + 3]
/* 398 */         .getX(), points[offset + 3].getY());
/*     */   }
/*     */   
/*     */   public void setCurve(CubicCurve2D curve) {
/* 402 */     setCurve(curve
/* 403 */         .getX1(), curve.getY1(), curve
/* 404 */         .getCtrlX1(), curve.getCtrlY1(), curve
/* 405 */         .getCtrlX2(), curve.getCtrlY2(), curve
/* 406 */         .getX2(), curve.getY2());
/*     */   }
/*     */   
/*     */   public double getFlatnessSq() {
/* 410 */     return getFlatnessSq(
/* 411 */         getX1(), getY1(), 
/* 412 */         getCtrlX1(), getCtrlY1(), 
/* 413 */         getCtrlX2(), getCtrlY2(), 
/* 414 */         getX2(), getY2());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static double getFlatnessSq(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2) {
/* 420 */     return Math.max(
/* 421 */         Line2D.ptSegDistSq(x1, y1, x2, y2, ctrlx1, ctrly1), 
/* 422 */         Line2D.ptSegDistSq(x1, y1, x2, y2, ctrlx2, ctrly2));
/*     */   }
/*     */   
/*     */   public static double getFlatnessSq(double[] coords, int offset) {
/* 426 */     return getFlatnessSq(coords[offset + 0], coords[offset + 1], coords[offset + 2], coords[offset + 3], coords[offset + 4], coords[offset + 5], coords[offset + 6], coords[offset + 7]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getFlatness() {
/* 434 */     return getFlatness(
/* 435 */         getX1(), getY1(), 
/* 436 */         getCtrlX1(), getCtrlY1(), 
/* 437 */         getCtrlX2(), getCtrlY2(), 
/* 438 */         getX2(), getY2());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static double getFlatness(double x1, double y1, double ctrlx1, double ctrly1, double ctrlx2, double ctrly2, double x2, double y2) {
/* 444 */     return Math.sqrt(getFlatnessSq(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2));
/*     */   }
/*     */   
/*     */   public static double getFlatness(double[] coords, int offset) {
/* 448 */     return getFlatness(coords[offset + 0], coords[offset + 1], coords[offset + 2], coords[offset + 3], coords[offset + 4], coords[offset + 5], coords[offset + 6], coords[offset + 7]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void subdivide(CubicCurve2D left, CubicCurve2D right) {
/* 456 */     subdivide(this, left, right);
/*     */   }
/*     */   
/*     */   public static void subdivide(CubicCurve2D src, CubicCurve2D left, CubicCurve2D right) {
/* 460 */     double x1 = src.getX1();
/* 461 */     double y1 = src.getY1();
/* 462 */     double cx1 = src.getCtrlX1();
/* 463 */     double cy1 = src.getCtrlY1();
/* 464 */     double cx2 = src.getCtrlX2();
/* 465 */     double cy2 = src.getCtrlY2();
/* 466 */     double x2 = src.getX2();
/* 467 */     double y2 = src.getY2();
/* 468 */     double cx = (cx1 + cx2) / 2.0D;
/* 469 */     double cy = (cy1 + cy2) / 2.0D;
/* 470 */     cx1 = (x1 + cx1) / 2.0D;
/* 471 */     cy1 = (y1 + cy1) / 2.0D;
/* 472 */     cx2 = (x2 + cx2) / 2.0D;
/* 473 */     cy2 = (y2 + cy2) / 2.0D;
/* 474 */     double ax = (cx1 + cx) / 2.0D;
/* 475 */     double ay = (cy1 + cy) / 2.0D;
/* 476 */     double bx = (cx2 + cx) / 2.0D;
/* 477 */     double by = (cy2 + cy) / 2.0D;
/* 478 */     cx = (ax + bx) / 2.0D;
/* 479 */     cy = (ay + by) / 2.0D;
/* 480 */     if (left != null) {
/* 481 */       left.setCurve(x1, y1, cx1, cy1, ax, ay, cx, cy);
/*     */     }
/* 483 */     if (right != null) {
/* 484 */       right.setCurve(cx, cy, bx, by, cx2, cy2, x2, y2);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void subdivide(double[] src, int srcOff, double[] left, int leftOff, double[] right, int rightOff) {
/* 489 */     double x1 = src[srcOff + 0];
/* 490 */     double y1 = src[srcOff + 1];
/* 491 */     double cx1 = src[srcOff + 2];
/* 492 */     double cy1 = src[srcOff + 3];
/* 493 */     double cx2 = src[srcOff + 4];
/* 494 */     double cy2 = src[srcOff + 5];
/* 495 */     double x2 = src[srcOff + 6];
/* 496 */     double y2 = src[srcOff + 7];
/* 497 */     double cx = (cx1 + cx2) / 2.0D;
/* 498 */     double cy = (cy1 + cy2) / 2.0D;
/* 499 */     cx1 = (x1 + cx1) / 2.0D;
/* 500 */     cy1 = (y1 + cy1) / 2.0D;
/* 501 */     cx2 = (x2 + cx2) / 2.0D;
/* 502 */     cy2 = (y2 + cy2) / 2.0D;
/* 503 */     double ax = (cx1 + cx) / 2.0D;
/* 504 */     double ay = (cy1 + cy) / 2.0D;
/* 505 */     double bx = (cx2 + cx) / 2.0D;
/* 506 */     double by = (cy2 + cy) / 2.0D;
/* 507 */     cx = (ax + bx) / 2.0D;
/* 508 */     cy = (ay + by) / 2.0D;
/* 509 */     if (left != null) {
/* 510 */       left[leftOff + 0] = x1;
/* 511 */       left[leftOff + 1] = y1;
/* 512 */       left[leftOff + 2] = cx1;
/* 513 */       left[leftOff + 3] = cy1;
/* 514 */       left[leftOff + 4] = ax;
/* 515 */       left[leftOff + 5] = ay;
/* 516 */       left[leftOff + 6] = cx;
/* 517 */       left[leftOff + 7] = cy;
/*     */     } 
/* 519 */     if (right != null) {
/* 520 */       right[rightOff + 0] = cx;
/* 521 */       right[rightOff + 1] = cy;
/* 522 */       right[rightOff + 2] = bx;
/* 523 */       right[rightOff + 3] = by;
/* 524 */       right[rightOff + 4] = cx2;
/* 525 */       right[rightOff + 5] = cy2;
/* 526 */       right[rightOff + 6] = x2;
/* 527 */       right[rightOff + 7] = y2;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int solveCubic(double[] eqn) {
/* 532 */     return solveCubic(eqn, eqn);
/*     */   }
/*     */   
/*     */   public static int solveCubic(double[] eqn, double[] res) {
/* 536 */     return Crossing.solveCubic(eqn, res);
/*     */   }
/*     */   
/*     */   public boolean contains(double px, double py) {
/* 540 */     return Crossing.isInsideEvenOdd(Crossing.crossShape(this, px, py));
/*     */   }
/*     */   
/*     */   public boolean contains(double rx, double ry, double rw, double rh) {
/* 544 */     int cross = Crossing.intersectShape(this, rx, ry, rw, rh);
/* 545 */     return (cross != 255 && Crossing.isInsideEvenOdd(cross));
/*     */   }
/*     */   
/*     */   public boolean intersects(double rx, double ry, double rw, double rh) {
/* 549 */     int cross = Crossing.intersectShape(this, rx, ry, rw, rh);
/* 550 */     return (cross == 255 || Crossing.isInsideEvenOdd(cross));
/*     */   }
/*     */   
/*     */   public boolean contains(Point2D p) {
/* 554 */     return contains(p.getX(), p.getY());
/*     */   }
/*     */   
/*     */   public boolean intersects(Rectangle2D r) {
/* 558 */     return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
/*     */   }
/*     */   
/*     */   public boolean contains(Rectangle2D r) {
/* 562 */     return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
/*     */   }
/*     */   
/*     */   public Rectangle getBounds() {
/* 566 */     return getBounds2D().getBounds();
/*     */   }
/*     */   
/*     */   public PathIterator getPathIterator(AffineTransform t) {
/* 570 */     return new Iterator(this, t);
/*     */   }
/*     */   
/*     */   public PathIterator getPathIterator(AffineTransform at, double flatness) {
/* 574 */     return new FlatteningPathIterator(getPathIterator(at), flatness);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 580 */       return super.clone();
/* 581 */     } catch (CloneNotSupportedException e) {
/* 582 */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/CubicCurve2D.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */