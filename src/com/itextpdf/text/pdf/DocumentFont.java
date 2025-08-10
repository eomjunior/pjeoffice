/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Utilities;
/*     */ import com.itextpdf.text.io.RandomAccessSourceFactory;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.AbstractCMap;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CMapParserEx;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CMapToUnicode;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CidLocation;
/*     */ import com.itextpdf.text.pdf.fonts.cmaps.CidLocationFromByte;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
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
/*     */ public class DocumentFont
/*     */   extends BaseFont
/*     */ {
/*  64 */   private HashMap<Integer, int[]> metrics = (HashMap)new HashMap<Integer, int>();
/*     */   private String fontName;
/*     */   private PRIndirectReference refFont;
/*     */   private PdfDictionary font;
/*  68 */   private IntHashtable uni2byte = new IntHashtable();
/*  69 */   private IntHashtable byte2uni = new IntHashtable();
/*     */   private IntHashtable diffmap;
/*  71 */   private float ascender = 800.0F;
/*  72 */   private float capHeight = 700.0F;
/*  73 */   private float descender = -200.0F;
/*  74 */   private float italicAngle = 0.0F;
/*  75 */   private float fontWeight = 0.0F;
/*  76 */   private float llx = -50.0F;
/*  77 */   private float lly = -200.0F;
/*  78 */   private float urx = 100.0F;
/*  79 */   private float ury = 900.0F;
/*     */   protected boolean isType0 = false;
/*  81 */   protected int defaultWidth = 1000;
/*     */   
/*     */   private IntHashtable hMetrics;
/*     */   
/*     */   protected String cjkEncoding;
/*     */   protected String uniMap;
/*     */   private BaseFont cjkMirror;
/*  88 */   private static final int[] stdEnc = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 33, 34, 35, 36, 37, 38, 8217, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 8216, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 161, 162, 163, 8260, 165, 402, 167, 164, 39, 8220, 171, 8249, 8250, 64257, 64258, 0, 8211, 8224, 8225, 183, 0, 182, 8226, 8218, 8222, 8221, 187, 8230, 8240, 0, 191, 0, 96, 180, 710, 732, 175, 728, 729, 168, 0, 730, 184, 0, 733, 731, 711, 8212, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 198, 0, 170, 0, 0, 0, 0, 321, 216, 338, 186, 0, 0, 0, 0, 0, 230, 0, 0, 0, 305, 0, 0, 322, 248, 339, 223, 0, 0, 0, 0 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DocumentFont(PdfDictionary font) {
/* 108 */     this.refFont = null;
/* 109 */     this.font = font;
/* 110 */     init();
/*     */   }
/*     */   
/*     */   DocumentFont(PRIndirectReference refFont) {
/* 114 */     this.refFont = refFont;
/* 115 */     this.font = (PdfDictionary)PdfReader.getPdfObject(refFont);
/* 116 */     init();
/*     */   }
/*     */   
/*     */   DocumentFont(PRIndirectReference refFont, PdfDictionary drEncoding) {
/* 120 */     this.refFont = refFont;
/* 121 */     this.font = (PdfDictionary)PdfReader.getPdfObject(refFont);
/* 122 */     if (this.font.get(PdfName.ENCODING) == null && drEncoding != null)
/*     */     {
/* 124 */       for (PdfName key : drEncoding.getKeys()) {
/* 125 */         this.font.put(PdfName.ENCODING, drEncoding.get(key));
/*     */       }
/*     */     }
/* 128 */     init();
/*     */   }
/*     */   
/*     */   public PdfDictionary getFontDictionary() {
/* 132 */     return this.font;
/*     */   }
/*     */   
/*     */   private void init() {
/* 136 */     this.encoding = "";
/* 137 */     this.fontSpecific = false;
/* 138 */     this.fontType = 4;
/* 139 */     PdfName baseFont = this.font.getAsName(PdfName.BASEFONT);
/* 140 */     this.fontName = (baseFont != null) ? PdfName.decodeName(baseFont.toString()) : "Unspecified Font Name";
/* 141 */     PdfName subType = this.font.getAsName(PdfName.SUBTYPE);
/* 142 */     if (PdfName.TYPE1.equals(subType) || PdfName.TRUETYPE.equals(subType)) {
/* 143 */       doType1TT();
/* 144 */     } else if (PdfName.TYPE3.equals(subType)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 150 */       fillEncoding((PdfName)null);
/* 151 */       fillDiffMap(this.font.getAsDict(PdfName.ENCODING), (CMapToUnicode)null);
/* 152 */       fillWidths();
/*     */     } else {
/*     */       
/* 155 */       PdfName encodingName = this.font.getAsName(PdfName.ENCODING);
/* 156 */       if (encodingName != null) {
/* 157 */         String enc = PdfName.decodeName(encodingName.toString());
/* 158 */         String ffontname = CJKFont.GetCompatibleFont(enc);
/* 159 */         if (ffontname != null) {
/*     */           try {
/* 161 */             this.cjkMirror = BaseFont.createFont(ffontname, enc, false);
/*     */           }
/* 163 */           catch (Exception e) {
/* 164 */             throw new ExceptionConverter(e);
/*     */           } 
/* 166 */           this.cjkEncoding = enc;
/* 167 */           this.uniMap = ((CJKFont)this.cjkMirror).getUniMap();
/*     */         } 
/* 169 */         if (PdfName.TYPE0.equals(subType)) {
/* 170 */           this.isType0 = true;
/* 171 */           if (!enc.equals("Identity-H") && this.cjkMirror != null) {
/* 172 */             PdfArray df = (PdfArray)PdfReader.getPdfObjectRelease(this.font.get(PdfName.DESCENDANTFONTS));
/* 173 */             PdfDictionary cidft = (PdfDictionary)PdfReader.getPdfObjectRelease(df.getPdfObject(0));
/* 174 */             PdfNumber dwo = (PdfNumber)PdfReader.getPdfObjectRelease(cidft.get(PdfName.DW));
/* 175 */             if (dwo != null)
/* 176 */               this.defaultWidth = dwo.intValue(); 
/* 177 */             this.hMetrics = readWidths((PdfArray)PdfReader.getPdfObjectRelease(cidft.get(PdfName.W)));
/*     */             
/* 179 */             PdfDictionary fontDesc = (PdfDictionary)PdfReader.getPdfObjectRelease(cidft.get(PdfName.FONTDESCRIPTOR));
/* 180 */             fillFontDesc(fontDesc);
/*     */           } else {
/* 182 */             processType0(this.font);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processType0(PdfDictionary font) {
/*     */     try {
/* 191 */       PdfObject toUniObject = PdfReader.getPdfObjectRelease(font.get(PdfName.TOUNICODE));
/* 192 */       PdfArray df = (PdfArray)PdfReader.getPdfObjectRelease(font.get(PdfName.DESCENDANTFONTS));
/* 193 */       PdfDictionary cidft = (PdfDictionary)PdfReader.getPdfObjectRelease(df.getPdfObject(0));
/* 194 */       PdfNumber dwo = (PdfNumber)PdfReader.getPdfObjectRelease(cidft.get(PdfName.DW));
/* 195 */       int dw = 1000;
/* 196 */       if (dwo != null)
/* 197 */         dw = dwo.intValue(); 
/* 198 */       IntHashtable widths = readWidths((PdfArray)PdfReader.getPdfObjectRelease(cidft.get(PdfName.W)));
/* 199 */       PdfDictionary fontDesc = (PdfDictionary)PdfReader.getPdfObjectRelease(cidft.get(PdfName.FONTDESCRIPTOR));
/* 200 */       fillFontDesc(fontDesc);
/* 201 */       if (toUniObject instanceof PRStream) {
/* 202 */         fillMetrics(PdfReader.getStreamBytes((PRStream)toUniObject), widths, dw);
/* 203 */       } else if ((new PdfName("Identity-H")).equals(toUniObject)) {
/* 204 */         fillMetricsIdentity(widths, dw);
/*     */       } 
/* 206 */     } catch (Exception e) {
/* 207 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private IntHashtable readWidths(PdfArray ws) {
/* 212 */     IntHashtable hh = new IntHashtable();
/* 213 */     if (ws == null)
/* 214 */       return hh; 
/* 215 */     for (int k = 0; k < ws.size(); k++) {
/* 216 */       int c1 = ((PdfNumber)PdfReader.getPdfObjectRelease(ws.getPdfObject(k))).intValue();
/* 217 */       PdfObject obj = PdfReader.getPdfObjectRelease(ws.getPdfObject(++k));
/* 218 */       if (obj.isArray()) {
/* 219 */         PdfArray a2 = (PdfArray)obj;
/* 220 */         for (int j = 0; j < a2.size(); j++) {
/* 221 */           int c2 = ((PdfNumber)PdfReader.getPdfObjectRelease(a2.getPdfObject(j))).intValue();
/* 222 */           hh.put(c1++, c2);
/*     */         } 
/*     */       } else {
/*     */         
/* 226 */         int c2 = ((PdfNumber)obj).intValue();
/* 227 */         int w = ((PdfNumber)PdfReader.getPdfObjectRelease(ws.getPdfObject(++k))).intValue();
/* 228 */         for (; c1 <= c2; c1++)
/* 229 */           hh.put(c1, w); 
/*     */       } 
/*     */     } 
/* 232 */     return hh;
/*     */   }
/*     */   
/*     */   private String decodeString(PdfString ps) {
/* 236 */     if (ps.isHexWriting()) {
/* 237 */       return PdfEncodings.convertToString(ps.getBytes(), "UnicodeBigUnmarked");
/*     */     }
/* 239 */     return ps.toUnicodeString();
/*     */   }
/*     */   
/*     */   private void fillMetricsIdentity(IntHashtable widths, int dw) {
/* 243 */     for (int i = 0; i < 65536; i++) {
/* 244 */       int w = dw;
/* 245 */       if (widths.containsKey(i))
/* 246 */         w = widths.get(i); 
/* 247 */       this.metrics.put(Integer.valueOf(i), new int[] { i, w });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void fillMetrics(byte[] touni, IntHashtable widths, int dw) {
/*     */     try {
/* 253 */       PdfContentParser ps = new PdfContentParser(new PRTokeniser(new RandomAccessFileOrArray((new RandomAccessSourceFactory()).createSource(touni))));
/* 254 */       PdfObject ob = null;
/* 255 */       boolean notFound = true;
/* 256 */       int nestLevel = 0;
/* 257 */       int maxExc = 50;
/* 258 */       label69: while (notFound || nestLevel > 0) {
/*     */         try {
/* 260 */           ob = ps.readPRObject();
/*     */         }
/* 262 */         catch (Exception ex) {
/* 263 */           if (--maxExc < 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/* 267 */         if (ob == null)
/*     */           break; 
/* 269 */         if (ob.type() == 200) {
/* 270 */           if (ob.toString().equals("begin")) {
/* 271 */             notFound = false;
/* 272 */             nestLevel++; continue;
/*     */           } 
/* 274 */           if (ob.toString().equals("end")) {
/* 275 */             nestLevel--; continue;
/*     */           } 
/* 277 */           if (ob.toString().equals("beginbfchar")) {
/*     */             while (true) {
/* 279 */               PdfObject nx = ps.readPRObject();
/* 280 */               if (nx.toString().equals("endbfchar"))
/*     */                 continue label69; 
/* 282 */               String cid = decodeString((PdfString)nx);
/* 283 */               String uni = decodeString((PdfString)ps.readPRObject());
/* 284 */               if (uni.length() == 1) {
/* 285 */                 int cidc = cid.charAt(0);
/* 286 */                 int unic = uni.charAt(uni.length() - 1);
/* 287 */                 int w = dw;
/* 288 */                 if (widths.containsKey(cidc))
/* 289 */                   w = widths.get(cidc); 
/* 290 */                 this.metrics.put(Integer.valueOf(unic), new int[] { cidc, w });
/*     */               } 
/*     */             } 
/*     */           }
/* 294 */           if (ob.toString().equals("beginbfrange")) {
/*     */             while (true) {
/* 296 */               PdfObject nx = ps.readPRObject();
/* 297 */               if (nx.toString().equals("endbfrange"))
/*     */                 continue label69; 
/* 299 */               String cid1 = decodeString((PdfString)nx);
/* 300 */               String cid2 = decodeString((PdfString)ps.readPRObject());
/* 301 */               int cid1c = cid1.charAt(0);
/* 302 */               int cid2c = cid2.charAt(0);
/* 303 */               PdfObject ob2 = ps.readPRObject();
/* 304 */               if (ob2.isString()) {
/* 305 */                 String uni = decodeString((PdfString)ob2);
/* 306 */                 if (uni.length() == 1) {
/* 307 */                   int unic = uni.charAt(uni.length() - 1);
/* 308 */                   for (; cid1c <= cid2c; cid1c++, unic++) {
/* 309 */                     int w = dw;
/* 310 */                     if (widths.containsKey(cid1c))
/* 311 */                       w = widths.get(cid1c); 
/* 312 */                     this.metrics.put(Integer.valueOf(unic), new int[] { cid1c, w });
/*     */                   } 
/*     */                 } 
/*     */                 continue;
/*     */               } 
/* 317 */               PdfArray a = (PdfArray)ob2;
/* 318 */               for (int j = 0; j < a.size(); j++, cid1c++) {
/* 319 */                 String uni = decodeString(a.getAsString(j));
/* 320 */                 if (uni.length() == 1) {
/* 321 */                   int unic = uni.charAt(uni.length() - 1);
/* 322 */                   int w = dw;
/* 323 */                   if (widths.containsKey(cid1c))
/* 324 */                     w = widths.get(cid1c); 
/* 325 */                   this.metrics.put(Integer.valueOf(unic), new int[] { cid1c, w });
/*     */                 }
/*     */               
/*     */               }
/*     */             
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 334 */     } catch (Exception e) {
/* 335 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doType1TT() {
/* 340 */     CMapToUnicode toUnicode = null;
/* 341 */     PdfObject enc = PdfReader.getPdfObject(this.font.get(PdfName.ENCODING));
/* 342 */     if (enc == null) {
/* 343 */       PdfName baseFont = this.font.getAsName(PdfName.BASEFONT);
/* 344 */       if (BuiltinFonts14.containsKey(this.fontName) && (PdfName.SYMBOL
/* 345 */         .equals(baseFont) || PdfName.ZAPFDINGBATS.equals(baseFont))) {
/* 346 */         fillEncoding(baseFont);
/*     */       } else {
/* 348 */         fillEncoding((PdfName)null);
/*     */       }  try {
/* 350 */         toUnicode = processToUnicode();
/* 351 */         if (toUnicode != null) {
/* 352 */           Map<Integer, Integer> rm = toUnicode.createReverseMapping();
/* 353 */           for (Map.Entry<Integer, Integer> kv : rm.entrySet()) {
/* 354 */             this.uni2byte.put(((Integer)kv.getKey()).intValue(), ((Integer)kv.getValue()).intValue());
/* 355 */             this.byte2uni.put(((Integer)kv.getValue()).intValue(), ((Integer)kv.getKey()).intValue());
/*     */           }
/*     */         
/*     */         } 
/* 359 */       } catch (Exception ex) {
/* 360 */         throw new ExceptionConverter(ex);
/*     */       }
/*     */     
/*     */     }
/* 364 */     else if (enc.isName()) {
/* 365 */       fillEncoding((PdfName)enc);
/* 366 */     } else if (enc.isDictionary()) {
/* 367 */       PdfDictionary encDic = (PdfDictionary)enc;
/* 368 */       enc = PdfReader.getPdfObject(encDic.get(PdfName.BASEENCODING));
/* 369 */       if (enc == null) {
/* 370 */         fillEncoding((PdfName)null);
/*     */       } else {
/* 372 */         fillEncoding((PdfName)enc);
/* 373 */       }  fillDiffMap(encDic, toUnicode);
/*     */     } 
/*     */ 
/*     */     
/* 377 */     if (BuiltinFonts14.containsKey(this.fontName)) {
/*     */       BaseFont bf;
/*     */       try {
/* 380 */         bf = BaseFont.createFont(this.fontName, "Cp1252", false);
/*     */       }
/* 382 */       catch (Exception exception) {
/* 383 */         throw new ExceptionConverter(exception);
/*     */       } 
/* 385 */       int[] e = this.uni2byte.toOrderedKeys(); int k;
/* 386 */       for (k = 0; k < e.length; k++) {
/* 387 */         int n = this.uni2byte.get(e[k]);
/* 388 */         this.widths[n] = bf.getRawWidth(n, GlyphList.unicodeToName(e[k]));
/*     */       } 
/* 390 */       if (this.diffmap != null) {
/* 391 */         e = this.diffmap.toOrderedKeys();
/* 392 */         for (k = 0; k < e.length; k++) {
/* 393 */           int n = this.diffmap.get(e[k]);
/* 394 */           this.widths[n] = bf.getRawWidth(n, GlyphList.unicodeToName(e[k]));
/*     */         } 
/* 396 */         this.diffmap = null;
/*     */       } 
/* 398 */       this.ascender = bf.getFontDescriptor(1, 1000.0F);
/* 399 */       this.capHeight = bf.getFontDescriptor(2, 1000.0F);
/* 400 */       this.descender = bf.getFontDescriptor(3, 1000.0F);
/* 401 */       this.italicAngle = bf.getFontDescriptor(4, 1000.0F);
/* 402 */       this.fontWeight = bf.getFontDescriptor(23, 1000.0F);
/* 403 */       this.llx = bf.getFontDescriptor(5, 1000.0F);
/* 404 */       this.lly = bf.getFontDescriptor(6, 1000.0F);
/* 405 */       this.urx = bf.getFontDescriptor(7, 1000.0F);
/* 406 */       this.ury = bf.getFontDescriptor(8, 1000.0F);
/*     */     } 
/* 408 */     fillWidths();
/* 409 */     fillFontDesc(this.font.getAsDict(PdfName.FONTDESCRIPTOR));
/*     */   }
/*     */   
/*     */   private void fillWidths() {
/* 413 */     PdfArray newWidths = this.font.getAsArray(PdfName.WIDTHS);
/* 414 */     PdfNumber first = this.font.getAsNumber(PdfName.FIRSTCHAR);
/* 415 */     PdfNumber last = this.font.getAsNumber(PdfName.LASTCHAR);
/* 416 */     if (first != null && last != null && newWidths != null) {
/* 417 */       int f = first.intValue();
/* 418 */       int nSize = f + newWidths.size();
/* 419 */       if (this.widths.length < nSize) {
/* 420 */         int[] tmp = new int[nSize];
/* 421 */         System.arraycopy(this.widths, 0, tmp, 0, f);
/* 422 */         this.widths = tmp;
/*     */       } 
/* 424 */       for (int k = 0; k < newWidths.size(); k++) {
/* 425 */         this.widths[f + k] = newWidths.getAsNumber(k).intValue();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void fillDiffMap(PdfDictionary encDic, CMapToUnicode toUnicode) {
/* 431 */     PdfArray diffs = encDic.getAsArray(PdfName.DIFFERENCES);
/* 432 */     if (diffs != null) {
/* 433 */       this.diffmap = new IntHashtable();
/* 434 */       int currentNumber = 0;
/* 435 */       for (int k = 0; k < diffs.size(); k++) {
/* 436 */         PdfObject obj = diffs.getPdfObject(k);
/* 437 */         if (obj.isNumber()) {
/* 438 */           currentNumber = ((PdfNumber)obj).intValue();
/*     */         } else {
/* 440 */           int[] c = GlyphList.nameToUnicode(PdfName.decodeName(((PdfName)obj).toString()));
/* 441 */           if (c != null && c.length > 0) {
/* 442 */             this.uni2byte.put(c[0], currentNumber);
/* 443 */             this.byte2uni.put(currentNumber, c[0]);
/* 444 */             this.diffmap.put(c[0], currentNumber);
/*     */           } else {
/*     */             
/* 447 */             if (toUnicode == null) {
/* 448 */               toUnicode = processToUnicode();
/* 449 */               if (toUnicode == null) {
/* 450 */                 toUnicode = new CMapToUnicode();
/*     */               }
/*     */             } 
/* 453 */             String unicode = toUnicode.lookup(new byte[] { (byte)currentNumber }, 0, 1);
/* 454 */             if (unicode != null && unicode.length() == 1) {
/* 455 */               this.uni2byte.put(unicode.charAt(0), currentNumber);
/* 456 */               this.byte2uni.put(currentNumber, unicode.charAt(0));
/* 457 */               this.diffmap.put(unicode.charAt(0), currentNumber);
/*     */             } 
/*     */           } 
/* 460 */           currentNumber++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private CMapToUnicode processToUnicode() {
/* 467 */     CMapToUnicode cmapRet = null;
/* 468 */     PdfObject toUni = PdfReader.getPdfObjectRelease(this.font.get(PdfName.TOUNICODE));
/* 469 */     if (toUni instanceof PRStream) {
/*     */       try {
/* 471 */         byte[] touni = PdfReader.getStreamBytes((PRStream)toUni);
/* 472 */         CidLocationFromByte lb = new CidLocationFromByte(touni);
/* 473 */         cmapRet = new CMapToUnicode();
/* 474 */         CMapParserEx.parseCid("", (AbstractCMap)cmapRet, (CidLocation)lb);
/* 475 */       } catch (Exception e) {
/* 476 */         cmapRet = null;
/*     */       } 
/*     */     }
/* 479 */     return cmapRet;
/*     */   }
/*     */   
/*     */   private void fillFontDesc(PdfDictionary fontDesc) {
/* 483 */     if (fontDesc == null)
/*     */       return; 
/* 485 */     PdfNumber v = fontDesc.getAsNumber(PdfName.ASCENT);
/* 486 */     if (v != null)
/* 487 */       this.ascender = v.floatValue(); 
/* 488 */     v = fontDesc.getAsNumber(PdfName.CAPHEIGHT);
/* 489 */     if (v != null)
/* 490 */       this.capHeight = v.floatValue(); 
/* 491 */     v = fontDesc.getAsNumber(PdfName.DESCENT);
/* 492 */     if (v != null)
/* 493 */       this.descender = v.floatValue(); 
/* 494 */     v = fontDesc.getAsNumber(PdfName.ITALICANGLE);
/* 495 */     if (v != null)
/* 496 */       this.italicAngle = v.floatValue(); 
/* 497 */     v = fontDesc.getAsNumber(PdfName.FONTWEIGHT);
/* 498 */     if (v != null) {
/* 499 */       this.fontWeight = v.floatValue();
/*     */     }
/* 501 */     PdfArray bbox = fontDesc.getAsArray(PdfName.FONTBBOX);
/* 502 */     if (bbox != null) {
/* 503 */       this.llx = bbox.getAsNumber(0).floatValue();
/* 504 */       this.lly = bbox.getAsNumber(1).floatValue();
/* 505 */       this.urx = bbox.getAsNumber(2).floatValue();
/* 506 */       this.ury = bbox.getAsNumber(3).floatValue();
/* 507 */       if (this.llx > this.urx) {
/* 508 */         float t = this.llx;
/* 509 */         this.llx = this.urx;
/* 510 */         this.urx = t;
/*     */       } 
/* 512 */       if (this.lly > this.ury) {
/* 513 */         float t = this.lly;
/* 514 */         this.lly = this.ury;
/* 515 */         this.ury = t;
/*     */       } 
/*     */     } 
/* 518 */     float maxAscent = Math.max(this.ury, this.ascender);
/* 519 */     float minDescent = Math.min(this.lly, this.descender);
/* 520 */     this.ascender = maxAscent * 1000.0F / (maxAscent - minDescent);
/* 521 */     this.descender = minDescent * 1000.0F / (maxAscent - minDescent);
/*     */   }
/*     */   
/*     */   private void fillEncoding(PdfName encoding) {
/* 525 */     if (encoding == null && isSymbolic()) {
/* 526 */       for (int k = 0; k < 256; k++) {
/* 527 */         this.uni2byte.put(k, k);
/* 528 */         this.byte2uni.put(k, k);
/*     */       } 
/* 530 */     } else if (PdfName.MAC_ROMAN_ENCODING.equals(encoding) || PdfName.WIN_ANSI_ENCODING.equals(encoding) || PdfName.SYMBOL
/* 531 */       .equals(encoding) || PdfName.ZAPFDINGBATS.equals(encoding)) {
/* 532 */       byte[] b = new byte[256];
/* 533 */       for (int k = 0; k < 256; k++)
/* 534 */         b[k] = (byte)k; 
/* 535 */       String enc = "Cp1252";
/* 536 */       if (PdfName.MAC_ROMAN_ENCODING.equals(encoding)) {
/* 537 */         enc = "MacRoman";
/* 538 */       } else if (PdfName.SYMBOL.equals(encoding)) {
/* 539 */         enc = "Symbol";
/* 540 */       } else if (PdfName.ZAPFDINGBATS.equals(encoding)) {
/* 541 */         enc = "ZapfDingbats";
/* 542 */       }  String cv = PdfEncodings.convertToString(b, enc);
/* 543 */       char[] arr = cv.toCharArray();
/* 544 */       for (int i = 0; i < 256; i++) {
/* 545 */         this.uni2byte.put(arr[i], i);
/* 546 */         this.byte2uni.put(i, arr[i]);
/*     */       } 
/* 548 */       this.encoding = enc;
/*     */     } else {
/*     */       
/* 551 */       for (int k = 0; k < 256; k++) {
/* 552 */         this.uni2byte.put(stdEnc[k], k);
/* 553 */         this.byte2uni.put(k, stdEnc[k]);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[][] getFamilyFontName() {
/* 569 */     return getFullFontName();
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
/*     */   public float getFontDescriptor(int key, float fontSize) {
/* 583 */     if (this.cjkMirror != null)
/* 584 */       return this.cjkMirror.getFontDescriptor(key, fontSize); 
/* 585 */     switch (key) {
/*     */       case 1:
/*     */       case 9:
/* 588 */         return this.ascender * fontSize / 1000.0F;
/*     */       case 2:
/* 590 */         return this.capHeight * fontSize / 1000.0F;
/*     */       case 3:
/*     */       case 10:
/* 593 */         return this.descender * fontSize / 1000.0F;
/*     */       case 4:
/* 595 */         return this.italicAngle;
/*     */       case 5:
/* 597 */         return this.llx * fontSize / 1000.0F;
/*     */       case 6:
/* 599 */         return this.lly * fontSize / 1000.0F;
/*     */       case 7:
/* 601 */         return this.urx * fontSize / 1000.0F;
/*     */       case 8:
/* 603 */         return this.ury * fontSize / 1000.0F;
/*     */       case 11:
/* 605 */         return 0.0F;
/*     */       case 12:
/* 607 */         return (this.urx - this.llx) * fontSize / 1000.0F;
/*     */       case 23:
/* 609 */         return this.fontWeight * fontSize / 1000.0F;
/*     */     } 
/* 611 */     return 0.0F;
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
/*     */   public String[][] getFullFontName() {
/* 625 */     return new String[][] { { "", "", "", this.fontName } };
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
/*     */   public String[][] getAllNameEntries() {
/* 639 */     return new String[][] { { "4", "", "", "", this.fontName } };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getKerning(int char1, int char2) {
/* 650 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPostscriptFontName() {
/* 659 */     return this.fontName;
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
/*     */   int getRawWidth(int c, String name) {
/* 671 */     return 0;
/*     */   }
/*     */ 
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
/*     */   
/*     */   void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params) throws DocumentException, IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfStream getFullFontStream() {
/* 702 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWidth(int char1) {
/* 712 */     if (this.isType0) {
/* 713 */       if (this.hMetrics != null && this.cjkMirror != null && !this.cjkMirror.isVertical()) {
/* 714 */         int c = this.cjkMirror.getCidCode(char1);
/* 715 */         int v = this.hMetrics.get(c);
/* 716 */         if (v > 0) {
/* 717 */           return v;
/*     */         }
/* 719 */         return this.defaultWidth;
/*     */       } 
/* 721 */       int[] ws = this.metrics.get(Integer.valueOf(char1));
/* 722 */       if (ws != null) {
/* 723 */         return ws[1];
/*     */       }
/* 725 */       return 0;
/*     */     } 
/*     */     
/* 728 */     if (this.cjkMirror != null)
/* 729 */       return this.cjkMirror.getWidth(char1); 
/* 730 */     return super.getWidth(char1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth(String text) {
/* 735 */     if (this.isType0) {
/* 736 */       int total = 0;
/* 737 */       if (this.hMetrics != null && this.cjkMirror != null && !this.cjkMirror.isVertical()) {
/* 738 */         if (((CJKFont)this.cjkMirror).isIdentity()) {
/* 739 */           for (int k = 0; k < text.length(); k++) {
/* 740 */             total += getWidth(text.charAt(k));
/*     */           }
/*     */         } else {
/*     */           
/* 744 */           for (int k = 0; k < text.length(); k++) {
/*     */             int val;
/* 746 */             if (Utilities.isSurrogatePair(text, k)) {
/* 747 */               val = Utilities.convertToUtf32(text, k);
/* 748 */               k++;
/*     */             } else {
/*     */               
/* 751 */               val = text.charAt(k);
/*     */             } 
/* 753 */             total += getWidth(val);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 757 */         char[] chars = text.toCharArray();
/* 758 */         int len = chars.length;
/* 759 */         for (int k = 0; k < len; k++) {
/* 760 */           int[] ws = this.metrics.get(Integer.valueOf(chars[k]));
/* 761 */           if (ws != null)
/* 762 */             total += ws[1]; 
/*     */         } 
/*     */       } 
/* 765 */       return total;
/*     */     } 
/* 767 */     if (this.cjkMirror != null)
/* 768 */       return this.cjkMirror.getWidth(text); 
/* 769 */     return super.getWidth(text);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] convertToBytes(String text) {
/* 774 */     if (this.cjkMirror != null)
/* 775 */       return this.cjkMirror.convertToBytes(text); 
/* 776 */     if (this.isType0) {
/* 777 */       char[] chars = text.toCharArray();
/* 778 */       int len = chars.length;
/* 779 */       byte[] arrayOfByte1 = new byte[len * 2];
/* 780 */       int bptr = 0;
/* 781 */       for (int i = 0; i < len; i++) {
/* 782 */         int[] ws = this.metrics.get(Integer.valueOf(chars[i]));
/* 783 */         if (ws != null) {
/* 784 */           int g = ws[0];
/* 785 */           arrayOfByte1[bptr++] = (byte)(g / 256);
/* 786 */           arrayOfByte1[bptr++] = (byte)g;
/*     */         } 
/*     */       } 
/* 789 */       if (bptr == arrayOfByte1.length) {
/* 790 */         return arrayOfByte1;
/*     */       }
/* 792 */       byte[] nb = new byte[bptr];
/* 793 */       System.arraycopy(arrayOfByte1, 0, nb, 0, bptr);
/* 794 */       return nb;
/*     */     } 
/*     */ 
/*     */     
/* 798 */     char[] cc = text.toCharArray();
/* 799 */     byte[] b = new byte[cc.length];
/* 800 */     int ptr = 0;
/* 801 */     for (int k = 0; k < cc.length; k++) {
/* 802 */       if (this.uni2byte.containsKey(cc[k]))
/* 803 */         b[ptr++] = (byte)this.uni2byte.get(cc[k]); 
/*     */     } 
/* 805 */     if (ptr == b.length) {
/* 806 */       return b;
/*     */     }
/* 808 */     byte[] b2 = new byte[ptr];
/* 809 */     System.arraycopy(b, 0, b2, 0, ptr);
/* 810 */     return b2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] convertToBytes(int char1) {
/* 817 */     if (this.cjkMirror != null)
/* 818 */       return this.cjkMirror.convertToBytes(char1); 
/* 819 */     if (this.isType0) {
/* 820 */       int[] ws = this.metrics.get(Integer.valueOf(char1));
/* 821 */       if (ws != null) {
/* 822 */         int g = ws[0];
/* 823 */         return new byte[] { (byte)(g / 256), (byte)g };
/*     */       } 
/*     */       
/* 826 */       return new byte[0];
/*     */     } 
/*     */     
/* 829 */     if (this.uni2byte.containsKey(char1)) {
/* 830 */       return new byte[] { (byte)this.uni2byte.get(char1) };
/*     */     }
/* 832 */     return new byte[0];
/*     */   }
/*     */ 
/*     */   
/*     */   PdfIndirectReference getIndirectReference() {
/* 837 */     if (this.refFont == null)
/* 838 */       throw new IllegalArgumentException("Font reuse not allowed with direct font objects."); 
/* 839 */     return this.refFont;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean charExists(int c) {
/* 844 */     if (this.cjkMirror != null)
/* 845 */       return this.cjkMirror.charExists(c); 
/* 846 */     if (this.isType0) {
/* 847 */       return this.metrics.containsKey(Integer.valueOf(c));
/*     */     }
/*     */     
/* 850 */     return super.charExists(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public double[] getFontMatrix() {
/* 855 */     if (this.font.getAsArray(PdfName.FONTMATRIX) != null) {
/* 856 */       return this.font.getAsArray(PdfName.FONTMATRIX).asDoubleArray();
/*     */     }
/* 858 */     return DEFAULT_FONT_MATRIX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPostscriptFontName(String name) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setKerning(int char1, int char2, int kern) {
/* 873 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getCharBBox(int c) {
/* 878 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int[] getRawCharBBox(int c, String name) {
/* 883 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isVertical() {
/* 888 */     if (this.cjkMirror != null) {
/* 889 */       return this.cjkMirror.isVertical();
/*     */     }
/* 891 */     return super.isVertical();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   IntHashtable getUni2Byte() {
/* 900 */     return this.uni2byte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   IntHashtable getByte2Uni() {
/* 909 */     return this.byte2uni;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   IntHashtable getDiffmap() {
/* 918 */     return this.diffmap;
/*     */   }
/*     */   
/*     */   boolean isSymbolic() {
/* 922 */     PdfDictionary fontDescriptor = this.font.getAsDict(PdfName.FONTDESCRIPTOR);
/* 923 */     if (fontDescriptor == null)
/* 924 */       return false; 
/* 925 */     PdfNumber flags = fontDescriptor.getAsNumber(PdfName.FLAGS);
/* 926 */     if (flags == null)
/* 927 */       return false; 
/* 928 */     return ((flags.intValue() & 0x4) != 0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/DocumentFont.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */