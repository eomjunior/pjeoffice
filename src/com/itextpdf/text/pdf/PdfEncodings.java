/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfEncodings
/*     */ {
/*  60 */   static final char[] winansiByteToChar = new char[] { Character.MIN_VALUE, '\001', '\002', '\003', '\004', '\005', '\006', '\007', '\b', '\t', '\n', '\013', '\f', '\r', '\016', '\017', '\020', '\021', '\022', '\023', '\024', '\025', '\026', '\027', '\030', '\031', '\032', '\033', '\034', '\035', '\036', '\037', ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', '', '€', '�', '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', '�', 'Ž', '�', '�', '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', '�', 'ž', 'Ÿ', ' ', '¡', '¢', '£', '¤', '¥', '¦', '§', '¨', '©', 'ª', '«', '¬', '­', '®', '¯', '°', '±', '²', '³', '´', 'µ', '¶', '·', '¸', '¹', 'º', '»', '¼', '½', '¾', '¿', 'À', 'Á', 'Â', 'Ã', 'Ä', 'Å', 'Æ', 'Ç', 'È', 'É', 'Ê', 'Ë', 'Ì', 'Í', 'Î', 'Ï', 'Ð', 'Ñ', 'Ò', 'Ó', 'Ô', 'Õ', 'Ö', '×', 'Ø', 'Ù', 'Ú', 'Û', 'Ü', 'Ý', 'Þ', 'ß', 'à', 'á', 'â', 'ã', 'ä', 'å', 'æ', 'ç', 'è', 'é', 'ê', 'ë', 'ì', 'í', 'î', 'ï', 'ð', 'ñ', 'ò', 'ó', 'ô', 'õ', 'ö', '÷', 'ø', 'ù', 'ú', 'û', 'ü', 'ý', 'þ', 'ÿ' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   static final char[] pdfEncodingByteToChar = new char[] { Character.MIN_VALUE, '\001', '\002', '\003', '\004', '\005', '\006', '\007', '\b', '\t', '\n', '\013', '\f', '\r', '\016', '\017', '\020', '\021', '\022', '\023', '\024', '\025', '\026', '\027', '\030', '\031', '\032', '\033', '\034', '\035', '\036', '\037', ' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~', '', '•', '†', '‡', '…', '—', '–', 'ƒ', '⁄', '‹', '›', '−', '‰', '„', '“', '”', '‘', '’', '‚', '™', 'ﬁ', 'ﬂ', 'Ł', 'Œ', 'Š', 'Ÿ', 'Ž', 'ı', 'ł', 'œ', 'š', 'ž', '�', '€', '¡', '¢', '£', '¤', '¥', '¦', '§', '¨', '©', 'ª', '«', '¬', '­', '®', '¯', '°', '±', '²', '³', '´', 'µ', '¶', '·', '¸', '¹', 'º', '»', '¼', '½', '¾', '¿', 'À', 'Á', 'Â', 'Ã', 'Ä', 'Å', 'Æ', 'Ç', 'È', 'É', 'Ê', 'Ë', 'Ì', 'Í', 'Î', 'Ï', 'Ð', 'Ñ', 'Ò', 'Ó', 'Ô', 'Õ', 'Ö', '×', 'Ø', 'Ù', 'Ú', 'Û', 'Ü', 'Ý', 'Þ', 'ß', 'à', 'á', 'â', 'ã', 'ä', 'å', 'æ', 'ç', 'è', 'é', 'ê', 'ë', 'ì', 'í', 'î', 'ï', 'ð', 'ñ', 'ò', 'ó', 'ô', 'õ', 'ö', '÷', 'ø', 'ù', 'ú', 'û', 'ü', 'ý', 'þ', 'ÿ' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   static final IntHashtable winansi = new IntHashtable();
/*     */   
/*  98 */   static final IntHashtable pdfEncoding = new IntHashtable();
/*     */   
/* 100 */   static HashMap<String, ExtraEncoding> extraEncodings = new HashMap<String, ExtraEncoding>();
/*     */   static {
/*     */     int k;
/* 103 */     for (k = 128; k < 161; k++) {
/* 104 */       char c = winansiByteToChar[k];
/* 105 */       if (c != '�') {
/* 106 */         winansi.put(c, k);
/*     */       }
/*     */     } 
/* 109 */     for (k = 128; k < 161; k++) {
/* 110 */       char c = pdfEncodingByteToChar[k];
/* 111 */       if (c != '�') {
/* 112 */         pdfEncoding.put(c, k);
/*     */       }
/*     */     } 
/* 115 */     addExtraEncoding("Wingdings", new WingdingsConversion());
/* 116 */     addExtraEncoding("Symbol", new SymbolConversion(true));
/* 117 */     addExtraEncoding("ZapfDingbats", new SymbolConversion(false));
/* 118 */     addExtraEncoding("SymbolTT", new SymbolTTConversion());
/* 119 */     addExtraEncoding("Cp437", new Cp437Conversion());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte[] convertToBytes(String text, String encoding) {
/* 129 */     if (text == null)
/* 130 */       return new byte[0]; 
/* 131 */     if (encoding == null || encoding.length() == 0) {
/* 132 */       int len = text.length();
/* 133 */       byte[] b = new byte[len];
/* 134 */       for (int k = 0; k < len; k++)
/* 135 */         b[k] = (byte)text.charAt(k); 
/* 136 */       return b;
/*     */     } 
/* 138 */     ExtraEncoding extra = extraEncodings.get(encoding.toLowerCase());
/* 139 */     if (extra != null) {
/* 140 */       byte[] b = extra.charToByte(text, encoding);
/* 141 */       if (b != null)
/* 142 */         return b; 
/*     */     } 
/* 144 */     IntHashtable hash = null;
/* 145 */     if (encoding.equals("Cp1252")) {
/* 146 */       hash = winansi;
/* 147 */     } else if (encoding.equals("PDF")) {
/* 148 */       hash = pdfEncoding;
/* 149 */     }  if (hash != null) {
/* 150 */       char[] cc = text.toCharArray();
/* 151 */       int len = cc.length;
/* 152 */       int ptr = 0;
/* 153 */       byte[] b = new byte[len];
/* 154 */       int c = 0;
/* 155 */       for (int k = 0; k < len; k++) {
/* 156 */         char char1 = cc[k];
/* 157 */         if (char1 < '' || (char1 > ' ' && char1 <= 'ÿ')) {
/* 158 */           c = char1;
/*     */         } else {
/* 160 */           c = hash.get(char1);
/* 161 */         }  if (c != 0)
/* 162 */           b[ptr++] = (byte)c; 
/*     */       } 
/* 164 */       if (ptr == len)
/* 165 */         return b; 
/* 166 */       byte[] b2 = new byte[ptr];
/* 167 */       System.arraycopy(b, 0, b2, 0, ptr);
/* 168 */       return b2;
/*     */     } 
/* 170 */     if (encoding.equals("UnicodeBig")) {
/*     */       
/* 172 */       char[] cc = text.toCharArray();
/* 173 */       int len = cc.length;
/* 174 */       byte[] b = new byte[cc.length * 2 + 2];
/* 175 */       b[0] = -2;
/* 176 */       b[1] = -1;
/* 177 */       int bptr = 2;
/* 178 */       for (int k = 0; k < len; k++) {
/* 179 */         char c = cc[k];
/* 180 */         b[bptr++] = (byte)(c >> 8);
/* 181 */         b[bptr++] = (byte)(c & 0xFF);
/*     */       } 
/* 183 */       return b;
/*     */     } 
/*     */     try {
/* 186 */       Charset cc = Charset.forName(encoding);
/* 187 */       CharsetEncoder ce = cc.newEncoder();
/* 188 */       ce.onUnmappableCharacter(CodingErrorAction.IGNORE);
/* 189 */       CharBuffer cb = CharBuffer.wrap(text.toCharArray());
/* 190 */       ByteBuffer bb = ce.encode(cb);
/* 191 */       bb.rewind();
/* 192 */       int lim = bb.limit();
/* 193 */       byte[] br = new byte[lim];
/* 194 */       bb.get(br);
/* 195 */       return br;
/*     */     }
/* 197 */     catch (IOException e) {
/* 198 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte[] convertToBytes(char char1, String encoding) {
/* 209 */     if (encoding == null || encoding.length() == 0)
/* 210 */       return new byte[] { (byte)char1 }; 
/* 211 */     ExtraEncoding extra = extraEncodings.get(encoding.toLowerCase());
/* 212 */     if (extra != null) {
/* 213 */       byte[] b = extra.charToByte(char1, encoding);
/* 214 */       if (b != null)
/* 215 */         return b; 
/*     */     } 
/* 217 */     IntHashtable hash = null;
/* 218 */     if (encoding.equals("Cp1252")) {
/* 219 */       hash = winansi;
/* 220 */     } else if (encoding.equals("PDF")) {
/* 221 */       hash = pdfEncoding;
/* 222 */     }  if (hash != null) {
/* 223 */       int c = 0;
/* 224 */       if (char1 < '' || (char1 > ' ' && char1 <= 'ÿ')) {
/* 225 */         c = char1;
/*     */       } else {
/* 227 */         c = hash.get(char1);
/* 228 */       }  if (c != 0) {
/* 229 */         return new byte[] { (byte)c };
/*     */       }
/* 231 */       return new byte[0];
/*     */     } 
/* 233 */     if (encoding.equals("UnicodeBig")) {
/*     */       
/* 235 */       byte[] b = new byte[4];
/* 236 */       b[0] = -2;
/* 237 */       b[1] = -1;
/* 238 */       b[2] = (byte)(char1 >> 8);
/* 239 */       b[3] = (byte)(char1 & 0xFF);
/* 240 */       return b;
/*     */     } 
/*     */     try {
/* 243 */       Charset cc = Charset.forName(encoding);
/* 244 */       CharsetEncoder ce = cc.newEncoder();
/* 245 */       ce.onUnmappableCharacter(CodingErrorAction.IGNORE);
/* 246 */       CharBuffer cb = CharBuffer.wrap(new char[] { char1 });
/* 247 */       ByteBuffer bb = ce.encode(cb);
/* 248 */       bb.rewind();
/* 249 */       int lim = bb.limit();
/* 250 */       byte[] br = new byte[lim];
/* 251 */       bb.get(br);
/* 252 */       return br;
/*     */     }
/* 254 */     catch (IOException e) {
/* 255 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String convertToString(byte[] bytes, String encoding) {
/* 266 */     if (bytes == null)
/* 267 */       return ""; 
/* 268 */     if (encoding == null || encoding.length() == 0) {
/* 269 */       char[] c = new char[bytes.length];
/* 270 */       for (int k = 0; k < bytes.length; k++)
/* 271 */         c[k] = (char)(bytes[k] & 0xFF); 
/* 272 */       return new String(c);
/*     */     } 
/* 274 */     ExtraEncoding extra = extraEncodings.get(encoding.toLowerCase());
/* 275 */     if (extra != null) {
/* 276 */       String text = extra.byteToChar(bytes, encoding);
/* 277 */       if (text != null)
/* 278 */         return text; 
/*     */     } 
/* 280 */     char[] ch = null;
/* 281 */     if (encoding.equals("Cp1252")) {
/* 282 */       ch = winansiByteToChar;
/* 283 */     } else if (encoding.equals("PDF")) {
/* 284 */       ch = pdfEncodingByteToChar;
/* 285 */     }  if (ch != null) {
/* 286 */       int len = bytes.length;
/* 287 */       char[] c = new char[len];
/* 288 */       for (int k = 0; k < len; k++) {
/* 289 */         c[k] = ch[bytes[k] & 0xFF];
/*     */       }
/* 291 */       return new String(c);
/*     */     } 
/*     */     try {
/* 294 */       return new String(bytes, encoding);
/*     */     }
/* 296 */     catch (UnsupportedEncodingException e) {
/* 297 */       throw new ExceptionConverter(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isPdfDocEncoding(String text) {
/* 306 */     if (text == null)
/* 307 */       return true; 
/* 308 */     int len = text.length();
/* 309 */     for (int k = 0; k < len; k++) {
/* 310 */       char char1 = text.charAt(k);
/* 311 */       if (char1 >= '' && (char1 <= ' ' || char1 > 'ÿ'))
/*     */       {
/* 313 */         if (!pdfEncoding.containsKey(char1))
/* 314 */           return false;  } 
/*     */     } 
/* 316 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addExtraEncoding(String name, ExtraEncoding enc) {
/* 325 */     synchronized (extraEncodings) {
/* 326 */       HashMap<String, ExtraEncoding> newEncodings = (HashMap<String, ExtraEncoding>)extraEncodings.clone();
/* 327 */       newEncodings.put(name.toLowerCase(), enc);
/* 328 */       extraEncodings = newEncodings;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class WingdingsConversion implements ExtraEncoding { private WingdingsConversion() {}
/*     */     
/*     */     public byte[] charToByte(char char1, String encoding) {
/* 335 */       if (char1 == ' ')
/* 336 */         return new byte[] { (byte)char1 }; 
/* 337 */       if (char1 >= '✁' && char1 <= '➾') {
/* 338 */         byte v = table[char1 - 9984];
/* 339 */         if (v != 0)
/* 340 */           return new byte[] { v }; 
/*     */       } 
/* 342 */       return new byte[0];
/*     */     }
/*     */     
/*     */     public byte[] charToByte(String text, String encoding) {
/* 346 */       char[] cc = text.toCharArray();
/* 347 */       byte[] b = new byte[cc.length];
/* 348 */       int ptr = 0;
/* 349 */       int len = cc.length;
/* 350 */       for (int k = 0; k < len; k++) {
/* 351 */         char c = cc[k];
/* 352 */         if (c == ' ') {
/* 353 */           b[ptr++] = (byte)c;
/* 354 */         } else if (c >= '✁' && c <= '➾') {
/* 355 */           byte v = table[c - 9984];
/* 356 */           if (v != 0)
/* 357 */             b[ptr++] = v; 
/*     */         } 
/*     */       } 
/* 360 */       if (ptr == len)
/* 361 */         return b; 
/* 362 */       byte[] b2 = new byte[ptr];
/* 363 */       System.arraycopy(b, 0, b2, 0, ptr);
/* 364 */       return b2;
/*     */     }
/*     */     
/*     */     public String byteToChar(byte[] b, String encoding) {
/* 368 */       return null;
/*     */     }
/*     */     
/* 371 */     private static final byte[] table = new byte[] { 0, 35, 34, 0, 0, 0, 41, 62, 81, 42, 0, 0, 65, 63, 0, 0, 0, 0, 0, -4, 0, 0, 0, -5, 0, 0, 0, 0, 0, 0, 86, 0, 88, 89, 0, 0, 0, 0, 0, 0, 0, 0, -75, 0, 0, 0, 0, 0, -74, 0, 0, 0, -83, -81, -84, 0, 0, 0, 0, 0, 0, 0, 0, 124, 123, 0, 0, 0, 84, 0, 0, 0, 0, 0, 0, 0, 0, -90, 0, 0, 0, 113, 114, 0, 0, 0, 117, 0, 0, 0, 0, 0, 0, 125, 126, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -116, -115, -114, -113, -112, -111, -110, -109, -108, -107, -127, -126, -125, -124, -123, -122, -121, -120, -119, -118, -116, -115, -114, -113, -112, -111, -110, -109, -108, -107, -24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -24, -40, 0, 0, -60, -58, 0, 0, -16, 0, 0, 0, 0, 0, 0, 0, 0, 0, -36, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Cp437Conversion
/*     */     implements ExtraEncoding
/*     */   {
/*     */     private Cp437Conversion() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 396 */     private static IntHashtable c2b = new IntHashtable();
/*     */     
/*     */     public byte[] charToByte(String text, String encoding) {
/* 399 */       char[] cc = text.toCharArray();
/* 400 */       byte[] b = new byte[cc.length];
/* 401 */       int ptr = 0;
/* 402 */       int len = cc.length;
/* 403 */       for (int k = 0; k < len; k++) {
/* 404 */         char c = cc[k];
/* 405 */         if (c < '') {
/* 406 */           b[ptr++] = (byte)c;
/*     */         } else {
/* 408 */           byte v = (byte)c2b.get(c);
/* 409 */           if (v != 0)
/* 410 */             b[ptr++] = v; 
/*     */         } 
/*     */       } 
/* 413 */       if (ptr == len)
/* 414 */         return b; 
/* 415 */       byte[] b2 = new byte[ptr];
/* 416 */       System.arraycopy(b, 0, b2, 0, ptr);
/* 417 */       return b2;
/*     */     }
/*     */     
/*     */     public byte[] charToByte(char char1, String encoding) {
/* 421 */       if (char1 < '') {
/* 422 */         return new byte[] { (byte)char1 };
/*     */       }
/* 424 */       byte v = (byte)c2b.get(char1);
/* 425 */       if (v != 0) {
/* 426 */         return new byte[] { v };
/*     */       }
/* 428 */       return new byte[0];
/*     */     }
/*     */ 
/*     */     
/*     */     public String byteToChar(byte[] b, String encoding) {
/* 433 */       int len = b.length;
/* 434 */       char[] cc = new char[len];
/* 435 */       int ptr = 0;
/* 436 */       for (int k = 0; k < len; k++) {
/* 437 */         int c = b[k] & 0xFF;
/* 438 */         if (c >= 32)
/*     */         {
/* 440 */           if (c < 128) {
/* 441 */             cc[ptr++] = (char)c;
/*     */           } else {
/* 443 */             char v = table[c - 128];
/* 444 */             cc[ptr++] = v;
/*     */           }  } 
/*     */       } 
/* 447 */       return new String(cc, 0, ptr);
/*     */     }
/*     */     
/* 450 */     private static final char[] table = new char[] { 'Ç', 'ü', 'é', 'â', 'ä', 'à', 'å', 'ç', 'ê', 'ë', 'è', 'ï', 'î', 'ì', 'Ä', 'Å', 'É', 'æ', 'Æ', 'ô', 'ö', 'ò', 'û', 'ù', 'ÿ', 'Ö', 'Ü', '¢', '£', '¥', '₧', 'ƒ', 'á', 'í', 'ó', 'ú', 'ñ', 'Ñ', 'ª', 'º', '¿', '⌐', '¬', '½', '¼', '¡', '«', '»', '░', '▒', '▓', '│', '┤', '╡', '╢', '╖', '╕', '╣', '║', '╗', '╝', '╜', '╛', '┐', '└', '┴', '┬', '├', '─', '┼', '╞', '╟', '╚', '╔', '╩', '╦', '╠', '═', '╬', '╧', '╨', '╤', '╥', '╙', '╘', '╒', '╓', '╫', '╪', '┘', '┌', '█', '▄', '▌', '▐', '▀', 'α', 'ß', 'Γ', 'π', 'Σ', 'σ', 'µ', 'τ', 'Φ', 'Θ', 'Ω', 'δ', '∞', 'φ', 'ε', '∩', '≡', '±', '≥', '≤', '⌠', '⌡', '÷', '≈', '°', '∙', '·', '√', 'ⁿ', '²', '■', ' ' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 462 */       for (int k = 0; k < table.length; k++)
/* 463 */         c2b.put(table[k], k + 128); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SymbolConversion
/*     */     implements ExtraEncoding {
/* 469 */     private static final IntHashtable t1 = new IntHashtable();
/* 470 */     private static final IntHashtable t2 = new IntHashtable();
/*     */     private IntHashtable translation;
/*     */     private final char[] byteToChar;
/*     */     
/*     */     SymbolConversion(boolean symbol) {
/* 475 */       if (symbol) {
/* 476 */         this.translation = t1;
/* 477 */         this.byteToChar = table1;
/*     */       } else {
/* 479 */         this.translation = t2;
/* 480 */         this.byteToChar = table2;
/*     */       } 
/*     */     }
/*     */     
/*     */     public byte[] charToByte(String text, String encoding) {
/* 485 */       char[] cc = text.toCharArray();
/* 486 */       byte[] b = new byte[cc.length];
/* 487 */       int ptr = 0;
/* 488 */       int len = cc.length;
/* 489 */       for (int k = 0; k < len; k++) {
/* 490 */         char c = cc[k];
/* 491 */         byte v = (byte)this.translation.get(c);
/* 492 */         if (v != 0)
/* 493 */           b[ptr++] = v; 
/*     */       } 
/* 495 */       if (ptr == len)
/* 496 */         return b; 
/* 497 */       byte[] b2 = new byte[ptr];
/* 498 */       System.arraycopy(b, 0, b2, 0, ptr);
/* 499 */       return b2;
/*     */     }
/*     */     
/*     */     public byte[] charToByte(char char1, String encoding) {
/* 503 */       byte v = (byte)this.translation.get(char1);
/* 504 */       if (v != 0) {
/* 505 */         return new byte[] { v };
/*     */       }
/* 507 */       return new byte[0];
/*     */     }
/*     */     
/*     */     public String byteToChar(byte[] b, String encoding) {
/* 511 */       int len = b.length;
/* 512 */       char[] cc = new char[len];
/* 513 */       int ptr = 0;
/* 514 */       for (int k = 0; k < len; k++) {
/* 515 */         int c = b[k] & 0xFF;
/* 516 */         char v = this.byteToChar[c];
/* 517 */         cc[ptr++] = v;
/*     */       } 
/* 519 */       return new String(cc, 0, ptr);
/*     */     }
/*     */     
/* 522 */     private static final char[] table1 = new char[] { Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, ' ', '!', '∀', '#', '∃', '%', '&', '∋', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '≅', 'Α', 'Β', 'Χ', 'Δ', 'Ε', 'Φ', 'Γ', 'Η', 'Ι', 'ϑ', 'Κ', 'Λ', 'Μ', 'Ν', 'Ο', 'Π', 'Θ', 'Ρ', 'Σ', 'Τ', 'Υ', 'ς', 'Ω', 'Ξ', 'Ψ', 'Ζ', '[', '∴', ']', '⊥', '_', '̅', 'α', 'β', 'χ', 'δ', 'ε', 'ϕ', 'γ', 'η', 'ι', 'φ', 'κ', 'λ', 'μ', 'ν', 'ο', 'π', 'θ', 'ρ', 'σ', 'τ', 'υ', 'ϖ', 'ω', 'ξ', 'ψ', 'ζ', '{', '|', '}', '~', Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, '€', 'ϒ', '′', '≤', '⁄', '∞', 'ƒ', '♣', '♦', '♥', '♠', '↔', '←', '↑', '→', '↓', '°', '±', '″', '≥', '×', '∝', '∂', '•', '÷', '≠', '≡', '≈', '…', '│', '─', '↵', 'ℵ', 'ℑ', 'ℜ', '℘', '⊗', '⊕', '∅', '∩', '∪', '⊃', '⊇', '⊄', '⊂', '⊆', '∈', '∉', '∠', '∇', '®', '©', '™', '∏', '√', '⋅', '¬', '∧', '∨', '⇔', '⇐', '⇑', '⇒', '⇓', '◊', '〈', Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, '∑', '⎛', '⎜', '⎝', '⎡', '⎢', '⎣', '⎧', '⎨', '⎩', '⎪', Character.MIN_VALUE, '〉', '∫', '⌠', '⎮', '⌡', '⎞', '⎟', '⎠', '⎤', '⎥', '⎦', '⎫', '⎬', '⎭', Character.MIN_VALUE };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 541 */     private static final char[] table2 = new char[] { Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, ' ', '✁', '✂', '✃', '✄', '☎', '✆', '✇', '✈', '✉', '☛', '☞', '✌', '✍', '✎', '✏', '✐', '✑', '✒', '✓', '✔', '✕', '✖', '✗', '✘', '✙', '✚', '✛', '✜', '✝', '✞', '✟', '✠', '✡', '✢', '✣', '✤', '✥', '✦', '✧', '★', '✩', '✪', '✫', '✬', '✭', '✮', '✯', '✰', '✱', '✲', '✳', '✴', '✵', '✶', '✷', '✸', '✹', '✺', '✻', '✼', '✽', '✾', '✿', '❀', '❁', '❂', '❃', '❄', '❅', '❆', '❇', '❈', '❉', '❊', '❋', '●', '❍', '■', '❏', '❐', '❑', '❒', '▲', '▼', '◆', '❖', '◗', '❘', '❙', '❚', '❛', '❜', '❝', '❞', Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, '❡', '❢', '❣', '❤', '❥', '❦', '❧', '♣', '♦', '♥', '♠', '①', '②', '③', '④', '⑤', '⑥', '⑦', '⑧', '⑨', '⑩', '❶', '❷', '❸', '❹', '❺', '❻', '❼', '❽', '❾', '❿', '➀', '➁', '➂', '➃', '➄', '➅', '➆', '➇', '➈', '➉', '➊', '➋', '➌', '➍', '➎', '➏', '➐', '➑', '➒', '➓', '➔', '→', '↔', '↕', '➘', '➙', '➚', '➛', '➜', '➝', '➞', '➟', '➠', '➡', '➢', '➣', '➤', '➥', '➦', '➧', '➨', '➩', '➪', '➫', '➬', '➭', '➮', '➯', Character.MIN_VALUE, '➱', '➲', '➳', '➴', '➵', '➶', '➷', '➸', '➹', '➺', '➻', '➼', '➽', '➾', Character.MIN_VALUE };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*     */       int k;
/* 561 */       for (k = 0; k < 256; k++) {
/* 562 */         int v = table1[k];
/* 563 */         if (v != 0)
/* 564 */           t1.put(v, k); 
/*     */       } 
/* 566 */       for (k = 0; k < 256; k++) {
/* 567 */         int v = table2[k];
/* 568 */         if (v != 0)
/* 569 */           t2.put(v, k); 
/*     */       } 
/*     */     } }
/*     */   
/*     */   private static class SymbolTTConversion implements ExtraEncoding {
/*     */     private SymbolTTConversion() {}
/*     */     
/*     */     public byte[] charToByte(char char1, String encoding) {
/* 577 */       if ((char1 & 0xFF00) == 0 || (char1 & 0xFF00) == 61440) {
/* 578 */         return new byte[] { (byte)char1 };
/*     */       }
/* 580 */       return new byte[0];
/*     */     }
/*     */     
/*     */     public byte[] charToByte(String text, String encoding) {
/* 584 */       char[] ch = text.toCharArray();
/* 585 */       byte[] b = new byte[ch.length];
/* 586 */       int ptr = 0;
/* 587 */       int len = ch.length;
/* 588 */       for (int k = 0; k < len; k++) {
/* 589 */         char c = ch[k];
/* 590 */         if ((c & 0xFF00) == 0 || (c & 0xFF00) == 61440)
/* 591 */           b[ptr++] = (byte)c; 
/*     */       } 
/* 593 */       if (ptr == len)
/* 594 */         return b; 
/* 595 */       byte[] b2 = new byte[ptr];
/* 596 */       System.arraycopy(b, 0, b2, 0, ptr);
/* 597 */       return b2;
/*     */     }
/*     */     
/*     */     public String byteToChar(byte[] b, String encoding) {
/* 601 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfEncodings.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */