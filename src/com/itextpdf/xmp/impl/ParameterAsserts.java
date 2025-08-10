/*     */ package com.itextpdf.xmp.impl;
/*     */ 
/*     */ import com.itextpdf.xmp.XMPConst;
/*     */ import com.itextpdf.xmp.XMPException;
/*     */ import com.itextpdf.xmp.XMPMeta;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ParameterAsserts
/*     */   implements XMPConst
/*     */ {
/*     */   public static void assertArrayName(String arrayName) throws XMPException {
/*  60 */     if (arrayName == null || arrayName.length() == 0)
/*     */     {
/*  62 */       throw new XMPException("Empty array name", 4);
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
/*     */   public static void assertPropName(String propName) throws XMPException {
/*  74 */     if (propName == null || propName.length() == 0)
/*     */     {
/*  76 */       throw new XMPException("Empty property name", 4);
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
/*     */   public static void assertSchemaNS(String schemaNS) throws XMPException {
/*  88 */     if (schemaNS == null || schemaNS.length() == 0)
/*     */     {
/*  90 */       throw new XMPException("Empty schema namespace URI", 4);
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
/*     */   public static void assertPrefix(String prefix) throws XMPException {
/* 102 */     if (prefix == null || prefix.length() == 0)
/*     */     {
/* 104 */       throw new XMPException("Empty prefix", 4);
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
/*     */   public static void assertSpecificLang(String specificLang) throws XMPException {
/* 116 */     if (specificLang == null || specificLang.length() == 0)
/*     */     {
/* 118 */       throw new XMPException("Empty specific language", 4);
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
/*     */   public static void assertStructName(String structName) throws XMPException {
/* 130 */     if (structName == null || structName.length() == 0)
/*     */     {
/* 132 */       throw new XMPException("Empty array name", 4);
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
/*     */   public static void assertNotNull(Object param) throws XMPException {
/* 144 */     if (param == null)
/*     */     {
/* 146 */       throw new XMPException("Parameter must not be null", 4);
/*     */     }
/* 148 */     if (param instanceof String && ((String)param).length() == 0)
/*     */     {
/* 150 */       throw new XMPException("Parameter must not be null or empty", 4);
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
/*     */   public static void assertImplementation(XMPMeta xmp) throws XMPException {
/* 163 */     if (xmp == null)
/*     */     {
/* 165 */       throw new XMPException("Parameter must not be null", 4);
/*     */     }
/*     */     
/* 168 */     if (!(xmp instanceof XMPMetaImpl))
/*     */     {
/* 170 */       throw new XMPException("The XMPMeta-object is not compatible with this implementation", 4);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/ParameterAsserts.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */