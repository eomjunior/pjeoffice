/*     */ package com.itextpdf.xmp.options;
/*     */ 
/*     */ import com.itextpdf.xmp.XMPException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AliasOptions
/*     */   extends Options
/*     */ {
/*     */   public static final int PROP_DIRECT = 0;
/*     */   public static final int PROP_ARRAY = 512;
/*     */   public static final int PROP_ARRAY_ORDERED = 1024;
/*     */   public static final int PROP_ARRAY_ALTERNATE = 2048;
/*     */   public static final int PROP_ARRAY_ALT_TEXT = 4096;
/*     */   
/*     */   public AliasOptions() {}
/*     */   
/*     */   public AliasOptions(int options) throws XMPException {
/*  72 */     super(options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSimple() {
/*  81 */     return (getOptions() == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArray() {
/*  90 */     return getOption(512);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AliasOptions setArray(boolean value) {
/* 100 */     setOption(512, value);
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArrayOrdered() {
/* 110 */     return getOption(1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AliasOptions setArrayOrdered(boolean value) {
/* 120 */     setOption(1536, value);
/* 121 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArrayAlternate() {
/* 130 */     return getOption(2048);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AliasOptions setArrayAlternate(boolean value) {
/* 140 */     setOption(3584, value);
/* 141 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArrayAltText() {
/* 150 */     return getOption(4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AliasOptions setArrayAltText(boolean value) {
/* 160 */     setOption(7680, value);
/*     */     
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyOptions toPropertyOptions() throws XMPException {
/* 172 */     return new PropertyOptions(getOptions());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String defineOptionName(int option) {
/* 181 */     switch (option) {
/*     */       case 0:
/* 183 */         return "PROP_DIRECT";
/* 184 */       case 512: return "ARRAY";
/* 185 */       case 1024: return "ARRAY_ORDERED";
/* 186 */       case 2048: return "ARRAY_ALTERNATE";
/* 187 */       case 4096: return "ARRAY_ALT_TEXT";
/* 188 */     }  return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getValidOptions() {
/* 198 */     return 7680;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/options/AliasOptions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */