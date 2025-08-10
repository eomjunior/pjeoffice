/*    */ package com.itextpdf.text.pdf;
/*    */ 
/*    */ import com.itextpdf.text.Chunk;
/*    */ import com.itextpdf.text.Element;
/*    */ import com.itextpdf.text.ElementListener;
/*    */ import com.itextpdf.text.Rectangle;
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
/*    */ 
/*    */ public class PdfBody
/*    */   extends Rectangle
/*    */   implements Element
/*    */ {
/*    */   public PdfBody(Rectangle rectangle) {
/* 56 */     super(rectangle);
/*    */   }
/*    */   
/*    */   public boolean process(ElementListener listener) {
/* 60 */     return false;
/*    */   }
/*    */   
/*    */   public int type() {
/* 64 */     return 38;
/*    */   }
/*    */   
/*    */   public boolean isContent() {
/* 68 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isNestable() {
/* 72 */     return false;
/*    */   }
/*    */   
/*    */   public List<Chunk> getChunks() {
/* 76 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfBody.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */