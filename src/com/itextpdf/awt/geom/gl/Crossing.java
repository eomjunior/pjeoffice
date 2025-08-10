/*     */ package com.itextpdf.awt.geom.gl;
/*     */ 
/*     */ import com.itextpdf.awt.geom.PathIterator;
/*     */ import com.itextpdf.awt.geom.Shape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Crossing
/*     */ {
/*     */   static final double DELTA = 1.0E-5D;
/*     */   static final double ROOT_DELTA = 1.0E-10D;
/*     */   public static final int CROSSING = 255;
/*     */   static final int UNKNOWN = 254;
/*     */   
/*     */   public static int solveQuad(double[] eqn, double[] res) {
/*  58 */     double a = eqn[2];
/*  59 */     double b = eqn[1];
/*  60 */     double c = eqn[0];
/*  61 */     int rc = 0;
/*  62 */     if (a == 0.0D) {
/*  63 */       if (b == 0.0D) {
/*  64 */         return -1;
/*     */       }
/*  66 */       res[rc++] = -c / b;
/*     */     } else {
/*  68 */       double d = b * b - 4.0D * a * c;
/*     */       
/*  70 */       if (d < 0.0D) {
/*  71 */         return 0;
/*     */       }
/*  73 */       d = Math.sqrt(d);
/*  74 */       res[rc++] = (-b + d) / a * 2.0D;
/*     */       
/*  76 */       if (d != 0.0D) {
/*  77 */         res[rc++] = (-b - d) / a * 2.0D;
/*     */       }
/*     */     } 
/*  80 */     return fixRoots(res, rc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int solveCubic(double[] eqn, double[] res) {
/*  90 */     double d = eqn[3];
/*  91 */     if (d == 0.0D) {
/*  92 */       return solveQuad(eqn, res);
/*     */     }
/*  94 */     double a = eqn[2] / d;
/*  95 */     double b = eqn[1] / d;
/*  96 */     double c = eqn[0] / d;
/*  97 */     int rc = 0;
/*     */     
/*  99 */     double Q = (a * a - 3.0D * b) / 9.0D;
/* 100 */     double R = (2.0D * a * a * a - 9.0D * a * b + 27.0D * c) / 54.0D;
/* 101 */     double Q3 = Q * Q * Q;
/* 102 */     double R2 = R * R;
/* 103 */     double n = -a / 3.0D;
/*     */     
/* 105 */     if (R2 < Q3) {
/* 106 */       double t = Math.acos(R / Math.sqrt(Q3)) / 3.0D;
/* 107 */       double p = 2.0943951023931953D;
/* 108 */       double m = -2.0D * Math.sqrt(Q);
/* 109 */       res[rc++] = m * Math.cos(t) + n;
/* 110 */       res[rc++] = m * Math.cos(t + p) + n;
/* 111 */       res[rc++] = m * Math.cos(t - p) + n;
/*     */     } else {
/*     */       
/* 114 */       double A = Math.pow(Math.abs(R) + Math.sqrt(R2 - Q3), 0.3333333333333333D);
/* 115 */       if (R > 0.0D) {
/* 116 */         A = -A;
/*     */       }
/*     */       
/* 119 */       if (-1.0E-10D < A && A < 1.0E-10D) {
/* 120 */         res[rc++] = n;
/*     */       } else {
/* 122 */         double B = Q / A;
/* 123 */         res[rc++] = A + B + n;
/*     */         
/* 125 */         double delta = R2 - Q3;
/* 126 */         if (-1.0E-10D < delta && delta < 1.0E-10D) {
/* 127 */           res[rc++] = -(A + B) / 2.0D + n;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 132 */     return fixRoots(res, rc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int fixRoots(double[] res, int rc) {
/* 142 */     int tc = 0;
/* 143 */     for (int i = 0; i < rc; i++) {
/*     */       
/* 145 */       int j = i + 1; while (true) { if (j < rc) {
/* 146 */           if (isZero(res[i] - res[j]))
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 150 */         res[tc++] = res[i]; break; }
/*     */     
/*     */     } 
/* 153 */     return tc;
/*     */   }
/*     */   public static class QuadCurve { double ax;
/*     */     double ay;
/*     */     double bx;
/*     */     double by;
/*     */     double Ax;
/*     */     double Ay;
/*     */     double Bx;
/*     */     double By;
/*     */     
/*     */     public QuadCurve(double x1, double y1, double cx, double cy, double x2, double y2) {
/* 165 */       this.ax = x2 - x1;
/* 166 */       this.ay = y2 - y1;
/* 167 */       this.bx = cx - x1;
/* 168 */       this.by = cy - y1;
/*     */       
/* 170 */       this.Bx = this.bx + this.bx;
/* 171 */       this.Ax = this.ax - this.Bx;
/*     */       
/* 173 */       this.By = this.by + this.by;
/* 174 */       this.Ay = this.ay - this.By;
/*     */     }
/*     */     
/*     */     int cross(double[] res, int rc, double py1, double py2) {
/* 178 */       int cross = 0;
/*     */       
/* 180 */       for (int i = 0; i < rc; i++) {
/* 181 */         double t = res[i];
/*     */ 
/*     */         
/* 184 */         if (t >= -1.0E-5D && t <= 1.00001D)
/*     */         {
/*     */ 
/*     */           
/* 188 */           if (t < 1.0E-5D) {
/* 189 */             if (py1 < 0.0D && ((this.bx != 0.0D) ? this.bx : (this.ax - this.bx)) < 0.0D) {
/* 190 */               cross--;
/*     */             
/*     */             }
/*     */           
/*     */           }
/* 195 */           else if (t > 0.99999D) {
/* 196 */             if (py1 < this.ay && ((this.ax != this.bx) ? (this.ax - this.bx) : this.bx) > 0.0D) {
/* 197 */               cross++;
/*     */             }
/*     */           }
/*     */           else {
/*     */             
/* 202 */             double ry = t * (t * this.Ay + this.By);
/*     */             
/* 204 */             if (ry > py2) {
/* 205 */               double rxt = t * this.Ax + this.bx;
/*     */               
/* 207 */               if (rxt <= -1.0E-5D || rxt >= 1.0E-5D)
/*     */               {
/*     */                 
/* 210 */                 cross += (rxt > 0.0D) ? 1 : -1; } 
/*     */             } 
/*     */           }  } 
/*     */       } 
/* 214 */       return cross;
/*     */     }
/*     */     
/*     */     int solvePoint(double[] res, double px) {
/* 218 */       double[] eqn = { -px, this.Bx, this.Ax };
/* 219 */       return Crossing.solveQuad(eqn, res);
/*     */     }
/*     */     
/*     */     int solveExtrem(double[] res) {
/* 223 */       int rc = 0;
/* 224 */       if (this.Ax != 0.0D) {
/* 225 */         res[rc++] = -this.Bx / (this.Ax + this.Ax);
/*     */       }
/* 227 */       if (this.Ay != 0.0D) {
/* 228 */         res[rc++] = -this.By / (this.Ay + this.Ay);
/*     */       }
/* 230 */       return rc;
/*     */     }
/*     */     
/*     */     int addBound(double[] bound, int bc, double[] res, int rc, double minX, double maxX, boolean changeId, int id) {
/* 234 */       for (int i = 0; i < rc; i++) {
/* 235 */         double t = res[i];
/* 236 */         if (t > -1.0E-5D && t < 1.00001D) {
/* 237 */           double rx = t * (t * this.Ax + this.Bx);
/* 238 */           if (minX <= rx && rx <= maxX) {
/* 239 */             bound[bc++] = t;
/* 240 */             bound[bc++] = rx;
/* 241 */             bound[bc++] = t * (t * this.Ay + this.By);
/* 242 */             bound[bc++] = id;
/* 243 */             if (changeId) {
/* 244 */               id++;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 249 */       return bc;
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class CubicCurve
/*     */   {
/*     */     double ax;
/*     */     double ay;
/*     */     double bx;
/*     */     double by;
/*     */     double cx;
/*     */     double cy;
/*     */     double Ax;
/*     */     
/*     */     public CubicCurve(double x1, double y1, double cx1, double cy1, double cx2, double cy2, double x2, double y2) {
/* 264 */       this.ax = x2 - x1;
/* 265 */       this.ay = y2 - y1;
/* 266 */       this.bx = cx1 - x1;
/* 267 */       this.by = cy1 - y1;
/* 268 */       this.cx = cx2 - x1;
/* 269 */       this.cy = cy2 - y1;
/*     */       
/* 271 */       this.Cx = this.bx + this.bx + this.bx;
/* 272 */       this.Bx = this.cx + this.cx + this.cx - this.Cx - this.Cx;
/* 273 */       this.Ax = this.ax - this.Bx - this.Cx;
/*     */       
/* 275 */       this.Cy = this.by + this.by + this.by;
/* 276 */       this.By = this.cy + this.cy + this.cy - this.Cy - this.Cy;
/* 277 */       this.Ay = this.ay - this.By - this.Cy;
/*     */       
/* 279 */       this.Ax3 = this.Ax + this.Ax + this.Ax;
/* 280 */       this.Bx2 = this.Bx + this.Bx;
/*     */     }
/*     */     double Ay; double Bx; double By; double Cx; double Cy; double Ax3; double Bx2;
/*     */     int cross(double[] res, int rc, double py1, double py2) {
/* 284 */       int cross = 0;
/* 285 */       for (int i = 0; i < rc; i++) {
/* 286 */         double t = res[i];
/*     */ 
/*     */         
/* 289 */         if (t < -1.0E-5D || t > 1.00001D) {
/*     */           continue;
/*     */         }
/*     */         
/* 293 */         if (t < 1.0E-5D) {
/* 294 */           if (py1 < 0.0D) if (((this.bx != 0.0D) ? this.bx : ((this.cx != this.bx) ? (this.cx - this.bx) : (this.ax - this.cx))) < 0.0D) {
/* 295 */               cross--;
/*     */             }
/*     */           
/*     */           continue;
/*     */         } 
/* 300 */         if (t > 0.99999D) {
/* 301 */           if (py1 < this.ay) if (((this.ax != this.cx) ? (this.ax - this.cx) : ((this.cx != this.bx) ? (this.cx - this.bx) : this.bx)) > 0.0D) {
/* 302 */               cross++;
/*     */             }
/*     */           
/*     */           continue;
/*     */         } 
/* 307 */         double ry = t * (t * (t * this.Ay + this.By) + this.Cy);
/*     */         
/* 309 */         if (ry > py2) {
/* 310 */           double rxt = t * (t * this.Ax3 + this.Bx2) + this.Cx;
/*     */           
/* 312 */           if (rxt > -1.0E-5D && rxt < 1.0E-5D) {
/* 313 */             rxt = t * (this.Ax3 + this.Ax3) + this.Bx2;
/*     */             
/* 315 */             if (rxt < -1.0E-5D || rxt > 1.0E-5D) {
/*     */               continue;
/*     */             }
/*     */             
/* 319 */             rxt = this.ax;
/*     */           } 
/* 321 */           cross += (rxt > 0.0D) ? 1 : -1;
/*     */         } 
/*     */         continue;
/*     */       } 
/* 325 */       return cross;
/*     */     }
/*     */     
/*     */     int solvePoint(double[] res, double px) {
/* 329 */       double[] eqn = { -px, this.Cx, this.Bx, this.Ax };
/* 330 */       return Crossing.solveCubic(eqn, res);
/*     */     }
/*     */     
/*     */     int solveExtremX(double[] res) {
/* 334 */       double[] eqn = { this.Cx, this.Bx2, this.Ax3 };
/* 335 */       return Crossing.solveQuad(eqn, res);
/*     */     }
/*     */     
/*     */     int solveExtremY(double[] res) {
/* 339 */       double[] eqn = { this.Cy, this.By + this.By, this.Ay + this.Ay + this.Ay };
/* 340 */       return Crossing.solveQuad(eqn, res);
/*     */     }
/*     */     
/*     */     int addBound(double[] bound, int bc, double[] res, int rc, double minX, double maxX, boolean changeId, int id) {
/* 344 */       for (int i = 0; i < rc; i++) {
/* 345 */         double t = res[i];
/* 346 */         if (t > -1.0E-5D && t < 1.00001D) {
/* 347 */           double rx = t * (t * (t * this.Ax + this.Bx) + this.Cx);
/* 348 */           if (minX <= rx && rx <= maxX) {
/* 349 */             bound[bc++] = t;
/* 350 */             bound[bc++] = rx;
/* 351 */             bound[bc++] = t * (t * (t * this.Ay + this.By) + this.Cy);
/* 352 */             bound[bc++] = id;
/* 353 */             if (changeId) {
/* 354 */               id++;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 359 */       return bc;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int crossLine(double x1, double y1, double x2, double y2, double x, double y) {
/* 370 */     if ((x < x1 && x < x2) || (x > x1 && x > x2) || (y > y1 && y > y2) || x1 == x2)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 375 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 379 */     if (y >= y1 || y >= y2)
/*     */     {
/*     */       
/* 382 */       if ((y2 - y1) * (x - x1) / (x2 - x1) <= y - y1)
/*     */       {
/* 384 */         return 0;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 389 */     if (x == x1) {
/* 390 */       return (x1 < x2) ? 0 : -1;
/*     */     }
/*     */ 
/*     */     
/* 394 */     if (x == x2) {
/* 395 */       return (x1 < x2) ? 1 : 0;
/*     */     }
/*     */ 
/*     */     
/* 399 */     return (x1 < x2) ? 1 : -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int crossQuad(double x1, double y1, double cx, double cy, double x2, double y2, double x, double y) {
/* 408 */     if ((x < x1 && x < cx && x < x2) || (x > x1 && x > cx && x > x2) || (y > y1 && y > cy && y > y2) || (x1 == cx && cx == x2))
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 413 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 417 */     if (y < y1 && y < cy && y < y2 && x != x1 && x != x2) {
/* 418 */       if (x1 < x2) {
/* 419 */         return (x1 < x && x < x2) ? 1 : 0;
/*     */       }
/* 421 */       return (x2 < x && x < x1) ? -1 : 0;
/*     */     } 
/*     */ 
/*     */     
/* 425 */     QuadCurve c = new QuadCurve(x1, y1, cx, cy, x2, y2);
/* 426 */     double px = x - x1;
/* 427 */     double py = y - y1;
/* 428 */     double[] res = new double[3];
/* 429 */     int rc = c.solvePoint(res, px);
/*     */     
/* 431 */     return c.cross(res, rc, py, py);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int crossCubic(double x1, double y1, double cx1, double cy1, double cx2, double cy2, double x2, double y2, double x, double y) {
/* 440 */     if ((x < x1 && x < cx1 && x < cx2 && x < x2) || (x > x1 && x > cx1 && x > cx2 && x > x2) || (y > y1 && y > cy1 && y > cy2 && y > y2) || (x1 == cx1 && cx1 == cx2 && cx2 == x2))
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 445 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 449 */     if (y < y1 && y < cy1 && y < cy2 && y < y2 && x != x1 && x != x2) {
/* 450 */       if (x1 < x2) {
/* 451 */         return (x1 < x && x < x2) ? 1 : 0;
/*     */       }
/* 453 */       return (x2 < x && x < x1) ? -1 : 0;
/*     */     } 
/*     */ 
/*     */     
/* 457 */     CubicCurve c = new CubicCurve(x1, y1, cx1, cy1, cx2, cy2, x2, y2);
/* 458 */     double px = x - x1;
/* 459 */     double py = y - y1;
/* 460 */     double[] res = new double[3];
/* 461 */     int rc = c.solvePoint(res, px);
/* 462 */     return c.cross(res, rc, py, py);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int crossPath(PathIterator p, double x, double y) {
/* 469 */     int cross = 0;
/*     */     
/* 471 */     double cy = 0.0D, cx = cy, my = cx, mx = my;
/* 472 */     double[] coords = new double[6];
/*     */     
/* 474 */     while (!p.isDone()) {
/* 475 */       switch (p.currentSegment(coords)) {
/*     */         case 0:
/* 477 */           if (cx != mx || cy != my) {
/* 478 */             cross += crossLine(cx, cy, mx, my, x, y);
/*     */           }
/* 480 */           mx = cx = coords[0];
/* 481 */           my = cy = coords[1];
/*     */         
/*     */         case 1:
/* 484 */           cross += crossLine(cx, cy, cx = coords[0], cy = coords[1], x, y);
/*     */         
/*     */         case 2:
/* 487 */           cross += crossQuad(cx, cy, coords[0], coords[1], cx = coords[2], cy = coords[3], x, y);
/*     */         
/*     */         case 3:
/* 490 */           cross += crossCubic(cx, cy, coords[0], coords[1], coords[2], coords[3], cx = coords[4], cy = coords[5], x, y);
/*     */         
/*     */         case 4:
/* 493 */           if (cy != my || cx != mx) {
/* 494 */             cross += crossLine(cx, cy, cx = mx, cy = my, x, y);
/*     */           }
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 500 */       if (x == cx && y == cy) {
/* 501 */         cross = 0;
/* 502 */         cy = my;
/*     */         break;
/*     */       } 
/* 505 */       p.next();
/*     */     } 
/* 507 */     if (cy != my) {
/* 508 */       cross += crossLine(cx, cy, mx, my, x, y);
/*     */     }
/* 510 */     return cross;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int crossShape(Shape s, double x, double y) {
/* 517 */     if (!s.getBounds2D().contains(x, y)) {
/* 518 */       return 0;
/*     */     }
/* 520 */     return crossPath(s.getPathIterator(null), x, y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isZero(double val) {
/* 527 */     return (-1.0E-5D < val && val < 1.0E-5D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void sortBound(double[] bound, int bc) {
/* 534 */     for (int i = 0; i < bc - 4; i += 4) {
/* 535 */       int k = i;
/* 536 */       for (int j = i + 4; j < bc; j += 4) {
/* 537 */         if (bound[k] > bound[j]) {
/* 538 */           k = j;
/*     */         }
/*     */       } 
/* 541 */       if (k != i) {
/* 542 */         double tmp = bound[i];
/* 543 */         bound[i] = bound[k];
/* 544 */         bound[k] = tmp;
/* 545 */         tmp = bound[i + 1];
/* 546 */         bound[i + 1] = bound[k + 1];
/* 547 */         bound[k + 1] = tmp;
/* 548 */         tmp = bound[i + 2];
/* 549 */         bound[i + 2] = bound[k + 2];
/* 550 */         bound[k + 2] = tmp;
/* 551 */         tmp = bound[i + 3];
/* 552 */         bound[i + 3] = bound[k + 3];
/* 553 */         bound[k + 3] = tmp;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int crossBound(double[] bound, int bc, double py1, double py2) {
/* 564 */     if (bc == 0) {
/* 565 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 569 */     int up = 0;
/* 570 */     int down = 0;
/* 571 */     for (int i = 2; i < bc; i += 4) {
/* 572 */       if (bound[i] < py1) {
/* 573 */         up++;
/*     */       
/*     */       }
/* 576 */       else if (bound[i] > py2) {
/* 577 */         down++;
/*     */       } else {
/*     */         
/* 580 */         return 255;
/*     */       } 
/*     */     } 
/*     */     
/* 584 */     if (down == 0) {
/* 585 */       return 0;
/*     */     }
/*     */     
/* 588 */     if (up != 0) {
/*     */       
/* 590 */       sortBound(bound, bc);
/* 591 */       boolean sign = (bound[2] > py2);
/* 592 */       for (int j = 6; j < bc; j += 4) {
/* 593 */         boolean sign2 = (bound[j] > py2);
/* 594 */         if (sign != sign2 && bound[j + 1] != bound[j - 3]) {
/* 595 */           return 255;
/*     */         }
/* 597 */         sign = sign2;
/*     */       } 
/*     */     } 
/* 600 */     return 254;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int intersectLine(double x1, double y1, double x2, double y2, double rx1, double ry1, double rx2, double ry2) {
/* 609 */     if ((rx2 < x1 && rx2 < x2) || (rx1 > x1 && rx1 > x2) || (ry1 > y1 && ry1 > y2))
/*     */     {
/*     */ 
/*     */       
/* 613 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 617 */     if (ry2 >= y1 || ry2 >= y2) {
/*     */       double bx1, bx2;
/*     */ 
/*     */       
/* 621 */       if (x1 == x2) {
/* 622 */         return 255;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 627 */       if (x1 < x2) {
/* 628 */         bx1 = (x1 < rx1) ? rx1 : x1;
/* 629 */         bx2 = (x2 < rx2) ? x2 : rx2;
/*     */       } else {
/* 631 */         bx1 = (x2 < rx1) ? rx1 : x2;
/* 632 */         bx2 = (x1 < rx2) ? x1 : rx2;
/*     */       } 
/* 634 */       double k = (y2 - y1) / (x2 - x1);
/* 635 */       double by1 = k * (bx1 - x1) + y1;
/* 636 */       double by2 = k * (bx2 - x1) + y1;
/*     */ 
/*     */       
/* 639 */       if (by1 < ry1 && by2 < ry1) {
/* 640 */         return 0;
/*     */       }
/*     */ 
/*     */       
/* 644 */       if (by1 <= ry2 || by2 <= ry2)
/*     */       {
/* 646 */         return 255;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 651 */     if (x1 == x2) {
/* 652 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 656 */     if (rx1 == x1) {
/* 657 */       return (x1 < x2) ? 0 : -1;
/*     */     }
/*     */ 
/*     */     
/* 661 */     if (rx1 == x2) {
/* 662 */       return (x1 < x2) ? 1 : 0;
/*     */     }
/*     */     
/* 665 */     if (x1 < x2) {
/* 666 */       return (x1 < rx1 && rx1 < x2) ? 1 : 0;
/*     */     }
/* 668 */     return (x2 < rx1 && rx1 < x1) ? -1 : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int intersectQuad(double x1, double y1, double cx, double cy, double x2, double y2, double rx1, double ry1, double rx2, double ry2) {
/* 678 */     if ((rx2 < x1 && rx2 < cx && rx2 < x2) || (rx1 > x1 && rx1 > cx && rx1 > x2) || (ry1 > y1 && ry1 > cy && ry1 > y2))
/*     */     {
/*     */ 
/*     */       
/* 682 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 686 */     if (ry2 < y1 && ry2 < cy && ry2 < y2 && rx1 != x1 && rx1 != x2) {
/* 687 */       if (x1 < x2) {
/* 688 */         return (x1 < rx1 && rx1 < x2) ? 1 : 0;
/*     */       }
/* 690 */       return (x2 < rx1 && rx1 < x1) ? -1 : 0;
/*     */     } 
/*     */ 
/*     */     
/* 694 */     QuadCurve c = new QuadCurve(x1, y1, cx, cy, x2, y2);
/* 695 */     double px1 = rx1 - x1;
/* 696 */     double py1 = ry1 - y1;
/* 697 */     double px2 = rx2 - x1;
/* 698 */     double py2 = ry2 - y1;
/*     */     
/* 700 */     double[] res1 = new double[3];
/* 701 */     double[] res2 = new double[3];
/* 702 */     int rc1 = c.solvePoint(res1, px1);
/* 703 */     int rc2 = c.solvePoint(res2, px2);
/*     */ 
/*     */     
/* 706 */     if (rc1 == 0 && rc2 == 0) {
/* 707 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 711 */     double minX = px1 - 1.0E-5D;
/* 712 */     double maxX = px2 + 1.0E-5D;
/* 713 */     double[] bound = new double[28];
/* 714 */     int bc = 0;
/*     */     
/* 716 */     bc = c.addBound(bound, bc, res1, rc1, minX, maxX, false, 0);
/* 717 */     bc = c.addBound(bound, bc, res2, rc2, minX, maxX, false, 1);
/*     */     
/* 719 */     rc2 = c.solveExtrem(res2);
/* 720 */     bc = c.addBound(bound, bc, res2, rc2, minX, maxX, true, 2);
/*     */     
/* 722 */     if (rx1 < x1 && x1 < rx2) {
/* 723 */       bound[bc++] = 0.0D;
/* 724 */       bound[bc++] = 0.0D;
/* 725 */       bound[bc++] = 0.0D;
/* 726 */       bound[bc++] = 4.0D;
/*     */     } 
/* 728 */     if (rx1 < x2 && x2 < rx2) {
/* 729 */       bound[bc++] = 1.0D;
/* 730 */       bound[bc++] = c.ax;
/* 731 */       bound[bc++] = c.ay;
/* 732 */       bound[bc++] = 5.0D;
/*     */     } 
/*     */ 
/*     */     
/* 736 */     int cross = crossBound(bound, bc, py1, py2);
/* 737 */     if (cross != 254) {
/* 738 */       return cross;
/*     */     }
/* 740 */     return c.cross(res1, rc1, py1, py2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int intersectCubic(double x1, double y1, double cx1, double cy1, double cx2, double cy2, double x2, double y2, double rx1, double ry1, double rx2, double ry2) {
/* 749 */     if ((rx2 < x1 && rx2 < cx1 && rx2 < cx2 && rx2 < x2) || (rx1 > x1 && rx1 > cx1 && rx1 > cx2 && rx1 > x2) || (ry1 > y1 && ry1 > cy1 && ry1 > cy2 && ry1 > y2))
/*     */     {
/*     */ 
/*     */       
/* 753 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 757 */     if (ry2 < y1 && ry2 < cy1 && ry2 < cy2 && ry2 < y2 && rx1 != x1 && rx1 != x2) {
/* 758 */       if (x1 < x2) {
/* 759 */         return (x1 < rx1 && rx1 < x2) ? 1 : 0;
/*     */       }
/* 761 */       return (x2 < rx1 && rx1 < x1) ? -1 : 0;
/*     */     } 
/*     */ 
/*     */     
/* 765 */     CubicCurve c = new CubicCurve(x1, y1, cx1, cy1, cx2, cy2, x2, y2);
/* 766 */     double px1 = rx1 - x1;
/* 767 */     double py1 = ry1 - y1;
/* 768 */     double px2 = rx2 - x1;
/* 769 */     double py2 = ry2 - y1;
/*     */     
/* 771 */     double[] res1 = new double[3];
/* 772 */     double[] res2 = new double[3];
/* 773 */     int rc1 = c.solvePoint(res1, px1);
/* 774 */     int rc2 = c.solvePoint(res2, px2);
/*     */ 
/*     */     
/* 777 */     if (rc1 == 0 && rc2 == 0) {
/* 778 */       return 0;
/*     */     }
/*     */     
/* 781 */     double minX = px1 - 1.0E-5D;
/* 782 */     double maxX = px2 + 1.0E-5D;
/*     */ 
/*     */     
/* 785 */     double[] bound = new double[40];
/* 786 */     int bc = 0;
/*     */     
/* 788 */     bc = c.addBound(bound, bc, res1, rc1, minX, maxX, false, 0);
/* 789 */     bc = c.addBound(bound, bc, res2, rc2, minX, maxX, false, 1);
/*     */     
/* 791 */     rc2 = c.solveExtremX(res2);
/* 792 */     bc = c.addBound(bound, bc, res2, rc2, minX, maxX, true, 2);
/* 793 */     rc2 = c.solveExtremY(res2);
/* 794 */     bc = c.addBound(bound, bc, res2, rc2, minX, maxX, true, 4);
/*     */     
/* 796 */     if (rx1 < x1 && x1 < rx2) {
/* 797 */       bound[bc++] = 0.0D;
/* 798 */       bound[bc++] = 0.0D;
/* 799 */       bound[bc++] = 0.0D;
/* 800 */       bound[bc++] = 6.0D;
/*     */     } 
/* 802 */     if (rx1 < x2 && x2 < rx2) {
/* 803 */       bound[bc++] = 1.0D;
/* 804 */       bound[bc++] = c.ax;
/* 805 */       bound[bc++] = c.ay;
/* 806 */       bound[bc++] = 7.0D;
/*     */     } 
/*     */ 
/*     */     
/* 810 */     int cross = crossBound(bound, bc, py1, py2);
/* 811 */     if (cross != 254) {
/* 812 */       return cross;
/*     */     }
/* 814 */     return c.cross(res1, rc1, py1, py2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int intersectPath(PathIterator p, double x, double y, double w, double h) {
/* 822 */     int cross = 0;
/*     */ 
/*     */     
/* 825 */     double cy = 0.0D, cx = cy, my = cx, mx = my;
/* 826 */     double[] coords = new double[6];
/*     */     
/* 828 */     double rx1 = x;
/* 829 */     double ry1 = y;
/* 830 */     double rx2 = x + w;
/* 831 */     double ry2 = y + h;
/*     */     
/* 833 */     while (!p.isDone()) {
/* 834 */       int count = 0;
/* 835 */       switch (p.currentSegment(coords)) {
/*     */         case 0:
/* 837 */           if (cx != mx || cy != my) {
/* 838 */             count = intersectLine(cx, cy, mx, my, rx1, ry1, rx2, ry2);
/*     */           }
/* 840 */           mx = cx = coords[0];
/* 841 */           my = cy = coords[1];
/*     */         
/*     */         case 1:
/* 844 */           count = intersectLine(cx, cy, cx = coords[0], cy = coords[1], rx1, ry1, rx2, ry2);
/*     */         
/*     */         case 2:
/* 847 */           count = intersectQuad(cx, cy, coords[0], coords[1], cx = coords[2], cy = coords[3], rx1, ry1, rx2, ry2);
/*     */         
/*     */         case 3:
/* 850 */           count = intersectCubic(cx, cy, coords[0], coords[1], coords[2], coords[3], cx = coords[4], cy = coords[5], rx1, ry1, rx2, ry2);
/*     */         
/*     */         case 4:
/* 853 */           if (cy != my || cx != mx) {
/* 854 */             count = intersectLine(cx, cy, mx, my, rx1, ry1, rx2, ry2);
/*     */           }
/* 856 */           cx = mx;
/* 857 */           cy = my;
/*     */           break;
/*     */       } 
/* 860 */       if (count == 255) {
/* 861 */         return 255;
/*     */       }
/* 863 */       cross += count;
/* 864 */       p.next();
/*     */     } 
/* 866 */     if (cy != my) {
/* 867 */       int count = intersectLine(cx, cy, mx, my, rx1, ry1, rx2, ry2);
/* 868 */       if (count == 255) {
/* 869 */         return 255;
/*     */       }
/* 871 */       cross += count;
/*     */     } 
/* 873 */     return cross;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int intersectShape(Shape s, double x, double y, double w, double h) {
/* 880 */     if (!s.getBounds2D().intersects(x, y, w, h)) {
/* 881 */       return 0;
/*     */     }
/* 883 */     return intersectPath(s.getPathIterator(null), x, y, w, h);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isInsideNonZero(int cross) {
/* 890 */     return (cross != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isInsideEvenOdd(int cross) {
/* 897 */     return ((cross & 0x1) != 0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/geom/gl/Crossing.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */