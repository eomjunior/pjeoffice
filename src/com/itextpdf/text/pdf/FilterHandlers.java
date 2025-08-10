/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.exceptions.UnsupportedPdfException;
/*     */ import com.itextpdf.text.pdf.codec.TIFFFaxDecoder;
/*     */ import com.itextpdf.text.pdf.codec.TIFFFaxDecompressor;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FilterHandlers
/*     */ {
/*     */   private static final Map<PdfName, FilterHandler> defaults;
/*     */   
/*     */   static {
/*  81 */     HashMap<PdfName, FilterHandler> map = new HashMap<PdfName, FilterHandler>();
/*     */     
/*  83 */     map.put(PdfName.FLATEDECODE, new Filter_FLATEDECODE());
/*  84 */     map.put(PdfName.FL, new Filter_FLATEDECODE());
/*  85 */     map.put(PdfName.ASCIIHEXDECODE, new Filter_ASCIIHEXDECODE());
/*  86 */     map.put(PdfName.AHX, new Filter_ASCIIHEXDECODE());
/*  87 */     map.put(PdfName.ASCII85DECODE, new Filter_ASCII85DECODE());
/*  88 */     map.put(PdfName.A85, new Filter_ASCII85DECODE());
/*  89 */     map.put(PdfName.LZWDECODE, new Filter_LZWDECODE());
/*  90 */     map.put(PdfName.CCITTFAXDECODE, new Filter_CCITTFAXDECODE());
/*  91 */     map.put(PdfName.CRYPT, new Filter_DoNothing());
/*  92 */     map.put(PdfName.RUNLENGTHDECODE, new Filter_RUNLENGTHDECODE());
/*     */     
/*  94 */     defaults = Collections.unmodifiableMap(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<PdfName, FilterHandler> getDefaultFilterHandlers() {
/* 101 */     return defaults;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteArrayOutputStream enableMemoryLimitsAwareHandler(PdfDictionary streamDictionary) {
/* 111 */     MemoryLimitsAwareOutputStream outputStream = new MemoryLimitsAwareOutputStream();
/* 112 */     MemoryLimitsAwareHandler memoryLimitsAwareHandler = null;
/* 113 */     if (streamDictionary instanceof PRStream && null != ((PRStream)streamDictionary).getReader()) {
/* 114 */       memoryLimitsAwareHandler = ((PRStream)streamDictionary).getReader().getMemoryLimitsAwareHandler();
/*     */     } else {
/*     */       
/* 117 */       memoryLimitsAwareHandler = new MemoryLimitsAwareHandler();
/*     */     } 
/* 119 */     if (null != memoryLimitsAwareHandler && memoryLimitsAwareHandler.considerCurrentPdfStream) {
/* 120 */       outputStream.setMaxStreamSize(memoryLimitsAwareHandler.getMaxSizeOfSingleDecompressedPdfStream());
/*     */     }
/* 122 */     return outputStream;
/*     */   }
/*     */   
/*     */   public static interface FilterHandler {
/*     */     byte[] decode(byte[] param1ArrayOfbyte, PdfName param1PdfName, PdfObject param1PdfObject, PdfDictionary param1PdfDictionary) throws IOException; }
/*     */   
/*     */   private static class Filter_FLATEDECODE implements FilterHandler {
/*     */     public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
/* 130 */       ByteArrayOutputStream out = FilterHandlers.enableMemoryLimitsAwareHandler(streamDictionary);
/* 131 */       b = PdfReader.FlateDecode(b, out);
/* 132 */       b = PdfReader.decodePredictor(b, decodeParams);
/* 133 */       return b;
/*     */     }
/*     */     
/*     */     private Filter_FLATEDECODE() {} }
/*     */   
/*     */   private static class Filter_ASCIIHEXDECODE implements FilterHandler {
/*     */     private Filter_ASCIIHEXDECODE() {}
/*     */     
/*     */     public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
/* 142 */       ByteArrayOutputStream out = FilterHandlers.enableMemoryLimitsAwareHandler(streamDictionary);
/* 143 */       b = PdfReader.ASCIIHexDecode(b, out);
/* 144 */       return b;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Filter_ASCII85DECODE
/*     */     implements FilterHandler {
/*     */     private Filter_ASCII85DECODE() {}
/*     */     
/*     */     public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
/* 153 */       ByteArrayOutputStream out = FilterHandlers.enableMemoryLimitsAwareHandler(streamDictionary);
/* 154 */       b = PdfReader.ASCII85Decode(b, out);
/* 155 */       return b;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Filter_LZWDECODE
/*     */     implements FilterHandler {
/*     */     private Filter_LZWDECODE() {}
/*     */     
/*     */     public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
/* 164 */       ByteArrayOutputStream out = FilterHandlers.enableMemoryLimitsAwareHandler(streamDictionary);
/* 165 */       b = PdfReader.LZWDecode(b, out);
/* 166 */       b = PdfReader.decodePredictor(b, decodeParams);
/* 167 */       return b;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Filter_CCITTFAXDECODE
/*     */     implements FilterHandler
/*     */   {
/*     */     private Filter_CCITTFAXDECODE() {}
/*     */     
/*     */     public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
/* 177 */       PdfNumber wn = (PdfNumber)PdfReader.getPdfObjectRelease(streamDictionary.get(PdfName.WIDTH));
/* 178 */       PdfNumber hn = (PdfNumber)PdfReader.getPdfObjectRelease(streamDictionary.get(PdfName.HEIGHT));
/* 179 */       if (wn == null || hn == null)
/* 180 */         throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("filter.ccittfaxdecode.is.only.supported.for.images", new Object[0])); 
/* 181 */       int width = wn.intValue();
/* 182 */       int height = hn.intValue();
/*     */       
/* 184 */       PdfDictionary param = (decodeParams instanceof PdfDictionary) ? (PdfDictionary)decodeParams : null;
/* 185 */       int k = 0;
/* 186 */       boolean blackIs1 = false;
/* 187 */       boolean byteAlign = false;
/* 188 */       if (param != null) {
/* 189 */         PdfNumber kn = param.getAsNumber(PdfName.K);
/* 190 */         if (kn != null)
/* 191 */           k = kn.intValue(); 
/* 192 */         PdfBoolean bo = param.getAsBoolean(PdfName.BLACKIS1);
/* 193 */         if (bo != null)
/* 194 */           blackIs1 = bo.booleanValue(); 
/* 195 */         bo = param.getAsBoolean(PdfName.ENCODEDBYTEALIGN);
/* 196 */         if (bo != null)
/* 197 */           byteAlign = bo.booleanValue(); 
/*     */       } 
/* 199 */       byte[] outBuf = new byte[(width + 7) / 8 * height];
/* 200 */       TIFFFaxDecompressor decoder = new TIFFFaxDecompressor();
/* 201 */       if (k == 0 || k > 0) {
/* 202 */         int tiffT4Options = (k > 0) ? 1 : 0;
/* 203 */         tiffT4Options |= byteAlign ? 4 : 0;
/* 204 */         decoder.SetOptions(1, 3, tiffT4Options, 0);
/* 205 */         decoder.decodeRaw(outBuf, b, width, height);
/* 206 */         if (decoder.fails > 0) {
/* 207 */           byte[] outBuf2 = new byte[(width + 7) / 8 * height];
/* 208 */           int oldFails = decoder.fails;
/* 209 */           decoder.SetOptions(1, 2, tiffT4Options, 0);
/* 210 */           decoder.decodeRaw(outBuf2, b, width, height);
/* 211 */           if (decoder.fails < oldFails) {
/* 212 */             outBuf = outBuf2;
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         
/* 217 */         TIFFFaxDecoder deca = new TIFFFaxDecoder(1L, width, height);
/* 218 */         deca.decodeT6(outBuf, b, 0, height, 0L);
/*     */       } 
/* 220 */       if (!blackIs1) {
/* 221 */         int len = outBuf.length;
/* 222 */         for (int t = 0; t < len; t++) {
/* 223 */           outBuf[t] = (byte)(outBuf[t] ^ 0xFF);
/*     */         }
/*     */       } 
/* 226 */       b = outBuf;
/* 227 */       return b;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Filter_DoNothing
/*     */     implements FilterHandler {
/*     */     private Filter_DoNothing() {}
/*     */     
/*     */     public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
/* 236 */       return b;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Filter_RUNLENGTHDECODE
/*     */     implements FilterHandler
/*     */   {
/*     */     private Filter_RUNLENGTHDECODE() {}
/*     */     
/*     */     public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
/* 247 */       ByteArrayOutputStream out = FilterHandlers.enableMemoryLimitsAwareHandler(streamDictionary);
/* 248 */       byte dupCount = -1;
/* 249 */       for (int i = 0; i < b.length; i++) {
/* 250 */         dupCount = b[i];
/* 251 */         if (dupCount == Byte.MIN_VALUE)
/*     */           break; 
/* 253 */         if (dupCount >= 0 && dupCount <= Byte.MAX_VALUE) {
/* 254 */           int bytesToCopy = dupCount + 1;
/* 255 */           out.write(b, i, bytesToCopy);
/* 256 */           i += bytesToCopy;
/*     */         } else {
/*     */           
/* 259 */           i++;
/* 260 */           for (int j = 0; j < 1 - dupCount; j++) {
/* 261 */             out.write(b[i]);
/*     */           }
/*     */         } 
/*     */       } 
/* 265 */       return out.toByteArray();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/FilterHandlers.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */