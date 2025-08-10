/*     */ package com.itextpdf.text.pdf.parser.clipper;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ public class ClipperOffset
/*     */ {
/*     */   private Paths destPolys;
/*     */   private Path srcPoly;
/*     */   private Path destPoly;
/*     */   private final List<Point.DoublePoint> normals;
/*     */   private double delta;
/*     */   private double inA;
/*     */   private double sin;
/*     */   private double cos;
/*     */   private double miterLim;
/*     */   private double stepsPerRad;
/*     */   private Point.LongPoint lowest;
/*     */   private final PolyNode polyNodes;
/*     */   private final double arcTolerance;
/*     */   private final double miterLimit;
/*     */   private static final double TWO_PI = 6.283185307179586D;
/*     */   private static final double DEFAULT_ARC_TOLERANCE = 0.25D;
/*     */   private static final double TOLERANCE = 1.0E-20D;
/*     */   
/*     */   private static boolean nearZero(double val) {
/*  60 */     return (val > -1.0E-20D && val < 1.0E-20D);
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
/*     */   public ClipperOffset() {
/*  84 */     this(2.0D, 0.25D);
/*     */   }
/*     */   
/*     */   public ClipperOffset(double miterLimit) {
/*  88 */     this(miterLimit, 0.25D);
/*     */   }
/*     */   
/*     */   public ClipperOffset(double miterLimit, double arcTolerance) {
/*  92 */     this.miterLimit = miterLimit;
/*  93 */     this.arcTolerance = arcTolerance;
/*  94 */     this.lowest = new Point.LongPoint();
/*  95 */     this.lowest.setX(Long.valueOf(-1L));
/*  96 */     this.polyNodes = new PolyNode();
/*  97 */     this.normals = new ArrayList<Point.DoublePoint>();
/*     */   }
/*     */   
/*     */   public void addPath(Path path, Clipper.JoinType joinType, Clipper.EndType endType) {
/* 101 */     int highI = path.size() - 1;
/* 102 */     if (highI < 0) {
/*     */       return;
/*     */     }
/* 105 */     PolyNode newNode = new PolyNode();
/* 106 */     newNode.setJoinType(joinType);
/* 107 */     newNode.setEndType(endType);
/*     */ 
/*     */     
/* 110 */     if (endType == Clipper.EndType.CLOSED_LINE || endType == Clipper.EndType.CLOSED_POLYGON) {
/* 111 */       while (highI > 0 && path.get(0) == path.get(highI)) {
/* 112 */         highI--;
/*     */       }
/*     */     }
/*     */     
/* 116 */     newNode.getPolygon().add(path.get(0));
/* 117 */     int j = 0, k = 0;
/* 118 */     for (int i = 1; i <= highI; i++) {
/* 119 */       if (newNode.getPolygon().get(j) != path.get(i)) {
/* 120 */         j++;
/* 121 */         newNode.getPolygon().add(path.get(i));
/* 122 */         if (path.get(i).getY() > newNode.getPolygon().get(k).getY() || (path.get(i).getY() == newNode.getPolygon().get(k).getY() && path
/* 123 */           .get(i).getX() < newNode.getPolygon().get(k).getX())) {
/* 124 */           k = j;
/*     */         }
/*     */       } 
/*     */     } 
/* 128 */     if (endType == Clipper.EndType.CLOSED_POLYGON && j < 2) {
/*     */       return;
/*     */     }
/*     */     
/* 132 */     this.polyNodes.addChild(newNode);
/*     */ 
/*     */     
/* 135 */     if (endType != Clipper.EndType.CLOSED_POLYGON) {
/*     */       return;
/*     */     }
/* 138 */     if (this.lowest.getX() < 0L) {
/* 139 */       this.lowest = new Point.LongPoint((this.polyNodes.getChildCount() - 1), k);
/*     */     } else {
/*     */       
/* 142 */       Point.LongPoint ip = ((PolyNode)this.polyNodes.getChilds().get((int)this.lowest.getX())).getPolygon().get((int)this.lowest.getY());
/* 143 */       if (newNode.getPolygon().get(k).getY() > ip.getY() || (newNode.getPolygon().get(k).getY() == ip.getY() && newNode
/* 144 */         .getPolygon().get(k).getX() < ip.getX())) {
/* 145 */         this.lowest = new Point.LongPoint((this.polyNodes.getChildCount() - 1), k);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addPaths(Paths paths, Clipper.JoinType joinType, Clipper.EndType endType) {
/* 151 */     for (Path p : paths) {
/* 152 */       addPath(p, joinType, endType);
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/* 157 */     this.polyNodes.getChilds().clear();
/* 158 */     this.lowest.setX(Long.valueOf(-1L));
/*     */   }
/*     */   
/*     */   private void doMiter(int j, int k, double r) {
/* 162 */     double q = this.delta / r;
/* 163 */     this.destPoly.add(new Point.LongPoint(Math.round(this.srcPoly.get(j).getX() + (((Point.DoublePoint)this.normals.get(k)).getX() + ((Point.DoublePoint)this.normals.get(j)).getX()) * q), 
/* 164 */           Math.round(this.srcPoly.get(j).getY() + (((Point.DoublePoint)this.normals.get(k)).getY() + ((Point.DoublePoint)this.normals.get(j)).getY()) * q)));
/*     */   }
/*     */   private void doOffset(double delta) {
/*     */     double y;
/* 168 */     this.destPolys = new Paths();
/* 169 */     this.delta = delta;
/*     */ 
/*     */     
/* 172 */     if (nearZero(delta)) {
/* 173 */       for (int j = 0; j < this.polyNodes.getChildCount(); j++) {
/* 174 */         PolyNode node = this.polyNodes.getChilds().get(j);
/* 175 */         if (node.getEndType() == Clipper.EndType.CLOSED_POLYGON) {
/* 176 */           this.destPolys.add(node.getPolygon());
/*     */         }
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 183 */     if (this.miterLimit > 2.0D) {
/* 184 */       this.miterLim = 2.0D / this.miterLimit * this.miterLimit;
/*     */     } else {
/*     */       
/* 187 */       this.miterLim = 0.5D;
/*     */     } 
/*     */ 
/*     */     
/* 191 */     if (this.arcTolerance <= 0.0D) {
/* 192 */       y = 0.25D;
/*     */     }
/* 194 */     else if (this.arcTolerance > Math.abs(delta) * 0.25D) {
/* 195 */       y = Math.abs(delta) * 0.25D;
/*     */     } else {
/*     */       
/* 198 */       y = this.arcTolerance;
/*     */     } 
/*     */     
/* 201 */     double steps = Math.PI / Math.acos(1.0D - y / Math.abs(delta));
/* 202 */     this.sin = Math.sin(6.283185307179586D / steps);
/* 203 */     this.cos = Math.cos(6.283185307179586D / steps);
/* 204 */     this.stepsPerRad = steps / 6.283185307179586D;
/* 205 */     if (delta < 0.0D) {
/* 206 */       this.sin = -this.sin;
/*     */     }
/*     */     
/* 209 */     for (int i = 0; i < this.polyNodes.getChildCount(); i++) {
/* 210 */       PolyNode node = this.polyNodes.getChilds().get(i);
/* 211 */       this.srcPoly = node.getPolygon();
/*     */       
/* 213 */       int len = this.srcPoly.size();
/*     */       
/* 215 */       if (len != 0 && (delta > 0.0D || (len >= 3 && node.getEndType() == Clipper.EndType.CLOSED_POLYGON))) {
/*     */ 
/*     */ 
/*     */         
/* 219 */         this.destPoly = new Path();
/*     */         
/* 221 */         if (len == 1) {
/* 222 */           if (node.getJoinType() == Clipper.JoinType.ROUND) {
/* 223 */             double X = 1.0D, Y = 0.0D;
/* 224 */             for (int j = 1; j <= steps; j++) {
/* 225 */               this.destPoly.add(new Point.LongPoint(Math.round(this.srcPoly.get(0).getX() + X * delta), Math.round(this.srcPoly.get(0).getY() + Y * delta)));
/*     */               
/* 227 */               double X2 = X;
/* 228 */               X = X * this.cos - this.sin * Y;
/* 229 */               Y = X2 * this.sin + Y * this.cos;
/*     */             } 
/*     */           } else {
/*     */             
/* 233 */             double X = -1.0D, Y = -1.0D;
/* 234 */             for (int j = 0; j < 4; j++) {
/* 235 */               this.destPoly.add(new Point.LongPoint(Math.round(this.srcPoly.get(0).getX() + X * delta), Math.round(this.srcPoly.get(0).getY() + Y * delta)));
/*     */               
/* 237 */               if (X < 0.0D) {
/* 238 */                 X = 1.0D;
/*     */               }
/* 240 */               else if (Y < 0.0D) {
/* 241 */                 Y = 1.0D;
/*     */               } else {
/*     */                 
/* 244 */                 X = -1.0D;
/*     */               } 
/*     */             } 
/*     */           } 
/* 248 */           this.destPolys.add(this.destPoly);
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 253 */           this.normals.clear();
/* 254 */           for (int j = 0; j < len - 1; j++) {
/* 255 */             this.normals.add(Point.getUnitNormal(this.srcPoly.get(j), this.srcPoly.get(j + 1)));
/*     */           }
/* 257 */           if (node.getEndType() == Clipper.EndType.CLOSED_LINE || node.getEndType() == Clipper.EndType.CLOSED_POLYGON) {
/* 258 */             this.normals.add(Point.getUnitNormal(this.srcPoly.get(len - 1), this.srcPoly.get(0)));
/*     */           } else {
/*     */             
/* 261 */             this.normals.add(new Point.DoublePoint(this.normals.get(len - 2)));
/*     */           } 
/*     */           
/* 264 */           if (node.getEndType() == Clipper.EndType.CLOSED_POLYGON) {
/* 265 */             int[] k = { len - 1 };
/* 266 */             for (int m = 0; m < len; m++) {
/* 267 */               offsetPoint(m, k, node.getJoinType());
/*     */             }
/* 269 */             this.destPolys.add(this.destPoly);
/*     */           }
/* 271 */           else if (node.getEndType() == Clipper.EndType.CLOSED_LINE) {
/* 272 */             int[] k = { len - 1 };
/* 273 */             for (int m = 0; m < len; m++) {
/* 274 */               offsetPoint(m, k, node.getJoinType());
/*     */             }
/* 276 */             this.destPolys.add(this.destPoly);
/* 277 */             this.destPoly = new Path();
/*     */             
/* 279 */             Point.DoublePoint n = this.normals.get(len - 1); int i1;
/* 280 */             for (i1 = len - 1; i1 > 0; i1--) {
/* 281 */               this.normals.set(i1, new Point.DoublePoint(-((Point.DoublePoint)this.normals.get(i1 - 1)).getX(), -((Point.DoublePoint)this.normals.get(i1 - 1)).getY()));
/*     */             }
/* 283 */             this.normals.set(0, new Point.DoublePoint(-n.getX(), -n.getY(), 0.0D));
/* 284 */             k[0] = 0;
/* 285 */             for (i1 = len - 1; i1 >= 0; i1--) {
/* 286 */               offsetPoint(i1, k, node.getJoinType());
/*     */             }
/* 288 */             this.destPolys.add(this.destPoly);
/*     */           } else {
/*     */             
/* 291 */             int[] k = new int[1];
/* 292 */             for (int m = 1; m < len - 1; m++) {
/* 293 */               offsetPoint(m, k, node.getJoinType());
/*     */             }
/*     */ 
/*     */             
/* 297 */             if (node.getEndType() == Clipper.EndType.OPEN_BUTT) {
/* 298 */               int i1 = len - 1;
/* 299 */               Point.LongPoint pt1 = new Point.LongPoint(Math.round(this.srcPoly.get(i1).getX() + ((Point.DoublePoint)this.normals.get(i1)).getX() * delta), Math.round(this.srcPoly.get(i1)
/* 300 */                     .getY() + ((Point.DoublePoint)this.normals.get(i1)).getY() * delta), 0L);
/* 301 */               this.destPoly.add(pt1);
/* 302 */               pt1 = new Point.LongPoint(Math.round(this.srcPoly.get(i1).getX() - ((Point.DoublePoint)this.normals.get(i1)).getX() * delta), Math.round(this.srcPoly.get(i1)
/* 303 */                     .getY() - ((Point.DoublePoint)this.normals.get(i1)).getY() * delta), 0L);
/* 304 */               this.destPoly.add(pt1);
/*     */             } else {
/*     */               
/* 307 */               int i1 = len - 1;
/* 308 */               k[0] = len - 2;
/* 309 */               this.inA = 0.0D;
/* 310 */               this.normals.set(i1, new Point.DoublePoint(-((Point.DoublePoint)this.normals.get(i1)).getX(), -((Point.DoublePoint)this.normals.get(i1)).getY()));
/* 311 */               if (node.getEndType() == Clipper.EndType.OPEN_SQUARE) {
/* 312 */                 doSquare(i1, k[0], true);
/*     */               } else {
/*     */                 
/* 315 */                 doRound(i1, k[0]);
/*     */               } 
/*     */             } 
/*     */             
/*     */             int n;
/* 320 */             for (n = len - 1; n > 0; n--) {
/* 321 */               this.normals.set(n, new Point.DoublePoint(-((Point.DoublePoint)this.normals.get(n - 1)).getX(), -((Point.DoublePoint)this.normals.get(n - 1)).getY()));
/*     */             }
/*     */             
/* 324 */             this.normals.set(0, new Point.DoublePoint(-((Point.DoublePoint)this.normals.get(1)).getX(), -((Point.DoublePoint)this.normals.get(1)).getY()));
/*     */             
/* 326 */             k[0] = len - 1;
/* 327 */             for (n = k[0] - 1; n > 0; n--) {
/* 328 */               offsetPoint(n, k, node.getJoinType());
/*     */             }
/*     */             
/* 331 */             if (node.getEndType() == Clipper.EndType.OPEN_BUTT) {
/* 332 */               Point.LongPoint pt1 = new Point.LongPoint(Math.round(this.srcPoly.get(0).getX() - ((Point.DoublePoint)this.normals.get(0)).getX() * delta), Math.round(this.srcPoly.get(0)
/* 333 */                     .getY() - ((Point.DoublePoint)this.normals.get(0)).getY() * delta));
/* 334 */               this.destPoly.add(pt1);
/* 335 */               pt1 = new Point.LongPoint(Math.round(this.srcPoly.get(0).getX() + ((Point.DoublePoint)this.normals.get(0)).getX() * delta), Math.round(this.srcPoly.get(0)
/* 336 */                     .getY() + ((Point.DoublePoint)this.normals.get(0)).getY() * delta));
/* 337 */               this.destPoly.add(pt1);
/*     */             } else {
/*     */               
/* 340 */               k[0] = 1;
/* 341 */               this.inA = 0.0D;
/* 342 */               if (node.getEndType() == Clipper.EndType.OPEN_SQUARE) {
/* 343 */                 doSquare(0, 1, true);
/*     */               } else {
/*     */                 
/* 346 */                 doRound(0, 1);
/*     */               } 
/*     */             } 
/* 349 */             this.destPolys.add(this.destPoly);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } private void doRound(int j, int k) {
/* 355 */     double a = Math.atan2(this.inA, ((Point.DoublePoint)this.normals.get(k)).getX() * ((Point.DoublePoint)this.normals.get(j)).getX() + ((Point.DoublePoint)this.normals.get(k)).getY() * ((Point.DoublePoint)this.normals.get(j)).getY());
/* 356 */     int steps = Math.max((int)Math.round(this.stepsPerRad * Math.abs(a)), 1);
/*     */     
/* 358 */     double X = ((Point.DoublePoint)this.normals.get(k)).getX(), Y = ((Point.DoublePoint)this.normals.get(k)).getY();
/* 359 */     for (int i = 0; i < steps; i++) {
/* 360 */       this.destPoly.add(new Point.LongPoint(Math.round(this.srcPoly.get(j).getX() + X * this.delta), Math.round(this.srcPoly.get(j).getY() + Y * this.delta)));
/* 361 */       double X2 = X;
/* 362 */       X = X * this.cos - this.sin * Y;
/* 363 */       Y = X2 * this.sin + Y * this.cos;
/*     */     } 
/* 365 */     this.destPoly.add(new Point.LongPoint(Math.round(this.srcPoly.get(j).getX() + ((Point.DoublePoint)this.normals.get(j)).getX() * this.delta), Math.round(this.srcPoly.get(j).getY() + ((Point.DoublePoint)this.normals
/* 366 */             .get(j)).getY() * this.delta)));
/*     */   }
/*     */   
/*     */   private void doSquare(int j, int k, boolean addExtra) {
/* 370 */     double nkx = ((Point.DoublePoint)this.normals.get(k)).getX();
/* 371 */     double nky = ((Point.DoublePoint)this.normals.get(k)).getY();
/* 372 */     double njx = ((Point.DoublePoint)this.normals.get(j)).getX();
/* 373 */     double njy = ((Point.DoublePoint)this.normals.get(j)).getY();
/* 374 */     double sjx = this.srcPoly.get(j).getX();
/* 375 */     double sjy = this.srcPoly.get(j).getY();
/* 376 */     double dx = Math.tan(Math.atan2(this.inA, nkx * njx + nky * njy) / 4.0D);
/* 377 */     this.destPoly.add(new Point.LongPoint(Math.round(sjx + this.delta * (nkx - (addExtra ? (nky * dx) : 0.0D))), Math.round(sjy + this.delta * (nky + (addExtra ? (nkx * dx) : 0.0D))), 0L));
/* 378 */     this.destPoly.add(new Point.LongPoint(Math.round(sjx + this.delta * (njx + (addExtra ? (njy * dx) : 0.0D))), Math.round(sjy + this.delta * (njy - (addExtra ? (njx * dx) : 0.0D))), 0L));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Paths solution, double delta) {
/* 384 */     solution.clear();
/* 385 */     fixOrientations();
/* 386 */     doOffset(delta);
/*     */     
/* 388 */     DefaultClipper clpr = new DefaultClipper(1);
/* 389 */     clpr.addPaths(this.destPolys, Clipper.PolyType.SUBJECT, true);
/* 390 */     if (delta > 0.0D) {
/* 391 */       clpr.execute(Clipper.ClipType.UNION, solution, Clipper.PolyFillType.POSITIVE, Clipper.PolyFillType.POSITIVE);
/*     */     } else {
/*     */       
/* 394 */       LongRect r = this.destPolys.getBounds();
/* 395 */       Path outer = new Path(4);
/*     */       
/* 397 */       outer.add(new Point.LongPoint(r.left - 10L, r.bottom + 10L, 0L));
/* 398 */       outer.add(new Point.LongPoint(r.right + 10L, r.bottom + 10L, 0L));
/* 399 */       outer.add(new Point.LongPoint(r.right + 10L, r.top - 10L, 0L));
/* 400 */       outer.add(new Point.LongPoint(r.left - 10L, r.top - 10L, 0L));
/*     */       
/* 402 */       clpr.addPath(outer, Clipper.PolyType.SUBJECT, true);
/*     */       
/* 404 */       clpr.execute(Clipper.ClipType.UNION, solution, Clipper.PolyFillType.NEGATIVE, Clipper.PolyFillType.NEGATIVE);
/* 405 */       if (solution.size() > 0) {
/* 406 */         solution.remove(0);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(PolyTree solution, double delta) {
/* 414 */     solution.Clear();
/* 415 */     fixOrientations();
/* 416 */     doOffset(delta);
/*     */ 
/*     */     
/* 419 */     DefaultClipper clpr = new DefaultClipper(1);
/* 420 */     clpr.addPaths(this.destPolys, Clipper.PolyType.SUBJECT, true);
/* 421 */     if (delta > 0.0D) {
/* 422 */       clpr.execute(Clipper.ClipType.UNION, solution, Clipper.PolyFillType.POSITIVE, Clipper.PolyFillType.POSITIVE);
/*     */     } else {
/*     */       
/* 425 */       LongRect r = this.destPolys.getBounds();
/* 426 */       Path outer = new Path(4);
/*     */       
/* 428 */       outer.add(new Point.LongPoint(r.left - 10L, r.bottom + 10L, 0L));
/* 429 */       outer.add(new Point.LongPoint(r.right + 10L, r.bottom + 10L, 0L));
/* 430 */       outer.add(new Point.LongPoint(r.right + 10L, r.top - 10L, 0L));
/* 431 */       outer.add(new Point.LongPoint(r.left - 10L, r.top - 10L, 0L));
/*     */       
/* 433 */       clpr.addPath(outer, Clipper.PolyType.SUBJECT, true);
/*     */       
/* 435 */       clpr.execute(Clipper.ClipType.UNION, solution, Clipper.PolyFillType.NEGATIVE, Clipper.PolyFillType.NEGATIVE);
/*     */       
/* 437 */       if (solution.getChildCount() == 1 && ((PolyNode)solution.getChilds().get(0)).getChildCount() > 0) {
/* 438 */         PolyNode outerNode = solution.getChilds().get(0);
/* 439 */         solution.getChilds().set(0, outerNode.getChilds().get(0));
/* 440 */         ((PolyNode)solution.getChilds().get(0)).setParent(solution);
/* 441 */         for (int i = 1; i < outerNode.getChildCount(); i++) {
/* 442 */           solution.addChild(outerNode.getChilds().get(i));
/*     */         }
/*     */       } else {
/*     */         
/* 446 */         solution.Clear();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fixOrientations() {
/* 456 */     if (this.lowest.getX() >= 0L && !((PolyNode)this.polyNodes.childs.get((int)this.lowest.getX())).getPolygon().orientation()) {
/* 457 */       for (int i = 0; i < this.polyNodes.getChildCount(); i++) {
/* 458 */         PolyNode node = this.polyNodes.childs.get(i);
/* 459 */         if (node.getEndType() == Clipper.EndType.CLOSED_POLYGON || (node.getEndType() == Clipper.EndType.CLOSED_LINE && node.getPolygon().orientation())) {
/* 460 */           Collections.reverse(node.getPolygon());
/*     */         }
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 466 */       for (int i = 0; i < this.polyNodes.getChildCount(); i++) {
/* 467 */         PolyNode node = this.polyNodes.childs.get(i);
/* 468 */         if (node.getEndType() == Clipper.EndType.CLOSED_LINE && !node.getPolygon().orientation()) {
/* 469 */           Collections.reverse(node.getPolygon());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void offsetPoint(int j, int[] kV, Clipper.JoinType jointype) {
/* 477 */     int k = kV[0];
/* 478 */     double nkx = ((Point.DoublePoint)this.normals.get(k)).getX();
/* 479 */     double nky = ((Point.DoublePoint)this.normals.get(k)).getY();
/* 480 */     double njy = ((Point.DoublePoint)this.normals.get(j)).getY();
/* 481 */     double njx = ((Point.DoublePoint)this.normals.get(j)).getX();
/* 482 */     long sjx = this.srcPoly.get(j).getX();
/* 483 */     long sjy = this.srcPoly.get(j).getY();
/* 484 */     this.inA = nkx * njy - njx * nky;
/*     */     
/* 486 */     if (Math.abs(this.inA * this.delta) < 1.0D) {
/*     */ 
/*     */       
/* 489 */       double cosA = nkx * njx + njy * nky;
/* 490 */       if (cosA > 0.0D) {
/*     */         
/* 492 */         this.destPoly.add(new Point.LongPoint(Math.round(sjx + nkx * this.delta), Math.round(sjy + nky * this.delta), 0L));
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/* 497 */     } else if (this.inA > 1.0D) {
/* 498 */       this.inA = 1.0D;
/*     */     }
/* 500 */     else if (this.inA < -1.0D) {
/* 501 */       this.inA = -1.0D;
/*     */     } 
/*     */     
/* 504 */     if (this.inA * this.delta < 0.0D) {
/* 505 */       this.destPoly.add(new Point.LongPoint(Math.round(sjx + nkx * this.delta), Math.round(sjy + nky * this.delta)));
/* 506 */       this.destPoly.add(this.srcPoly.get(j));
/* 507 */       this.destPoly.add(new Point.LongPoint(Math.round(sjx + njx * this.delta), Math.round(sjy + njy * this.delta)));
/*     */     } else {
/*     */       double r;
/* 510 */       switch (jointype) {
/*     */         case MITER:
/* 512 */           r = 1.0D + njx * nkx + njy * nky;
/* 513 */           if (r >= this.miterLim) {
/* 514 */             doMiter(j, k, r);
/*     */             break;
/*     */           } 
/* 517 */           doSquare(j, k, false);
/*     */           break;
/*     */ 
/*     */         
/*     */         case BEVEL:
/* 522 */           doSquare(j, k, false);
/*     */           break;
/*     */         case ROUND:
/* 525 */           doRound(j, k);
/*     */           break;
/*     */       } 
/*     */     } 
/* 529 */     kV[0] = j;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/clipper/ClipperOffset.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */