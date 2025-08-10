/*      */ package com.itextpdf.text.pdf.codec;
/*      */ 
/*      */ import com.itextpdf.text.ExceptionConverter;
/*      */ import com.itextpdf.text.Image;
/*      */ import com.itextpdf.text.ImgRaw;
/*      */ import com.itextpdf.text.Utilities;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.log.Logger;
/*      */ import com.itextpdf.text.log.LoggerFactory;
/*      */ import com.itextpdf.text.pdf.ByteBuffer;
/*      */ import com.itextpdf.text.pdf.ICC_Profile;
/*      */ import com.itextpdf.text.pdf.PdfArray;
/*      */ import com.itextpdf.text.pdf.PdfDictionary;
/*      */ import com.itextpdf.text.pdf.PdfLiteral;
/*      */ import com.itextpdf.text.pdf.PdfName;
/*      */ import com.itextpdf.text.pdf.PdfNumber;
/*      */ import com.itextpdf.text.pdf.PdfObject;
/*      */ import com.itextpdf.text.pdf.PdfReader;
/*      */ import com.itextpdf.text.pdf.PdfString;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.net.URL;
/*      */ import java.util.zip.Inflater;
/*      */ import java.util.zip.InflaterInputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PngImage
/*      */ {
/*  126 */   public static final int[] PNGID = new int[] { 137, 80, 78, 71, 13, 10, 26, 10 };
/*      */ 
/*      */   
/*      */   public static final String IHDR = "IHDR";
/*      */ 
/*      */   
/*      */   public static final String PLTE = "PLTE";
/*      */ 
/*      */   
/*      */   public static final String IDAT = "IDAT";
/*      */ 
/*      */   
/*      */   public static final String IEND = "IEND";
/*      */   
/*      */   public static final String tRNS = "tRNS";
/*      */   
/*      */   public static final String pHYs = "pHYs";
/*      */   
/*      */   public static final String gAMA = "gAMA";
/*      */   
/*      */   public static final String cHRM = "cHRM";
/*      */   
/*      */   public static final String sRGB = "sRGB";
/*      */   
/*      */   public static final String iCCP = "iCCP";
/*      */   
/*      */   private static final int TRANSFERSIZE = 4096;
/*      */   
/*      */   private static final int PNG_FILTER_NONE = 0;
/*      */   
/*      */   private static final int PNG_FILTER_SUB = 1;
/*      */   
/*      */   private static final int PNG_FILTER_UP = 2;
/*      */   
/*      */   private static final int PNG_FILTER_AVERAGE = 3;
/*      */   
/*      */   private static final int PNG_FILTER_PAETH = 4;
/*      */   
/*  164 */   private static final PdfName[] intents = new PdfName[] { PdfName.PERCEPTUAL, PdfName.RELATIVECOLORIMETRIC, PdfName.SATURATION, PdfName.ABSOLUTECOLORIMETRIC };
/*      */   
/*      */   InputStream is;
/*      */   
/*      */   DataInputStream dataStream;
/*      */   int width;
/*      */   int height;
/*      */   int bitDepth;
/*      */   int colorType;
/*      */   int compressionMethod;
/*      */   int filterMethod;
/*      */   int interlaceMethod;
/*  176 */   PdfDictionary additional = new PdfDictionary();
/*      */   byte[] image;
/*      */   byte[] smask;
/*      */   byte[] trans;
/*  180 */   NewByteArrayOutputStream idat = new NewByteArrayOutputStream();
/*      */   int dpiX;
/*      */   int dpiY;
/*      */   float XYRatio;
/*      */   boolean genBWMask;
/*      */   boolean palShades;
/*  186 */   int transRedGray = -1;
/*  187 */   int transGreen = -1;
/*  188 */   int transBlue = -1;
/*      */   int inputBands;
/*      */   int bytesPerPixel;
/*      */   byte[] colorTable;
/*  192 */   float gamma = 1.0F; boolean hasCHRM = false; float xW; float yW; float xR;
/*      */   float yR;
/*      */   float xG;
/*      */   float yG;
/*      */   float xB;
/*      */   float yB;
/*      */   PdfName intent;
/*      */   ICC_Profile icc_profile;
/*      */   
/*      */   PngImage(InputStream is) {
/*  202 */     this.is = is;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Image getImage(URL url) throws IOException {
/*  211 */     InputStream is = null;
/*      */     try {
/*  213 */       is = url.openStream();
/*  214 */       Image img = getImage(is);
/*  215 */       img.setUrl(url);
/*  216 */       return img;
/*      */     } finally {
/*      */       
/*  219 */       if (is != null) {
/*  220 */         is.close();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Image getImage(InputStream is) throws IOException {
/*  231 */     PngImage png = new PngImage(is);
/*  232 */     return png.getImage();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Image getImage(String file) throws IOException {
/*  241 */     return getImage(Utilities.toURL(file));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Image getImage(byte[] data) throws IOException {
/*  250 */     ByteArrayInputStream is = new ByteArrayInputStream(data);
/*  251 */     Image img = getImage(is);
/*  252 */     img.setOriginalData(data);
/*  253 */     return img;
/*      */   }
/*      */   
/*      */   boolean checkMarker(String s) {
/*  257 */     if (s.length() != 4)
/*  258 */       return false; 
/*  259 */     for (int k = 0; k < 4; k++) {
/*  260 */       char c = s.charAt(k);
/*  261 */       if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z'))
/*  262 */         return false; 
/*      */     } 
/*  264 */     return true;
/*      */   }
/*      */   
/*      */   void readPng() throws IOException {
/*  268 */     for (int i = 0; i < PNGID.length; i++) {
/*  269 */       if (PNGID[i] != this.is.read()) {
/*  270 */         throw new IOException(MessageLocalization.getComposedMessage("file.is.not.a.valid.png", new Object[0]));
/*      */       }
/*      */     } 
/*  273 */     byte[] buffer = new byte[4096];
/*      */     while (true) {
/*  275 */       int len = getInt(this.is);
/*  276 */       String marker = getString(this.is);
/*  277 */       if (len < 0 || !checkMarker(marker))
/*  278 */         throw new IOException(MessageLocalization.getComposedMessage("corrupted.png.file", new Object[0])); 
/*  279 */       if ("IDAT".equals(marker))
/*      */       
/*  281 */       { while (len != 0) {
/*  282 */           int size = this.is.read(buffer, 0, Math.min(len, 4096));
/*  283 */           if (size < 0)
/*      */             return; 
/*  285 */           this.idat.write(buffer, 0, size);
/*  286 */           len -= size;
/*      */         }
/*      */          }
/*  289 */       else if ("tRNS".equals(marker))
/*  290 */       { switch (this.colorType) {
/*      */           case 0:
/*  292 */             if (len >= 2) {
/*  293 */               len -= 2;
/*  294 */               int gray = getWord(this.is);
/*  295 */               if (this.bitDepth == 16) {
/*  296 */                 this.transRedGray = gray; break;
/*      */               } 
/*  298 */               this.additional.put(PdfName.MASK, (PdfObject)new PdfLiteral("[" + gray + " " + gray + "]"));
/*      */             } 
/*      */             break;
/*      */           case 2:
/*  302 */             if (len >= 6) {
/*  303 */               len -= 6;
/*  304 */               int red = getWord(this.is);
/*  305 */               int green = getWord(this.is);
/*  306 */               int blue = getWord(this.is);
/*  307 */               if (this.bitDepth == 16) {
/*  308 */                 this.transRedGray = red;
/*  309 */                 this.transGreen = green;
/*  310 */                 this.transBlue = blue;
/*      */                 break;
/*      */               } 
/*  313 */               this.additional.put(PdfName.MASK, (PdfObject)new PdfLiteral("[" + red + " " + red + " " + green + " " + green + " " + blue + " " + blue + "]"));
/*      */             } 
/*      */             break;
/*      */           case 3:
/*  317 */             if (len > 0) {
/*  318 */               this.trans = new byte[len];
/*  319 */               for (int k = 0; k < len; k++)
/*  320 */                 this.trans[k] = (byte)this.is.read(); 
/*  321 */               len = 0;
/*      */             } 
/*      */             break;
/*      */         } 
/*  325 */         Utilities.skip(this.is, len); }
/*      */       
/*  327 */       else if ("IHDR".equals(marker))
/*  328 */       { this.width = getInt(this.is);
/*  329 */         this.height = getInt(this.is);
/*      */         
/*  331 */         this.bitDepth = this.is.read();
/*  332 */         this.colorType = this.is.read();
/*  333 */         this.compressionMethod = this.is.read();
/*  334 */         this.filterMethod = this.is.read();
/*  335 */         this.interlaceMethod = this.is.read(); }
/*      */       
/*  337 */       else if ("PLTE".equals(marker))
/*  338 */       { if (this.colorType == 3) {
/*  339 */           PdfArray colorspace = new PdfArray();
/*  340 */           colorspace.add((PdfObject)PdfName.INDEXED);
/*  341 */           colorspace.add(getColorspace());
/*  342 */           colorspace.add((PdfObject)new PdfNumber(len / 3 - 1));
/*  343 */           ByteBuffer colortable = new ByteBuffer();
/*  344 */           while (len-- > 0) {
/*  345 */             colortable.append_i(this.is.read());
/*      */           }
/*  347 */           colorspace.add((PdfObject)new PdfString(this.colorTable = colortable.toByteArray()));
/*  348 */           this.additional.put(PdfName.COLORSPACE, (PdfObject)colorspace);
/*      */         } else {
/*      */           
/*  351 */           Utilities.skip(this.is, len);
/*      */         }
/*      */          }
/*  354 */       else if ("pHYs".equals(marker))
/*  355 */       { int dx = getInt(this.is);
/*  356 */         int dy = getInt(this.is);
/*  357 */         int unit = this.is.read();
/*  358 */         if (unit == 1) {
/*  359 */           this.dpiX = (int)(dx * 0.0254F + 0.5F);
/*  360 */           this.dpiY = (int)(dy * 0.0254F + 0.5F);
/*      */         
/*      */         }
/*  363 */         else if (dy != 0) {
/*  364 */           this.XYRatio = dx / dy;
/*      */         }
/*      */          }
/*  367 */       else if ("cHRM".equals(marker))
/*  368 */       { this.xW = getInt(this.is) / 100000.0F;
/*  369 */         this.yW = getInt(this.is) / 100000.0F;
/*  370 */         this.xR = getInt(this.is) / 100000.0F;
/*  371 */         this.yR = getInt(this.is) / 100000.0F;
/*  372 */         this.xG = getInt(this.is) / 100000.0F;
/*  373 */         this.yG = getInt(this.is) / 100000.0F;
/*  374 */         this.xB = getInt(this.is) / 100000.0F;
/*  375 */         this.yB = getInt(this.is) / 100000.0F;
/*  376 */         this.hasCHRM = (Math.abs(this.xW) >= 1.0E-4F && Math.abs(this.yW) >= 1.0E-4F && Math.abs(this.xR) >= 1.0E-4F && Math.abs(this.yR) >= 1.0E-4F && Math.abs(this.xG) >= 1.0E-4F && Math.abs(this.yG) >= 1.0E-4F && Math.abs(this.xB) >= 1.0E-4F && Math.abs(this.yB) >= 1.0E-4F); }
/*      */       
/*  378 */       else if ("sRGB".equals(marker))
/*  379 */       { int ri = this.is.read();
/*  380 */         this.intent = intents[ri];
/*  381 */         this.gamma = 2.2F;
/*  382 */         this.xW = 0.3127F;
/*  383 */         this.yW = 0.329F;
/*  384 */         this.xR = 0.64F;
/*  385 */         this.yR = 0.33F;
/*  386 */         this.xG = 0.3F;
/*  387 */         this.yG = 0.6F;
/*  388 */         this.xB = 0.15F;
/*  389 */         this.yB = 0.06F;
/*  390 */         this.hasCHRM = true; }
/*      */       
/*  392 */       else if ("gAMA".equals(marker))
/*  393 */       { int gm = getInt(this.is);
/*  394 */         if (gm != 0) {
/*  395 */           this.gamma = 100000.0F / gm;
/*  396 */           if (!this.hasCHRM) {
/*  397 */             this.xW = 0.3127F;
/*  398 */             this.yW = 0.329F;
/*  399 */             this.xR = 0.64F;
/*  400 */             this.yR = 0.33F;
/*  401 */             this.xG = 0.3F;
/*  402 */             this.yG = 0.6F;
/*  403 */             this.xB = 0.15F;
/*  404 */             this.yB = 0.06F;
/*  405 */             this.hasCHRM = true;
/*      */           }
/*      */         
/*      */         }  }
/*  409 */       else if ("iCCP".equals(marker))
/*      */       { while (true)
/*  411 */         { len--;
/*  412 */           if (this.is.read() == 0) {
/*  413 */             this.is.read();
/*  414 */             len--;
/*  415 */             byte[] icccom = new byte[len];
/*  416 */             int p = 0;
/*  417 */             while (len > 0) {
/*  418 */               int r = this.is.read(icccom, p, len);
/*  419 */               if (r < 0)
/*  420 */                 throw new IOException(MessageLocalization.getComposedMessage("premature.end.of.file", new Object[0])); 
/*  421 */               p += r;
/*  422 */               len -= r;
/*      */             } 
/*  424 */             byte[] iccp = PdfReader.FlateDecode(icccom, true);
/*  425 */             icccom = null;
/*      */             try {
/*  427 */               this.icc_profile = ICC_Profile.getInstance(iccp);
/*      */               break;
/*  429 */             } catch (RuntimeException e) {
/*  430 */               this.icc_profile = null;
/*      */             } 
/*      */           } else {
/*      */             continue;
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  439 */           Utilities.skip(this.is, 4); }  } else { if ("IEND".equals(marker)) break;  Utilities.skip(this.is, len); }  Utilities.skip(this.is, 4);
/*      */     } 
/*      */   }
/*      */   
/*      */   PdfObject getColorspace() {
/*  444 */     if (this.icc_profile != null) {
/*  445 */       if ((this.colorType & 0x2) == 0) {
/*  446 */         return (PdfObject)PdfName.DEVICEGRAY;
/*      */       }
/*  448 */       return (PdfObject)PdfName.DEVICERGB;
/*      */     } 
/*  450 */     if (this.gamma == 1.0F && !this.hasCHRM) {
/*  451 */       if ((this.colorType & 0x2) == 0) {
/*  452 */         return (PdfObject)PdfName.DEVICEGRAY;
/*      */       }
/*  454 */       return (PdfObject)PdfName.DEVICERGB;
/*      */     } 
/*      */     
/*  457 */     PdfArray array = new PdfArray();
/*  458 */     PdfDictionary dic = new PdfDictionary();
/*  459 */     if ((this.colorType & 0x2) == 0) {
/*  460 */       if (this.gamma == 1.0F)
/*  461 */         return (PdfObject)PdfName.DEVICEGRAY; 
/*  462 */       array.add((PdfObject)PdfName.CALGRAY);
/*  463 */       dic.put(PdfName.GAMMA, (PdfObject)new PdfNumber(this.gamma));
/*  464 */       dic.put(PdfName.WHITEPOINT, (PdfObject)new PdfLiteral("[1 1 1]"));
/*  465 */       array.add((PdfObject)dic);
/*      */     } else {
/*      */       PdfArray pdfArray;
/*  468 */       PdfLiteral pdfLiteral = new PdfLiteral("[1 1 1]");
/*  469 */       array.add((PdfObject)PdfName.CALRGB);
/*  470 */       if (this.gamma != 1.0F) {
/*  471 */         PdfArray gm = new PdfArray();
/*  472 */         PdfNumber n = new PdfNumber(this.gamma);
/*  473 */         gm.add((PdfObject)n);
/*  474 */         gm.add((PdfObject)n);
/*  475 */         gm.add((PdfObject)n);
/*  476 */         dic.put(PdfName.GAMMA, (PdfObject)gm);
/*      */       } 
/*  478 */       if (this.hasCHRM) {
/*  479 */         float z = this.yW * ((this.xG - this.xB) * this.yR - (this.xR - this.xB) * this.yG + (this.xR - this.xG) * this.yB);
/*  480 */         float YA = this.yR * ((this.xG - this.xB) * this.yW - (this.xW - this.xB) * this.yG + (this.xW - this.xG) * this.yB) / z;
/*  481 */         float XA = YA * this.xR / this.yR;
/*  482 */         float ZA = YA * ((1.0F - this.xR) / this.yR - 1.0F);
/*  483 */         float YB = -this.yG * ((this.xR - this.xB) * this.yW - (this.xW - this.xB) * this.yR + (this.xW - this.xR) * this.yB) / z;
/*  484 */         float XB = YB * this.xG / this.yG;
/*  485 */         float ZB = YB * ((1.0F - this.xG) / this.yG - 1.0F);
/*  486 */         float YC = this.yB * ((this.xR - this.xG) * this.yW - (this.xW - this.xG) * this.yW + (this.xW - this.xR) * this.yG) / z;
/*  487 */         float XC = YC * this.xB / this.yB;
/*  488 */         float ZC = YC * ((1.0F - this.xB) / this.yB - 1.0F);
/*  489 */         float XW = XA + XB + XC;
/*  490 */         float YW = 1.0F;
/*  491 */         float ZW = ZA + ZB + ZC;
/*  492 */         PdfArray wpa = new PdfArray();
/*  493 */         wpa.add((PdfObject)new PdfNumber(XW));
/*  494 */         wpa.add((PdfObject)new PdfNumber(YW));
/*  495 */         wpa.add((PdfObject)new PdfNumber(ZW));
/*  496 */         pdfArray = wpa;
/*  497 */         PdfArray matrix = new PdfArray();
/*  498 */         matrix.add((PdfObject)new PdfNumber(XA));
/*  499 */         matrix.add((PdfObject)new PdfNumber(YA));
/*  500 */         matrix.add((PdfObject)new PdfNumber(ZA));
/*  501 */         matrix.add((PdfObject)new PdfNumber(XB));
/*  502 */         matrix.add((PdfObject)new PdfNumber(YB));
/*  503 */         matrix.add((PdfObject)new PdfNumber(ZB));
/*  504 */         matrix.add((PdfObject)new PdfNumber(XC));
/*  505 */         matrix.add((PdfObject)new PdfNumber(YC));
/*  506 */         matrix.add((PdfObject)new PdfNumber(ZC));
/*  507 */         dic.put(PdfName.MATRIX, (PdfObject)matrix);
/*      */       } 
/*  509 */       dic.put(PdfName.WHITEPOINT, (PdfObject)pdfArray);
/*  510 */       array.add((PdfObject)dic);
/*      */     } 
/*  512 */     return (PdfObject)array;
/*      */   }
/*      */ 
/*      */   
/*      */   Image getImage() throws IOException {
/*  517 */     readPng();
/*  518 */     checkIccProfile(); try {
/*      */       ImgRaw imgRaw;
/*  520 */       int pal0 = 0;
/*  521 */       int palIdx = 0;
/*  522 */       this.palShades = false;
/*  523 */       if (this.trans != null) {
/*  524 */         for (int k = 0; k < this.trans.length; k++) {
/*  525 */           int n = this.trans[k] & 0xFF;
/*  526 */           if (n == 0) {
/*  527 */             pal0++;
/*  528 */             palIdx = k;
/*      */           } 
/*  530 */           if (n != 0 && n != 255) {
/*  531 */             this.palShades = true;
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       }
/*  536 */       if ((this.colorType & 0x4) != 0)
/*  537 */         this.palShades = true; 
/*  538 */       this.genBWMask = (!this.palShades && (pal0 > 1 || this.transRedGray >= 0));
/*  539 */       if (!this.palShades && !this.genBWMask && pal0 == 1) {
/*  540 */         this.additional.put(PdfName.MASK, (PdfObject)new PdfLiteral("[" + palIdx + " " + palIdx + "]"));
/*      */       }
/*  542 */       boolean needDecode = (this.interlaceMethod == 1 || this.bitDepth == 16 || (this.colorType & 0x4) != 0 || this.palShades || this.genBWMask);
/*  543 */       switch (this.colorType) {
/*      */         case 0:
/*  545 */           this.inputBands = 1;
/*      */           break;
/*      */         case 2:
/*  548 */           this.inputBands = 3;
/*      */           break;
/*      */         case 3:
/*  551 */           this.inputBands = 1;
/*      */           break;
/*      */         case 4:
/*  554 */           this.inputBands = 2;
/*      */           break;
/*      */         case 6:
/*  557 */           this.inputBands = 4;
/*      */           break;
/*      */       } 
/*  560 */       if (needDecode)
/*  561 */         decodeIdat(); 
/*  562 */       int components = this.inputBands;
/*  563 */       if ((this.colorType & 0x4) != 0)
/*  564 */         components--; 
/*  565 */       int bpc = this.bitDepth;
/*  566 */       if (bpc == 16) {
/*  567 */         bpc = 8;
/*      */       }
/*  569 */       if (this.image != null) {
/*  570 */         if (this.colorType == 3) {
/*  571 */           imgRaw = new ImgRaw(this.width, this.height, components, bpc, this.image);
/*      */         } else {
/*  573 */           Image img = Image.getInstance(this.width, this.height, components, bpc, this.image);
/*      */         } 
/*      */       } else {
/*  576 */         imgRaw = new ImgRaw(this.width, this.height, components, bpc, this.idat.toByteArray());
/*  577 */         imgRaw.setDeflated(true);
/*  578 */         PdfDictionary decodeparms = new PdfDictionary();
/*  579 */         decodeparms.put(PdfName.BITSPERCOMPONENT, (PdfObject)new PdfNumber(this.bitDepth));
/*  580 */         decodeparms.put(PdfName.PREDICTOR, (PdfObject)new PdfNumber(15));
/*  581 */         decodeparms.put(PdfName.COLUMNS, (PdfObject)new PdfNumber(this.width));
/*  582 */         decodeparms.put(PdfName.COLORS, (PdfObject)new PdfNumber((this.colorType == 3 || (this.colorType & 0x2) == 0) ? 1 : 3));
/*  583 */         this.additional.put(PdfName.DECODEPARMS, (PdfObject)decodeparms);
/*      */       } 
/*  585 */       if (this.additional.get(PdfName.COLORSPACE) == null)
/*  586 */         this.additional.put(PdfName.COLORSPACE, getColorspace()); 
/*  587 */       if (this.intent != null)
/*  588 */         this.additional.put(PdfName.INTENT, (PdfObject)this.intent); 
/*  589 */       if (this.additional.size() > 0)
/*  590 */         imgRaw.setAdditional(this.additional); 
/*  591 */       if (this.icc_profile != null)
/*  592 */         imgRaw.tagICC(this.icc_profile); 
/*  593 */       if (this.palShades) {
/*  594 */         Image im2 = Image.getInstance(this.width, this.height, 1, 8, this.smask);
/*  595 */         im2.makeMask();
/*  596 */         imgRaw.setImageMask(im2);
/*      */       } 
/*  598 */       if (this.genBWMask) {
/*  599 */         Image im2 = Image.getInstance(this.width, this.height, 1, 1, this.smask);
/*  600 */         im2.makeMask();
/*  601 */         imgRaw.setImageMask(im2);
/*      */       } 
/*  603 */       imgRaw.setDpi(this.dpiX, this.dpiY);
/*  604 */       imgRaw.setXYRatio(this.XYRatio);
/*  605 */       imgRaw.setOriginalType(2);
/*  606 */       return (Image)imgRaw;
/*      */     }
/*  608 */     catch (Exception e) {
/*  609 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   void decodeIdat() {
/*  614 */     int nbitDepth = this.bitDepth;
/*  615 */     if (nbitDepth == 16)
/*  616 */       nbitDepth = 8; 
/*  617 */     int size = -1;
/*  618 */     this.bytesPerPixel = (this.bitDepth == 16) ? 2 : 1;
/*  619 */     switch (this.colorType) {
/*      */       case 0:
/*  621 */         size = (nbitDepth * this.width + 7) / 8 * this.height;
/*      */         break;
/*      */       case 2:
/*  624 */         size = this.width * 3 * this.height;
/*  625 */         this.bytesPerPixel *= 3;
/*      */         break;
/*      */       case 3:
/*  628 */         if (this.interlaceMethod == 1)
/*  629 */           size = (nbitDepth * this.width + 7) / 8 * this.height; 
/*  630 */         this.bytesPerPixel = 1;
/*      */         break;
/*      */       case 4:
/*  633 */         size = this.width * this.height;
/*  634 */         this.bytesPerPixel *= 2;
/*      */         break;
/*      */       case 6:
/*  637 */         size = this.width * 3 * this.height;
/*  638 */         this.bytesPerPixel *= 4;
/*      */         break;
/*      */     } 
/*  641 */     if (size >= 0)
/*  642 */       this.image = new byte[size]; 
/*  643 */     if (this.palShades) {
/*  644 */       this.smask = new byte[this.width * this.height];
/*  645 */     } else if (this.genBWMask) {
/*  646 */       this.smask = new byte[(this.width + 7) / 8 * this.height];
/*  647 */     }  ByteArrayInputStream bai = new ByteArrayInputStream(this.idat.getBuf(), 0, this.idat.size());
/*  648 */     InputStream infStream = new InflaterInputStream(bai, new Inflater());
/*  649 */     this.dataStream = new DataInputStream(infStream);
/*      */     
/*  651 */     if (this.interlaceMethod != 1) {
/*  652 */       decodePass(0, 0, 1, 1, this.width, this.height);
/*      */     } else {
/*      */       
/*  655 */       decodePass(0, 0, 8, 8, (this.width + 7) / 8, (this.height + 7) / 8);
/*  656 */       decodePass(4, 0, 8, 8, (this.width + 3) / 8, (this.height + 7) / 8);
/*  657 */       decodePass(0, 4, 4, 8, (this.width + 3) / 4, (this.height + 3) / 8);
/*  658 */       decodePass(2, 0, 4, 4, (this.width + 1) / 4, (this.height + 3) / 4);
/*  659 */       decodePass(0, 2, 2, 4, (this.width + 1) / 2, (this.height + 1) / 4);
/*  660 */       decodePass(1, 0, 2, 2, this.width / 2, (this.height + 1) / 2);
/*  661 */       decodePass(0, 1, 1, 2, this.width, this.height / 2);
/*      */     } 
/*      */     
/*      */     try {
/*  665 */       this.dataStream.close();
/*  666 */     } catch (IOException e) {
/*  667 */       Logger logger = LoggerFactory.getLogger(getClass());
/*  668 */       logger.warn("Datastream of PngImage#decodeIdat didn't close properly.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void decodePass(int xOffset, int yOffset, int xStep, int yStep, int passWidth, int passHeight) {
/*  675 */     if (passWidth == 0 || passHeight == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  679 */     int bytesPerRow = (this.inputBands * passWidth * this.bitDepth + 7) / 8;
/*  680 */     byte[] curr = new byte[bytesPerRow];
/*  681 */     byte[] prior = new byte[bytesPerRow];
/*      */ 
/*      */ 
/*      */     
/*  685 */     int srcY = 0, dstY = yOffset;
/*  686 */     for (; srcY < passHeight; 
/*  687 */       srcY++, dstY += yStep) {
/*      */       
/*  689 */       int filter = 0;
/*      */       try {
/*  691 */         filter = this.dataStream.read();
/*  692 */         this.dataStream.readFully(curr, 0, bytesPerRow);
/*  693 */       } catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */       
/*  697 */       switch (filter) {
/*      */         case 0:
/*      */           break;
/*      */         case 1:
/*  701 */           decodeSubFilter(curr, bytesPerRow, this.bytesPerPixel);
/*      */           break;
/*      */         case 2:
/*  704 */           decodeUpFilter(curr, prior, bytesPerRow);
/*      */           break;
/*      */         case 3:
/*  707 */           decodeAverageFilter(curr, prior, bytesPerRow, this.bytesPerPixel);
/*      */           break;
/*      */         case 4:
/*  710 */           decodePaethFilter(curr, prior, bytesPerRow, this.bytesPerPixel);
/*      */           break;
/*      */         
/*      */         default:
/*  714 */           throw new RuntimeException(MessageLocalization.getComposedMessage("png.filter.unknown", new Object[0]));
/*      */       } 
/*      */       
/*  717 */       processPixels(curr, xOffset, xStep, dstY, passWidth);
/*      */ 
/*      */       
/*  720 */       byte[] tmp = prior;
/*  721 */       prior = curr;
/*  722 */       curr = tmp;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void processPixels(byte[] curr, int xOffset, int step, int y, int width) {
/*  729 */     int[] out = getPixel(curr);
/*  730 */     int sizes = 0;
/*  731 */     switch (this.colorType) {
/*      */       case 0:
/*      */       case 3:
/*      */       case 4:
/*  735 */         sizes = 1;
/*      */         break;
/*      */       case 2:
/*      */       case 6:
/*  739 */         sizes = 3;
/*      */         break;
/*      */     } 
/*  742 */     if (this.image != null) {
/*  743 */       int dstX = xOffset;
/*  744 */       int yStride = (sizes * this.width * ((this.bitDepth == 16) ? 8 : this.bitDepth) + 7) / 8;
/*  745 */       for (int srcX = 0; srcX < width; srcX++) {
/*  746 */         setPixel(this.image, out, this.inputBands * srcX, sizes, dstX, y, this.bitDepth, yStride);
/*  747 */         dstX += step;
/*      */       } 
/*      */     } 
/*  750 */     if (this.palShades) {
/*  751 */       if ((this.colorType & 0x4) != 0) {
/*  752 */         if (this.bitDepth == 16)
/*  753 */           for (int k = 0; k < width; k++) {
/*  754 */             out[k * this.inputBands + sizes] = out[k * this.inputBands + sizes] >>> 8;
/*      */           } 
/*  756 */         int yStride = this.width;
/*  757 */         int dstX = xOffset;
/*  758 */         for (int srcX = 0; srcX < width; srcX++) {
/*  759 */           setPixel(this.smask, out, this.inputBands * srcX + sizes, 1, dstX, y, 8, yStride);
/*  760 */           dstX += step;
/*      */         } 
/*      */       } else {
/*      */         
/*  764 */         int yStride = this.width;
/*  765 */         int[] v = new int[1];
/*  766 */         int dstX = xOffset;
/*  767 */         for (int srcX = 0; srcX < width; srcX++) {
/*  768 */           int idx = out[srcX];
/*  769 */           if (idx < this.trans.length) {
/*  770 */             v[0] = this.trans[idx];
/*      */           } else {
/*  772 */             v[0] = 255;
/*  773 */           }  setPixel(this.smask, v, 0, 1, dstX, y, 8, yStride);
/*  774 */           dstX += step;
/*      */         }
/*      */       
/*      */       } 
/*  778 */     } else if (this.genBWMask) {
/*  779 */       int srcX; int dstX; int yStride; int[] v; switch (this.colorType) {
/*      */         case 3:
/*  781 */           yStride = (this.width + 7) / 8;
/*  782 */           v = new int[1];
/*  783 */           dstX = xOffset;
/*  784 */           for (srcX = 0; srcX < width; srcX++) {
/*  785 */             int idx = out[srcX];
/*  786 */             v[0] = (idx < this.trans.length && this.trans[idx] == 0) ? 1 : 0;
/*  787 */             setPixel(this.smask, v, 0, 1, dstX, y, 1, yStride);
/*  788 */             dstX += step;
/*      */           } 
/*      */           break;
/*      */         
/*      */         case 0:
/*  793 */           yStride = (this.width + 7) / 8;
/*  794 */           v = new int[1];
/*  795 */           dstX = xOffset;
/*  796 */           for (srcX = 0; srcX < width; srcX++) {
/*  797 */             int g = out[srcX];
/*  798 */             v[0] = (g == this.transRedGray) ? 1 : 0;
/*  799 */             setPixel(this.smask, v, 0, 1, dstX, y, 1, yStride);
/*  800 */             dstX += step;
/*      */           } 
/*      */           break;
/*      */         
/*      */         case 2:
/*  805 */           yStride = (this.width + 7) / 8;
/*  806 */           v = new int[1];
/*  807 */           dstX = xOffset;
/*  808 */           for (srcX = 0; srcX < width; srcX++) {
/*  809 */             int markRed = this.inputBands * srcX;
/*  810 */             v[0] = (out[markRed] == this.transRedGray && out[markRed + 1] == this.transGreen && out[markRed + 2] == this.transBlue) ? 1 : 0;
/*      */             
/*  812 */             setPixel(this.smask, v, 0, 1, dstX, y, 1, yStride);
/*  813 */             dstX += step;
/*      */           } 
/*      */           break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static int getPixel(byte[] image, int x, int y, int bitDepth, int bytesPerRow) {
/*  822 */     if (bitDepth == 8) {
/*  823 */       int i = bytesPerRow * y + x;
/*  824 */       return image[i] & 0xFF;
/*      */     } 
/*      */     
/*  827 */     int pos = bytesPerRow * y + x / 8 / bitDepth;
/*  828 */     int v = image[pos] >> 8 - bitDepth * x % 8 / bitDepth - bitDepth;
/*  829 */     return v & (1 << bitDepth) - 1;
/*      */   }
/*      */ 
/*      */   
/*      */   static void setPixel(byte[] image, int[] data, int offset, int size, int x, int y, int bitDepth, int bytesPerRow) {
/*  834 */     if (bitDepth == 8) {
/*  835 */       int pos = bytesPerRow * y + size * x;
/*  836 */       for (int k = 0; k < size; k++) {
/*  837 */         image[pos + k] = (byte)data[k + offset];
/*      */       }
/*  839 */     } else if (bitDepth == 16) {
/*  840 */       int pos = bytesPerRow * y + size * x;
/*  841 */       for (int k = 0; k < size; k++) {
/*  842 */         image[pos + k] = (byte)(data[k + offset] >>> 8);
/*      */       }
/*      */     } else {
/*  845 */       int pos = bytesPerRow * y + x / 8 / bitDepth;
/*  846 */       int v = data[offset] << 8 - bitDepth * x % 8 / bitDepth - bitDepth;
/*  847 */       image[pos] = (byte)(image[pos] | v);
/*      */     } 
/*      */   }
/*      */   int[] getPixel(byte[] curr) {
/*      */     int k;
/*  852 */     switch (this.bitDepth) {
/*      */       case 8:
/*  854 */         out = new int[curr.length];
/*  855 */         for (k = 0; k < out.length; k++)
/*  856 */           out[k] = curr[k] & 0xFF; 
/*  857 */         return out;
/*      */       
/*      */       case 16:
/*  860 */         out = new int[curr.length / 2];
/*  861 */         for (k = 0; k < out.length; k++)
/*  862 */           out[k] = ((curr[k * 2] & 0xFF) << 8) + (curr[k * 2 + 1] & 0xFF); 
/*  863 */         return out;
/*      */     } 
/*      */     
/*  866 */     int[] out = new int[curr.length * 8 / this.bitDepth];
/*  867 */     int idx = 0;
/*  868 */     int passes = 8 / this.bitDepth;
/*  869 */     int mask = (1 << this.bitDepth) - 1;
/*  870 */     for (int i = 0; i < curr.length; i++) {
/*  871 */       for (int j = passes - 1; j >= 0; j--) {
/*  872 */         out[idx++] = curr[i] >>> this.bitDepth * j & mask;
/*      */       }
/*      */     } 
/*  875 */     return out;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int getExpectedIccNumberOfComponents() {
/*  881 */     if (this.colorType == 0 || this.colorType == 4) {
/*  882 */       return 1;
/*      */     }
/*  884 */     return 3;
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkIccProfile() {
/*  889 */     if (this.icc_profile != null && this.icc_profile.getNumComponents() != getExpectedIccNumberOfComponents()) {
/*  890 */       LoggerFactory.getLogger(getClass()).warn(MessageLocalization.getComposedMessage("unexpected.color.space.in.embedded.icc.profile", new Object[0]));
/*  891 */       this.icc_profile = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void decodeSubFilter(byte[] curr, int count, int bpp) {
/*  896 */     for (int i = bpp; i < count; i++) {
/*      */ 
/*      */       
/*  899 */       int val = curr[i] & 0xFF;
/*  900 */       val += curr[i - bpp] & 0xFF;
/*      */       
/*  902 */       curr[i] = (byte)val;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void decodeUpFilter(byte[] curr, byte[] prev, int count) {
/*  908 */     for (int i = 0; i < count; i++) {
/*  909 */       int raw = curr[i] & 0xFF;
/*  910 */       int prior = prev[i] & 0xFF;
/*      */       
/*  912 */       curr[i] = (byte)(raw + prior);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void decodeAverageFilter(byte[] curr, byte[] prev, int count, int bpp) {
/*      */     int i;
/*  920 */     for (i = 0; i < bpp; i++) {
/*  921 */       int raw = curr[i] & 0xFF;
/*  922 */       int priorRow = prev[i] & 0xFF;
/*      */       
/*  924 */       curr[i] = (byte)(raw + priorRow / 2);
/*      */     } 
/*      */     
/*  927 */     for (i = bpp; i < count; i++) {
/*  928 */       int raw = curr[i] & 0xFF;
/*  929 */       int priorPixel = curr[i - bpp] & 0xFF;
/*  930 */       int priorRow = prev[i] & 0xFF;
/*      */       
/*  932 */       curr[i] = (byte)(raw + (priorPixel + priorRow) / 2);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static int paethPredictor(int a, int b, int c) {
/*  937 */     int p = a + b - c;
/*  938 */     int pa = Math.abs(p - a);
/*  939 */     int pb = Math.abs(p - b);
/*  940 */     int pc = Math.abs(p - c);
/*      */     
/*  942 */     if (pa <= pb && pa <= pc)
/*  943 */       return a; 
/*  944 */     if (pb <= pc) {
/*  945 */       return b;
/*      */     }
/*  947 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void decodePaethFilter(byte[] curr, byte[] prev, int count, int bpp) {
/*      */     int i;
/*  955 */     for (i = 0; i < bpp; i++) {
/*  956 */       int raw = curr[i] & 0xFF;
/*  957 */       int priorRow = prev[i] & 0xFF;
/*      */       
/*  959 */       curr[i] = (byte)(raw + priorRow);
/*      */     } 
/*      */     
/*  962 */     for (i = bpp; i < count; i++) {
/*  963 */       int raw = curr[i] & 0xFF;
/*  964 */       int priorPixel = curr[i - bpp] & 0xFF;
/*  965 */       int priorRow = prev[i] & 0xFF;
/*  966 */       int priorRowPixel = prev[i - bpp] & 0xFF;
/*      */       
/*  968 */       curr[i] = (byte)(raw + paethPredictor(priorPixel, priorRow, priorRowPixel));
/*      */     } 
/*      */   }
/*      */   
/*      */   static class NewByteArrayOutputStream
/*      */     extends ByteArrayOutputStream
/*      */   {
/*      */     public byte[] getBuf() {
/*  976 */       return this.buf;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int getInt(InputStream is) throws IOException {
/*  988 */     return (is.read() << 24) + (is.read() << 16) + (is.read() << 8) + is.read();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int getWord(InputStream is) throws IOException {
/*  999 */     return (is.read() << 8) + is.read();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String getString(InputStream is) throws IOException {
/* 1010 */     StringBuffer buf = new StringBuffer();
/* 1011 */     for (int i = 0; i < 4; i++) {
/* 1012 */       buf.append((char)is.read());
/*      */     }
/* 1014 */     return buf.toString();
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/PngImage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */