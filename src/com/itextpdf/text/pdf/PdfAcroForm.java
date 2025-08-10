/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.HashSet;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfAcroForm
/*     */   extends PdfDictionary
/*     */ {
/*     */   private PdfWriter writer;
/*  65 */   private HashSet<PdfTemplate> fieldTemplates = new HashSet<PdfTemplate>();
/*     */ 
/*     */   
/*  68 */   private PdfArray documentFields = new PdfArray();
/*     */ 
/*     */   
/*  71 */   private PdfArray calculationOrder = new PdfArray();
/*     */ 
/*     */   
/*  74 */   private int sigFlags = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfAcroForm(PdfWriter writer) {
/*  81 */     this.writer = writer;
/*     */   }
/*     */   
/*     */   public void setNeedAppearances(boolean value) {
/*  85 */     put(PdfName.NEEDAPPEARANCES, new PdfBoolean(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFieldTemplates(HashSet<PdfTemplate> ft) {
/*  94 */     this.fieldTemplates.addAll(ft);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDocumentField(PdfIndirectReference ref) {
/* 103 */     this.documentFields.add(ref);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValid() {
/* 112 */     if (this.documentFields.size() == 0) return false; 
/* 113 */     put(PdfName.FIELDS, this.documentFields);
/* 114 */     if (this.sigFlags != 0)
/* 115 */       put(PdfName.SIGFLAGS, new PdfNumber(this.sigFlags)); 
/* 116 */     if (this.calculationOrder.size() > 0)
/* 117 */       put(PdfName.CO, this.calculationOrder); 
/* 118 */     if (this.fieldTemplates.isEmpty()) return true; 
/* 119 */     PdfDictionary dic = new PdfDictionary();
/* 120 */     for (PdfTemplate template : this.fieldTemplates) {
/* 121 */       PdfFormField.mergeResources(dic, (PdfDictionary)template.getResources());
/*     */     }
/* 123 */     put(PdfName.DR, dic);
/* 124 */     put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
/* 125 */     PdfDictionary fonts = (PdfDictionary)dic.get(PdfName.FONT);
/* 126 */     if (fonts != null) {
/* 127 */       this.writer.eliminateFontSubset(fonts);
/*     */     }
/* 129 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCalculationOrder(PdfFormField formField) {
/* 138 */     this.calculationOrder.add(formField.getIndirectReference());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSigFlags(int f) {
/* 147 */     this.sigFlags |= f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFormField(PdfFormField formField) {
/* 156 */     this.writer.addAnnotation(formField);
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
/*     */   public PdfFormField addHtmlPostButton(String name, String caption, String value, String url, BaseFont font, float fontSize, float llx, float lly, float urx, float ury) {
/* 173 */     PdfAction action = PdfAction.createSubmitForm(url, null, 4);
/* 174 */     PdfFormField button = new PdfFormField(this.writer, llx, lly, urx, ury, action);
/* 175 */     setButtonParams(button, 65536, name, value);
/* 176 */     drawButton(button, caption, font, fontSize, llx, lly, urx, ury);
/* 177 */     addFormField(button);
/* 178 */     return button;
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
/*     */   public PdfFormField addResetButton(String name, String caption, String value, BaseFont font, float fontSize, float llx, float lly, float urx, float ury) {
/* 194 */     PdfAction action = PdfAction.createResetForm(null, 0);
/* 195 */     PdfFormField button = new PdfFormField(this.writer, llx, lly, urx, ury, action);
/* 196 */     setButtonParams(button, 65536, name, value);
/* 197 */     drawButton(button, caption, font, fontSize, llx, lly, urx, ury);
/* 198 */     addFormField(button);
/* 199 */     return button;
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
/*     */   public PdfFormField addMap(String name, String value, String url, PdfContentByte appearance, float llx, float lly, float urx, float ury) {
/* 214 */     PdfAction action = PdfAction.createSubmitForm(url, null, 20);
/* 215 */     PdfFormField button = new PdfFormField(this.writer, llx, lly, urx, ury, action);
/* 216 */     setButtonParams(button, 65536, name, (String)null);
/* 217 */     PdfAppearance pa = PdfAppearance.createAppearance(this.writer, urx - llx, ury - lly);
/* 218 */     pa.add(appearance);
/* 219 */     button.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, pa);
/* 220 */     addFormField(button);
/* 221 */     return button;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setButtonParams(PdfFormField button, int characteristics, String name, String value) {
/* 231 */     button.setButton(characteristics);
/* 232 */     button.setFlags(4);
/* 233 */     button.setPage();
/* 234 */     button.setFieldName(name);
/* 235 */     if (value != null) button.setValueAsString(value);
/*     */   
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
/*     */   public void drawButton(PdfFormField button, String caption, BaseFont font, float fontSize, float llx, float lly, float urx, float ury) {
/* 249 */     PdfAppearance pa = PdfAppearance.createAppearance(this.writer, urx - llx, ury - lly);
/* 250 */     pa.drawButton(0.0F, 0.0F, urx - llx, ury - lly, caption, font, fontSize);
/* 251 */     button.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, pa);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfFormField addHiddenField(String name, String value) {
/* 260 */     PdfFormField hidden = PdfFormField.createEmpty(this.writer);
/* 261 */     hidden.setFieldName(name);
/* 262 */     hidden.setValueAsName(value);
/* 263 */     addFormField(hidden);
/* 264 */     return hidden;
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
/*     */   public PdfFormField addSingleLineTextField(String name, String text, BaseFont font, float fontSize, float llx, float lly, float urx, float ury) {
/* 279 */     PdfFormField field = PdfFormField.createTextField(this.writer, false, false, 0);
/* 280 */     setTextFieldParams(field, text, name, llx, lly, urx, ury);
/* 281 */     drawSingleLineOfText(field, text, font, fontSize, llx, lly, urx, ury);
/* 282 */     addFormField(field);
/* 283 */     return field;
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
/*     */   public PdfFormField addMultiLineTextField(String name, String text, BaseFont font, float fontSize, float llx, float lly, float urx, float ury) {
/* 298 */     PdfFormField field = PdfFormField.createTextField(this.writer, true, false, 0);
/* 299 */     setTextFieldParams(field, text, name, llx, lly, urx, ury);
/* 300 */     drawMultiLineOfText(field, text, font, fontSize, llx, lly, urx, ury);
/* 301 */     addFormField(field);
/* 302 */     return field;
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
/*     */   public PdfFormField addSingleLinePasswordField(String name, String text, BaseFont font, float fontSize, float llx, float lly, float urx, float ury) {
/* 317 */     PdfFormField field = PdfFormField.createTextField(this.writer, false, true, 0);
/* 318 */     setTextFieldParams(field, text, name, llx, lly, urx, ury);
/* 319 */     drawSingleLineOfText(field, text, font, fontSize, llx, lly, urx, ury);
/* 320 */     addFormField(field);
/* 321 */     return field;
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
/*     */   public void setTextFieldParams(PdfFormField field, String text, String name, float llx, float lly, float urx, float ury) {
/* 334 */     field.setWidget(new Rectangle(llx, lly, urx, ury), PdfAnnotation.HIGHLIGHT_INVERT);
/* 335 */     field.setValueAsString(text);
/* 336 */     field.setDefaultValueAsString(text);
/* 337 */     field.setFieldName(name);
/* 338 */     field.setFlags(4);
/* 339 */     field.setPage();
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
/*     */   public void drawSingleLineOfText(PdfFormField field, String text, BaseFont font, float fontSize, float llx, float lly, float urx, float ury) {
/* 353 */     PdfAppearance tp = PdfAppearance.createAppearance(this.writer, urx - llx, ury - lly);
/* 354 */     PdfAppearance tp2 = (PdfAppearance)tp.getDuplicate();
/* 355 */     tp2.setFontAndSize(font, fontSize);
/* 356 */     tp2.resetRGBColorFill();
/* 357 */     field.setDefaultAppearanceString(tp2);
/* 358 */     tp.drawTextField(0.0F, 0.0F, urx - llx, ury - lly);
/* 359 */     tp.beginVariableText();
/* 360 */     tp.saveState();
/* 361 */     tp.rectangle(3.0F, 3.0F, urx - llx - 6.0F, ury - lly - 6.0F);
/* 362 */     tp.clip();
/* 363 */     tp.newPath();
/* 364 */     tp.beginText();
/* 365 */     tp.setFontAndSize(font, fontSize);
/* 366 */     tp.resetRGBColorFill();
/* 367 */     tp.setTextMatrix(4.0F, (ury - lly) / 2.0F - fontSize * 0.3F);
/* 368 */     tp.showText(text);
/* 369 */     tp.endText();
/* 370 */     tp.restoreState();
/* 371 */     tp.endVariableText();
/* 372 */     field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, tp);
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
/*     */   public void drawMultiLineOfText(PdfFormField field, String text, BaseFont font, float fontSize, float llx, float lly, float urx, float ury) {
/* 386 */     PdfAppearance tp = PdfAppearance.createAppearance(this.writer, urx - llx, ury - lly);
/* 387 */     PdfAppearance tp2 = (PdfAppearance)tp.getDuplicate();
/* 388 */     tp2.setFontAndSize(font, fontSize);
/* 389 */     tp2.resetRGBColorFill();
/* 390 */     field.setDefaultAppearanceString(tp2);
/* 391 */     tp.drawTextField(0.0F, 0.0F, urx - llx, ury - lly);
/* 392 */     tp.beginVariableText();
/* 393 */     tp.saveState();
/* 394 */     tp.rectangle(3.0F, 3.0F, urx - llx - 6.0F, ury - lly - 6.0F);
/* 395 */     tp.clip();
/* 396 */     tp.newPath();
/* 397 */     tp.beginText();
/* 398 */     tp.setFontAndSize(font, fontSize);
/* 399 */     tp.resetRGBColorFill();
/* 400 */     tp.setTextMatrix(4.0F, 5.0F);
/* 401 */     StringTokenizer tokenizer = new StringTokenizer(text, "\n");
/* 402 */     float yPos = ury - lly;
/* 403 */     while (tokenizer.hasMoreTokens()) {
/* 404 */       yPos -= fontSize * 1.2F;
/* 405 */       tp.showTextAligned(0, tokenizer.nextToken(), 3.0F, yPos, 0.0F);
/*     */     } 
/* 407 */     tp.endText();
/* 408 */     tp.restoreState();
/* 409 */     tp.endVariableText();
/* 410 */     field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, tp);
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
/*     */   public PdfFormField addCheckBox(String name, String value, boolean status, float llx, float lly, float urx, float ury) {
/* 424 */     PdfFormField field = PdfFormField.createCheckBox(this.writer);
/* 425 */     setCheckBoxParams(field, name, value, status, llx, lly, urx, ury);
/* 426 */     drawCheckBoxAppearences(field, value, llx, lly, urx, ury);
/* 427 */     addFormField(field);
/* 428 */     return field;
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
/*     */   public void setCheckBoxParams(PdfFormField field, String name, String value, boolean status, float llx, float lly, float urx, float ury) {
/* 442 */     field.setWidget(new Rectangle(llx, lly, urx, ury), PdfAnnotation.HIGHLIGHT_TOGGLE);
/* 443 */     field.setFieldName(name);
/* 444 */     if (status) {
/* 445 */       field.setValueAsName(value);
/* 446 */       field.setAppearanceState(value);
/*     */     } else {
/*     */       
/* 449 */       field.setValueAsName("Off");
/* 450 */       field.setAppearanceState("Off");
/*     */     } 
/* 452 */     field.setFlags(4);
/* 453 */     field.setPage();
/* 454 */     field.setBorderStyle(new PdfBorderDictionary(1.0F, 0));
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
/*     */   public void drawCheckBoxAppearences(PdfFormField field, String value, float llx, float lly, float urx, float ury) {
/* 466 */     BaseFont font = null;
/*     */     try {
/* 468 */       font = BaseFont.createFont("ZapfDingbats", "Cp1252", false);
/*     */     }
/* 470 */     catch (Exception e) {
/* 471 */       throw new ExceptionConverter(e);
/*     */     } 
/* 473 */     float size = ury - lly;
/* 474 */     PdfAppearance tpOn = PdfAppearance.createAppearance(this.writer, urx - llx, ury - lly);
/* 475 */     PdfAppearance tp2 = (PdfAppearance)tpOn.getDuplicate();
/* 476 */     tp2.setFontAndSize(font, size);
/* 477 */     tp2.resetRGBColorFill();
/* 478 */     field.setDefaultAppearanceString(tp2);
/* 479 */     tpOn.drawTextField(0.0F, 0.0F, urx - llx, ury - lly);
/* 480 */     tpOn.saveState();
/* 481 */     tpOn.resetRGBColorFill();
/* 482 */     tpOn.beginText();
/* 483 */     tpOn.setFontAndSize(font, size);
/* 484 */     tpOn.showTextAligned(1, "4", (urx - llx) / 2.0F, (ury - lly) / 2.0F - size * 0.3F, 0.0F);
/* 485 */     tpOn.endText();
/* 486 */     tpOn.restoreState();
/* 487 */     field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, value, tpOn);
/* 488 */     PdfAppearance tpOff = PdfAppearance.createAppearance(this.writer, urx - llx, ury - lly);
/* 489 */     tpOff.drawTextField(0.0F, 0.0F, urx - llx, ury - lly);
/* 490 */     field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "Off", tpOff);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfFormField getRadioGroup(String name, String defaultValue, boolean noToggleToOff) {
/* 500 */     PdfFormField radio = PdfFormField.createRadioButton(this.writer, noToggleToOff);
/* 501 */     radio.setFieldName(name);
/* 502 */     radio.setValueAsName(defaultValue);
/* 503 */     return radio;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addRadioGroup(PdfFormField radiogroup) {
/* 510 */     addFormField(radiogroup);
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
/*     */   public PdfFormField addRadioButton(PdfFormField radiogroup, String value, float llx, float lly, float urx, float ury) {
/* 523 */     PdfFormField radio = PdfFormField.createEmpty(this.writer);
/* 524 */     radio.setWidget(new Rectangle(llx, lly, urx, ury), PdfAnnotation.HIGHLIGHT_TOGGLE);
/* 525 */     String name = ((PdfName)radiogroup.get(PdfName.V)).toString().substring(1);
/* 526 */     if (name.equals(value)) {
/* 527 */       radio.setAppearanceState(value);
/*     */     } else {
/*     */       
/* 530 */       radio.setAppearanceState("Off");
/*     */     } 
/* 532 */     drawRadioAppearences(radio, value, llx, lly, urx, ury);
/* 533 */     radiogroup.addKid(radio);
/* 534 */     return radio;
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
/*     */   public void drawRadioAppearences(PdfFormField field, String value, float llx, float lly, float urx, float ury) {
/* 546 */     PdfAppearance tpOn = PdfAppearance.createAppearance(this.writer, urx - llx, ury - lly);
/* 547 */     tpOn.drawRadioField(0.0F, 0.0F, urx - llx, ury - lly, true);
/* 548 */     field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, value, tpOn);
/* 549 */     PdfAppearance tpOff = PdfAppearance.createAppearance(this.writer, urx - llx, ury - lly);
/* 550 */     tpOff.drawRadioField(0.0F, 0.0F, urx - llx, ury - lly, false);
/* 551 */     field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "Off", tpOff);
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
/*     */   public PdfFormField addSelectList(String name, String[] options, String defaultValue, BaseFont font, float fontSize, float llx, float lly, float urx, float ury) {
/* 567 */     PdfFormField choice = PdfFormField.createList(this.writer, options, 0);
/* 568 */     setChoiceParams(choice, name, defaultValue, llx, lly, urx, ury);
/* 569 */     StringBuffer text = new StringBuffer();
/* 570 */     for (String option : options) {
/* 571 */       text.append(option).append('\n');
/*     */     }
/* 573 */     drawMultiLineOfText(choice, text.toString(), font, fontSize, llx, lly, urx, ury);
/* 574 */     addFormField(choice);
/* 575 */     return choice;
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
/*     */   public PdfFormField addSelectList(String name, String[][] options, String defaultValue, BaseFont font, float fontSize, float llx, float lly, float urx, float ury) {
/* 591 */     PdfFormField choice = PdfFormField.createList(this.writer, options, 0);
/* 592 */     setChoiceParams(choice, name, defaultValue, llx, lly, urx, ury);
/* 593 */     StringBuffer text = new StringBuffer();
/* 594 */     for (String[] option : options) {
/* 595 */       text.append(option[1]).append('\n');
/*     */     }
/* 597 */     drawMultiLineOfText(choice, text.toString(), font, fontSize, llx, lly, urx, ury);
/* 598 */     addFormField(choice);
/* 599 */     return choice;
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
/*     */   public PdfFormField addComboBox(String name, String[] options, String defaultValue, boolean editable, BaseFont font, float fontSize, float llx, float lly, float urx, float ury) {
/* 616 */     PdfFormField choice = PdfFormField.createCombo(this.writer, editable, options, 0);
/* 617 */     setChoiceParams(choice, name, defaultValue, llx, lly, urx, ury);
/* 618 */     if (defaultValue == null) {
/* 619 */       defaultValue = options[0];
/*     */     }
/* 621 */     drawSingleLineOfText(choice, defaultValue, font, fontSize, llx, lly, urx, ury);
/* 622 */     addFormField(choice);
/* 623 */     return choice;
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
/*     */   public PdfFormField addComboBox(String name, String[][] options, String defaultValue, boolean editable, BaseFont font, float fontSize, float llx, float lly, float urx, float ury) {
/* 640 */     PdfFormField choice = PdfFormField.createCombo(this.writer, editable, options, 0);
/* 641 */     setChoiceParams(choice, name, defaultValue, llx, lly, urx, ury);
/* 642 */     String value = null;
/* 643 */     for (String[] option : options) {
/* 644 */       if (option[0].equals(defaultValue)) {
/* 645 */         value = option[1];
/*     */         break;
/*     */       } 
/*     */     } 
/* 649 */     if (value == null) {
/* 650 */       value = options[0][1];
/*     */     }
/* 652 */     drawSingleLineOfText(choice, value, font, fontSize, llx, lly, urx, ury);
/* 653 */     addFormField(choice);
/* 654 */     return choice;
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
/*     */   public void setChoiceParams(PdfFormField field, String name, String defaultValue, float llx, float lly, float urx, float ury) {
/* 667 */     field.setWidget(new Rectangle(llx, lly, urx, ury), PdfAnnotation.HIGHLIGHT_INVERT);
/* 668 */     if (defaultValue != null) {
/* 669 */       field.setValueAsString(defaultValue);
/* 670 */       field.setDefaultValueAsString(defaultValue);
/*     */     } 
/* 672 */     field.setFieldName(name);
/* 673 */     field.setFlags(4);
/* 674 */     field.setPage();
/* 675 */     field.setBorderStyle(new PdfBorderDictionary(2.0F, 0));
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
/*     */   public PdfFormField addSignature(String name, float llx, float lly, float urx, float ury) {
/* 688 */     PdfFormField signature = PdfFormField.createSignature(this.writer);
/* 689 */     setSignatureParams(signature, name, llx, lly, urx, ury);
/* 690 */     drawSignatureAppearences(signature, llx, lly, urx, ury);
/* 691 */     addFormField(signature);
/* 692 */     return signature;
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
/*     */   public void setSignatureParams(PdfFormField field, String name, float llx, float lly, float urx, float ury) {
/* 705 */     field.setWidget(new Rectangle(llx, lly, urx, ury), PdfAnnotation.HIGHLIGHT_INVERT);
/* 706 */     field.setFieldName(name);
/* 707 */     field.setFlags(4);
/* 708 */     field.setPage();
/* 709 */     field.setMKBorderColor(BaseColor.BLACK);
/* 710 */     field.setMKBackgroundColor(BaseColor.WHITE);
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
/*     */   public void drawSignatureAppearences(PdfFormField field, float llx, float lly, float urx, float ury) {
/* 722 */     PdfAppearance tp = PdfAppearance.createAppearance(this.writer, urx - llx, ury - lly);
/* 723 */     tp.setGrayFill(1.0F);
/* 724 */     tp.rectangle(0.0F, 0.0F, urx - llx, ury - lly);
/* 725 */     tp.fill();
/* 726 */     tp.setGrayStroke(0.0F);
/* 727 */     tp.setLineWidth(1.0F);
/* 728 */     tp.rectangle(0.5F, 0.5F, urx - llx - 0.5F, ury - lly - 0.5F);
/* 729 */     tp.closePathStroke();
/* 730 */     tp.saveState();
/* 731 */     tp.rectangle(1.0F, 1.0F, urx - llx - 2.0F, ury - lly - 2.0F);
/* 732 */     tp.clip();
/* 733 */     tp.newPath();
/* 734 */     tp.restoreState();
/* 735 */     field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, tp);
/*     */   }
/*     */ 
/*     */   
/*     */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/* 740 */     PdfWriter.checkPdfIsoConformance(writer, 15, this);
/* 741 */     super.toPdf(writer, os);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfAcroForm.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */