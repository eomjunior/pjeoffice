/*    */ package com.itextpdf.text.pdf.languages;
/*    */ 
/*    */ import com.itextpdf.text.pdf.Glyph;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class IndicGlyphRepositioner
/*    */   implements GlyphRepositioner
/*    */ {
/*    */   public void repositionGlyphs(List<Glyph> glyphList) {
/* 58 */     for (int i = 0; i < glyphList.size(); i++) {
/* 59 */       Glyph glyph = glyphList.get(i);
/* 60 */       Glyph nextGlyph = getNextGlyph(glyphList, i);
/*    */       
/* 62 */       if (nextGlyph != null && 
/* 63 */         getCharactersToBeShiftedLeftByOnePosition().contains(nextGlyph.chars)) {
/*    */         
/* 65 */         glyphList.set(i, nextGlyph);
/* 66 */         glyphList.set(i + 1, glyph);
/* 67 */         i++;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   abstract List<String> getCharactersToBeShiftedLeftByOnePosition();
/*    */ 
/*    */   
/*    */   private Glyph getNextGlyph(List<Glyph> glyphs, int currentIndex) {
/* 77 */     if (currentIndex + 1 < glyphs.size()) {
/* 78 */       return glyphs.get(currentIndex + 1);
/*    */     }
/* 80 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/languages/IndicGlyphRepositioner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */