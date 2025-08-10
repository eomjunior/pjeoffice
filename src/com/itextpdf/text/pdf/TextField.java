/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.Chunk;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.Font;
/*     */ import com.itextpdf.text.Phrase;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextField
/*     */   extends BaseField
/*     */ {
/*     */   private String defaultText;
/*     */   private String[] choices;
/*     */   private String[] choiceExports;
/*  68 */   private ArrayList<Integer> choiceSelections = new ArrayList<Integer>();
/*     */   
/*     */   private int topFirst;
/*     */   
/*  72 */   private int visibleTopChoice = -1;
/*     */ 
/*     */   
/*     */   private float extraMarginLeft;
/*     */   
/*     */   private float extraMarginTop;
/*     */   
/*     */   private ArrayList<BaseFont> substitutionFonts;
/*     */   
/*     */   private BaseFont extensionFont;
/*     */ 
/*     */   
/*     */   public TextField(PdfWriter writer, Rectangle box, String fieldName) {
/*  85 */     super(writer, box, fieldName);
/*     */   }
/*     */   
/*     */   private static boolean checkRTL(String text) {
/*  89 */     if (text == null || text.length() == 0)
/*  90 */       return false; 
/*  91 */     char[] cc = text.toCharArray();
/*  92 */     for (int k = 0; k < cc.length; k++) {
/*  93 */       int c = cc[k];
/*  94 */       if (c >= 1424 && c < 1920)
/*  95 */         return true; 
/*     */     } 
/*  97 */     return false;
/*     */   }
/*     */   
/*     */   private static void changeFontSize(Phrase p, float size) {
/* 101 */     for (int k = 0; k < p.size(); k++)
/* 102 */       ((Chunk)p.get(k)).getFont().setSize(size); 
/*     */   }
/*     */   
/*     */   private Phrase composePhrase(String text, BaseFont ufont, BaseColor color, float fontSize) {
/* 106 */     Phrase phrase = null;
/* 107 */     if (this.extensionFont == null && (this.substitutionFonts == null || this.substitutionFonts.isEmpty())) {
/* 108 */       phrase = new Phrase(new Chunk(text, new Font(ufont, fontSize, 0, color)));
/*     */     } else {
/* 110 */       FontSelector fs = new FontSelector();
/* 111 */       fs.addFont(new Font(ufont, fontSize, 0, color));
/* 112 */       if (this.extensionFont != null)
/* 113 */         fs.addFont(new Font(this.extensionFont, fontSize, 0, color)); 
/* 114 */       if (this.substitutionFonts != null)
/* 115 */         for (int k = 0; k < this.substitutionFonts.size(); k++) {
/* 116 */           fs.addFont(new Font(this.substitutionFonts.get(k), fontSize, 0, color));
/*     */         } 
/* 118 */       phrase = fs.process(text);
/*     */     } 
/* 120 */     return phrase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String removeCRLF(String text) {
/* 131 */     if (text.indexOf('\n') >= 0 || text.indexOf('\r') >= 0) {
/* 132 */       char[] p = text.toCharArray();
/* 133 */       StringBuffer sb = new StringBuffer(p.length);
/* 134 */       for (int k = 0; k < p.length; k++) {
/* 135 */         char c = p[k];
/* 136 */         if (c == '\n') {
/* 137 */           sb.append(' ');
/* 138 */         } else if (c == '\r') {
/* 139 */           sb.append(' ');
/* 140 */           if (k < p.length - 1 && p[k + 1] == '\n') {
/* 141 */             k++;
/*     */           }
/*     */         } else {
/* 144 */           sb.append(c);
/*     */         } 
/* 146 */       }  return sb.toString();
/*     */     } 
/* 148 */     return text;
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
/*     */   public static String obfuscatePassword(String text) {
/* 160 */     char[] pchar = new char[text.length()];
/* 161 */     for (int i = 0; i < text.length(); i++)
/* 162 */       pchar[i] = '*'; 
/* 163 */     return new String(pchar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfAppearance getAppearance() throws IOException, DocumentException {
/*     */     String ptext;
/* 173 */     PdfAppearance app = getBorderAppearance();
/* 174 */     app.beginVariableText();
/* 175 */     if (this.text == null || this.text.length() == 0) {
/* 176 */       app.endVariableText();
/* 177 */       return app;
/*     */     } 
/*     */     
/* 180 */     boolean borderExtra = (this.borderStyle == 2 || this.borderStyle == 3);
/* 181 */     float h = this.box.getHeight() - this.borderWidth * 2.0F - this.extraMarginTop;
/* 182 */     float bw2 = this.borderWidth;
/* 183 */     if (borderExtra) {
/* 184 */       h -= this.borderWidth * 2.0F;
/* 185 */       bw2 *= 2.0F;
/*     */     } 
/* 187 */     float offsetX = Math.max(bw2, 1.0F);
/* 188 */     float offX = Math.min(bw2, offsetX);
/* 189 */     app.saveState();
/* 190 */     app.rectangle(offX, offX, this.box.getWidth() - 2.0F * offX, this.box.getHeight() - 2.0F * offX);
/* 191 */     app.clip();
/* 192 */     app.newPath();
/*     */     
/* 194 */     if ((this.options & 0x2000) != 0) {
/* 195 */       ptext = obfuscatePassword(this.text);
/* 196 */     } else if ((this.options & 0x1000) == 0) {
/* 197 */       ptext = removeCRLF(this.text);
/*     */     } else {
/* 199 */       ptext = this.text;
/* 200 */     }  BaseFont ufont = getRealFont();
/* 201 */     BaseColor fcolor = (this.textColor == null) ? GrayColor.GRAYBLACK : this.textColor;
/* 202 */     int rtl = checkRTL(ptext) ? 2 : 1;
/* 203 */     float usize = this.fontSize;
/* 204 */     Phrase phrase = composePhrase(ptext, ufont, fcolor, usize);
/* 205 */     if ((this.options & 0x1000) != 0) {
/* 206 */       float width = this.box.getWidth() - 4.0F * offsetX - this.extraMarginLeft;
/* 207 */       float factor = ufont.getFontDescriptor(8, 1.0F) - ufont.getFontDescriptor(6, 1.0F);
/* 208 */       ColumnText ct = new ColumnText(null);
/* 209 */       if (usize == 0.0F) {
/* 210 */         usize = h / factor;
/* 211 */         if (usize > 4.0F) {
/* 212 */           if (usize > 12.0F)
/* 213 */             usize = 12.0F; 
/* 214 */           float step = Math.max((usize - 4.0F) / 10.0F, 0.2F);
/* 215 */           ct.setSimpleColumn(0.0F, -h, width, 0.0F);
/* 216 */           ct.setAlignment(this.alignment);
/* 217 */           ct.setRunDirection(rtl);
/* 218 */           for (; usize > 4.0F; usize -= step) {
/* 219 */             ct.setYLine(0.0F);
/* 220 */             changeFontSize(phrase, usize);
/* 221 */             ct.setText(phrase);
/* 222 */             ct.setLeading(factor * usize);
/* 223 */             int status = ct.go(true);
/* 224 */             if ((status & 0x2) == 0)
/*     */               break; 
/*     */           } 
/*     */         } 
/* 228 */         if (usize < 4.0F)
/* 229 */           usize = 4.0F; 
/*     */       } 
/* 231 */       changeFontSize(phrase, usize);
/* 232 */       ct.setCanvas(app);
/* 233 */       float leading = usize * factor;
/* 234 */       float offsetY = offsetX + h - ufont.getFontDescriptor(8, usize);
/* 235 */       ct.setSimpleColumn(this.extraMarginLeft + 2.0F * offsetX, -20000.0F, this.box.getWidth() - 2.0F * offsetX, offsetY + leading);
/* 236 */       ct.setLeading(leading);
/* 237 */       ct.setAlignment(this.alignment);
/* 238 */       ct.setRunDirection(rtl);
/* 239 */       ct.setText(phrase);
/* 240 */       ct.go();
/*     */     } else {
/*     */       
/* 243 */       if (usize == 0.0F) {
/* 244 */         float maxCalculatedSize = h / (ufont.getFontDescriptor(7, 1.0F) - ufont.getFontDescriptor(6, 1.0F));
/* 245 */         changeFontSize(phrase, 1.0F);
/* 246 */         float wd = ColumnText.getWidth(phrase, rtl, 0);
/* 247 */         if (wd == 0.0F) {
/* 248 */           usize = maxCalculatedSize;
/*     */         } else {
/* 250 */           usize = Math.min(maxCalculatedSize, (this.box.getWidth() - this.extraMarginLeft - 4.0F * offsetX) / wd);
/* 251 */         }  if (usize < 4.0F)
/* 252 */           usize = 4.0F; 
/*     */       } 
/* 254 */       changeFontSize(phrase, usize);
/* 255 */       float offsetY = offX + (this.box.getHeight() - 2.0F * offX - ufont.getFontDescriptor(1, usize)) / 2.0F;
/* 256 */       if (offsetY < offX)
/* 257 */         offsetY = offX; 
/* 258 */       if (offsetY - offX < -ufont.getFontDescriptor(3, usize)) {
/* 259 */         float ny = -ufont.getFontDescriptor(3, usize) + offX;
/* 260 */         float dy = this.box.getHeight() - offX - ufont.getFontDescriptor(1, usize);
/* 261 */         offsetY = Math.min(ny, Math.max(offsetY, dy));
/*     */       } 
/* 263 */       if ((this.options & 0x1000000) != 0 && this.maxCharacterLength > 0) {
/* 264 */         int textLen = Math.min(this.maxCharacterLength, ptext.length());
/* 265 */         int position = 0;
/* 266 */         if (this.alignment == 2) {
/* 267 */           position = this.maxCharacterLength - textLen;
/* 268 */         } else if (this.alignment == 1) {
/* 269 */           position = (this.maxCharacterLength - textLen) / 2;
/* 270 */         }  float step = (this.box.getWidth() - this.extraMarginLeft) / this.maxCharacterLength;
/* 271 */         float start = step / 2.0F + position * step;
/* 272 */         if (this.textColor == null) {
/* 273 */           app.setGrayFill(0.0F);
/*     */         } else {
/* 275 */           app.setColorFill(this.textColor);
/* 276 */         }  app.beginText();
/* 277 */         for (int k = 0; k < phrase.size(); k++) {
/* 278 */           Chunk ck = (Chunk)phrase.get(k);
/* 279 */           BaseFont bf = ck.getFont().getBaseFont();
/* 280 */           app.setFontAndSize(bf, usize);
/* 281 */           StringBuffer sb = ck.append("");
/* 282 */           for (int j = 0; j < sb.length(); j++) {
/* 283 */             String c = sb.substring(j, j + 1);
/* 284 */             float wd = bf.getWidthPoint(c, usize);
/* 285 */             app.setTextMatrix(this.extraMarginLeft + start - wd / 2.0F, offsetY - this.extraMarginTop);
/* 286 */             app.showText(c);
/* 287 */             start += step;
/*     */           } 
/*     */         } 
/* 290 */         app.endText();
/*     */       } else {
/*     */         float x;
/*     */         
/* 294 */         switch (this.alignment) {
/*     */           case 2:
/* 296 */             x = this.extraMarginLeft + this.box.getWidth() - 2.0F * offsetX;
/*     */             break;
/*     */           case 1:
/* 299 */             x = this.extraMarginLeft + this.box.getWidth() / 2.0F;
/*     */             break;
/*     */           default:
/* 302 */             x = this.extraMarginLeft + 2.0F * offsetX; break;
/*     */         } 
/* 304 */         ColumnText.showTextAligned(app, this.alignment, phrase, x, offsetY - this.extraMarginTop, 0.0F, rtl, 0);
/*     */       } 
/*     */     } 
/* 307 */     app.restoreState();
/* 308 */     app.endVariableText();
/* 309 */     return app;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfAppearance getListAppearance() throws IOException, DocumentException {
/* 319 */     PdfAppearance app = getBorderAppearance();
/* 320 */     if (this.choices == null || this.choices.length == 0) {
/* 321 */       return app;
/*     */     }
/* 323 */     app.beginVariableText();
/*     */     
/* 325 */     int topChoice = getTopChoice();
/*     */     
/* 327 */     BaseFont ufont = getRealFont();
/* 328 */     float usize = this.fontSize;
/* 329 */     if (usize == 0.0F) {
/* 330 */       usize = 12.0F;
/*     */     }
/* 332 */     boolean borderExtra = (this.borderStyle == 2 || this.borderStyle == 3);
/* 333 */     float h = this.box.getHeight() - this.borderWidth * 2.0F;
/* 334 */     float offsetX = this.borderWidth;
/* 335 */     if (borderExtra) {
/* 336 */       h -= this.borderWidth * 2.0F;
/* 337 */       offsetX *= 2.0F;
/*     */     } 
/*     */     
/* 340 */     float leading = ufont.getFontDescriptor(8, usize) - ufont.getFontDescriptor(6, usize);
/* 341 */     int maxFit = (int)(h / leading) + 1;
/* 342 */     int first = 0;
/* 343 */     int last = 0;
/* 344 */     first = topChoice;
/* 345 */     last = first + maxFit;
/* 346 */     if (last > this.choices.length)
/* 347 */       last = this.choices.length; 
/* 348 */     this.topFirst = first;
/* 349 */     app.saveState();
/* 350 */     app.rectangle(offsetX, offsetX, this.box.getWidth() - 2.0F * offsetX, this.box.getHeight() - 2.0F * offsetX);
/* 351 */     app.clip();
/* 352 */     app.newPath();
/* 353 */     BaseColor fcolor = (this.textColor == null) ? GrayColor.GRAYBLACK : this.textColor;
/*     */ 
/*     */ 
/*     */     
/* 357 */     app.setColorFill(new BaseColor(10, 36, 106));
/* 358 */     for (int curVal = 0; curVal < this.choiceSelections.size(); curVal++) {
/* 359 */       int curChoice = ((Integer)this.choiceSelections.get(curVal)).intValue();
/*     */ 
/*     */       
/* 362 */       if (curChoice >= first && curChoice <= last) {
/* 363 */         app.rectangle(offsetX, offsetX + h - (curChoice - first + 1) * leading, this.box.getWidth() - 2.0F * offsetX, leading);
/* 364 */         app.fill();
/*     */       } 
/*     */     } 
/* 367 */     float xp = offsetX * 2.0F;
/* 368 */     float yp = offsetX + h - ufont.getFontDescriptor(8, usize);
/* 369 */     for (int idx = first; idx < last; idx++, yp -= leading) {
/* 370 */       String ptext = this.choices[idx];
/* 371 */       int rtl = checkRTL(ptext) ? 2 : 1;
/* 372 */       ptext = removeCRLF(ptext);
/*     */       
/* 374 */       BaseColor textCol = this.choiceSelections.contains(Integer.valueOf(idx)) ? GrayColor.GRAYWHITE : fcolor;
/* 375 */       Phrase phrase = composePhrase(ptext, ufont, textCol, usize);
/* 376 */       ColumnText.showTextAligned(app, 0, phrase, xp, yp, 0.0F, rtl, 0);
/*     */     } 
/* 378 */     app.restoreState();
/* 379 */     app.endVariableText();
/* 380 */     return app;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfFormField getTextField() throws IOException, DocumentException {
/* 390 */     if (this.maxCharacterLength <= 0)
/* 391 */       this.options &= 0xFEFFFFFF; 
/* 392 */     if ((this.options & 0x1000000) != 0)
/* 393 */       this.options &= 0xFFFFEFFF; 
/* 394 */     PdfFormField field = PdfFormField.createTextField(this.writer, false, false, this.maxCharacterLength);
/* 395 */     field.setWidget(this.box, PdfAnnotation.HIGHLIGHT_INVERT);
/* 396 */     switch (this.alignment) {
/*     */       case 1:
/* 398 */         field.setQuadding(1);
/*     */         break;
/*     */       case 2:
/* 401 */         field.setQuadding(2);
/*     */         break;
/*     */     } 
/* 404 */     if (this.rotation != 0)
/* 405 */       field.setMKRotation(this.rotation); 
/* 406 */     if (this.fieldName != null) {
/* 407 */       field.setFieldName(this.fieldName);
/* 408 */       if (!"".equals(this.text))
/* 409 */         field.setValueAsString(this.text); 
/* 410 */       if (this.defaultText != null)
/* 411 */         field.setDefaultValueAsString(this.defaultText); 
/* 412 */       if ((this.options & 0x1) != 0)
/* 413 */         field.setFieldFlags(1); 
/* 414 */       if ((this.options & 0x2) != 0)
/* 415 */         field.setFieldFlags(2); 
/* 416 */       if ((this.options & 0x1000) != 0)
/* 417 */         field.setFieldFlags(4096); 
/* 418 */       if ((this.options & 0x800000) != 0)
/* 419 */         field.setFieldFlags(8388608); 
/* 420 */       if ((this.options & 0x2000) != 0)
/* 421 */         field.setFieldFlags(8192); 
/* 422 */       if ((this.options & 0x100000) != 0)
/* 423 */         field.setFieldFlags(1048576); 
/* 424 */       if ((this.options & 0x400000) != 0)
/* 425 */         field.setFieldFlags(4194304); 
/* 426 */       if ((this.options & 0x1000000) != 0)
/* 427 */         field.setFieldFlags(16777216); 
/*     */     } 
/* 429 */     field.setBorderStyle(new PdfBorderDictionary(this.borderWidth, this.borderStyle, new PdfDashPattern(3.0F)));
/* 430 */     PdfAppearance tp = getAppearance();
/* 431 */     field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, tp);
/* 432 */     PdfAppearance da = (PdfAppearance)tp.getDuplicate();
/* 433 */     da.setFontAndSize(getRealFont(), this.fontSize);
/* 434 */     if (this.textColor == null) {
/* 435 */       da.setGrayFill(0.0F);
/*     */     } else {
/* 437 */       da.setColorFill(this.textColor);
/* 438 */     }  field.setDefaultAppearanceString(da);
/* 439 */     if (this.borderColor != null)
/* 440 */       field.setMKBorderColor(this.borderColor); 
/* 441 */     if (this.backgroundColor != null)
/* 442 */       field.setMKBackgroundColor(this.backgroundColor); 
/* 443 */     switch (this.visibility) {
/*     */       case 1:
/* 445 */         field.setFlags(6);
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
/* 456 */         return field;
/*     */       case 3:
/*     */         field.setFlags(36);
/*     */     } 
/*     */     field.setFlags(4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfFormField getComboField() throws IOException, DocumentException {
/* 466 */     return getChoiceField(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfFormField getListField() throws IOException, DocumentException {
/* 476 */     return getChoiceField(true);
/*     */   }
/*     */   
/*     */   private int getTopChoice() {
/* 480 */     if (this.choiceSelections == null || this.choiceSelections.size() == 0) {
/* 481 */       return 0;
/*     */     }
/*     */     
/* 484 */     Integer firstValue = this.choiceSelections.get(0);
/*     */     
/* 486 */     if (firstValue == null) {
/* 487 */       return 0;
/*     */     }
/*     */     
/* 490 */     int topChoice = 0;
/* 491 */     if (this.choices != null) {
/* 492 */       if (this.visibleTopChoice != -1) {
/* 493 */         return this.visibleTopChoice;
/*     */       }
/*     */       
/* 496 */       topChoice = firstValue.intValue();
/* 497 */       topChoice = Math.min(topChoice, this.choices.length);
/* 498 */       topChoice = Math.max(0, topChoice);
/*     */     } 
/* 500 */     return topChoice;
/*     */   }
/*     */   
/*     */   protected PdfFormField getChoiceField(boolean isList) throws IOException, DocumentException { PdfAppearance tp;
/* 504 */     this.options &= 0xFEFFEFFF;
/* 505 */     String[] uchoices = this.choices;
/* 506 */     if (uchoices == null) {
/* 507 */       uchoices = new String[0];
/*     */     }
/* 509 */     int topChoice = getTopChoice();
/*     */     
/* 511 */     if (uchoices.length > 0 && topChoice >= 0) {
/* 512 */       this.text = uchoices[topChoice];
/*     */     }
/* 514 */     if (this.text == null) {
/* 515 */       this.text = "";
/*     */     }
/* 517 */     PdfFormField field = null;
/* 518 */     String[][] mix = (String[][])null;
/*     */     
/* 520 */     if (this.choiceExports == null) {
/* 521 */       if (isList) {
/* 522 */         field = PdfFormField.createList(this.writer, uchoices, topChoice);
/*     */       } else {
/* 524 */         field = PdfFormField.createCombo(this.writer, ((this.options & 0x40000) != 0), uchoices, topChoice);
/*     */       } 
/*     */     } else {
/* 527 */       mix = new String[uchoices.length][2];
/* 528 */       for (int k = 0; k < mix.length; k++) {
/* 529 */         mix[k][1] = uchoices[k]; mix[k][0] = uchoices[k];
/* 530 */       }  int top = Math.min(uchoices.length, this.choiceExports.length);
/* 531 */       for (int i = 0; i < top; i++) {
/* 532 */         if (this.choiceExports[i] != null)
/* 533 */           mix[i][0] = this.choiceExports[i]; 
/*     */       } 
/* 535 */       if (isList) {
/* 536 */         field = PdfFormField.createList(this.writer, mix, topChoice);
/*     */       } else {
/* 538 */         field = PdfFormField.createCombo(this.writer, ((this.options & 0x40000) != 0), mix, topChoice);
/*     */       } 
/* 540 */     }  field.setWidget(this.box, PdfAnnotation.HIGHLIGHT_INVERT);
/* 541 */     if (this.rotation != 0)
/* 542 */       field.setMKRotation(this.rotation); 
/* 543 */     if (this.fieldName != null) {
/* 544 */       field.setFieldName(this.fieldName);
/* 545 */       if (uchoices.length > 0) {
/* 546 */         if (mix != null) {
/* 547 */           if (this.choiceSelections.size() < 2) {
/* 548 */             field.setValueAsString(mix[topChoice][0]);
/* 549 */             field.setDefaultValueAsString(mix[topChoice][0]);
/*     */           } else {
/* 551 */             writeMultipleValues(field, mix);
/*     */           }
/*     */         
/* 554 */         } else if (this.choiceSelections.size() < 2) {
/* 555 */           field.setValueAsString(this.text);
/* 556 */           field.setDefaultValueAsString(this.text);
/*     */         } else {
/* 558 */           writeMultipleValues(field, (String[][])null);
/*     */         } 
/*     */       }
/*     */       
/* 562 */       if ((this.options & 0x1) != 0)
/* 563 */         field.setFieldFlags(1); 
/* 564 */       if ((this.options & 0x2) != 0)
/* 565 */         field.setFieldFlags(2); 
/* 566 */       if ((this.options & 0x400000) != 0)
/* 567 */         field.setFieldFlags(4194304); 
/* 568 */       if ((this.options & 0x200000) != 0) {
/* 569 */         field.setFieldFlags(2097152);
/*     */       }
/*     */     } 
/* 572 */     field.setBorderStyle(new PdfBorderDictionary(this.borderWidth, this.borderStyle, new PdfDashPattern(3.0F)));
/*     */     
/* 574 */     if (isList) {
/* 575 */       tp = getListAppearance();
/* 576 */       if (this.topFirst > 0) {
/* 577 */         field.put(PdfName.TI, new PdfNumber(this.topFirst));
/*     */       }
/*     */     } else {
/* 580 */       tp = getAppearance();
/* 581 */     }  field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, tp);
/* 582 */     PdfAppearance da = (PdfAppearance)tp.getDuplicate();
/* 583 */     da.setFontAndSize(getRealFont(), this.fontSize);
/* 584 */     if (this.textColor == null) {
/* 585 */       da.setGrayFill(0.0F);
/*     */     } else {
/* 587 */       da.setColorFill(this.textColor);
/* 588 */     }  field.setDefaultAppearanceString(da);
/* 589 */     if (this.borderColor != null)
/* 590 */       field.setMKBorderColor(this.borderColor); 
/* 591 */     if (this.backgroundColor != null)
/* 592 */       field.setMKBackgroundColor(this.backgroundColor); 
/* 593 */     switch (this.visibility) {
/*     */       case 1:
/* 595 */         field.setFlags(6);
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
/* 606 */         return field;
/*     */       case 3:
/*     */         field.setFlags(36);
/*     */     } 
/* 610 */     field.setFlags(4); } private void writeMultipleValues(PdfFormField field, String[][] mix) { PdfArray indexes = new PdfArray();
/* 611 */     PdfArray values = new PdfArray();
/* 612 */     for (int i = 0; i < this.choiceSelections.size(); i++) {
/* 613 */       int idx = ((Integer)this.choiceSelections.get(i)).intValue();
/* 614 */       indexes.add(new PdfNumber(idx));
/*     */       
/* 616 */       if (mix != null) {
/* 617 */         values.add(new PdfString(mix[idx][0]));
/* 618 */       } else if (this.choices != null) {
/* 619 */         values.add(new PdfString(this.choices[idx]));
/*     */       } 
/*     */     } 
/* 622 */     field.put(PdfName.V, values);
/* 623 */     field.put(PdfName.I, indexes); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultText() {
/* 632 */     return this.defaultText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultText(String defaultText) {
/* 640 */     this.defaultText = defaultText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getChoices() {
/* 648 */     return this.choices;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChoices(String[] choices) {
/* 656 */     this.choices = choices;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getChoiceExports() {
/* 664 */     return this.choiceExports;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChoiceExports(String[] choiceExports) {
/* 674 */     this.choiceExports = choiceExports;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getChoiceSelection() {
/* 682 */     return getTopChoice();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<Integer> getChoiceSelections() {
/* 692 */     return this.choiceSelections;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisibleTopChoice(int visibleTopChoice) {
/* 702 */     if (visibleTopChoice < 0) {
/*     */       return;
/*     */     }
/*     */     
/* 706 */     if (this.choices != null && 
/* 707 */       visibleTopChoice < this.choices.length) {
/* 708 */       this.visibleTopChoice = visibleTopChoice;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVisibleTopChoice() {
/* 719 */     return this.visibleTopChoice;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChoiceSelection(int choiceSelection) {
/* 727 */     this.choiceSelections = new ArrayList<Integer>();
/* 728 */     this.choiceSelections.add(Integer.valueOf(choiceSelection));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addChoiceSelection(int selection) {
/* 737 */     if ((this.options & 0x200000) != 0) {
/* 738 */       this.choiceSelections.add(Integer.valueOf(selection));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChoiceSelections(ArrayList<Integer> selections) {
/* 748 */     if (selections != null) {
/* 749 */       this.choiceSelections = new ArrayList<Integer>(selections);
/* 750 */       if (this.choiceSelections.size() > 1 && (this.options & 0x200000) == 0)
/*     */       {
/* 752 */         while (this.choiceSelections.size() > 1) {
/* 753 */           this.choiceSelections.remove(1);
/*     */         }
/*     */       }
/*     */     } else {
/*     */       
/* 758 */       this.choiceSelections.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   int getTopFirst() {
/* 763 */     return this.topFirst;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExtraMargin(float extraMarginLeft, float extraMarginTop) {
/* 772 */     this.extraMarginLeft = extraMarginLeft;
/* 773 */     this.extraMarginTop = extraMarginTop;
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
/*     */   public ArrayList<BaseFont> getSubstitutionFonts() {
/* 787 */     return this.substitutionFonts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubstitutionFonts(ArrayList<BaseFont> substitutionFonts) {
/* 796 */     this.substitutionFonts = substitutionFonts;
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
/*     */   public BaseFont getExtensionFont() {
/* 810 */     return this.extensionFont;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExtensionFont(BaseFont extensionFont) {
/* 819 */     this.extensionFont = extensionFont;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/TextField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */