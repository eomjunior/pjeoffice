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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PropertyOptions
/*     */   extends Options
/*     */ {
/*     */   public static final int NO_OPTIONS = 0;
/*     */   public static final int URI = 2;
/*     */   public static final int HAS_QUALIFIERS = 16;
/*     */   public static final int QUALIFIER = 32;
/*     */   public static final int HAS_LANGUAGE = 64;
/*     */   public static final int HAS_TYPE = 128;
/*     */   public static final int STRUCT = 256;
/*     */   public static final int ARRAY = 512;
/*     */   public static final int ARRAY_ORDERED = 1024;
/*     */   public static final int ARRAY_ALTERNATE = 2048;
/*     */   public static final int ARRAY_ALT_TEXT = 4096;
/*     */   public static final int SCHEMA_NODE = -2147483648;
/*     */   public static final int DELETE_EXISTING = 536870912;
/*     */   public static final int SEPARATE_NODE = 1073741824;
/*     */   
/*     */   public PropertyOptions() {}
/*     */   
/*     */   public PropertyOptions(int options) throws XMPException {
/*  93 */     super(options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isURI() {
/* 103 */     return getOption(2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyOptions setURI(boolean value) {
/* 113 */     setOption(2, value);
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getHasQualifiers() {
/* 125 */     return getOption(16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyOptions setHasQualifiers(boolean value) {
/* 135 */     setOption(16, value);
/* 136 */     return this;
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
/*     */   public boolean isQualifier() {
/* 148 */     return getOption(32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyOptions setQualifier(boolean value) {
/* 158 */     setOption(32, value);
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getHasLanguage() {
/* 166 */     return getOption(64);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyOptions setHasLanguage(boolean value) {
/* 176 */     setOption(64, value);
/* 177 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getHasType() {
/* 184 */     return getOption(128);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyOptions setHasType(boolean value) {
/* 194 */     setOption(128, value);
/* 195 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStruct() {
/* 202 */     return getOption(256);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyOptions setStruct(boolean value) {
/* 212 */     setOption(256, value);
/* 213 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArray() {
/* 223 */     return getOption(512);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyOptions setArray(boolean value) {
/* 233 */     setOption(512, value);
/* 234 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArrayOrdered() {
/* 244 */     return getOption(1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyOptions setArrayOrdered(boolean value) {
/* 254 */     setOption(1024, value);
/* 255 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArrayAlternate() {
/* 265 */     return getOption(2048);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyOptions setArrayAlternate(boolean value) {
/* 275 */     setOption(2048, value);
/* 276 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArrayAltText() {
/* 287 */     return getOption(4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyOptions setArrayAltText(boolean value) {
/* 297 */     setOption(4096, value);
/* 298 */     return this;
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
/*     */   public boolean isSchemaNode() {
/* 313 */     return getOption(-2147483648);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyOptions setSchemaNode(boolean value) {
/* 323 */     setOption(-2147483648, value);
/* 324 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCompositeProperty() {
/* 335 */     return ((getOptions() & 0x300) > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSimple() {
/* 344 */     return ((getOptions() & 0x300) == 0);
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
/*     */   public boolean equalArrayTypes(PropertyOptions options) {
/* 356 */     return (
/* 357 */       isArray() == options.isArray() && 
/* 358 */       isArrayOrdered() == options.isArrayOrdered() && 
/* 359 */       isArrayAlternate() == options.isArrayAlternate() && 
/* 360 */       isArrayAltText() == options.isArrayAltText());
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
/*     */   public void mergeWith(PropertyOptions options) throws XMPException {
/* 373 */     if (options != null)
/*     */     {
/* 375 */       setOptions(getOptions() | options.getOptions());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOnlyArrayOptions() {
/* 385 */     return ((getOptions() & 0xFFFFE1FF) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getValidOptions() {
/* 395 */     return -1073733646;
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
/*     */   protected String defineOptionName(int option) {
/* 416 */     switch (option) {
/*     */       case 2:
/* 418 */         return "URI";
/* 419 */       case 16: return "HAS_QUALIFIER";
/* 420 */       case 32: return "QUALIFIER";
/* 421 */       case 64: return "HAS_LANGUAGE";
/* 422 */       case 128: return "HAS_TYPE";
/* 423 */       case 256: return "STRUCT";
/* 424 */       case 512: return "ARRAY";
/* 425 */       case 1024: return "ARRAY_ORDERED";
/* 426 */       case 2048: return "ARRAY_ALTERNATE";
/* 427 */       case 4096: return "ARRAY_ALT_TEXT";
/* 428 */       case -2147483648: return "SCHEMA_NODE";
/* 429 */     }  return null;
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
/*     */   public void assertConsistency(int options) throws XMPException {
/* 443 */     if ((options & 0x100) > 0 && (options & 0x200) > 0)
/*     */     {
/* 445 */       throw new XMPException("IsStruct and IsArray options are mutually exclusive", 103);
/*     */     }
/*     */     
/* 448 */     if ((options & 0x2) > 0 && (options & 0x300) > 0)
/*     */     {
/* 450 */       throw new XMPException("Structs and arrays can't have \"value\" options", 103);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/options/PropertyOptions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */