/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfLabColor
/*     */   implements ICachedColorSpace
/*     */ {
/*  52 */   float[] whitePoint = new float[] { 0.9505F, 1.0F, 1.089F };
/*  53 */   float[] blackPoint = null;
/*  54 */   float[] range = null;
/*     */   
/*     */   public PdfLabColor() {}
/*     */   
/*     */   public PdfLabColor(float[] whitePoint) {
/*  59 */     if (whitePoint == null || whitePoint.length != 3 || whitePoint[0] < 1.0E-6F || whitePoint[2] < 1.0E-6F || whitePoint[1] < 0.999999F || whitePoint[1] > 1.000001F)
/*     */     {
/*     */ 
/*     */       
/*  63 */       throw new RuntimeException(MessageLocalization.getComposedMessage("lab.cs.white.point", new Object[0])); } 
/*  64 */     this.whitePoint = whitePoint;
/*     */   }
/*     */   
/*     */   public PdfLabColor(float[] whitePoint, float[] blackPoint) {
/*  68 */     this(whitePoint);
/*  69 */     this.blackPoint = blackPoint;
/*     */   }
/*     */   
/*     */   public PdfLabColor(float[] whitePoint, float[] blackPoint, float[] range) {
/*  73 */     this(whitePoint, blackPoint);
/*  74 */     this.range = range;
/*     */   }
/*     */   
/*     */   public PdfObject getPdfObject(PdfWriter writer) {
/*  78 */     PdfArray array = new PdfArray(PdfName.LAB);
/*  79 */     PdfDictionary dictionary = new PdfDictionary();
/*  80 */     if (this.whitePoint == null || this.whitePoint.length != 3 || this.whitePoint[0] < 1.0E-6F || this.whitePoint[2] < 1.0E-6F || this.whitePoint[1] < 0.999999F || this.whitePoint[1] > 1.000001F)
/*     */     {
/*     */ 
/*     */       
/*  84 */       throw new RuntimeException(MessageLocalization.getComposedMessage("lab.cs.white.point", new Object[0])); } 
/*  85 */     dictionary.put(PdfName.WHITEPOINT, new PdfArray(this.whitePoint));
/*  86 */     if (this.blackPoint != null) {
/*  87 */       if (this.blackPoint.length != 3 || this.blackPoint[0] < -1.0E-6F || this.blackPoint[1] < -1.0E-6F || this.blackPoint[2] < -1.0E-6F)
/*     */       {
/*  89 */         throw new RuntimeException(MessageLocalization.getComposedMessage("lab.cs.black.point", new Object[0])); } 
/*  90 */       dictionary.put(PdfName.BLACKPOINT, new PdfArray(this.blackPoint));
/*     */     } 
/*  92 */     if (this.range != null) {
/*  93 */       if (this.range.length != 4 || this.range[0] > this.range[1] || this.range[2] > this.range[3])
/*  94 */         throw new RuntimeException(MessageLocalization.getComposedMessage("lab.cs.range", new Object[0])); 
/*  95 */       dictionary.put(PdfName.RANGE, new PdfArray(this.range));
/*     */     } 
/*  97 */     array.add(dictionary);
/*  98 */     return array;
/*     */   }
/*     */   
/*     */   public BaseColor lab2Rgb(float l, float a, float b) {
/* 102 */     double[] clinear = lab2RgbLinear(l, a, b);
/* 103 */     return new BaseColor((float)clinear[0], (float)clinear[1], (float)clinear[2]);
/*     */   }
/*     */   
/*     */   CMYKColor lab2Cmyk(float l, float a, float b) {
/* 107 */     double[] clinear = lab2RgbLinear(l, a, b);
/*     */     
/* 109 */     double r = clinear[0];
/* 110 */     double g = clinear[1];
/* 111 */     double bee = clinear[2];
/* 112 */     double computedC = 0.0D, computedM = 0.0D, computedY = 0.0D, computedK = 0.0D;
/*     */ 
/*     */     
/* 115 */     if (r == 0.0D && g == 0.0D && b == 0.0F) {
/* 116 */       computedK = 1.0D;
/*     */     } else {
/* 118 */       computedC = 1.0D - r;
/* 119 */       computedM = 1.0D - g;
/* 120 */       computedY = 1.0D - bee;
/*     */       
/* 122 */       double minCMY = Math.min(computedC, 
/* 123 */           Math.min(computedM, computedY));
/* 124 */       computedC = (computedC - minCMY) / (1.0D - minCMY);
/* 125 */       computedM = (computedM - minCMY) / (1.0D - minCMY);
/* 126 */       computedY = (computedY - minCMY) / (1.0D - minCMY);
/* 127 */       computedK = minCMY;
/*     */     } 
/*     */     
/* 130 */     return new CMYKColor((float)computedC, (float)computedM, (float)computedY, (float)computedK);
/*     */   }
/*     */   
/*     */   protected double[] lab2RgbLinear(float l, float a, float b) {
/* 134 */     if (this.range != null && this.range.length == 4) {
/* 135 */       if (a < this.range[0])
/* 136 */         a = this.range[0]; 
/* 137 */       if (a > this.range[1])
/* 138 */         a = this.range[1]; 
/* 139 */       if (b < this.range[2])
/* 140 */         b = this.range[2]; 
/* 141 */       if (b > this.range[3])
/* 142 */         b = this.range[3]; 
/*     */     } 
/* 144 */     double theta = 0.20689655172413793D;
/*     */     
/* 146 */     double fy = (l + 16.0F) / 116.0D;
/* 147 */     double fx = fy + a / 500.0D;
/* 148 */     double fz = fy - b / 200.0D;
/*     */     
/* 150 */     double x = (fx > theta) ? (this.whitePoint[0] * fx * fx * fx) : ((fx - 0.13793103448275862D) * 3.0D * theta * theta * this.whitePoint[0]);
/* 151 */     double y = (fy > theta) ? (this.whitePoint[1] * fy * fy * fy) : ((fy - 0.13793103448275862D) * 3.0D * theta * theta * this.whitePoint[1]);
/* 152 */     double z = (fz > theta) ? (this.whitePoint[2] * fz * fz * fz) : ((fz - 0.13793103448275862D) * 3.0D * theta * theta * this.whitePoint[2]);
/*     */     
/* 154 */     double[] clinear = new double[3];
/* 155 */     clinear[0] = x * 3.241D - y * 1.5374D - z * 0.4986D;
/* 156 */     clinear[1] = -x * 0.9692D + y * 1.876D - z * 0.0416D;
/* 157 */     clinear[2] = x * 0.0556D - y * 0.204D + z * 1.057D;
/*     */     
/* 159 */     for (int i = 0; i < 3; i++) {
/* 160 */       clinear[i] = (clinear[i] <= 0.0031308D) ? (12.92D * clinear[i]) : (1.055D * 
/*     */         
/* 162 */         Math.pow(clinear[i], 0.4166666666666667D) - 0.055D);
/* 163 */       if (clinear[i] < 0.0D) {
/* 164 */         clinear[i] = 0.0D;
/* 165 */       } else if (clinear[i] > 1.0D) {
/* 166 */         clinear[i] = 1.0D;
/*     */       } 
/*     */     } 
/* 169 */     return clinear;
/*     */   }
/*     */   
/*     */   public LabColor rgb2lab(BaseColor baseColor) {
/* 173 */     double rLinear = (baseColor.getRed() / 255.0F);
/* 174 */     double gLinear = (baseColor.getGreen() / 255.0F);
/* 175 */     double bLinear = (baseColor.getBlue() / 255.0F);
/*     */ 
/*     */     
/* 178 */     double r = (rLinear > 0.04045D) ? Math.pow((rLinear + 0.055D) / 1.055D, 2.2D) : (rLinear / 12.92D);
/* 179 */     double g = (gLinear > 0.04045D) ? Math.pow((gLinear + 0.055D) / 1.055D, 2.2D) : (gLinear / 12.92D);
/* 180 */     double b = (bLinear > 0.04045D) ? Math.pow((bLinear + 0.055D) / 1.055D, 2.2D) : (bLinear / 12.92D);
/*     */ 
/*     */     
/* 183 */     double x = r * 0.4124D + g * 0.3576D + b * 0.1805D;
/* 184 */     double y = r * 0.2126D + g * 0.7152D + b * 0.0722D;
/* 185 */     double z = r * 0.0193D + g * 0.1192D + b * 0.9505D;
/*     */     
/* 187 */     float l = (float)Math.round((116.0D * fXyz(y / this.whitePoint[1]) - 16.0D) * 1000.0D) / 1000.0F;
/* 188 */     float a = (float)Math.round(500.0D * (fXyz(x / this.whitePoint[0]) - fXyz(y / this.whitePoint[1])) * 1000.0D) / 1000.0F;
/* 189 */     float bee = (float)Math.round(200.0D * (fXyz(y / this.whitePoint[1]) - fXyz(z / this.whitePoint[2])) * 1000.0D) / 1000.0F;
/*     */     
/* 191 */     return new LabColor(this, l, a, bee);
/*     */   }
/*     */   
/*     */   private static double fXyz(double t) {
/* 195 */     return (t > 0.008856D) ? Math.pow(t, 0.3333333333333333D) : (7.787D * t + 0.13793103448275862D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 200 */     if (this == o) return true; 
/* 201 */     if (!(o instanceof PdfLabColor)) return false;
/*     */     
/* 203 */     PdfLabColor that = (PdfLabColor)o;
/*     */     
/* 205 */     if (!Arrays.equals(this.blackPoint, that.blackPoint)) return false; 
/* 206 */     if (!Arrays.equals(this.range, that.range)) return false; 
/* 207 */     if (!Arrays.equals(this.whitePoint, that.whitePoint)) return false;
/*     */     
/* 209 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 214 */     int result = Arrays.hashCode(this.whitePoint);
/* 215 */     result = 31 * result + ((this.blackPoint != null) ? Arrays.hashCode(this.blackPoint) : 0);
/* 216 */     result = 31 * result + ((this.range != null) ? Arrays.hashCode(this.range) : 0);
/* 217 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfLabColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */