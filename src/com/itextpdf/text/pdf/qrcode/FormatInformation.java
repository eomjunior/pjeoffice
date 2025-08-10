/*     */ package com.itextpdf.text.pdf.qrcode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class FormatInformation
/*     */ {
/*     */   private static final int FORMAT_INFO_MASK_QR = 21522;
/*  34 */   private static final int[][] FORMAT_INFO_DECODE_LOOKUP = new int[][] { { 21522, 0 }, { 20773, 1 }, { 24188, 2 }, { 23371, 3 }, { 17913, 4 }, { 16590, 5 }, { 20375, 6 }, { 19104, 7 }, { 30660, 8 }, { 29427, 9 }, { 32170, 10 }, { 30877, 11 }, { 26159, 12 }, { 25368, 13 }, { 27713, 14 }, { 26998, 15 }, { 5769, 16 }, { 5054, 17 }, { 7399, 18 }, { 6608, 19 }, { 1890, 20 }, { 597, 21 }, { 3340, 22 }, { 2107, 23 }, { 13663, 24 }, { 12392, 25 }, { 16177, 26 }, { 14854, 27 }, { 9396, 28 }, { 8579, 29 }, { 11994, 30 }, { 11245, 31 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   private static final int[] BITS_SET_IN_HALF_BYTE = new int[] { 0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4 };
/*     */   
/*     */   private final ErrorCorrectionLevel errorCorrectionLevel;
/*     */   
/*     */   private final byte dataMask;
/*     */ 
/*     */   
/*     */   private FormatInformation(int formatInfo) {
/*  80 */     this.errorCorrectionLevel = ErrorCorrectionLevel.forBits(formatInfo >> 3 & 0x3);
/*     */     
/*  82 */     this.dataMask = (byte)(formatInfo & 0x7);
/*     */   }
/*     */   
/*     */   static int numBitsDiffering(int a, int b) {
/*  86 */     a ^= b;
/*     */     
/*  88 */     return BITS_SET_IN_HALF_BYTE[a & 0xF] + BITS_SET_IN_HALF_BYTE[a >>> 4 & 0xF] + BITS_SET_IN_HALF_BYTE[a >>> 8 & 0xF] + BITS_SET_IN_HALF_BYTE[a >>> 12 & 0xF] + BITS_SET_IN_HALF_BYTE[a >>> 16 & 0xF] + BITS_SET_IN_HALF_BYTE[a >>> 20 & 0xF] + BITS_SET_IN_HALF_BYTE[a >>> 24 & 0xF] + BITS_SET_IN_HALF_BYTE[a >>> 28 & 0xF];
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static FormatInformation decodeFormatInformation(int maskedFormatInfo1, int maskedFormatInfo2) {
/* 106 */     FormatInformation formatInfo = doDecodeFormatInformation(maskedFormatInfo1, maskedFormatInfo2);
/* 107 */     if (formatInfo != null) {
/* 108 */       return formatInfo;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 113 */     return doDecodeFormatInformation(maskedFormatInfo1 ^ 0x5412, maskedFormatInfo2 ^ 0x5412);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static FormatInformation doDecodeFormatInformation(int maskedFormatInfo1, int maskedFormatInfo2) {
/* 119 */     int bestDifference = Integer.MAX_VALUE;
/* 120 */     int bestFormatInfo = 0;
/* 121 */     for (int i = 0; i < FORMAT_INFO_DECODE_LOOKUP.length; i++) {
/* 122 */       int[] decodeInfo = FORMAT_INFO_DECODE_LOOKUP[i];
/* 123 */       int targetInfo = decodeInfo[0];
/* 124 */       if (targetInfo == maskedFormatInfo1 || targetInfo == maskedFormatInfo2)
/*     */       {
/* 126 */         return new FormatInformation(decodeInfo[1]);
/*     */       }
/* 128 */       int bitsDifference = numBitsDiffering(maskedFormatInfo1, targetInfo);
/* 129 */       if (bitsDifference < bestDifference) {
/* 130 */         bestFormatInfo = decodeInfo[1];
/* 131 */         bestDifference = bitsDifference;
/*     */       } 
/* 133 */       if (maskedFormatInfo1 != maskedFormatInfo2) {
/*     */         
/* 135 */         bitsDifference = numBitsDiffering(maskedFormatInfo2, targetInfo);
/* 136 */         if (bitsDifference < bestDifference) {
/* 137 */           bestFormatInfo = decodeInfo[1];
/* 138 */           bestDifference = bitsDifference;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 144 */     if (bestDifference <= 3) {
/* 145 */       return new FormatInformation(bestFormatInfo);
/*     */     }
/* 147 */     return null;
/*     */   }
/*     */   
/*     */   ErrorCorrectionLevel getErrorCorrectionLevel() {
/* 151 */     return this.errorCorrectionLevel;
/*     */   }
/*     */   
/*     */   byte getDataMask() {
/* 155 */     return this.dataMask;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 159 */     return this.errorCorrectionLevel.ordinal() << 3 | this.dataMask;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 163 */     if (!(o instanceof FormatInformation)) {
/* 164 */       return false;
/*     */     }
/* 166 */     FormatInformation other = (FormatInformation)o;
/* 167 */     return (this.errorCorrectionLevel == other.errorCorrectionLevel && this.dataMask == other.dataMask);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/qrcode/FormatInformation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */