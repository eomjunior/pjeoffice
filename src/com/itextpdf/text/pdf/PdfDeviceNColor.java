/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfDeviceNColor
/*     */   implements ICachedColorSpace, IPdfSpecialColorSpace
/*     */ {
/*     */   PdfSpotColor[] spotColors;
/*     */   ColorDetails[] colorantsDetails;
/*     */   
/*     */   public PdfDeviceNColor(PdfSpotColor[] spotColors) {
/*  57 */     this.spotColors = spotColors;
/*     */   }
/*     */   
/*     */   public int getNumberOfColorants() {
/*  61 */     return this.spotColors.length;
/*     */   }
/*     */   
/*     */   public PdfSpotColor[] getSpotColors() {
/*  65 */     return this.spotColors;
/*     */   }
/*     */   
/*     */   public ColorDetails[] getColorantDetails(PdfWriter writer) {
/*  69 */     if (this.colorantsDetails == null) {
/*  70 */       this.colorantsDetails = new ColorDetails[this.spotColors.length];
/*  71 */       int i = 0;
/*  72 */       for (PdfSpotColor spotColorant : this.spotColors) {
/*  73 */         this.colorantsDetails[i] = writer.addSimple(spotColorant);
/*  74 */         i++;
/*     */       } 
/*     */     } 
/*  77 */     return this.colorantsDetails;
/*     */   }
/*     */   
/*     */   public PdfObject getPdfObject(PdfWriter writer) {
/*  81 */     PdfArray array = new PdfArray(PdfName.DEVICEN);
/*     */     
/*  83 */     PdfArray colorants = new PdfArray();
/*  84 */     float[] colorantsRanges = new float[this.spotColors.length * 2];
/*  85 */     PdfDictionary colorantsDict = new PdfDictionary();
/*  86 */     String psFunFooter = "";
/*     */     
/*  88 */     int numberOfColorants = this.spotColors.length;
/*  89 */     float[][] CMYK = new float[4][numberOfColorants];
/*  90 */     int i = 0;
/*  91 */     for (; i < numberOfColorants; i++) {
/*  92 */       PdfSpotColor spotColorant = this.spotColors[i];
/*  93 */       colorantsRanges[2 * i] = 0.0F;
/*  94 */       colorantsRanges[2 * i + 1] = 1.0F;
/*  95 */       colorants.add(spotColorant.getName());
/*  96 */       if (colorantsDict.get(spotColorant.getName()) != null)
/*  97 */         throw new RuntimeException(MessageLocalization.getComposedMessage("devicen.component.names.shall.be.different", new Object[0])); 
/*  98 */       if (this.colorantsDetails != null) {
/*  99 */         colorantsDict.put(spotColorant.getName(), this.colorantsDetails[i].getIndirectReference());
/*     */       } else {
/* 101 */         colorantsDict.put(spotColorant.getName(), spotColorant.getPdfObject(writer));
/* 102 */       }  BaseColor color = spotColorant.getAlternativeCS();
/* 103 */       if (color instanceof ExtendedColor) {
/* 104 */         CMYKColor cmyk; int type = ((ExtendedColor)color).type;
/* 105 */         switch (type) {
/*     */           case 1:
/* 107 */             CMYK[0][i] = 0.0F;
/* 108 */             CMYK[1][i] = 0.0F;
/* 109 */             CMYK[2][i] = 0.0F;
/* 110 */             CMYK[3][i] = 1.0F - ((GrayColor)color).getGray();
/*     */             break;
/*     */           case 2:
/* 113 */             CMYK[0][i] = ((CMYKColor)color).getCyan();
/* 114 */             CMYK[1][i] = ((CMYKColor)color).getMagenta();
/* 115 */             CMYK[2][i] = ((CMYKColor)color).getYellow();
/* 116 */             CMYK[3][i] = ((CMYKColor)color).getBlack();
/*     */             break;
/*     */           case 7:
/* 119 */             cmyk = ((LabColor)color).toCmyk();
/* 120 */             CMYK[0][i] = cmyk.getCyan();
/* 121 */             CMYK[1][i] = cmyk.getMagenta();
/* 122 */             CMYK[2][i] = cmyk.getYellow();
/* 123 */             CMYK[3][i] = cmyk.getBlack();
/*     */             break;
/*     */           default:
/* 126 */             throw new RuntimeException(MessageLocalization.getComposedMessage("only.rgb.gray.and.cmyk.are.supported.as.alternative.color.spaces", new Object[0]));
/*     */         } 
/*     */       } else {
/* 129 */         float r = color.getRed();
/* 130 */         float g = color.getGreen();
/* 131 */         float b = color.getBlue();
/* 132 */         float computedC = 0.0F, computedM = 0.0F, computedY = 0.0F, computedK = 0.0F;
/*     */ 
/*     */         
/* 135 */         if (r == 0.0F && g == 0.0F && b == 0.0F) {
/* 136 */           computedK = 1.0F;
/*     */         } else {
/* 138 */           computedC = 1.0F - r / 255.0F;
/* 139 */           computedM = 1.0F - g / 255.0F;
/* 140 */           computedY = 1.0F - b / 255.0F;
/*     */           
/* 142 */           float minCMY = Math.min(computedC, 
/* 143 */               Math.min(computedM, computedY));
/* 144 */           computedC = (computedC - minCMY) / (1.0F - minCMY);
/* 145 */           computedM = (computedM - minCMY) / (1.0F - minCMY);
/* 146 */           computedY = (computedY - minCMY) / (1.0F - minCMY);
/* 147 */           computedK = minCMY;
/*     */         } 
/* 149 */         CMYK[0][i] = computedC;
/* 150 */         CMYK[1][i] = computedM;
/* 151 */         CMYK[2][i] = computedY;
/* 152 */         CMYK[3][i] = computedK;
/*     */       } 
/* 154 */       psFunFooter = psFunFooter + "pop ";
/*     */     } 
/* 156 */     array.add(colorants);
/*     */     
/* 158 */     String psFunHeader = String.format(Locale.US, "1.000000 %d 1 roll ", new Object[] { Integer.valueOf(numberOfColorants + 1) });
/* 159 */     array.add(PdfName.DEVICECMYK);
/* 160 */     psFunHeader = psFunHeader + psFunHeader + psFunHeader + psFunHeader;
/* 161 */     String psFun = "";
/* 162 */     i = numberOfColorants + 4;
/* 163 */     for (; i > numberOfColorants; i--) {
/* 164 */       psFun = psFun + String.format(Locale.US, "%d -1 roll ", new Object[] { Integer.valueOf(i) });
/* 165 */       for (int j = numberOfColorants; j > 0; j--) {
/* 166 */         psFun = psFun + String.format(Locale.US, "%d index %f mul 1.000000 cvr exch sub mul ", new Object[] { Integer.valueOf(j), Float.valueOf(CMYK[numberOfColorants + 4 - i][numberOfColorants - j]) });
/*     */       } 
/* 168 */       psFun = psFun + String.format(Locale.US, "1.000000 cvr exch sub %d 1 roll ", new Object[] { Integer.valueOf(i) });
/*     */     } 
/*     */     
/* 171 */     PdfFunction func = PdfFunction.type4(writer, colorantsRanges, new float[] { 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 1.0F }, "{ " + psFunHeader + psFun + psFunFooter + "}");
/* 172 */     array.add(func.getReference());
/*     */     
/* 174 */     PdfDictionary attr = new PdfDictionary();
/* 175 */     attr.put(PdfName.SUBTYPE, PdfName.NCHANNEL);
/* 176 */     attr.put(PdfName.COLORANTS, colorantsDict);
/* 177 */     array.add(attr);
/*     */     
/* 179 */     return array;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 184 */     if (this == o) return true; 
/* 185 */     if (!(o instanceof PdfDeviceNColor)) return false;
/*     */     
/* 187 */     PdfDeviceNColor that = (PdfDeviceNColor)o;
/*     */     
/* 189 */     if (!Arrays.equals((Object[])this.spotColors, (Object[])that.spotColors)) return false;
/*     */     
/* 191 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 196 */     return Arrays.hashCode((Object[])this.spotColors);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfDeviceNColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */