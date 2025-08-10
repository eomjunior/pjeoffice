/*     */ package com.itextpdf.text.pdf.languages;
/*     */ 
/*     */ import com.itextpdf.text.pdf.Glyph;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class BanglaGlyphRepositioner
/*     */   extends IndicGlyphRepositioner
/*     */ {
/*  58 */   private static final String[] CHARCTERS_TO_BE_SHIFTED_LEFT_BY_1 = new String[] { "ি", "ে", "ৈ" };
/*     */   
/*     */   private final Map<Integer, int[]> cmap31;
/*     */   
/*     */   private final Map<String, Glyph> glyphSubstitutionMap;
/*     */   
/*     */   public BanglaGlyphRepositioner(Map<Integer, int[]> cmap31, Map<String, Glyph> glyphSubstitutionMap) {
/*  65 */     this.cmap31 = cmap31;
/*  66 */     this.glyphSubstitutionMap = glyphSubstitutionMap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void repositionGlyphs(List<Glyph> glyphList) {
/*  72 */     for (int i = 0; i < glyphList.size(); i++) {
/*  73 */       Glyph glyph = glyphList.get(i);
/*     */       
/*  75 */       if (glyph.chars.equals("ো")) {
/*  76 */         handleOKaarAndOUKaar(i, glyphList, 'ে', 'া');
/*  77 */       } else if (glyph.chars.equals("ৌ")) {
/*  78 */         handleOKaarAndOUKaar(i, glyphList, 'ে', 'ৗ');
/*     */       } 
/*     */     } 
/*     */     
/*  82 */     super.repositionGlyphs(glyphList);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getCharactersToBeShiftedLeftByOnePosition() {
/*  87 */     return Arrays.asList(CHARCTERS_TO_BE_SHIFTED_LEFT_BY_1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleOKaarAndOUKaar(int currentIndex, List<Glyph> glyphList, char first, char second) {
/*  97 */     Glyph g1 = getGlyph(first);
/*  98 */     Glyph g2 = getGlyph(second);
/*  99 */     glyphList.set(currentIndex, g1);
/* 100 */     glyphList.add(currentIndex + 1, g2);
/*     */   }
/*     */ 
/*     */   
/*     */   private Glyph getGlyph(char c) {
/* 105 */     Glyph glyph = this.glyphSubstitutionMap.get(String.valueOf(c));
/*     */     
/* 107 */     if (glyph != null) {
/* 108 */       return glyph;
/*     */     }
/*     */     
/* 111 */     int[] metrics = this.cmap31.get(Integer.valueOf(c));
/* 112 */     int glyphCode = metrics[0];
/* 113 */     int glyphWidth = metrics[1];
/* 114 */     return new Glyph(glyphCode, glyphWidth, String.valueOf(c));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/languages/BanglaGlyphRepositioner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */