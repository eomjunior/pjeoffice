/*     */ package META-INF.versions.9.org.bouncycastle.util.encoders;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import org.bouncycastle.util.Strings;
/*     */ import org.bouncycastle.util.encoders.DecoderException;
/*     */ import org.bouncycastle.util.encoders.EncoderException;
/*     */ import org.bouncycastle.util.encoders.HexEncoder;
/*     */ 
/*     */ 
/*     */ public class Hex
/*     */ {
/*  14 */   private static final HexEncoder encoder = new HexEncoder();
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toHexString(byte[] paramArrayOfbyte) {
/*  19 */     return toHexString(paramArrayOfbyte, 0, paramArrayOfbyte.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toHexString(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*  27 */     byte[] arrayOfByte = encode(paramArrayOfbyte, paramInt1, paramInt2);
/*  28 */     return Strings.fromByteArray(arrayOfByte);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] encode(byte[] paramArrayOfbyte) {
/*  39 */     return encode(paramArrayOfbyte, 0, paramArrayOfbyte.length);
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
/*     */   public static byte[] encode(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
/*  52 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*     */ 
/*     */     
/*     */     try {
/*  56 */       encoder.encode(paramArrayOfbyte, paramInt1, paramInt2, byteArrayOutputStream);
/*     */     }
/*  58 */     catch (Exception exception) {
/*     */       
/*  60 */       throw new EncoderException("exception encoding Hex string: " + exception.getMessage(), exception);
/*     */     } 
/*     */     
/*  63 */     return byteArrayOutputStream.toByteArray();
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
/*     */   public static int encode(byte[] paramArrayOfbyte, OutputStream paramOutputStream) throws IOException {
/*  76 */     return encoder.encode(paramArrayOfbyte, 0, paramArrayOfbyte.length, paramOutputStream);
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
/*     */   public static int encode(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, OutputStream paramOutputStream) throws IOException {
/*  91 */     return encoder.encode(paramArrayOfbyte, paramInt1, paramInt2, paramOutputStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decode(byte[] paramArrayOfbyte) {
/* 102 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*     */ 
/*     */     
/*     */     try {
/* 106 */       encoder.decode(paramArrayOfbyte, 0, paramArrayOfbyte.length, byteArrayOutputStream);
/*     */     }
/* 108 */     catch (Exception exception) {
/*     */       
/* 110 */       throw new DecoderException("exception decoding Hex data: " + exception.getMessage(), exception);
/*     */     } 
/*     */     
/* 113 */     return byteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decode(String paramString) {
/* 124 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*     */ 
/*     */     
/*     */     try {
/* 128 */       encoder.decode(paramString, byteArrayOutputStream);
/*     */     }
/* 130 */     catch (Exception exception) {
/*     */       
/* 132 */       throw new DecoderException("exception decoding Hex string: " + exception.getMessage(), exception);
/*     */     } 
/*     */     
/* 135 */     return byteArrayOutputStream.toByteArray();
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
/*     */   public static int decode(String paramString, OutputStream paramOutputStream) throws IOException {
/* 149 */     return encoder.decode(paramString, paramOutputStream);
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
/*     */   public static byte[] decodeStrict(String paramString) {
/*     */     try {
/* 162 */       return encoder.decodeStrict(paramString, 0, paramString.length());
/*     */     }
/* 164 */     catch (Exception exception) {
/*     */       
/* 166 */       throw new DecoderException("exception decoding Hex string: " + exception.getMessage(), exception);
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
/*     */   public static byte[] decodeStrict(String paramString, int paramInt1, int paramInt2) {
/*     */     try {
/* 180 */       return encoder.decodeStrict(paramString, paramInt1, paramInt2);
/*     */     }
/* 182 */     catch (Exception exception) {
/*     */       
/* 184 */       throw new DecoderException("exception decoding Hex string: " + exception.getMessage(), exception);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/util/encoders/Hex.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */