/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.Rectangle;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseField
/*     */ {
/*     */   public static final float BORDER_WIDTH_THIN = 1.0F;
/*     */   public static final float BORDER_WIDTH_MEDIUM = 2.0F;
/*     */   public static final float BORDER_WIDTH_THICK = 3.0F;
/*     */   public static final int VISIBLE = 0;
/*     */   public static final int HIDDEN = 1;
/*     */   public static final int VISIBLE_BUT_DOES_NOT_PRINT = 2;
/*     */   public static final int HIDDEN_BUT_PRINTABLE = 3;
/*     */   public static final int READ_ONLY = 1;
/*     */   public static final int REQUIRED = 2;
/*     */   public static final int MULTILINE = 4096;
/*     */   public static final int DO_NOT_SCROLL = 8388608;
/*     */   public static final int PASSWORD = 8192;
/*     */   public static final int FILE_SELECTION = 1048576;
/*     */   public static final int DO_NOT_SPELL_CHECK = 4194304;
/*     */   public static final int EDIT = 262144;
/*     */   public static final int MULTISELECT = 2097152;
/*     */   public static final int COMB = 16777216;
/* 121 */   protected float borderWidth = 1.0F;
/* 122 */   protected int borderStyle = 0;
/*     */   protected BaseColor borderColor;
/*     */   protected BaseColor backgroundColor;
/*     */   protected BaseColor textColor;
/*     */   protected BaseFont font;
/* 127 */   protected float fontSize = 0.0F;
/* 128 */   protected int alignment = 0;
/*     */   
/*     */   protected PdfWriter writer;
/*     */   
/*     */   protected String text;
/*     */   protected Rectangle box;
/* 134 */   protected int rotation = 0;
/*     */ 
/*     */   
/*     */   protected int visibility;
/*     */ 
/*     */   
/*     */   protected String fieldName;
/*     */ 
/*     */   
/*     */   protected int options;
/*     */ 
/*     */   
/*     */   protected int maxCharacterLength;
/*     */   
/* 148 */   private static final HashMap<PdfName, Integer> fieldKeys = new HashMap<PdfName, Integer>();
/*     */   
/*     */   static {
/* 151 */     fieldKeys.putAll(PdfCopyFieldsImp.fieldKeys);
/* 152 */     fieldKeys.put(PdfName.T, Integer.valueOf(1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseField(PdfWriter writer, Rectangle box, String fieldName) {
/* 161 */     this.writer = writer;
/* 162 */     setBox(box);
/* 163 */     this.fieldName = fieldName;
/*     */   }
/*     */   
/*     */   protected BaseFont getRealFont() throws IOException, DocumentException {
/* 167 */     if (this.font == null) {
/* 168 */       return BaseFont.createFont("Helvetica", "Cp1252", false);
/*     */     }
/* 170 */     return this.font;
/*     */   }
/*     */   
/*     */   protected PdfAppearance getBorderAppearance() {
/* 174 */     PdfAppearance app = PdfAppearance.createAppearance(this.writer, this.box.getWidth(), this.box.getHeight());
/* 175 */     switch (this.rotation) {
/*     */       case 90:
/* 177 */         app.setMatrix(0.0F, 1.0F, -1.0F, 0.0F, this.box.getHeight(), 0.0F);
/*     */         break;
/*     */       case 180:
/* 180 */         app.setMatrix(-1.0F, 0.0F, 0.0F, -1.0F, this.box.getWidth(), this.box.getHeight());
/*     */         break;
/*     */       case 270:
/* 183 */         app.setMatrix(0.0F, -1.0F, 1.0F, 0.0F, 0.0F, this.box.getWidth());
/*     */         break;
/*     */     } 
/* 186 */     app.saveState();
/*     */     
/* 188 */     if (this.backgroundColor != null) {
/* 189 */       app.setColorFill(this.backgroundColor);
/* 190 */       app.rectangle(0.0F, 0.0F, this.box.getWidth(), this.box.getHeight());
/* 191 */       app.fill();
/*     */     } 
/*     */     
/* 194 */     if (this.borderStyle == 4) {
/* 195 */       if (this.borderWidth != 0.0F && this.borderColor != null) {
/* 196 */         app.setColorStroke(this.borderColor);
/* 197 */         app.setLineWidth(this.borderWidth);
/* 198 */         app.moveTo(0.0F, this.borderWidth / 2.0F);
/* 199 */         app.lineTo(this.box.getWidth(), this.borderWidth / 2.0F);
/* 200 */         app.stroke();
/*     */       }
/*     */     
/* 203 */     } else if (this.borderStyle == 2) {
/* 204 */       if (this.borderWidth != 0.0F && this.borderColor != null) {
/* 205 */         app.setColorStroke(this.borderColor);
/* 206 */         app.setLineWidth(this.borderWidth);
/* 207 */         app.rectangle(this.borderWidth / 2.0F, this.borderWidth / 2.0F, this.box.getWidth() - this.borderWidth, this.box.getHeight() - this.borderWidth);
/* 208 */         app.stroke();
/*     */       } 
/*     */       
/* 211 */       BaseColor actual = this.backgroundColor;
/* 212 */       if (actual == null)
/* 213 */         actual = BaseColor.WHITE; 
/* 214 */       app.setGrayFill(1.0F);
/* 215 */       drawTopFrame(app);
/* 216 */       app.setColorFill(actual.darker());
/* 217 */       drawBottomFrame(app);
/*     */     }
/* 219 */     else if (this.borderStyle == 3) {
/* 220 */       if (this.borderWidth != 0.0F && this.borderColor != null) {
/* 221 */         app.setColorStroke(this.borderColor);
/* 222 */         app.setLineWidth(this.borderWidth);
/* 223 */         app.rectangle(this.borderWidth / 2.0F, this.borderWidth / 2.0F, this.box.getWidth() - this.borderWidth, this.box.getHeight() - this.borderWidth);
/* 224 */         app.stroke();
/*     */       } 
/*     */       
/* 227 */       app.setGrayFill(0.5F);
/* 228 */       drawTopFrame(app);
/* 229 */       app.setGrayFill(0.75F);
/* 230 */       drawBottomFrame(app);
/*     */     
/*     */     }
/* 233 */     else if (this.borderWidth != 0.0F && this.borderColor != null) {
/* 234 */       if (this.borderStyle == 1)
/* 235 */         app.setLineDash(3.0F, 0.0F); 
/* 236 */       app.setColorStroke(this.borderColor);
/* 237 */       app.setLineWidth(this.borderWidth);
/* 238 */       app.rectangle(this.borderWidth / 2.0F, this.borderWidth / 2.0F, this.box.getWidth() - this.borderWidth, this.box.getHeight() - this.borderWidth);
/* 239 */       app.stroke();
/* 240 */       if ((this.options & 0x1000000) != 0 && this.maxCharacterLength > 1) {
/* 241 */         float step = this.box.getWidth() / this.maxCharacterLength;
/* 242 */         float yb = this.borderWidth / 2.0F;
/* 243 */         float yt = this.box.getHeight() - this.borderWidth / 2.0F;
/* 244 */         for (int k = 1; k < this.maxCharacterLength; k++) {
/* 245 */           float x = step * k;
/* 246 */           app.moveTo(x, yb);
/* 247 */           app.lineTo(x, yt);
/*     */         } 
/* 249 */         app.stroke();
/*     */       } 
/*     */     } 
/*     */     
/* 253 */     app.restoreState();
/* 254 */     return app;
/*     */   }
/*     */   
/*     */   protected static ArrayList<String> getHardBreaks(String text) {
/* 258 */     ArrayList<String> arr = new ArrayList<String>();
/* 259 */     char[] cs = text.toCharArray();
/* 260 */     int len = cs.length;
/* 261 */     StringBuffer buf = new StringBuffer();
/* 262 */     for (int k = 0; k < len; k++) {
/* 263 */       char c = cs[k];
/* 264 */       if (c == '\r') {
/* 265 */         if (k + 1 < len && cs[k + 1] == '\n')
/* 266 */           k++; 
/* 267 */         arr.add(buf.toString());
/* 268 */         buf = new StringBuffer();
/*     */       }
/* 270 */       else if (c == '\n') {
/* 271 */         arr.add(buf.toString());
/* 272 */         buf = new StringBuffer();
/*     */       } else {
/*     */         
/* 275 */         buf.append(c);
/*     */       } 
/* 277 */     }  arr.add(buf.toString());
/* 278 */     return arr;
/*     */   }
/*     */   
/*     */   protected static void trimRight(StringBuffer buf) {
/* 282 */     int len = buf.length();
/*     */     while (true) {
/* 284 */       if (len == 0)
/*     */         return; 
/* 286 */       if (buf.charAt(--len) != ' ')
/*     */         return; 
/* 288 */       buf.setLength(len);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static ArrayList<String> breakLines(ArrayList<String> breaks, BaseFont font, float fontSize, float width) {
/* 293 */     ArrayList<String> lines = new ArrayList<String>();
/* 294 */     StringBuffer buf = new StringBuffer();
/* 295 */     for (int ck = 0; ck < breaks.size(); ck++) {
/* 296 */       buf.setLength(0);
/* 297 */       float w = 0.0F;
/* 298 */       char[] cs = ((String)breaks.get(ck)).toCharArray();
/* 299 */       int len = cs.length;
/*     */       
/* 301 */       int state = 0;
/* 302 */       int lastspace = -1;
/* 303 */       char c = Character.MIN_VALUE;
/* 304 */       int refk = 0;
/* 305 */       for (int k = 0; k < len; k++) {
/* 306 */         c = cs[k];
/* 307 */         switch (state) {
/*     */           case 0:
/* 309 */             w += font.getWidthPoint(c, fontSize);
/* 310 */             buf.append(c);
/* 311 */             if (w > width) {
/* 312 */               w = 0.0F;
/* 313 */               if (buf.length() > 1) {
/* 314 */                 k--;
/* 315 */                 buf.setLength(buf.length() - 1);
/*     */               } 
/* 317 */               lines.add(buf.toString());
/* 318 */               buf.setLength(0);
/* 319 */               refk = k;
/* 320 */               if (c == ' ') {
/* 321 */                 state = 2; break;
/*     */               } 
/* 323 */               state = 1;
/*     */               break;
/*     */             } 
/* 326 */             if (c != ' ') {
/* 327 */               state = 1;
/*     */             }
/*     */             break;
/*     */           case 1:
/* 331 */             w += font.getWidthPoint(c, fontSize);
/* 332 */             buf.append(c);
/* 333 */             if (c == ' ')
/* 334 */               lastspace = k; 
/* 335 */             if (w > width) {
/* 336 */               w = 0.0F;
/* 337 */               if (lastspace >= 0) {
/* 338 */                 k = lastspace;
/* 339 */                 buf.setLength(lastspace - refk);
/* 340 */                 trimRight(buf);
/* 341 */                 lines.add(buf.toString());
/* 342 */                 buf.setLength(0);
/* 343 */                 refk = k;
/* 344 */                 lastspace = -1;
/* 345 */                 state = 2;
/*     */                 break;
/*     */               } 
/* 348 */               if (buf.length() > 1) {
/* 349 */                 k--;
/* 350 */                 buf.setLength(buf.length() - 1);
/*     */               } 
/* 352 */               lines.add(buf.toString());
/* 353 */               buf.setLength(0);
/* 354 */               refk = k;
/* 355 */               if (c == ' ') {
/* 356 */                 state = 2;
/*     */               }
/*     */             } 
/*     */             break;
/*     */           case 2:
/* 361 */             if (c != ' ') {
/* 362 */               w = 0.0F;
/* 363 */               k--;
/* 364 */               state = 1;
/*     */             } 
/*     */             break;
/*     */         } 
/*     */       } 
/* 369 */       trimRight(buf);
/* 370 */       lines.add(buf.toString());
/*     */     } 
/* 372 */     return lines;
/*     */   }
/*     */   
/*     */   private void drawTopFrame(PdfAppearance app) {
/* 376 */     app.moveTo(this.borderWidth, this.borderWidth);
/* 377 */     app.lineTo(this.borderWidth, this.box.getHeight() - this.borderWidth);
/* 378 */     app.lineTo(this.box.getWidth() - this.borderWidth, this.box.getHeight() - this.borderWidth);
/* 379 */     app.lineTo(this.box.getWidth() - 2.0F * this.borderWidth, this.box.getHeight() - 2.0F * this.borderWidth);
/* 380 */     app.lineTo(2.0F * this.borderWidth, this.box.getHeight() - 2.0F * this.borderWidth);
/* 381 */     app.lineTo(2.0F * this.borderWidth, 2.0F * this.borderWidth);
/* 382 */     app.lineTo(this.borderWidth, this.borderWidth);
/* 383 */     app.fill();
/*     */   }
/*     */   
/*     */   private void drawBottomFrame(PdfAppearance app) {
/* 387 */     app.moveTo(this.borderWidth, this.borderWidth);
/* 388 */     app.lineTo(this.box.getWidth() - this.borderWidth, this.borderWidth);
/* 389 */     app.lineTo(this.box.getWidth() - this.borderWidth, this.box.getHeight() - this.borderWidth);
/* 390 */     app.lineTo(this.box.getWidth() - 2.0F * this.borderWidth, this.box.getHeight() - 2.0F * this.borderWidth);
/* 391 */     app.lineTo(this.box.getWidth() - 2.0F * this.borderWidth, 2.0F * this.borderWidth);
/* 392 */     app.lineTo(2.0F * this.borderWidth, 2.0F * this.borderWidth);
/* 393 */     app.lineTo(this.borderWidth, this.borderWidth);
/* 394 */     app.fill();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBorderWidth() {
/* 400 */     return this.borderWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderWidth(float borderWidth) {
/* 408 */     this.borderWidth = borderWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBorderStyle() {
/* 415 */     return this.borderStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderStyle(int borderStyle) {
/* 425 */     this.borderStyle = borderStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getBorderColor() {
/* 432 */     return this.borderColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBorderColor(BaseColor borderColor) {
/* 440 */     this.borderColor = borderColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getBackgroundColor() {
/* 447 */     return this.backgroundColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBackgroundColor(BaseColor backgroundColor) {
/* 455 */     this.backgroundColor = backgroundColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getTextColor() {
/* 462 */     return this.textColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTextColor(BaseColor textColor) {
/* 470 */     this.textColor = textColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseFont getFont() {
/* 477 */     return this.font;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFont(BaseFont font) {
/* 485 */     this.font = font;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getFontSize() {
/* 492 */     return this.fontSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFontSize(float fontSize) {
/* 500 */     this.fontSize = fontSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAlignment() {
/* 507 */     return this.alignment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlignment(int alignment) {
/* 515 */     this.alignment = alignment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getText() {
/* 522 */     return this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setText(String text) {
/* 529 */     this.text = text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getBox() {
/* 536 */     return this.box;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBox(Rectangle box) {
/* 543 */     if (box == null) {
/* 544 */       this.box = null;
/*     */     } else {
/*     */       
/* 547 */       this.box = new Rectangle(box);
/* 548 */       this.box.normalize();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRotation() {
/* 556 */     return this.rotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotation(int rotation) {
/* 564 */     if (rotation % 90 != 0)
/* 565 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("rotation.must.be.a.multiple.of.90", new Object[0])); 
/* 566 */     rotation %= 360;
/* 567 */     if (rotation < 0)
/* 568 */       rotation += 360; 
/* 569 */     this.rotation = rotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRotationFromPage(Rectangle page) {
/* 577 */     setRotation(page.getRotation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVisibility() {
/* 584 */     return this.visibility;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisibility(int visibility) {
/* 593 */     this.visibility = visibility;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/* 600 */     return this.fieldName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFieldName(String fieldName) {
/* 608 */     this.fieldName = fieldName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOptions() {
/* 615 */     return this.options;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOptions(int options) {
/* 626 */     this.options = options;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxCharacterLength() {
/* 633 */     return this.maxCharacterLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxCharacterLength(int maxCharacterLength) {
/* 641 */     this.maxCharacterLength = maxCharacterLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfWriter getWriter() {
/* 649 */     return this.writer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWriter(PdfWriter writer) {
/* 657 */     this.writer = writer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void moveFields(PdfDictionary from, PdfDictionary to) {
/* 667 */     for (Iterator<PdfName> i = from.getKeys().iterator(); i.hasNext(); ) {
/* 668 */       PdfName key = i.next();
/* 669 */       if (fieldKeys.containsKey(key)) {
/* 670 */         if (to != null)
/* 671 */           to.put(key, from.get(key)); 
/* 672 */         i.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/BaseField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */