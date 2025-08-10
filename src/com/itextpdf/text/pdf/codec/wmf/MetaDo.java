/*     */ package com.itextpdf.text.pdf.codec.wmf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.pdf.BaseFont;
/*     */ import com.itextpdf.text.pdf.PdfContentByte;
/*     */ import com.itextpdf.text.pdf.codec.BmpImage;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MetaDo
/*     */ {
/*     */   public static final int META_SETBKCOLOR = 513;
/*     */   public static final int META_SETBKMODE = 258;
/*     */   public static final int META_SETMAPMODE = 259;
/*     */   public static final int META_SETROP2 = 260;
/*     */   public static final int META_SETRELABS = 261;
/*     */   public static final int META_SETPOLYFILLMODE = 262;
/*     */   public static final int META_SETSTRETCHBLTMODE = 263;
/*     */   public static final int META_SETTEXTCHAREXTRA = 264;
/*     */   public static final int META_SETTEXTCOLOR = 521;
/*     */   public static final int META_SETTEXTJUSTIFICATION = 522;
/*     */   public static final int META_SETWINDOWORG = 523;
/*     */   public static final int META_SETWINDOWEXT = 524;
/*     */   public static final int META_SETVIEWPORTORG = 525;
/*     */   public static final int META_SETVIEWPORTEXT = 526;
/*     */   public static final int META_OFFSETWINDOWORG = 527;
/*     */   public static final int META_SCALEWINDOWEXT = 1040;
/*     */   public static final int META_OFFSETVIEWPORTORG = 529;
/*     */   public static final int META_SCALEVIEWPORTEXT = 1042;
/*     */   public static final int META_LINETO = 531;
/*     */   public static final int META_MOVETO = 532;
/*     */   public static final int META_EXCLUDECLIPRECT = 1045;
/*     */   public static final int META_INTERSECTCLIPRECT = 1046;
/*     */   public static final int META_ARC = 2071;
/*     */   public static final int META_ELLIPSE = 1048;
/*     */   public static final int META_FLOODFILL = 1049;
/*     */   public static final int META_PIE = 2074;
/*     */   public static final int META_RECTANGLE = 1051;
/*     */   public static final int META_ROUNDRECT = 1564;
/*     */   public static final int META_PATBLT = 1565;
/*     */   public static final int META_SAVEDC = 30;
/*     */   public static final int META_SETPIXEL = 1055;
/*     */   public static final int META_OFFSETCLIPRGN = 544;
/*     */   public static final int META_TEXTOUT = 1313;
/*     */   public static final int META_BITBLT = 2338;
/*     */   public static final int META_STRETCHBLT = 2851;
/*     */   public static final int META_POLYGON = 804;
/*     */   public static final int META_POLYLINE = 805;
/*     */   public static final int META_ESCAPE = 1574;
/*     */   public static final int META_RESTOREDC = 295;
/*     */   public static final int META_FILLREGION = 552;
/*     */   public static final int META_FRAMEREGION = 1065;
/*     */   public static final int META_INVERTREGION = 298;
/*     */   public static final int META_PAINTREGION = 299;
/*     */   public static final int META_SELECTCLIPREGION = 300;
/*     */   public static final int META_SELECTOBJECT = 301;
/*     */   public static final int META_SETTEXTALIGN = 302;
/*     */   public static final int META_CHORD = 2096;
/*     */   public static final int META_SETMAPPERFLAGS = 561;
/*     */   public static final int META_EXTTEXTOUT = 2610;
/*     */   public static final int META_SETDIBTODEV = 3379;
/*     */   public static final int META_SELECTPALETTE = 564;
/*     */   public static final int META_REALIZEPALETTE = 53;
/*     */   public static final int META_ANIMATEPALETTE = 1078;
/*     */   public static final int META_SETPALENTRIES = 55;
/*     */   public static final int META_POLYPOLYGON = 1336;
/*     */   public static final int META_RESIZEPALETTE = 313;
/*     */   public static final int META_DIBBITBLT = 2368;
/*     */   public static final int META_DIBSTRETCHBLT = 2881;
/*     */   public static final int META_DIBCREATEPATTERNBRUSH = 322;
/*     */   public static final int META_STRETCHDIB = 3907;
/*     */   public static final int META_EXTFLOODFILL = 1352;
/*     */   public static final int META_DELETEOBJECT = 496;
/*     */   public static final int META_CREATEPALETTE = 247;
/*     */   public static final int META_CREATEPATTERNBRUSH = 505;
/*     */   public static final int META_CREATEPENINDIRECT = 762;
/*     */   public static final int META_CREATEFONTINDIRECT = 763;
/*     */   public static final int META_CREATEBRUSHINDIRECT = 764;
/*     */   public static final int META_CREATEREGION = 1791;
/*     */   public PdfContentByte cb;
/*     */   public InputMeta in;
/*     */   int left;
/*     */   int top;
/*     */   int right;
/*     */   int bottom;
/*     */   int inch;
/* 139 */   MetaState state = new MetaState();
/*     */   
/*     */   public MetaDo(InputStream in, PdfContentByte cb) {
/* 142 */     this.cb = cb;
/* 143 */     this.in = new InputMeta(in);
/*     */   }
/*     */   
/*     */   public void readAll() throws IOException, DocumentException {
/* 147 */     if (this.in.readInt() != -1698247209) {
/* 148 */       throw new DocumentException(MessageLocalization.getComposedMessage("not.a.placeable.windows.metafile", new Object[0]));
/*     */     }
/* 150 */     this.in.readWord();
/* 151 */     this.left = this.in.readShort();
/* 152 */     this.top = this.in.readShort();
/* 153 */     this.right = this.in.readShort();
/* 154 */     this.bottom = this.in.readShort();
/* 155 */     this.inch = this.in.readWord();
/* 156 */     this.state.setScalingX((this.right - this.left) / this.inch * 72.0F);
/* 157 */     this.state.setScalingY((this.bottom - this.top) / this.inch * 72.0F);
/* 158 */     this.state.setOffsetWx(this.left);
/* 159 */     this.state.setOffsetWy(this.top);
/* 160 */     this.state.setExtentWx(this.right - this.left);
/* 161 */     this.state.setExtentWy(this.bottom - this.top);
/* 162 */     this.in.readInt();
/* 163 */     this.in.readWord();
/* 164 */     this.in.skip(18);
/*     */ 
/*     */ 
/*     */     
/* 168 */     this.cb.setLineCap(1);
/* 169 */     this.cb.setLineJoin(1); while (true) {
/*     */       MetaPen pen; MetaBrush brush; MetaFont font; int idx, m, len, numPoly, i; float yend, f1, h, b; int y, count; BaseColor color; int rop; Point p; int i2, sx, lens[], i1; float xend, f2, w, r; int x; byte[] text; int n, srcHeight; Point point1; int i7, sy, i6, j, i5; float ystart, f4, f3, t; int i4, k, i3, srcWidth, i9, i8; float xstart, f6, f5, l; int flag; String s; int ySrc; float f8, f7; int x1, i10, xSrc; float f10, f9; int y1, i11; float destHeight, f11; int x2; float destWidth, f12; int y2; float yDest, f13; double cx; byte[] arrayOfByte1; float xDest, cy; int i12; byte[] arrayOfByte2; float f14; double arc1, d1; String str1; int i13; float arc2; double d3, d2; ArrayList<double[]> ar; double d4, pt[]; int i14; ArrayList<double[]> arrayList1; double[] arrayOfDouble1;
/* 171 */       int i15, lenMarker = this.in.getLength();
/* 172 */       int tsize = this.in.readInt();
/* 173 */       if (tsize < 3)
/*     */         break; 
/* 175 */       int function = this.in.readWord();
/* 176 */       switch (function) {
/*     */ 
/*     */         
/*     */         case 247:
/*     */         case 322:
/*     */         case 1791:
/* 182 */           this.state.addMetaObject(new MetaObject());
/*     */           break;
/*     */         
/*     */         case 762:
/* 186 */           pen = new MetaPen();
/* 187 */           pen.init(this.in);
/* 188 */           this.state.addMetaObject(pen);
/*     */           break;
/*     */ 
/*     */         
/*     */         case 764:
/* 193 */           brush = new MetaBrush();
/* 194 */           brush.init(this.in);
/* 195 */           this.state.addMetaObject(brush);
/*     */           break;
/*     */ 
/*     */         
/*     */         case 763:
/* 200 */           font = new MetaFont();
/* 201 */           font.init(this.in);
/* 202 */           this.state.addMetaObject(font);
/*     */           break;
/*     */ 
/*     */         
/*     */         case 301:
/* 207 */           idx = this.in.readWord();
/* 208 */           this.state.selectMetaObject(idx, this.cb);
/*     */           break;
/*     */ 
/*     */         
/*     */         case 496:
/* 213 */           idx = this.in.readWord();
/* 214 */           this.state.deleteMetaObject(idx);
/*     */           break;
/*     */         
/*     */         case 30:
/* 218 */           this.state.saveState(this.cb);
/*     */           break;
/*     */         
/*     */         case 295:
/* 222 */           idx = this.in.readShort();
/* 223 */           this.state.restoreState(idx, this.cb);
/*     */           break;
/*     */         
/*     */         case 523:
/* 227 */           this.state.setOffsetWy(this.in.readShort());
/* 228 */           this.state.setOffsetWx(this.in.readShort());
/*     */           break;
/*     */         case 524:
/* 231 */           this.state.setExtentWy(this.in.readShort());
/* 232 */           this.state.setExtentWx(this.in.readShort());
/*     */           break;
/*     */         
/*     */         case 532:
/* 236 */           m = this.in.readShort();
/* 237 */           p = new Point(this.in.readShort(), m);
/* 238 */           this.state.setCurrentPoint(p);
/*     */           break;
/*     */ 
/*     */         
/*     */         case 531:
/* 243 */           m = this.in.readShort();
/* 244 */           i2 = this.in.readShort();
/* 245 */           point1 = this.state.getCurrentPoint();
/* 246 */           this.cb.moveTo(this.state.transformX(point1.x), this.state.transformY(point1.y));
/* 247 */           this.cb.lineTo(this.state.transformX(i2), this.state.transformY(m));
/* 248 */           this.cb.stroke();
/* 249 */           this.state.setCurrentPoint(new Point(i2, m));
/*     */           break;
/*     */ 
/*     */         
/*     */         case 805:
/* 254 */           this.state.setLineJoinPolygon(this.cb);
/* 255 */           len = this.in.readWord();
/* 256 */           i2 = this.in.readShort();
/* 257 */           i7 = this.in.readShort();
/* 258 */           this.cb.moveTo(this.state.transformX(i2), this.state.transformY(i7));
/* 259 */           for (i9 = 1; i9 < len; i9++) {
/* 260 */             i2 = this.in.readShort();
/* 261 */             i7 = this.in.readShort();
/* 262 */             this.cb.lineTo(this.state.transformX(i2), this.state.transformY(i7));
/*     */           } 
/* 264 */           this.cb.stroke();
/*     */           break;
/*     */ 
/*     */         
/*     */         case 804:
/* 269 */           if (isNullStrokeFill(false))
/*     */             break; 
/* 271 */           len = this.in.readWord();
/* 272 */           sx = this.in.readShort();
/* 273 */           sy = this.in.readShort();
/* 274 */           this.cb.moveTo(this.state.transformX(sx), this.state.transformY(sy));
/* 275 */           for (i9 = 1; i9 < len; i9++) {
/* 276 */             int i16 = this.in.readShort();
/* 277 */             int i17 = this.in.readShort();
/* 278 */             this.cb.lineTo(this.state.transformX(i16), this.state.transformY(i17));
/*     */           } 
/* 280 */           this.cb.lineTo(this.state.transformX(sx), this.state.transformY(sy));
/* 281 */           strokeAndFill();
/*     */           break;
/*     */ 
/*     */         
/*     */         case 1336:
/* 286 */           if (isNullStrokeFill(false))
/*     */             break; 
/* 288 */           numPoly = this.in.readWord();
/* 289 */           lens = new int[numPoly];
/* 290 */           for (i6 = 0; i6 < lens.length; i6++)
/* 291 */             lens[i6] = this.in.readWord(); 
/* 292 */           for (j = 0; j < lens.length; j++) {
/* 293 */             int i16 = lens[j];
/* 294 */             int i17 = this.in.readShort();
/* 295 */             int i18 = this.in.readShort();
/* 296 */             this.cb.moveTo(this.state.transformX(i17), this.state.transformY(i18));
/* 297 */             for (int i19 = 1; i19 < i16; i19++) {
/* 298 */               int i20 = this.in.readShort();
/* 299 */               int i21 = this.in.readShort();
/* 300 */               this.cb.lineTo(this.state.transformX(i20), this.state.transformY(i21));
/*     */             } 
/* 302 */             this.cb.lineTo(this.state.transformX(i17), this.state.transformY(i18));
/*     */           } 
/* 304 */           strokeAndFill();
/*     */           break;
/*     */ 
/*     */         
/*     */         case 1048:
/* 309 */           if (isNullStrokeFill(this.state.getLineNeutral()))
/*     */             break; 
/* 311 */           i = this.in.readShort();
/* 312 */           i1 = this.in.readShort();
/* 313 */           i5 = this.in.readShort();
/* 314 */           i8 = this.in.readShort();
/* 315 */           this.cb.arc(this.state.transformX(i8), this.state.transformY(i), this.state.transformX(i1), this.state.transformY(i5), 0.0F, 360.0F);
/* 316 */           strokeAndFill();
/*     */           break;
/*     */ 
/*     */         
/*     */         case 2071:
/* 321 */           if (isNullStrokeFill(this.state.getLineNeutral()))
/*     */             break; 
/* 323 */           yend = this.state.transformY(this.in.readShort());
/* 324 */           xend = this.state.transformX(this.in.readShort());
/* 325 */           ystart = this.state.transformY(this.in.readShort());
/* 326 */           xstart = this.state.transformX(this.in.readShort());
/* 327 */           f8 = this.state.transformY(this.in.readShort());
/* 328 */           f10 = this.state.transformX(this.in.readShort());
/* 329 */           f11 = this.state.transformY(this.in.readShort());
/* 330 */           f12 = this.state.transformX(this.in.readShort());
/* 331 */           f13 = (f10 + f12) / 2.0F;
/* 332 */           cy = (f11 + f8) / 2.0F;
/* 333 */           f14 = getArc(f13, cy, xstart, ystart);
/* 334 */           arc2 = getArc(f13, cy, xend, yend);
/* 335 */           arc2 -= f14;
/* 336 */           if (arc2 <= 0.0F)
/* 337 */             arc2 += 360.0F; 
/* 338 */           this.cb.arc(f12, f8, f10, f11, f14, arc2);
/* 339 */           this.cb.stroke();
/*     */           break;
/*     */ 
/*     */         
/*     */         case 2074:
/* 344 */           if (isNullStrokeFill(this.state.getLineNeutral()))
/*     */             break; 
/* 346 */           yend = this.state.transformY(this.in.readShort());
/* 347 */           xend = this.state.transformX(this.in.readShort());
/* 348 */           ystart = this.state.transformY(this.in.readShort());
/* 349 */           xstart = this.state.transformX(this.in.readShort());
/* 350 */           f8 = this.state.transformY(this.in.readShort());
/* 351 */           f10 = this.state.transformX(this.in.readShort());
/* 352 */           f11 = this.state.transformY(this.in.readShort());
/* 353 */           f12 = this.state.transformX(this.in.readShort());
/* 354 */           f13 = (f10 + f12) / 2.0F;
/* 355 */           cy = (f11 + f8) / 2.0F;
/* 356 */           arc1 = getArc(f13, cy, xstart, ystart);
/* 357 */           d3 = getArc(f13, cy, xend, yend);
/* 358 */           d3 -= arc1;
/* 359 */           if (d3 <= 0.0D)
/* 360 */             d3 += 360.0D; 
/* 361 */           ar = PdfContentByte.bezierArc(f12, f8, f10, f11, arc1, d3);
/* 362 */           if (ar.isEmpty())
/*     */             break; 
/* 364 */           pt = ar.get(0);
/* 365 */           this.cb.moveTo(f13, cy);
/* 366 */           this.cb.lineTo(pt[0], pt[1]);
/* 367 */           for (i14 = 0; i14 < ar.size(); i14++) {
/* 368 */             pt = ar.get(i14);
/* 369 */             this.cb.curveTo(pt[2], pt[3], pt[4], pt[5], pt[6], pt[7]);
/*     */           } 
/* 371 */           this.cb.lineTo(f13, cy);
/* 372 */           strokeAndFill();
/*     */           break;
/*     */ 
/*     */         
/*     */         case 2096:
/* 377 */           if (isNullStrokeFill(this.state.getLineNeutral()))
/*     */             break; 
/* 379 */           yend = this.state.transformY(this.in.readShort());
/* 380 */           xend = this.state.transformX(this.in.readShort());
/* 381 */           ystart = this.state.transformY(this.in.readShort());
/* 382 */           xstart = this.state.transformX(this.in.readShort());
/* 383 */           f8 = this.state.transformY(this.in.readShort());
/* 384 */           f10 = this.state.transformX(this.in.readShort());
/* 385 */           f11 = this.state.transformY(this.in.readShort());
/* 386 */           f12 = this.state.transformX(this.in.readShort());
/* 387 */           cx = ((f10 + f12) / 2.0F);
/* 388 */           d1 = ((f11 + f8) / 2.0F);
/* 389 */           d2 = getArc(cx, d1, xstart, ystart);
/* 390 */           d4 = getArc(cx, d1, xend, yend);
/* 391 */           d4 -= d2;
/* 392 */           if (d4 <= 0.0D)
/* 393 */             d4 += 360.0D; 
/* 394 */           arrayList1 = PdfContentByte.bezierArc(f12, f8, f10, f11, d2, d4);
/* 395 */           if (arrayList1.isEmpty())
/*     */             break; 
/* 397 */           arrayOfDouble1 = arrayList1.get(0);
/* 398 */           cx = arrayOfDouble1[0];
/* 399 */           d1 = arrayOfDouble1[1];
/* 400 */           this.cb.moveTo(cx, d1);
/* 401 */           for (i15 = 0; i15 < arrayList1.size(); i15++) {
/* 402 */             arrayOfDouble1 = arrayList1.get(i15);
/* 403 */             this.cb.curveTo(arrayOfDouble1[2], arrayOfDouble1[3], arrayOfDouble1[4], arrayOfDouble1[5], arrayOfDouble1[6], arrayOfDouble1[7]);
/*     */           } 
/* 405 */           this.cb.lineTo(cx, d1);
/* 406 */           strokeAndFill();
/*     */           break;
/*     */ 
/*     */         
/*     */         case 1051:
/* 411 */           if (isNullStrokeFill(true))
/*     */             break; 
/* 413 */           f1 = this.state.transformY(this.in.readShort());
/* 414 */           f2 = this.state.transformX(this.in.readShort());
/* 415 */           f4 = this.state.transformY(this.in.readShort());
/* 416 */           f6 = this.state.transformX(this.in.readShort());
/* 417 */           this.cb.rectangle(f6, f1, f2 - f6, f4 - f1);
/* 418 */           strokeAndFill();
/*     */           break;
/*     */ 
/*     */         
/*     */         case 1564:
/* 423 */           if (isNullStrokeFill(true))
/*     */             break; 
/* 425 */           h = this.state.transformY(0) - this.state.transformY(this.in.readShort());
/* 426 */           w = this.state.transformX(this.in.readShort()) - this.state.transformX(0);
/* 427 */           f3 = this.state.transformY(this.in.readShort());
/* 428 */           f5 = this.state.transformX(this.in.readShort());
/* 429 */           f7 = this.state.transformY(this.in.readShort());
/* 430 */           f9 = this.state.transformX(this.in.readShort());
/* 431 */           this.cb.roundRectangle(f9, f3, f5 - f9, f7 - f3, (h + w) / 4.0F);
/* 432 */           strokeAndFill();
/*     */           break;
/*     */ 
/*     */         
/*     */         case 1046:
/* 437 */           b = this.state.transformY(this.in.readShort());
/* 438 */           r = this.state.transformX(this.in.readShort());
/* 439 */           t = this.state.transformY(this.in.readShort());
/* 440 */           l = this.state.transformX(this.in.readShort());
/* 441 */           this.cb.rectangle(l, b, r - l, t - b);
/* 442 */           this.cb.eoClip();
/* 443 */           this.cb.newPath();
/*     */           break;
/*     */ 
/*     */         
/*     */         case 2610:
/* 448 */           y = this.in.readShort();
/* 449 */           x = this.in.readShort();
/* 450 */           i4 = this.in.readWord();
/* 451 */           flag = this.in.readWord();
/* 452 */           x1 = 0;
/* 453 */           y1 = 0;
/* 454 */           x2 = 0;
/* 455 */           y2 = 0;
/* 456 */           if ((flag & 0x6) != 0) {
/* 457 */             x1 = this.in.readShort();
/* 458 */             y1 = this.in.readShort();
/* 459 */             x2 = this.in.readShort();
/* 460 */             y2 = this.in.readShort();
/*     */           } 
/* 462 */           arrayOfByte1 = new byte[i4];
/*     */           
/* 464 */           for (i12 = 0; i12 < i4; i12++) {
/* 465 */             byte c = (byte)this.in.readByte();
/* 466 */             if (c == 0)
/*     */               break; 
/* 468 */             arrayOfByte1[i12] = c;
/*     */           } 
/*     */           
/*     */           try {
/* 472 */             str1 = new String(arrayOfByte1, 0, i12, "Cp1252");
/*     */           }
/* 474 */           catch (UnsupportedEncodingException e) {
/* 475 */             str1 = new String(arrayOfByte1, 0, i12);
/*     */           } 
/* 477 */           outputText(x, y, flag, x1, y1, x2, y2, str1);
/*     */           break;
/*     */ 
/*     */         
/*     */         case 1313:
/* 482 */           count = this.in.readWord();
/* 483 */           text = new byte[count];
/*     */           
/* 485 */           for (k = 0; k < count; k++) {
/* 486 */             byte c = (byte)this.in.readByte();
/* 487 */             if (c == 0)
/*     */               break; 
/* 489 */             text[k] = c;
/*     */           } 
/*     */           
/*     */           try {
/* 493 */             s = new String(text, 0, k, "Cp1252");
/*     */           }
/* 495 */           catch (UnsupportedEncodingException e) {
/* 496 */             s = new String(text, 0, k);
/*     */           } 
/* 498 */           count = count + 1 & 0xFFFE;
/* 499 */           this.in.skip(count - k);
/* 500 */           i10 = this.in.readShort();
/* 501 */           i11 = this.in.readShort();
/* 502 */           outputText(i11, i10, 0, 0, 0, 0, 0, s);
/*     */           break;
/*     */         
/*     */         case 513:
/* 506 */           this.state.setCurrentBackgroundColor(this.in.readColor());
/*     */           break;
/*     */         case 521:
/* 509 */           this.state.setCurrentTextColor(this.in.readColor());
/*     */           break;
/*     */         case 302:
/* 512 */           this.state.setTextAlign(this.in.readWord());
/*     */           break;
/*     */         case 258:
/* 515 */           this.state.setBackgroundMode(this.in.readWord());
/*     */           break;
/*     */         case 262:
/* 518 */           this.state.setPolyFillMode(this.in.readWord());
/*     */           break;
/*     */         
/*     */         case 1055:
/* 522 */           color = this.in.readColor();
/* 523 */           n = this.in.readShort();
/* 524 */           i3 = this.in.readShort();
/* 525 */           this.cb.saveState();
/* 526 */           this.cb.setColorFill(color);
/* 527 */           this.cb.rectangle(this.state.transformX(i3), this.state.transformY(n), 0.2F, 0.2F);
/* 528 */           this.cb.fill();
/* 529 */           this.cb.restoreState();
/*     */           break;
/*     */         
/*     */         case 2881:
/*     */         case 3907:
/* 534 */           rop = this.in.readInt();
/* 535 */           if (function == 3907) {
/* 536 */             this.in.readWord();
/*     */           }
/* 538 */           srcHeight = this.in.readShort();
/* 539 */           srcWidth = this.in.readShort();
/* 540 */           ySrc = this.in.readShort();
/* 541 */           xSrc = this.in.readShort();
/* 542 */           destHeight = this.state.transformY(this.in.readShort()) - this.state.transformY(0);
/* 543 */           destWidth = this.state.transformX(this.in.readShort()) - this.state.transformX(0);
/* 544 */           yDest = this.state.transformY(this.in.readShort());
/* 545 */           xDest = this.state.transformX(this.in.readShort());
/* 546 */           arrayOfByte2 = new byte[tsize * 2 - this.in.getLength() - lenMarker];
/* 547 */           for (i13 = 0; i13 < arrayOfByte2.length; i13++)
/* 548 */             arrayOfByte2[i13] = (byte)this.in.readByte(); 
/*     */           try {
/* 550 */             ByteArrayInputStream inb = new ByteArrayInputStream(arrayOfByte2);
/* 551 */             Image bmp = BmpImage.getImage(inb, true, arrayOfByte2.length);
/* 552 */             this.cb.saveState();
/* 553 */             this.cb.rectangle(xDest, yDest, destWidth, destHeight);
/* 554 */             this.cb.clip();
/* 555 */             this.cb.newPath();
/* 556 */             bmp.scaleAbsolute(destWidth * bmp.getWidth() / srcWidth, -destHeight * bmp.getHeight() / srcHeight);
/* 557 */             bmp.setAbsolutePosition(xDest - destWidth * xSrc / srcWidth, yDest + destHeight * ySrc / srcHeight - bmp.getScaledHeight());
/* 558 */             this.cb.addImage(bmp);
/* 559 */             this.cb.restoreState();
/*     */           }
/* 561 */           catch (Exception exception) {}
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 567 */       this.in.skip(tsize * 2 - this.in.getLength() - lenMarker);
/*     */     } 
/* 569 */     this.state.cleanup(this.cb);
/*     */   }
/*     */   
/*     */   public void outputText(int x, int y, int flag, int x1, int y1, int x2, int y2, String text) {
/* 573 */     MetaFont font = this.state.getCurrentFont();
/* 574 */     float refX = this.state.transformX(x);
/* 575 */     float refY = this.state.transformY(y);
/* 576 */     float angle = this.state.transformAngle(font.getAngle());
/* 577 */     float sin = (float)Math.sin(angle);
/* 578 */     float cos = (float)Math.cos(angle);
/* 579 */     float fontSize = font.getFontSize(this.state);
/* 580 */     BaseFont bf = font.getFont();
/* 581 */     int align = this.state.getTextAlign();
/* 582 */     float textWidth = bf.getWidthPoint(text, fontSize);
/* 583 */     float tx = 0.0F;
/* 584 */     float ty = 0.0F;
/* 585 */     float descender = bf.getFontDescriptor(3, fontSize);
/* 586 */     float ury = bf.getFontDescriptor(8, fontSize);
/* 587 */     this.cb.saveState();
/* 588 */     this.cb.concatCTM(cos, sin, -sin, cos, refX, refY);
/* 589 */     if ((align & 0x6) == 6) {
/* 590 */       tx = -textWidth / 2.0F;
/* 591 */     } else if ((align & 0x2) == 2) {
/* 592 */       tx = -textWidth;
/* 593 */     }  if ((align & 0x18) == 24) {
/* 594 */       ty = 0.0F;
/* 595 */     } else if ((align & 0x8) == 8) {
/* 596 */       ty = -descender;
/*     */     } else {
/* 598 */       ty = -ury;
/*     */     } 
/* 600 */     if (this.state.getBackgroundMode() == 2) {
/* 601 */       BaseColor baseColor = this.state.getCurrentBackgroundColor();
/* 602 */       this.cb.setColorFill(baseColor);
/* 603 */       this.cb.rectangle(tx, ty + descender, textWidth, ury - descender);
/* 604 */       this.cb.fill();
/*     */     } 
/* 606 */     BaseColor textColor = this.state.getCurrentTextColor();
/* 607 */     this.cb.setColorFill(textColor);
/* 608 */     this.cb.beginText();
/* 609 */     this.cb.setFontAndSize(bf, fontSize);
/* 610 */     this.cb.setTextMatrix(tx, ty);
/* 611 */     this.cb.showText(text);
/* 612 */     this.cb.endText();
/* 613 */     if (font.isUnderline()) {
/* 614 */       this.cb.rectangle(tx, ty - fontSize / 4.0F, textWidth, fontSize / 15.0F);
/* 615 */       this.cb.fill();
/*     */     } 
/* 617 */     if (font.isStrikeout()) {
/* 618 */       this.cb.rectangle(tx, ty + fontSize / 3.0F, textWidth, fontSize / 15.0F);
/* 619 */       this.cb.fill();
/*     */     } 
/* 621 */     this.cb.restoreState();
/*     */   }
/*     */   
/*     */   public boolean isNullStrokeFill(boolean isRectangle) {
/* 625 */     MetaPen pen = this.state.getCurrentPen();
/* 626 */     MetaBrush brush = this.state.getCurrentBrush();
/* 627 */     boolean noPen = (pen.getStyle() == 5);
/* 628 */     int style = brush.getStyle();
/* 629 */     boolean isBrush = (style == 0 || (style == 2 && this.state.getBackgroundMode() == 2));
/* 630 */     boolean result = (noPen && !isBrush);
/* 631 */     if (!noPen)
/* 632 */       if (isRectangle) {
/* 633 */         this.state.setLineJoinRectangle(this.cb);
/*     */       } else {
/* 635 */         this.state.setLineJoinPolygon(this.cb);
/*     */       }  
/* 637 */     return result;
/*     */   }
/*     */   
/*     */   public void strokeAndFill() {
/* 641 */     MetaPen pen = this.state.getCurrentPen();
/* 642 */     MetaBrush brush = this.state.getCurrentBrush();
/* 643 */     int penStyle = pen.getStyle();
/* 644 */     int brushStyle = brush.getStyle();
/* 645 */     if (penStyle == 5) {
/* 646 */       this.cb.closePath();
/* 647 */       if (this.state.getPolyFillMode() == 1) {
/* 648 */         this.cb.eoFill();
/*     */       } else {
/*     */         
/* 651 */         this.cb.fill();
/*     */       } 
/*     */     } else {
/*     */       
/* 655 */       boolean isBrush = (brushStyle == 0 || (brushStyle == 2 && this.state.getBackgroundMode() == 2));
/* 656 */       if (isBrush) {
/* 657 */         if (this.state.getPolyFillMode() == 1) {
/* 658 */           this.cb.closePathEoFillStroke();
/*     */         } else {
/* 660 */           this.cb.closePathFillStroke();
/*     */         } 
/*     */       } else {
/* 663 */         this.cb.closePathStroke();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static float getArc(float xCenter, float yCenter, float xDot, float yDot) {
/* 669 */     return (float)getArc(xCenter, yCenter, xDot, yDot);
/*     */   }
/*     */   
/*     */   static double getArc(double xCenter, double yCenter, double xDot, double yDot) {
/* 673 */     double s = Math.atan2(yDot - yCenter, xDot - xCenter);
/* 674 */     if (s < 0.0D)
/* 675 */       s += 6.283185307179586D; 
/* 676 */     return (float)(s / Math.PI * 180.0D);
/*     */   }
/*     */   
/*     */   public static byte[] wrapBMP(Image image) throws IOException {
/* 680 */     if (image.getOriginalType() != 4) {
/* 681 */       throw new IOException(MessageLocalization.getComposedMessage("only.bmp.can.be.wrapped.in.wmf", new Object[0]));
/*     */     }
/* 683 */     byte[] data = null;
/* 684 */     if (image.getOriginalData() == null) {
/* 685 */       InputStream imgIn = image.getUrl().openStream();
/* 686 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 687 */       int b = 0;
/* 688 */       while ((b = imgIn.read()) != -1)
/* 689 */         out.write(b); 
/* 690 */       imgIn.close();
/* 691 */       data = out.toByteArray();
/*     */     } else {
/*     */       
/* 694 */       data = image.getOriginalData();
/* 695 */     }  int sizeBmpWords = data.length - 14 + 1 >>> 1;
/* 696 */     ByteArrayOutputStream os = new ByteArrayOutputStream();
/*     */     
/* 698 */     writeWord(os, 1);
/* 699 */     writeWord(os, 9);
/* 700 */     writeWord(os, 768);
/* 701 */     writeDWord(os, 36 + sizeBmpWords + 3);
/* 702 */     writeWord(os, 1);
/* 703 */     writeDWord(os, 14 + sizeBmpWords);
/* 704 */     writeWord(os, 0);
/*     */     
/* 706 */     writeDWord(os, 4);
/* 707 */     writeWord(os, 259);
/* 708 */     writeWord(os, 8);
/*     */     
/* 710 */     writeDWord(os, 5);
/* 711 */     writeWord(os, 523);
/* 712 */     writeWord(os, 0);
/* 713 */     writeWord(os, 0);
/*     */     
/* 715 */     writeDWord(os, 5);
/* 716 */     writeWord(os, 524);
/* 717 */     writeWord(os, (int)image.getHeight());
/* 718 */     writeWord(os, (int)image.getWidth());
/*     */     
/* 720 */     writeDWord(os, 13 + sizeBmpWords);
/* 721 */     writeWord(os, 2881);
/* 722 */     writeDWord(os, 13369376);
/* 723 */     writeWord(os, (int)image.getHeight());
/* 724 */     writeWord(os, (int)image.getWidth());
/* 725 */     writeWord(os, 0);
/* 726 */     writeWord(os, 0);
/* 727 */     writeWord(os, (int)image.getHeight());
/* 728 */     writeWord(os, (int)image.getWidth());
/* 729 */     writeWord(os, 0);
/* 730 */     writeWord(os, 0);
/* 731 */     os.write(data, 14, data.length - 14);
/* 732 */     if ((data.length & 0x1) == 1) {
/* 733 */       os.write(0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 750 */     writeDWord(os, 3);
/* 751 */     writeWord(os, 0);
/* 752 */     os.close();
/* 753 */     return os.toByteArray();
/*     */   }
/*     */   
/*     */   public static void writeWord(OutputStream os, int v) throws IOException {
/* 757 */     os.write(v & 0xFF);
/* 758 */     os.write(v >>> 8 & 0xFF);
/*     */   }
/*     */   
/*     */   public static void writeDWord(OutputStream os, int v) throws IOException {
/* 762 */     writeWord(os, v & 0xFFFF);
/* 763 */     writeWord(os, v >>> 16 & 0xFFFF);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/wmf/MetaDo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */