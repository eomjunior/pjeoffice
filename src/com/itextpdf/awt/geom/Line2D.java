/*     */ package com.itextpdf.awt.geom;
/*     */ 
/*     */ import com.itextpdf.awt.geom.misc.Messages;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Line2D
/*     */   implements Shape, Cloneable
/*     */ {
/*     */   public abstract double getX1();
/*     */   
/*     */   public abstract double getY1();
/*     */   
/*     */   public abstract double getX2();
/*     */   
/*     */   public abstract double getY2();
/*     */   
/*     */   public abstract Point2D getP1();
/*     */   
/*     */   public abstract Point2D getP2();
/*     */   
/*     */   public abstract void setLine(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*     */   
/*     */   public static class Float
/*     */     extends Line2D
/*     */   {
/*     */     public float x1;
/*     */     public float y1;
/*     */     public float x2;
/*     */     public float y2;
/*     */     
/*     */     public Float() {}
/*     */     
/*     */     public Float(float x1, float y1, float x2, float y2) {
/*  43 */       setLine(x1, y1, x2, y2);
/*     */     }
/*     */     
/*     */     public Float(Point2D p1, Point2D p2) {
/*  47 */       setLine(p1, p2);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX1() {
/*  52 */       return this.x1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY1() {
/*  57 */       return this.y1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX2() {
/*  62 */       return this.x2;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY2() {
/*  67 */       return this.y2;
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getP1() {
/*  72 */       return new Point2D.Float(this.x1, this.y1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getP2() {
/*  77 */       return new Point2D.Float(this.x2, this.y2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setLine(double x1, double y1, double x2, double y2) {
/*  82 */       this.x1 = (float)x1;
/*  83 */       this.y1 = (float)y1;
/*  84 */       this.x2 = (float)x2;
/*  85 */       this.y2 = (float)y2;
/*     */     }
/*     */     
/*     */     public void setLine(float x1, float y1, float x2, float y2) {
/*  89 */       this.x1 = x1;
/*  90 */       this.y1 = y1;
/*  91 */       this.x2 = x2;
/*  92 */       this.y2 = y2; } public Rectangle2D getBounds2D() {
/*     */       float rx;
/*     */       float ry;
/*     */       float rw;
/*     */       float rh;
/*  97 */       if (this.x1 < this.x2) {
/*  98 */         rx = this.x1;
/*  99 */         rw = this.x2 - this.x1;
/*     */       } else {
/* 101 */         rx = this.x2;
/* 102 */         rw = this.x1 - this.x2;
/*     */       } 
/* 104 */       if (this.y1 < this.y2) {
/* 105 */         ry = this.y1;
/* 106 */         rh = this.y2 - this.y1;
/*     */       } else {
/* 108 */         ry = this.y2;
/* 109 */         rh = this.y1 - this.y2;
/*     */       } 
/* 111 */       return new Rectangle2D.Float(rx, ry, rw, rh);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Double
/*     */     extends Line2D
/*     */   {
/*     */     public double x1;
/*     */     public double y1;
/*     */     public double x2;
/*     */     public double y2;
/*     */     
/*     */     public Double() {}
/*     */     
/*     */     public Double(double x1, double y1, double x2, double y2) {
/* 126 */       setLine(x1, y1, x2, y2);
/*     */     }
/*     */     
/*     */     public Double(Point2D p1, Point2D p2) {
/* 130 */       setLine(p1, p2);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX1() {
/* 135 */       return this.x1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY1() {
/* 140 */       return this.y1;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX2() {
/* 145 */       return this.x2;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY2() {
/* 150 */       return this.y2;
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getP1() {
/* 155 */       return new Point2D.Double(this.x1, this.y1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Point2D getP2() {
/* 160 */       return new Point2D.Double(this.x2, this.y2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setLine(double x1, double y1, double x2, double y2) {
/* 165 */       this.x1 = x1;
/* 166 */       this.y1 = y1;
/* 167 */       this.x2 = x2;
/* 168 */       this.y2 = y2; } public Rectangle2D getBounds2D() {
/*     */       double rx;
/*     */       double ry;
/*     */       double rw;
/*     */       double rh;
/* 173 */       if (this.x1 < this.x2) {
/* 174 */         rx = this.x1;
/* 175 */         rw = this.x2 - this.x1;
/*     */       } else {
/* 177 */         rx = this.x2;
/* 178 */         rw = this.x1 - this.x2;
/*     */       } 
/* 180 */       if (this.y1 < this.y2) {
/* 181 */         ry = this.y1;
/* 182 */         rh = this.y2 - this.y1;
/*     */       } else {
/* 184 */         ry = this.y2;
/* 185 */         rh = this.y1 - this.y2;
/*     */       } 
/* 187 */       return new Rectangle2D.Double(rx, ry, rw, rh);
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
/*     */     double x1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     double y1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     double x2;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     double y2;
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
/*     */     Iterator(Line2D l, AffineTransform at) {
/* 232 */       this.x1 = l.getX1();
/* 233 */       this.y1 = l.getY1();
/* 234 */       this.x2 = l.getX2();
/* 235 */       this.y2 = l.getY2();
/* 236 */       this.t = at;
/*     */     }
/*     */     
/*     */     public int getWindingRule() {
/* 240 */       return 1;
/*     */     }
/*     */     
/*     */     public boolean isDone() {
/* 244 */       return (this.index > 1);
/*     */     }
/*     */     
/*     */     public void next() {
/* 248 */       this.index++;
/*     */     }
/*     */     public int currentSegment(double[] coords) {
/*     */       int type;
/* 252 */       if (isDone())
/*     */       {
/* 254 */         throw new NoSuchElementException(Messages.getString("awt.4B"));
/*     */       }
/*     */       
/* 257 */       if (this.index == 0) {
/* 258 */         type = 0;
/* 259 */         coords[0] = this.x1;
/* 260 */         coords[1] = this.y1;
/*     */       } else {
/* 262 */         type = 1;
/* 263 */         coords[0] = this.x2;
/* 264 */         coords[1] = this.y2;
/*     */       } 
/* 266 */       if (this.t != null) {
/* 267 */         this.t.transform(coords, 0, coords, 0, 1);
/*     */       }
/* 269 */       return type;
/*     */     }
/*     */     public int currentSegment(float[] coords) {
/*     */       int type;
/* 273 */       if (isDone())
/*     */       {
/* 275 */         throw new NoSuchElementException(Messages.getString("awt.4B"));
/*     */       }
/*     */       
/* 278 */       if (this.index == 0) {
/* 279 */         type = 0;
/* 280 */         coords[0] = (float)this.x1;
/* 281 */         coords[1] = (float)this.y1;
/*     */       } else {
/* 283 */         type = 1;
/* 284 */         coords[0] = (float)this.x2;
/* 285 */         coords[1] = (float)this.y2;
/*     */       } 
/* 287 */       if (this.t != null) {
/* 288 */         this.t.transform(coords, 0, coords, 0, 1);
/*     */       }
/* 290 */       return type;
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
/*     */   public void setLine(Point2D p1, Point2D p2) {
/* 313 */     setLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
/*     */   }
/*     */   
/*     */   public void setLine(Line2D line) {
/* 317 */     setLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
/*     */   }
/*     */   
/*     */   public Rectangle getBounds() {
/* 321 */     return getBounds2D().getBounds();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int relativeCCW(double x1, double y1, double x2, double y2, double px, double py) {
/* 328 */     x2 -= x1;
/* 329 */     y2 -= y1;
/* 330 */     px -= x1;
/* 331 */     py -= y1;
/* 332 */     double t = px * y2 - py * x2;
/* 333 */     if (t == 0.0D) {
/* 334 */       t = px * x2 + py * y2;
/* 335 */       if (t > 0.0D) {
/* 336 */         px -= x2;
/* 337 */         py -= y2;
/* 338 */         t = px * x2 + py * y2;
/* 339 */         if (t < 0.0D) {
/* 340 */           t = 0.0D;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 345 */     return (t < 0.0D) ? -1 : ((t > 0.0D) ? 1 : 0);
/*     */   }
/*     */   
/*     */   public int relativeCCW(double px, double py) {
/* 349 */     return relativeCCW(getX1(), getY1(), getX2(), getY2(), px, py);
/*     */   }
/*     */   
/*     */   public int relativeCCW(Point2D p) {
/* 353 */     return relativeCCW(getX1(), getY1(), getX2(), getY2(), p.getX(), p.getY());
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
/*     */   public static boolean linesIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
/* 369 */     x2 -= x1;
/* 370 */     y2 -= y1;
/* 371 */     x3 -= x1;
/* 372 */     y3 -= y1;
/* 373 */     x4 -= x1;
/* 374 */     y4 -= y1;
/*     */     
/* 376 */     double AvB = x2 * y3 - x3 * y2;
/* 377 */     double AvC = x2 * y4 - x4 * y2;
/*     */ 
/*     */     
/* 380 */     if (AvB == 0.0D && AvC == 0.0D) {
/* 381 */       if (x2 != 0.0D) {
/* 382 */         return (x4 * x3 <= 0.0D || (x3 * x2 >= 0.0D && ((x2 > 0.0D) ? (x3 <= x2 || x4 <= x2) : (x3 >= x2 || x4 >= x2))));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 387 */       if (y2 != 0.0D) {
/* 388 */         return (y4 * y3 <= 0.0D || (y3 * y2 >= 0.0D && ((y2 > 0.0D) ? (y3 <= y2 || y4 <= y2) : (y3 >= y2 || y4 >= y2))));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 393 */       return false;
/*     */     } 
/*     */     
/* 396 */     double BvC = x3 * y4 - x4 * y3;
/*     */     
/* 398 */     return (AvB * AvC <= 0.0D && BvC * (AvB + BvC - AvC) <= 0.0D);
/*     */   }
/*     */   
/*     */   public boolean intersectsLine(double x1, double y1, double x2, double y2) {
/* 402 */     return linesIntersect(x1, y1, x2, y2, getX1(), getY1(), getX2(), getY2());
/*     */   }
/*     */   
/*     */   public boolean intersectsLine(Line2D l) {
/* 406 */     return linesIntersect(l.getX1(), l.getY1(), l.getX2(), l.getY2(), getX1(), getY1(), getX2(), getY2());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static double ptSegDistSq(double x1, double y1, double x2, double y2, double px, double py) {
/*     */     double dist;
/* 413 */     x2 -= x1;
/* 414 */     y2 -= y1;
/* 415 */     px -= x1;
/* 416 */     py -= y1;
/*     */     
/* 418 */     if (px * x2 + py * y2 <= 0.0D) {
/* 419 */       dist = px * px + py * py;
/*     */     } else {
/* 421 */       px = x2 - px;
/* 422 */       py = y2 - py;
/* 423 */       if (px * x2 + py * y2 <= 0.0D) {
/* 424 */         dist = px * px + py * py;
/*     */       } else {
/* 426 */         dist = px * y2 - py * x2;
/* 427 */         dist = dist * dist / (x2 * x2 + y2 * y2);
/*     */       } 
/*     */     } 
/* 430 */     if (dist < 0.0D) {
/* 431 */       dist = 0.0D;
/*     */     }
/* 433 */     return dist;
/*     */   }
/*     */   
/*     */   public static double ptSegDist(double x1, double y1, double x2, double y2, double px, double py) {
/* 437 */     return Math.sqrt(ptSegDistSq(x1, y1, x2, y2, px, py));
/*     */   }
/*     */   
/*     */   public double ptSegDistSq(double px, double py) {
/* 441 */     return ptSegDistSq(getX1(), getY1(), getX2(), getY2(), px, py);
/*     */   }
/*     */   
/*     */   public double ptSegDistSq(Point2D p) {
/* 445 */     return ptSegDistSq(getX1(), getY1(), getX2(), getY2(), p.getX(), p.getY());
/*     */   }
/*     */   
/*     */   public double ptSegDist(double px, double py) {
/* 449 */     return ptSegDist(getX1(), getY1(), getX2(), getY2(), px, py);
/*     */   }
/*     */   
/*     */   public double ptSegDist(Point2D p) {
/* 453 */     return ptSegDist(getX1(), getY1(), getX2(), getY2(), p.getX(), p.getY());
/*     */   }
/*     */   
/*     */   public static double ptLineDistSq(double x1, double y1, double x2, double y2, double px, double py) {
/* 457 */     x2 -= x1;
/* 458 */     y2 -= y1;
/* 459 */     px -= x1;
/* 460 */     py -= y1;
/* 461 */     double s = px * y2 - py * x2;
/* 462 */     return s * s / (x2 * x2 + y2 * y2);
/*     */   }
/*     */   
/*     */   public static double ptLineDist(double x1, double y1, double x2, double y2, double px, double py) {
/* 466 */     return Math.sqrt(ptLineDistSq(x1, y1, x2, y2, px, py));
/*     */   }
/*     */   
/*     */   public double ptLineDistSq(double px, double py) {
/* 470 */     return ptLineDistSq(getX1(), getY1(), getX2(), getY2(), px, py);
/*     */   }
/*     */   
/*     */   public double ptLineDistSq(Point2D p) {
/* 474 */     return ptLineDistSq(getX1(), getY1(), getX2(), getY2(), p.getX(), p.getY());
/*     */   }
/*     */   
/*     */   public double ptLineDist(double px, double py) {
/* 478 */     return ptLineDist(getX1(), getY1(), getX2(), getY2(), px, py);
/*     */   }
/*     */   
/*     */   public double ptLineDist(Point2D p) {
/* 482 */     return ptLineDist(getX1(), getY1(), getX2(), getY2(), p.getX(), p.getY());
/*     */   }
/*     */   
/*     */   public boolean contains(double px, double py) {
/* 486 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(Point2D p) {
/* 490 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(Rectangle2D r) {
/* 494 */     return false;
/*     */   }
/*     */   
/*     */   public boolean contains(double rx, double ry, double rw, double rh) {
/* 498 */     return false;
/*     */   }
/*     */   
/*     */   public boolean intersects(double rx, double ry, double rw, double rh) {
/* 502 */     return intersects(new Rectangle2D.Double(rx, ry, rw, rh));
/*     */   }
/*     */   
/*     */   public boolean intersects(Rectangle2D r) {
/* 506 */     return r.intersectsLine(getX1(), getY1(), getX2(), getY2());
/*     */   }
/*     */   
/*     */   public PathIterator getPathIterator(AffineTransform at) {
/* 510 */     return new Iterator(this, at);
/*     */   }
/*     */   
/*     */   public PathIterator getPathIterator(AffineTransform at, double flatness) {
/* 514 */     return new Iterator(this, at);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 520 */       return super.clone();
/* 521 */     } catch (CloneNotSupportedException e) {
/* 522 */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/Line2D.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */