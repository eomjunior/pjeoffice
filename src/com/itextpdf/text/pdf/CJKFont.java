/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Utilities;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.io.StreamUtil;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CMapCache;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CMapCidByte;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CMapCidUni;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CMapUniCid;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CJKFont
/*     */   extends BaseFont
/*     */ {
/*     */   static final String CJK_ENCODING = "UnicodeBigUnmarked";
/*     */   private static final int FIRST = 0;
/*     */   private static final int BRACKET = 1;
/*     */   private static final int SERIAL = 2;
/*     */   private static final int V1Y = 880;
/*  77 */   static Properties cjkFonts = new Properties();
/*  78 */   static Properties cjkEncodings = new Properties();
/*  79 */   private static final HashMap<String, HashMap<String, Object>> allFonts = new HashMap<String, HashMap<String, Object>>();
/*     */   
/*     */   private static boolean propertiesLoaded = false;
/*     */   
/*     */   public static final String RESOURCE_PATH_CMAP = "com/itextpdf/text/pdf/fonts/cmaps/";
/*  84 */   private static final HashMap<String, Set<String>> registryNames = new HashMap<String, Set<String>>();
/*     */   
/*     */   private CMapCidByte cidByte;
/*     */   
/*     */   private CMapUniCid uniCid;
/*     */   
/*     */   private CMapCidUni cidUni;
/*     */   private String uniMap;
/*     */   private String fontName;
/*  93 */   private String style = "";
/*     */   
/*     */   private String CMap;
/*     */   
/*     */   private boolean cidDirect = false;
/*     */   
/*     */   private IntHashtable vMetrics;
/*     */   
/*     */   private IntHashtable hMetrics;
/*     */   private HashMap<String, Object> fontDesc;
/*     */   
/*     */   private static void loadProperties() {
/* 105 */     if (propertiesLoaded)
/*     */       return; 
/* 107 */     synchronized (allFonts) {
/* 108 */       if (propertiesLoaded)
/*     */         return; 
/*     */       try {
/* 111 */         loadRegistry();
/* 112 */         for (String font : registryNames.get("fonts")) {
/* 113 */           allFonts.put(font, readFontProperties(font));
/*     */         }
/*     */       }
/* 116 */       catch (Exception exception) {}
/*     */       
/* 118 */       propertiesLoaded = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void loadRegistry() throws IOException {
/* 123 */     InputStream is = StreamUtil.getResourceStream("com/itextpdf/text/pdf/fonts/cmaps/cjk_registry.properties");
/* 124 */     Properties p = new Properties();
/* 125 */     p.load(is);
/* 126 */     is.close();
/* 127 */     for (Object key : p.keySet()) {
/* 128 */       String value = p.getProperty((String)key);
/* 129 */       String[] sp = value.split(" ");
/* 130 */       Set<String> hs = new HashSet<String>();
/* 131 */       for (String s : sp) {
/* 132 */         if (s.length() > 0)
/* 133 */           hs.add(s); 
/*     */       } 
/* 135 */       registryNames.put((String)key, hs);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CJKFont(String fontName, String enc, boolean emb) throws DocumentException {
/* 146 */     loadProperties();
/* 147 */     this.fontType = 2;
/* 148 */     String nameBase = getBaseName(fontName);
/* 149 */     if (!isCJKFont(nameBase, enc))
/* 150 */       throw new DocumentException(MessageLocalization.getComposedMessage("font.1.with.2.encoding.is.not.a.cjk.font", new Object[] { fontName, enc })); 
/* 151 */     if (nameBase.length() < fontName.length()) {
/* 152 */       this.style = fontName.substring(nameBase.length());
/* 153 */       fontName = nameBase;
/*     */     } 
/* 155 */     this.fontName = fontName;
/* 156 */     this.encoding = "UnicodeBigUnmarked";
/* 157 */     this.vertical = enc.endsWith("V");
/* 158 */     this.CMap = enc;
/* 159 */     if (enc.equals("Identity-H") || enc.equals("Identity-V"))
/* 160 */       this.cidDirect = true; 
/* 161 */     loadCMaps();
/*     */   }
/*     */   
/*     */   String getUniMap() {
/* 165 */     return this.uniMap;
/*     */   }
/*     */   
/*     */   private void loadCMaps() throws DocumentException {
/*     */     try {
/* 170 */       this.fontDesc = allFonts.get(this.fontName);
/* 171 */       this.hMetrics = (IntHashtable)this.fontDesc.get("W");
/* 172 */       this.vMetrics = (IntHashtable)this.fontDesc.get("W2");
/* 173 */       String registry = (String)this.fontDesc.get("Registry");
/* 174 */       this.uniMap = "";
/* 175 */       for (String name : registryNames.get(registry + "_Uni")) {
/* 176 */         this.uniMap = name;
/* 177 */         if (name.endsWith("V") && this.vertical)
/*     */           break; 
/* 179 */         if (!name.endsWith("V") && !this.vertical)
/*     */           break; 
/*     */       } 
/* 182 */       if (this.cidDirect) {
/* 183 */         this.cidUni = CMapCache.getCachedCMapCidUni(this.uniMap);
/*     */       } else {
/*     */         
/* 186 */         this.uniCid = CMapCache.getCachedCMapUniCid(this.uniMap);
/* 187 */         this.cidByte = CMapCache.getCachedCMapCidByte(this.CMap);
/*     */       }
/*     */     
/* 190 */     } catch (Exception ex) {
/* 191 */       throw new DocumentException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String GetCompatibleFont(String enc) {
/* 201 */     loadProperties();
/* 202 */     String registry = null;
/* 203 */     for (Map.Entry<String, Set<String>> e : registryNames.entrySet()) {
/* 204 */       if (((Set)e.getValue()).contains(enc)) {
/* 205 */         registry = e.getKey();
/* 206 */         for (Map.Entry<String, HashMap<String, Object>> e1 : allFonts.entrySet()) {
/* 207 */           if (registry.equals(((HashMap)e1.getValue()).get("Registry")))
/* 208 */             return e1.getKey(); 
/*     */         } 
/*     */       } 
/*     */     } 
/* 212 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCJKFont(String fontName, String enc) {
/* 221 */     loadProperties();
/* 222 */     if (!registryNames.containsKey("fonts"))
/* 223 */       return false; 
/* 224 */     if (!((Set)registryNames.get("fonts")).contains(fontName))
/* 225 */       return false; 
/* 226 */     if (enc.equals("Identity-H") || enc.equals("Identity-V"))
/* 227 */       return true; 
/* 228 */     String registry = (String)((HashMap)allFonts.get(fontName)).get("Registry");
/* 229 */     Set<String> encodings = registryNames.get(registry);
/* 230 */     return (encodings != null && encodings.contains(enc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWidth(int char1) {
/* 240 */     int v, c = char1;
/* 241 */     if (!this.cidDirect) {
/* 242 */       c = this.uniCid.lookup(char1);
/*     */     }
/* 244 */     if (this.vertical) {
/* 245 */       v = this.vMetrics.get(c);
/*     */     } else {
/* 247 */       v = this.hMetrics.get(c);
/* 248 */     }  if (v > 0) {
/* 249 */       return v;
/*     */     }
/* 251 */     return 1000;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth(String text) {
/* 256 */     int total = 0;
/* 257 */     if (this.cidDirect) {
/* 258 */       for (int k = 0; k < text.length(); k++) {
/* 259 */         total += getWidth(text.charAt(k));
/*     */       }
/*     */     } else {
/*     */       
/* 263 */       for (int k = 0; k < text.length(); k++) {
/*     */         int val;
/* 265 */         if (Utilities.isSurrogatePair(text, k)) {
/* 266 */           val = Utilities.convertToUtf32(text, k);
/* 267 */           k++;
/*     */         } else {
/*     */           
/* 270 */           val = text.charAt(k);
/*     */         } 
/* 272 */         total += getWidth(val);
/*     */       } 
/*     */     } 
/* 275 */     return total;
/*     */   }
/*     */ 
/*     */   
/*     */   int getRawWidth(int c, String name) {
/* 280 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getKerning(int char1, int char2) {
/* 285 */     return 0;
/*     */   }
/*     */   
/*     */   private PdfDictionary getFontDescriptor() {
/* 289 */     PdfDictionary dic = new PdfDictionary(PdfName.FONTDESCRIPTOR);
/* 290 */     dic.put(PdfName.ASCENT, new PdfLiteral((String)this.fontDesc.get("Ascent")));
/* 291 */     dic.put(PdfName.CAPHEIGHT, new PdfLiteral((String)this.fontDesc.get("CapHeight")));
/* 292 */     dic.put(PdfName.DESCENT, new PdfLiteral((String)this.fontDesc.get("Descent")));
/* 293 */     dic.put(PdfName.FLAGS, new PdfLiteral((String)this.fontDesc.get("Flags")));
/* 294 */     dic.put(PdfName.FONTBBOX, new PdfLiteral((String)this.fontDesc.get("FontBBox")));
/* 295 */     dic.put(PdfName.FONTNAME, new PdfName(this.fontName + this.style));
/* 296 */     dic.put(PdfName.ITALICANGLE, new PdfLiteral((String)this.fontDesc.get("ItalicAngle")));
/* 297 */     dic.put(PdfName.STEMV, new PdfLiteral((String)this.fontDesc.get("StemV")));
/* 298 */     PdfDictionary pdic = new PdfDictionary();
/* 299 */     pdic.put(PdfName.PANOSE, new PdfString((String)this.fontDesc.get("Panose"), null));
/* 300 */     dic.put(PdfName.STYLE, pdic);
/* 301 */     return dic;
/*     */   }
/*     */   
/*     */   private PdfDictionary getCIDFont(PdfIndirectReference fontDescriptor, IntHashtable cjkTag) {
/* 305 */     PdfDictionary dic = new PdfDictionary(PdfName.FONT);
/* 306 */     dic.put(PdfName.SUBTYPE, PdfName.CIDFONTTYPE0);
/* 307 */     dic.put(PdfName.BASEFONT, new PdfName(this.fontName + this.style));
/* 308 */     dic.put(PdfName.FONTDESCRIPTOR, fontDescriptor);
/* 309 */     int[] keys = cjkTag.toOrderedKeys();
/* 310 */     String w = convertToHCIDMetrics(keys, this.hMetrics);
/* 311 */     if (w != null)
/* 312 */       dic.put(PdfName.W, new PdfLiteral(w)); 
/* 313 */     if (this.vertical) {
/* 314 */       w = convertToVCIDMetrics(keys, this.vMetrics, this.hMetrics);
/* 315 */       if (w != null) {
/* 316 */         dic.put(PdfName.W2, new PdfLiteral(w));
/*     */       }
/*     */     } else {
/* 319 */       dic.put(PdfName.DW, new PdfNumber(1000));
/* 320 */     }  PdfDictionary cdic = new PdfDictionary();
/* 321 */     if (this.cidDirect) {
/* 322 */       cdic.put(PdfName.REGISTRY, new PdfString(this.cidUni.getRegistry(), null));
/* 323 */       cdic.put(PdfName.ORDERING, new PdfString(this.cidUni.getOrdering(), null));
/* 324 */       cdic.put(PdfName.SUPPLEMENT, new PdfNumber(this.cidUni.getSupplement()));
/*     */     } else {
/*     */       
/* 327 */       cdic.put(PdfName.REGISTRY, new PdfString(this.cidByte.getRegistry(), null));
/* 328 */       cdic.put(PdfName.ORDERING, new PdfString(this.cidByte.getOrdering(), null));
/* 329 */       cdic.put(PdfName.SUPPLEMENT, new PdfNumber(this.cidByte.getSupplement()));
/*     */     } 
/* 331 */     dic.put(PdfName.CIDSYSTEMINFO, cdic);
/* 332 */     return dic;
/*     */   }
/*     */   
/*     */   private PdfDictionary getFontBaseType(PdfIndirectReference CIDFont) {
/* 336 */     PdfDictionary dic = new PdfDictionary(PdfName.FONT);
/* 337 */     dic.put(PdfName.SUBTYPE, PdfName.TYPE0);
/* 338 */     String name = this.fontName;
/* 339 */     if (this.style.length() > 0)
/* 340 */       name = name + "-" + this.style.substring(1); 
/* 341 */     name = name + "-" + this.CMap;
/* 342 */     dic.put(PdfName.BASEFONT, new PdfName(name));
/* 343 */     dic.put(PdfName.ENCODING, new PdfName(this.CMap));
/* 344 */     dic.put(PdfName.DESCENDANTFONTS, new PdfArray(CIDFont));
/* 345 */     return dic;
/*     */   }
/*     */ 
/*     */   
/*     */   void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params) throws DocumentException, IOException {
/* 350 */     IntHashtable cjkTag = (IntHashtable)params[0];
/* 351 */     PdfIndirectReference ind_font = null;
/* 352 */     PdfObject pobj = null;
/* 353 */     PdfIndirectObject obj = null;
/* 354 */     pobj = getFontDescriptor();
/* 355 */     if (pobj != null) {
/* 356 */       obj = writer.addToBody(pobj);
/* 357 */       ind_font = obj.getIndirectReference();
/*     */     } 
/* 359 */     pobj = getCIDFont(ind_font, cjkTag);
/* 360 */     if (pobj != null) {
/* 361 */       obj = writer.addToBody(pobj);
/* 362 */       ind_font = obj.getIndirectReference();
/*     */     } 
/* 364 */     pobj = getFontBaseType(ind_font);
/* 365 */     writer.addToBody(pobj, ref);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfStream getFullFontStream() {
/* 376 */     return null;
/*     */   }
/*     */   
/*     */   private float getDescNumber(String name) {
/* 380 */     return Integer.parseInt((String)this.fontDesc.get(name));
/*     */   }
/*     */   
/*     */   private float getBBox(int idx) {
/* 384 */     String s = (String)this.fontDesc.get("FontBBox");
/* 385 */     StringTokenizer tk = new StringTokenizer(s, " []\r\n\t\f");
/* 386 */     String ret = tk.nextToken();
/* 387 */     for (int k = 0; k < idx; k++)
/* 388 */       ret = tk.nextToken(); 
/* 389 */     return Integer.parseInt(ret);
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
/*     */   public float getFontDescriptor(int key, float fontSize) {
/* 401 */     switch (key) {
/*     */       case 1:
/*     */       case 9:
/* 404 */         return getDescNumber("Ascent") * fontSize / 1000.0F;
/*     */       case 2:
/* 406 */         return getDescNumber("CapHeight") * fontSize / 1000.0F;
/*     */       case 3:
/*     */       case 10:
/* 409 */         return getDescNumber("Descent") * fontSize / 1000.0F;
/*     */       case 4:
/* 411 */         return getDescNumber("ItalicAngle");
/*     */       case 5:
/* 413 */         return fontSize * getBBox(0) / 1000.0F;
/*     */       case 6:
/* 415 */         return fontSize * getBBox(1) / 1000.0F;
/*     */       case 7:
/* 417 */         return fontSize * getBBox(2) / 1000.0F;
/*     */       case 8:
/* 419 */         return fontSize * getBBox(3) / 1000.0F;
/*     */       case 11:
/* 421 */         return 0.0F;
/*     */       case 12:
/* 423 */         return fontSize * (getBBox(2) - getBBox(0)) / 1000.0F;
/*     */     } 
/* 425 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPostscriptFontName() {
/* 430 */     return this.fontName;
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
/*     */   public String[][] getFullFontName() {
/* 443 */     return new String[][] { { "", "", "", this.fontName } };
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
/*     */   public String[][] getAllNameEntries() {
/* 456 */     return new String[][] { { "4", "", "", "", this.fontName } };
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
/*     */   public String[][] getFamilyFontName() {
/* 469 */     return getFullFontName();
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
/*     */   static IntHashtable createMetric(String s) {
/* 489 */     IntHashtable h = new IntHashtable();
/* 490 */     StringTokenizer tk = new StringTokenizer(s);
/* 491 */     while (tk.hasMoreTokens()) {
/* 492 */       int n1 = Integer.parseInt(tk.nextToken());
/* 493 */       h.put(n1, Integer.parseInt(tk.nextToken()));
/*     */     } 
/* 495 */     return h;
/*     */   }
/*     */   
/*     */   static String convertToHCIDMetrics(int[] keys, IntHashtable h) {
/* 499 */     if (keys.length == 0)
/* 500 */       return null; 
/* 501 */     int lastCid = 0;
/* 502 */     int lastValue = 0;
/*     */     int start;
/* 504 */     for (start = 0; start < keys.length; start++) {
/* 505 */       lastCid = keys[start];
/* 506 */       lastValue = h.get(lastCid);
/* 507 */       if (lastValue != 0) {
/* 508 */         start++;
/*     */         break;
/*     */       } 
/*     */     } 
/* 512 */     if (lastValue == 0)
/* 513 */       return null; 
/* 514 */     StringBuilder buf = new StringBuilder();
/* 515 */     buf.append('[');
/* 516 */     buf.append(lastCid);
/* 517 */     int state = 0;
/* 518 */     for (int k = start; k < keys.length; k++) {
/* 519 */       int cid = keys[k];
/* 520 */       int value = h.get(cid);
/* 521 */       if (value != 0) {
/*     */         
/* 523 */         switch (state) {
/*     */           case 0:
/* 525 */             if (cid == lastCid + 1 && value == lastValue) {
/* 526 */               state = 2; break;
/*     */             } 
/* 528 */             if (cid == lastCid + 1) {
/* 529 */               state = 1;
/* 530 */               buf.append('[').append(lastValue);
/*     */               break;
/*     */             } 
/* 533 */             buf.append('[').append(lastValue).append(']').append(cid);
/*     */             break;
/*     */ 
/*     */           
/*     */           case 1:
/* 538 */             if (cid == lastCid + 1 && value == lastValue) {
/* 539 */               state = 2;
/* 540 */               buf.append(']').append(lastCid); break;
/*     */             } 
/* 542 */             if (cid == lastCid + 1) {
/* 543 */               buf.append(' ').append(lastValue);
/*     */               break;
/*     */             } 
/* 546 */             state = 0;
/* 547 */             buf.append(' ').append(lastValue).append(']').append(cid);
/*     */             break;
/*     */ 
/*     */           
/*     */           case 2:
/* 552 */             if (cid != lastCid + 1 || value != lastValue) {
/* 553 */               buf.append(' ').append(lastCid).append(' ').append(lastValue).append(' ').append(cid);
/* 554 */               state = 0;
/*     */             } 
/*     */             break;
/*     */         } 
/*     */         
/* 559 */         lastValue = value;
/* 560 */         lastCid = cid;
/*     */       } 
/* 562 */     }  switch (state) {
/*     */       case 0:
/* 564 */         buf.append('[').append(lastValue).append("]]");
/*     */         break;
/*     */       
/*     */       case 1:
/* 568 */         buf.append(' ').append(lastValue).append("]]");
/*     */         break;
/*     */       
/*     */       case 2:
/* 572 */         buf.append(' ').append(lastCid).append(' ').append(lastValue).append(']');
/*     */         break;
/*     */     } 
/*     */     
/* 576 */     return buf.toString();
/*     */   }
/*     */   
/*     */   static String convertToVCIDMetrics(int[] keys, IntHashtable v, IntHashtable h) {
/* 580 */     if (keys.length == 0)
/* 581 */       return null; 
/* 582 */     int lastCid = 0;
/* 583 */     int lastValue = 0;
/* 584 */     int lastHValue = 0;
/*     */     int start;
/* 586 */     for (start = 0; start < keys.length; start++) {
/* 587 */       lastCid = keys[start];
/* 588 */       lastValue = v.get(lastCid);
/* 589 */       if (lastValue != 0) {
/* 590 */         start++;
/*     */         
/*     */         break;
/*     */       } 
/* 594 */       lastHValue = h.get(lastCid);
/*     */     } 
/* 596 */     if (lastValue == 0)
/* 597 */       return null; 
/* 598 */     if (lastHValue == 0)
/* 599 */       lastHValue = 1000; 
/* 600 */     StringBuilder buf = new StringBuilder();
/* 601 */     buf.append('[');
/* 602 */     buf.append(lastCid);
/* 603 */     int state = 0;
/* 604 */     for (int k = start; k < keys.length; k++) {
/* 605 */       int cid = keys[k];
/* 606 */       int value = v.get(cid);
/* 607 */       if (value != 0) {
/*     */         
/* 609 */         int hValue = h.get(lastCid);
/* 610 */         if (hValue == 0)
/* 611 */           hValue = 1000; 
/* 612 */         switch (state) {
/*     */           case 0:
/* 614 */             if (cid == lastCid + 1 && value == lastValue && hValue == lastHValue) {
/* 615 */               state = 2;
/*     */               break;
/*     */             } 
/* 618 */             buf.append(' ').append(lastCid).append(' ').append(-lastValue).append(' ').append(lastHValue / 2).append(' ').append(880).append(' ').append(cid);
/*     */             break;
/*     */ 
/*     */           
/*     */           case 2:
/* 623 */             if (cid != lastCid + 1 || value != lastValue || hValue != lastHValue) {
/* 624 */               buf.append(' ').append(lastCid).append(' ').append(-lastValue).append(' ').append(lastHValue / 2).append(' ').append(880).append(' ').append(cid);
/* 625 */               state = 0;
/*     */             } 
/*     */             break;
/*     */         } 
/*     */         
/* 630 */         lastValue = value;
/* 631 */         lastCid = cid;
/* 632 */         lastHValue = hValue;
/*     */       } 
/* 634 */     }  buf.append(' ').append(lastCid).append(' ').append(-lastValue).append(' ').append(lastHValue / 2).append(' ').append(880).append(" ]");
/* 635 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private static HashMap<String, Object> readFontProperties(String name) throws IOException {
/* 639 */     name = name + ".properties";
/* 640 */     InputStream is = StreamUtil.getResourceStream("com/itextpdf/text/pdf/fonts/cmaps/" + name);
/* 641 */     Properties p = new Properties();
/* 642 */     p.load(is);
/* 643 */     is.close();
/* 644 */     IntHashtable W = createMetric(p.getProperty("W"));
/* 645 */     p.remove("W");
/* 646 */     IntHashtable W2 = createMetric(p.getProperty("W2"));
/* 647 */     p.remove("W2");
/* 648 */     HashMap<String, Object> map = new HashMap<String, Object>();
/* 649 */     for (Enumeration<Object> e = p.keys(); e.hasMoreElements(); ) {
/* 650 */       Object obj = e.nextElement();
/* 651 */       map.put((String)obj, p.getProperty((String)obj));
/*     */     } 
/* 653 */     map.put("W", W);
/* 654 */     map.put("W2", W2);
/* 655 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUnicodeEquivalent(int c) {
/* 660 */     if (this.cidDirect) {
/* 661 */       if (c == 32767)
/* 662 */         return 10; 
/* 663 */       return this.cidUni.lookup(c);
/*     */     } 
/* 665 */     return c;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCidCode(int c) {
/* 670 */     if (this.cidDirect)
/* 671 */       return c; 
/* 672 */     return this.uniCid.lookup(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasKernPairs() {
/* 680 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean charExists(int c) {
/* 691 */     if (this.cidDirect)
/* 692 */       return true; 
/* 693 */     return ((this.cidByte.lookup(this.uniCid.lookup(c))).length > 0);
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
/* 705 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPostscriptFontName(String name) {
/* 715 */     this.fontName = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setKerning(int char1, int char2, int kern) {
/* 720 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getCharBBox(int c) {
/* 725 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int[] getRawCharBBox(int c, String name) {
/* 730 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] convertToBytes(String text) {
/* 741 */     if (this.cidDirect)
/* 742 */       return super.convertToBytes(text); 
/*     */     try {
/* 744 */       if (text.length() == 1)
/* 745 */         return convertToBytes(text.charAt(0)); 
/* 746 */       ByteArrayOutputStream bout = new ByteArrayOutputStream();
/* 747 */       for (int k = 0; k < text.length(); k++) {
/*     */         int val;
/* 749 */         if (Utilities.isSurrogatePair(text, k)) {
/* 750 */           val = Utilities.convertToUtf32(text, k);
/* 751 */           k++;
/*     */         } else {
/*     */           
/* 754 */           val = text.charAt(k);
/*     */         } 
/* 756 */         bout.write(convertToBytes(val));
/*     */       } 
/* 758 */       return bout.toByteArray();
/*     */     }
/* 760 */     catch (Exception ex) {
/* 761 */       throw new ExceptionConverter(ex);
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
/*     */   byte[] convertToBytes(int char1) {
/* 773 */     if (this.cidDirect)
/* 774 */       return super.convertToBytes(char1); 
/* 775 */     return this.cidByte.lookup(this.uniCid.lookup(char1));
/*     */   }
/*     */   
/*     */   public boolean isIdentity() {
/* 779 */     return this.cidDirect;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/CJKFont.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */