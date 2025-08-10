/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Rectangle;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RadioCheckField
/*     */   extends BaseField
/*     */ {
/*     */   public static final int TYPE_CHECK = 1;
/*     */   public static final int TYPE_CIRCLE = 2;
/*     */   public static final int TYPE_CROSS = 3;
/*     */   public static final int TYPE_DIAMOND = 4;
/*     */   public static final int TYPE_SQUARE = 5;
/*     */   public static final int TYPE_STAR = 6;
/* 111 */   protected static String[] typeChars = new String[] { "4", "l", "8", "u", "n", "H" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int checkType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String onValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checked;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RadioCheckField(PdfWriter writer, Rectangle box, String fieldName, String onValue) {
/* 136 */     super(writer, box, fieldName);
/* 137 */     setOnValue(onValue);
/* 138 */     setCheckType(2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCheckType() {
/* 146 */     return this.checkType;
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
/*     */   public void setCheckType(int checkType) {
/* 160 */     if (checkType < 1 || checkType > 6)
/* 161 */       checkType = 2; 
/* 162 */     this.checkType = checkType;
/* 163 */     setText(typeChars[checkType - 1]);
/*     */     try {
/* 165 */       setFont(BaseFont.createFont("ZapfDingbats", "Cp1252", false));
/*     */     }
/* 167 */     catch (Exception e) {
/* 168 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOnValue() {
/* 177 */     return this.onValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnValue(String onValue) {
/* 185 */     this.onValue = onValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChecked() {
/* 193 */     return this.checked;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChecked(boolean checked) {
/* 202 */     this.checked = checked;
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
/*     */   public PdfAppearance getAppearance(boolean isRadio, boolean on) throws IOException, DocumentException {
/* 216 */     if (isRadio && this.checkType == 2)
/* 217 */       return getAppearanceRadioCircle(on); 
/* 218 */     PdfAppearance app = getBorderAppearance();
/* 219 */     if (!on)
/* 220 */       return app; 
/* 221 */     BaseFont ufont = getRealFont();
/* 222 */     boolean borderExtra = (this.borderStyle == 2 || this.borderStyle == 3);
/* 223 */     float h = this.box.getHeight() - this.borderWidth * 2.0F;
/* 224 */     float bw2 = this.borderWidth;
/* 225 */     if (borderExtra) {
/* 226 */       h -= this.borderWidth * 2.0F;
/* 227 */       bw2 *= 2.0F;
/*     */     } 
/* 229 */     float offsetX = borderExtra ? (2.0F * this.borderWidth) : this.borderWidth;
/* 230 */     offsetX = Math.max(offsetX, 1.0F);
/* 231 */     float offX = Math.min(bw2, offsetX);
/* 232 */     float wt = this.box.getWidth() - 2.0F * offX;
/* 233 */     float ht = this.box.getHeight() - 2.0F * offX;
/* 234 */     float fsize = this.fontSize;
/* 235 */     if (fsize == 0.0F) {
/* 236 */       float bw = ufont.getWidthPoint(this.text, 1.0F);
/* 237 */       if (bw == 0.0F) {
/* 238 */         fsize = 12.0F;
/*     */       } else {
/* 240 */         fsize = wt / bw;
/* 241 */       }  float nfsize = h / ufont.getFontDescriptor(1, 1.0F);
/* 242 */       fsize = Math.min(fsize, nfsize);
/*     */     } 
/* 244 */     app.saveState();
/* 245 */     app.rectangle(offX, offX, wt, ht);
/* 246 */     app.clip();
/* 247 */     app.newPath();
/* 248 */     if (this.textColor == null) {
/* 249 */       app.resetGrayFill();
/*     */     } else {
/* 251 */       app.setColorFill(this.textColor);
/* 252 */     }  app.beginText();
/* 253 */     app.setFontAndSize(ufont, fsize);
/* 254 */     app.setTextMatrix((this.box.getWidth() - ufont.getWidthPoint(this.text, fsize)) / 2.0F, (this.box
/* 255 */         .getHeight() - ufont.getAscentPoint(this.text, fsize)) / 2.0F);
/* 256 */     app.showText(this.text);
/* 257 */     app.endText();
/* 258 */     app.restoreState();
/* 259 */     return app;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfAppearance getAppearanceRadioCircle(boolean on) {
/* 269 */     PdfAppearance app = PdfAppearance.createAppearance(this.writer, this.box.getWidth(), this.box.getHeight());
/* 270 */     switch (this.rotation) {
/*     */       case 90:
/* 272 */         app.setMatrix(0.0F, 1.0F, -1.0F, 0.0F, this.box.getHeight(), 0.0F);
/*     */         break;
/*     */       case 180:
/* 275 */         app.setMatrix(-1.0F, 0.0F, 0.0F, -1.0F, this.box.getWidth(), this.box.getHeight());
/*     */         break;
/*     */       case 270:
/* 278 */         app.setMatrix(0.0F, -1.0F, 1.0F, 0.0F, 0.0F, this.box.getWidth());
/*     */         break;
/*     */     } 
/* 281 */     Rectangle box = new Rectangle(app.getBoundingBox());
/* 282 */     float cx = box.getWidth() / 2.0F;
/* 283 */     float cy = box.getHeight() / 2.0F;
/* 284 */     float r = (Math.min(box.getWidth(), box.getHeight()) - this.borderWidth) / 2.0F;
/* 285 */     if (r <= 0.0F)
/* 286 */       return app; 
/* 287 */     if (this.backgroundColor != null) {
/* 288 */       app.setColorFill(this.backgroundColor);
/* 289 */       app.circle(cx, cy, r + this.borderWidth / 2.0F);
/* 290 */       app.fill();
/*     */     } 
/* 292 */     if (this.borderWidth > 0.0F && this.borderColor != null) {
/* 293 */       app.setLineWidth(this.borderWidth);
/* 294 */       app.setColorStroke(this.borderColor);
/* 295 */       app.circle(cx, cy, r);
/* 296 */       app.stroke();
/*     */     } 
/* 298 */     if (on) {
/* 299 */       if (this.textColor == null) {
/* 300 */         app.resetGrayFill();
/*     */       } else {
/* 302 */         app.setColorFill(this.textColor);
/* 303 */       }  app.circle(cx, cy, r / 2.0F);
/* 304 */       app.fill();
/*     */     } 
/* 306 */     return app;
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
/*     */   
/*     */   public PdfFormField getRadioGroup(boolean noToggleToOff, boolean radiosInUnison) {
/* 323 */     PdfFormField field = PdfFormField.createRadioButton(this.writer, noToggleToOff);
/* 324 */     if (radiosInUnison)
/* 325 */       field.setFieldFlags(33554432); 
/* 326 */     field.setFieldName(this.fieldName);
/* 327 */     if ((this.options & 0x1) != 0)
/* 328 */       field.setFieldFlags(1); 
/* 329 */     if ((this.options & 0x2) != 0)
/* 330 */       field.setFieldFlags(2); 
/* 331 */     field.setValueAsName(this.checked ? this.onValue : "Off");
/* 332 */     return field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfFormField getRadioField() throws IOException, DocumentException {
/* 343 */     return getField(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfFormField getCheckField() throws IOException, DocumentException {
/* 353 */     return getField(false);
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
/*     */   protected PdfFormField getField(boolean isRadio) throws IOException, DocumentException {
/* 365 */     PdfFormField field = null;
/* 366 */     if (isRadio) {
/* 367 */       field = PdfFormField.createEmpty(this.writer);
/*     */     } else {
/* 369 */       field = PdfFormField.createCheckBox(this.writer);
/* 370 */     }  field.setWidget(this.box, PdfAnnotation.HIGHLIGHT_INVERT);
/* 371 */     if (!isRadio) {
/* 372 */       field.setFieldName(this.fieldName);
/* 373 */       if ((this.options & 0x1) != 0)
/* 374 */         field.setFieldFlags(1); 
/* 375 */       if ((this.options & 0x2) != 0)
/* 376 */         field.setFieldFlags(2); 
/* 377 */       field.setValueAsName(this.checked ? this.onValue : "Off");
/* 378 */       setCheckType(this.checkType);
/*     */     } 
/* 380 */     if (this.text != null)
/* 381 */       field.setMKNormalCaption(this.text); 
/* 382 */     if (this.rotation != 0)
/* 383 */       field.setMKRotation(this.rotation); 
/* 384 */     field.setBorderStyle(new PdfBorderDictionary(this.borderWidth, this.borderStyle, new PdfDashPattern(3.0F)));
/* 385 */     PdfAppearance tpon = getAppearance(isRadio, true);
/* 386 */     PdfAppearance tpoff = getAppearance(isRadio, false);
/* 387 */     field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, this.onValue, tpon);
/* 388 */     field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "Off", tpoff);
/* 389 */     field.setAppearanceState(this.checked ? this.onValue : "Off");
/* 390 */     PdfAppearance da = (PdfAppearance)tpon.getDuplicate();
/* 391 */     BaseFont realFont = getRealFont();
/* 392 */     if (realFont != null)
/* 393 */       da.setFontAndSize(getRealFont(), this.fontSize); 
/* 394 */     if (this.textColor == null) {
/* 395 */       da.setGrayFill(0.0F);
/*     */     } else {
/* 397 */       da.setColorFill(this.textColor);
/* 398 */     }  field.setDefaultAppearanceString(da);
/* 399 */     if (this.borderColor != null)
/* 400 */       field.setMKBorderColor(this.borderColor); 
/* 401 */     if (this.backgroundColor != null)
/* 402 */       field.setMKBackgroundColor(this.backgroundColor); 
/* 403 */     switch (this.visibility) {
/*     */       case 1:
/* 405 */         field.setFlags(6);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/* 416 */         return field;
/*     */       case 3:
/*     */         field.setFlags(36);
/*     */     } 
/*     */     field.setFlags(4);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/RadioCheckField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */