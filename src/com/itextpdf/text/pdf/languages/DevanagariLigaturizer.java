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
/*    */ 
/*    */ 
/*    */ public class DevanagariLigaturizer
/*    */   extends IndicLigaturizer
/*    */ {
/*    */   public static final char DEVA_MATRA_AA = 'ा';
/*    */   public static final char DEVA_MATRA_I = 'ि';
/*    */   public static final char DEVA_MATRA_E = 'े';
/*    */   public static final char DEVA_MATRA_AI = 'ै';
/*    */   public static final char DEVA_MATRA_HLR = 'ॢ';
/*    */   public static final char DEVA_MATRA_HLRR = 'ॣ';
/*    */   public static final char DEVA_LETTER_A = 'अ';
/*    */   public static final char DEVA_LETTER_AU = 'औ';
/*    */   public static final char DEVA_LETTER_KA = 'क';
/*    */   public static final char DEVA_LETTER_HA = 'ह';
/*    */   public static final char DEVA_HALANTA = '्';
/*    */   
/*    */   public DevanagariLigaturizer() {
/* 70 */     this.langTable = new char[11];
/* 71 */     this.langTable[0] = 'ा';
/* 72 */     this.langTable[1] = 'ि';
/* 73 */     this.langTable[2] = 'े';
/* 74 */     this.langTable[3] = 'ै';
/* 75 */     this.langTable[4] = 'ॢ';
/* 76 */     this.langTable[5] = 'ॣ';
/* 77 */     this.langTable[6] = 'अ';
/* 78 */     this.langTable[7] = 'औ';
/* 79 */     this.langTable[8] = 'क';
/* 80 */     this.langTable[9] = 'ह';
/* 81 */     this.langTable[10] = '्';
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/languages/DevanagariLigaturizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */