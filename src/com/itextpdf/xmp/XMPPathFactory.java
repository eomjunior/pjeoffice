/*     */ package com.itextpdf.xmp;
/*     */ 
/*     */ import com.itextpdf.xmp.impl.Utils;
/*     */ import com.itextpdf.xmp.impl.xpath.XMPPath;
/*     */ import com.itextpdf.xmp.impl.xpath.XMPPathParser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class XMPPathFactory
/*     */ {
/*     */   public static String composeArrayItemPath(String arrayName, int itemIndex) throws XMPException {
/* 106 */     if (itemIndex > 0)
/*     */     {
/* 108 */       return arrayName + '[' + itemIndex + ']';
/*     */     }
/* 110 */     if (itemIndex == -1)
/*     */     {
/* 112 */       return arrayName + "[last()]";
/*     */     }
/*     */ 
/*     */     
/* 116 */     throw new XMPException("Array index must be larger than zero", 104);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String composeStructFieldPath(String fieldNS, String fieldName) throws XMPException {
/* 138 */     assertFieldNS(fieldNS);
/* 139 */     assertFieldName(fieldName);
/*     */     
/* 141 */     XMPPath fieldPath = XMPPathParser.expandXPath(fieldNS, fieldName);
/* 142 */     if (fieldPath.size() != 2)
/*     */     {
/* 144 */       throw new XMPException("The field name must be simple", 102);
/*     */     }
/*     */     
/* 147 */     return '/' + fieldPath.getSegment(1).getName();
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
/*     */ 
/*     */   
/*     */   public static String composeQualifierPath(String qualNS, String qualName) throws XMPException {
/* 167 */     assertQualNS(qualNS);
/* 168 */     assertQualName(qualName);
/*     */     
/* 170 */     XMPPath qualPath = XMPPathParser.expandXPath(qualNS, qualName);
/* 171 */     if (qualPath.size() != 2)
/*     */     {
/* 173 */       throw new XMPException("The qualifier name must be simple", 102);
/*     */     }
/*     */     
/* 176 */     return "/?" + qualPath.getSegment(1).getName();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String composeLangSelector(String arrayName, String langName) {
/* 205 */     return arrayName + "[?xml:lang=\"" + Utils.normalizeLangValue(langName) + "\"]";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String composeFieldSelector(String arrayName, String fieldNS, String fieldName, String fieldValue) throws XMPException {
/* 245 */     XMPPath fieldPath = XMPPathParser.expandXPath(fieldNS, fieldName);
/* 246 */     if (fieldPath.size() != 2)
/*     */     {
/* 248 */       throw new XMPException("The fieldName name must be simple", 102);
/*     */     }
/*     */     
/* 251 */     return arrayName + '[' + fieldPath.getSegment(1).getName() + "=\"" + fieldValue + "\"]";
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
/*     */   private static void assertQualNS(String qualNS) throws XMPException {
/* 263 */     if (qualNS == null || qualNS.length() == 0)
/*     */     {
/* 265 */       throw new XMPException("Empty qualifier namespace URI", 101);
/*     */     }
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
/*     */   private static void assertQualName(String qualName) throws XMPException {
/* 278 */     if (qualName == null || qualName.length() == 0)
/*     */     {
/* 280 */       throw new XMPException("Empty qualifier name", 102);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void assertFieldNS(String fieldNS) throws XMPException {
/* 292 */     if (fieldNS == null || fieldNS.length() == 0)
/*     */     {
/* 294 */       throw new XMPException("Empty field namespace URI", 101);
/*     */     }
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
/*     */   private static void assertFieldName(String fieldName) throws XMPException {
/* 307 */     if (fieldName == null || fieldName.length() == 0)
/*     */     {
/* 309 */       throw new XMPException("Empty f name", 102);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/XMPPathFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */