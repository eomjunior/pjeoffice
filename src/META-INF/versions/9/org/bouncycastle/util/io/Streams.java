/*     */ package META-INF.versions.9.org.bouncycastle.util.io;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.bouncycastle.util.io.StreamOverflowException;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Streams
/*     */ {
/*  13 */   private static int BUFFER_SIZE = 4096;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void drain(InputStream paramInputStream) throws IOException {
/*  24 */     byte[] arrayOfByte = new byte[BUFFER_SIZE];
/*  25 */     while (paramInputStream.read(arrayOfByte, 0, arrayOfByte.length) >= 0);
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
/*     */   public static byte[] readAll(InputStream paramInputStream) throws IOException {
/*  40 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*  41 */     pipeAll(paramInputStream, byteArrayOutputStream);
/*  42 */     return byteArrayOutputStream.toByteArray();
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
/*     */   public static byte[] readAllLimited(InputStream paramInputStream, int paramInt) throws IOException {
/*  57 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*  58 */     pipeAllLimited(paramInputStream, paramInt, byteArrayOutputStream);
/*  59 */     return byteArrayOutputStream.toByteArray();
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
/*     */   public static int readFully(InputStream paramInputStream, byte[] paramArrayOfbyte) throws IOException {
/*  73 */     return readFully(paramInputStream, paramArrayOfbyte, 0, paramArrayOfbyte.length);
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
/*     */   public static int readFully(InputStream paramInputStream, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
/*  89 */     int i = 0;
/*  90 */     while (i < paramInt2) {
/*     */       
/*  92 */       int j = paramInputStream.read(paramArrayOfbyte, paramInt1 + i, paramInt2 - i);
/*  93 */       if (j < 0) {
/*     */         break;
/*     */       }
/*     */       
/*  97 */       i += j;
/*     */     } 
/*  99 */     return i;
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
/*     */   public static void pipeAll(InputStream paramInputStream, OutputStream paramOutputStream) throws IOException {
/* 112 */     byte[] arrayOfByte = new byte[BUFFER_SIZE];
/*     */     int i;
/* 114 */     while ((i = paramInputStream.read(arrayOfByte, 0, arrayOfByte.length)) >= 0)
/*     */     {
/* 116 */       paramOutputStream.write(arrayOfByte, 0, i);
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
/*     */   public static long pipeAllLimited(InputStream paramInputStream, long paramLong, OutputStream paramOutputStream) throws IOException {
/* 131 */     long l = 0L;
/* 132 */     byte[] arrayOfByte = new byte[BUFFER_SIZE];
/*     */     int i;
/* 134 */     while ((i = paramInputStream.read(arrayOfByte, 0, arrayOfByte.length)) >= 0) {
/*     */       
/* 136 */       if (paramLong - l < i)
/*     */       {
/* 138 */         throw new StreamOverflowException("Data Overflow");
/*     */       }
/* 140 */       l += i;
/* 141 */       paramOutputStream.write(arrayOfByte, 0, i);
/*     */     } 
/* 143 */     return l;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeBufTo(ByteArrayOutputStream paramByteArrayOutputStream, OutputStream paramOutputStream) throws IOException {
/* 149 */     paramByteArrayOutputStream.writeTo(paramOutputStream);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/util/io/Streams.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */