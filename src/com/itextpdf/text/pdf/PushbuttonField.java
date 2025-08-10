/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PushbuttonField
/*     */   extends BaseField
/*     */ {
/*     */   public static final int LAYOUT_LABEL_ONLY = 1;
/*     */   public static final int LAYOUT_ICON_ONLY = 2;
/*     */   public static final int LAYOUT_ICON_TOP_LABEL_BOTTOM = 3;
/*     */   public static final int LAYOUT_LABEL_TOP_ICON_BOTTOM = 4;
/*     */   public static final int LAYOUT_ICON_LEFT_LABEL_RIGHT = 5;
/*     */   public static final int LAYOUT_LABEL_LEFT_ICON_RIGHT = 6;
/*     */   public static final int LAYOUT_LABEL_OVER_ICON = 7;
/*     */   public static final int SCALE_ICON_ALWAYS = 1;
/*     */   public static final int SCALE_ICON_NEVER = 2;
/*     */   public static final int SCALE_ICON_IS_TOO_BIG = 3;
/*     */   public static final int SCALE_ICON_IS_TOO_SMALL = 4;
/* 109 */   private int layout = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Image image;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PdfTemplate template;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   private int scaleIcon = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean proportionalIcon = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 134 */   private float iconVerticalAdjustment = 0.5F;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   private float iconHorizontalAdjustment = 0.5F;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean iconFitToBounds;
/*     */ 
/*     */ 
/*     */   
/*     */   private PdfTemplate tp;
/*     */ 
/*     */ 
/*     */   
/*     */   private PRIndirectReference iconReference;
/*     */ 
/*     */ 
/*     */   
/*     */   public PushbuttonField(PdfWriter writer, Rectangle box, String fieldName) {
/* 156 */     super(writer, box, fieldName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLayout() {
/* 164 */     return this.layout;
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
/*     */   public void setLayout(int layout) {
/* 176 */     if (layout < 1 || layout > 7)
/* 177 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("layout.out.of.bounds", new Object[0])); 
/* 178 */     this.layout = layout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Image getImage() {
/* 186 */     return this.image;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImage(Image image) {
/* 194 */     this.image = image;
/* 195 */     this.template = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfTemplate getTemplate() {
/* 203 */     return this.template;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTemplate(PdfTemplate template) {
/* 211 */     this.template = template;
/* 212 */     this.image = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getScaleIcon() {
/* 220 */     return this.scaleIcon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScaleIcon(int scaleIcon) {
/* 231 */     if (scaleIcon < 1 || scaleIcon > 4)
/* 232 */       scaleIcon = 1; 
/* 233 */     this.scaleIcon = scaleIcon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProportionalIcon() {
/* 241 */     return this.proportionalIcon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProportionalIcon(boolean proportionalIcon) {
/* 250 */     this.proportionalIcon = proportionalIcon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getIconVerticalAdjustment() {
/* 258 */     return this.iconVerticalAdjustment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIconVerticalAdjustment(float iconVerticalAdjustment) {
/* 268 */     if (iconVerticalAdjustment < 0.0F) {
/* 269 */       iconVerticalAdjustment = 0.0F;
/* 270 */     } else if (iconVerticalAdjustment > 1.0F) {
/* 271 */       iconVerticalAdjustment = 1.0F;
/* 272 */     }  this.iconVerticalAdjustment = iconVerticalAdjustment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getIconHorizontalAdjustment() {
/* 280 */     return this.iconHorizontalAdjustment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIconHorizontalAdjustment(float iconHorizontalAdjustment) {
/* 290 */     if (iconHorizontalAdjustment < 0.0F) {
/* 291 */       iconHorizontalAdjustment = 0.0F;
/* 292 */     } else if (iconHorizontalAdjustment > 1.0F) {
/* 293 */       iconHorizontalAdjustment = 1.0F;
/* 294 */     }  this.iconHorizontalAdjustment = iconHorizontalAdjustment;
/*     */   }
/*     */   
/*     */   private float calculateFontSize(float w, float h) throws IOException, DocumentException {
/* 298 */     BaseFont ufont = getRealFont();
/* 299 */     float fsize = this.fontSize;
/* 300 */     if (fsize == 0.0F) {
/* 301 */       float bw = ufont.getWidthPoint(this.text, 1.0F);
/* 302 */       if (bw == 0.0F) {
/* 303 */         fsize = 12.0F;
/*     */       } else {
/* 305 */         fsize = w / bw;
/* 306 */       }  float nfsize = h / (1.0F - ufont.getFontDescriptor(3, 1.0F));
/* 307 */       fsize = Math.min(fsize, nfsize);
/* 308 */       if (fsize < 4.0F)
/* 309 */         fsize = 4.0F; 
/*     */     } 
/* 311 */     return fsize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfAppearance getAppearance() throws IOException, DocumentException {
/* 321 */     PdfAppearance app = getBorderAppearance();
/* 322 */     Rectangle box = new Rectangle(app.getBoundingBox());
/* 323 */     if ((this.text == null || this.text.length() == 0) && (this.layout == 1 || (this.image == null && this.template == null && this.iconReference == null))) {
/* 324 */       return app;
/*     */     }
/* 326 */     if (this.layout == 2 && this.image == null && this.template == null && this.iconReference == null)
/* 327 */       return app; 
/* 328 */     BaseFont ufont = getRealFont();
/* 329 */     boolean borderExtra = (this.borderStyle == 2 || this.borderStyle == 3);
/* 330 */     float h = box.getHeight() - this.borderWidth * 2.0F;
/* 331 */     float bw2 = this.borderWidth;
/* 332 */     if (borderExtra) {
/* 333 */       h -= this.borderWidth * 2.0F;
/* 334 */       bw2 *= 2.0F;
/*     */     } 
/* 336 */     float offsetX = borderExtra ? (2.0F * this.borderWidth) : this.borderWidth;
/* 337 */     offsetX = Math.max(offsetX, 1.0F);
/* 338 */     float offX = Math.min(bw2, offsetX);
/* 339 */     this.tp = null;
/* 340 */     float textX = Float.NaN;
/* 341 */     float textY = 0.0F;
/* 342 */     float fsize = this.fontSize;
/* 343 */     float wt = box.getWidth() - 2.0F * offX - 2.0F;
/* 344 */     float ht = box.getHeight() - 2.0F * offX;
/* 345 */     float adj = this.iconFitToBounds ? 0.0F : (offX + 1.0F);
/* 346 */     int nlayout = this.layout;
/* 347 */     if (this.image == null && this.template == null && this.iconReference == null)
/* 348 */       nlayout = 1; 
/* 349 */     Rectangle iconBox = null; while (true) {
/*     */       float nht; float nw;
/* 351 */       switch (nlayout) {
/*     */         case 1:
/*     */         case 7:
/* 354 */           if (this.text != null && this.text.length() > 0 && wt > 0.0F && ht > 0.0F) {
/* 355 */             fsize = calculateFontSize(wt, ht);
/* 356 */             textX = (box.getWidth() - ufont.getWidthPoint(this.text, fsize)) / 2.0F;
/* 357 */             textY = (box.getHeight() - ufont.getFontDescriptor(1, fsize)) / 2.0F;
/*     */           } 
/*     */         case 2:
/* 360 */           if (nlayout == 7 || nlayout == 2)
/* 361 */             iconBox = new Rectangle(box.getLeft() + adj, box.getBottom() + adj, box.getRight() - adj, box.getTop() - adj); 
/*     */           break;
/*     */         case 3:
/* 364 */           if (this.text == null || this.text.length() == 0 || wt <= 0.0F || ht <= 0.0F) {
/* 365 */             nlayout = 2;
/*     */             continue;
/*     */           } 
/* 368 */           nht = box.getHeight() * 0.35F - offX;
/* 369 */           if (nht > 0.0F) {
/* 370 */             fsize = calculateFontSize(wt, nht);
/*     */           } else {
/* 372 */             fsize = 4.0F;
/* 373 */           }  textX = (box.getWidth() - ufont.getWidthPoint(this.text, fsize)) / 2.0F;
/* 374 */           textY = offX - ufont.getFontDescriptor(3, fsize);
/* 375 */           iconBox = new Rectangle(box.getLeft() + adj, textY + fsize, box.getRight() - adj, box.getTop() - adj);
/*     */           break;
/*     */         case 4:
/* 378 */           if (this.text == null || this.text.length() == 0 || wt <= 0.0F || ht <= 0.0F) {
/* 379 */             nlayout = 2;
/*     */             continue;
/*     */           } 
/* 382 */           nht = box.getHeight() * 0.35F - offX;
/* 383 */           if (nht > 0.0F) {
/* 384 */             fsize = calculateFontSize(wt, nht);
/*     */           } else {
/* 386 */             fsize = 4.0F;
/* 387 */           }  textX = (box.getWidth() - ufont.getWidthPoint(this.text, fsize)) / 2.0F;
/* 388 */           textY = box.getHeight() - offX - fsize;
/* 389 */           if (textY < offX)
/* 390 */             textY = offX; 
/* 391 */           iconBox = new Rectangle(box.getLeft() + adj, box.getBottom() + adj, box.getRight() - adj, textY + ufont.getFontDescriptor(3, fsize));
/*     */           break;
/*     */         case 6:
/* 394 */           if (this.text == null || this.text.length() == 0 || wt <= 0.0F || ht <= 0.0F) {
/* 395 */             nlayout = 2;
/*     */             continue;
/*     */           } 
/* 398 */           nw = box.getWidth() * 0.35F - offX;
/* 399 */           if (nw > 0.0F) {
/* 400 */             fsize = calculateFontSize(wt, nw);
/*     */           } else {
/* 402 */             fsize = 4.0F;
/* 403 */           }  if (ufont.getWidthPoint(this.text, fsize) >= wt) {
/* 404 */             nlayout = 1;
/* 405 */             fsize = this.fontSize;
/*     */             continue;
/*     */           } 
/* 408 */           textX = offX + 1.0F;
/* 409 */           textY = (box.getHeight() - ufont.getFontDescriptor(1, fsize)) / 2.0F;
/* 410 */           iconBox = new Rectangle(textX + ufont.getWidthPoint(this.text, fsize), box.getBottom() + adj, box.getRight() - adj, box.getTop() - adj);
/*     */           break;
/*     */         case 5:
/* 413 */           if (this.text == null || this.text.length() == 0 || wt <= 0.0F || ht <= 0.0F) {
/* 414 */             nlayout = 2;
/*     */             continue;
/*     */           } 
/* 417 */           nw = box.getWidth() * 0.35F - offX;
/* 418 */           if (nw > 0.0F) {
/* 419 */             fsize = calculateFontSize(wt, nw);
/*     */           } else {
/* 421 */             fsize = 4.0F;
/* 422 */           }  if (ufont.getWidthPoint(this.text, fsize) >= wt) {
/* 423 */             nlayout = 1;
/* 424 */             fsize = this.fontSize;
/*     */             continue;
/*     */           } 
/* 427 */           textX = box.getWidth() - ufont.getWidthPoint(this.text, fsize) - offX - 1.0F;
/* 428 */           textY = (box.getHeight() - ufont.getFontDescriptor(1, fsize)) / 2.0F;
/* 429 */           iconBox = new Rectangle(box.getLeft() + adj, box.getBottom() + adj, textX - 1.0F, box.getTop() - adj);
/*     */           break;
/*     */       } 
/*     */       break;
/*     */     } 
/* 434 */     if (textY < box.getBottom() + offX)
/* 435 */       textY = box.getBottom() + offX; 
/* 436 */     if (iconBox != null && (iconBox.getWidth() <= 0.0F || iconBox.getHeight() <= 0.0F))
/* 437 */       iconBox = null; 
/* 438 */     boolean haveIcon = false;
/* 439 */     float boundingBoxWidth = 0.0F;
/* 440 */     float boundingBoxHeight = 0.0F;
/* 441 */     PdfArray matrix = null;
/* 442 */     if (iconBox != null) {
/* 443 */       if (this.image != null) {
/* 444 */         this.tp = new PdfTemplate(this.writer);
/* 445 */         this.tp.setBoundingBox(new Rectangle((Rectangle)this.image));
/* 446 */         this.writer.addDirectTemplateSimple(this.tp, PdfName.FRM);
/* 447 */         this.tp.addImage(this.image, this.image.getWidth(), 0.0F, 0.0F, this.image.getHeight(), 0.0F, 0.0F);
/* 448 */         haveIcon = true;
/* 449 */         boundingBoxWidth = this.tp.getBoundingBox().getWidth();
/* 450 */         boundingBoxHeight = this.tp.getBoundingBox().getHeight();
/*     */       }
/* 452 */       else if (this.template != null) {
/* 453 */         this.tp = new PdfTemplate(this.writer);
/* 454 */         this.tp.setBoundingBox(new Rectangle(this.template.getWidth(), this.template.getHeight()));
/* 455 */         this.writer.addDirectTemplateSimple(this.tp, PdfName.FRM);
/* 456 */         this.tp.addTemplate(this.template, this.template.getBoundingBox().getLeft(), this.template.getBoundingBox().getBottom());
/* 457 */         haveIcon = true;
/* 458 */         boundingBoxWidth = this.tp.getBoundingBox().getWidth();
/* 459 */         boundingBoxHeight = this.tp.getBoundingBox().getHeight();
/*     */       }
/* 461 */       else if (this.iconReference != null) {
/* 462 */         PdfDictionary dic = (PdfDictionary)PdfReader.getPdfObject(this.iconReference);
/* 463 */         if (dic != null) {
/* 464 */           Rectangle r2 = PdfReader.getNormalizedRectangle(dic.getAsArray(PdfName.BBOX));
/* 465 */           matrix = dic.getAsArray(PdfName.MATRIX);
/* 466 */           haveIcon = true;
/* 467 */           boundingBoxWidth = r2.getWidth();
/* 468 */           boundingBoxHeight = r2.getHeight();
/*     */         } 
/*     */       } 
/*     */     }
/* 472 */     if (haveIcon) {
/* 473 */       float icx = iconBox.getWidth() / boundingBoxWidth;
/* 474 */       float icy = iconBox.getHeight() / boundingBoxHeight;
/* 475 */       if (this.proportionalIcon) {
/* 476 */         switch (this.scaleIcon) {
/*     */           case 3:
/* 478 */             icx = Math.min(icx, icy);
/* 479 */             icx = Math.min(icx, 1.0F);
/*     */             break;
/*     */           case 4:
/* 482 */             icx = Math.min(icx, icy);
/* 483 */             icx = Math.max(icx, 1.0F);
/*     */             break;
/*     */           case 2:
/* 486 */             icx = 1.0F;
/*     */             break;
/*     */           default:
/* 489 */             icx = Math.min(icx, icy);
/*     */             break;
/*     */         } 
/* 492 */         icy = icx;
/*     */       } else {
/*     */         
/* 495 */         switch (this.scaleIcon) {
/*     */           case 3:
/* 497 */             icx = Math.min(icx, 1.0F);
/* 498 */             icy = Math.min(icy, 1.0F);
/*     */             break;
/*     */           case 4:
/* 501 */             icx = Math.max(icx, 1.0F);
/* 502 */             icy = Math.max(icy, 1.0F);
/*     */             break;
/*     */           case 2:
/* 505 */             icx = icy = 1.0F;
/*     */             break;
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/* 511 */       float xpos = iconBox.getLeft() + (iconBox.getWidth() - boundingBoxWidth * icx) * this.iconHorizontalAdjustment;
/* 512 */       float ypos = iconBox.getBottom() + (iconBox.getHeight() - boundingBoxHeight * icy) * this.iconVerticalAdjustment;
/* 513 */       app.saveState();
/* 514 */       app.rectangle(iconBox.getLeft(), iconBox.getBottom(), iconBox.getWidth(), iconBox.getHeight());
/* 515 */       app.clip();
/* 516 */       app.newPath();
/* 517 */       if (this.tp != null) {
/* 518 */         app.addTemplate(this.tp, icx, 0.0F, 0.0F, icy, xpos, ypos);
/*     */       } else {
/* 520 */         float cox = 0.0F;
/* 521 */         float coy = 0.0F;
/* 522 */         if (matrix != null && matrix.size() == 6) {
/* 523 */           PdfNumber nm = matrix.getAsNumber(4);
/* 524 */           if (nm != null)
/* 525 */             cox = nm.floatValue(); 
/* 526 */           nm = matrix.getAsNumber(5);
/* 527 */           if (nm != null)
/* 528 */             coy = nm.floatValue(); 
/*     */         } 
/* 530 */         app.addTemplateReference(this.iconReference, PdfName.FRM, icx, 0.0F, 0.0F, icy, xpos - cox * icx, ypos - coy * icy);
/*     */       } 
/* 532 */       app.restoreState();
/*     */     } 
/* 534 */     if (!Float.isNaN(textX)) {
/* 535 */       app.saveState();
/* 536 */       app.rectangle(offX, offX, box.getWidth() - 2.0F * offX, box.getHeight() - 2.0F * offX);
/* 537 */       app.clip();
/* 538 */       app.newPath();
/* 539 */       if (this.textColor == null) {
/* 540 */         app.resetGrayFill();
/*     */       } else {
/* 542 */         app.setColorFill(this.textColor);
/* 543 */       }  app.beginText();
/* 544 */       app.setFontAndSize(ufont, fsize);
/* 545 */       app.setTextMatrix(textX, textY);
/* 546 */       app.showText(this.text);
/* 547 */       app.endText();
/* 548 */       app.restoreState();
/*     */     } 
/* 550 */     return app;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfFormField getField() throws IOException, DocumentException {
/* 560 */     PdfFormField field = PdfFormField.createPushButton(this.writer);
/* 561 */     field.setWidget(this.box, PdfAnnotation.HIGHLIGHT_INVERT);
/* 562 */     if (this.fieldName != null) {
/* 563 */       field.setFieldName(this.fieldName);
/* 564 */       if ((this.options & 0x1) != 0)
/* 565 */         field.setFieldFlags(1); 
/* 566 */       if ((this.options & 0x2) != 0)
/* 567 */         field.setFieldFlags(2); 
/*     */     } 
/* 569 */     if (this.text != null)
/* 570 */       field.setMKNormalCaption(this.text); 
/* 571 */     if (this.rotation != 0)
/* 572 */       field.setMKRotation(this.rotation); 
/* 573 */     field.setBorderStyle(new PdfBorderDictionary(this.borderWidth, this.borderStyle, new PdfDashPattern(3.0F)));
/* 574 */     PdfAppearance tpa = getAppearance();
/* 575 */     field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, tpa);
/* 576 */     PdfAppearance da = (PdfAppearance)tpa.getDuplicate();
/* 577 */     da.setFontAndSize(getRealFont(), this.fontSize);
/* 578 */     if (this.textColor == null) {
/* 579 */       da.setGrayFill(0.0F);
/*     */     } else {
/* 581 */       da.setColorFill(this.textColor);
/* 582 */     }  field.setDefaultAppearanceString(da);
/* 583 */     if (this.borderColor != null)
/* 584 */       field.setMKBorderColor(this.borderColor); 
/* 585 */     if (this.backgroundColor != null)
/* 586 */       field.setMKBackgroundColor(this.backgroundColor); 
/* 587 */     switch (this.visibility) {
/*     */       case 1:
/* 589 */         field.setFlags(6);
/*     */         break;
/*     */       case 2:
/*     */         break;
/*     */       case 3:
/* 594 */         field.setFlags(36);
/*     */         break;
/*     */       default:
/* 597 */         field.setFlags(4);
/*     */         break;
/*     */     } 
/* 600 */     if (this.tp != null)
/* 601 */       field.setMKNormalIcon(this.tp); 
/* 602 */     field.setMKTextPosition(this.layout - 1);
/* 603 */     PdfName scale = PdfName.A;
/* 604 */     if (this.scaleIcon == 3) {
/* 605 */       scale = PdfName.B;
/* 606 */     } else if (this.scaleIcon == 4) {
/* 607 */       scale = PdfName.S;
/* 608 */     } else if (this.scaleIcon == 2) {
/* 609 */       scale = PdfName.N;
/* 610 */     }  field.setMKIconFit(scale, this.proportionalIcon ? PdfName.P : PdfName.A, this.iconHorizontalAdjustment, this.iconVerticalAdjustment, this.iconFitToBounds);
/*     */     
/* 612 */     return field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIconFitToBounds() {
/* 620 */     return this.iconFitToBounds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIconFitToBounds(boolean iconFitToBounds) {
/* 631 */     this.iconFitToBounds = iconFitToBounds;
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
/*     */   public PRIndirectReference getIconReference() {
/* 644 */     return this.iconReference;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIconReference(PRIndirectReference iconReference) {
/* 652 */     this.iconReference = iconReference;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PushbuttonField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */