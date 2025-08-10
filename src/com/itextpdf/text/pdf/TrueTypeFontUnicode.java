/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.Utilities;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.fonts.otf.GlyphSubstitutionTableReader;
/*     */ import com.itextpdf.text.pdf.fonts.otf.Language;
/*     */ import com.itextpdf.text.pdf.languages.ArabicLigaturizer;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TrueTypeFontUnicode
/*     */   extends TrueTypeFont
/*     */   implements Comparator<int[]>
/*     */ {
/*  68 */   private static final List<Language> SUPPORTED_LANGUAGES_FOR_OTF = Arrays.asList(new Language[] { Language.BENGALI });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Glyph> glyphSubstitutionMap;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Language supportedLanguage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TrueTypeFontUnicode(String ttFile, String enc, boolean emb, byte[] ttfAfm, boolean forceRead) throws DocumentException, IOException {
/*  85 */     String nameBase = getBaseName(ttFile);
/*  86 */     String ttcName = getTTCName(nameBase);
/*  87 */     if (nameBase.length() < ttFile.length()) {
/*  88 */       this.style = ttFile.substring(nameBase.length());
/*     */     }
/*  90 */     this.encoding = enc;
/*  91 */     this.embedded = emb;
/*  92 */     this.fileName = ttcName;
/*  93 */     this.ttcIndex = "";
/*  94 */     if (ttcName.length() < nameBase.length())
/*  95 */       this.ttcIndex = nameBase.substring(ttcName.length() + 1); 
/*  96 */     this.fontType = 3;
/*  97 */     if ((this.fileName.toLowerCase().endsWith(".ttf") || this.fileName.toLowerCase().endsWith(".otf") || this.fileName.toLowerCase().endsWith(".ttc")) && (enc.equals("Identity-H") || enc.equals("Identity-V")) && emb) {
/*  98 */       process(ttfAfm, forceRead);
/*  99 */       if (this.os_2.fsType == 2) {
/* 100 */         throw new DocumentException(MessageLocalization.getComposedMessage("1.cannot.be.embedded.due.to.licensing.restrictions", new Object[] { this.fileName + this.style }));
/*     */       }
/* 102 */       if ((this.cmap31 == null && !this.fontSpecific) || (this.cmap10 == null && this.fontSpecific)) {
/* 103 */         this.directTextToByte = true;
/*     */       }
/* 105 */       if (this.fontSpecific) {
/* 106 */         this.fontSpecific = false;
/* 107 */         String tempEncoding = this.encoding;
/* 108 */         this.encoding = "";
/* 109 */         createEncoding();
/* 110 */         this.encoding = tempEncoding;
/* 111 */         this.fontSpecific = true;
/*     */       } 
/*     */     } else {
/*     */       
/* 115 */       throw new DocumentException(MessageLocalization.getComposedMessage("1.2.is.not.a.ttf.font.file", new Object[] { this.fileName, this.style }));
/* 116 */     }  this.vertical = enc.endsWith("V");
/*     */   }
/*     */ 
/*     */   
/*     */   void process(byte[] ttfAfm, boolean preload) throws DocumentException, IOException {
/* 121 */     super.process(ttfAfm, preload);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWidth(int char1) {
/* 132 */     if (this.vertical)
/* 133 */       return 1000; 
/* 134 */     if (this.fontSpecific) {
/* 135 */       if ((char1 & 0xFF00) == 0 || (char1 & 0xFF00) == 61440) {
/* 136 */         return getRawWidth(char1 & 0xFF, (String)null);
/*     */       }
/* 138 */       return 0;
/*     */     } 
/*     */     
/* 141 */     return getRawWidth(char1, this.encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWidth(String text) {
/* 152 */     if (this.vertical)
/* 153 */       return text.length() * 1000; 
/* 154 */     int total = 0;
/* 155 */     if (this.fontSpecific) {
/* 156 */       char[] cc = text.toCharArray();
/* 157 */       int len = cc.length;
/* 158 */       for (int k = 0; k < len; k++) {
/* 159 */         char c = cc[k];
/* 160 */         if ((c & 0xFF00) == 0 || (c & 0xFF00) == 61440) {
/* 161 */           total += getRawWidth(c & 0xFF, (String)null);
/*     */         }
/*     */       } 
/*     */     } else {
/* 165 */       int len = text.length();
/* 166 */       for (int k = 0; k < len; k++) {
/* 167 */         if (Utilities.isSurrogatePair(text, k)) {
/* 168 */           total += getRawWidth(Utilities.convertToUtf32(text, k), this.encoding);
/* 169 */           k++;
/*     */         } else {
/*     */           
/* 172 */           total += getRawWidth(text.charAt(k), this.encoding);
/*     */         } 
/*     */       } 
/* 175 */     }  return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfStream getToUnicode(Object[] metrics) {
/* 184 */     if (metrics.length == 0)
/* 185 */       return null; 
/* 186 */     StringBuffer buf = new StringBuffer("/CIDInit /ProcSet findresource begin\n12 dict begin\nbegincmap\n/CIDSystemInfo\n<< /Registry (TTX+0)\n/Ordering (T42UV)\n/Supplement 0\n>> def\n/CMapName /TTX+0 def\n/CMapType 2 def\n1 begincodespacerange\n<0000><FFFF>\nendcodespacerange\n");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     int size = 0;
/* 201 */     for (int k = 0; k < metrics.length; k++) {
/* 202 */       if (size == 0) {
/* 203 */         if (k != 0) {
/* 204 */           buf.append("endbfrange\n");
/*     */         }
/* 206 */         size = Math.min(100, metrics.length - k);
/* 207 */         buf.append(size).append(" beginbfrange\n");
/*     */       } 
/* 209 */       size--;
/* 210 */       int[] metric = (int[])metrics[k];
/* 211 */       String fromTo = toHex(metric[0]);
/* 212 */       buf.append(fromTo).append(fromTo).append(toHex(metric[2])).append('\n');
/*     */     } 
/* 214 */     buf.append("endbfrange\nendcmap\nCMapName currentdict /CMap defineresource pop\nend end\n");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     String s = buf.toString();
/* 220 */     PdfStream stream = new PdfStream(PdfEncodings.convertToBytes(s, (String)null));
/* 221 */     stream.flateCompress(this.compressionLevel);
/* 222 */     return stream;
/*     */   }
/*     */   
/*     */   private static String toHex4(int n) {
/* 226 */     String s = "0000" + Integer.toHexString(n);
/* 227 */     return s.substring(s.length() - 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String toHex(int n) {
/* 235 */     if (n < 65536)
/* 236 */       return "<" + toHex4(n) + ">"; 
/* 237 */     n -= 65536;
/* 238 */     int high = n / 1024 + 55296;
/* 239 */     int low = n % 1024 + 56320;
/* 240 */     return "[<" + toHex4(high) + toHex4(low) + ">]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDictionary getCIDFontType2(PdfIndirectReference fontDescriptor, String subsetPrefix, Object[] metrics) {
/* 250 */     PdfDictionary dic = new PdfDictionary(PdfName.FONT);
/*     */     
/* 252 */     if (this.cff) {
/* 253 */       dic.put(PdfName.SUBTYPE, PdfName.CIDFONTTYPE0);
/* 254 */       dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + this.fontName + "-" + this.encoding));
/*     */     } else {
/*     */       
/* 257 */       dic.put(PdfName.SUBTYPE, PdfName.CIDFONTTYPE2);
/* 258 */       dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + this.fontName));
/*     */     } 
/* 260 */     dic.put(PdfName.FONTDESCRIPTOR, fontDescriptor);
/* 261 */     if (!this.cff)
/* 262 */       dic.put(PdfName.CIDTOGIDMAP, PdfName.IDENTITY); 
/* 263 */     PdfDictionary cdic = new PdfDictionary();
/* 264 */     cdic.put(PdfName.REGISTRY, new PdfString("Adobe"));
/* 265 */     cdic.put(PdfName.ORDERING, new PdfString("Identity"));
/* 266 */     cdic.put(PdfName.SUPPLEMENT, new PdfNumber(0));
/* 267 */     dic.put(PdfName.CIDSYSTEMINFO, cdic);
/* 268 */     if (!this.vertical) {
/* 269 */       dic.put(PdfName.DW, new PdfNumber(1000));
/* 270 */       StringBuffer buf = new StringBuffer("[");
/* 271 */       int lastNumber = -10;
/* 272 */       boolean firstTime = true;
/* 273 */       for (int k = 0; k < metrics.length; k++) {
/* 274 */         int[] metric = (int[])metrics[k];
/* 275 */         if (metric[1] != 1000) {
/*     */           
/* 277 */           int m = metric[0];
/* 278 */           if (m == lastNumber + 1) {
/* 279 */             buf.append(' ').append(metric[1]);
/*     */           } else {
/*     */             
/* 282 */             if (!firstTime) {
/* 283 */               buf.append(']');
/*     */             }
/* 285 */             firstTime = false;
/* 286 */             buf.append(m).append('[').append(metric[1]);
/*     */           } 
/* 288 */           lastNumber = m;
/*     */         } 
/* 290 */       }  if (buf.length() > 1) {
/* 291 */         buf.append("]]");
/* 292 */         dic.put(PdfName.W, new PdfLiteral(buf.toString()));
/*     */       } 
/*     */     } 
/* 295 */     return dic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDictionary getFontBaseType(PdfIndirectReference descendant, String subsetPrefix, PdfIndirectReference toUnicode) {
/* 305 */     PdfDictionary dic = new PdfDictionary(PdfName.FONT);
/*     */     
/* 307 */     dic.put(PdfName.SUBTYPE, PdfName.TYPE0);
/*     */     
/* 309 */     if (this.cff) {
/* 310 */       dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + this.fontName + "-" + this.encoding));
/*     */     } else {
/*     */       
/* 313 */       dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + this.fontName));
/*     */     } 
/* 315 */     dic.put(PdfName.ENCODING, new PdfName(this.encoding));
/* 316 */     dic.put(PdfName.DESCENDANTFONTS, new PdfArray(descendant));
/* 317 */     if (toUnicode != null)
/* 318 */       dic.put(PdfName.TOUNICODE, toUnicode); 
/* 319 */     return dic;
/*     */   }
/*     */   
/*     */   public int GetCharFromGlyphId(int gid) {
/* 323 */     if (this.glyphIdToChar == null) {
/* 324 */       int[] g2 = new int[this.maxGlyphId];
/* 325 */       HashMap<Integer, int[]> map = null;
/* 326 */       if (this.cmapExt != null) {
/* 327 */         map = this.cmapExt;
/*     */       }
/* 329 */       else if (this.cmap31 != null) {
/* 330 */         map = this.cmap31;
/*     */       } 
/* 332 */       if (map != null) {
/* 333 */         for (Map.Entry<Integer, int[]> entry : map.entrySet()) {
/* 334 */           g2[((int[])entry.getValue())[0]] = ((Integer)entry.getKey()).intValue();
/*     */         }
/*     */       }
/* 337 */       this.glyphIdToChar = g2;
/*     */     } 
/* 339 */     return this.glyphIdToChar[gid];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(int[] o1, int[] o2) {
/* 348 */     int m1 = o1[0];
/* 349 */     int m2 = o2[0];
/* 350 */     if (m1 < m2)
/* 351 */       return -1; 
/* 352 */     if (m1 == m2)
/* 353 */       return 0; 
/* 354 */     return 1;
/*     */   }
/*     */   
/* 357 */   private static final byte[] rotbits = new byte[] { Byte.MIN_VALUE, 64, 32, 16, 8, 4, 2, 1 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params) throws DocumentException, IOException {
/* 368 */     writer.getTtfUnicodeWriter().writeFont(this, ref, params, rotbits);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfStream getFullFontStream() throws IOException, DocumentException {
/* 378 */     if (this.cff) {
/* 379 */       return new BaseFont.StreamFont(readCffFont(), "CIDFontType0C", this.compressionLevel);
/*     */     }
/* 381 */     return super.getFullFontStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] convertToBytes(String text) {
/* 390 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   byte[] convertToBytes(int char1) {
/* 395 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getMetricsTT(int c) {
/* 404 */     if (this.cmapExt != null)
/* 405 */       return this.cmapExt.get(Integer.valueOf(c)); 
/* 406 */     HashMap<Integer, int[]> map = null;
/* 407 */     if (this.fontSpecific) {
/* 408 */       map = this.cmap10;
/*     */     } else {
/* 410 */       map = this.cmap31;
/* 411 */     }  if (map == null)
/* 412 */       return null; 
/* 413 */     if (this.fontSpecific) {
/* 414 */       if ((c & 0xFFFFFF00) == 0 || (c & 0xFFFFFF00) == 61440) {
/* 415 */         return map.get(Integer.valueOf(c & 0xFF));
/*     */       }
/* 417 */       return null;
/*     */     } 
/*     */     
/* 420 */     int[] result = map.get(Integer.valueOf(c));
/* 421 */     if (result == null) {
/* 422 */       Character ch = ArabicLigaturizer.getReverseMapping((char)c);
/* 423 */       if (ch != null)
/* 424 */         result = map.get(Integer.valueOf(ch.charValue())); 
/*     */     } 
/* 426 */     return result;
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
/*     */   public boolean charExists(int c) {
/* 438 */     return (getMetricsTT(c) != null);
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
/*     */   public boolean setCharAdvance(int c, int advance) {
/* 450 */     int[] m = getMetricsTT(c);
/* 451 */     if (m == null)
/* 452 */       return false; 
/* 453 */     m[1] = advance;
/* 454 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getCharBBox(int c) {
/* 459 */     if (this.bboxes == null)
/* 460 */       return null; 
/* 461 */     int[] m = getMetricsTT(c);
/* 462 */     if (m == null)
/* 463 */       return null; 
/* 464 */     return this.bboxes[m[0]];
/*     */   }
/*     */   
/*     */   protected Map<String, Glyph> getGlyphSubstitutionMap() {
/* 468 */     return this.glyphSubstitutionMap;
/*     */   }
/*     */   
/*     */   Language getSupportedLanguage() {
/* 472 */     return this.supportedLanguage;
/*     */   }
/*     */   
/*     */   private void readGsubTable() throws IOException {
/* 476 */     if (this.tables.get("GSUB") != null) {
/*     */       
/* 478 */       Map<Integer, Character> glyphToCharacterMap = new HashMap<Integer, Character>(this.cmap31.size());
/*     */       
/* 480 */       for (Integer charCode : this.cmap31.keySet()) {
/* 481 */         char c = (char)charCode.intValue();
/* 482 */         int glyphCode = ((int[])this.cmap31.get(charCode))[0];
/* 483 */         glyphToCharacterMap.put(Integer.valueOf(glyphCode), Character.valueOf(c));
/*     */       } 
/*     */ 
/*     */       
/* 487 */       GlyphSubstitutionTableReader gsubReader = new GlyphSubstitutionTableReader(this.rf, ((int[])this.tables.get("GSUB"))[0], glyphToCharacterMap, this.glyphWidthsByIndex);
/*     */       
/*     */       try {
/* 490 */         gsubReader.read();
/* 491 */         this.supportedLanguage = gsubReader.getSupportedLanguage();
/*     */         
/* 493 */         if (SUPPORTED_LANGUAGES_FOR_OTF.contains(this.supportedLanguage)) {
/* 494 */           this.glyphSubstitutionMap = gsubReader.getGlyphSubstitutionMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 515 */       catch (Exception e) {
/* 516 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/TrueTypeFontUnicode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */