/*    */ package com.itextpdf.text.pdf.parser.clipper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Clipper
/*    */ {
/*    */   public static final int REVERSE_SOLUTION = 1;
/*    */   public static final int STRICTLY_SIMPLE = 2;
/*    */   public static final int PRESERVE_COLINEAR = 4;
/*    */   
/*    */   boolean addPath(Path paramPath, PolyType paramPolyType, boolean paramBoolean);
/*    */   
/*    */   boolean addPaths(Paths paramPaths, PolyType paramPolyType, boolean paramBoolean);
/*    */   
/*    */   void clear();
/*    */   
/*    */   boolean execute(ClipType paramClipType, Paths paramPaths);
/*    */   
/*    */   boolean execute(ClipType paramClipType, Paths paramPaths, PolyFillType paramPolyFillType1, PolyFillType paramPolyFillType2);
/*    */   
/*    */   boolean execute(ClipType paramClipType, PolyTree paramPolyTree);
/*    */   
/*    */   boolean execute(ClipType paramClipType, PolyTree paramPolyTree, PolyFillType paramPolyFillType1, PolyFillType paramPolyFillType2);
/*    */   
/*    */   public enum ClipType
/*    */   {
/* 50 */     INTERSECTION, UNION, DIFFERENCE, XOR;
/*    */   }
/*    */   
/*    */   public enum Direction {
/* 54 */     RIGHT_TO_LEFT, LEFT_TO_RIGHT;
/*    */   }
/*    */   
/*    */   public enum EndType {
/* 58 */     CLOSED_POLYGON, CLOSED_LINE, OPEN_BUTT, OPEN_SQUARE, OPEN_ROUND;
/*    */   }
/*    */   
/*    */   public enum JoinType {
/* 62 */     BEVEL, ROUND, MITER;
/*    */   }
/*    */   
/*    */   public enum PolyFillType {
/* 66 */     EVEN_ODD, NON_ZERO, POSITIVE, NEGATIVE;
/*    */   }
/*    */   
/*    */   public enum PolyType {
/* 70 */     SUBJECT, CLIP;
/*    */   }
/*    */   
/*    */   public static interface ZFillCallback {
/*    */     void zFill(Point.LongPoint param1LongPoint1, Point.LongPoint param1LongPoint2, Point.LongPoint param1LongPoint3, Point.LongPoint param1LongPoint4, Point.LongPoint param1LongPoint5);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/clipper/Clipper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */