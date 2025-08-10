/*     */ package com.yworks.yshrink.util;
/*     */ 
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.objectweb.asm.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Util
/*     */ {
/*     */   public static final String toJavaClass(String className) {
/*  23 */     if (className.endsWith(".class")) {
/*  24 */       className = className.substring(0, className.length() - 6);
/*     */     }
/*  26 */     return className.replace('/', '.');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String toInternalClass(String className) {
/*  36 */     if (className.endsWith(".class")) {
/*  37 */       className = className.substring(0, className.length() - 6);
/*     */     }
/*  39 */     return className.replace('.', '/');
/*     */   }
/*     */   
/*     */   private static final String toNativeType(String type, int arraydim) {
/*  43 */     StringBuffer nat = new StringBuffer(30);
/*  44 */     for (int i = 0; i < arraydim; i++) {
/*  45 */       nat.append('[');
/*     */     }
/*  47 */     if ("byte".equals(type)) {
/*  48 */       nat.append('B');
/*  49 */     } else if ("char".equals(type)) {
/*  50 */       nat.append('C');
/*  51 */     } else if ("double".equals(type)) {
/*  52 */       nat.append('D');
/*  53 */     } else if ("float".equals(type)) {
/*  54 */       nat.append('F');
/*  55 */     } else if ("int".equals(type)) {
/*  56 */       nat.append('I');
/*  57 */     } else if ("long".equals(type)) {
/*  58 */       nat.append('J');
/*  59 */     } else if ("short".equals(type)) {
/*  60 */       nat.append('S');
/*  61 */     } else if ("boolean".equals(type)) {
/*  62 */       nat.append('Z');
/*  63 */     } else if ("void".equals(type)) {
/*  64 */       nat.append('V');
/*     */     } else {
/*  66 */       nat.append('L');
/*  67 */       nat.append(type.replace('.', '/'));
/*  68 */       nat.append(';');
/*     */     } 
/*  70 */     return nat.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String verboseToNativeType(String type) {
/*  81 */     if (type == "") return null;
/*     */     
/*  83 */     Pattern p = Pattern.compile("\\s*\\[\\s*\\]\\s*");
/*  84 */     Matcher m = p.matcher(type);
/*     */     
/*  86 */     int arrayDim = 0;
/*  87 */     while (m.find()) {
/*  88 */       arrayDim++;
/*     */     }
/*     */     
/*  91 */     return toNativeType(type.substring(0, type.length() - arrayDim * 2), arrayDim);
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
/*     */   public static final String getTypeNameFromDescriptor(String desc) {
/* 103 */     String r = desc;
/*     */     
/* 105 */     int i = desc.lastIndexOf('[');
/* 106 */     if (i != -1) {
/* 107 */       char type = desc.charAt(i + 1);
/* 108 */       if (type != 'L') {
/* 109 */         r = String.valueOf(type);
/*     */       } else {
/* 111 */         r = desc.substring(i + 2, desc.length() - 1);
/*     */       }
/*     */     
/* 114 */     } else if (desc.endsWith(";")) {
/* 115 */       r = desc.substring(1, desc.length() - 1);
/*     */     } 
/*     */     
/* 118 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toJavaType(String type) {
/*     */     String className;
/* 128 */     StringBuffer nat = new StringBuffer(30);
/* 129 */     int arraydim = 0;
/* 130 */     for (; type.charAt(arraydim) == '['; arraydim++);
/* 131 */     type = type.substring(arraydim);
/* 132 */     switch (type.charAt(0)) {
/*     */       default:
/* 134 */         throw new IllegalArgumentException("unknown native type:" + type);
/*     */       case 'B':
/* 136 */         nat.append("byte");
/*     */         break;
/*     */       case 'C':
/* 139 */         nat.append("char");
/*     */         break;
/*     */       case 'D':
/* 142 */         nat.append("double");
/*     */         break;
/*     */       case 'F':
/* 145 */         nat.append("float");
/*     */         break;
/*     */       case 'I':
/* 148 */         nat.append("int");
/*     */         break;
/*     */       case 'J':
/* 151 */         nat.append("long");
/*     */         break;
/*     */       case 'S':
/* 154 */         nat.append("short");
/*     */         break;
/*     */       case 'Z':
/* 157 */         nat.append("boolean");
/*     */         break;
/*     */       case 'V':
/* 160 */         nat.append("void");
/*     */         break;
/*     */       case 'L':
/* 163 */         className = type.substring(1, type.length() - 1);
/* 164 */         if (className.indexOf('<') >= 0) {
/* 165 */           String parameters = type.substring(className.indexOf('<') + 2, className.lastIndexOf('>') - 1);
/* 166 */           className = className.substring(0, className.indexOf('<'));
/* 167 */           nat.append(className.replace('/', '.'));
/* 168 */           nat.append('<');
/* 169 */           nat.append(toJavaParameters(parameters));
/* 170 */           nat.append('>'); break;
/*     */         } 
/* 172 */         nat.append(className.replace('/', '.'));
/*     */         break;
/*     */     } 
/*     */     
/* 176 */     for (int i = 0; i < arraydim; i++) {
/* 177 */       nat.append("[]");
/*     */     }
/* 179 */     return nat.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toJavaParameters(String parameters) {
/*     */     int len;
/* 189 */     StringBuffer nat = new StringBuffer(30);
/* 190 */     switch (parameters.charAt(0))
/*     */     { default:
/* 192 */         throw new IllegalArgumentException("unknown native type:" + parameters.charAt(0));
/*     */       case '+':
/* 194 */         nat.append("? extends ").append(toJavaParameters(parameters.substring(1)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 247 */         return nat.toString();case '-': nat.append("? super ").append(toJavaParameters(parameters.substring(1))); return nat.toString();case '*': nat.append("*"); if (parameters.length() > 1) nat.append(", ").append(toJavaParameters(parameters.substring(1)));  return nat.toString();case 'B': nat.append("byte"); return nat.toString();case 'C': nat.append("char"); return nat.toString();case 'D': nat.append("double"); return nat.toString();case 'F': nat.append("float"); return nat.toString();case 'I': nat.append("int"); return nat.toString();case 'J': nat.append("long"); return nat.toString();case 'S': nat.append("short"); return nat.toString();case 'Z': nat.append("boolean"); return nat.toString();case 'V': nat.append("void"); return nat.toString();case 'L': len = parameters.indexOf('<'); if (len >= 0) len = Math.min(len, parameters.indexOf(';'));  return nat.toString();case 'T': break; }  int index = parameters.indexOf(';'); nat.append(parameters.substring(1, index)); if (parameters.length() > index) { nat.append(", "); nat.append(parameters.substring(index)); }  return nat.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getArgumentString(Type[] arguments) {
/* 258 */     StringBuilder buf = new StringBuilder();
/* 259 */     for (int i = 0; i < arguments.length - 1; i++)
/*     */     {
/* 261 */       buf.append(toJavaType(arguments[i].getDescriptor())).append(",");
/*     */     }
/* 263 */     if (arguments.length > 0) {
/* 264 */       buf.append(toJavaType(arguments[arguments.length - 1].getDescriptor()));
/*     */     }
/* 266 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String[] toNativeMethod(String javaMethod) {
/* 276 */     StringTokenizer tokenizer = new StringTokenizer(javaMethod, "(,[]) ", true);
/* 277 */     String tmp = tokenizer.nextToken();
/*     */     
/* 279 */     while (tmp.trim().length() == 0) {
/* 280 */       tmp = tokenizer.nextToken();
/*     */     }
/* 282 */     String returnType = tmp;
/* 283 */     tmp = tokenizer.nextToken();
/* 284 */     int retarraydim = 0;
/* 285 */     while (tmp.equals("[")) {
/* 286 */       tmp = tokenizer.nextToken();
/* 287 */       if (!tmp.equals("]")) throw new IllegalArgumentException("']' expected but found " + tmp); 
/* 288 */       retarraydim++;
/* 289 */       tmp = tokenizer.nextToken();
/*     */     } 
/* 291 */     if (tmp.trim().length() != 0) {
/* 292 */       throw new IllegalArgumentException("space expected but found " + tmp);
/*     */     }
/* 294 */     tmp = tokenizer.nextToken();
/* 295 */     while (tmp.trim().length() == 0) {
/* 296 */       tmp = tokenizer.nextToken();
/*     */     }
/* 298 */     String name = tmp;
/* 299 */     StringBuffer nativeMethod = new StringBuffer(30);
/* 300 */     nativeMethod.append('(');
/* 301 */     tmp = tokenizer.nextToken();
/* 302 */     while (tmp.trim().length() == 0) {
/* 303 */       tmp = tokenizer.nextToken();
/*     */     }
/* 305 */     if (!tmp.equals("(")) throw new IllegalArgumentException("'(' expected but found " + tmp); 
/* 306 */     tmp = tokenizer.nextToken();
/* 307 */     while (!tmp.equals(")")) {
/* 308 */       while (tmp.trim().length() == 0) {
/* 309 */         tmp = tokenizer.nextToken();
/*     */       }
/* 311 */       String type = tmp;
/* 312 */       tmp = tokenizer.nextToken();
/* 313 */       while (tmp.trim().length() == 0) {
/* 314 */         tmp = tokenizer.nextToken();
/*     */       }
/* 316 */       int arraydim = 0;
/* 317 */       while (tmp.equals("[")) {
/* 318 */         tmp = tokenizer.nextToken();
/* 319 */         if (!tmp.equals("]")) throw new IllegalArgumentException("']' expected but found " + tmp); 
/* 320 */         arraydim++;
/* 321 */         tmp = tokenizer.nextToken();
/*     */       } 
/* 323 */       while (tmp.trim().length() == 0) {
/* 324 */         tmp = tokenizer.nextToken();
/*     */       }
/*     */       
/* 327 */       nativeMethod.append(toNativeType(type, arraydim));
/* 328 */       if (tmp.equals(",")) {
/* 329 */         tmp = tokenizer.nextToken();
/* 330 */         while (tmp.trim().length() == 0) {
/* 331 */           tmp = tokenizer.nextToken();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 336 */     nativeMethod.append(')');
/* 337 */     nativeMethod.append(toNativeType(returnType, retarraydim));
/* 338 */     String[] result = { name, nativeMethod.toString() };
/* 339 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 345 */   private static final char[] base64 = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final char pad = '=';
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toBase64(byte[] b) {
/* 359 */     StringBuffer sb = new StringBuffer();
/* 360 */     for (int ptr = 0; ptr < b.length; ptr += 3) {
/* 361 */       sb.append(base64[b[ptr] >> 2 & 0x3F]);
/* 362 */       if (ptr + 1 < b.length) {
/* 363 */         sb.append(base64[b[ptr] << 4 & 0x30 | b[ptr + 1] >> 4 & 0xF]);
/* 364 */         if (ptr + 2 < b.length) {
/* 365 */           sb.append(base64[b[ptr + 1] << 2 & 0x3C | b[ptr + 2] >> 6 & 0x3]);
/* 366 */           sb.append(base64[b[ptr + 2] & 0x3F]);
/*     */         } else {
/* 368 */           sb.append(base64[b[ptr + 1] << 2 & 0x3C]);
/* 369 */           sb.append('=');
/*     */         } 
/*     */       } else {
/* 372 */         sb.append(base64[b[ptr] << 4 & 0x30]);
/* 373 */         sb.append('=');
/* 374 */         sb.append('=');
/*     */       } 
/*     */     } 
/* 377 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/util/Util.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */