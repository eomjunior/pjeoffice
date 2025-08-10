/*     */ package com.itextpdf.text.pdf.draw;
/*     */ 
/*     */ import com.itextpdf.text.Chunk;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.Element;
/*     */ import com.itextpdf.text.ElementListener;
/*     */ import com.itextpdf.text.pdf.PdfContentByte;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VerticalPositionMark
/*     */   implements DrawInterface, Element
/*     */ {
/*  65 */   protected DrawInterface drawInterface = null;
/*     */ 
/*     */   
/*  68 */   protected float offset = 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VerticalPositionMark() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VerticalPositionMark(DrawInterface drawInterface, float offset) {
/*  84 */     this.drawInterface = drawInterface;
/*  85 */     this.offset = offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
/*  92 */     if (this.drawInterface != null) {
/*  93 */       this.drawInterface.draw(canvas, llx, lly, urx, ury, y + this.offset);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean process(ElementListener listener) {
/*     */     try {
/* 102 */       return listener.add(this);
/* 103 */     } catch (DocumentException e) {
/* 104 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int type() {
/* 112 */     return 55;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isContent() {
/* 119 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNestable() {
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Chunk> getChunks() {
/* 133 */     List<Chunk> list = new ArrayList<Chunk>();
/* 134 */     list.add(new Chunk(this, true));
/* 135 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DrawInterface getDrawInterface() {
/* 143 */     return this.drawInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDrawInterface(DrawInterface drawInterface) {
/* 151 */     this.drawInterface = drawInterface;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getOffset() {
/* 159 */     return this.offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOffset(float offset) {
/* 169 */     this.offset = offset;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/draw/VerticalPositionMark.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */