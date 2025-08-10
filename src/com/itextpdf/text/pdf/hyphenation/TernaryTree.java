/*     */ package com.itextpdf.text.pdf.hyphenation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Stack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TernaryTree
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5313366505322983510L;
/*     */   protected char[] lo;
/*     */   protected char[] hi;
/*     */   protected char[] eq;
/*     */   protected char[] sc;
/*     */   protected CharVector kv;
/*     */   protected char root;
/*     */   protected char freenode;
/*     */   protected int length;
/*     */   protected static final int BLOCK_SIZE = 2048;
/*     */   
/*     */   TernaryTree() {
/* 116 */     init();
/*     */   }
/*     */   
/*     */   protected void init() {
/* 120 */     this.root = Character.MIN_VALUE;
/* 121 */     this.freenode = '\001';
/* 122 */     this.length = 0;
/* 123 */     this.lo = new char[2048];
/* 124 */     this.hi = new char[2048];
/* 125 */     this.eq = new char[2048];
/* 126 */     this.sc = new char[2048];
/* 127 */     this.kv = new CharVector();
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
/*     */   public void insert(String key, char val) {
/* 140 */     int len = key.length() + 1;
/*     */     
/* 142 */     if (this.freenode + len > this.eq.length) {
/* 143 */       redimNodeArrays(this.eq.length + 2048);
/*     */     }
/* 145 */     char[] strkey = new char[len--];
/* 146 */     key.getChars(0, len, strkey, 0);
/* 147 */     strkey[len] = Character.MIN_VALUE;
/* 148 */     this.root = insert(this.root, strkey, 0, val);
/*     */   }
/*     */   
/*     */   public void insert(char[] key, int start, char val) {
/* 152 */     int len = strlen(key) + 1;
/* 153 */     if (this.freenode + len > this.eq.length) {
/* 154 */       redimNodeArrays(this.eq.length + 2048);
/*     */     }
/* 156 */     this.root = insert(this.root, key, start, val);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private char insert(char p, char[] key, int start, char val) {
/* 163 */     int len = strlen(key, start);
/* 164 */     if (p == '\000') {
/*     */ 
/*     */ 
/*     */       
/* 168 */       p = this.freenode = (char)(this.freenode + 1);
/* 169 */       this.eq[p] = val;
/* 170 */       this.length++;
/* 171 */       this.hi[p] = Character.MIN_VALUE;
/* 172 */       if (len > 0) {
/* 173 */         this.sc[p] = Character.MAX_VALUE;
/* 174 */         this.lo[p] = (char)this.kv.alloc(len + 1);
/*     */         
/* 176 */         strcpy(this.kv.getArray(), this.lo[p], key, start);
/*     */       } else {
/* 178 */         this.sc[p] = Character.MIN_VALUE;
/* 179 */         this.lo[p] = Character.MIN_VALUE;
/*     */       } 
/* 181 */       return p;
/*     */     } 
/*     */     
/* 184 */     if (this.sc[p] == Character.MAX_VALUE) {
/*     */ 
/*     */ 
/*     */       
/* 188 */       char pp = this.freenode = (char)(this.freenode + 1);
/* 189 */       this.lo[pp] = this.lo[p];
/* 190 */       this.eq[pp] = this.eq[p];
/* 191 */       this.lo[p] = Character.MIN_VALUE;
/* 192 */       if (len > 0) {
/* 193 */         this.sc[p] = this.kv.get(this.lo[pp]);
/* 194 */         this.eq[p] = pp;
/* 195 */         this.lo[pp] = (char)(this.lo[pp] + 1);
/* 196 */         if (this.kv.get(this.lo[pp]) == '\000') {
/*     */           
/* 198 */           this.lo[pp] = Character.MIN_VALUE;
/* 199 */           this.sc[pp] = Character.MIN_VALUE;
/* 200 */           this.hi[pp] = Character.MIN_VALUE;
/*     */         } else {
/*     */           
/* 203 */           this.sc[pp] = Character.MAX_VALUE;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 208 */         this.sc[pp] = Character.MAX_VALUE;
/* 209 */         this.hi[p] = pp;
/* 210 */         this.sc[p] = Character.MIN_VALUE;
/* 211 */         this.eq[p] = val;
/* 212 */         this.length++;
/* 213 */         return p;
/*     */       } 
/*     */     } 
/* 216 */     char s = key[start];
/* 217 */     if (s < this.sc[p]) {
/* 218 */       this.lo[p] = insert(this.lo[p], key, start, val);
/* 219 */     } else if (s == this.sc[p]) {
/* 220 */       if (s != '\000') {
/* 221 */         this.eq[p] = insert(this.eq[p], key, start + 1, val);
/*     */       } else {
/*     */         
/* 224 */         this.eq[p] = val;
/*     */       } 
/*     */     } else {
/* 227 */       this.hi[p] = insert(this.hi[p], key, start, val);
/*     */     } 
/* 229 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int strcmp(char[] a, int startA, char[] b, int startB) {
/* 236 */     for (; a[startA] == b[startB]; startA++, startB++) {
/* 237 */       if (a[startA] == '\000') {
/* 238 */         return 0;
/*     */       }
/*     */     } 
/* 241 */     return a[startA] - b[startB];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int strcmp(String str, char[] a, int start) {
/* 248 */     int len = str.length(); int i;
/* 249 */     for (i = 0; i < len; i++) {
/* 250 */       int d = str.charAt(i) - a[start + i];
/* 251 */       if (d != 0) {
/* 252 */         return d;
/*     */       }
/* 254 */       if (a[start + i] == '\000') {
/* 255 */         return d;
/*     */       }
/*     */     } 
/* 258 */     if (a[start + i] != '\000') {
/* 259 */       return -a[start + i];
/*     */     }
/* 261 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void strcpy(char[] dst, int di, char[] src, int si) {
/* 266 */     while (src[si] != '\000') {
/* 267 */       dst[di++] = src[si++];
/*     */     }
/* 269 */     dst[di] = Character.MIN_VALUE;
/*     */   }
/*     */   
/*     */   public static int strlen(char[] a, int start) {
/* 273 */     int len = 0;
/* 274 */     for (int i = start; i < a.length && a[i] != '\000'; i++) {
/* 275 */       len++;
/*     */     }
/* 277 */     return len;
/*     */   }
/*     */   
/*     */   public static int strlen(char[] a) {
/* 281 */     return strlen(a, 0);
/*     */   }
/*     */   
/*     */   public int find(String key) {
/* 285 */     int len = key.length();
/* 286 */     char[] strkey = new char[len + 1];
/* 287 */     key.getChars(0, len, strkey, 0);
/* 288 */     strkey[len] = Character.MIN_VALUE;
/*     */     
/* 290 */     return find(strkey, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int find(char[] key, int start) {
/* 295 */     char p = this.root;
/* 296 */     int i = start;
/*     */ 
/*     */     
/* 299 */     while (p != '\000') {
/* 300 */       if (this.sc[p] == Character.MAX_VALUE) {
/* 301 */         if (strcmp(key, i, this.kv.getArray(), this.lo[p]) == 0) {
/* 302 */           return this.eq[p];
/*     */         }
/* 304 */         return -1;
/*     */       } 
/*     */       
/* 307 */       char c = key[i];
/* 308 */       int d = c - this.sc[p];
/* 309 */       if (d == 0) {
/* 310 */         if (c == '\000') {
/* 311 */           return this.eq[p];
/*     */         }
/* 313 */         i++;
/* 314 */         p = this.eq[p]; continue;
/* 315 */       }  if (d < 0) {
/* 316 */         p = this.lo[p]; continue;
/*     */       } 
/* 318 */       p = this.hi[p];
/*     */     } 
/*     */     
/* 321 */     return -1;
/*     */   }
/*     */   
/*     */   public boolean knows(String key) {
/* 325 */     return (find(key) >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private void redimNodeArrays(int newsize) {
/* 330 */     int len = (newsize < this.lo.length) ? newsize : this.lo.length;
/* 331 */     char[] na = new char[newsize];
/* 332 */     System.arraycopy(this.lo, 0, na, 0, len);
/* 333 */     this.lo = na;
/* 334 */     na = new char[newsize];
/* 335 */     System.arraycopy(this.hi, 0, na, 0, len);
/* 336 */     this.hi = na;
/* 337 */     na = new char[newsize];
/* 338 */     System.arraycopy(this.eq, 0, na, 0, len);
/* 339 */     this.eq = na;
/* 340 */     na = new char[newsize];
/* 341 */     System.arraycopy(this.sc, 0, na, 0, len);
/* 342 */     this.sc = na;
/*     */   }
/*     */   
/*     */   public int size() {
/* 346 */     return this.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 351 */     TernaryTree t = new TernaryTree();
/* 352 */     t.lo = (char[])this.lo.clone();
/* 353 */     t.hi = (char[])this.hi.clone();
/* 354 */     t.eq = (char[])this.eq.clone();
/* 355 */     t.sc = (char[])this.sc.clone();
/* 356 */     t.kv = (CharVector)this.kv.clone();
/* 357 */     t.root = this.root;
/* 358 */     t.freenode = this.freenode;
/* 359 */     t.length = this.length;
/*     */     
/* 361 */     return t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void insertBalanced(String[] k, char[] v, int offset, int n) {
/* 372 */     if (n < 1) {
/*     */       return;
/*     */     }
/* 375 */     int m = n >> 1;
/*     */     
/* 377 */     insert(k[m + offset], v[m + offset]);
/* 378 */     insertBalanced(k, v, offset, m);
/*     */     
/* 380 */     insertBalanced(k, v, offset + m + 1, n - m - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void balance() {
/* 390 */     int i = 0, n = this.length;
/* 391 */     String[] k = new String[n];
/* 392 */     char[] v = new char[n];
/* 393 */     Iterator iter = new Iterator();
/* 394 */     while (iter.hasMoreElements()) {
/* 395 */       v[i] = iter.getValue();
/* 396 */       k[i++] = iter.nextElement();
/*     */     } 
/* 398 */     init();
/* 399 */     insertBalanced(k, v, 0, n);
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
/*     */   public void trimToSize() {
/* 420 */     balance();
/*     */ 
/*     */     
/* 423 */     redimNodeArrays(this.freenode);
/*     */ 
/*     */     
/* 426 */     CharVector kx = new CharVector();
/* 427 */     kx.alloc(1);
/* 428 */     TernaryTree map = new TernaryTree();
/* 429 */     compact(kx, map, this.root);
/* 430 */     this.kv = kx;
/* 431 */     this.kv.trimToSize();
/*     */   }
/*     */ 
/*     */   
/*     */   private void compact(CharVector kx, TernaryTree map, char p) {
/* 436 */     if (p == '\000') {
/*     */       return;
/*     */     }
/* 439 */     if (this.sc[p] == Character.MAX_VALUE) {
/* 440 */       int k = map.find(this.kv.getArray(), this.lo[p]);
/* 441 */       if (k < 0) {
/* 442 */         k = kx.alloc(strlen(this.kv.getArray(), this.lo[p]) + 1);
/* 443 */         strcpy(kx.getArray(), k, this.kv.getArray(), this.lo[p]);
/* 444 */         map.insert(kx.getArray(), k, (char)k);
/*     */       } 
/* 446 */       this.lo[p] = (char)k;
/*     */     } else {
/* 448 */       compact(kx, map, this.lo[p]);
/* 449 */       if (this.sc[p] != '\000') {
/* 450 */         compact(kx, map, this.eq[p]);
/*     */       }
/* 452 */       compact(kx, map, this.hi[p]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration<String> keys() {
/* 458 */     return new Iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public class Iterator
/*     */     implements Enumeration<String>
/*     */   {
/*     */     int cur;
/*     */     
/*     */     String curkey;
/*     */     Stack<Item> ns;
/*     */     StringBuffer ks;
/*     */     
/*     */     private class Item
/*     */       implements Cloneable
/*     */     {
/*     */       char parent;
/*     */       char child;
/*     */       
/*     */       public Item() {
/* 478 */         this.parent = Character.MIN_VALUE;
/* 479 */         this.child = Character.MIN_VALUE;
/*     */       }
/*     */       
/*     */       public Item(char p, char c) {
/* 483 */         this.parent = p;
/* 484 */         this.child = c;
/*     */       }
/*     */ 
/*     */       
/*     */       public Item clone() {
/* 489 */         return new Item(this.parent, this.child);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator() {
/* 505 */       this.cur = -1;
/* 506 */       this.ns = new Stack<Item>();
/* 507 */       this.ks = new StringBuffer();
/* 508 */       rewind();
/*     */     }
/*     */     
/*     */     public void rewind() {
/* 512 */       this.ns.removeAllElements();
/* 513 */       this.ks.setLength(0);
/* 514 */       this.cur = TernaryTree.this.root;
/* 515 */       run();
/*     */     }
/*     */     
/*     */     public String nextElement() {
/* 519 */       String res = this.curkey;
/* 520 */       this.cur = up();
/* 521 */       run();
/* 522 */       return res;
/*     */     }
/*     */     
/*     */     public char getValue() {
/* 526 */       if (this.cur >= 0) {
/* 527 */         return TernaryTree.this.eq[this.cur];
/*     */       }
/* 529 */       return Character.MIN_VALUE;
/*     */     }
/*     */     
/*     */     public boolean hasMoreElements() {
/* 533 */       return (this.cur != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int up() {
/* 540 */       Item i = new Item();
/* 541 */       int res = 0;
/*     */       
/* 543 */       if (this.ns.empty()) {
/* 544 */         return -1;
/*     */       }
/*     */       
/* 547 */       if (this.cur != 0 && TernaryTree.this.sc[this.cur] == '\000') {
/* 548 */         return TernaryTree.this.lo[this.cur];
/*     */       }
/*     */       
/* 551 */       boolean climb = true;
/*     */       
/* 553 */       while (climb) {
/* 554 */         i = this.ns.pop();
/* 555 */         i.child = (char)(i.child + 1);
/* 556 */         switch (i.child) {
/*     */           case '\001':
/* 558 */             if (TernaryTree.this.sc[i.parent] != '\000') {
/* 559 */               res = TernaryTree.this.eq[i.parent];
/* 560 */               this.ns.push(i.clone());
/* 561 */               this.ks.append(TernaryTree.this.sc[i.parent]);
/*     */             } else {
/* 563 */               i.child = (char)(i.child + 1);
/* 564 */               this.ns.push(i.clone());
/* 565 */               res = TernaryTree.this.hi[i.parent];
/*     */             } 
/* 567 */             climb = false;
/*     */             continue;
/*     */           
/*     */           case '\002':
/* 571 */             res = TernaryTree.this.hi[i.parent];
/* 572 */             this.ns.push(i.clone());
/* 573 */             if (this.ks.length() > 0) {
/* 574 */               this.ks.setLength(this.ks.length() - 1);
/*     */             }
/* 576 */             climb = false;
/*     */             continue;
/*     */         } 
/*     */         
/* 580 */         if (this.ns.empty()) {
/* 581 */           return -1;
/*     */         }
/* 583 */         climb = true;
/*     */       } 
/*     */ 
/*     */       
/* 587 */       return res;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int run() {
/* 594 */       if (this.cur == -1) {
/* 595 */         return -1;
/*     */       }
/*     */       
/* 598 */       boolean leaf = false;
/*     */       
/*     */       while (true) {
/* 601 */         if (this.cur != 0)
/* 602 */           if (TernaryTree.this.sc[this.cur] == Character.MAX_VALUE) {
/* 603 */             leaf = true;
/*     */           } else {
/*     */             
/* 606 */             this.ns.push(new Item((char)this.cur, false));
/* 607 */             if (TernaryTree.this.sc[this.cur] == '\000') {
/* 608 */               leaf = true;
/*     */             } else {
/*     */               
/* 611 */               this.cur = TernaryTree.this.lo[this.cur]; continue;
/*     */             } 
/* 613 */           }   if (leaf) {
/*     */           break;
/*     */         }
/*     */         
/* 617 */         this.cur = up();
/* 618 */         if (this.cur == -1) {
/* 619 */           return -1;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 624 */       StringBuffer buf = new StringBuffer(this.ks.toString());
/* 625 */       if (TernaryTree.this.sc[this.cur] == Character.MAX_VALUE) {
/* 626 */         int p = TernaryTree.this.lo[this.cur];
/* 627 */         while (TernaryTree.this.kv.get(p) != '\000') {
/* 628 */           buf.append(TernaryTree.this.kv.get(p++));
/*     */         }
/*     */       } 
/* 631 */       this.curkey = buf.toString();
/* 632 */       return 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStats() {
/* 638 */     System.out.println("Number of keys = " + Integer.toString(this.length));
/* 639 */     System.out.println("Node count = " + Integer.toString(this.freenode));
/*     */     
/* 641 */     System.out.println("Key Array length = " + 
/* 642 */         Integer.toString(this.kv.length()));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/hyphenation/TernaryTree.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */