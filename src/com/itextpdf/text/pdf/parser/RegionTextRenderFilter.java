/*    */ package com.itextpdf.text.pdf.parser;
/*    */ 
/*    */ import com.itextpdf.awt.geom.Rectangle;
/*    */ import com.itextpdf.awt.geom.Rectangle2D;
/*    */ import com.itextpdf.text.Rectangle;
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
/*    */ public class RegionTextRenderFilter
/*    */   extends RenderFilter
/*    */ {
/*    */   private final Rectangle2D filterRect;
/*    */   
/*    */   public RegionTextRenderFilter(Rectangle2D filterRect) {
/* 63 */     this.filterRect = filterRect;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RegionTextRenderFilter(Rectangle filterRect) {
/* 71 */     this.filterRect = (Rectangle2D)new Rectangle(filterRect);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean allowText(TextRenderInfo renderInfo) {
/* 77 */     LineSegment segment = renderInfo.getBaseline();
/* 78 */     Vector startPoint = segment.getStartPoint();
/* 79 */     Vector endPoint = segment.getEndPoint();
/*    */     
/* 81 */     float x1 = startPoint.get(0);
/* 82 */     float y1 = startPoint.get(1);
/* 83 */     float x2 = endPoint.get(0);
/* 84 */     float y2 = endPoint.get(1);
/*    */     
/* 86 */     return this.filterRect.intersectsLine(x1, y1, x2, y2);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/RegionTextRenderFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */