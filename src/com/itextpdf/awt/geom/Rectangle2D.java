/*     */ package com.itextpdf.awt.geom;
/*     */ 
/*     */ import com.itextpdf.awt.geom.misc.HashCode;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Rectangle2D
/*     */   extends RectangularShape
/*     */ {
/*     */   public static final int OUT_LEFT = 1;
/*     */   public static final int OUT_TOP = 2;
/*     */   public static final int OUT_RIGHT = 4;
/*     */   public static final int OUT_BOTTOM = 8;
/*     */   
/*     */   public abstract void setRect(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4);
/*     */   
/*     */   public abstract int outcode(double paramDouble1, double paramDouble2);
/*     */   
/*     */   public abstract Rectangle2D createIntersection(Rectangle2D paramRectangle2D);
/*     */   
/*     */   public abstract Rectangle2D createUnion(Rectangle2D paramRectangle2D);
/*     */   
/*     */   public static class Float
/*     */     extends Rectangle2D
/*     */   {
/*     */     public float x;
/*     */     public float y;
/*     */     public float width;
/*     */     public float height;
/*     */     
/*     */     public Float() {}
/*     */     
/*     */     public Float(float x, float y, float width, float height) {
/*  49 */       setRect(x, y, width, height);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX() {
/*  54 */       return this.x;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY() {
/*  59 */       return this.y;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getWidth() {
/*  64 */       return this.width;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getHeight() {
/*  69 */       return this.height;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/*  74 */       return (this.width <= 0.0F || this.height <= 0.0F);
/*     */     }
/*     */     
/*     */     public void setRect(float x, float y, float width, float height) {
/*  78 */       this.x = x;
/*  79 */       this.y = y;
/*  80 */       this.width = width;
/*  81 */       this.height = height;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setRect(double x, double y, double width, double height) {
/*  86 */       this.x = (float)x;
/*  87 */       this.y = (float)y;
/*  88 */       this.width = (float)width;
/*  89 */       this.height = (float)height;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setRect(Rectangle2D r) {
/*  94 */       this.x = (float)r.getX();
/*  95 */       this.y = (float)r.getY();
/*  96 */       this.width = (float)r.getWidth();
/*  97 */       this.height = (float)r.getHeight();
/*     */     }
/*     */ 
/*     */     
/*     */     public int outcode(double px, double py) {
/* 102 */       int code = 0;
/*     */       
/* 104 */       if (this.width <= 0.0F) {
/* 105 */         code |= 0x5;
/*     */       }
/* 107 */       else if (px < this.x) {
/* 108 */         code |= 0x1;
/*     */       }
/* 110 */       else if (px > (this.x + this.width)) {
/* 111 */         code |= 0x4;
/*     */       } 
/*     */       
/* 114 */       if (this.height <= 0.0F) {
/* 115 */         code |= 0xA;
/*     */       }
/* 117 */       else if (py < this.y) {
/* 118 */         code |= 0x2;
/*     */       }
/* 120 */       else if (py > (this.y + this.height)) {
/* 121 */         code |= 0x8;
/*     */       } 
/*     */       
/* 124 */       return code;
/*     */     }
/*     */ 
/*     */     
/*     */     public Rectangle2D getBounds2D() {
/* 129 */       return new Float(this.x, this.y, this.width, this.height);
/*     */     }
/*     */ 
/*     */     
/*     */     public Rectangle2D createIntersection(Rectangle2D r) {
/*     */       Rectangle2D dst;
/* 135 */       if (r instanceof Rectangle2D.Double) {
/* 136 */         dst = new Rectangle2D.Double();
/*     */       } else {
/* 138 */         dst = new Float();
/*     */       } 
/* 140 */       Rectangle2D.intersect(this, r, dst);
/* 141 */       return dst;
/*     */     }
/*     */ 
/*     */     
/*     */     public Rectangle2D createUnion(Rectangle2D r) {
/*     */       Rectangle2D dst;
/* 147 */       if (r instanceof Rectangle2D.Double) {
/* 148 */         dst = new Rectangle2D.Double();
/*     */       } else {
/* 150 */         dst = new Float();
/*     */       } 
/* 152 */       Rectangle2D.union(this, r, dst);
/* 153 */       return dst;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 160 */       return getClass().getName() + "[x=" + this.x + ",y=" + this.y + ",width=" + this.width + ",height=" + this.height + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Double
/*     */     extends Rectangle2D
/*     */   {
/*     */     public double x;
/*     */     public double y;
/*     */     public double width;
/*     */     public double height;
/*     */     
/*     */     public Double() {}
/*     */     
/*     */     public Double(double x, double y, double width, double height) {
/* 175 */       setRect(x, y, width, height);
/*     */     }
/*     */ 
/*     */     
/*     */     public double getX() {
/* 180 */       return this.x;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getY() {
/* 185 */       return this.y;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getWidth() {
/* 190 */       return this.width;
/*     */     }
/*     */ 
/*     */     
/*     */     public double getHeight() {
/* 195 */       return this.height;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 200 */       return (this.width <= 0.0D || this.height <= 0.0D);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setRect(double x, double y, double width, double height) {
/* 205 */       this.x = x;
/* 206 */       this.y = y;
/* 207 */       this.width = width;
/* 208 */       this.height = height;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setRect(Rectangle2D r) {
/* 213 */       this.x = r.getX();
/* 214 */       this.y = r.getY();
/* 215 */       this.width = r.getWidth();
/* 216 */       this.height = r.getHeight();
/*     */     }
/*     */ 
/*     */     
/*     */     public int outcode(double px, double py) {
/* 221 */       int code = 0;
/*     */       
/* 223 */       if (this.width <= 0.0D) {
/* 224 */         code |= 0x5;
/*     */       }
/* 226 */       else if (px < this.x) {
/* 227 */         code |= 0x1;
/*     */       }
/* 229 */       else if (px > this.x + this.width) {
/* 230 */         code |= 0x4;
/*     */       } 
/*     */       
/* 233 */       if (this.height <= 0.0D) {
/* 234 */         code |= 0xA;
/*     */       }
/* 236 */       else if (py < this.y) {
/* 237 */         code |= 0x2;
/*     */       }
/* 239 */       else if (py > this.y + this.height) {
/* 240 */         code |= 0x8;
/*     */       } 
/*     */       
/* 243 */       return code;
/*     */     }
/*     */ 
/*     */     
/*     */     public Rectangle2D getBounds2D() {
/* 248 */       return new Double(this.x, this.y, this.width, this.height);
/*     */     }
/*     */ 
/*     */     
/*     */     public Rectangle2D createIntersection(Rectangle2D r) {
/* 253 */       Rectangle2D dst = new Double();
/* 254 */       Rectangle2D.intersect(this, r, dst);
/* 255 */       return dst;
/*     */     }
/*     */ 
/*     */     
/*     */     public Rectangle2D createUnion(Rectangle2D r) {
/* 260 */       Rectangle2D dest = new Double();
/* 261 */       Rectangle2D.union(this, r, dest);
/* 262 */       return dest;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 269 */       return getClass().getName() + "[x=" + this.x + ",y=" + this.y + ",width=" + this.width + ",height=" + this.height + "]";
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
/*     */     double x;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     double y;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     double width;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     double height;
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
/*     */     Iterator(Rectangle2D r, AffineTransform at) {
/* 315 */       this.x = r.getX();
/* 316 */       this.y = r.getY();
/* 317 */       this.width = r.getWidth();
/* 318 */       this.height = r.getHeight();
/* 319 */       this.t = at;
/* 320 */       if (this.width < 0.0D || this.height < 0.0D) {
/* 321 */         this.index = 6;
/*     */       }
/*     */     }
/*     */     
/*     */     public int getWindingRule() {
/* 326 */       return 1;
/*     */     }
/*     */     
/*     */     public boolean isDone() {
/* 330 */       return (this.index > 5);
/*     */     }
/*     */     
/*     */     public void next() {
/* 334 */       this.index++;
/*     */     }
/*     */     public int currentSegment(double[] coords) {
/*     */       int type;
/* 338 */       if (isDone()) {
/* 339 */         throw new NoSuchElementException(Messages.getString("awt.4B"));
/*     */       }
/* 341 */       if (this.index == 5) {
/* 342 */         return 4;
/*     */       }
/*     */       
/* 345 */       if (this.index == 0) {
/* 346 */         type = 0;
/* 347 */         coords[0] = this.x;
/* 348 */         coords[1] = this.y;
/*     */       } else {
/* 350 */         type = 1;
/* 351 */         switch (this.index) {
/*     */           case 1:
/* 353 */             coords[0] = this.x + this.width;
/* 354 */             coords[1] = this.y;
/*     */             break;
/*     */           case 2:
/* 357 */             coords[0] = this.x + this.width;
/* 358 */             coords[1] = this.y + this.height;
/*     */             break;
/*     */           case 3:
/* 361 */             coords[0] = this.x;
/* 362 */             coords[1] = this.y + this.height;
/*     */             break;
/*     */           case 4:
/* 365 */             coords[0] = this.x;
/* 366 */             coords[1] = this.y;
/*     */             break;
/*     */         } 
/*     */       } 
/* 370 */       if (this.t != null) {
/* 371 */         this.t.transform(coords, 0, coords, 0, 1);
/*     */       }
/* 373 */       return type;
/*     */     }
/*     */     public int currentSegment(float[] coords) {
/*     */       int type;
/* 377 */       if (isDone()) {
/* 378 */         throw new NoSuchElementException(Messages.getString("awt.4B"));
/*     */       }
/* 380 */       if (this.index == 5) {
/* 381 */         return 4;
/*     */       }
/*     */       
/* 384 */       if (this.index == 0) {
/* 385 */         coords[0] = (float)this.x;
/* 386 */         coords[1] = (float)this.y;
/* 387 */         type = 0;
/*     */       } else {
/* 389 */         type = 1;
/* 390 */         switch (this.index) {
/*     */           case 1:
/* 392 */             coords[0] = (float)(this.x + this.width);
/* 393 */             coords[1] = (float)this.y;
/*     */             break;
/*     */           case 2:
/* 396 */             coords[0] = (float)(this.x + this.width);
/* 397 */             coords[1] = (float)(this.y + this.height);
/*     */             break;
/*     */           case 3:
/* 400 */             coords[0] = (float)this.x;
/* 401 */             coords[1] = (float)(this.y + this.height);
/*     */             break;
/*     */           case 4:
/* 404 */             coords[0] = (float)this.x;
/* 405 */             coords[1] = (float)this.y;
/*     */             break;
/*     */         } 
/*     */       } 
/* 409 */       if (this.t != null) {
/* 410 */         this.t.transform(coords, 0, coords, 0, 1);
/*     */       }
/* 412 */       return type;
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
/*     */   public void setRect(Rectangle2D r) {
/* 429 */     setRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFrame(double x, double y, double width, double height) {
/* 434 */     setRect(x, y, width, height);
/*     */   }
/*     */   
/*     */   public Rectangle2D getBounds2D() {
/* 438 */     return (Rectangle2D)clone();
/*     */   }
/*     */   
/*     */   public boolean intersectsLine(double x1, double y1, double x2, double y2) {
/* 442 */     double rx1 = getX();
/* 443 */     double ry1 = getY();
/* 444 */     double rx2 = rx1 + getWidth();
/* 445 */     double ry2 = ry1 + getHeight();
/* 446 */     return ((rx1 <= x1 && x1 <= rx2 && ry1 <= y1 && y1 <= ry2) || (rx1 <= x2 && x2 <= rx2 && ry1 <= y2 && y2 <= ry2) || 
/*     */ 
/*     */       
/* 449 */       Line2D.linesIntersect(rx1, ry1, rx2, ry2, x1, y1, x2, y2) || 
/* 450 */       Line2D.linesIntersect(rx2, ry1, rx1, ry2, x1, y1, x2, y2));
/*     */   }
/*     */   
/*     */   public boolean intersectsLine(Line2D l) {
/* 454 */     return intersectsLine(l.getX1(), l.getY1(), l.getX2(), l.getY2());
/*     */   }
/*     */   
/*     */   public int outcode(Point2D p) {
/* 458 */     return outcode(p.getX(), p.getY());
/*     */   }
/*     */   
/*     */   public boolean contains(double x, double y) {
/* 462 */     if (isEmpty()) {
/* 463 */       return false;
/*     */     }
/*     */     
/* 466 */     double x1 = getX();
/* 467 */     double y1 = getY();
/* 468 */     double x2 = x1 + getWidth();
/* 469 */     double y2 = y1 + getHeight();
/*     */     
/* 471 */     return (x1 <= x && x < x2 && y1 <= y && y < y2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersects(double x, double y, double width, double height) {
/* 477 */     if (isEmpty() || width <= 0.0D || height <= 0.0D) {
/* 478 */       return false;
/*     */     }
/*     */     
/* 481 */     double x1 = getX();
/* 482 */     double y1 = getY();
/* 483 */     double x2 = x1 + getWidth();
/* 484 */     double y2 = y1 + getHeight();
/*     */     
/* 486 */     return (x + width > x1 && x < x2 && y + height > y1 && y < y2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(double x, double y, double width, double height) {
/* 492 */     if (isEmpty() || width <= 0.0D || height <= 0.0D) {
/* 493 */       return false;
/*     */     }
/*     */     
/* 496 */     double x1 = getX();
/* 497 */     double y1 = getY();
/* 498 */     double x2 = x1 + getWidth();
/* 499 */     double y2 = y1 + getHeight();
/*     */     
/* 501 */     return (x1 <= x && x + width <= x2 && y1 <= y && y + height <= y2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void intersect(Rectangle2D src1, Rectangle2D src2, Rectangle2D dst) {
/* 507 */     double x1 = Math.max(src1.getMinX(), src2.getMinX());
/* 508 */     double y1 = Math.max(src1.getMinY(), src2.getMinY());
/* 509 */     double x2 = Math.min(src1.getMaxX(), src2.getMaxX());
/* 510 */     double y2 = Math.min(src1.getMaxY(), src2.getMaxY());
/* 511 */     dst.setFrame(x1, y1, x2 - x1, y2 - y1);
/*     */   }
/*     */   
/*     */   public static void union(Rectangle2D src1, Rectangle2D src2, Rectangle2D dst) {
/* 515 */     double x1 = Math.min(src1.getMinX(), src2.getMinX());
/* 516 */     double y1 = Math.min(src1.getMinY(), src2.getMinY());
/* 517 */     double x2 = Math.max(src1.getMaxX(), src2.getMaxX());
/* 518 */     double y2 = Math.max(src1.getMaxY(), src2.getMaxY());
/* 519 */     dst.setFrame(x1, y1, x2 - x1, y2 - y1);
/*     */   }
/*     */   
/*     */   public void add(double x, double y) {
/* 523 */     double x1 = Math.min(getMinX(), x);
/* 524 */     double y1 = Math.min(getMinY(), y);
/* 525 */     double x2 = Math.max(getMaxX(), x);
/* 526 */     double y2 = Math.max(getMaxY(), y);
/* 527 */     setRect(x1, y1, x2 - x1, y2 - y1);
/*     */   }
/*     */   
/*     */   public void add(Point2D p) {
/* 531 */     add(p.getX(), p.getY());
/*     */   }
/*     */   
/*     */   public void add(Rectangle2D r) {
/* 535 */     union(this, r, this);
/*     */   }
/*     */   
/*     */   public PathIterator getPathIterator(AffineTransform t) {
/* 539 */     return new Iterator(this, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public PathIterator getPathIterator(AffineTransform t, double flatness) {
/* 544 */     return new Iterator(this, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 549 */     HashCode hash = new HashCode();
/* 550 */     hash.append(getX());
/* 551 */     hash.append(getY());
/* 552 */     hash.append(getWidth());
/* 553 */     hash.append(getHeight());
/* 554 */     return hash.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 559 */     if (obj == this) {
/* 560 */       return true;
/*     */     }
/* 562 */     if (obj instanceof Rectangle2D) {
/* 563 */       Rectangle2D r = (Rectangle2D)obj;
/* 564 */       return (
/* 565 */         getX() == r.getX() && 
/* 566 */         getY() == r.getY() && 
/* 567 */         getWidth() == r.getWidth() && 
/* 568 */         getHeight() == r.getHeight());
/*     */     } 
/* 570 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/Rectangle2D.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */