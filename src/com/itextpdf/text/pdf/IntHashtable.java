/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IntHashtable
/*     */   implements Cloneable
/*     */ {
/*     */   private transient Entry[] table;
/*     */   private transient int count;
/*     */   private int threshold;
/*     */   private float loadFactor;
/*     */   
/*     */   public IntHashtable() {
/*  84 */     this(150, 0.75F);
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
/*     */   public IntHashtable(int initialCapacity) {
/*  96 */     this(initialCapacity, 0.75F);
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
/*     */   public IntHashtable(int initialCapacity, float loadFactor) {
/* 110 */     if (initialCapacity < 0) {
/* 111 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.capacity.1", initialCapacity));
/*     */     }
/* 113 */     if (loadFactor <= 0.0F) {
/* 114 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.load.1", new Object[] { String.valueOf(loadFactor) }));
/*     */     }
/* 116 */     if (initialCapacity == 0) {
/* 117 */       initialCapacity = 1;
/*     */     }
/* 119 */     this.loadFactor = loadFactor;
/* 120 */     this.table = new Entry[initialCapacity];
/* 121 */     this.threshold = (int)(initialCapacity * loadFactor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 130 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 140 */     return (this.count == 0);
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
/*     */   public boolean contains(int value) {
/* 163 */     Entry[] tab = this.table;
/* 164 */     for (int i = tab.length; i-- > 0;) {
/* 165 */       for (Entry e = tab[i]; e != null; e = e.next) {
/* 166 */         if (e.value == value) {
/* 167 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 171 */     return false;
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
/*     */   public boolean containsValue(int value) {
/* 187 */     return contains(value);
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
/*     */   public boolean containsKey(int key) {
/* 200 */     Entry[] tab = this.table;
/* 201 */     int hash = key;
/* 202 */     int index = (hash & Integer.MAX_VALUE) % tab.length;
/* 203 */     for (Entry e = tab[index]; e != null; e = e.next) {
/* 204 */       if (e.hash == hash && e.key == key) {
/* 205 */         return true;
/*     */       }
/*     */     } 
/* 208 */     return false;
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
/*     */   public int get(int key) {
/* 221 */     Entry[] tab = this.table;
/* 222 */     int hash = key;
/* 223 */     int index = (hash & Integer.MAX_VALUE) % tab.length;
/* 224 */     for (Entry e = tab[index]; e != null; e = e.next) {
/* 225 */       if (e.hash == hash && e.key == key) {
/* 226 */         return e.value;
/*     */       }
/*     */     } 
/* 229 */     return 0;
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
/*     */   protected void rehash() {
/* 263 */     int oldCapacity = this.table.length;
/* 264 */     Entry[] oldMap = this.table;
/*     */     
/* 266 */     int newCapacity = oldCapacity * 2 + 1;
/* 267 */     Entry[] newMap = new Entry[newCapacity];
/*     */     
/* 269 */     this.threshold = (int)(newCapacity * this.loadFactor);
/* 270 */     this.table = newMap;
/*     */     
/* 272 */     for (int i = oldCapacity; i-- > 0;) {
/* 273 */       for (Entry old = oldMap[i]; old != null; ) {
/* 274 */         Entry e = old;
/* 275 */         old = old.next;
/*     */         
/* 277 */         int index = (e.hash & Integer.MAX_VALUE) % newCapacity;
/* 278 */         e.next = newMap[index];
/* 279 */         newMap[index] = e;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int put(int key, int value) {
/* 301 */     Entry[] tab = this.table;
/* 302 */     int hash = key;
/* 303 */     int index = (hash & Integer.MAX_VALUE) % tab.length; Entry e;
/* 304 */     for (e = tab[index]; e != null; e = e.next) {
/* 305 */       if (e.hash == hash && e.key == key) {
/* 306 */         int old = e.value;
/*     */         
/* 308 */         e.value = value;
/* 309 */         return old;
/*     */       } 
/*     */     } 
/*     */     
/* 313 */     if (this.count >= this.threshold) {
/*     */       
/* 315 */       rehash();
/*     */       
/* 317 */       tab = this.table;
/* 318 */       index = (hash & Integer.MAX_VALUE) % tab.length;
/*     */     } 
/*     */ 
/*     */     
/* 322 */     e = new Entry(hash, key, value, tab[index]);
/* 323 */     tab[index] = e;
/* 324 */     this.count++;
/* 325 */     return 0;
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
/*     */   public int remove(int key) {
/* 340 */     Entry[] tab = this.table;
/* 341 */     int hash = key;
/* 342 */     int index = (hash & Integer.MAX_VALUE) % tab.length;
/* 343 */     for (Entry e = tab[index], prev = null; e != null; prev = e, e = e.next) {
/* 344 */       if (e.hash == hash && e.key == key) {
/* 345 */         if (prev != null) {
/* 346 */           prev.next = e.next;
/*     */         } else {
/* 348 */           tab[index] = e.next;
/*     */         } 
/* 350 */         this.count--;
/* 351 */         int oldValue = e.value;
/* 352 */         e.value = 0;
/* 353 */         return oldValue;
/*     */       } 
/*     */     } 
/* 356 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 363 */     Entry[] tab = this.table;
/* 364 */     for (int index = tab.length; --index >= 0;) {
/* 365 */       tab[index] = null;
/*     */     }
/* 367 */     this.count = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Entry
/*     */   {
/*     */     int hash;
/*     */ 
/*     */ 
/*     */     
/*     */     int key;
/*     */ 
/*     */     
/*     */     int value;
/*     */ 
/*     */     
/*     */     Entry next;
/*     */ 
/*     */ 
/*     */     
/*     */     protected Entry(int hash, int key, int value, Entry next) {
/* 390 */       this.hash = hash;
/* 391 */       this.key = key;
/* 392 */       this.value = value;
/* 393 */       this.next = next;
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
/*     */     public int getKey() {
/* 408 */       return this.key;
/*     */     }
/*     */     public int getValue() {
/* 411 */       return this.value;
/*     */     }
/*     */     
/*     */     protected Object clone() {
/* 415 */       Entry entry = new Entry(this.hash, this.key, this.value, (this.next != null) ? (Entry)this.next.clone() : null);
/* 416 */       return entry;
/*     */     }
/*     */   }
/*     */   
/*     */   static class IntHashtableIterator
/*     */     implements Iterator<Entry> {
/*     */     int index;
/*     */     IntHashtable.Entry[] table;
/*     */     IntHashtable.Entry entry;
/*     */     
/*     */     IntHashtableIterator(IntHashtable.Entry[] table) {
/* 427 */       this.table = table;
/* 428 */       this.index = table.length;
/*     */     }
/*     */     public boolean hasNext() {
/* 431 */       if (this.entry != null) {
/* 432 */         return true;
/*     */       }
/* 434 */       while (this.index-- > 0) {
/* 435 */         if ((this.entry = this.table[this.index]) != null) {
/* 436 */           return true;
/*     */         }
/*     */       } 
/* 439 */       return false;
/*     */     }
/*     */     
/*     */     public IntHashtable.Entry next() {
/* 443 */       if (this.entry == null) {
/* 444 */         while (this.index-- > 0 && (this.entry = this.table[this.index]) == null);
/*     */       }
/* 446 */       if (this.entry != null) {
/* 447 */         IntHashtable.Entry e = this.entry;
/* 448 */         this.entry = e.next;
/* 449 */         return e;
/*     */       } 
/* 451 */       throw new NoSuchElementException(MessageLocalization.getComposedMessage("inthashtableiterator", new Object[0]));
/*     */     }
/*     */     public void remove() {
/* 454 */       throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("remove.not.supported", new Object[0]));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Entry> getEntryIterator() {
/* 461 */     return new IntHashtableIterator(this.table);
/*     */   }
/*     */   
/*     */   public int[] toOrderedKeys() {
/* 465 */     int[] res = getKeys();
/* 466 */     Arrays.sort(res);
/* 467 */     return res;
/*     */   }
/*     */   
/*     */   public int[] getKeys() {
/* 471 */     int[] res = new int[this.count];
/* 472 */     int ptr = 0;
/* 473 */     int index = this.table.length;
/* 474 */     Entry entry = null;
/*     */     while (true) {
/* 476 */       if (entry == null)
/* 477 */         while (index-- > 0 && (entry = this.table[index]) == null); 
/* 478 */       if (entry == null)
/*     */         break; 
/* 480 */       Entry e = entry;
/* 481 */       entry = e.next;
/* 482 */       res[ptr++] = e.key;
/*     */     } 
/* 484 */     return res;
/*     */   }
/*     */   
/*     */   public int getOneKey() {
/* 488 */     if (this.count == 0)
/* 489 */       return 0; 
/* 490 */     int index = this.table.length;
/* 491 */     Entry entry = null;
/* 492 */     while (index-- > 0 && (entry = this.table[index]) == null);
/* 493 */     if (entry == null)
/* 494 */       return 0; 
/* 495 */     return entry.key;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 501 */       IntHashtable t = (IntHashtable)super.clone();
/* 502 */       t.table = new Entry[this.table.length];
/* 503 */       for (int i = this.table.length; i-- > 0;) {
/* 504 */         t.table[i] = (this.table[i] != null) ? (Entry)this.table[i]
/* 505 */           .clone() : null;
/*     */       }
/* 507 */       return t;
/* 508 */     } catch (CloneNotSupportedException e) {
/*     */       
/* 510 */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/IntHashtable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */