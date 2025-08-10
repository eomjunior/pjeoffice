/*      */ package com.itextpdf.text.pdf.codec;
/*      */ 
/*      */ import com.itextpdf.text.BadElementException;
/*      */ import com.itextpdf.text.ExceptionConverter;
/*      */ import com.itextpdf.text.Image;
/*      */ import com.itextpdf.text.ImgRaw;
/*      */ import com.itextpdf.text.Utilities;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.pdf.PdfArray;
/*      */ import com.itextpdf.text.pdf.PdfDictionary;
/*      */ import com.itextpdf.text.pdf.PdfName;
/*      */ import com.itextpdf.text.pdf.PdfNumber;
/*      */ import com.itextpdf.text.pdf.PdfObject;
/*      */ import com.itextpdf.text.pdf.PdfString;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.net.URL;
/*      */ import java.util.HashMap;
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
/*      */ public class BmpImage
/*      */ {
/*      */   private InputStream inputStream;
/*      */   private long bitmapFileSize;
/*      */   private long bitmapOffset;
/*      */   private long compression;
/*      */   private long imageSize;
/*      */   private byte[] palette;
/*      */   private int imageType;
/*      */   private int numBands;
/*      */   private boolean isBottomUp;
/*      */   private int bitsPerPixel;
/*      */   private int redMask;
/*      */   private int greenMask;
/*      */   private int blueMask;
/*      */   private int alphaMask;
/*  129 */   public HashMap<String, Object> properties = new HashMap<String, Object>();
/*      */   
/*      */   private long xPelsPerMeter;
/*      */   
/*      */   private long yPelsPerMeter;
/*      */   
/*      */   private static final int VERSION_2_1_BIT = 0;
/*      */   
/*      */   private static final int VERSION_2_4_BIT = 1;
/*      */   
/*      */   private static final int VERSION_2_8_BIT = 2;
/*      */   
/*      */   private static final int VERSION_2_24_BIT = 3;
/*      */   
/*      */   private static final int VERSION_3_1_BIT = 4;
/*      */   
/*      */   private static final int VERSION_3_4_BIT = 5;
/*      */   
/*      */   private static final int VERSION_3_8_BIT = 6;
/*      */   private static final int VERSION_3_24_BIT = 7;
/*      */   private static final int VERSION_3_NT_16_BIT = 8;
/*      */   private static final int VERSION_3_NT_32_BIT = 9;
/*      */   private static final int VERSION_4_1_BIT = 10;
/*      */   private static final int VERSION_4_4_BIT = 11;
/*      */   private static final int VERSION_4_8_BIT = 12;
/*      */   private static final int VERSION_4_16_BIT = 13;
/*      */   private static final int VERSION_4_24_BIT = 14;
/*      */   private static final int VERSION_4_32_BIT = 15;
/*      */   private static final int LCS_CALIBRATED_RGB = 0;
/*      */   private static final int LCS_sRGB = 1;
/*      */   private static final int LCS_CMYK = 2;
/*      */   private static final int BI_RGB = 0;
/*      */   private static final int BI_RLE8 = 1;
/*      */   private static final int BI_RLE4 = 2;
/*      */   private static final int BI_BITFIELDS = 3;
/*      */   int width;
/*      */   int height;
/*      */   
/*      */   BmpImage(InputStream is, boolean noHeader, int size) throws IOException {
/*  168 */     this.bitmapFileSize = size;
/*  169 */     this.bitmapOffset = 0L;
/*  170 */     process(is, noHeader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Image getImage(URL url) throws IOException {
/*  179 */     InputStream is = null;
/*      */     try {
/*  181 */       is = url.openStream();
/*  182 */       Image img = getImage(is);
/*  183 */       img.setUrl(url);
/*  184 */       return img;
/*      */     } finally {
/*      */       
/*  187 */       if (is != null) {
/*  188 */         is.close();
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
/*  199 */     return getImage(is, false, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Image getImage(InputStream is, boolean noHeader, int size) throws IOException {
/*  211 */     BmpImage bmp = new BmpImage(is, noHeader, size);
/*      */     try {
/*  213 */       Image img = bmp.getImage();
/*  214 */       img.setDpi((int)(bmp.xPelsPerMeter * 0.0254D + 0.5D), (int)(bmp.yPelsPerMeter * 0.0254D + 0.5D));
/*  215 */       img.setOriginalType(4);
/*  216 */       return img;
/*      */     }
/*  218 */     catch (BadElementException be) {
/*  219 */       throw new ExceptionConverter(be);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Image getImage(String file) throws IOException {
/*  229 */     return getImage(Utilities.toURL(file));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Image getImage(byte[] data) throws IOException {
/*  238 */     ByteArrayInputStream is = new ByteArrayInputStream(data);
/*  239 */     Image img = getImage(is);
/*  240 */     img.setOriginalData(data);
/*  241 */     return img;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void process(InputStream stream, boolean noHeader) throws IOException {
/*  246 */     if (noHeader || stream instanceof BufferedInputStream) {
/*  247 */       this.inputStream = stream;
/*      */     } else {
/*  249 */       this.inputStream = new BufferedInputStream(stream);
/*      */     } 
/*  251 */     if (!noHeader) {
/*      */       
/*  253 */       if (readUnsignedByte(this.inputStream) != 66 || 
/*  254 */         readUnsignedByte(this.inputStream) != 77) {
/*  255 */         throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.magic.value.for.bmp.file", new Object[0]));
/*      */       }
/*      */ 
/*      */       
/*  259 */       this.bitmapFileSize = readDWord(this.inputStream);
/*      */ 
/*      */       
/*  262 */       readWord(this.inputStream);
/*  263 */       readWord(this.inputStream);
/*      */ 
/*      */       
/*  266 */       this.bitmapOffset = readDWord(this.inputStream);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  271 */     long size = readDWord(this.inputStream);
/*      */     
/*  273 */     if (size == 12L) {
/*  274 */       this.width = readWord(this.inputStream);
/*  275 */       this.height = readWord(this.inputStream);
/*      */     } else {
/*  277 */       this.width = readLong(this.inputStream);
/*  278 */       this.height = readLong(this.inputStream);
/*      */     } 
/*      */     
/*  281 */     int planes = readWord(this.inputStream);
/*  282 */     this.bitsPerPixel = readWord(this.inputStream);
/*      */     
/*  284 */     this.properties.put("color_planes", Integer.valueOf(planes));
/*  285 */     this.properties.put("bits_per_pixel", Integer.valueOf(this.bitsPerPixel));
/*      */ 
/*      */ 
/*      */     
/*  289 */     this.numBands = 3;
/*  290 */     if (this.bitmapOffset == 0L)
/*  291 */       this.bitmapOffset = size; 
/*  292 */     if (size == 12L) {
/*      */       
/*  294 */       this.properties.put("bmp_version", "BMP v. 2.x");
/*      */ 
/*      */       
/*  297 */       if (this.bitsPerPixel == 1) {
/*  298 */         this.imageType = 0;
/*  299 */       } else if (this.bitsPerPixel == 4) {
/*  300 */         this.imageType = 1;
/*  301 */       } else if (this.bitsPerPixel == 8) {
/*  302 */         this.imageType = 2;
/*  303 */       } else if (this.bitsPerPixel == 24) {
/*  304 */         this.imageType = 3;
/*      */       } 
/*      */ 
/*      */       
/*  308 */       int numberOfEntries = (int)((this.bitmapOffset - 14L - size) / 3L);
/*  309 */       int sizeOfPalette = numberOfEntries * 3;
/*  310 */       if (this.bitmapOffset == size) {
/*  311 */         switch (this.imageType) {
/*      */           case 0:
/*  313 */             sizeOfPalette = 6;
/*      */             break;
/*      */           case 1:
/*  316 */             sizeOfPalette = 48;
/*      */             break;
/*      */           case 2:
/*  319 */             sizeOfPalette = 768;
/*      */             break;
/*      */           case 3:
/*  322 */             sizeOfPalette = 0;
/*      */             break;
/*      */         } 
/*  325 */         this.bitmapOffset = size + sizeOfPalette;
/*      */       } 
/*  327 */       readPalette(sizeOfPalette);
/*      */     } else {
/*      */       
/*  330 */       this.compression = readDWord(this.inputStream);
/*  331 */       this.imageSize = readDWord(this.inputStream);
/*  332 */       this.xPelsPerMeter = readLong(this.inputStream);
/*  333 */       this.yPelsPerMeter = readLong(this.inputStream);
/*  334 */       long colorsUsed = readDWord(this.inputStream);
/*  335 */       long colorsImportant = readDWord(this.inputStream);
/*      */       
/*  337 */       switch ((int)this.compression) {
/*      */         case 0:
/*  339 */           this.properties.put("compression", "BI_RGB");
/*      */           break;
/*      */         
/*      */         case 1:
/*  343 */           this.properties.put("compression", "BI_RLE8");
/*      */           break;
/*      */         
/*      */         case 2:
/*  347 */           this.properties.put("compression", "BI_RLE4");
/*      */           break;
/*      */         
/*      */         case 3:
/*  351 */           this.properties.put("compression", "BI_BITFIELDS");
/*      */           break;
/*      */       } 
/*      */       
/*  355 */       this.properties.put("x_pixels_per_meter", Long.valueOf(this.xPelsPerMeter));
/*  356 */       this.properties.put("y_pixels_per_meter", Long.valueOf(this.yPelsPerMeter));
/*  357 */       this.properties.put("colors_used", Long.valueOf(colorsUsed));
/*  358 */       this.properties.put("colors_important", Long.valueOf(colorsImportant));
/*      */       
/*  360 */       if (size == 40L || size == 52L || size == 56L) {
/*      */         int numberOfEntries; int sizeOfPalette;
/*  362 */         switch ((int)this.compression) {
/*      */ 
/*      */           
/*      */           case 0:
/*      */           case 1:
/*      */           case 2:
/*  368 */             if (this.bitsPerPixel == 1) {
/*  369 */               this.imageType = 4;
/*  370 */             } else if (this.bitsPerPixel == 4) {
/*  371 */               this.imageType = 5;
/*  372 */             } else if (this.bitsPerPixel == 8) {
/*  373 */               this.imageType = 6;
/*  374 */             } else if (this.bitsPerPixel == 24) {
/*  375 */               this.imageType = 7;
/*  376 */             } else if (this.bitsPerPixel == 16) {
/*  377 */               this.imageType = 8;
/*  378 */               this.redMask = 31744;
/*  379 */               this.greenMask = 992;
/*  380 */               this.blueMask = 31;
/*  381 */               this.properties.put("red_mask", Integer.valueOf(this.redMask));
/*  382 */               this.properties.put("green_mask", Integer.valueOf(this.greenMask));
/*  383 */               this.properties.put("blue_mask", Integer.valueOf(this.blueMask));
/*  384 */             } else if (this.bitsPerPixel == 32) {
/*  385 */               this.imageType = 9;
/*  386 */               this.redMask = 16711680;
/*  387 */               this.greenMask = 65280;
/*  388 */               this.blueMask = 255;
/*  389 */               this.properties.put("red_mask", Integer.valueOf(this.redMask));
/*  390 */               this.properties.put("green_mask", Integer.valueOf(this.greenMask));
/*  391 */               this.properties.put("blue_mask", Integer.valueOf(this.blueMask));
/*      */             } 
/*      */ 
/*      */             
/*  395 */             if (size >= 52L) {
/*  396 */               this.redMask = (int)readDWord(this.inputStream);
/*  397 */               this.greenMask = (int)readDWord(this.inputStream);
/*  398 */               this.blueMask = (int)readDWord(this.inputStream);
/*  399 */               this.properties.put("red_mask", Integer.valueOf(this.redMask));
/*  400 */               this.properties.put("green_mask", Integer.valueOf(this.greenMask));
/*  401 */               this.properties.put("blue_mask", Integer.valueOf(this.blueMask));
/*      */             } 
/*      */             
/*  404 */             if (size == 56L) {
/*  405 */               this.alphaMask = (int)readDWord(this.inputStream);
/*  406 */               this.properties.put("alpha_mask", Integer.valueOf(this.alphaMask));
/*      */             } 
/*      */ 
/*      */             
/*  410 */             numberOfEntries = (int)((this.bitmapOffset - 14L - size) / 4L);
/*  411 */             sizeOfPalette = numberOfEntries * 4;
/*  412 */             if (this.bitmapOffset == size) {
/*  413 */               switch (this.imageType) {
/*      */                 case 4:
/*  415 */                   sizeOfPalette = (int)((colorsUsed == 0L) ? 2L : colorsUsed) * 4;
/*      */                   break;
/*      */                 case 5:
/*  418 */                   sizeOfPalette = (int)((colorsUsed == 0L) ? 16L : colorsUsed) * 4;
/*      */                   break;
/*      */                 case 6:
/*  421 */                   sizeOfPalette = (int)((colorsUsed == 0L) ? 256L : colorsUsed) * 4;
/*      */                   break;
/*      */                 default:
/*  424 */                   sizeOfPalette = 0;
/*      */                   break;
/*      */               } 
/*  427 */               this.bitmapOffset = size + sizeOfPalette;
/*      */             } 
/*  429 */             readPalette(sizeOfPalette);
/*      */             
/*  431 */             this.properties.put("bmp_version", "BMP v. 3.x");
/*      */             break;
/*      */ 
/*      */           
/*      */           case 3:
/*  436 */             if (this.bitsPerPixel == 16) {
/*  437 */               this.imageType = 8;
/*  438 */             } else if (this.bitsPerPixel == 32) {
/*  439 */               this.imageType = 9;
/*      */             } 
/*      */ 
/*      */             
/*  443 */             this.redMask = (int)readDWord(this.inputStream);
/*  444 */             this.greenMask = (int)readDWord(this.inputStream);
/*  445 */             this.blueMask = (int)readDWord(this.inputStream);
/*      */ 
/*      */             
/*  448 */             if (size == 56L) {
/*  449 */               this.alphaMask = (int)readDWord(this.inputStream);
/*  450 */               this.properties.put("alpha_mask", Integer.valueOf(this.alphaMask));
/*      */             } 
/*      */             
/*  453 */             this.properties.put("red_mask", Integer.valueOf(this.redMask));
/*  454 */             this.properties.put("green_mask", Integer.valueOf(this.greenMask));
/*  455 */             this.properties.put("blue_mask", Integer.valueOf(this.blueMask));
/*      */             
/*  457 */             if (colorsUsed != 0L) {
/*      */               
/*  459 */               sizeOfPalette = (int)colorsUsed * 4;
/*  460 */               readPalette(sizeOfPalette);
/*      */             } 
/*      */             
/*  463 */             this.properties.put("bmp_version", "BMP v. 3.x NT");
/*      */             break;
/*      */           
/*      */           default:
/*  467 */             throw new RuntimeException("Invalid compression specified in BMP file.");
/*      */         } 
/*      */       
/*  470 */       } else if (size == 108L) {
/*      */ 
/*      */         
/*  473 */         this.properties.put("bmp_version", "BMP v. 4.x");
/*      */ 
/*      */         
/*  476 */         this.redMask = (int)readDWord(this.inputStream);
/*  477 */         this.greenMask = (int)readDWord(this.inputStream);
/*  478 */         this.blueMask = (int)readDWord(this.inputStream);
/*      */         
/*  480 */         this.alphaMask = (int)readDWord(this.inputStream);
/*  481 */         long csType = readDWord(this.inputStream);
/*  482 */         int redX = readLong(this.inputStream);
/*  483 */         int redY = readLong(this.inputStream);
/*  484 */         int redZ = readLong(this.inputStream);
/*  485 */         int greenX = readLong(this.inputStream);
/*  486 */         int greenY = readLong(this.inputStream);
/*  487 */         int greenZ = readLong(this.inputStream);
/*  488 */         int blueX = readLong(this.inputStream);
/*  489 */         int blueY = readLong(this.inputStream);
/*  490 */         int blueZ = readLong(this.inputStream);
/*  491 */         long gammaRed = readDWord(this.inputStream);
/*  492 */         long gammaGreen = readDWord(this.inputStream);
/*  493 */         long gammaBlue = readDWord(this.inputStream);
/*      */         
/*  495 */         if (this.bitsPerPixel == 1) {
/*  496 */           this.imageType = 10;
/*  497 */         } else if (this.bitsPerPixel == 4) {
/*  498 */           this.imageType = 11;
/*  499 */         } else if (this.bitsPerPixel == 8) {
/*  500 */           this.imageType = 12;
/*  501 */         } else if (this.bitsPerPixel == 16) {
/*  502 */           this.imageType = 13;
/*  503 */           if ((int)this.compression == 0) {
/*  504 */             this.redMask = 31744;
/*  505 */             this.greenMask = 992;
/*  506 */             this.blueMask = 31;
/*      */           } 
/*  508 */         } else if (this.bitsPerPixel == 24) {
/*  509 */           this.imageType = 14;
/*  510 */         } else if (this.bitsPerPixel == 32) {
/*  511 */           this.imageType = 15;
/*  512 */           if ((int)this.compression == 0) {
/*  513 */             this.redMask = 16711680;
/*  514 */             this.greenMask = 65280;
/*  515 */             this.blueMask = 255;
/*      */           } 
/*      */         } 
/*      */         
/*  519 */         this.properties.put("red_mask", Integer.valueOf(this.redMask));
/*  520 */         this.properties.put("green_mask", Integer.valueOf(this.greenMask));
/*  521 */         this.properties.put("blue_mask", Integer.valueOf(this.blueMask));
/*  522 */         this.properties.put("alpha_mask", Integer.valueOf(this.alphaMask));
/*      */ 
/*      */         
/*  525 */         int numberOfEntries = (int)((this.bitmapOffset - 14L - size) / 4L);
/*  526 */         int sizeOfPalette = numberOfEntries * 4;
/*  527 */         if (this.bitmapOffset == size) {
/*  528 */           switch (this.imageType) {
/*      */             case 10:
/*  530 */               sizeOfPalette = (int)((colorsUsed == 0L) ? 2L : colorsUsed) * 4;
/*      */               break;
/*      */             case 11:
/*  533 */               sizeOfPalette = (int)((colorsUsed == 0L) ? 16L : colorsUsed) * 4;
/*      */               break;
/*      */             case 12:
/*  536 */               sizeOfPalette = (int)((colorsUsed == 0L) ? 256L : colorsUsed) * 4;
/*      */               break;
/*      */             default:
/*  539 */               sizeOfPalette = 0;
/*      */               break;
/*      */           } 
/*  542 */           this.bitmapOffset = size + sizeOfPalette;
/*      */         } 
/*  544 */         readPalette(sizeOfPalette);
/*      */         
/*  546 */         switch ((int)csType) {
/*      */           
/*      */           case 0:
/*  549 */             this.properties.put("color_space", "LCS_CALIBRATED_RGB");
/*  550 */             this.properties.put("redX", Integer.valueOf(redX));
/*  551 */             this.properties.put("redY", Integer.valueOf(redY));
/*  552 */             this.properties.put("redZ", Integer.valueOf(redZ));
/*  553 */             this.properties.put("greenX", Integer.valueOf(greenX));
/*  554 */             this.properties.put("greenY", Integer.valueOf(greenY));
/*  555 */             this.properties.put("greenZ", Integer.valueOf(greenZ));
/*  556 */             this.properties.put("blueX", Integer.valueOf(blueX));
/*  557 */             this.properties.put("blueY", Integer.valueOf(blueY));
/*  558 */             this.properties.put("blueZ", Integer.valueOf(blueZ));
/*  559 */             this.properties.put("gamma_red", Long.valueOf(gammaRed));
/*  560 */             this.properties.put("gamma_green", Long.valueOf(gammaGreen));
/*  561 */             this.properties.put("gamma_blue", Long.valueOf(gammaBlue));
/*      */ 
/*      */             
/*  564 */             throw new RuntimeException("Not implemented yet.");
/*      */ 
/*      */ 
/*      */           
/*      */           case 1:
/*  569 */             this.properties.put("color_space", "LCS_sRGB");
/*      */             break;
/*      */           
/*      */           case 2:
/*  573 */             this.properties.put("color_space", "LCS_CMYK");
/*      */             
/*  575 */             throw new RuntimeException("Not implemented yet.");
/*      */         } 
/*      */ 
/*      */       
/*      */       } else {
/*  580 */         this.properties.put("bmp_version", "BMP v. 5.x");
/*  581 */         throw new RuntimeException("BMP version 5 not implemented yet.");
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  586 */     if (this.height > 0) {
/*      */       
/*  588 */       this.isBottomUp = true;
/*      */     } else {
/*      */       
/*  591 */       this.isBottomUp = false;
/*  592 */       this.height = Math.abs(this.height);
/*      */     } 
/*      */     
/*  595 */     if (this.bitsPerPixel == 1 || this.bitsPerPixel == 4 || this.bitsPerPixel == 8) {
/*      */       
/*  597 */       this.numBands = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  603 */       if (this.imageType == 0 || this.imageType == 1 || this.imageType == 2) {
/*      */ 
/*      */ 
/*      */         
/*  607 */         int sizep = this.palette.length / 3;
/*      */         
/*  609 */         if (sizep > 256) {
/*  610 */           sizep = 256;
/*      */         }
/*      */ 
/*      */         
/*  614 */         byte[] r = new byte[sizep];
/*  615 */         byte[] g = new byte[sizep];
/*  616 */         byte[] b = new byte[sizep];
/*  617 */         for (int i = 0; i < sizep; i++) {
/*  618 */           int off = 3 * i;
/*  619 */           b[i] = this.palette[off];
/*  620 */           g[i] = this.palette[off + 1];
/*  621 */           r[i] = this.palette[off + 2];
/*      */         } 
/*      */       } else {
/*  624 */         int sizep = this.palette.length / 4;
/*      */         
/*  626 */         if (sizep > 256) {
/*  627 */           sizep = 256;
/*      */         }
/*      */ 
/*      */         
/*  631 */         byte[] r = new byte[sizep];
/*  632 */         byte[] g = new byte[sizep];
/*  633 */         byte[] b = new byte[sizep];
/*  634 */         for (int i = 0; i < sizep; i++) {
/*  635 */           int off = 4 * i;
/*  636 */           b[i] = this.palette[off];
/*  637 */           g[i] = this.palette[off + 1];
/*  638 */           r[i] = this.palette[off + 2];
/*      */         }
/*      */       
/*      */       } 
/*  642 */     } else if (this.bitsPerPixel == 16) {
/*  643 */       this.numBands = 3;
/*  644 */     } else if (this.bitsPerPixel == 32) {
/*  645 */       this.numBands = (this.alphaMask == 0) ? 3 : 4;
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  650 */       this.numBands = 3;
/*      */     } 
/*      */   }
/*      */   
/*      */   private byte[] getPalette(int group) {
/*  655 */     if (this.palette == null)
/*  656 */       return null; 
/*  657 */     byte[] np = new byte[this.palette.length / group * 3];
/*  658 */     int e = this.palette.length / group;
/*  659 */     for (int k = 0; k < e; k++) {
/*  660 */       int src = k * group;
/*  661 */       int dest = k * 3;
/*  662 */       np[dest + 2] = this.palette[src++];
/*  663 */       np[dest + 1] = this.palette[src++];
/*  664 */       np[dest] = this.palette[src];
/*      */     } 
/*  666 */     return np;
/*      */   }
/*      */   
/*      */   private Image getImage() throws IOException, BadElementException {
/*  670 */     byte[] bdata = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  680 */     switch (this.imageType) {
/*      */ 
/*      */       
/*      */       case 0:
/*  684 */         return read1Bit(3);
/*      */ 
/*      */       
/*      */       case 1:
/*  688 */         return read4Bit(3);
/*      */ 
/*      */       
/*      */       case 2:
/*  692 */         return read8Bit(3);
/*      */ 
/*      */       
/*      */       case 3:
/*  696 */         bdata = new byte[this.width * this.height * 3];
/*  697 */         read24Bit(bdata);
/*  698 */         return (Image)new ImgRaw(this.width, this.height, 3, 8, bdata);
/*      */ 
/*      */       
/*      */       case 4:
/*  702 */         return read1Bit(4);
/*      */       
/*      */       case 5:
/*  705 */         switch ((int)this.compression) {
/*      */           case 0:
/*  707 */             return read4Bit(4);
/*      */           
/*      */           case 2:
/*  710 */             return readRLE4();
/*      */         } 
/*      */         
/*  713 */         throw new RuntimeException("Invalid compression specified for BMP file.");
/*      */ 
/*      */ 
/*      */       
/*      */       case 6:
/*  718 */         switch ((int)this.compression) {
/*      */           case 0:
/*  720 */             return read8Bit(4);
/*      */           
/*      */           case 1:
/*  723 */             return readRLE8();
/*      */         } 
/*      */         
/*  726 */         throw new RuntimeException("Invalid compression specified for BMP file.");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 7:
/*  732 */         bdata = new byte[this.width * this.height * 3];
/*  733 */         read24Bit(bdata);
/*  734 */         return (Image)new ImgRaw(this.width, this.height, 3, 8, bdata);
/*      */       
/*      */       case 8:
/*  737 */         return read1632Bit(false);
/*      */       
/*      */       case 9:
/*  740 */         return read1632Bit(true);
/*      */       
/*      */       case 10:
/*  743 */         return read1Bit(4);
/*      */       
/*      */       case 11:
/*  746 */         switch ((int)this.compression) {
/*      */           
/*      */           case 0:
/*  749 */             return read4Bit(4);
/*      */           
/*      */           case 2:
/*  752 */             return readRLE4();
/*      */         } 
/*      */         
/*  755 */         throw new RuntimeException("Invalid compression specified for BMP file.");
/*      */ 
/*      */ 
/*      */       
/*      */       case 12:
/*  760 */         switch ((int)this.compression) {
/*      */           
/*      */           case 0:
/*  763 */             return read8Bit(4);
/*      */           
/*      */           case 1:
/*  766 */             return readRLE8();
/*      */         } 
/*      */         
/*  769 */         throw new RuntimeException("Invalid compression specified for BMP file.");
/*      */ 
/*      */ 
/*      */       
/*      */       case 13:
/*  774 */         return read1632Bit(false);
/*      */       
/*      */       case 14:
/*  777 */         bdata = new byte[this.width * this.height * 3];
/*  778 */         read24Bit(bdata);
/*  779 */         return (Image)new ImgRaw(this.width, this.height, 3, 8, bdata);
/*      */       
/*      */       case 15:
/*  782 */         return read1632Bit(true);
/*      */     } 
/*  784 */     return null;
/*      */   }
/*      */   
/*      */   private Image indexedModel(byte[] bdata, int bpc, int paletteEntries) throws BadElementException {
/*  788 */     ImgRaw imgRaw = new ImgRaw(this.width, this.height, 1, bpc, bdata);
/*  789 */     PdfArray colorspace = new PdfArray();
/*  790 */     colorspace.add((PdfObject)PdfName.INDEXED);
/*  791 */     colorspace.add((PdfObject)PdfName.DEVICERGB);
/*  792 */     byte[] np = getPalette(paletteEntries);
/*  793 */     int len = np.length;
/*  794 */     colorspace.add((PdfObject)new PdfNumber(len / 3 - 1));
/*  795 */     colorspace.add((PdfObject)new PdfString(np));
/*  796 */     PdfDictionary ad = new PdfDictionary();
/*  797 */     ad.put(PdfName.COLORSPACE, (PdfObject)colorspace);
/*  798 */     imgRaw.setAdditional(ad);
/*  799 */     return (Image)imgRaw;
/*      */   }
/*      */   
/*      */   private void readPalette(int sizeOfPalette) throws IOException {
/*  803 */     if (sizeOfPalette == 0) {
/*      */       return;
/*      */     }
/*      */     
/*  807 */     this.palette = new byte[sizeOfPalette];
/*  808 */     int bytesRead = 0;
/*  809 */     while (bytesRead < sizeOfPalette) {
/*  810 */       int r = this.inputStream.read(this.palette, bytesRead, sizeOfPalette - bytesRead);
/*  811 */       if (r < 0) {
/*  812 */         throw new RuntimeException(MessageLocalization.getComposedMessage("incomplete.palette", new Object[0]));
/*      */       }
/*  814 */       bytesRead += r;
/*      */     } 
/*  816 */     this.properties.put("palette", this.palette);
/*      */   }
/*      */ 
/*      */   
/*      */   private Image read1Bit(int paletteEntries) throws IOException, BadElementException {
/*  821 */     byte[] bdata = new byte[(this.width + 7) / 8 * this.height];
/*  822 */     int padding = 0;
/*  823 */     int bytesPerScanline = (int)Math.ceil(this.width / 8.0D);
/*      */     
/*  825 */     int remainder = bytesPerScanline % 4;
/*  826 */     if (remainder != 0) {
/*  827 */       padding = 4 - remainder;
/*      */     }
/*      */     
/*  830 */     int imSize = (bytesPerScanline + padding) * this.height;
/*      */ 
/*      */     
/*  833 */     byte[] values = new byte[imSize];
/*  834 */     int bytesRead = 0;
/*  835 */     while (bytesRead < imSize) {
/*  836 */       bytesRead += this.inputStream.read(values, bytesRead, imSize - bytesRead);
/*      */     }
/*      */ 
/*      */     
/*  840 */     if (this.isBottomUp) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  845 */       for (int i = 0; i < this.height; i++) {
/*  846 */         System.arraycopy(values, imSize - (i + 1) * (bytesPerScanline + padding), bdata, i * bytesPerScanline, bytesPerScanline);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  853 */       for (int i = 0; i < this.height; i++) {
/*  854 */         System.arraycopy(values, i * (bytesPerScanline + padding), bdata, i * bytesPerScanline, bytesPerScanline);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  861 */     return indexedModel(bdata, 1, paletteEntries);
/*      */   }
/*      */ 
/*      */   
/*      */   private Image read4Bit(int paletteEntries) throws IOException, BadElementException {
/*  866 */     byte[] bdata = new byte[(this.width + 1) / 2 * this.height];
/*      */ 
/*      */     
/*  869 */     int padding = 0;
/*      */     
/*  871 */     int bytesPerScanline = (int)Math.ceil(this.width / 2.0D);
/*  872 */     int remainder = bytesPerScanline % 4;
/*  873 */     if (remainder != 0) {
/*  874 */       padding = 4 - remainder;
/*      */     }
/*      */     
/*  877 */     int imSize = (bytesPerScanline + padding) * this.height;
/*      */ 
/*      */     
/*  880 */     byte[] values = new byte[imSize];
/*  881 */     int bytesRead = 0;
/*  882 */     while (bytesRead < imSize) {
/*  883 */       bytesRead += this.inputStream.read(values, bytesRead, imSize - bytesRead);
/*      */     }
/*      */ 
/*      */     
/*  887 */     if (this.isBottomUp) {
/*      */ 
/*      */ 
/*      */       
/*  891 */       for (int i = 0; i < this.height; i++) {
/*  892 */         System.arraycopy(values, imSize - (i + 1) * (bytesPerScanline + padding), bdata, i * bytesPerScanline, bytesPerScanline);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  899 */       for (int i = 0; i < this.height; i++) {
/*  900 */         System.arraycopy(values, i * (bytesPerScanline + padding), bdata, i * bytesPerScanline, bytesPerScanline);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  907 */     return indexedModel(bdata, 4, paletteEntries);
/*      */   }
/*      */ 
/*      */   
/*      */   private Image read8Bit(int paletteEntries) throws IOException, BadElementException {
/*  912 */     byte[] bdata = new byte[this.width * this.height];
/*      */     
/*  914 */     int padding = 0;
/*      */ 
/*      */     
/*  917 */     int bitsPerScanline = this.width * 8;
/*  918 */     if (bitsPerScanline % 32 != 0) {
/*  919 */       padding = (bitsPerScanline / 32 + 1) * 32 - bitsPerScanline;
/*  920 */       padding = (int)Math.ceil(padding / 8.0D);
/*      */     } 
/*      */     
/*  923 */     int imSize = (this.width + padding) * this.height;
/*      */ 
/*      */     
/*  926 */     byte[] values = new byte[imSize];
/*  927 */     int bytesRead = 0;
/*  928 */     while (bytesRead < imSize) {
/*  929 */       bytesRead += this.inputStream.read(values, bytesRead, imSize - bytesRead);
/*      */     }
/*      */     
/*  932 */     if (this.isBottomUp) {
/*      */ 
/*      */ 
/*      */       
/*  936 */       for (int i = 0; i < this.height; i++) {
/*  937 */         System.arraycopy(values, imSize - (i + 1) * (this.width + padding), bdata, i * this.width, this.width);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  944 */       for (int i = 0; i < this.height; i++) {
/*  945 */         System.arraycopy(values, i * (this.width + padding), bdata, i * this.width, this.width);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  952 */     return indexedModel(bdata, 8, paletteEntries);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void read24Bit(byte[] bdata) {
/*  958 */     int padding = 0;
/*      */ 
/*      */     
/*  961 */     int bitsPerScanline = this.width * 24;
/*  962 */     if (bitsPerScanline % 32 != 0) {
/*  963 */       padding = (bitsPerScanline / 32 + 1) * 32 - bitsPerScanline;
/*  964 */       padding = (int)Math.ceil(padding / 8.0D);
/*      */     } 
/*      */ 
/*      */     
/*  968 */     int imSize = (this.width * 3 + 3) / 4 * 4 * this.height;
/*      */     
/*  970 */     byte[] values = new byte[imSize];
/*      */     try {
/*  972 */       int bytesRead = 0;
/*  973 */       while (bytesRead < imSize) {
/*  974 */         int r = this.inputStream.read(values, bytesRead, imSize - bytesRead);
/*      */         
/*  976 */         if (r < 0)
/*      */           break; 
/*  978 */         bytesRead += r;
/*      */       } 
/*  980 */     } catch (IOException ioe) {
/*  981 */       throw new ExceptionConverter(ioe);
/*      */     } 
/*      */     
/*  984 */     int l = 0;
/*      */     
/*  986 */     if (this.isBottomUp) {
/*  987 */       int max = this.width * this.height * 3 - 1;
/*      */       
/*  989 */       int count = -padding;
/*  990 */       for (int i = 0; i < this.height; i++) {
/*  991 */         l = max - (i + 1) * this.width * 3 + 1;
/*  992 */         count += padding;
/*  993 */         for (int j = 0; j < this.width; j++) {
/*  994 */           bdata[l + 2] = values[count++];
/*  995 */           bdata[l + 1] = values[count++];
/*  996 */           bdata[l] = values[count++];
/*  997 */           l += 3;
/*      */         } 
/*      */       } 
/*      */     } else {
/* 1001 */       int count = -padding;
/* 1002 */       for (int i = 0; i < this.height; i++) {
/* 1003 */         count += padding;
/* 1004 */         for (int j = 0; j < this.width; j++) {
/* 1005 */           bdata[l + 2] = values[count++];
/* 1006 */           bdata[l + 1] = values[count++];
/* 1007 */           bdata[l] = values[count++];
/* 1008 */           l += 3;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private int findMask(int mask) {
/* 1015 */     int k = 0;
/* 1016 */     for (; k < 32 && (
/* 1017 */       mask & 0x1) != 1; k++)
/*      */     {
/* 1019 */       mask >>>= 1;
/*      */     }
/* 1021 */     return mask;
/*      */   }
/*      */   
/*      */   private int findShift(int mask) {
/* 1025 */     int k = 0;
/* 1026 */     for (; k < 32 && (
/* 1027 */       mask & 0x1) != 1; k++)
/*      */     {
/* 1029 */       mask >>>= 1;
/*      */     }
/* 1031 */     return k;
/*      */   }
/*      */ 
/*      */   
/*      */   private Image read1632Bit(boolean is32) throws IOException, BadElementException {
/* 1036 */     int red_mask = findMask(this.redMask);
/* 1037 */     int red_shift = findShift(this.redMask);
/* 1038 */     int red_factor = red_mask + 1;
/* 1039 */     int green_mask = findMask(this.greenMask);
/* 1040 */     int green_shift = findShift(this.greenMask);
/* 1041 */     int green_factor = green_mask + 1;
/* 1042 */     int blue_mask = findMask(this.blueMask);
/* 1043 */     int blue_shift = findShift(this.blueMask);
/* 1044 */     int blue_factor = blue_mask + 1;
/* 1045 */     byte[] bdata = new byte[this.width * this.height * 3];
/*      */     
/* 1047 */     int padding = 0;
/*      */     
/* 1049 */     if (!is32) {
/*      */       
/* 1051 */       int bitsPerScanline = this.width * 16;
/* 1052 */       if (bitsPerScanline % 32 != 0) {
/* 1053 */         padding = (bitsPerScanline / 32 + 1) * 32 - bitsPerScanline;
/* 1054 */         padding = (int)Math.ceil(padding / 8.0D);
/*      */       } 
/*      */     } 
/*      */     
/* 1058 */     int imSize = (int)this.imageSize;
/* 1059 */     if (imSize == 0) {
/* 1060 */       imSize = (int)(this.bitmapFileSize - this.bitmapOffset);
/*      */     }
/*      */     
/* 1063 */     int l = 0;
/*      */     
/* 1065 */     if (this.isBottomUp) {
/* 1066 */       for (int i = this.height - 1; i >= 0; i--) {
/* 1067 */         l = this.width * 3 * i;
/* 1068 */         for (int j = 0; j < this.width; j++) {
/* 1069 */           int v; if (is32) {
/* 1070 */             v = (int)readDWord(this.inputStream);
/*      */           } else {
/* 1072 */             v = readWord(this.inputStream);
/* 1073 */           }  bdata[l++] = (byte)((v >>> red_shift & red_mask) * 256 / red_factor);
/* 1074 */           bdata[l++] = (byte)((v >>> green_shift & green_mask) * 256 / green_factor);
/* 1075 */           bdata[l++] = (byte)((v >>> blue_shift & blue_mask) * 256 / blue_factor);
/*      */         } 
/* 1077 */         for (int m = 0; m < padding; m++) {
/* 1078 */           this.inputStream.read();
/*      */         }
/*      */       } 
/*      */     } else {
/* 1082 */       for (int i = 0; i < this.height; i++) {
/* 1083 */         for (int j = 0; j < this.width; j++) {
/* 1084 */           int v; if (is32) {
/* 1085 */             v = (int)readDWord(this.inputStream);
/*      */           } else {
/* 1087 */             v = readWord(this.inputStream);
/* 1088 */           }  bdata[l++] = (byte)((v >>> red_shift & red_mask) * 256 / red_factor);
/* 1089 */           bdata[l++] = (byte)((v >>> green_shift & green_mask) * 256 / green_factor);
/* 1090 */           bdata[l++] = (byte)((v >>> blue_shift & blue_mask) * 256 / blue_factor);
/*      */         } 
/* 1092 */         for (int m = 0; m < padding; m++) {
/* 1093 */           this.inputStream.read();
/*      */         }
/*      */       } 
/*      */     } 
/* 1097 */     return (Image)new ImgRaw(this.width, this.height, 3, 8, bdata);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Image readRLE8() throws IOException, BadElementException {
/* 1103 */     int imSize = (int)this.imageSize;
/* 1104 */     if (imSize == 0) {
/* 1105 */       imSize = (int)(this.bitmapFileSize - this.bitmapOffset);
/*      */     }
/*      */ 
/*      */     
/* 1109 */     byte[] values = new byte[imSize];
/* 1110 */     int bytesRead = 0;
/* 1111 */     while (bytesRead < imSize) {
/* 1112 */       bytesRead += this.inputStream.read(values, bytesRead, imSize - bytesRead);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1117 */     byte[] val = decodeRLE(true, values);
/*      */ 
/*      */     
/* 1120 */     imSize = this.width * this.height;
/*      */     
/* 1122 */     if (this.isBottomUp) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1127 */       byte[] temp = new byte[val.length];
/* 1128 */       int bytesPerScanline = this.width;
/* 1129 */       for (int i = 0; i < this.height; i++) {
/* 1130 */         System.arraycopy(val, imSize - (i + 1) * bytesPerScanline, temp, i * bytesPerScanline, bytesPerScanline);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1135 */       val = temp;
/*      */     } 
/* 1137 */     return indexedModel(val, 8, 4);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Image readRLE4() throws IOException, BadElementException {
/* 1143 */     int imSize = (int)this.imageSize;
/* 1144 */     if (imSize == 0) {
/* 1145 */       imSize = (int)(this.bitmapFileSize - this.bitmapOffset);
/*      */     }
/*      */ 
/*      */     
/* 1149 */     byte[] values = new byte[imSize];
/* 1150 */     int bytesRead = 0;
/* 1151 */     while (bytesRead < imSize) {
/* 1152 */       bytesRead += this.inputStream.read(values, bytesRead, imSize - bytesRead);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1157 */     byte[] val = decodeRLE(false, values);
/*      */ 
/*      */     
/* 1160 */     if (this.isBottomUp) {
/*      */       
/* 1162 */       byte[] inverted = val;
/* 1163 */       val = new byte[this.width * this.height];
/* 1164 */       int l = 0;
/*      */       
/* 1166 */       for (int i = this.height - 1; i >= 0; i--) {
/* 1167 */         int index = i * this.width;
/* 1168 */         int lineEnd = l + this.width;
/* 1169 */         while (l != lineEnd) {
/* 1170 */           val[l++] = inverted[index++];
/*      */         }
/*      */       } 
/*      */     } 
/* 1174 */     int stride = (this.width + 1) / 2;
/* 1175 */     byte[] bdata = new byte[stride * this.height];
/* 1176 */     int ptr = 0;
/* 1177 */     int sh = 0;
/* 1178 */     for (int h = 0; h < this.height; h++) {
/* 1179 */       for (int w = 0; w < this.width; w++) {
/* 1180 */         if ((w & 0x1) == 0) {
/* 1181 */           bdata[sh + w / 2] = (byte)(val[ptr++] << 4);
/*      */         } else {
/* 1183 */           bdata[sh + w / 2] = (byte)(bdata[sh + w / 2] | (byte)(val[ptr++] & 0xF));
/*      */         } 
/* 1185 */       }  sh += stride;
/*      */     } 
/* 1187 */     return indexedModel(bdata, 4, 4);
/*      */   }
/*      */   
/*      */   private byte[] decodeRLE(boolean is8, byte[] values) {
/* 1191 */     byte[] val = new byte[this.width * this.height];
/*      */     try {
/* 1193 */       int ptr = 0;
/* 1194 */       int x = 0;
/* 1195 */       int q = 0;
/* 1196 */       for (int y = 0; y < this.height && ptr < values.length; ) {
/* 1197 */         int count = values[ptr++] & 0xFF;
/* 1198 */         if (count != 0) {
/*      */           
/* 1200 */           int bt = values[ptr++] & 0xFF;
/* 1201 */           if (is8) {
/* 1202 */             for (int i = count; i != 0; i--) {
/* 1203 */               val[q++] = (byte)bt;
/*      */             }
/*      */           } else {
/*      */             
/* 1207 */             for (int i = 0; i < count; i++) {
/* 1208 */               val[q++] = (byte)(((i & 0x1) == 1) ? (bt & 0xF) : (bt >>> 4 & 0xF));
/*      */             }
/*      */           } 
/* 1211 */           x += count;
/*      */           
/*      */           continue;
/*      */         } 
/* 1215 */         count = values[ptr++] & 0xFF;
/* 1216 */         if (count == 1)
/*      */           break; 
/* 1218 */         switch (count) {
/*      */           case 0:
/* 1220 */             x = 0;
/* 1221 */             y++;
/* 1222 */             q = y * this.width;
/*      */             continue;
/*      */           
/*      */           case 2:
/* 1226 */             x += values[ptr++] & 0xFF;
/* 1227 */             y += values[ptr++] & 0xFF;
/* 1228 */             q = y * this.width + x;
/*      */             continue;
/*      */         } 
/*      */         
/* 1232 */         if (is8) {
/* 1233 */           for (int i = count; i != 0; i--) {
/* 1234 */             val[q++] = (byte)(values[ptr++] & 0xFF);
/*      */           }
/*      */         } else {
/* 1237 */           int bt = 0;
/* 1238 */           for (int i = 0; i < count; i++) {
/* 1239 */             if ((i & 0x1) == 0)
/* 1240 */               bt = values[ptr++] & 0xFF; 
/* 1241 */             val[q++] = (byte)(((i & 0x1) == 1) ? (bt & 0xF) : (bt >>> 4 & 0xF));
/*      */           } 
/*      */         } 
/* 1244 */         x += count;
/*      */         
/* 1246 */         if (is8) {
/* 1247 */           if ((count & 0x1) == 1)
/* 1248 */             ptr++; 
/*      */           continue;
/*      */         } 
/* 1251 */         if ((count & 0x3) == 1 || (count & 0x3) == 2) {
/* 1252 */           ptr++;
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 1259 */     catch (RuntimeException runtimeException) {}
/*      */ 
/*      */ 
/*      */     
/* 1263 */     return val;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int readUnsignedByte(InputStream stream) throws IOException {
/* 1270 */     return stream.read() & 0xFF;
/*      */   }
/*      */ 
/*      */   
/*      */   private int readUnsignedShort(InputStream stream) throws IOException {
/* 1275 */     int b1 = readUnsignedByte(stream);
/* 1276 */     int b2 = readUnsignedByte(stream);
/* 1277 */     return (b2 << 8 | b1) & 0xFFFF;
/*      */   }
/*      */ 
/*      */   
/*      */   private int readShort(InputStream stream) throws IOException {
/* 1282 */     int b1 = readUnsignedByte(stream);
/* 1283 */     int b2 = readUnsignedByte(stream);
/* 1284 */     return b2 << 8 | b1;
/*      */   }
/*      */ 
/*      */   
/*      */   private int readWord(InputStream stream) throws IOException {
/* 1289 */     return readUnsignedShort(stream);
/*      */   }
/*      */ 
/*      */   
/*      */   private long readUnsignedInt(InputStream stream) throws IOException {
/* 1294 */     int b1 = readUnsignedByte(stream);
/* 1295 */     int b2 = readUnsignedByte(stream);
/* 1296 */     int b3 = readUnsignedByte(stream);
/* 1297 */     int b4 = readUnsignedByte(stream);
/* 1298 */     long l = (b4 << 24 | b3 << 16 | b2 << 8 | b1);
/* 1299 */     return l & 0xFFFFFFFFFFFFFFFFL;
/*      */   }
/*      */ 
/*      */   
/*      */   private int readInt(InputStream stream) throws IOException {
/* 1304 */     int b1 = readUnsignedByte(stream);
/* 1305 */     int b2 = readUnsignedByte(stream);
/* 1306 */     int b3 = readUnsignedByte(stream);
/* 1307 */     int b4 = readUnsignedByte(stream);
/* 1308 */     return b4 << 24 | b3 << 16 | b2 << 8 | b1;
/*      */   }
/*      */ 
/*      */   
/*      */   private long readDWord(InputStream stream) throws IOException {
/* 1313 */     return readUnsignedInt(stream);
/*      */   }
/*      */ 
/*      */   
/*      */   private int readLong(InputStream stream) throws IOException {
/* 1318 */     return readInt(stream);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/BmpImage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */