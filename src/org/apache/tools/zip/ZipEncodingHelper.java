/*     */ package org.apache.tools.zip;
/*     */ 
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
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
/*     */ public abstract class ZipEncodingHelper
/*     */ {
/*     */   private static final Map<String, SimpleEncodingHolder> simpleEncodings;
/*     */   
/*     */   private static class SimpleEncodingHolder
/*     */   {
/*     */     private final char[] highChars;
/*     */     private Simple8BitZipEncoding encoding;
/*     */     
/*     */     SimpleEncodingHolder(char[] highChars) {
/*  52 */       this.highChars = highChars;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized Simple8BitZipEncoding getEncoding() {
/*  60 */       if (this.encoding == null) {
/*  61 */         this.encoding = new Simple8BitZipEncoding(this.highChars);
/*     */       }
/*  63 */       return this.encoding;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  70 */     Map<String, SimpleEncodingHolder> se = new HashMap<>();
/*     */     
/*  72 */     char[] cp437_high_chars = { 'Ç', 'ü', 'é', 'â', 'ä', 'à', 'å', 'ç', 'ê', 'ë', 'è', 'ï', 'î', 'ì', 'Ä', 'Å', 'É', 'æ', 'Æ', 'ô', 'ö', 'ò', 'û', 'ù', 'ÿ', 'Ö', 'Ü', '¢', '£', '¥', '₧', 'ƒ', 'á', 'í', 'ó', 'ú', 'ñ', 'Ñ', 'ª', 'º', '¿', '⌐', '¬', '½', '¼', '¡', '«', '»', '░', '▒', '▓', '│', '┤', '╡', '╢', '╖', '╕', '╣', '║', '╗', '╝', '╜', '╛', '┐', '└', '┴', '┬', '├', '─', '┼', '╞', '╟', '╚', '╔', '╩', '╦', '╠', '═', '╬', '╧', '╨', '╤', '╥', '╙', '╘', '╒', '╓', '╫', '╪', '┘', '┌', '█', '▄', '▌', '▐', '▀', 'α', 'ß', 'Γ', 'π', 'Σ', 'σ', 'µ', 'τ', 'Φ', 'Θ', 'Ω', 'δ', '∞', 'φ', 'ε', '∩', '≡', '±', '≥', '≤', '⌠', '⌡', '÷', '≈', '°', '∙', '·', '√', 'ⁿ', '²', '■', ' ' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  96 */     SimpleEncodingHolder cp437 = new SimpleEncodingHolder(cp437_high_chars);
/*     */     
/*  98 */     se.put("CP437", cp437);
/*  99 */     se.put("Cp437", cp437);
/* 100 */     se.put("cp437", cp437);
/* 101 */     se.put("IBM437", cp437);
/* 102 */     se.put("ibm437", cp437);
/*     */     
/* 104 */     char[] cp850_high_chars = { 'Ç', 'ü', 'é', 'â', 'ä', 'à', 'å', 'ç', 'ê', 'ë', 'è', 'ï', 'î', 'ì', 'Ä', 'Å', 'É', 'æ', 'Æ', 'ô', 'ö', 'ò', 'û', 'ù', 'ÿ', 'Ö', 'Ü', 'ø', '£', 'Ø', '×', 'ƒ', 'á', 'í', 'ó', 'ú', 'ñ', 'Ñ', 'ª', 'º', '¿', '®', '¬', '½', '¼', '¡', '«', '»', '░', '▒', '▓', '│', '┤', 'Á', 'Â', 'À', '©', '╣', '║', '╗', '╝', '¢', '¥', '┐', '└', '┴', '┬', '├', '─', '┼', 'ã', 'Ã', '╚', '╔', '╩', '╦', '╠', '═', '╬', '¤', 'ð', 'Ð', 'Ê', 'Ë', 'È', 'ı', 'Í', 'Î', 'Ï', '┘', '┌', '█', '▄', '¦', 'Ì', '▀', 'Ó', 'ß', 'Ô', 'Ò', 'õ', 'Õ', 'µ', 'þ', 'Þ', 'Ú', 'Û', 'Ù', 'ý', 'Ý', '¯', '´', '­', '±', '‗', '¾', '¶', '§', '÷', '¸', '°', '¨', '·', '¹', '³', '²', '■', ' ' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     SimpleEncodingHolder cp850 = new SimpleEncodingHolder(cp850_high_chars);
/*     */     
/* 130 */     se.put("CP850", cp850);
/* 131 */     se.put("Cp850", cp850);
/* 132 */     se.put("cp850", cp850);
/* 133 */     se.put("IBM850", cp850);
/* 134 */     se.put("ibm850", cp850);
/* 135 */     simpleEncodings = Collections.unmodifiableMap(se);
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
/*     */   static ByteBuffer growBuffer(ByteBuffer b, int newCapacity) {
/* 151 */     prepareBufferForRead(b);
/* 152 */     int c2 = b.capacity() * 2;
/* 153 */     ByteBuffer on = ByteBuffer.allocate((c2 < newCapacity) ? newCapacity : c2);
/*     */     
/* 155 */     on.put(b);
/* 156 */     return on;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void prepareBufferForRead(Buffer b) {
/* 167 */     b.limit(b.position());
/* 168 */     b.rewind();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   private static final byte[] HEX_DIGITS = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
/*     */ 
/*     */ 
/*     */   
/*     */   static final String UTF8 = "UTF8";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String UTF_DASH_8 = "utf-8";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void appendSurrogate(ByteBuffer bb, char c) {
/* 190 */     bb.put((byte)37);
/* 191 */     bb.put((byte)85);
/*     */     
/* 193 */     bb.put(HEX_DIGITS[c >> 12 & 0xF]);
/* 194 */     bb.put(HEX_DIGITS[c >> 8 & 0xF]);
/* 195 */     bb.put(HEX_DIGITS[c >> 4 & 0xF]);
/* 196 */     bb.put(HEX_DIGITS[c & 0xF]);
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
/* 213 */   static final ZipEncoding UTF8_ZIP_ENCODING = new FallbackZipEncoding("UTF8");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ZipEncoding getZipEncoding(String name) {
/* 225 */     if (isUTF8(name)) {
/* 226 */       return UTF8_ZIP_ENCODING;
/*     */     }
/*     */     
/* 229 */     if (name == null) {
/* 230 */       return new FallbackZipEncoding();
/*     */     }
/*     */     
/* 233 */     SimpleEncodingHolder h = simpleEncodings.get(name);
/*     */     
/* 235 */     if (h != null) {
/* 236 */       return h.getEncoding();
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 241 */       Charset cs = Charset.forName(name);
/* 242 */       return new NioZipEncoding(cs);
/*     */     }
/* 244 */     catch (UnsupportedCharsetException e) {
/* 245 */       return new FallbackZipEncoding(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isUTF8(String encoding) {
/* 254 */     if (encoding == null)
/*     */     {
/* 256 */       encoding = System.getProperty("file.encoding");
/*     */     }
/* 258 */     return ("UTF8".equalsIgnoreCase(encoding) || "utf-8"
/* 259 */       .equalsIgnoreCase(encoding));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/ZipEncodingHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */