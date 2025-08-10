/*      */ package META-INF.versions.9.org.bouncycastle.math.raw;
/*      */ 
/*      */ import java.math.BigInteger;
/*      */ import org.bouncycastle.util.Pack;
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Nat
/*      */ {
/*      */   private static final long M = 4294967295L;
/*      */   
/*      */   public static int add(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*   13 */     long l = 0L;
/*   14 */     for (byte b = 0; b < paramInt; b++) {
/*      */       
/*   16 */       l += (paramArrayOfint1[b] & 0xFFFFFFFFL) + (paramArrayOfint2[b] & 0xFFFFFFFFL);
/*   17 */       paramArrayOfint3[b] = (int)l;
/*   18 */       l >>>= 32L;
/*      */     } 
/*   20 */     return (int)l;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int add33At(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
/*   26 */     long l = (paramArrayOfint[paramInt3 + 0] & 0xFFFFFFFFL) + (paramInt2 & 0xFFFFFFFFL);
/*   27 */     paramArrayOfint[paramInt3 + 0] = (int)l;
/*   28 */     l >>>= 32L;
/*   29 */     l += (paramArrayOfint[paramInt3 + 1] & 0xFFFFFFFFL) + 1L;
/*   30 */     paramArrayOfint[paramInt3 + 1] = (int)l;
/*   31 */     l >>>= 32L;
/*   32 */     return (l == 0L) ? 0 : incAt(paramInt1, paramArrayOfint, paramInt3 + 2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int add33At(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3, int paramInt4) {
/*   38 */     long l = (paramArrayOfint[paramInt3 + paramInt4] & 0xFFFFFFFFL) + (paramInt2 & 0xFFFFFFFFL);
/*   39 */     paramArrayOfint[paramInt3 + paramInt4] = (int)l;
/*   40 */     l >>>= 32L;
/*   41 */     l += (paramArrayOfint[paramInt3 + paramInt4 + 1] & 0xFFFFFFFFL) + 1L;
/*   42 */     paramArrayOfint[paramInt3 + paramInt4 + 1] = (int)l;
/*   43 */     l >>>= 32L;
/*   44 */     return (l == 0L) ? 0 : incAt(paramInt1, paramArrayOfint, paramInt3, paramInt4 + 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int add33To(int paramInt1, int paramInt2, int[] paramArrayOfint) {
/*   49 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) + (paramInt2 & 0xFFFFFFFFL);
/*   50 */     paramArrayOfint[0] = (int)l;
/*   51 */     l >>>= 32L;
/*   52 */     l += (paramArrayOfint[1] & 0xFFFFFFFFL) + 1L;
/*   53 */     paramArrayOfint[1] = (int)l;
/*   54 */     l >>>= 32L;
/*   55 */     return (l == 0L) ? 0 : incAt(paramInt1, paramArrayOfint, 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int add33To(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
/*   60 */     long l = (paramArrayOfint[paramInt3 + 0] & 0xFFFFFFFFL) + (paramInt2 & 0xFFFFFFFFL);
/*   61 */     paramArrayOfint[paramInt3 + 0] = (int)l;
/*   62 */     l >>>= 32L;
/*   63 */     l += (paramArrayOfint[paramInt3 + 1] & 0xFFFFFFFFL) + 1L;
/*   64 */     paramArrayOfint[paramInt3 + 1] = (int)l;
/*   65 */     l >>>= 32L;
/*   66 */     return (l == 0L) ? 0 : incAt(paramInt1, paramArrayOfint, paramInt3, 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addBothTo(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*   71 */     long l = 0L;
/*   72 */     for (byte b = 0; b < paramInt; b++) {
/*      */       
/*   74 */       l += (paramArrayOfint1[b] & 0xFFFFFFFFL) + (paramArrayOfint2[b] & 0xFFFFFFFFL) + (paramArrayOfint3[b] & 0xFFFFFFFFL);
/*   75 */       paramArrayOfint3[b] = (int)l;
/*   76 */       l >>>= 32L;
/*      */     } 
/*   78 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addBothTo(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3, int[] paramArrayOfint3, int paramInt4) {
/*   83 */     long l = 0L;
/*   84 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*   86 */       l += (paramArrayOfint1[paramInt2 + b] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + b] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt4 + b] & 0xFFFFFFFFL);
/*   87 */       paramArrayOfint3[paramInt4 + b] = (int)l;
/*   88 */       l >>>= 32L;
/*      */     } 
/*   90 */     return (int)l;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int addDWordAt(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2) {
/*   96 */     long l = (paramArrayOfint[paramInt2 + 0] & 0xFFFFFFFFL) + (paramLong & 0xFFFFFFFFL);
/*   97 */     paramArrayOfint[paramInt2 + 0] = (int)l;
/*   98 */     l >>>= 32L;
/*   99 */     l += (paramArrayOfint[paramInt2 + 1] & 0xFFFFFFFFL) + (paramLong >>> 32L);
/*  100 */     paramArrayOfint[paramInt2 + 1] = (int)l;
/*  101 */     l >>>= 32L;
/*  102 */     return (l == 0L) ? 0 : incAt(paramInt1, paramArrayOfint, paramInt2 + 2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int addDWordAt(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2, int paramInt3) {
/*  108 */     long l = (paramArrayOfint[paramInt2 + paramInt3] & 0xFFFFFFFFL) + (paramLong & 0xFFFFFFFFL);
/*  109 */     paramArrayOfint[paramInt2 + paramInt3] = (int)l;
/*  110 */     l >>>= 32L;
/*  111 */     l += (paramArrayOfint[paramInt2 + paramInt3 + 1] & 0xFFFFFFFFL) + (paramLong >>> 32L);
/*  112 */     paramArrayOfint[paramInt2 + paramInt3 + 1] = (int)l;
/*  113 */     l >>>= 32L;
/*  114 */     return (l == 0L) ? 0 : incAt(paramInt1, paramArrayOfint, paramInt2, paramInt3 + 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addDWordTo(int paramInt, long paramLong, int[] paramArrayOfint) {
/*  119 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) + (paramLong & 0xFFFFFFFFL);
/*  120 */     paramArrayOfint[0] = (int)l;
/*  121 */     l >>>= 32L;
/*  122 */     l += (paramArrayOfint[1] & 0xFFFFFFFFL) + (paramLong >>> 32L);
/*  123 */     paramArrayOfint[1] = (int)l;
/*  124 */     l >>>= 32L;
/*  125 */     return (l == 0L) ? 0 : incAt(paramInt, paramArrayOfint, 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addDWordTo(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2) {
/*  130 */     long l = (paramArrayOfint[paramInt2 + 0] & 0xFFFFFFFFL) + (paramLong & 0xFFFFFFFFL);
/*  131 */     paramArrayOfint[paramInt2 + 0] = (int)l;
/*  132 */     l >>>= 32L;
/*  133 */     l += (paramArrayOfint[paramInt2 + 1] & 0xFFFFFFFFL) + (paramLong >>> 32L);
/*  134 */     paramArrayOfint[paramInt2 + 1] = (int)l;
/*  135 */     l >>>= 32L;
/*  136 */     return (l == 0L) ? 0 : incAt(paramInt1, paramArrayOfint, paramInt2, 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addTo(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  141 */     long l = 0L;
/*  142 */     for (byte b = 0; b < paramInt; b++) {
/*      */       
/*  144 */       l += (paramArrayOfint1[b] & 0xFFFFFFFFL) + (paramArrayOfint2[b] & 0xFFFFFFFFL);
/*  145 */       paramArrayOfint2[b] = (int)l;
/*  146 */       l >>>= 32L;
/*      */     } 
/*  148 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addTo(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/*  153 */     long l = 0L;
/*  154 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*  156 */       l += (paramArrayOfint1[paramInt2 + b] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + b] & 0xFFFFFFFFL);
/*  157 */       paramArrayOfint2[paramInt3 + b] = (int)l;
/*  158 */       l >>>= 32L;
/*      */     } 
/*  160 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addTo(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3, int paramInt4) {
/*  165 */     long l = paramInt4 & 0xFFFFFFFFL;
/*  166 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*  168 */       l += (paramArrayOfint1[paramInt2 + b] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + b] & 0xFFFFFFFFL);
/*  169 */       paramArrayOfint2[paramInt3 + b] = (int)l;
/*  170 */       l >>>= 32L;
/*      */     } 
/*  172 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addToEachOther(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/*  177 */     long l = 0L;
/*  178 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*  180 */       l += (paramArrayOfint1[paramInt2 + b] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + b] & 0xFFFFFFFFL);
/*  181 */       paramArrayOfint1[paramInt2 + b] = (int)l;
/*  182 */       paramArrayOfint2[paramInt3 + b] = (int)l;
/*  183 */       l >>>= 32L;
/*      */     } 
/*  185 */     return (int)l;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int addWordAt(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
/*  191 */     long l = (paramInt2 & 0xFFFFFFFFL) + (paramArrayOfint[paramInt3] & 0xFFFFFFFFL);
/*  192 */     paramArrayOfint[paramInt3] = (int)l;
/*  193 */     l >>>= 32L;
/*  194 */     return (l == 0L) ? 0 : incAt(paramInt1, paramArrayOfint, paramInt3 + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int addWordAt(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3, int paramInt4) {
/*  200 */     long l = (paramInt2 & 0xFFFFFFFFL) + (paramArrayOfint[paramInt3 + paramInt4] & 0xFFFFFFFFL);
/*  201 */     paramArrayOfint[paramInt3 + paramInt4] = (int)l;
/*  202 */     l >>>= 32L;
/*  203 */     return (l == 0L) ? 0 : incAt(paramInt1, paramArrayOfint, paramInt3, paramInt4 + 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addWordTo(int paramInt1, int paramInt2, int[] paramArrayOfint) {
/*  208 */     long l = (paramInt2 & 0xFFFFFFFFL) + (paramArrayOfint[0] & 0xFFFFFFFFL);
/*  209 */     paramArrayOfint[0] = (int)l;
/*  210 */     l >>>= 32L;
/*  211 */     return (l == 0L) ? 0 : incAt(paramInt1, paramArrayOfint, 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addWordTo(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
/*  216 */     long l = (paramInt2 & 0xFFFFFFFFL) + (paramArrayOfint[paramInt3] & 0xFFFFFFFFL);
/*  217 */     paramArrayOfint[paramInt3] = (int)l;
/*  218 */     l >>>= 32L;
/*  219 */     return (l == 0L) ? 0 : incAt(paramInt1, paramArrayOfint, paramInt3, 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int cadd(int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  224 */     long l1 = -(paramInt2 & 0x1) & 0xFFFFFFFFL;
/*  225 */     long l2 = 0L;
/*  226 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*  228 */       l2 += (paramArrayOfint1[b] & 0xFFFFFFFFL) + (paramArrayOfint2[b] & l1);
/*  229 */       paramArrayOfint3[b] = (int)l2;
/*  230 */       l2 >>>= 32L;
/*      */     } 
/*  232 */     return (int)l2;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void cmov(int paramInt1, int paramInt2, int[] paramArrayOfint1, int paramInt3, int[] paramArrayOfint2, int paramInt4) {
/*  237 */     paramInt2 = -(paramInt2 & 0x1);
/*      */     
/*  239 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*  241 */       int i = paramArrayOfint2[paramInt4 + b], j = i ^ paramArrayOfint1[paramInt3 + b];
/*  242 */       i ^= j & paramInt2;
/*  243 */       paramArrayOfint2[paramInt4 + b] = i;
/*      */     } 
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
/*      */   public static int compare(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  259 */     for (int i = paramInt - 1; i >= 0; i--) {
/*      */       
/*  261 */       int j = paramArrayOfint1[i] ^ Integer.MIN_VALUE;
/*  262 */       int k = paramArrayOfint2[i] ^ Integer.MIN_VALUE;
/*  263 */       if (j < k)
/*  264 */         return -1; 
/*  265 */       if (j > k)
/*  266 */         return 1; 
/*      */     } 
/*  268 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int compare(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/*  273 */     for (int i = paramInt1 - 1; i >= 0; i--) {
/*      */       
/*  275 */       int j = paramArrayOfint1[paramInt2 + i] ^ Integer.MIN_VALUE;
/*  276 */       int k = paramArrayOfint2[paramInt3 + i] ^ Integer.MIN_VALUE;
/*  277 */       if (j < k)
/*  278 */         return -1; 
/*  279 */       if (j > k)
/*  280 */         return 1; 
/*      */     } 
/*  282 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] copy(int paramInt, int[] paramArrayOfint) {
/*  287 */     int[] arrayOfInt = new int[paramInt];
/*  288 */     System.arraycopy(paramArrayOfint, 0, arrayOfInt, 0, paramInt);
/*  289 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void copy(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  294 */     System.arraycopy(paramArrayOfint1, 0, paramArrayOfint2, 0, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void copy(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/*  299 */     System.arraycopy(paramArrayOfint1, paramInt2, paramArrayOfint2, paramInt3, paramInt1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static long[] copy64(int paramInt, long[] paramArrayOflong) {
/*  304 */     long[] arrayOfLong = new long[paramInt];
/*  305 */     System.arraycopy(paramArrayOflong, 0, arrayOfLong, 0, paramInt);
/*  306 */     return arrayOfLong;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void copy64(int paramInt, long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  311 */     System.arraycopy(paramArrayOflong1, 0, paramArrayOflong2, 0, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void copy64(int paramInt1, long[] paramArrayOflong1, int paramInt2, long[] paramArrayOflong2, int paramInt3) {
/*  316 */     System.arraycopy(paramArrayOflong1, paramInt2, paramArrayOflong2, paramInt3, paramInt1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] create(int paramInt) {
/*  321 */     return new int[paramInt];
/*      */   }
/*      */ 
/*      */   
/*      */   public static long[] create64(int paramInt) {
/*  326 */     return new long[paramInt];
/*      */   }
/*      */ 
/*      */   
/*      */   public static int csub(int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  331 */     long l1 = -(paramInt2 & 0x1) & 0xFFFFFFFFL;
/*  332 */     long l2 = 0L;
/*  333 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*  335 */       l2 += (paramArrayOfint1[b] & 0xFFFFFFFFL) - (paramArrayOfint2[b] & l1);
/*  336 */       paramArrayOfint3[b] = (int)l2;
/*  337 */       l2 >>= 32L;
/*      */     } 
/*  339 */     return (int)l2;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int csub(int paramInt1, int paramInt2, int[] paramArrayOfint1, int paramInt3, int[] paramArrayOfint2, int paramInt4, int[] paramArrayOfint3, int paramInt5) {
/*  344 */     long l1 = -(paramInt2 & 0x1) & 0xFFFFFFFFL;
/*  345 */     long l2 = 0L;
/*  346 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*  348 */       l2 += (paramArrayOfint1[paramInt3 + b] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt4 + b] & l1);
/*  349 */       paramArrayOfint3[paramInt5 + b] = (int)l2;
/*  350 */       l2 >>= 32L;
/*      */     } 
/*  352 */     return (int)l2;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int dec(int paramInt, int[] paramArrayOfint) {
/*  357 */     for (byte b = 0; b < paramInt; b++) {
/*      */       
/*  359 */       paramArrayOfint[b] = paramArrayOfint[b] - 1; if (paramArrayOfint[b] - 1 != -1)
/*      */       {
/*  361 */         return 0;
/*      */       }
/*      */     } 
/*  364 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int dec(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  369 */     byte b = 0;
/*  370 */     while (b < paramInt) {
/*      */       
/*  372 */       int i = paramArrayOfint1[b] - 1;
/*  373 */       paramArrayOfint2[b] = i;
/*  374 */       b++;
/*  375 */       if (i != -1) {
/*      */         
/*  377 */         while (b < paramInt) {
/*      */           
/*  379 */           paramArrayOfint2[b] = paramArrayOfint1[b];
/*  380 */           b++;
/*      */         } 
/*  382 */         return 0;
/*      */       } 
/*      */     } 
/*  385 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int decAt(int paramInt1, int[] paramArrayOfint, int paramInt2) {
/*  391 */     for (int i = paramInt2; i < paramInt1; i++) {
/*      */       
/*  393 */       paramArrayOfint[i] = paramArrayOfint[i] - 1; if (paramArrayOfint[i] - 1 != -1)
/*      */       {
/*  395 */         return 0;
/*      */       }
/*      */     } 
/*  398 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int decAt(int paramInt1, int[] paramArrayOfint, int paramInt2, int paramInt3) {
/*  404 */     for (int i = paramInt3; i < paramInt1; i++) {
/*      */       
/*  406 */       paramArrayOfint[paramInt2 + i] = paramArrayOfint[paramInt2 + i] - 1; if (paramArrayOfint[paramInt2 + i] - 1 != -1)
/*      */       {
/*  408 */         return 0;
/*      */       }
/*      */     } 
/*  411 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean diff(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3, int[] paramArrayOfint3, int paramInt4) {
/*  416 */     boolean bool = gte(paramInt1, paramArrayOfint1, paramInt2, paramArrayOfint2, paramInt3);
/*  417 */     if (bool) {
/*      */       
/*  419 */       sub(paramInt1, paramArrayOfint1, paramInt2, paramArrayOfint2, paramInt3, paramArrayOfint3, paramInt4);
/*      */     }
/*      */     else {
/*      */       
/*  423 */       sub(paramInt1, paramArrayOfint2, paramInt3, paramArrayOfint1, paramInt2, paramArrayOfint3, paramInt4);
/*      */     } 
/*  425 */     return bool;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean eq(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  430 */     for (int i = paramInt - 1; i >= 0; i--) {
/*      */       
/*  432 */       if (paramArrayOfint1[i] != paramArrayOfint2[i])
/*      */       {
/*  434 */         return false;
/*      */       }
/*      */     } 
/*  437 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int equalTo(int paramInt1, int[] paramArrayOfint, int paramInt2) {
/*  442 */     int i = paramArrayOfint[0] ^ paramInt2;
/*  443 */     for (byte b = 1; b < paramInt1; b++)
/*      */     {
/*  445 */       i |= paramArrayOfint[b];
/*      */     }
/*  447 */     i = i >>> 1 | i & 0x1;
/*  448 */     return i - 1 >> 31;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int equalTo(int paramInt1, int[] paramArrayOfint, int paramInt2, int paramInt3) {
/*  453 */     int i = paramArrayOfint[paramInt2] ^ paramInt3;
/*  454 */     for (byte b = 1; b < paramInt1; b++)
/*      */     {
/*  456 */       i |= paramArrayOfint[paramInt2 + b];
/*      */     }
/*  458 */     i = i >>> 1 | i & 0x1;
/*  459 */     return i - 1 >> 31;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int equalTo(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  464 */     int i = 0;
/*  465 */     for (byte b = 0; b < paramInt; b++)
/*      */     {
/*  467 */       i |= paramArrayOfint1[b] ^ paramArrayOfint2[b];
/*      */     }
/*  469 */     i = i >>> 1 | i & 0x1;
/*  470 */     return i - 1 >> 31;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int equalTo(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/*  475 */     int i = 0;
/*  476 */     for (byte b = 0; b < paramInt1; b++)
/*      */     {
/*  478 */       i |= paramArrayOfint1[paramInt2 + b] ^ paramArrayOfint2[paramInt3 + b];
/*      */     }
/*  480 */     i = i >>> 1 | i & 0x1;
/*  481 */     return i - 1 >> 31;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int equalToZero(int paramInt, int[] paramArrayOfint) {
/*  486 */     int i = 0;
/*  487 */     for (byte b = 0; b < paramInt; b++)
/*      */     {
/*  489 */       i |= paramArrayOfint[b];
/*      */     }
/*  491 */     i = i >>> 1 | i & 0x1;
/*  492 */     return i - 1 >> 31;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int equalToZero(int paramInt1, int[] paramArrayOfint, int paramInt2) {
/*  497 */     int i = 0;
/*  498 */     for (byte b = 0; b < paramInt1; b++)
/*      */     {
/*  500 */       i |= paramArrayOfint[paramInt2 + b];
/*      */     }
/*  502 */     i = i >>> 1 | i & 0x1;
/*  503 */     return i - 1 >> 31;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] fromBigInteger(int paramInt, BigInteger paramBigInteger) {
/*  508 */     if (paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > paramInt)
/*      */     {
/*  510 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*  513 */     int i = paramInt + 31 >> 5;
/*  514 */     int[] arrayOfInt = create(i);
/*      */ 
/*      */     
/*  517 */     for (byte b = 0; b < i; b++) {
/*      */       
/*  519 */       arrayOfInt[b] = paramBigInteger.intValue();
/*  520 */       paramBigInteger = paramBigInteger.shiftRight(32);
/*      */     } 
/*  522 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   
/*      */   public static long[] fromBigInteger64(int paramInt, BigInteger paramBigInteger) {
/*  527 */     if (paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > paramInt)
/*      */     {
/*  529 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*  532 */     int i = paramInt + 63 >> 6;
/*  533 */     long[] arrayOfLong = create64(i);
/*      */ 
/*      */     
/*  536 */     for (byte b = 0; b < i; b++) {
/*      */       
/*  538 */       arrayOfLong[b] = paramBigInteger.longValue();
/*  539 */       paramBigInteger = paramBigInteger.shiftRight(64);
/*      */     } 
/*  541 */     return arrayOfLong;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getBit(int[] paramArrayOfint, int paramInt) {
/*  546 */     if (paramInt == 0)
/*      */     {
/*  548 */       return paramArrayOfint[0] & 0x1;
/*      */     }
/*  550 */     int i = paramInt >> 5;
/*  551 */     if (i < 0 || i >= paramArrayOfint.length)
/*      */     {
/*  553 */       return 0;
/*      */     }
/*  555 */     int j = paramInt & 0x1F;
/*  556 */     return paramArrayOfint[i] >>> j & 0x1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean gte(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  561 */     for (int i = paramInt - 1; i >= 0; i--) {
/*      */       
/*  563 */       int j = paramArrayOfint1[i] ^ Integer.MIN_VALUE;
/*  564 */       int k = paramArrayOfint2[i] ^ Integer.MIN_VALUE;
/*  565 */       if (j < k)
/*  566 */         return false; 
/*  567 */       if (j > k)
/*  568 */         return true; 
/*      */     } 
/*  570 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean gte(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/*  575 */     for (int i = paramInt1 - 1; i >= 0; i--) {
/*      */       
/*  577 */       int j = paramArrayOfint1[paramInt2 + i] ^ Integer.MIN_VALUE;
/*  578 */       int k = paramArrayOfint2[paramInt3 + i] ^ Integer.MIN_VALUE;
/*  579 */       if (j < k)
/*  580 */         return false; 
/*  581 */       if (j > k)
/*  582 */         return true; 
/*      */     } 
/*  584 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int inc(int paramInt, int[] paramArrayOfint) {
/*  589 */     for (byte b = 0; b < paramInt; b++) {
/*      */       
/*  591 */       paramArrayOfint[b] = paramArrayOfint[b] + 1; if (paramArrayOfint[b] + 1 != 0)
/*      */       {
/*  593 */         return 0;
/*      */       }
/*      */     } 
/*  596 */     return 1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int inc(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  601 */     byte b = 0;
/*  602 */     while (b < paramInt) {
/*      */       
/*  604 */       int i = paramArrayOfint1[b] + 1;
/*  605 */       paramArrayOfint2[b] = i;
/*  606 */       b++;
/*  607 */       if (i != 0) {
/*      */         
/*  609 */         while (b < paramInt) {
/*      */           
/*  611 */           paramArrayOfint2[b] = paramArrayOfint1[b];
/*  612 */           b++;
/*      */         } 
/*  614 */         return 0;
/*      */       } 
/*      */     } 
/*  617 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int incAt(int paramInt1, int[] paramArrayOfint, int paramInt2) {
/*  623 */     for (int i = paramInt2; i < paramInt1; i++) {
/*      */       
/*  625 */       paramArrayOfint[i] = paramArrayOfint[i] + 1; if (paramArrayOfint[i] + 1 != 0)
/*      */       {
/*  627 */         return 0;
/*      */       }
/*      */     } 
/*  630 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int incAt(int paramInt1, int[] paramArrayOfint, int paramInt2, int paramInt3) {
/*  636 */     for (int i = paramInt3; i < paramInt1; i++) {
/*      */       
/*  638 */       paramArrayOfint[paramInt2 + i] = paramArrayOfint[paramInt2 + i] + 1; if (paramArrayOfint[paramInt2 + i] + 1 != 0)
/*      */       {
/*  640 */         return 0;
/*      */       }
/*      */     } 
/*  643 */     return 1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isOne(int paramInt, int[] paramArrayOfint) {
/*  648 */     if (paramArrayOfint[0] != 1)
/*      */     {
/*  650 */       return false;
/*      */     }
/*  652 */     for (byte b = 1; b < paramInt; b++) {
/*      */       
/*  654 */       if (paramArrayOfint[b] != 0)
/*      */       {
/*  656 */         return false;
/*      */       }
/*      */     } 
/*  659 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isZero(int paramInt, int[] paramArrayOfint) {
/*  664 */     for (byte b = 0; b < paramInt; b++) {
/*      */       
/*  666 */       if (paramArrayOfint[b] != 0)
/*      */       {
/*  668 */         return false;
/*      */       }
/*      */     } 
/*  671 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int lessThan(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  676 */     long l = 0L;
/*  677 */     for (byte b = 0; b < paramInt; b++) {
/*      */       
/*  679 */       l += (paramArrayOfint1[b] & 0xFFFFFFFFL) - (paramArrayOfint2[b] & 0xFFFFFFFFL);
/*  680 */       l >>= 32L;
/*      */     } 
/*      */     
/*  683 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int lessThan(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/*  688 */     long l = 0L;
/*  689 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*  691 */       l += (paramArrayOfint1[paramInt2 + b] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt3 + b] & 0xFFFFFFFFL);
/*  692 */       l >>= 32L;
/*      */     } 
/*      */     
/*  695 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void mul(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  700 */     paramArrayOfint3[paramInt] = mulWord(paramInt, paramArrayOfint1[0], paramArrayOfint2, paramArrayOfint3);
/*      */     
/*  702 */     for (byte b = 1; b < paramInt; b++)
/*      */     {
/*  704 */       paramArrayOfint3[b + paramInt] = mulWordAddTo(paramInt, paramArrayOfint1[b], paramArrayOfint2, 0, paramArrayOfint3, b);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void mul(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3, int[] paramArrayOfint3, int paramInt4) {
/*  710 */     paramArrayOfint3[paramInt4 + paramInt1] = mulWord(paramInt1, paramArrayOfint1[paramInt2], paramArrayOfint2, paramInt3, paramArrayOfint3, paramInt4);
/*      */     
/*  712 */     for (byte b = 1; b < paramInt1; b++)
/*      */     {
/*  714 */       paramArrayOfint3[paramInt4 + b + paramInt1] = mulWordAddTo(paramInt1, paramArrayOfint1[paramInt2 + b], paramArrayOfint2, paramInt3, paramArrayOfint3, paramInt4 + b);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void mul(int[] paramArrayOfint1, int paramInt1, int paramInt2, int[] paramArrayOfint2, int paramInt3, int paramInt4, int[] paramArrayOfint3, int paramInt5) {
/*  720 */     paramArrayOfint3[paramInt5 + paramInt4] = mulWord(paramInt4, paramArrayOfint1[paramInt1], paramArrayOfint2, paramInt3, paramArrayOfint3, paramInt5);
/*      */     
/*  722 */     for (byte b = 1; b < paramInt2; b++)
/*      */     {
/*  724 */       paramArrayOfint3[paramInt5 + b + paramInt4] = mulWordAddTo(paramInt4, paramArrayOfint1[paramInt1 + b], paramArrayOfint2, paramInt3, paramArrayOfint3, paramInt5 + b);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulAddTo(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  730 */     long l = 0L;
/*  731 */     for (byte b = 0; b < paramInt; b++) {
/*      */       
/*  733 */       l += mulWordAddTo(paramInt, paramArrayOfint1[b], paramArrayOfint2, 0, paramArrayOfint3, b) & 0xFFFFFFFFL;
/*  734 */       l += paramArrayOfint3[b + paramInt] & 0xFFFFFFFFL;
/*  735 */       paramArrayOfint3[b + paramInt] = (int)l;
/*  736 */       l >>>= 32L;
/*      */     } 
/*  738 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulAddTo(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3, int[] paramArrayOfint3, int paramInt4) {
/*  743 */     long l = 0L;
/*  744 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*  746 */       l += mulWordAddTo(paramInt1, paramArrayOfint1[paramInt2 + b], paramArrayOfint2, paramInt3, paramArrayOfint3, paramInt4) & 0xFFFFFFFFL;
/*  747 */       l += paramArrayOfint3[paramInt4 + paramInt1] & 0xFFFFFFFFL;
/*  748 */       paramArrayOfint3[paramInt4 + paramInt1] = (int)l;
/*  749 */       l >>>= 32L;
/*  750 */       paramInt4++;
/*      */     } 
/*  752 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mul31BothAdd(int paramInt1, int paramInt2, int[] paramArrayOfint1, int paramInt3, int[] paramArrayOfint2, int[] paramArrayOfint3, int paramInt4) {
/*  757 */     long l1 = 0L, l2 = paramInt2 & 0xFFFFFFFFL, l3 = paramInt3 & 0xFFFFFFFFL;
/*  758 */     byte b = 0;
/*      */     
/*      */     while (true) {
/*  761 */       l1 += l2 * (paramArrayOfint1[b] & 0xFFFFFFFFL) + l3 * (paramArrayOfint2[b] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt4 + b] & 0xFFFFFFFFL);
/*  762 */       paramArrayOfint3[paramInt4 + b] = (int)l1;
/*  763 */       l1 >>>= 32L;
/*      */       
/*  765 */       if (++b >= paramInt1)
/*  766 */         return (int)l1; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static int mulWord(int paramInt1, int paramInt2, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  771 */     long l1 = 0L, l2 = paramInt2 & 0xFFFFFFFFL;
/*  772 */     byte b = 0;
/*      */     
/*      */     while (true) {
/*  775 */       l1 += l2 * (paramArrayOfint1[b] & 0xFFFFFFFFL);
/*  776 */       paramArrayOfint2[b] = (int)l1;
/*  777 */       l1 >>>= 32L;
/*      */       
/*  779 */       if (++b >= paramInt1)
/*  780 */         return (int)l1; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static int mulWord(int paramInt1, int paramInt2, int[] paramArrayOfint1, int paramInt3, int[] paramArrayOfint2, int paramInt4) {
/*  785 */     long l1 = 0L, l2 = paramInt2 & 0xFFFFFFFFL;
/*  786 */     byte b = 0;
/*      */     
/*      */     while (true) {
/*  789 */       l1 += l2 * (paramArrayOfint1[paramInt3 + b] & 0xFFFFFFFFL);
/*  790 */       paramArrayOfint2[paramInt4 + b] = (int)l1;
/*  791 */       l1 >>>= 32L;
/*      */       
/*  793 */       if (++b >= paramInt1)
/*  794 */         return (int)l1; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static int mulWordAddTo(int paramInt1, int paramInt2, int[] paramArrayOfint1, int paramInt3, int[] paramArrayOfint2, int paramInt4) {
/*  799 */     long l1 = 0L, l2 = paramInt2 & 0xFFFFFFFFL;
/*  800 */     byte b = 0;
/*      */     
/*      */     while (true) {
/*  803 */       l1 += l2 * (paramArrayOfint1[paramInt3 + b] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt4 + b] & 0xFFFFFFFFL);
/*  804 */       paramArrayOfint2[paramInt4 + b] = (int)l1;
/*  805 */       l1 >>>= 32L;
/*      */       
/*  807 */       if (++b >= paramInt1) {
/*  808 */         return (int)l1;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public static int mulWordDwordAddAt(int paramInt1, int paramInt2, long paramLong, int[] paramArrayOfint, int paramInt3) {
/*  814 */     long l1 = 0L, l2 = paramInt2 & 0xFFFFFFFFL;
/*  815 */     l1 += l2 * (paramLong & 0xFFFFFFFFL) + (paramArrayOfint[paramInt3 + 0] & 0xFFFFFFFFL);
/*  816 */     paramArrayOfint[paramInt3 + 0] = (int)l1;
/*  817 */     l1 >>>= 32L;
/*  818 */     l1 += l2 * (paramLong >>> 32L) + (paramArrayOfint[paramInt3 + 1] & 0xFFFFFFFFL);
/*  819 */     paramArrayOfint[paramInt3 + 1] = (int)l1;
/*  820 */     l1 >>>= 32L;
/*  821 */     l1 += paramArrayOfint[paramInt3 + 2] & 0xFFFFFFFFL;
/*  822 */     paramArrayOfint[paramInt3 + 2] = (int)l1;
/*  823 */     l1 >>>= 32L;
/*  824 */     return (l1 == 0L) ? 0 : incAt(paramInt1, paramArrayOfint, paramInt3 + 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int shiftDownBit(int paramInt1, int[] paramArrayOfint, int paramInt2) {
/*  829 */     int i = paramInt1;
/*  830 */     while (--i >= 0) {
/*      */       
/*  832 */       int j = paramArrayOfint[i];
/*  833 */       paramArrayOfint[i] = j >>> 1 | paramInt2 << 31;
/*  834 */       paramInt2 = j;
/*      */     } 
/*  836 */     return paramInt2 << 31;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int shiftDownBit(int paramInt1, int[] paramArrayOfint, int paramInt2, int paramInt3) {
/*  841 */     int i = paramInt1;
/*  842 */     while (--i >= 0) {
/*      */       
/*  844 */       int j = paramArrayOfint[paramInt2 + i];
/*  845 */       paramArrayOfint[paramInt2 + i] = j >>> 1 | paramInt3 << 31;
/*  846 */       paramInt3 = j;
/*      */     } 
/*  848 */     return paramInt3 << 31;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int shiftDownBit(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2) {
/*  853 */     int i = paramInt1;
/*  854 */     while (--i >= 0) {
/*      */       
/*  856 */       int j = paramArrayOfint1[i];
/*  857 */       paramArrayOfint2[i] = j >>> 1 | paramInt2 << 31;
/*  858 */       paramInt2 = j;
/*      */     } 
/*  860 */     return paramInt2 << 31;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int shiftDownBit(int paramInt1, int[] paramArrayOfint1, int paramInt2, int paramInt3, int[] paramArrayOfint2, int paramInt4) {
/*  865 */     int i = paramInt1;
/*  866 */     while (--i >= 0) {
/*      */       
/*  868 */       int j = paramArrayOfint1[paramInt2 + i];
/*  869 */       paramArrayOfint2[paramInt4 + i] = j >>> 1 | paramInt3 << 31;
/*  870 */       paramInt3 = j;
/*      */     } 
/*  872 */     return paramInt3 << 31;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int shiftDownBits(int paramInt1, int[] paramArrayOfint, int paramInt2, int paramInt3) {
/*  878 */     int i = paramInt1;
/*  879 */     while (--i >= 0) {
/*      */       
/*  881 */       int j = paramArrayOfint[i];
/*  882 */       paramArrayOfint[i] = j >>> paramInt2 | paramInt3 << -paramInt2;
/*  883 */       paramInt3 = j;
/*      */     } 
/*  885 */     return paramInt3 << -paramInt2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int shiftDownBits(int paramInt1, int[] paramArrayOfint, int paramInt2, int paramInt3, int paramInt4) {
/*  891 */     int i = paramInt1;
/*  892 */     while (--i >= 0) {
/*      */       
/*  894 */       int j = paramArrayOfint[paramInt2 + i];
/*  895 */       paramArrayOfint[paramInt2 + i] = j >>> paramInt3 | paramInt4 << -paramInt3;
/*  896 */       paramInt4 = j;
/*      */     } 
/*  898 */     return paramInt4 << -paramInt3;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int shiftDownBits(int paramInt1, int[] paramArrayOfint1, int paramInt2, int paramInt3, int[] paramArrayOfint2) {
/*  904 */     int i = paramInt1;
/*  905 */     while (--i >= 0) {
/*      */       
/*  907 */       int j = paramArrayOfint1[i];
/*  908 */       paramArrayOfint2[i] = j >>> paramInt2 | paramInt3 << -paramInt2;
/*  909 */       paramInt3 = j;
/*      */     } 
/*  911 */     return paramInt3 << -paramInt2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int shiftDownBits(int paramInt1, int[] paramArrayOfint1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint2, int paramInt5) {
/*  917 */     int i = paramInt1;
/*  918 */     while (--i >= 0) {
/*      */       
/*  920 */       int j = paramArrayOfint1[paramInt2 + i];
/*  921 */       paramArrayOfint2[paramInt5 + i] = j >>> paramInt3 | paramInt4 << -paramInt3;
/*  922 */       paramInt4 = j;
/*      */     } 
/*  924 */     return paramInt4 << -paramInt3;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int shiftDownWord(int paramInt1, int[] paramArrayOfint, int paramInt2) {
/*  929 */     int i = paramInt1;
/*  930 */     while (--i >= 0) {
/*      */       
/*  932 */       int j = paramArrayOfint[i];
/*  933 */       paramArrayOfint[i] = paramInt2;
/*  934 */       paramInt2 = j;
/*      */     } 
/*  936 */     return paramInt2;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int shiftUpBit(int paramInt1, int[] paramArrayOfint, int paramInt2) {
/*  941 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*  943 */       int i = paramArrayOfint[b];
/*  944 */       paramArrayOfint[b] = i << 1 | paramInt2 >>> 31;
/*  945 */       paramInt2 = i;
/*      */     } 
/*  947 */     return paramInt2 >>> 31;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int shiftUpBit(int paramInt1, int[] paramArrayOfint, int paramInt2, int paramInt3) {
/*  952 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*  954 */       int i = paramArrayOfint[paramInt2 + b];
/*  955 */       paramArrayOfint[paramInt2 + b] = i << 1 | paramInt3 >>> 31;
/*  956 */       paramInt3 = i;
/*      */     } 
/*  958 */     return paramInt3 >>> 31;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int shiftUpBit(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2) {
/*  963 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*  965 */       int i = paramArrayOfint1[b];
/*  966 */       paramArrayOfint2[b] = i << 1 | paramInt2 >>> 31;
/*  967 */       paramInt2 = i;
/*      */     } 
/*  969 */     return paramInt2 >>> 31;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int shiftUpBit(int paramInt1, int[] paramArrayOfint1, int paramInt2, int paramInt3, int[] paramArrayOfint2, int paramInt4) {
/*  974 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*  976 */       int i = paramArrayOfint1[paramInt2 + b];
/*  977 */       paramArrayOfint2[paramInt4 + b] = i << 1 | paramInt3 >>> 31;
/*  978 */       paramInt3 = i;
/*      */     } 
/*  980 */     return paramInt3 >>> 31;
/*      */   }
/*      */ 
/*      */   
/*      */   public static long shiftUpBit64(int paramInt1, long[] paramArrayOflong1, int paramInt2, long paramLong, long[] paramArrayOflong2, int paramInt3) {
/*  985 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*  987 */       long l = paramArrayOflong1[paramInt2 + b];
/*  988 */       paramArrayOflong2[paramInt3 + b] = l << 1L | paramLong >>> 63L;
/*  989 */       paramLong = l;
/*      */     } 
/*  991 */     return paramLong >>> 63L;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int shiftUpBits(int paramInt1, int[] paramArrayOfint, int paramInt2, int paramInt3) {
/*  997 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/*  999 */       int i = paramArrayOfint[b];
/* 1000 */       paramArrayOfint[b] = i << paramInt2 | paramInt3 >>> -paramInt2;
/* 1001 */       paramInt3 = i;
/*      */     } 
/* 1003 */     return paramInt3 >>> -paramInt2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int shiftUpBits(int paramInt1, int[] paramArrayOfint, int paramInt2, int paramInt3, int paramInt4) {
/* 1009 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/* 1011 */       int i = paramArrayOfint[paramInt2 + b];
/* 1012 */       paramArrayOfint[paramInt2 + b] = i << paramInt3 | paramInt4 >>> -paramInt3;
/* 1013 */       paramInt4 = i;
/*      */     } 
/* 1015 */     return paramInt4 >>> -paramInt3;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static long shiftUpBits64(int paramInt1, long[] paramArrayOflong, int paramInt2, int paramInt3, long paramLong) {
/* 1021 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/* 1023 */       long l = paramArrayOflong[paramInt2 + b];
/* 1024 */       paramArrayOflong[paramInt2 + b] = l << paramInt3 | paramLong >>> -paramInt3;
/* 1025 */       paramLong = l;
/*      */     } 
/* 1027 */     return paramLong >>> -paramInt3;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int shiftUpBits(int paramInt1, int[] paramArrayOfint1, int paramInt2, int paramInt3, int[] paramArrayOfint2) {
/* 1033 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/* 1035 */       int i = paramArrayOfint1[b];
/* 1036 */       paramArrayOfint2[b] = i << paramInt2 | paramInt3 >>> -paramInt2;
/* 1037 */       paramInt3 = i;
/*      */     } 
/* 1039 */     return paramInt3 >>> -paramInt2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int shiftUpBits(int paramInt1, int[] paramArrayOfint1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint2, int paramInt5) {
/* 1045 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/* 1047 */       int i = paramArrayOfint1[paramInt2 + b];
/* 1048 */       paramArrayOfint2[paramInt5 + b] = i << paramInt3 | paramInt4 >>> -paramInt3;
/* 1049 */       paramInt4 = i;
/*      */     } 
/* 1051 */     return paramInt4 >>> -paramInt3;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static long shiftUpBits64(int paramInt1, long[] paramArrayOflong1, int paramInt2, int paramInt3, long paramLong, long[] paramArrayOflong2, int paramInt4) {
/* 1057 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/* 1059 */       long l = paramArrayOflong1[paramInt2 + b];
/* 1060 */       paramArrayOflong2[paramInt4 + b] = l << paramInt3 | paramLong >>> -paramInt3;
/* 1061 */       paramLong = l;
/*      */     } 
/* 1063 */     return paramLong >>> -paramInt3;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void square(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 1068 */     int i = paramInt << 1;
/* 1069 */     int j = 0;
/* 1070 */     int k = paramInt, m = i;
/*      */     
/*      */     do {
/* 1073 */       long l1 = paramArrayOfint1[--k] & 0xFFFFFFFFL;
/* 1074 */       long l2 = l1 * l1;
/* 1075 */       paramArrayOfint2[--m] = j << 31 | (int)(l2 >>> 33L);
/* 1076 */       paramArrayOfint2[--m] = (int)(l2 >>> 1L);
/* 1077 */       j = (int)l2;
/*      */     }
/* 1079 */     while (k > 0);
/*      */     
/* 1081 */     long l = 0L;
/* 1082 */     byte b1 = 2;
/*      */     
/* 1084 */     for (byte b2 = 1; b2 < paramInt; b2++) {
/*      */       
/* 1086 */       l += squareWordAddTo(paramArrayOfint1, b2, paramArrayOfint2) & 0xFFFFFFFFL;
/* 1087 */       l += paramArrayOfint2[b1] & 0xFFFFFFFFL;
/* 1088 */       paramArrayOfint2[b1++] = (int)l; l >>>= 32L;
/* 1089 */       l += paramArrayOfint2[b1] & 0xFFFFFFFFL;
/* 1090 */       paramArrayOfint2[b1++] = (int)l; l >>>= 32L;
/*      */     } 
/*      */ 
/*      */     
/* 1094 */     shiftUpBit(i, paramArrayOfint2, paramArrayOfint1[0] << 31);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void square(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/* 1099 */     int i = paramInt1 << 1;
/* 1100 */     int j = 0;
/* 1101 */     int k = paramInt1, m = i;
/*      */     
/*      */     do {
/* 1104 */       long l1 = paramArrayOfint1[paramInt2 + --k] & 0xFFFFFFFFL;
/* 1105 */       long l2 = l1 * l1;
/* 1106 */       paramArrayOfint2[paramInt3 + --m] = j << 31 | (int)(l2 >>> 33L);
/* 1107 */       paramArrayOfint2[paramInt3 + --m] = (int)(l2 >>> 1L);
/* 1108 */       j = (int)l2;
/*      */     }
/* 1110 */     while (k > 0);
/*      */     
/* 1112 */     long l = 0L;
/* 1113 */     int n = paramInt3 + 2;
/*      */     
/* 1115 */     for (byte b = 1; b < paramInt1; b++) {
/*      */       
/* 1117 */       l += squareWordAddTo(paramArrayOfint1, paramInt2, b, paramArrayOfint2, paramInt3) & 0xFFFFFFFFL;
/* 1118 */       l += paramArrayOfint2[n] & 0xFFFFFFFFL;
/* 1119 */       paramArrayOfint2[n++] = (int)l; l >>>= 32L;
/* 1120 */       l += paramArrayOfint2[n] & 0xFFFFFFFFL;
/* 1121 */       paramArrayOfint2[n++] = (int)l; l >>>= 32L;
/*      */     } 
/*      */ 
/*      */     
/* 1125 */     shiftUpBit(i, paramArrayOfint2, paramInt3, paramArrayOfint1[paramInt2] << 31);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int squareWordAdd(int[] paramArrayOfint1, int paramInt, int[] paramArrayOfint2) {
/* 1133 */     long l1 = 0L, l2 = paramArrayOfint1[paramInt] & 0xFFFFFFFFL;
/* 1134 */     byte b = 0;
/*      */     
/*      */     while (true) {
/* 1137 */       l1 += l2 * (paramArrayOfint1[b] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt + b] & 0xFFFFFFFFL);
/* 1138 */       paramArrayOfint2[paramInt + b] = (int)l1;
/* 1139 */       l1 >>>= 32L;
/*      */       
/* 1141 */       if (++b >= paramInt) {
/* 1142 */         return (int)l1;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int squareWordAdd(int[] paramArrayOfint1, int paramInt1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/* 1150 */     long l1 = 0L, l2 = paramArrayOfint1[paramInt1 + paramInt2] & 0xFFFFFFFFL;
/* 1151 */     byte b = 0;
/*      */     
/*      */     while (true) {
/* 1154 */       l1 += l2 * (paramArrayOfint1[paramInt1 + b] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + paramInt3] & 0xFFFFFFFFL);
/* 1155 */       paramArrayOfint2[paramInt2 + paramInt3] = (int)l1;
/* 1156 */       l1 >>>= 32L;
/* 1157 */       paramInt3++;
/*      */       
/* 1159 */       if (++b >= paramInt2)
/* 1160 */         return (int)l1; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static int squareWordAddTo(int[] paramArrayOfint1, int paramInt, int[] paramArrayOfint2) {
/* 1165 */     long l1 = 0L, l2 = paramArrayOfint1[paramInt] & 0xFFFFFFFFL;
/* 1166 */     byte b = 0;
/*      */     
/*      */     while (true) {
/* 1169 */       l1 += l2 * (paramArrayOfint1[b] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt + b] & 0xFFFFFFFFL);
/* 1170 */       paramArrayOfint2[paramInt + b] = (int)l1;
/* 1171 */       l1 >>>= 32L;
/*      */       
/* 1173 */       if (++b >= paramInt)
/* 1174 */         return (int)l1; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static int squareWordAddTo(int[] paramArrayOfint1, int paramInt1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/* 1179 */     long l1 = 0L, l2 = paramArrayOfint1[paramInt1 + paramInt2] & 0xFFFFFFFFL;
/* 1180 */     byte b = 0;
/*      */     
/*      */     while (true) {
/* 1183 */       l1 += l2 * (paramArrayOfint1[paramInt1 + b] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + paramInt3] & 0xFFFFFFFFL);
/* 1184 */       paramArrayOfint2[paramInt2 + paramInt3] = (int)l1;
/* 1185 */       l1 >>>= 32L;
/* 1186 */       paramInt3++;
/*      */       
/* 1188 */       if (++b >= paramInt2)
/* 1189 */         return (int)l1; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static int sub(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 1194 */     long l = 0L;
/* 1195 */     for (byte b = 0; b < paramInt; b++) {
/*      */       
/* 1197 */       l += (paramArrayOfint1[b] & 0xFFFFFFFFL) - (paramArrayOfint2[b] & 0xFFFFFFFFL);
/* 1198 */       paramArrayOfint3[b] = (int)l;
/* 1199 */       l >>= 32L;
/*      */     } 
/* 1201 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int sub(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3, int[] paramArrayOfint3, int paramInt4) {
/* 1206 */     long l = 0L;
/* 1207 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/* 1209 */       l += (paramArrayOfint1[paramInt2 + b] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt3 + b] & 0xFFFFFFFFL);
/* 1210 */       paramArrayOfint3[paramInt4 + b] = (int)l;
/* 1211 */       l >>= 32L;
/*      */     } 
/* 1213 */     return (int)l;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int sub33At(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
/* 1219 */     long l = (paramArrayOfint[paramInt3 + 0] & 0xFFFFFFFFL) - (paramInt2 & 0xFFFFFFFFL);
/* 1220 */     paramArrayOfint[paramInt3 + 0] = (int)l;
/* 1221 */     l >>= 32L;
/* 1222 */     l += (paramArrayOfint[paramInt3 + 1] & 0xFFFFFFFFL) - 1L;
/* 1223 */     paramArrayOfint[paramInt3 + 1] = (int)l;
/* 1224 */     l >>= 32L;
/* 1225 */     return (l == 0L) ? 0 : decAt(paramInt1, paramArrayOfint, paramInt3 + 2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int sub33At(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3, int paramInt4) {
/* 1231 */     long l = (paramArrayOfint[paramInt3 + paramInt4] & 0xFFFFFFFFL) - (paramInt2 & 0xFFFFFFFFL);
/* 1232 */     paramArrayOfint[paramInt3 + paramInt4] = (int)l;
/* 1233 */     l >>= 32L;
/* 1234 */     l += (paramArrayOfint[paramInt3 + paramInt4 + 1] & 0xFFFFFFFFL) - 1L;
/* 1235 */     paramArrayOfint[paramInt3 + paramInt4 + 1] = (int)l;
/* 1236 */     l >>= 32L;
/* 1237 */     return (l == 0L) ? 0 : decAt(paramInt1, paramArrayOfint, paramInt3, paramInt4 + 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int sub33From(int paramInt1, int paramInt2, int[] paramArrayOfint) {
/* 1242 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) - (paramInt2 & 0xFFFFFFFFL);
/* 1243 */     paramArrayOfint[0] = (int)l;
/* 1244 */     l >>= 32L;
/* 1245 */     l += (paramArrayOfint[1] & 0xFFFFFFFFL) - 1L;
/* 1246 */     paramArrayOfint[1] = (int)l;
/* 1247 */     l >>= 32L;
/* 1248 */     return (l == 0L) ? 0 : decAt(paramInt1, paramArrayOfint, 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int sub33From(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
/* 1253 */     long l = (paramArrayOfint[paramInt3 + 0] & 0xFFFFFFFFL) - (paramInt2 & 0xFFFFFFFFL);
/* 1254 */     paramArrayOfint[paramInt3 + 0] = (int)l;
/* 1255 */     l >>= 32L;
/* 1256 */     l += (paramArrayOfint[paramInt3 + 1] & 0xFFFFFFFFL) - 1L;
/* 1257 */     paramArrayOfint[paramInt3 + 1] = (int)l;
/* 1258 */     l >>= 32L;
/* 1259 */     return (l == 0L) ? 0 : decAt(paramInt1, paramArrayOfint, paramInt3, 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subBothFrom(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 1264 */     long l = 0L;
/* 1265 */     for (byte b = 0; b < paramInt; b++) {
/*      */       
/* 1267 */       l += (paramArrayOfint3[b] & 0xFFFFFFFFL) - (paramArrayOfint1[b] & 0xFFFFFFFFL) - (paramArrayOfint2[b] & 0xFFFFFFFFL);
/* 1268 */       paramArrayOfint3[b] = (int)l;
/* 1269 */       l >>= 32L;
/*      */     } 
/* 1271 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subBothFrom(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3, int[] paramArrayOfint3, int paramInt4) {
/* 1276 */     long l = 0L;
/* 1277 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/* 1279 */       l += (paramArrayOfint3[paramInt4 + b] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt2 + b] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt3 + b] & 0xFFFFFFFFL);
/* 1280 */       paramArrayOfint3[paramInt4 + b] = (int)l;
/* 1281 */       l >>= 32L;
/*      */     } 
/* 1283 */     return (int)l;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int subDWordAt(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2) {
/* 1289 */     long l = (paramArrayOfint[paramInt2 + 0] & 0xFFFFFFFFL) - (paramLong & 0xFFFFFFFFL);
/* 1290 */     paramArrayOfint[paramInt2 + 0] = (int)l;
/* 1291 */     l >>= 32L;
/* 1292 */     l += (paramArrayOfint[paramInt2 + 1] & 0xFFFFFFFFL) - (paramLong >>> 32L);
/* 1293 */     paramArrayOfint[paramInt2 + 1] = (int)l;
/* 1294 */     l >>= 32L;
/* 1295 */     return (l == 0L) ? 0 : decAt(paramInt1, paramArrayOfint, paramInt2 + 2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int subDWordAt(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2, int paramInt3) {
/* 1301 */     long l = (paramArrayOfint[paramInt2 + paramInt3] & 0xFFFFFFFFL) - (paramLong & 0xFFFFFFFFL);
/* 1302 */     paramArrayOfint[paramInt2 + paramInt3] = (int)l;
/* 1303 */     l >>= 32L;
/* 1304 */     l += (paramArrayOfint[paramInt2 + paramInt3 + 1] & 0xFFFFFFFFL) - (paramLong >>> 32L);
/* 1305 */     paramArrayOfint[paramInt2 + paramInt3 + 1] = (int)l;
/* 1306 */     l >>= 32L;
/* 1307 */     return (l == 0L) ? 0 : decAt(paramInt1, paramArrayOfint, paramInt2, paramInt3 + 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subDWordFrom(int paramInt, long paramLong, int[] paramArrayOfint) {
/* 1312 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) - (paramLong & 0xFFFFFFFFL);
/* 1313 */     paramArrayOfint[0] = (int)l;
/* 1314 */     l >>= 32L;
/* 1315 */     l += (paramArrayOfint[1] & 0xFFFFFFFFL) - (paramLong >>> 32L);
/* 1316 */     paramArrayOfint[1] = (int)l;
/* 1317 */     l >>= 32L;
/* 1318 */     return (l == 0L) ? 0 : decAt(paramInt, paramArrayOfint, 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subDWordFrom(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2) {
/* 1323 */     long l = (paramArrayOfint[paramInt2 + 0] & 0xFFFFFFFFL) - (paramLong & 0xFFFFFFFFL);
/* 1324 */     paramArrayOfint[paramInt2 + 0] = (int)l;
/* 1325 */     l >>= 32L;
/* 1326 */     l += (paramArrayOfint[paramInt2 + 1] & 0xFFFFFFFFL) - (paramLong >>> 32L);
/* 1327 */     paramArrayOfint[paramInt2 + 1] = (int)l;
/* 1328 */     l >>= 32L;
/* 1329 */     return (l == 0L) ? 0 : decAt(paramInt1, paramArrayOfint, paramInt2, 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subFrom(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 1334 */     long l = 0L;
/* 1335 */     for (byte b = 0; b < paramInt; b++) {
/*      */       
/* 1337 */       l += (paramArrayOfint2[b] & 0xFFFFFFFFL) - (paramArrayOfint1[b] & 0xFFFFFFFFL);
/* 1338 */       paramArrayOfint2[b] = (int)l;
/* 1339 */       l >>= 32L;
/*      */     } 
/* 1341 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subFrom(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/* 1346 */     long l = 0L;
/* 1347 */     for (byte b = 0; b < paramInt1; b++) {
/*      */       
/* 1349 */       l += (paramArrayOfint2[paramInt3 + b] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt2 + b] & 0xFFFFFFFFL);
/* 1350 */       paramArrayOfint2[paramInt3 + b] = (int)l;
/* 1351 */       l >>= 32L;
/*      */     } 
/* 1353 */     return (int)l;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int subWordAt(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
/* 1359 */     long l = (paramArrayOfint[paramInt3] & 0xFFFFFFFFL) - (paramInt2 & 0xFFFFFFFFL);
/* 1360 */     paramArrayOfint[paramInt3] = (int)l;
/* 1361 */     l >>= 32L;
/* 1362 */     return (l == 0L) ? 0 : decAt(paramInt1, paramArrayOfint, paramInt3 + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int subWordAt(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3, int paramInt4) {
/* 1368 */     long l = (paramArrayOfint[paramInt3 + paramInt4] & 0xFFFFFFFFL) - (paramInt2 & 0xFFFFFFFFL);
/* 1369 */     paramArrayOfint[paramInt3 + paramInt4] = (int)l;
/* 1370 */     l >>= 32L;
/* 1371 */     return (l == 0L) ? 0 : decAt(paramInt1, paramArrayOfint, paramInt3, paramInt4 + 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subWordFrom(int paramInt1, int paramInt2, int[] paramArrayOfint) {
/* 1376 */     long l = (paramArrayOfint[0] & 0xFFFFFFFFL) - (paramInt2 & 0xFFFFFFFFL);
/* 1377 */     paramArrayOfint[0] = (int)l;
/* 1378 */     l >>= 32L;
/* 1379 */     return (l == 0L) ? 0 : decAt(paramInt1, paramArrayOfint, 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subWordFrom(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
/* 1384 */     long l = (paramArrayOfint[paramInt3 + 0] & 0xFFFFFFFFL) - (paramInt2 & 0xFFFFFFFFL);
/* 1385 */     paramArrayOfint[paramInt3 + 0] = (int)l;
/* 1386 */     l >>= 32L;
/* 1387 */     return (l == 0L) ? 0 : decAt(paramInt1, paramArrayOfint, paramInt3, 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigInteger toBigInteger(int paramInt, int[] paramArrayOfint) {
/* 1392 */     byte[] arrayOfByte = new byte[paramInt << 2];
/* 1393 */     for (byte b = 0; b < paramInt; b++) {
/*      */       
/* 1395 */       int i = paramArrayOfint[b];
/* 1396 */       if (i != 0)
/*      */       {
/* 1398 */         Pack.intToBigEndian(i, arrayOfByte, paramInt - 1 - b << 2);
/*      */       }
/*      */     } 
/* 1401 */     return new BigInteger(1, arrayOfByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void zero(int paramInt, int[] paramArrayOfint) {
/* 1406 */     for (byte b = 0; b < paramInt; b++)
/*      */     {
/* 1408 */       paramArrayOfint[b] = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void zero(int paramInt1, int[] paramArrayOfint, int paramInt2) {
/* 1414 */     for (byte b = 0; b < paramInt1; b++)
/*      */     {
/* 1416 */       paramArrayOfint[paramInt2 + b] = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static void zero64(int paramInt, long[] paramArrayOflong) {
/* 1422 */     for (byte b = 0; b < paramInt; b++)
/*      */     {
/* 1424 */       paramArrayOflong[b] = 0L;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/raw/Nat.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */