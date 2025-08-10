/*     */ package META-INF.versions.9.org.bouncycastle.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Vector;
/*     */ import org.bouncycastle.util.StringList;
/*     */ import org.bouncycastle.util.encoders.UTF8;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Strings
/*     */ {
/*     */   private static String LINE_SEPARATOR;
/*     */   
/*     */   static {
/*     */     try {
/*  24 */       LINE_SEPARATOR = AccessController.<String>doPrivileged((PrivilegedAction<String>)new Object());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*  34 */     catch (Exception exception) {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/*  39 */         LINE_SEPARATOR = String.format("%n", new Object[0]);
/*     */       }
/*  41 */       catch (Exception exception1) {
/*     */         
/*  43 */         LINE_SEPARATOR = "\n";
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String fromUTF8ByteArray(byte[] paramArrayOfbyte) {
/*  50 */     char[] arrayOfChar = new char[paramArrayOfbyte.length];
/*  51 */     int i = UTF8.transcodeToUTF16(paramArrayOfbyte, arrayOfChar);
/*  52 */     if (i < 0)
/*     */     {
/*  54 */       throw new IllegalArgumentException("Invalid UTF-8 input");
/*     */     }
/*  56 */     return new String(arrayOfChar, 0, i);
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] toUTF8ByteArray(String paramString) {
/*  61 */     return toUTF8ByteArray(paramString.toCharArray());
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] toUTF8ByteArray(char[] paramArrayOfchar) {
/*  66 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*     */ 
/*     */     
/*     */     try {
/*  70 */       toUTF8ByteArray(paramArrayOfchar, byteArrayOutputStream);
/*     */     }
/*  72 */     catch (IOException iOException) {
/*     */       
/*  74 */       throw new IllegalStateException("cannot encode string to byte array!");
/*     */     } 
/*     */     
/*  77 */     return byteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void toUTF8ByteArray(char[] paramArrayOfchar, OutputStream paramOutputStream) throws IOException {
/*  83 */     char[] arrayOfChar = paramArrayOfchar;
/*  84 */     byte b = 0;
/*     */     
/*  86 */     while (b < arrayOfChar.length) {
/*     */       
/*  88 */       char c = arrayOfChar[b];
/*     */       
/*  90 */       if (c < '') {
/*     */         
/*  92 */         paramOutputStream.write(c);
/*     */       }
/*  94 */       else if (c < 'ࠀ') {
/*     */         
/*  96 */         paramOutputStream.write(0xC0 | c >> 6);
/*  97 */         paramOutputStream.write(0x80 | c & 0x3F);
/*     */       
/*     */       }
/* 100 */       else if (c >= '?' && c <= '?') {
/*     */ 
/*     */ 
/*     */         
/* 104 */         if (b + 1 >= arrayOfChar.length)
/*     */         {
/* 106 */           throw new IllegalStateException("invalid UTF-16 codepoint");
/*     */         }
/* 108 */         char c1 = c;
/* 109 */         c = arrayOfChar[++b];
/* 110 */         char c2 = c;
/*     */ 
/*     */         
/* 113 */         if (c1 > '?')
/*     */         {
/* 115 */           throw new IllegalStateException("invalid UTF-16 codepoint");
/*     */         }
/* 117 */         int i = ((c1 & 0x3FF) << 10 | c2 & 0x3FF) + 65536;
/* 118 */         paramOutputStream.write(0xF0 | i >> 18);
/* 119 */         paramOutputStream.write(0x80 | i >> 12 & 0x3F);
/* 120 */         paramOutputStream.write(0x80 | i >> 6 & 0x3F);
/* 121 */         paramOutputStream.write(0x80 | i & 0x3F);
/*     */       }
/*     */       else {
/*     */         
/* 125 */         paramOutputStream.write(0xE0 | c >> 12);
/* 126 */         paramOutputStream.write(0x80 | c >> 6 & 0x3F);
/* 127 */         paramOutputStream.write(0x80 | c & 0x3F);
/*     */       } 
/*     */       
/* 130 */       b++;
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
/*     */   public static String toUpperCase(String paramString) {
/* 142 */     boolean bool = false;
/* 143 */     char[] arrayOfChar = paramString.toCharArray();
/*     */     
/* 145 */     for (byte b = 0; b != arrayOfChar.length; b++) {
/*     */       
/* 147 */       char c = arrayOfChar[b];
/* 148 */       if ('a' <= c && 'z' >= c) {
/*     */         
/* 150 */         bool = true;
/* 151 */         arrayOfChar[b] = (char)(c - 97 + 65);
/*     */       } 
/*     */     } 
/*     */     
/* 155 */     if (bool)
/*     */     {
/* 157 */       return new String(arrayOfChar);
/*     */     }
/*     */     
/* 160 */     return paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toLowerCase(String paramString) {
/* 171 */     boolean bool = false;
/* 172 */     char[] arrayOfChar = paramString.toCharArray();
/*     */     
/* 174 */     for (byte b = 0; b != arrayOfChar.length; b++) {
/*     */       
/* 176 */       char c = arrayOfChar[b];
/* 177 */       if ('A' <= c && 'Z' >= c) {
/*     */         
/* 179 */         bool = true;
/* 180 */         arrayOfChar[b] = (char)(c - 65 + 97);
/*     */       } 
/*     */     } 
/*     */     
/* 184 */     if (bool)
/*     */     {
/* 186 */       return new String(arrayOfChar);
/*     */     }
/*     */     
/* 189 */     return paramString;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] toByteArray(char[] paramArrayOfchar) {
/* 194 */     byte[] arrayOfByte = new byte[paramArrayOfchar.length];
/*     */     
/* 196 */     for (byte b = 0; b != arrayOfByte.length; b++)
/*     */     {
/* 198 */       arrayOfByte[b] = (byte)paramArrayOfchar[b];
/*     */     }
/*     */     
/* 201 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] toByteArray(String paramString) {
/* 207 */     byte[] arrayOfByte = new byte[paramString.length()];
/*     */     
/* 209 */     for (byte b = 0; b != arrayOfByte.length; b++) {
/*     */       
/* 211 */       char c = paramString.charAt(b);
/*     */       
/* 213 */       arrayOfByte[b] = (byte)c;
/*     */     } 
/*     */     
/* 216 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int toByteArray(String paramString, byte[] paramArrayOfbyte, int paramInt) {
/* 221 */     int i = paramString.length();
/* 222 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 224 */       char c = paramString.charAt(b);
/* 225 */       paramArrayOfbyte[paramInt + b] = (byte)c;
/*     */     } 
/* 227 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String fromByteArray(byte[] paramArrayOfbyte) {
/* 238 */     return new String(asCharArray(paramArrayOfbyte));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char[] asCharArray(byte[] paramArrayOfbyte) {
/* 249 */     char[] arrayOfChar = new char[paramArrayOfbyte.length];
/*     */     
/* 251 */     for (byte b = 0; b != arrayOfChar.length; b++)
/*     */     {
/* 253 */       arrayOfChar[b] = (char)(paramArrayOfbyte[b] & 0xFF);
/*     */     }
/*     */     
/* 256 */     return arrayOfChar;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String[] split(String paramString, char paramChar) {
/* 261 */     Vector<String> vector = new Vector();
/* 262 */     boolean bool = true;
/*     */ 
/*     */     
/* 265 */     while (bool) {
/*     */       
/* 267 */       int i = paramString.indexOf(paramChar);
/* 268 */       if (i > 0) {
/*     */         
/* 270 */         String str = paramString.substring(0, i);
/* 271 */         vector.addElement(str);
/* 272 */         paramString = paramString.substring(i + 1);
/*     */         
/*     */         continue;
/*     */       } 
/* 276 */       bool = false;
/* 277 */       vector.addElement(paramString);
/*     */     } 
/*     */ 
/*     */     
/* 281 */     String[] arrayOfString = new String[vector.size()];
/*     */     
/* 283 */     for (byte b = 0; b != arrayOfString.length; b++)
/*     */     {
/* 285 */       arrayOfString[b] = vector.elementAt(b);
/*     */     }
/* 287 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   
/*     */   public static StringList newList() {
/* 292 */     return (StringList)new StringListImpl(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String lineSeparator() {
/* 297 */     return LINE_SEPARATOR;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/util/Strings.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */