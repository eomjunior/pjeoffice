/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.Document;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.io.StreamUtil;
/*     */ import com.itextpdf.text.pdf.fonts.FontsResourceAnchor;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Type1Font
/*     */   extends BaseFont
/*     */ {
/*     */   private static FontsResourceAnchor resourceAnchor;
/*     */   protected byte[] pfb;
/*     */   private String FontName;
/*     */   private String FullName;
/*     */   private String FamilyName;
/*  80 */   private String Weight = "";
/*     */ 
/*     */   
/*  83 */   private float ItalicAngle = 0.0F;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean IsFixedPitch = false;
/*     */ 
/*     */   
/*     */   private String CharacterSet;
/*     */ 
/*     */   
/*  93 */   private int llx = -50;
/*     */ 
/*     */   
/*  96 */   private int lly = -200;
/*     */ 
/*     */   
/*  99 */   private int urx = 1000;
/*     */ 
/*     */   
/* 102 */   private int ury = 900;
/*     */ 
/*     */   
/* 105 */   private int UnderlinePosition = -100;
/*     */ 
/*     */   
/* 108 */   private int UnderlineThickness = 50;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   private String EncodingScheme = "FontSpecific";
/*     */ 
/*     */   
/* 117 */   private int CapHeight = 700;
/*     */ 
/*     */   
/* 120 */   private int XHeight = 480;
/*     */ 
/*     */   
/* 123 */   private int Ascender = 800;
/*     */ 
/*     */   
/* 126 */   private int Descender = -200;
/*     */ 
/*     */   
/*     */   private int StdHW;
/*     */ 
/*     */   
/* 132 */   private int StdVW = 80;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   private HashMap<Object, Object[]> CharMetrics = (HashMap)new HashMap<Object, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   private HashMap<String, Object[]> KernPairs = (HashMap)new HashMap<String, Object>();
/*     */ 
/*     */ 
/*     */   
/*     */   private String fileName;
/*     */ 
/*     */   
/*     */   private boolean builtinFont = false;
/*     */ 
/*     */   
/* 156 */   private static final int[] PFB_TYPES = new int[] { 1, 2, 1 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Type1Font(String afmFile, String enc, boolean emb, byte[] ttfAfm, byte[] pfb, boolean forceRead) throws DocumentException, IOException {
/* 170 */     if (emb && ttfAfm != null && pfb == null)
/* 171 */       throw new DocumentException(MessageLocalization.getComposedMessage("two.byte.arrays.are.needed.if.the.type1.font.is.embedded", new Object[0])); 
/* 172 */     if (emb && ttfAfm != null)
/* 173 */       this.pfb = pfb; 
/* 174 */     this.encoding = enc;
/* 175 */     this.embedded = emb;
/* 176 */     this.fileName = afmFile;
/* 177 */     this.fontType = 0;
/* 178 */     RandomAccessFileOrArray rf = null;
/* 179 */     InputStream is = null;
/* 180 */     if (BuiltinFonts14.containsKey(afmFile)) {
/* 181 */       this.embedded = false;
/* 182 */       this.builtinFont = true;
/* 183 */       byte[] buf = new byte[1024];
/*     */       try {
/* 185 */         if (resourceAnchor == null)
/* 186 */           resourceAnchor = new FontsResourceAnchor(); 
/* 187 */         is = StreamUtil.getResourceStream("com/itextpdf/text/pdf/fonts/" + afmFile + ".afm", resourceAnchor.getClass().getClassLoader());
/* 188 */         if (is == null) {
/* 189 */           String msg = MessageLocalization.getComposedMessage("1.not.found.as.resource", new Object[] { afmFile });
/* 190 */           System.err.println(msg);
/* 191 */           throw new DocumentException(msg);
/*     */         } 
/* 193 */         ByteArrayOutputStream out = new ByteArrayOutputStream();
/*     */         while (true) {
/* 195 */           int size = is.read(buf);
/* 196 */           if (size < 0)
/*     */             break; 
/* 198 */           out.write(buf, 0, size);
/*     */         } 
/* 200 */         buf = out.toByteArray();
/*     */       } finally {
/*     */         
/* 203 */         if (is != null) {
/*     */           try {
/* 205 */             is.close();
/*     */           }
/* 207 */           catch (Exception exception) {}
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 213 */         rf = new RandomAccessFileOrArray(buf);
/* 214 */         process(rf);
/*     */       } finally {
/*     */         
/* 217 */         if (rf != null) {
/*     */           try {
/* 219 */             rf.close();
/*     */           }
/* 221 */           catch (Exception exception) {}
/*     */         
/*     */         }
/*     */       }
/*     */     
/*     */     }
/* 227 */     else if (afmFile.toLowerCase().endsWith(".afm")) {
/*     */       try {
/* 229 */         if (ttfAfm == null) {
/* 230 */           rf = new RandomAccessFileOrArray(afmFile, forceRead, Document.plainRandomAccess);
/*     */         } else {
/* 232 */           rf = new RandomAccessFileOrArray(ttfAfm);
/* 233 */         }  process(rf);
/*     */       } finally {
/*     */         
/* 236 */         if (rf != null) {
/*     */           try {
/* 238 */             rf.close();
/*     */           }
/* 240 */           catch (Exception exception) {}
/*     */         
/*     */         }
/*     */       }
/*     */     
/*     */     }
/* 246 */     else if (afmFile.toLowerCase().endsWith(".pfm")) {
/*     */       try {
/* 248 */         ByteArrayOutputStream ba = new ByteArrayOutputStream();
/* 249 */         if (ttfAfm == null) {
/* 250 */           rf = new RandomAccessFileOrArray(afmFile, forceRead, Document.plainRandomAccess);
/*     */         } else {
/* 252 */           rf = new RandomAccessFileOrArray(ttfAfm);
/* 253 */         }  Pfm2afm.convert(rf, ba);
/* 254 */         rf.close();
/* 255 */         rf = new RandomAccessFileOrArray(ba.toByteArray());
/* 256 */         process(rf);
/*     */       } finally {
/*     */         
/* 259 */         if (rf != null) {
/*     */           try {
/* 261 */             rf.close();
/*     */           }
/* 263 */           catch (Exception exception) {}
/*     */         }
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 270 */       throw new DocumentException(MessageLocalization.getComposedMessage("1.is.not.an.afm.or.pfm.font.file", new Object[] { afmFile }));
/*     */     } 
/* 272 */     this.EncodingScheme = this.EncodingScheme.trim();
/* 273 */     if (this.EncodingScheme.equals("AdobeStandardEncoding") || this.EncodingScheme.equals("StandardEncoding")) {
/* 274 */       this.fontSpecific = false;
/*     */     }
/* 276 */     if (!this.encoding.startsWith("#"))
/* 277 */       PdfEncodings.convertToBytes(" ", enc); 
/* 278 */     createEncoding();
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
/*     */     Object[] metrics;
/* 291 */     if (name == null) {
/* 292 */       metrics = this.CharMetrics.get(Integer.valueOf(c));
/*     */     } else {
/*     */       
/* 295 */       if (name.equals(".notdef"))
/* 296 */         return 0; 
/* 297 */       metrics = this.CharMetrics.get(name);
/*     */     } 
/* 299 */     if (metrics != null)
/* 300 */       return ((Integer)metrics[1]).intValue(); 
/* 301 */     return 0;
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
/*     */   public int getKerning(int char1, int char2) {
/* 314 */     String first = GlyphList.unicodeToName(char1);
/* 315 */     if (first == null)
/* 316 */       return 0; 
/* 317 */     String second = GlyphList.unicodeToName(char2);
/* 318 */     if (second == null)
/* 319 */       return 0; 
/* 320 */     Object[] obj = this.KernPairs.get(first);
/* 321 */     if (obj == null)
/* 322 */       return 0; 
/* 323 */     for (int k = 0; k < obj.length; k += 2) {
/* 324 */       if (second.equals(obj[k]))
/* 325 */         return ((Integer)obj[k + 1]).intValue(); 
/*     */     } 
/* 327 */     return 0;
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
/*     */   public void process(RandomAccessFileOrArray rf) throws DocumentException, IOException {
/* 339 */     boolean isMetrics = false; String line;
/* 340 */     while ((line = rf.readLine()) != null) {
/*     */       
/* 342 */       StringTokenizer tok = new StringTokenizer(line, " ,\n\r\t\f");
/* 343 */       if (!tok.hasMoreTokens())
/*     */         continue; 
/* 345 */       String ident = tok.nextToken();
/* 346 */       if (ident.equals("FontName")) {
/* 347 */         this.FontName = tok.nextToken("ÿ").substring(1); continue;
/* 348 */       }  if (ident.equals("FullName")) {
/* 349 */         this.FullName = tok.nextToken("ÿ").substring(1); continue;
/* 350 */       }  if (ident.equals("FamilyName")) {
/* 351 */         this.FamilyName = tok.nextToken("ÿ").substring(1); continue;
/* 352 */       }  if (ident.equals("Weight")) {
/* 353 */         this.Weight = tok.nextToken("ÿ").substring(1); continue;
/* 354 */       }  if (ident.equals("ItalicAngle")) {
/* 355 */         this.ItalicAngle = Float.parseFloat(tok.nextToken()); continue;
/* 356 */       }  if (ident.equals("IsFixedPitch")) {
/* 357 */         this.IsFixedPitch = tok.nextToken().equals("true"); continue;
/* 358 */       }  if (ident.equals("CharacterSet")) {
/* 359 */         this.CharacterSet = tok.nextToken("ÿ").substring(1); continue;
/* 360 */       }  if (ident.equals("FontBBox")) {
/*     */         
/* 362 */         this.llx = (int)Float.parseFloat(tok.nextToken());
/* 363 */         this.lly = (int)Float.parseFloat(tok.nextToken());
/* 364 */         this.urx = (int)Float.parseFloat(tok.nextToken());
/* 365 */         this.ury = (int)Float.parseFloat(tok.nextToken()); continue;
/*     */       } 
/* 367 */       if (ident.equals("UnderlinePosition")) {
/* 368 */         this.UnderlinePosition = (int)Float.parseFloat(tok.nextToken()); continue;
/* 369 */       }  if (ident.equals("UnderlineThickness")) {
/* 370 */         this.UnderlineThickness = (int)Float.parseFloat(tok.nextToken()); continue;
/* 371 */       }  if (ident.equals("EncodingScheme")) {
/* 372 */         this.EncodingScheme = tok.nextToken("ÿ").substring(1); continue;
/* 373 */       }  if (ident.equals("CapHeight")) {
/* 374 */         this.CapHeight = (int)Float.parseFloat(tok.nextToken()); continue;
/* 375 */       }  if (ident.equals("XHeight")) {
/* 376 */         this.XHeight = (int)Float.parseFloat(tok.nextToken()); continue;
/* 377 */       }  if (ident.equals("Ascender")) {
/* 378 */         this.Ascender = (int)Float.parseFloat(tok.nextToken()); continue;
/* 379 */       }  if (ident.equals("Descender")) {
/* 380 */         this.Descender = (int)Float.parseFloat(tok.nextToken()); continue;
/* 381 */       }  if (ident.equals("StdHW")) {
/* 382 */         this.StdHW = (int)Float.parseFloat(tok.nextToken()); continue;
/* 383 */       }  if (ident.equals("StdVW")) {
/* 384 */         this.StdVW = (int)Float.parseFloat(tok.nextToken()); continue;
/* 385 */       }  if (ident.equals("StartCharMetrics")) {
/*     */         
/* 387 */         isMetrics = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 391 */     if (!isMetrics)
/* 392 */       throw new DocumentException(MessageLocalization.getComposedMessage("missing.startcharmetrics.in.1", new Object[] { this.fileName })); 
/* 393 */     while ((line = rf.readLine()) != null) {
/*     */       
/* 395 */       StringTokenizer tok = new StringTokenizer(line);
/* 396 */       if (!tok.hasMoreTokens())
/*     */         continue; 
/* 398 */       String ident = tok.nextToken();
/* 399 */       if (ident.equals("EndCharMetrics")) {
/*     */         
/* 401 */         isMetrics = false;
/*     */         break;
/*     */       } 
/* 404 */       Integer C = Integer.valueOf(-1);
/* 405 */       Integer WX = Integer.valueOf(250);
/* 406 */       String N = "";
/* 407 */       int[] B = null;
/*     */       
/* 409 */       tok = new StringTokenizer(line, ";");
/* 410 */       while (tok.hasMoreTokens()) {
/*     */         
/* 412 */         StringTokenizer tokc = new StringTokenizer(tok.nextToken());
/* 413 */         if (!tokc.hasMoreTokens())
/*     */           continue; 
/* 415 */         ident = tokc.nextToken();
/* 416 */         if (ident.equals("C")) {
/* 417 */           C = Integer.valueOf(tokc.nextToken()); continue;
/* 418 */         }  if (ident.equals("WX")) {
/* 419 */           WX = Integer.valueOf((int)Float.parseFloat(tokc.nextToken())); continue;
/* 420 */         }  if (ident.equals("N")) {
/* 421 */           N = tokc.nextToken(); continue;
/* 422 */         }  if (ident.equals("B"))
/*     */         {
/*     */ 
/*     */           
/* 426 */           B = new int[] { Integer.parseInt(tokc.nextToken()), Integer.parseInt(tokc.nextToken()), Integer.parseInt(tokc.nextToken()), Integer.parseInt(tokc.nextToken()) };
/*     */         }
/*     */       } 
/* 429 */       Object[] metrics = { C, WX, N, B };
/* 430 */       if (C.intValue() >= 0)
/* 431 */         this.CharMetrics.put(C, metrics); 
/* 432 */       this.CharMetrics.put(N, metrics);
/*     */     } 
/* 434 */     if (isMetrics)
/* 435 */       throw new DocumentException(MessageLocalization.getComposedMessage("missing.endcharmetrics.in.1", new Object[] { this.fileName })); 
/* 436 */     if (!this.CharMetrics.containsKey("nonbreakingspace")) {
/* 437 */       Object[] space = this.CharMetrics.get("space");
/* 438 */       if (space != null)
/* 439 */         this.CharMetrics.put("nonbreakingspace", space); 
/*     */     } 
/* 441 */     while ((line = rf.readLine()) != null) {
/*     */       
/* 443 */       StringTokenizer tok = new StringTokenizer(line);
/* 444 */       if (!tok.hasMoreTokens())
/*     */         continue; 
/* 446 */       String ident = tok.nextToken();
/* 447 */       if (ident.equals("EndFontMetrics"))
/*     */         return; 
/* 449 */       if (ident.equals("StartKernPairs")) {
/*     */         
/* 451 */         isMetrics = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 455 */     if (!isMetrics)
/* 456 */       throw new DocumentException(MessageLocalization.getComposedMessage("missing.endfontmetrics.in.1", new Object[] { this.fileName })); 
/* 457 */     while ((line = rf.readLine()) != null) {
/*     */       
/* 459 */       StringTokenizer tok = new StringTokenizer(line);
/* 460 */       if (!tok.hasMoreTokens())
/*     */         continue; 
/* 462 */       String ident = tok.nextToken();
/* 463 */       if (ident.equals("KPX")) {
/*     */         
/* 465 */         String first = tok.nextToken();
/* 466 */         String second = tok.nextToken();
/* 467 */         Integer width = Integer.valueOf((int)Float.parseFloat(tok.nextToken()));
/* 468 */         Object[] relates = this.KernPairs.get(first);
/* 469 */         if (relates == null) {
/* 470 */           this.KernPairs.put(first, new Object[] { second, width });
/*     */           continue;
/*     */         } 
/* 473 */         int n = relates.length;
/* 474 */         Object[] relates2 = new Object[n + 2];
/* 475 */         System.arraycopy(relates, 0, relates2, 0, n);
/* 476 */         relates2[n] = second;
/* 477 */         relates2[n + 1] = width;
/* 478 */         this.KernPairs.put(first, relates2);
/*     */         continue;
/*     */       } 
/* 481 */       if (ident.equals("EndKernPairs")) {
/*     */         
/* 483 */         isMetrics = false;
/*     */         break;
/*     */       } 
/*     */     } 
/* 487 */     if (isMetrics)
/* 488 */       throw new DocumentException(MessageLocalization.getComposedMessage("missing.endkernpairs.in.1", new Object[] { this.fileName })); 
/* 489 */     rf.close();
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
/*     */   public PdfStream getFullFontStream() throws DocumentException {
/* 502 */     if (this.builtinFont || !this.embedded)
/* 503 */       return null; 
/* 504 */     RandomAccessFileOrArray rf = null;
/*     */     try {
/* 506 */       String filePfb = this.fileName.substring(0, this.fileName.length() - 3) + "pfb";
/* 507 */       if (this.pfb == null) {
/* 508 */         rf = new RandomAccessFileOrArray(filePfb, true, Document.plainRandomAccess);
/*     */       } else {
/* 510 */         rf = new RandomAccessFileOrArray(this.pfb);
/* 511 */       }  int fileLength = (int)rf.length();
/* 512 */       byte[] st = new byte[fileLength - 18];
/* 513 */       int[] lengths = new int[3];
/* 514 */       int bytePtr = 0;
/* 515 */       for (int k = 0; k < 3; k++) {
/* 516 */         if (rf.read() != 128)
/* 517 */           throw new DocumentException(MessageLocalization.getComposedMessage("start.marker.missing.in.1", new Object[] { filePfb })); 
/* 518 */         if (rf.read() != PFB_TYPES[k])
/* 519 */           throw new DocumentException(MessageLocalization.getComposedMessage("incorrect.segment.type.in.1", new Object[] { filePfb })); 
/* 520 */         int size = rf.read();
/* 521 */         size += rf.read() << 8;
/* 522 */         size += rf.read() << 16;
/* 523 */         size += rf.read() << 24;
/* 524 */         lengths[k] = size;
/* 525 */         while (size != 0) {
/* 526 */           int got = rf.read(st, bytePtr, size);
/* 527 */           if (got < 0)
/* 528 */             throw new DocumentException(MessageLocalization.getComposedMessage("premature.end.in.1", new Object[] { filePfb })); 
/* 529 */           bytePtr += got;
/* 530 */           size -= got;
/*     */         } 
/*     */       } 
/* 533 */       return new BaseFont.StreamFont(st, lengths, this.compressionLevel);
/*     */     }
/* 535 */     catch (Exception e) {
/* 536 */       throw new DocumentException(e);
/*     */     } finally {
/*     */       
/* 539 */       if (rf != null) {
/*     */         try {
/* 541 */           rf.close();
/*     */         }
/* 543 */         catch (Exception exception) {}
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
/*     */   private PdfDictionary getFontDescriptor(PdfIndirectReference fontStream) {
/* 557 */     if (this.builtinFont)
/* 558 */       return null; 
/* 559 */     PdfDictionary dic = new PdfDictionary(PdfName.FONTDESCRIPTOR);
/* 560 */     dic.put(PdfName.ASCENT, new PdfNumber(this.Ascender));
/* 561 */     dic.put(PdfName.CAPHEIGHT, new PdfNumber(this.CapHeight));
/* 562 */     dic.put(PdfName.DESCENT, new PdfNumber(this.Descender));
/* 563 */     dic.put(PdfName.FONTBBOX, new PdfRectangle(this.llx, this.lly, this.urx, this.ury));
/* 564 */     dic.put(PdfName.FONTNAME, new PdfName(this.FontName));
/* 565 */     dic.put(PdfName.ITALICANGLE, new PdfNumber(this.ItalicAngle));
/* 566 */     dic.put(PdfName.STEMV, new PdfNumber(this.StdVW));
/* 567 */     if (fontStream != null)
/* 568 */       dic.put(PdfName.FONTFILE, fontStream); 
/* 569 */     int flags = 0;
/* 570 */     if (this.IsFixedPitch)
/* 571 */       flags |= 0x1; 
/* 572 */     flags |= this.fontSpecific ? 4 : 32;
/* 573 */     if (this.ItalicAngle < 0.0F)
/* 574 */       flags |= 0x40; 
/* 575 */     if (this.FontName.indexOf("Caps") >= 0 || this.FontName.endsWith("SC"))
/* 576 */       flags |= 0x20000; 
/* 577 */     if (this.Weight.equals("Bold"))
/* 578 */       flags |= 0x40000; 
/* 579 */     dic.put(PdfName.FLAGS, new PdfNumber(flags));
/*     */     
/* 581 */     return dic;
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
/*     */   private PdfDictionary getFontBaseType(PdfIndirectReference fontDescriptor, int firstChar, int lastChar, byte[] shortTag) {
/* 593 */     PdfDictionary dic = new PdfDictionary(PdfName.FONT);
/* 594 */     dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
/* 595 */     dic.put(PdfName.BASEFONT, new PdfName(this.FontName));
/* 596 */     boolean stdEncoding = (this.encoding.equals("Cp1252") || this.encoding.equals("MacRoman"));
/* 597 */     if (!this.fontSpecific || this.specialMap != null) {
/* 598 */       for (int k = firstChar; k <= lastChar; k++) {
/* 599 */         if (!this.differences[k].equals(".notdef")) {
/* 600 */           firstChar = k;
/*     */           break;
/*     */         } 
/*     */       } 
/* 604 */       if (stdEncoding) {
/* 605 */         dic.put(PdfName.ENCODING, this.encoding.equals("Cp1252") ? PdfName.WIN_ANSI_ENCODING : PdfName.MAC_ROMAN_ENCODING);
/*     */       } else {
/* 607 */         PdfDictionary enc = new PdfDictionary(PdfName.ENCODING);
/* 608 */         PdfArray dif = new PdfArray();
/* 609 */         boolean gap = true;
/* 610 */         for (int i = firstChar; i <= lastChar; i++) {
/* 611 */           if (shortTag[i] != 0) {
/* 612 */             if (gap) {
/* 613 */               dif.add(new PdfNumber(i));
/* 614 */               gap = false;
/*     */             } 
/* 616 */             dif.add(new PdfName(this.differences[i]));
/*     */           } else {
/*     */             
/* 619 */             gap = true;
/*     */           } 
/* 621 */         }  enc.put(PdfName.DIFFERENCES, dif);
/* 622 */         dic.put(PdfName.ENCODING, enc);
/*     */       } 
/*     */     } 
/* 625 */     if (this.specialMap != null || this.forceWidthsOutput || !this.builtinFont || (!this.fontSpecific && !stdEncoding)) {
/* 626 */       dic.put(PdfName.FIRSTCHAR, new PdfNumber(firstChar));
/* 627 */       dic.put(PdfName.LASTCHAR, new PdfNumber(lastChar));
/* 628 */       PdfArray wd = new PdfArray();
/* 629 */       for (int k = firstChar; k <= lastChar; k++) {
/* 630 */         if (shortTag[k] == 0) {
/* 631 */           wd.add(new PdfNumber(0));
/*     */         } else {
/* 633 */           wd.add(new PdfNumber(this.widths[k]));
/*     */         } 
/* 635 */       }  dic.put(PdfName.WIDTHS, wd);
/*     */     } 
/* 637 */     if (!this.builtinFont && fontDescriptor != null)
/* 638 */       dic.put(PdfName.FONTDESCRIPTOR, fontDescriptor); 
/* 639 */     return dic;
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
/*     */   void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params) throws DocumentException, IOException {
/* 651 */     int firstChar = ((Integer)params[0]).intValue();
/* 652 */     int lastChar = ((Integer)params[1]).intValue();
/* 653 */     byte[] shortTag = (byte[])params[2];
/* 654 */     boolean subsetp = (((Boolean)params[3]).booleanValue() && this.subset);
/* 655 */     if (!subsetp || !this.embedded) {
/* 656 */       firstChar = 0;
/* 657 */       lastChar = shortTag.length - 1;
/* 658 */       for (int k = 0; k < shortTag.length; k++)
/* 659 */         shortTag[k] = 1; 
/*     */     } 
/* 661 */     PdfIndirectReference ind_font = null;
/* 662 */     PdfObject pobj = null;
/* 663 */     PdfIndirectObject obj = null;
/* 664 */     pobj = getFullFontStream();
/* 665 */     if (pobj != null) {
/* 666 */       obj = writer.addToBody(pobj);
/* 667 */       ind_font = obj.getIndirectReference();
/*     */     } 
/* 669 */     pobj = getFontDescriptor(ind_font);
/* 670 */     if (pobj != null) {
/* 671 */       obj = writer.addToBody(pobj);
/* 672 */       ind_font = obj.getIndirectReference();
/*     */     } 
/* 674 */     pobj = getFontBaseType(ind_font, firstChar, lastChar, shortTag);
/* 675 */     writer.addToBody(pobj, ref);
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
/*     */   public float getFontDescriptor(int key, float fontSize) {
/* 688 */     switch (key) {
/*     */       case 1:
/*     */       case 9:
/* 691 */         return this.Ascender * fontSize / 1000.0F;
/*     */       case 2:
/* 693 */         return this.CapHeight * fontSize / 1000.0F;
/*     */       case 3:
/*     */       case 10:
/* 696 */         return this.Descender * fontSize / 1000.0F;
/*     */       case 4:
/* 698 */         return this.ItalicAngle;
/*     */       case 5:
/* 700 */         return this.llx * fontSize / 1000.0F;
/*     */       case 6:
/* 702 */         return this.lly * fontSize / 1000.0F;
/*     */       case 7:
/* 704 */         return this.urx * fontSize / 1000.0F;
/*     */       case 8:
/* 706 */         return this.ury * fontSize / 1000.0F;
/*     */       case 11:
/* 708 */         return 0.0F;
/*     */       case 12:
/* 710 */         return (this.urx - this.llx) * fontSize / 1000.0F;
/*     */       case 13:
/* 712 */         return this.UnderlinePosition * fontSize / 1000.0F;
/*     */       case 14:
/* 714 */         return this.UnderlineThickness * fontSize / 1000.0F;
/*     */     } 
/* 716 */     return 0.0F;
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
/*     */   public void setFontDescriptor(int key, float value) {
/* 729 */     switch (key) {
/*     */       case 1:
/*     */       case 9:
/* 732 */         this.Ascender = (int)value;
/*     */         break;
/*     */       case 3:
/*     */       case 10:
/* 736 */         this.Descender = (int)value;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPostscriptFontName() {
/* 748 */     return this.FontName;
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
/* 761 */     return new String[][] { { "", "", "", this.FullName } };
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
/* 774 */     return new String[][] { { "4", "", "", "", this.FullName } };
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
/* 787 */     return new String[][] { { "", "", "", this.FamilyName } };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasKernPairs() {
/* 795 */     return !this.KernPairs.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPostscriptFontName(String name) {
/* 805 */     this.FontName = name;
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
/*     */   public boolean setKerning(int char1, int char2, int kern) {
/* 817 */     String first = GlyphList.unicodeToName(char1);
/* 818 */     if (first == null)
/* 819 */       return false; 
/* 820 */     String second = GlyphList.unicodeToName(char2);
/* 821 */     if (second == null)
/* 822 */       return false; 
/* 823 */     Object[] obj = this.KernPairs.get(first);
/* 824 */     if (obj == null) {
/* 825 */       obj = new Object[] { second, Integer.valueOf(kern) };
/* 826 */       this.KernPairs.put(first, obj);
/* 827 */       return true;
/*     */     } 
/* 829 */     for (int k = 0; k < obj.length; k += 2) {
/* 830 */       if (second.equals(obj[k])) {
/* 831 */         obj[k + 1] = Integer.valueOf(kern);
/* 832 */         return true;
/*     */       } 
/*     */     } 
/* 835 */     int size = obj.length;
/* 836 */     Object[] obj2 = new Object[size + 2];
/* 837 */     System.arraycopy(obj, 0, obj2, 0, size);
/* 838 */     obj2[size] = second;
/* 839 */     obj2[size + 1] = Integer.valueOf(kern);
/* 840 */     this.KernPairs.put(first, obj2);
/* 841 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int[] getRawCharBBox(int c, String name) {
/*     */     Object[] metrics;
/* 847 */     if (name == null) {
/* 848 */       metrics = this.CharMetrics.get(Integer.valueOf(c));
/*     */     } else {
/*     */       
/* 851 */       if (name.equals(".notdef"))
/* 852 */         return null; 
/* 853 */       metrics = this.CharMetrics.get(name);
/*     */     } 
/* 855 */     if (metrics != null)
/* 856 */       return (int[])metrics[3]; 
/* 857 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/Type1Font.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */