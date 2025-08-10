/*    */ package com.itextpdf.awt;
/*    */ 
/*    */ import com.itextpdf.text.pdf.BaseFont;
/*    */ import java.awt.Font;
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
/*    */ public class AsianFontMapper
/*    */   extends DefaultFontMapper
/*    */ {
/*    */   public static final String ChineseSimplifiedFont = "STSong-Light";
/*    */   public static final String ChineseSimplifiedEncoding_H = "UniGB-UCS2-H";
/*    */   public static final String ChineseSimplifiedEncoding_V = "UniGB-UCS2-V";
/*    */   public static final String ChineseTraditionalFont_MHei = "MHei-Medium";
/*    */   public static final String ChineseTraditionalFont_MSung = "MSung-Light";
/*    */   public static final String ChineseTraditionalEncoding_H = "UniCNS-UCS2-H";
/*    */   public static final String ChineseTraditionalEncoding_V = "UniCNS-UCS2-V";
/*    */   public static final String JapaneseFont_Go = "HeiseiKakuGo-W5";
/*    */   public static final String JapaneseFont_Min = "HeiseiMin-W3";
/*    */   public static final String JapaneseEncoding_H = "UniJIS-UCS2-H";
/*    */   public static final String JapaneseEncoding_V = "UniJIS-UCS2-V";
/*    */   public static final String JapaneseEncoding_HW_H = "UniJIS-UCS2-HW-H";
/*    */   public static final String JapaneseEncoding_HW_V = "UniJIS-UCS2-HW-V";
/*    */   public static final String KoreanFont_GoThic = "HYGoThic-Medium";
/*    */   public static final String KoreanFont_SMyeongJo = "HYSMyeongJo-Medium";
/*    */   public static final String KoreanEncoding_H = "UniKS-UCS2-H";
/*    */   public static final String KoreanEncoding_V = "UniKS-UCS2-V";
/*    */   private final String defaultFont;
/*    */   private final String encoding;
/*    */   
/*    */   public AsianFontMapper(String font, String encoding) {
/* 79 */     this.defaultFont = font;
/* 80 */     this.encoding = encoding;
/*    */   }
/*    */   
/*    */   public BaseFont awtToPdf(Font font) {
/*    */     try {
/* 85 */       DefaultFontMapper.BaseFontParameters p = getBaseFontParameters(font.getFontName());
/* 86 */       if (p != null) {
/* 87 */         return BaseFont.createFont(p.fontName, p.encoding, p.embedded, p.cached, p.ttfAfm, p.pfb);
/*    */       }
/* 89 */       return BaseFont.createFont(this.defaultFont, this.encoding, true);
/*    */     
/*    */     }
/* 92 */     catch (Exception e) {
/* 93 */       e.printStackTrace();
/*    */       
/* 95 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/AsianFontMapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */