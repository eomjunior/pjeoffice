/*     */ package META-INF.versions.9.org.bouncycastle.util.encoders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UTF8
/*     */ {
/*     */   private static final byte C_ILL = 0;
/*     */   private static final byte C_CR1 = 1;
/*     */   private static final byte C_CR2 = 2;
/*     */   private static final byte C_CR3 = 3;
/*     */   private static final byte C_L2A = 4;
/*     */   private static final byte C_L3A = 5;
/*     */   private static final byte C_L3B = 6;
/*     */   private static final byte C_L3C = 7;
/*     */   private static final byte C_L4A = 8;
/*     */   private static final byte C_L4B = 9;
/*     */   private static final byte C_L4C = 10;
/*     */   private static final byte S_ERR = -2;
/*     */   private static final byte S_END = -1;
/*     */   private static final byte S_CS1 = 0;
/*     */   private static final byte S_CS2 = 16;
/*     */   private static final byte S_CS3 = 32;
/*     */   private static final byte S_P3A = 48;
/*     */   private static final byte S_P3B = 64;
/*     */   private static final byte S_P4A = 80;
/*     */   private static final byte S_P4B = 96;
/*  37 */   private static final short[] firstUnitTable = new short[128];
/*  38 */   private static final byte[] transitionTable = new byte[112];
/*     */ 
/*     */   
/*     */   private static void fill(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, byte paramByte) {
/*  42 */     for (int i = paramInt1; i <= paramInt2; i++)
/*     */     {
/*  44 */       paramArrayOfbyte[i] = paramByte;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/*  50 */     byte[] arrayOfByte1 = new byte[128];
/*  51 */     fill(arrayOfByte1, 0, 15, (byte)1);
/*  52 */     fill(arrayOfByte1, 16, 31, (byte)2);
/*  53 */     fill(arrayOfByte1, 32, 63, (byte)3);
/*  54 */     fill(arrayOfByte1, 64, 65, (byte)0);
/*  55 */     fill(arrayOfByte1, 66, 95, (byte)4);
/*  56 */     fill(arrayOfByte1, 96, 96, (byte)5);
/*  57 */     fill(arrayOfByte1, 97, 108, (byte)6);
/*  58 */     fill(arrayOfByte1, 109, 109, (byte)7);
/*  59 */     fill(arrayOfByte1, 110, 111, (byte)6);
/*  60 */     fill(arrayOfByte1, 112, 112, (byte)8);
/*  61 */     fill(arrayOfByte1, 113, 115, (byte)9);
/*  62 */     fill(arrayOfByte1, 116, 116, (byte)10);
/*  63 */     fill(arrayOfByte1, 117, 127, (byte)0);
/*     */     
/*  65 */     fill(transitionTable, 0, transitionTable.length - 1, (byte)-2);
/*  66 */     fill(transitionTable, 8, 11, (byte)-1);
/*  67 */     fill(transitionTable, 24, 27, (byte)0);
/*  68 */     fill(transitionTable, 40, 43, (byte)16);
/*  69 */     fill(transitionTable, 58, 59, (byte)0);
/*  70 */     fill(transitionTable, 72, 73, (byte)0);
/*  71 */     fill(transitionTable, 89, 91, (byte)16);
/*  72 */     fill(transitionTable, 104, 104, (byte)16);
/*     */     
/*  74 */     byte[] arrayOfByte2 = { 0, 0, 0, 0, 31, 15, 15, 15, 7, 7, 7 };
/*  75 */     byte[] arrayOfByte3 = { -2, -2, -2, -2, 0, 48, 16, 64, 80, 32, 96 };
/*     */     
/*  77 */     for (byte b = 0; b < ''; b++) {
/*     */       
/*  79 */       byte b1 = arrayOfByte1[b];
/*     */       
/*  81 */       int i = b & arrayOfByte2[b1];
/*  82 */       byte b2 = arrayOfByte3[b1];
/*     */       
/*  84 */       firstUnitTable[b] = (short)(i << 8 | b2);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int transcodeToUTF16(byte[] paramArrayOfbyte, char[] paramArrayOfchar) {
/* 109 */     byte b1 = 0, b2 = 0;
/*     */     
/* 111 */     while (b1 < paramArrayOfbyte.length) {
/*     */       
/* 113 */       byte b3 = paramArrayOfbyte[b1++];
/* 114 */       if (b3 >= 0) {
/*     */         
/* 116 */         if (b2 >= paramArrayOfchar.length) return -1;
/*     */         
/* 118 */         paramArrayOfchar[b2++] = (char)b3;
/*     */         
/*     */         continue;
/*     */       } 
/* 122 */       short s = firstUnitTable[b3 & Byte.MAX_VALUE];
/* 123 */       int i = s >>> 8;
/* 124 */       byte b4 = (byte)s;
/*     */       
/* 126 */       while (b4 >= 0) {
/*     */         
/* 128 */         if (b1 >= paramArrayOfbyte.length) return -1;
/*     */         
/* 130 */         b3 = paramArrayOfbyte[b1++];
/* 131 */         i = i << 6 | b3 & 0x3F;
/* 132 */         b4 = transitionTable[b4 + ((b3 & 0xFF) >>> 4)];
/*     */       } 
/*     */       
/* 135 */       if (b4 == -2) return -1;
/*     */       
/* 137 */       if (i <= 65535) {
/*     */         
/* 139 */         if (b2 >= paramArrayOfchar.length) return -1;
/*     */ 
/*     */         
/* 142 */         paramArrayOfchar[b2++] = (char)i;
/*     */         
/*     */         continue;
/*     */       } 
/* 146 */       if (b2 >= paramArrayOfchar.length - 1) return -1;
/*     */ 
/*     */       
/* 149 */       paramArrayOfchar[b2++] = (char)(55232 + (i >>> 10));
/* 150 */       paramArrayOfchar[b2++] = (char)(0xDC00 | i & 0x3FF);
/*     */     } 
/*     */ 
/*     */     
/* 154 */     return b2;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/util/encoders/UTF8.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */