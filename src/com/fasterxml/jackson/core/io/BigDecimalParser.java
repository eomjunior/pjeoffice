/*     */ package com.fasterxml.jackson.core.io;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BigDecimalParser
/*     */ {
/*     */   private static final int MAX_CHARS_TO_REPORT = 1000;
/*     */   private final char[] chars;
/*     */   
/*     */   BigDecimalParser(char[] chars) {
/*  28 */     this.chars = chars;
/*     */   }
/*     */   
/*     */   public static BigDecimal parse(String valueStr) {
/*  32 */     return parse(valueStr.toCharArray());
/*     */   }
/*     */   
/*     */   public static BigDecimal parse(char[] chars, int off, int len) {
/*  36 */     if (off > 0 || len != chars.length) {
/*  37 */       chars = Arrays.copyOfRange(chars, off, off + len);
/*     */     }
/*  39 */     return parse(chars);
/*     */   }
/*     */   
/*     */   public static BigDecimal parse(char[] chars) {
/*  43 */     int len = chars.length;
/*     */     try {
/*  45 */       if (len < 500) {
/*  46 */         return new BigDecimal(chars);
/*     */       }
/*  48 */       return (new BigDecimalParser(chars)).parseBigDecimal(len / 10);
/*  49 */     } catch (NumberFormatException e) {
/*  50 */       String stringToReport, desc = e.getMessage();
/*     */       
/*  52 */       if (desc == null) {
/*  53 */         desc = "Not a valid number representation";
/*     */       }
/*     */       
/*  56 */       if (chars.length <= 1000) {
/*  57 */         stringToReport = new String(chars);
/*     */       } else {
/*  59 */         stringToReport = new String(Arrays.copyOfRange(chars, 0, 1000)) + "(truncated, full length is " + chars.length + " chars)";
/*     */       } 
/*     */       
/*  62 */       throw new NumberFormatException("Value \"" + stringToReport + "\" can not be represented as `java.math.BigDecimal`, reason: " + desc);
/*     */     } 
/*     */   }
/*     */   private BigDecimal parseBigDecimal(int splitLen) {
/*     */     int numEndIdx;
/*     */     BigDecimal res;
/*  68 */     boolean numHasSign = false;
/*  69 */     boolean expHasSign = false;
/*  70 */     boolean neg = false;
/*  71 */     int numIdx = 0;
/*  72 */     int expIdx = -1;
/*  73 */     int dotIdx = -1;
/*  74 */     int scale = 0;
/*  75 */     int len = this.chars.length;
/*     */     
/*  77 */     for (int i = 0; i < len; i++) {
/*  78 */       char c = this.chars[i];
/*  79 */       switch (c) {
/*     */         case '+':
/*  81 */           if (expIdx >= 0) {
/*  82 */             if (expHasSign) {
/*  83 */               throw new NumberFormatException("Multiple signs in exponent");
/*     */             }
/*  85 */             expHasSign = true; break;
/*     */           } 
/*  87 */           if (numHasSign) {
/*  88 */             throw new NumberFormatException("Multiple signs in number");
/*     */           }
/*  90 */           numHasSign = true;
/*  91 */           numIdx = i + 1;
/*     */           break;
/*     */         
/*     */         case '-':
/*  95 */           if (expIdx >= 0) {
/*  96 */             if (expHasSign) {
/*  97 */               throw new NumberFormatException("Multiple signs in exponent");
/*     */             }
/*  99 */             expHasSign = true; break;
/*     */           } 
/* 101 */           if (numHasSign) {
/* 102 */             throw new NumberFormatException("Multiple signs in number");
/*     */           }
/* 104 */           numHasSign = true;
/* 105 */           neg = true;
/* 106 */           numIdx = i + 1;
/*     */           break;
/*     */         
/*     */         case 'E':
/*     */         case 'e':
/* 111 */           if (expIdx >= 0) {
/* 112 */             throw new NumberFormatException("Multiple exponent markers");
/*     */           }
/* 114 */           expIdx = i;
/*     */           break;
/*     */         case '.':
/* 117 */           if (dotIdx >= 0) {
/* 118 */             throw new NumberFormatException("Multiple decimal points");
/*     */           }
/* 120 */           dotIdx = i;
/*     */           break;
/*     */         default:
/* 123 */           if (dotIdx >= 0 && expIdx == -1) {
/* 124 */             scale++;
/*     */           }
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 130 */     int exp = 0;
/* 131 */     if (expIdx >= 0) {
/* 132 */       numEndIdx = expIdx;
/* 133 */       String expStr = new String(this.chars, expIdx + 1, len - expIdx - 1);
/* 134 */       exp = Integer.parseInt(expStr);
/* 135 */       scale = adjustScale(scale, exp);
/*     */     } else {
/* 137 */       numEndIdx = len;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 142 */     if (dotIdx >= 0) {
/* 143 */       int leftLen = dotIdx - numIdx;
/* 144 */       BigDecimal left = toBigDecimalRec(numIdx, leftLen, exp, splitLen);
/*     */       
/* 146 */       int rightLen = numEndIdx - dotIdx - 1;
/* 147 */       BigDecimal right = toBigDecimalRec(dotIdx + 1, rightLen, exp - rightLen, splitLen);
/*     */       
/* 149 */       res = left.add(right);
/*     */     } else {
/* 151 */       res = toBigDecimalRec(numIdx, numEndIdx - numIdx, exp, splitLen);
/*     */     } 
/*     */     
/* 154 */     if (scale != 0) {
/* 155 */       res = res.setScale(scale);
/*     */     }
/*     */     
/* 158 */     if (neg) {
/* 159 */       res = res.negate();
/*     */     }
/*     */     
/* 162 */     return res;
/*     */   }
/*     */   
/*     */   private int adjustScale(int scale, long exp) {
/* 166 */     long adjScale = scale - exp;
/* 167 */     if (adjScale > 2147483647L || adjScale < -2147483648L) {
/* 168 */       throw new NumberFormatException("Scale out of range: " + adjScale + " while adjusting scale " + scale + " to exponent " + exp);
/*     */     }
/*     */ 
/*     */     
/* 172 */     return (int)adjScale;
/*     */   }
/*     */   
/*     */   private BigDecimal toBigDecimalRec(int off, int len, int scale, int splitLen) {
/* 176 */     if (len > splitLen) {
/* 177 */       int mid = len / 2;
/* 178 */       BigDecimal left = toBigDecimalRec(off, mid, scale + len - mid, splitLen);
/* 179 */       BigDecimal right = toBigDecimalRec(off + mid, len - mid, scale, splitLen);
/*     */       
/* 181 */       return left.add(right);
/*     */     } 
/*     */     
/* 184 */     return (len == 0) ? BigDecimal.ZERO : (new BigDecimal(this.chars, off, len)).movePointRight(scale);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/core/io/BigDecimalParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */