/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SequenceList
/*     */ {
/*     */   protected static final int COMMA = 1;
/*     */   protected static final int MINUS = 2;
/*     */   protected static final int NOT = 3;
/*     */   protected static final int TEXT = 4;
/*     */   protected static final int NUMBER = 5;
/*     */   protected static final int END = 6;
/*     */   protected static final char EOT = '￿';
/*     */   private static final int FIRST = 0;
/*     */   private static final int DIGIT = 1;
/*     */   private static final int OTHER = 2;
/*     */   private static final int DIGIT2 = 3;
/*     */   private static final String NOT_OTHER = "-,!0123456789";
/*     */   protected char[] text;
/*     */   protected int ptr;
/*     */   protected int number;
/*     */   protected String other;
/*     */   protected int low;
/*     */   protected int high;
/*     */   protected boolean odd;
/*     */   protected boolean even;
/*     */   protected boolean inverse;
/*     */   
/*     */   protected SequenceList(String range) {
/*  88 */     this.ptr = 0;
/*  89 */     this.text = range.toCharArray();
/*     */   }
/*     */   
/*     */   protected char nextChar() {
/*     */     while (true) {
/*  94 */       if (this.ptr >= this.text.length)
/*  95 */         return Character.MAX_VALUE; 
/*  96 */       char c = this.text[this.ptr++];
/*  97 */       if (c > ' ')
/*  98 */         return c; 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void putBack() {
/* 103 */     this.ptr--;
/* 104 */     if (this.ptr < 0)
/* 105 */       this.ptr = 0; 
/*     */   }
/*     */   
/*     */   protected int getType() {
/* 109 */     StringBuffer buf = new StringBuffer();
/* 110 */     int state = 0;
/*     */     while (true) {
/* 112 */       char c = nextChar();
/* 113 */       if (c == Character.MAX_VALUE) {
/* 114 */         if (state == 1) {
/* 115 */           this.number = Integer.parseInt(this.other = buf.toString());
/* 116 */           return 5;
/*     */         } 
/* 118 */         if (state == 2) {
/* 119 */           this.other = buf.toString().toLowerCase();
/* 120 */           return 4;
/*     */         } 
/* 122 */         return 6;
/*     */       } 
/* 124 */       switch (state) {
/*     */         case 0:
/* 126 */           switch (c) {
/*     */             case '!':
/* 128 */               return 3;
/*     */             case '-':
/* 130 */               return 2;
/*     */             case ',':
/* 132 */               return 1;
/*     */           } 
/* 134 */           buf.append(c);
/* 135 */           if (c >= '0' && c <= '9') {
/* 136 */             state = 1; continue;
/*     */           } 
/* 138 */           state = 2;
/*     */         
/*     */         case 1:
/* 141 */           if (c >= '0' && c <= '9') {
/* 142 */             buf.append(c); continue;
/*     */           } 
/* 144 */           putBack();
/* 145 */           this.number = Integer.parseInt(this.other = buf.toString());
/* 146 */           return 5;
/*     */ 
/*     */         
/*     */         case 2:
/* 150 */           if ("-,!0123456789".indexOf(c) < 0)
/* 151 */           { buf.append(c); continue; }  break;
/*     */       } 
/* 153 */     }  putBack();
/* 154 */     this.other = buf.toString().toLowerCase();
/* 155 */     return 4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void otherProc() {
/* 163 */     if (this.other.equals("odd") || this.other.equals("o")) {
/* 164 */       this.odd = true;
/* 165 */       this.even = false;
/*     */     }
/* 167 */     else if (this.other.equals("even") || this.other.equals("e")) {
/* 168 */       this.odd = false;
/* 169 */       this.even = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean getAttributes() {
/* 174 */     this.low = -1;
/* 175 */     this.high = -1;
/* 176 */     this.odd = this.even = this.inverse = false;
/* 177 */     int state = 2;
/*     */     while (true) {
/* 179 */       int type = getType();
/* 180 */       if (type == 6 || type == 1) {
/* 181 */         if (state == 1)
/* 182 */           this.high = this.low; 
/* 183 */         return (type == 6);
/*     */       } 
/* 185 */       switch (state) {
/*     */         case 2:
/* 187 */           switch (type) {
/*     */             case 3:
/* 189 */               this.inverse = true;
/*     */               continue;
/*     */             case 2:
/* 192 */               state = 3;
/*     */               continue;
/*     */           } 
/* 195 */           if (type == 5) {
/* 196 */             this.low = this.number;
/* 197 */             state = 1;
/*     */             continue;
/*     */           } 
/* 200 */           otherProc();
/*     */ 
/*     */ 
/*     */         
/*     */         case 1:
/* 205 */           switch (type) {
/*     */             case 3:
/* 207 */               this.inverse = true;
/* 208 */               state = 2;
/* 209 */               this.high = this.low;
/*     */               continue;
/*     */             case 2:
/* 212 */               state = 3;
/*     */               continue;
/*     */           } 
/* 215 */           this.high = this.low;
/* 216 */           state = 2;
/* 217 */           otherProc();
/*     */ 
/*     */ 
/*     */         
/*     */         case 3:
/* 222 */           switch (type) {
/*     */             case 3:
/* 224 */               this.inverse = true;
/* 225 */               state = 2;
/*     */               continue;
/*     */             case 2:
/*     */               continue;
/*     */             case 5:
/* 230 */               this.high = this.number;
/* 231 */               state = 2;
/*     */               continue;
/*     */           } 
/* 234 */           state = 2;
/* 235 */           otherProc();
/*     */       } 
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
/*     */   public static List<Integer> expand(String ranges, int maxNumber) {
/* 250 */     SequenceList parse = new SequenceList(ranges);
/* 251 */     LinkedList<Integer> list = new LinkedList<Integer>();
/* 252 */     boolean sair = false;
/* 253 */     while (!sair) {
/* 254 */       sair = parse.getAttributes();
/* 255 */       if (parse.low == -1 && parse.high == -1 && !parse.even && !parse.odd)
/*     */         continue; 
/* 257 */       if (parse.low < 1)
/* 258 */         parse.low = 1; 
/* 259 */       if (parse.high < 1 || parse.high > maxNumber)
/* 260 */         parse.high = maxNumber; 
/* 261 */       if (parse.low > maxNumber) {
/* 262 */         parse.low = maxNumber;
/*     */       }
/*     */       
/* 265 */       int inc = 1;
/* 266 */       if (parse.inverse) {
/* 267 */         if (parse.low > parse.high) {
/* 268 */           int t = parse.low;
/* 269 */           parse.low = parse.high;
/* 270 */           parse.high = t;
/*     */         } 
/* 272 */         for (ListIterator<Integer> it = list.listIterator(); it.hasNext(); ) {
/* 273 */           int n = ((Integer)it.next()).intValue();
/* 274 */           if (parse.even && (n & 0x1) == 1)
/*     */             continue; 
/* 276 */           if (parse.odd && (n & 0x1) == 0)
/*     */             continue; 
/* 278 */           if (n >= parse.low && n <= parse.high)
/* 279 */             it.remove(); 
/*     */         } 
/*     */         continue;
/*     */       } 
/* 283 */       if (parse.low > parse.high) {
/* 284 */         inc = -1;
/* 285 */         if (parse.odd || parse.even) {
/* 286 */           inc--;
/* 287 */           if (parse.even) {
/* 288 */             parse.low &= 0xFFFFFFFE;
/*     */           } else {
/* 290 */             parse.low -= ((parse.low & 0x1) == 1) ? 0 : 1;
/*     */           } 
/* 292 */         }  int i; for (i = parse.low; i >= parse.high; i += inc)
/* 293 */           list.add(Integer.valueOf(i)); 
/*     */         continue;
/*     */       } 
/* 296 */       if (parse.odd || parse.even) {
/* 297 */         inc++;
/* 298 */         if (parse.odd) {
/* 299 */           parse.low |= 0x1;
/*     */         } else {
/* 301 */           parse.low += ((parse.low & 0x1) == 1) ? 1 : 0;
/*     */         } 
/* 303 */       }  int k; for (k = parse.low; k <= parse.high; k += inc) {
/* 304 */         list.add(Integer.valueOf(k));
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     return list;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/SequenceList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */