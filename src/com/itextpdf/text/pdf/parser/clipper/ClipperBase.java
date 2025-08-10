/*     */ package com.itextpdf.text.pdf.parser.clipper;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ClipperBase
/*     */   implements Clipper
/*     */ {
/*     */   private static final long LOW_RANGE = 1073741823L;
/*     */   private static final long HI_RANGE = 4611686018427387903L;
/*     */   protected LocalMinima minimaList;
/*     */   protected LocalMinima currentLM;
/*     */   private final List<List<Edge>> edges;
/*     */   protected boolean useFullRange;
/*     */   protected boolean hasOpenPaths;
/*     */   protected final boolean preserveCollinear;
/*     */   
/*     */   protected class LocalMinima
/*     */   {
/*     */     long y;
/*     */     Edge leftBound;
/*     */     Edge rightBound;
/*     */     LocalMinima next;
/*     */   }
/*     */   
/*     */   protected class Scanbeam
/*     */   {
/*     */     long y;
/*     */     Scanbeam next;
/*     */   }
/*     */   
/*     */   private static void initEdge(Edge e, Edge eNext, Edge ePrev, Point.LongPoint pt) {
/*  97 */     e.next = eNext;
/*  98 */     e.prev = ePrev;
/*  99 */     e.setCurrent(new Point.LongPoint(pt));
/* 100 */     e.outIdx = -1;
/*     */   }
/*     */   
/*     */   private static void initEdge2(Edge e, Clipper.PolyType polyType) {
/* 104 */     if (e.getCurrent().getY() >= e.next.getCurrent().getY()) {
/* 105 */       e.setBot(new Point.LongPoint(e.getCurrent()));
/* 106 */       e.setTop(new Point.LongPoint(e.next.getCurrent()));
/*     */     } else {
/*     */       
/* 109 */       e.setTop(new Point.LongPoint(e.getCurrent()));
/* 110 */       e.setBot(new Point.LongPoint(e.next.getCurrent()));
/*     */     } 
/* 112 */     e.updateDeltaX();
/* 113 */     e.polyTyp = polyType;
/*     */   }
/*     */   
/*     */   private static boolean rangeTest(Point.LongPoint Pt, boolean useFullRange) {
/* 117 */     if (useFullRange) {
/* 118 */       if (Pt.getX() > 4611686018427387903L || Pt.getY() > 4611686018427387903L || -Pt.getX() > 4611686018427387903L || -Pt.getY() > 4611686018427387903L)
/* 119 */         throw new IllegalStateException("Coordinate outside allowed range"); 
/* 120 */     } else if (Pt.getX() > 1073741823L || Pt.getY() > 1073741823L || -Pt.getX() > 1073741823L || -Pt.getY() > 1073741823L) {
/* 121 */       return rangeTest(Pt, true);
/*     */     } 
/*     */     
/* 124 */     return useFullRange;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Edge removeEdge(Edge e) {
/* 129 */     e.prev.next = e.next;
/* 130 */     e.next.prev = e.prev;
/* 131 */     Edge result = e.next;
/* 132 */     e.prev = null;
/* 133 */     return result;
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
/* 152 */   private static final Logger LOGGER = Logger.getLogger(Clipper.class.getName());
/*     */ 
/*     */   
/*     */   protected ClipperBase(boolean preserveCollinear) {
/* 156 */     this.preserveCollinear = preserveCollinear;
/* 157 */     this.minimaList = null;
/* 158 */     this.currentLM = null;
/* 159 */     this.hasOpenPaths = false;
/* 160 */     this.edges = new ArrayList<List<Edge>>();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addPath(Path pg, Clipper.PolyType polyType, boolean Closed) {
/* 165 */     if (!Closed && polyType == Clipper.PolyType.CLIP) {
/* 166 */       throw new IllegalStateException("AddPath: Open paths must be subject.");
/*     */     }
/*     */     
/* 169 */     int highI = pg.size() - 1;
/* 170 */     if (Closed) {
/* 171 */       while (highI > 0 && pg.get(highI).equals(pg.get(0))) {
/* 172 */         highI--;
/*     */       }
/*     */     }
/* 175 */     while (highI > 0 && pg.get(highI).equals(pg.get(highI - 1))) {
/* 176 */       highI--;
/*     */     }
/* 178 */     if ((Closed && highI < 2) || (!Closed && highI < 1)) {
/* 179 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 183 */     List<Edge> edges = new ArrayList<Edge>(highI + 1);
/* 184 */     for (int i = 0; i <= highI; i++) {
/* 185 */       edges.add(new Edge());
/*     */     }
/*     */     
/* 188 */     boolean IsFlat = true;
/*     */ 
/*     */     
/* 191 */     ((Edge)edges.get(1)).setCurrent(new Point.LongPoint(pg.get(1)));
/* 192 */     this.useFullRange = rangeTest(pg.get(0), this.useFullRange);
/* 193 */     this.useFullRange = rangeTest(pg.get(highI), this.useFullRange);
/* 194 */     initEdge(edges.get(0), edges.get(1), edges.get(highI), pg.get(0));
/* 195 */     initEdge(edges.get(highI), edges.get(0), edges.get(highI - 1), pg.get(highI));
/* 196 */     for (int j = highI - 1; j >= 1; j--) {
/* 197 */       this.useFullRange = rangeTest(pg.get(j), this.useFullRange);
/* 198 */       initEdge(edges.get(j), edges.get(j + 1), edges.get(j - 1), pg.get(j));
/*     */     } 
/* 200 */     Edge eStart = edges.get(0);
/*     */ 
/*     */     
/* 203 */     Edge e = eStart, eLoopStop = eStart;
/*     */     
/*     */     while (true) {
/* 206 */       if (e.getCurrent().equals(e.next.getCurrent()) && (Closed || !e.next.equals(eStart))) {
/* 207 */         if (e == e.next) {
/*     */           break;
/*     */         }
/* 210 */         if (e == eStart) {
/* 211 */           eStart = e.next;
/*     */         }
/* 213 */         e = removeEdge(e);
/* 214 */         eLoopStop = e;
/*     */         continue;
/*     */       } 
/* 217 */       if (e.prev == e.next) {
/*     */         break;
/*     */       }
/* 220 */       if (Closed && Point.slopesEqual(e.prev.getCurrent(), e.getCurrent(), e.next.getCurrent(), this.useFullRange) && (
/* 221 */         !isPreserveCollinear() || !Point.isPt2BetweenPt1AndPt3(e.prev.getCurrent(), e.getCurrent(), e.next.getCurrent()))) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 226 */         if (e == eStart) {
/* 227 */           eStart = e.next;
/*     */         }
/* 229 */         e = removeEdge(e);
/* 230 */         e = e.prev;
/* 231 */         eLoopStop = e;
/*     */         continue;
/*     */       } 
/* 234 */       e = e.next;
/* 235 */       if (e == eLoopStop || (!Closed && e.next == eStart)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 240 */     if ((!Closed && e == e.next) || (Closed && e.prev == e.next)) {
/* 241 */       return false;
/*     */     }
/*     */     
/* 244 */     if (!Closed) {
/* 245 */       this.hasOpenPaths = true;
/* 246 */       eStart.prev.outIdx = -2;
/*     */     } 
/*     */ 
/*     */     
/* 250 */     e = eStart;
/*     */     do {
/* 252 */       initEdge2(e, polyType);
/* 253 */       e = e.next;
/* 254 */       if (!IsFlat || e.getCurrent().getY() == eStart.getCurrent().getY())
/* 255 */         continue;  IsFlat = false;
/*     */     
/*     */     }
/* 258 */     while (e != eStart);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 264 */     if (IsFlat) {
/* 265 */       if (Closed) {
/* 266 */         return false;
/*     */       }
/* 268 */       e.prev.outIdx = -2;
/* 269 */       LocalMinima locMin = new LocalMinima();
/* 270 */       locMin.next = null;
/* 271 */       locMin.y = e.getBot().getY();
/* 272 */       locMin.leftBound = null;
/* 273 */       locMin.rightBound = e;
/* 274 */       locMin.rightBound.side = Edge.Side.RIGHT;
/* 275 */       locMin.rightBound.windDelta = 0;
/*     */       
/*     */       while (true) {
/* 278 */         if (e.getBot().getX() != e.prev.getTop().getX()) e.reverseHorizontal(); 
/* 279 */         if (e.next.outIdx == -2)
/* 280 */           break;  e.nextInLML = e.next;
/* 281 */         e = e.next;
/*     */       } 
/* 283 */       insertLocalMinima(locMin);
/* 284 */       this.edges.add(edges);
/* 285 */       return true;
/*     */     } 
/*     */     
/* 288 */     this.edges.add(edges);
/*     */     
/* 290 */     Edge EMin = null;
/*     */ 
/*     */ 
/*     */     
/* 294 */     if (e.prev.getBot().equals(e.prev.getTop())) {
/* 295 */       e = e.next;
/*     */     }
/*     */     while (true) {
/*     */       boolean leftBoundIsForward;
/* 299 */       e = e.findNextLocMin();
/* 300 */       if (e == EMin) {
/*     */         break;
/*     */       }
/* 303 */       if (EMin == null) {
/* 304 */         EMin = e;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 309 */       LocalMinima locMin = new LocalMinima();
/* 310 */       locMin.next = null;
/* 311 */       locMin.y = e.getBot().getY();
/* 312 */       if (e.deltaX < e.prev.deltaX) {
/* 313 */         locMin.leftBound = e.prev;
/* 314 */         locMin.rightBound = e;
/* 315 */         leftBoundIsForward = false;
/*     */       } else {
/*     */         
/* 318 */         locMin.leftBound = e;
/* 319 */         locMin.rightBound = e.prev;
/* 320 */         leftBoundIsForward = true;
/*     */       } 
/* 322 */       locMin.leftBound.side = Edge.Side.LEFT;
/* 323 */       locMin.rightBound.side = Edge.Side.RIGHT;
/*     */       
/* 325 */       if (!Closed) {
/* 326 */         locMin.leftBound.windDelta = 0;
/*     */       }
/* 328 */       else if (locMin.leftBound.next == locMin.rightBound) {
/* 329 */         locMin.leftBound.windDelta = -1;
/*     */       } else {
/*     */         
/* 332 */         locMin.leftBound.windDelta = 1;
/*     */       } 
/* 334 */       locMin.rightBound.windDelta = -locMin.leftBound.windDelta;
/*     */       
/* 336 */       e = processBound(locMin.leftBound, leftBoundIsForward);
/* 337 */       if (e.outIdx == -2) {
/* 338 */         e = processBound(e, leftBoundIsForward);
/*     */       }
/*     */       
/* 341 */       Edge E2 = processBound(locMin.rightBound, !leftBoundIsForward);
/* 342 */       if (E2.outIdx == -2) {
/* 343 */         E2 = processBound(E2, !leftBoundIsForward);
/*     */       }
/*     */       
/* 346 */       if (locMin.leftBound.outIdx == -2) {
/* 347 */         locMin.leftBound = null;
/*     */       }
/* 349 */       else if (locMin.rightBound.outIdx == -2) {
/* 350 */         locMin.rightBound = null;
/*     */       } 
/* 352 */       insertLocalMinima(locMin);
/* 353 */       if (!leftBoundIsForward) {
/* 354 */         e = E2;
/*     */       }
/*     */     } 
/* 357 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addPaths(Paths ppg, Clipper.PolyType polyType, boolean closed) {
/* 362 */     boolean result = false;
/* 363 */     for (int i = 0; i < ppg.size(); i++) {
/* 364 */       if (addPath(ppg.get(i), polyType, closed)) {
/* 365 */         result = true;
/*     */       }
/*     */     } 
/* 368 */     return result;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 372 */     disposeLocalMinimaList();
/* 373 */     this.edges.clear();
/* 374 */     this.useFullRange = false;
/* 375 */     this.hasOpenPaths = false;
/*     */   }
/*     */   
/*     */   private void disposeLocalMinimaList() {
/* 379 */     while (this.minimaList != null) {
/* 380 */       LocalMinima tmpLm = this.minimaList.next;
/* 381 */       this.minimaList = null;
/* 382 */       this.minimaList = tmpLm;
/*     */     } 
/* 384 */     this.currentLM = null;
/*     */   }
/*     */   
/*     */   private void insertLocalMinima(LocalMinima newLm) {
/* 388 */     if (this.minimaList == null) {
/* 389 */       this.minimaList = newLm;
/*     */     }
/* 391 */     else if (newLm.y >= this.minimaList.y) {
/* 392 */       newLm.next = this.minimaList;
/* 393 */       this.minimaList = newLm;
/*     */     } else {
/*     */       
/* 396 */       LocalMinima tmpLm = this.minimaList;
/* 397 */       while (tmpLm.next != null && newLm.y < tmpLm.next.y) {
/* 398 */         tmpLm = tmpLm.next;
/*     */       }
/* 400 */       newLm.next = tmpLm.next;
/* 401 */       tmpLm.next = newLm;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isPreserveCollinear() {
/* 406 */     return this.preserveCollinear;
/*     */   }
/*     */   
/*     */   protected void popLocalMinima() {
/* 410 */     LOGGER.entering(ClipperBase.class.getName(), "popLocalMinima");
/* 411 */     if (this.currentLM == null) {
/*     */       return;
/*     */     }
/* 414 */     this.currentLM = this.currentLM.next;
/*     */   }
/*     */   
/*     */   private Edge processBound(Edge e, boolean LeftBoundIsForward) {
/* 418 */     Edge result = e;
/*     */ 
/*     */     
/* 421 */     if (result.outIdx == -2) {
/*     */ 
/*     */       
/* 424 */       e = result;
/* 425 */       if (LeftBoundIsForward) {
/* 426 */         while (e.getTop().getY() == e.next.getBot().getY()) {
/* 427 */           e = e.next;
/*     */         }
/* 429 */         while (e != result && e.deltaX == -3.4E38D) {
/* 430 */           e = e.prev;
/*     */         }
/*     */       } else {
/*     */         
/* 434 */         while (e.getTop().getY() == e.prev.getBot().getY()) {
/* 435 */           e = e.prev;
/*     */         }
/* 437 */         while (e != result && e.deltaX == -3.4E38D) {
/* 438 */           e = e.next;
/*     */         }
/*     */       } 
/* 441 */       if (e == result) {
/* 442 */         if (LeftBoundIsForward) {
/* 443 */           result = e.next;
/*     */         } else {
/*     */           
/* 446 */           result = e.prev;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 451 */         if (LeftBoundIsForward) {
/* 452 */           e = result.next;
/*     */         } else {
/*     */           
/* 455 */           e = result.prev;
/*     */         } 
/* 457 */         LocalMinima locMin = new LocalMinima();
/* 458 */         locMin.next = null;
/* 459 */         locMin.y = e.getBot().getY();
/* 460 */         locMin.leftBound = null;
/* 461 */         locMin.rightBound = e;
/* 462 */         e.windDelta = 0;
/* 463 */         result = processBound(e, LeftBoundIsForward);
/* 464 */         insertLocalMinima(locMin);
/*     */       } 
/* 466 */       return result;
/*     */     } 
/*     */     
/* 469 */     if (e.deltaX == -3.4E38D) {
/*     */       Edge edge;
/*     */ 
/*     */       
/* 473 */       if (LeftBoundIsForward) {
/* 474 */         edge = e.prev;
/*     */       } else {
/*     */         
/* 477 */         edge = e.next;
/*     */       } 
/* 479 */       if (edge.deltaX == -3.4E38D) {
/*     */         
/* 481 */         if (edge.getBot().getX() != e.getBot().getX() && edge.getTop().getX() != e.getBot().getX()) {
/* 482 */           e.reverseHorizontal();
/*     */         }
/* 484 */       } else if (edge.getBot().getX() != e.getBot().getX()) {
/* 485 */         e.reverseHorizontal();
/*     */       } 
/*     */     } 
/* 488 */     Edge EStart = e;
/* 489 */     if (LeftBoundIsForward) {
/* 490 */       while (result.getTop().getY() == result.next.getBot().getY() && result.next.outIdx != -2) {
/* 491 */         result = result.next;
/*     */       }
/* 493 */       if (result.deltaX == -3.4E38D && result.next.outIdx != -2) {
/*     */ 
/*     */ 
/*     */         
/* 497 */         Edge Horz = result;
/* 498 */         while (Horz.prev.deltaX == -3.4E38D) {
/* 499 */           Horz = Horz.prev;
/*     */         }
/* 501 */         if (Horz.prev.getTop().getX() > result.next.getTop().getX()) result = Horz.prev; 
/*     */       } 
/* 503 */       while (e != result) {
/* 504 */         e.nextInLML = e.next;
/* 505 */         if (e.deltaX == -3.4E38D && e != EStart && e.getBot().getX() != e.prev.getTop().getX()) {
/* 506 */           e.reverseHorizontal();
/*     */         }
/* 508 */         e = e.next;
/*     */       } 
/* 510 */       if (e.deltaX == -3.4E38D && e != EStart && e.getBot().getX() != e.prev.getTop().getX()) {
/* 511 */         e.reverseHorizontal();
/*     */       }
/* 513 */       result = result.next;
/*     */     } else {
/*     */       
/* 516 */       while (result.getTop().getY() == result.prev.getBot().getY() && result.prev.outIdx != -2) {
/* 517 */         result = result.prev;
/*     */       }
/* 519 */       if (result.deltaX == -3.4E38D && result.prev.outIdx != -2) {
/* 520 */         Edge Horz = result;
/* 521 */         while (Horz.next.deltaX == -3.4E38D) {
/* 522 */           Horz = Horz.next;
/*     */         }
/* 524 */         if (Horz.next.getTop().getX() == result.prev.getTop().getX() || Horz.next
/* 525 */           .getTop().getX() > result.prev.getTop().getX()) result = Horz.next;
/*     */       
/*     */       } 
/* 528 */       while (e != result) {
/* 529 */         e.nextInLML = e.prev;
/* 530 */         if (e.deltaX == -3.4E38D && e != EStart && e.getBot().getX() != e.next.getTop().getX()) {
/* 531 */           e.reverseHorizontal();
/*     */         }
/* 533 */         e = e.prev;
/*     */       } 
/* 535 */       if (e.deltaX == -3.4E38D && e != EStart && e.getBot().getX() != e.next.getTop().getX()) {
/* 536 */         e.reverseHorizontal();
/*     */       }
/* 538 */       result = result.prev;
/*     */     } 
/* 540 */     return result;
/*     */   }
/*     */   
/*     */   protected static Path.OutRec parseFirstLeft(Path.OutRec FirstLeft) {
/* 544 */     while (FirstLeft != null && FirstLeft.getPoints() == null)
/* 545 */       FirstLeft = FirstLeft.firstLeft; 
/* 546 */     return FirstLeft;
/*     */   }
/*     */   
/*     */   protected void reset() {
/* 550 */     this.currentLM = this.minimaList;
/* 551 */     if (this.currentLM == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 556 */     LocalMinima lm = this.minimaList;
/* 557 */     while (lm != null) {
/* 558 */       Edge e = lm.leftBound;
/* 559 */       if (e != null) {
/* 560 */         e.setCurrent(new Point.LongPoint(e.getBot()));
/* 561 */         e.side = Edge.Side.LEFT;
/* 562 */         e.outIdx = -1;
/*     */       } 
/* 564 */       e = lm.rightBound;
/* 565 */       if (e != null) {
/* 566 */         e.setCurrent(new Point.LongPoint(e.getBot()));
/* 567 */         e.side = Edge.Side.RIGHT;
/* 568 */         e.outIdx = -1;
/*     */       } 
/* 570 */       lm = lm.next;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/clipper/ClipperBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */