/*     */ package com.itextpdf.awt.geom;
/*     */ 
/*     */ import com.itextpdf.awt.geom.misc.HashCode;
/*     */ import com.itextpdf.awt.geom.misc.Messages;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AffineTransform
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1330973210523860834L;
/*     */   public static final int TYPE_IDENTITY = 0;
/*     */   public static final int TYPE_TRANSLATION = 1;
/*     */   public static final int TYPE_UNIFORM_SCALE = 2;
/*     */   public static final int TYPE_GENERAL_SCALE = 4;
/*     */   public static final int TYPE_QUADRANT_ROTATION = 8;
/*     */   public static final int TYPE_GENERAL_ROTATION = 16;
/*     */   public static final int TYPE_GENERAL_TRANSFORM = 32;
/*     */   public static final int TYPE_FLIP = 64;
/*     */   public static final int TYPE_MASK_SCALE = 6;
/*     */   public static final int TYPE_MASK_ROTATION = 24;
/*     */   static final int TYPE_UNKNOWN = -1;
/*     */   static final double ZERO = 1.0E-10D;
/*     */   double m00;
/*     */   double m10;
/*     */   double m01;
/*     */   double m11;
/*     */   double m02;
/*     */   double m12;
/*     */   transient int type;
/*     */   
/*     */   public AffineTransform() {
/*  73 */     this.type = 0;
/*  74 */     this.m00 = this.m11 = 1.0D;
/*  75 */     this.m10 = this.m01 = this.m02 = this.m12 = 0.0D;
/*     */   }
/*     */   
/*     */   public AffineTransform(AffineTransform t) {
/*  79 */     this.type = t.type;
/*  80 */     this.m00 = t.m00;
/*  81 */     this.m10 = t.m10;
/*  82 */     this.m01 = t.m01;
/*  83 */     this.m11 = t.m11;
/*  84 */     this.m02 = t.m02;
/*  85 */     this.m12 = t.m12;
/*     */   }
/*     */   
/*     */   public AffineTransform(float m00, float m10, float m01, float m11, float m02, float m12) {
/*  89 */     this.type = -1;
/*  90 */     this.m00 = m00;
/*  91 */     this.m10 = m10;
/*  92 */     this.m01 = m01;
/*  93 */     this.m11 = m11;
/*  94 */     this.m02 = m02;
/*  95 */     this.m12 = m12;
/*     */   }
/*     */   
/*     */   public AffineTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
/*  99 */     this.type = -1;
/* 100 */     this.m00 = m00;
/* 101 */     this.m10 = m10;
/* 102 */     this.m01 = m01;
/* 103 */     this.m11 = m11;
/* 104 */     this.m02 = m02;
/* 105 */     this.m12 = m12;
/*     */   }
/*     */   
/*     */   public AffineTransform(float[] matrix) {
/* 109 */     this.type = -1;
/* 110 */     this.m00 = matrix[0];
/* 111 */     this.m10 = matrix[1];
/* 112 */     this.m01 = matrix[2];
/* 113 */     this.m11 = matrix[3];
/* 114 */     if (matrix.length > 4) {
/* 115 */       this.m02 = matrix[4];
/* 116 */       this.m12 = matrix[5];
/*     */     } 
/*     */   }
/*     */   
/*     */   public AffineTransform(double[] matrix) {
/* 121 */     this.type = -1;
/* 122 */     this.m00 = matrix[0];
/* 123 */     this.m10 = matrix[1];
/* 124 */     this.m01 = matrix[2];
/* 125 */     this.m11 = matrix[3];
/* 126 */     if (matrix.length > 4) {
/* 127 */       this.m02 = matrix[4];
/* 128 */       this.m12 = matrix[5];
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
/*     */   public int getType() {
/* 152 */     if (this.type != -1) {
/* 153 */       return this.type;
/*     */     }
/*     */     
/* 156 */     int type = 0;
/*     */     
/* 158 */     if (this.m00 * this.m01 + this.m10 * this.m11 != 0.0D) {
/* 159 */       type |= 0x20;
/* 160 */       return type;
/*     */     } 
/*     */     
/* 163 */     if (this.m02 != 0.0D || this.m12 != 0.0D) {
/* 164 */       type |= 0x1;
/*     */     }
/* 166 */     else if (this.m00 == 1.0D && this.m11 == 1.0D && this.m01 == 0.0D && this.m10 == 0.0D) {
/* 167 */       type = 0;
/* 168 */       return type;
/*     */     } 
/*     */     
/* 171 */     if (this.m00 * this.m11 - this.m01 * this.m10 < 0.0D) {
/* 172 */       type |= 0x40;
/*     */     }
/*     */     
/* 175 */     double dx = this.m00 * this.m00 + this.m10 * this.m10;
/* 176 */     double dy = this.m01 * this.m01 + this.m11 * this.m11;
/* 177 */     if (dx != dy) {
/* 178 */       type |= 0x4;
/*     */     }
/* 180 */     else if (dx != 1.0D) {
/* 181 */       type |= 0x2;
/*     */     } 
/*     */     
/* 184 */     if ((this.m00 == 0.0D && this.m11 == 0.0D) || (this.m10 == 0.0D && this.m01 == 0.0D && (this.m00 < 0.0D || this.m11 < 0.0D))) {
/*     */ 
/*     */       
/* 187 */       type |= 0x8;
/*     */     }
/* 189 */     else if (this.m01 != 0.0D || this.m10 != 0.0D) {
/* 190 */       type |= 0x10;
/*     */     } 
/*     */     
/* 193 */     return type;
/*     */   }
/*     */   
/*     */   public double getScaleX() {
/* 197 */     return this.m00;
/*     */   }
/*     */   
/*     */   public double getScaleY() {
/* 201 */     return this.m11;
/*     */   }
/*     */   
/*     */   public double getShearX() {
/* 205 */     return this.m01;
/*     */   }
/*     */   
/*     */   public double getShearY() {
/* 209 */     return this.m10;
/*     */   }
/*     */   
/*     */   public double getTranslateX() {
/* 213 */     return this.m02;
/*     */   }
/*     */   
/*     */   public double getTranslateY() {
/* 217 */     return this.m12;
/*     */   }
/*     */   
/*     */   public boolean isIdentity() {
/* 221 */     return (getType() == 0);
/*     */   }
/*     */   
/*     */   public void getMatrix(double[] matrix) {
/* 225 */     matrix[0] = this.m00;
/* 226 */     matrix[1] = this.m10;
/* 227 */     matrix[2] = this.m01;
/* 228 */     matrix[3] = this.m11;
/* 229 */     if (matrix.length > 4) {
/* 230 */       matrix[4] = this.m02;
/* 231 */       matrix[5] = this.m12;
/*     */     } 
/*     */   }
/*     */   
/*     */   public double getDeterminant() {
/* 236 */     return this.m00 * this.m11 - this.m01 * this.m10;
/*     */   }
/*     */   
/*     */   public void setTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
/* 240 */     this.type = -1;
/* 241 */     this.m00 = m00;
/* 242 */     this.m10 = m10;
/* 243 */     this.m01 = m01;
/* 244 */     this.m11 = m11;
/* 245 */     this.m02 = m02;
/* 246 */     this.m12 = m12;
/*     */   }
/*     */   
/*     */   public void setTransform(AffineTransform t) {
/* 250 */     this.type = t.type;
/* 251 */     setTransform(t.m00, t.m10, t.m01, t.m11, t.m02, t.m12);
/*     */   }
/*     */   
/*     */   public void setToIdentity() {
/* 255 */     this.type = 0;
/* 256 */     this.m00 = this.m11 = 1.0D;
/* 257 */     this.m10 = this.m01 = this.m02 = this.m12 = 0.0D;
/*     */   }
/*     */   
/*     */   public void setToTranslation(double mx, double my) {
/* 261 */     this.m00 = this.m11 = 1.0D;
/* 262 */     this.m01 = this.m10 = 0.0D;
/* 263 */     this.m02 = mx;
/* 264 */     this.m12 = my;
/* 265 */     if (mx == 0.0D && my == 0.0D) {
/* 266 */       this.type = 0;
/*     */     } else {
/* 268 */       this.type = 1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setToScale(double scx, double scy) {
/* 273 */     this.m00 = scx;
/* 274 */     this.m11 = scy;
/* 275 */     this.m10 = this.m01 = this.m02 = this.m12 = 0.0D;
/* 276 */     if (scx != 1.0D || scy != 1.0D) {
/* 277 */       this.type = -1;
/*     */     } else {
/* 279 */       this.type = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setToShear(double shx, double shy) {
/* 284 */     this.m00 = this.m11 = 1.0D;
/* 285 */     this.m02 = this.m12 = 0.0D;
/* 286 */     this.m01 = shx;
/* 287 */     this.m10 = shy;
/* 288 */     if (shx != 0.0D || shy != 0.0D) {
/* 289 */       this.type = -1;
/*     */     } else {
/* 291 */       this.type = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setToRotation(double angle) {
/* 296 */     double sin = Math.sin(angle);
/* 297 */     double cos = Math.cos(angle);
/* 298 */     if (Math.abs(cos) < 1.0E-10D) {
/* 299 */       cos = 0.0D;
/* 300 */       sin = (sin > 0.0D) ? 1.0D : -1.0D;
/*     */     }
/* 302 */     else if (Math.abs(sin) < 1.0E-10D) {
/* 303 */       sin = 0.0D;
/* 304 */       cos = (cos > 0.0D) ? 1.0D : -1.0D;
/*     */     } 
/* 306 */     this.m00 = this.m11 = cos;
/* 307 */     this.m01 = -sin;
/* 308 */     this.m10 = sin;
/* 309 */     this.m02 = this.m12 = 0.0D;
/* 310 */     this.type = -1;
/*     */   }
/*     */   
/*     */   public void setToRotation(double angle, double px, double py) {
/* 314 */     setToRotation(angle);
/* 315 */     this.m02 = px * (1.0D - this.m00) + py * this.m10;
/* 316 */     this.m12 = py * (1.0D - this.m00) - px * this.m10;
/* 317 */     this.type = -1;
/*     */   }
/*     */   
/*     */   public static AffineTransform getTranslateInstance(double mx, double my) {
/* 321 */     AffineTransform t = new AffineTransform();
/* 322 */     t.setToTranslation(mx, my);
/* 323 */     return t;
/*     */   }
/*     */   
/*     */   public static AffineTransform getScaleInstance(double scx, double scY) {
/* 327 */     AffineTransform t = new AffineTransform();
/* 328 */     t.setToScale(scx, scY);
/* 329 */     return t;
/*     */   }
/*     */   
/*     */   public static AffineTransform getShearInstance(double shx, double shy) {
/* 333 */     AffineTransform m = new AffineTransform();
/* 334 */     m.setToShear(shx, shy);
/* 335 */     return m;
/*     */   }
/*     */   
/*     */   public static AffineTransform getRotateInstance(double angle) {
/* 339 */     AffineTransform t = new AffineTransform();
/* 340 */     t.setToRotation(angle);
/* 341 */     return t;
/*     */   }
/*     */   
/*     */   public static AffineTransform getRotateInstance(double angle, double x, double y) {
/* 345 */     AffineTransform t = new AffineTransform();
/* 346 */     t.setToRotation(angle, x, y);
/* 347 */     return t;
/*     */   }
/*     */   
/*     */   public void translate(double mx, double my) {
/* 351 */     concatenate(getTranslateInstance(mx, my));
/*     */   }
/*     */   
/*     */   public void scale(double scx, double scy) {
/* 355 */     concatenate(getScaleInstance(scx, scy));
/*     */   }
/*     */   
/*     */   public void shear(double shx, double shy) {
/* 359 */     concatenate(getShearInstance(shx, shy));
/*     */   }
/*     */   
/*     */   public void rotate(double angle) {
/* 363 */     concatenate(getRotateInstance(angle));
/*     */   }
/*     */   
/*     */   public void rotate(double angle, double px, double py) {
/* 367 */     concatenate(getRotateInstance(angle, px, py));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AffineTransform multiply(AffineTransform t1, AffineTransform t2) {
/* 377 */     return new AffineTransform(t1.m00 * t2.m00 + t1.m10 * t2.m01, t1.m00 * t2.m10 + t1.m10 * t2.m11, t1.m01 * t2.m00 + t1.m11 * t2.m01, t1.m01 * t2.m10 + t1.m11 * t2.m11, t1.m02 * t2.m00 + t1.m12 * t2.m01 + t2.m02, t1.m02 * t2.m10 + t1.m12 * t2.m11 + t2.m12);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void concatenate(AffineTransform t) {
/* 387 */     setTransform(multiply(t, this));
/*     */   }
/*     */   
/*     */   public void preConcatenate(AffineTransform t) {
/* 391 */     setTransform(multiply(this, t));
/*     */   }
/*     */   
/*     */   public AffineTransform createInverse() throws NoninvertibleTransformException {
/* 395 */     double det = getDeterminant();
/* 396 */     if (Math.abs(det) < 1.0E-10D)
/*     */     {
/* 398 */       throw new NoninvertibleTransformException(Messages.getString("awt.204"));
/*     */     }
/* 400 */     return new AffineTransform(this.m11 / det, -this.m10 / det, -this.m01 / det, this.m00 / det, (this.m01 * this.m12 - this.m11 * this.m02) / det, (this.m10 * this.m02 - this.m00 * this.m12) / det);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Point2D transform(Point2D src, Point2D dst) {
/* 411 */     if (dst == null) {
/* 412 */       if (src instanceof Point2D.Double) {
/* 413 */         dst = new Point2D.Double();
/*     */       } else {
/* 415 */         dst = new Point2D.Float();
/*     */       } 
/*     */     }
/*     */     
/* 419 */     double x = src.getX();
/* 420 */     double y = src.getY();
/*     */     
/* 422 */     dst.setLocation(x * this.m00 + y * this.m01 + this.m02, x * this.m10 + y * this.m11 + this.m12);
/* 423 */     return dst;
/*     */   }
/*     */   
/*     */   public void transform(Point2D[] src, int srcOff, Point2D[] dst, int dstOff, int length) {
/* 427 */     while (--length >= 0) {
/* 428 */       Point2D srcPoint = src[srcOff++];
/* 429 */       double x = srcPoint.getX();
/* 430 */       double y = srcPoint.getY();
/* 431 */       Point2D dstPoint = dst[dstOff];
/* 432 */       if (dstPoint == null) {
/* 433 */         if (srcPoint instanceof Point2D.Double) {
/* 434 */           dstPoint = new Point2D.Double();
/*     */         } else {
/* 436 */           dstPoint = new Point2D.Float();
/*     */         } 
/*     */       }
/* 439 */       dstPoint.setLocation(x * this.m00 + y * this.m01 + this.m02, x * this.m10 + y * this.m11 + this.m12);
/* 440 */       dst[dstOff++] = dstPoint;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void transform(double[] src, int srcOff, double[] dst, int dstOff, int length) {
/* 445 */     int step = 2;
/* 446 */     if (src == dst && srcOff < dstOff && dstOff < srcOff + length * 2) {
/* 447 */       srcOff = srcOff + length * 2 - 2;
/* 448 */       dstOff = dstOff + length * 2 - 2;
/* 449 */       step = -2;
/*     */     } 
/* 451 */     while (--length >= 0) {
/* 452 */       double x = src[srcOff + 0];
/* 453 */       double y = src[srcOff + 1];
/* 454 */       dst[dstOff + 0] = x * this.m00 + y * this.m01 + this.m02;
/* 455 */       dst[dstOff + 1] = x * this.m10 + y * this.m11 + this.m12;
/* 456 */       srcOff += step;
/* 457 */       dstOff += step;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void transform(float[] src, int srcOff, float[] dst, int dstOff, int length) {
/* 462 */     int step = 2;
/* 463 */     if (src == dst && srcOff < dstOff && dstOff < srcOff + length * 2) {
/* 464 */       srcOff = srcOff + length * 2 - 2;
/* 465 */       dstOff = dstOff + length * 2 - 2;
/* 466 */       step = -2;
/*     */     } 
/* 468 */     while (--length >= 0) {
/* 469 */       float x = src[srcOff + 0];
/* 470 */       float y = src[srcOff + 1];
/* 471 */       dst[dstOff + 0] = (float)(x * this.m00 + y * this.m01 + this.m02);
/* 472 */       dst[dstOff + 1] = (float)(x * this.m10 + y * this.m11 + this.m12);
/* 473 */       srcOff += step;
/* 474 */       dstOff += step;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void transform(float[] src, int srcOff, double[] dst, int dstOff, int length) {
/* 479 */     while (--length >= 0) {
/* 480 */       float x = src[srcOff++];
/* 481 */       float y = src[srcOff++];
/* 482 */       dst[dstOff++] = x * this.m00 + y * this.m01 + this.m02;
/* 483 */       dst[dstOff++] = x * this.m10 + y * this.m11 + this.m12;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void transform(double[] src, int srcOff, float[] dst, int dstOff, int length) {
/* 488 */     while (--length >= 0) {
/* 489 */       double x = src[srcOff++];
/* 490 */       double y = src[srcOff++];
/* 491 */       dst[dstOff++] = (float)(x * this.m00 + y * this.m01 + this.m02);
/* 492 */       dst[dstOff++] = (float)(x * this.m10 + y * this.m11 + this.m12);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Point2D deltaTransform(Point2D src, Point2D dst) {
/* 497 */     if (dst == null) {
/* 498 */       if (src instanceof Point2D.Double) {
/* 499 */         dst = new Point2D.Double();
/*     */       } else {
/* 501 */         dst = new Point2D.Float();
/*     */       } 
/*     */     }
/*     */     
/* 505 */     double x = src.getX();
/* 506 */     double y = src.getY();
/*     */     
/* 508 */     dst.setLocation(x * this.m00 + y * this.m01, x * this.m10 + y * this.m11);
/* 509 */     return dst;
/*     */   }
/*     */   
/*     */   public void deltaTransform(double[] src, int srcOff, double[] dst, int dstOff, int length) {
/* 513 */     while (--length >= 0) {
/* 514 */       double x = src[srcOff++];
/* 515 */       double y = src[srcOff++];
/* 516 */       dst[dstOff++] = x * this.m00 + y * this.m01;
/* 517 */       dst[dstOff++] = x * this.m10 + y * this.m11;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Point2D inverseTransform(Point2D src, Point2D dst) throws NoninvertibleTransformException {
/* 522 */     double det = getDeterminant();
/* 523 */     if (Math.abs(det) < 1.0E-10D)
/*     */     {
/* 525 */       throw new NoninvertibleTransformException(Messages.getString("awt.204"));
/*     */     }
/*     */     
/* 528 */     if (dst == null) {
/* 529 */       if (src instanceof Point2D.Double) {
/* 530 */         dst = new Point2D.Double();
/*     */       } else {
/* 532 */         dst = new Point2D.Float();
/*     */       } 
/*     */     }
/*     */     
/* 536 */     double x = src.getX() - this.m02;
/* 537 */     double y = src.getY() - this.m12;
/*     */     
/* 539 */     dst.setLocation((x * this.m11 - y * this.m01) / det, (y * this.m00 - x * this.m10) / det);
/* 540 */     return dst;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void inverseTransform(double[] src, int srcOff, double[] dst, int dstOff, int length) throws NoninvertibleTransformException {
/* 546 */     double det = getDeterminant();
/* 547 */     if (Math.abs(det) < 1.0E-10D)
/*     */     {
/* 549 */       throw new NoninvertibleTransformException(Messages.getString("awt.204"));
/*     */     }
/*     */     
/* 552 */     while (--length >= 0) {
/* 553 */       double x = src[srcOff++] - this.m02;
/* 554 */       double y = src[srcOff++] - this.m12;
/* 555 */       dst[dstOff++] = (x * this.m11 - y * this.m01) / det;
/* 556 */       dst[dstOff++] = (y * this.m00 - x * this.m10) / det;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void inverseTransform(float[] src, int srcOff, float[] dst, int dstOff, int length) throws NoninvertibleTransformException {
/* 563 */     float det = (float)getDeterminant();
/* 564 */     if (Math.abs(det) < 1.0E-10D)
/*     */     {
/* 566 */       throw new NoninvertibleTransformException(Messages.getString("awt.204"));
/*     */     }
/*     */     
/* 569 */     while (--length >= 0) {
/* 570 */       float x = src[srcOff++] - (float)this.m02;
/* 571 */       float y = src[srcOff++] - (float)this.m12;
/* 572 */       dst[dstOff++] = (x * (float)this.m11 - y * (float)this.m01) / det;
/* 573 */       dst[dstOff++] = (y * (float)this.m00 - x * (float)this.m10) / det;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Shape createTransformedShape(Shape src) {
/* 578 */     if (src == null) {
/* 579 */       return null;
/*     */     }
/* 581 */     if (src instanceof GeneralPath) {
/* 582 */       return ((GeneralPath)src).createTransformedShape(this);
/*     */     }
/* 584 */     PathIterator path = src.getPathIterator(this);
/* 585 */     GeneralPath dst = new GeneralPath(path.getWindingRule());
/* 586 */     dst.append(path, false);
/* 587 */     return dst;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 592 */     return 
/* 593 */       getClass().getName() + "[[" + this.m00 + ", " + this.m01 + ", " + this.m02 + "], [" + this.m10 + ", " + this.m11 + ", " + this.m12 + "]]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 601 */       return super.clone();
/* 602 */     } catch (CloneNotSupportedException e) {
/* 603 */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 609 */     HashCode hash = new HashCode();
/* 610 */     hash.append(this.m00);
/* 611 */     hash.append(this.m01);
/* 612 */     hash.append(this.m02);
/* 613 */     hash.append(this.m10);
/* 614 */     hash.append(this.m11);
/* 615 */     hash.append(this.m12);
/* 616 */     return hash.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 621 */     if (obj == this) {
/* 622 */       return true;
/*     */     }
/* 624 */     if (obj instanceof AffineTransform) {
/* 625 */       AffineTransform t = (AffineTransform)obj;
/* 626 */       return (this.m00 == t.m00 && this.m01 == t.m01 && this.m02 == t.m02 && this.m10 == t.m10 && this.m11 == t.m11 && this.m12 == t.m12);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 631 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 641 */     stream.defaultWriteObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 652 */     stream.defaultReadObject();
/* 653 */     this.type = -1;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/AffineTransform.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */