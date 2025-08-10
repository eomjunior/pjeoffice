/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfImage
/*     */   extends PdfStream
/*     */ {
/*     */   static final int TRANSFERSIZE = 4096;
/*  64 */   protected PdfName name = null;
/*     */   
/*  66 */   protected Image image = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfImage(Image image, String name, PdfIndirectReference maskRef) throws BadPdfFormatException {
/*  80 */     this.image = image;
/*  81 */     if (name == null) {
/*  82 */       generateImgResName(image);
/*     */     } else {
/*  84 */       this.name = new PdfName(name);
/*  85 */     }  put(PdfName.TYPE, PdfName.XOBJECT);
/*  86 */     put(PdfName.SUBTYPE, PdfName.IMAGE);
/*  87 */     put(PdfName.WIDTH, new PdfNumber(image.getWidth()));
/*  88 */     put(PdfName.HEIGHT, new PdfNumber(image.getHeight()));
/*  89 */     if (image.getLayer() != null)
/*  90 */       put(PdfName.OC, image.getLayer().getRef()); 
/*  91 */     if (image.isMask() && (image.getBpc() == 1 || image.getBpc() > 255))
/*  92 */       put(PdfName.IMAGEMASK, PdfBoolean.PDFTRUE); 
/*  93 */     if (maskRef != null)
/*  94 */       if (image.isSmask()) {
/*  95 */         put(PdfName.SMASK, maskRef);
/*     */       } else {
/*  97 */         put(PdfName.MASK, maskRef);
/*     */       }  
/*  99 */     if (image.isMask() && image.isInverted())
/* 100 */       put(PdfName.DECODE, new PdfLiteral("[1 0]")); 
/* 101 */     if (image.isInterpolation())
/* 102 */       put(PdfName.INTERPOLATE, PdfBoolean.PDFTRUE); 
/* 103 */     InputStream is = null;
/*     */     try {
/*     */       String errorID;
/* 106 */       int[] transparency = image.getTransparency();
/* 107 */       if (transparency != null && !image.isMask() && maskRef == null) {
/* 108 */         StringBuilder s = new StringBuilder("[");
/* 109 */         for (int k = 0; k < transparency.length; k++)
/* 110 */           s.append(transparency[k]).append(" "); 
/* 111 */         s.append("]");
/* 112 */         put(PdfName.MASK, new PdfLiteral(s.toString()));
/*     */       } 
/*     */       
/* 115 */       if (image.isImgRaw()) {
/*     */         
/* 117 */         int colorspace = image.getColorspace();
/* 118 */         this.bytes = image.getRawData();
/* 119 */         put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
/* 120 */         int bpc = image.getBpc();
/* 121 */         if (bpc > 255) {
/* 122 */           if (!image.isMask())
/* 123 */             put(PdfName.COLORSPACE, PdfName.DEVICEGRAY); 
/* 124 */           put(PdfName.BITSPERCOMPONENT, new PdfNumber(1));
/* 125 */           put(PdfName.FILTER, PdfName.CCITTFAXDECODE);
/* 126 */           int k = bpc - 257;
/* 127 */           PdfDictionary decodeparms = new PdfDictionary();
/* 128 */           if (k != 0)
/* 129 */             decodeparms.put(PdfName.K, new PdfNumber(k)); 
/* 130 */           if ((colorspace & 0x1) != 0)
/* 131 */             decodeparms.put(PdfName.BLACKIS1, PdfBoolean.PDFTRUE); 
/* 132 */           if ((colorspace & 0x2) != 0)
/* 133 */             decodeparms.put(PdfName.ENCODEDBYTEALIGN, PdfBoolean.PDFTRUE); 
/* 134 */           if ((colorspace & 0x4) != 0)
/* 135 */             decodeparms.put(PdfName.ENDOFLINE, PdfBoolean.PDFTRUE); 
/* 136 */           if ((colorspace & 0x8) != 0)
/* 137 */             decodeparms.put(PdfName.ENDOFBLOCK, PdfBoolean.PDFFALSE); 
/* 138 */           decodeparms.put(PdfName.COLUMNS, new PdfNumber(image.getWidth()));
/* 139 */           decodeparms.put(PdfName.ROWS, new PdfNumber(image.getHeight()));
/* 140 */           put(PdfName.DECODEPARMS, decodeparms);
/*     */         } else {
/*     */           
/* 143 */           switch (colorspace) {
/*     */             case 1:
/* 145 */               put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
/* 146 */               if (image.isInverted())
/* 147 */                 put(PdfName.DECODE, new PdfLiteral("[1 0]")); 
/*     */               break;
/*     */             case 3:
/* 150 */               put(PdfName.COLORSPACE, PdfName.DEVICERGB);
/* 151 */               if (image.isInverted()) {
/* 152 */                 put(PdfName.DECODE, new PdfLiteral("[1 0 1 0 1 0]"));
/*     */               }
/*     */               break;
/*     */             default:
/* 156 */               put(PdfName.COLORSPACE, PdfName.DEVICECMYK);
/* 157 */               if (image.isInverted())
/* 158 */                 put(PdfName.DECODE, new PdfLiteral("[1 0 1 0 1 0 1 0]"));  break;
/*     */           } 
/* 160 */           PdfDictionary additional = image.getAdditional();
/* 161 */           if (additional != null)
/* 162 */             putAll(additional); 
/* 163 */           if (image.isMask() && (image.getBpc() == 1 || image.getBpc() > 8))
/* 164 */             remove(PdfName.COLORSPACE); 
/* 165 */           put(PdfName.BITSPERCOMPONENT, new PdfNumber(image.getBpc()));
/* 166 */           if (image.isDeflated()) {
/* 167 */             put(PdfName.FILTER, PdfName.FLATEDECODE);
/*     */           } else {
/* 169 */             flateCompress(image.getCompressionLevel());
/*     */           } 
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 176 */       if (image.getRawData() == null) {
/* 177 */         is = image.getUrl().openStream();
/* 178 */         errorID = image.getUrl().toString();
/*     */       } else {
/*     */         
/* 181 */         is = new ByteArrayInputStream(image.getRawData());
/* 182 */         errorID = "Byte array";
/*     */       } 
/* 184 */       switch (image.type()) {
/*     */         case 32:
/* 186 */           put(PdfName.FILTER, PdfName.DCTDECODE);
/* 187 */           if (image.getColorTransform() == 0) {
/* 188 */             PdfDictionary decodeparms = new PdfDictionary();
/* 189 */             decodeparms.put(PdfName.COLORTRANSFORM, new PdfNumber(0));
/* 190 */             put(PdfName.DECODEPARMS, decodeparms);
/*     */           } 
/* 192 */           switch (image.getColorspace()) {
/*     */             case 1:
/* 194 */               put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
/*     */               break;
/*     */             case 3:
/* 197 */               put(PdfName.COLORSPACE, PdfName.DEVICERGB);
/*     */               break;
/*     */             default:
/* 200 */               put(PdfName.COLORSPACE, PdfName.DEVICECMYK);
/* 201 */               if (image.isInverted())
/* 202 */                 put(PdfName.DECODE, new PdfLiteral("[1 0 1 0 1 0 1 0]")); 
/*     */               break;
/*     */           } 
/* 205 */           put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
/* 206 */           if (image.getRawData() != null) {
/* 207 */             this.bytes = image.getRawData();
/* 208 */             put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
/*     */             return;
/*     */           } 
/* 211 */           this.streamBytes = new ByteArrayOutputStream();
/* 212 */           transferBytes(is, this.streamBytes, -1);
/*     */           break;
/*     */         case 33:
/* 215 */           put(PdfName.FILTER, PdfName.JPXDECODE);
/* 216 */           if (image.getColorspace() > 0) {
/* 217 */             switch (image.getColorspace()) {
/*     */               case 1:
/* 219 */                 put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
/*     */                 break;
/*     */               case 3:
/* 222 */                 put(PdfName.COLORSPACE, PdfName.DEVICERGB);
/*     */                 break;
/*     */               default:
/* 225 */                 put(PdfName.COLORSPACE, PdfName.DEVICECMYK); break;
/*     */             } 
/* 227 */             put(PdfName.BITSPERCOMPONENT, new PdfNumber(image.getBpc()));
/*     */           } 
/* 229 */           if (image.getRawData() != null) {
/* 230 */             this.bytes = image.getRawData();
/* 231 */             put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
/*     */             return;
/*     */           } 
/* 234 */           this.streamBytes = new ByteArrayOutputStream();
/* 235 */           transferBytes(is, this.streamBytes, -1);
/*     */           break;
/*     */         case 36:
/* 238 */           put(PdfName.FILTER, PdfName.JBIG2DECODE);
/* 239 */           put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
/* 240 */           put(PdfName.BITSPERCOMPONENT, new PdfNumber(1));
/* 241 */           if (image.getRawData() != null) {
/* 242 */             this.bytes = image.getRawData();
/* 243 */             put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
/*     */             return;
/*     */           } 
/* 246 */           this.streamBytes = new ByteArrayOutputStream();
/* 247 */           transferBytes(is, this.streamBytes, -1);
/*     */           break;
/*     */         default:
/* 250 */           throw new BadPdfFormatException(MessageLocalization.getComposedMessage("1.is.an.unknown.image.format", new Object[] { errorID }));
/*     */       } 
/* 252 */       if (image.getCompressionLevel() > 0)
/* 253 */         flateCompress(image.getCompressionLevel()); 
/* 254 */       put(PdfName.LENGTH, new PdfNumber(this.streamBytes.size()));
/*     */     }
/* 256 */     catch (IOException ioe) {
/* 257 */       throw new BadPdfFormatException(ioe.getMessage());
/*     */     } finally {
/*     */       
/* 260 */       if (is != null) {
/*     */         try {
/* 262 */           is.close();
/*     */         }
/* 264 */         catch (Exception exception) {}
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
/*     */   public PdfName name() {
/* 278 */     return this.name;
/*     */   }
/*     */   
/*     */   public Image getImage() {
/* 282 */     return this.image;
/*     */   }
/*     */   
/*     */   static void transferBytes(InputStream in, OutputStream out, int len) throws IOException {
/* 286 */     byte[] buffer = new byte[4096];
/* 287 */     if (len < 0) {
/* 288 */       len = 2147418112;
/*     */     }
/* 290 */     while (len != 0) {
/* 291 */       int size = in.read(buffer, 0, Math.min(len, 4096));
/* 292 */       if (size < 0)
/*     */         return; 
/* 294 */       out.write(buffer, 0, size);
/* 295 */       len -= size;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void importAll(PdfImage dup) {
/* 300 */     this.name = dup.name;
/* 301 */     this.compressed = dup.compressed;
/* 302 */     this.compressionLevel = dup.compressionLevel;
/* 303 */     this.streamBytes = dup.streamBytes;
/* 304 */     this.bytes = dup.bytes;
/* 305 */     this.hashMap = dup.hashMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateImgResName(Image img) {
/* 314 */     this.name = new PdfName("img" + Long.toHexString(img.getMySerialId().longValue()));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfImage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */