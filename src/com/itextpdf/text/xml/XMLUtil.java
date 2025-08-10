/*     */ package com.itextpdf.text.xml;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLUtil
/*     */ {
/*     */   public static String escapeXML(String s, boolean onlyASCII) {
/*  62 */     char[] cc = s.toCharArray();
/*  63 */     int len = cc.length;
/*  64 */     StringBuffer sb = new StringBuffer();
/*  65 */     for (int k = 0; k < len; k++) {
/*  66 */       int c = cc[k];
/*  67 */       switch (c) {
/*     */         case 60:
/*  69 */           sb.append("&lt;");
/*     */           break;
/*     */         case 62:
/*  72 */           sb.append("&gt;");
/*     */           break;
/*     */         case 38:
/*  75 */           sb.append("&amp;");
/*     */           break;
/*     */         case 34:
/*  78 */           sb.append("&quot;");
/*     */           break;
/*     */         case 39:
/*  81 */           sb.append("&apos;");
/*     */           break;
/*     */         default:
/*  84 */           if (isValidCharacterValue(c)) {
/*  85 */             if (onlyASCII && c > 127) {
/*  86 */               sb.append("&#").append(c).append(';'); break;
/*     */             } 
/*  88 */             sb.append((char)c);
/*     */           }  break;
/*     */       } 
/*     */     } 
/*  92 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String unescapeXML(String s) {
/* 102 */     char[] cc = s.toCharArray();
/* 103 */     int len = cc.length;
/* 104 */     StringBuffer sb = new StringBuffer();
/*     */ 
/*     */     
/* 107 */     int i = 0; while (true) { if (i < len)
/* 108 */       { int c = cc[i];
/* 109 */         if (c == 38) {
/* 110 */           int pos = findInArray(';', cc, i + 3);
/* 111 */           if (pos > -1) {
/* 112 */             String esc = new String(cc, i + 1, pos - i - 1);
/* 113 */             if (esc.startsWith("#")) {
/* 114 */               esc = esc.substring(1);
/* 115 */               if (isValidCharacterValue(esc)) {
/* 116 */                 c = (char)Integer.parseInt(esc);
/* 117 */                 i = pos;
/*     */               } else {
/* 119 */                 i = pos;
/*     */                 
/*     */                 i++;
/*     */               } 
/*     */             } else {
/* 124 */               int tmp = unescape(esc);
/* 125 */               if (tmp > 0) {
/* 126 */                 c = tmp;
/* 127 */                 i = pos;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/* 132 */         sb.append((char)c); } else { break; }
/*     */        i++; }
/* 134 */      return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int unescape(String s) {
/* 144 */     if ("apos".equals(s))
/* 145 */       return 39; 
/* 146 */     if ("quot".equals(s))
/* 147 */       return 34; 
/* 148 */     if ("lt".equals(s))
/* 149 */       return 60; 
/* 150 */     if ("gt".equals(s))
/* 151 */       return 62; 
/* 152 */     if ("amp".equals(s))
/* 153 */       return 38; 
/* 154 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidCharacterValue(String s) {
/*     */     try {
/* 164 */       int i = Integer.parseInt(s);
/* 165 */       return isValidCharacterValue(i);
/*     */     }
/* 167 */     catch (NumberFormatException nfe) {
/* 168 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidCharacterValue(int c) {
/* 178 */     return (c == 9 || c == 10 || c == 13 || (c >= 32 && c <= 55295) || (c >= 57344 && c <= 65533) || (c >= 65536 && c <= 1114111));
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
/*     */   public static int findInArray(char needle, char[] haystack, int start) {
/* 192 */     for (int i = start; i < haystack.length; i++) {
/* 193 */       if (haystack[i] == ';')
/* 194 */         return i; 
/*     */     } 
/* 196 */     return -1;
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
/*     */   public static String getEncodingName(byte[] b4) {
/* 220 */     int b0 = b4[0] & 0xFF;
/* 221 */     int b1 = b4[1] & 0xFF;
/* 222 */     if (b0 == 254 && b1 == 255)
/*     */     {
/* 224 */       return "UTF-16BE";
/*     */     }
/* 226 */     if (b0 == 255 && b1 == 254)
/*     */     {
/* 228 */       return "UTF-16LE";
/*     */     }
/*     */ 
/*     */     
/* 232 */     int b2 = b4[2] & 0xFF;
/* 233 */     if (b0 == 239 && b1 == 187 && b2 == 191) {
/* 234 */       return "UTF-8";
/*     */     }
/*     */ 
/*     */     
/* 238 */     int b3 = b4[3] & 0xFF;
/* 239 */     if (b0 == 0 && b1 == 0 && b2 == 0 && b3 == 60)
/*     */     {
/* 241 */       return "ISO-10646-UCS-4";
/*     */     }
/* 243 */     if (b0 == 60 && b1 == 0 && b2 == 0 && b3 == 0)
/*     */     {
/* 245 */       return "ISO-10646-UCS-4";
/*     */     }
/* 247 */     if (b0 == 0 && b1 == 0 && b2 == 60 && b3 == 0)
/*     */     {
/*     */       
/* 250 */       return "ISO-10646-UCS-4";
/*     */     }
/* 252 */     if (b0 == 0 && b1 == 60 && b2 == 0 && b3 == 0)
/*     */     {
/*     */       
/* 255 */       return "ISO-10646-UCS-4";
/*     */     }
/* 257 */     if (b0 == 0 && b1 == 60 && b2 == 0 && b3 == 63)
/*     */     {
/*     */ 
/*     */       
/* 261 */       return "UTF-16BE";
/*     */     }
/* 263 */     if (b0 == 60 && b1 == 0 && b2 == 63 && b3 == 0)
/*     */     {
/*     */       
/* 266 */       return "UTF-16LE";
/*     */     }
/* 268 */     if (b0 == 76 && b1 == 111 && b2 == 167 && b3 == 148)
/*     */     {
/*     */       
/* 271 */       return "CP037";
/*     */     }
/*     */ 
/*     */     
/* 275 */     return "UTF-8";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/xml/XMLUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */