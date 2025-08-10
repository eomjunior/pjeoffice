/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfSpotColor
/*     */   implements ICachedColorSpace, IPdfSpecialColorSpace
/*     */ {
/*     */   public PdfName name;
/*     */   public BaseColor altcs;
/*     */   public ColorDetails altColorDetails;
/*     */   
/*     */   public PdfSpotColor(String name, BaseColor altcs) {
/*  74 */     this.name = new PdfName(name);
/*  75 */     this.altcs = altcs;
/*     */   }
/*     */   
/*     */   public ColorDetails[] getColorantDetails(PdfWriter writer) {
/*  79 */     if (this.altColorDetails == null && this.altcs instanceof ExtendedColor && ((ExtendedColor)this.altcs).getType() == 7) {
/*  80 */       this.altColorDetails = writer.addSimple(((LabColor)this.altcs).getLabColorSpace());
/*     */     }
/*  82 */     return new ColorDetails[] { this.altColorDetails };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getAlternativeCS() {
/*  90 */     return this.altcs;
/*     */   }
/*     */   
/*     */   public PdfName getName() {
/*  94 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected PdfObject getSpotObject(PdfWriter writer) {
/* 100 */     return getPdfObject(writer);
/*     */   }
/*     */   
/*     */   public PdfObject getPdfObject(PdfWriter writer) {
/* 104 */     PdfArray array = new PdfArray(PdfName.SEPARATION);
/* 105 */     array.add(this.name);
/* 106 */     PdfFunction func = null;
/* 107 */     if (this.altcs instanceof ExtendedColor)
/* 108 */     { CMYKColor cmyk; LabColor lab; int type = ((ExtendedColor)this.altcs).type;
/* 109 */       switch (type)
/*     */       { case 1:
/* 111 */           array.add(PdfName.DEVICEGRAY);
/* 112 */           func = PdfFunction.type2(writer, new float[] { 0.0F, 1.0F }, null, new float[] { 1.0F }, new float[] { ((GrayColor)this.altcs).getGray() }, 1.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 138 */           array.add(func.getReference());
/* 139 */           return array;case 2: array.add(PdfName.DEVICECMYK); cmyk = (CMYKColor)this.altcs; func = PdfFunction.type2(writer, new float[] { 0.0F, 1.0F }, null, new float[] { 0.0F, 0.0F, 0.0F, 0.0F }, new float[] { cmyk.getCyan(), cmyk.getMagenta(), cmyk.getYellow(), cmyk.getBlack() }, 1.0F); array.add(func.getReference()); return array;case 7: lab = (LabColor)this.altcs; if (this.altColorDetails != null) { array.add(this.altColorDetails.getIndirectReference()); } else { array.add(lab.getLabColorSpace().getPdfObject(writer)); }  func = PdfFunction.type2(writer, new float[] { 0.0F, 1.0F }, null, new float[] { 100.0F, 0.0F, 0.0F }, new float[] { lab.getL(), lab.getA(), lab.getB() }, 1.0F); array.add(func.getReference()); return array; }  throw new RuntimeException(MessageLocalization.getComposedMessage("only.rgb.gray.and.cmyk.are.supported.as.alternative.color.spaces", new Object[0])); }  array.add(PdfName.DEVICERGB); func = PdfFunction.type2(writer, new float[] { 0.0F, 1.0F }, null, new float[] { 1.0F, 1.0F, 1.0F }, new float[] { this.altcs.getRed() / 255.0F, this.altcs.getGreen() / 255.0F, this.altcs.getBlue() / 255.0F }, 1.0F); array.add(func.getReference()); return array;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 144 */     if (this == o) return true; 
/* 145 */     if (!(o instanceof PdfSpotColor)) return false;
/*     */     
/* 147 */     PdfSpotColor spotColor = (PdfSpotColor)o;
/*     */     
/* 149 */     if (!this.altcs.equals(spotColor.altcs)) return false; 
/* 150 */     if (!this.name.equals(spotColor.name)) return false;
/*     */     
/* 152 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 157 */     int result = this.name.hashCode();
/* 158 */     result = 31 * result + this.altcs.hashCode();
/* 159 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfSpotColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */