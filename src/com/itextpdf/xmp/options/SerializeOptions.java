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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SerializeOptions
/*     */   extends Options
/*     */ {
/*     */   public static final int OMIT_PACKET_WRAPPER = 16;
/*     */   public static final int READONLY_PACKET = 32;
/*     */   public static final int USE_COMPACT_FORMAT = 64;
/*     */   public static final int USE_CANONICAL_FORMAT = 128;
/*     */   public static final int INCLUDE_THUMBNAIL_PAD = 256;
/*     */   public static final int EXACT_PACKET_LENGTH = 512;
/*     */   public static final int OMIT_XMPMETA_ELEMENT = 4096;
/*     */   public static final int SORT = 8192;
/*     */   private static final int LITTLEENDIAN_BIT = 1;
/*     */   private static final int UTF16_BIT = 2;
/*     */   public static final int ENCODE_UTF8 = 0;
/*     */   public static final int ENCODE_UTF16BE = 2;
/*     */   public static final int ENCODE_UTF16LE = 3;
/*     */   private static final int ENCODING_MASK = 3;
/*  93 */   private int padding = 2048;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   private String newline = "\n";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   private String indent = "  ";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   private int baseIndent = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean omitVersionAttribute = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions(int options) throws XMPException {
/* 129 */     super(options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getOmitPacketWrapper() {
/* 138 */     return getOption(16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions setOmitPacketWrapper(boolean value) {
/* 148 */     setOption(16, value);
/* 149 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getOmitXmpMetaElement() {
/* 158 */     return getOption(4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions setOmitXmpMetaElement(boolean value) {
/* 168 */     setOption(4096, value);
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getReadOnlyPacket() {
/* 178 */     return getOption(32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions setReadOnlyPacket(boolean value) {
/* 188 */     setOption(32, value);
/* 189 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getUseCompactFormat() {
/* 198 */     return getOption(64);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions setUseCompactFormat(boolean value) {
/* 208 */     setOption(64, value);
/* 209 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getUseCanonicalFormat() {
/* 218 */     return getOption(128);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions setUseCanonicalFormat(boolean value) {
/* 228 */     setOption(128, value);
/* 229 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getIncludeThumbnailPad() {
/* 237 */     return getOption(256);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions setIncludeThumbnailPad(boolean value) {
/* 247 */     setOption(256, value);
/* 248 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getExactPacketLength() {
/* 257 */     return getOption(512);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions setExactPacketLength(boolean value) {
/* 267 */     setOption(512, value);
/* 268 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getSort() {
/* 277 */     return getOption(8192);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions setSort(boolean value) {
/* 287 */     setOption(8192, value);
/* 288 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getEncodeUTF16BE() {
/* 297 */     return ((getOptions() & 0x3) == 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions setEncodeUTF16BE(boolean value) {
/* 308 */     setOption(3, false);
/* 309 */     setOption(2, value);
/* 310 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getEncodeUTF16LE() {
/* 319 */     return ((getOptions() & 0x3) == 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions setEncodeUTF16LE(boolean value) {
/* 330 */     setOption(3, false);
/* 331 */     setOption(3, value);
/* 332 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBaseIndent() {
/* 341 */     return this.baseIndent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions setBaseIndent(int baseIndent) {
/* 352 */     this.baseIndent = baseIndent;
/* 353 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIndent() {
/* 362 */     return this.indent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions setIndent(String indent) {
/* 373 */     this.indent = indent;
/* 374 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNewline() {
/* 383 */     return this.newline;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions setNewline(String newline) {
/* 394 */     this.newline = newline;
/* 395 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPadding() {
/* 404 */     return this.padding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializeOptions setPadding(int padding) {
/* 415 */     this.padding = padding;
/* 416 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getOmitVersionAttribute() {
/* 426 */     return this.omitVersionAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 435 */     if (getEncodeUTF16BE())
/*     */     {
/* 437 */       return "UTF-16BE";
/*     */     }
/* 439 */     if (getEncodeUTF16LE())
/*     */     {
/* 441 */       return "UTF-16LE";
/*     */     }
/*     */ 
/*     */     
/* 445 */     return "UTF-8";
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
/*     */   public Object clone() throws CloneNotSupportedException {
/*     */     try {
/* 460 */       SerializeOptions clone = new SerializeOptions(getOptions());
/* 461 */       clone.setBaseIndent(this.baseIndent);
/* 462 */       clone.setIndent(this.indent);
/* 463 */       clone.setNewline(this.newline);
/* 464 */       clone.setPadding(this.padding);
/* 465 */       return clone;
/*     */     }
/* 467 */     catch (XMPException e) {
/*     */ 
/*     */       
/* 470 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String defineOptionName(int option) {
/* 480 */     switch (option) {
/*     */       case 16:
/* 482 */         return "OMIT_PACKET_WRAPPER";
/* 483 */       case 32: return "READONLY_PACKET";
/* 484 */       case 64: return "USE_COMPACT_FORMAT";
/*     */       case 256:
/* 486 */         return "INCLUDE_THUMBNAIL_PAD";
/* 487 */       case 512: return "EXACT_PACKET_LENGTH";
/* 488 */       case 4096: return "OMIT_XMPMETA_ELEMENT";
/* 489 */       case 8192: return "NORMALIZED";
/* 490 */     }  return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getValidOptions() {
/* 500 */     return 13168;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/options/SerializeOptions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */