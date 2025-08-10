/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocWriter;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteBuffer
/*     */   extends OutputStream
/*     */ {
/*     */   protected int count;
/*     */   protected byte[] buf;
/*  68 */   private static int byteCacheSize = 0;
/*     */   
/*  70 */   private static byte[][] byteCache = new byte[byteCacheSize][];
/*     */   public static final byte ZERO = 48;
/*  72 */   private static final char[] chars = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
/*  73 */   private static final byte[] bytes = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
/*     */ 
/*     */   
/*     */   public static boolean HIGH_PRECISION = false;
/*     */ 
/*     */   
/*  79 */   private static final DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
/*     */ 
/*     */   
/*     */   public ByteBuffer() {
/*  83 */     this(128);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer(int size) {
/*  91 */     if (size < 1)
/*  92 */       size = 128; 
/*  93 */     this.buf = new byte[size];
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
/*     */   public static void setCacheSize(int size) {
/* 106 */     if (size > 3276700) size = 3276700; 
/* 107 */     if (size <= byteCacheSize)
/* 108 */       return;  byte[][] tmpCache = new byte[size][];
/* 109 */     System.arraycopy(byteCache, 0, tmpCache, 0, byteCacheSize);
/* 110 */     byteCache = tmpCache;
/* 111 */     byteCacheSize = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void fillCache(int decimals) {
/* 121 */     int step = 1;
/* 122 */     switch (decimals) {
/*     */       case 0:
/* 124 */         step = 100;
/*     */         break;
/*     */       case 1:
/* 127 */         step = 10;
/*     */         break;
/*     */     } 
/* 130 */     for (int i = 1; i < byteCacheSize; i += step) {
/* 131 */       if (byteCache[i] == null) {
/* 132 */         byteCache[i] = convertToBytes(i);
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
/*     */   private static byte[] convertToBytes(int i) {
/* 144 */     int size = (int)Math.floor(Math.log(i) / Math.log(10.0D));
/* 145 */     if (i % 100 != 0) {
/* 146 */       size += 2;
/*     */     }
/* 148 */     if (i % 10 != 0) {
/* 149 */       size++;
/*     */     }
/* 151 */     if (i < 100) {
/* 152 */       size++;
/* 153 */       if (i < 10) {
/* 154 */         size++;
/*     */       }
/*     */     } 
/* 157 */     size--;
/* 158 */     byte[] cache = new byte[size];
/* 159 */     size--;
/* 160 */     if (i < 100) {
/* 161 */       cache[0] = 48;
/*     */     }
/* 163 */     if (i % 10 != 0) {
/* 164 */       cache[size--] = bytes[i % 10];
/*     */     }
/* 166 */     if (i % 100 != 0) {
/* 167 */       cache[size--] = bytes[i / 10 % 10];
/* 168 */       cache[size--] = 46;
/*     */     } 
/* 170 */     size = (int)Math.floor(Math.log(i) / Math.log(10.0D)) - 1;
/* 171 */     int add = 0;
/* 172 */     while (add < size) {
/* 173 */       cache[add] = bytes[i / (int)Math.pow(10.0D, (size - add + 1)) % 10];
/* 174 */       add++;
/*     */     } 
/* 176 */     return cache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer append_i(int b) {
/* 185 */     int newcount = this.count + 1;
/* 186 */     if (newcount > this.buf.length) {
/* 187 */       byte[] newbuf = new byte[Math.max(this.buf.length << 1, newcount)];
/* 188 */       System.arraycopy(this.buf, 0, newbuf, 0, this.count);
/* 189 */       this.buf = newbuf;
/*     */     } 
/* 191 */     this.buf[this.count] = (byte)b;
/* 192 */     this.count = newcount;
/* 193 */     return this;
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
/*     */   public ByteBuffer append(byte[] b, int off, int len) {
/* 205 */     if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0 || len == 0)
/*     */     {
/* 207 */       return this; } 
/* 208 */     int newcount = this.count + len;
/* 209 */     if (newcount > this.buf.length) {
/* 210 */       byte[] newbuf = new byte[Math.max(this.buf.length << 1, newcount)];
/* 211 */       System.arraycopy(this.buf, 0, newbuf, 0, this.count);
/* 212 */       this.buf = newbuf;
/*     */     } 
/* 214 */     System.arraycopy(b, off, this.buf, this.count, len);
/* 215 */     this.count = newcount;
/* 216 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer append(byte[] b) {
/* 225 */     return append(b, 0, b.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer append(String str) {
/* 235 */     if (str != null)
/* 236 */       return append(DocWriter.getISOBytes(str)); 
/* 237 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer append(char c) {
/* 247 */     return append_i(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer append(ByteBuffer buf) {
/* 256 */     return append(buf.buf, 0, buf.count);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer append(int i) {
/* 265 */     return append(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer append(long i) {
/* 274 */     return append(Long.toString(i));
/*     */   }
/*     */   
/*     */   public ByteBuffer append(byte b) {
/* 278 */     return append_i(b);
/*     */   }
/*     */   
/*     */   public ByteBuffer appendHex(byte b) {
/* 282 */     append(bytes[b >> 4 & 0xF]);
/* 283 */     return append(bytes[b & 0xF]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer append(float i) {
/* 293 */     return append(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer append(double d) {
/* 303 */     append(formatDouble(d, this));
/* 304 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String formatDouble(double d) {
/* 313 */     return formatDouble(d, null);
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
/*     */   public static String formatDouble(double d, ByteBuffer buf) {
/* 325 */     if (HIGH_PRECISION) {
/* 326 */       DecimalFormat dn = new DecimalFormat("0.######", dfs);
/* 327 */       String sform = dn.format(d);
/* 328 */       if (buf == null) {
/* 329 */         return sform;
/*     */       }
/* 331 */       buf.append(sform);
/* 332 */       return null;
/*     */     } 
/*     */     
/* 335 */     boolean negative = false;
/* 336 */     if (Math.abs(d) < 1.5E-5D) {
/* 337 */       if (buf != null) {
/* 338 */         buf.append((byte)48);
/* 339 */         return null;
/*     */       } 
/* 341 */       return "0";
/*     */     } 
/*     */     
/* 344 */     if (d < 0.0D) {
/* 345 */       negative = true;
/* 346 */       d = -d;
/*     */     } 
/* 348 */     if (d < 1.0D) {
/* 349 */       d += 5.0E-6D;
/* 350 */       if (d >= 1.0D) {
/* 351 */         if (negative) {
/* 352 */           if (buf != null) {
/* 353 */             buf.append((byte)45);
/* 354 */             buf.append((byte)49);
/* 355 */             return null;
/*     */           } 
/* 357 */           return "-1";
/*     */         } 
/*     */         
/* 360 */         if (buf != null) {
/* 361 */           buf.append((byte)49);
/* 362 */           return null;
/*     */         } 
/* 364 */         return "1";
/*     */       } 
/*     */ 
/*     */       
/* 368 */       if (buf != null) {
/* 369 */         int j = (int)(d * 100000.0D);
/*     */         
/* 371 */         if (negative) buf.append((byte)45); 
/* 372 */         buf.append((byte)48);
/* 373 */         buf.append((byte)46);
/*     */         
/* 375 */         buf.append((byte)(j / 10000 + 48));
/* 376 */         if (j % 10000 != 0) {
/* 377 */           buf.append((byte)(j / 1000 % 10 + 48));
/* 378 */           if (j % 1000 != 0) {
/* 379 */             buf.append((byte)(j / 100 % 10 + 48));
/* 380 */             if (j % 100 != 0) {
/* 381 */               buf.append((byte)(j / 10 % 10 + 48));
/* 382 */               if (j % 10 != 0) {
/* 383 */                 buf.append((byte)(j % 10 + 48));
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/* 388 */         return null;
/*     */       } 
/* 390 */       int x = 100000;
/* 391 */       int i = (int)(d * x);
/*     */       
/* 393 */       StringBuilder res = new StringBuilder();
/* 394 */       if (negative) res.append('-'); 
/* 395 */       res.append("0.");
/*     */       
/* 397 */       while (i < x / 10) {
/* 398 */         res.append('0');
/* 399 */         x /= 10;
/*     */       } 
/* 401 */       res.append(i);
/* 402 */       int cut = res.length() - 1;
/* 403 */       while (res.charAt(cut) == '0') {
/* 404 */         cut--;
/*     */       }
/* 406 */       res.setLength(cut + 1);
/* 407 */       return res.toString();
/*     */     } 
/* 409 */     if (d <= 32767.0D) {
/* 410 */       d += 0.005D;
/* 411 */       int i = (int)(d * 100.0D);
/*     */       
/* 413 */       if (i < byteCacheSize && byteCache[i] != null) {
/* 414 */         if (buf != null) {
/* 415 */           if (negative) buf.append((byte)45); 
/* 416 */           buf.append(byteCache[i]);
/* 417 */           return null;
/*     */         } 
/* 419 */         String tmp = PdfEncodings.convertToString(byteCache[i], null);
/* 420 */         if (negative) tmp = "-" + tmp; 
/* 421 */         return tmp;
/*     */       } 
/*     */       
/* 424 */       if (buf != null) {
/* 425 */         if (i < byteCacheSize) {
/*     */ 
/*     */           
/* 428 */           int size = 0;
/* 429 */           if (i >= 1000000) {
/*     */             
/* 431 */             size += 5;
/* 432 */           } else if (i >= 100000) {
/*     */             
/* 434 */             size += 4;
/* 435 */           } else if (i >= 10000) {
/*     */             
/* 437 */             size += 3;
/* 438 */           } else if (i >= 1000) {
/*     */             
/* 440 */             size += 2;
/* 441 */           } else if (i >= 100) {
/*     */             
/* 443 */             size++;
/*     */           } 
/*     */ 
/*     */           
/* 447 */           if (i % 100 != 0)
/*     */           {
/* 449 */             size += 2;
/*     */           }
/* 451 */           if (i % 10 != 0) {
/* 452 */             size++;
/*     */           }
/* 454 */           byte[] cache = new byte[size];
/* 455 */           int add = 0;
/* 456 */           if (i >= 1000000) {
/* 457 */             cache[add++] = bytes[i / 1000000];
/*     */           }
/* 459 */           if (i >= 100000) {
/* 460 */             cache[add++] = bytes[i / 100000 % 10];
/*     */           }
/* 462 */           if (i >= 10000) {
/* 463 */             cache[add++] = bytes[i / 10000 % 10];
/*     */           }
/* 465 */           if (i >= 1000) {
/* 466 */             cache[add++] = bytes[i / 1000 % 10];
/*     */           }
/* 468 */           if (i >= 100) {
/* 469 */             cache[add++] = bytes[i / 100 % 10];
/*     */           }
/*     */           
/* 472 */           if (i % 100 != 0) {
/* 473 */             cache[add++] = 46;
/* 474 */             cache[add++] = bytes[i / 10 % 10];
/* 475 */             if (i % 10 != 0) {
/* 476 */               cache[add++] = bytes[i % 10];
/*     */             }
/*     */           } 
/* 479 */           byteCache[i] = cache;
/*     */         } 
/*     */         
/* 482 */         if (negative) buf.append((byte)45); 
/* 483 */         if (i >= 1000000) {
/* 484 */           buf.append(bytes[i / 1000000]);
/*     */         }
/* 486 */         if (i >= 100000) {
/* 487 */           buf.append(bytes[i / 100000 % 10]);
/*     */         }
/* 489 */         if (i >= 10000) {
/* 490 */           buf.append(bytes[i / 10000 % 10]);
/*     */         }
/* 492 */         if (i >= 1000) {
/* 493 */           buf.append(bytes[i / 1000 % 10]);
/*     */         }
/* 495 */         if (i >= 100) {
/* 496 */           buf.append(bytes[i / 100 % 10]);
/*     */         }
/*     */         
/* 499 */         if (i % 100 != 0) {
/* 500 */           buf.append((byte)46);
/* 501 */           buf.append(bytes[i / 10 % 10]);
/* 502 */           if (i % 10 != 0) {
/* 503 */             buf.append(bytes[i % 10]);
/*     */           }
/*     */         } 
/* 506 */         return null;
/*     */       } 
/* 508 */       StringBuilder res = new StringBuilder();
/* 509 */       if (negative) res.append('-'); 
/* 510 */       if (i >= 1000000) {
/* 511 */         res.append(chars[i / 1000000]);
/*     */       }
/* 513 */       if (i >= 100000) {
/* 514 */         res.append(chars[i / 100000 % 10]);
/*     */       }
/* 516 */       if (i >= 10000) {
/* 517 */         res.append(chars[i / 10000 % 10]);
/*     */       }
/* 519 */       if (i >= 1000) {
/* 520 */         res.append(chars[i / 1000 % 10]);
/*     */       }
/* 522 */       if (i >= 100) {
/* 523 */         res.append(chars[i / 100 % 10]);
/*     */       }
/*     */       
/* 526 */       if (i % 100 != 0) {
/* 527 */         res.append('.');
/* 528 */         res.append(chars[i / 10 % 10]);
/* 529 */         if (i % 10 != 0) {
/* 530 */           res.append(chars[i % 10]);
/*     */         }
/*     */       } 
/* 533 */       return res.toString();
/*     */     } 
/*     */     
/* 536 */     d += 0.5D;
/* 537 */     long v = (long)d;
/* 538 */     if (negative) {
/* 539 */       return "-" + Long.toString(v);
/*     */     }
/* 541 */     return Long.toString(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 549 */     this.count = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toByteArray() {
/* 560 */     byte[] newbuf = new byte[this.count];
/* 561 */     System.arraycopy(this.buf, 0, newbuf, 0, this.count);
/* 562 */     return newbuf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 571 */     return this.count;
/*     */   }
/*     */   
/*     */   public void setSize(int size) {
/* 575 */     if (size > this.count || size < 0)
/* 576 */       throw new IndexOutOfBoundsException(MessageLocalization.getComposedMessage("the.new.size.must.be.positive.and.lt.eq.of.the.current.size", new Object[0])); 
/* 577 */     this.count = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 588 */     return new String(this.buf, 0, this.count);
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
/*     */   public String toString(String enc) throws UnsupportedEncodingException {
/* 601 */     return new String(this.buf, 0, this.count, enc);
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
/*     */   public void writeTo(OutputStream out) throws IOException {
/* 613 */     out.write(this.buf, 0, this.count);
/*     */   }
/*     */   
/*     */   public void write(int b) throws IOException {
/* 617 */     append((byte)b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) {
/* 622 */     append(b, off, len);
/*     */   }
/*     */   
/*     */   public byte[] getBuffer() {
/* 626 */     return this.buf;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/ByteBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */