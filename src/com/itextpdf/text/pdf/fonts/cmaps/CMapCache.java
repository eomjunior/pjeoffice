/*     */ package com.itextpdf.text.pdf.fonts.cmaps;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CMapCache
/*     */ {
/*  54 */   private static final HashMap<String, CMapUniCid> cacheUniCid = new HashMap<String, CMapUniCid>();
/*  55 */   private static final HashMap<String, CMapCidUni> cacheCidUni = new HashMap<String, CMapCidUni>();
/*  56 */   private static final HashMap<String, CMapCidByte> cacheCidByte = new HashMap<String, CMapCidByte>();
/*  57 */   private static final HashMap<String, CMapByteCid> cacheByteCid = new HashMap<String, CMapByteCid>();
/*     */   
/*     */   public static CMapUniCid getCachedCMapUniCid(String name) throws IOException {
/*  60 */     CMapUniCid cmap = null;
/*  61 */     synchronized (cacheUniCid) {
/*  62 */       cmap = cacheUniCid.get(name);
/*     */     } 
/*  64 */     if (cmap == null) {
/*  65 */       cmap = new CMapUniCid();
/*  66 */       CMapParserEx.parseCid(name, cmap, new CidResource());
/*  67 */       synchronized (cacheUniCid) {
/*  68 */         cacheUniCid.put(name, cmap);
/*     */       } 
/*     */     } 
/*  71 */     return cmap;
/*     */   }
/*     */   
/*     */   public static CMapCidUni getCachedCMapCidUni(String name) throws IOException {
/*  75 */     CMapCidUni cmap = null;
/*  76 */     synchronized (cacheCidUni) {
/*  77 */       cmap = cacheCidUni.get(name);
/*     */     } 
/*  79 */     if (cmap == null) {
/*  80 */       cmap = new CMapCidUni();
/*  81 */       CMapParserEx.parseCid(name, cmap, new CidResource());
/*  82 */       synchronized (cacheCidUni) {
/*  83 */         cacheCidUni.put(name, cmap);
/*     */       } 
/*     */     } 
/*  86 */     return cmap;
/*     */   }
/*     */   
/*     */   public static CMapCidByte getCachedCMapCidByte(String name) throws IOException {
/*  90 */     CMapCidByte cmap = null;
/*  91 */     synchronized (cacheCidByte) {
/*  92 */       cmap = cacheCidByte.get(name);
/*     */     } 
/*  94 */     if (cmap == null) {
/*  95 */       cmap = new CMapCidByte();
/*  96 */       CMapParserEx.parseCid(name, cmap, new CidResource());
/*  97 */       synchronized (cacheCidByte) {
/*  98 */         cacheCidByte.put(name, cmap);
/*     */       } 
/*     */     } 
/* 101 */     return cmap;
/*     */   }
/*     */   
/*     */   public static CMapByteCid getCachedCMapByteCid(String name) throws IOException {
/* 105 */     CMapByteCid cmap = null;
/* 106 */     synchronized (cacheByteCid) {
/* 107 */       cmap = cacheByteCid.get(name);
/*     */     } 
/* 109 */     if (cmap == null) {
/* 110 */       cmap = new CMapByteCid();
/* 111 */       CMapParserEx.parseCid(name, cmap, new CidResource());
/* 112 */       synchronized (cacheByteCid) {
/* 113 */         cacheByteCid.put(name, cmap);
/*     */       } 
/*     */     } 
/* 116 */     return cmap;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/cmaps/CMapCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */