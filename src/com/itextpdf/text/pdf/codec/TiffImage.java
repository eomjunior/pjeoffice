/*     */ package com.itextpdf.text.pdf.codec;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.ImgRaw;
/*     */ import com.itextpdf.text.Jpeg;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.exceptions.InvalidImageException;
/*     */ import com.itextpdf.text.pdf.ICC_Profile;
/*     */ import com.itextpdf.text.pdf.PdfArray;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfNumber;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfString;
/*     */ import com.itextpdf.text.pdf.RandomAccessFileOrArray;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.zip.DataFormatException;
/*     */ import java.util.zip.DeflaterOutputStream;
/*     */ import java.util.zip.Inflater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TiffImage
/*     */ {
/*     */   public static int getNumberOfPages(RandomAccessFileOrArray s) {
/*     */     try {
/*  76 */       return TIFFDirectory.getNumDirectories(s);
/*     */     }
/*  78 */     catch (Exception e) {
/*  79 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static int getDpi(TIFFField fd, int resolutionUnit) {
/*  84 */     if (fd == null)
/*  85 */       return 0; 
/*  86 */     long[] res = fd.getAsRational(0);
/*  87 */     float frac = (float)res[0] / (float)res[1];
/*  88 */     int dpi = 0;
/*  89 */     switch (resolutionUnit) {
/*     */       case 1:
/*     */       case 2:
/*  92 */         dpi = (int)(frac + 0.5D);
/*     */         break;
/*     */       case 3:
/*  95 */         dpi = (int)(frac * 2.54D + 0.5D);
/*     */         break;
/*     */     } 
/*  98 */     return dpi;
/*     */   }
/*     */   
/*     */   public static Image getTiffImage(RandomAccessFileOrArray s, boolean recoverFromImageError, int page, boolean direct) {
/* 102 */     if (page < 1)
/* 103 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.page.number.must.be.gt.eq.1", new Object[0]));  try {
/*     */       TIFFField t4OptionsField, t6OptionsField;
/* 105 */       TIFFDirectory dir = new TIFFDirectory(s, page - 1);
/* 106 */       if (dir.isTagPresent(322))
/* 107 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("tiles.are.not.supported", new Object[0])); 
/* 108 */       int compression = 1;
/* 109 */       if (dir.isTagPresent(259)) {
/* 110 */         compression = (int)dir.getFieldAsLong(259);
/*     */       }
/* 112 */       switch (compression) {
/*     */         case 2:
/*     */         case 3:
/*     */         case 4:
/*     */         case 32771:
/*     */           break;
/*     */         default:
/* 119 */           return getTiffImageColor(dir, s);
/*     */       } 
/* 121 */       float rotation = 0.0F;
/* 122 */       if (dir.isTagPresent(274)) {
/* 123 */         int rot = (int)dir.getFieldAsLong(274);
/* 124 */         if (rot == 3 || rot == 4) {
/* 125 */           rotation = 3.1415927F;
/* 126 */         } else if (rot == 5 || rot == 8) {
/* 127 */           rotation = 1.5707964F;
/* 128 */         } else if (rot == 6 || rot == 7) {
/* 129 */           rotation = -1.5707964F;
/*     */         } 
/*     */       } 
/* 132 */       Image img = null;
/* 133 */       long tiffT4Options = 0L;
/* 134 */       long tiffT6Options = 0L;
/* 135 */       long fillOrder = 1L;
/* 136 */       int h = (int)dir.getFieldAsLong(257);
/* 137 */       int w = (int)dir.getFieldAsLong(256);
/* 138 */       int dpiX = 0;
/* 139 */       int dpiY = 0;
/* 140 */       float XYRatio = 0.0F;
/* 141 */       int resolutionUnit = 2;
/* 142 */       if (dir.isTagPresent(296))
/* 143 */         resolutionUnit = (int)dir.getFieldAsLong(296); 
/* 144 */       dpiX = getDpi(dir.getField(282), resolutionUnit);
/* 145 */       dpiY = getDpi(dir.getField(283), resolutionUnit);
/* 146 */       if (resolutionUnit == 1) {
/* 147 */         if (dpiY != 0)
/* 148 */           XYRatio = dpiX / dpiY; 
/* 149 */         dpiX = 0;
/* 150 */         dpiY = 0;
/*     */       } 
/* 152 */       int rowsStrip = h;
/* 153 */       if (dir.isTagPresent(278))
/* 154 */         rowsStrip = (int)dir.getFieldAsLong(278); 
/* 155 */       if (rowsStrip <= 0 || rowsStrip > h)
/* 156 */         rowsStrip = h; 
/* 157 */       long[] offset = getArrayLongShort(dir, 273);
/* 158 */       long[] size = getArrayLongShort(dir, 279);
/* 159 */       if ((size == null || (size.length == 1 && (size[0] == 0L || size[0] + offset[0] > s.length()))) && h == rowsStrip) {
/* 160 */         size = new long[] { s.length() - (int)offset[0] };
/*     */       }
/* 162 */       boolean reverse = false;
/* 163 */       TIFFField fillOrderField = dir.getField(266);
/* 164 */       if (fillOrderField != null)
/* 165 */         fillOrder = fillOrderField.getAsLong(0); 
/* 166 */       reverse = (fillOrder == 2L);
/* 167 */       int params = 0;
/* 168 */       if (dir.isTagPresent(262)) {
/* 169 */         long photo = dir.getFieldAsLong(262);
/* 170 */         if (photo == 1L)
/* 171 */           params |= 0x1; 
/*     */       } 
/* 173 */       int imagecomp = 0;
/* 174 */       switch (compression) {
/*     */         case 2:
/*     */         case 32771:
/* 177 */           imagecomp = 257;
/* 178 */           params |= 0xA;
/*     */           break;
/*     */         case 3:
/* 181 */           imagecomp = 257;
/* 182 */           params |= 0xC;
/* 183 */           t4OptionsField = dir.getField(292);
/* 184 */           if (t4OptionsField != null) {
/* 185 */             tiffT4Options = t4OptionsField.getAsLong(0);
/* 186 */             if ((tiffT4Options & 0x1L) != 0L)
/* 187 */               imagecomp = 258; 
/* 188 */             if ((tiffT4Options & 0x4L) != 0L)
/* 189 */               params |= 0x2; 
/*     */           } 
/*     */           break;
/*     */         case 4:
/* 193 */           imagecomp = 256;
/* 194 */           t6OptionsField = dir.getField(293);
/* 195 */           if (t6OptionsField != null)
/* 196 */             tiffT6Options = t6OptionsField.getAsLong(0); 
/*     */           break;
/*     */       } 
/* 199 */       if (direct && rowsStrip == h) {
/* 200 */         byte[] im = new byte[(int)size[0]];
/* 201 */         s.seek(offset[0]);
/* 202 */         s.readFully(im);
/* 203 */         img = Image.getInstance(w, h, false, imagecomp, params, im);
/* 204 */         img.setInverted(true);
/*     */       } else {
/*     */         
/* 207 */         int rowsLeft = h;
/* 208 */         CCITTG4Encoder g4 = new CCITTG4Encoder(w);
/* 209 */         for (int k = 0; k < offset.length; k++) {
/* 210 */           byte[] im = new byte[(int)size[k]];
/* 211 */           s.seek(offset[k]);
/* 212 */           s.readFully(im);
/* 213 */           int height = Math.min(rowsStrip, rowsLeft);
/* 214 */           TIFFFaxDecoder decoder = new TIFFFaxDecoder(fillOrder, w, height);
/* 215 */           decoder.setRecoverFromImageError(recoverFromImageError);
/* 216 */           byte[] outBuf = new byte[(w + 7) / 8 * height];
/* 217 */           switch (compression) {
/*     */             case 2:
/*     */             case 32771:
/* 220 */               decoder.decode1D(outBuf, im, 0, height);
/* 221 */               g4.fax4Encode(outBuf, height);
/*     */               break;
/*     */             case 3:
/*     */               try {
/* 225 */                 decoder.decode2D(outBuf, im, 0, height, tiffT4Options);
/*     */               }
/* 227 */               catch (RuntimeException e) {
/*     */                 
/* 229 */                 tiffT4Options ^= 0x4L;
/*     */                 try {
/* 231 */                   decoder.decode2D(outBuf, im, 0, height, tiffT4Options);
/*     */                 }
/* 233 */                 catch (RuntimeException e2) {
/* 234 */                   if (!recoverFromImageError)
/* 235 */                     throw e; 
/* 236 */                   if (rowsStrip == 1) {
/* 237 */                     throw e;
/*     */                   }
/*     */                   
/* 240 */                   im = new byte[(int)size[0]];
/* 241 */                   s.seek(offset[0]);
/* 242 */                   s.readFully(im);
/* 243 */                   img = Image.getInstance(w, h, false, imagecomp, params, im);
/* 244 */                   img.setInverted(true);
/* 245 */                   img.setDpi(dpiX, dpiY);
/* 246 */                   img.setXYRatio(XYRatio);
/* 247 */                   img.setOriginalType(5);
/* 248 */                   if (rotation != 0.0F)
/* 249 */                     img.setInitialRotation(rotation); 
/* 250 */                   return img;
/*     */                 } 
/*     */               } 
/* 253 */               g4.fax4Encode(outBuf, height);
/*     */               break;
/*     */             case 4:
/*     */               try {
/* 257 */                 decoder.decodeT6(outBuf, im, 0, height, tiffT6Options);
/* 258 */               } catch (InvalidImageException e) {
/* 259 */                 if (!recoverFromImageError) {
/* 260 */                   throw e;
/*     */                 }
/*     */               } 
/*     */               
/* 264 */               g4.fax4Encode(outBuf, height);
/*     */               break;
/*     */           } 
/* 267 */           rowsLeft -= rowsStrip;
/*     */         } 
/* 269 */         byte[] g4pic = g4.close();
/* 270 */         img = Image.getInstance(w, h, false, 256, params & 0x1, g4pic);
/*     */       } 
/* 272 */       img.setDpi(dpiX, dpiY);
/* 273 */       img.setXYRatio(XYRatio);
/* 274 */       if (dir.isTagPresent(34675)) {
/*     */         try {
/* 276 */           TIFFField fd = dir.getField(34675);
/* 277 */           ICC_Profile icc_prof = ICC_Profile.getInstance(fd.getAsBytes());
/* 278 */           if (icc_prof.getNumComponents() == 1) {
/* 279 */             img.tagICC(icc_prof);
/*     */           }
/* 281 */         } catch (RuntimeException runtimeException) {}
/*     */       }
/*     */ 
/*     */       
/* 285 */       img.setOriginalType(5);
/* 286 */       if (rotation != 0.0F)
/* 287 */         img.setInitialRotation(rotation); 
/* 288 */       return img;
/*     */     }
/* 290 */     catch (Exception e) {
/* 291 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Image getTiffImage(RandomAccessFileOrArray s, boolean recoverFromImageError, int page) {
/* 296 */     return getTiffImage(s, recoverFromImageError, page, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Image getTiffImage(RandomAccessFileOrArray s, int page) {
/* 305 */     return getTiffImage(s, page, false);
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
/*     */   public static Image getTiffImage(RandomAccessFileOrArray s, int page, boolean direct) {
/* 317 */     return getTiffImage(s, false, page, direct);
/*     */   }
/*     */   protected static Image getTiffImageColor(TIFFDirectory dir, RandomAccessFileOrArray s) {
/*     */     try {
/*     */       ImgRaw imgRaw;
/* 322 */       int compression = 1;
/* 323 */       if (dir.isTagPresent(259)) {
/* 324 */         compression = (int)dir.getFieldAsLong(259);
/*     */       }
/* 326 */       int predictor = 1;
/* 327 */       TIFFLZWDecoder lzwDecoder = null;
/* 328 */       switch (compression) {
/*     */         case 1:
/*     */         case 5:
/*     */         case 6:
/*     */         case 7:
/*     */         case 8:
/*     */         case 32773:
/*     */         case 32946:
/*     */           break;
/*     */         default:
/* 338 */           throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.compression.1.is.not.supported", compression));
/*     */       } 
/* 340 */       int photometric = (int)dir.getFieldAsLong(262);
/* 341 */       switch (photometric) {
/*     */         case 0:
/*     */         case 1:
/*     */         case 2:
/*     */         case 3:
/*     */         case 5:
/*     */           break;
/*     */         default:
/* 349 */           if (compression != 6 && compression != 7)
/* 350 */             throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.photometric.1.is.not.supported", photometric));  break;
/*     */       } 
/* 352 */       float rotation = 0.0F;
/* 353 */       if (dir.isTagPresent(274)) {
/* 354 */         int rot = (int)dir.getFieldAsLong(274);
/* 355 */         if (rot == 3 || rot == 4) {
/* 356 */           rotation = 3.1415927F;
/* 357 */         } else if (rot == 5 || rot == 8) {
/* 358 */           rotation = 1.5707964F;
/* 359 */         } else if (rot == 6 || rot == 7) {
/* 360 */           rotation = -1.5707964F;
/*     */         } 
/* 362 */       }  if (dir.isTagPresent(284) && dir
/* 363 */         .getFieldAsLong(284) == 2L)
/* 364 */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("planar.images.are.not.supported", new Object[0])); 
/* 365 */       int extraSamples = 0;
/* 366 */       if (dir.isTagPresent(338))
/* 367 */         extraSamples = 1; 
/* 368 */       int samplePerPixel = 1;
/* 369 */       if (dir.isTagPresent(277))
/* 370 */         samplePerPixel = (int)dir.getFieldAsLong(277); 
/* 371 */       int bitsPerSample = 1;
/* 372 */       if (dir.isTagPresent(258))
/* 373 */         bitsPerSample = (int)dir.getFieldAsLong(258); 
/* 374 */       switch (bitsPerSample) {
/*     */         case 1:
/*     */         case 2:
/*     */         case 4:
/*     */         case 8:
/*     */           break;
/*     */         default:
/* 381 */           throw new IllegalArgumentException(MessageLocalization.getComposedMessage("bits.per.sample.1.is.not.supported", bitsPerSample));
/*     */       } 
/* 383 */       Image img = null;
/*     */       
/* 385 */       int h = (int)dir.getFieldAsLong(257);
/* 386 */       int w = (int)dir.getFieldAsLong(256);
/* 387 */       int dpiX = 0;
/* 388 */       int dpiY = 0;
/* 389 */       int resolutionUnit = 2;
/* 390 */       if (dir.isTagPresent(296))
/* 391 */         resolutionUnit = (int)dir.getFieldAsLong(296); 
/* 392 */       dpiX = getDpi(dir.getField(282), resolutionUnit);
/* 393 */       dpiY = getDpi(dir.getField(283), resolutionUnit);
/* 394 */       int fillOrder = 1;
/* 395 */       boolean reverse = false;
/* 396 */       TIFFField fillOrderField = dir.getField(266);
/* 397 */       if (fillOrderField != null)
/* 398 */         fillOrder = fillOrderField.getAsInt(0); 
/* 399 */       reverse = (fillOrder == 2);
/* 400 */       int rowsStrip = h;
/* 401 */       if (dir.isTagPresent(278))
/* 402 */         rowsStrip = (int)dir.getFieldAsLong(278); 
/* 403 */       if (rowsStrip <= 0 || rowsStrip > h)
/* 404 */         rowsStrip = h; 
/* 405 */       long[] offset = getArrayLongShort(dir, 273);
/* 406 */       long[] size = getArrayLongShort(dir, 279);
/* 407 */       if ((size == null || (size.length == 1 && (size[0] == 0L || size[0] + offset[0] > s.length()))) && h == rowsStrip) {
/* 408 */         size = new long[] { s.length() - (int)offset[0] };
/*     */       }
/* 410 */       if (compression == 5 || compression == 32946 || compression == 8) {
/* 411 */         TIFFField predictorField = dir.getField(317);
/* 412 */         if (predictorField != null) {
/* 413 */           predictor = predictorField.getAsInt(0);
/* 414 */           if (predictor != 1 && predictor != 2) {
/* 415 */             throw new RuntimeException(MessageLocalization.getComposedMessage("illegal.value.for.predictor.in.tiff.file", new Object[0]));
/*     */           }
/* 417 */           if (predictor == 2 && bitsPerSample != 8) {
/* 418 */             throw new RuntimeException(MessageLocalization.getComposedMessage("1.bit.samples.are.not.supported.for.horizontal.differencing.predictor", bitsPerSample));
/*     */           }
/*     */         } 
/*     */       } 
/* 422 */       if (compression == 5) {
/* 423 */         lzwDecoder = new TIFFLZWDecoder(w, predictor, samplePerPixel);
/*     */       }
/* 425 */       int rowsLeft = h;
/* 426 */       ByteArrayOutputStream stream = null;
/* 427 */       ByteArrayOutputStream mstream = null;
/* 428 */       DeflaterOutputStream zip = null;
/* 429 */       DeflaterOutputStream mzip = null;
/* 430 */       if (extraSamples > 0) {
/* 431 */         mstream = new ByteArrayOutputStream();
/* 432 */         mzip = new DeflaterOutputStream(mstream);
/*     */       } 
/*     */       
/* 435 */       CCITTG4Encoder g4 = null;
/* 436 */       if (bitsPerSample == 1 && samplePerPixel == 1 && photometric != 3) {
/* 437 */         g4 = new CCITTG4Encoder(w);
/*     */       } else {
/*     */         
/* 440 */         stream = new ByteArrayOutputStream();
/* 441 */         if (compression != 6 && compression != 7)
/* 442 */           zip = new DeflaterOutputStream(stream); 
/*     */       } 
/* 444 */       if (compression == 6) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 449 */         if (!dir.isTagPresent(513)) {
/* 450 */           throw new IOException(MessageLocalization.getComposedMessage("missing.tag.s.for.ojpeg.compression", new Object[0]));
/*     */         }
/* 452 */         int jpegOffset = (int)dir.getFieldAsLong(513);
/* 453 */         int jpegLength = (int)s.length() - jpegOffset;
/*     */         
/* 455 */         if (dir.isTagPresent(514)) {
/* 456 */           jpegLength = (int)dir.getFieldAsLong(514) + (int)size[0];
/*     */         }
/*     */ 
/*     */         
/* 460 */         byte[] jpeg = new byte[Math.min(jpegLength, (int)s.length() - jpegOffset)];
/*     */ 
/*     */         
/* 463 */         s.seek(jpegOffset);
/* 464 */         s.readFully(jpeg);
/* 465 */         Jpeg jpeg1 = new Jpeg(jpeg);
/*     */       }
/* 467 */       else if (compression == 7) {
/* 468 */         if (size.length > 1)
/* 469 */           throw new IOException(MessageLocalization.getComposedMessage("compression.jpeg.is.only.supported.with.a.single.strip.this.image.has.1.strips", size.length)); 
/* 470 */         byte[] jpeg = new byte[(int)size[0]];
/* 471 */         s.seek(offset[0]);
/* 472 */         s.readFully(jpeg);
/*     */ 
/*     */         
/* 475 */         TIFFField jpegtables = dir.getField(347);
/* 476 */         if (jpegtables != null) {
/* 477 */           byte[] temp = jpegtables.getAsBytes();
/* 478 */           int tableoffset = 0;
/* 479 */           int tablelength = temp.length;
/*     */           
/* 481 */           if (temp[0] == -1 && temp[1] == -40) {
/* 482 */             tableoffset = 2;
/* 483 */             tablelength -= 2;
/*     */           } 
/*     */           
/* 486 */           if (temp[temp.length - 2] == -1 && temp[temp.length - 1] == -39)
/* 487 */             tablelength -= 2; 
/* 488 */           byte[] tables = new byte[tablelength];
/* 489 */           System.arraycopy(temp, tableoffset, tables, 0, tablelength);
/*     */           
/* 491 */           byte[] jpegwithtables = new byte[jpeg.length + tables.length];
/* 492 */           System.arraycopy(jpeg, 0, jpegwithtables, 0, 2);
/* 493 */           System.arraycopy(tables, 0, jpegwithtables, 2, tables.length);
/* 494 */           System.arraycopy(jpeg, 2, jpegwithtables, tables.length + 2, jpeg.length - 2);
/* 495 */           jpeg = jpegwithtables;
/*     */         } 
/* 497 */         Jpeg jpeg1 = new Jpeg(jpeg);
/* 498 */         if (photometric == 2) {
/* 499 */           jpeg1.setColorTransform(0);
/*     */         }
/*     */       } else {
/*     */         
/* 503 */         for (int k = 0; k < offset.length; k++) {
/* 504 */           byte[] im = new byte[(int)size[k]];
/* 505 */           s.seek(offset[k]);
/* 506 */           s.readFully(im);
/* 507 */           int height = Math.min(rowsStrip, rowsLeft);
/* 508 */           byte[] outBuf = null;
/* 509 */           if (compression != 1)
/* 510 */             outBuf = new byte[(w * bitsPerSample * samplePerPixel + 7) / 8 * height]; 
/* 511 */           if (reverse)
/* 512 */             TIFFFaxDecoder.reverseBits(im); 
/* 513 */           switch (compression) {
/*     */             case 8:
/*     */             case 32946:
/* 516 */               inflate(im, outBuf);
/* 517 */               applyPredictor(outBuf, predictor, w, height, samplePerPixel);
/*     */               break;
/*     */             case 1:
/* 520 */               outBuf = im;
/*     */               break;
/*     */             case 32773:
/* 523 */               decodePackbits(im, outBuf);
/*     */               break;
/*     */             case 5:
/* 526 */               lzwDecoder.decode(im, outBuf, height);
/*     */               break;
/*     */           } 
/* 529 */           if (bitsPerSample == 1 && samplePerPixel == 1 && photometric != 3) {
/* 530 */             g4.fax4Encode(outBuf, height);
/*     */           
/*     */           }
/* 533 */           else if (extraSamples > 0) {
/* 534 */             ProcessExtraSamples(zip, mzip, outBuf, samplePerPixel, bitsPerSample, w, height);
/*     */           } else {
/* 536 */             zip.write(outBuf);
/*     */           } 
/* 538 */           rowsLeft -= rowsStrip;
/*     */         } 
/* 540 */         if (bitsPerSample == 1 && samplePerPixel == 1 && photometric != 3) {
/* 541 */           img = Image.getInstance(w, h, false, 256, (photometric == 1) ? 1 : 0, g4
/* 542 */               .close());
/*     */         } else {
/*     */           
/* 545 */           zip.close();
/* 546 */           imgRaw = new ImgRaw(w, h, samplePerPixel - extraSamples, bitsPerSample, stream.toByteArray());
/* 547 */           imgRaw.setDeflated(true);
/*     */         } 
/*     */       } 
/* 550 */       imgRaw.setDpi(dpiX, dpiY);
/* 551 */       if (compression != 6 && compression != 7) {
/* 552 */         if (dir.isTagPresent(34675)) {
/*     */           try {
/* 554 */             TIFFField fd = dir.getField(34675);
/* 555 */             ICC_Profile icc_prof = ICC_Profile.getInstance(fd.getAsBytes());
/* 556 */             if (samplePerPixel - extraSamples == icc_prof.getNumComponents()) {
/* 557 */               imgRaw.tagICC(icc_prof);
/*     */             }
/* 559 */           } catch (RuntimeException runtimeException) {}
/*     */         }
/*     */ 
/*     */         
/* 563 */         if (dir.isTagPresent(320)) {
/* 564 */           TIFFField fd = dir.getField(320);
/* 565 */           char[] rgb = fd.getAsChars();
/* 566 */           byte[] palette = new byte[rgb.length];
/* 567 */           int gColor = rgb.length / 3;
/* 568 */           int bColor = gColor * 2;
/* 569 */           for (int k = 0; k < gColor; k++) {
/* 570 */             palette[k * 3] = (byte)(rgb[k] >>> 8);
/* 571 */             palette[k * 3 + 1] = (byte)(rgb[k + gColor] >>> 8);
/* 572 */             palette[k * 3 + 2] = (byte)(rgb[k + bColor] >>> 8);
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 577 */           boolean colormapBroken = true; int i;
/* 578 */           for (i = 0; i < palette.length; i++) {
/* 579 */             if (palette[i] != 0) {
/* 580 */               colormapBroken = false;
/*     */               break;
/*     */             } 
/*     */           } 
/* 584 */           if (colormapBroken) {
/* 585 */             for (i = 0; i < gColor; i++) {
/* 586 */               palette[i * 3] = (byte)rgb[i];
/* 587 */               palette[i * 3 + 1] = (byte)rgb[i + gColor];
/* 588 */               palette[i * 3 + 2] = (byte)rgb[i + bColor];
/*     */             } 
/*     */           }
/* 591 */           PdfArray indexed = new PdfArray();
/* 592 */           indexed.add((PdfObject)PdfName.INDEXED);
/* 593 */           indexed.add((PdfObject)PdfName.DEVICERGB);
/* 594 */           indexed.add((PdfObject)new PdfNumber(gColor - 1));
/* 595 */           indexed.add((PdfObject)new PdfString(palette));
/* 596 */           PdfDictionary additional = new PdfDictionary();
/* 597 */           additional.put(PdfName.COLORSPACE, (PdfObject)indexed);
/* 598 */           imgRaw.setAdditional(additional);
/*     */         } 
/* 600 */         imgRaw.setOriginalType(5);
/*     */       } 
/* 602 */       if (photometric == 0)
/* 603 */         imgRaw.setInverted(true); 
/* 604 */       if (rotation != 0.0F)
/* 605 */         imgRaw.setInitialRotation(rotation); 
/* 606 */       if (extraSamples > 0) {
/* 607 */         mzip.close();
/* 608 */         Image mimg = Image.getInstance(w, h, 1, bitsPerSample, mstream.toByteArray());
/* 609 */         mimg.makeMask();
/* 610 */         mimg.setDeflated(true);
/* 611 */         imgRaw.setImageMask(mimg);
/*     */       } 
/* 613 */       return (Image)imgRaw;
/*     */     }
/* 615 */     catch (Exception e) {
/* 616 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static Image ProcessExtraSamples(DeflaterOutputStream zip, DeflaterOutputStream mzip, byte[] outBuf, int samplePerPixel, int bitsPerSample, int width, int height) throws IOException {
/* 621 */     if (bitsPerSample == 8) {
/* 622 */       byte[] mask = new byte[width * height];
/* 623 */       int mptr = 0;
/* 624 */       int optr = 0;
/* 625 */       int total = width * height * samplePerPixel; int k;
/* 626 */       for (k = 0; k < total; k += samplePerPixel) {
/* 627 */         for (int s = 0; s < samplePerPixel - 1; s++) {
/* 628 */           outBuf[optr++] = outBuf[k + s];
/*     */         }
/* 630 */         mask[mptr++] = outBuf[k + samplePerPixel - 1];
/*     */       } 
/* 632 */       zip.write(outBuf, 0, optr);
/* 633 */       mzip.write(mask, 0, mptr);
/*     */     } else {
/*     */       
/* 636 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("extra.samples.are.not.supported", new Object[0]));
/* 637 */     }  return null;
/*     */   }
/*     */   static long[] getArrayLongShort(TIFFDirectory dir, int tag) {
/*     */     long[] offset;
/* 641 */     TIFFField field = dir.getField(tag);
/* 642 */     if (field == null) {
/* 643 */       return null;
/*     */     }
/* 645 */     if (field.getType() == 4) {
/* 646 */       offset = field.getAsLongs();
/*     */     } else {
/* 648 */       char[] temp = field.getAsChars();
/* 649 */       offset = new long[temp.length];
/* 650 */       for (int k = 0; k < temp.length; k++)
/* 651 */         offset[k] = temp[k]; 
/*     */     } 
/* 653 */     return offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void decodePackbits(byte[] data, byte[] dst) {
/* 658 */     int srcCount = 0, dstCount = 0;
/*     */ 
/*     */     
/*     */     try {
/* 662 */       while (dstCount < dst.length) {
/* 663 */         byte b = data[srcCount++];
/* 664 */         if (b >= 0 && b <= Byte.MAX_VALUE) {
/*     */           
/* 666 */           for (int i = 0; i < b + 1; i++)
/* 667 */             dst[dstCount++] = data[srcCount++]; 
/*     */           continue;
/*     */         } 
/* 670 */         if (b <= -1 && b >= -127) {
/*     */           
/* 672 */           byte repeat = data[srcCount++];
/* 673 */           for (int i = 0; i < -b + 1; i++) {
/* 674 */             dst[dstCount++] = repeat;
/*     */           }
/*     */           continue;
/*     */         } 
/* 678 */         srcCount++;
/*     */       }
/*     */     
/*     */     }
/* 682 */     catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void inflate(byte[] deflated, byte[] inflated) {
/* 688 */     Inflater inflater = new Inflater();
/* 689 */     inflater.setInput(deflated);
/*     */     try {
/* 691 */       inflater.inflate(inflated);
/*     */     }
/* 693 */     catch (DataFormatException dfe) {
/* 694 */       throw new ExceptionConverter(dfe);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void applyPredictor(byte[] uncompData, int predictor, int w, int h, int samplesPerPixel) {
/* 699 */     if (predictor != 2) {
/*     */       return;
/*     */     }
/* 702 */     for (int j = 0; j < h; j++) {
/* 703 */       int count = samplesPerPixel * (j * w + 1);
/* 704 */       for (int i = samplesPerPixel; i < w * samplesPerPixel; i++) {
/* 705 */         uncompData[count] = (byte)(uncompData[count] + uncompData[count - samplesPerPixel]);
/* 706 */         count++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/TiffImage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */