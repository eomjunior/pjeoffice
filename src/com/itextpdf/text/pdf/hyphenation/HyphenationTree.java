/*     */ package com.itextpdf.text.pdf.hyphenation;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
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
/*     */ public class HyphenationTree
/*     */   extends TernaryTree
/*     */   implements PatternConsumer
/*     */ {
/*     */   private static final long serialVersionUID = -7763254239309429432L;
/*     */   protected ByteVector vspace;
/*     */   protected HashMap<String, ArrayList<Object>> stoplist;
/*     */   protected TernaryTree classmap;
/*     */   private transient TernaryTree ivalues;
/*     */   
/*     */   public HyphenationTree() {
/*  57 */     this.stoplist = new HashMap<String, ArrayList<Object>>(23);
/*  58 */     this.classmap = new TernaryTree();
/*  59 */     this.vspace = new ByteVector();
/*  60 */     this.vspace.alloc(1);
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
/*     */   protected int packValues(String values) {
/*  73 */     int n = values.length();
/*  74 */     int m = ((n & 0x1) == 1) ? ((n >> 1) + 2) : ((n >> 1) + 1);
/*  75 */     int offset = this.vspace.alloc(m);
/*  76 */     byte[] va = this.vspace.getArray();
/*  77 */     for (int i = 0; i < n; i++) {
/*  78 */       int j = i >> 1;
/*  79 */       byte v = (byte)(values.charAt(i) - 48 + 1 & 0xF);
/*  80 */       if ((i & 0x1) == 1) {
/*  81 */         va[j + offset] = (byte)(va[j + offset] | v);
/*     */       } else {
/*  83 */         va[j + offset] = (byte)(v << 4);
/*     */       } 
/*     */     } 
/*  86 */     va[m - 1 + offset] = 0;
/*  87 */     return offset;
/*     */   }
/*     */   
/*     */   protected String unpackValues(int k) {
/*  91 */     StringBuffer buf = new StringBuffer();
/*  92 */     byte v = this.vspace.get(k++);
/*  93 */     while (v != 0) {
/*  94 */       char c = (char)((v >>> 4) - 1 + 48);
/*  95 */       buf.append(c);
/*  96 */       c = (char)(v & 0xF);
/*  97 */       if (c == '\000') {
/*     */         break;
/*     */       }
/* 100 */       c = (char)(c - 1 + 48);
/* 101 */       buf.append(c);
/* 102 */       v = this.vspace.get(k++);
/*     */     } 
/* 104 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public void loadSimplePatterns(InputStream stream) {
/* 108 */     SimplePatternParser pp = new SimplePatternParser();
/* 109 */     this.ivalues = new TernaryTree();
/*     */     
/* 111 */     pp.parse(stream, this);
/*     */ 
/*     */ 
/*     */     
/* 115 */     trimToSize();
/* 116 */     this.vspace.trimToSize();
/* 117 */     this.classmap.trimToSize();
/*     */ 
/*     */     
/* 120 */     this.ivalues = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String findPattern(String pat) {
/* 125 */     int k = find(pat);
/* 126 */     if (k >= 0) {
/* 127 */       return unpackValues(k);
/*     */     }
/* 129 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int hstrcmp(char[] s, int si, char[] t, int ti) {
/* 137 */     for (; s[si] == t[ti]; si++, ti++) {
/* 138 */       if (s[si] == '\000') {
/* 139 */         return 0;
/*     */       }
/*     */     } 
/* 142 */     if (t[ti] == '\000') {
/* 143 */       return 0;
/*     */     }
/* 145 */     return s[si] - t[ti];
/*     */   }
/*     */   
/*     */   protected byte[] getValues(int k) {
/* 149 */     StringBuffer buf = new StringBuffer();
/* 150 */     byte v = this.vspace.get(k++);
/* 151 */     while (v != 0) {
/* 152 */       char c = (char)((v >>> 4) - 1);
/* 153 */       buf.append(c);
/* 154 */       c = (char)(v & 0xF);
/* 155 */       if (c == '\000') {
/*     */         break;
/*     */       }
/* 158 */       c = (char)(c - 1);
/* 159 */       buf.append(c);
/* 160 */       v = this.vspace.get(k++);
/*     */     } 
/* 162 */     byte[] res = new byte[buf.length()];
/* 163 */     for (int i = 0; i < res.length; i++) {
/* 164 */       res[i] = (byte)buf.charAt(i);
/*     */     }
/* 166 */     return res;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void searchPatterns(char[] word, int index, byte[] il) {
/* 195 */     int i = index;
/*     */     
/* 197 */     char sp = word[i];
/* 198 */     char p = this.root;
/*     */     
/* 200 */     while (p > '\000' && p < this.sc.length) {
/* 201 */       if (this.sc[p] == Character.MAX_VALUE) {
/* 202 */         if (hstrcmp(word, i, this.kv.getArray(), this.lo[p]) == 0) {
/* 203 */           byte[] values = getValues(this.eq[p]);
/* 204 */           int j = index;
/* 205 */           for (byte value : values) {
/* 206 */             if (j < il.length && value > il[j]) {
/* 207 */               il[j] = value;
/*     */             }
/* 209 */             j++;
/*     */           } 
/*     */         } 
/*     */         return;
/*     */       } 
/* 214 */       int d = sp - this.sc[p];
/* 215 */       if (d == 0) {
/* 216 */         if (sp == '\000') {
/*     */           break;
/*     */         }
/* 219 */         sp = word[++i];
/* 220 */         p = this.eq[p];
/* 221 */         char q = p;
/*     */ 
/*     */ 
/*     */         
/* 225 */         while (q > '\000' && q < this.sc.length && 
/* 226 */           this.sc[q] != Character.MAX_VALUE) {
/*     */ 
/*     */           
/* 229 */           if (this.sc[q] == '\000') {
/* 230 */             byte[] values = getValues(this.eq[q]);
/* 231 */             int j = index;
/* 232 */             for (byte value : values) {
/* 233 */               if (j < il.length && value > il[j]) {
/* 234 */                 il[j] = value;
/*     */               }
/* 236 */               j++;
/*     */             } 
/*     */             break;
/*     */           } 
/* 240 */           q = this.lo[q];
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 250 */       p = (d < 0) ? this.lo[p] : this.hi[p];
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
/*     */   public Hyphenation hyphenate(String word, int remainCharCount, int pushCharCount) {
/* 267 */     char[] w = word.toCharArray();
/* 268 */     return hyphenate(w, 0, w.length, remainCharCount, pushCharCount);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Hyphenation hyphenate(char[] w, int offset, int len, int remainCharCount, int pushCharCount) {
/* 309 */     char[] word = new char[len + 3];
/*     */ 
/*     */     
/* 312 */     char[] c = new char[2];
/* 313 */     int iIgnoreAtBeginning = 0;
/* 314 */     int iLength = len;
/* 315 */     boolean bEndOfLetters = false; int i;
/* 316 */     for (i = 1; i <= len; i++) {
/* 317 */       c[0] = w[offset + i - 1];
/* 318 */       int nc = this.classmap.find(c, 0);
/* 319 */       if (nc < 0) {
/* 320 */         if (i == 1 + iIgnoreAtBeginning) {
/*     */           
/* 322 */           iIgnoreAtBeginning++;
/*     */         } else {
/*     */           
/* 325 */           bEndOfLetters = true;
/*     */         } 
/* 327 */         iLength--;
/*     */       }
/* 329 */       else if (!bEndOfLetters) {
/* 330 */         word[i - iIgnoreAtBeginning] = (char)nc;
/*     */       } else {
/* 332 */         return null;
/*     */       } 
/*     */     } 
/*     */     
/* 336 */     int origlen = len;
/* 337 */     len = iLength;
/* 338 */     if (len < remainCharCount + pushCharCount)
/*     */     {
/* 340 */       return null;
/*     */     }
/* 342 */     int[] result = new int[len + 1];
/* 343 */     int k = 0;
/*     */ 
/*     */     
/* 346 */     String sw = new String(word, 1, len);
/* 347 */     if (this.stoplist.containsKey(sw)) {
/*     */       
/* 349 */       ArrayList<Object> hw = this.stoplist.get(sw);
/* 350 */       int j = 0;
/* 351 */       for (i = 0; i < hw.size(); i++) {
/* 352 */         Object o = hw.get(i);
/*     */ 
/*     */         
/* 355 */         if (o instanceof String) {
/* 356 */           j += ((String)o).length();
/* 357 */           if (j >= remainCharCount && j < len - pushCharCount) {
/* 358 */             result[k++] = j + iIgnoreAtBeginning;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 364 */       word[0] = '.';
/* 365 */       word[len + 1] = '.';
/* 366 */       word[len + 2] = Character.MIN_VALUE;
/* 367 */       byte[] il = new byte[len + 3];
/* 368 */       for (i = 0; i < len + 1; i++) {
/* 369 */         searchPatterns(word, i, il);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 376 */       for (i = 0; i < len; i++) {
/* 377 */         if ((il[i + 1] & 0x1) == 1 && i >= remainCharCount && i <= len - pushCharCount)
/*     */         {
/* 379 */           result[k++] = i + iIgnoreAtBeginning;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 385 */     if (k > 0) {
/*     */       
/* 387 */       int[] res = new int[k];
/* 388 */       System.arraycopy(result, 0, res, 0, k);
/* 389 */       return new Hyphenation(new String(w, offset, origlen), res);
/*     */     } 
/* 391 */     return null;
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
/*     */   public void addClass(String chargroup) {
/* 408 */     if (chargroup.length() > 0) {
/* 409 */       char equivChar = chargroup.charAt(0);
/* 410 */       char[] key = new char[2];
/* 411 */       key[1] = Character.MIN_VALUE;
/* 412 */       for (int i = 0; i < chargroup.length(); i++) {
/* 413 */         key[0] = chargroup.charAt(i);
/* 414 */         this.classmap.insert(key, 0, equivChar);
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
/*     */   public void addException(String word, ArrayList<Object> hyphenatedword) {
/* 428 */     this.stoplist.put(word, hyphenatedword);
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
/*     */   public void addPattern(String pattern, String ivalue) {
/* 442 */     int k = this.ivalues.find(ivalue);
/* 443 */     if (k <= 0) {
/* 444 */       k = packValues(ivalue);
/* 445 */       this.ivalues.insert(ivalue, (char)k);
/*     */     } 
/* 447 */     insert(pattern, (char)k);
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStats() {
/* 452 */     System.out.println("Value space size = " + 
/* 453 */         Integer.toString(this.vspace.length()));
/* 454 */     super.printStats();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/hyphenation/HyphenationTree.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */