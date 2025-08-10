/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.Chunk;
/*     */ import com.itextpdf.text.Element;
/*     */ import com.itextpdf.text.Font;
/*     */ import com.itextpdf.text.Phrase;
/*     */ import com.itextpdf.text.Utilities;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
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
/*     */ public class FontSelector
/*     */ {
/*  67 */   private static final Logger LOGGER = LoggerFactory.getLogger(PdfSmartCopy.class);
/*     */   
/*  69 */   protected ArrayList<Font> fonts = new ArrayList<Font>();
/*  70 */   protected ArrayList<Font> unsupportedFonts = new ArrayList<Font>();
/*  71 */   protected Font currentFont = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFont(Font font) {
/*  78 */     if (!isSupported(font)) {
/*  79 */       this.unsupportedFonts.add(font);
/*     */       return;
/*     */     } 
/*  82 */     if (font.getBaseFont() != null) {
/*  83 */       this.fonts.add(font);
/*     */       return;
/*     */     } 
/*  86 */     BaseFont bf = font.getCalculatedBaseFont(true);
/*  87 */     Font f2 = new Font(bf, font.getSize(), font.getCalculatedStyle(), font.getColor());
/*  88 */     this.fonts.add(f2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Phrase process(String text) {
/*  98 */     if (getSize() == 0)
/*  99 */       throw new IndexOutOfBoundsException(MessageLocalization.getComposedMessage("no.font.is.defined", new Object[0])); 
/* 100 */     char[] cc = text.toCharArray();
/* 101 */     int len = cc.length;
/* 102 */     StringBuffer sb = new StringBuffer();
/* 103 */     Phrase ret = new Phrase();
/* 104 */     this.currentFont = null;
/* 105 */     for (int k = 0; k < len; k++) {
/* 106 */       Chunk newChunk = processChar(cc, k, sb);
/* 107 */       if (newChunk != null) {
/* 108 */         ret.add((Element)newChunk);
/*     */       }
/*     */     } 
/* 111 */     if (sb.length() > 0) {
/* 112 */       Chunk ck = new Chunk(sb.toString(), (this.currentFont != null) ? this.currentFont : getFont(0));
/* 113 */       ret.add((Element)ck);
/*     */     } 
/* 115 */     return ret;
/*     */   }
/*     */   
/*     */   protected Chunk processChar(char[] cc, int k, StringBuffer sb) {
/* 119 */     Chunk newChunk = null;
/* 120 */     char c = cc[k];
/* 121 */     if (c == '\n' || c == '\r') {
/* 122 */       sb.append(c);
/*     */     } else {
/* 124 */       Font font = null;
/* 125 */       if (Utilities.isSurrogatePair(cc, k)) {
/* 126 */         int u = Utilities.convertToUtf32(cc, k);
/* 127 */         for (int f = 0; f < getSize(); f++) {
/* 128 */           font = getFont(f);
/* 129 */           if (font.getBaseFont().charExists(u) || Character.getType(u) == 16) {
/* 130 */             if (this.currentFont != font) {
/* 131 */               if (sb.length() > 0 && this.currentFont != null) {
/* 132 */                 newChunk = new Chunk(sb.toString(), this.currentFont);
/* 133 */                 sb.setLength(0);
/*     */               } 
/* 135 */               this.currentFont = font;
/*     */             } 
/* 137 */             sb.append(c);
/* 138 */             sb.append(cc[++k]);
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } else {
/* 143 */         for (int f = 0; f < getSize(); f++) {
/* 144 */           font = getFont(f);
/* 145 */           if (font.getBaseFont().charExists(c) || Character.getType(c) == 16) {
/* 146 */             if (this.currentFont != font) {
/* 147 */               if (sb.length() > 0 && this.currentFont != null) {
/* 148 */                 newChunk = new Chunk(sb.toString(), this.currentFont);
/* 149 */                 sb.setLength(0);
/*     */               } 
/* 151 */               this.currentFont = font;
/*     */             } 
/* 153 */             sb.append(c);
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 159 */     return newChunk;
/*     */   }
/*     */   
/*     */   protected int getSize() {
/* 163 */     return this.fonts.size() + this.unsupportedFonts.size();
/*     */   }
/*     */   
/*     */   protected Font getFont(int i) {
/* 167 */     return (i < this.fonts.size()) ? this.fonts
/* 168 */       .get(i) : this.unsupportedFonts
/* 169 */       .get(i - this.fonts.size());
/*     */   }
/*     */   
/*     */   private boolean isSupported(Font font) {
/* 173 */     BaseFont bf = font.getBaseFont();
/* 174 */     if (bf instanceof TrueTypeFont && "Cp1252".equals(bf.getEncoding()) && !((TrueTypeFont)bf).isWinAnsiSupported()) {
/* 175 */       LOGGER.warn(MessageFormat.format("cmap(1, 0) not found for TrueType Font {0}, it is required for WinAnsi encoding.", new Object[] { font }));
/* 176 */       return false;
/*     */     } 
/* 178 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/FontSelector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */