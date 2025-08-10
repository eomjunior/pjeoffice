/*     */ package com.yworks.yguard;
/*     */ 
/*     */ import com.yworks.yguard.obf.ClassTree;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Conversion
/*     */ {
/*     */   public static String toJavaClass(String className) {
/*  31 */     if (className.endsWith(".class")) {
/*  32 */       className = className.substring(0, className.length() - 6);
/*     */     }
/*  34 */     return className.replace('/', '.');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toJavaType(String type) {
/*     */     String className;
/*  45 */     StringBuffer nat = new StringBuffer(30);
/*  46 */     int arraydim = 0;
/*  47 */     for (; type.charAt(arraydim) == '['; arraydim++);
/*  48 */     type = type.substring(arraydim);
/*  49 */     switch (type.charAt(0)) {
/*     */       default:
/*  51 */         throw new IllegalArgumentException("unknown native type:" + type);
/*     */       case 'B':
/*  53 */         nat.append("byte");
/*     */         break;
/*     */       case 'C':
/*  56 */         nat.append("char");
/*     */         break;
/*     */       case 'D':
/*  59 */         nat.append("double");
/*     */         break;
/*     */       case 'F':
/*  62 */         nat.append("float");
/*     */         break;
/*     */       case 'I':
/*  65 */         nat.append("int");
/*     */         break;
/*     */       case 'J':
/*  68 */         nat.append("long");
/*     */         break;
/*     */       case 'S':
/*  71 */         nat.append("short");
/*     */         break;
/*     */       case 'Z':
/*  74 */         nat.append("boolean");
/*     */         break;
/*     */       case 'V':
/*  77 */         nat.append("void");
/*     */         break;
/*     */       case 'L':
/*  80 */         className = type.substring(1, type.length() - 1);
/*  81 */         if (className.indexOf('<') >= 0) {
/*  82 */           String parameters = type.substring(className.indexOf('<') + 2, className.lastIndexOf('>') - 1);
/*  83 */           className = className.substring(0, className.indexOf('<'));
/*  84 */           nat.append(className.replace('/', '.'));
/*  85 */           nat.append('<');
/*  86 */           nat.append(toJavaParameters(parameters));
/*  87 */           nat.append('>'); break;
/*     */         } 
/*  89 */         nat.append(className.replace('/', '.'));
/*     */         break;
/*     */     } 
/*     */     
/*  93 */     for (int i = 0; i < arraydim; i++) {
/*  94 */       nat.append("[]");
/*     */     }
/*  96 */     return nat.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String mapSignature(String signature) {
/* 107 */     return (new ClassTree()).mapSignature(signature);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toJavaParameters(String parameters) {
/*     */     int len;
/* 118 */     StringBuffer nat = new StringBuffer(30);
/* 119 */     switch (parameters.charAt(0))
/*     */     { default:
/* 121 */         throw new IllegalArgumentException("unknown native type:" + parameters.charAt(0));
/*     */       case '+':
/* 123 */         nat.append("? extends ").append(toJavaParameters(parameters.substring(1)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 176 */         return nat.toString();case '-': nat.append("? super ").append(toJavaParameters(parameters.substring(1))); return nat.toString();case '*': nat.append("*"); if (parameters.length() > 1) nat.append(", ").append(toJavaParameters(parameters.substring(1)));  return nat.toString();case 'B': nat.append("byte"); return nat.toString();case 'C': nat.append("char"); return nat.toString();case 'D': nat.append("double"); return nat.toString();case 'F': nat.append("float"); return nat.toString();case 'I': nat.append("int"); return nat.toString();case 'J': nat.append("long"); return nat.toString();case 'S': nat.append("short"); return nat.toString();case 'Z': nat.append("boolean"); return nat.toString();case 'V': nat.append("void"); return nat.toString();case 'L': len = parameters.indexOf('<'); if (len >= 0) len = Math.min(len, parameters.indexOf(';'));  return nat.toString();case 'T': break; }  int index = parameters.indexOf(';'); nat.append(parameters.substring(1, index)); if (parameters.length() > index) { nat.append(", "); nat.append(parameters.substring(index)); }  return nat.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toJavaMethod(String name, String signature) {
/* 187 */     String argsonly = signature.substring(signature.indexOf('(') + 1);
/* 188 */     String ret = signature.substring(signature.indexOf(')') + 1);
/* 189 */     ret = toJavaType(ret);
/* 190 */     StringBuffer args = new StringBuffer();
/* 191 */     args.append('(');
/* 192 */     if (argsonly.indexOf(')') > 0) {
/* 193 */       argsonly = argsonly.substring(0, argsonly.indexOf(')'));
/* 194 */       toJavaArguments(argsonly, args);
/*     */     } 
/* 196 */     args.append(')');
/* 197 */     return ret + " " + name + args.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toJavaArguments(String args) {
/* 207 */     StringBuffer b = new StringBuffer(args.length() + 32);
/* 208 */     toJavaArguments(args, b);
/* 209 */     return b.toString();
/*     */   }
/*     */   
/*     */   private static void toJavaArguments(String argsonly, StringBuffer args) {
/* 213 */     int argcount = 0;
/* 214 */     int pos = 0;
/* 215 */     StringBuffer arg = new StringBuffer(20);
/* 216 */     while (pos < argsonly.length()) {
/* 217 */       while (argsonly.charAt(pos) == '[') {
/* 218 */         arg.append('[');
/* 219 */         pos++;
/*     */       } 
/* 221 */       if (argsonly.charAt(pos) == 'L') {
/* 222 */         while (argsonly.charAt(pos) != ';') {
/* 223 */           arg.append(argsonly.charAt(pos));
/* 224 */           pos++;
/*     */         } 
/* 226 */         arg.append(';');
/* 227 */         if (argcount > 0) {
/* 228 */           args.append(',');
/* 229 */           args.append(' ');
/*     */         } 
/* 231 */         args.append(toJavaType(arg.toString()));
/* 232 */         argcount++;
/* 233 */         arg.setLength(0);
/* 234 */         pos++; continue;
/*     */       } 
/* 236 */       arg.append(argsonly.charAt(pos));
/* 237 */       if (argcount > 0) {
/* 238 */         args.append(',');
/* 239 */         args.append(' ');
/*     */       } 
/* 241 */       args.append(toJavaType(arg.toString()));
/* 242 */       argcount++;
/* 243 */       arg.setLength(0);
/* 244 */       pos++;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/Conversion.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */