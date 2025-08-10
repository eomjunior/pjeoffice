/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.Chunk;
/*     */ import com.itextpdf.text.Font;
/*     */ import com.itextpdf.text.Image;
/*     */ import com.itextpdf.text.SplitCharacter;
/*     */ import com.itextpdf.text.TabSettings;
/*     */ import com.itextpdf.text.TabStop;
/*     */ import com.itextpdf.text.Utilities;
/*     */ import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
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
/*     */ public class PdfChunk
/*     */ {
/*  66 */   private static final char[] singleSpace = new char[] { ' ' };
/*     */   
/*     */   private static final float ITALIC_ANGLE = 0.21256F;
/*  69 */   private static final HashSet<String> keysAttributes = new HashSet<String>();
/*     */ 
/*     */   
/*  72 */   private static final HashSet<String> keysNoStroke = new HashSet<String>();
/*     */   private static final String TABSTOP = "TABSTOP";
/*     */   
/*     */   static {
/*  76 */     keysAttributes.add("ACTION");
/*  77 */     keysAttributes.add("UNDERLINE");
/*  78 */     keysAttributes.add("REMOTEGOTO");
/*  79 */     keysAttributes.add("LOCALGOTO");
/*  80 */     keysAttributes.add("LOCALDESTINATION");
/*  81 */     keysAttributes.add("GENERICTAG");
/*  82 */     keysAttributes.add("NEWPAGE");
/*  83 */     keysAttributes.add("IMAGE");
/*  84 */     keysAttributes.add("BACKGROUND");
/*  85 */     keysAttributes.add("PDFANNOTATION");
/*  86 */     keysAttributes.add("SKEW");
/*  87 */     keysAttributes.add("HSCALE");
/*  88 */     keysAttributes.add("SEPARATOR");
/*  89 */     keysAttributes.add("TAB");
/*  90 */     keysAttributes.add("TABSETTINGS");
/*  91 */     keysAttributes.add("CHAR_SPACING");
/*  92 */     keysAttributes.add("WORD_SPACING");
/*  93 */     keysAttributes.add("LINEHEIGHT");
/*  94 */     keysNoStroke.add("SUBSUPSCRIPT");
/*  95 */     keysNoStroke.add("SPLITCHARACTER");
/*  96 */     keysNoStroke.add("HYPHENATION");
/*  97 */     keysNoStroke.add("TEXTRENDERMODE");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   protected String value = "";
/*     */ 
/*     */   
/* 106 */   protected String encoding = "Cp1252";
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfFont font;
/*     */ 
/*     */ 
/*     */   
/*     */   protected BaseFont baseFont;
/*     */ 
/*     */ 
/*     */   
/*     */   protected SplitCharacter splitCharacter;
/*     */ 
/*     */   
/* 121 */   protected HashMap<String, Object> attributes = new HashMap<String, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   protected HashMap<String, Object> noStroke = new HashMap<String, Object>();
/*     */ 
/*     */   
/*     */   protected boolean newlineSplit;
/*     */   
/*     */   protected Image image;
/*     */   
/* 136 */   protected float imageScalePercentage = 1.0F;
/*     */ 
/*     */   
/*     */   protected float offsetX;
/*     */ 
/*     */   
/*     */   protected float offsetY;
/*     */ 
/*     */   
/*     */   protected boolean changeLeading = false;
/*     */ 
/*     */   
/* 148 */   protected float leading = 0.0F;
/*     */   
/* 150 */   protected IAccessibleElement accessibleElement = null;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float UNDERLINE_THICKNESS = 0.06666667F;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float UNDERLINE_OFFSET = -0.33333334F;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfChunk(String string, PdfChunk other) {
/* 164 */     this.value = string;
/* 165 */     this.font = other.font;
/* 166 */     this.attributes = other.attributes;
/* 167 */     this.noStroke = other.noStroke;
/* 168 */     this.baseFont = other.baseFont;
/* 169 */     this.changeLeading = other.changeLeading;
/* 170 */     this.leading = other.leading;
/* 171 */     Object[] obj = (Object[])this.attributes.get("IMAGE");
/* 172 */     if (obj == null) {
/* 173 */       this.image = null;
/*     */     } else {
/* 175 */       this.image = (Image)obj[0];
/* 176 */       this.offsetX = ((Float)obj[1]).floatValue();
/* 177 */       this.offsetY = ((Float)obj[2]).floatValue();
/* 178 */       this.changeLeading = ((Boolean)obj[3]).booleanValue();
/*     */     } 
/* 180 */     this.encoding = this.font.getFont().getEncoding();
/* 181 */     this.splitCharacter = (SplitCharacter)this.noStroke.get("SPLITCHARACTER");
/* 182 */     if (this.splitCharacter == null)
/* 183 */       this.splitCharacter = DefaultSplitCharacter.DEFAULT; 
/* 184 */     this.accessibleElement = other.accessibleElement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfChunk(Chunk chunk, PdfAction action) {
/* 195 */     this.value = chunk.getContent();
/*     */     
/* 197 */     Font f = chunk.getFont();
/* 198 */     float size = f.getSize();
/* 199 */     if (size == -1.0F)
/* 200 */       size = 12.0F; 
/* 201 */     this.baseFont = f.getBaseFont();
/* 202 */     int style = f.getStyle();
/* 203 */     if (style == -1) {
/* 204 */       style = 0;
/*     */     }
/* 206 */     if (this.baseFont == null) {
/*     */       
/* 208 */       this.baseFont = f.getCalculatedBaseFont(false);
/*     */     }
/*     */     else {
/*     */       
/* 212 */       if ((style & 0x1) != 0) {
/* 213 */         this.attributes.put("TEXTRENDERMODE", new Object[] { Integer.valueOf(2), new Float(size / 30.0F), null });
/*     */       }
/* 215 */       if ((style & 0x2) != 0)
/* 216 */         this.attributes.put("SKEW", new float[] { 0.0F, 0.21256F }); 
/*     */     } 
/* 218 */     this.font = new PdfFont(this.baseFont, size);
/*     */     
/* 220 */     HashMap<String, Object> attr = chunk.getAttributes();
/* 221 */     if (attr != null) {
/* 222 */       for (Map.Entry<String, Object> entry : attr.entrySet()) {
/* 223 */         String name = entry.getKey();
/* 224 */         if (keysAttributes.contains(name)) {
/* 225 */           this.attributes.put(name, entry.getValue()); continue;
/*     */         } 
/* 227 */         if (keysNoStroke.contains(name)) {
/* 228 */           this.noStroke.put(name, entry.getValue());
/*     */         }
/*     */       } 
/* 231 */       if ("".equals(attr.get("GENERICTAG"))) {
/* 232 */         this.attributes.put("GENERICTAG", chunk.getContent());
/*     */       }
/*     */     } 
/* 235 */     if (f.isUnderlined()) {
/* 236 */       Object[] arrayOfObject = { null, { 0.0F, 0.06666667F, 0.0F, -0.33333334F, 0.0F } };
/* 237 */       Object[][] unders = Utilities.addToArray((Object[][])this.attributes.get("UNDERLINE"), arrayOfObject);
/* 238 */       this.attributes.put("UNDERLINE", unders);
/*     */     } 
/* 240 */     if (f.isStrikethru()) {
/* 241 */       Object[] arrayOfObject = { null, { 0.0F, 0.06666667F, 0.0F, 0.33333334F, 0.0F } };
/* 242 */       Object[][] unders = Utilities.addToArray((Object[][])this.attributes.get("UNDERLINE"), arrayOfObject);
/* 243 */       this.attributes.put("UNDERLINE", unders);
/*     */     } 
/* 245 */     if (action != null) {
/* 246 */       this.attributes.put("ACTION", action);
/*     */     }
/* 248 */     this.noStroke.put("COLOR", f.getColor());
/* 249 */     this.noStroke.put("ENCODING", this.font.getFont().getEncoding());
/*     */     
/* 251 */     Float lh = (Float)this.attributes.get("LINEHEIGHT");
/* 252 */     if (lh != null) {
/* 253 */       this.changeLeading = true;
/* 254 */       this.leading = lh.floatValue();
/*     */     } 
/*     */     
/* 257 */     Object[] obj = (Object[])this.attributes.get("IMAGE");
/* 258 */     if (obj == null) {
/* 259 */       this.image = null;
/*     */     } else {
/*     */       
/* 262 */       this.attributes.remove("HSCALE");
/* 263 */       this.image = (Image)obj[0];
/* 264 */       this.offsetX = ((Float)obj[1]).floatValue();
/* 265 */       this.offsetY = ((Float)obj[2]).floatValue();
/* 266 */       this.changeLeading = ((Boolean)obj[3]).booleanValue();
/*     */     } 
/* 268 */     Float hs = (Float)this.attributes.get("HSCALE");
/* 269 */     if (hs != null)
/* 270 */       this.font.setHorizontalScaling(hs.floatValue()); 
/* 271 */     this.encoding = this.font.getFont().getEncoding();
/* 272 */     this.splitCharacter = (SplitCharacter)this.noStroke.get("SPLITCHARACTER");
/* 273 */     if (this.splitCharacter == null)
/* 274 */       this.splitCharacter = DefaultSplitCharacter.DEFAULT; 
/* 275 */     this.accessibleElement = (IAccessibleElement)chunk;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PdfChunk(Chunk chunk, PdfAction action, TabSettings tabSettings) {
/* 286 */     this(chunk, action);
/* 287 */     if (tabSettings != null && this.attributes.get("TABSETTINGS") == null) {
/* 288 */       this.attributes.put("TABSETTINGS", tabSettings);
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
/*     */   public int getUnicodeEquivalent(int c) {
/* 300 */     return this.baseFont.getUnicodeEquivalent(c);
/*     */   }
/*     */   
/*     */   protected int getWord(String text, int start) {
/* 304 */     int len = text.length();
/* 305 */     while (start < len && 
/* 306 */       Character.isLetter(text.charAt(start)))
/*     */     {
/* 308 */       start++;
/*     */     }
/* 310 */     return start;
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
/*     */   PdfChunk split(float width) {
/* 323 */     this.newlineSplit = false;
/* 324 */     if (this.image != null) {
/* 325 */       if (this.image.getScaledWidth() > width) {
/* 326 */         PdfChunk pdfChunk = new PdfChunk("￼", this);
/* 327 */         this.value = "";
/* 328 */         this.attributes = new HashMap<String, Object>();
/* 329 */         this.image = null;
/* 330 */         this.font = PdfFont.getDefaultFont();
/* 331 */         return pdfChunk;
/*     */       } 
/*     */       
/* 334 */       return null;
/*     */     } 
/* 336 */     HyphenationEvent hyphenationEvent = (HyphenationEvent)this.noStroke.get("HYPHENATION");
/* 337 */     int currentPosition = 0;
/* 338 */     int splitPosition = -1;
/* 339 */     float currentWidth = 0.0F;
/*     */ 
/*     */ 
/*     */     
/* 343 */     int lastSpace = -1;
/* 344 */     float lastSpaceWidth = 0.0F;
/* 345 */     int length = this.value.length();
/* 346 */     char[] valueArray = this.value.toCharArray();
/* 347 */     char character = Character.MIN_VALUE;
/* 348 */     BaseFont ft = this.font.getFont();
/* 349 */     boolean surrogate = false;
/* 350 */     if (ft.getFontType() == 2 && ft.getUnicodeEquivalent(32) != 32) {
/* 351 */       while (currentPosition < length) {
/*     */         
/* 353 */         char cidChar = valueArray[currentPosition];
/* 354 */         character = (char)ft.getUnicodeEquivalent(cidChar);
/*     */         
/* 356 */         if (character == '\n') {
/* 357 */           this.newlineSplit = true;
/* 358 */           String str = this.value.substring(currentPosition + 1);
/* 359 */           this.value = this.value.substring(0, currentPosition);
/* 360 */           if (this.value.length() < 1) {
/* 361 */             this.value = "\001";
/*     */           }
/* 363 */           PdfChunk pdfChunk = new PdfChunk(str, this);
/* 364 */           return pdfChunk;
/*     */         } 
/* 366 */         currentWidth += getCharWidth(cidChar);
/* 367 */         if (character == ' ') {
/* 368 */           lastSpace = currentPosition + 1;
/* 369 */           lastSpaceWidth = currentWidth;
/*     */         } 
/* 371 */         if (currentWidth > width) {
/*     */           break;
/*     */         }
/* 374 */         if (this.splitCharacter.isSplitCharacter(0, currentPosition, length, valueArray, new PdfChunk[] { this }))
/* 375 */           splitPosition = currentPosition + 1; 
/* 376 */         currentPosition++;
/*     */       } 
/*     */     } else {
/*     */       
/* 380 */       while (currentPosition < length) {
/*     */         
/* 382 */         character = valueArray[currentPosition];
/*     */         
/* 384 */         if (character == '\r' || character == '\n') {
/* 385 */           this.newlineSplit = true;
/* 386 */           int inc = 1;
/* 387 */           if (character == '\r' && currentPosition + 1 < length && valueArray[currentPosition + 1] == '\n')
/* 388 */             inc = 2; 
/* 389 */           String str = this.value.substring(currentPosition + inc);
/* 390 */           this.value = this.value.substring(0, currentPosition);
/* 391 */           if (this.value.length() < 1) {
/* 392 */             this.value = " ";
/*     */           }
/* 394 */           PdfChunk pdfChunk = new PdfChunk(str, this);
/* 395 */           return pdfChunk;
/*     */         } 
/* 397 */         surrogate = Utilities.isSurrogatePair(valueArray, currentPosition);
/* 398 */         if (surrogate) {
/* 399 */           currentWidth += getCharWidth(Utilities.convertToUtf32(valueArray[currentPosition], valueArray[currentPosition + 1]));
/*     */         } else {
/* 401 */           currentWidth += getCharWidth(character);
/* 402 */         }  if (character == ' ') {
/* 403 */           lastSpace = currentPosition + 1;
/* 404 */           lastSpaceWidth = currentWidth;
/*     */         } 
/* 406 */         if (surrogate)
/* 407 */           currentPosition++; 
/* 408 */         if (currentWidth > width) {
/*     */           break;
/*     */         }
/* 411 */         if (this.splitCharacter.isSplitCharacter(0, currentPosition, length, valueArray, null))
/* 412 */           splitPosition = currentPosition + 1; 
/* 413 */         currentPosition++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 418 */     if (currentPosition == length) {
/* 419 */       return null;
/*     */     }
/*     */     
/* 422 */     if (splitPosition < 0) {
/* 423 */       String str = this.value;
/* 424 */       this.value = "";
/* 425 */       PdfChunk pdfChunk = new PdfChunk(str, this);
/* 426 */       return pdfChunk;
/*     */     } 
/* 428 */     if (lastSpace > splitPosition && this.splitCharacter.isSplitCharacter(0, 0, 1, singleSpace, null))
/* 429 */       splitPosition = lastSpace; 
/* 430 */     if (hyphenationEvent != null && lastSpace >= 0 && lastSpace < currentPosition) {
/* 431 */       int wordIdx = getWord(this.value, lastSpace);
/* 432 */       if (wordIdx > lastSpace) {
/* 433 */         String pre = hyphenationEvent.getHyphenatedWordPre(this.value.substring(lastSpace, wordIdx), this.font.getFont(), this.font.size(), width - lastSpaceWidth);
/* 434 */         String post = hyphenationEvent.getHyphenatedWordPost();
/* 435 */         if (pre.length() > 0) {
/* 436 */           String str = post + this.value.substring(wordIdx);
/* 437 */           this.value = trim(this.value.substring(0, lastSpace) + pre);
/* 438 */           PdfChunk pdfChunk = new PdfChunk(str, this);
/* 439 */           return pdfChunk;
/*     */         } 
/*     */       } 
/*     */     } 
/* 443 */     String returnValue = this.value.substring(splitPosition);
/* 444 */     this.value = trim(this.value.substring(0, splitPosition));
/* 445 */     PdfChunk pc = new PdfChunk(returnValue, this);
/* 446 */     return pc;
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
/*     */   PdfChunk truncate(float width) {
/* 458 */     if (this.image != null) {
/* 459 */       if (this.image.getScaledWidth() > width) {
/*     */         
/* 461 */         if (this.image.isScaleToFitLineWhenOverflow()) {
/*     */ 
/*     */           
/* 464 */           setImageScalePercentage(width / this.image.getWidth());
/* 465 */           return null;
/*     */         } 
/* 467 */         PdfChunk pdfChunk = new PdfChunk("", this);
/* 468 */         this.value = "";
/* 469 */         this.attributes.remove("IMAGE");
/* 470 */         this.image = null;
/* 471 */         this.font = PdfFont.getDefaultFont();
/* 472 */         return pdfChunk;
/*     */       } 
/*     */       
/* 475 */       return null;
/*     */     } 
/*     */     
/* 478 */     int currentPosition = 0;
/* 479 */     float currentWidth = 0.0F;
/*     */ 
/*     */     
/* 482 */     if (width < this.font.width()) {
/* 483 */       String str = this.value.substring(1);
/* 484 */       this.value = this.value.substring(0, 1);
/* 485 */       PdfChunk pdfChunk = new PdfChunk(str, this);
/* 486 */       return pdfChunk;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 491 */     int length = this.value.length();
/* 492 */     boolean surrogate = false;
/* 493 */     while (currentPosition < length) {
/*     */       
/* 495 */       surrogate = Utilities.isSurrogatePair(this.value, currentPosition);
/* 496 */       if (surrogate) {
/* 497 */         currentWidth += getCharWidth(Utilities.convertToUtf32(this.value, currentPosition));
/*     */       } else {
/* 499 */         currentWidth += getCharWidth(this.value.charAt(currentPosition));
/* 500 */       }  if (currentWidth > width)
/*     */         break; 
/* 502 */       if (surrogate)
/* 503 */         currentPosition++; 
/* 504 */       currentPosition++;
/*     */     } 
/*     */ 
/*     */     
/* 508 */     if (currentPosition == length) {
/* 509 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 515 */     if (currentPosition == 0) {
/* 516 */       currentPosition = 1;
/* 517 */       if (surrogate)
/* 518 */         currentPosition++; 
/*     */     } 
/* 520 */     String returnValue = this.value.substring(currentPosition);
/* 521 */     this.value = this.value.substring(0, currentPosition);
/* 522 */     PdfChunk pc = new PdfChunk(returnValue, this);
/* 523 */     return pc;
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
/*     */   PdfFont font() {
/* 535 */     return this.font;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BaseColor color() {
/* 545 */     return (BaseColor)this.noStroke.get("COLOR");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float width() {
/* 555 */     return width(this.value);
/*     */   }
/*     */   
/*     */   float width(String str) {
/* 559 */     if (isAttribute("SEPARATOR")) {
/* 560 */       return 0.0F;
/*     */     }
/* 562 */     if (isImage()) {
/* 563 */       return getImageWidth();
/*     */     }
/*     */     
/* 566 */     float width = this.font.width(str);
/*     */     
/* 568 */     if (isAttribute("CHAR_SPACING")) {
/* 569 */       Float cs = (Float)getAttribute("CHAR_SPACING");
/* 570 */       width += str.length() * cs.floatValue();
/*     */     } 
/* 572 */     if (isAttribute("WORD_SPACING")) {
/* 573 */       int numberOfSpaces = 0;
/* 574 */       int idx = -1;
/* 575 */       while ((idx = str.indexOf(' ', idx + 1)) >= 0)
/* 576 */         numberOfSpaces++; 
/* 577 */       Float ws = (Float)getAttribute("WORD_SPACING");
/* 578 */       width += numberOfSpaces * ws.floatValue();
/*     */     } 
/* 580 */     return width;
/*     */   }
/*     */   
/*     */   float height() {
/* 584 */     if (isImage()) {
/* 585 */       return getImageHeight();
/*     */     }
/* 587 */     return this.font.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNewlineSplit() {
/* 598 */     return this.newlineSplit;
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
/*     */   public float getWidthCorrected(float charSpacing, float wordSpacing) {
/* 611 */     if (this.image != null) {
/* 612 */       return this.image.getScaledWidth() + charSpacing;
/*     */     }
/* 614 */     int numberOfSpaces = 0;
/* 615 */     int idx = -1;
/* 616 */     while ((idx = this.value.indexOf(' ', idx + 1)) >= 0)
/* 617 */       numberOfSpaces++; 
/* 618 */     return this.font.width(this.value) + this.value.length() * charSpacing + numberOfSpaces * wordSpacing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getTextRise() {
/* 626 */     Float f = (Float)getAttribute("SUBSUPSCRIPT");
/* 627 */     if (f != null) {
/* 628 */       return f.floatValue();
/*     */     }
/* 630 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float trimLastSpace() {
/* 640 */     BaseFont ft = this.font.getFont();
/* 641 */     if (ft.getFontType() == 2 && ft.getUnicodeEquivalent(32) != 32) {
/* 642 */       if (this.value.length() > 1 && this.value.endsWith("\001")) {
/* 643 */         this.value = this.value.substring(0, this.value.length() - 1);
/* 644 */         return this.font.width(1);
/*     */       }
/*     */     
/*     */     }
/* 648 */     else if (this.value.length() > 1 && this.value.endsWith(" ")) {
/* 649 */       this.value = this.value.substring(0, this.value.length() - 1);
/* 650 */       return this.font.width(32);
/*     */     } 
/*     */     
/* 653 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public float trimFirstSpace() {
/* 657 */     BaseFont ft = this.font.getFont();
/* 658 */     if (ft.getFontType() == 2 && ft.getUnicodeEquivalent(32) != 32) {
/* 659 */       if (this.value.length() > 1 && this.value.startsWith("\001")) {
/* 660 */         this.value = this.value.substring(1);
/* 661 */         return this.font.width(1);
/*     */       }
/*     */     
/*     */     }
/* 665 */     else if (this.value.length() > 1 && this.value.startsWith(" ")) {
/* 666 */       this.value = this.value.substring(1);
/* 667 */       return this.font.width(32);
/*     */     } 
/*     */     
/* 670 */     return 0.0F;
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
/*     */   Object getAttribute(String name) {
/* 682 */     if (this.attributes.containsKey(name))
/* 683 */       return this.attributes.get(name); 
/* 684 */     return this.noStroke.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isAttribute(String name) {
/* 695 */     if (this.attributes.containsKey(name))
/* 696 */       return true; 
/* 697 */     return this.noStroke.containsKey(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isStroked() {
/* 707 */     return !this.attributes.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSeparator() {
/* 716 */     return isAttribute("SEPARATOR");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isHorizontalSeparator() {
/* 725 */     if (isAttribute("SEPARATOR")) {
/* 726 */       Object[] o = (Object[])getAttribute("SEPARATOR");
/* 727 */       return !((Boolean)o[1]).booleanValue();
/*     */     } 
/* 729 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isTab() {
/* 738 */     return isAttribute("TAB");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   void adjustLeft(float newValue) {
/* 748 */     Object[] o = (Object[])this.attributes.get("TAB");
/* 749 */     if (o != null) {
/* 750 */       this.attributes.put("TAB", new Object[] { o[0], o[1], o[2], new Float(newValue) });
/*     */     }
/*     */   }
/*     */   
/*     */   static TabStop getTabStop(PdfChunk tab, float tabPosition) {
/* 755 */     TabStop tabStop = null;
/* 756 */     Object[] o = (Object[])tab.attributes.get("TAB");
/* 757 */     if (o != null) {
/* 758 */       Float tabInterval = (Float)o[0];
/* 759 */       if (Float.isNaN(tabInterval.floatValue())) {
/* 760 */         tabStop = TabSettings.getTabStopNewInstance(tabPosition, (TabSettings)tab.attributes.get("TABSETTINGS"));
/*     */       } else {
/* 762 */         tabStop = TabStop.newInstance(tabPosition, tabInterval.floatValue());
/*     */       } 
/*     */     } 
/* 765 */     return tabStop;
/*     */   }
/*     */   
/*     */   TabStop getTabStop() {
/* 769 */     return (TabStop)this.attributes.get("TABSTOP");
/*     */   }
/*     */   
/*     */   void setTabStop(TabStop tabStop) {
/* 773 */     this.attributes.put("TABSTOP", tabStop);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isImage() {
/* 783 */     return (this.image != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Image getImage() {
/* 793 */     return this.image;
/*     */   }
/*     */   
/*     */   float getImageHeight() {
/* 797 */     return this.image.getScaledHeight() * this.imageScalePercentage;
/*     */   }
/*     */   
/*     */   float getImageWidth() {
/* 801 */     return this.image.getScaledWidth() * this.imageScalePercentage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getImageScalePercentage() {
/* 809 */     return this.imageScalePercentage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImageScalePercentage(float imageScalePercentage) {
/* 817 */     this.imageScalePercentage = imageScalePercentage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setImageOffsetX(float offsetX) {
/* 827 */     this.offsetX = offsetX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getImageOffsetX() {
/* 837 */     return this.offsetX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setImageOffsetY(float offsetY) {
/* 847 */     this.offsetY = offsetY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getImageOffsetY() {
/* 857 */     return this.offsetY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setValue(String value) {
/* 867 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 875 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSpecialEncoding() {
/* 884 */     return (this.encoding.equals("UnicodeBigUnmarked") || this.encoding.equals("Identity-H"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getEncoding() {
/* 894 */     return this.encoding;
/*     */   }
/*     */   
/*     */   int length() {
/* 898 */     return this.value.length();
/*     */   }
/*     */   
/*     */   int lengthUtf32() {
/* 902 */     if (!"Identity-H".equals(this.encoding))
/* 903 */       return this.value.length(); 
/* 904 */     int total = 0;
/* 905 */     int len = this.value.length();
/* 906 */     for (int k = 0; k < len; k++) {
/* 907 */       if (Utilities.isSurrogateHigh(this.value.charAt(k)))
/* 908 */         k++; 
/* 909 */       total++;
/*     */     } 
/* 911 */     return total;
/*     */   }
/*     */   
/*     */   boolean isExtSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
/* 915 */     return this.splitCharacter.isSplitCharacter(start, current, end, cc, ck);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String trim(String string) {
/* 925 */     BaseFont ft = this.font.getFont();
/* 926 */     if (ft.getFontType() == 2 && ft.getUnicodeEquivalent(32) != 32) {
/* 927 */       while (string.endsWith("\001")) {
/* 928 */         string = string.substring(0, string.length() - 1);
/*     */       }
/*     */     } else {
/*     */       
/* 932 */       while (string.endsWith(" ") || string.endsWith("\t")) {
/* 933 */         string = string.substring(0, string.length() - 1);
/*     */       }
/*     */     } 
/* 936 */     return string;
/*     */   }
/*     */   
/*     */   public boolean changeLeading() {
/* 940 */     return this.changeLeading;
/*     */   }
/*     */   
/*     */   public float getLeading() {
/* 944 */     return this.leading;
/*     */   }
/*     */   
/*     */   float getCharWidth(int c) {
/* 948 */     if (noPrint(c))
/* 949 */       return 0.0F; 
/* 950 */     if (isAttribute("CHAR_SPACING")) {
/* 951 */       Float cs = (Float)getAttribute("CHAR_SPACING");
/* 952 */       return this.font.width(c) + cs.floatValue() * this.font.getHorizontalScaling();
/*     */     } 
/* 954 */     if (isImage()) {
/* 955 */       return getImageWidth();
/*     */     }
/* 957 */     return this.font.width(c);
/*     */   }
/*     */   
/*     */   public static boolean noPrint(int c) {
/* 961 */     return ((c >= 8203 && c <= 8207) || (c >= 8234 && c <= 8238) || c == 173);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfChunk.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */