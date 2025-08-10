/*    */ package com.itextpdf.text.pdf.languages;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GujaratiLigaturizer
/*    */   extends IndicLigaturizer
/*    */ {
/*    */   public static final char GUJR_MATRA_AA = 'ા';
/*    */   public static final char GUJR_MATRA_I = 'િ';
/*    */   public static final char GUJR_MATRA_E = 'ે';
/*    */   public static final char GUJR_MATRA_AI = 'ૈ';
/*    */   public static final char GUJR_MATRA_HLR = 'ૢ';
/*    */   public static final char GUJR_MATRA_HLRR = 'ૣ';
/*    */   public static final char GUJR_LETTER_A = 'અ';
/*    */   public static final char GUJR_LETTER_AU = 'ઔ';
/*    */   public static final char GUJR_LETTER_KA = 'ક';
/*    */   public static final char GUJR_LETTER_HA = 'હ';
/*    */   public static final char GUJR_HALANTA = '્';
/*    */   
/*    */   public GujaratiLigaturizer() {
/* 68 */     this.langTable = new char[11];
/* 69 */     this.langTable[0] = 'ા';
/* 70 */     this.langTable[1] = 'િ';
/* 71 */     this.langTable[2] = 'ે';
/* 72 */     this.langTable[3] = 'ૈ';
/* 73 */     this.langTable[4] = 'ૢ';
/* 74 */     this.langTable[5] = 'ૣ';
/* 75 */     this.langTable[6] = 'અ';
/* 76 */     this.langTable[7] = 'ઔ';
/* 77 */     this.langTable[8] = 'ક';
/* 78 */     this.langTable[9] = 'હ';
/* 79 */     this.langTable[10] = '્';
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/languages/GujaratiLigaturizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */