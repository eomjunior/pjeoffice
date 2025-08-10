/*      */ package META-INF.versions.9.org.bouncycastle.math.raw;
/*      */ 
/*      */ import java.math.BigInteger;
/*      */ import org.bouncycastle.math.raw.Nat;
/*      */ import org.bouncycastle.util.Pack;
/*      */ 
/*      */ 
/*      */ public abstract class Nat224
/*      */ {
/*      */   private static final long M = 4294967295L;
/*      */   
/*      */   public static int add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*   13 */     long l = 0L;
/*   14 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) + (paramArrayOfint2[0] & 0xFFFFFFFFL);
/*   15 */     paramArrayOfint3[0] = (int)l;
/*   16 */     l >>>= 32L;
/*   17 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) + (paramArrayOfint2[1] & 0xFFFFFFFFL);
/*   18 */     paramArrayOfint3[1] = (int)l;
/*   19 */     l >>>= 32L;
/*   20 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) + (paramArrayOfint2[2] & 0xFFFFFFFFL);
/*   21 */     paramArrayOfint3[2] = (int)l;
/*   22 */     l >>>= 32L;
/*   23 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) + (paramArrayOfint2[3] & 0xFFFFFFFFL);
/*   24 */     paramArrayOfint3[3] = (int)l;
/*   25 */     l >>>= 32L;
/*   26 */     l += (paramArrayOfint1[4] & 0xFFFFFFFFL) + (paramArrayOfint2[4] & 0xFFFFFFFFL);
/*   27 */     paramArrayOfint3[4] = (int)l;
/*   28 */     l >>>= 32L;
/*   29 */     l += (paramArrayOfint1[5] & 0xFFFFFFFFL) + (paramArrayOfint2[5] & 0xFFFFFFFFL);
/*   30 */     paramArrayOfint3[5] = (int)l;
/*   31 */     l >>>= 32L;
/*   32 */     l += (paramArrayOfint1[6] & 0xFFFFFFFFL) + (paramArrayOfint2[6] & 0xFFFFFFFFL);
/*   33 */     paramArrayOfint3[6] = (int)l;
/*   34 */     l >>>= 32L;
/*   35 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int add(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/*   40 */     long l = 0L;
/*   41 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/*   42 */     paramArrayOfint3[paramInt3 + 0] = (int)l;
/*   43 */     l >>>= 32L;
/*   44 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/*   45 */     paramArrayOfint3[paramInt3 + 1] = (int)l;
/*   46 */     l >>>= 32L;
/*   47 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/*   48 */     paramArrayOfint3[paramInt3 + 2] = (int)l;
/*   49 */     l >>>= 32L;
/*   50 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/*   51 */     paramArrayOfint3[paramInt3 + 3] = (int)l;
/*   52 */     l >>>= 32L;
/*   53 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL);
/*   54 */     paramArrayOfint3[paramInt3 + 4] = (int)l;
/*   55 */     l >>>= 32L;
/*   56 */     l += (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL);
/*   57 */     paramArrayOfint3[paramInt3 + 5] = (int)l;
/*   58 */     l >>>= 32L;
/*   59 */     l += (paramArrayOfint1[paramInt1 + 6] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL);
/*   60 */     paramArrayOfint3[paramInt3 + 6] = (int)l;
/*   61 */     l >>>= 32L;
/*   62 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addBothTo(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*   67 */     long l = 0L;
/*   68 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) + (paramArrayOfint2[0] & 0xFFFFFFFFL) + (paramArrayOfint3[0] & 0xFFFFFFFFL);
/*   69 */     paramArrayOfint3[0] = (int)l;
/*   70 */     l >>>= 32L;
/*   71 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) + (paramArrayOfint2[1] & 0xFFFFFFFFL) + (paramArrayOfint3[1] & 0xFFFFFFFFL);
/*   72 */     paramArrayOfint3[1] = (int)l;
/*   73 */     l >>>= 32L;
/*   74 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) + (paramArrayOfint2[2] & 0xFFFFFFFFL) + (paramArrayOfint3[2] & 0xFFFFFFFFL);
/*   75 */     paramArrayOfint3[2] = (int)l;
/*   76 */     l >>>= 32L;
/*   77 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) + (paramArrayOfint2[3] & 0xFFFFFFFFL) + (paramArrayOfint3[3] & 0xFFFFFFFFL);
/*   78 */     paramArrayOfint3[3] = (int)l;
/*   79 */     l >>>= 32L;
/*   80 */     l += (paramArrayOfint1[4] & 0xFFFFFFFFL) + (paramArrayOfint2[4] & 0xFFFFFFFFL) + (paramArrayOfint3[4] & 0xFFFFFFFFL);
/*   81 */     paramArrayOfint3[4] = (int)l;
/*   82 */     l >>>= 32L;
/*   83 */     l += (paramArrayOfint1[5] & 0xFFFFFFFFL) + (paramArrayOfint2[5] & 0xFFFFFFFFL) + (paramArrayOfint3[5] & 0xFFFFFFFFL);
/*   84 */     paramArrayOfint3[5] = (int)l;
/*   85 */     l >>>= 32L;
/*   86 */     l += (paramArrayOfint1[6] & 0xFFFFFFFFL) + (paramArrayOfint2[6] & 0xFFFFFFFFL) + (paramArrayOfint3[6] & 0xFFFFFFFFL);
/*   87 */     paramArrayOfint3[6] = (int)l;
/*   88 */     l >>>= 32L;
/*   89 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addBothTo(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/*   94 */     long l = 0L;
/*   95 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt3 + 0] & 0xFFFFFFFFL);
/*   96 */     paramArrayOfint3[paramInt3 + 0] = (int)l;
/*   97 */     l >>>= 32L;
/*   98 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt3 + 1] & 0xFFFFFFFFL);
/*   99 */     paramArrayOfint3[paramInt3 + 1] = (int)l;
/*  100 */     l >>>= 32L;
/*  101 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt3 + 2] & 0xFFFFFFFFL);
/*  102 */     paramArrayOfint3[paramInt3 + 2] = (int)l;
/*  103 */     l >>>= 32L;
/*  104 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt3 + 3] & 0xFFFFFFFFL);
/*  105 */     paramArrayOfint3[paramInt3 + 3] = (int)l;
/*  106 */     l >>>= 32L;
/*  107 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt3 + 4] & 0xFFFFFFFFL);
/*  108 */     paramArrayOfint3[paramInt3 + 4] = (int)l;
/*  109 */     l >>>= 32L;
/*  110 */     l += (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt3 + 5] & 0xFFFFFFFFL);
/*  111 */     paramArrayOfint3[paramInt3 + 5] = (int)l;
/*  112 */     l >>>= 32L;
/*  113 */     l += (paramArrayOfint1[paramInt1 + 6] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt3 + 6] & 0xFFFFFFFFL);
/*  114 */     paramArrayOfint3[paramInt3 + 6] = (int)l;
/*  115 */     l >>>= 32L;
/*  116 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addTo(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  121 */     long l = 0L;
/*  122 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) + (paramArrayOfint2[0] & 0xFFFFFFFFL);
/*  123 */     paramArrayOfint2[0] = (int)l;
/*  124 */     l >>>= 32L;
/*  125 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) + (paramArrayOfint2[1] & 0xFFFFFFFFL);
/*  126 */     paramArrayOfint2[1] = (int)l;
/*  127 */     l >>>= 32L;
/*  128 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) + (paramArrayOfint2[2] & 0xFFFFFFFFL);
/*  129 */     paramArrayOfint2[2] = (int)l;
/*  130 */     l >>>= 32L;
/*  131 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) + (paramArrayOfint2[3] & 0xFFFFFFFFL);
/*  132 */     paramArrayOfint2[3] = (int)l;
/*  133 */     l >>>= 32L;
/*  134 */     l += (paramArrayOfint1[4] & 0xFFFFFFFFL) + (paramArrayOfint2[4] & 0xFFFFFFFFL);
/*  135 */     paramArrayOfint2[4] = (int)l;
/*  136 */     l >>>= 32L;
/*  137 */     l += (paramArrayOfint1[5] & 0xFFFFFFFFL) + (paramArrayOfint2[5] & 0xFFFFFFFFL);
/*  138 */     paramArrayOfint2[5] = (int)l;
/*  139 */     l >>>= 32L;
/*  140 */     l += (paramArrayOfint1[6] & 0xFFFFFFFFL) + (paramArrayOfint2[6] & 0xFFFFFFFFL);
/*  141 */     paramArrayOfint2[6] = (int)l;
/*  142 */     l >>>= 32L;
/*  143 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addTo(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int paramInt3) {
/*  148 */     long l = paramInt3 & 0xFFFFFFFFL;
/*  149 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/*  150 */     paramArrayOfint2[paramInt2 + 0] = (int)l;
/*  151 */     l >>>= 32L;
/*  152 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/*  153 */     paramArrayOfint2[paramInt2 + 1] = (int)l;
/*  154 */     l >>>= 32L;
/*  155 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/*  156 */     paramArrayOfint2[paramInt2 + 2] = (int)l;
/*  157 */     l >>>= 32L;
/*  158 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/*  159 */     paramArrayOfint2[paramInt2 + 3] = (int)l;
/*  160 */     l >>>= 32L;
/*  161 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL);
/*  162 */     paramArrayOfint2[paramInt2 + 4] = (int)l;
/*  163 */     l >>>= 32L;
/*  164 */     l += (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL);
/*  165 */     paramArrayOfint2[paramInt2 + 5] = (int)l;
/*  166 */     l >>>= 32L;
/*  167 */     l += (paramArrayOfint1[paramInt1 + 6] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL);
/*  168 */     paramArrayOfint2[paramInt2 + 6] = (int)l;
/*  169 */     l >>>= 32L;
/*  170 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addToEachOther(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/*  175 */     long l = 0L;
/*  176 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/*  177 */     paramArrayOfint1[paramInt1 + 0] = (int)l;
/*  178 */     paramArrayOfint2[paramInt2 + 0] = (int)l;
/*  179 */     l >>>= 32L;
/*  180 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/*  181 */     paramArrayOfint1[paramInt1 + 1] = (int)l;
/*  182 */     paramArrayOfint2[paramInt2 + 1] = (int)l;
/*  183 */     l >>>= 32L;
/*  184 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/*  185 */     paramArrayOfint1[paramInt1 + 2] = (int)l;
/*  186 */     paramArrayOfint2[paramInt2 + 2] = (int)l;
/*  187 */     l >>>= 32L;
/*  188 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/*  189 */     paramArrayOfint1[paramInt1 + 3] = (int)l;
/*  190 */     paramArrayOfint2[paramInt2 + 3] = (int)l;
/*  191 */     l >>>= 32L;
/*  192 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL);
/*  193 */     paramArrayOfint1[paramInt1 + 4] = (int)l;
/*  194 */     paramArrayOfint2[paramInt2 + 4] = (int)l;
/*  195 */     l >>>= 32L;
/*  196 */     l += (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL);
/*  197 */     paramArrayOfint1[paramInt1 + 5] = (int)l;
/*  198 */     paramArrayOfint2[paramInt2 + 5] = (int)l;
/*  199 */     l >>>= 32L;
/*  200 */     l += (paramArrayOfint1[paramInt1 + 6] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL);
/*  201 */     paramArrayOfint1[paramInt1 + 6] = (int)l;
/*  202 */     paramArrayOfint2[paramInt2 + 6] = (int)l;
/*  203 */     l >>>= 32L;
/*  204 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void copy(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  209 */     paramArrayOfint2[0] = paramArrayOfint1[0];
/*  210 */     paramArrayOfint2[1] = paramArrayOfint1[1];
/*  211 */     paramArrayOfint2[2] = paramArrayOfint1[2];
/*  212 */     paramArrayOfint2[3] = paramArrayOfint1[3];
/*  213 */     paramArrayOfint2[4] = paramArrayOfint1[4];
/*  214 */     paramArrayOfint2[5] = paramArrayOfint1[5];
/*  215 */     paramArrayOfint2[6] = paramArrayOfint1[6];
/*      */   }
/*      */ 
/*      */   
/*      */   public static void copy(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/*  220 */     paramArrayOfint2[paramInt2 + 0] = paramArrayOfint1[paramInt1 + 0];
/*  221 */     paramArrayOfint2[paramInt2 + 1] = paramArrayOfint1[paramInt1 + 1];
/*  222 */     paramArrayOfint2[paramInt2 + 2] = paramArrayOfint1[paramInt1 + 2];
/*  223 */     paramArrayOfint2[paramInt2 + 3] = paramArrayOfint1[paramInt1 + 3];
/*  224 */     paramArrayOfint2[paramInt2 + 4] = paramArrayOfint1[paramInt1 + 4];
/*  225 */     paramArrayOfint2[paramInt2 + 5] = paramArrayOfint1[paramInt1 + 5];
/*  226 */     paramArrayOfint2[paramInt2 + 6] = paramArrayOfint1[paramInt1 + 6];
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] create() {
/*  231 */     return new int[7];
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] createExt() {
/*  236 */     return new int[14];
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean diff(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/*  241 */     boolean bool = gte(paramArrayOfint1, paramInt1, paramArrayOfint2, paramInt2);
/*  242 */     if (bool) {
/*      */       
/*  244 */       sub(paramArrayOfint1, paramInt1, paramArrayOfint2, paramInt2, paramArrayOfint3, paramInt3);
/*      */     }
/*      */     else {
/*      */       
/*  248 */       sub(paramArrayOfint2, paramInt2, paramArrayOfint1, paramInt1, paramArrayOfint3, paramInt3);
/*      */     } 
/*  250 */     return bool;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean eq(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  255 */     for (byte b = 6; b >= 0; b--) {
/*      */       
/*  257 */       if (paramArrayOfint1[b] != paramArrayOfint2[b])
/*      */       {
/*  259 */         return false;
/*      */       }
/*      */     } 
/*  262 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/*  267 */     if (paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > 224)
/*      */     {
/*  269 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*  272 */     int[] arrayOfInt = create();
/*      */ 
/*      */     
/*  275 */     for (byte b = 0; b < 7; b++) {
/*      */       
/*  277 */       arrayOfInt[b] = paramBigInteger.intValue();
/*  278 */       paramBigInteger = paramBigInteger.shiftRight(32);
/*      */     } 
/*  280 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getBit(int[] paramArrayOfint, int paramInt) {
/*  285 */     if (paramInt == 0)
/*      */     {
/*  287 */       return paramArrayOfint[0] & 0x1;
/*      */     }
/*  289 */     int i = paramInt >> 5;
/*  290 */     if (i < 0 || i >= 7)
/*      */     {
/*  292 */       return 0;
/*      */     }
/*  294 */     int j = paramInt & 0x1F;
/*  295 */     return paramArrayOfint[i] >>> j & 0x1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean gte(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  300 */     for (byte b = 6; b >= 0; b--) {
/*      */       
/*  302 */       int i = paramArrayOfint1[b] ^ Integer.MIN_VALUE;
/*  303 */       int j = paramArrayOfint2[b] ^ Integer.MIN_VALUE;
/*  304 */       if (i < j)
/*  305 */         return false; 
/*  306 */       if (i > j)
/*  307 */         return true; 
/*      */     } 
/*  309 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean gte(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/*  314 */     for (byte b = 6; b >= 0; b--) {
/*      */       
/*  316 */       int i = paramArrayOfint1[paramInt1 + b] ^ Integer.MIN_VALUE;
/*  317 */       int j = paramArrayOfint2[paramInt2 + b] ^ Integer.MIN_VALUE;
/*  318 */       if (i < j)
/*  319 */         return false; 
/*  320 */       if (i > j)
/*  321 */         return true; 
/*      */     } 
/*  323 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isOne(int[] paramArrayOfint) {
/*  328 */     if (paramArrayOfint[0] != 1)
/*      */     {
/*  330 */       return false;
/*      */     }
/*  332 */     for (byte b = 1; b < 7; b++) {
/*      */       
/*  334 */       if (paramArrayOfint[b] != 0)
/*      */       {
/*  336 */         return false;
/*      */       }
/*      */     } 
/*  339 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isZero(int[] paramArrayOfint) {
/*  344 */     for (byte b = 0; b < 7; b++) {
/*      */       
/*  346 */       if (paramArrayOfint[b] != 0)
/*      */       {
/*  348 */         return false;
/*      */       }
/*      */     } 
/*  351 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void mul(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  356 */     long l1 = paramArrayOfint2[0] & 0xFFFFFFFFL;
/*  357 */     long l2 = paramArrayOfint2[1] & 0xFFFFFFFFL;
/*  358 */     long l3 = paramArrayOfint2[2] & 0xFFFFFFFFL;
/*  359 */     long l4 = paramArrayOfint2[3] & 0xFFFFFFFFL;
/*  360 */     long l5 = paramArrayOfint2[4] & 0xFFFFFFFFL;
/*  361 */     long l6 = paramArrayOfint2[5] & 0xFFFFFFFFL;
/*  362 */     long l7 = paramArrayOfint2[6] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  365 */     long l8 = 0L, l9 = paramArrayOfint1[0] & 0xFFFFFFFFL;
/*  366 */     l8 += l9 * l1;
/*  367 */     paramArrayOfint3[0] = (int)l8;
/*  368 */     l8 >>>= 32L;
/*  369 */     l8 += l9 * l2;
/*  370 */     paramArrayOfint3[1] = (int)l8;
/*  371 */     l8 >>>= 32L;
/*  372 */     l8 += l9 * l3;
/*  373 */     paramArrayOfint3[2] = (int)l8;
/*  374 */     l8 >>>= 32L;
/*  375 */     l8 += l9 * l4;
/*  376 */     paramArrayOfint3[3] = (int)l8;
/*  377 */     l8 >>>= 32L;
/*  378 */     l8 += l9 * l5;
/*  379 */     paramArrayOfint3[4] = (int)l8;
/*  380 */     l8 >>>= 32L;
/*  381 */     l8 += l9 * l6;
/*  382 */     paramArrayOfint3[5] = (int)l8;
/*  383 */     l8 >>>= 32L;
/*  384 */     l8 += l9 * l7;
/*  385 */     paramArrayOfint3[6] = (int)l8;
/*  386 */     l8 >>>= 32L;
/*  387 */     paramArrayOfint3[7] = (int)l8;
/*      */ 
/*      */     
/*  390 */     for (byte b = 1; b < 7; b++) {
/*      */       
/*  392 */       long l10 = 0L, l11 = paramArrayOfint1[b] & 0xFFFFFFFFL;
/*  393 */       l10 += l11 * l1 + (paramArrayOfint3[b + 0] & 0xFFFFFFFFL);
/*  394 */       paramArrayOfint3[b + 0] = (int)l10;
/*  395 */       l10 >>>= 32L;
/*  396 */       l10 += l11 * l2 + (paramArrayOfint3[b + 1] & 0xFFFFFFFFL);
/*  397 */       paramArrayOfint3[b + 1] = (int)l10;
/*  398 */       l10 >>>= 32L;
/*  399 */       l10 += l11 * l3 + (paramArrayOfint3[b + 2] & 0xFFFFFFFFL);
/*  400 */       paramArrayOfint3[b + 2] = (int)l10;
/*  401 */       l10 >>>= 32L;
/*  402 */       l10 += l11 * l4 + (paramArrayOfint3[b + 3] & 0xFFFFFFFFL);
/*  403 */       paramArrayOfint3[b + 3] = (int)l10;
/*  404 */       l10 >>>= 32L;
/*  405 */       l10 += l11 * l5 + (paramArrayOfint3[b + 4] & 0xFFFFFFFFL);
/*  406 */       paramArrayOfint3[b + 4] = (int)l10;
/*  407 */       l10 >>>= 32L;
/*  408 */       l10 += l11 * l6 + (paramArrayOfint3[b + 5] & 0xFFFFFFFFL);
/*  409 */       paramArrayOfint3[b + 5] = (int)l10;
/*  410 */       l10 >>>= 32L;
/*  411 */       l10 += l11 * l7 + (paramArrayOfint3[b + 6] & 0xFFFFFFFFL);
/*  412 */       paramArrayOfint3[b + 6] = (int)l10;
/*  413 */       l10 >>>= 32L;
/*  414 */       paramArrayOfint3[b + 7] = (int)l10;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void mul(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/*  420 */     long l1 = paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL;
/*  421 */     long l2 = paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL;
/*  422 */     long l3 = paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL;
/*  423 */     long l4 = paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL;
/*  424 */     long l5 = paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL;
/*  425 */     long l6 = paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL;
/*  426 */     long l7 = paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  429 */     long l8 = 0L, l9 = paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL;
/*  430 */     l8 += l9 * l1;
/*  431 */     paramArrayOfint3[paramInt3 + 0] = (int)l8;
/*  432 */     l8 >>>= 32L;
/*  433 */     l8 += l9 * l2;
/*  434 */     paramArrayOfint3[paramInt3 + 1] = (int)l8;
/*  435 */     l8 >>>= 32L;
/*  436 */     l8 += l9 * l3;
/*  437 */     paramArrayOfint3[paramInt3 + 2] = (int)l8;
/*  438 */     l8 >>>= 32L;
/*  439 */     l8 += l9 * l4;
/*  440 */     paramArrayOfint3[paramInt3 + 3] = (int)l8;
/*  441 */     l8 >>>= 32L;
/*  442 */     l8 += l9 * l5;
/*  443 */     paramArrayOfint3[paramInt3 + 4] = (int)l8;
/*  444 */     l8 >>>= 32L;
/*  445 */     l8 += l9 * l6;
/*  446 */     paramArrayOfint3[paramInt3 + 5] = (int)l8;
/*  447 */     l8 >>>= 32L;
/*  448 */     l8 += l9 * l7;
/*  449 */     paramArrayOfint3[paramInt3 + 6] = (int)l8;
/*  450 */     l8 >>>= 32L;
/*  451 */     paramArrayOfint3[paramInt3 + 7] = (int)l8;
/*      */ 
/*      */     
/*  454 */     for (byte b = 1; b < 7; b++) {
/*      */       
/*  456 */       paramInt3++;
/*  457 */       long l10 = 0L, l11 = paramArrayOfint1[paramInt1 + b] & 0xFFFFFFFFL;
/*  458 */       l10 += l11 * l1 + (paramArrayOfint3[paramInt3 + 0] & 0xFFFFFFFFL);
/*  459 */       paramArrayOfint3[paramInt3 + 0] = (int)l10;
/*  460 */       l10 >>>= 32L;
/*  461 */       l10 += l11 * l2 + (paramArrayOfint3[paramInt3 + 1] & 0xFFFFFFFFL);
/*  462 */       paramArrayOfint3[paramInt3 + 1] = (int)l10;
/*  463 */       l10 >>>= 32L;
/*  464 */       l10 += l11 * l3 + (paramArrayOfint3[paramInt3 + 2] & 0xFFFFFFFFL);
/*  465 */       paramArrayOfint3[paramInt3 + 2] = (int)l10;
/*  466 */       l10 >>>= 32L;
/*  467 */       l10 += l11 * l4 + (paramArrayOfint3[paramInt3 + 3] & 0xFFFFFFFFL);
/*  468 */       paramArrayOfint3[paramInt3 + 3] = (int)l10;
/*  469 */       l10 >>>= 32L;
/*  470 */       l10 += l11 * l5 + (paramArrayOfint3[paramInt3 + 4] & 0xFFFFFFFFL);
/*  471 */       paramArrayOfint3[paramInt3 + 4] = (int)l10;
/*  472 */       l10 >>>= 32L;
/*  473 */       l10 += l11 * l6 + (paramArrayOfint3[paramInt3 + 5] & 0xFFFFFFFFL);
/*  474 */       paramArrayOfint3[paramInt3 + 5] = (int)l10;
/*  475 */       l10 >>>= 32L;
/*  476 */       l10 += l11 * l7 + (paramArrayOfint3[paramInt3 + 6] & 0xFFFFFFFFL);
/*  477 */       paramArrayOfint3[paramInt3 + 6] = (int)l10;
/*  478 */       l10 >>>= 32L;
/*  479 */       paramArrayOfint3[paramInt3 + 7] = (int)l10;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulAddTo(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  485 */     long l1 = paramArrayOfint2[0] & 0xFFFFFFFFL;
/*  486 */     long l2 = paramArrayOfint2[1] & 0xFFFFFFFFL;
/*  487 */     long l3 = paramArrayOfint2[2] & 0xFFFFFFFFL;
/*  488 */     long l4 = paramArrayOfint2[3] & 0xFFFFFFFFL;
/*  489 */     long l5 = paramArrayOfint2[4] & 0xFFFFFFFFL;
/*  490 */     long l6 = paramArrayOfint2[5] & 0xFFFFFFFFL;
/*  491 */     long l7 = paramArrayOfint2[6] & 0xFFFFFFFFL;
/*      */     
/*  493 */     long l8 = 0L;
/*  494 */     for (byte b = 0; b < 7; b++) {
/*      */       
/*  496 */       long l9 = 0L, l10 = paramArrayOfint1[b] & 0xFFFFFFFFL;
/*  497 */       l9 += l10 * l1 + (paramArrayOfint3[b + 0] & 0xFFFFFFFFL);
/*  498 */       paramArrayOfint3[b + 0] = (int)l9;
/*  499 */       l9 >>>= 32L;
/*  500 */       l9 += l10 * l2 + (paramArrayOfint3[b + 1] & 0xFFFFFFFFL);
/*  501 */       paramArrayOfint3[b + 1] = (int)l9;
/*  502 */       l9 >>>= 32L;
/*  503 */       l9 += l10 * l3 + (paramArrayOfint3[b + 2] & 0xFFFFFFFFL);
/*  504 */       paramArrayOfint3[b + 2] = (int)l9;
/*  505 */       l9 >>>= 32L;
/*  506 */       l9 += l10 * l4 + (paramArrayOfint3[b + 3] & 0xFFFFFFFFL);
/*  507 */       paramArrayOfint3[b + 3] = (int)l9;
/*  508 */       l9 >>>= 32L;
/*  509 */       l9 += l10 * l5 + (paramArrayOfint3[b + 4] & 0xFFFFFFFFL);
/*  510 */       paramArrayOfint3[b + 4] = (int)l9;
/*  511 */       l9 >>>= 32L;
/*  512 */       l9 += l10 * l6 + (paramArrayOfint3[b + 5] & 0xFFFFFFFFL);
/*  513 */       paramArrayOfint3[b + 5] = (int)l9;
/*  514 */       l9 >>>= 32L;
/*  515 */       l9 += l10 * l7 + (paramArrayOfint3[b + 6] & 0xFFFFFFFFL);
/*  516 */       paramArrayOfint3[b + 6] = (int)l9;
/*  517 */       l9 >>>= 32L;
/*      */       
/*  519 */       l8 += l9 + (paramArrayOfint3[b + 7] & 0xFFFFFFFFL);
/*  520 */       paramArrayOfint3[b + 7] = (int)l8;
/*  521 */       l8 >>>= 32L;
/*      */     } 
/*  523 */     return (int)l8;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulAddTo(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/*  528 */     long l1 = paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL;
/*  529 */     long l2 = paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL;
/*  530 */     long l3 = paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL;
/*  531 */     long l4 = paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL;
/*  532 */     long l5 = paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL;
/*  533 */     long l6 = paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL;
/*  534 */     long l7 = paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL;
/*      */     
/*  536 */     long l8 = 0L;
/*  537 */     for (byte b = 0; b < 7; b++) {
/*      */       
/*  539 */       long l9 = 0L, l10 = paramArrayOfint1[paramInt1 + b] & 0xFFFFFFFFL;
/*  540 */       l9 += l10 * l1 + (paramArrayOfint3[paramInt3 + 0] & 0xFFFFFFFFL);
/*  541 */       paramArrayOfint3[paramInt3 + 0] = (int)l9;
/*  542 */       l9 >>>= 32L;
/*  543 */       l9 += l10 * l2 + (paramArrayOfint3[paramInt3 + 1] & 0xFFFFFFFFL);
/*  544 */       paramArrayOfint3[paramInt3 + 1] = (int)l9;
/*  545 */       l9 >>>= 32L;
/*  546 */       l9 += l10 * l3 + (paramArrayOfint3[paramInt3 + 2] & 0xFFFFFFFFL);
/*  547 */       paramArrayOfint3[paramInt3 + 2] = (int)l9;
/*  548 */       l9 >>>= 32L;
/*  549 */       l9 += l10 * l4 + (paramArrayOfint3[paramInt3 + 3] & 0xFFFFFFFFL);
/*  550 */       paramArrayOfint3[paramInt3 + 3] = (int)l9;
/*  551 */       l9 >>>= 32L;
/*  552 */       l9 += l10 * l5 + (paramArrayOfint3[paramInt3 + 4] & 0xFFFFFFFFL);
/*  553 */       paramArrayOfint3[paramInt3 + 4] = (int)l9;
/*  554 */       l9 >>>= 32L;
/*  555 */       l9 += l10 * l6 + (paramArrayOfint3[paramInt3 + 5] & 0xFFFFFFFFL);
/*  556 */       paramArrayOfint3[paramInt3 + 5] = (int)l9;
/*  557 */       l9 >>>= 32L;
/*  558 */       l9 += l10 * l7 + (paramArrayOfint3[paramInt3 + 6] & 0xFFFFFFFFL);
/*  559 */       paramArrayOfint3[paramInt3 + 6] = (int)l9;
/*  560 */       l9 >>>= 32L;
/*      */       
/*  562 */       l8 += l9 + (paramArrayOfint3[paramInt3 + 7] & 0xFFFFFFFFL);
/*  563 */       paramArrayOfint3[paramInt3 + 7] = (int)l8;
/*  564 */       l8 >>>= 32L;
/*  565 */       paramInt3++;
/*      */     } 
/*  567 */     return (int)l8;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long mul33Add(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3, int[] paramArrayOfint3, int paramInt4) {
/*  574 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/*  575 */     long l3 = paramArrayOfint1[paramInt2 + 0] & 0xFFFFFFFFL;
/*  576 */     l1 += l2 * l3 + (paramArrayOfint2[paramInt3 + 0] & 0xFFFFFFFFL);
/*  577 */     paramArrayOfint3[paramInt4 + 0] = (int)l1;
/*  578 */     l1 >>>= 32L;
/*  579 */     long l4 = paramArrayOfint1[paramInt2 + 1] & 0xFFFFFFFFL;
/*  580 */     l1 += l2 * l4 + l3 + (paramArrayOfint2[paramInt3 + 1] & 0xFFFFFFFFL);
/*  581 */     paramArrayOfint3[paramInt4 + 1] = (int)l1;
/*  582 */     l1 >>>= 32L;
/*  583 */     long l5 = paramArrayOfint1[paramInt2 + 2] & 0xFFFFFFFFL;
/*  584 */     l1 += l2 * l5 + l4 + (paramArrayOfint2[paramInt3 + 2] & 0xFFFFFFFFL);
/*  585 */     paramArrayOfint3[paramInt4 + 2] = (int)l1;
/*  586 */     l1 >>>= 32L;
/*  587 */     long l6 = paramArrayOfint1[paramInt2 + 3] & 0xFFFFFFFFL;
/*  588 */     l1 += l2 * l6 + l5 + (paramArrayOfint2[paramInt3 + 3] & 0xFFFFFFFFL);
/*  589 */     paramArrayOfint3[paramInt4 + 3] = (int)l1;
/*  590 */     l1 >>>= 32L;
/*  591 */     long l7 = paramArrayOfint1[paramInt2 + 4] & 0xFFFFFFFFL;
/*  592 */     l1 += l2 * l7 + l6 + (paramArrayOfint2[paramInt3 + 4] & 0xFFFFFFFFL);
/*  593 */     paramArrayOfint3[paramInt4 + 4] = (int)l1;
/*  594 */     l1 >>>= 32L;
/*  595 */     long l8 = paramArrayOfint1[paramInt2 + 5] & 0xFFFFFFFFL;
/*  596 */     l1 += l2 * l8 + l7 + (paramArrayOfint2[paramInt3 + 5] & 0xFFFFFFFFL);
/*  597 */     paramArrayOfint3[paramInt4 + 5] = (int)l1;
/*  598 */     l1 >>>= 32L;
/*  599 */     long l9 = paramArrayOfint1[paramInt2 + 6] & 0xFFFFFFFFL;
/*  600 */     l1 += l2 * l9 + l8 + (paramArrayOfint2[paramInt3 + 6] & 0xFFFFFFFFL);
/*  601 */     paramArrayOfint3[paramInt4 + 6] = (int)l1;
/*  602 */     l1 >>>= 32L;
/*  603 */     l1 += l9;
/*  604 */     return l1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulByWord(int paramInt, int[] paramArrayOfint) {
/*  609 */     long l1 = 0L, l2 = paramInt & 0xFFFFFFFFL;
/*  610 */     l1 += l2 * (paramArrayOfint[0] & 0xFFFFFFFFL);
/*  611 */     paramArrayOfint[0] = (int)l1;
/*  612 */     l1 >>>= 32L;
/*  613 */     l1 += l2 * (paramArrayOfint[1] & 0xFFFFFFFFL);
/*  614 */     paramArrayOfint[1] = (int)l1;
/*  615 */     l1 >>>= 32L;
/*  616 */     l1 += l2 * (paramArrayOfint[2] & 0xFFFFFFFFL);
/*  617 */     paramArrayOfint[2] = (int)l1;
/*  618 */     l1 >>>= 32L;
/*  619 */     l1 += l2 * (paramArrayOfint[3] & 0xFFFFFFFFL);
/*  620 */     paramArrayOfint[3] = (int)l1;
/*  621 */     l1 >>>= 32L;
/*  622 */     l1 += l2 * (paramArrayOfint[4] & 0xFFFFFFFFL);
/*  623 */     paramArrayOfint[4] = (int)l1;
/*  624 */     l1 >>>= 32L;
/*  625 */     l1 += l2 * (paramArrayOfint[5] & 0xFFFFFFFFL);
/*  626 */     paramArrayOfint[5] = (int)l1;
/*  627 */     l1 >>>= 32L;
/*  628 */     l1 += l2 * (paramArrayOfint[6] & 0xFFFFFFFFL);
/*  629 */     paramArrayOfint[6] = (int)l1;
/*  630 */     l1 >>>= 32L;
/*  631 */     return (int)l1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulByWordAddTo(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  636 */     long l1 = 0L, l2 = paramInt & 0xFFFFFFFFL;
/*  637 */     l1 += l2 * (paramArrayOfint2[0] & 0xFFFFFFFFL) + (paramArrayOfint1[0] & 0xFFFFFFFFL);
/*  638 */     paramArrayOfint2[0] = (int)l1;
/*  639 */     l1 >>>= 32L;
/*  640 */     l1 += l2 * (paramArrayOfint2[1] & 0xFFFFFFFFL) + (paramArrayOfint1[1] & 0xFFFFFFFFL);
/*  641 */     paramArrayOfint2[1] = (int)l1;
/*  642 */     l1 >>>= 32L;
/*  643 */     l1 += l2 * (paramArrayOfint2[2] & 0xFFFFFFFFL) + (paramArrayOfint1[2] & 0xFFFFFFFFL);
/*  644 */     paramArrayOfint2[2] = (int)l1;
/*  645 */     l1 >>>= 32L;
/*  646 */     l1 += l2 * (paramArrayOfint2[3] & 0xFFFFFFFFL) + (paramArrayOfint1[3] & 0xFFFFFFFFL);
/*  647 */     paramArrayOfint2[3] = (int)l1;
/*  648 */     l1 >>>= 32L;
/*  649 */     l1 += l2 * (paramArrayOfint2[4] & 0xFFFFFFFFL) + (paramArrayOfint1[4] & 0xFFFFFFFFL);
/*  650 */     paramArrayOfint2[4] = (int)l1;
/*  651 */     l1 >>>= 32L;
/*  652 */     l1 += l2 * (paramArrayOfint2[5] & 0xFFFFFFFFL) + (paramArrayOfint1[5] & 0xFFFFFFFFL);
/*  653 */     paramArrayOfint2[5] = (int)l1;
/*  654 */     l1 >>>= 32L;
/*  655 */     l1 += l2 * (paramArrayOfint2[6] & 0xFFFFFFFFL) + (paramArrayOfint1[6] & 0xFFFFFFFFL);
/*  656 */     paramArrayOfint2[6] = (int)l1;
/*  657 */     l1 >>>= 32L;
/*  658 */     return (int)l1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulWordAddTo(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/*  663 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/*  664 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 0] & 0xFFFFFFFFL);
/*  665 */     paramArrayOfint2[paramInt3 + 0] = (int)l1;
/*  666 */     l1 >>>= 32L;
/*  667 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 1] & 0xFFFFFFFFL);
/*  668 */     paramArrayOfint2[paramInt3 + 1] = (int)l1;
/*  669 */     l1 >>>= 32L;
/*  670 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 2] & 0xFFFFFFFFL);
/*  671 */     paramArrayOfint2[paramInt3 + 2] = (int)l1;
/*  672 */     l1 >>>= 32L;
/*  673 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 3] & 0xFFFFFFFFL);
/*  674 */     paramArrayOfint2[paramInt3 + 3] = (int)l1;
/*  675 */     l1 >>>= 32L;
/*  676 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 4] & 0xFFFFFFFFL);
/*  677 */     paramArrayOfint2[paramInt3 + 4] = (int)l1;
/*  678 */     l1 >>>= 32L;
/*  679 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 5] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 5] & 0xFFFFFFFFL);
/*  680 */     paramArrayOfint2[paramInt3 + 5] = (int)l1;
/*  681 */     l1 >>>= 32L;
/*  682 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 6] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 6] & 0xFFFFFFFFL);
/*  683 */     paramArrayOfint2[paramInt3 + 6] = (int)l1;
/*  684 */     l1 >>>= 32L;
/*  685 */     return (int)l1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int mul33DWordAdd(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2) {
/*  693 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/*  694 */     long l3 = paramLong & 0xFFFFFFFFL;
/*  695 */     l1 += l2 * l3 + (paramArrayOfint[paramInt2 + 0] & 0xFFFFFFFFL);
/*  696 */     paramArrayOfint[paramInt2 + 0] = (int)l1;
/*  697 */     l1 >>>= 32L;
/*  698 */     long l4 = paramLong >>> 32L;
/*  699 */     l1 += l2 * l4 + l3 + (paramArrayOfint[paramInt2 + 1] & 0xFFFFFFFFL);
/*  700 */     paramArrayOfint[paramInt2 + 1] = (int)l1;
/*  701 */     l1 >>>= 32L;
/*  702 */     l1 += l4 + (paramArrayOfint[paramInt2 + 2] & 0xFFFFFFFFL);
/*  703 */     paramArrayOfint[paramInt2 + 2] = (int)l1;
/*  704 */     l1 >>>= 32L;
/*  705 */     l1 += paramArrayOfint[paramInt2 + 3] & 0xFFFFFFFFL;
/*  706 */     paramArrayOfint[paramInt2 + 3] = (int)l1;
/*  707 */     l1 >>>= 32L;
/*  708 */     return (l1 == 0L) ? 0 : Nat.incAt(7, paramArrayOfint, paramInt2, 4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int mul33WordAdd(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
/*  716 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL, l3 = paramInt2 & 0xFFFFFFFFL;
/*  717 */     l1 += l3 * l2 + (paramArrayOfint[paramInt3 + 0] & 0xFFFFFFFFL);
/*  718 */     paramArrayOfint[paramInt3 + 0] = (int)l1;
/*  719 */     l1 >>>= 32L;
/*  720 */     l1 += l3 + (paramArrayOfint[paramInt3 + 1] & 0xFFFFFFFFL);
/*  721 */     paramArrayOfint[paramInt3 + 1] = (int)l1;
/*  722 */     l1 >>>= 32L;
/*  723 */     l1 += paramArrayOfint[paramInt3 + 2] & 0xFFFFFFFFL;
/*  724 */     paramArrayOfint[paramInt3 + 2] = (int)l1;
/*  725 */     l1 >>>= 32L;
/*  726 */     return (l1 == 0L) ? 0 : Nat.incAt(7, paramArrayOfint, paramInt3, 3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int mulWordDwordAdd(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2) {
/*  732 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/*  733 */     l1 += l2 * (paramLong & 0xFFFFFFFFL) + (paramArrayOfint[paramInt2 + 0] & 0xFFFFFFFFL);
/*  734 */     paramArrayOfint[paramInt2 + 0] = (int)l1;
/*  735 */     l1 >>>= 32L;
/*  736 */     l1 += l2 * (paramLong >>> 32L) + (paramArrayOfint[paramInt2 + 1] & 0xFFFFFFFFL);
/*  737 */     paramArrayOfint[paramInt2 + 1] = (int)l1;
/*  738 */     l1 >>>= 32L;
/*  739 */     l1 += paramArrayOfint[paramInt2 + 2] & 0xFFFFFFFFL;
/*  740 */     paramArrayOfint[paramInt2 + 2] = (int)l1;
/*  741 */     l1 >>>= 32L;
/*  742 */     return (l1 == 0L) ? 0 : Nat.incAt(7, paramArrayOfint, paramInt2, 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulWord(int paramInt1, int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt2) {
/*  747 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/*  748 */     byte b = 0;
/*      */     
/*      */     while (true) {
/*  751 */       l1 += l2 * (paramArrayOfint1[b] & 0xFFFFFFFFL);
/*  752 */       paramArrayOfint2[paramInt2 + b] = (int)l1;
/*  753 */       l1 >>>= 32L;
/*      */       
/*  755 */       if (++b >= 7)
/*  756 */         return (int)l1; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  761 */     long l1 = paramArrayOfint1[0] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  764 */     int i = 0;
/*      */     
/*  766 */     byte b1 = 6, b2 = 14;
/*      */     
/*      */     do {
/*  769 */       long l20 = paramArrayOfint1[b1--] & 0xFFFFFFFFL;
/*  770 */       long l21 = l20 * l20;
/*  771 */       paramArrayOfint2[--b2] = i << 31 | (int)(l21 >>> 33L);
/*  772 */       paramArrayOfint2[--b2] = (int)(l21 >>> 1L);
/*  773 */       i = (int)l21;
/*      */     }
/*  775 */     while (b1 > 0);
/*      */ 
/*      */     
/*  778 */     long l4 = l1 * l1;
/*  779 */     long l2 = (i << 31) & 0xFFFFFFFFL | l4 >>> 33L;
/*  780 */     paramArrayOfint2[0] = (int)l4;
/*  781 */     i = (int)(l4 >>> 32L) & 0x1;
/*      */ 
/*      */ 
/*      */     
/*  785 */     long l3 = paramArrayOfint1[1] & 0xFFFFFFFFL;
/*  786 */     l4 = paramArrayOfint2[2] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  789 */     l2 += l3 * l1;
/*  790 */     int j = (int)l2;
/*  791 */     paramArrayOfint2[1] = j << 1 | i;
/*  792 */     i = j >>> 31;
/*  793 */     l4 += l2 >>> 32L;
/*      */ 
/*      */     
/*  796 */     long l5 = paramArrayOfint1[2] & 0xFFFFFFFFL;
/*  797 */     long l6 = paramArrayOfint2[3] & 0xFFFFFFFFL;
/*  798 */     long l7 = paramArrayOfint2[4] & 0xFFFFFFFFL;
/*      */     
/*  800 */     l4 += l5 * l1;
/*  801 */     j = (int)l4;
/*  802 */     paramArrayOfint2[2] = j << 1 | i;
/*  803 */     i = j >>> 31;
/*  804 */     l6 += (l4 >>> 32L) + l5 * l3;
/*  805 */     l7 += l6 >>> 32L;
/*  806 */     l6 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  809 */     long l8 = paramArrayOfint1[3] & 0xFFFFFFFFL;
/*  810 */     long l9 = (paramArrayOfint2[5] & 0xFFFFFFFFL) + (l7 >>> 32L); l7 &= 0xFFFFFFFFL;
/*  811 */     long l10 = (paramArrayOfint2[6] & 0xFFFFFFFFL) + (l9 >>> 32L); l9 &= 0xFFFFFFFFL;
/*      */     
/*  813 */     l6 += l8 * l1;
/*  814 */     j = (int)l6;
/*  815 */     paramArrayOfint2[3] = j << 1 | i;
/*  816 */     i = j >>> 31;
/*  817 */     l7 += (l6 >>> 32L) + l8 * l3;
/*  818 */     l9 += (l7 >>> 32L) + l8 * l5;
/*  819 */     l7 &= 0xFFFFFFFFL;
/*  820 */     l10 += l9 >>> 32L;
/*  821 */     l9 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  824 */     long l11 = paramArrayOfint1[4] & 0xFFFFFFFFL;
/*  825 */     long l12 = (paramArrayOfint2[7] & 0xFFFFFFFFL) + (l10 >>> 32L); l10 &= 0xFFFFFFFFL;
/*  826 */     long l13 = (paramArrayOfint2[8] & 0xFFFFFFFFL) + (l12 >>> 32L); l12 &= 0xFFFFFFFFL;
/*      */     
/*  828 */     l7 += l11 * l1;
/*  829 */     j = (int)l7;
/*  830 */     paramArrayOfint2[4] = j << 1 | i;
/*  831 */     i = j >>> 31;
/*  832 */     l9 += (l7 >>> 32L) + l11 * l3;
/*  833 */     l10 += (l9 >>> 32L) + l11 * l5;
/*  834 */     l9 &= 0xFFFFFFFFL;
/*  835 */     l12 += (l10 >>> 32L) + l11 * l8;
/*  836 */     l10 &= 0xFFFFFFFFL;
/*  837 */     l13 += l12 >>> 32L;
/*  838 */     l12 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  841 */     long l14 = paramArrayOfint1[5] & 0xFFFFFFFFL;
/*  842 */     long l15 = (paramArrayOfint2[9] & 0xFFFFFFFFL) + (l13 >>> 32L); l13 &= 0xFFFFFFFFL;
/*  843 */     long l16 = (paramArrayOfint2[10] & 0xFFFFFFFFL) + (l15 >>> 32L); l15 &= 0xFFFFFFFFL;
/*      */     
/*  845 */     l9 += l14 * l1;
/*  846 */     j = (int)l9;
/*  847 */     paramArrayOfint2[5] = j << 1 | i;
/*  848 */     i = j >>> 31;
/*  849 */     l10 += (l9 >>> 32L) + l14 * l3;
/*  850 */     l12 += (l10 >>> 32L) + l14 * l5;
/*  851 */     l10 &= 0xFFFFFFFFL;
/*  852 */     l13 += (l12 >>> 32L) + l14 * l8;
/*  853 */     l12 &= 0xFFFFFFFFL;
/*  854 */     l15 += (l13 >>> 32L) + l14 * l11;
/*  855 */     l13 &= 0xFFFFFFFFL;
/*  856 */     l16 += l15 >>> 32L;
/*  857 */     l15 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  860 */     long l17 = paramArrayOfint1[6] & 0xFFFFFFFFL;
/*  861 */     long l18 = (paramArrayOfint2[11] & 0xFFFFFFFFL) + (l16 >>> 32L); l16 &= 0xFFFFFFFFL;
/*  862 */     long l19 = (paramArrayOfint2[12] & 0xFFFFFFFFL) + (l18 >>> 32L); l18 &= 0xFFFFFFFFL;
/*      */     
/*  864 */     l10 += l17 * l1;
/*  865 */     j = (int)l10;
/*  866 */     paramArrayOfint2[6] = j << 1 | i;
/*  867 */     i = j >>> 31;
/*  868 */     l12 += (l10 >>> 32L) + l17 * l3;
/*  869 */     l13 += (l12 >>> 32L) + l17 * l5;
/*  870 */     l15 += (l13 >>> 32L) + l17 * l8;
/*  871 */     l16 += (l15 >>> 32L) + l17 * l11;
/*  872 */     l18 += (l16 >>> 32L) + l17 * l14;
/*  873 */     l19 += l18 >>> 32L;
/*      */ 
/*      */     
/*  876 */     j = (int)l12;
/*  877 */     paramArrayOfint2[7] = j << 1 | i;
/*  878 */     i = j >>> 31;
/*  879 */     j = (int)l13;
/*  880 */     paramArrayOfint2[8] = j << 1 | i;
/*  881 */     i = j >>> 31;
/*  882 */     j = (int)l15;
/*  883 */     paramArrayOfint2[9] = j << 1 | i;
/*  884 */     i = j >>> 31;
/*  885 */     j = (int)l16;
/*  886 */     paramArrayOfint2[10] = j << 1 | i;
/*  887 */     i = j >>> 31;
/*  888 */     j = (int)l18;
/*  889 */     paramArrayOfint2[11] = j << 1 | i;
/*  890 */     i = j >>> 31;
/*  891 */     j = (int)l19;
/*  892 */     paramArrayOfint2[12] = j << 1 | i;
/*  893 */     i = j >>> 31;
/*  894 */     j = paramArrayOfint2[13] + (int)(l19 >>> 32L);
/*  895 */     paramArrayOfint2[13] = j << 1 | i;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void square(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/*  900 */     long l1 = paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  903 */     int i = 0;
/*      */     
/*  905 */     byte b1 = 6, b2 = 14;
/*      */     
/*      */     do {
/*  908 */       long l20 = paramArrayOfint1[paramInt1 + b1--] & 0xFFFFFFFFL;
/*  909 */       long l21 = l20 * l20;
/*  910 */       paramArrayOfint2[paramInt2 + --b2] = i << 31 | (int)(l21 >>> 33L);
/*  911 */       paramArrayOfint2[paramInt2 + --b2] = (int)(l21 >>> 1L);
/*  912 */       i = (int)l21;
/*      */     }
/*  914 */     while (b1 > 0);
/*      */ 
/*      */     
/*  917 */     long l4 = l1 * l1;
/*  918 */     long l2 = (i << 31) & 0xFFFFFFFFL | l4 >>> 33L;
/*  919 */     paramArrayOfint2[paramInt2 + 0] = (int)l4;
/*  920 */     i = (int)(l4 >>> 32L) & 0x1;
/*      */ 
/*      */ 
/*      */     
/*  924 */     long l3 = paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL;
/*  925 */     l4 = paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  928 */     l2 += l3 * l1;
/*  929 */     int j = (int)l2;
/*  930 */     paramArrayOfint2[paramInt2 + 1] = j << 1 | i;
/*  931 */     i = j >>> 31;
/*  932 */     l4 += l2 >>> 32L;
/*      */ 
/*      */     
/*  935 */     long l5 = paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL;
/*  936 */     long l6 = paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL;
/*  937 */     long l7 = paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL;
/*      */     
/*  939 */     l4 += l5 * l1;
/*  940 */     j = (int)l4;
/*  941 */     paramArrayOfint2[paramInt2 + 2] = j << 1 | i;
/*  942 */     i = j >>> 31;
/*  943 */     l6 += (l4 >>> 32L) + l5 * l3;
/*  944 */     l7 += l6 >>> 32L;
/*  945 */     l6 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  948 */     long l8 = paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL;
/*  949 */     long l9 = (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL) + (l7 >>> 32L); l7 &= 0xFFFFFFFFL;
/*  950 */     long l10 = (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL) + (l9 >>> 32L); l9 &= 0xFFFFFFFFL;
/*      */     
/*  952 */     l6 += l8 * l1;
/*  953 */     j = (int)l6;
/*  954 */     paramArrayOfint2[paramInt2 + 3] = j << 1 | i;
/*  955 */     i = j >>> 31;
/*  956 */     l7 += (l6 >>> 32L) + l8 * l3;
/*  957 */     l9 += (l7 >>> 32L) + l8 * l5;
/*  958 */     l7 &= 0xFFFFFFFFL;
/*  959 */     l10 += l9 >>> 32L;
/*  960 */     l9 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  963 */     long l11 = paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL;
/*  964 */     long l12 = (paramArrayOfint2[paramInt2 + 7] & 0xFFFFFFFFL) + (l10 >>> 32L); l10 &= 0xFFFFFFFFL;
/*  965 */     long l13 = (paramArrayOfint2[paramInt2 + 8] & 0xFFFFFFFFL) + (l12 >>> 32L); l12 &= 0xFFFFFFFFL;
/*      */     
/*  967 */     l7 += l11 * l1;
/*  968 */     j = (int)l7;
/*  969 */     paramArrayOfint2[paramInt2 + 4] = j << 1 | i;
/*  970 */     i = j >>> 31;
/*  971 */     l9 += (l7 >>> 32L) + l11 * l3;
/*  972 */     l10 += (l9 >>> 32L) + l11 * l5;
/*  973 */     l9 &= 0xFFFFFFFFL;
/*  974 */     l12 += (l10 >>> 32L) + l11 * l8;
/*  975 */     l10 &= 0xFFFFFFFFL;
/*  976 */     l13 += l12 >>> 32L;
/*  977 */     l12 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  980 */     long l14 = paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL;
/*  981 */     long l15 = (paramArrayOfint2[paramInt2 + 9] & 0xFFFFFFFFL) + (l13 >>> 32L); l13 &= 0xFFFFFFFFL;
/*  982 */     long l16 = (paramArrayOfint2[paramInt2 + 10] & 0xFFFFFFFFL) + (l15 >>> 32L); l15 &= 0xFFFFFFFFL;
/*      */     
/*  984 */     l9 += l14 * l1;
/*  985 */     j = (int)l9;
/*  986 */     paramArrayOfint2[paramInt2 + 5] = j << 1 | i;
/*  987 */     i = j >>> 31;
/*  988 */     l10 += (l9 >>> 32L) + l14 * l3;
/*  989 */     l12 += (l10 >>> 32L) + l14 * l5;
/*  990 */     l10 &= 0xFFFFFFFFL;
/*  991 */     l13 += (l12 >>> 32L) + l14 * l8;
/*  992 */     l12 &= 0xFFFFFFFFL;
/*  993 */     l15 += (l13 >>> 32L) + l14 * l11;
/*  994 */     l13 &= 0xFFFFFFFFL;
/*  995 */     l16 += l15 >>> 32L;
/*  996 */     l15 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  999 */     long l17 = paramArrayOfint1[paramInt1 + 6] & 0xFFFFFFFFL;
/* 1000 */     long l18 = (paramArrayOfint2[paramInt2 + 11] & 0xFFFFFFFFL) + (l16 >>> 32L); l16 &= 0xFFFFFFFFL;
/* 1001 */     long l19 = (paramArrayOfint2[paramInt2 + 12] & 0xFFFFFFFFL) + (l18 >>> 32L); l18 &= 0xFFFFFFFFL;
/*      */     
/* 1003 */     l10 += l17 * l1;
/* 1004 */     j = (int)l10;
/* 1005 */     paramArrayOfint2[paramInt2 + 6] = j << 1 | i;
/* 1006 */     i = j >>> 31;
/* 1007 */     l12 += (l10 >>> 32L) + l17 * l3;
/* 1008 */     l13 += (l12 >>> 32L) + l17 * l5;
/* 1009 */     l15 += (l13 >>> 32L) + l17 * l8;
/* 1010 */     l16 += (l15 >>> 32L) + l17 * l11;
/* 1011 */     l18 += (l16 >>> 32L) + l17 * l14;
/* 1012 */     l19 += l18 >>> 32L;
/*      */ 
/*      */     
/* 1015 */     j = (int)l12;
/* 1016 */     paramArrayOfint2[paramInt2 + 7] = j << 1 | i;
/* 1017 */     i = j >>> 31;
/* 1018 */     j = (int)l13;
/* 1019 */     paramArrayOfint2[paramInt2 + 8] = j << 1 | i;
/* 1020 */     i = j >>> 31;
/* 1021 */     j = (int)l15;
/* 1022 */     paramArrayOfint2[paramInt2 + 9] = j << 1 | i;
/* 1023 */     i = j >>> 31;
/* 1024 */     j = (int)l16;
/* 1025 */     paramArrayOfint2[paramInt2 + 10] = j << 1 | i;
/* 1026 */     i = j >>> 31;
/* 1027 */     j = (int)l18;
/* 1028 */     paramArrayOfint2[paramInt2 + 11] = j << 1 | i;
/* 1029 */     i = j >>> 31;
/* 1030 */     j = (int)l19;
/* 1031 */     paramArrayOfint2[paramInt2 + 12] = j << 1 | i;
/* 1032 */     i = j >>> 31;
/* 1033 */     j = paramArrayOfint2[paramInt2 + 13] + (int)(l19 >>> 32L);
/* 1034 */     paramArrayOfint2[paramInt2 + 13] = j << 1 | i;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int sub(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 1039 */     long l = 0L;
/* 1040 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) - (paramArrayOfint2[0] & 0xFFFFFFFFL);
/* 1041 */     paramArrayOfint3[0] = (int)l;
/* 1042 */     l >>= 32L;
/* 1043 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) - (paramArrayOfint2[1] & 0xFFFFFFFFL);
/* 1044 */     paramArrayOfint3[1] = (int)l;
/* 1045 */     l >>= 32L;
/* 1046 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) - (paramArrayOfint2[2] & 0xFFFFFFFFL);
/* 1047 */     paramArrayOfint3[2] = (int)l;
/* 1048 */     l >>= 32L;
/* 1049 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) - (paramArrayOfint2[3] & 0xFFFFFFFFL);
/* 1050 */     paramArrayOfint3[3] = (int)l;
/* 1051 */     l >>= 32L;
/* 1052 */     l += (paramArrayOfint1[4] & 0xFFFFFFFFL) - (paramArrayOfint2[4] & 0xFFFFFFFFL);
/* 1053 */     paramArrayOfint3[4] = (int)l;
/* 1054 */     l >>= 32L;
/* 1055 */     l += (paramArrayOfint1[5] & 0xFFFFFFFFL) - (paramArrayOfint2[5] & 0xFFFFFFFFL);
/* 1056 */     paramArrayOfint3[5] = (int)l;
/* 1057 */     l >>= 32L;
/* 1058 */     l += (paramArrayOfint1[6] & 0xFFFFFFFFL) - (paramArrayOfint2[6] & 0xFFFFFFFFL);
/* 1059 */     paramArrayOfint3[6] = (int)l;
/* 1060 */     l >>= 32L;
/* 1061 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int sub(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/* 1066 */     long l = 0L;
/* 1067 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/* 1068 */     paramArrayOfint3[paramInt3 + 0] = (int)l;
/* 1069 */     l >>= 32L;
/* 1070 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/* 1071 */     paramArrayOfint3[paramInt3 + 1] = (int)l;
/* 1072 */     l >>= 32L;
/* 1073 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/* 1074 */     paramArrayOfint3[paramInt3 + 2] = (int)l;
/* 1075 */     l >>= 32L;
/* 1076 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/* 1077 */     paramArrayOfint3[paramInt3 + 3] = (int)l;
/* 1078 */     l >>= 32L;
/* 1079 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL);
/* 1080 */     paramArrayOfint3[paramInt3 + 4] = (int)l;
/* 1081 */     l >>= 32L;
/* 1082 */     l += (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL);
/* 1083 */     paramArrayOfint3[paramInt3 + 5] = (int)l;
/* 1084 */     l >>= 32L;
/* 1085 */     l += (paramArrayOfint1[paramInt1 + 6] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL);
/* 1086 */     paramArrayOfint3[paramInt3 + 6] = (int)l;
/* 1087 */     l >>= 32L;
/* 1088 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subBothFrom(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 1093 */     long l = 0L;
/* 1094 */     l += (paramArrayOfint3[0] & 0xFFFFFFFFL) - (paramArrayOfint1[0] & 0xFFFFFFFFL) - (paramArrayOfint2[0] & 0xFFFFFFFFL);
/* 1095 */     paramArrayOfint3[0] = (int)l;
/* 1096 */     l >>= 32L;
/* 1097 */     l += (paramArrayOfint3[1] & 0xFFFFFFFFL) - (paramArrayOfint1[1] & 0xFFFFFFFFL) - (paramArrayOfint2[1] & 0xFFFFFFFFL);
/* 1098 */     paramArrayOfint3[1] = (int)l;
/* 1099 */     l >>= 32L;
/* 1100 */     l += (paramArrayOfint3[2] & 0xFFFFFFFFL) - (paramArrayOfint1[2] & 0xFFFFFFFFL) - (paramArrayOfint2[2] & 0xFFFFFFFFL);
/* 1101 */     paramArrayOfint3[2] = (int)l;
/* 1102 */     l >>= 32L;
/* 1103 */     l += (paramArrayOfint3[3] & 0xFFFFFFFFL) - (paramArrayOfint1[3] & 0xFFFFFFFFL) - (paramArrayOfint2[3] & 0xFFFFFFFFL);
/* 1104 */     paramArrayOfint3[3] = (int)l;
/* 1105 */     l >>= 32L;
/* 1106 */     l += (paramArrayOfint3[4] & 0xFFFFFFFFL) - (paramArrayOfint1[4] & 0xFFFFFFFFL) - (paramArrayOfint2[4] & 0xFFFFFFFFL);
/* 1107 */     paramArrayOfint3[4] = (int)l;
/* 1108 */     l >>= 32L;
/* 1109 */     l += (paramArrayOfint3[5] & 0xFFFFFFFFL) - (paramArrayOfint1[5] & 0xFFFFFFFFL) - (paramArrayOfint2[5] & 0xFFFFFFFFL);
/* 1110 */     paramArrayOfint3[5] = (int)l;
/* 1111 */     l >>= 32L;
/* 1112 */     l += (paramArrayOfint3[6] & 0xFFFFFFFFL) - (paramArrayOfint1[6] & 0xFFFFFFFFL) - (paramArrayOfint2[6] & 0xFFFFFFFFL);
/* 1113 */     paramArrayOfint3[6] = (int)l;
/* 1114 */     l >>= 32L;
/* 1115 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subFrom(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 1120 */     long l = 0L;
/* 1121 */     l += (paramArrayOfint2[0] & 0xFFFFFFFFL) - (paramArrayOfint1[0] & 0xFFFFFFFFL);
/* 1122 */     paramArrayOfint2[0] = (int)l;
/* 1123 */     l >>= 32L;
/* 1124 */     l += (paramArrayOfint2[1] & 0xFFFFFFFFL) - (paramArrayOfint1[1] & 0xFFFFFFFFL);
/* 1125 */     paramArrayOfint2[1] = (int)l;
/* 1126 */     l >>= 32L;
/* 1127 */     l += (paramArrayOfint2[2] & 0xFFFFFFFFL) - (paramArrayOfint1[2] & 0xFFFFFFFFL);
/* 1128 */     paramArrayOfint2[2] = (int)l;
/* 1129 */     l >>= 32L;
/* 1130 */     l += (paramArrayOfint2[3] & 0xFFFFFFFFL) - (paramArrayOfint1[3] & 0xFFFFFFFFL);
/* 1131 */     paramArrayOfint2[3] = (int)l;
/* 1132 */     l >>= 32L;
/* 1133 */     l += (paramArrayOfint2[4] & 0xFFFFFFFFL) - (paramArrayOfint1[4] & 0xFFFFFFFFL);
/* 1134 */     paramArrayOfint2[4] = (int)l;
/* 1135 */     l >>= 32L;
/* 1136 */     l += (paramArrayOfint2[5] & 0xFFFFFFFFL) - (paramArrayOfint1[5] & 0xFFFFFFFFL);
/* 1137 */     paramArrayOfint2[5] = (int)l;
/* 1138 */     l >>= 32L;
/* 1139 */     l += (paramArrayOfint2[6] & 0xFFFFFFFFL) - (paramArrayOfint1[6] & 0xFFFFFFFFL);
/* 1140 */     paramArrayOfint2[6] = (int)l;
/* 1141 */     l >>= 32L;
/* 1142 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subFrom(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/* 1147 */     long l = 0L;
/* 1148 */     l += (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL);
/* 1149 */     paramArrayOfint2[paramInt2 + 0] = (int)l;
/* 1150 */     l >>= 32L;
/* 1151 */     l += (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL);
/* 1152 */     paramArrayOfint2[paramInt2 + 1] = (int)l;
/* 1153 */     l >>= 32L;
/* 1154 */     l += (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL);
/* 1155 */     paramArrayOfint2[paramInt2 + 2] = (int)l;
/* 1156 */     l >>= 32L;
/* 1157 */     l += (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL);
/* 1158 */     paramArrayOfint2[paramInt2 + 3] = (int)l;
/* 1159 */     l >>= 32L;
/* 1160 */     l += (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL);
/* 1161 */     paramArrayOfint2[paramInt2 + 4] = (int)l;
/* 1162 */     l >>= 32L;
/* 1163 */     l += (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL);
/* 1164 */     paramArrayOfint2[paramInt2 + 5] = (int)l;
/* 1165 */     l >>= 32L;
/* 1166 */     l += (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 6] & 0xFFFFFFFFL);
/* 1167 */     paramArrayOfint2[paramInt2 + 6] = (int)l;
/* 1168 */     l >>= 32L;
/* 1169 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigInteger toBigInteger(int[] paramArrayOfint) {
/* 1174 */     byte[] arrayOfByte = new byte[28];
/* 1175 */     for (byte b = 0; b < 7; b++) {
/*      */       
/* 1177 */       int i = paramArrayOfint[b];
/* 1178 */       if (i != 0)
/*      */       {
/* 1180 */         Pack.intToBigEndian(i, arrayOfByte, 6 - b << 2);
/*      */       }
/*      */     } 
/* 1183 */     return new BigInteger(1, arrayOfByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void zero(int[] paramArrayOfint) {
/* 1188 */     paramArrayOfint[0] = 0;
/* 1189 */     paramArrayOfint[1] = 0;
/* 1190 */     paramArrayOfint[2] = 0;
/* 1191 */     paramArrayOfint[3] = 0;
/* 1192 */     paramArrayOfint[4] = 0;
/* 1193 */     paramArrayOfint[5] = 0;
/* 1194 */     paramArrayOfint[6] = 0;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/raw/Nat224.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */