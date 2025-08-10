/*      */ package META-INF.versions.9.org.bouncycastle.math.raw;
/*      */ 
/*      */ import java.math.BigInteger;
/*      */ import org.bouncycastle.math.raw.Nat;
/*      */ import org.bouncycastle.util.Pack;
/*      */ 
/*      */ 
/*      */ public abstract class Nat256
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
/*   35 */     l += (paramArrayOfint1[7] & 0xFFFFFFFFL) + (paramArrayOfint2[7] & 0xFFFFFFFFL);
/*   36 */     paramArrayOfint3[7] = (int)l;
/*   37 */     l >>>= 32L;
/*   38 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int add(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/*   43 */     long l = 0L;
/*   44 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/*   45 */     paramArrayOfint3[paramInt3 + 0] = (int)l;
/*   46 */     l >>>= 32L;
/*   47 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/*   48 */     paramArrayOfint3[paramInt3 + 1] = (int)l;
/*   49 */     l >>>= 32L;
/*   50 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/*   51 */     paramArrayOfint3[paramInt3 + 2] = (int)l;
/*   52 */     l >>>= 32L;
/*   53 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/*   54 */     paramArrayOfint3[paramInt3 + 3] = (int)l;
/*   55 */     l >>>= 32L;
/*   56 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL);
/*   57 */     paramArrayOfint3[paramInt3 + 4] = (int)l;
/*   58 */     l >>>= 32L;
/*   59 */     l += (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL);
/*   60 */     paramArrayOfint3[paramInt3 + 5] = (int)l;
/*   61 */     l >>>= 32L;
/*   62 */     l += (paramArrayOfint1[paramInt1 + 6] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL);
/*   63 */     paramArrayOfint3[paramInt3 + 6] = (int)l;
/*   64 */     l >>>= 32L;
/*   65 */     l += (paramArrayOfint1[paramInt1 + 7] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 7] & 0xFFFFFFFFL);
/*   66 */     paramArrayOfint3[paramInt3 + 7] = (int)l;
/*   67 */     l >>>= 32L;
/*   68 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addBothTo(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*   73 */     long l = 0L;
/*   74 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) + (paramArrayOfint2[0] & 0xFFFFFFFFL) + (paramArrayOfint3[0] & 0xFFFFFFFFL);
/*   75 */     paramArrayOfint3[0] = (int)l;
/*   76 */     l >>>= 32L;
/*   77 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) + (paramArrayOfint2[1] & 0xFFFFFFFFL) + (paramArrayOfint3[1] & 0xFFFFFFFFL);
/*   78 */     paramArrayOfint3[1] = (int)l;
/*   79 */     l >>>= 32L;
/*   80 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) + (paramArrayOfint2[2] & 0xFFFFFFFFL) + (paramArrayOfint3[2] & 0xFFFFFFFFL);
/*   81 */     paramArrayOfint3[2] = (int)l;
/*   82 */     l >>>= 32L;
/*   83 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) + (paramArrayOfint2[3] & 0xFFFFFFFFL) + (paramArrayOfint3[3] & 0xFFFFFFFFL);
/*   84 */     paramArrayOfint3[3] = (int)l;
/*   85 */     l >>>= 32L;
/*   86 */     l += (paramArrayOfint1[4] & 0xFFFFFFFFL) + (paramArrayOfint2[4] & 0xFFFFFFFFL) + (paramArrayOfint3[4] & 0xFFFFFFFFL);
/*   87 */     paramArrayOfint3[4] = (int)l;
/*   88 */     l >>>= 32L;
/*   89 */     l += (paramArrayOfint1[5] & 0xFFFFFFFFL) + (paramArrayOfint2[5] & 0xFFFFFFFFL) + (paramArrayOfint3[5] & 0xFFFFFFFFL);
/*   90 */     paramArrayOfint3[5] = (int)l;
/*   91 */     l >>>= 32L;
/*   92 */     l += (paramArrayOfint1[6] & 0xFFFFFFFFL) + (paramArrayOfint2[6] & 0xFFFFFFFFL) + (paramArrayOfint3[6] & 0xFFFFFFFFL);
/*   93 */     paramArrayOfint3[6] = (int)l;
/*   94 */     l >>>= 32L;
/*   95 */     l += (paramArrayOfint1[7] & 0xFFFFFFFFL) + (paramArrayOfint2[7] & 0xFFFFFFFFL) + (paramArrayOfint3[7] & 0xFFFFFFFFL);
/*   96 */     paramArrayOfint3[7] = (int)l;
/*   97 */     l >>>= 32L;
/*   98 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addBothTo(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/*  103 */     long l = 0L;
/*  104 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt3 + 0] & 0xFFFFFFFFL);
/*  105 */     paramArrayOfint3[paramInt3 + 0] = (int)l;
/*  106 */     l >>>= 32L;
/*  107 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt3 + 1] & 0xFFFFFFFFL);
/*  108 */     paramArrayOfint3[paramInt3 + 1] = (int)l;
/*  109 */     l >>>= 32L;
/*  110 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt3 + 2] & 0xFFFFFFFFL);
/*  111 */     paramArrayOfint3[paramInt3 + 2] = (int)l;
/*  112 */     l >>>= 32L;
/*  113 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt3 + 3] & 0xFFFFFFFFL);
/*  114 */     paramArrayOfint3[paramInt3 + 3] = (int)l;
/*  115 */     l >>>= 32L;
/*  116 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt3 + 4] & 0xFFFFFFFFL);
/*  117 */     paramArrayOfint3[paramInt3 + 4] = (int)l;
/*  118 */     l >>>= 32L;
/*  119 */     l += (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt3 + 5] & 0xFFFFFFFFL);
/*  120 */     paramArrayOfint3[paramInt3 + 5] = (int)l;
/*  121 */     l >>>= 32L;
/*  122 */     l += (paramArrayOfint1[paramInt1 + 6] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt3 + 6] & 0xFFFFFFFFL);
/*  123 */     paramArrayOfint3[paramInt3 + 6] = (int)l;
/*  124 */     l >>>= 32L;
/*  125 */     l += (paramArrayOfint1[paramInt1 + 7] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 7] & 0xFFFFFFFFL) + (paramArrayOfint3[paramInt3 + 7] & 0xFFFFFFFFL);
/*  126 */     paramArrayOfint3[paramInt3 + 7] = (int)l;
/*  127 */     l >>>= 32L;
/*  128 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addTo(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  133 */     long l = 0L;
/*  134 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) + (paramArrayOfint2[0] & 0xFFFFFFFFL);
/*  135 */     paramArrayOfint2[0] = (int)l;
/*  136 */     l >>>= 32L;
/*  137 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) + (paramArrayOfint2[1] & 0xFFFFFFFFL);
/*  138 */     paramArrayOfint2[1] = (int)l;
/*  139 */     l >>>= 32L;
/*  140 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) + (paramArrayOfint2[2] & 0xFFFFFFFFL);
/*  141 */     paramArrayOfint2[2] = (int)l;
/*  142 */     l >>>= 32L;
/*  143 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) + (paramArrayOfint2[3] & 0xFFFFFFFFL);
/*  144 */     paramArrayOfint2[3] = (int)l;
/*  145 */     l >>>= 32L;
/*  146 */     l += (paramArrayOfint1[4] & 0xFFFFFFFFL) + (paramArrayOfint2[4] & 0xFFFFFFFFL);
/*  147 */     paramArrayOfint2[4] = (int)l;
/*  148 */     l >>>= 32L;
/*  149 */     l += (paramArrayOfint1[5] & 0xFFFFFFFFL) + (paramArrayOfint2[5] & 0xFFFFFFFFL);
/*  150 */     paramArrayOfint2[5] = (int)l;
/*  151 */     l >>>= 32L;
/*  152 */     l += (paramArrayOfint1[6] & 0xFFFFFFFFL) + (paramArrayOfint2[6] & 0xFFFFFFFFL);
/*  153 */     paramArrayOfint2[6] = (int)l;
/*  154 */     l >>>= 32L;
/*  155 */     l += (paramArrayOfint1[7] & 0xFFFFFFFFL) + (paramArrayOfint2[7] & 0xFFFFFFFFL);
/*  156 */     paramArrayOfint2[7] = (int)l;
/*  157 */     l >>>= 32L;
/*  158 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addTo(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int paramInt3) {
/*  163 */     long l = paramInt3 & 0xFFFFFFFFL;
/*  164 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/*  165 */     paramArrayOfint2[paramInt2 + 0] = (int)l;
/*  166 */     l >>>= 32L;
/*  167 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/*  168 */     paramArrayOfint2[paramInt2 + 1] = (int)l;
/*  169 */     l >>>= 32L;
/*  170 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/*  171 */     paramArrayOfint2[paramInt2 + 2] = (int)l;
/*  172 */     l >>>= 32L;
/*  173 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/*  174 */     paramArrayOfint2[paramInt2 + 3] = (int)l;
/*  175 */     l >>>= 32L;
/*  176 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL);
/*  177 */     paramArrayOfint2[paramInt2 + 4] = (int)l;
/*  178 */     l >>>= 32L;
/*  179 */     l += (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL);
/*  180 */     paramArrayOfint2[paramInt2 + 5] = (int)l;
/*  181 */     l >>>= 32L;
/*  182 */     l += (paramArrayOfint1[paramInt1 + 6] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL);
/*  183 */     paramArrayOfint2[paramInt2 + 6] = (int)l;
/*  184 */     l >>>= 32L;
/*  185 */     l += (paramArrayOfint1[paramInt1 + 7] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 7] & 0xFFFFFFFFL);
/*  186 */     paramArrayOfint2[paramInt2 + 7] = (int)l;
/*  187 */     l >>>= 32L;
/*  188 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int addToEachOther(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/*  193 */     long l = 0L;
/*  194 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/*  195 */     paramArrayOfint1[paramInt1 + 0] = (int)l;
/*  196 */     paramArrayOfint2[paramInt2 + 0] = (int)l;
/*  197 */     l >>>= 32L;
/*  198 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/*  199 */     paramArrayOfint1[paramInt1 + 1] = (int)l;
/*  200 */     paramArrayOfint2[paramInt2 + 1] = (int)l;
/*  201 */     l >>>= 32L;
/*  202 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/*  203 */     paramArrayOfint1[paramInt1 + 2] = (int)l;
/*  204 */     paramArrayOfint2[paramInt2 + 2] = (int)l;
/*  205 */     l >>>= 32L;
/*  206 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/*  207 */     paramArrayOfint1[paramInt1 + 3] = (int)l;
/*  208 */     paramArrayOfint2[paramInt2 + 3] = (int)l;
/*  209 */     l >>>= 32L;
/*  210 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL);
/*  211 */     paramArrayOfint1[paramInt1 + 4] = (int)l;
/*  212 */     paramArrayOfint2[paramInt2 + 4] = (int)l;
/*  213 */     l >>>= 32L;
/*  214 */     l += (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL);
/*  215 */     paramArrayOfint1[paramInt1 + 5] = (int)l;
/*  216 */     paramArrayOfint2[paramInt2 + 5] = (int)l;
/*  217 */     l >>>= 32L;
/*  218 */     l += (paramArrayOfint1[paramInt1 + 6] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL);
/*  219 */     paramArrayOfint1[paramInt1 + 6] = (int)l;
/*  220 */     paramArrayOfint2[paramInt2 + 6] = (int)l;
/*  221 */     l >>>= 32L;
/*  222 */     l += (paramArrayOfint1[paramInt1 + 7] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt2 + 7] & 0xFFFFFFFFL);
/*  223 */     paramArrayOfint1[paramInt1 + 7] = (int)l;
/*  224 */     paramArrayOfint2[paramInt2 + 7] = (int)l;
/*  225 */     l >>>= 32L;
/*  226 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void copy(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  231 */     paramArrayOfint2[0] = paramArrayOfint1[0];
/*  232 */     paramArrayOfint2[1] = paramArrayOfint1[1];
/*  233 */     paramArrayOfint2[2] = paramArrayOfint1[2];
/*  234 */     paramArrayOfint2[3] = paramArrayOfint1[3];
/*  235 */     paramArrayOfint2[4] = paramArrayOfint1[4];
/*  236 */     paramArrayOfint2[5] = paramArrayOfint1[5];
/*  237 */     paramArrayOfint2[6] = paramArrayOfint1[6];
/*  238 */     paramArrayOfint2[7] = paramArrayOfint1[7];
/*      */   }
/*      */ 
/*      */   
/*      */   public static void copy(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/*  243 */     paramArrayOfint2[paramInt2 + 0] = paramArrayOfint1[paramInt1 + 0];
/*  244 */     paramArrayOfint2[paramInt2 + 1] = paramArrayOfint1[paramInt1 + 1];
/*  245 */     paramArrayOfint2[paramInt2 + 2] = paramArrayOfint1[paramInt1 + 2];
/*  246 */     paramArrayOfint2[paramInt2 + 3] = paramArrayOfint1[paramInt1 + 3];
/*  247 */     paramArrayOfint2[paramInt2 + 4] = paramArrayOfint1[paramInt1 + 4];
/*  248 */     paramArrayOfint2[paramInt2 + 5] = paramArrayOfint1[paramInt1 + 5];
/*  249 */     paramArrayOfint2[paramInt2 + 6] = paramArrayOfint1[paramInt1 + 6];
/*  250 */     paramArrayOfint2[paramInt2 + 7] = paramArrayOfint1[paramInt1 + 7];
/*      */   }
/*      */ 
/*      */   
/*      */   public static void copy64(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  255 */     paramArrayOflong2[0] = paramArrayOflong1[0];
/*  256 */     paramArrayOflong2[1] = paramArrayOflong1[1];
/*  257 */     paramArrayOflong2[2] = paramArrayOflong1[2];
/*  258 */     paramArrayOflong2[3] = paramArrayOflong1[3];
/*      */   }
/*      */ 
/*      */   
/*      */   public static void copy64(long[] paramArrayOflong1, int paramInt1, long[] paramArrayOflong2, int paramInt2) {
/*  263 */     paramArrayOflong2[paramInt2 + 0] = paramArrayOflong1[paramInt1 + 0];
/*  264 */     paramArrayOflong2[paramInt2 + 1] = paramArrayOflong1[paramInt1 + 1];
/*  265 */     paramArrayOflong2[paramInt2 + 2] = paramArrayOflong1[paramInt1 + 2];
/*  266 */     paramArrayOflong2[paramInt2 + 3] = paramArrayOflong1[paramInt1 + 3];
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] create() {
/*  271 */     return new int[8];
/*      */   }
/*      */ 
/*      */   
/*      */   public static long[] create64() {
/*  276 */     return new long[4];
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] createExt() {
/*  281 */     return new int[16];
/*      */   }
/*      */ 
/*      */   
/*      */   public static long[] createExt64() {
/*  286 */     return new long[8];
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean diff(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/*  291 */     boolean bool = gte(paramArrayOfint1, paramInt1, paramArrayOfint2, paramInt2);
/*  292 */     if (bool) {
/*      */       
/*  294 */       sub(paramArrayOfint1, paramInt1, paramArrayOfint2, paramInt2, paramArrayOfint3, paramInt3);
/*      */     }
/*      */     else {
/*      */       
/*  298 */       sub(paramArrayOfint2, paramInt2, paramArrayOfint1, paramInt1, paramArrayOfint3, paramInt3);
/*      */     } 
/*  300 */     return bool;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean eq(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  305 */     for (byte b = 7; b >= 0; b--) {
/*      */       
/*  307 */       if (paramArrayOfint1[b] != paramArrayOfint2[b])
/*      */       {
/*  309 */         return false;
/*      */       }
/*      */     } 
/*  312 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean eq64(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  317 */     for (byte b = 3; b >= 0; b--) {
/*      */       
/*  319 */       if (paramArrayOflong1[b] != paramArrayOflong2[b])
/*      */       {
/*  321 */         return false;
/*      */       }
/*      */     } 
/*  324 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/*  329 */     if (paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > 256)
/*      */     {
/*  331 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*  334 */     int[] arrayOfInt = create();
/*      */ 
/*      */     
/*  337 */     for (byte b = 0; b < 8; b++) {
/*      */       
/*  339 */       arrayOfInt[b] = paramBigInteger.intValue();
/*  340 */       paramBigInteger = paramBigInteger.shiftRight(32);
/*      */     } 
/*  342 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   
/*      */   public static long[] fromBigInteger64(BigInteger paramBigInteger) {
/*  347 */     if (paramBigInteger.signum() < 0 || paramBigInteger.bitLength() > 256)
/*      */     {
/*  349 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*  352 */     long[] arrayOfLong = create64();
/*      */ 
/*      */     
/*  355 */     for (byte b = 0; b < 4; b++) {
/*      */       
/*  357 */       arrayOfLong[b] = paramBigInteger.longValue();
/*  358 */       paramBigInteger = paramBigInteger.shiftRight(64);
/*      */     } 
/*  360 */     return arrayOfLong;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int getBit(int[] paramArrayOfint, int paramInt) {
/*  365 */     if (paramInt == 0)
/*      */     {
/*  367 */       return paramArrayOfint[0] & 0x1;
/*      */     }
/*  369 */     if ((paramInt & 0xFF) != paramInt)
/*      */     {
/*  371 */       return 0;
/*      */     }
/*  373 */     int i = paramInt >>> 5;
/*  374 */     int j = paramInt & 0x1F;
/*  375 */     return paramArrayOfint[i] >>> j & 0x1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean gte(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  380 */     for (byte b = 7; b >= 0; b--) {
/*      */       
/*  382 */       int i = paramArrayOfint1[b] ^ Integer.MIN_VALUE;
/*  383 */       int j = paramArrayOfint2[b] ^ Integer.MIN_VALUE;
/*  384 */       if (i < j)
/*  385 */         return false; 
/*  386 */       if (i > j)
/*  387 */         return true; 
/*      */     } 
/*  389 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean gte(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/*  394 */     for (byte b = 7; b >= 0; b--) {
/*      */       
/*  396 */       int i = paramArrayOfint1[paramInt1 + b] ^ Integer.MIN_VALUE;
/*  397 */       int j = paramArrayOfint2[paramInt2 + b] ^ Integer.MIN_VALUE;
/*  398 */       if (i < j)
/*  399 */         return false; 
/*  400 */       if (i > j)
/*  401 */         return true; 
/*      */     } 
/*  403 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isOne(int[] paramArrayOfint) {
/*  408 */     if (paramArrayOfint[0] != 1)
/*      */     {
/*  410 */       return false;
/*      */     }
/*  412 */     for (byte b = 1; b < 8; b++) {
/*      */       
/*  414 */       if (paramArrayOfint[b] != 0)
/*      */       {
/*  416 */         return false;
/*      */       }
/*      */     } 
/*  419 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isOne64(long[] paramArrayOflong) {
/*  424 */     if (paramArrayOflong[0] != 1L)
/*      */     {
/*  426 */       return false;
/*      */     }
/*  428 */     for (byte b = 1; b < 4; b++) {
/*      */       
/*  430 */       if (paramArrayOflong[b] != 0L)
/*      */       {
/*  432 */         return false;
/*      */       }
/*      */     } 
/*  435 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isZero(int[] paramArrayOfint) {
/*  440 */     for (byte b = 0; b < 8; b++) {
/*      */       
/*  442 */       if (paramArrayOfint[b] != 0)
/*      */       {
/*  444 */         return false;
/*      */       }
/*      */     } 
/*  447 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isZero64(long[] paramArrayOflong) {
/*  452 */     for (byte b = 0; b < 4; b++) {
/*      */       
/*  454 */       if (paramArrayOflong[b] != 0L)
/*      */       {
/*  456 */         return false;
/*      */       }
/*      */     } 
/*  459 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void mul(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  464 */     long l1 = paramArrayOfint2[0] & 0xFFFFFFFFL;
/*  465 */     long l2 = paramArrayOfint2[1] & 0xFFFFFFFFL;
/*  466 */     long l3 = paramArrayOfint2[2] & 0xFFFFFFFFL;
/*  467 */     long l4 = paramArrayOfint2[3] & 0xFFFFFFFFL;
/*  468 */     long l5 = paramArrayOfint2[4] & 0xFFFFFFFFL;
/*  469 */     long l6 = paramArrayOfint2[5] & 0xFFFFFFFFL;
/*  470 */     long l7 = paramArrayOfint2[6] & 0xFFFFFFFFL;
/*  471 */     long l8 = paramArrayOfint2[7] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  474 */     long l9 = 0L, l10 = paramArrayOfint1[0] & 0xFFFFFFFFL;
/*  475 */     l9 += l10 * l1;
/*  476 */     paramArrayOfint3[0] = (int)l9;
/*  477 */     l9 >>>= 32L;
/*  478 */     l9 += l10 * l2;
/*  479 */     paramArrayOfint3[1] = (int)l9;
/*  480 */     l9 >>>= 32L;
/*  481 */     l9 += l10 * l3;
/*  482 */     paramArrayOfint3[2] = (int)l9;
/*  483 */     l9 >>>= 32L;
/*  484 */     l9 += l10 * l4;
/*  485 */     paramArrayOfint3[3] = (int)l9;
/*  486 */     l9 >>>= 32L;
/*  487 */     l9 += l10 * l5;
/*  488 */     paramArrayOfint3[4] = (int)l9;
/*  489 */     l9 >>>= 32L;
/*  490 */     l9 += l10 * l6;
/*  491 */     paramArrayOfint3[5] = (int)l9;
/*  492 */     l9 >>>= 32L;
/*  493 */     l9 += l10 * l7;
/*  494 */     paramArrayOfint3[6] = (int)l9;
/*  495 */     l9 >>>= 32L;
/*  496 */     l9 += l10 * l8;
/*  497 */     paramArrayOfint3[7] = (int)l9;
/*  498 */     l9 >>>= 32L;
/*  499 */     paramArrayOfint3[8] = (int)l9;
/*      */ 
/*      */     
/*  502 */     for (byte b = 1; b < 8; b++) {
/*      */       
/*  504 */       long l11 = 0L, l12 = paramArrayOfint1[b] & 0xFFFFFFFFL;
/*  505 */       l11 += l12 * l1 + (paramArrayOfint3[b + 0] & 0xFFFFFFFFL);
/*  506 */       paramArrayOfint3[b + 0] = (int)l11;
/*  507 */       l11 >>>= 32L;
/*  508 */       l11 += l12 * l2 + (paramArrayOfint3[b + 1] & 0xFFFFFFFFL);
/*  509 */       paramArrayOfint3[b + 1] = (int)l11;
/*  510 */       l11 >>>= 32L;
/*  511 */       l11 += l12 * l3 + (paramArrayOfint3[b + 2] & 0xFFFFFFFFL);
/*  512 */       paramArrayOfint3[b + 2] = (int)l11;
/*  513 */       l11 >>>= 32L;
/*  514 */       l11 += l12 * l4 + (paramArrayOfint3[b + 3] & 0xFFFFFFFFL);
/*  515 */       paramArrayOfint3[b + 3] = (int)l11;
/*  516 */       l11 >>>= 32L;
/*  517 */       l11 += l12 * l5 + (paramArrayOfint3[b + 4] & 0xFFFFFFFFL);
/*  518 */       paramArrayOfint3[b + 4] = (int)l11;
/*  519 */       l11 >>>= 32L;
/*  520 */       l11 += l12 * l6 + (paramArrayOfint3[b + 5] & 0xFFFFFFFFL);
/*  521 */       paramArrayOfint3[b + 5] = (int)l11;
/*  522 */       l11 >>>= 32L;
/*  523 */       l11 += l12 * l7 + (paramArrayOfint3[b + 6] & 0xFFFFFFFFL);
/*  524 */       paramArrayOfint3[b + 6] = (int)l11;
/*  525 */       l11 >>>= 32L;
/*  526 */       l11 += l12 * l8 + (paramArrayOfint3[b + 7] & 0xFFFFFFFFL);
/*  527 */       paramArrayOfint3[b + 7] = (int)l11;
/*  528 */       l11 >>>= 32L;
/*  529 */       paramArrayOfint3[b + 8] = (int)l11;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void mul(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/*  535 */     long l1 = paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL;
/*  536 */     long l2 = paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL;
/*  537 */     long l3 = paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL;
/*  538 */     long l4 = paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL;
/*  539 */     long l5 = paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL;
/*  540 */     long l6 = paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL;
/*  541 */     long l7 = paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL;
/*  542 */     long l8 = paramArrayOfint2[paramInt2 + 7] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  545 */     long l9 = 0L, l10 = paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL;
/*  546 */     l9 += l10 * l1;
/*  547 */     paramArrayOfint3[paramInt3 + 0] = (int)l9;
/*  548 */     l9 >>>= 32L;
/*  549 */     l9 += l10 * l2;
/*  550 */     paramArrayOfint3[paramInt3 + 1] = (int)l9;
/*  551 */     l9 >>>= 32L;
/*  552 */     l9 += l10 * l3;
/*  553 */     paramArrayOfint3[paramInt3 + 2] = (int)l9;
/*  554 */     l9 >>>= 32L;
/*  555 */     l9 += l10 * l4;
/*  556 */     paramArrayOfint3[paramInt3 + 3] = (int)l9;
/*  557 */     l9 >>>= 32L;
/*  558 */     l9 += l10 * l5;
/*  559 */     paramArrayOfint3[paramInt3 + 4] = (int)l9;
/*  560 */     l9 >>>= 32L;
/*  561 */     l9 += l10 * l6;
/*  562 */     paramArrayOfint3[paramInt3 + 5] = (int)l9;
/*  563 */     l9 >>>= 32L;
/*  564 */     l9 += l10 * l7;
/*  565 */     paramArrayOfint3[paramInt3 + 6] = (int)l9;
/*  566 */     l9 >>>= 32L;
/*  567 */     l9 += l10 * l8;
/*  568 */     paramArrayOfint3[paramInt3 + 7] = (int)l9;
/*  569 */     l9 >>>= 32L;
/*  570 */     paramArrayOfint3[paramInt3 + 8] = (int)l9;
/*      */ 
/*      */     
/*  573 */     for (byte b = 1; b < 8; b++) {
/*      */       
/*  575 */       paramInt3++;
/*  576 */       long l11 = 0L, l12 = paramArrayOfint1[paramInt1 + b] & 0xFFFFFFFFL;
/*  577 */       l11 += l12 * l1 + (paramArrayOfint3[paramInt3 + 0] & 0xFFFFFFFFL);
/*  578 */       paramArrayOfint3[paramInt3 + 0] = (int)l11;
/*  579 */       l11 >>>= 32L;
/*  580 */       l11 += l12 * l2 + (paramArrayOfint3[paramInt3 + 1] & 0xFFFFFFFFL);
/*  581 */       paramArrayOfint3[paramInt3 + 1] = (int)l11;
/*  582 */       l11 >>>= 32L;
/*  583 */       l11 += l12 * l3 + (paramArrayOfint3[paramInt3 + 2] & 0xFFFFFFFFL);
/*  584 */       paramArrayOfint3[paramInt3 + 2] = (int)l11;
/*  585 */       l11 >>>= 32L;
/*  586 */       l11 += l12 * l4 + (paramArrayOfint3[paramInt3 + 3] & 0xFFFFFFFFL);
/*  587 */       paramArrayOfint3[paramInt3 + 3] = (int)l11;
/*  588 */       l11 >>>= 32L;
/*  589 */       l11 += l12 * l5 + (paramArrayOfint3[paramInt3 + 4] & 0xFFFFFFFFL);
/*  590 */       paramArrayOfint3[paramInt3 + 4] = (int)l11;
/*  591 */       l11 >>>= 32L;
/*  592 */       l11 += l12 * l6 + (paramArrayOfint3[paramInt3 + 5] & 0xFFFFFFFFL);
/*  593 */       paramArrayOfint3[paramInt3 + 5] = (int)l11;
/*  594 */       l11 >>>= 32L;
/*  595 */       l11 += l12 * l7 + (paramArrayOfint3[paramInt3 + 6] & 0xFFFFFFFFL);
/*  596 */       paramArrayOfint3[paramInt3 + 6] = (int)l11;
/*  597 */       l11 >>>= 32L;
/*  598 */       l11 += l12 * l8 + (paramArrayOfint3[paramInt3 + 7] & 0xFFFFFFFFL);
/*  599 */       paramArrayOfint3[paramInt3 + 7] = (int)l11;
/*  600 */       l11 >>>= 32L;
/*  601 */       paramArrayOfint3[paramInt3 + 8] = (int)l11;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulAddTo(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  607 */     long l1 = paramArrayOfint2[0] & 0xFFFFFFFFL;
/*  608 */     long l2 = paramArrayOfint2[1] & 0xFFFFFFFFL;
/*  609 */     long l3 = paramArrayOfint2[2] & 0xFFFFFFFFL;
/*  610 */     long l4 = paramArrayOfint2[3] & 0xFFFFFFFFL;
/*  611 */     long l5 = paramArrayOfint2[4] & 0xFFFFFFFFL;
/*  612 */     long l6 = paramArrayOfint2[5] & 0xFFFFFFFFL;
/*  613 */     long l7 = paramArrayOfint2[6] & 0xFFFFFFFFL;
/*  614 */     long l8 = paramArrayOfint2[7] & 0xFFFFFFFFL;
/*      */     
/*  616 */     long l9 = 0L;
/*  617 */     for (byte b = 0; b < 8; b++) {
/*      */       
/*  619 */       long l10 = 0L, l11 = paramArrayOfint1[b] & 0xFFFFFFFFL;
/*  620 */       l10 += l11 * l1 + (paramArrayOfint3[b + 0] & 0xFFFFFFFFL);
/*  621 */       paramArrayOfint3[b + 0] = (int)l10;
/*  622 */       l10 >>>= 32L;
/*  623 */       l10 += l11 * l2 + (paramArrayOfint3[b + 1] & 0xFFFFFFFFL);
/*  624 */       paramArrayOfint3[b + 1] = (int)l10;
/*  625 */       l10 >>>= 32L;
/*  626 */       l10 += l11 * l3 + (paramArrayOfint3[b + 2] & 0xFFFFFFFFL);
/*  627 */       paramArrayOfint3[b + 2] = (int)l10;
/*  628 */       l10 >>>= 32L;
/*  629 */       l10 += l11 * l4 + (paramArrayOfint3[b + 3] & 0xFFFFFFFFL);
/*  630 */       paramArrayOfint3[b + 3] = (int)l10;
/*  631 */       l10 >>>= 32L;
/*  632 */       l10 += l11 * l5 + (paramArrayOfint3[b + 4] & 0xFFFFFFFFL);
/*  633 */       paramArrayOfint3[b + 4] = (int)l10;
/*  634 */       l10 >>>= 32L;
/*  635 */       l10 += l11 * l6 + (paramArrayOfint3[b + 5] & 0xFFFFFFFFL);
/*  636 */       paramArrayOfint3[b + 5] = (int)l10;
/*  637 */       l10 >>>= 32L;
/*  638 */       l10 += l11 * l7 + (paramArrayOfint3[b + 6] & 0xFFFFFFFFL);
/*  639 */       paramArrayOfint3[b + 6] = (int)l10;
/*  640 */       l10 >>>= 32L;
/*  641 */       l10 += l11 * l8 + (paramArrayOfint3[b + 7] & 0xFFFFFFFFL);
/*  642 */       paramArrayOfint3[b + 7] = (int)l10;
/*  643 */       l10 >>>= 32L;
/*      */       
/*  645 */       l9 += l10 + (paramArrayOfint3[b + 8] & 0xFFFFFFFFL);
/*  646 */       paramArrayOfint3[b + 8] = (int)l9;
/*  647 */       l9 >>>= 32L;
/*      */     } 
/*  649 */     return (int)l9;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulAddTo(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/*  654 */     long l1 = paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL;
/*  655 */     long l2 = paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL;
/*  656 */     long l3 = paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL;
/*  657 */     long l4 = paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL;
/*  658 */     long l5 = paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL;
/*  659 */     long l6 = paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL;
/*  660 */     long l7 = paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL;
/*  661 */     long l8 = paramArrayOfint2[paramInt2 + 7] & 0xFFFFFFFFL;
/*      */     
/*  663 */     long l9 = 0L;
/*  664 */     for (byte b = 0; b < 8; b++) {
/*      */       
/*  666 */       long l10 = 0L, l11 = paramArrayOfint1[paramInt1 + b] & 0xFFFFFFFFL;
/*  667 */       l10 += l11 * l1 + (paramArrayOfint3[paramInt3 + 0] & 0xFFFFFFFFL);
/*  668 */       paramArrayOfint3[paramInt3 + 0] = (int)l10;
/*  669 */       l10 >>>= 32L;
/*  670 */       l10 += l11 * l2 + (paramArrayOfint3[paramInt3 + 1] & 0xFFFFFFFFL);
/*  671 */       paramArrayOfint3[paramInt3 + 1] = (int)l10;
/*  672 */       l10 >>>= 32L;
/*  673 */       l10 += l11 * l3 + (paramArrayOfint3[paramInt3 + 2] & 0xFFFFFFFFL);
/*  674 */       paramArrayOfint3[paramInt3 + 2] = (int)l10;
/*  675 */       l10 >>>= 32L;
/*  676 */       l10 += l11 * l4 + (paramArrayOfint3[paramInt3 + 3] & 0xFFFFFFFFL);
/*  677 */       paramArrayOfint3[paramInt3 + 3] = (int)l10;
/*  678 */       l10 >>>= 32L;
/*  679 */       l10 += l11 * l5 + (paramArrayOfint3[paramInt3 + 4] & 0xFFFFFFFFL);
/*  680 */       paramArrayOfint3[paramInt3 + 4] = (int)l10;
/*  681 */       l10 >>>= 32L;
/*  682 */       l10 += l11 * l6 + (paramArrayOfint3[paramInt3 + 5] & 0xFFFFFFFFL);
/*  683 */       paramArrayOfint3[paramInt3 + 5] = (int)l10;
/*  684 */       l10 >>>= 32L;
/*  685 */       l10 += l11 * l7 + (paramArrayOfint3[paramInt3 + 6] & 0xFFFFFFFFL);
/*  686 */       paramArrayOfint3[paramInt3 + 6] = (int)l10;
/*  687 */       l10 >>>= 32L;
/*  688 */       l10 += l11 * l8 + (paramArrayOfint3[paramInt3 + 7] & 0xFFFFFFFFL);
/*  689 */       paramArrayOfint3[paramInt3 + 7] = (int)l10;
/*  690 */       l10 >>>= 32L;
/*      */       
/*  692 */       l9 += l10 + (paramArrayOfint3[paramInt3 + 8] & 0xFFFFFFFFL);
/*  693 */       paramArrayOfint3[paramInt3 + 8] = (int)l9;
/*  694 */       l9 >>>= 32L;
/*  695 */       paramInt3++;
/*      */     } 
/*  697 */     return (int)l9;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long mul33Add(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3, int[] paramArrayOfint3, int paramInt4) {
/*  704 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/*  705 */     long l3 = paramArrayOfint1[paramInt2 + 0] & 0xFFFFFFFFL;
/*  706 */     l1 += l2 * l3 + (paramArrayOfint2[paramInt3 + 0] & 0xFFFFFFFFL);
/*  707 */     paramArrayOfint3[paramInt4 + 0] = (int)l1;
/*  708 */     l1 >>>= 32L;
/*  709 */     long l4 = paramArrayOfint1[paramInt2 + 1] & 0xFFFFFFFFL;
/*  710 */     l1 += l2 * l4 + l3 + (paramArrayOfint2[paramInt3 + 1] & 0xFFFFFFFFL);
/*  711 */     paramArrayOfint3[paramInt4 + 1] = (int)l1;
/*  712 */     l1 >>>= 32L;
/*  713 */     long l5 = paramArrayOfint1[paramInt2 + 2] & 0xFFFFFFFFL;
/*  714 */     l1 += l2 * l5 + l4 + (paramArrayOfint2[paramInt3 + 2] & 0xFFFFFFFFL);
/*  715 */     paramArrayOfint3[paramInt4 + 2] = (int)l1;
/*  716 */     l1 >>>= 32L;
/*  717 */     long l6 = paramArrayOfint1[paramInt2 + 3] & 0xFFFFFFFFL;
/*  718 */     l1 += l2 * l6 + l5 + (paramArrayOfint2[paramInt3 + 3] & 0xFFFFFFFFL);
/*  719 */     paramArrayOfint3[paramInt4 + 3] = (int)l1;
/*  720 */     l1 >>>= 32L;
/*  721 */     long l7 = paramArrayOfint1[paramInt2 + 4] & 0xFFFFFFFFL;
/*  722 */     l1 += l2 * l7 + l6 + (paramArrayOfint2[paramInt3 + 4] & 0xFFFFFFFFL);
/*  723 */     paramArrayOfint3[paramInt4 + 4] = (int)l1;
/*  724 */     l1 >>>= 32L;
/*  725 */     long l8 = paramArrayOfint1[paramInt2 + 5] & 0xFFFFFFFFL;
/*  726 */     l1 += l2 * l8 + l7 + (paramArrayOfint2[paramInt3 + 5] & 0xFFFFFFFFL);
/*  727 */     paramArrayOfint3[paramInt4 + 5] = (int)l1;
/*  728 */     l1 >>>= 32L;
/*  729 */     long l9 = paramArrayOfint1[paramInt2 + 6] & 0xFFFFFFFFL;
/*  730 */     l1 += l2 * l9 + l8 + (paramArrayOfint2[paramInt3 + 6] & 0xFFFFFFFFL);
/*  731 */     paramArrayOfint3[paramInt4 + 6] = (int)l1;
/*  732 */     l1 >>>= 32L;
/*  733 */     long l10 = paramArrayOfint1[paramInt2 + 7] & 0xFFFFFFFFL;
/*  734 */     l1 += l2 * l10 + l9 + (paramArrayOfint2[paramInt3 + 7] & 0xFFFFFFFFL);
/*  735 */     paramArrayOfint3[paramInt4 + 7] = (int)l1;
/*  736 */     l1 >>>= 32L;
/*  737 */     l1 += l10;
/*  738 */     return l1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulByWord(int paramInt, int[] paramArrayOfint) {
/*  743 */     long l1 = 0L, l2 = paramInt & 0xFFFFFFFFL;
/*  744 */     l1 += l2 * (paramArrayOfint[0] & 0xFFFFFFFFL);
/*  745 */     paramArrayOfint[0] = (int)l1;
/*  746 */     l1 >>>= 32L;
/*  747 */     l1 += l2 * (paramArrayOfint[1] & 0xFFFFFFFFL);
/*  748 */     paramArrayOfint[1] = (int)l1;
/*  749 */     l1 >>>= 32L;
/*  750 */     l1 += l2 * (paramArrayOfint[2] & 0xFFFFFFFFL);
/*  751 */     paramArrayOfint[2] = (int)l1;
/*  752 */     l1 >>>= 32L;
/*  753 */     l1 += l2 * (paramArrayOfint[3] & 0xFFFFFFFFL);
/*  754 */     paramArrayOfint[3] = (int)l1;
/*  755 */     l1 >>>= 32L;
/*  756 */     l1 += l2 * (paramArrayOfint[4] & 0xFFFFFFFFL);
/*  757 */     paramArrayOfint[4] = (int)l1;
/*  758 */     l1 >>>= 32L;
/*  759 */     l1 += l2 * (paramArrayOfint[5] & 0xFFFFFFFFL);
/*  760 */     paramArrayOfint[5] = (int)l1;
/*  761 */     l1 >>>= 32L;
/*  762 */     l1 += l2 * (paramArrayOfint[6] & 0xFFFFFFFFL);
/*  763 */     paramArrayOfint[6] = (int)l1;
/*  764 */     l1 >>>= 32L;
/*  765 */     l1 += l2 * (paramArrayOfint[7] & 0xFFFFFFFFL);
/*  766 */     paramArrayOfint[7] = (int)l1;
/*  767 */     l1 >>>= 32L;
/*  768 */     return (int)l1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulByWordAddTo(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  773 */     long l1 = 0L, l2 = paramInt & 0xFFFFFFFFL;
/*  774 */     l1 += l2 * (paramArrayOfint2[0] & 0xFFFFFFFFL) + (paramArrayOfint1[0] & 0xFFFFFFFFL);
/*  775 */     paramArrayOfint2[0] = (int)l1;
/*  776 */     l1 >>>= 32L;
/*  777 */     l1 += l2 * (paramArrayOfint2[1] & 0xFFFFFFFFL) + (paramArrayOfint1[1] & 0xFFFFFFFFL);
/*  778 */     paramArrayOfint2[1] = (int)l1;
/*  779 */     l1 >>>= 32L;
/*  780 */     l1 += l2 * (paramArrayOfint2[2] & 0xFFFFFFFFL) + (paramArrayOfint1[2] & 0xFFFFFFFFL);
/*  781 */     paramArrayOfint2[2] = (int)l1;
/*  782 */     l1 >>>= 32L;
/*  783 */     l1 += l2 * (paramArrayOfint2[3] & 0xFFFFFFFFL) + (paramArrayOfint1[3] & 0xFFFFFFFFL);
/*  784 */     paramArrayOfint2[3] = (int)l1;
/*  785 */     l1 >>>= 32L;
/*  786 */     l1 += l2 * (paramArrayOfint2[4] & 0xFFFFFFFFL) + (paramArrayOfint1[4] & 0xFFFFFFFFL);
/*  787 */     paramArrayOfint2[4] = (int)l1;
/*  788 */     l1 >>>= 32L;
/*  789 */     l1 += l2 * (paramArrayOfint2[5] & 0xFFFFFFFFL) + (paramArrayOfint1[5] & 0xFFFFFFFFL);
/*  790 */     paramArrayOfint2[5] = (int)l1;
/*  791 */     l1 >>>= 32L;
/*  792 */     l1 += l2 * (paramArrayOfint2[6] & 0xFFFFFFFFL) + (paramArrayOfint1[6] & 0xFFFFFFFFL);
/*  793 */     paramArrayOfint2[6] = (int)l1;
/*  794 */     l1 >>>= 32L;
/*  795 */     l1 += l2 * (paramArrayOfint2[7] & 0xFFFFFFFFL) + (paramArrayOfint1[7] & 0xFFFFFFFFL);
/*  796 */     paramArrayOfint2[7] = (int)l1;
/*  797 */     l1 >>>= 32L;
/*  798 */     return (int)l1;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulWordAddTo(int paramInt1, int[] paramArrayOfint1, int paramInt2, int[] paramArrayOfint2, int paramInt3) {
/*  803 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/*  804 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 0] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 0] & 0xFFFFFFFFL);
/*  805 */     paramArrayOfint2[paramInt3 + 0] = (int)l1;
/*  806 */     l1 >>>= 32L;
/*  807 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 1] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 1] & 0xFFFFFFFFL);
/*  808 */     paramArrayOfint2[paramInt3 + 1] = (int)l1;
/*  809 */     l1 >>>= 32L;
/*  810 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 2] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 2] & 0xFFFFFFFFL);
/*  811 */     paramArrayOfint2[paramInt3 + 2] = (int)l1;
/*  812 */     l1 >>>= 32L;
/*  813 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 3] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 3] & 0xFFFFFFFFL);
/*  814 */     paramArrayOfint2[paramInt3 + 3] = (int)l1;
/*  815 */     l1 >>>= 32L;
/*  816 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 4] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 4] & 0xFFFFFFFFL);
/*  817 */     paramArrayOfint2[paramInt3 + 4] = (int)l1;
/*  818 */     l1 >>>= 32L;
/*  819 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 5] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 5] & 0xFFFFFFFFL);
/*  820 */     paramArrayOfint2[paramInt3 + 5] = (int)l1;
/*  821 */     l1 >>>= 32L;
/*  822 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 6] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 6] & 0xFFFFFFFFL);
/*  823 */     paramArrayOfint2[paramInt3 + 6] = (int)l1;
/*  824 */     l1 >>>= 32L;
/*  825 */     l1 += l2 * (paramArrayOfint1[paramInt2 + 7] & 0xFFFFFFFFL) + (paramArrayOfint2[paramInt3 + 7] & 0xFFFFFFFFL);
/*  826 */     paramArrayOfint2[paramInt3 + 7] = (int)l1;
/*  827 */     l1 >>>= 32L;
/*  828 */     return (int)l1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int mul33DWordAdd(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2) {
/*  836 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/*  837 */     long l3 = paramLong & 0xFFFFFFFFL;
/*  838 */     l1 += l2 * l3 + (paramArrayOfint[paramInt2 + 0] & 0xFFFFFFFFL);
/*  839 */     paramArrayOfint[paramInt2 + 0] = (int)l1;
/*  840 */     l1 >>>= 32L;
/*  841 */     long l4 = paramLong >>> 32L;
/*  842 */     l1 += l2 * l4 + l3 + (paramArrayOfint[paramInt2 + 1] & 0xFFFFFFFFL);
/*  843 */     paramArrayOfint[paramInt2 + 1] = (int)l1;
/*  844 */     l1 >>>= 32L;
/*  845 */     l1 += l4 + (paramArrayOfint[paramInt2 + 2] & 0xFFFFFFFFL);
/*  846 */     paramArrayOfint[paramInt2 + 2] = (int)l1;
/*  847 */     l1 >>>= 32L;
/*  848 */     l1 += paramArrayOfint[paramInt2 + 3] & 0xFFFFFFFFL;
/*  849 */     paramArrayOfint[paramInt2 + 3] = (int)l1;
/*  850 */     l1 >>>= 32L;
/*  851 */     return (l1 == 0L) ? 0 : Nat.incAt(8, paramArrayOfint, paramInt2, 4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int mul33WordAdd(int paramInt1, int paramInt2, int[] paramArrayOfint, int paramInt3) {
/*  859 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL, l3 = paramInt2 & 0xFFFFFFFFL;
/*  860 */     l1 += l3 * l2 + (paramArrayOfint[paramInt3 + 0] & 0xFFFFFFFFL);
/*  861 */     paramArrayOfint[paramInt3 + 0] = (int)l1;
/*  862 */     l1 >>>= 32L;
/*  863 */     l1 += l3 + (paramArrayOfint[paramInt3 + 1] & 0xFFFFFFFFL);
/*  864 */     paramArrayOfint[paramInt3 + 1] = (int)l1;
/*  865 */     l1 >>>= 32L;
/*  866 */     l1 += paramArrayOfint[paramInt3 + 2] & 0xFFFFFFFFL;
/*  867 */     paramArrayOfint[paramInt3 + 2] = (int)l1;
/*  868 */     l1 >>>= 32L;
/*  869 */     return (l1 == 0L) ? 0 : Nat.incAt(8, paramArrayOfint, paramInt3, 3);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int mulWordDwordAdd(int paramInt1, long paramLong, int[] paramArrayOfint, int paramInt2) {
/*  875 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/*  876 */     l1 += l2 * (paramLong & 0xFFFFFFFFL) + (paramArrayOfint[paramInt2 + 0] & 0xFFFFFFFFL);
/*  877 */     paramArrayOfint[paramInt2 + 0] = (int)l1;
/*  878 */     l1 >>>= 32L;
/*  879 */     l1 += l2 * (paramLong >>> 32L) + (paramArrayOfint[paramInt2 + 1] & 0xFFFFFFFFL);
/*  880 */     paramArrayOfint[paramInt2 + 1] = (int)l1;
/*  881 */     l1 >>>= 32L;
/*  882 */     l1 += paramArrayOfint[paramInt2 + 2] & 0xFFFFFFFFL;
/*  883 */     paramArrayOfint[paramInt2 + 2] = (int)l1;
/*  884 */     l1 >>>= 32L;
/*  885 */     return (l1 == 0L) ? 0 : Nat.incAt(8, paramArrayOfint, paramInt2, 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int mulWord(int paramInt1, int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt2) {
/*  890 */     long l1 = 0L, l2 = paramInt1 & 0xFFFFFFFFL;
/*  891 */     byte b = 0;
/*      */     
/*      */     while (true) {
/*  894 */       l1 += l2 * (paramArrayOfint1[b] & 0xFFFFFFFFL);
/*  895 */       paramArrayOfint2[paramInt2 + b] = (int)l1;
/*  896 */       l1 >>>= 32L;
/*      */       
/*  898 */       if (++b >= 8)
/*  899 */         return (int)l1; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  904 */     long l1 = paramArrayOfint1[0] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  907 */     int i = 0;
/*      */     
/*  909 */     byte b1 = 7, b2 = 16;
/*      */     
/*      */     do {
/*  912 */       long l23 = paramArrayOfint1[b1--] & 0xFFFFFFFFL;
/*  913 */       long l24 = l23 * l23;
/*  914 */       paramArrayOfint2[--b2] = i << 31 | (int)(l24 >>> 33L);
/*  915 */       paramArrayOfint2[--b2] = (int)(l24 >>> 1L);
/*  916 */       i = (int)l24;
/*      */     }
/*  918 */     while (b1 > 0);
/*      */ 
/*      */     
/*  921 */     long l4 = l1 * l1;
/*  922 */     long l2 = (i << 31) & 0xFFFFFFFFL | l4 >>> 33L;
/*  923 */     paramArrayOfint2[0] = (int)l4;
/*  924 */     i = (int)(l4 >>> 32L) & 0x1;
/*      */ 
/*      */ 
/*      */     
/*  928 */     long l3 = paramArrayOfint1[1] & 0xFFFFFFFFL;
/*  929 */     l4 = paramArrayOfint2[2] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  932 */     l2 += l3 * l1;
/*  933 */     int j = (int)l2;
/*  934 */     paramArrayOfint2[1] = j << 1 | i;
/*  935 */     i = j >>> 31;
/*  936 */     l4 += l2 >>> 32L;
/*      */ 
/*      */     
/*  939 */     long l5 = paramArrayOfint1[2] & 0xFFFFFFFFL;
/*  940 */     long l6 = paramArrayOfint2[3] & 0xFFFFFFFFL;
/*  941 */     long l7 = paramArrayOfint2[4] & 0xFFFFFFFFL;
/*      */     
/*  943 */     l4 += l5 * l1;
/*  944 */     j = (int)l4;
/*  945 */     paramArrayOfint2[2] = j << 1 | i;
/*  946 */     i = j >>> 31;
/*  947 */     l6 += (l4 >>> 32L) + l5 * l3;
/*  948 */     l7 += l6 >>> 32L;
/*  949 */     l6 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  952 */     long l8 = paramArrayOfint1[3] & 0xFFFFFFFFL;
/*  953 */     long l9 = (paramArrayOfint2[5] & 0xFFFFFFFFL) + (l7 >>> 32L); l7 &= 0xFFFFFFFFL;
/*  954 */     long l10 = (paramArrayOfint2[6] & 0xFFFFFFFFL) + (l9 >>> 32L); l9 &= 0xFFFFFFFFL;
/*      */     
/*  956 */     l6 += l8 * l1;
/*  957 */     j = (int)l6;
/*  958 */     paramArrayOfint2[3] = j << 1 | i;
/*  959 */     i = j >>> 31;
/*  960 */     l7 += (l6 >>> 32L) + l8 * l3;
/*  961 */     l9 += (l7 >>> 32L) + l8 * l5;
/*  962 */     l7 &= 0xFFFFFFFFL;
/*  963 */     l10 += l9 >>> 32L;
/*  964 */     l9 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  967 */     long l11 = paramArrayOfint1[4] & 0xFFFFFFFFL;
/*  968 */     long l12 = (paramArrayOfint2[7] & 0xFFFFFFFFL) + (l10 >>> 32L); l10 &= 0xFFFFFFFFL;
/*  969 */     long l13 = (paramArrayOfint2[8] & 0xFFFFFFFFL) + (l12 >>> 32L); l12 &= 0xFFFFFFFFL;
/*      */     
/*  971 */     l7 += l11 * l1;
/*  972 */     j = (int)l7;
/*  973 */     paramArrayOfint2[4] = j << 1 | i;
/*  974 */     i = j >>> 31;
/*  975 */     l9 += (l7 >>> 32L) + l11 * l3;
/*  976 */     l10 += (l9 >>> 32L) + l11 * l5;
/*  977 */     l9 &= 0xFFFFFFFFL;
/*  978 */     l12 += (l10 >>> 32L) + l11 * l8;
/*  979 */     l10 &= 0xFFFFFFFFL;
/*  980 */     l13 += l12 >>> 32L;
/*  981 */     l12 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/*  984 */     long l14 = paramArrayOfint1[5] & 0xFFFFFFFFL;
/*  985 */     long l15 = (paramArrayOfint2[9] & 0xFFFFFFFFL) + (l13 >>> 32L); l13 &= 0xFFFFFFFFL;
/*  986 */     long l16 = (paramArrayOfint2[10] & 0xFFFFFFFFL) + (l15 >>> 32L); l15 &= 0xFFFFFFFFL;
/*      */     
/*  988 */     l9 += l14 * l1;
/*  989 */     j = (int)l9;
/*  990 */     paramArrayOfint2[5] = j << 1 | i;
/*  991 */     i = j >>> 31;
/*  992 */     l10 += (l9 >>> 32L) + l14 * l3;
/*  993 */     l12 += (l10 >>> 32L) + l14 * l5;
/*  994 */     l10 &= 0xFFFFFFFFL;
/*  995 */     l13 += (l12 >>> 32L) + l14 * l8;
/*  996 */     l12 &= 0xFFFFFFFFL;
/*  997 */     l15 += (l13 >>> 32L) + l14 * l11;
/*  998 */     l13 &= 0xFFFFFFFFL;
/*  999 */     l16 += l15 >>> 32L;
/* 1000 */     l15 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/* 1003 */     long l17 = paramArrayOfint1[6] & 0xFFFFFFFFL;
/* 1004 */     long l18 = (paramArrayOfint2[11] & 0xFFFFFFFFL) + (l16 >>> 32L); l16 &= 0xFFFFFFFFL;
/* 1005 */     long l19 = (paramArrayOfint2[12] & 0xFFFFFFFFL) + (l18 >>> 32L); l18 &= 0xFFFFFFFFL;
/*      */     
/* 1007 */     l10 += l17 * l1;
/* 1008 */     j = (int)l10;
/* 1009 */     paramArrayOfint2[6] = j << 1 | i;
/* 1010 */     i = j >>> 31;
/* 1011 */     l12 += (l10 >>> 32L) + l17 * l3;
/* 1012 */     l13 += (l12 >>> 32L) + l17 * l5;
/* 1013 */     l12 &= 0xFFFFFFFFL;
/* 1014 */     l15 += (l13 >>> 32L) + l17 * l8;
/* 1015 */     l13 &= 0xFFFFFFFFL;
/* 1016 */     l16 += (l15 >>> 32L) + l17 * l11;
/* 1017 */     l15 &= 0xFFFFFFFFL;
/* 1018 */     l18 += (l16 >>> 32L) + l17 * l14;
/* 1019 */     l16 &= 0xFFFFFFFFL;
/* 1020 */     l19 += l18 >>> 32L;
/* 1021 */     l18 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/* 1024 */     long l20 = paramArrayOfint1[7] & 0xFFFFFFFFL;
/* 1025 */     long l21 = (paramArrayOfint2[13] & 0xFFFFFFFFL) + (l19 >>> 32L); l19 &= 0xFFFFFFFFL;
/* 1026 */     long l22 = (paramArrayOfint2[14] & 0xFFFFFFFFL) + (l21 >>> 32L); l21 &= 0xFFFFFFFFL;
/*      */     
/* 1028 */     l12 += l20 * l1;
/* 1029 */     j = (int)l12;
/* 1030 */     paramArrayOfint2[7] = j << 1 | i;
/* 1031 */     i = j >>> 31;
/* 1032 */     l13 += (l12 >>> 32L) + l20 * l3;
/* 1033 */     l15 += (l13 >>> 32L) + l20 * l5;
/* 1034 */     l16 += (l15 >>> 32L) + l20 * l8;
/* 1035 */     l18 += (l16 >>> 32L) + l20 * l11;
/* 1036 */     l19 += (l18 >>> 32L) + l20 * l14;
/* 1037 */     l21 += (l19 >>> 32L) + l20 * l17;
/* 1038 */     l22 += l21 >>> 32L;
/*      */ 
/*      */     
/* 1041 */     j = (int)l13;
/* 1042 */     paramArrayOfint2[8] = j << 1 | i;
/* 1043 */     i = j >>> 31;
/* 1044 */     j = (int)l15;
/* 1045 */     paramArrayOfint2[9] = j << 1 | i;
/* 1046 */     i = j >>> 31;
/* 1047 */     j = (int)l16;
/* 1048 */     paramArrayOfint2[10] = j << 1 | i;
/* 1049 */     i = j >>> 31;
/* 1050 */     j = (int)l18;
/* 1051 */     paramArrayOfint2[11] = j << 1 | i;
/* 1052 */     i = j >>> 31;
/* 1053 */     j = (int)l19;
/* 1054 */     paramArrayOfint2[12] = j << 1 | i;
/* 1055 */     i = j >>> 31;
/* 1056 */     j = (int)l21;
/* 1057 */     paramArrayOfint2[13] = j << 1 | i;
/* 1058 */     i = j >>> 31;
/* 1059 */     j = (int)l22;
/* 1060 */     paramArrayOfint2[14] = j << 1 | i;
/* 1061 */     i = j >>> 31;
/* 1062 */     j = paramArrayOfint2[15] + (int)(l22 >>> 32L);
/* 1063 */     paramArrayOfint2[15] = j << 1 | i;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void square(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/* 1068 */     long l1 = paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/* 1071 */     int i = 0;
/*      */     
/* 1073 */     byte b1 = 7, b2 = 16;
/*      */     
/*      */     do {
/* 1076 */       long l23 = paramArrayOfint1[paramInt1 + b1--] & 0xFFFFFFFFL;
/* 1077 */       long l24 = l23 * l23;
/* 1078 */       paramArrayOfint2[paramInt2 + --b2] = i << 31 | (int)(l24 >>> 33L);
/* 1079 */       paramArrayOfint2[paramInt2 + --b2] = (int)(l24 >>> 1L);
/* 1080 */       i = (int)l24;
/*      */     }
/* 1082 */     while (b1 > 0);
/*      */ 
/*      */     
/* 1085 */     long l4 = l1 * l1;
/* 1086 */     long l2 = (i << 31) & 0xFFFFFFFFL | l4 >>> 33L;
/* 1087 */     paramArrayOfint2[paramInt2 + 0] = (int)l4;
/* 1088 */     i = (int)(l4 >>> 32L) & 0x1;
/*      */ 
/*      */ 
/*      */     
/* 1092 */     long l3 = paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL;
/* 1093 */     l4 = paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL;
/*      */ 
/*      */     
/* 1096 */     l2 += l3 * l1;
/* 1097 */     int j = (int)l2;
/* 1098 */     paramArrayOfint2[paramInt2 + 1] = j << 1 | i;
/* 1099 */     i = j >>> 31;
/* 1100 */     l4 += l2 >>> 32L;
/*      */ 
/*      */     
/* 1103 */     long l5 = paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL;
/* 1104 */     long l6 = paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL;
/* 1105 */     long l7 = paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL;
/*      */     
/* 1107 */     l4 += l5 * l1;
/* 1108 */     j = (int)l4;
/* 1109 */     paramArrayOfint2[paramInt2 + 2] = j << 1 | i;
/* 1110 */     i = j >>> 31;
/* 1111 */     l6 += (l4 >>> 32L) + l5 * l3;
/* 1112 */     l7 += l6 >>> 32L;
/* 1113 */     l6 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/* 1116 */     long l8 = paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL;
/* 1117 */     long l9 = (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL) + (l7 >>> 32L); l7 &= 0xFFFFFFFFL;
/* 1118 */     long l10 = (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL) + (l9 >>> 32L); l9 &= 0xFFFFFFFFL;
/*      */     
/* 1120 */     l6 += l8 * l1;
/* 1121 */     j = (int)l6;
/* 1122 */     paramArrayOfint2[paramInt2 + 3] = j << 1 | i;
/* 1123 */     i = j >>> 31;
/* 1124 */     l7 += (l6 >>> 32L) + l8 * l3;
/* 1125 */     l9 += (l7 >>> 32L) + l8 * l5;
/* 1126 */     l7 &= 0xFFFFFFFFL;
/* 1127 */     l10 += l9 >>> 32L;
/* 1128 */     l9 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/* 1131 */     long l11 = paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL;
/* 1132 */     long l12 = (paramArrayOfint2[paramInt2 + 7] & 0xFFFFFFFFL) + (l10 >>> 32L); l10 &= 0xFFFFFFFFL;
/* 1133 */     long l13 = (paramArrayOfint2[paramInt2 + 8] & 0xFFFFFFFFL) + (l12 >>> 32L); l12 &= 0xFFFFFFFFL;
/*      */     
/* 1135 */     l7 += l11 * l1;
/* 1136 */     j = (int)l7;
/* 1137 */     paramArrayOfint2[paramInt2 + 4] = j << 1 | i;
/* 1138 */     i = j >>> 31;
/* 1139 */     l9 += (l7 >>> 32L) + l11 * l3;
/* 1140 */     l10 += (l9 >>> 32L) + l11 * l5;
/* 1141 */     l9 &= 0xFFFFFFFFL;
/* 1142 */     l12 += (l10 >>> 32L) + l11 * l8;
/* 1143 */     l10 &= 0xFFFFFFFFL;
/* 1144 */     l13 += l12 >>> 32L;
/* 1145 */     l12 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/* 1148 */     long l14 = paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL;
/* 1149 */     long l15 = (paramArrayOfint2[paramInt2 + 9] & 0xFFFFFFFFL) + (l13 >>> 32L); l13 &= 0xFFFFFFFFL;
/* 1150 */     long l16 = (paramArrayOfint2[paramInt2 + 10] & 0xFFFFFFFFL) + (l15 >>> 32L); l15 &= 0xFFFFFFFFL;
/*      */     
/* 1152 */     l9 += l14 * l1;
/* 1153 */     j = (int)l9;
/* 1154 */     paramArrayOfint2[paramInt2 + 5] = j << 1 | i;
/* 1155 */     i = j >>> 31;
/* 1156 */     l10 += (l9 >>> 32L) + l14 * l3;
/* 1157 */     l12 += (l10 >>> 32L) + l14 * l5;
/* 1158 */     l10 &= 0xFFFFFFFFL;
/* 1159 */     l13 += (l12 >>> 32L) + l14 * l8;
/* 1160 */     l12 &= 0xFFFFFFFFL;
/* 1161 */     l15 += (l13 >>> 32L) + l14 * l11;
/* 1162 */     l13 &= 0xFFFFFFFFL;
/* 1163 */     l16 += l15 >>> 32L;
/* 1164 */     l15 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/* 1167 */     long l17 = paramArrayOfint1[paramInt1 + 6] & 0xFFFFFFFFL;
/* 1168 */     long l18 = (paramArrayOfint2[paramInt2 + 11] & 0xFFFFFFFFL) + (l16 >>> 32L); l16 &= 0xFFFFFFFFL;
/* 1169 */     long l19 = (paramArrayOfint2[paramInt2 + 12] & 0xFFFFFFFFL) + (l18 >>> 32L); l18 &= 0xFFFFFFFFL;
/*      */     
/* 1171 */     l10 += l17 * l1;
/* 1172 */     j = (int)l10;
/* 1173 */     paramArrayOfint2[paramInt2 + 6] = j << 1 | i;
/* 1174 */     i = j >>> 31;
/* 1175 */     l12 += (l10 >>> 32L) + l17 * l3;
/* 1176 */     l13 += (l12 >>> 32L) + l17 * l5;
/* 1177 */     l12 &= 0xFFFFFFFFL;
/* 1178 */     l15 += (l13 >>> 32L) + l17 * l8;
/* 1179 */     l13 &= 0xFFFFFFFFL;
/* 1180 */     l16 += (l15 >>> 32L) + l17 * l11;
/* 1181 */     l15 &= 0xFFFFFFFFL;
/* 1182 */     l18 += (l16 >>> 32L) + l17 * l14;
/* 1183 */     l16 &= 0xFFFFFFFFL;
/* 1184 */     l19 += l18 >>> 32L;
/* 1185 */     l18 &= 0xFFFFFFFFL;
/*      */ 
/*      */     
/* 1188 */     long l20 = paramArrayOfint1[paramInt1 + 7] & 0xFFFFFFFFL;
/* 1189 */     long l21 = (paramArrayOfint2[paramInt2 + 13] & 0xFFFFFFFFL) + (l19 >>> 32L); l19 &= 0xFFFFFFFFL;
/* 1190 */     long l22 = (paramArrayOfint2[paramInt2 + 14] & 0xFFFFFFFFL) + (l21 >>> 32L); l21 &= 0xFFFFFFFFL;
/*      */     
/* 1192 */     l12 += l20 * l1;
/* 1193 */     j = (int)l12;
/* 1194 */     paramArrayOfint2[paramInt2 + 7] = j << 1 | i;
/* 1195 */     i = j >>> 31;
/* 1196 */     l13 += (l12 >>> 32L) + l20 * l3;
/* 1197 */     l15 += (l13 >>> 32L) + l20 * l5;
/* 1198 */     l16 += (l15 >>> 32L) + l20 * l8;
/* 1199 */     l18 += (l16 >>> 32L) + l20 * l11;
/* 1200 */     l19 += (l18 >>> 32L) + l20 * l14;
/* 1201 */     l21 += (l19 >>> 32L) + l20 * l17;
/* 1202 */     l22 += l21 >>> 32L;
/*      */ 
/*      */     
/* 1205 */     j = (int)l13;
/* 1206 */     paramArrayOfint2[paramInt2 + 8] = j << 1 | i;
/* 1207 */     i = j >>> 31;
/* 1208 */     j = (int)l15;
/* 1209 */     paramArrayOfint2[paramInt2 + 9] = j << 1 | i;
/* 1210 */     i = j >>> 31;
/* 1211 */     j = (int)l16;
/* 1212 */     paramArrayOfint2[paramInt2 + 10] = j << 1 | i;
/* 1213 */     i = j >>> 31;
/* 1214 */     j = (int)l18;
/* 1215 */     paramArrayOfint2[paramInt2 + 11] = j << 1 | i;
/* 1216 */     i = j >>> 31;
/* 1217 */     j = (int)l19;
/* 1218 */     paramArrayOfint2[paramInt2 + 12] = j << 1 | i;
/* 1219 */     i = j >>> 31;
/* 1220 */     j = (int)l21;
/* 1221 */     paramArrayOfint2[paramInt2 + 13] = j << 1 | i;
/* 1222 */     i = j >>> 31;
/* 1223 */     j = (int)l22;
/* 1224 */     paramArrayOfint2[paramInt2 + 14] = j << 1 | i;
/* 1225 */     i = j >>> 31;
/* 1226 */     j = paramArrayOfint2[paramInt2 + 15] + (int)(l22 >>> 32L);
/* 1227 */     paramArrayOfint2[paramInt2 + 15] = j << 1 | i;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int sub(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 1232 */     long l = 0L;
/* 1233 */     l += (paramArrayOfint1[0] & 0xFFFFFFFFL) - (paramArrayOfint2[0] & 0xFFFFFFFFL);
/* 1234 */     paramArrayOfint3[0] = (int)l;
/* 1235 */     l >>= 32L;
/* 1236 */     l += (paramArrayOfint1[1] & 0xFFFFFFFFL) - (paramArrayOfint2[1] & 0xFFFFFFFFL);
/* 1237 */     paramArrayOfint3[1] = (int)l;
/* 1238 */     l >>= 32L;
/* 1239 */     l += (paramArrayOfint1[2] & 0xFFFFFFFFL) - (paramArrayOfint2[2] & 0xFFFFFFFFL);
/* 1240 */     paramArrayOfint3[2] = (int)l;
/* 1241 */     l >>= 32L;
/* 1242 */     l += (paramArrayOfint1[3] & 0xFFFFFFFFL) - (paramArrayOfint2[3] & 0xFFFFFFFFL);
/* 1243 */     paramArrayOfint3[3] = (int)l;
/* 1244 */     l >>= 32L;
/* 1245 */     l += (paramArrayOfint1[4] & 0xFFFFFFFFL) - (paramArrayOfint2[4] & 0xFFFFFFFFL);
/* 1246 */     paramArrayOfint3[4] = (int)l;
/* 1247 */     l >>= 32L;
/* 1248 */     l += (paramArrayOfint1[5] & 0xFFFFFFFFL) - (paramArrayOfint2[5] & 0xFFFFFFFFL);
/* 1249 */     paramArrayOfint3[5] = (int)l;
/* 1250 */     l >>= 32L;
/* 1251 */     l += (paramArrayOfint1[6] & 0xFFFFFFFFL) - (paramArrayOfint2[6] & 0xFFFFFFFFL);
/* 1252 */     paramArrayOfint3[6] = (int)l;
/* 1253 */     l >>= 32L;
/* 1254 */     l += (paramArrayOfint1[7] & 0xFFFFFFFFL) - (paramArrayOfint2[7] & 0xFFFFFFFFL);
/* 1255 */     paramArrayOfint3[7] = (int)l;
/* 1256 */     l >>= 32L;
/* 1257 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int sub(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2, int[] paramArrayOfint3, int paramInt3) {
/* 1262 */     long l = 0L;
/* 1263 */     l += (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL);
/* 1264 */     paramArrayOfint3[paramInt3 + 0] = (int)l;
/* 1265 */     l >>= 32L;
/* 1266 */     l += (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL);
/* 1267 */     paramArrayOfint3[paramInt3 + 1] = (int)l;
/* 1268 */     l >>= 32L;
/* 1269 */     l += (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL);
/* 1270 */     paramArrayOfint3[paramInt3 + 2] = (int)l;
/* 1271 */     l >>= 32L;
/* 1272 */     l += (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL);
/* 1273 */     paramArrayOfint3[paramInt3 + 3] = (int)l;
/* 1274 */     l >>= 32L;
/* 1275 */     l += (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL);
/* 1276 */     paramArrayOfint3[paramInt3 + 4] = (int)l;
/* 1277 */     l >>= 32L;
/* 1278 */     l += (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL);
/* 1279 */     paramArrayOfint3[paramInt3 + 5] = (int)l;
/* 1280 */     l >>= 32L;
/* 1281 */     l += (paramArrayOfint1[paramInt1 + 6] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL);
/* 1282 */     paramArrayOfint3[paramInt3 + 6] = (int)l;
/* 1283 */     l >>= 32L;
/* 1284 */     l += (paramArrayOfint1[paramInt1 + 7] & 0xFFFFFFFFL) - (paramArrayOfint2[paramInt2 + 7] & 0xFFFFFFFFL);
/* 1285 */     paramArrayOfint3[paramInt3 + 7] = (int)l;
/* 1286 */     l >>= 32L;
/* 1287 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subBothFrom(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 1292 */     long l = 0L;
/* 1293 */     l += (paramArrayOfint3[0] & 0xFFFFFFFFL) - (paramArrayOfint1[0] & 0xFFFFFFFFL) - (paramArrayOfint2[0] & 0xFFFFFFFFL);
/* 1294 */     paramArrayOfint3[0] = (int)l;
/* 1295 */     l >>= 32L;
/* 1296 */     l += (paramArrayOfint3[1] & 0xFFFFFFFFL) - (paramArrayOfint1[1] & 0xFFFFFFFFL) - (paramArrayOfint2[1] & 0xFFFFFFFFL);
/* 1297 */     paramArrayOfint3[1] = (int)l;
/* 1298 */     l >>= 32L;
/* 1299 */     l += (paramArrayOfint3[2] & 0xFFFFFFFFL) - (paramArrayOfint1[2] & 0xFFFFFFFFL) - (paramArrayOfint2[2] & 0xFFFFFFFFL);
/* 1300 */     paramArrayOfint3[2] = (int)l;
/* 1301 */     l >>= 32L;
/* 1302 */     l += (paramArrayOfint3[3] & 0xFFFFFFFFL) - (paramArrayOfint1[3] & 0xFFFFFFFFL) - (paramArrayOfint2[3] & 0xFFFFFFFFL);
/* 1303 */     paramArrayOfint3[3] = (int)l;
/* 1304 */     l >>= 32L;
/* 1305 */     l += (paramArrayOfint3[4] & 0xFFFFFFFFL) - (paramArrayOfint1[4] & 0xFFFFFFFFL) - (paramArrayOfint2[4] & 0xFFFFFFFFL);
/* 1306 */     paramArrayOfint3[4] = (int)l;
/* 1307 */     l >>= 32L;
/* 1308 */     l += (paramArrayOfint3[5] & 0xFFFFFFFFL) - (paramArrayOfint1[5] & 0xFFFFFFFFL) - (paramArrayOfint2[5] & 0xFFFFFFFFL);
/* 1309 */     paramArrayOfint3[5] = (int)l;
/* 1310 */     l >>= 32L;
/* 1311 */     l += (paramArrayOfint3[6] & 0xFFFFFFFFL) - (paramArrayOfint1[6] & 0xFFFFFFFFL) - (paramArrayOfint2[6] & 0xFFFFFFFFL);
/* 1312 */     paramArrayOfint3[6] = (int)l;
/* 1313 */     l >>= 32L;
/* 1314 */     l += (paramArrayOfint3[7] & 0xFFFFFFFFL) - (paramArrayOfint1[7] & 0xFFFFFFFFL) - (paramArrayOfint2[7] & 0xFFFFFFFFL);
/* 1315 */     paramArrayOfint3[7] = (int)l;
/* 1316 */     l >>= 32L;
/* 1317 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subFrom(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 1322 */     long l = 0L;
/* 1323 */     l += (paramArrayOfint2[0] & 0xFFFFFFFFL) - (paramArrayOfint1[0] & 0xFFFFFFFFL);
/* 1324 */     paramArrayOfint2[0] = (int)l;
/* 1325 */     l >>= 32L;
/* 1326 */     l += (paramArrayOfint2[1] & 0xFFFFFFFFL) - (paramArrayOfint1[1] & 0xFFFFFFFFL);
/* 1327 */     paramArrayOfint2[1] = (int)l;
/* 1328 */     l >>= 32L;
/* 1329 */     l += (paramArrayOfint2[2] & 0xFFFFFFFFL) - (paramArrayOfint1[2] & 0xFFFFFFFFL);
/* 1330 */     paramArrayOfint2[2] = (int)l;
/* 1331 */     l >>= 32L;
/* 1332 */     l += (paramArrayOfint2[3] & 0xFFFFFFFFL) - (paramArrayOfint1[3] & 0xFFFFFFFFL);
/* 1333 */     paramArrayOfint2[3] = (int)l;
/* 1334 */     l >>= 32L;
/* 1335 */     l += (paramArrayOfint2[4] & 0xFFFFFFFFL) - (paramArrayOfint1[4] & 0xFFFFFFFFL);
/* 1336 */     paramArrayOfint2[4] = (int)l;
/* 1337 */     l >>= 32L;
/* 1338 */     l += (paramArrayOfint2[5] & 0xFFFFFFFFL) - (paramArrayOfint1[5] & 0xFFFFFFFFL);
/* 1339 */     paramArrayOfint2[5] = (int)l;
/* 1340 */     l >>= 32L;
/* 1341 */     l += (paramArrayOfint2[6] & 0xFFFFFFFFL) - (paramArrayOfint1[6] & 0xFFFFFFFFL);
/* 1342 */     paramArrayOfint2[6] = (int)l;
/* 1343 */     l >>= 32L;
/* 1344 */     l += (paramArrayOfint2[7] & 0xFFFFFFFFL) - (paramArrayOfint1[7] & 0xFFFFFFFFL);
/* 1345 */     paramArrayOfint2[7] = (int)l;
/* 1346 */     l >>= 32L;
/* 1347 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int subFrom(int[] paramArrayOfint1, int paramInt1, int[] paramArrayOfint2, int paramInt2) {
/* 1352 */     long l = 0L;
/* 1353 */     l += (paramArrayOfint2[paramInt2 + 0] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 0] & 0xFFFFFFFFL);
/* 1354 */     paramArrayOfint2[paramInt2 + 0] = (int)l;
/* 1355 */     l >>= 32L;
/* 1356 */     l += (paramArrayOfint2[paramInt2 + 1] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 1] & 0xFFFFFFFFL);
/* 1357 */     paramArrayOfint2[paramInt2 + 1] = (int)l;
/* 1358 */     l >>= 32L;
/* 1359 */     l += (paramArrayOfint2[paramInt2 + 2] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 2] & 0xFFFFFFFFL);
/* 1360 */     paramArrayOfint2[paramInt2 + 2] = (int)l;
/* 1361 */     l >>= 32L;
/* 1362 */     l += (paramArrayOfint2[paramInt2 + 3] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 3] & 0xFFFFFFFFL);
/* 1363 */     paramArrayOfint2[paramInt2 + 3] = (int)l;
/* 1364 */     l >>= 32L;
/* 1365 */     l += (paramArrayOfint2[paramInt2 + 4] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 4] & 0xFFFFFFFFL);
/* 1366 */     paramArrayOfint2[paramInt2 + 4] = (int)l;
/* 1367 */     l >>= 32L;
/* 1368 */     l += (paramArrayOfint2[paramInt2 + 5] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 5] & 0xFFFFFFFFL);
/* 1369 */     paramArrayOfint2[paramInt2 + 5] = (int)l;
/* 1370 */     l >>= 32L;
/* 1371 */     l += (paramArrayOfint2[paramInt2 + 6] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 6] & 0xFFFFFFFFL);
/* 1372 */     paramArrayOfint2[paramInt2 + 6] = (int)l;
/* 1373 */     l >>= 32L;
/* 1374 */     l += (paramArrayOfint2[paramInt2 + 7] & 0xFFFFFFFFL) - (paramArrayOfint1[paramInt1 + 7] & 0xFFFFFFFFL);
/* 1375 */     paramArrayOfint2[paramInt2 + 7] = (int)l;
/* 1376 */     l >>= 32L;
/* 1377 */     return (int)l;
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigInteger toBigInteger(int[] paramArrayOfint) {
/* 1382 */     byte[] arrayOfByte = new byte[32];
/* 1383 */     for (byte b = 0; b < 8; b++) {
/*      */       
/* 1385 */       int i = paramArrayOfint[b];
/* 1386 */       if (i != 0)
/*      */       {
/* 1388 */         Pack.intToBigEndian(i, arrayOfByte, 7 - b << 2);
/*      */       }
/*      */     } 
/* 1391 */     return new BigInteger(1, arrayOfByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigInteger toBigInteger64(long[] paramArrayOflong) {
/* 1396 */     byte[] arrayOfByte = new byte[32];
/* 1397 */     for (byte b = 0; b < 4; b++) {
/*      */       
/* 1399 */       long l = paramArrayOflong[b];
/* 1400 */       if (l != 0L)
/*      */       {
/* 1402 */         Pack.longToBigEndian(l, arrayOfByte, 3 - b << 3);
/*      */       }
/*      */     } 
/* 1405 */     return new BigInteger(1, arrayOfByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void zero(int[] paramArrayOfint) {
/* 1410 */     paramArrayOfint[0] = 0;
/* 1411 */     paramArrayOfint[1] = 0;
/* 1412 */     paramArrayOfint[2] = 0;
/* 1413 */     paramArrayOfint[3] = 0;
/* 1414 */     paramArrayOfint[4] = 0;
/* 1415 */     paramArrayOfint[5] = 0;
/* 1416 */     paramArrayOfint[6] = 0;
/* 1417 */     paramArrayOfint[7] = 0;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/raw/Nat256.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */