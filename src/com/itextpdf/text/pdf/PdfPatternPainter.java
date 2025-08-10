/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PdfPatternPainter
/*     */   extends PdfTemplate
/*     */ {
/*     */   float xstep;
/*     */   float ystep;
/*     */   boolean stencil = false;
/*     */   BaseColor defaultColor;
/*     */   
/*     */   private PdfPatternPainter() {
/*  69 */     this.type = 3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfPatternPainter(PdfWriter wr) {
/*  79 */     super(wr);
/*  80 */     this.type = 3;
/*     */   }
/*     */   
/*     */   PdfPatternPainter(PdfWriter wr, BaseColor defaultColor) {
/*  84 */     this(wr);
/*  85 */     this.stencil = true;
/*  86 */     if (defaultColor == null) {
/*  87 */       this.defaultColor = BaseColor.GRAY;
/*     */     } else {
/*  89 */       this.defaultColor = defaultColor;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setXStep(float xstep) {
/*  99 */     this.xstep = xstep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setYStep(float ystep) {
/* 109 */     this.ystep = ystep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getXStep() {
/* 117 */     return this.xstep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getYStep() {
/* 125 */     return this.ystep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStencil() {
/* 133 */     return this.stencil;
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
/*     */   
/*     */   public void setPatternMatrix(float a, float b, float c, float d, float e, float f) {
/* 146 */     setMatrix(a, b, c, d, e, f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfPattern getPattern() {
/* 153 */     return new PdfPattern(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfPattern getPattern(int compressionLevel) {
/* 163 */     return new PdfPattern(this, compressionLevel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfContentByte getDuplicate() {
/* 173 */     PdfPatternPainter tpl = new PdfPatternPainter();
/* 174 */     tpl.writer = this.writer;
/* 175 */     tpl.pdf = this.pdf;
/* 176 */     tpl.thisReference = this.thisReference;
/* 177 */     tpl.pageResources = this.pageResources;
/* 178 */     tpl.bBox = new Rectangle(this.bBox);
/* 179 */     tpl.xstep = this.xstep;
/* 180 */     tpl.ystep = this.ystep;
/* 181 */     tpl.matrix = this.matrix;
/* 182 */     tpl.stencil = this.stencil;
/* 183 */     tpl.defaultColor = this.defaultColor;
/* 184 */     return tpl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getDefaultColor() {
/* 192 */     return this.defaultColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGrayFill(float gray) {
/* 199 */     checkNoColor();
/* 200 */     super.setGrayFill(gray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetGrayFill() {
/* 207 */     checkNoColor();
/* 208 */     super.resetGrayFill();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGrayStroke(float gray) {
/* 215 */     checkNoColor();
/* 216 */     super.setGrayStroke(gray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetGrayStroke() {
/* 223 */     checkNoColor();
/* 224 */     super.resetGrayStroke();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRGBColorFillF(float red, float green, float blue) {
/* 231 */     checkNoColor();
/* 232 */     super.setRGBColorFillF(red, green, blue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetRGBColorFill() {
/* 239 */     checkNoColor();
/* 240 */     super.resetRGBColorFill();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRGBColorStrokeF(float red, float green, float blue) {
/* 247 */     checkNoColor();
/* 248 */     super.setRGBColorStrokeF(red, green, blue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetRGBColorStroke() {
/* 255 */     checkNoColor();
/* 256 */     super.resetRGBColorStroke();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCMYKColorFillF(float cyan, float magenta, float yellow, float black) {
/* 263 */     checkNoColor();
/* 264 */     super.setCMYKColorFillF(cyan, magenta, yellow, black);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetCMYKColorFill() {
/* 271 */     checkNoColor();
/* 272 */     super.resetCMYKColorFill();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCMYKColorStrokeF(float cyan, float magenta, float yellow, float black) {
/* 279 */     checkNoColor();
/* 280 */     super.setCMYKColorStrokeF(cyan, magenta, yellow, black);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetCMYKColorStroke() {
/* 287 */     checkNoColor();
/* 288 */     super.resetCMYKColorStroke();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addImage(Image image, float a, float b, float c, float d, float e, float f) throws DocumentException {
/* 295 */     if (this.stencil && !image.isMask())
/* 296 */       checkNoColor(); 
/* 297 */     super.addImage(image, a, b, c, d, e, f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCMYKColorFill(int cyan, int magenta, int yellow, int black) {
/* 304 */     checkNoColor();
/* 305 */     super.setCMYKColorFill(cyan, magenta, yellow, black);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCMYKColorStroke(int cyan, int magenta, int yellow, int black) {
/* 312 */     checkNoColor();
/* 313 */     super.setCMYKColorStroke(cyan, magenta, yellow, black);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRGBColorFill(int red, int green, int blue) {
/* 320 */     checkNoColor();
/* 321 */     super.setRGBColorFill(red, green, blue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRGBColorStroke(int red, int green, int blue) {
/* 328 */     checkNoColor();
/* 329 */     super.setRGBColorStroke(red, green, blue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColorStroke(BaseColor color) {
/* 336 */     checkNoColor();
/* 337 */     super.setColorStroke(color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColorFill(BaseColor color) {
/* 344 */     checkNoColor();
/* 345 */     super.setColorFill(color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColorFill(PdfSpotColor sp, float tint) {
/* 352 */     checkNoColor();
/* 353 */     super.setColorFill(sp, tint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColorStroke(PdfSpotColor sp, float tint) {
/* 360 */     checkNoColor();
/* 361 */     super.setColorStroke(sp, tint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPatternFill(PdfPatternPainter p) {
/* 368 */     checkNoColor();
/* 369 */     super.setPatternFill(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPatternFill(PdfPatternPainter p, BaseColor color, float tint) {
/* 376 */     checkNoColor();
/* 377 */     super.setPatternFill(p, color, tint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPatternStroke(PdfPatternPainter p, BaseColor color, float tint) {
/* 384 */     checkNoColor();
/* 385 */     super.setPatternStroke(p, color, tint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPatternStroke(PdfPatternPainter p) {
/* 392 */     checkNoColor();
/* 393 */     super.setPatternStroke(p);
/*     */   }
/*     */   
/*     */   void checkNoColor() {
/* 397 */     if (this.stencil)
/* 398 */       throw new RuntimeException(MessageLocalization.getComposedMessage("colors.are.not.allowed.in.uncolored.tile.patterns", new Object[0])); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfPatternPainter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */