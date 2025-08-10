/*     */ package com.yworks.yguard.obf;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class YGuardRule
/*     */ {
/*     */   public static final int PUBLIC = 1;
/*     */   public static final int PROTECTED = 4;
/*     */   public static final int FRIENDLY = 4096;
/*     */   public static final int PRIVATE = 2;
/*     */   public static final int LEVEL_NONE = 0;
/*     */   public static final int LEVEL_PUBLIC = 1;
/*     */   public static final int LEVEL_PROTECTED = 5;
/*     */   public static final int LEVEL_FRIENDLY = 4101;
/*     */   public static final int LEVEL_PRIVATE = 4103;
/*     */   public static final int TYPE_ATTR = 0;
/*     */   public static final int TYPE_CLASS = 1;
/*     */   public static final int TYPE_FIELD = 2;
/*     */   public static final int TYPE_METHOD = 3;
/*     */   public static final int TYPE_PACKAGE_MAP = 4;
/*     */   public static final int TYPE_CLASS_MAP = 5;
/*     */   public static final int TYPE_FIELD_MAP = 6;
/*     */   public static final int TYPE_METHOD_MAP = 7;
/*     */   public static final int TYPE_SOURCE_ATTRIBUTE_MAP = 8;
/*     */   public static final int TYPE_LINE_NUMBER_MAPPER = 9;
/*     */   public static final int TYPE_ATTR2 = 10;
/*     */   public static final int TYPE_PACKAGE = 11;
/*     */   public int type;
/*     */   public String name;
/*     */   public String descriptor;
/*     */   public String obfName;
/*     */   public LineNumberTableMapper lineNumberTableMapper;
/* 132 */   public int retainFields = 0;
/*     */ 
/*     */ 
/*     */   
/* 136 */   public int retainMethods = 0;
/*     */ 
/*     */ 
/*     */   
/* 140 */   public int retainClasses = 4103;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public YGuardRule(int type, String name) {
/* 150 */     this.type = type;
/* 151 */     this.name = name;
/* 152 */     this.descriptor = null;
/* 153 */     this.obfName = null;
/* 154 */     this.lineNumberTableMapper = null;
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
/*     */   public YGuardRule(int type, String name, String descriptor) {
/* 166 */     this.obfName = null;
/* 167 */     this.type = type;
/* 168 */     this.name = name;
/* 169 */     this.descriptor = descriptor;
/* 170 */     this.lineNumberTableMapper = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public YGuardRule(String className, LineNumberTableMapper lineNumberTableMapper) {
/* 180 */     this.descriptor = null;
/* 181 */     this.obfName = null;
/* 182 */     this.name = className;
/* 183 */     this.type = 9;
/* 184 */     this.lineNumberTableMapper = lineNumberTableMapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logProperties(PrintWriter pw) {
/* 194 */     if (this.type == 9) {
/* 195 */       this.lineNumberTableMapper.logProperties(pw);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 201 */     return typeToString(this.type) + " " + this.name + " " + this.descriptor + " fields: " + 
/* 202 */       methodToString(this.retainFields) + " methods: " + 
/* 203 */       methodToString(this.retainMethods) + " classes: " + 
/* 204 */       methodToString(this.retainClasses);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String typeToString(int type) {
/* 214 */     switch (type) { default:
/* 215 */         return "Unknown type " + type;
/*     */       case 0:
/* 217 */         return "ATTRIBUTE";
/*     */       case 10:
/* 219 */         return "ATTRIBUTE PER CLASS";
/*     */       case 1:
/* 221 */         return "CLASS";
/*     */       case 5:
/* 223 */         return "CLASS_MAP";
/*     */       case 2:
/* 225 */         return "FIELD";
/*     */       case 6:
/* 227 */         return "FIELD_MAP";
/*     */       case 3:
/* 229 */         return "METHOD";
/*     */       case 7:
/* 231 */         return "METHOD_MAP";
/*     */       case 4:
/* 233 */         return "PACKAGE_MAP";
/*     */       case 8:
/* 235 */         return "SOURCE_ATTRIBUTE_MAP";
/*     */       case 9:
/* 237 */         return "LINE_NUMBER_MAPPER";
/*     */       case 11:
/* 239 */         break; }  return "PACKAGE";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String methodToString(int modifier) {
/* 250 */     switch (modifier) { default:
/* 251 */         return "Unknown modifier " + modifier;
/*     */       case 0:
/* 253 */         return "LEVEL_NONE";
/*     */       case 4101:
/* 255 */         return "LEVEL_FRIENDLY";
/*     */       case 4103:
/* 257 */         return "LEVEL_PRIVATE";
/*     */       case 5:
/* 259 */         return "LEVEL_PROTECTED";
/*     */       case 1:
/* 261 */         break; }  return "LEVEL_PUBLIC";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/YGuardRule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */