/*     */ package com.itextpdf.awt.geom;
/*     */ 
/*     */ import com.itextpdf.awt.geom.gl.Crossing;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GeneralPath
/*     */   implements Shape, Cloneable
/*     */ {
/*     */   public static final int WIND_EVEN_ODD = 0;
/*     */   public static final int WIND_NON_ZERO = 1;
/*     */   private static final int BUFFER_SIZE = 10;
/*     */   private static final int BUFFER_CAPACITY = 10;
/*     */   byte[] types;
/*     */   float[] points;
/*     */   int typeSize;
/*     */   int pointSize;
/*     */   int rule;
/*  74 */   static int[] pointShift = new int[] { 2, 2, 4, 6, 0 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class Iterator
/*     */     implements PathIterator
/*     */   {
/*     */     int typeIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int pointIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     GeneralPath p;
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
/*     */ 
/*     */     
/*     */     Iterator(GeneralPath path) {
/* 111 */       this(path, null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator(GeneralPath path, AffineTransform at) {
/* 120 */       this.p = path;
/* 121 */       this.t = at;
/*     */     }
/*     */     
/*     */     public int getWindingRule() {
/* 125 */       return this.p.getWindingRule();
/*     */     }
/*     */     
/*     */     public boolean isDone() {
/* 129 */       return (this.typeIndex >= this.p.typeSize);
/*     */     }
/*     */     
/*     */     public void next() {
/* 133 */       this.typeIndex++;
/*     */     }
/*     */     
/*     */     public int currentSegment(double[] coords) {
/* 137 */       if (isDone())
/*     */       {
/* 139 */         throw new NoSuchElementException(Messages.getString("awt.4B"));
/*     */       }
/* 141 */       int type = this.p.types[this.typeIndex];
/* 142 */       int count = GeneralPath.pointShift[type];
/* 143 */       for (int i = 0; i < count; i++) {
/* 144 */         coords[i] = this.p.points[this.pointIndex + i];
/*     */       }
/* 146 */       if (this.t != null) {
/* 147 */         this.t.transform(coords, 0, coords, 0, count / 2);
/*     */       }
/* 149 */       this.pointIndex += count;
/* 150 */       return type;
/*     */     }
/*     */     
/*     */     public int currentSegment(float[] coords) {
/* 154 */       if (isDone())
/*     */       {
/* 156 */         throw new NoSuchElementException(Messages.getString("awt.4B"));
/*     */       }
/* 158 */       int type = this.p.types[this.typeIndex];
/* 159 */       int count = GeneralPath.pointShift[type];
/* 160 */       System.arraycopy(this.p.points, this.pointIndex, coords, 0, count);
/* 161 */       if (this.t != null) {
/* 162 */         this.t.transform(coords, 0, coords, 0, count / 2);
/*     */       }
/* 164 */       this.pointIndex += count;
/* 165 */       return type;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public GeneralPath() {
/* 171 */     this(1, 10);
/*     */   }
/*     */   
/*     */   public GeneralPath(int rule) {
/* 175 */     this(rule, 10);
/*     */   }
/*     */   
/*     */   public GeneralPath(int rule, int initialCapacity) {
/* 179 */     setWindingRule(rule);
/* 180 */     this.types = new byte[initialCapacity];
/* 181 */     this.points = new float[initialCapacity * 2];
/*     */   }
/*     */   
/*     */   public GeneralPath(Shape shape) {
/* 185 */     this(1, 10);
/* 186 */     PathIterator p = shape.getPathIterator(null);
/* 187 */     setWindingRule(p.getWindingRule());
/* 188 */     append(p, false);
/*     */   }
/*     */   
/*     */   public void setWindingRule(int rule) {
/* 192 */     if (rule != 0 && rule != 1)
/*     */     {
/* 194 */       throw new IllegalArgumentException(Messages.getString("awt.209"));
/*     */     }
/* 196 */     this.rule = rule;
/*     */   }
/*     */   
/*     */   public int getWindingRule() {
/* 200 */     return this.rule;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void checkBuf(int pointCount, boolean checkMove) {
/* 208 */     if (checkMove && this.typeSize == 0)
/*     */     {
/* 210 */       throw new IllegalPathStateException(Messages.getString("awt.20A"));
/*     */     }
/* 212 */     if (this.typeSize == this.types.length) {
/* 213 */       byte[] tmp = new byte[this.typeSize + 10];
/* 214 */       System.arraycopy(this.types, 0, tmp, 0, this.typeSize);
/* 215 */       this.types = tmp;
/*     */     } 
/* 217 */     if (this.pointSize + pointCount > this.points.length) {
/* 218 */       float[] tmp = new float[this.pointSize + Math.max(20, pointCount)];
/* 219 */       System.arraycopy(this.points, 0, tmp, 0, this.pointSize);
/* 220 */       this.points = tmp;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void moveTo(float x, float y) {
/* 225 */     if (this.typeSize > 0 && this.types[this.typeSize - 1] == 0) {
/* 226 */       this.points[this.pointSize - 2] = x;
/* 227 */       this.points[this.pointSize - 1] = y;
/*     */     } else {
/* 229 */       checkBuf(2, false);
/* 230 */       this.types[this.typeSize++] = 0;
/* 231 */       this.points[this.pointSize++] = x;
/* 232 */       this.points[this.pointSize++] = y;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void lineTo(float x, float y) {
/* 237 */     checkBuf(2, true);
/* 238 */     this.types[this.typeSize++] = 1;
/* 239 */     this.points[this.pointSize++] = x;
/* 240 */     this.points[this.pointSize++] = y;
/*     */   }
/*     */   
/*     */   public void quadTo(float x1, float y1, float x2, float y2) {
/* 244 */     checkBuf(4, true);
/* 245 */     this.types[this.typeSize++] = 2;
/* 246 */     this.points[this.pointSize++] = x1;
/* 247 */     this.points[this.pointSize++] = y1;
/* 248 */     this.points[this.pointSize++] = x2;
/* 249 */     this.points[this.pointSize++] = y2;
/*     */   }
/*     */   
/*     */   public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
/* 253 */     checkBuf(6, true);
/* 254 */     this.types[this.typeSize++] = 3;
/* 255 */     this.points[this.pointSize++] = x1;
/* 256 */     this.points[this.pointSize++] = y1;
/* 257 */     this.points[this.pointSize++] = x2;
/* 258 */     this.points[this.pointSize++] = y2;
/* 259 */     this.points[this.pointSize++] = x3;
/* 260 */     this.points[this.pointSize++] = y3;
/*     */   }
/*     */   
/*     */   public void closePath() {
/* 264 */     if (this.typeSize == 0 || this.types[this.typeSize - 1] != 4) {
/* 265 */       checkBuf(0, true);
/* 266 */       this.types[this.typeSize++] = 4;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void append(Shape shape, boolean connect) {
/* 271 */     PathIterator p = shape.getPathIterator(null);
/* 272 */     append(p, connect);
/*     */   }
/*     */   
/*     */   public void append(PathIterator path, boolean connect) {
/* 276 */     while (!path.isDone()) {
/* 277 */       float[] coords = new float[6];
/* 278 */       switch (path.currentSegment(coords)) {
/*     */         case 0:
/* 280 */           if (!connect || this.typeSize == 0) {
/* 281 */             moveTo(coords[0], coords[1]);
/*     */             break;
/*     */           } 
/* 284 */           if (this.types[this.typeSize - 1] != 4 && this.points[this.pointSize - 2] == coords[0] && this.points[this.pointSize - 1] == coords[1]) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 1:
/* 292 */           lineTo(coords[0], coords[1]);
/*     */           break;
/*     */         case 2:
/* 295 */           quadTo(coords[0], coords[1], coords[2], coords[3]);
/*     */           break;
/*     */         case 3:
/* 298 */           curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
/*     */           break;
/*     */         case 4:
/* 301 */           closePath();
/*     */           break;
/*     */       } 
/* 304 */       path.next();
/* 305 */       connect = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Point2D getCurrentPoint() {
/* 310 */     if (this.typeSize == 0) {
/* 311 */       return null;
/*     */     }
/* 313 */     int j = this.pointSize - 2;
/* 314 */     if (this.types[this.typeSize - 1] == 4)
/*     */     {
/* 316 */       for (int i = this.typeSize - 2; i > 0; i--) {
/* 317 */         int type = this.types[i];
/* 318 */         if (type == 0) {
/*     */           break;
/*     */         }
/* 321 */         j -= pointShift[type];
/*     */       } 
/*     */     }
/* 324 */     return new Point2D.Float(this.points[j], this.points[j + 1]);
/*     */   }
/*     */   
/*     */   public void reset() {
/* 328 */     this.typeSize = 0;
/* 329 */     this.pointSize = 0;
/*     */   }
/*     */   
/*     */   public void transform(AffineTransform t) {
/* 333 */     t.transform(this.points, 0, this.points, 0, this.pointSize / 2);
/*     */   }
/*     */   
/*     */   public Shape createTransformedShape(AffineTransform t) {
/* 337 */     GeneralPath p = (GeneralPath)clone();
/* 338 */     if (t != null) {
/* 339 */       p.transform(t);
/*     */     }
/* 341 */     return p; } public Rectangle2D getBounds2D() {
/*     */     float rx1;
/*     */     float ry1;
/*     */     float rx2;
/*     */     float ry2;
/* 346 */     if (this.pointSize == 0) {
/* 347 */       rx1 = ry1 = rx2 = ry2 = 0.0F;
/*     */     } else {
/* 349 */       int i = this.pointSize - 1;
/* 350 */       ry1 = ry2 = this.points[i--];
/* 351 */       rx1 = rx2 = this.points[i--];
/* 352 */       while (i > 0) {
/* 353 */         float y = this.points[i--];
/* 354 */         float x = this.points[i--];
/* 355 */         if (x < rx1) {
/* 356 */           rx1 = x;
/*     */         }
/* 358 */         else if (x > rx2) {
/* 359 */           rx2 = x;
/*     */         } 
/* 361 */         if (y < ry1) {
/* 362 */           ry1 = y; continue;
/*     */         } 
/* 364 */         if (y > ry2) {
/* 365 */           ry2 = y;
/*     */         }
/*     */       } 
/*     */     } 
/* 369 */     return new Rectangle2D.Float(rx1, ry1, rx2 - rx1, ry2 - ry1);
/*     */   }
/*     */   
/*     */   public Rectangle getBounds() {
/* 373 */     return getBounds2D().getBounds();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isInside(int cross) {
/* 382 */     if (this.rule == 1) {
/* 383 */       return Crossing.isInsideNonZero(cross);
/*     */     }
/* 385 */     return Crossing.isInsideEvenOdd(cross);
/*     */   }
/*     */   
/*     */   public boolean contains(double px, double py) {
/* 389 */     return isInside(Crossing.crossShape(this, px, py));
/*     */   }
/*     */   
/*     */   public boolean contains(double rx, double ry, double rw, double rh) {
/* 393 */     int cross = Crossing.intersectShape(this, rx, ry, rw, rh);
/* 394 */     return (cross != 255 && isInside(cross));
/*     */   }
/*     */   
/*     */   public boolean intersects(double rx, double ry, double rw, double rh) {
/* 398 */     int cross = Crossing.intersectShape(this, rx, ry, rw, rh);
/* 399 */     return (cross == 255 || isInside(cross));
/*     */   }
/*     */   
/*     */   public boolean contains(Point2D p) {
/* 403 */     return contains(p.getX(), p.getY());
/*     */   }
/*     */   
/*     */   public boolean contains(Rectangle2D r) {
/* 407 */     return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
/*     */   }
/*     */   
/*     */   public boolean intersects(Rectangle2D r) {
/* 411 */     return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
/*     */   }
/*     */   
/*     */   public PathIterator getPathIterator(AffineTransform t) {
/* 415 */     return new Iterator(this, t);
/*     */   }
/*     */   
/*     */   public PathIterator getPathIterator(AffineTransform t, double flatness) {
/* 419 */     return new FlatteningPathIterator(getPathIterator(t), flatness);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 425 */       GeneralPath p = (GeneralPath)super.clone();
/* 426 */       p.types = (byte[])this.types.clone();
/* 427 */       p.points = (float[])this.points.clone();
/* 428 */       return p;
/* 429 */     } catch (CloneNotSupportedException e) {
/* 430 */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/GeneralPath.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */