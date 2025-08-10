/*     */ package com.itextpdf.text.pdf.codec;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TIFFField
/*     */   implements Comparable<TIFFField>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 9088332901412823834L;
/*     */   public static final int TIFF_BYTE = 1;
/*     */   public static final int TIFF_ASCII = 2;
/*     */   public static final int TIFF_SHORT = 3;
/*     */   public static final int TIFF_LONG = 4;
/*     */   public static final int TIFF_RATIONAL = 5;
/*     */   public static final int TIFF_SBYTE = 6;
/*     */   public static final int TIFF_UNDEFINED = 7;
/*     */   public static final int TIFF_SSHORT = 8;
/*     */   public static final int TIFF_SLONG = 9;
/*     */   public static final int TIFF_SRATIONAL = 10;
/*     */   public static final int TIFF_FLOAT = 11;
/*     */   public static final int TIFF_DOUBLE = 12;
/*     */   int tag;
/*     */   int type;
/*     */   int count;
/*     */   Object data;
/*     */   
/*     */   TIFFField() {}
/*     */   
/*     */   public TIFFField(int tag, int type, int count, Object data) {
/* 158 */     this.tag = tag;
/* 159 */     this.type = type;
/* 160 */     this.count = count;
/* 161 */     this.data = data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTag() {
/* 168 */     return this.tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType() {
/* 179 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCount() {
/* 186 */     return this.count;
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
/*     */   public byte[] getAsBytes() {
/* 202 */     return (byte[])this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[] getAsChars() {
/* 213 */     return (char[])this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] getAsShorts() {
/* 224 */     return (short[])this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getAsInts() {
/* 235 */     return (int[])this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long[] getAsLongs() {
/* 246 */     return (long[])this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float[] getAsFloats() {
/* 256 */     return (float[])this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double[] getAsDoubles() {
/* 266 */     return (double[])this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[][] getAsSRationals() {
/* 276 */     return (int[][])this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long[][] getAsRationals() {
/* 286 */     return (long[][])this.data;
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
/*     */   public int getAsInt(int index) {
/* 303 */     switch (this.type) { case 1:
/*     */       case 7:
/* 305 */         return ((byte[])this.data)[index] & 0xFF;
/*     */       case 6:
/* 307 */         return ((byte[])this.data)[index];
/*     */       case 3:
/* 309 */         return ((char[])this.data)[index] & Character.MAX_VALUE;
/*     */       case 8:
/* 311 */         return ((short[])this.data)[index];
/*     */       case 9:
/* 313 */         return ((int[])this.data)[index]; }
/*     */     
/* 315 */     throw new ClassCastException();
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
/*     */   public long getAsLong(int index) {
/* 333 */     switch (this.type) { case 1:
/*     */       case 7:
/* 335 */         return (((byte[])this.data)[index] & 0xFF);
/*     */       case 6:
/* 337 */         return ((byte[])this.data)[index];
/*     */       case 3:
/* 339 */         return (((char[])this.data)[index] & Character.MAX_VALUE);
/*     */       case 8:
/* 341 */         return ((short[])this.data)[index];
/*     */       case 9:
/* 343 */         return ((int[])this.data)[index];
/*     */       case 4:
/* 345 */         return ((long[])this.data)[index]; }
/*     */     
/* 347 */     throw new ClassCastException();
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
/*     */   public float getAsFloat(int index) {
/*     */     int[] ivalue;
/*     */     long[] lvalue;
/* 363 */     switch (this.type) {
/*     */       case 1:
/* 365 */         return (((byte[])this.data)[index] & 0xFF);
/*     */       case 6:
/* 367 */         return ((byte[])this.data)[index];
/*     */       case 3:
/* 369 */         return (((char[])this.data)[index] & Character.MAX_VALUE);
/*     */       case 8:
/* 371 */         return ((short[])this.data)[index];
/*     */       case 9:
/* 373 */         return ((int[])this.data)[index];
/*     */       case 4:
/* 375 */         return (float)((long[])this.data)[index];
/*     */       case 11:
/* 377 */         return ((float[])this.data)[index];
/*     */       case 12:
/* 379 */         return (float)((double[])this.data)[index];
/*     */       case 10:
/* 381 */         ivalue = getAsSRational(index);
/* 382 */         return (float)(ivalue[0] / ivalue[1]);
/*     */       case 5:
/* 384 */         lvalue = getAsRational(index);
/* 385 */         return (float)(lvalue[0] / lvalue[1]);
/*     */     } 
/* 387 */     throw new ClassCastException();
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
/*     */   public double getAsDouble(int index) {
/*     */     int[] ivalue;
/*     */     long[] lvalue;
/* 401 */     switch (this.type) {
/*     */       case 1:
/* 403 */         return (((byte[])this.data)[index] & 0xFF);
/*     */       case 6:
/* 405 */         return ((byte[])this.data)[index];
/*     */       case 3:
/* 407 */         return (((char[])this.data)[index] & Character.MAX_VALUE);
/*     */       case 8:
/* 409 */         return ((short[])this.data)[index];
/*     */       case 9:
/* 411 */         return ((int[])this.data)[index];
/*     */       case 4:
/* 413 */         return ((long[])this.data)[index];
/*     */       case 11:
/* 415 */         return ((float[])this.data)[index];
/*     */       case 12:
/* 417 */         return ((double[])this.data)[index];
/*     */       case 10:
/* 419 */         ivalue = getAsSRational(index);
/* 420 */         return ivalue[0] / ivalue[1];
/*     */       case 5:
/* 422 */         lvalue = getAsRational(index);
/* 423 */         return lvalue[0] / lvalue[1];
/*     */     } 
/* 425 */     throw new ClassCastException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAsString(int index) {
/* 436 */     return ((String[])this.data)[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getAsSRational(int index) {
/* 447 */     return ((int[][])this.data)[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long[] getAsRational(int index) {
/* 458 */     if (this.type == 4)
/* 459 */       return getAsLongs(); 
/* 460 */     return ((long[][])this.data)[index];
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
/*     */   public int compareTo(TIFFField o) {
/* 473 */     if (o == null) {
/* 474 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 477 */     int oTag = o.getTag();
/*     */     
/* 479 */     if (this.tag < oTag)
/* 480 */       return -1; 
/* 481 */     if (this.tag > oTag) {
/* 482 */       return 1;
/*     */     }
/* 484 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/TIFFField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */