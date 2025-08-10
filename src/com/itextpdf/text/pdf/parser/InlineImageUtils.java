/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import com.itextpdf.text.exceptions.UnsupportedPdfException;
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import com.itextpdf.text.pdf.FilterHandlers;
/*     */ import com.itextpdf.text.pdf.PRTokeniser;
/*     */ import com.itextpdf.text.pdf.PdfArray;
/*     */ import com.itextpdf.text.pdf.PdfContentParser;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfNumber;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfReader;
/*     */ import java.io.ByteArrayOutputStream;
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
/*     */ 
/*     */ 
/*     */ public final class InlineImageUtils
/*     */ {
/*  69 */   private static final Logger LOGGER = LoggerFactory.getLogger(InlineImageUtils.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class InlineImageParseException
/*     */     extends IOException
/*     */   {
/*     */     private static final long serialVersionUID = 233760879000268548L;
/*     */ 
/*     */ 
/*     */     
/*     */     public InlineImageParseException(String message) {
/*  82 */       super(message);
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
/*  93 */   private static final Map<PdfName, PdfName> inlineImageEntryAbbreviationMap = new HashMap<PdfName, PdfName>();
/*     */   
/*     */   static {
/*  96 */     inlineImageEntryAbbreviationMap.put(PdfName.BITSPERCOMPONENT, PdfName.BITSPERCOMPONENT);
/*  97 */     inlineImageEntryAbbreviationMap.put(PdfName.COLORSPACE, PdfName.COLORSPACE);
/*  98 */     inlineImageEntryAbbreviationMap.put(PdfName.DECODE, PdfName.DECODE);
/*  99 */     inlineImageEntryAbbreviationMap.put(PdfName.DECODEPARMS, PdfName.DECODEPARMS);
/* 100 */     inlineImageEntryAbbreviationMap.put(PdfName.FILTER, PdfName.FILTER);
/* 101 */     inlineImageEntryAbbreviationMap.put(PdfName.HEIGHT, PdfName.HEIGHT);
/* 102 */     inlineImageEntryAbbreviationMap.put(PdfName.IMAGEMASK, PdfName.IMAGEMASK);
/* 103 */     inlineImageEntryAbbreviationMap.put(PdfName.INTENT, PdfName.INTENT);
/* 104 */     inlineImageEntryAbbreviationMap.put(PdfName.INTERPOLATE, PdfName.INTERPOLATE);
/* 105 */     inlineImageEntryAbbreviationMap.put(PdfName.WIDTH, PdfName.WIDTH);
/*     */ 
/*     */     
/* 108 */     inlineImageEntryAbbreviationMap.put(new PdfName("BPC"), PdfName.BITSPERCOMPONENT);
/* 109 */     inlineImageEntryAbbreviationMap.put(new PdfName("CS"), PdfName.COLORSPACE);
/* 110 */     inlineImageEntryAbbreviationMap.put(new PdfName("D"), PdfName.DECODE);
/* 111 */     inlineImageEntryAbbreviationMap.put(new PdfName("DP"), PdfName.DECODEPARMS);
/* 112 */     inlineImageEntryAbbreviationMap.put(new PdfName("F"), PdfName.FILTER);
/* 113 */     inlineImageEntryAbbreviationMap.put(new PdfName("H"), PdfName.HEIGHT);
/* 114 */     inlineImageEntryAbbreviationMap.put(new PdfName("IM"), PdfName.IMAGEMASK);
/* 115 */     inlineImageEntryAbbreviationMap.put(new PdfName("I"), PdfName.INTERPOLATE);
/* 116 */     inlineImageEntryAbbreviationMap.put(new PdfName("W"), PdfName.WIDTH);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   private static final Map<PdfName, PdfName> inlineImageColorSpaceAbbreviationMap = new HashMap<PdfName, PdfName>();
/*     */   static {
/* 126 */     inlineImageColorSpaceAbbreviationMap.put(new PdfName("G"), PdfName.DEVICEGRAY);
/* 127 */     inlineImageColorSpaceAbbreviationMap.put(new PdfName("RGB"), PdfName.DEVICERGB);
/* 128 */     inlineImageColorSpaceAbbreviationMap.put(new PdfName("CMYK"), PdfName.DEVICECMYK);
/* 129 */     inlineImageColorSpaceAbbreviationMap.put(new PdfName("I"), PdfName.INDEXED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   private static final Map<PdfName, PdfName> inlineImageFilterAbbreviationMap = new HashMap<PdfName, PdfName>();
/*     */   static {
/* 139 */     inlineImageFilterAbbreviationMap.put(new PdfName("AHx"), PdfName.ASCIIHEXDECODE);
/* 140 */     inlineImageFilterAbbreviationMap.put(new PdfName("A85"), PdfName.ASCII85DECODE);
/* 141 */     inlineImageFilterAbbreviationMap.put(new PdfName("LZW"), PdfName.LZWDECODE);
/* 142 */     inlineImageFilterAbbreviationMap.put(new PdfName("Fl"), PdfName.FLATEDECODE);
/* 143 */     inlineImageFilterAbbreviationMap.put(new PdfName("RL"), PdfName.RUNLENGTHDECODE);
/* 144 */     inlineImageFilterAbbreviationMap.put(new PdfName("CCF"), PdfName.CCITTFAXDECODE);
/* 145 */     inlineImageFilterAbbreviationMap.put(new PdfName("DCT"), PdfName.DCTDECODE);
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
/*     */   public static InlineImageInfo parseInlineImage(PdfContentParser ps, PdfDictionary colorSpaceDic) throws IOException {
/* 158 */     PdfDictionary inlineImageDictionary = parseInlineImageDictionary(ps);
/* 159 */     byte[] samples = parseInlineImageSamples(inlineImageDictionary, colorSpaceDic, ps);
/* 160 */     return new InlineImageInfo(samples, inlineImageDictionary);
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
/*     */   private static PdfDictionary parseInlineImageDictionary(PdfContentParser ps) throws IOException {
/* 172 */     PdfDictionary dictionary = new PdfDictionary();
/*     */     
/* 174 */     for (PdfObject key = ps.readPRObject(); key != null && !"ID".equals(key.toString()); key = ps.readPRObject()) {
/* 175 */       PdfObject value = ps.readPRObject();
/*     */       
/* 177 */       PdfName resolvedKey = inlineImageEntryAbbreviationMap.get(key);
/* 178 */       if (resolvedKey == null) {
/* 179 */         resolvedKey = (PdfName)key;
/*     */       }
/* 181 */       dictionary.put(resolvedKey, getAlternateValue(resolvedKey, value));
/*     */     } 
/*     */     
/* 184 */     int ch = ps.getTokeniser().read();
/* 185 */     if (!PRTokeniser.isWhitespace(ch)) {
/* 186 */       throw new IOException("Unexpected character " + ch + " found after ID in inline image");
/*     */     }
/* 188 */     return dictionary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static PdfObject getAlternateValue(PdfName key, PdfObject value) {
/* 198 */     if (key == PdfName.FILTER) {
/* 199 */       if (value instanceof PdfName) {
/* 200 */         PdfName altValue = inlineImageFilterAbbreviationMap.get(value);
/* 201 */         if (altValue != null)
/* 202 */           return (PdfObject)altValue; 
/* 203 */       } else if (value instanceof PdfArray) {
/* 204 */         PdfArray array = (PdfArray)value;
/* 205 */         PdfArray altArray = new PdfArray();
/* 206 */         int count = array.size();
/* 207 */         for (int i = 0; i < count; i++) {
/* 208 */           altArray.add(getAlternateValue(key, array.getPdfObject(i)));
/*     */         }
/* 210 */         return (PdfObject)altArray;
/*     */       } 
/* 212 */     } else if (key == PdfName.COLORSPACE) {
/* 213 */       PdfName altValue = inlineImageColorSpaceAbbreviationMap.get(value);
/* 214 */       if (altValue != null) {
/* 215 */         return (PdfObject)altValue;
/*     */       }
/*     */     } 
/* 218 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getComponentsPerPixel(PdfName colorSpaceName, PdfDictionary colorSpaceDic) {
/* 226 */     if (colorSpaceName == null)
/* 227 */       return 1; 
/* 228 */     if (colorSpaceName.equals(PdfName.DEVICEGRAY))
/* 229 */       return 1; 
/* 230 */     if (colorSpaceName.equals(PdfName.DEVICERGB))
/* 231 */       return 3; 
/* 232 */     if (colorSpaceName.equals(PdfName.DEVICECMYK)) {
/* 233 */       return 4;
/*     */     }
/* 235 */     if (colorSpaceDic != null) {
/* 236 */       PdfArray colorSpace = colorSpaceDic.getAsArray(colorSpaceName);
/* 237 */       if (colorSpace != null) {
/* 238 */         if (PdfName.INDEXED.equals(colorSpace.getAsName(0))) {
/* 239 */           return 1;
/*     */         }
/*     */       } else {
/*     */         
/* 243 */         PdfName tempName = colorSpaceDic.getAsName(colorSpaceName);
/* 244 */         if (tempName != null) {
/* 245 */           return getComponentsPerPixel(tempName, colorSpaceDic);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 250 */     throw new IllegalArgumentException("Unexpected color space " + colorSpaceName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int computeBytesPerRow(PdfDictionary imageDictionary, PdfDictionary colorSpaceDic) {
/* 261 */     PdfNumber wObj = imageDictionary.getAsNumber(PdfName.WIDTH);
/* 262 */     PdfNumber bpcObj = imageDictionary.getAsNumber(PdfName.BITSPERCOMPONENT);
/* 263 */     int cpp = getComponentsPerPixel(imageDictionary.getAsName(PdfName.COLORSPACE), colorSpaceDic);
/*     */     
/* 265 */     int w = wObj.intValue();
/* 266 */     int bpc = (bpcObj != null) ? bpcObj.intValue() : 1;
/*     */ 
/*     */     
/* 269 */     int bytesPerRow = (w * bpc * cpp + 7) / 8;
/*     */     
/* 271 */     return bytesPerRow;
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
/*     */   private static byte[] parseUnfilteredSamples(PdfDictionary imageDictionary, PdfDictionary colorSpaceDic, PdfContentParser ps) throws IOException {
/* 287 */     if (imageDictionary.contains(PdfName.FILTER)) {
/* 288 */       throw new IllegalArgumentException("Dictionary contains filters");
/*     */     }
/* 290 */     PdfNumber h = imageDictionary.getAsNumber(PdfName.HEIGHT);
/*     */     
/* 292 */     int bytesToRead = computeBytesPerRow(imageDictionary, colorSpaceDic) * h.intValue();
/* 293 */     byte[] bytes = new byte[bytesToRead];
/* 294 */     PRTokeniser tokeniser = ps.getTokeniser();
/*     */     
/* 296 */     int shouldBeWhiteSpace = tokeniser.read();
/*     */ 
/*     */     
/* 299 */     int startIndex = 0;
/* 300 */     if (!PRTokeniser.isWhitespace(shouldBeWhiteSpace) || shouldBeWhiteSpace == 0) {
/* 301 */       bytes[0] = (byte)shouldBeWhiteSpace;
/* 302 */       startIndex++;
/*     */     } 
/* 304 */     for (int i = startIndex; i < bytesToRead; i++) {
/* 305 */       int ch = tokeniser.read();
/* 306 */       if (ch == -1) {
/* 307 */         throw new InlineImageParseException("End of content stream reached before end of image data");
/*     */       }
/* 309 */       bytes[i] = (byte)ch;
/*     */     } 
/* 311 */     PdfObject ei = ps.readPRObject();
/* 312 */     if (!ei.toString().equals("EI")) {
/*     */ 
/*     */       
/* 315 */       PdfObject ei2 = ps.readPRObject();
/* 316 */       if (!ei2.toString().equals("EI"))
/* 317 */         throw new InlineImageParseException("EI not found after end of image data"); 
/*     */     } 
/* 319 */     return bytes;
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
/*     */   private static byte[] parseInlineImageSamples(PdfDictionary imageDictionary, PdfDictionary colorSpaceDic, PdfContentParser ps) throws IOException {
/* 335 */     if (!imageDictionary.contains(PdfName.FILTER)) {
/* 336 */       return parseUnfilteredSamples(imageDictionary, colorSpaceDic, ps);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 346 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 347 */     ByteArrayOutputStream accumulated = new ByteArrayOutputStream();
/*     */     
/* 349 */     int found = 0;
/* 350 */     PRTokeniser tokeniser = ps.getTokeniser();
/*     */     int ch;
/* 352 */     while ((ch = tokeniser.read()) != -1) {
/* 353 */       if (found == 0 && PRTokeniser.isWhitespace(ch)) {
/* 354 */         found++;
/* 355 */         accumulated.write(ch); continue;
/* 356 */       }  if (found == 1 && ch == 69) {
/* 357 */         found++;
/* 358 */         accumulated.write(ch); continue;
/* 359 */       }  if (found == 1 && PRTokeniser.isWhitespace(ch)) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 364 */         baos.write(accumulated.toByteArray());
/* 365 */         accumulated.reset();
/* 366 */         accumulated.write(ch); continue;
/* 367 */       }  if (found == 2 && ch == 73) {
/* 368 */         found++;
/* 369 */         accumulated.write(ch); continue;
/* 370 */       }  if (found == 3 && PRTokeniser.isWhitespace(ch)) {
/* 371 */         byte[] tmp = baos.toByteArray();
/* 372 */         if (inlineImageStreamBytesAreComplete(tmp, imageDictionary)) {
/* 373 */           return tmp;
/*     */         }
/* 375 */         baos.write(accumulated.toByteArray());
/* 376 */         accumulated.reset();
/*     */         
/* 378 */         baos.write(ch);
/* 379 */         found = 0;
/*     */         continue;
/*     */       } 
/* 382 */       baos.write(accumulated.toByteArray());
/* 383 */       accumulated.reset();
/*     */       
/* 385 */       baos.write(ch);
/* 386 */       found = 0;
/*     */     } 
/*     */     
/* 389 */     throw new InlineImageParseException("Could not find image data or EI");
/*     */   }
/*     */   
/*     */   private static boolean inlineImageStreamBytesAreComplete(byte[] samples, PdfDictionary imageDictionary) {
/*     */     try {
/* 394 */       PdfReader.decodeBytes(samples, imageDictionary, FilterHandlers.getDefaultFilterHandlers());
/* 395 */       return true;
/*     */     }
/* 397 */     catch (UnsupportedPdfException e) {
/* 398 */       LOGGER.warn(e.getMessage());
/* 399 */       return true;
/*     */     }
/* 401 */     catch (IOException e) {
/* 402 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/InlineImageUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */