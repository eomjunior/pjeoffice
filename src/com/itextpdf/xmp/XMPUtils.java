/*     */ package com.itextpdf.xmp;
/*     */ 
/*     */ import com.itextpdf.xmp.impl.Base64;
/*     */ import com.itextpdf.xmp.impl.ISO8601Converter;
/*     */ import com.itextpdf.xmp.impl.XMPUtilsImpl;
/*     */ import com.itextpdf.xmp.options.PropertyOptions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMPUtils
/*     */ {
/*     */   public static String catenateArrayItems(XMPMeta xmp, String schemaNS, String arrayName, String separator, String quotes, boolean allowCommas) throws XMPException {
/*  60 */     return 
/*  61 */       XMPUtilsImpl.catenateArrayItems(xmp, schemaNS, arrayName, separator, quotes, allowCommas);
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
/*     */   public static void separateArrayItems(XMPMeta xmp, String schemaNS, String arrayName, String catedStr, PropertyOptions arrayOptions, boolean preserveCommas) throws XMPException {
/*  87 */     XMPUtilsImpl.separateArrayItems(xmp, schemaNS, arrayName, catedStr, arrayOptions, preserveCommas);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeProperties(XMPMeta xmp, String schemaNS, String propName, boolean doAllProperties, boolean includeAliases) throws XMPException {
/* 141 */     XMPUtilsImpl.removeProperties(xmp, schemaNS, propName, doAllProperties, includeAliases);
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
/*     */   public static void appendProperties(XMPMeta source, XMPMeta dest, boolean doAllProperties, boolean replaceOldValues) throws XMPException {
/* 157 */     appendProperties(source, dest, doAllProperties, replaceOldValues, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void appendProperties(XMPMeta source, XMPMeta dest, boolean doAllProperties, boolean replaceOldValues, boolean deleteEmptyValues) throws XMPException {
/* 241 */     XMPUtilsImpl.appendProperties(source, dest, doAllProperties, replaceOldValues, deleteEmptyValues);
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
/*     */   public static boolean convertToBoolean(String value) throws XMPException {
/* 264 */     if (value == null || value.length() == 0)
/*     */     {
/* 266 */       throw new XMPException("Empty convert-string", 5);
/*     */     }
/* 268 */     value = value.toLowerCase();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 273 */       return (Integer.parseInt(value) != 0);
/*     */     }
/* 275 */     catch (NumberFormatException e) {
/*     */       
/* 277 */       return ("true"
/* 278 */         .equals(value) || "t"
/* 279 */         .equals(value) || "on"
/* 280 */         .equals(value) || "yes"
/* 281 */         .equals(value));
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static String convertFromBoolean(boolean value) {
/* 297 */     return value ? "True" : "False";
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
/*     */   public static int convertToInteger(String rawValue) throws XMPException {
/*     */     try {
/* 315 */       if (rawValue == null || rawValue.length() == 0)
/*     */       {
/* 317 */         throw new XMPException("Empty convert-string", 5);
/*     */       }
/* 319 */       if (rawValue.startsWith("0x"))
/*     */       {
/* 321 */         return Integer.parseInt(rawValue.substring(2), 16);
/*     */       }
/*     */ 
/*     */       
/* 325 */       return Integer.parseInt(rawValue);
/*     */     
/*     */     }
/* 328 */     catch (NumberFormatException e) {
/*     */       
/* 330 */       throw new XMPException("Invalid integer string", 5);
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
/*     */   
/*     */   public static String convertFromInteger(int value) {
/* 344 */     return String.valueOf(value);
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
/*     */   public static long convertToLong(String rawValue) throws XMPException {
/*     */     try {
/* 362 */       if (rawValue == null || rawValue.length() == 0)
/*     */       {
/* 364 */         throw new XMPException("Empty convert-string", 5);
/*     */       }
/* 366 */       if (rawValue.startsWith("0x"))
/*     */       {
/* 368 */         return Long.parseLong(rawValue.substring(2), 16);
/*     */       }
/*     */ 
/*     */       
/* 372 */       return Long.parseLong(rawValue);
/*     */     
/*     */     }
/* 375 */     catch (NumberFormatException e) {
/*     */       
/* 377 */       throw new XMPException("Invalid long string", 5);
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
/*     */   
/*     */   public static String convertFromLong(long value) {
/* 391 */     return String.valueOf(value);
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
/*     */   public static double convertToDouble(String rawValue) throws XMPException {
/*     */     try {
/* 409 */       if (rawValue == null || rawValue.length() == 0)
/*     */       {
/* 411 */         throw new XMPException("Empty convert-string", 5);
/*     */       }
/*     */ 
/*     */       
/* 415 */       return Double.parseDouble(rawValue);
/*     */     
/*     */     }
/* 418 */     catch (NumberFormatException e) {
/*     */       
/* 420 */       throw new XMPException("Invalid double string", 5);
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
/*     */   
/*     */   public static String convertFromDouble(double value) {
/* 434 */     return String.valueOf(value);
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
/*     */   public static XMPDateTime convertToDate(String rawValue) throws XMPException {
/* 450 */     if (rawValue == null || rawValue.length() == 0)
/*     */     {
/* 452 */       throw new XMPException("Empty convert-string", 5);
/*     */     }
/*     */ 
/*     */     
/* 456 */     return ISO8601Converter.parse(rawValue);
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
/*     */   public static String convertFromDate(XMPDateTime value) {
/* 470 */     return ISO8601Converter.render(value);
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
/*     */   public static String encodeBase64(byte[] buffer) {
/* 483 */     return new String(Base64.encode(buffer));
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
/*     */   public static byte[] decodeBase64(String base64String) throws XMPException {
/*     */     try {
/* 499 */       return Base64.decode(base64String.getBytes());
/*     */     }
/* 501 */     catch (Throwable e) {
/*     */       
/* 503 */       throw new XMPException("Invalid base64 string", 5, e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/XMPUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */