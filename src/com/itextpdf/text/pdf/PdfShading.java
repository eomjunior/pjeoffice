/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfShading
/*     */ {
/*     */   protected PdfDictionary shading;
/*     */   protected PdfWriter writer;
/*     */   protected int shadingType;
/*     */   protected ColorDetails colorDetails;
/*     */   protected PdfName shadingName;
/*     */   protected PdfIndirectReference shadingReference;
/*     */   private BaseColor cspace;
/*     */   protected float[] bBox;
/*     */   protected boolean antiAlias = false;
/*     */   
/*     */   protected PdfShading(PdfWriter writer) {
/*  77 */     this.writer = writer;
/*     */   } protected void setColorSpace(BaseColor color) {
/*     */     SpotColor spot;
/*     */     DeviceNColor deviceNColor;
/*  81 */     this.cspace = color;
/*  82 */     int type = ExtendedColor.getType(color);
/*  83 */     PdfObject colorSpace = null;
/*  84 */     switch (type) {
/*     */       case 1:
/*  86 */         colorSpace = PdfName.DEVICEGRAY;
/*     */         break;
/*     */       
/*     */       case 2:
/*  90 */         colorSpace = PdfName.DEVICECMYK;
/*     */         break;
/*     */       
/*     */       case 3:
/*  94 */         spot = (SpotColor)color;
/*  95 */         this.colorDetails = this.writer.addSimple(spot.getPdfSpotColor());
/*  96 */         colorSpace = this.colorDetails.getIndirectReference();
/*     */         break;
/*     */       
/*     */       case 6:
/* 100 */         deviceNColor = (DeviceNColor)color;
/* 101 */         this.colorDetails = this.writer.addSimple(deviceNColor.getPdfDeviceNColor());
/* 102 */         colorSpace = this.colorDetails.getIndirectReference();
/*     */         break;
/*     */       
/*     */       case 4:
/*     */       case 5:
/* 107 */         throwColorSpaceError();
/*     */       
/*     */       default:
/* 110 */         colorSpace = PdfName.DEVICERGB;
/*     */         break;
/*     */     } 
/* 113 */     this.shading.put(PdfName.COLORSPACE, colorSpace);
/*     */   }
/*     */   
/*     */   public BaseColor getColorSpace() {
/* 117 */     return this.cspace;
/*     */   }
/*     */   
/*     */   public static void throwColorSpaceError() {
/* 121 */     throw new IllegalArgumentException(MessageLocalization.getComposedMessage("a.tiling.or.shading.pattern.cannot.be.used.as.a.color.space.in.a.shading.pattern", new Object[0]));
/*     */   }
/*     */   
/*     */   public static void checkCompatibleColors(BaseColor c1, BaseColor c2) {
/* 125 */     int type1 = ExtendedColor.getType(c1);
/* 126 */     int type2 = ExtendedColor.getType(c2);
/* 127 */     if (type1 != type2)
/* 128 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("both.colors.must.be.of.the.same.type", new Object[0])); 
/* 129 */     if (type1 == 3 && ((SpotColor)c1).getPdfSpotColor() != ((SpotColor)c2).getPdfSpotColor())
/* 130 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.spot.color.must.be.the.same.only.the.tint.can.vary", new Object[0])); 
/* 131 */     if (type1 == 4 || type1 == 5)
/* 132 */       throwColorSpaceError(); 
/*     */   }
/*     */   public static float[] getColorArray(BaseColor color) {
/*     */     CMYKColor cmyk;
/* 136 */     int type = ExtendedColor.getType(color);
/* 137 */     switch (type) {
/*     */       case 1:
/* 139 */         return new float[] { ((GrayColor)color).getGray() };
/*     */       
/*     */       case 2:
/* 142 */         cmyk = (CMYKColor)color;
/* 143 */         return new float[] { cmyk.getCyan(), cmyk.getMagenta(), cmyk.getYellow(), cmyk.getBlack() };
/*     */       
/*     */       case 3:
/* 146 */         return new float[] { ((SpotColor)color).getTint() };
/*     */       
/*     */       case 6:
/* 149 */         return ((DeviceNColor)color).getTints();
/*     */       
/*     */       case 0:
/* 152 */         return new float[] { color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F };
/*     */     } 
/*     */     
/* 155 */     throwColorSpaceError();
/* 156 */     return null;
/*     */   }
/*     */   
/*     */   public static PdfShading type1(PdfWriter writer, BaseColor colorSpace, float[] domain, float[] tMatrix, PdfFunction function) {
/* 160 */     PdfShading sp = new PdfShading(writer);
/* 161 */     sp.shading = new PdfDictionary();
/* 162 */     sp.shadingType = 1;
/* 163 */     sp.shading.put(PdfName.SHADINGTYPE, new PdfNumber(sp.shadingType));
/* 164 */     sp.setColorSpace(colorSpace);
/* 165 */     if (domain != null)
/* 166 */       sp.shading.put(PdfName.DOMAIN, new PdfArray(domain)); 
/* 167 */     if (tMatrix != null)
/* 168 */       sp.shading.put(PdfName.MATRIX, new PdfArray(tMatrix)); 
/* 169 */     sp.shading.put(PdfName.FUNCTION, function.getReference());
/* 170 */     return sp;
/*     */   }
/*     */   
/*     */   public static PdfShading type2(PdfWriter writer, BaseColor colorSpace, float[] coords, float[] domain, PdfFunction function, boolean[] extend) {
/* 174 */     PdfShading sp = new PdfShading(writer);
/* 175 */     sp.shading = new PdfDictionary();
/* 176 */     sp.shadingType = 2;
/* 177 */     sp.shading.put(PdfName.SHADINGTYPE, new PdfNumber(sp.shadingType));
/* 178 */     sp.setColorSpace(colorSpace);
/* 179 */     sp.shading.put(PdfName.COORDS, new PdfArray(coords));
/* 180 */     if (domain != null)
/* 181 */       sp.shading.put(PdfName.DOMAIN, new PdfArray(domain)); 
/* 182 */     sp.shading.put(PdfName.FUNCTION, function.getReference());
/* 183 */     if (extend != null && (extend[0] || extend[1])) {
/* 184 */       PdfArray array = new PdfArray(extend[0] ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
/* 185 */       array.add(extend[1] ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
/* 186 */       sp.shading.put(PdfName.EXTEND, array);
/*     */     } 
/* 188 */     return sp;
/*     */   }
/*     */   
/*     */   public static PdfShading type3(PdfWriter writer, BaseColor colorSpace, float[] coords, float[] domain, PdfFunction function, boolean[] extend) {
/* 192 */     PdfShading sp = type2(writer, colorSpace, coords, domain, function, extend);
/* 193 */     sp.shadingType = 3;
/* 194 */     sp.shading.put(PdfName.SHADINGTYPE, new PdfNumber(sp.shadingType));
/* 195 */     return sp;
/*     */   }
/*     */   
/*     */   public static PdfShading simpleAxial(PdfWriter writer, float x0, float y0, float x1, float y1, BaseColor startColor, BaseColor endColor, boolean extendStart, boolean extendEnd) {
/* 199 */     checkCompatibleColors(startColor, endColor);
/* 200 */     PdfFunction function = PdfFunction.type2(writer, new float[] { 0.0F, 1.0F }, null, getColorArray(startColor), 
/* 201 */         getColorArray(endColor), 1.0F);
/* 202 */     return type2(writer, startColor, new float[] { x0, y0, x1, y1 }, null, function, new boolean[] { extendStart, extendEnd });
/*     */   }
/*     */   
/*     */   public static PdfShading simpleAxial(PdfWriter writer, float x0, float y0, float x1, float y1, BaseColor startColor, BaseColor endColor) {
/* 206 */     return simpleAxial(writer, x0, y0, x1, y1, startColor, endColor, true, true);
/*     */   }
/*     */   
/*     */   public static PdfShading simpleRadial(PdfWriter writer, float x0, float y0, float r0, float x1, float y1, float r1, BaseColor startColor, BaseColor endColor, boolean extendStart, boolean extendEnd) {
/* 210 */     checkCompatibleColors(startColor, endColor);
/* 211 */     PdfFunction function = PdfFunction.type2(writer, new float[] { 0.0F, 1.0F }, null, getColorArray(startColor), 
/* 212 */         getColorArray(endColor), 1.0F);
/* 213 */     return type3(writer, startColor, new float[] { x0, y0, r0, x1, y1, r1 }, null, function, new boolean[] { extendStart, extendEnd });
/*     */   }
/*     */   
/*     */   public static PdfShading simpleRadial(PdfWriter writer, float x0, float y0, float r0, float x1, float y1, float r1, BaseColor startColor, BaseColor endColor) {
/* 217 */     return simpleRadial(writer, x0, y0, r0, x1, y1, r1, startColor, endColor, true, true);
/*     */   }
/*     */   
/*     */   PdfName getShadingName() {
/* 221 */     return this.shadingName;
/*     */   }
/*     */   
/*     */   PdfIndirectReference getShadingReference() {
/* 225 */     if (this.shadingReference == null)
/* 226 */       this.shadingReference = this.writer.getPdfIndirectReference(); 
/* 227 */     return this.shadingReference;
/*     */   }
/*     */   
/*     */   void setName(int number) {
/* 231 */     this.shadingName = new PdfName("Sh" + number);
/*     */   }
/*     */   
/*     */   public void addToBody() throws IOException {
/* 235 */     if (this.bBox != null)
/* 236 */       this.shading.put(PdfName.BBOX, new PdfArray(this.bBox)); 
/* 237 */     if (this.antiAlias)
/* 238 */       this.shading.put(PdfName.ANTIALIAS, PdfBoolean.PDFTRUE); 
/* 239 */     this.writer.addToBody(this.shading, getShadingReference());
/*     */   }
/*     */   
/*     */   PdfWriter getWriter() {
/* 243 */     return this.writer;
/*     */   }
/*     */   
/*     */   ColorDetails getColorDetails() {
/* 247 */     return this.colorDetails;
/*     */   }
/*     */   
/*     */   public float[] getBBox() {
/* 251 */     return this.bBox;
/*     */   }
/*     */   
/*     */   public void setBBox(float[] bBox) {
/* 255 */     if (bBox.length != 4)
/* 256 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bbox.must.be.a.4.element.array", new Object[0])); 
/* 257 */     this.bBox = bBox;
/*     */   }
/*     */   
/*     */   public boolean isAntiAlias() {
/* 261 */     return this.antiAlias;
/*     */   }
/*     */   
/*     */   public void setAntiAlias(boolean antiAlias) {
/* 265 */     this.antiAlias = antiAlias;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfShading.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */