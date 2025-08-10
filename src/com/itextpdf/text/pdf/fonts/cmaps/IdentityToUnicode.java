/*     */ package com.itextpdf.text.pdf.fonts.cmaps;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IdentityToUnicode
/*     */ {
/*     */   private static CMapToUnicode identityCNS;
/*     */   private static CMapToUnicode identityJapan;
/*     */   private static CMapToUnicode identityKorea;
/*     */   private static CMapToUnicode identityGB;
/*     */   private static CMapToUnicode identityH;
/*     */   
/*     */   public static CMapToUnicode GetMapFromOrdering(String ordering) throws IOException {
/*  60 */     if (ordering.equals("CNS1")) {
/*  61 */       if (identityCNS == null) {
/*  62 */         CMapUniCid uni = CMapCache.getCachedCMapUniCid("UniCNS-UTF16-H");
/*  63 */         if (uni == null)
/*  64 */           return null; 
/*  65 */         identityCNS = uni.exportToUnicode();
/*     */       } 
/*  67 */       return identityCNS;
/*     */     } 
/*  69 */     if (ordering.equals("Japan1")) {
/*  70 */       if (identityJapan == null) {
/*  71 */         CMapUniCid uni = CMapCache.getCachedCMapUniCid("UniJIS-UTF16-H");
/*  72 */         if (uni == null)
/*  73 */           return null; 
/*  74 */         identityJapan = uni.exportToUnicode();
/*     */       } 
/*  76 */       return identityJapan;
/*     */     } 
/*  78 */     if (ordering.equals("Korea1")) {
/*  79 */       if (identityKorea == null) {
/*  80 */         CMapUniCid uni = CMapCache.getCachedCMapUniCid("UniKS-UTF16-H");
/*  81 */         if (uni == null)
/*  82 */           return null; 
/*  83 */         identityKorea = uni.exportToUnicode();
/*     */       } 
/*  85 */       return identityKorea;
/*     */     } 
/*  87 */     if (ordering.equals("GB1")) {
/*  88 */       if (identityGB == null) {
/*  89 */         CMapUniCid uni = CMapCache.getCachedCMapUniCid("UniGB-UTF16-H");
/*  90 */         if (uni == null)
/*  91 */           return null; 
/*  92 */         identityGB = uni.exportToUnicode();
/*     */       } 
/*  94 */       return identityGB;
/*     */     } 
/*  96 */     if (ordering.equals("Identity")) {
/*  97 */       if (identityH == null) {
/*  98 */         identityH = CMapToUnicode.getIdentity();
/*     */       }
/* 100 */       return identityH;
/*     */     } 
/* 102 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/cmaps/IdentityToUnicode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */