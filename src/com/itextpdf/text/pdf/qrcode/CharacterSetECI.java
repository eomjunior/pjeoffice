/*    */ package com.itextpdf.text.pdf.qrcode;
/*    */ 
/*    */ import java.util.HashMap;
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
/*    */ public final class CharacterSetECI
/*    */ {
/*    */   private static HashMap<String, CharacterSetECI> NAME_TO_ECI;
/*    */   private final String encodingName;
/*    */   private final int value;
/*    */   
/*    */   private static void initialize() {
/* 33 */     HashMap<String, CharacterSetECI> n = new HashMap<String, CharacterSetECI>(29);
/*    */     
/* 35 */     addCharacterSet(0, "Cp437", n);
/* 36 */     addCharacterSet(1, new String[] { "ISO8859_1", "ISO-8859-1" }, n);
/* 37 */     addCharacterSet(2, "Cp437", n);
/* 38 */     addCharacterSet(3, new String[] { "ISO8859_1", "ISO-8859-1" }, n);
/* 39 */     addCharacterSet(4, new String[] { "ISO8859_2", "ISO-8859-2" }, n);
/* 40 */     addCharacterSet(5, new String[] { "ISO8859_3", "ISO-8859-3" }, n);
/* 41 */     addCharacterSet(6, new String[] { "ISO8859_4", "ISO-8859-4" }, n);
/* 42 */     addCharacterSet(7, new String[] { "ISO8859_5", "ISO-8859-5" }, n);
/* 43 */     addCharacterSet(8, new String[] { "ISO8859_6", "ISO-8859-6" }, n);
/* 44 */     addCharacterSet(9, new String[] { "ISO8859_7", "ISO-8859-7" }, n);
/* 45 */     addCharacterSet(10, new String[] { "ISO8859_8", "ISO-8859-8" }, n);
/* 46 */     addCharacterSet(11, new String[] { "ISO8859_9", "ISO-8859-9" }, n);
/* 47 */     addCharacterSet(12, new String[] { "ISO8859_10", "ISO-8859-10" }, n);
/* 48 */     addCharacterSet(13, new String[] { "ISO8859_11", "ISO-8859-11" }, n);
/* 49 */     addCharacterSet(15, new String[] { "ISO8859_13", "ISO-8859-13" }, n);
/* 50 */     addCharacterSet(16, new String[] { "ISO8859_14", "ISO-8859-14" }, n);
/* 51 */     addCharacterSet(17, new String[] { "ISO8859_15", "ISO-8859-15" }, n);
/* 52 */     addCharacterSet(18, new String[] { "ISO8859_16", "ISO-8859-16" }, n);
/* 53 */     addCharacterSet(20, new String[] { "SJIS", "Shift_JIS" }, n);
/* 54 */     NAME_TO_ECI = n;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private CharacterSetECI(int value, String encodingName) {
/* 61 */     this.encodingName = encodingName;
/* 62 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getEncodingName() {
/* 66 */     return this.encodingName;
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 70 */     return this.value;
/*    */   }
/*    */   
/*    */   private static void addCharacterSet(int value, String encodingName, HashMap<String, CharacterSetECI> n) {
/* 74 */     CharacterSetECI eci = new CharacterSetECI(value, encodingName);
/* 75 */     n.put(encodingName, eci);
/*    */   }
/*    */   
/*    */   private static void addCharacterSet(int value, String[] encodingNames, HashMap<String, CharacterSetECI> n) {
/* 79 */     CharacterSetECI eci = new CharacterSetECI(value, encodingNames[0]);
/* 80 */     for (int i = 0; i < encodingNames.length; i++) {
/* 81 */       n.put(encodingNames[i], eci);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CharacterSetECI getCharacterSetECIByName(String name) {
/* 91 */     if (NAME_TO_ECI == null) {
/* 92 */       initialize();
/*    */     }
/* 94 */     return NAME_TO_ECI.get(name);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/qrcode/CharacterSetECI.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */