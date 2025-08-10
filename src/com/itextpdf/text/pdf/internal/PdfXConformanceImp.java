/*     */ package com.itextpdf.text.pdf.internal;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.BaseFont;
/*     */ import com.itextpdf.text.pdf.ExtendedColor;
/*     */ import com.itextpdf.text.pdf.PatternColor;
/*     */ import com.itextpdf.text.pdf.PdfArray;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfGState;
/*     */ import com.itextpdf.text.pdf.PdfImage;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfNumber;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfWriter;
/*     */ import com.itextpdf.text.pdf.PdfXConformanceException;
/*     */ import com.itextpdf.text.pdf.ShadingColor;
/*     */ import com.itextpdf.text.pdf.SpotColor;
/*     */ import com.itextpdf.text.pdf.interfaces.PdfXConformance;
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
/*     */ public class PdfXConformanceImp
/*     */   implements PdfXConformance
/*     */ {
/*  70 */   protected int pdfxConformance = 0;
/*     */   protected PdfWriter writer;
/*     */   
/*     */   public PdfXConformanceImp(PdfWriter writer) {
/*  74 */     this.writer = writer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPDFXConformance(int pdfxConformance) {
/*  81 */     this.pdfxConformance = pdfxConformance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPDFXConformance() {
/*  88 */     return this.pdfxConformance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPdfIso() {
/*  95 */     return isPdfX();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPdfX() {
/* 103 */     return (this.pdfxConformance != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPdfX1A2001() {
/* 110 */     return (this.pdfxConformance == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPdfX32002() {
/* 117 */     return (this.pdfxConformance == 2);
/*     */   }
/*     */   
/*     */   public void checkPdfIsoConformance(int key, Object obj1) {
/*     */     PdfImage image;
/*     */     PdfObject cs;
/*     */     PdfDictionary gs;
/*     */     PdfObject obj;
/*     */     double v;
/* 126 */     if (this.writer == null || !this.writer.isPdfX())
/*     */       return; 
/* 128 */     int conf = this.writer.getPDFXConformance();
/* 129 */     switch (key) {
/*     */       case 1:
/* 131 */         switch (conf) {
/*     */           case 1:
/* 133 */             if (obj1 instanceof ExtendedColor) {
/* 134 */               SpotColor sc; ShadingColor xc; PatternColor pc; ExtendedColor ec = (ExtendedColor)obj1;
/* 135 */               switch (ec.getType()) {
/*     */                 case 1:
/*     */                 case 2:
/*     */                   return;
/*     */                 case 0:
/* 140 */                   throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed", new Object[0]));
/*     */                 case 3:
/* 142 */                   sc = (SpotColor)ec;
/* 143 */                   checkPdfIsoConformance(1, sc.getPdfSpotColor().getAlternativeCS());
/*     */                   break;
/*     */                 case 5:
/* 146 */                   xc = (ShadingColor)ec;
/* 147 */                   checkPdfIsoConformance(1, xc.getPdfShadingPattern().getShading().getColorSpace());
/*     */                   break;
/*     */                 case 4:
/* 150 */                   pc = (PatternColor)ec;
/* 151 */                   checkPdfIsoConformance(1, pc.getPainter().getDefaultColor()); break;
/*     */               } 
/*     */               break;
/*     */             } 
/* 155 */             if (obj1 instanceof com.itextpdf.text.BaseColor) {
/* 156 */               throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed", new Object[0]));
/*     */             }
/*     */             break;
/*     */         } 
/*     */         
/*     */         break;
/*     */       case 3:
/* 163 */         if (conf == 1)
/* 164 */           throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed", new Object[0])); 
/*     */         break;
/*     */       case 4:
/* 167 */         if (!((BaseFont)obj1).isEmbedded())
/* 168 */           throw new PdfXConformanceException(MessageLocalization.getComposedMessage("all.the.fonts.must.be.embedded.this.one.isn.t.1", new Object[] { ((BaseFont)obj1).getPostscriptFontName() })); 
/*     */         break;
/*     */       case 5:
/* 171 */         image = (PdfImage)obj1;
/* 172 */         if (image.get(PdfName.SMASK) != null)
/* 173 */           throw new PdfXConformanceException(MessageLocalization.getComposedMessage("the.smask.key.is.not.allowed.in.images", new Object[0])); 
/* 174 */         switch (conf) {
/*     */           case 1:
/* 176 */             cs = image.get(PdfName.COLORSPACE);
/* 177 */             if (cs == null)
/*     */               return; 
/* 179 */             if (cs.isName()) {
/* 180 */               if (PdfName.DEVICERGB.equals(cs))
/* 181 */                 throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed", new Object[0]));  break;
/*     */             } 
/* 183 */             if (cs.isArray() && 
/* 184 */               PdfName.CALRGB.equals(((PdfArray)cs).getPdfObject(0))) {
/* 185 */               throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.calrgb.is.not.allowed", new Object[0]));
/*     */             }
/*     */             break;
/*     */         } 
/*     */         break;
/*     */       case 6:
/* 191 */         gs = (PdfDictionary)obj1;
/*     */         
/* 193 */         if (gs == null)
/*     */           break; 
/* 195 */         obj = gs.get(PdfName.BM);
/* 196 */         if (obj != null && !PdfGState.BM_NORMAL.equals(obj) && !PdfGState.BM_COMPATIBLE.equals(obj))
/* 197 */           throw new PdfXConformanceException(MessageLocalization.getComposedMessage("blend.mode.1.not.allowed", new Object[] { obj.toString() })); 
/* 198 */         obj = gs.get(PdfName.CA);
/* 199 */         v = 0.0D;
/* 200 */         if (obj != null && (v = ((PdfNumber)obj).doubleValue()) != 1.0D)
/* 201 */           throw new PdfXConformanceException(MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1", new Object[] { String.valueOf(v) })); 
/* 202 */         obj = gs.get(PdfName.ca);
/* 203 */         v = 0.0D;
/* 204 */         if (obj != null && (v = ((PdfNumber)obj).doubleValue()) != 1.0D)
/* 205 */           throw new PdfXConformanceException(MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1", new Object[] { String.valueOf(v) })); 
/*     */         break;
/*     */       case 7:
/* 208 */         throw new PdfXConformanceException(MessageLocalization.getComposedMessage("layers.are.not.allowed", new Object[0]));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/internal/PdfXConformanceImp.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */