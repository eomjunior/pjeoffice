/*     */ package com.itextpdf.awt;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.pdf.BaseFont;
/*     */ import java.awt.Font;
/*     */ import java.io.File;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultFontMapper
/*     */   implements FontMapper
/*     */ {
/*     */   public static class BaseFontParameters
/*     */   {
/*     */     public String fontName;
/*     */     public String encoding;
/*     */     public boolean embedded;
/*     */     public boolean cached;
/*     */     public byte[] ttfAfm;
/*     */     public byte[] pfb;
/*     */     
/*     */     public BaseFontParameters(String fontName) {
/*  84 */       this.fontName = fontName;
/*  85 */       this.encoding = "Cp1252";
/*  86 */       this.embedded = true;
/*  87 */       this.cached = true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  93 */   private HashMap<String, String> aliases = new HashMap<String, String>();
/*     */ 
/*     */   
/*  96 */   private HashMap<String, BaseFontParameters> mapper = new HashMap<String, BaseFontParameters>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseFont awtToPdf(Font font) {
/*     */     try {
/* 106 */       BaseFontParameters p = getBaseFontParameters(font.getFontName());
/* 107 */       if (p != null)
/* 108 */         return BaseFont.createFont(p.fontName, p.encoding, p.embedded, p.cached, p.ttfAfm, p.pfb); 
/* 109 */       String fontKey = null;
/* 110 */       String logicalName = font.getName();
/*     */       
/* 112 */       if (logicalName.equalsIgnoreCase("DialogInput") || logicalName.equalsIgnoreCase("Monospaced") || logicalName.equalsIgnoreCase("Courier")) {
/*     */         
/* 114 */         if (font.isItalic()) {
/* 115 */           if (font.isBold()) {
/* 116 */             fontKey = "Courier-BoldOblique";
/*     */           } else {
/*     */             
/* 119 */             fontKey = "Courier-Oblique";
/*     */           }
/*     */         
/*     */         }
/* 123 */         else if (font.isBold()) {
/* 124 */           fontKey = "Courier-Bold";
/*     */         } else {
/*     */           
/* 127 */           fontKey = "Courier";
/*     */         }
/*     */       
/*     */       }
/* 131 */       else if (logicalName.equalsIgnoreCase("Serif") || logicalName.equalsIgnoreCase("TimesRoman")) {
/*     */         
/* 133 */         if (font.isItalic()) {
/* 134 */           if (font.isBold()) {
/* 135 */             fontKey = "Times-BoldItalic";
/*     */           } else {
/*     */             
/* 138 */             fontKey = "Times-Italic";
/*     */           }
/*     */         
/*     */         }
/* 142 */         else if (font.isBold()) {
/* 143 */           fontKey = "Times-Bold";
/*     */         } else {
/*     */           
/* 146 */           fontKey = "Times-Roman";
/*     */         
/*     */         }
/*     */ 
/*     */       
/*     */       }
/* 152 */       else if (font.isItalic()) {
/* 153 */         if (font.isBold()) {
/* 154 */           fontKey = "Helvetica-BoldOblique";
/*     */         } else {
/*     */           
/* 157 */           fontKey = "Helvetica-Oblique";
/*     */         }
/*     */       
/*     */       }
/* 161 */       else if (font.isBold()) {
/* 162 */         fontKey = "Helvetica-Bold";
/*     */       } else {
/* 164 */         fontKey = "Helvetica";
/*     */       } 
/*     */ 
/*     */       
/* 168 */       return BaseFont.createFont(fontKey, "Cp1252", false);
/*     */     }
/* 170 */     catch (Exception e) {
/* 171 */       throw new ExceptionConverter(e);
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
/*     */   public Font pdfToAwt(BaseFont font, int size) {
/* 184 */     String[][] names = font.getFullFontName();
/* 185 */     if (names.length == 1)
/* 186 */       return new Font(names[0][3], 0, size); 
/* 187 */     String name10 = null;
/* 188 */     String name3x = null;
/* 189 */     for (int k = 0; k < names.length; k++) {
/* 190 */       String[] name = names[k];
/* 191 */       if (name[0].equals("1") && name[1].equals("0")) {
/* 192 */         name10 = name[3];
/* 193 */       } else if (name[2].equals("1033")) {
/* 194 */         name3x = name[3];
/*     */         break;
/*     */       } 
/*     */     } 
/* 198 */     String finalName = name3x;
/* 199 */     if (finalName == null)
/* 200 */       finalName = name10; 
/* 201 */     if (finalName == null)
/* 202 */       finalName = names[0][3]; 
/* 203 */     return new Font(finalName, 0, size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putName(String awtName, BaseFontParameters parameters) {
/* 211 */     this.mapper.put(awtName, parameters);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAlias(String alias, String awtName) {
/* 219 */     this.aliases.put(alias, awtName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseFontParameters getBaseFontParameters(String name) {
/* 227 */     String alias = this.aliases.get(name);
/* 228 */     if (alias == null)
/* 229 */       return this.mapper.get(name); 
/* 230 */     BaseFontParameters p = this.mapper.get(alias);
/* 231 */     if (p == null) {
/* 232 */       return this.mapper.get(name);
/*     */     }
/* 234 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertNames(Object[] allNames, String path) {
/* 243 */     String[][] names = (String[][])allNames[2];
/* 244 */     String main = null;
/* 245 */     for (int k = 0; k < names.length; k++) {
/* 246 */       String[] name = names[k];
/* 247 */       if (name[2].equals("1033")) {
/* 248 */         main = name[3];
/*     */         break;
/*     */       } 
/*     */     } 
/* 252 */     if (main == null)
/* 253 */       main = names[0][3]; 
/* 254 */     BaseFontParameters p = new BaseFontParameters(path);
/* 255 */     this.mapper.put(main, p);
/* 256 */     for (int i = 0; i < names.length; i++) {
/* 257 */       this.aliases.put(names[i][3], main);
/*     */     }
/* 259 */     this.aliases.put((String)allNames[0], main);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int insertFile(File file) {
/* 270 */     String name = file.getPath().toLowerCase();
/*     */     try {
/* 272 */       if (name.endsWith(".ttf") || name.endsWith(".otf") || name.endsWith(".afm")) {
/* 273 */         Object[] allNames = BaseFont.getAllFontNames(file.getPath(), "Cp1252", null);
/* 274 */         insertNames(allNames, file.getPath());
/* 275 */         return 1;
/* 276 */       }  if (name.endsWith(".ttc")) {
/* 277 */         String[] ttcs = BaseFont.enumerateTTCNames(file.getPath());
/* 278 */         for (int j = 0; j < ttcs.length; j++) {
/* 279 */           String nt = file.getPath() + "," + j;
/* 280 */           Object[] allNames = BaseFont.getAllFontNames(nt, "Cp1252", null);
/* 281 */           insertNames(allNames, nt);
/*     */         } 
/* 283 */         return 1;
/*     */       } 
/* 285 */     } catch (Exception exception) {}
/*     */     
/* 287 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int insertDirectory(String dir) {
/* 298 */     File file = new File(dir);
/* 299 */     if (!file.exists() || !file.isDirectory())
/* 300 */       return 0; 
/* 301 */     File[] files = file.listFiles();
/* 302 */     if (files == null)
/* 303 */       return 0; 
/* 304 */     int count = 0;
/* 305 */     for (int k = 0; k < files.length; k++) {
/* 306 */       count += insertFile(files[k]);
/*     */     }
/* 308 */     return count;
/*     */   }
/*     */   
/*     */   public HashMap<String, BaseFontParameters> getMapper() {
/* 312 */     return this.mapper;
/*     */   }
/*     */   
/*     */   public HashMap<String, String> getAliases() {
/* 316 */     return this.aliases;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/awt/DefaultFontMapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */