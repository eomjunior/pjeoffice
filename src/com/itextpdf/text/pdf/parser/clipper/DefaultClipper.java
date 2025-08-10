/*      */ package com.itextpdf.text.pdf.parser.clipper;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DefaultClipper
/*      */   extends ClipperBase
/*      */ {
/*      */   protected final List<Path.OutRec> polyOuts;
/*      */   private Clipper.ClipType clipType;
/*      */   private ClipperBase.Scanbeam scanbeam;
/*      */   private Path.Maxima maxima;
/*      */   private Edge activeEdges;
/*      */   private Edge sortedEdges;
/*      */   private final List<IntersectNode> intersectList;
/*      */   private final Comparator<IntersectNode> intersectNodeComparer;
/*      */   private Clipper.PolyFillType clipFillType;
/*      */   private Clipper.PolyFillType subjFillType;
/*      */   private final List<Path.Join> joins;
/*      */   private final List<Path.Join> ghostJoins;
/*      */   private boolean usingPolyTree;
/*      */   public Clipper.ZFillCallback zFillFunction;
/*      */   private final boolean reverseSolution;
/*      */   private final boolean strictlySimple;
/*      */   
/*      */   private class IntersectNode
/*      */   {
/*      */     Edge edge1;
/*      */     Edge Edge2;
/*      */     private Point.LongPoint pt;
/*      */     
/*      */     private IntersectNode() {}
/*      */     
/*      */     public Point.LongPoint getPt() {
/*   94 */       return this.pt;
/*      */     }
/*      */     
/*      */     public void setPt(Point.LongPoint pt) {
/*   98 */       this.pt = pt;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static void getHorzDirection(Edge HorzEdge, Clipper.Direction[] Dir, long[] Left, long[] Right) {
/*  104 */     if (HorzEdge.getBot().getX() < HorzEdge.getTop().getX()) {
/*  105 */       Left[0] = HorzEdge.getBot().getX();
/*  106 */       Right[0] = HorzEdge.getTop().getX();
/*  107 */       Dir[0] = Clipper.Direction.LEFT_TO_RIGHT;
/*      */     } else {
/*      */       
/*  110 */       Left[0] = HorzEdge.getTop().getX();
/*  111 */       Right[0] = HorzEdge.getBot().getX();
/*  112 */       Dir[0] = Clipper.Direction.RIGHT_TO_LEFT;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static boolean getOverlap(long a1, long a2, long b1, long b2, long[] Left, long[] Right) {
/*  117 */     if (a1 < a2) {
/*  118 */       if (b1 < b2) {
/*  119 */         Left[0] = Math.max(a1, b1);
/*  120 */         Right[0] = Math.min(a2, b2);
/*      */       } else {
/*      */         
/*  123 */         Left[0] = Math.max(a1, b2);
/*  124 */         Right[0] = Math.min(a2, b1);
/*      */       }
/*      */     
/*      */     }
/*  128 */     else if (b1 < b2) {
/*  129 */       Left[0] = Math.max(a2, b1);
/*  130 */       Right[0] = Math.min(a1, b2);
/*      */     } else {
/*      */       
/*  133 */       Left[0] = Math.max(a2, b2);
/*  134 */       Right[0] = Math.min(a1, b1);
/*      */     } 
/*      */     
/*  137 */     return (Left[0] < Right[0]);
/*      */   }
/*      */   
/*      */   private static boolean isParam1RightOfParam2(Path.OutRec outRec1, Path.OutRec outRec2) {
/*      */     while (true) {
/*  142 */       outRec1 = outRec1.firstLeft;
/*  143 */       if (outRec1 == outRec2) {
/*  144 */         return true;
/*      */       }
/*      */       
/*  147 */       if (outRec1 == null) {
/*  148 */         return false;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static int isPointInPolygon(Point.LongPoint pt, Path.OutPt op) {
/*  155 */     int result = 0;
/*  156 */     Path.OutPt startOp = op;
/*  157 */     long ptx = pt.getX(), pty = pt.getY();
/*  158 */     long poly0x = op.getPt().getX(), poly0y = op.getPt().getY();
/*      */     do {
/*  160 */       op = op.next;
/*  161 */       long poly1x = op.getPt().getX(), poly1y = op.getPt().getY();
/*      */       
/*  163 */       if (poly1y == pty) {
/*  164 */         if (poly1x != ptx) { if (poly0y == pty) if (((poly1x > ptx) ? true : false) == ((poly0x < ptx) ? true : false))
/*  165 */               return -1;   } else { return -1; }
/*      */       
/*      */       }
/*  168 */       if (((poly0y < pty) ? true : false) != ((poly1y < pty) ? true : false)) {
/*  169 */         if (poly0x >= ptx) {
/*  170 */           if (poly1x > ptx) {
/*  171 */             result = 1 - result;
/*      */           } else {
/*      */             
/*  174 */             double d = (poly0x - ptx) * (poly1y - pty) - (poly1x - ptx) * (poly0y - pty);
/*  175 */             if (d == 0.0D) {
/*  176 */               return -1;
/*      */             }
/*  178 */             if (((d > 0.0D) ? true : false) == ((poly1y > poly0y) ? true : false)) {
/*  179 */               result = 1 - result;
/*      */             }
/*      */           }
/*      */         
/*      */         }
/*  184 */         else if (poly1x > ptx) {
/*  185 */           double d = (poly0x - ptx) * (poly1y - pty) - (poly1x - ptx) * (poly0y - pty);
/*  186 */           if (d == 0.0D) {
/*  187 */             return -1;
/*      */           }
/*  189 */           if (((d > 0.0D) ? true : false) == ((poly1y > poly0y) ? true : false)) {
/*  190 */             result = 1 - result;
/*      */           }
/*      */         } 
/*      */       }
/*      */       
/*  195 */       poly0x = poly1x;
/*  196 */       poly0y = poly1y;
/*      */     }
/*  198 */     while (startOp != op);
/*      */     
/*  200 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean joinHorz(Path.OutPt op1, Path.OutPt op1b, Path.OutPt op2, Path.OutPt op2b, Point.LongPoint Pt, boolean DiscardLeft) {
/*  205 */     Clipper.Direction Dir1 = (op1.getPt().getX() > op1b.getPt().getX()) ? Clipper.Direction.RIGHT_TO_LEFT : Clipper.Direction.LEFT_TO_RIGHT;
/*  206 */     Clipper.Direction Dir2 = (op2.getPt().getX() > op2b.getPt().getX()) ? Clipper.Direction.RIGHT_TO_LEFT : Clipper.Direction.LEFT_TO_RIGHT;
/*  207 */     if (Dir1 == Dir2) {
/*  208 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  216 */     if (Dir1 == Clipper.Direction.LEFT_TO_RIGHT) {
/*  217 */       while (op1.next.getPt().getX() <= Pt.getX() && op1.next.getPt().getX() >= op1.getPt().getX() && op1.next.getPt().getY() == Pt.getY()) {
/*  218 */         op1 = op1.next;
/*      */       }
/*  220 */       if (DiscardLeft && op1.getPt().getX() != Pt.getX()) {
/*  221 */         op1 = op1.next;
/*      */       }
/*  223 */       op1b = op1.duplicate(!DiscardLeft);
/*  224 */       if (!op1b.getPt().equals(Pt)) {
/*  225 */         op1 = op1b;
/*  226 */         op1.setPt(Pt);
/*  227 */         op1b = op1.duplicate(!DiscardLeft);
/*      */       } 
/*      */     } else {
/*      */       
/*  231 */       while (op1.next.getPt().getX() >= Pt.getX() && op1.next.getPt().getX() <= op1.getPt().getX() && op1.next.getPt().getY() == Pt.getY()) {
/*  232 */         op1 = op1.next;
/*      */       }
/*  234 */       if (!DiscardLeft && op1.getPt().getX() != Pt.getX()) {
/*  235 */         op1 = op1.next;
/*      */       }
/*  237 */       op1b = op1.duplicate(DiscardLeft);
/*  238 */       if (!op1b.getPt().equals(Pt)) {
/*  239 */         op1 = op1b;
/*  240 */         op1.setPt(Pt);
/*  241 */         op1b = op1.duplicate(DiscardLeft);
/*      */       } 
/*      */     } 
/*      */     
/*  245 */     if (Dir2 == Clipper.Direction.LEFT_TO_RIGHT) {
/*  246 */       while (op2.next.getPt().getX() <= Pt.getX() && op2.next.getPt().getX() >= op2.getPt().getX() && op2.next.getPt().getY() == Pt.getY()) {
/*  247 */         op2 = op2.next;
/*      */       }
/*  249 */       if (DiscardLeft && op2.getPt().getX() != Pt.getX()) {
/*  250 */         op2 = op2.next;
/*      */       }
/*  252 */       op2b = op2.duplicate(!DiscardLeft);
/*  253 */       if (!op2b.getPt().equals(Pt)) {
/*  254 */         op2 = op2b;
/*  255 */         op2.setPt(Pt);
/*  256 */         op2b = op2.duplicate(!DiscardLeft);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  261 */       while (op2.next.getPt().getX() >= Pt.getX() && op2.next.getPt().getX() <= op2.getPt().getX() && op2.next.getPt().getY() == Pt.getY()) {
/*  262 */         op2 = op2.next;
/*      */       }
/*  264 */       if (!DiscardLeft && op2.getPt().getX() != Pt.getX()) {
/*  265 */         op2 = op2.next;
/*      */       }
/*  267 */       op2b = op2.duplicate(DiscardLeft);
/*  268 */       if (!op2b.getPt().equals(Pt)) {
/*  269 */         op2 = op2b;
/*  270 */         op2.setPt(Pt);
/*  271 */         op2b = op2.duplicate(DiscardLeft);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  277 */     if (((Dir1 == Clipper.Direction.LEFT_TO_RIGHT)) == DiscardLeft) {
/*  278 */       op1.prev = op2;
/*  279 */       op2.next = op1;
/*  280 */       op1b.next = op2b;
/*  281 */       op2b.prev = op1b;
/*      */     } else {
/*      */       
/*  284 */       op1.next = op2;
/*  285 */       op2.prev = op1;
/*  286 */       op1b.prev = op2b;
/*  287 */       op2b.next = op1b;
/*      */     } 
/*  289 */     return true;
/*      */   }
/*      */   
/*      */   private boolean joinPoints(Path.Join j, Path.OutRec outRec1, Path.OutRec outRec2) {
/*  293 */     Path.OutPt op1 = j.outPt1;
/*  294 */     Path.OutPt op2 = j.outPt2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  303 */     boolean isHorizontal = (j.outPt1.getPt().getY() == j.getOffPt().getY());
/*      */     
/*  305 */     if (isHorizontal && j.getOffPt().equals(j.outPt1.getPt()) && j.getOffPt().equals(j.outPt2.getPt())) {
/*      */       
/*  307 */       if (outRec1 != outRec2) {
/*  308 */         return false;
/*      */       }
/*  310 */       Path.OutPt outPt1 = j.outPt1.next;
/*  311 */       while (outPt1 != op1 && outPt1.getPt().equals(j.getOffPt())) {
/*  312 */         outPt1 = outPt1.next;
/*      */       }
/*  314 */       boolean reverse1 = (outPt1.getPt().getY() > j.getOffPt().getY());
/*  315 */       Path.OutPt outPt2 = j.outPt2.next;
/*  316 */       while (outPt2 != op2 && outPt2.getPt().equals(j.getOffPt())) {
/*  317 */         outPt2 = outPt2.next;
/*      */       }
/*  319 */       boolean reverse2 = (outPt2.getPt().getY() > j.getOffPt().getY());
/*  320 */       if (reverse1 == reverse2) {
/*  321 */         return false;
/*      */       }
/*  323 */       if (reverse1) {
/*  324 */         outPt1 = op1.duplicate(false);
/*  325 */         outPt2 = op2.duplicate(true);
/*  326 */         op1.prev = op2;
/*  327 */         op2.next = op1;
/*  328 */         outPt1.next = outPt2;
/*  329 */         outPt2.prev = outPt1;
/*  330 */         j.outPt1 = op1;
/*  331 */         j.outPt2 = outPt1;
/*  332 */         return true;
/*      */       } 
/*      */       
/*  335 */       outPt1 = op1.duplicate(true);
/*  336 */       outPt2 = op2.duplicate(false);
/*  337 */       op1.next = op2;
/*  338 */       op2.prev = op1;
/*  339 */       outPt1.prev = outPt2;
/*  340 */       outPt2.next = outPt1;
/*  341 */       j.outPt1 = op1;
/*  342 */       j.outPt2 = outPt1;
/*  343 */       return true;
/*      */     } 
/*      */     
/*  346 */     if (isHorizontal) {
/*      */       Point.LongPoint Pt;
/*      */       
/*      */       boolean DiscardLeftSide;
/*  350 */       Path.OutPt outPt1 = op1;
/*  351 */       while (op1.prev.getPt().getY() == op1.getPt().getY() && op1.prev != outPt1 && op1.prev != op2) {
/*  352 */         op1 = op1.prev;
/*      */       }
/*  354 */       while (outPt1.next.getPt().getY() == outPt1.getPt().getY() && outPt1.next != op1 && outPt1.next != op2) {
/*  355 */         outPt1 = outPt1.next;
/*      */       }
/*  357 */       if (outPt1.next == op1 || outPt1.next == op2) {
/*  358 */         return false;
/*      */       }
/*      */       
/*  361 */       Path.OutPt outPt2 = op2;
/*  362 */       while (op2.prev.getPt().getY() == op2.getPt().getY() && op2.prev != outPt2 && op2.prev != outPt1) {
/*  363 */         op2 = op2.prev;
/*      */       }
/*  365 */       while (outPt2.next.getPt().getY() == outPt2.getPt().getY() && outPt2.next != op2 && outPt2.next != op1) {
/*  366 */         outPt2 = outPt2.next;
/*      */       }
/*  368 */       if (outPt2.next == op2 || outPt2.next == op1) {
/*  369 */         return false;
/*      */       }
/*      */       
/*  372 */       long[] LeftV = new long[1], RightV = new long[1];
/*      */       
/*  374 */       if (!getOverlap(op1.getPt().getX(), outPt1.getPt().getX(), op2.getPt().getX(), outPt2.getPt().getX(), LeftV, RightV)) {
/*  375 */         return false;
/*      */       }
/*  377 */       long Left = LeftV[0];
/*  378 */       long Right = RightV[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  385 */       if (op1.getPt().getX() >= Left && op1.getPt().getX() <= Right) {
/*  386 */         Pt = new Point.LongPoint(op1.getPt());
/*  387 */         DiscardLeftSide = (op1.getPt().getX() > outPt1.getPt().getX());
/*      */       }
/*  389 */       else if (op2.getPt().getX() >= Left && op2.getPt().getX() <= Right) {
/*  390 */         Pt = new Point.LongPoint(op2.getPt());
/*  391 */         DiscardLeftSide = (op2.getPt().getX() > outPt2.getPt().getX());
/*      */       }
/*  393 */       else if (outPt1.getPt().getX() >= Left && outPt1.getPt().getX() <= Right) {
/*  394 */         Pt = new Point.LongPoint(outPt1.getPt());
/*  395 */         DiscardLeftSide = (outPt1.getPt().getX() > op1.getPt().getX());
/*      */       } else {
/*      */         
/*  398 */         Pt = new Point.LongPoint(outPt2.getPt());
/*  399 */         DiscardLeftSide = (outPt2.getPt().getX() > op2.getPt().getX());
/*      */       } 
/*  401 */       j.outPt1 = op1;
/*  402 */       j.outPt2 = op2;
/*  403 */       return joinHorz(op1, outPt1, op2, outPt2, Pt, DiscardLeftSide);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  411 */     Path.OutPt op1b = op1.next;
/*  412 */     while (op1b.getPt().equals(op1.getPt()) && op1b != op1) {
/*  413 */       op1b = op1b.next;
/*      */     }
/*  415 */     boolean Reverse1 = (op1b.getPt().getY() > op1.getPt().getY() || !Point.slopesEqual(op1.getPt(), op1b.getPt(), j.getOffPt(), this.useFullRange));
/*  416 */     if (Reverse1) {
/*  417 */       op1b = op1.prev;
/*  418 */       while (op1b.getPt().equals(op1.getPt()) && op1b != op1) {
/*  419 */         op1b = op1b.prev;
/*      */       }
/*  421 */       if (op1b.getPt().getY() > op1.getPt().getY() || !Point.slopesEqual(op1.getPt(), op1b.getPt(), j.getOffPt(), this.useFullRange)) {
/*  422 */         return false;
/*      */       }
/*      */     } 
/*      */     
/*  426 */     Path.OutPt op2b = op2.next;
/*  427 */     while (op2b.getPt().equals(op2.getPt()) && op2b != op2) {
/*  428 */       op2b = op2b.next;
/*      */     }
/*  430 */     boolean Reverse2 = (op2b.getPt().getY() > op2.getPt().getY() || !Point.slopesEqual(op2.getPt(), op2b.getPt(), j.getOffPt(), this.useFullRange));
/*  431 */     if (Reverse2) {
/*  432 */       op2b = op2.prev;
/*  433 */       while (op2b.getPt().equals(op2.getPt()) && op2b != op2) {
/*  434 */         op2b = op2b.prev;
/*      */       }
/*  436 */       if (op2b.getPt().getY() > op2.getPt().getY() || !Point.slopesEqual(op2.getPt(), op2b.getPt(), j.getOffPt(), this.useFullRange)) {
/*  437 */         return false;
/*      */       }
/*      */     } 
/*      */     
/*  441 */     if (op1b == op1 || op2b == op2 || op1b == op2b || (outRec1 == outRec2 && Reverse1 == Reverse2)) {
/*  442 */       return false;
/*      */     }
/*      */     
/*  445 */     if (Reverse1) {
/*  446 */       op1b = op1.duplicate(false);
/*  447 */       op2b = op2.duplicate(true);
/*  448 */       op1.prev = op2;
/*  449 */       op2.next = op1;
/*  450 */       op1b.next = op2b;
/*  451 */       op2b.prev = op1b;
/*  452 */       j.outPt1 = op1;
/*  453 */       j.outPt2 = op1b;
/*  454 */       return true;
/*      */     } 
/*      */     
/*  457 */     op1b = op1.duplicate(true);
/*  458 */     op2b = op2.duplicate(false);
/*  459 */     op1.next = op2;
/*  460 */     op2.prev = op1;
/*  461 */     op1b.prev = op2b;
/*  462 */     op2b.next = op1b;
/*  463 */     j.outPt1 = op1;
/*  464 */     j.outPt2 = op1b;
/*  465 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Paths minkowski(Path pattern, Path path, boolean IsSum, boolean IsClosed) {
/*  471 */     int delta = IsClosed ? 1 : 0;
/*  472 */     int polyCnt = pattern.size();
/*  473 */     int pathCnt = path.size();
/*  474 */     Paths result = new Paths(pathCnt);
/*  475 */     if (IsSum) {
/*  476 */       for (int j = 0; j < pathCnt; j++) {
/*  477 */         Path p = new Path(polyCnt);
/*  478 */         for (Point.LongPoint ip : pattern) {
/*  479 */           p.add(new Point.LongPoint(path.get(j).getX() + ip.getX(), path.get(j).getY() + ip.getY(), 0L));
/*      */         }
/*  481 */         result.add(p);
/*      */       } 
/*      */     } else {
/*      */       
/*  485 */       for (int j = 0; j < pathCnt; j++) {
/*  486 */         Path p = new Path(polyCnt);
/*  487 */         for (Point.LongPoint ip : pattern) {
/*  488 */           p.add(new Point.LongPoint(path.get(j).getX() - ip.getX(), path.get(j).getY() - ip.getY(), 0L));
/*      */         }
/*  490 */         result.add(p);
/*      */       } 
/*      */     } 
/*      */     
/*  494 */     Paths quads = new Paths((pathCnt + delta) * (polyCnt + 1));
/*  495 */     for (int i = 0; i < pathCnt - 1 + delta; i++) {
/*  496 */       for (int j = 0; j < polyCnt; j++) {
/*  497 */         Path quad = new Path(4);
/*  498 */         quad.add(result.get(i % pathCnt).get(j % polyCnt));
/*  499 */         quad.add(result.get((i + 1) % pathCnt).get(j % polyCnt));
/*  500 */         quad.add(result.get((i + 1) % pathCnt).get((j + 1) % polyCnt));
/*  501 */         quad.add(result.get(i % pathCnt).get((j + 1) % polyCnt));
/*  502 */         if (!quad.orientation()) {
/*  503 */           Collections.reverse(quad);
/*      */         }
/*  505 */         quads.add(quad);
/*      */       } 
/*      */     } 
/*  508 */     return quads;
/*      */   }
/*      */   
/*      */   public static Paths minkowskiDiff(Path poly1, Path poly2) {
/*  512 */     Paths paths = minkowski(poly1, poly2, false, true);
/*  513 */     DefaultClipper c = new DefaultClipper();
/*  514 */     c.addPaths(paths, Clipper.PolyType.SUBJECT, true);
/*  515 */     c.execute(Clipper.ClipType.UNION, paths, Clipper.PolyFillType.NON_ZERO, Clipper.PolyFillType.NON_ZERO);
/*  516 */     return paths;
/*      */   }
/*      */   
/*      */   public static Paths minkowskiSum(Path pattern, Path path, boolean pathIsClosed) {
/*  520 */     Paths paths = minkowski(pattern, path, true, pathIsClosed);
/*  521 */     DefaultClipper c = new DefaultClipper();
/*  522 */     c.addPaths(paths, Clipper.PolyType.SUBJECT, true);
/*  523 */     c.execute(Clipper.ClipType.UNION, paths, Clipper.PolyFillType.NON_ZERO, Clipper.PolyFillType.NON_ZERO);
/*  524 */     return paths;
/*      */   }
/*      */   
/*      */   public static Paths minkowskiSum(Path pattern, Paths paths, boolean pathIsClosed) {
/*  528 */     Paths solution = new Paths();
/*  529 */     DefaultClipper c = new DefaultClipper();
/*  530 */     for (int i = 0; i < paths.size(); i++) {
/*  531 */       Paths tmp = minkowski(pattern, paths.get(i), true, pathIsClosed);
/*  532 */       c.addPaths(tmp, Clipper.PolyType.SUBJECT, true);
/*  533 */       if (pathIsClosed) {
/*  534 */         Path path = paths.get(i).TranslatePath(pattern.get(0));
/*  535 */         c.addPath(path, Clipper.PolyType.CLIP, true);
/*      */       } 
/*      */     } 
/*  538 */     c.execute(Clipper.ClipType.UNION, solution, Clipper.PolyFillType.NON_ZERO, Clipper.PolyFillType.NON_ZERO);
/*  539 */     return solution;
/*      */   }
/*      */   
/*      */   private static boolean poly2ContainsPoly1(Path.OutPt outPt1, Path.OutPt outPt2) {
/*  543 */     Path.OutPt op = outPt1;
/*      */     
/*      */     while (true) {
/*  546 */       int res = isPointInPolygon(op.getPt(), outPt2);
/*  547 */       if (res >= 0) {
/*  548 */         return (res > 0);
/*      */       }
/*  550 */       op = op.next;
/*      */       
/*  552 */       if (op == outPt1) {
/*  553 */         return true;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Paths simplifyPolygon(Path poly) {
/*  561 */     return simplifyPolygon(poly, Clipper.PolyFillType.EVEN_ODD);
/*      */   }
/*      */   
/*      */   public static Paths simplifyPolygon(Path poly, Clipper.PolyFillType fillType) {
/*  565 */     Paths result = new Paths();
/*  566 */     DefaultClipper c = new DefaultClipper(2);
/*      */     
/*  568 */     c.addPath(poly, Clipper.PolyType.SUBJECT, true);
/*  569 */     c.execute(Clipper.ClipType.UNION, result, fillType, fillType);
/*  570 */     return result;
/*      */   }
/*      */   
/*      */   public static Paths simplifyPolygons(Paths polys) {
/*  574 */     return simplifyPolygons(polys, Clipper.PolyFillType.EVEN_ODD);
/*      */   }
/*      */   
/*      */   public static Paths simplifyPolygons(Paths polys, Clipper.PolyFillType fillType) {
/*  578 */     Paths result = new Paths();
/*  579 */     DefaultClipper c = new DefaultClipper(2);
/*      */     
/*  581 */     c.addPaths(polys, Clipper.PolyType.SUBJECT, true);
/*  582 */     c.execute(Clipper.ClipType.UNION, result, fillType, fillType);
/*  583 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  628 */   private static final Logger LOGGER = Logger.getLogger(DefaultClipper.class.getName());
/*      */   
/*      */   public DefaultClipper() {
/*  631 */     this(0);
/*      */   }
/*      */ 
/*      */   
/*      */   public DefaultClipper(int InitOptions) {
/*  636 */     super(((0x4 & InitOptions) != 0));
/*  637 */     this.scanbeam = null;
/*  638 */     this.maxima = null;
/*  639 */     this.activeEdges = null;
/*  640 */     this.sortedEdges = null;
/*  641 */     this.intersectList = new ArrayList<IntersectNode>();
/*  642 */     this.intersectNodeComparer = new Comparator<IntersectNode>() {
/*      */         public int compare(DefaultClipper.IntersectNode o1, DefaultClipper.IntersectNode o2) {
/*  644 */           long i = o2.getPt().getY() - o1.getPt().getY();
/*  645 */           if (i > 0L) {
/*  646 */             return 1;
/*      */           }
/*  648 */           if (i < 0L) {
/*  649 */             return -1;
/*      */           }
/*      */           
/*  652 */           return 0;
/*      */         }
/*      */       };
/*      */ 
/*      */     
/*  657 */     this.usingPolyTree = false;
/*  658 */     this.polyOuts = new ArrayList<Path.OutRec>();
/*  659 */     this.joins = new ArrayList<Path.Join>();
/*  660 */     this.ghostJoins = new ArrayList<Path.Join>();
/*  661 */     this.reverseSolution = ((0x1 & InitOptions) != 0);
/*  662 */     this.strictlySimple = ((0x2 & InitOptions) != 0);
/*      */     
/*  664 */     this.zFillFunction = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void insertScanbeam(long Y) {
/*  672 */     if (this.scanbeam == null) {
/*      */       
/*  674 */       this.scanbeam = new ClipperBase.Scanbeam(this);
/*  675 */       this.scanbeam.next = null;
/*  676 */       this.scanbeam.y = Y;
/*      */     }
/*  678 */     else if (Y > this.scanbeam.y) {
/*      */       
/*  680 */       ClipperBase.Scanbeam newSb = new ClipperBase.Scanbeam(this);
/*  681 */       newSb.y = Y;
/*  682 */       newSb.next = this.scanbeam;
/*  683 */       this.scanbeam = newSb;
/*      */     }
/*      */     else {
/*      */       
/*  687 */       ClipperBase.Scanbeam sb2 = this.scanbeam;
/*  688 */       for (; sb2.next != null && Y <= sb2.next.y; sb2 = sb2.next);
/*  689 */       if (Y == sb2.y)
/*  690 */         return;  ClipperBase.Scanbeam newSb = new ClipperBase.Scanbeam(this);
/*  691 */       newSb.y = Y;
/*  692 */       newSb.next = sb2.next;
/*  693 */       sb2.next = newSb;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void InsertMaxima(long X) {
/*  701 */     Path.Maxima newMax = new Path.Maxima();
/*  702 */     newMax.X = X;
/*  703 */     if (this.maxima == null) {
/*      */       
/*  705 */       this.maxima = newMax;
/*  706 */       this.maxima.Next = null;
/*  707 */       this.maxima.Prev = null;
/*      */     }
/*  709 */     else if (X < this.maxima.X) {
/*      */       
/*  711 */       newMax.Next = this.maxima;
/*  712 */       newMax.Prev = null;
/*  713 */       this.maxima = newMax;
/*      */     }
/*      */     else {
/*      */       
/*  717 */       Path.Maxima m = this.maxima;
/*  718 */       for (; m.Next != null && X >= m.Next.X; m = m.Next);
/*  719 */       if (X == m.X)
/*      */         return; 
/*  721 */       newMax.Next = m.Next;
/*  722 */       newMax.Prev = m;
/*  723 */       if (m.Next != null) m.Next.Prev = newMax; 
/*  724 */       m.Next = newMax;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void addEdgeToSEL(Edge edge) {
/*  730 */     LOGGER.entering(DefaultClipper.class.getName(), "addEdgeToSEL");
/*      */ 
/*      */ 
/*      */     
/*  734 */     if (this.sortedEdges == null) {
/*  735 */       this.sortedEdges = edge;
/*  736 */       edge.prevInSEL = null;
/*  737 */       edge.nextInSEL = null;
/*      */     } else {
/*      */       
/*  740 */       edge.nextInSEL = this.sortedEdges;
/*  741 */       edge.prevInSEL = null;
/*  742 */       this.sortedEdges.prevInSEL = edge;
/*  743 */       this.sortedEdges = edge;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void addGhostJoin(Path.OutPt Op, Point.LongPoint OffPt) {
/*  748 */     Path.Join j = new Path.Join();
/*  749 */     j.outPt1 = Op;
/*  750 */     j.setOffPt(OffPt);
/*  751 */     this.ghostJoins.add(j);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addJoin(Path.OutPt Op1, Path.OutPt Op2, Point.LongPoint OffPt) {
/*  757 */     LOGGER.entering(DefaultClipper.class.getName(), "addJoin");
/*  758 */     Path.Join j = new Path.Join();
/*  759 */     j.outPt1 = Op1;
/*  760 */     j.outPt2 = Op2;
/*  761 */     j.setOffPt(OffPt);
/*  762 */     this.joins.add(j);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addLocalMaxPoly(Edge e1, Edge e2, Point.LongPoint pt) {
/*  768 */     addOutPt(e1, pt);
/*  769 */     if (e2.windDelta == 0) {
/*  770 */       addOutPt(e2, pt);
/*      */     }
/*  772 */     if (e1.outIdx == e2.outIdx) {
/*  773 */       e1.outIdx = -1;
/*  774 */       e2.outIdx = -1;
/*      */     }
/*  776 */     else if (e1.outIdx < e2.outIdx) {
/*  777 */       appendPolygon(e1, e2);
/*      */     } else {
/*      */       
/*  780 */       appendPolygon(e2, e1);
/*      */     } 
/*      */   }
/*      */   
/*      */   private Path.OutPt addLocalMinPoly(Edge e1, Edge e2, Point.LongPoint pt) {
/*      */     Path.OutPt result;
/*      */     Edge e, prevE;
/*  787 */     LOGGER.entering(DefaultClipper.class.getName(), "addLocalMinPoly");
/*      */ 
/*      */     
/*  790 */     if (e2.isHorizontal() || e1.deltaX > e2.deltaX) {
/*  791 */       result = addOutPt(e1, pt);
/*  792 */       e2.outIdx = e1.outIdx;
/*  793 */       e1.side = Edge.Side.LEFT;
/*  794 */       e2.side = Edge.Side.RIGHT;
/*  795 */       e = e1;
/*  796 */       if (e.prevInAEL == e2) {
/*  797 */         prevE = e2.prevInAEL;
/*      */       } else {
/*      */         
/*  800 */         prevE = e.prevInAEL;
/*      */       } 
/*      */     } else {
/*      */       
/*  804 */       result = addOutPt(e2, pt);
/*  805 */       e1.outIdx = e2.outIdx;
/*  806 */       e1.side = Edge.Side.RIGHT;
/*  807 */       e2.side = Edge.Side.LEFT;
/*  808 */       e = e2;
/*  809 */       if (e.prevInAEL == e1) {
/*  810 */         prevE = e1.prevInAEL;
/*      */       } else {
/*      */         
/*  813 */         prevE = e.prevInAEL;
/*      */       } 
/*      */     } 
/*      */     
/*  817 */     if (prevE != null && prevE.outIdx >= 0 && 
/*  818 */       Edge.topX(prevE, pt.getY()) == Edge.topX(e, pt.getY()) && 
/*  819 */       Edge.slopesEqual(e, prevE, this.useFullRange) && e.windDelta != 0 && prevE.windDelta != 0) {
/*      */       
/*  821 */       Path.OutPt outPt = addOutPt(prevE, pt);
/*  822 */       addJoin(result, outPt, e.getTop());
/*      */     } 
/*  824 */     return result;
/*      */   }
/*      */   
/*      */   private Path.OutPt addOutPt(Edge e, Point.LongPoint pt) {
/*  828 */     LOGGER.entering(DefaultClipper.class.getName(), "addOutPt");
/*  829 */     if (e.outIdx < 0) {
/*      */       
/*  831 */       Path.OutRec outRec1 = createOutRec();
/*  832 */       outRec1.isOpen = (e.windDelta == 0);
/*  833 */       Path.OutPt outPt = new Path.OutPt();
/*  834 */       outRec1.pts = outPt;
/*  835 */       outPt.idx = outRec1.Idx;
/*  836 */       outPt.pt = pt;
/*  837 */       outPt.next = outPt;
/*  838 */       outPt.prev = outPt;
/*  839 */       if (!outRec1.isOpen)
/*  840 */         setHoleState(e, outRec1); 
/*  841 */       e.outIdx = outRec1.Idx;
/*  842 */       return outPt;
/*      */     } 
/*      */     
/*  845 */     Path.OutRec outRec = this.polyOuts.get(e.outIdx);
/*      */     
/*  847 */     Path.OutPt op = outRec.getPoints();
/*  848 */     boolean ToFront = (e.side == Edge.Side.LEFT);
/*  849 */     LOGGER.finest("op=" + op.getPointCount());
/*  850 */     LOGGER.finest(ToFront + " " + pt + " " + op.getPt());
/*  851 */     if (ToFront && pt.equals(op.getPt())) {
/*  852 */       return op;
/*      */     }
/*  854 */     if (!ToFront && pt.equals(op.prev.getPt())) {
/*  855 */       return op.prev;
/*      */     }
/*      */     
/*  858 */     Path.OutPt newOp = new Path.OutPt();
/*  859 */     newOp.idx = outRec.Idx;
/*  860 */     newOp.setPt(new Point.LongPoint(pt));
/*  861 */     newOp.next = op;
/*  862 */     newOp.prev = op.prev;
/*  863 */     newOp.prev.next = newOp;
/*  864 */     op.prev = newOp;
/*  865 */     if (ToFront) {
/*  866 */       outRec.setPoints(newOp);
/*      */     }
/*  868 */     return newOp;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Path.OutPt GetLastOutPt(Edge e) {
/*  874 */     Path.OutRec outRec = this.polyOuts.get(e.outIdx);
/*  875 */     if (e.side == Edge.Side.LEFT) {
/*  876 */       return outRec.pts;
/*      */     }
/*  878 */     return outRec.pts.prev;
/*      */   }
/*      */   private void appendPolygon(Edge e1, Edge e2) {
/*      */     Path.OutRec holeStateRec;
/*      */     Edge.Side side;
/*  883 */     LOGGER.entering(DefaultClipper.class.getName(), "appendPolygon");
/*      */ 
/*      */     
/*  886 */     Path.OutRec outRec1 = this.polyOuts.get(e1.outIdx);
/*  887 */     Path.OutRec outRec2 = this.polyOuts.get(e2.outIdx);
/*  888 */     LOGGER.finest("" + e1.outIdx);
/*  889 */     LOGGER.finest("" + e2.outIdx);
/*      */ 
/*      */     
/*  892 */     if (isParam1RightOfParam2(outRec1, outRec2)) {
/*  893 */       holeStateRec = outRec2;
/*      */     }
/*  895 */     else if (isParam1RightOfParam2(outRec2, outRec1)) {
/*  896 */       holeStateRec = outRec1;
/*      */     } else {
/*      */       
/*  899 */       holeStateRec = Path.OutPt.getLowerMostRec(outRec1, outRec2);
/*      */     } 
/*      */     
/*  902 */     Path.OutPt p1_lft = outRec1.getPoints();
/*  903 */     Path.OutPt p1_rt = p1_lft.prev;
/*  904 */     Path.OutPt p2_lft = outRec2.getPoints();
/*  905 */     Path.OutPt p2_rt = p2_lft.prev;
/*      */     
/*  907 */     LOGGER.finest("p1_lft.getPointCount() = " + p1_lft.getPointCount());
/*  908 */     LOGGER.finest("p1_rt.getPointCount() = " + p1_rt.getPointCount());
/*  909 */     LOGGER.finest("p2_lft.getPointCount() = " + p2_lft.getPointCount());
/*  910 */     LOGGER.finest("p2_rt.getPointCount() = " + p2_rt.getPointCount());
/*      */ 
/*      */ 
/*      */     
/*  914 */     if (e1.side == Edge.Side.LEFT) {
/*  915 */       if (e2.side == Edge.Side.LEFT) {
/*      */         
/*  917 */         p2_lft.reversePolyPtLinks();
/*  918 */         p2_lft.next = p1_lft;
/*  919 */         p1_lft.prev = p2_lft;
/*  920 */         p1_rt.next = p2_rt;
/*  921 */         p2_rt.prev = p1_rt;
/*  922 */         outRec1.setPoints(p2_rt);
/*      */       }
/*      */       else {
/*      */         
/*  926 */         p2_rt.next = p1_lft;
/*  927 */         p1_lft.prev = p2_rt;
/*  928 */         p2_lft.prev = p1_rt;
/*  929 */         p1_rt.next = p2_lft;
/*  930 */         outRec1.setPoints(p2_lft);
/*      */       } 
/*  932 */       side = Edge.Side.LEFT;
/*      */     } else {
/*      */       
/*  935 */       if (e2.side == Edge.Side.RIGHT) {
/*      */         
/*  937 */         p2_lft.reversePolyPtLinks();
/*  938 */         p1_rt.next = p2_rt;
/*  939 */         p2_rt.prev = p1_rt;
/*  940 */         p2_lft.next = p1_lft;
/*  941 */         p1_lft.prev = p2_lft;
/*      */       }
/*      */       else {
/*      */         
/*  945 */         p1_rt.next = p2_lft;
/*  946 */         p2_lft.prev = p1_rt;
/*  947 */         p1_lft.prev = p2_rt;
/*  948 */         p2_rt.next = p1_lft;
/*      */       } 
/*  950 */       side = Edge.Side.RIGHT;
/*      */     } 
/*  952 */     outRec1.bottomPt = null;
/*  953 */     if (holeStateRec.equals(outRec2)) {
/*  954 */       if (outRec2.firstLeft != outRec1) {
/*  955 */         outRec1.firstLeft = outRec2.firstLeft;
/*      */       }
/*  957 */       outRec1.isHole = outRec2.isHole;
/*      */     } 
/*  959 */     outRec2.setPoints(null);
/*  960 */     outRec2.bottomPt = null;
/*      */     
/*  962 */     outRec2.firstLeft = outRec1;
/*      */     
/*  964 */     int OKIdx = e1.outIdx;
/*  965 */     int ObsoleteIdx = e2.outIdx;
/*      */     
/*  967 */     e1.outIdx = -1;
/*  968 */     e2.outIdx = -1;
/*      */     
/*  970 */     Edge e = this.activeEdges;
/*  971 */     while (e != null) {
/*  972 */       if (e.outIdx == ObsoleteIdx) {
/*  973 */         e.outIdx = OKIdx;
/*  974 */         e.side = side;
/*      */         break;
/*      */       } 
/*  977 */       e = e.nextInAEL;
/*      */     } 
/*  979 */     outRec2.Idx = outRec1.Idx;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void buildIntersectList(long topY) {
/*  985 */     if (this.activeEdges == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  990 */     Edge e = this.activeEdges;
/*  991 */     this.sortedEdges = e;
/*  992 */     while (e != null) {
/*  993 */       e.prevInSEL = e.prevInAEL;
/*  994 */       e.nextInSEL = e.nextInAEL;
/*  995 */       e.getCurrent().setX(Long.valueOf(Edge.topX(e, topY)));
/*  996 */       e = e.nextInAEL;
/*      */     } 
/*      */ 
/*      */     
/* 1000 */     boolean isModified = true;
/* 1001 */     while (isModified && this.sortedEdges != null) {
/* 1002 */       isModified = false;
/* 1003 */       e = this.sortedEdges;
/* 1004 */       while (e.nextInSEL != null) {
/* 1005 */         Edge eNext = e.nextInSEL;
/* 1006 */         Point.LongPoint[] pt = new Point.LongPoint[1];
/* 1007 */         if (e.getCurrent().getX() > eNext.getCurrent().getX()) {
/* 1008 */           intersectPoint(e, eNext, pt);
/* 1009 */           IntersectNode newNode = new IntersectNode();
/* 1010 */           newNode.edge1 = e;
/* 1011 */           newNode.Edge2 = eNext;
/* 1012 */           newNode.setPt(pt[0]);
/* 1013 */           this.intersectList.add(newNode);
/*      */           
/* 1015 */           swapPositionsInSEL(e, eNext);
/* 1016 */           isModified = true;
/*      */           continue;
/*      */         } 
/* 1019 */         e = eNext;
/*      */       } 
/*      */       
/* 1022 */       if (e.prevInSEL != null) {
/* 1023 */         e.prevInSEL.nextInSEL = null;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1029 */     this.sortedEdges = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void buildResult(Paths polyg) {
/* 1035 */     polyg.clear();
/* 1036 */     for (int i = 0; i < this.polyOuts.size(); i++) {
/* 1037 */       Path.OutRec outRec = this.polyOuts.get(i);
/* 1038 */       if (outRec.getPoints() != null) {
/*      */ 
/*      */         
/* 1041 */         Path.OutPt p = (outRec.getPoints()).prev;
/* 1042 */         int cnt = p.getPointCount();
/* 1043 */         LOGGER.finest("cnt = " + cnt);
/* 1044 */         if (cnt >= 2) {
/*      */ 
/*      */           
/* 1047 */           Path pg = new Path(cnt);
/* 1048 */           for (int j = 0; j < cnt; j++) {
/* 1049 */             pg.add(p.getPt());
/* 1050 */             p = p.prev;
/*      */           } 
/* 1052 */           polyg.add(pg);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   } private void buildResult2(PolyTree polytree) {
/* 1057 */     polytree.Clear();
/*      */     
/*      */     int i;
/* 1060 */     for (i = 0; i < this.polyOuts.size(); i++) {
/* 1061 */       Path.OutRec outRec = this.polyOuts.get(i);
/* 1062 */       int cnt = (outRec.getPoints() != null) ? outRec.getPoints().getPointCount() : 0;
/* 1063 */       if ((!outRec.isOpen || cnt >= 2) && (outRec.isOpen || cnt >= 3)) {
/*      */ 
/*      */         
/* 1066 */         outRec.fixHoleLinkage();
/* 1067 */         PolyNode pn = new PolyNode();
/* 1068 */         polytree.getAllPolys().add(pn);
/* 1069 */         outRec.polyNode = pn;
/* 1070 */         Path.OutPt op = (outRec.getPoints()).prev;
/* 1071 */         for (int j = 0; j < cnt; j++) {
/* 1072 */           pn.getPolygon().add(op.getPt());
/* 1073 */           op = op.prev;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1078 */     for (i = 0; i < this.polyOuts.size(); i++) {
/* 1079 */       Path.OutRec outRec = this.polyOuts.get(i);
/* 1080 */       if (outRec.polyNode != null)
/*      */       {
/*      */         
/* 1083 */         if (outRec.isOpen) {
/* 1084 */           outRec.polyNode.setOpen(true);
/* 1085 */           polytree.addChild(outRec.polyNode);
/*      */         }
/* 1087 */         else if (outRec.firstLeft != null && outRec.firstLeft.polyNode != null) {
/* 1088 */           outRec.firstLeft.polyNode.addChild(outRec.polyNode);
/*      */         } else {
/*      */           
/* 1091 */           polytree.addChild(outRec.polyNode);
/*      */         }  } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void copyAELToSEL() {
/* 1097 */     Edge e = this.activeEdges;
/* 1098 */     this.sortedEdges = e;
/* 1099 */     while (e != null) {
/* 1100 */       e.prevInSEL = e.prevInAEL;
/* 1101 */       e.nextInSEL = e.nextInAEL;
/* 1102 */       e = e.nextInAEL;
/*      */     } 
/*      */   }
/*      */   
/*      */   private Path.OutRec createOutRec() {
/* 1107 */     Path.OutRec result = new Path.OutRec();
/* 1108 */     result.Idx = -1;
/* 1109 */     result.isHole = false;
/* 1110 */     result.isOpen = false;
/* 1111 */     result.firstLeft = null;
/* 1112 */     result.setPoints(null);
/* 1113 */     result.bottomPt = null;
/* 1114 */     result.polyNode = null;
/* 1115 */     this.polyOuts.add(result);
/* 1116 */     result.Idx = this.polyOuts.size() - 1;
/* 1117 */     return result;
/*      */   }
/*      */   
/*      */   private void deleteFromAEL(Edge e) {
/* 1121 */     LOGGER.entering(DefaultClipper.class.getName(), "deleteFromAEL");
/*      */     
/* 1123 */     Edge AelPrev = e.prevInAEL;
/* 1124 */     Edge AelNext = e.nextInAEL;
/* 1125 */     if (AelPrev == null && AelNext == null && e != this.activeEdges) {
/*      */       return;
/*      */     }
/* 1128 */     if (AelPrev != null) {
/* 1129 */       AelPrev.nextInAEL = AelNext;
/*      */     } else {
/*      */       
/* 1132 */       this.activeEdges = AelNext;
/*      */     } 
/* 1134 */     if (AelNext != null) {
/* 1135 */       AelNext.prevInAEL = AelPrev;
/*      */     }
/* 1137 */     e.nextInAEL = null;
/* 1138 */     e.prevInAEL = null;
/* 1139 */     LOGGER.exiting(DefaultClipper.class.getName(), "deleteFromAEL");
/*      */   }
/*      */   
/*      */   private void deleteFromSEL(Edge e) {
/* 1143 */     LOGGER.entering(DefaultClipper.class.getName(), "deleteFromSEL");
/*      */     
/* 1145 */     Edge SelPrev = e.prevInSEL;
/* 1146 */     Edge SelNext = e.nextInSEL;
/* 1147 */     if (SelPrev == null && SelNext == null && !e.equals(this.sortedEdges)) {
/*      */       return;
/*      */     }
/* 1150 */     if (SelPrev != null) {
/* 1151 */       SelPrev.nextInSEL = SelNext;
/*      */     } else {
/*      */       
/* 1154 */       this.sortedEdges = SelNext;
/*      */     } 
/* 1156 */     if (SelNext != null) {
/* 1157 */       SelNext.prevInSEL = SelPrev;
/*      */     }
/* 1159 */     e.nextInSEL = null;
/* 1160 */     e.prevInSEL = null;
/*      */   }
/*      */   
/*      */   private boolean doHorzSegmentsOverlap(long seg1a, long seg1b, long seg2a, long seg2b) {
/* 1164 */     if (seg1a > seg1b) {
/* 1165 */       long tmp = seg1a;
/* 1166 */       seg1a = seg1b;
/* 1167 */       seg1b = tmp;
/*      */     } 
/* 1169 */     if (seg2a > seg2b) {
/* 1170 */       long tmp = seg2a;
/* 1171 */       seg2a = seg2b;
/* 1172 */       seg2b = tmp;
/*      */     } 
/* 1174 */     return (seg1a < seg2b && seg2a < seg1b);
/*      */   }
/*      */   
/*      */   private void doMaxima(Edge e) {
/* 1178 */     Edge eMaxPair = e.getMaximaPair();
/* 1179 */     if (eMaxPair == null) {
/* 1180 */       if (e.outIdx >= 0) {
/* 1181 */         addOutPt(e, e.getTop());
/*      */       }
/* 1183 */       deleteFromAEL(e);
/*      */       
/*      */       return;
/*      */     } 
/* 1187 */     Edge eNext = e.nextInAEL;
/* 1188 */     while (eNext != null && eNext != eMaxPair) {
/* 1189 */       Point.LongPoint tmp = new Point.LongPoint(e.getTop());
/* 1190 */       intersectEdges(e, eNext, tmp);
/* 1191 */       e.setTop(tmp);
/* 1192 */       swapPositionsInAEL(e, eNext);
/* 1193 */       eNext = e.nextInAEL;
/*      */     } 
/*      */     
/* 1196 */     if (e.outIdx == -1 && eMaxPair.outIdx == -1) {
/* 1197 */       deleteFromAEL(e);
/* 1198 */       deleteFromAEL(eMaxPair);
/*      */     }
/* 1200 */     else if (e.outIdx >= 0 && eMaxPair.outIdx >= 0) {
/* 1201 */       if (e.outIdx >= 0) {
/* 1202 */         addLocalMaxPoly(e, eMaxPair, e.getTop());
/*      */       }
/* 1204 */       deleteFromAEL(e);
/* 1205 */       deleteFromAEL(eMaxPair);
/*      */     
/*      */     }
/* 1208 */     else if (e.windDelta == 0) {
/* 1209 */       if (e.outIdx >= 0) {
/* 1210 */         addOutPt(e, e.getTop());
/* 1211 */         e.outIdx = -1;
/*      */       } 
/* 1213 */       deleteFromAEL(e);
/*      */       
/* 1215 */       if (eMaxPair.outIdx >= 0) {
/* 1216 */         addOutPt(eMaxPair, e.getTop());
/* 1217 */         eMaxPair.outIdx = -1;
/*      */       } 
/* 1219 */       deleteFromAEL(eMaxPair);
/*      */     } else {
/*      */       
/* 1222 */       throw new IllegalStateException("DoMaxima error");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void doSimplePolygons() {
/* 1229 */     int i = 0;
/* 1230 */     label43: while (i < this.polyOuts.size()) {
/* 1231 */       Path.OutRec outrec = this.polyOuts.get(i++);
/* 1232 */       Path.OutPt op = outrec.getPoints();
/* 1233 */       if (op == null || outrec.isOpen) {
/*      */         continue;
/*      */       }
/*      */       
/*      */       while (true) {
/* 1238 */         Path.OutPt op2 = op.next;
/* 1239 */         while (op2 != outrec.getPoints()) {
/* 1240 */           if (op.getPt().equals(op2.getPt()) && !op2.next.equals(op) && !op2.prev.equals(op)) {
/*      */             
/* 1242 */             Path.OutPt op3 = op.prev;
/* 1243 */             Path.OutPt op4 = op2.prev;
/* 1244 */             op.prev = op4;
/* 1245 */             op4.next = op;
/* 1246 */             op2.prev = op3;
/* 1247 */             op3.next = op2;
/*      */             
/* 1249 */             outrec.setPoints(op);
/* 1250 */             Path.OutRec outrec2 = createOutRec();
/* 1251 */             outrec2.setPoints(op2);
/* 1252 */             updateOutPtIdxs(outrec2);
/* 1253 */             if (poly2ContainsPoly1(outrec2.getPoints(), outrec.getPoints())) {
/*      */               
/* 1255 */               outrec2.isHole = !outrec.isHole;
/* 1256 */               outrec2.firstLeft = outrec;
/* 1257 */               if (this.usingPolyTree) {
/* 1258 */                 fixupFirstLefts2(outrec2, outrec);
/*      */               }
/*      */             }
/* 1261 */             else if (poly2ContainsPoly1(outrec.getPoints(), outrec2.getPoints())) {
/*      */               
/* 1263 */               outrec2.isHole = outrec.isHole;
/* 1264 */               outrec.isHole = !outrec2.isHole;
/* 1265 */               outrec2.firstLeft = outrec.firstLeft;
/* 1266 */               outrec.firstLeft = outrec2;
/* 1267 */               if (this.usingPolyTree) {
/* 1268 */                 fixupFirstLefts2(outrec, outrec2);
/*      */               }
/*      */             }
/*      */             else {
/*      */               
/* 1273 */               outrec2.isHole = outrec.isHole;
/* 1274 */               outrec2.firstLeft = outrec.firstLeft;
/* 1275 */               if (this.usingPolyTree) {
/* 1276 */                 fixupFirstLefts1(outrec, outrec2);
/*      */               }
/*      */             } 
/* 1279 */             op2 = op;
/*      */           } 
/* 1281 */           op2 = op2.next;
/*      */         } 
/* 1283 */         op = op.next;
/*      */         
/* 1285 */         if (op == outrec.getPoints())
/*      */           continue label43; 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean EdgesAdjacent(IntersectNode inode) {
/* 1292 */     return (inode.edge1.nextInSEL == inode.Edge2 || inode.edge1.prevInSEL == inode.Edge2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean execute(Clipper.ClipType clipType, Paths solution, Clipper.PolyFillType FillType) {
/* 1301 */     return execute(clipType, solution, FillType, FillType);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean execute(Clipper.ClipType clipType, PolyTree polytree) {
/* 1306 */     return execute(clipType, polytree, Clipper.PolyFillType.EVEN_ODD);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean execute(Clipper.ClipType clipType, PolyTree polytree, Clipper.PolyFillType FillType) {
/* 1312 */     return execute(clipType, polytree, FillType, FillType);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean execute(Clipper.ClipType clipType, Paths solution) {
/* 1317 */     return execute(clipType, solution, Clipper.PolyFillType.EVEN_ODD);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean execute(Clipper.ClipType clipType, Paths solution, Clipper.PolyFillType subjFillType, Clipper.PolyFillType clipFillType) {
/* 1322 */     synchronized (this) {
/*      */       
/* 1324 */       if (this.hasOpenPaths) {
/* 1325 */         throw new IllegalStateException("Error: PolyTree struct is needed for open path clipping.");
/*      */       }
/*      */       
/* 1328 */       solution.clear();
/* 1329 */       this.subjFillType = subjFillType;
/* 1330 */       this.clipFillType = clipFillType;
/* 1331 */       this.clipType = clipType;
/* 1332 */       this.usingPolyTree = false;
/*      */       
/*      */       try {
/* 1335 */         boolean succeeded = executeInternal();
/*      */         
/* 1337 */         if (succeeded) {
/* 1338 */           buildResult(solution);
/*      */         }
/* 1340 */         return succeeded;
/*      */       } finally {
/*      */         
/* 1343 */         this.polyOuts.clear();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean execute(Clipper.ClipType clipType, PolyTree polytree, Clipper.PolyFillType subjFillType, Clipper.PolyFillType clipFillType) {
/* 1351 */     synchronized (this) {
/* 1352 */       boolean succeeded; this.subjFillType = subjFillType;
/* 1353 */       this.clipFillType = clipFillType;
/* 1354 */       this.clipType = clipType;
/* 1355 */       this.usingPolyTree = true;
/*      */       
/*      */       try {
/* 1358 */         succeeded = executeInternal();
/*      */         
/* 1360 */         if (succeeded) {
/* 1361 */           buildResult2(polytree);
/*      */         }
/*      */       } finally {
/*      */         
/* 1365 */         this.polyOuts.clear();
/*      */       } 
/* 1367 */       return succeeded;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean executeInternal() {
/*      */     try {
/* 1375 */       reset();
/* 1376 */       if (this.currentLM == null) {
/* 1377 */         return false;
/*      */       }
/* 1379 */       long botY = popScanbeam();
/*      */       do {
/* 1381 */         insertLocalMinimaIntoAEL(botY);
/* 1382 */         processHorizontals();
/* 1383 */         this.ghostJoins.clear();
/* 1384 */         if (this.scanbeam == null)
/*      */           break; 
/* 1386 */         long topY = popScanbeam();
/* 1387 */         if (!processIntersections(topY))
/* 1388 */           return false; 
/* 1389 */         processEdgesAtTopOfScanbeam(topY);
/* 1390 */         botY = topY;
/* 1391 */       } while (this.scanbeam != null || this.currentLM != null);
/*      */       
/*      */       int i;
/* 1394 */       for (i = 0; i < this.polyOuts.size(); i++) {
/* 1395 */         Path.OutRec outRec = this.polyOuts.get(i);
/* 1396 */         if (outRec.pts != null && !outRec.isOpen)
/*      */         {
/* 1398 */           if ((outRec.isHole ^ this.reverseSolution) == ((outRec.area() > 0.0D) ? 1 : 0))
/* 1399 */             outRec.getPoints().reversePolyPtLinks(); 
/*      */         }
/*      */       } 
/* 1402 */       joinCommonEdges();
/*      */       
/* 1404 */       for (i = 0; i < this.polyOuts.size(); i++) {
/* 1405 */         Path.OutRec outRec = this.polyOuts.get(i);
/* 1406 */         if (outRec.getPoints() != null)
/*      */         {
/* 1408 */           if (outRec.isOpen) {
/* 1409 */             fixupOutPolyline(outRec);
/*      */           } else {
/* 1411 */             fixupOutPolygon(outRec);
/*      */           }  } 
/*      */       } 
/* 1414 */       if (this.strictlySimple)
/* 1415 */         doSimplePolygons(); 
/* 1416 */       i = 1; return i;
/*      */     }
/*      */     finally {
/*      */       
/* 1420 */       this.joins.clear();
/* 1421 */       this.ghostJoins.clear();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void fixupFirstLefts1(Path.OutRec OldOutRec, Path.OutRec NewOutRec) {
/* 1428 */     for (int i = 0; i < this.polyOuts.size(); i++) {
/* 1429 */       Path.OutRec outRec = this.polyOuts.get(i);
/* 1430 */       if (outRec.getPoints() != null && outRec.firstLeft != null) {
/*      */ 
/*      */         
/* 1433 */         Path.OutRec firstLeft = parseFirstLeft(outRec.firstLeft);
/* 1434 */         if (firstLeft.equals(OldOutRec) && 
/* 1435 */           poly2ContainsPoly1(outRec.getPoints(), NewOutRec.getPoints())) {
/* 1436 */           outRec.firstLeft = NewOutRec;
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void fixupFirstLefts2(Path.OutRec OldOutRec, Path.OutRec NewOutRec) {
/* 1443 */     for (Path.OutRec outRec : this.polyOuts) {
/* 1444 */       if (outRec.firstLeft == OldOutRec) {
/* 1445 */         outRec.firstLeft = NewOutRec;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean fixupIntersectionOrder() {
/* 1454 */     Collections.sort(this.intersectList, this.intersectNodeComparer);
/*      */     
/* 1456 */     copyAELToSEL();
/* 1457 */     int cnt = this.intersectList.size();
/* 1458 */     for (int i = 0; i < cnt; i++) {
/* 1459 */       if (!EdgesAdjacent(this.intersectList.get(i))) {
/* 1460 */         int j = i + 1;
/* 1461 */         while (j < cnt && !EdgesAdjacent(this.intersectList.get(j))) {
/* 1462 */           j++;
/*      */         }
/* 1464 */         if (j == cnt) {
/* 1465 */           return false;
/*      */         }
/*      */         
/* 1468 */         IntersectNode tmp = this.intersectList.get(i);
/* 1469 */         this.intersectList.set(i, this.intersectList.get(j));
/* 1470 */         this.intersectList.set(j, tmp);
/*      */       } 
/*      */       
/* 1473 */       swapPositionsInSEL(((IntersectNode)this.intersectList.get(i)).edge1, ((IntersectNode)this.intersectList.get(i)).Edge2);
/*      */     } 
/* 1475 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fixupOutPolyline(Path.OutRec outrec) {
/* 1482 */     Path.OutPt pp = outrec.pts;
/* 1483 */     Path.OutPt lastPP = pp.prev;
/* 1484 */     while (pp != lastPP) {
/*      */       
/* 1486 */       pp = pp.next;
/* 1487 */       if (pp.pt.equals(pp.prev.pt)) {
/*      */         
/* 1489 */         if (pp == lastPP) lastPP = pp.prev; 
/* 1490 */         Path.OutPt tmpPP = pp.prev;
/* 1491 */         tmpPP.next = pp.next;
/* 1492 */         pp.next.prev = tmpPP;
/* 1493 */         pp = tmpPP;
/*      */       } 
/*      */     } 
/* 1496 */     if (pp == pp.prev) outrec.pts = null;
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   private void fixupOutPolygon(Path.OutRec outRec) {
/* 1502 */     Path.OutPt lastOK = null;
/* 1503 */     outRec.bottomPt = null;
/* 1504 */     Path.OutPt pp = outRec.getPoints();
/* 1505 */     boolean preserveCol = (this.preserveCollinear || this.strictlySimple);
/*      */     while (true) {
/* 1507 */       if (pp.prev == pp || pp.prev == pp.next) {
/* 1508 */         outRec.setPoints(null);
/*      */         
/*      */         return;
/*      */       } 
/* 1512 */       if (pp.getPt().equals(pp.next.getPt()) || pp.getPt().equals(pp.prev.getPt()) || (
/* 1513 */         Point.slopesEqual(pp.prev.getPt(), pp.getPt(), pp.next.getPt(), this.useFullRange) && (!preserveCol || 
/* 1514 */         !Point.isPt2BetweenPt1AndPt3(pp.prev.getPt(), pp.getPt(), pp.next.getPt())))) {
/* 1515 */         lastOK = null;
/* 1516 */         pp.prev.next = pp.next;
/* 1517 */         pp.next.prev = pp.prev;
/* 1518 */         pp = pp.prev; continue;
/*      */       } 
/* 1520 */       if (pp == lastOK) {
/*      */         break;
/*      */       }
/*      */       
/* 1524 */       if (lastOK == null) {
/* 1525 */         lastOK = pp;
/*      */       }
/* 1527 */       pp = pp.next;
/*      */     } 
/*      */     
/* 1530 */     outRec.setPoints(pp);
/*      */   }
/*      */   
/*      */   private Path.OutRec getOutRec(int idx) {
/* 1534 */     Path.OutRec outrec = this.polyOuts.get(idx);
/* 1535 */     while (outrec != this.polyOuts.get(outrec.Idx)) {
/* 1536 */       outrec = this.polyOuts.get(outrec.Idx);
/*      */     }
/* 1538 */     return outrec;
/*      */   }
/*      */   
/*      */   private void insertEdgeIntoAEL(Edge edge, Edge startEdge) {
/* 1542 */     LOGGER.entering(DefaultClipper.class.getName(), "insertEdgeIntoAEL");
/*      */     
/* 1544 */     if (this.activeEdges == null) {
/* 1545 */       edge.prevInAEL = null;
/* 1546 */       edge.nextInAEL = null;
/* 1547 */       LOGGER.finest("Edge " + edge.outIdx + " -> " + null);
/* 1548 */       this.activeEdges = edge;
/*      */     }
/* 1550 */     else if (startEdge == null && Edge.doesE2InsertBeforeE1(this.activeEdges, edge)) {
/* 1551 */       edge.prevInAEL = null;
/* 1552 */       edge.nextInAEL = this.activeEdges;
/* 1553 */       LOGGER.finest("Edge " + edge.outIdx + " -> " + edge.nextInAEL.outIdx);
/* 1554 */       this.activeEdges.prevInAEL = edge;
/* 1555 */       this.activeEdges = edge;
/*      */     } else {
/*      */       
/* 1558 */       LOGGER.finest("activeEdges unchanged");
/* 1559 */       if (startEdge == null) {
/* 1560 */         startEdge = this.activeEdges;
/*      */       }
/* 1562 */       while (startEdge.nextInAEL != null && 
/* 1563 */         !Edge.doesE2InsertBeforeE1(startEdge.nextInAEL, edge)) {
/* 1564 */         startEdge = startEdge.nextInAEL;
/*      */       }
/* 1566 */       edge.nextInAEL = startEdge.nextInAEL;
/* 1567 */       if (startEdge.nextInAEL != null) {
/* 1568 */         startEdge.nextInAEL.prevInAEL = edge;
/*      */       }
/* 1570 */       edge.prevInAEL = startEdge;
/* 1571 */       startEdge.nextInAEL = edge;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void insertLocalMinimaIntoAEL(long botY) {
/* 1578 */     LOGGER.entering(DefaultClipper.class.getName(), "insertLocalMinimaIntoAEL");
/*      */     
/* 1580 */     while (this.currentLM != null && this.currentLM.y == botY) {
/* 1581 */       Edge lb = this.currentLM.leftBound;
/* 1582 */       Edge rb = this.currentLM.rightBound;
/* 1583 */       popLocalMinima();
/*      */       
/* 1585 */       Path.OutPt Op1 = null;
/* 1586 */       if (lb == null) {
/* 1587 */         insertEdgeIntoAEL(rb, (Edge)null);
/* 1588 */         updateWindingCount(rb);
/* 1589 */         if (rb.isContributing(this.clipFillType, this.subjFillType, this.clipType)) {
/* 1590 */           Op1 = addOutPt(rb, rb.getBot());
/*      */         }
/*      */       }
/* 1593 */       else if (rb == null) {
/* 1594 */         insertEdgeIntoAEL(lb, (Edge)null);
/* 1595 */         updateWindingCount(lb);
/* 1596 */         if (lb.isContributing(this.clipFillType, this.subjFillType, this.clipType)) {
/* 1597 */           Op1 = addOutPt(lb, lb.getBot());
/*      */         }
/* 1599 */         insertScanbeam(lb.getTop().getY());
/*      */       } else {
/*      */         
/* 1602 */         insertEdgeIntoAEL(lb, (Edge)null);
/* 1603 */         insertEdgeIntoAEL(rb, lb);
/* 1604 */         updateWindingCount(lb);
/* 1605 */         rb.windCnt = lb.windCnt;
/* 1606 */         rb.windCnt2 = lb.windCnt2;
/* 1607 */         if (lb.isContributing(this.clipFillType, this.subjFillType, this.clipType)) {
/* 1608 */           Op1 = addLocalMinPoly(lb, rb, lb.getBot());
/*      */         }
/* 1610 */         insertScanbeam(lb.getTop().getY());
/*      */       } 
/*      */       
/* 1613 */       if (rb != null) {
/* 1614 */         if (rb.isHorizontal()) {
/* 1615 */           addEdgeToSEL(rb);
/*      */         } else {
/*      */           
/* 1618 */           insertScanbeam(rb.getTop().getY());
/*      */         } 
/*      */       }
/*      */       
/* 1622 */       if (lb == null || rb == null) {
/*      */         continue;
/*      */       }
/*      */ 
/*      */       
/* 1627 */       if (Op1 != null && rb.isHorizontal() && this.ghostJoins
/* 1628 */         .size() > 0 && rb.windDelta != 0) {
/* 1629 */         for (int i = 0; i < this.ghostJoins.size(); i++) {
/*      */ 
/*      */           
/* 1632 */           Path.Join j = this.ghostJoins.get(i);
/* 1633 */           if (doHorzSegmentsOverlap(j.outPt1.getPt().getX(), j.getOffPt().getX(), rb.getBot().getX(), rb.getTop().getX())) {
/* 1634 */             addJoin(j.outPt1, Op1, j.getOffPt());
/*      */           }
/*      */         } 
/*      */       }
/*      */       
/* 1639 */       if (lb.outIdx >= 0 && lb.prevInAEL != null && lb.prevInAEL
/* 1640 */         .getCurrent().getX() == lb.getBot().getX() && lb.prevInAEL.outIdx >= 0 && 
/*      */         
/* 1642 */         Edge.slopesEqual(lb.prevInAEL, lb, this.useFullRange) && lb.windDelta != 0 && lb.prevInAEL.windDelta != 0) {
/*      */         
/* 1644 */         Path.OutPt Op2 = addOutPt(lb.prevInAEL, lb.getBot());
/* 1645 */         addJoin(Op1, Op2, lb.getTop());
/*      */       } 
/*      */       
/* 1648 */       if (lb.nextInAEL != rb) {
/*      */         
/* 1650 */         if (rb.outIdx >= 0 && rb.prevInAEL.outIdx >= 0 && 
/* 1651 */           Edge.slopesEqual(rb.prevInAEL, rb, this.useFullRange) && rb.windDelta != 0 && rb.prevInAEL.windDelta != 0) {
/*      */           
/* 1653 */           Path.OutPt Op2 = addOutPt(rb.prevInAEL, rb.getBot());
/* 1654 */           addJoin(Op1, Op2, rb.getTop());
/*      */         } 
/*      */         
/* 1657 */         Edge e = lb.nextInAEL;
/* 1658 */         if (e != null) {
/* 1659 */           while (e != rb) {
/*      */ 
/*      */             
/* 1662 */             intersectEdges(rb, e, lb.getCurrent());
/* 1663 */             e = e.nextInAEL;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void intersectEdges(Edge e1, Edge e2, Point.LongPoint pt) {
/*      */     Clipper.PolyFillType e1FillType, e2FillType, e1FillType2, e2FillType2;
/*      */     int e1Wc, e2Wc;
/* 1705 */     LOGGER.entering(DefaultClipper.class.getName(), "insersectEdges");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1710 */     boolean e1Contributing = (e1.outIdx >= 0);
/* 1711 */     boolean e2Contributing = (e2.outIdx >= 0);
/*      */     
/* 1713 */     setZ(pt, e1, e2);
/*      */ 
/*      */     
/* 1716 */     if (e1.windDelta == 0 || e2.windDelta == 0) {
/*      */ 
/*      */       
/* 1719 */       if (e1.windDelta == 0 && e2.windDelta == 0) {
/*      */         return;
/*      */       }
/* 1722 */       if (e1.polyTyp == e2.polyTyp && e1.windDelta != e2.windDelta && this.clipType == Clipper.ClipType.UNION) {
/*      */         
/* 1724 */         if (e1.windDelta == 0) {
/* 1725 */           if (e2Contributing) {
/* 1726 */             addOutPt(e1, pt);
/* 1727 */             if (e1Contributing) {
/* 1728 */               e1.outIdx = -1;
/*      */             }
/*      */           }
/*      */         
/*      */         }
/* 1733 */         else if (e1Contributing) {
/* 1734 */           addOutPt(e2, pt);
/* 1735 */           if (e2Contributing) {
/* 1736 */             e2.outIdx = -1;
/*      */           }
/*      */         }
/*      */       
/*      */       }
/* 1741 */       else if (e1.polyTyp != e2.polyTyp) {
/* 1742 */         if (e1.windDelta == 0 && Math.abs(e2.windCnt) == 1 && (this.clipType != Clipper.ClipType.UNION || e2.windCnt2 == 0)) {
/* 1743 */           addOutPt(e1, pt);
/* 1744 */           if (e1Contributing) {
/* 1745 */             e1.outIdx = -1;
/*      */           }
/*      */         }
/* 1748 */         else if (e2.windDelta == 0 && Math.abs(e1.windCnt) == 1 && (this.clipType != Clipper.ClipType.UNION || e1.windCnt2 == 0)) {
/* 1749 */           addOutPt(e2, pt);
/* 1750 */           if (e2Contributing) {
/* 1751 */             e2.outIdx = -1;
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1760 */     if (e1.polyTyp == e2.polyTyp) {
/* 1761 */       if (e1.isEvenOddFillType(this.clipFillType, this.subjFillType)) {
/* 1762 */         int oldE1WindCnt = e1.windCnt;
/* 1763 */         e1.windCnt = e2.windCnt;
/* 1764 */         e2.windCnt = oldE1WindCnt;
/*      */       } else {
/*      */         
/* 1767 */         if (e1.windCnt + e2.windDelta == 0) {
/* 1768 */           e1.windCnt = -e1.windCnt;
/*      */         } else {
/*      */           
/* 1771 */           e1.windCnt += e2.windDelta;
/*      */         } 
/* 1773 */         if (e2.windCnt - e1.windDelta == 0) {
/* 1774 */           e2.windCnt = -e2.windCnt;
/*      */         } else {
/*      */           
/* 1777 */           e2.windCnt -= e1.windDelta;
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 1782 */       if (!e2.isEvenOddFillType(this.clipFillType, this.subjFillType)) {
/* 1783 */         e1.windCnt2 += e2.windDelta;
/*      */       } else {
/*      */         
/* 1786 */         e1.windCnt2 = (e1.windCnt2 == 0) ? 1 : 0;
/*      */       } 
/* 1788 */       if (!e1.isEvenOddFillType(this.clipFillType, this.subjFillType)) {
/* 1789 */         e2.windCnt2 -= e1.windDelta;
/*      */       } else {
/*      */         
/* 1792 */         e2.windCnt2 = (e2.windCnt2 == 0) ? 1 : 0;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1797 */     if (e1.polyTyp == Clipper.PolyType.SUBJECT) {
/* 1798 */       e1FillType = this.subjFillType;
/* 1799 */       e1FillType2 = this.clipFillType;
/*      */     } else {
/*      */       
/* 1802 */       e1FillType = this.clipFillType;
/* 1803 */       e1FillType2 = this.subjFillType;
/*      */     } 
/* 1805 */     if (e2.polyTyp == Clipper.PolyType.SUBJECT) {
/* 1806 */       e2FillType = this.subjFillType;
/* 1807 */       e2FillType2 = this.clipFillType;
/*      */     } else {
/*      */       
/* 1810 */       e2FillType = this.clipFillType;
/* 1811 */       e2FillType2 = this.subjFillType;
/*      */     } 
/*      */ 
/*      */     
/* 1815 */     switch (e1FillType) {
/*      */       case INTERSECTION:
/* 1817 */         e1Wc = e1.windCnt;
/*      */         break;
/*      */       case UNION:
/* 1820 */         e1Wc = -e1.windCnt;
/*      */         break;
/*      */       default:
/* 1823 */         e1Wc = Math.abs(e1.windCnt);
/*      */         break;
/*      */     } 
/* 1826 */     switch (e2FillType) {
/*      */       case INTERSECTION:
/* 1828 */         e2Wc = e2.windCnt;
/*      */         break;
/*      */       case UNION:
/* 1831 */         e2Wc = -e2.windCnt;
/*      */         break;
/*      */       default:
/* 1834 */         e2Wc = Math.abs(e2.windCnt);
/*      */         break;
/*      */     } 
/*      */     
/* 1838 */     if (e1Contributing && e2Contributing) {
/* 1839 */       if ((e1Wc != 0 && e1Wc != 1) || (e2Wc != 0 && e2Wc != 1) || (e1.polyTyp != e2.polyTyp && this.clipType != Clipper.ClipType.XOR)) {
/* 1840 */         addLocalMaxPoly(e1, e2, pt);
/*      */       } else {
/*      */         
/* 1843 */         addOutPt(e1, pt);
/* 1844 */         addOutPt(e2, pt);
/* 1845 */         Edge.swapSides(e1, e2);
/* 1846 */         Edge.swapPolyIndexes(e1, e2);
/*      */       }
/*      */     
/* 1849 */     } else if (e1Contributing) {
/* 1850 */       if (e2Wc == 0 || e2Wc == 1) {
/* 1851 */         addOutPt(e1, pt);
/* 1852 */         Edge.swapSides(e1, e2);
/* 1853 */         Edge.swapPolyIndexes(e1, e2);
/*      */       }
/*      */     
/*      */     }
/* 1857 */     else if (e2Contributing) {
/* 1858 */       if (e1Wc == 0 || e1Wc == 1) {
/* 1859 */         addOutPt(e2, pt);
/* 1860 */         Edge.swapSides(e1, e2);
/* 1861 */         Edge.swapPolyIndexes(e1, e2);
/*      */       }
/*      */     
/* 1864 */     } else if ((e1Wc == 0 || e1Wc == 1) && (e2Wc == 0 || e2Wc == 1)) {
/*      */       int e1Wc2;
/*      */       int e2Wc2;
/* 1867 */       switch (e1FillType2) {
/*      */         case INTERSECTION:
/* 1869 */           e1Wc2 = e1.windCnt2;
/*      */           break;
/*      */         case UNION:
/* 1872 */           e1Wc2 = -e1.windCnt2;
/*      */           break;
/*      */         default:
/* 1875 */           e1Wc2 = Math.abs(e1.windCnt2);
/*      */           break;
/*      */       } 
/* 1878 */       switch (e2FillType2) {
/*      */         case INTERSECTION:
/* 1880 */           e2Wc2 = e2.windCnt2;
/*      */           break;
/*      */         case UNION:
/* 1883 */           e2Wc2 = -e2.windCnt2;
/*      */           break;
/*      */         default:
/* 1886 */           e2Wc2 = Math.abs(e2.windCnt2);
/*      */           break;
/*      */       } 
/*      */       
/* 1890 */       if (e1.polyTyp != e2.polyTyp) {
/* 1891 */         addLocalMinPoly(e1, e2, pt);
/*      */       }
/* 1893 */       else if (e1Wc == 1 && e2Wc == 1) {
/* 1894 */         switch (this.clipType) {
/*      */           case INTERSECTION:
/* 1896 */             if (e1Wc2 > 0 && e2Wc2 > 0) {
/* 1897 */               addLocalMinPoly(e1, e2, pt);
/*      */             }
/*      */             break;
/*      */           case UNION:
/* 1901 */             if (e1Wc2 <= 0 && e2Wc2 <= 0) {
/* 1902 */               addLocalMinPoly(e1, e2, pt);
/*      */             }
/*      */             break;
/*      */           case DIFFERENCE:
/* 1906 */             if ((e1.polyTyp == Clipper.PolyType.CLIP && e1Wc2 > 0 && e2Wc2 > 0) || (e1.polyTyp == Clipper.PolyType.SUBJECT && e1Wc2 <= 0 && e2Wc2 <= 0)) {
/* 1907 */               addLocalMinPoly(e1, e2, pt);
/*      */             }
/*      */             break;
/*      */           case XOR:
/* 1911 */             addLocalMinPoly(e1, e2, pt);
/*      */             break;
/*      */         } 
/*      */       
/*      */       } else {
/* 1916 */         Edge.swapSides(e1, e2);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void intersectPoint(Edge edge1, Edge edge2, Point.LongPoint[] ipV) {
/* 1922 */     Point.LongPoint ip = ipV[0] = new Point.LongPoint();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1927 */     if (edge1.deltaX == edge2.deltaX) {
/* 1928 */       ip.setY(Long.valueOf(edge1.getCurrent().getY()));
/* 1929 */       ip.setX(Long.valueOf(Edge.topX(edge1, ip.getY())));
/*      */       
/*      */       return;
/*      */     } 
/* 1933 */     if (edge1.getDelta().getX() == 0L) {
/* 1934 */       ip.setX(Long.valueOf(edge1.getBot().getX()));
/* 1935 */       if (edge2.isHorizontal()) {
/* 1936 */         ip.setY(Long.valueOf(edge2.getBot().getY()));
/*      */       } else {
/*      */         
/* 1939 */         double b2 = edge2.getBot().getY() - edge2.getBot().getX() / edge2.deltaX;
/* 1940 */         ip.setY(Long.valueOf(Math.round(ip.getX() / edge2.deltaX + b2)));
/*      */       }
/*      */     
/* 1943 */     } else if (edge2.getDelta().getX() == 0L) {
/* 1944 */       ip.setX(Long.valueOf(edge2.getBot().getX()));
/* 1945 */       if (edge1.isHorizontal()) {
/* 1946 */         ip.setY(Long.valueOf(edge1.getBot().getY()));
/*      */       } else {
/*      */         
/* 1949 */         double b1 = edge1.getBot().getY() - edge1.getBot().getX() / edge1.deltaX;
/* 1950 */         ip.setY(Long.valueOf(Math.round(ip.getX() / edge1.deltaX + b1)));
/*      */       } 
/*      */     } else {
/*      */       
/* 1954 */       double b1 = edge1.getBot().getX() - edge1.getBot().getY() * edge1.deltaX;
/* 1955 */       double b2 = edge2.getBot().getX() - edge2.getBot().getY() * edge2.deltaX;
/* 1956 */       double q = (b2 - b1) / (edge1.deltaX - edge2.deltaX);
/* 1957 */       ip.setY(Long.valueOf(Math.round(q)));
/* 1958 */       if (Math.abs(edge1.deltaX) < Math.abs(edge2.deltaX)) {
/* 1959 */         ip.setX(Long.valueOf(Math.round(edge1.deltaX * q + b1)));
/*      */       } else {
/*      */         
/* 1962 */         ip.setX(Long.valueOf(Math.round(edge2.deltaX * q + b2)));
/*      */       } 
/*      */     } 
/*      */     
/* 1966 */     if (ip.getY() < edge1.getTop().getY() || ip.getY() < edge2.getTop().getY()) {
/* 1967 */       if (edge1.getTop().getY() > edge2.getTop().getY()) {
/* 1968 */         ip.setY(Long.valueOf(edge1.getTop().getY()));
/*      */       } else {
/*      */         
/* 1971 */         ip.setY(Long.valueOf(edge2.getTop().getY()));
/*      */       } 
/* 1973 */       if (Math.abs(edge1.deltaX) < Math.abs(edge2.deltaX)) {
/* 1974 */         ip.setX(Long.valueOf(Edge.topX(edge1, ip.getY())));
/*      */       } else {
/*      */         
/* 1977 */         ip.setX(Long.valueOf(Edge.topX(edge2, ip.getY())));
/*      */       } 
/*      */     } 
/*      */     
/* 1981 */     if (ip.getY() > edge1.getCurrent().getY()) {
/* 1982 */       ip.setY(Long.valueOf(edge1.getCurrent().getY()));
/*      */       
/* 1984 */       if (Math.abs(edge1.deltaX) > Math.abs(edge2.deltaX)) {
/* 1985 */         ip.setX(Long.valueOf(Edge.topX(edge2, ip.getY())));
/*      */       } else {
/*      */         
/* 1988 */         ip.setX(Long.valueOf(Edge.topX(edge1, ip.getY())));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void joinCommonEdges() {
/* 1994 */     for (int i = 0; i < this.joins.size(); i++) {
/* 1995 */       Path.Join join = this.joins.get(i);
/*      */       
/* 1997 */       Path.OutRec outRec1 = getOutRec(join.outPt1.idx);
/* 1998 */       Path.OutRec outRec2 = getOutRec(join.outPt2.idx);
/*      */       
/* 2000 */       if (outRec1.getPoints() != null && outRec2.getPoints() != null)
/*      */       {
/*      */         
/* 2003 */         if (!outRec1.isOpen && !outRec2.isOpen) {
/*      */           Path.OutRec holeStateRec;
/*      */ 
/*      */ 
/*      */           
/* 2008 */           if (outRec1 == outRec2) {
/* 2009 */             holeStateRec = outRec1;
/*      */           }
/* 2011 */           else if (isParam1RightOfParam2(outRec1, outRec2)) {
/* 2012 */             holeStateRec = outRec2;
/*      */           }
/* 2014 */           else if (isParam1RightOfParam2(outRec2, outRec1)) {
/* 2015 */             holeStateRec = outRec1;
/*      */           } else {
/*      */             
/* 2018 */             holeStateRec = Path.OutPt.getLowerMostRec(outRec1, outRec2);
/*      */           } 
/*      */           
/* 2021 */           if (joinPoints(join, outRec1, outRec2))
/*      */           {
/*      */ 
/*      */             
/* 2025 */             if (outRec1 == outRec2) {
/*      */ 
/*      */               
/* 2028 */               outRec1.setPoints(join.outPt1);
/* 2029 */               outRec1.bottomPt = null;
/* 2030 */               outRec2 = createOutRec();
/* 2031 */               outRec2.setPoints(join.outPt2);
/*      */ 
/*      */               
/* 2034 */               updateOutPtIdxs(outRec2);
/*      */ 
/*      */ 
/*      */               
/* 2038 */               if (this.usingPolyTree) {
/* 2039 */                 for (int j = 0; j < this.polyOuts.size() - 1; j++) {
/* 2040 */                   Path.OutRec oRec = this.polyOuts.get(j);
/* 2041 */                   if (oRec.getPoints() != null && parseFirstLeft(oRec.firstLeft) == outRec1 && oRec.isHole != outRec1.isHole)
/*      */                   {
/*      */                     
/* 2044 */                     if (poly2ContainsPoly1(oRec.getPoints(), join.outPt2)) {
/* 2045 */                       oRec.firstLeft = outRec2;
/*      */                     }
/*      */                   }
/*      */                 } 
/*      */               }
/* 2050 */               if (poly2ContainsPoly1(outRec2.getPoints(), outRec1.getPoints()))
/*      */               {
/* 2052 */                 outRec2.isHole = !outRec1.isHole;
/* 2053 */                 outRec2.firstLeft = outRec1;
/*      */ 
/*      */                 
/* 2056 */                 if (this.usingPolyTree) {
/* 2057 */                   fixupFirstLefts2(outRec2, outRec1);
/*      */                 }
/*      */                 
/* 2060 */                 if ((outRec2.isHole ^ this.reverseSolution) == ((outRec2.area() > 0.0D) ? 1 : 0)) {
/* 2061 */                   outRec2.getPoints().reversePolyPtLinks();
/*      */                 
/*      */                 }
/*      */               }
/* 2065 */               else if (poly2ContainsPoly1(outRec1.getPoints(), outRec2.getPoints()))
/*      */               {
/* 2067 */                 outRec2.isHole = outRec1.isHole;
/* 2068 */                 outRec1.isHole = !outRec2.isHole;
/* 2069 */                 outRec2.firstLeft = outRec1.firstLeft;
/* 2070 */                 outRec1.firstLeft = outRec2;
/*      */ 
/*      */                 
/* 2073 */                 if (this.usingPolyTree) {
/* 2074 */                   fixupFirstLefts2(outRec1, outRec2);
/*      */                 }
/*      */                 
/* 2077 */                 if ((outRec1.isHole ^ this.reverseSolution) == ((outRec1.area() > 0.0D) ? 1 : 0)) {
/* 2078 */                   outRec1.getPoints().reversePolyPtLinks();
/*      */                 }
/*      */               }
/*      */               else
/*      */               {
/* 2083 */                 outRec2.isHole = outRec1.isHole;
/* 2084 */                 outRec2.firstLeft = outRec1.firstLeft;
/*      */ 
/*      */                 
/* 2087 */                 if (this.usingPolyTree) {
/* 2088 */                   fixupFirstLefts1(outRec1, outRec2);
/*      */                 
/*      */                 }
/*      */               }
/*      */             
/*      */             }
/*      */             else {
/*      */               
/* 2096 */               outRec2.setPoints(null);
/* 2097 */               outRec2.bottomPt = null;
/* 2098 */               outRec2.Idx = outRec1.Idx;
/*      */               
/* 2100 */               outRec1.isHole = holeStateRec.isHole;
/* 2101 */               if (holeStateRec == outRec2) {
/* 2102 */                 outRec1.firstLeft = outRec2.firstLeft;
/*      */               }
/* 2104 */               outRec2.firstLeft = outRec1;
/*      */ 
/*      */               
/* 2107 */               if (this.usingPolyTree)
/* 2108 */                 fixupFirstLefts2(outRec2, outRec1); 
/*      */             }  } 
/*      */         }  } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private long popScanbeam() {
/* 2115 */     LOGGER.entering(DefaultClipper.class.getName(), "popBeam");
/*      */     
/* 2117 */     long y = this.scanbeam.y;
/* 2118 */     this.scanbeam = this.scanbeam.next;
/* 2119 */     return y;
/*      */   }
/*      */   
/*      */   private void processEdgesAtTopOfScanbeam(long topY) {
/* 2123 */     LOGGER.entering(DefaultClipper.class.getName(), "processEdgesAtTopOfScanbeam");
/*      */     
/* 2125 */     Edge e = this.activeEdges;
/* 2126 */     while (e != null) {
/*      */ 
/*      */       
/* 2129 */       boolean IsMaximaEdge = e.isMaxima(topY);
/*      */       
/* 2131 */       if (IsMaximaEdge) {
/* 2132 */         Edge eMaxPair = e.getMaximaPair();
/* 2133 */         IsMaximaEdge = (eMaxPair == null || !eMaxPair.isHorizontal());
/*      */       } 
/*      */       
/* 2136 */       if (IsMaximaEdge) {
/* 2137 */         if (this.strictlySimple) InsertMaxima(e.getTop().getX()); 
/* 2138 */         Edge ePrev = e.prevInAEL;
/* 2139 */         doMaxima(e);
/* 2140 */         if (ePrev == null) {
/* 2141 */           e = this.activeEdges;
/*      */           continue;
/*      */         } 
/* 2144 */         e = ePrev.nextInAEL;
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/* 2149 */       if (e.isIntermediate(topY) && e.nextInLML.isHorizontal()) {
/* 2150 */         Edge[] t = { e };
/* 2151 */         updateEdgeIntoAEL(t);
/* 2152 */         e = t[0];
/* 2153 */         if (e.outIdx >= 0) {
/* 2154 */           addOutPt(e, e.getBot());
/*      */         }
/* 2156 */         addEdgeToSEL(e);
/*      */       } else {
/*      */         
/* 2159 */         e.getCurrent().setX(Long.valueOf(Edge.topX(e, topY)));
/* 2160 */         e.getCurrent().setY(Long.valueOf(topY));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2165 */       if (this.strictlySimple) {
/* 2166 */         Edge ePrev = e.prevInAEL;
/* 2167 */         if (e.outIdx >= 0 && e.windDelta != 0 && ePrev != null && ePrev.outIdx >= 0 && ePrev.getCurrent().getX() == e.getCurrent().getX() && ePrev.windDelta != 0) {
/*      */           
/* 2169 */           Point.LongPoint ip = new Point.LongPoint(e.getCurrent());
/*      */           
/* 2171 */           setZ(ip, ePrev, e);
/*      */           
/* 2173 */           Path.OutPt op = addOutPt(ePrev, ip);
/* 2174 */           Path.OutPt op2 = addOutPt(e, ip);
/* 2175 */           addJoin(op, op2, ip);
/*      */         } 
/*      */       } 
/*      */       
/* 2179 */       e = e.nextInAEL;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2184 */     processHorizontals();
/* 2185 */     this.maxima = null;
/*      */ 
/*      */     
/* 2188 */     e = this.activeEdges;
/* 2189 */     while (e != null) {
/* 2190 */       if (e.isIntermediate(topY)) {
/* 2191 */         Path.OutPt op = null;
/* 2192 */         if (e.outIdx >= 0) {
/* 2193 */           op = addOutPt(e, e.getTop());
/*      */         }
/* 2195 */         Edge[] t = { e };
/* 2196 */         updateEdgeIntoAEL(t);
/* 2197 */         e = t[0];
/*      */ 
/*      */         
/* 2200 */         Edge ePrev = e.prevInAEL;
/* 2201 */         Edge eNext = e.nextInAEL;
/* 2202 */         if (ePrev != null && ePrev.getCurrent().getX() == e.getBot().getX() && ePrev.getCurrent().getY() == e.getBot().getY() && op != null && ePrev.outIdx >= 0 && ePrev
/* 2203 */           .getCurrent().getY() > ePrev.getTop().getY() && Edge.slopesEqual(e, ePrev, this.useFullRange) && e.windDelta != 0 && ePrev.windDelta != 0) {
/*      */           
/* 2205 */           Path.OutPt op2 = addOutPt(ePrev, e.getBot());
/* 2206 */           addJoin(op, op2, e.getTop());
/*      */         }
/* 2208 */         else if (eNext != null && eNext.getCurrent().getX() == e.getBot().getX() && eNext.getCurrent().getY() == e.getBot().getY() && op != null && eNext.outIdx >= 0 && eNext
/* 2209 */           .getCurrent().getY() > eNext.getTop().getY() && Edge.slopesEqual(e, eNext, this.useFullRange) && e.windDelta != 0 && eNext.windDelta != 0) {
/*      */           
/* 2211 */           Path.OutPt op2 = addOutPt(eNext, e.getBot());
/* 2212 */           addJoin(op, op2, e.getTop());
/*      */         } 
/*      */       } 
/* 2215 */       e = e.nextInAEL;
/*      */     } 
/* 2217 */     LOGGER.exiting(DefaultClipper.class.getName(), "processEdgesAtTopOfScanbeam");
/*      */   }
/*      */   
/*      */   private void processHorizontal(Edge horzEdge) {
/* 2221 */     LOGGER.entering(DefaultClipper.class.getName(), "isHorizontal");
/* 2222 */     Clipper.Direction[] dir = new Clipper.Direction[1];
/* 2223 */     long[] horzLeft = new long[1], horzRight = new long[1];
/* 2224 */     boolean IsOpen = (horzEdge.outIdx >= 0 && ((Path.OutRec)this.polyOuts.get(horzEdge.outIdx)).isOpen);
/*      */     
/* 2226 */     getHorzDirection(horzEdge, dir, horzLeft, horzRight);
/*      */     
/* 2228 */     Edge eLastHorz = horzEdge, eMaxPair = null;
/* 2229 */     while (eLastHorz.nextInLML != null && eLastHorz.nextInLML.isHorizontal()) {
/* 2230 */       eLastHorz = eLastHorz.nextInLML;
/*      */     }
/* 2232 */     if (eLastHorz.nextInLML == null) {
/* 2233 */       eMaxPair = eLastHorz.getMaximaPair();
/*      */     }
/*      */     
/* 2236 */     Path.Maxima currMax = this.maxima;
/* 2237 */     if (currMax != null)
/*      */     {
/*      */       
/* 2240 */       if (dir[0] == Clipper.Direction.LEFT_TO_RIGHT) {
/*      */         
/* 2242 */         while (currMax != null && currMax.X <= horzEdge.getBot().getX())
/* 2243 */           currMax = currMax.Next; 
/* 2244 */         if (currMax != null && currMax.X >= eLastHorz.getBot().getX()) {
/* 2245 */           currMax = null;
/*      */         }
/*      */       } else {
/*      */         
/* 2249 */         while (currMax.Next != null && currMax.Next.X < horzEdge.getBot().getX())
/* 2250 */           currMax = currMax.Next; 
/* 2251 */         if (currMax.X <= eLastHorz.getTop().getX()) currMax = null;
/*      */       
/*      */       } 
/*      */     }
/* 2255 */     Path.OutPt op1 = null;
/*      */     while (true) {
/* 2257 */       boolean IsLastHorz = (horzEdge == eLastHorz);
/* 2258 */       Edge e = horzEdge.getNextInAEL(dir[0]);
/* 2259 */       while (e != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2264 */         if (currMax != null)
/*      */         {
/* 2266 */           if (dir[0] == Clipper.Direction.LEFT_TO_RIGHT) {
/*      */             
/* 2268 */             while (currMax != null && currMax.X < e.getCurrent().getX())
/*      */             {
/* 2270 */               if (horzEdge.outIdx >= 0 && !IsOpen)
/* 2271 */                 addOutPt(horzEdge, new Point.LongPoint(currMax.X, horzEdge.getBot().getY())); 
/* 2272 */               currMax = currMax.Next;
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 2277 */             while (currMax != null && currMax.X > e.getCurrent().getX()) {
/*      */               
/* 2279 */               if (horzEdge.outIdx >= 0 && !IsOpen)
/* 2280 */                 addOutPt(horzEdge, new Point.LongPoint(currMax.X, horzEdge.getBot().getY())); 
/* 2281 */               currMax = currMax.Prev;
/*      */             } 
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/* 2287 */         if ((dir[0] == Clipper.Direction.LEFT_TO_RIGHT && e.getCurrent().getX() > horzRight[0]) || (dir[0] == Clipper.Direction.RIGHT_TO_LEFT && e
/* 2288 */           .getCurrent().getX() < horzLeft[0])) {
/*      */           break;
/*      */         }
/* 2291 */         if (e.getCurrent().getX() == horzEdge.getTop().getX() && horzEdge.nextInLML != null && e.deltaX < horzEdge.nextInLML.deltaX) {
/*      */           break;
/*      */         }
/* 2294 */         if (horzEdge.outIdx >= 0 && !IsOpen) {
/*      */           
/* 2296 */           op1 = addOutPt(horzEdge, e.getCurrent());
/* 2297 */           Edge eNextHorz = this.sortedEdges;
/* 2298 */           while (eNextHorz != null) {
/*      */             
/* 2300 */             if (eNextHorz.outIdx >= 0 && 
/* 2301 */               doHorzSegmentsOverlap(horzEdge.getBot().getX(), horzEdge
/* 2302 */                 .getTop().getX(), eNextHorz.getBot().getX(), eNextHorz.getTop().getX())) {
/*      */               
/* 2304 */               Path.OutPt op2 = GetLastOutPt(eNextHorz);
/* 2305 */               addJoin(op2, op1, eNextHorz.getTop());
/*      */             } 
/* 2307 */             eNextHorz = eNextHorz.nextInSEL;
/*      */           } 
/* 2309 */           addGhostJoin(op1, horzEdge.getBot());
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 2314 */         if (e == eMaxPair && IsLastHorz) {
/*      */           
/* 2316 */           if (horzEdge.outIdx >= 0)
/* 2317 */             addLocalMaxPoly(horzEdge, eMaxPair, horzEdge.getTop()); 
/* 2318 */           deleteFromAEL(horzEdge);
/* 2319 */           deleteFromAEL(eMaxPair);
/*      */           
/*      */           return;
/*      */         } 
/* 2323 */         if (dir[0] == Clipper.Direction.LEFT_TO_RIGHT) {
/*      */           
/* 2325 */           Point.LongPoint Pt = new Point.LongPoint(e.getCurrent().getX(), horzEdge.getCurrent().getY());
/* 2326 */           intersectEdges(horzEdge, e, Pt);
/*      */         }
/*      */         else {
/*      */           
/* 2330 */           Point.LongPoint Pt = new Point.LongPoint(e.getCurrent().getX(), horzEdge.getCurrent().getY());
/* 2331 */           intersectEdges(e, horzEdge, Pt);
/*      */         } 
/* 2333 */         Edge eNext = e.getNextInAEL(dir[0]);
/* 2334 */         swapPositionsInAEL(horzEdge, e);
/* 2335 */         e = eNext;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2340 */       if (horzEdge.nextInLML == null || !horzEdge.nextInLML.isHorizontal())
/*      */         break; 
/* 2342 */       Edge[] temp = new Edge[1];
/* 2343 */       temp[0] = horzEdge;
/* 2344 */       updateEdgeIntoAEL(temp);
/* 2345 */       horzEdge = temp[0];
/*      */       
/* 2347 */       if (horzEdge.outIdx >= 0) addOutPt(horzEdge, horzEdge.getBot()); 
/* 2348 */       getHorzDirection(horzEdge, dir, horzLeft, horzRight);
/*      */     } 
/*      */ 
/*      */     
/* 2352 */     if (horzEdge.outIdx >= 0 && op1 == null) {
/*      */       
/* 2354 */       op1 = GetLastOutPt(horzEdge);
/* 2355 */       Edge eNextHorz = this.sortedEdges;
/* 2356 */       while (eNextHorz != null) {
/*      */         
/* 2358 */         if (eNextHorz.outIdx >= 0 && 
/* 2359 */           doHorzSegmentsOverlap(horzEdge.getBot().getX(), horzEdge
/* 2360 */             .getTop().getX(), eNextHorz.getBot().getX(), eNextHorz.getTop().getX())) {
/*      */           
/* 2362 */           Path.OutPt op2 = GetLastOutPt(eNextHorz);
/* 2363 */           addJoin(op2, op1, eNextHorz.getTop());
/*      */         } 
/* 2365 */         eNextHorz = eNextHorz.nextInSEL;
/*      */       } 
/* 2367 */       addGhostJoin(op1, horzEdge.getTop());
/*      */     } 
/*      */     
/* 2370 */     if (horzEdge.nextInLML != null) {
/* 2371 */       if (horzEdge.outIdx >= 0) {
/* 2372 */         op1 = addOutPt(horzEdge, horzEdge.getTop());
/*      */         
/* 2374 */         Edge[] t = { horzEdge };
/* 2375 */         updateEdgeIntoAEL(t);
/* 2376 */         horzEdge = t[0];
/*      */         
/* 2378 */         if (horzEdge.windDelta == 0) {
/*      */           return;
/*      */         }
/*      */         
/* 2382 */         Edge ePrev = horzEdge.prevInAEL;
/* 2383 */         Edge eNext = horzEdge.nextInAEL;
/* 2384 */         if (ePrev != null && ePrev.getCurrent().getX() == horzEdge.getBot().getX() && ePrev.getCurrent().getY() == horzEdge.getBot().getY() && ePrev.windDelta != 0 && ePrev.outIdx >= 0 && ePrev
/* 2385 */           .getCurrent().getY() > ePrev.getTop().getY() && 
/* 2386 */           Edge.slopesEqual(horzEdge, ePrev, this.useFullRange)) {
/* 2387 */           Path.OutPt op2 = addOutPt(ePrev, horzEdge.getBot());
/* 2388 */           addJoin(op1, op2, horzEdge.getTop());
/*      */         }
/* 2390 */         else if (eNext != null && eNext.getCurrent().getX() == horzEdge.getBot().getX() && eNext.getCurrent().getY() == horzEdge.getBot().getY() && eNext.windDelta != 0 && eNext.outIdx >= 0 && eNext
/* 2391 */           .getCurrent().getY() > eNext.getTop().getY() && 
/* 2392 */           Edge.slopesEqual(horzEdge, eNext, this.useFullRange)) {
/* 2393 */           Path.OutPt op2 = addOutPt(eNext, horzEdge.getBot());
/* 2394 */           addJoin(op1, op2, horzEdge.getTop());
/*      */         } 
/*      */       } else {
/*      */         
/* 2398 */         Edge[] t = { horzEdge };
/* 2399 */         updateEdgeIntoAEL(t);
/* 2400 */         horzEdge = t[0];
/*      */       } 
/*      */     } else {
/*      */       
/* 2404 */       if (horzEdge.outIdx >= 0) {
/* 2405 */         addOutPt(horzEdge, horzEdge.getTop());
/*      */       }
/* 2407 */       deleteFromAEL(horzEdge);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void processHorizontals() {
/* 2414 */     LOGGER.entering(DefaultClipper.class.getName(), "processHorizontals");
/*      */     
/* 2416 */     Edge horzEdge = this.sortedEdges;
/* 2417 */     while (horzEdge != null) {
/* 2418 */       deleteFromSEL(horzEdge);
/* 2419 */       processHorizontal(horzEdge);
/* 2420 */       horzEdge = this.sortedEdges;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean processIntersections(long topY) {
/* 2427 */     LOGGER.entering(DefaultClipper.class.getName(), "processIntersections");
/*      */     
/* 2429 */     if (this.activeEdges == null) {
/* 2430 */       return true;
/*      */     }
/*      */     try {
/* 2433 */       buildIntersectList(topY);
/* 2434 */       if (this.intersectList.size() == 0) {
/* 2435 */         return true;
/*      */       }
/* 2437 */       if (this.intersectList.size() == 1 || fixupIntersectionOrder()) {
/* 2438 */         processIntersectList();
/*      */       } else {
/*      */         
/* 2441 */         return false;
/*      */       }
/*      */     
/* 2444 */     } catch (Exception e) {
/* 2445 */       this.sortedEdges = null;
/* 2446 */       this.intersectList.clear();
/* 2447 */       throw new IllegalStateException("ProcessIntersections error", e);
/*      */     } 
/* 2449 */     this.sortedEdges = null;
/* 2450 */     return true;
/*      */   }
/*      */   
/*      */   private void processIntersectList() {
/* 2454 */     for (int i = 0; i < this.intersectList.size(); i++) {
/* 2455 */       IntersectNode iNode = this.intersectList.get(i);
/*      */       
/* 2457 */       intersectEdges(iNode.edge1, iNode.Edge2, iNode.getPt());
/* 2458 */       swapPositionsInAEL(iNode.edge1, iNode.Edge2);
/*      */     } 
/*      */     
/* 2461 */     this.intersectList.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void reset() {
/* 2468 */     super.reset();
/* 2469 */     this.scanbeam = null;
/* 2470 */     this.maxima = null;
/* 2471 */     this.activeEdges = null;
/* 2472 */     this.sortedEdges = null;
/* 2473 */     ClipperBase.LocalMinima lm = this.minimaList;
/* 2474 */     while (lm != null) {
/* 2475 */       insertScanbeam(lm.y);
/* 2476 */       lm = lm.next;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void setHoleState(Edge e, Path.OutRec outRec) {
/* 2481 */     boolean isHole = false;
/* 2482 */     Edge e2 = e.prevInAEL;
/* 2483 */     while (e2 != null) {
/* 2484 */       if (e2.outIdx >= 0 && e2.windDelta != 0) {
/* 2485 */         isHole = !isHole;
/* 2486 */         if (outRec.firstLeft == null) {
/* 2487 */           outRec.firstLeft = this.polyOuts.get(e2.outIdx);
/*      */         }
/*      */       } 
/* 2490 */       e2 = e2.prevInAEL;
/*      */     } 
/* 2492 */     if (isHole) {
/* 2493 */       outRec.isHole = true;
/*      */     }
/*      */   }
/*      */   
/*      */   private void setZ(Point.LongPoint pt, Edge e1, Edge e2) {
/* 2498 */     if (pt.getZ() != 0L || this.zFillFunction == null) {
/*      */       return;
/*      */     }
/* 2501 */     if (pt.equals(e1.getBot())) {
/* 2502 */       pt.setZ(Long.valueOf(e1.getBot().getZ()));
/*      */     }
/* 2504 */     else if (pt.equals(e1.getTop())) {
/* 2505 */       pt.setZ(Long.valueOf(e1.getTop().getZ()));
/*      */     }
/* 2507 */     else if (pt.equals(e2.getBot())) {
/* 2508 */       pt.setZ(Long.valueOf(e2.getBot().getZ()));
/*      */     }
/* 2510 */     else if (pt.equals(e2.getTop())) {
/* 2511 */       pt.setZ(Long.valueOf(e2.getTop().getZ()));
/*      */     } else {
/*      */       
/* 2514 */       this.zFillFunction.zFill(e1.getBot(), e1.getTop(), e2.getBot(), e2.getTop(), pt);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void swapPositionsInAEL(Edge edge1, Edge edge2) {
/* 2519 */     LOGGER.entering(DefaultClipper.class.getName(), "swapPositionsInAEL");
/*      */ 
/*      */     
/* 2522 */     if (edge1.nextInAEL == edge1.prevInAEL || edge2.nextInAEL == edge2.prevInAEL) {
/*      */       return;
/*      */     }
/*      */     
/* 2526 */     if (edge1.nextInAEL == edge2) {
/* 2527 */       Edge next = edge2.nextInAEL;
/* 2528 */       if (next != null) {
/* 2529 */         next.prevInAEL = edge1;
/*      */       }
/* 2531 */       Edge prev = edge1.prevInAEL;
/* 2532 */       if (prev != null) {
/* 2533 */         prev.nextInAEL = edge2;
/*      */       }
/* 2535 */       edge2.prevInAEL = prev;
/* 2536 */       edge2.nextInAEL = edge1;
/* 2537 */       edge1.prevInAEL = edge2;
/* 2538 */       edge1.nextInAEL = next;
/*      */     }
/* 2540 */     else if (edge2.nextInAEL == edge1) {
/* 2541 */       Edge next = edge1.nextInAEL;
/* 2542 */       if (next != null) {
/* 2543 */         next.prevInAEL = edge2;
/*      */       }
/* 2545 */       Edge prev = edge2.prevInAEL;
/* 2546 */       if (prev != null) {
/* 2547 */         prev.nextInAEL = edge1;
/*      */       }
/* 2549 */       edge1.prevInAEL = prev;
/* 2550 */       edge1.nextInAEL = edge2;
/* 2551 */       edge2.prevInAEL = edge1;
/* 2552 */       edge2.nextInAEL = next;
/*      */     } else {
/*      */       
/* 2555 */       Edge next = edge1.nextInAEL;
/* 2556 */       Edge prev = edge1.prevInAEL;
/* 2557 */       edge1.nextInAEL = edge2.nextInAEL;
/* 2558 */       if (edge1.nextInAEL != null) {
/* 2559 */         edge1.nextInAEL.prevInAEL = edge1;
/*      */       }
/* 2561 */       edge1.prevInAEL = edge2.prevInAEL;
/* 2562 */       if (edge1.prevInAEL != null) {
/* 2563 */         edge1.prevInAEL.nextInAEL = edge1;
/*      */       }
/* 2565 */       edge2.nextInAEL = next;
/* 2566 */       if (edge2.nextInAEL != null) {
/* 2567 */         edge2.nextInAEL.prevInAEL = edge2;
/*      */       }
/* 2569 */       edge2.prevInAEL = prev;
/* 2570 */       if (edge2.prevInAEL != null) {
/* 2571 */         edge2.prevInAEL.nextInAEL = edge2;
/*      */       }
/*      */     } 
/*      */     
/* 2575 */     if (edge1.prevInAEL == null) {
/* 2576 */       this.activeEdges = edge1;
/*      */     }
/* 2578 */     else if (edge2.prevInAEL == null) {
/* 2579 */       this.activeEdges = edge2;
/*      */     } 
/*      */     
/* 2582 */     LOGGER.exiting(DefaultClipper.class.getName(), "swapPositionsInAEL");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void swapPositionsInSEL(Edge edge1, Edge edge2) {
/* 2588 */     if (edge1.nextInSEL == null && edge1.prevInSEL == null) {
/*      */       return;
/*      */     }
/* 2591 */     if (edge2.nextInSEL == null && edge2.prevInSEL == null) {
/*      */       return;
/*      */     }
/*      */     
/* 2595 */     if (edge1.nextInSEL == edge2) {
/* 2596 */       Edge next = edge2.nextInSEL;
/* 2597 */       if (next != null) {
/* 2598 */         next.prevInSEL = edge1;
/*      */       }
/* 2600 */       Edge prev = edge1.prevInSEL;
/* 2601 */       if (prev != null) {
/* 2602 */         prev.nextInSEL = edge2;
/*      */       }
/* 2604 */       edge2.prevInSEL = prev;
/* 2605 */       edge2.nextInSEL = edge1;
/* 2606 */       edge1.prevInSEL = edge2;
/* 2607 */       edge1.nextInSEL = next;
/*      */     }
/* 2609 */     else if (edge2.nextInSEL == edge1) {
/* 2610 */       Edge next = edge1.nextInSEL;
/* 2611 */       if (next != null) {
/* 2612 */         next.prevInSEL = edge2;
/*      */       }
/* 2614 */       Edge prev = edge2.prevInSEL;
/* 2615 */       if (prev != null) {
/* 2616 */         prev.nextInSEL = edge1;
/*      */       }
/* 2618 */       edge1.prevInSEL = prev;
/* 2619 */       edge1.nextInSEL = edge2;
/* 2620 */       edge2.prevInSEL = edge1;
/* 2621 */       edge2.nextInSEL = next;
/*      */     } else {
/*      */       
/* 2624 */       Edge next = edge1.nextInSEL;
/* 2625 */       Edge prev = edge1.prevInSEL;
/* 2626 */       edge1.nextInSEL = edge2.nextInSEL;
/* 2627 */       if (edge1.nextInSEL != null) {
/* 2628 */         edge1.nextInSEL.prevInSEL = edge1;
/*      */       }
/* 2630 */       edge1.prevInSEL = edge2.prevInSEL;
/* 2631 */       if (edge1.prevInSEL != null) {
/* 2632 */         edge1.prevInSEL.nextInSEL = edge1;
/*      */       }
/* 2634 */       edge2.nextInSEL = next;
/* 2635 */       if (edge2.nextInSEL != null) {
/* 2636 */         edge2.nextInSEL.prevInSEL = edge2;
/*      */       }
/* 2638 */       edge2.prevInSEL = prev;
/* 2639 */       if (edge2.prevInSEL != null) {
/* 2640 */         edge2.prevInSEL.nextInSEL = edge2;
/*      */       }
/*      */     } 
/*      */     
/* 2644 */     if (edge1.prevInSEL == null) {
/* 2645 */       this.sortedEdges = edge1;
/*      */     }
/* 2647 */     else if (edge2.prevInSEL == null) {
/* 2648 */       this.sortedEdges = edge2;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateEdgeIntoAEL(Edge[] eV) {
/* 2653 */     Edge e = eV[0];
/* 2654 */     if (e.nextInLML == null) {
/* 2655 */       throw new IllegalStateException("UpdateEdgeIntoAEL: invalid call");
/*      */     }
/* 2657 */     Edge AelPrev = e.prevInAEL;
/* 2658 */     Edge AelNext = e.nextInAEL;
/* 2659 */     e.nextInLML.outIdx = e.outIdx;
/* 2660 */     if (AelPrev != null) {
/* 2661 */       AelPrev.nextInAEL = e.nextInLML;
/*      */     } else {
/*      */       
/* 2664 */       this.activeEdges = e.nextInLML;
/*      */     } 
/* 2666 */     if (AelNext != null) {
/* 2667 */       AelNext.prevInAEL = e.nextInLML;
/*      */     }
/* 2669 */     e.nextInLML.side = e.side;
/* 2670 */     e.nextInLML.windDelta = e.windDelta;
/* 2671 */     e.nextInLML.windCnt = e.windCnt;
/* 2672 */     e.nextInLML.windCnt2 = e.windCnt2;
/* 2673 */     eV[0] = e = e.nextInLML;
/* 2674 */     e.setCurrent(e.getBot());
/* 2675 */     e.prevInAEL = AelPrev;
/* 2676 */     e.nextInAEL = AelNext;
/* 2677 */     if (!e.isHorizontal()) {
/* 2678 */       insertScanbeam(e.getTop().getY());
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateOutPtIdxs(Path.OutRec outrec) {
/* 2683 */     Path.OutPt op = outrec.getPoints();
/*      */     do {
/* 2685 */       op.idx = outrec.Idx;
/* 2686 */       op = op.prev;
/*      */     }
/* 2688 */     while (op != outrec.getPoints());
/*      */   }
/*      */   
/*      */   private void updateWindingCount(Edge edge) {
/* 2692 */     LOGGER.entering(DefaultClipper.class.getName(), "updateWindingCount");
/*      */     
/* 2694 */     Edge e = edge.prevInAEL;
/*      */     
/* 2696 */     while (e != null && (e.polyTyp != edge.polyTyp || e.windDelta == 0)) {
/* 2697 */       e = e.prevInAEL;
/*      */     }
/* 2699 */     if (e == null) {
/* 2700 */       edge.windCnt = (edge.windDelta == 0) ? 1 : edge.windDelta;
/* 2701 */       edge.windCnt2 = 0;
/* 2702 */       e = this.activeEdges;
/*      */     }
/* 2704 */     else if (edge.windDelta == 0 && this.clipType != Clipper.ClipType.UNION) {
/* 2705 */       edge.windCnt = 1;
/* 2706 */       edge.windCnt2 = e.windCnt2;
/* 2707 */       e = e.nextInAEL;
/*      */     }
/* 2709 */     else if (edge.isEvenOddFillType(this.clipFillType, this.subjFillType)) {
/*      */       
/* 2711 */       if (edge.windDelta == 0) {
/*      */         
/* 2713 */         boolean Inside = true;
/* 2714 */         Edge e2 = e.prevInAEL;
/* 2715 */         while (e2 != null) {
/* 2716 */           if (e2.polyTyp == e.polyTyp && e2.windDelta != 0) {
/* 2717 */             Inside = !Inside;
/*      */           }
/* 2719 */           e2 = e2.prevInAEL;
/*      */         } 
/* 2721 */         edge.windCnt = Inside ? 0 : 1;
/*      */       } else {
/*      */         
/* 2724 */         edge.windCnt = edge.windDelta;
/*      */       } 
/* 2726 */       edge.windCnt2 = e.windCnt2;
/* 2727 */       e = e.nextInAEL;
/*      */     }
/*      */     else {
/*      */       
/* 2731 */       if (e.windCnt * e.windDelta < 0) {
/*      */ 
/*      */         
/* 2734 */         if (Math.abs(e.windCnt) > 1) {
/*      */ 
/*      */           
/* 2737 */           if (e.windDelta * edge.windDelta < 0) {
/* 2738 */             edge.windCnt = e.windCnt;
/*      */           } else {
/*      */             
/* 2741 */             e.windCnt += edge.windDelta;
/*      */           }
/*      */         
/*      */         } else {
/*      */           
/* 2746 */           edge.windCnt = (edge.windDelta == 0) ? 1 : edge.windDelta;
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/* 2752 */       else if (edge.windDelta == 0) {
/* 2753 */         edge.windCnt = (e.windCnt < 0) ? (e.windCnt - 1) : (e.windCnt + 1);
/*      */       }
/* 2755 */       else if (e.windDelta * edge.windDelta < 0) {
/* 2756 */         edge.windCnt = e.windCnt;
/*      */       } else {
/*      */         
/* 2759 */         e.windCnt += edge.windDelta;
/*      */       } 
/*      */       
/* 2762 */       edge.windCnt2 = e.windCnt2;
/* 2763 */       e = e.nextInAEL;
/*      */     } 
/*      */ 
/*      */     
/* 2767 */     if (edge.isEvenOddAltFillType(this.clipFillType, this.subjFillType)) {
/*      */       
/* 2769 */       while (e != edge) {
/* 2770 */         if (e.windDelta != 0) {
/* 2771 */           edge.windCnt2 = (edge.windCnt2 == 0) ? 1 : 0;
/*      */         }
/* 2773 */         e = e.nextInAEL;
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 2778 */       while (e != edge) {
/* 2779 */         edge.windCnt2 += e.windDelta;
/* 2780 */         e = e.nextInAEL;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/clipper/DefaultClipper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */