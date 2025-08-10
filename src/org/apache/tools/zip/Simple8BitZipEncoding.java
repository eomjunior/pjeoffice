/*     */ package org.apache.tools.zip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Simple8BitZipEncoding
/*     */   implements ZipEncoding
/*     */ {
/*     */   private final char[] highChars;
/*     */   private final List<Simple8BitChar> reverseMapping;
/*     */   
/*     */   private static final class Simple8BitChar
/*     */     implements Comparable<Simple8BitChar>
/*     */   {
/*     */     public final char unicode;
/*     */     public final byte code;
/*     */     
/*     */     Simple8BitChar(byte code, char unicode) {
/*  57 */       this.code = code;
/*  58 */       this.unicode = unicode;
/*     */     }
/*     */     
/*     */     public int compareTo(Simple8BitChar a) {
/*  62 */       return this.unicode - a.unicode;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  67 */       return "0x" + Integer.toHexString(Character.MAX_VALUE & this.unicode) + "->0x" + 
/*  68 */         Integer.toHexString(0xFF & this.code);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/*  73 */       if (o instanceof Simple8BitChar) {
/*  74 */         Simple8BitChar other = (Simple8BitChar)o;
/*  75 */         return (this.unicode == other.unicode && this.code == other.code);
/*     */       } 
/*  77 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  82 */       return this.unicode;
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
/*     */   public Simple8BitZipEncoding(char[] highChars) {
/* 104 */     this.highChars = (char[])highChars.clone();
/* 105 */     List<Simple8BitChar> temp = new ArrayList<>(this.highChars.length);
/*     */ 
/*     */     
/* 108 */     byte code = Byte.MAX_VALUE;
/*     */     
/* 110 */     for (char highChar : this.highChars) {
/* 111 */       code = (byte)(code + 1); temp.add(new Simple8BitChar(code, highChar));
/*     */     } 
/*     */     
/* 114 */     Collections.sort(temp);
/* 115 */     this.reverseMapping = Collections.unmodifiableList(temp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char decodeByte(byte b) {
/* 126 */     if (b >= 0) {
/* 127 */       return (char)b;
/*     */     }
/*     */ 
/*     */     
/* 131 */     return this.highChars[128 + b];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canEncodeChar(char c) {
/* 140 */     if (c >= '\000' && c < '') {
/* 141 */       return true;
/*     */     }
/*     */     
/* 144 */     Simple8BitChar r = encodeHighChar(c);
/* 145 */     return (r != null);
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
/*     */   public boolean pushEncodedChar(ByteBuffer bb, char c) {
/* 159 */     if (c >= '\000' && c < '') {
/* 160 */       bb.put((byte)c);
/* 161 */       return true;
/*     */     } 
/*     */     
/* 164 */     Simple8BitChar r = encodeHighChar(c);
/* 165 */     if (r == null) {
/* 166 */       return false;
/*     */     }
/* 168 */     bb.put(r.code);
/* 169 */     return true;
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
/*     */   private Simple8BitChar encodeHighChar(char c) {
/* 181 */     int i0 = 0;
/* 182 */     int i1 = this.reverseMapping.size();
/*     */     
/* 184 */     while (i1 > i0) {
/*     */       
/* 186 */       int i = i0 + (i1 - i0) / 2;
/*     */       
/* 188 */       Simple8BitChar m = this.reverseMapping.get(i);
/*     */       
/* 190 */       if (m.unicode == c) {
/* 191 */         return m;
/*     */       }
/*     */       
/* 194 */       if (m.unicode < c) {
/* 195 */         i0 = i + 1; continue;
/*     */       } 
/* 197 */       i1 = i;
/*     */     } 
/*     */ 
/*     */     
/* 201 */     if (i0 >= this.reverseMapping.size()) {
/* 202 */       return null;
/*     */     }
/*     */     
/* 205 */     Simple8BitChar r = this.reverseMapping.get(i0);
/*     */     
/* 207 */     if (r.unicode != c) {
/* 208 */       return null;
/*     */     }
/*     */     
/* 211 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canEncode(String name) {
/* 219 */     for (int i = 0; i < name.length(); i++) {
/*     */       
/* 221 */       char c = name.charAt(i);
/*     */       
/* 223 */       if (!canEncodeChar(c)) {
/* 224 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 228 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer encode(String name) {
/* 235 */     ByteBuffer out = ByteBuffer.allocate(name.length() + 6 + (name
/* 236 */         .length() + 1) / 2);
/*     */     
/* 238 */     for (int i = 0; i < name.length(); i++) {
/* 239 */       char c = name.charAt(i);
/*     */       
/* 241 */       if (out.remaining() < 6) {
/* 242 */         out = ZipEncodingHelper.growBuffer(out, out.position() + 6);
/*     */       }
/*     */       
/* 245 */       if (!pushEncodedChar(out, c)) {
/* 246 */         ZipEncodingHelper.appendSurrogate(out, c);
/*     */       }
/*     */     } 
/*     */     
/* 250 */     ZipEncodingHelper.prepareBufferForRead(out);
/* 251 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String decode(byte[] data) throws IOException {
/* 258 */     char[] ret = new char[data.length];
/*     */     
/* 260 */     for (int i = 0; i < data.length; i++) {
/* 261 */       ret[i] = decodeByte(data[i]);
/*     */     }
/*     */     
/* 264 */     return new String(ret);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/Simple8BitZipEncoding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */