/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import com.itextpdf.text.Version;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.exceptions.UnsupportedPdfException;
/*     */ import com.itextpdf.text.pdf.FilterHandlers;
/*     */ import com.itextpdf.text.pdf.PRStream;
/*     */ import com.itextpdf.text.pdf.PdfArray;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfReader;
/*     */ import com.itextpdf.text.pdf.PdfString;
/*     */ import com.itextpdf.text.pdf.codec.PngWriter;
/*     */ import com.itextpdf.text.pdf.codec.TiffWriter;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.imageio.ImageIO;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfImageObject
/*     */ {
/*     */   private PdfDictionary dictionary;
/*     */   private byte[] imageBytes;
/*     */   private PdfDictionary colorSpaceDic;
/*     */   
/*     */   public enum ImageBytesType
/*     */   {
/*  80 */     PNG("png"),
/*  81 */     JPG("jpg"),
/*  82 */     JP2("jp2"),
/*  83 */     CCITT("tif"),
/*  84 */     JBIG2("jbig2");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String fileExtension;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ImageBytesType(String fileExtension) {
/*  96 */       this.fileExtension = fileExtension;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getFileExtension() {
/* 103 */       return this.fileExtension;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class TrackingFilter
/*     */     implements FilterHandlers.FilterHandler
/*     */   {
/* 112 */     public PdfName lastFilterName = null;
/*     */     
/*     */     public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
/* 115 */       this.lastFilterName = filterName;
/* 116 */       return b;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private TrackingFilter() {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 127 */   private int pngColorType = -1;
/*     */   
/*     */   private int pngBitDepth;
/*     */   
/*     */   private int width;
/*     */   
/*     */   private int height;
/*     */   
/*     */   private int bpc;
/*     */   private byte[] palette;
/*     */   private byte[] icc;
/*     */   private int stride;
/* 139 */   private ImageBytesType streamContentType = null;
/*     */   
/*     */   public String getFileType() {
/* 142 */     return this.streamContentType.getFileExtension();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImageBytesType getImageBytesType() {
/* 149 */     return this.streamContentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfImageObject(PRStream stream) throws IOException {
/* 158 */     this((PdfDictionary)stream, PdfReader.getStreamBytesRaw(stream), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfImageObject(PRStream stream, PdfDictionary colorSpaceDic) throws IOException {
/* 168 */     this((PdfDictionary)stream, PdfReader.getStreamBytesRaw(stream), colorSpaceDic);
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
/*     */   protected PdfImageObject(PdfDictionary dictionary, byte[] samples, PdfDictionary colorSpaceDic) throws IOException {
/* 181 */     this.dictionary = dictionary;
/* 182 */     this.colorSpaceDic = colorSpaceDic;
/* 183 */     TrackingFilter trackingFilter = new TrackingFilter();
/* 184 */     Map<PdfName, FilterHandlers.FilterHandler> handlers = new HashMap<PdfName, FilterHandlers.FilterHandler>(FilterHandlers.getDefaultFilterHandlers());
/* 185 */     handlers.put(PdfName.JBIG2DECODE, trackingFilter);
/* 186 */     handlers.put(PdfName.DCTDECODE, trackingFilter);
/* 187 */     handlers.put(PdfName.JPXDECODE, trackingFilter);
/*     */     
/* 189 */     this.imageBytes = PdfReader.decodeBytes(samples, dictionary, handlers);
/*     */     
/* 191 */     if (trackingFilter.lastFilterName != null)
/* 192 */     { if (PdfName.JBIG2DECODE.equals(trackingFilter.lastFilterName)) {
/* 193 */         this.streamContentType = ImageBytesType.JBIG2;
/* 194 */       } else if (PdfName.DCTDECODE.equals(trackingFilter.lastFilterName)) {
/* 195 */         this.streamContentType = ImageBytesType.JPG;
/* 196 */       } else if (PdfName.JPXDECODE.equals(trackingFilter.lastFilterName)) {
/* 197 */         this.streamContentType = ImageBytesType.JP2;
/*     */       }  }
/* 199 */     else { decodeImageBytes(); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfObject get(PdfName key) {
/* 209 */     return this.dictionary.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDictionary getDictionary() {
/* 217 */     return this.dictionary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findColorspace(PdfObject colorspace, boolean allowIndexed) throws IOException {
/* 227 */     if (colorspace == null && this.bpc == 1) {
/* 228 */       this.stride = (this.width * this.bpc + 7) / 8;
/* 229 */       this.pngColorType = 0;
/*     */     }
/* 231 */     else if (PdfName.DEVICEGRAY.equals(colorspace)) {
/* 232 */       this.stride = (this.width * this.bpc + 7) / 8;
/* 233 */       this.pngColorType = 0;
/*     */     }
/* 235 */     else if (PdfName.DEVICERGB.equals(colorspace)) {
/* 236 */       if (this.bpc == 8 || this.bpc == 16) {
/* 237 */         this.stride = (this.width * this.bpc * 3 + 7) / 8;
/* 238 */         this.pngColorType = 2;
/*     */       }
/*     */     
/* 241 */     } else if (colorspace instanceof PdfArray) {
/* 242 */       PdfArray ca = (PdfArray)colorspace;
/* 243 */       PdfObject tyca = ca.getDirectObject(0);
/* 244 */       if (PdfName.CALGRAY.equals(tyca)) {
/* 245 */         this.stride = (this.width * this.bpc + 7) / 8;
/* 246 */         this.pngColorType = 0;
/*     */       }
/* 248 */       else if (PdfName.CALRGB.equals(tyca)) {
/* 249 */         if (this.bpc == 8 || this.bpc == 16) {
/* 250 */           this.stride = (this.width * this.bpc * 3 + 7) / 8;
/* 251 */           this.pngColorType = 2;
/*     */         }
/*     */       
/* 254 */       } else if (PdfName.ICCBASED.equals(tyca)) {
/* 255 */         PRStream pr = (PRStream)ca.getDirectObject(1);
/* 256 */         int n = pr.getAsNumber(PdfName.N).intValue();
/* 257 */         if (n == 1) {
/* 258 */           this.stride = (this.width * this.bpc + 7) / 8;
/* 259 */           this.pngColorType = 0;
/* 260 */           this.icc = PdfReader.getStreamBytes(pr);
/*     */         }
/* 262 */         else if (n == 3) {
/* 263 */           this.stride = (this.width * this.bpc * 3 + 7) / 8;
/* 264 */           this.pngColorType = 2;
/* 265 */           this.icc = PdfReader.getStreamBytes(pr);
/*     */         }
/*     */       
/* 268 */       } else if (allowIndexed && PdfName.INDEXED.equals(tyca)) {
/* 269 */         findColorspace(ca.getDirectObject(1), false);
/* 270 */         if (this.pngColorType == 2) {
/* 271 */           PdfObject id2 = ca.getDirectObject(3);
/* 272 */           if (id2 instanceof PdfString) {
/* 273 */             this.palette = ((PdfString)id2).getBytes();
/*     */           }
/* 275 */           else if (id2 instanceof PRStream) {
/* 276 */             this.palette = PdfReader.getStreamBytes((PRStream)id2);
/*     */           } 
/* 278 */           this.stride = (this.width * this.bpc + 7) / 8;
/* 279 */           this.pngColorType = 3;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void decodeImageBytes() throws IOException {
/* 291 */     if (this.streamContentType != null) {
/* 292 */       throw new IllegalStateException(MessageLocalization.getComposedMessage("Decoding.can't.happen.on.this.type.of.stream.(.1.)", new Object[] { this.streamContentType }));
/*     */     }
/* 294 */     this.pngColorType = -1;
/* 295 */     PdfArray decode = this.dictionary.getAsArray(PdfName.DECODE);
/* 296 */     this.width = this.dictionary.getAsNumber(PdfName.WIDTH).intValue();
/* 297 */     this.height = this.dictionary.getAsNumber(PdfName.HEIGHT).intValue();
/* 298 */     this.bpc = this.dictionary.getAsNumber(PdfName.BITSPERCOMPONENT).intValue();
/* 299 */     this.pngBitDepth = this.bpc;
/* 300 */     PdfObject colorspace = this.dictionary.getDirectObject(PdfName.COLORSPACE);
/* 301 */     if (colorspace instanceof PdfName && this.colorSpaceDic != null) {
/* 302 */       PdfObject csLookup = this.colorSpaceDic.getDirectObject((PdfName)colorspace);
/* 303 */       if (csLookup != null) {
/* 304 */         colorspace = csLookup;
/*     */       }
/*     */     } 
/* 307 */     this.palette = null;
/* 308 */     this.icc = null;
/* 309 */     this.stride = 0;
/* 310 */     findColorspace(colorspace, true);
/* 311 */     ByteArrayOutputStream ms = new ByteArrayOutputStream();
/* 312 */     if (this.pngColorType < 0) {
/* 313 */       if (this.bpc != 8) {
/* 314 */         throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("the.color.depth.1.is.not.supported", this.bpc));
/*     */       }
/* 316 */       if (!PdfName.DEVICECMYK.equals(colorspace))
/*     */       {
/* 318 */         if (colorspace instanceof PdfArray) {
/* 319 */           PdfArray ca = (PdfArray)colorspace;
/* 320 */           PdfObject tyca = ca.getDirectObject(0);
/* 321 */           if (!PdfName.ICCBASED.equals(tyca))
/* 322 */             throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("the.color.space.1.is.not.supported", new Object[] { colorspace })); 
/* 323 */           PRStream pr = (PRStream)ca.getDirectObject(1);
/* 324 */           int n = pr.getAsNumber(PdfName.N).intValue();
/* 325 */           if (n != 4) {
/* 326 */             throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("N.value.1.is.not.supported", n));
/*     */           }
/* 328 */           this.icc = PdfReader.getStreamBytes(pr);
/*     */         } else {
/*     */           
/* 331 */           throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("the.color.space.1.is.not.supported", new Object[] { colorspace }));
/* 332 */         }  }  this.stride = 4 * this.width;
/* 333 */       TiffWriter wr = new TiffWriter();
/* 334 */       wr.addField((TiffWriter.FieldBase)new TiffWriter.FieldShort(277, 4));
/* 335 */       wr.addField((TiffWriter.FieldBase)new TiffWriter.FieldShort(258, new int[] { 8, 8, 8, 8 }));
/* 336 */       wr.addField((TiffWriter.FieldBase)new TiffWriter.FieldShort(262, 5));
/* 337 */       wr.addField((TiffWriter.FieldBase)new TiffWriter.FieldLong(256, this.width));
/* 338 */       wr.addField((TiffWriter.FieldBase)new TiffWriter.FieldLong(257, this.height));
/* 339 */       wr.addField((TiffWriter.FieldBase)new TiffWriter.FieldShort(259, 5));
/* 340 */       wr.addField((TiffWriter.FieldBase)new TiffWriter.FieldShort(317, 2));
/* 341 */       wr.addField((TiffWriter.FieldBase)new TiffWriter.FieldLong(278, this.height));
/* 342 */       wr.addField((TiffWriter.FieldBase)new TiffWriter.FieldRational(282, new int[] { 300, 1 }));
/* 343 */       wr.addField((TiffWriter.FieldBase)new TiffWriter.FieldRational(283, new int[] { 300, 1 }));
/* 344 */       wr.addField((TiffWriter.FieldBase)new TiffWriter.FieldShort(296, 2));
/* 345 */       wr.addField((TiffWriter.FieldBase)new TiffWriter.FieldAscii(305, Version.getInstance().getVersion()));
/* 346 */       ByteArrayOutputStream comp = new ByteArrayOutputStream();
/* 347 */       TiffWriter.compressLZW(comp, 2, this.imageBytes, this.height, 4, this.stride);
/* 348 */       byte[] buf = comp.toByteArray();
/* 349 */       wr.addField((TiffWriter.FieldBase)new TiffWriter.FieldImage(buf));
/* 350 */       wr.addField((TiffWriter.FieldBase)new TiffWriter.FieldLong(279, buf.length));
/* 351 */       if (this.icc != null)
/* 352 */         wr.addField((TiffWriter.FieldBase)new TiffWriter.FieldUndefined(34675, this.icc)); 
/* 353 */       wr.writeFile(ms);
/* 354 */       this.streamContentType = ImageBytesType.CCITT;
/* 355 */       this.imageBytes = ms.toByteArray();
/*     */       return;
/*     */     } 
/* 358 */     PngWriter png = new PngWriter(ms);
/* 359 */     if (decode != null && 
/* 360 */       this.pngBitDepth == 1)
/*     */     {
/* 362 */       if (decode.getAsNumber(0).intValue() == 1 && decode.getAsNumber(1).intValue() == 0) {
/* 363 */         int len = this.imageBytes.length;
/* 364 */         for (int t = 0; t < len; t++) {
/* 365 */           this.imageBytes[t] = (byte)(this.imageBytes[t] ^ 0xFF);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 374 */     png.writeHeader(this.width, this.height, this.pngBitDepth, this.pngColorType);
/* 375 */     if (this.icc != null)
/* 376 */       png.writeIccProfile(this.icc); 
/* 377 */     if (this.palette != null)
/* 378 */       png.writePalette(this.palette); 
/* 379 */     png.writeData(this.imageBytes, this.stride);
/* 380 */     png.writeEnd();
/* 381 */     this.streamContentType = ImageBytesType.PNG;
/* 382 */     this.imageBytes = ms.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getImageAsBytes() {
/* 392 */     return this.imageBytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedImage getBufferedImage() throws IOException {
/* 401 */     byte[] img = getImageAsBytes();
/* 402 */     if (img == null)
/* 403 */       return null; 
/* 404 */     return ImageIO.read(new ByteArrayInputStream(img));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/PdfImageObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */