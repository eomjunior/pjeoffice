/*     */ package com.itextpdf.text.pdf.codec;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.ImgRaw;
/*     */ import com.itextpdf.text.Utilities;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.PdfArray;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfNumber;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfString;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GifImage
/*     */ {
/*     */   protected DataInputStream in;
/*     */   protected int width;
/*     */   protected int height;
/*     */   protected boolean gctFlag;
/*     */   protected int bgIndex;
/*     */   protected int bgColor;
/*     */   protected int pixelAspect;
/*     */   protected boolean lctFlag;
/*     */   protected boolean interlace;
/*     */   protected int lctSize;
/*     */   protected int ix;
/*     */   protected int iy;
/*     */   protected int iw;
/*     */   protected int ih;
/*  78 */   protected byte[] block = new byte[256];
/*  79 */   protected int blockSize = 0;
/*     */ 
/*     */   
/*  82 */   protected int dispose = 0;
/*     */   protected boolean transparency = false;
/*  84 */   protected int delay = 0;
/*     */   
/*     */   protected int transIndex;
/*     */   
/*     */   protected static final int MaxStackSize = 4096;
/*     */   
/*     */   protected short[] prefix;
/*     */   
/*     */   protected byte[] suffix;
/*     */   
/*     */   protected byte[] pixelStack;
/*     */   
/*     */   protected byte[] pixels;
/*     */   protected byte[] m_out;
/*     */   protected int m_bpc;
/*     */   protected int m_gbpc;
/*     */   protected byte[] m_global_table;
/*     */   protected byte[] m_local_table;
/*     */   protected byte[] m_curr_table;
/*     */   protected int m_line_stride;
/*     */   protected byte[] fromData;
/*     */   protected URL fromUrl;
/* 106 */   protected ArrayList<GifFrame> frames = new ArrayList<GifFrame>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GifImage(URL url) throws IOException {
/* 113 */     this.fromUrl = url;
/* 114 */     InputStream is = null;
/*     */     try {
/* 116 */       is = url.openStream();
/*     */       
/* 118 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 119 */       int read = 0;
/* 120 */       byte[] bytes = new byte[1024];
/*     */       
/* 122 */       while ((read = is.read(bytes)) != -1) {
/* 123 */         baos.write(bytes, 0, read);
/*     */       }
/* 125 */       is.close();
/*     */       
/* 127 */       is = new ByteArrayInputStream(baos.toByteArray());
/* 128 */       baos.flush();
/* 129 */       baos.close();
/*     */       
/* 131 */       process(is);
/*     */     } finally {
/*     */       
/* 134 */       if (is != null) {
/* 135 */         is.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GifImage(String file) throws IOException {
/* 145 */     this(Utilities.toURL(file));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GifImage(byte[] data) throws IOException {
/* 153 */     this.fromData = data;
/* 154 */     InputStream is = null;
/*     */     try {
/* 156 */       is = new ByteArrayInputStream(data);
/* 157 */       process(is);
/*     */     } finally {
/*     */       
/* 160 */       if (is != null) {
/* 161 */         is.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GifImage(InputStream is) throws IOException {
/* 171 */     process(is);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFrameCount() {
/* 178 */     return this.frames.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Image getImage(int frame) {
/* 186 */     GifFrame gf = this.frames.get(frame - 1);
/* 187 */     return gf.image;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getFramePosition(int frame) {
/* 196 */     GifFrame gf = this.frames.get(frame - 1);
/* 197 */     return new int[] { gf.ix, gf.iy };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getLogicalScreen() {
/* 207 */     return new int[] { this.width, this.height };
/*     */   }
/*     */   
/*     */   void process(InputStream is) throws IOException {
/* 211 */     this.in = new DataInputStream(new BufferedInputStream(is));
/* 212 */     readHeader();
/* 213 */     readContents();
/* 214 */     if (this.frames.isEmpty()) {
/* 215 */       throw new IOException(MessageLocalization.getComposedMessage("the.file.does.not.contain.any.valid.image", new Object[0]));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readHeader() throws IOException {
/* 222 */     StringBuilder id = new StringBuilder("");
/* 223 */     for (int i = 0; i < 6; i++)
/* 224 */       id.append((char)this.in.read()); 
/* 225 */     if (!id.toString().startsWith("GIF8")) {
/* 226 */       throw new IOException(MessageLocalization.getComposedMessage("gif.signature.nor.found", new Object[0]));
/*     */     }
/*     */     
/* 229 */     readLSD();
/* 230 */     if (this.gctFlag) {
/* 231 */       this.m_global_table = readColorTable(this.m_gbpc);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readLSD() throws IOException {
/* 241 */     this.width = readShort();
/* 242 */     this.height = readShort();
/*     */ 
/*     */     
/* 245 */     int packed = this.in.read();
/* 246 */     this.gctFlag = ((packed & 0x80) != 0);
/* 247 */     this.m_gbpc = (packed & 0x7) + 1;
/* 248 */     this.bgIndex = this.in.read();
/* 249 */     this.pixelAspect = this.in.read();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int readShort() throws IOException {
/* 257 */     return this.in.read() | this.in.read() << 8;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int readBlock() throws IOException {
/* 266 */     this.blockSize = this.in.read();
/* 267 */     if (this.blockSize <= 0) {
/* 268 */       return this.blockSize = 0;
/*     */     }
/* 270 */     this.blockSize = this.in.read(this.block, 0, this.blockSize);
/*     */     
/* 272 */     return this.blockSize;
/*     */   }
/*     */   
/*     */   protected byte[] readColorTable(int bpc) throws IOException {
/* 276 */     int ncolors = 1 << bpc;
/* 277 */     int nbytes = 3 * ncolors;
/* 278 */     bpc = newBpc(bpc);
/* 279 */     byte[] table = new byte[(1 << bpc) * 3];
/* 280 */     this.in.readFully(table, 0, nbytes);
/* 281 */     return table;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static int newBpc(int bpc) {
/* 286 */     switch (bpc) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 1:
/*     */       case 2:
/*     */       case 4:
/* 296 */         return bpc;
/*     */       case 3:
/*     */         return 4;
/*     */     } 
/*     */     return 8; } protected void readContents() throws IOException {
/* 301 */     boolean done = false;
/* 302 */     while (!done) {
/* 303 */       int code = this.in.read();
/* 304 */       switch (code) {
/*     */         
/*     */         case 44:
/* 307 */           readImage();
/*     */           continue;
/*     */         
/*     */         case 33:
/* 311 */           code = this.in.read();
/* 312 */           switch (code) {
/*     */             
/*     */             case 249:
/* 315 */               readGraphicControlExt();
/*     */               continue;
/*     */             
/*     */             case 255:
/* 319 */               readBlock();
/* 320 */               skip();
/*     */               continue;
/*     */           } 
/*     */           
/* 324 */           skip();
/*     */           continue;
/*     */       } 
/*     */ 
/*     */       
/* 329 */       done = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readImage() throws IOException {
/*     */     ImgRaw imgRaw;
/* 339 */     this.ix = readShort();
/* 340 */     this.iy = readShort();
/* 341 */     this.iw = readShort();
/* 342 */     this.ih = readShort();
/*     */     
/* 344 */     int packed = this.in.read();
/* 345 */     this.lctFlag = ((packed & 0x80) != 0);
/* 346 */     this.interlace = ((packed & 0x40) != 0);
/*     */ 
/*     */     
/* 349 */     this.lctSize = 2 << (packed & 0x7);
/* 350 */     this.m_bpc = newBpc(this.m_gbpc);
/* 351 */     if (this.lctFlag) {
/* 352 */       this.m_curr_table = readColorTable((packed & 0x7) + 1);
/* 353 */       this.m_bpc = newBpc((packed & 0x7) + 1);
/*     */     } else {
/*     */       
/* 356 */       this.m_curr_table = this.m_global_table;
/*     */     } 
/* 358 */     if (this.transparency && this.transIndex >= this.m_curr_table.length / 3)
/* 359 */       this.transparency = false; 
/* 360 */     if (this.transparency && this.m_bpc == 1) {
/* 361 */       byte[] tp = new byte[12];
/* 362 */       System.arraycopy(this.m_curr_table, 0, tp, 0, 6);
/* 363 */       this.m_curr_table = tp;
/* 364 */       this.m_bpc = 2;
/*     */     } 
/* 366 */     boolean skipZero = decodeImageData();
/* 367 */     if (!skipZero) {
/* 368 */       skip();
/*     */     }
/* 370 */     Image img = null;
/*     */     try {
/* 372 */       imgRaw = new ImgRaw(this.iw, this.ih, 1, this.m_bpc, this.m_out);
/* 373 */       PdfArray colorspace = new PdfArray();
/* 374 */       colorspace.add((PdfObject)PdfName.INDEXED);
/* 375 */       colorspace.add((PdfObject)PdfName.DEVICERGB);
/* 376 */       int len = this.m_curr_table.length;
/* 377 */       colorspace.add((PdfObject)new PdfNumber(len / 3 - 1));
/* 378 */       colorspace.add((PdfObject)new PdfString(this.m_curr_table));
/* 379 */       PdfDictionary ad = new PdfDictionary();
/* 380 */       ad.put(PdfName.COLORSPACE, (PdfObject)colorspace);
/* 381 */       imgRaw.setAdditional(ad);
/* 382 */       if (this.transparency) {
/* 383 */         imgRaw.setTransparency(new int[] { this.transIndex, this.transIndex });
/*     */       }
/*     */     }
/* 386 */     catch (Exception e) {
/* 387 */       throw new ExceptionConverter(e);
/*     */     } 
/* 389 */     imgRaw.setOriginalType(3);
/* 390 */     imgRaw.setOriginalData(this.fromData);
/* 391 */     imgRaw.setUrl(this.fromUrl);
/* 392 */     GifFrame gf = new GifFrame();
/* 393 */     gf.image = (Image)imgRaw;
/* 394 */     gf.ix = this.ix;
/* 395 */     gf.iy = this.iy;
/* 396 */     this.frames.add(gf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean decodeImageData() throws IOException {
/* 403 */     int NullCode = -1;
/* 404 */     int npix = this.iw * this.ih;
/*     */ 
/*     */     
/* 407 */     boolean skipZero = false;
/*     */     
/* 409 */     if (this.prefix == null)
/* 410 */       this.prefix = new short[4096]; 
/* 411 */     if (this.suffix == null)
/* 412 */       this.suffix = new byte[4096]; 
/* 413 */     if (this.pixelStack == null) {
/* 414 */       this.pixelStack = new byte[4097];
/*     */     }
/* 416 */     this.m_line_stride = (this.iw * this.m_bpc + 7) / 8;
/* 417 */     this.m_out = new byte[this.m_line_stride * this.ih];
/* 418 */     int pass = 1;
/* 419 */     int inc = this.interlace ? 8 : 1;
/* 420 */     int line = 0;
/* 421 */     int xpos = 0;
/*     */ 
/*     */ 
/*     */     
/* 425 */     int data_size = this.in.read();
/* 426 */     int clear = 1 << data_size;
/* 427 */     int end_of_information = clear + 1;
/* 428 */     int available = clear + 2;
/* 429 */     int old_code = NullCode;
/* 430 */     int code_size = data_size + 1;
/* 431 */     int code_mask = (1 << code_size) - 1; int code;
/* 432 */     for (code = 0; code < clear; code++) {
/* 433 */       this.prefix[code] = 0;
/* 434 */       this.suffix[code] = (byte)code;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 439 */     int bi = 0, top = bi, first = top, count = first, bits = count, datum = bits;
/*     */     int i;
/* 441 */     label75: for (i = 0; i < npix; ) {
/* 442 */       if (top == 0) {
/* 443 */         if (bits < code_size) {
/*     */           
/* 445 */           if (count == 0) {
/*     */             
/* 447 */             count = readBlock();
/* 448 */             if (count <= 0) {
/* 449 */               skipZero = true;
/*     */               break;
/*     */             } 
/* 452 */             bi = 0;
/*     */           } 
/* 454 */           datum += (this.block[bi] & 0xFF) << bits;
/* 455 */           bits += 8;
/* 456 */           bi++;
/* 457 */           count--;
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 463 */         code = datum & code_mask;
/* 464 */         datum >>= code_size;
/* 465 */         bits -= code_size;
/*     */ 
/*     */ 
/*     */         
/* 469 */         if (code > available || code == end_of_information)
/*     */           break; 
/* 471 */         if (code == clear) {
/*     */           
/* 473 */           code_size = data_size + 1;
/* 474 */           code_mask = (1 << code_size) - 1;
/* 475 */           available = clear + 2;
/* 476 */           old_code = NullCode;
/*     */           continue;
/*     */         } 
/* 479 */         if (old_code == NullCode) {
/* 480 */           this.pixelStack[top++] = this.suffix[code];
/* 481 */           old_code = code;
/* 482 */           first = code;
/*     */           continue;
/*     */         } 
/* 485 */         int in_code = code;
/* 486 */         if (code == available) {
/* 487 */           this.pixelStack[top++] = (byte)first;
/* 488 */           code = old_code;
/*     */         } 
/* 490 */         while (code > clear) {
/* 491 */           this.pixelStack[top++] = this.suffix[code];
/* 492 */           code = this.prefix[code];
/*     */         } 
/* 494 */         first = this.suffix[code] & 0xFF;
/*     */ 
/*     */ 
/*     */         
/* 498 */         if (available >= 4096)
/*     */           break; 
/* 500 */         this.pixelStack[top++] = (byte)first;
/* 501 */         this.prefix[available] = (short)old_code;
/* 502 */         this.suffix[available] = (byte)first;
/* 503 */         available++;
/* 504 */         if ((available & code_mask) == 0 && available < 4096) {
/* 505 */           code_size++;
/* 506 */           code_mask += available;
/*     */         } 
/* 508 */         old_code = in_code;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 513 */       top--;
/* 514 */       i++;
/*     */       
/* 516 */       setPixel(xpos, line, this.pixelStack[top]);
/* 517 */       xpos++;
/* 518 */       if (xpos >= this.iw) {
/* 519 */         xpos = 0;
/* 520 */         line += inc;
/* 521 */         if (line >= this.ih) {
/* 522 */           if (this.interlace)
/*     */             while (true) {
/* 524 */               pass++;
/* 525 */               switch (pass) {
/*     */                 case 2:
/* 527 */                   line = 4;
/*     */                   break;
/*     */                 case 3:
/* 530 */                   line = 2;
/* 531 */                   inc = 4;
/*     */                   break;
/*     */                 case 4:
/* 534 */                   line = 1;
/* 535 */                   inc = 2;
/*     */                   break;
/*     */                 default:
/* 538 */                   line = this.ih - 1;
/* 539 */                   inc = 0; break;
/*     */               } 
/* 541 */               if (line < this.ih)
/*     */                 continue label75; 
/*     */             }  
/* 544 */           line = this.ih - 1;
/* 545 */           inc = 0;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 550 */     return skipZero;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setPixel(int x, int y, int v) {
/* 555 */     if (this.m_bpc == 8) {
/* 556 */       int pos = x + this.iw * y;
/* 557 */       this.m_out[pos] = (byte)v;
/*     */     } else {
/*     */       
/* 560 */       int pos = this.m_line_stride * y + x / 8 / this.m_bpc;
/* 561 */       int vout = v << 8 - this.m_bpc * x % 8 / this.m_bpc - this.m_bpc;
/* 562 */       this.m_out[pos] = (byte)(this.m_out[pos] | vout);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void resetFrame() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readGraphicControlExt() throws IOException {
/* 579 */     this.in.read();
/* 580 */     int packed = this.in.read();
/* 581 */     this.dispose = (packed & 0x1C) >> 2;
/* 582 */     if (this.dispose == 0)
/* 583 */       this.dispose = 1; 
/* 584 */     this.transparency = ((packed & 0x1) != 0);
/* 585 */     this.delay = readShort() * 10;
/* 586 */     this.transIndex = this.in.read();
/* 587 */     this.in.read();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void skip() throws IOException {
/*     */     do {
/* 596 */       readBlock();
/* 597 */     } while (this.blockSize > 0);
/*     */   }
/*     */   
/*     */   static class GifFrame {
/*     */     Image image;
/*     */     int ix;
/*     */     int iy;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/GifImage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */