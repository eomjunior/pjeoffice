/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.pdf.CMapAwareDocumentFont;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GraphicsState
/*     */ {
/*     */   Matrix ctm;
/*     */   float characterSpacing;
/*     */   float wordSpacing;
/*     */   float horizontalScaling;
/*     */   float leading;
/*     */   CMapAwareDocumentFont font;
/*     */   float fontSize;
/*     */   int renderMode;
/*     */   float rise;
/*     */   boolean knockout;
/*     */   PdfName colorSpaceFill;
/*     */   PdfName colorSpaceStroke;
/*  81 */   BaseColor fillColor = BaseColor.BLACK;
/*     */   
/*  83 */   BaseColor strokeColor = BaseColor.BLACK;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private float lineWidth;
/*     */ 
/*     */ 
/*     */   
/*     */   private int lineCapStyle;
/*     */ 
/*     */ 
/*     */   
/*     */   private int lineJoinStyle;
/*     */ 
/*     */ 
/*     */   
/*     */   private float miterLimit;
/*     */ 
/*     */ 
/*     */   
/*     */   private LineDashPattern lineDashPattern;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GraphicsState() {
/* 110 */     this.ctm = new Matrix();
/* 111 */     this.characterSpacing = 0.0F;
/* 112 */     this.wordSpacing = 0.0F;
/* 113 */     this.horizontalScaling = 1.0F;
/* 114 */     this.leading = 0.0F;
/* 115 */     this.font = null;
/* 116 */     this.fontSize = 0.0F;
/* 117 */     this.renderMode = 0;
/* 118 */     this.rise = 0.0F;
/* 119 */     this.knockout = true;
/* 120 */     this.colorSpaceFill = null;
/* 121 */     this.colorSpaceStroke = null;
/* 122 */     this.fillColor = null;
/* 123 */     this.strokeColor = null;
/* 124 */     this.lineWidth = 1.0F;
/* 125 */     this.lineCapStyle = 0;
/* 126 */     this.lineJoinStyle = 0;
/* 127 */     this.miterLimit = 10.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GraphicsState(GraphicsState source) {
/* 137 */     this.ctm = source.ctm;
/* 138 */     this.characterSpacing = source.characterSpacing;
/* 139 */     this.wordSpacing = source.wordSpacing;
/* 140 */     this.horizontalScaling = source.horizontalScaling;
/* 141 */     this.leading = source.leading;
/* 142 */     this.font = source.font;
/* 143 */     this.fontSize = source.fontSize;
/* 144 */     this.renderMode = source.renderMode;
/* 145 */     this.rise = source.rise;
/* 146 */     this.knockout = source.knockout;
/* 147 */     this.colorSpaceFill = source.colorSpaceFill;
/* 148 */     this.colorSpaceStroke = source.colorSpaceStroke;
/* 149 */     this.fillColor = source.fillColor;
/* 150 */     this.strokeColor = source.strokeColor;
/* 151 */     this.lineWidth = source.lineWidth;
/* 152 */     this.lineCapStyle = source.lineCapStyle;
/* 153 */     this.lineJoinStyle = source.lineJoinStyle;
/* 154 */     this.miterLimit = source.miterLimit;
/*     */     
/* 156 */     if (source.lineDashPattern != null) {
/* 157 */       this.lineDashPattern = new LineDashPattern(source.lineDashPattern.getDashArray(), source.lineDashPattern.getDashPhase());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix getCtm() {
/* 167 */     return this.ctm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getCharacterSpacing() {
/* 176 */     return this.characterSpacing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWordSpacing() {
/* 185 */     return this.wordSpacing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getHorizontalScaling() {
/* 194 */     return this.horizontalScaling;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getLeading() {
/* 203 */     return this.leading;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CMapAwareDocumentFont getFont() {
/* 212 */     return this.font;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getFontSize() {
/* 221 */     return this.fontSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRenderMode() {
/* 230 */     return this.renderMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getRise() {
/* 239 */     return this.rise;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isKnockout() {
/* 248 */     return this.knockout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfName getColorSpaceFill() {
/* 255 */     return this.colorSpaceFill;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfName getColorSpaceStroke() {
/* 262 */     return this.colorSpaceStroke;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getFillColor() {
/* 270 */     return this.fillColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getStrokeColor() {
/* 278 */     return this.strokeColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getLineWidth() {
/* 287 */     return this.lineWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineWidth(float lineWidth) {
/* 296 */     this.lineWidth = lineWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineCapStyle() {
/* 306 */     return this.lineCapStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineCapStyle(int lineCapStyle) {
/* 316 */     this.lineCapStyle = lineCapStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineJoinStyle() {
/* 326 */     return this.lineJoinStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineJoinStyle(int lineJoinStyle) {
/* 336 */     this.lineJoinStyle = lineJoinStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getMiterLimit() {
/* 345 */     return this.miterLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMiterLimit(float miterLimit) {
/* 354 */     this.miterLimit = miterLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineDashPattern getLineDashPattern() {
/* 363 */     return this.lineDashPattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineDashPattern(LineDashPattern lineDashPattern) {
/* 372 */     this.lineDashPattern = new LineDashPattern(lineDashPattern.getDashArray(), lineDashPattern.getDashPhase());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/GraphicsState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */