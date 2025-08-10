/*     */ package com.itextpdf.text.pdf.parser.clipper;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   extends ArrayList<Point.LongPoint>
/*     */ {
/*     */   private static final long serialVersionUID = -7120161578077546673L;
/*     */   
/*     */   static class Join
/*     */   {
/*     */     Path.OutPt outPt1;
/*     */     Path.OutPt outPt2;
/*     */     private Point.LongPoint offPt;
/*     */     
/*     */     public Point.LongPoint getOffPt() {
/*  96 */       return this.offPt;
/*     */     }
/*     */     
/*     */     public void setOffPt(Point.LongPoint offPt) {
/* 100 */       this.offPt = offPt;
/*     */     } }
/*     */   
/*     */   static class OutPt {
/*     */     int idx;
/*     */     protected Point.LongPoint pt;
/*     */     
/*     */     public static Path.OutRec getLowerMostRec(Path.OutRec outRec1, Path.OutRec outRec2) {
/* 108 */       if (outRec1.bottomPt == null) {
/* 109 */         outRec1.bottomPt = outRec1.pts.getBottomPt();
/*     */       }
/* 111 */       if (outRec2.bottomPt == null) {
/* 112 */         outRec2.bottomPt = outRec2.pts.getBottomPt();
/*     */       }
/* 114 */       OutPt bPt1 = outRec1.bottomPt;
/* 115 */       OutPt bPt2 = outRec2.bottomPt;
/* 116 */       if (bPt1.getPt().getY() > bPt2.getPt().getY()) {
/* 117 */         return outRec1;
/*     */       }
/* 119 */       if (bPt1.getPt().getY() < bPt2.getPt().getY()) {
/* 120 */         return outRec2;
/*     */       }
/* 122 */       if (bPt1.getPt().getX() < bPt2.getPt().getX()) {
/* 123 */         return outRec1;
/*     */       }
/* 125 */       if (bPt1.getPt().getX() > bPt2.getPt().getX()) {
/* 126 */         return outRec2;
/*     */       }
/* 128 */       if (bPt1.next == bPt1) {
/* 129 */         return outRec2;
/*     */       }
/* 131 */       if (bPt2.next == bPt2) {
/* 132 */         return outRec1;
/*     */       }
/* 134 */       if (isFirstBottomPt(bPt1, bPt2)) {
/* 135 */         return outRec1;
/*     */       }
/*     */       
/* 138 */       return outRec2;
/*     */     }
/*     */     OutPt next; OutPt prev;
/*     */     
/*     */     private static boolean isFirstBottomPt(OutPt btmPt1, OutPt btmPt2) {
/* 143 */       OutPt p = btmPt1.prev;
/* 144 */       while (p.getPt().equals(btmPt1.getPt()) && !p.equals(btmPt1)) {
/* 145 */         p = p.prev;
/*     */       }
/* 147 */       double dx1p = Math.abs(Point.LongPoint.getDeltaX(btmPt1.getPt(), p.getPt()));
/* 148 */       p = btmPt1.next;
/* 149 */       while (p.getPt().equals(btmPt1.getPt()) && !p.equals(btmPt1)) {
/* 150 */         p = p.next;
/*     */       }
/* 152 */       double dx1n = Math.abs(Point.LongPoint.getDeltaX(btmPt1.getPt(), p.getPt()));
/*     */       
/* 154 */       p = btmPt2.prev;
/* 155 */       while (p.getPt().equals(btmPt2.getPt()) && !p.equals(btmPt2)) {
/* 156 */         p = p.prev;
/*     */       }
/* 158 */       double dx2p = Math.abs(Point.LongPoint.getDeltaX(btmPt2.getPt(), p.getPt()));
/* 159 */       p = btmPt2.next;
/* 160 */       while (p.getPt().equals(btmPt2.getPt()) && p.equals(btmPt2)) {
/* 161 */         p = p.next;
/*     */       }
/* 163 */       double dx2n = Math.abs(Point.LongPoint.getDeltaX(btmPt2.getPt(), p.getPt()));
/* 164 */       return ((dx1p >= dx2p && dx1p >= dx2n) || (dx1n >= dx2p && dx1n >= dx2n));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutPt duplicate(boolean InsertAfter) {
/* 174 */       OutPt result = new OutPt();
/* 175 */       result.setPt(new Point.LongPoint(getPt()));
/* 176 */       result.idx = this.idx;
/* 177 */       if (InsertAfter) {
/* 178 */         result.next = this.next;
/* 179 */         result.prev = this;
/* 180 */         this.next.prev = result;
/* 181 */         this.next = result;
/*     */       } else {
/*     */         
/* 184 */         result.prev = this.prev;
/* 185 */         result.next = this;
/* 186 */         this.prev.next = result;
/* 187 */         this.prev = result;
/*     */       } 
/* 189 */       return result;
/*     */     }
/*     */     
/*     */     OutPt getBottomPt() {
/* 193 */       OutPt dups = null;
/* 194 */       OutPt p = this.next;
/* 195 */       OutPt pp = this;
/* 196 */       while (p != pp) {
/* 197 */         if (p.getPt().getY() > pp.getPt().getY()) {
/* 198 */           pp = p;
/* 199 */           dups = null;
/*     */         }
/* 201 */         else if (p.getPt().getY() == pp.getPt().getY() && p.getPt().getX() <= pp.getPt().getX()) {
/* 202 */           if (p.getPt().getX() < pp.getPt().getX()) {
/* 203 */             dups = null;
/* 204 */             pp = p;
/*     */           
/*     */           }
/* 207 */           else if (p.next != pp && p.prev != pp) {
/* 208 */             dups = p;
/*     */           } 
/*     */         } 
/*     */         
/* 212 */         p = p.next;
/*     */       } 
/* 214 */       if (dups != null)
/*     */       {
/* 216 */         while (dups != p) {
/* 217 */           if (!isFirstBottomPt(p, dups)) {
/* 218 */             pp = dups;
/*     */           }
/* 220 */           dups = dups.next;
/* 221 */           while (!dups.getPt().equals(pp.getPt())) {
/* 222 */             dups = dups.next;
/*     */           }
/*     */         } 
/*     */       }
/* 226 */       return pp;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getPointCount() {
/* 231 */       int result = 0;
/* 232 */       OutPt p = this;
/*     */       do {
/* 234 */         result++;
/* 235 */         p = p.next;
/*     */       }
/* 237 */       while (p != this && p != null);
/* 238 */       return result;
/*     */     }
/*     */     
/*     */     public Point.LongPoint getPt() {
/* 242 */       return this.pt;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void reversePolyPtLinks() {
/* 249 */       OutPt pp1 = this;
/*     */       do {
/* 251 */         OutPt pp2 = pp1.next;
/* 252 */         pp1.next = pp1.prev;
/* 253 */         pp1.prev = pp2;
/* 254 */         pp1 = pp2;
/*     */       }
/* 256 */       while (pp1 != this);
/*     */     }
/*     */     
/*     */     public void setPt(Point.LongPoint pt) {
/* 260 */       this.pt = pt;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected static class Maxima
/*     */   {
/*     */     protected long X;
/*     */     protected Maxima Next;
/*     */     protected Maxima Prev;
/*     */   }
/*     */   
/*     */   static class OutRec
/*     */   {
/*     */     int Idx;
/*     */     boolean isHole;
/*     */     boolean isOpen;
/*     */     OutRec firstLeft;
/*     */     protected Path.OutPt pts;
/*     */     Path.OutPt bottomPt;
/*     */     PolyNode polyNode;
/*     */     
/*     */     public double area() {
/* 283 */       Path.OutPt op = this.pts;
/* 284 */       if (op == null) {
/* 285 */         return 0.0D;
/*     */       }
/* 287 */       double a = 0.0D;
/*     */       while (true) {
/* 289 */         a += (op.prev.getPt().getX() + op.getPt().getX()) * (op.prev.getPt().getY() - op.getPt().getY());
/* 290 */         op = op.next;
/*     */         
/* 292 */         if (op == this.pts) {
/* 293 */           return a * 0.5D;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public void fixHoleLinkage() {
/* 299 */       if (this.firstLeft == null || (this.isHole != this.firstLeft.isHole && this.firstLeft.pts != null)) {
/*     */         return;
/*     */       }
/*     */       
/* 303 */       OutRec orfl = this.firstLeft;
/* 304 */       while (orfl != null && (orfl.isHole == this.isHole || orfl.pts == null)) {
/* 305 */         orfl = orfl.firstLeft;
/*     */       }
/* 307 */       this.firstLeft = orfl;
/*     */     }
/*     */     
/*     */     public Path.OutPt getPoints() {
/* 311 */       return this.pts;
/*     */     }
/*     */     
/*     */     public void setPoints(Path.OutPt pts) {
/* 315 */       this.pts = pts;
/*     */     }
/*     */   }
/*     */   
/*     */   private static OutPt excludeOp(OutPt op) {
/* 320 */     OutPt result = op.prev;
/* 321 */     result.next = op.next;
/* 322 */     op.next.prev = result;
/* 323 */     result.idx = 0;
/* 324 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path(Point.LongPoint[] points) {
/* 337 */     this();
/* 338 */     for (Point.LongPoint point : points) {
/* 339 */       add(point);
/*     */     }
/*     */   }
/*     */   
/*     */   public Path(int cnt) {
/* 344 */     super(cnt);
/*     */   }
/*     */   
/*     */   public Path(Collection<? extends Point.LongPoint> c) {
/* 348 */     super(c);
/*     */   }
/*     */   
/*     */   public double area() {
/* 352 */     int cnt = size();
/* 353 */     if (cnt < 3) {
/* 354 */       return 0.0D;
/*     */     }
/* 356 */     double a = 0.0D;
/* 357 */     for (int i = 0, j = cnt - 1; i < cnt; i++) {
/* 358 */       a += (get(j).getX() + get(i).getX()) * (get(j).getY() - get(i).getY());
/* 359 */       j = i;
/*     */     } 
/* 361 */     return -a * 0.5D;
/*     */   }
/*     */   
/*     */   public Path cleanPolygon() {
/* 365 */     return cleanPolygon(1.415D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path cleanPolygon(double distance) {
/* 373 */     int cnt = size();
/*     */     
/* 375 */     if (cnt == 0) {
/* 376 */       return new Path();
/*     */     }
/*     */     
/* 379 */     OutPt[] outPts = new OutPt[cnt]; int i;
/* 380 */     for (i = 0; i < cnt; i++) {
/* 381 */       outPts[i] = new OutPt();
/*     */     }
/*     */     
/* 384 */     for (i = 0; i < cnt; i++) {
/* 385 */       (outPts[i]).pt = get(i);
/* 386 */       (outPts[i]).next = outPts[(i + 1) % cnt];
/* 387 */       (outPts[i]).next.prev = outPts[i];
/* 388 */       (outPts[i]).idx = 0;
/*     */     } 
/*     */     
/* 391 */     double distSqrd = distance * distance;
/* 392 */     OutPt op = outPts[0];
/* 393 */     while (op.idx == 0 && op.next != op.prev) {
/* 394 */       if (Point.arePointsClose(op.pt, op.prev.pt, distSqrd)) {
/* 395 */         op = excludeOp(op);
/* 396 */         cnt--; continue;
/*     */       } 
/* 398 */       if (Point.arePointsClose(op.prev.pt, op.next.pt, distSqrd)) {
/* 399 */         excludeOp(op.next);
/* 400 */         op = excludeOp(op);
/* 401 */         cnt -= 2; continue;
/*     */       } 
/* 403 */       if (Point.slopesNearCollinear(op.prev.pt, op.pt, op.next.pt, distSqrd)) {
/* 404 */         op = excludeOp(op);
/* 405 */         cnt--;
/*     */         continue;
/*     */       } 
/* 408 */       op.idx = 1;
/* 409 */       op = op.next;
/*     */     } 
/*     */ 
/*     */     
/* 413 */     if (cnt < 3) {
/* 414 */       cnt = 0;
/*     */     }
/* 416 */     Path result = new Path(cnt);
/* 417 */     for (int j = 0; j < cnt; j++) {
/* 418 */       result.add(op.pt);
/* 419 */       op = op.next;
/*     */     } 
/* 421 */     outPts = null;
/* 422 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int isPointInPolygon(Point.LongPoint pt) {
/* 429 */     int result = 0;
/* 430 */     int cnt = size();
/* 431 */     if (cnt < 3) {
/* 432 */       return 0;
/*     */     }
/* 434 */     Point.LongPoint ip = get(0);
/* 435 */     for (int i = 1; i <= cnt; i++) {
/* 436 */       Point.LongPoint ipNext = (i == cnt) ? get(0) : get(i);
/* 437 */       if (ipNext.getY() == pt.getY()) {
/* 438 */         if (ipNext.getX() != pt.getX()) { if (ip.getY() == pt.getY()) if (((ipNext.getX() > pt.getX()) ? true : false) == ((ip.getX() < pt.getX()) ? true : false))
/* 439 */               return -1;   } else { return -1; }
/*     */       
/*     */       }
/* 442 */       if (((ip.getY() < pt.getY()) ? true : false) != ((ipNext.getY() < pt.getY()) ? true : false)) {
/* 443 */         if (ip.getX() >= pt.getX()) {
/* 444 */           if (ipNext.getX() > pt.getX()) {
/* 445 */             result = 1 - result;
/*     */           }
/*     */           else {
/*     */             
/* 449 */             double d = (ip.getX() - pt.getX()) * (ipNext.getY() - pt.getY()) - (ipNext.getX() - pt.getX()) * (ip.getY() - pt.getY());
/* 450 */             if (d == 0.0D) {
/* 451 */               return -1;
/*     */             }
/* 453 */             if (((d > 0.0D) ? true : false) == ((ipNext.getY() > ip.getY()) ? true : false)) {
/* 454 */               result = 1 - result;
/*     */             }
/*     */           }
/*     */         
/*     */         }
/* 459 */         else if (ipNext.getX() > pt.getX()) {
/*     */           
/* 461 */           double d = (ip.getX() - pt.getX()) * (ipNext.getY() - pt.getY()) - (ipNext.getX() - pt.getX()) * (ip.getY() - pt.getY());
/* 462 */           if (d == 0.0D) {
/* 463 */             return -1;
/*     */           }
/* 465 */           if (((d > 0.0D) ? true : false) == ((ipNext.getY() > ip.getY()) ? true : false)) {
/* 466 */             result = 1 - result;
/*     */           }
/*     */         } 
/*     */       }
/*     */       
/* 471 */       ip = ipNext;
/*     */     } 
/* 473 */     return result;
/*     */   }
/*     */   
/*     */   public boolean orientation() {
/* 477 */     return (area() >= 0.0D);
/*     */   }
/*     */   
/*     */   public void reverse() {
/* 481 */     Collections.reverse(this);
/*     */   }
/*     */   
/*     */   public Path TranslatePath(Point.LongPoint delta) {
/* 485 */     Path outPath = new Path(size());
/* 486 */     for (int i = 0; i < size(); i++) {
/* 487 */       outPath.add(new Point.LongPoint(get(i).getX() + delta.getX(), get(i).getY() + delta.getY()));
/*     */     }
/* 489 */     return outPath;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/clipper/Path.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */