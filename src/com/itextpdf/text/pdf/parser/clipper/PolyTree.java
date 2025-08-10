/*    */ package com.itextpdf.text.pdf.parser.clipper;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class PolyTree
/*    */   extends PolyNode
/*    */ {
/* 50 */   private final List<PolyNode> allPolys = new ArrayList<PolyNode>();
/*    */   
/*    */   public void Clear() {
/* 53 */     this.allPolys.clear();
/* 54 */     this.childs.clear();
/*    */   }
/*    */   
/*    */   public List<PolyNode> getAllPolys() {
/* 58 */     return this.allPolys;
/*    */   }
/*    */   
/*    */   public PolyNode getFirst() {
/* 62 */     if (!this.childs.isEmpty()) {
/* 63 */       return this.childs.get(0);
/*    */     }
/*    */     
/* 66 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTotalSize() {
/* 71 */     int result = this.allPolys.size();
/*    */     
/* 73 */     if (result > 0 && this.childs.get(0) != this.allPolys.get(0)) {
/* 74 */       result--;
/*    */     }
/* 76 */     return result;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/clipper/PolyTree.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */