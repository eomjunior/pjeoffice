/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.log.Level;
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import com.itextpdf.text.pdf.BaseFont;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Hashtable;
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
/*     */ public class FontFactoryImp
/*     */   implements FontProvider
/*     */ {
/*  66 */   private static final Logger LOGGER = LoggerFactory.getLogger(FontFactoryImp.class);
/*     */   
/*  68 */   private final Hashtable<String, String> trueTypeFonts = new Hashtable<String, String>();
/*     */   
/*  70 */   private static String[] TTFamilyOrder = new String[] { "3", "1", "1033", "3", "0", "1033", "1", "0", "0", "0", "3", "0" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   private final Hashtable<String, ArrayList<String>> fontFamilies = new Hashtable<String, ArrayList<String>>();
/*     */ 
/*     */   
/*  81 */   public String defaultEncoding = "Cp1252";
/*     */ 
/*     */   
/*     */   public boolean defaultEmbedding = false;
/*     */ 
/*     */   
/*     */   public FontFactoryImp() {
/*  88 */     this.trueTypeFonts.put("Courier".toLowerCase(), "Courier");
/*  89 */     this.trueTypeFonts.put("Courier-Bold".toLowerCase(), "Courier-Bold");
/*  90 */     this.trueTypeFonts.put("Courier-Oblique".toLowerCase(), "Courier-Oblique");
/*  91 */     this.trueTypeFonts.put("Courier-BoldOblique".toLowerCase(), "Courier-BoldOblique");
/*  92 */     this.trueTypeFonts.put("Helvetica".toLowerCase(), "Helvetica");
/*  93 */     this.trueTypeFonts.put("Helvetica-Bold".toLowerCase(), "Helvetica-Bold");
/*  94 */     this.trueTypeFonts.put("Helvetica-Oblique".toLowerCase(), "Helvetica-Oblique");
/*  95 */     this.trueTypeFonts.put("Helvetica-BoldOblique".toLowerCase(), "Helvetica-BoldOblique");
/*  96 */     this.trueTypeFonts.put("Symbol".toLowerCase(), "Symbol");
/*  97 */     this.trueTypeFonts.put("Times-Roman".toLowerCase(), "Times-Roman");
/*  98 */     this.trueTypeFonts.put("Times-Bold".toLowerCase(), "Times-Bold");
/*  99 */     this.trueTypeFonts.put("Times-Italic".toLowerCase(), "Times-Italic");
/* 100 */     this.trueTypeFonts.put("Times-BoldItalic".toLowerCase(), "Times-BoldItalic");
/* 101 */     this.trueTypeFonts.put("ZapfDingbats".toLowerCase(), "ZapfDingbats");
/*     */ 
/*     */     
/* 104 */     ArrayList<String> tmp = new ArrayList<String>();
/* 105 */     tmp.add("Courier");
/* 106 */     tmp.add("Courier-Bold");
/* 107 */     tmp.add("Courier-Oblique");
/* 108 */     tmp.add("Courier-BoldOblique");
/* 109 */     this.fontFamilies.put("Courier".toLowerCase(), tmp);
/* 110 */     tmp = new ArrayList<String>();
/* 111 */     tmp.add("Helvetica");
/* 112 */     tmp.add("Helvetica-Bold");
/* 113 */     tmp.add("Helvetica-Oblique");
/* 114 */     tmp.add("Helvetica-BoldOblique");
/* 115 */     this.fontFamilies.put("Helvetica".toLowerCase(), tmp);
/* 116 */     tmp = new ArrayList<String>();
/* 117 */     tmp.add("Symbol");
/* 118 */     this.fontFamilies.put("Symbol".toLowerCase(), tmp);
/* 119 */     tmp = new ArrayList<String>();
/* 120 */     tmp.add("Times-Roman");
/* 121 */     tmp.add("Times-Bold");
/* 122 */     tmp.add("Times-Italic");
/* 123 */     tmp.add("Times-BoldItalic");
/* 124 */     this.fontFamilies.put("Times".toLowerCase(), tmp);
/* 125 */     this.fontFamilies.put("Times-Roman".toLowerCase(), tmp);
/* 126 */     tmp = new ArrayList<String>();
/* 127 */     tmp.add("ZapfDingbats");
/* 128 */     this.fontFamilies.put("ZapfDingbats".toLowerCase(), tmp);
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
/*     */   public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color) {
/* 143 */     return getFont(fontname, encoding, embedded, size, style, color, true);
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
/*     */   public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color, boolean cached) {
/* 162 */     if (fontname == null) return new Font(Font.FontFamily.UNDEFINED, size, style, color); 
/* 163 */     String lowercasefontname = fontname.toLowerCase();
/* 164 */     ArrayList<String> tmp = this.fontFamilies.get(lowercasefontname);
/* 165 */     if (tmp != null) {
/* 166 */       synchronized (tmp) {
/*     */         
/* 168 */         int s = (style == -1) ? 0 : style;
/* 169 */         int fs = 0;
/* 170 */         boolean found = false;
/* 171 */         for (String f : tmp) {
/* 172 */           String lcf = f.toLowerCase();
/* 173 */           fs = 0;
/* 174 */           if (lcf.indexOf("bold") != -1) fs |= 0x1; 
/* 175 */           if (lcf.indexOf("italic") != -1 || lcf.indexOf("oblique") != -1) fs |= 0x2; 
/* 176 */           if ((s & 0x3) == fs) {
/* 177 */             fontname = f;
/* 178 */             found = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 182 */         if (style != -1 && found) {
/* 183 */           style &= fs ^ 0xFFFFFFFF;
/*     */         }
/*     */       } 
/*     */     }
/* 187 */     BaseFont basefont = null;
/*     */     try {
/* 189 */       basefont = getBaseFont(fontname, encoding, embedded, cached);
/* 190 */       if (basefont == null)
/*     */       {
/* 192 */         return new Font(Font.FontFamily.UNDEFINED, size, style, color);
/*     */       }
/*     */     }
/* 195 */     catch (DocumentException de) {
/*     */       
/* 197 */       throw new ExceptionConverter(de);
/*     */     }
/* 199 */     catch (IOException ioe) {
/*     */       
/* 201 */       return new Font(Font.FontFamily.UNDEFINED, size, style, color);
/*     */     }
/* 203 */     catch (NullPointerException npe) {
/*     */       
/* 205 */       return new Font(Font.FontFamily.UNDEFINED, size, style, color);
/*     */     } 
/* 207 */     return new Font(basefont, size, style, color);
/*     */   }
/*     */   
/*     */   protected BaseFont getBaseFont(String fontname, String encoding, boolean embedded, boolean cached) throws IOException, DocumentException {
/* 211 */     BaseFont basefont = null;
/*     */     
/*     */     try {
/* 214 */       basefont = BaseFont.createFont(fontname, encoding, embedded, cached, null, null, true);
/* 215 */     } catch (DocumentException documentException) {}
/*     */     
/* 217 */     if (basefont == null) {
/*     */       
/* 219 */       fontname = this.trueTypeFonts.get(fontname.toLowerCase());
/*     */       
/* 221 */       if (fontname != null) {
/* 222 */         basefont = BaseFont.createFont(fontname, encoding, embedded, cached, null, null);
/*     */       }
/*     */     } 
/* 225 */     return basefont;
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
/*     */   public Font getFont(String fontname, String encoding, boolean embedded, float size, int style) {
/* 240 */     return getFont(fontname, encoding, embedded, size, style, null);
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
/*     */   public Font getFont(String fontname, String encoding, boolean embedded, float size) {
/* 254 */     return getFont(fontname, encoding, embedded, size, -1, null);
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
/*     */   public Font getFont(String fontname, String encoding, boolean embedded) {
/* 267 */     return getFont(fontname, encoding, embedded, -1.0F, -1, null);
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
/*     */   public Font getFont(String fontname, String encoding, float size, int style, BaseColor color) {
/* 282 */     return getFont(fontname, encoding, this.defaultEmbedding, size, style, color);
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
/*     */   public Font getFont(String fontname, String encoding, float size, int style) {
/* 296 */     return getFont(fontname, encoding, this.defaultEmbedding, size, style, null);
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
/*     */   public Font getFont(String fontname, String encoding, float size) {
/* 309 */     return getFont(fontname, encoding, this.defaultEmbedding, size, -1, null);
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
/*     */   public Font getFont(String fontname, float size, BaseColor color) {
/* 324 */     return getFont(fontname, this.defaultEncoding, this.defaultEmbedding, size, -1, color);
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
/*     */   public Font getFont(String fontname, String encoding) {
/* 336 */     return getFont(fontname, encoding, this.defaultEmbedding, -1.0F, -1, null);
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
/*     */   public Font getFont(String fontname, float size, int style, BaseColor color) {
/* 350 */     return getFont(fontname, this.defaultEncoding, this.defaultEmbedding, size, style, color);
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
/*     */   public Font getFont(String fontname, float size, int style) {
/* 363 */     return getFont(fontname, this.defaultEncoding, this.defaultEmbedding, size, style, null);
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
/*     */   public Font getFont(String fontname, float size) {
/* 375 */     return getFont(fontname, this.defaultEncoding, this.defaultEmbedding, size, -1, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Font getFont(String fontname) {
/* 386 */     return getFont(fontname, this.defaultEncoding, this.defaultEmbedding, -1.0F, -1, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerFamily(String familyName, String fullName, String path) {
/*     */     ArrayList<String> tmp;
/* 396 */     if (path != null) {
/* 397 */       this.trueTypeFonts.put(fullName, path);
/*     */     }
/* 399 */     synchronized (this.fontFamilies) {
/* 400 */       tmp = this.fontFamilies.get(familyName);
/* 401 */       if (tmp == null) {
/* 402 */         tmp = new ArrayList<String>();
/* 403 */         this.fontFamilies.put(familyName, tmp);
/*     */       } 
/*     */     } 
/* 406 */     synchronized (tmp) {
/* 407 */       if (!tmp.contains(fullName)) {
/* 408 */         int fullNameLength = fullName.length();
/* 409 */         boolean inserted = false;
/* 410 */         for (int j = 0; j < tmp.size(); j++) {
/* 411 */           if (((String)tmp.get(j)).length() >= fullNameLength) {
/* 412 */             tmp.add(j, fullName);
/* 413 */             inserted = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 417 */         if (!inserted) {
/* 418 */           tmp.add(fullName);
/* 419 */           String newFullName = fullName.toLowerCase();
/* 420 */           if (newFullName.endsWith("regular")) {
/*     */             
/* 422 */             newFullName = newFullName.substring(0, newFullName.length() - 7).trim();
/*     */             
/* 424 */             tmp.add(0, fullName.substring(0, newFullName.length()));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(String path) {
/* 438 */     register(path, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(String path, String alias) {
/*     */     try {
/* 450 */       if (path.toLowerCase().endsWith(".ttf") || path.toLowerCase().endsWith(".otf") || path.toLowerCase().indexOf(".ttc,") > 0) {
/* 451 */         Object[] allNames = BaseFont.getAllFontNames(path, "Cp1252", null);
/* 452 */         this.trueTypeFonts.put(((String)allNames[0]).toLowerCase(), path);
/* 453 */         if (alias != null) {
/* 454 */           String lcAlias = alias.toLowerCase();
/* 455 */           this.trueTypeFonts.put(lcAlias, path);
/* 456 */           if (lcAlias.endsWith("regular"))
/*     */           {
/* 458 */             saveCopyOfRegularFont(lcAlias, path);
/*     */           }
/*     */         } 
/*     */         
/* 462 */         String[][] names = (String[][])allNames[2];
/* 463 */         for (String[] name : names) {
/* 464 */           String lcName = name[3].toLowerCase();
/* 465 */           this.trueTypeFonts.put(lcName, path);
/* 466 */           if (lcName.endsWith("regular"))
/*     */           {
/* 468 */             saveCopyOfRegularFont(lcName, path);
/*     */           }
/*     */         } 
/* 471 */         String fullName = null;
/* 472 */         String familyName = null;
/* 473 */         names = (String[][])allNames[1];
/* 474 */         for (int k = 0; k < TTFamilyOrder.length; k += 3) {
/* 475 */           for (String[] name : names) {
/* 476 */             if (TTFamilyOrder[k].equals(name[0]) && TTFamilyOrder[k + 1].equals(name[1]) && TTFamilyOrder[k + 2].equals(name[2])) {
/* 477 */               familyName = name[3].toLowerCase();
/* 478 */               k = TTFamilyOrder.length;
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/* 483 */         if (familyName != null) {
/* 484 */           String lastName = "";
/* 485 */           names = (String[][])allNames[2];
/* 486 */           for (String[] name : names) {
/* 487 */             for (int i = 0; i < TTFamilyOrder.length; i += 3) {
/* 488 */               if (TTFamilyOrder[i].equals(name[0]) && TTFamilyOrder[i + 1].equals(name[1]) && TTFamilyOrder[i + 2].equals(name[2])) {
/* 489 */                 fullName = name[3];
/* 490 */                 if (!fullName.equals(lastName)) {
/*     */                   
/* 492 */                   lastName = fullName;
/* 493 */                   registerFamily(familyName, fullName, null);
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 500 */       } else if (path.toLowerCase().endsWith(".ttc")) {
/* 501 */         if (alias != null)
/* 502 */           LOGGER.error("You can't define an alias for a true type collection."); 
/* 503 */         String[] names = BaseFont.enumerateTTCNames(path);
/* 504 */         for (int i = 0; i < names.length; i++) {
/* 505 */           register(path + "," + i);
/*     */         }
/*     */       }
/* 508 */       else if (path.toLowerCase().endsWith(".afm") || path.toLowerCase().endsWith(".pfm")) {
/* 509 */         BaseFont bf = BaseFont.createFont(path, "Cp1252", false);
/* 510 */         String fullName = bf.getFullFontName()[0][3].toLowerCase();
/* 511 */         String familyName = bf.getFamilyFontName()[0][3].toLowerCase();
/* 512 */         String psName = bf.getPostscriptFontName().toLowerCase();
/* 513 */         registerFamily(familyName, fullName, null);
/* 514 */         this.trueTypeFonts.put(psName, path);
/* 515 */         this.trueTypeFonts.put(fullName, path);
/*     */       } 
/* 517 */       if (LOGGER.isLogging(Level.TRACE)) {
/* 518 */         LOGGER.trace(String.format("Registered %s", new Object[] { path }));
/*     */       }
/*     */     }
/* 521 */     catch (DocumentException de) {
/*     */       
/* 523 */       throw new ExceptionConverter(de);
/*     */     }
/* 525 */     catch (IOException ioe) {
/* 526 */       throw new ExceptionConverter(ioe);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean saveCopyOfRegularFont(String regularFontName, String path) {
/* 535 */     String alias = regularFontName.substring(0, regularFontName.length() - 7).trim();
/* 536 */     if (!this.trueTypeFonts.containsKey(alias)) {
/* 537 */       this.trueTypeFonts.put(alias, path);
/* 538 */       return true;
/*     */     } 
/* 540 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int registerDirectory(String dir) {
/* 548 */     return registerDirectory(dir, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int registerDirectory(String dir, boolean scanSubdirectories) {
/* 559 */     if (LOGGER.isLogging(Level.DEBUG)) {
/* 560 */       LOGGER.debug(String.format("Registering directory %s, looking for fonts", new Object[] { dir }));
/*     */     }
/* 562 */     int count = 0;
/*     */     try {
/* 564 */       File file = new File(dir);
/* 565 */       if (!file.exists() || !file.isDirectory())
/* 566 */         return 0; 
/* 567 */       String[] files = file.list();
/* 568 */       if (files == null)
/* 569 */         return 0; 
/* 570 */       for (int k = 0; k < files.length; k++) {
/*     */         try {
/* 572 */           file = new File(dir, files[k]);
/* 573 */           if (file.isDirectory()) {
/* 574 */             if (scanSubdirectories) {
/* 575 */               count += registerDirectory(file.getAbsolutePath(), true);
/*     */             }
/*     */           } else {
/* 578 */             String name = file.getPath();
/* 579 */             String suffix = (name.length() < 4) ? null : name.substring(name.length() - 4).toLowerCase();
/* 580 */             if (".afm".equals(suffix) || ".pfm".equals(suffix)) {
/*     */               
/* 582 */               File pfb = new File(name.substring(0, name.length() - 4) + ".pfb");
/* 583 */               if (pfb.exists()) {
/* 584 */                 register(name, null);
/* 585 */                 count++;
/*     */               } 
/* 587 */             } else if (".ttf".equals(suffix) || ".otf".equals(suffix) || ".ttc".equals(suffix)) {
/* 588 */               register(name, null);
/* 589 */               count++;
/*     */             }
/*     */           
/*     */           } 
/* 593 */         } catch (Exception exception) {}
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 598 */     catch (Exception exception) {}
/*     */ 
/*     */     
/* 601 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int registerDirectories() {
/* 609 */     int count = 0;
/* 610 */     String windir = null;
/*     */     try {
/* 612 */       windir = System.getenv("windir");
/* 613 */     } catch (SecurityException e) {
/* 614 */       LOGGER.warn("Can't access System.getenv(\"windir\") to load fonts. Please, add RuntimePermission for getenv.windir.");
/*     */     } 
/* 616 */     String fileseparator = System.getProperty("file.separator");
/* 617 */     if (windir != null && fileseparator != null) {
/* 618 */       count += registerDirectory(windir + fileseparator + "fonts");
/*     */     }
/* 620 */     count += registerDirectory("/usr/share/X11/fonts", true);
/* 621 */     count += registerDirectory("/usr/X/lib/X11/fonts", true);
/* 622 */     count += registerDirectory("/usr/openwin/lib/X11/fonts", true);
/* 623 */     count += registerDirectory("/usr/share/fonts", true);
/* 624 */     count += registerDirectory("/usr/X11R6/lib/X11/fonts", true);
/* 625 */     count += registerDirectory("/Library/Fonts");
/* 626 */     count += registerDirectory("/System/Library/Fonts");
/* 627 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getRegisteredFonts() {
/* 636 */     return this.trueTypeFonts.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getRegisteredFamilies() {
/* 645 */     return this.fontFamilies.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRegistered(String fontname) {
/* 655 */     return this.trueTypeFonts.containsKey(fontname.toLowerCase());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/FontFactoryImp.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */