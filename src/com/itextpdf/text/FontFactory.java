/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FontFactory
/*     */ {
/*     */   public static final String COURIER = "Courier";
/*     */   public static final String COURIER_BOLD = "Courier-Bold";
/*     */   public static final String COURIER_OBLIQUE = "Courier-Oblique";
/*     */   public static final String COURIER_BOLDOBLIQUE = "Courier-BoldOblique";
/*     */   public static final String HELVETICA = "Helvetica";
/*     */   public static final String HELVETICA_BOLD = "Helvetica-Bold";
/*     */   public static final String HELVETICA_OBLIQUE = "Helvetica-Oblique";
/*     */   public static final String HELVETICA_BOLDOBLIQUE = "Helvetica-BoldOblique";
/*     */   public static final String SYMBOL = "Symbol";
/*     */   public static final String TIMES = "Times";
/*     */   public static final String TIMES_ROMAN = "Times-Roman";
/*     */   public static final String TIMES_BOLD = "Times-Bold";
/*     */   public static final String TIMES_ITALIC = "Times-Italic";
/*     */   public static final String TIMES_BOLDITALIC = "Times-BoldItalic";
/*     */   public static final String ZAPFDINGBATS = "ZapfDingbats";
/* 106 */   private static FontFactoryImp fontImp = new FontFactoryImp();
/*     */ 
/*     */   
/* 109 */   public static String defaultEncoding = "Cp1252";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean defaultEmbedding = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color) {
/* 131 */     return fontImp.getFont(fontname, encoding, embedded, size, style, color);
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
/*     */   public static Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color, boolean cached) {
/* 149 */     return fontImp.getFont(fontname, encoding, embedded, size, style, color, cached);
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
/*     */   public static Font getFont(String fontname, String encoding, boolean embedded, float size, int style) {
/* 164 */     return getFont(fontname, encoding, embedded, size, style, null);
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
/*     */   public static Font getFont(String fontname, String encoding, boolean embedded, float size) {
/* 178 */     return getFont(fontname, encoding, embedded, size, -1, null);
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
/*     */   public static Font getFont(String fontname, String encoding, boolean embedded) {
/* 191 */     return getFont(fontname, encoding, embedded, -1.0F, -1, null);
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
/*     */   public static Font getFont(String fontname, String encoding, float size, int style, BaseColor color) {
/* 206 */     return getFont(fontname, encoding, defaultEmbedding, size, style, color);
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
/*     */   public static Font getFont(String fontname, String encoding, float size, int style) {
/* 220 */     return getFont(fontname, encoding, defaultEmbedding, size, style, null);
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
/*     */   public static Font getFont(String fontname, String encoding, float size) {
/* 233 */     return getFont(fontname, encoding, defaultEmbedding, size, -1, null);
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
/*     */   public static Font getFont(String fontname, String encoding) {
/* 245 */     return getFont(fontname, encoding, defaultEmbedding, -1.0F, -1, null);
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
/*     */   public static Font getFont(String fontname, float size, int style, BaseColor color) {
/* 259 */     return getFont(fontname, defaultEncoding, defaultEmbedding, size, style, color);
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
/*     */   public static Font getFont(String fontname, float size, BaseColor color) {
/* 273 */     return getFont(fontname, defaultEncoding, defaultEmbedding, size, -1, color);
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
/*     */   public static Font getFont(String fontname, float size, int style) {
/* 286 */     return getFont(fontname, defaultEncoding, defaultEmbedding, size, style, null);
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
/*     */   public static Font getFont(String fontname, float size) {
/* 298 */     return getFont(fontname, defaultEncoding, defaultEmbedding, size, -1, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Font getFont(String fontname) {
/* 309 */     return getFont(fontname, defaultEncoding, defaultEmbedding, -1.0F, -1, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void registerFamily(String familyName, String fullName, String path) {
/* 319 */     fontImp.registerFamily(familyName, fullName, path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void register(String path) {
/* 329 */     register(path, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void register(String path, String alias) {
/* 340 */     fontImp.register(path, alias);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int registerDirectory(String dir) {
/* 348 */     return fontImp.registerDirectory(dir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int registerDirectory(String dir, boolean scanSubdirectories) {
/* 359 */     return fontImp.registerDirectory(dir, scanSubdirectories);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int registerDirectories() {
/* 367 */     return fontImp.registerDirectories();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<String> getRegisteredFonts() {
/* 376 */     return fontImp.getRegisteredFonts();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<String> getRegisteredFamilies() {
/* 385 */     return fontImp.getRegisteredFamilies();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(String fontname) {
/* 395 */     return fontImp.isRegistered(fontname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isRegistered(String fontname) {
/* 406 */     return fontImp.isRegistered(fontname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FontFactoryImp getFontImp() {
/* 414 */     return fontImp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setFontImp(FontFactoryImp fontImp) {
/* 422 */     if (fontImp == null)
/* 423 */       throw new NullPointerException(MessageLocalization.getComposedMessage("fontfactoryimp.cannot.be.null", new Object[0])); 
/* 424 */     FontFactory.fontImp = fontImp;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/FontFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */