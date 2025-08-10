/*      */ package META-INF.versions.9.org.bouncycastle.util;
/*      */ 
/*      */ import java.math.BigInteger;
/*      */ import org.bouncycastle.util.Objects;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Arrays
/*      */ {
/*      */   public static boolean areAllZeroes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*   18 */     int i = 0;
/*   19 */     for (byte b = 0; b < paramInt2; b++)
/*      */     {
/*   21 */       i |= paramArrayOfbyte[paramInt1 + b];
/*      */     }
/*   23 */     return (i == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean areEqual(boolean[] paramArrayOfboolean1, boolean[] paramArrayOfboolean2) {
/*   28 */     return java.util.Arrays.equals(paramArrayOfboolean1, paramArrayOfboolean2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean areEqual(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/*   33 */     return java.util.Arrays.equals(paramArrayOfbyte1, paramArrayOfbyte2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean areEqual(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, byte[] paramArrayOfbyte2, int paramInt3, int paramInt4) {
/*   38 */     int i = paramInt2 - paramInt1;
/*   39 */     int j = paramInt4 - paramInt3;
/*      */     
/*   41 */     if (i != j)
/*      */     {
/*   43 */       return false;
/*      */     }
/*      */     
/*   46 */     for (byte b = 0; b < i; b++) {
/*      */       
/*   48 */       if (paramArrayOfbyte1[paramInt1 + b] != paramArrayOfbyte2[paramInt3 + b])
/*      */       {
/*   50 */         return false;
/*      */       }
/*      */     } 
/*      */     
/*   54 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean areEqual(char[] paramArrayOfchar1, char[] paramArrayOfchar2) {
/*   59 */     return java.util.Arrays.equals(paramArrayOfchar1, paramArrayOfchar2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean areEqual(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*   64 */     return java.util.Arrays.equals(paramArrayOfint1, paramArrayOfint2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean areEqual(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*   69 */     return java.util.Arrays.equals(paramArrayOflong1, paramArrayOflong2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean areEqual(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2) {
/*   74 */     return java.util.Arrays.equals(paramArrayOfObject1, paramArrayOfObject2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean areEqual(short[] paramArrayOfshort1, short[] paramArrayOfshort2) {
/*   79 */     return java.util.Arrays.equals(paramArrayOfshort1, paramArrayOfshort2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean constantTimeAreEqual(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/*   95 */     if (paramArrayOfbyte1 == null || paramArrayOfbyte2 == null)
/*      */     {
/*   97 */       return false;
/*      */     }
/*      */     
/*  100 */     if (paramArrayOfbyte1 == paramArrayOfbyte2)
/*      */     {
/*  102 */       return true;
/*      */     }
/*      */     
/*  105 */     int i = (paramArrayOfbyte1.length < paramArrayOfbyte2.length) ? paramArrayOfbyte1.length : paramArrayOfbyte2.length;
/*      */     
/*  107 */     int j = paramArrayOfbyte1.length ^ paramArrayOfbyte2.length;
/*      */     int k;
/*  109 */     for (k = 0; k != i; k++)
/*      */     {
/*  111 */       j |= paramArrayOfbyte1[k] ^ paramArrayOfbyte2[k];
/*      */     }
/*  113 */     for (k = i; k < paramArrayOfbyte2.length; k++)
/*      */     {
/*  115 */       j |= paramArrayOfbyte2[k] ^ paramArrayOfbyte2[k] ^ 0xFFFFFFFF;
/*      */     }
/*      */     
/*  118 */     return (j == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean constantTimeAreEqual(int paramInt1, byte[] paramArrayOfbyte1, int paramInt2, byte[] paramArrayOfbyte2, int paramInt3) {
/*  123 */     if (null == paramArrayOfbyte1)
/*      */     {
/*  125 */       throw new NullPointerException("'a' cannot be null");
/*      */     }
/*  127 */     if (null == paramArrayOfbyte2)
/*      */     {
/*  129 */       throw new NullPointerException("'b' cannot be null");
/*      */     }
/*  131 */     if (paramInt1 < 0)
/*      */     {
/*  133 */       throw new IllegalArgumentException("'len' cannot be negative");
/*      */     }
/*  135 */     if (paramInt2 > paramArrayOfbyte1.length - paramInt1)
/*      */     {
/*  137 */       throw new IndexOutOfBoundsException("'aOff' value invalid for specified length");
/*      */     }
/*  139 */     if (paramInt3 > paramArrayOfbyte2.length - paramInt1)
/*      */     {
/*  141 */       throw new IndexOutOfBoundsException("'bOff' value invalid for specified length");
/*      */     }
/*      */     
/*  144 */     int i = 0;
/*  145 */     for (byte b = 0; b < paramInt1; b++)
/*      */     {
/*  147 */       i |= paramArrayOfbyte1[paramInt2 + b] ^ paramArrayOfbyte2[paramInt3 + b];
/*      */     }
/*  149 */     return (0 == i);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int compareUnsigned(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/*  154 */     if (paramArrayOfbyte1 == paramArrayOfbyte2)
/*      */     {
/*  156 */       return 0;
/*      */     }
/*  158 */     if (paramArrayOfbyte1 == null)
/*      */     {
/*  160 */       return -1;
/*      */     }
/*  162 */     if (paramArrayOfbyte2 == null)
/*      */     {
/*  164 */       return 1;
/*      */     }
/*  166 */     int i = Math.min(paramArrayOfbyte1.length, paramArrayOfbyte2.length);
/*  167 */     for (byte b = 0; b < i; b++) {
/*      */       
/*  169 */       int j = paramArrayOfbyte1[b] & 0xFF, k = paramArrayOfbyte2[b] & 0xFF;
/*  170 */       if (j < k)
/*      */       {
/*  172 */         return -1;
/*      */       }
/*  174 */       if (j > k)
/*      */       {
/*  176 */         return 1;
/*      */       }
/*      */     } 
/*  179 */     if (paramArrayOfbyte1.length < paramArrayOfbyte2.length)
/*      */     {
/*  181 */       return -1;
/*      */     }
/*  183 */     if (paramArrayOfbyte1.length > paramArrayOfbyte2.length)
/*      */     {
/*  185 */       return 1;
/*      */     }
/*  187 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean contains(boolean[] paramArrayOfboolean, boolean paramBoolean) {
/*  192 */     for (byte b = 0; b < paramArrayOfboolean.length; b++) {
/*      */       
/*  194 */       if (paramArrayOfboolean[b] == paramBoolean)
/*      */       {
/*  196 */         return true;
/*      */       }
/*      */     } 
/*  199 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean contains(byte[] paramArrayOfbyte, byte paramByte) {
/*  204 */     for (byte b = 0; b < paramArrayOfbyte.length; b++) {
/*      */       
/*  206 */       if (paramArrayOfbyte[b] == paramByte)
/*      */       {
/*  208 */         return true;
/*      */       }
/*      */     } 
/*  211 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean contains(char[] paramArrayOfchar, char paramChar) {
/*  216 */     for (byte b = 0; b < paramArrayOfchar.length; b++) {
/*      */       
/*  218 */       if (paramArrayOfchar[b] == paramChar)
/*      */       {
/*  220 */         return true;
/*      */       }
/*      */     } 
/*  223 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean contains(int[] paramArrayOfint, int paramInt) {
/*  228 */     for (byte b = 0; b < paramArrayOfint.length; b++) {
/*      */       
/*  230 */       if (paramArrayOfint[b] == paramInt)
/*      */       {
/*  232 */         return true;
/*      */       }
/*      */     } 
/*  235 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean contains(long[] paramArrayOflong, long paramLong) {
/*  240 */     for (byte b = 0; b < paramArrayOflong.length; b++) {
/*      */       
/*  242 */       if (paramArrayOflong[b] == paramLong)
/*      */       {
/*  244 */         return true;
/*      */       }
/*      */     } 
/*  247 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean contains(short[] paramArrayOfshort, short paramShort) {
/*  252 */     for (byte b = 0; b < paramArrayOfshort.length; b++) {
/*      */       
/*  254 */       if (paramArrayOfshort[b] == paramShort)
/*      */       {
/*  256 */         return true;
/*      */       }
/*      */     } 
/*  259 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void fill(boolean[] paramArrayOfboolean, boolean paramBoolean) {
/*  264 */     java.util.Arrays.fill(paramArrayOfboolean, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void fill(boolean[] paramArrayOfboolean, int paramInt1, int paramInt2, boolean paramBoolean) {
/*  269 */     java.util.Arrays.fill(paramArrayOfboolean, paramInt1, paramInt2, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void fill(byte[] paramArrayOfbyte, byte paramByte) {
/*  274 */     java.util.Arrays.fill(paramArrayOfbyte, paramByte);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void fill(byte[] paramArrayOfbyte, int paramInt, byte paramByte) {
/*  282 */     fill(paramArrayOfbyte, paramInt, paramArrayOfbyte.length, paramByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void fill(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, byte paramByte) {
/*  287 */     java.util.Arrays.fill(paramArrayOfbyte, paramInt1, paramInt2, paramByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void fill(char[] paramArrayOfchar, char paramChar) {
/*  292 */     java.util.Arrays.fill(paramArrayOfchar, paramChar);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void fill(char[] paramArrayOfchar, int paramInt1, int paramInt2, char paramChar) {
/*  297 */     java.util.Arrays.fill(paramArrayOfchar, paramInt1, paramInt2, paramChar);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void fill(int[] paramArrayOfint, int paramInt) {
/*  302 */     java.util.Arrays.fill(paramArrayOfint, paramInt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void fill(int[] paramArrayOfint, int paramInt1, int paramInt2) {
/*  310 */     java.util.Arrays.fill(paramArrayOfint, paramInt1, paramArrayOfint.length, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void fill(int[] paramArrayOfint, int paramInt1, int paramInt2, int paramInt3) {
/*  315 */     java.util.Arrays.fill(paramArrayOfint, paramInt1, paramInt2, paramInt3);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void fill(long[] paramArrayOflong, long paramLong) {
/*  320 */     java.util.Arrays.fill(paramArrayOflong, paramLong);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void fill(long[] paramArrayOflong, int paramInt, long paramLong) {
/*  328 */     java.util.Arrays.fill(paramArrayOflong, paramInt, paramArrayOflong.length, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void fill(long[] paramArrayOflong, int paramInt1, int paramInt2, long paramLong) {
/*  333 */     java.util.Arrays.fill(paramArrayOflong, paramInt1, paramInt2, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void fill(Object[] paramArrayOfObject, Object paramObject) {
/*  338 */     java.util.Arrays.fill(paramArrayOfObject, paramObject);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void fill(Object[] paramArrayOfObject, int paramInt1, int paramInt2, Object paramObject) {
/*  343 */     java.util.Arrays.fill(paramArrayOfObject, paramInt1, paramInt2, paramObject);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void fill(short[] paramArrayOfshort, short paramShort) {
/*  348 */     java.util.Arrays.fill(paramArrayOfshort, paramShort);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void fill(short[] paramArrayOfshort, int paramInt, short paramShort) {
/*  356 */     java.util.Arrays.fill(paramArrayOfshort, paramInt, paramArrayOfshort.length, paramShort);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void fill(short[] paramArrayOfshort, int paramInt1, int paramInt2, short paramShort) {
/*  361 */     java.util.Arrays.fill(paramArrayOfshort, paramInt1, paramInt2, paramShort);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int hashCode(byte[] paramArrayOfbyte) {
/*  366 */     if (paramArrayOfbyte == null)
/*      */     {
/*  368 */       return 0;
/*      */     }
/*      */     
/*  371 */     int i = paramArrayOfbyte.length;
/*  372 */     int j = i + 1;
/*      */     
/*  374 */     while (--i >= 0) {
/*      */       
/*  376 */       j *= 257;
/*  377 */       j ^= paramArrayOfbyte[i];
/*      */     } 
/*      */     
/*  380 */     return j;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int hashCode(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*  385 */     if (paramArrayOfbyte == null)
/*      */     {
/*  387 */       return 0;
/*      */     }
/*      */     
/*  390 */     int i = paramInt2;
/*  391 */     int j = i + 1;
/*      */     
/*  393 */     while (--i >= 0) {
/*      */       
/*  395 */       j *= 257;
/*  396 */       j ^= paramArrayOfbyte[paramInt1 + i];
/*      */     } 
/*      */     
/*  399 */     return j;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int hashCode(char[] paramArrayOfchar) {
/*  404 */     if (paramArrayOfchar == null)
/*      */     {
/*  406 */       return 0;
/*      */     }
/*      */     
/*  409 */     int i = paramArrayOfchar.length;
/*  410 */     int j = i + 1;
/*      */     
/*  412 */     while (--i >= 0) {
/*      */       
/*  414 */       j *= 257;
/*  415 */       j ^= paramArrayOfchar[i];
/*      */     } 
/*      */     
/*  418 */     return j;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int hashCode(int[][] paramArrayOfint) {
/*  423 */     int i = 0;
/*      */     
/*  425 */     for (byte b = 0; b != paramArrayOfint.length; b++)
/*      */     {
/*  427 */       i = i * 257 + hashCode(paramArrayOfint[b]);
/*      */     }
/*      */     
/*  430 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int hashCode(int[] paramArrayOfint) {
/*  435 */     if (paramArrayOfint == null)
/*      */     {
/*  437 */       return 0;
/*      */     }
/*      */     
/*  440 */     int i = paramArrayOfint.length;
/*  441 */     int j = i + 1;
/*      */     
/*  443 */     while (--i >= 0) {
/*      */       
/*  445 */       j *= 257;
/*  446 */       j ^= paramArrayOfint[i];
/*      */     } 
/*      */     
/*  449 */     return j;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int hashCode(int[] paramArrayOfint, int paramInt1, int paramInt2) {
/*  454 */     if (paramArrayOfint == null)
/*      */     {
/*  456 */       return 0;
/*      */     }
/*      */     
/*  459 */     int i = paramInt2;
/*  460 */     int j = i + 1;
/*      */     
/*  462 */     while (--i >= 0) {
/*      */       
/*  464 */       j *= 257;
/*  465 */       j ^= paramArrayOfint[paramInt1 + i];
/*      */     } 
/*      */     
/*  468 */     return j;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int hashCode(long[] paramArrayOflong) {
/*  473 */     if (paramArrayOflong == null)
/*      */     {
/*  475 */       return 0;
/*      */     }
/*      */     
/*  478 */     int i = paramArrayOflong.length;
/*  479 */     int j = i + 1;
/*      */     
/*  481 */     while (--i >= 0) {
/*      */       
/*  483 */       long l = paramArrayOflong[i];
/*  484 */       j *= 257;
/*  485 */       j ^= (int)l;
/*  486 */       j *= 257;
/*  487 */       j ^= (int)(l >>> 32L);
/*      */     } 
/*      */     
/*  490 */     return j;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int hashCode(long[] paramArrayOflong, int paramInt1, int paramInt2) {
/*  495 */     if (paramArrayOflong == null)
/*      */     {
/*  497 */       return 0;
/*      */     }
/*      */     
/*  500 */     int i = paramInt2;
/*  501 */     int j = i + 1;
/*      */     
/*  503 */     while (--i >= 0) {
/*      */       
/*  505 */       long l = paramArrayOflong[paramInt1 + i];
/*  506 */       j *= 257;
/*  507 */       j ^= (int)l;
/*  508 */       j *= 257;
/*  509 */       j ^= (int)(l >>> 32L);
/*      */     } 
/*      */     
/*  512 */     return j;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int hashCode(short[][][] paramArrayOfshort) {
/*  517 */     int i = 0;
/*      */     
/*  519 */     for (byte b = 0; b != paramArrayOfshort.length; b++)
/*      */     {
/*  521 */       i = i * 257 + hashCode(paramArrayOfshort[b]);
/*      */     }
/*      */     
/*  524 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int hashCode(short[][] paramArrayOfshort) {
/*  529 */     int i = 0;
/*      */     
/*  531 */     for (byte b = 0; b != paramArrayOfshort.length; b++)
/*      */     {
/*  533 */       i = i * 257 + hashCode(paramArrayOfshort[b]);
/*      */     }
/*      */     
/*  536 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int hashCode(short[] paramArrayOfshort) {
/*  541 */     if (paramArrayOfshort == null)
/*      */     {
/*  543 */       return 0;
/*      */     }
/*      */     
/*  546 */     int i = paramArrayOfshort.length;
/*  547 */     int j = i + 1;
/*      */     
/*  549 */     while (--i >= 0) {
/*      */       
/*  551 */       j *= 257;
/*  552 */       j ^= paramArrayOfshort[i] & 0xFF;
/*      */     } 
/*      */     
/*  555 */     return j;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int hashCode(Object[] paramArrayOfObject) {
/*  560 */     if (paramArrayOfObject == null)
/*      */     {
/*  562 */       return 0;
/*      */     }
/*      */     
/*  565 */     int i = paramArrayOfObject.length;
/*  566 */     int j = i + 1;
/*      */     
/*  568 */     while (--i >= 0) {
/*      */       
/*  570 */       j *= 257;
/*  571 */       j ^= Objects.hashCode(paramArrayOfObject[i]);
/*      */     } 
/*      */     
/*  574 */     return j;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean[] clone(boolean[] paramArrayOfboolean) {
/*  579 */     return (null == paramArrayOfboolean) ? null : (boolean[])paramArrayOfboolean.clone();
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] clone(byte[] paramArrayOfbyte) {
/*  584 */     return (null == paramArrayOfbyte) ? null : (byte[])paramArrayOfbyte.clone();
/*      */   }
/*      */ 
/*      */   
/*      */   public static char[] clone(char[] paramArrayOfchar) {
/*  589 */     return (null == paramArrayOfchar) ? null : (char[])paramArrayOfchar.clone();
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] clone(int[] paramArrayOfint) {
/*  594 */     return (null == paramArrayOfint) ? null : (int[])paramArrayOfint.clone();
/*      */   }
/*      */ 
/*      */   
/*      */   public static long[] clone(long[] paramArrayOflong) {
/*  599 */     return (null == paramArrayOflong) ? null : (long[])paramArrayOflong.clone();
/*      */   }
/*      */ 
/*      */   
/*      */   public static short[] clone(short[] paramArrayOfshort) {
/*  604 */     return (null == paramArrayOfshort) ? null : (short[])paramArrayOfshort.clone();
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigInteger[] clone(BigInteger[] paramArrayOfBigInteger) {
/*  609 */     return (null == paramArrayOfBigInteger) ? null : (BigInteger[])paramArrayOfBigInteger.clone();
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] clone(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/*  614 */     if (paramArrayOfbyte1 == null)
/*      */     {
/*  616 */       return null;
/*      */     }
/*  618 */     if (paramArrayOfbyte2 == null || paramArrayOfbyte2.length != paramArrayOfbyte1.length)
/*      */     {
/*  620 */       return clone(paramArrayOfbyte1);
/*      */     }
/*  622 */     System.arraycopy(paramArrayOfbyte1, 0, paramArrayOfbyte2, 0, paramArrayOfbyte2.length);
/*  623 */     return paramArrayOfbyte2;
/*      */   }
/*      */ 
/*      */   
/*      */   public static long[] clone(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  628 */     if (paramArrayOflong1 == null)
/*      */     {
/*  630 */       return null;
/*      */     }
/*  632 */     if (paramArrayOflong2 == null || paramArrayOflong2.length != paramArrayOflong1.length)
/*      */     {
/*  634 */       return clone(paramArrayOflong1);
/*      */     }
/*  636 */     System.arraycopy(paramArrayOflong1, 0, paramArrayOflong2, 0, paramArrayOflong2.length);
/*  637 */     return paramArrayOflong2;
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[][] clone(byte[][] paramArrayOfbyte) {
/*  642 */     if (paramArrayOfbyte == null)
/*      */     {
/*  644 */       return null;
/*      */     }
/*      */     
/*  647 */     byte[][] arrayOfByte = new byte[paramArrayOfbyte.length][];
/*      */     
/*  649 */     for (byte b = 0; b != arrayOfByte.length; b++)
/*      */     {
/*  651 */       arrayOfByte[b] = clone(paramArrayOfbyte[b]);
/*      */     }
/*      */     
/*  654 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[][][] clone(byte[][][] paramArrayOfbyte) {
/*  659 */     if (paramArrayOfbyte == null)
/*      */     {
/*  661 */       return null;
/*      */     }
/*      */     
/*  664 */     byte[][][] arrayOfByte = new byte[paramArrayOfbyte.length][][];
/*      */     
/*  666 */     for (byte b = 0; b != arrayOfByte.length; b++)
/*      */     {
/*  668 */       arrayOfByte[b] = clone(paramArrayOfbyte[b]);
/*      */     }
/*      */     
/*  671 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean[] copyOf(boolean[] paramArrayOfboolean, int paramInt) {
/*  676 */     boolean[] arrayOfBoolean = new boolean[paramInt];
/*  677 */     System.arraycopy(paramArrayOfboolean, 0, arrayOfBoolean, 0, Math.min(paramArrayOfboolean.length, paramInt));
/*  678 */     return arrayOfBoolean;
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] copyOf(byte[] paramArrayOfbyte, int paramInt) {
/*  683 */     byte[] arrayOfByte = new byte[paramInt];
/*  684 */     System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, 0, Math.min(paramArrayOfbyte.length, paramInt));
/*  685 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   
/*      */   public static char[] copyOf(char[] paramArrayOfchar, int paramInt) {
/*  690 */     char[] arrayOfChar = new char[paramInt];
/*  691 */     System.arraycopy(paramArrayOfchar, 0, arrayOfChar, 0, Math.min(paramArrayOfchar.length, paramInt));
/*  692 */     return arrayOfChar;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] copyOf(int[] paramArrayOfint, int paramInt) {
/*  697 */     int[] arrayOfInt = new int[paramInt];
/*  698 */     System.arraycopy(paramArrayOfint, 0, arrayOfInt, 0, Math.min(paramArrayOfint.length, paramInt));
/*  699 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   
/*      */   public static long[] copyOf(long[] paramArrayOflong, int paramInt) {
/*  704 */     long[] arrayOfLong = new long[paramInt];
/*  705 */     System.arraycopy(paramArrayOflong, 0, arrayOfLong, 0, Math.min(paramArrayOflong.length, paramInt));
/*  706 */     return arrayOfLong;
/*      */   }
/*      */ 
/*      */   
/*      */   public static short[] copyOf(short[] paramArrayOfshort, int paramInt) {
/*  711 */     short[] arrayOfShort = new short[paramInt];
/*  712 */     System.arraycopy(paramArrayOfshort, 0, arrayOfShort, 0, Math.min(paramArrayOfshort.length, paramInt));
/*  713 */     return arrayOfShort;
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigInteger[] copyOf(BigInteger[] paramArrayOfBigInteger, int paramInt) {
/*  718 */     BigInteger[] arrayOfBigInteger = new BigInteger[paramInt];
/*  719 */     System.arraycopy(paramArrayOfBigInteger, 0, arrayOfBigInteger, 0, Math.min(paramArrayOfBigInteger.length, paramInt));
/*  720 */     return arrayOfBigInteger;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean[] copyOfRange(boolean[] paramArrayOfboolean, int paramInt1, int paramInt2) {
/*  725 */     int i = getLength(paramInt1, paramInt2);
/*  726 */     boolean[] arrayOfBoolean = new boolean[i];
/*  727 */     System.arraycopy(paramArrayOfboolean, paramInt1, arrayOfBoolean, 0, Math.min(paramArrayOfboolean.length - paramInt1, i));
/*  728 */     return arrayOfBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] copyOfRange(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*  746 */     int i = getLength(paramInt1, paramInt2);
/*  747 */     byte[] arrayOfByte = new byte[i];
/*  748 */     System.arraycopy(paramArrayOfbyte, paramInt1, arrayOfByte, 0, Math.min(paramArrayOfbyte.length - paramInt1, i));
/*  749 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   
/*      */   public static char[] copyOfRange(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
/*  754 */     int i = getLength(paramInt1, paramInt2);
/*  755 */     char[] arrayOfChar = new char[i];
/*  756 */     System.arraycopy(paramArrayOfchar, paramInt1, arrayOfChar, 0, Math.min(paramArrayOfchar.length - paramInt1, i));
/*  757 */     return arrayOfChar;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] copyOfRange(int[] paramArrayOfint, int paramInt1, int paramInt2) {
/*  762 */     int i = getLength(paramInt1, paramInt2);
/*  763 */     int[] arrayOfInt = new int[i];
/*  764 */     System.arraycopy(paramArrayOfint, paramInt1, arrayOfInt, 0, Math.min(paramArrayOfint.length - paramInt1, i));
/*  765 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   
/*      */   public static long[] copyOfRange(long[] paramArrayOflong, int paramInt1, int paramInt2) {
/*  770 */     int i = getLength(paramInt1, paramInt2);
/*  771 */     long[] arrayOfLong = new long[i];
/*  772 */     System.arraycopy(paramArrayOflong, paramInt1, arrayOfLong, 0, Math.min(paramArrayOflong.length - paramInt1, i));
/*  773 */     return arrayOfLong;
/*      */   }
/*      */ 
/*      */   
/*      */   public static short[] copyOfRange(short[] paramArrayOfshort, int paramInt1, int paramInt2) {
/*  778 */     int i = getLength(paramInt1, paramInt2);
/*  779 */     short[] arrayOfShort = new short[i];
/*  780 */     System.arraycopy(paramArrayOfshort, paramInt1, arrayOfShort, 0, Math.min(paramArrayOfshort.length - paramInt1, i));
/*  781 */     return arrayOfShort;
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigInteger[] copyOfRange(BigInteger[] paramArrayOfBigInteger, int paramInt1, int paramInt2) {
/*  786 */     int i = getLength(paramInt1, paramInt2);
/*  787 */     BigInteger[] arrayOfBigInteger = new BigInteger[i];
/*  788 */     System.arraycopy(paramArrayOfBigInteger, paramInt1, arrayOfBigInteger, 0, Math.min(paramArrayOfBigInteger.length - paramInt1, i));
/*  789 */     return arrayOfBigInteger;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int getLength(int paramInt1, int paramInt2) {
/*  794 */     int i = paramInt2 - paramInt1;
/*  795 */     if (i < 0) {
/*      */       
/*  797 */       StringBuffer stringBuffer = new StringBuffer(paramInt1);
/*  798 */       stringBuffer.append(" > ").append(paramInt2);
/*  799 */       throw new IllegalArgumentException(stringBuffer.toString());
/*      */     } 
/*  801 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] append(byte[] paramArrayOfbyte, byte paramByte) {
/*  806 */     if (paramArrayOfbyte == null)
/*      */     {
/*  808 */       return new byte[] { paramByte };
/*      */     }
/*      */     
/*  811 */     int i = paramArrayOfbyte.length;
/*  812 */     byte[] arrayOfByte = new byte[i + 1];
/*  813 */     System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, 0, i);
/*  814 */     arrayOfByte[i] = paramByte;
/*  815 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   
/*      */   public static short[] append(short[] paramArrayOfshort, short paramShort) {
/*  820 */     if (paramArrayOfshort == null)
/*      */     {
/*  822 */       return new short[] { paramShort };
/*      */     }
/*      */     
/*  825 */     int i = paramArrayOfshort.length;
/*  826 */     short[] arrayOfShort = new short[i + 1];
/*  827 */     System.arraycopy(paramArrayOfshort, 0, arrayOfShort, 0, i);
/*  828 */     arrayOfShort[i] = paramShort;
/*  829 */     return arrayOfShort;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] append(int[] paramArrayOfint, int paramInt) {
/*  834 */     if (paramArrayOfint == null)
/*      */     {
/*  836 */       return new int[] { paramInt };
/*      */     }
/*      */     
/*  839 */     int i = paramArrayOfint.length;
/*  840 */     int[] arrayOfInt = new int[i + 1];
/*  841 */     System.arraycopy(paramArrayOfint, 0, arrayOfInt, 0, i);
/*  842 */     arrayOfInt[i] = paramInt;
/*  843 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String[] append(String[] paramArrayOfString, String paramString) {
/*  848 */     if (paramArrayOfString == null)
/*      */     {
/*  850 */       return new String[] { paramString };
/*      */     }
/*      */     
/*  853 */     int i = paramArrayOfString.length;
/*  854 */     String[] arrayOfString = new String[i + 1];
/*  855 */     System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, i);
/*  856 */     arrayOfString[i] = paramString;
/*  857 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] concatenate(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/*  862 */     if (null == paramArrayOfbyte1)
/*      */     {
/*      */       
/*  865 */       return clone(paramArrayOfbyte2);
/*      */     }
/*  867 */     if (null == paramArrayOfbyte2)
/*      */     {
/*      */       
/*  870 */       return clone(paramArrayOfbyte1);
/*      */     }
/*      */     
/*  873 */     byte[] arrayOfByte = new byte[paramArrayOfbyte1.length + paramArrayOfbyte2.length];
/*  874 */     System.arraycopy(paramArrayOfbyte1, 0, arrayOfByte, 0, paramArrayOfbyte1.length);
/*  875 */     System.arraycopy(paramArrayOfbyte2, 0, arrayOfByte, paramArrayOfbyte1.length, paramArrayOfbyte2.length);
/*  876 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   
/*      */   public static short[] concatenate(short[] paramArrayOfshort1, short[] paramArrayOfshort2) {
/*  881 */     if (null == paramArrayOfshort1)
/*      */     {
/*      */       
/*  884 */       return clone(paramArrayOfshort2);
/*      */     }
/*  886 */     if (null == paramArrayOfshort2)
/*      */     {
/*      */       
/*  889 */       return clone(paramArrayOfshort1);
/*      */     }
/*      */     
/*  892 */     short[] arrayOfShort = new short[paramArrayOfshort1.length + paramArrayOfshort2.length];
/*  893 */     System.arraycopy(paramArrayOfshort1, 0, arrayOfShort, 0, paramArrayOfshort1.length);
/*  894 */     System.arraycopy(paramArrayOfshort2, 0, arrayOfShort, paramArrayOfshort1.length, paramArrayOfshort2.length);
/*  895 */     return arrayOfShort;
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] concatenate(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3) {
/*  900 */     if (null == paramArrayOfbyte1)
/*      */     {
/*  902 */       return concatenate(paramArrayOfbyte2, paramArrayOfbyte3);
/*      */     }
/*  904 */     if (null == paramArrayOfbyte2)
/*      */     {
/*  906 */       return concatenate(paramArrayOfbyte1, paramArrayOfbyte3);
/*      */     }
/*  908 */     if (null == paramArrayOfbyte3)
/*      */     {
/*  910 */       return concatenate(paramArrayOfbyte1, paramArrayOfbyte2);
/*      */     }
/*      */     
/*  913 */     byte[] arrayOfByte = new byte[paramArrayOfbyte1.length + paramArrayOfbyte2.length + paramArrayOfbyte3.length];
/*  914 */     int i = 0;
/*  915 */     System.arraycopy(paramArrayOfbyte1, 0, arrayOfByte, i, paramArrayOfbyte1.length); i += paramArrayOfbyte1.length;
/*  916 */     System.arraycopy(paramArrayOfbyte2, 0, arrayOfByte, i, paramArrayOfbyte2.length); i += paramArrayOfbyte2.length;
/*  917 */     System.arraycopy(paramArrayOfbyte3, 0, arrayOfByte, i, paramArrayOfbyte3.length);
/*  918 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] concatenate(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, byte[] paramArrayOfbyte4) {
/*  923 */     if (null == paramArrayOfbyte1)
/*      */     {
/*  925 */       return concatenate(paramArrayOfbyte2, paramArrayOfbyte3, paramArrayOfbyte4);
/*      */     }
/*  927 */     if (null == paramArrayOfbyte2)
/*      */     {
/*  929 */       return concatenate(paramArrayOfbyte1, paramArrayOfbyte3, paramArrayOfbyte4);
/*      */     }
/*  931 */     if (null == paramArrayOfbyte3)
/*      */     {
/*  933 */       return concatenate(paramArrayOfbyte1, paramArrayOfbyte2, paramArrayOfbyte4);
/*      */     }
/*  935 */     if (null == paramArrayOfbyte4)
/*      */     {
/*  937 */       return concatenate(paramArrayOfbyte1, paramArrayOfbyte2, paramArrayOfbyte3);
/*      */     }
/*      */     
/*  940 */     byte[] arrayOfByte = new byte[paramArrayOfbyte1.length + paramArrayOfbyte2.length + paramArrayOfbyte3.length + paramArrayOfbyte4.length];
/*  941 */     int i = 0;
/*  942 */     System.arraycopy(paramArrayOfbyte1, 0, arrayOfByte, i, paramArrayOfbyte1.length); i += paramArrayOfbyte1.length;
/*  943 */     System.arraycopy(paramArrayOfbyte2, 0, arrayOfByte, i, paramArrayOfbyte2.length); i += paramArrayOfbyte2.length;
/*  944 */     System.arraycopy(paramArrayOfbyte3, 0, arrayOfByte, i, paramArrayOfbyte3.length); i += paramArrayOfbyte3.length;
/*  945 */     System.arraycopy(paramArrayOfbyte4, 0, arrayOfByte, i, paramArrayOfbyte4.length);
/*  946 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] concatenate(byte[][] paramArrayOfbyte) {
/*  951 */     int i = 0;
/*  952 */     for (byte b1 = 0; b1 != paramArrayOfbyte.length; b1++)
/*      */     {
/*  954 */       i += (paramArrayOfbyte[b1]).length;
/*      */     }
/*      */     
/*  957 */     byte[] arrayOfByte = new byte[i];
/*      */     
/*  959 */     int j = 0;
/*  960 */     for (byte b2 = 0; b2 != paramArrayOfbyte.length; b2++) {
/*      */       
/*  962 */       System.arraycopy(paramArrayOfbyte[b2], 0, arrayOfByte, j, (paramArrayOfbyte[b2]).length);
/*  963 */       j += (paramArrayOfbyte[b2]).length;
/*      */     } 
/*      */     
/*  966 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] concatenate(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  971 */     if (null == paramArrayOfint1)
/*      */     {
/*      */       
/*  974 */       return clone(paramArrayOfint2);
/*      */     }
/*  976 */     if (null == paramArrayOfint2)
/*      */     {
/*      */       
/*  979 */       return clone(paramArrayOfint1);
/*      */     }
/*      */     
/*  982 */     int[] arrayOfInt = new int[paramArrayOfint1.length + paramArrayOfint2.length];
/*  983 */     System.arraycopy(paramArrayOfint1, 0, arrayOfInt, 0, paramArrayOfint1.length);
/*  984 */     System.arraycopy(paramArrayOfint2, 0, arrayOfInt, paramArrayOfint1.length, paramArrayOfint2.length);
/*  985 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] prepend(byte[] paramArrayOfbyte, byte paramByte) {
/*  990 */     if (paramArrayOfbyte == null)
/*      */     {
/*  992 */       return new byte[] { paramByte };
/*      */     }
/*      */     
/*  995 */     int i = paramArrayOfbyte.length;
/*  996 */     byte[] arrayOfByte = new byte[i + 1];
/*  997 */     System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, 1, i);
/*  998 */     arrayOfByte[0] = paramByte;
/*  999 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   
/*      */   public static short[] prepend(short[] paramArrayOfshort, short paramShort) {
/* 1004 */     if (paramArrayOfshort == null)
/*      */     {
/* 1006 */       return new short[] { paramShort };
/*      */     }
/*      */     
/* 1009 */     int i = paramArrayOfshort.length;
/* 1010 */     short[] arrayOfShort = new short[i + 1];
/* 1011 */     System.arraycopy(paramArrayOfshort, 0, arrayOfShort, 1, i);
/* 1012 */     arrayOfShort[0] = paramShort;
/* 1013 */     return arrayOfShort;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] prepend(int[] paramArrayOfint, int paramInt) {
/* 1018 */     if (paramArrayOfint == null)
/*      */     {
/* 1020 */       return new int[] { paramInt };
/*      */     }
/*      */     
/* 1023 */     int i = paramArrayOfint.length;
/* 1024 */     int[] arrayOfInt = new int[i + 1];
/* 1025 */     System.arraycopy(paramArrayOfint, 0, arrayOfInt, 1, i);
/* 1026 */     arrayOfInt[0] = paramInt;
/* 1027 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] reverse(byte[] paramArrayOfbyte) {
/* 1032 */     if (paramArrayOfbyte == null)
/*      */     {
/* 1034 */       return null;
/*      */     }
/*      */     
/* 1037 */     byte b = 0; int i = paramArrayOfbyte.length;
/* 1038 */     byte[] arrayOfByte = new byte[i];
/*      */     
/* 1040 */     while (--i >= 0)
/*      */     {
/* 1042 */       arrayOfByte[i] = paramArrayOfbyte[b++];
/*      */     }
/*      */     
/* 1045 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] reverse(int[] paramArrayOfint) {
/* 1050 */     if (paramArrayOfint == null)
/*      */     {
/* 1052 */       return null;
/*      */     }
/*      */     
/* 1055 */     byte b = 0; int i = paramArrayOfint.length;
/* 1056 */     int[] arrayOfInt = new int[i];
/*      */     
/* 1058 */     while (--i >= 0)
/*      */     {
/* 1060 */       arrayOfInt[i] = paramArrayOfint[b++];
/*      */     }
/*      */     
/* 1063 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void clear(byte[] paramArrayOfbyte) {
/* 1117 */     if (null != paramArrayOfbyte)
/*      */     {
/* 1119 */       java.util.Arrays.fill(paramArrayOfbyte, (byte)0);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void clear(int[] paramArrayOfint) {
/* 1125 */     if (null != paramArrayOfint)
/*      */     {
/* 1127 */       java.util.Arrays.fill(paramArrayOfint, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isNullOrContainsNull(Object[] paramArrayOfObject) {
/* 1133 */     if (null == paramArrayOfObject)
/*      */     {
/* 1135 */       return true;
/*      */     }
/* 1137 */     int i = paramArrayOfObject.length;
/* 1138 */     for (byte b = 0; b < i; b++) {
/*      */       
/* 1140 */       if (null == paramArrayOfObject[b])
/*      */       {
/* 1142 */         return true;
/*      */       }
/*      */     } 
/* 1145 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isNullOrEmpty(byte[] paramArrayOfbyte) {
/* 1150 */     return (null == paramArrayOfbyte || paramArrayOfbyte.length < 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isNullOrEmpty(int[] paramArrayOfint) {
/* 1155 */     return (null == paramArrayOfint || paramArrayOfint.length < 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isNullOrEmpty(Object[] paramArrayOfObject) {
/* 1160 */     return (null == paramArrayOfObject || paramArrayOfObject.length < 1);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/util/Arrays.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */