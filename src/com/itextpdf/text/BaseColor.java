/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BaseColor
/*     */ {
/*  53 */   public static final BaseColor WHITE = new BaseColor(255, 255, 255);
/*  54 */   public static final BaseColor LIGHT_GRAY = new BaseColor(192, 192, 192);
/*  55 */   public static final BaseColor GRAY = new BaseColor(128, 128, 128);
/*  56 */   public static final BaseColor DARK_GRAY = new BaseColor(64, 64, 64);
/*  57 */   public static final BaseColor BLACK = new BaseColor(0, 0, 0);
/*  58 */   public static final BaseColor RED = new BaseColor(255, 0, 0);
/*  59 */   public static final BaseColor PINK = new BaseColor(255, 175, 175);
/*  60 */   public static final BaseColor ORANGE = new BaseColor(255, 200, 0);
/*  61 */   public static final BaseColor YELLOW = new BaseColor(255, 255, 0);
/*  62 */   public static final BaseColor GREEN = new BaseColor(0, 255, 0);
/*  63 */   public static final BaseColor MAGENTA = new BaseColor(255, 0, 255);
/*  64 */   public static final BaseColor CYAN = new BaseColor(0, 255, 255);
/*  65 */   public static final BaseColor BLUE = new BaseColor(0, 0, 255);
/*     */ 
/*     */ 
/*     */   
/*     */   private static final double FACTOR = 0.7D;
/*     */ 
/*     */   
/*     */   private int value;
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor(int red, int green, int blue, int alpha) {
/*  77 */     setValue(red, green, blue, alpha);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor(int red, int green, int blue) {
/*  86 */     this(red, green, blue, 255);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor(float red, float green, float blue, float alpha) {
/*  97 */     this((int)((red * 255.0F) + 0.5D), (int)((green * 255.0F) + 0.5D), (int)((blue * 255.0F) + 0.5D), (int)((alpha * 255.0F) + 0.5D));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor(float red, float green, float blue) {
/* 107 */     this(red, green, blue, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor(int argb) {
/* 114 */     this.value = argb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRGB() {
/* 121 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRed() {
/* 128 */     return getRGB() >> 16 & 0xFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGreen() {
/* 135 */     return getRGB() >> 8 & 0xFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBlue() {
/* 142 */     return getRGB() >> 0 & 0xFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAlpha() {
/* 149 */     return getRGB() >> 24 & 0xFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor brighter() {
/* 157 */     int r = getRed();
/* 158 */     int g = getGreen();
/* 159 */     int b = getBlue();
/*     */     
/* 161 */     int i = 3;
/* 162 */     if (r == 0 && g == 0 && b == 0) {
/* 163 */       return new BaseColor(i, i, i);
/*     */     }
/* 165 */     if (r > 0 && r < i)
/* 166 */       r = i; 
/* 167 */     if (g > 0 && g < i)
/* 168 */       g = i; 
/* 169 */     if (b > 0 && b < i) {
/* 170 */       b = i;
/*     */     }
/* 172 */     return new BaseColor(Math.min((int)(r / 0.7D), 255), 
/* 173 */         Math.min((int)(g / 0.7D), 255), 
/* 174 */         Math.min((int)(b / 0.7D), 255));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor darker() {
/* 182 */     return new BaseColor(Math.max((int)(getRed() * 0.7D), 0), 
/* 183 */         Math.max((int)(getGreen() * 0.7D), 0), 
/* 184 */         Math.max((int)(getBlue() * 0.7D), 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 189 */     return (obj instanceof BaseColor && ((BaseColor)obj).value == this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 194 */     return this.value;
/*     */   }
/*     */   
/*     */   protected void setValue(int red, int green, int blue, int alpha) {
/* 198 */     validate(red);
/* 199 */     validate(green);
/* 200 */     validate(blue);
/* 201 */     validate(alpha);
/* 202 */     this.value = (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF) << 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void validate(int value) {
/* 207 */     if (value < 0 || value > 255) {
/* 208 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("color.value.outside.range.0.255", new Object[0]));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 216 */     return "Color value[" + Integer.toString(this.value, 16) + "]";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/BaseColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */