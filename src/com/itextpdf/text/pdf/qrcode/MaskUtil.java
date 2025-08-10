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
/*     */ public final class MaskUtil
/*     */ {
/*     */   public static int applyMaskPenaltyRule1(ByteMatrix matrix) {
/*  33 */     return applyMaskPenaltyRule1Internal(matrix, true) + applyMaskPenaltyRule1Internal(matrix, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int applyMaskPenaltyRule2(ByteMatrix matrix) {
/*  39 */     int penalty = 0;
/*  40 */     byte[][] array = matrix.getArray();
/*  41 */     int width = matrix.getWidth();
/*  42 */     int height = matrix.getHeight();
/*  43 */     for (int y = 0; y < height - 1; y++) {
/*  44 */       for (int x = 0; x < width - 1; x++) {
/*  45 */         int value = array[y][x];
/*  46 */         if (value == array[y][x + 1] && value == array[y + 1][x] && value == array[y + 1][x + 1]) {
/*  47 */           penalty += 3;
/*     */         }
/*     */       } 
/*     */     } 
/*  51 */     return penalty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int applyMaskPenaltyRule3(ByteMatrix matrix) {
/*  58 */     int penalty = 0;
/*  59 */     byte[][] array = matrix.getArray();
/*  60 */     int width = matrix.getWidth();
/*  61 */     int height = matrix.getHeight();
/*  62 */     for (int y = 0; y < height; y++) {
/*  63 */       for (int x = 0; x < width; x++) {
/*     */         
/*  65 */         if (x + 6 < width && array[y][x] == 1 && array[y][x + 1] == 0 && array[y][x + 2] == 1 && array[y][x + 3] == 1 && array[y][x + 4] == 1 && array[y][x + 5] == 0 && array[y][x + 6] == 1 && ((x + 10 < width && array[y][x + 7] == 0 && array[y][x + 8] == 0 && array[y][x + 9] == 0 && array[y][x + 10] == 0) || (x - 4 >= 0 && array[y][x - 1] == 0 && array[y][x - 2] == 0 && array[y][x - 3] == 0 && array[y][x - 4] == 0)))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  83 */           penalty += 40;
/*     */         }
/*  85 */         if (y + 6 < height && array[y][x] == 1 && array[y + 1][x] == 0 && array[y + 2][x] == 1 && array[y + 3][x] == 1 && array[y + 4][x] == 1 && array[y + 5][x] == 0 && array[y + 6][x] == 1 && ((y + 10 < height && array[y + 7][x] == 0 && array[y + 8][x] == 0 && array[y + 9][x] == 0 && array[y + 10][x] == 0) || (y - 4 >= 0 && array[y - 1][x] == 0 && array[y - 2][x] == 0 && array[y - 3][x] == 0 && array[y - 4][x] == 0)))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 103 */           penalty += 40;
/*     */         }
/*     */       } 
/*     */     } 
/* 107 */     return penalty;
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
/*     */   public static int applyMaskPenaltyRule4(ByteMatrix matrix) {
/* 120 */     int numDarkCells = 0;
/* 121 */     byte[][] array = matrix.getArray();
/* 122 */     int width = matrix.getWidth();
/* 123 */     int height = matrix.getHeight();
/* 124 */     for (int y = 0; y < height; y++) {
/* 125 */       for (int x = 0; x < width; x++) {
/* 126 */         if (array[y][x] == 1) {
/* 127 */           numDarkCells++;
/*     */         }
/*     */       } 
/*     */     } 
/* 131 */     int numTotalCells = matrix.getHeight() * matrix.getWidth();
/* 132 */     double darkRatio = numDarkCells / numTotalCells;
/* 133 */     return Math.abs((int)(darkRatio * 100.0D - 50.0D)) / 5 * 10;
/*     */   }
/*     */   
/*     */   public static boolean getDataMaskBit(int maskPattern, int x, int y) {
/*     */     int intermediate;
/*     */     int temp;
/* 139 */     if (!QRCode.isValidMaskPattern(maskPattern)) {
/* 140 */       throw new IllegalArgumentException("Invalid mask pattern");
/*     */     }
/*     */     
/* 143 */     switch (maskPattern) {
/*     */       case 0:
/* 145 */         intermediate = y + x & 0x1;
/*     */         break;
/*     */       case 1:
/* 148 */         intermediate = y & 0x1;
/*     */         break;
/*     */       case 2:
/* 151 */         intermediate = x % 3;
/*     */         break;
/*     */       case 3:
/* 154 */         intermediate = (y + x) % 3;
/*     */         break;
/*     */       case 4:
/* 157 */         intermediate = (y >>> 1) + x / 3 & 0x1;
/*     */         break;
/*     */       case 5:
/* 160 */         temp = y * x;
/* 161 */         intermediate = (temp & 0x1) + temp % 3;
/*     */         break;
/*     */       case 6:
/* 164 */         temp = y * x;
/* 165 */         intermediate = (temp & 0x1) + temp % 3 & 0x1;
/*     */         break;
/*     */       case 7:
/* 168 */         temp = y * x;
/* 169 */         intermediate = temp % 3 + (y + x & 0x1) & 0x1;
/*     */         break;
/*     */       default:
/* 172 */         throw new IllegalArgumentException("Invalid mask pattern: " + maskPattern);
/*     */     } 
/* 174 */     return (intermediate == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int applyMaskPenaltyRule1Internal(ByteMatrix matrix, boolean isHorizontal) {
/* 180 */     int penalty = 0;
/* 181 */     int numSameBitCells = 0;
/* 182 */     int prevBit = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     int iLimit = isHorizontal ? matrix.getHeight() : matrix.getWidth();
/* 192 */     int jLimit = isHorizontal ? matrix.getWidth() : matrix.getHeight();
/* 193 */     byte[][] array = matrix.getArray();
/* 194 */     for (int i = 0; i < iLimit; i++) {
/* 195 */       for (int j = 0; j < jLimit; j++) {
/* 196 */         int bit = isHorizontal ? array[i][j] : array[j][i];
/* 197 */         if (bit == prevBit) {
/* 198 */           numSameBitCells++;
/*     */ 
/*     */           
/* 201 */           if (numSameBitCells == 5) {
/* 202 */             penalty += 3;
/* 203 */           } else if (numSameBitCells > 5) {
/*     */ 
/*     */             
/* 206 */             penalty++;
/*     */           } 
/*     */         } else {
/* 209 */           numSameBitCells = 1;
/* 210 */           prevBit = bit;
/*     */         } 
/*     */       } 
/* 213 */       numSameBitCells = 0;
/*     */     } 
/* 215 */     return penalty;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/qrcode/MaskUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */