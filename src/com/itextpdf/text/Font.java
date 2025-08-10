/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.pdf.BaseFont;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Font
/*     */   implements Comparable<Font>
/*     */ {
/*     */   public static final int NORMAL = 0;
/*     */   public static final int BOLD = 1;
/*     */   public static final int ITALIC = 2;
/*     */   public static final int UNDERLINE = 4;
/*     */   public static final int STRIKETHRU = 8;
/*     */   public static final int BOLDITALIC = 3;
/*     */   public static final int UNDEFINED = -1;
/*     */   public static final int DEFAULTSIZE = 12;
/*     */   
/*     */   public enum FontFamily
/*     */   {
/*  71 */     COURIER,
/*  72 */     HELVETICA,
/*  73 */     TIMES_ROMAN,
/*  74 */     SYMBOL,
/*  75 */     ZAPFDINGBATS,
/*  76 */     UNDEFINED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum FontStyle
/*     */   {
/*  87 */     NORMAL("normal"), BOLD("bold"), ITALIC("italic"), OBLIQUE("oblique"), UNDERLINE("underline"),
/*  88 */     LINETHROUGH("line-through");
/*     */     
/*     */     private String code;
/*     */     
/*     */     FontStyle(String code) {
/*  93 */       this.code = code;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getValue() {
/* 101 */       return this.code;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   private FontFamily family = FontFamily.UNDEFINED;
/*     */ 
/*     */   
/* 140 */   private float size = -1.0F;
/*     */ 
/*     */   
/* 143 */   private int style = -1;
/*     */ 
/*     */   
/* 146 */   private BaseColor color = null;
/*     */ 
/*     */   
/* 149 */   private BaseFont baseFont = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Font(Font other) {
/* 160 */     this.family = other.family;
/* 161 */     this.size = other.size;
/* 162 */     this.style = other.style;
/* 163 */     this.color = other.color;
/* 164 */     this.baseFont = other.baseFont;
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
/*     */   
/*     */   public Font(FontFamily family, float size, int style, BaseColor color) {
/* 182 */     this.family = family;
/* 183 */     this.size = size;
/* 184 */     this.style = style;
/* 185 */     this.color = color;
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
/*     */   public Font(BaseFont bf, float size, int style, BaseColor color) {
/* 202 */     this.baseFont = bf;
/* 203 */     this.size = size;
/* 204 */     this.style = style;
/* 205 */     this.color = color;
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
/*     */   public Font(BaseFont bf, float size, int style) {
/* 219 */     this(bf, size, style, (BaseColor)null);
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
/*     */   public Font(BaseFont bf, float size) {
/* 231 */     this(bf, size, -1, (BaseColor)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Font(BaseFont bf) {
/* 241 */     this(bf, -1.0F, -1, (BaseColor)null);
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
/*     */   public Font(FontFamily family, float size, int style) {
/* 257 */     this(family, size, style, (BaseColor)null);
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
/*     */   public Font(FontFamily family, float size) {
/* 271 */     this(family, size, -1, (BaseColor)null);
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
/*     */   public Font(FontFamily family) {
/* 283 */     this(family, -1.0F, -1, (BaseColor)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Font() {
/* 291 */     this(FontFamily.UNDEFINED, -1.0F, -1, (BaseColor)null);
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
/*     */   public int compareTo(Font font) {
/* 304 */     if (font == null) {
/* 305 */       return -1;
/*     */     }
/*     */     
/*     */     try {
/* 309 */       if (this.baseFont != null && !this.baseFont.equals(font.getBaseFont())) {
/* 310 */         return -2;
/*     */       }
/* 312 */       if (this.family != font.getFamily()) {
/* 313 */         return 1;
/*     */       }
/* 315 */       if (this.size != font.getSize()) {
/* 316 */         return 2;
/*     */       }
/* 318 */       if (this.style != font.getStyle()) {
/* 319 */         return 3;
/*     */       }
/* 321 */       if (this.color == null) {
/* 322 */         if (font.color == null) {
/* 323 */           return 0;
/*     */         }
/* 325 */         return 4;
/*     */       } 
/* 327 */       if (font.color == null) {
/* 328 */         return 4;
/*     */       }
/* 330 */       if (this.color.equals(font.getColor())) {
/* 331 */         return 0;
/*     */       }
/* 333 */       return 4;
/* 334 */     } catch (ClassCastException cce) {
/* 335 */       return -3;
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
/*     */   public FontFamily getFamily() {
/* 347 */     return this.family;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFamilyname() {
/* 356 */     String tmp = "unknown";
/* 357 */     switch (getFamily()) {
/*     */       case COURIER:
/* 359 */         return "Courier";
/*     */       case HELVETICA:
/* 361 */         return "Helvetica";
/*     */       case TIMES_ROMAN:
/* 363 */         return "Times-Roman";
/*     */       case SYMBOL:
/* 365 */         return "Symbol";
/*     */       case ZAPFDINGBATS:
/* 367 */         return "ZapfDingbats";
/*     */     } 
/* 369 */     if (this.baseFont != null) {
/* 370 */       String[][] names = this.baseFont.getFamilyFontName();
/* 371 */       for (String[] name : names) {
/* 372 */         if ("0".equals(name[2])) {
/* 373 */           return name[3];
/*     */         }
/* 375 */         if ("1033".equals(name[2])) {
/* 376 */           tmp = name[3];
/*     */         }
/* 378 */         if ("".equals(name[2])) {
/* 379 */           tmp = name[3];
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 384 */     return tmp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFamily(String family) {
/* 395 */     this.family = getFamily(family);
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
/*     */   public static FontFamily getFamily(String family) {
/* 409 */     if (family.equalsIgnoreCase("Courier")) {
/* 410 */       return FontFamily.COURIER;
/*     */     }
/* 412 */     if (family.equalsIgnoreCase("Helvetica")) {
/* 413 */       return FontFamily.HELVETICA;
/*     */     }
/* 415 */     if (family.equalsIgnoreCase("Times-Roman")) {
/* 416 */       return FontFamily.TIMES_ROMAN;
/*     */     }
/* 418 */     if (family.equalsIgnoreCase("Symbol")) {
/* 419 */       return FontFamily.SYMBOL;
/*     */     }
/* 421 */     if (family.equalsIgnoreCase("ZapfDingbats")) {
/* 422 */       return FontFamily.ZAPFDINGBATS;
/*     */     }
/* 424 */     return FontFamily.UNDEFINED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getSize() {
/* 435 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getCalculatedSize() {
/* 446 */     float s = this.size;
/* 447 */     if (s == -1.0F) {
/* 448 */       s = 12.0F;
/*     */     }
/* 450 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getCalculatedLeading(float multipliedLeading) {
/* 461 */     return multipliedLeading * getCalculatedSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(float size) {
/* 471 */     this.size = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStyle() {
/* 482 */     return this.style;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCalculatedStyle() {
/* 493 */     int style = this.style;
/* 494 */     if (style == -1) {
/* 495 */       style = 0;
/*     */     }
/* 497 */     if (this.baseFont != null)
/* 498 */       return style; 
/* 499 */     if (this.family == FontFamily.SYMBOL || this.family == FontFamily.ZAPFDINGBATS) {
/* 500 */       return style;
/*     */     }
/* 502 */     return style & 0xFFFFFFFC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBold() {
/* 511 */     if (this.style == -1) {
/* 512 */       return false;
/*     */     }
/* 514 */     return ((this.style & 0x1) == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isItalic() {
/* 523 */     if (this.style == -1) {
/* 524 */       return false;
/*     */     }
/* 526 */     return ((this.style & 0x2) == 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnderlined() {
/* 535 */     if (this.style == -1) {
/* 536 */       return false;
/*     */     }
/* 538 */     return ((this.style & 0x4) == 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStrikethru() {
/* 547 */     if (this.style == -1) {
/* 548 */       return false;
/*     */     }
/* 550 */     return ((this.style & 0x8) == 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStyle(int style) {
/* 560 */     this.style = style;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStyle(String style) {
/* 571 */     if (this.style == -1)
/* 572 */       this.style = 0; 
/* 573 */     this.style |= getStyleValue(style);
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
/*     */   public static int getStyleValue(String style) {
/* 586 */     int s = 0;
/* 587 */     if (style.indexOf(FontStyle.NORMAL.getValue()) != -1) {
/* 588 */       s |= 0x0;
/*     */     }
/* 590 */     if (style.indexOf(FontStyle.BOLD.getValue()) != -1) {
/* 591 */       s |= 0x1;
/*     */     }
/* 593 */     if (style.indexOf(FontStyle.ITALIC.getValue()) != -1) {
/* 594 */       s |= 0x2;
/*     */     }
/* 596 */     if (style.indexOf(FontStyle.OBLIQUE.getValue()) != -1) {
/* 597 */       s |= 0x2;
/*     */     }
/* 599 */     if (style.indexOf(FontStyle.UNDERLINE.getValue()) != -1) {
/* 600 */       s |= 0x4;
/*     */     }
/* 602 */     if (style.indexOf(FontStyle.LINETHROUGH.getValue()) != -1) {
/* 603 */       s |= 0x8;
/*     */     }
/* 605 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getColor() {
/* 616 */     return this.color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(BaseColor color) {
/* 627 */     this.color = color;
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
/*     */   public void setColor(int red, int green, int blue) {
/* 641 */     this.color = new BaseColor(red, green, blue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseFont getBaseFont() {
/* 652 */     return this.baseFont;
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
/*     */   public BaseFont getCalculatedBaseFont(boolean specialEncoding) {
/* 666 */     if (this.baseFont != null)
/* 667 */       return this.baseFont; 
/* 668 */     int style = this.style;
/* 669 */     if (style == -1) {
/* 670 */       style = 0;
/*     */     }
/* 672 */     String fontName = "Helvetica";
/* 673 */     String encoding = "Cp1252";
/* 674 */     BaseFont cfont = null;
/* 675 */     switch (this.family) {
/*     */       case COURIER:
/* 677 */         switch (style & 0x3) {
/*     */           case 1:
/* 679 */             fontName = "Courier-Bold";
/*     */             break;
/*     */           case 2:
/* 682 */             fontName = "Courier-Oblique";
/*     */             break;
/*     */           case 3:
/* 685 */             fontName = "Courier-BoldOblique";
/*     */             break;
/*     */         } 
/*     */         
/* 689 */         fontName = "Courier";
/*     */         break;
/*     */ 
/*     */       
/*     */       case TIMES_ROMAN:
/* 694 */         switch (style & 0x3) {
/*     */           case 1:
/* 696 */             fontName = "Times-Bold";
/*     */             break;
/*     */           case 2:
/* 699 */             fontName = "Times-Italic";
/*     */             break;
/*     */           case 3:
/* 702 */             fontName = "Times-BoldItalic";
/*     */             break;
/*     */         } 
/*     */         
/* 706 */         fontName = "Times-Roman";
/*     */         break;
/*     */ 
/*     */       
/*     */       case SYMBOL:
/* 711 */         fontName = "Symbol";
/* 712 */         if (specialEncoding)
/* 713 */           encoding = "Symbol"; 
/*     */         break;
/*     */       case ZAPFDINGBATS:
/* 716 */         fontName = "ZapfDingbats";
/* 717 */         if (specialEncoding) {
/* 718 */           encoding = "ZapfDingbats";
/*     */         }
/*     */         break;
/*     */       default:
/* 722 */         switch (style & 0x3) {
/*     */           case 1:
/* 724 */             fontName = "Helvetica-Bold";
/*     */             break;
/*     */           case 2:
/* 727 */             fontName = "Helvetica-Oblique";
/*     */             break;
/*     */           case 3:
/* 730 */             fontName = "Helvetica-BoldOblique";
/*     */             break;
/*     */         } 
/*     */         
/* 734 */         fontName = "Helvetica";
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 740 */       cfont = BaseFont.createFont(fontName, encoding, false);
/* 741 */     } catch (Exception ee) {
/* 742 */       throw new ExceptionConverter(ee);
/*     */     } 
/* 744 */     return cfont;
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
/*     */   public boolean isStandardFont() {
/* 757 */     return (this.family == FontFamily.UNDEFINED && this.size == -1.0F && this.style == -1 && this.color == null && this.baseFont == null);
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
/*     */   public Font difference(Font font) {
/* 769 */     if (font == null) {
/* 770 */       return this;
/*     */     }
/* 772 */     float dSize = font.size;
/* 773 */     if (dSize == -1.0F) {
/* 774 */       dSize = this.size;
/*     */     }
/*     */     
/* 777 */     int dStyle = -1;
/* 778 */     int style1 = this.style;
/* 779 */     int style2 = font.getStyle();
/* 780 */     if (style1 != -1 || style2 != -1) {
/* 781 */       if (style1 == -1)
/* 782 */         style1 = 0; 
/* 783 */       if (style2 == -1)
/* 784 */         style2 = 0; 
/* 785 */       dStyle = style1 | style2;
/*     */     } 
/*     */     
/* 788 */     BaseColor dColor = font.color;
/* 789 */     if (dColor == null) {
/* 790 */       dColor = this.color;
/*     */     }
/*     */     
/* 793 */     if (font.baseFont != null) {
/* 794 */       return new Font(font.baseFont, dSize, dStyle, dColor);
/*     */     }
/* 796 */     if (font.getFamily() != FontFamily.UNDEFINED) {
/* 797 */       return new Font(font.family, dSize, dStyle, dColor);
/*     */     }
/* 799 */     if (this.baseFont != null) {
/* 800 */       if (dStyle == style1) {
/* 801 */         return new Font(this.baseFont, dSize, dStyle, dColor);
/*     */       }
/* 803 */       return FontFactory.getFont(getFamilyname(), dSize, dStyle, dColor);
/*     */     } 
/*     */ 
/*     */     
/* 807 */     return new Font(this.family, dSize, dStyle, dColor);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/Font.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */