/*     */ package com.itextpdf.text.pdf.parser.clipper;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Edge
/*     */ {
/*     */   private final Point.LongPoint bot;
/*     */   private final Point.LongPoint current;
/*     */   private final Point.LongPoint top;
/*     */   private final Point.LongPoint delta;
/*     */   double deltaX;
/*     */   Clipper.PolyType polyTyp;
/*     */   Side side;
/*     */   int windDelta;
/*     */   int windCnt;
/*     */   int windCnt2;
/*     */   int outIdx;
/*     */   Edge next;
/*     */   Edge prev;
/*     */   Edge nextInLML;
/*     */   Edge nextInAEL;
/*     */   Edge prevInAEL;
/*     */   Edge nextInSEL;
/*     */   Edge prevInSEL;
/*     */   protected static final int SKIP = -2;
/*     */   protected static final int UNASSIGNED = -1;
/*     */   protected static final double HORIZONTAL = -3.4E38D;
/*     */   
/*     */   enum Side
/*     */   {
/*  88 */     LEFT, RIGHT;
/*     */   }
/*     */   
/*     */   static boolean doesE2InsertBeforeE1(Edge e1, Edge e2) {
/*  92 */     if (e2.current.getX() == e1.current.getX()) {
/*  93 */       if (e2.top.getY() > e1.top.getY()) {
/*  94 */         return (e2.top.getX() < topX(e1, e2.top.getY()));
/*     */       }
/*     */       
/*  97 */       return (e1.top.getX() > topX(e2, e1.top.getY()));
/*     */     } 
/*     */ 
/*     */     
/* 101 */     return (e2.current.getX() < e1.current.getX());
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean slopesEqual(Edge e1, Edge e2, boolean useFullRange) {
/* 106 */     if (useFullRange) {
/* 107 */       return BigInteger.valueOf(e1.getDelta().getY()).multiply(BigInteger.valueOf(e2.getDelta().getX())).equals(
/* 108 */           BigInteger.valueOf(e1.getDelta().getX()).multiply(BigInteger.valueOf(e2.getDelta().getY())));
/*     */     }
/* 110 */     return (e1.getDelta().getY() * e2.getDelta().getX() == e1.getDelta().getX() * e2.getDelta().getY());
/*     */   }
/*     */ 
/*     */   
/*     */   static void swapPolyIndexes(Edge edge1, Edge edge2) {
/* 115 */     int outIdx = edge1.outIdx;
/* 116 */     edge1.outIdx = edge2.outIdx;
/* 117 */     edge2.outIdx = outIdx;
/*     */   }
/*     */   
/*     */   static void swapSides(Edge edge1, Edge edge2) {
/* 121 */     Side side = edge1.side;
/* 122 */     edge1.side = edge2.side;
/* 123 */     edge2.side = side;
/*     */   }
/*     */   
/*     */   static long topX(Edge edge, long currentY) {
/* 127 */     if (currentY == edge.getTop().getY()) {
/* 128 */       return edge.getTop().getX();
/*     */     }
/* 130 */     return edge.getBot().getX() + Math.round(edge.deltaX * (currentY - edge.getBot().getY()));
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
/*     */   
/* 165 */   private static final Logger LOGGER = Logger.getLogger(Edge.class.getName());
/*     */   
/*     */   public Edge() {
/* 168 */     this.delta = new Point.LongPoint();
/* 169 */     this.top = new Point.LongPoint();
/* 170 */     this.bot = new Point.LongPoint();
/* 171 */     this.current = new Point.LongPoint();
/*     */   }
/*     */   
/*     */   public Edge findNextLocMin() {
/* 175 */     Edge e = this;
/*     */     
/*     */     while (true) {
/* 178 */       if (!e.bot.equals(e.prev.bot) || e.current.equals(e.top)) {
/* 179 */         e = e.next; continue;
/*     */       } 
/* 181 */       if (e.deltaX != -3.4E38D && e.prev.deltaX != -3.4E38D) {
/*     */         break;
/*     */       }
/* 184 */       while (e.prev.deltaX == -3.4E38D) {
/* 185 */         e = e.prev;
/*     */       }
/* 187 */       Edge e2 = e;
/* 188 */       while (e.deltaX == -3.4E38D) {
/* 189 */         e = e.next;
/*     */       }
/* 191 */       if (e.top.getY() == e.prev.bot.getY()) {
/*     */         continue;
/*     */       }
/* 194 */       if (e2.prev.bot.getX() < e.bot.getX()) {
/* 195 */         e = e2;
/*     */       }
/*     */       break;
/*     */     } 
/* 199 */     return e;
/*     */   }
/*     */   
/*     */   public Point.LongPoint getBot() {
/* 203 */     return this.bot;
/*     */   }
/*     */   
/*     */   public Point.LongPoint getCurrent() {
/* 207 */     return this.current;
/*     */   }
/*     */   
/*     */   public Point.LongPoint getDelta() {
/* 211 */     return this.delta;
/*     */   }
/*     */   
/*     */   public Edge getMaximaPair() {
/* 215 */     Edge result = null;
/* 216 */     if (this.next.top.equals(this.top) && this.next.nextInLML == null) {
/* 217 */       result = this.next;
/*     */     }
/* 219 */     else if (this.prev.top.equals(this.top) && this.prev.nextInLML == null) {
/* 220 */       result = this.prev;
/*     */     } 
/* 222 */     if (result != null && (result.outIdx == -2 || (result.nextInAEL == result.prevInAEL && !result.isHorizontal()))) {
/* 223 */       return null;
/*     */     }
/* 225 */     return result;
/*     */   }
/*     */   
/*     */   public Edge getNextInAEL(Clipper.Direction direction) {
/* 229 */     return (direction == Clipper.Direction.LEFT_TO_RIGHT) ? this.nextInAEL : this.prevInAEL;
/*     */   }
/*     */   
/*     */   public Point.LongPoint getTop() {
/* 233 */     return this.top;
/*     */   }
/*     */   public boolean isContributing(Clipper.PolyFillType clipFillType, Clipper.PolyFillType subjFillType, Clipper.ClipType clipType) {
/*     */     Clipper.PolyFillType pft, pft2;
/* 237 */     LOGGER.entering(Edge.class.getName(), "isContributing");
/*     */ 
/*     */     
/* 240 */     if (this.polyTyp == Clipper.PolyType.SUBJECT) {
/* 241 */       pft = subjFillType;
/* 242 */       pft2 = clipFillType;
/*     */     } else {
/*     */       
/* 245 */       pft = clipFillType;
/* 246 */       pft2 = subjFillType;
/*     */     } 
/*     */     
/* 249 */     switch (pft) {
/*     */       
/*     */       case INTERSECTION:
/* 252 */         if (this.windDelta == 0 && this.windCnt != 1) {
/* 253 */           return false;
/*     */         }
/*     */         break;
/*     */       case UNION:
/* 257 */         if (Math.abs(this.windCnt) != 1) {
/* 258 */           return false;
/*     */         }
/*     */         break;
/*     */       case DIFFERENCE:
/* 262 */         if (this.windCnt != 1) {
/* 263 */           return false;
/*     */         }
/*     */         break;
/*     */       default:
/* 267 */         if (this.windCnt != -1) {
/* 268 */           return false;
/*     */         }
/*     */         break;
/*     */     } 
/*     */     
/* 273 */     switch (clipType) {
/*     */       case INTERSECTION:
/* 275 */         switch (pft2) {
/*     */           case INTERSECTION:
/*     */           case UNION:
/* 278 */             return (this.windCnt2 != 0);
/*     */           case DIFFERENCE:
/* 280 */             return (this.windCnt2 > 0);
/*     */         } 
/* 282 */         return (this.windCnt2 < 0);
/*     */       
/*     */       case UNION:
/* 285 */         switch (pft2) {
/*     */           case INTERSECTION:
/*     */           case UNION:
/* 288 */             return (this.windCnt2 == 0);
/*     */           case DIFFERENCE:
/* 290 */             return (this.windCnt2 <= 0);
/*     */         } 
/* 292 */         return (this.windCnt2 >= 0);
/*     */       
/*     */       case DIFFERENCE:
/* 295 */         if (this.polyTyp == Clipper.PolyType.SUBJECT) {
/* 296 */           switch (pft2) {
/*     */             case INTERSECTION:
/*     */             case UNION:
/* 299 */               return (this.windCnt2 == 0);
/*     */             case DIFFERENCE:
/* 301 */               return (this.windCnt2 <= 0);
/*     */           } 
/* 303 */           return (this.windCnt2 >= 0);
/*     */         } 
/*     */ 
/*     */         
/* 307 */         switch (pft2) {
/*     */           case INTERSECTION:
/*     */           case UNION:
/* 310 */             return (this.windCnt2 != 0);
/*     */           case DIFFERENCE:
/* 312 */             return (this.windCnt2 > 0);
/*     */         } 
/* 314 */         return (this.windCnt2 < 0);
/*     */ 
/*     */       
/*     */       case XOR:
/* 318 */         if (this.windDelta == 0) {
/* 319 */           switch (pft2) {
/*     */             case INTERSECTION:
/*     */             case UNION:
/* 322 */               return (this.windCnt2 == 0);
/*     */             case DIFFERENCE:
/* 324 */               return (this.windCnt2 <= 0);
/*     */           } 
/* 326 */           return (this.windCnt2 >= 0);
/*     */         } 
/*     */ 
/*     */         
/* 330 */         return true;
/*     */     } 
/*     */     
/* 333 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isEvenOddAltFillType(Clipper.PolyFillType clipFillType, Clipper.PolyFillType subjFillType) {
/* 337 */     if (this.polyTyp == Clipper.PolyType.SUBJECT) {
/* 338 */       return (clipFillType == Clipper.PolyFillType.EVEN_ODD);
/*     */     }
/*     */     
/* 341 */     return (subjFillType == Clipper.PolyFillType.EVEN_ODD);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEvenOddFillType(Clipper.PolyFillType clipFillType, Clipper.PolyFillType subjFillType) {
/* 346 */     if (this.polyTyp == Clipper.PolyType.SUBJECT) {
/* 347 */       return (subjFillType == Clipper.PolyFillType.EVEN_ODD);
/*     */     }
/*     */     
/* 350 */     return (clipFillType == Clipper.PolyFillType.EVEN_ODD);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isHorizontal() {
/* 355 */     return (this.delta.getY() == 0L);
/*     */   }
/*     */   
/*     */   public boolean isIntermediate(double y) {
/* 359 */     return (this.top.getY() == y && this.nextInLML != null);
/*     */   }
/*     */   
/*     */   public boolean isMaxima(double Y) {
/* 363 */     return (this.top.getY() == Y && this.nextInLML == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reverseHorizontal() {
/* 370 */     long temp = this.top.getX();
/* 371 */     this.top.setX(Long.valueOf(this.bot.getX()));
/* 372 */     this.bot.setX(Long.valueOf(temp));
/*     */     
/* 374 */     temp = this.top.getZ();
/* 375 */     this.top.setZ(Long.valueOf(this.bot.getZ()));
/* 376 */     this.bot.setZ(Long.valueOf(temp));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBot(Point.LongPoint bot) {
/* 381 */     this.bot.set(bot);
/*     */   }
/*     */   
/*     */   public void setCurrent(Point.LongPoint current) {
/* 385 */     this.current.set(current);
/*     */   }
/*     */   
/*     */   public void setTop(Point.LongPoint top) {
/* 389 */     this.top.set(top);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 394 */     return "TEdge [Bot=" + this.bot + ", Curr=" + this.current + ", Top=" + this.top + ", Delta=" + this.delta + ", Dx=" + this.deltaX + ", PolyTyp=" + this.polyTyp + ", Side=" + this.side + ", WindDelta=" + this.windDelta + ", WindCnt=" + this.windCnt + ", WindCnt2=" + this.windCnt2 + ", OutIdx=" + this.outIdx + ", Next=" + this.next + ", Prev=" + this.prev + ", NextInLML=" + this.nextInLML + ", NextInAEL=" + this.nextInAEL + ", PrevInAEL=" + this.prevInAEL + ", NextInSEL=" + this.nextInSEL + ", PrevInSEL=" + this.prevInSEL + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateDeltaX() {
/* 402 */     this.delta.setX(Long.valueOf(this.top.getX() - this.bot.getX()));
/* 403 */     this.delta.setY(Long.valueOf(this.top.getY() - this.bot.getY()));
/* 404 */     if (this.delta.getY() == 0L) {
/* 405 */       this.deltaX = -3.4E38D;
/*     */     } else {
/*     */       
/* 408 */       this.deltaX = this.delta.getX() / this.delta.getY();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/clipper/Edge.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */