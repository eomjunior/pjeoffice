/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import java.util.HashMap;
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
/*     */ public class PdfAppearance
/*     */   extends PdfTemplate
/*     */ {
/*  55 */   public static final HashMap<String, PdfName> stdFieldFontNames = new HashMap<String, PdfName>();
/*     */   static {
/*  57 */     stdFieldFontNames.put("Courier-BoldOblique", new PdfName("CoBO"));
/*  58 */     stdFieldFontNames.put("Courier-Bold", new PdfName("CoBo"));
/*  59 */     stdFieldFontNames.put("Courier-Oblique", new PdfName("CoOb"));
/*  60 */     stdFieldFontNames.put("Courier", new PdfName("Cour"));
/*  61 */     stdFieldFontNames.put("Helvetica-BoldOblique", new PdfName("HeBO"));
/*  62 */     stdFieldFontNames.put("Helvetica-Bold", new PdfName("HeBo"));
/*  63 */     stdFieldFontNames.put("Helvetica-Oblique", new PdfName("HeOb"));
/*  64 */     stdFieldFontNames.put("Helvetica", PdfName.HELV);
/*  65 */     stdFieldFontNames.put("Symbol", new PdfName("Symb"));
/*  66 */     stdFieldFontNames.put("Times-BoldItalic", new PdfName("TiBI"));
/*  67 */     stdFieldFontNames.put("Times-Bold", new PdfName("TiBo"));
/*  68 */     stdFieldFontNames.put("Times-Italic", new PdfName("TiIt"));
/*  69 */     stdFieldFontNames.put("Times-Roman", new PdfName("TiRo"));
/*  70 */     stdFieldFontNames.put("ZapfDingbats", PdfName.ZADB);
/*  71 */     stdFieldFontNames.put("HYSMyeongJo-Medium", new PdfName("HySm"));
/*  72 */     stdFieldFontNames.put("HYGoThic-Medium", new PdfName("HyGo"));
/*  73 */     stdFieldFontNames.put("HeiseiKakuGo-W5", new PdfName("KaGo"));
/*  74 */     stdFieldFontNames.put("HeiseiMin-W3", new PdfName("KaMi"));
/*  75 */     stdFieldFontNames.put("MHei-Medium", new PdfName("MHei"));
/*  76 */     stdFieldFontNames.put("MSung-Light", new PdfName("MSun"));
/*  77 */     stdFieldFontNames.put("STSong-Light", new PdfName("STSo"));
/*  78 */     stdFieldFontNames.put("MSungStd-Light", new PdfName("MSun"));
/*  79 */     stdFieldFontNames.put("STSongStd-Light", new PdfName("STSo"));
/*  80 */     stdFieldFontNames.put("HYSMyeongJoStd-Medium", new PdfName("HySm"));
/*  81 */     stdFieldFontNames.put("KozMinPro-Regular", new PdfName("KaMi"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfAppearance() {
/*  90 */     this.separator = 32;
/*     */   }
/*     */   
/*     */   PdfAppearance(PdfIndirectReference iref) {
/*  94 */     this.thisReference = iref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfAppearance(PdfWriter wr) {
/* 104 */     super(wr);
/* 105 */     this.separator = 32;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PdfAppearance createAppearance(PdfWriter writer, float width, float height) {
/* 117 */     return createAppearance(writer, width, height, (PdfName)null);
/*     */   }
/*     */   
/*     */   static PdfAppearance createAppearance(PdfWriter writer, float width, float height, PdfName forcedName) {
/* 121 */     PdfAppearance template = new PdfAppearance(writer);
/* 122 */     template.setWidth(width);
/* 123 */     template.setHeight(height);
/* 124 */     writer.addDirectTemplateSimple(template, forcedName);
/* 125 */     return template;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFontAndSize(BaseFont bf, float size) {
/* 136 */     checkWriter();
/* 137 */     this.state.size = size;
/* 138 */     if (bf.getFontType() == 4) {
/* 139 */       this.state.fontDetails = new FontDetails(null, ((DocumentFont)bf).getIndirectReference(), bf);
/*     */     } else {
/*     */       
/* 142 */       this.state.fontDetails = this.writer.addSimple(bf);
/* 143 */     }  PdfName psn = stdFieldFontNames.get(bf.getPostscriptFontName());
/* 144 */     if (psn == null) {
/* 145 */       if (bf.isSubset() && bf.getFontType() == 3) {
/* 146 */         psn = this.state.fontDetails.getFontName();
/*     */       } else {
/* 148 */         psn = new PdfName(bf.getPostscriptFontName());
/* 149 */         this.state.fontDetails.setSubset(false);
/*     */       } 
/*     */     }
/* 152 */     PageResources prs = getPageResources();
/*     */     
/* 154 */     prs.addFont(psn, this.state.fontDetails.getIndirectReference());
/* 155 */     this.content.append(psn.getBytes()).append(' ').append(size).append(" Tf").append_i(this.separator);
/*     */   }
/*     */ 
/*     */   
/*     */   public PdfContentByte getDuplicate() {
/* 160 */     PdfAppearance tpl = new PdfAppearance();
/* 161 */     tpl.writer = this.writer;
/* 162 */     tpl.pdf = this.pdf;
/* 163 */     tpl.thisReference = this.thisReference;
/* 164 */     tpl.pageResources = this.pageResources;
/* 165 */     tpl.bBox = new Rectangle(this.bBox);
/* 166 */     tpl.group = this.group;
/* 167 */     tpl.layer = this.layer;
/* 168 */     if (this.matrix != null) {
/* 169 */       tpl.matrix = new PdfArray(this.matrix);
/*     */     }
/* 171 */     tpl.separator = this.separator;
/* 172 */     return tpl;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfAppearance.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */