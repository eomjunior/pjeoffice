/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.channels.FileChannel;
/*     */ import org.bouncycastle.asn1.ASN1InputStream;
/*     */ import org.bouncycastle.asn1.LimitedInputStream;
/*     */ 
/*     */ class StreamUtil {
/*  11 */   private static final long MAX_MEMORY = Runtime.getRuntime().maxMemory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int findLimit(InputStream paramInputStream) {
/*  21 */     if (paramInputStream instanceof LimitedInputStream)
/*     */     {
/*  23 */       return ((LimitedInputStream)paramInputStream).getLimit();
/*     */     }
/*  25 */     if (paramInputStream instanceof ASN1InputStream)
/*     */     {
/*  27 */       return ((ASN1InputStream)paramInputStream).getLimit();
/*     */     }
/*  29 */     if (paramInputStream instanceof ByteArrayInputStream)
/*     */     {
/*  31 */       return ((ByteArrayInputStream)paramInputStream).available();
/*     */     }
/*  33 */     if (paramInputStream instanceof FileInputStream) {
/*     */       
/*     */       try {
/*     */         
/*  37 */         FileChannel fileChannel = ((FileInputStream)paramInputStream).getChannel();
/*  38 */         long l = (fileChannel != null) ? fileChannel.size() : 2147483647L;
/*     */         
/*  40 */         if (l < 2147483647L)
/*     */         {
/*  42 */           return (int)l;
/*     */         }
/*     */       }
/*  45 */       catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     if (MAX_MEMORY > 2147483647L)
/*     */     {
/*  53 */       return Integer.MAX_VALUE;
/*     */     }
/*     */     
/*  56 */     return (int)MAX_MEMORY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int calculateBodyLength(int paramInt) {
/*  62 */     byte b = 1;
/*     */     
/*  64 */     if (paramInt > 127) {
/*     */       
/*  66 */       byte b1 = 1;
/*  67 */       int i = paramInt;
/*     */       
/*  69 */       while ((i >>>= 8) != 0)
/*     */       {
/*  71 */         b1++;
/*     */       }
/*     */       
/*  74 */       for (int j = (b1 - 1) * 8; j >= 0; j -= 8)
/*     */       {
/*  76 */         b++;
/*     */       }
/*     */     } 
/*     */     
/*  80 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int calculateTagLength(int paramInt) throws IOException {
/*  86 */     int i = 1;
/*     */     
/*  88 */     if (paramInt >= 31)
/*     */     {
/*  90 */       if (paramInt < 128) {
/*     */         
/*  92 */         i++;
/*     */       }
/*     */       else {
/*     */         
/*  96 */         byte[] arrayOfByte = new byte[5];
/*  97 */         int j = arrayOfByte.length;
/*     */         
/*  99 */         arrayOfByte[--j] = (byte)(paramInt & 0x7F);
/*     */ 
/*     */         
/*     */         do {
/* 103 */           paramInt >>= 7;
/* 104 */           arrayOfByte[--j] = (byte)(paramInt & 0x7F | 0x80);
/*     */         }
/* 106 */         while (paramInt > 127);
/*     */         
/* 108 */         i += arrayOfByte.length - j;
/*     */       } 
/*     */     }
/*     */     
/* 112 */     return i;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/StreamUtil.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */