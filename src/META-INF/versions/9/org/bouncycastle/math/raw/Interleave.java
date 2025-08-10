/*     */ package META-INF.versions.9.org.bouncycastle.math.raw;
/*     */ 
/*     */ import org.bouncycastle.math.raw.Bits;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Interleave
/*     */ {
/*     */   private static final long M32 = 1431655765L;
/*     */   private static final long M64 = 6148914691236517205L;
/*     */   private static final long M64R = -6148914691236517206L;
/*     */   
/*     */   public static int expand8to16(int paramInt) {
/*  53 */     paramInt &= 0xFF;
/*  54 */     paramInt = (paramInt | paramInt << 4) & 0xF0F;
/*  55 */     paramInt = (paramInt | paramInt << 2) & 0x3333;
/*  56 */     paramInt = (paramInt | paramInt << 1) & 0x5555;
/*  57 */     return paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int expand16to32(int paramInt) {
/*  62 */     paramInt &= 0xFFFF;
/*  63 */     paramInt = (paramInt | paramInt << 8) & 0xFF00FF;
/*  64 */     paramInt = (paramInt | paramInt << 4) & 0xF0F0F0F;
/*  65 */     paramInt = (paramInt | paramInt << 2) & 0x33333333;
/*  66 */     paramInt = (paramInt | paramInt << 1) & 0x55555555;
/*  67 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static long expand32to64(int paramInt) {
/*  73 */     paramInt = Bits.bitPermuteStep(paramInt, 65280, 8);
/*  74 */     paramInt = Bits.bitPermuteStep(paramInt, 15728880, 4);
/*  75 */     paramInt = Bits.bitPermuteStep(paramInt, 202116108, 2);
/*  76 */     paramInt = Bits.bitPermuteStep(paramInt, 572662306, 1);
/*     */     
/*  78 */     return ((paramInt >>> 1) & 0x55555555L) << 32L | paramInt & 0x55555555L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void expand64To128(long paramLong, long[] paramArrayOflong, int paramInt) {
/*  84 */     paramLong = Bits.bitPermuteStep(paramLong, 4294901760L, 16);
/*  85 */     paramLong = Bits.bitPermuteStep(paramLong, 280375465148160L, 8);
/*  86 */     paramLong = Bits.bitPermuteStep(paramLong, 67555025218437360L, 4);
/*  87 */     paramLong = Bits.bitPermuteStep(paramLong, 868082074056920076L, 2);
/*  88 */     paramLong = Bits.bitPermuteStep(paramLong, 2459565876494606882L, 1);
/*     */     
/*  90 */     paramArrayOflong[paramInt] = paramLong & 0x5555555555555555L;
/*  91 */     paramArrayOflong[paramInt + 1] = paramLong >>> 1L & 0x5555555555555555L;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void expand64To128(long[] paramArrayOflong1, int paramInt1, int paramInt2, long[] paramArrayOflong2, int paramInt3) {
/*  96 */     for (byte b = 0; b < paramInt2; b++) {
/*     */       
/*  98 */       expand64To128(paramArrayOflong1[paramInt1 + b], paramArrayOflong2, paramInt3);
/*  99 */       paramInt3 += 2;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void expand64To128Rev(long paramLong, long[] paramArrayOflong, int paramInt) {
/* 106 */     paramLong = Bits.bitPermuteStep(paramLong, 4294901760L, 16);
/* 107 */     paramLong = Bits.bitPermuteStep(paramLong, 280375465148160L, 8);
/* 108 */     paramLong = Bits.bitPermuteStep(paramLong, 67555025218437360L, 4);
/* 109 */     paramLong = Bits.bitPermuteStep(paramLong, 868082074056920076L, 2);
/* 110 */     paramLong = Bits.bitPermuteStep(paramLong, 2459565876494606882L, 1);
/*     */     
/* 112 */     paramArrayOflong[paramInt] = paramLong & 0xAAAAAAAAAAAAAAAAL;
/* 113 */     paramArrayOflong[paramInt + 1] = paramLong << 1L & 0xAAAAAAAAAAAAAAAAL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int shuffle(int paramInt) {
/* 119 */     paramInt = Bits.bitPermuteStep(paramInt, 65280, 8);
/* 120 */     paramInt = Bits.bitPermuteStep(paramInt, 15728880, 4);
/* 121 */     paramInt = Bits.bitPermuteStep(paramInt, 202116108, 2);
/* 122 */     paramInt = Bits.bitPermuteStep(paramInt, 572662306, 1);
/* 123 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static long shuffle(long paramLong) {
/* 129 */     paramLong = Bits.bitPermuteStep(paramLong, 4294901760L, 16);
/* 130 */     paramLong = Bits.bitPermuteStep(paramLong, 280375465148160L, 8);
/* 131 */     paramLong = Bits.bitPermuteStep(paramLong, 67555025218437360L, 4);
/* 132 */     paramLong = Bits.bitPermuteStep(paramLong, 868082074056920076L, 2);
/* 133 */     paramLong = Bits.bitPermuteStep(paramLong, 2459565876494606882L, 1);
/* 134 */     return paramLong;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int shuffle2(int paramInt) {
/* 140 */     paramInt = Bits.bitPermuteStep(paramInt, 11141290, 7);
/* 141 */     paramInt = Bits.bitPermuteStep(paramInt, 52428, 14);
/* 142 */     paramInt = Bits.bitPermuteStep(paramInt, 15728880, 4);
/* 143 */     paramInt = Bits.bitPermuteStep(paramInt, 65280, 8);
/* 144 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static long shuffle2(long paramLong) {
/* 150 */     paramLong = Bits.bitPermuteStep(paramLong, 4278255360L, 24);
/* 151 */     paramLong = Bits.bitPermuteStep(paramLong, 57421771435671756L, 6);
/* 152 */     paramLong = Bits.bitPermuteStep(paramLong, 264913582878960L, 12);
/* 153 */     paramLong = Bits.bitPermuteStep(paramLong, 723401728380766730L, 3);
/* 154 */     return paramLong;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static long shuffle3(long paramLong) {
/* 160 */     paramLong = Bits.bitPermuteStep(paramLong, 47851476196393130L, 7);
/* 161 */     paramLong = Bits.bitPermuteStep(paramLong, 225176545447116L, 14);
/* 162 */     paramLong = Bits.bitPermuteStep(paramLong, 4042322160L, 28);
/* 163 */     return paramLong;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int unshuffle(int paramInt) {
/* 169 */     paramInt = Bits.bitPermuteStep(paramInt, 572662306, 1);
/* 170 */     paramInt = Bits.bitPermuteStep(paramInt, 202116108, 2);
/* 171 */     paramInt = Bits.bitPermuteStep(paramInt, 15728880, 4);
/* 172 */     paramInt = Bits.bitPermuteStep(paramInt, 65280, 8);
/* 173 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static long unshuffle(long paramLong) {
/* 179 */     paramLong = Bits.bitPermuteStep(paramLong, 2459565876494606882L, 1);
/* 180 */     paramLong = Bits.bitPermuteStep(paramLong, 868082074056920076L, 2);
/* 181 */     paramLong = Bits.bitPermuteStep(paramLong, 67555025218437360L, 4);
/* 182 */     paramLong = Bits.bitPermuteStep(paramLong, 280375465148160L, 8);
/* 183 */     paramLong = Bits.bitPermuteStep(paramLong, 4294901760L, 16);
/* 184 */     return paramLong;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int unshuffle2(int paramInt) {
/* 190 */     paramInt = Bits.bitPermuteStep(paramInt, 65280, 8);
/* 191 */     paramInt = Bits.bitPermuteStep(paramInt, 15728880, 4);
/* 192 */     paramInt = Bits.bitPermuteStep(paramInt, 52428, 14);
/* 193 */     paramInt = Bits.bitPermuteStep(paramInt, 11141290, 7);
/* 194 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static long unshuffle2(long paramLong) {
/* 200 */     paramLong = Bits.bitPermuteStep(paramLong, 723401728380766730L, 3);
/* 201 */     paramLong = Bits.bitPermuteStep(paramLong, 264913582878960L, 12);
/* 202 */     paramLong = Bits.bitPermuteStep(paramLong, 57421771435671756L, 6);
/* 203 */     paramLong = Bits.bitPermuteStep(paramLong, 4278255360L, 24);
/* 204 */     return paramLong;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static long unshuffle3(long paramLong) {
/* 210 */     return shuffle3(paramLong);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/raw/Interleave.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */