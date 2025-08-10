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
/*     */ public class LongHashtable
/*     */   implements Cloneable
/*     */ {
/*     */   private transient Entry[] table;
/*     */   private transient int count;
/*     */   private int threshold;
/*     */   private float loadFactor;
/*     */   
/*     */   public LongHashtable() {
/*  83 */     this(150, 0.75F);
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
/*     */   public LongHashtable(int initialCapacity) {
/*  95 */     this(initialCapacity, 0.75F);
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
/*     */   public LongHashtable(int initialCapacity, float loadFactor) {
/* 109 */     if (initialCapacity < 0) {
/* 110 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.capacity.1", initialCapacity));
/*     */     }
/* 112 */     if (loadFactor <= 0.0F) {
/* 113 */       throw new IllegalArgumentException(MessageLocalization.getComposedMessage("illegal.load.1", new Object[] { String.valueOf(loadFactor) }));
/*     */     }
/* 115 */     if (initialCapacity == 0) {
/* 116 */       initialCapacity = 1;
/*     */     }
/* 118 */     this.loadFactor = loadFactor;
/* 119 */     this.table = new Entry[initialCapacity];
/* 120 */     this.threshold = (int)(initialCapacity * loadFactor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 129 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 139 */     return (this.count == 0);
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
/*     */   public boolean contains(long value) {
/* 162 */     Entry[] tab = this.table;
/* 163 */     for (int i = tab.length; i-- > 0;) {
/* 164 */       for (Entry e = tab[i]; e != null; e = e.next) {
/* 165 */         if (e.value == value) {
/* 166 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 170 */     return false;
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
/*     */   public boolean containsValue(long value) {
/* 186 */     return contains(value);
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
/*     */   public boolean containsKey(long key) {
/* 199 */     Entry[] tab = this.table;
/* 200 */     int hash = (int)(key ^ key >>> 32L);
/* 201 */     int index = (hash & Integer.MAX_VALUE) % tab.length;
/* 202 */     for (Entry e = tab[index]; e != null; e = e.next) {
/* 203 */       if (e.hash == hash && e.key == key) {
/* 204 */         return true;
/*     */       }
/*     */     } 
/* 207 */     return false;
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
/*     */   public long get(long key) {
/* 220 */     Entry[] tab = this.table;
/* 221 */     int hash = (int)(key ^ key >>> 32L);
/* 222 */     int index = (hash & Integer.MAX_VALUE) % tab.length;
/* 223 */     for (Entry e = tab[index]; e != null; e = e.next) {
/* 224 */       if (e.hash == hash && e.key == key) {
/* 225 */         return e.value;
/*     */       }
/*     */     } 
/* 228 */     return 0L;
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
/*     */   protected void rehash() {
/* 241 */     int oldCapacity = this.table.length;
/* 242 */     Entry[] oldMap = this.table;
/*     */     
/* 244 */     int newCapacity = oldCapacity * 2 + 1;
/* 245 */     Entry[] newMap = new Entry[newCapacity];
/*     */     
/* 247 */     this.threshold = (int)(newCapacity * this.loadFactor);
/* 248 */     this.table = newMap;
/*     */     
/* 250 */     for (int i = oldCapacity; i-- > 0;) {
/* 251 */       for (Entry old = oldMap[i]; old != null; ) {
/* 252 */         Entry e = old;
/* 253 */         old = old.next;
/*     */         
/* 255 */         int index = (e.hash & Integer.MAX_VALUE) % newCapacity;
/* 256 */         e.next = newMap[index];
/* 257 */         newMap[index] = e;
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
/*     */   public long put(long key, long value) {
/* 279 */     Entry[] tab = this.table;
/* 280 */     int hash = (int)(key ^ key >>> 32L);
/* 281 */     int index = (hash & Integer.MAX_VALUE) % tab.length; Entry e;
/* 282 */     for (e = tab[index]; e != null; e = e.next) {
/* 283 */       if (e.hash == hash && e.key == key) {
/* 284 */         long old = e.value;
/* 285 */         e.value = value;
/* 286 */         return old;
/*     */       } 
/*     */     } 
/*     */     
/* 290 */     if (this.count >= this.threshold) {
/*     */       
/* 292 */       rehash();
/*     */       
/* 294 */       tab = this.table;
/* 295 */       index = (hash & Integer.MAX_VALUE) % tab.length;
/*     */     } 
/*     */ 
/*     */     
/* 299 */     e = new Entry(hash, key, value, tab[index]);
/* 300 */     tab[index] = e;
/* 301 */     this.count++;
/* 302 */     return 0L;
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
/*     */   public long remove(long key) {
/* 317 */     Entry[] tab = this.table;
/* 318 */     int hash = (int)(key ^ key >>> 32L);
/* 319 */     int index = (hash & Integer.MAX_VALUE) % tab.length;
/* 320 */     for (Entry e = tab[index], prev = null; e != null; prev = e, e = e.next) {
/* 321 */       if (e.hash == hash && e.key == key) {
/* 322 */         if (prev != null) {
/* 323 */           prev.next = e.next;
/*     */         } else {
/* 325 */           tab[index] = e.next;
/*     */         } 
/* 327 */         this.count--;
/* 328 */         long oldValue = e.value;
/* 329 */         e.value = 0L;
/* 330 */         return oldValue;
/*     */       } 
/*     */     } 
/* 333 */     return 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 340 */     Entry[] tab = this.table;
/* 341 */     for (int index = tab.length; --index >= 0;) {
/* 342 */       tab[index] = null;
/*     */     }
/* 344 */     this.count = 0;
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
/*     */     long key;
/*     */ 
/*     */     
/*     */     long value;
/*     */ 
/*     */     
/*     */     Entry next;
/*     */ 
/*     */ 
/*     */     
/*     */     protected Entry(int hash, long key, long value, Entry next) {
/* 366 */       this.hash = hash;
/* 367 */       this.key = key;
/* 368 */       this.value = value;
/* 369 */       this.next = next;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getKey() {
/* 374 */       return this.key;
/*     */     }
/*     */     public long getValue() {
/* 377 */       return this.value;
/*     */     }
/*     */     
/*     */     protected Object clone() {
/* 381 */       Entry entry = new Entry(this.hash, this.key, this.value, (this.next != null) ? (Entry)this.next.clone() : null);
/* 382 */       return entry;
/*     */     }
/*     */   }
/*     */   
/*     */   static class LongHashtableIterator
/*     */     implements Iterator<Entry> {
/*     */     int index;
/*     */     LongHashtable.Entry[] table;
/*     */     LongHashtable.Entry entry;
/*     */     
/*     */     LongHashtableIterator(LongHashtable.Entry[] table) {
/* 393 */       this.table = table;
/* 394 */       this.index = table.length;
/*     */     }
/*     */     public boolean hasNext() {
/* 397 */       if (this.entry != null) {
/* 398 */         return true;
/*     */       }
/* 400 */       while (this.index-- > 0) {
/* 401 */         if ((this.entry = this.table[this.index]) != null) {
/* 402 */           return true;
/*     */         }
/*     */       } 
/* 405 */       return false;
/*     */     }
/*     */     
/*     */     public LongHashtable.Entry next() {
/* 409 */       if (this.entry == null) {
/* 410 */         while (this.index-- > 0 && (this.entry = this.table[this.index]) == null);
/*     */       }
/* 412 */       if (this.entry != null) {
/* 413 */         LongHashtable.Entry e = this.entry;
/* 414 */         this.entry = e.next;
/* 415 */         return e;
/*     */       } 
/* 417 */       throw new NoSuchElementException(MessageLocalization.getComposedMessage("inthashtableiterator", new Object[0]));
/*     */     }
/*     */     public void remove() {
/* 420 */       throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("remove.not.supported", new Object[0]));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Entry> getEntryIterator() {
/* 427 */     return new LongHashtableIterator(this.table);
/*     */   }
/*     */   
/*     */   public long[] toOrderedKeys() {
/* 431 */     long[] res = getKeys();
/* 432 */     Arrays.sort(res);
/* 433 */     return res;
/*     */   }
/*     */   
/*     */   public long[] getKeys() {
/* 437 */     long[] res = new long[this.count];
/* 438 */     int ptr = 0;
/* 439 */     int index = this.table.length;
/* 440 */     Entry entry = null;
/*     */     while (true) {
/* 442 */       if (entry == null)
/* 443 */         while (index-- > 0 && (entry = this.table[index]) == null); 
/* 444 */       if (entry == null)
/*     */         break; 
/* 446 */       Entry e = entry;
/* 447 */       entry = e.next;
/* 448 */       res[ptr++] = e.key;
/*     */     } 
/* 450 */     return res;
/*     */   }
/*     */   
/*     */   public long getOneKey() {
/* 454 */     if (this.count == 0)
/* 455 */       return 0L; 
/* 456 */     int index = this.table.length;
/* 457 */     Entry entry = null;
/* 458 */     while (index-- > 0 && (entry = this.table[index]) == null);
/* 459 */     if (entry == null)
/* 460 */       return 0L; 
/* 461 */     return entry.key;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 467 */       LongHashtable t = (LongHashtable)super.clone();
/* 468 */       t.table = new Entry[this.table.length];
/* 469 */       for (int i = this.table.length; i-- > 0;) {
/* 470 */         t.table[i] = (this.table[i] != null) ? (Entry)this.table[i]
/* 471 */           .clone() : null;
/*     */       }
/* 473 */       return t;
/* 474 */     } catch (CloneNotSupportedException e) {
/*     */       
/* 476 */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/LongHashtable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */