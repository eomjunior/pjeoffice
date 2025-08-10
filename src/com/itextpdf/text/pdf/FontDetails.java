/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Utilities;
/*     */ import com.itextpdf.text.pdf.fonts.otf.Language;
/*     */ import com.itextpdf.text.pdf.languages.BanglaGlyphRepositioner;
/*     */ import com.itextpdf.text.pdf.languages.GlyphRepositioner;
/*     */ import com.itextpdf.text.pdf.languages.IndicCompositeCharacterComparator;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
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
/*     */ class FontDetails
/*     */ {
/*     */   PdfIndirectReference indirectReference;
/*     */   PdfName fontName;
/*     */   BaseFont baseFont;
/*     */   TrueTypeFontUnicode ttu;
/*     */   CJKFont cjkFont;
/*     */   byte[] shortTag;
/*     */   HashMap<Integer, int[]> longTag;
/*     */   IntHashtable cjkTag;
/*     */   int fontType;
/*     */   boolean symbolic;
/*     */   protected boolean subset = true;
/*     */   
/*     */   FontDetails(PdfName fontName, PdfIndirectReference indirectReference, BaseFont baseFont) {
/* 120 */     this.fontName = fontName;
/* 121 */     this.indirectReference = indirectReference;
/* 122 */     this.baseFont = baseFont;
/* 123 */     this.fontType = baseFont.getFontType();
/* 124 */     switch (this.fontType) {
/*     */       case 0:
/*     */       case 1:
/* 127 */         this.shortTag = new byte[256];
/*     */         break;
/*     */       case 2:
/* 130 */         this.cjkTag = new IntHashtable();
/* 131 */         this.cjkFont = (CJKFont)baseFont;
/*     */         break;
/*     */       case 3:
/* 134 */         this.longTag = (HashMap)new HashMap<Integer, int>();
/* 135 */         this.ttu = (TrueTypeFontUnicode)baseFont;
/* 136 */         this.symbolic = baseFont.isFontSpecific();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfIndirectReference getIndirectReference() {
/* 146 */     return this.indirectReference;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfName getFontName() {
/* 154 */     return this.fontName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BaseFont getBaseFont() {
/* 162 */     return this.baseFont;
/*     */   }
/*     */   
/*     */   Object[] convertToBytesGid(String gids) {
/* 166 */     if (this.fontType != 3)
/* 167 */       throw new IllegalArgumentException("GID require TT Unicode"); 
/*     */     try {
/* 169 */       StringBuilder sb = new StringBuilder();
/* 170 */       int totalWidth = 0;
/* 171 */       for (char gid : gids.toCharArray()) {
/* 172 */         int width = this.ttu.getGlyphWidth(gid);
/* 173 */         totalWidth += width;
/* 174 */         int vchar = this.ttu.GetCharFromGlyphId(gid);
/* 175 */         if (vchar != 0) {
/* 176 */           sb.append(Utilities.convertFromUtf32(vchar));
/*     */         }
/* 178 */         Integer gl = Integer.valueOf(gid);
/* 179 */         if (!this.longTag.containsKey(gl))
/* 180 */           this.longTag.put(gl, new int[] { gid, width, vchar }); 
/*     */       } 
/* 182 */       return new Object[] { gids.getBytes("UnicodeBigUnmarked"), sb.toString(), Integer.valueOf(totalWidth) };
/*     */     }
/* 184 */     catch (Exception e) {
/* 185 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] convertToBytes(String text) {
/*     */     int len, k;
/* 197 */     byte[] b = null;
/* 198 */     switch (this.fontType) {
/*     */       case 5:
/* 200 */         return this.baseFont.convertToBytes(text);
/*     */       case 0:
/*     */       case 1:
/* 203 */         b = this.baseFont.convertToBytes(text);
/* 204 */         len = b.length;
/* 205 */         for (k = 0; k < len; k++) {
/* 206 */           this.shortTag[b[k] & 0xFF] = 1;
/*     */         }
/*     */         break;
/*     */       case 2:
/* 210 */         len = text.length();
/* 211 */         if (this.cjkFont.isIdentity()) {
/* 212 */           for (k = 0; k < len; k++) {
/* 213 */             this.cjkTag.put(text.charAt(k), 0);
/*     */           }
/*     */         } else {
/*     */           
/* 217 */           for (k = 0; k < len; k++) {
/*     */             int val;
/* 219 */             if (Utilities.isSurrogatePair(text, k)) {
/* 220 */               val = Utilities.convertToUtf32(text, k);
/* 221 */               k++;
/*     */             } else {
/*     */               
/* 224 */               val = text.charAt(k);
/*     */             } 
/* 226 */             this.cjkTag.put(this.cjkFont.getCidCode(val), 0);
/*     */           } 
/*     */         } 
/* 229 */         b = this.cjkFont.convertToBytes(text);
/*     */         break;
/*     */       
/*     */       case 4:
/* 233 */         b = this.baseFont.convertToBytes(text);
/*     */         break;
/*     */       
/*     */       case 3:
/*     */         try {
/* 238 */           len = text.length();
/* 239 */           int[] metrics = null;
/* 240 */           char[] glyph = new char[len];
/* 241 */           int i = 0;
/* 242 */           if (this.symbolic)
/* 243 */           { b = PdfEncodings.convertToBytes(text, "symboltt");
/* 244 */             len = b.length;
/* 245 */             for (int j = 0; j < len; j++) {
/* 246 */               metrics = this.ttu.getMetricsTT(b[j] & 0xFF);
/* 247 */               if (metrics != null)
/*     */               
/* 249 */               { this.longTag.put(Integer.valueOf(metrics[0]), new int[] { metrics[0], metrics[1], this.ttu.getUnicodeDifferences(b[j] & 0xFF) });
/* 250 */                 glyph[i++] = (char)metrics[0]; } 
/*     */             }  }
/* 252 */           else { if (canApplyGlyphSubstitution()) {
/* 253 */               return convertToBytesAfterGlyphSubstitution(text);
/*     */             }
/* 255 */             for (int j = 0; j < len; j++) {
/*     */               int val;
/* 257 */               if (Utilities.isSurrogatePair(text, j)) {
/* 258 */                 val = Utilities.convertToUtf32(text, j);
/* 259 */                 j++;
/*     */               } else {
/*     */                 
/* 262 */                 val = text.charAt(j);
/*     */               } 
/* 264 */               metrics = this.ttu.getMetricsTT(val);
/* 265 */               if (metrics != null) {
/*     */                 
/* 267 */                 int m0 = metrics[0];
/* 268 */                 Integer gl = Integer.valueOf(m0);
/* 269 */                 if (!this.longTag.containsKey(gl))
/* 270 */                   this.longTag.put(gl, new int[] { m0, metrics[1], val }); 
/* 271 */                 glyph[i++] = (char)m0;
/*     */               } 
/*     */             }  }
/* 274 */            glyph = Utilities.copyOfRange(glyph, 0, i);
/* 275 */           b = StringUtils.convertCharsToBytes(glyph);
/*     */         }
/* 277 */         catch (UnsupportedEncodingException e) {
/* 278 */           throw new ExceptionConverter(e);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */     
/* 283 */     return b;
/*     */   }
/*     */   
/*     */   private boolean canApplyGlyphSubstitution() {
/* 287 */     return (this.fontType == 3 && this.ttu.getGlyphSubstitutionMap() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private byte[] convertToBytesAfterGlyphSubstitution(String text) throws UnsupportedEncodingException {
/* 292 */     if (!canApplyGlyphSubstitution()) {
/* 293 */       throw new IllegalArgumentException("Make sure the font type if TTF Unicode and a valid GlyphSubstitutionTable exists!");
/*     */     }
/*     */     
/* 296 */     Map<String, Glyph> glyphSubstitutionMap = this.ttu.getGlyphSubstitutionMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 301 */     Set<String> compositeCharacters = new TreeSet<String>((Comparator<? super String>)new IndicCompositeCharacterComparator());
/* 302 */     compositeCharacters.addAll(glyphSubstitutionMap.keySet());
/*     */ 
/*     */     
/* 305 */     ArrayBasedStringTokenizer tokenizer = new ArrayBasedStringTokenizer(compositeCharacters.<String>toArray(new String[0]));
/* 306 */     String[] tokens = tokenizer.tokenize(text);
/*     */     
/* 308 */     List<Glyph> glyphList = new ArrayList<Glyph>(50);
/*     */     
/* 310 */     for (String token : tokens) {
/*     */ 
/*     */       
/* 313 */       Glyph subsGlyph = glyphSubstitutionMap.get(token);
/*     */       
/* 315 */       if (subsGlyph != null) {
/* 316 */         glyphList.add(subsGlyph);
/*     */       } else {
/*     */         
/* 319 */         for (char c : token.toCharArray()) {
/* 320 */           int[] metrics = this.ttu.getMetricsTT(c);
/* 321 */           int glyphCode = metrics[0];
/* 322 */           int glyphWidth = metrics[1];
/* 323 */           glyphList.add(new Glyph(glyphCode, glyphWidth, String.valueOf(c)));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 329 */     GlyphRepositioner glyphRepositioner = getGlyphRepositioner();
/*     */     
/* 331 */     if (glyphRepositioner != null) {
/* 332 */       glyphRepositioner.repositionGlyphs(glyphList);
/*     */     }
/*     */     
/* 335 */     char[] charEncodedGlyphCodes = new char[glyphList.size()];
/*     */ 
/*     */     
/* 338 */     for (int i = 0; i < glyphList.size(); i++) {
/* 339 */       Glyph glyph = glyphList.get(i);
/* 340 */       charEncodedGlyphCodes[i] = (char)glyph.code;
/* 341 */       Integer glyphCode = Integer.valueOf(glyph.code);
/*     */       
/* 343 */       if (!this.longTag.containsKey(glyphCode))
/*     */       {
/* 345 */         this.longTag.put(glyphCode, new int[] { glyph.code, glyph.width, glyph.chars.charAt(0) });
/*     */       }
/*     */     } 
/*     */     
/* 349 */     return (new String(charEncodedGlyphCodes)).getBytes("UnicodeBigUnmarked");
/*     */   }
/*     */   
/*     */   private GlyphRepositioner getGlyphRepositioner() {
/* 353 */     Language language = this.ttu.getSupportedLanguage();
/*     */     
/* 355 */     if (language == null) {
/* 356 */       throw new IllegalArgumentException("The supported language field cannot be null in " + this.ttu.getClass().getName());
/*     */     }
/*     */     
/* 359 */     switch (language) {
/*     */       case BENGALI:
/* 361 */         return (GlyphRepositioner)new BanglaGlyphRepositioner(Collections.unmodifiableMap(this.ttu.cmap31), this.ttu.getGlyphSubstitutionMap());
/*     */     } 
/* 363 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeFont(PdfWriter writer) {
/*     */     try {
/*     */       int firstChar;
/*     */       int lastChar;
/* 373 */       switch (this.fontType) {
/*     */         case 5:
/* 375 */           this.baseFont.writeFont(writer, this.indirectReference, null);
/*     */           break;
/*     */ 
/*     */         
/*     */         case 0:
/*     */         case 1:
/* 381 */           for (firstChar = 0; firstChar < 256 && 
/* 382 */             this.shortTag[firstChar] == 0; firstChar++);
/*     */ 
/*     */           
/* 385 */           for (lastChar = 255; lastChar >= firstChar && 
/* 386 */             this.shortTag[lastChar] == 0; lastChar--);
/*     */ 
/*     */           
/* 389 */           if (firstChar > 255) {
/* 390 */             firstChar = 255;
/* 391 */             lastChar = 255;
/*     */           } 
/* 393 */           this.baseFont.writeFont(writer, this.indirectReference, new Object[] { Integer.valueOf(firstChar), Integer.valueOf(lastChar), this.shortTag, Boolean.valueOf(this.subset) });
/*     */           break;
/*     */         
/*     */         case 2:
/* 397 */           this.baseFont.writeFont(writer, this.indirectReference, new Object[] { this.cjkTag });
/*     */           break;
/*     */         case 3:
/* 400 */           this.baseFont.writeFont(writer, this.indirectReference, new Object[] { this.longTag, Boolean.valueOf(this.subset) });
/*     */           break;
/*     */       } 
/*     */     
/* 404 */     } catch (Exception e) {
/* 405 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSubset() {
/* 415 */     return this.subset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubset(boolean subset) {
/* 425 */     this.subset = subset;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/FontDetails.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */