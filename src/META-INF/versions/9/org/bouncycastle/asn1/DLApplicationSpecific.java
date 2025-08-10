/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.asn1.ASN1ApplicationSpecific;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1Object;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1ParsingException;
/*     */ 
/*     */ 
/*     */ public class DLApplicationSpecific
/*     */   extends ASN1ApplicationSpecific
/*     */ {
/*     */   DLApplicationSpecific(boolean paramBoolean, int paramInt, byte[] paramArrayOfbyte) {
/*  17 */     super(paramBoolean, paramInt, paramArrayOfbyte);
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
/*     */   public DLApplicationSpecific(int paramInt, byte[] paramArrayOfbyte) {
/*  31 */     this(false, paramInt, paramArrayOfbyte);
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
/*     */   public DLApplicationSpecific(int paramInt, ASN1Encodable paramASN1Encodable) throws IOException {
/*  45 */     this(true, paramInt, paramASN1Encodable);
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
/*     */   public DLApplicationSpecific(boolean paramBoolean, int paramInt, ASN1Encodable paramASN1Encodable) throws IOException {
/*  61 */     super((paramBoolean || paramASN1Encodable.toASN1Primitive().isConstructed()), paramInt, getEncoding(paramBoolean, paramASN1Encodable));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] getEncoding(boolean paramBoolean, ASN1Encodable paramASN1Encodable) throws IOException {
/*  67 */     byte[] arrayOfByte1 = paramASN1Encodable.toASN1Primitive().getEncoded("DL");
/*     */     
/*  69 */     if (paramBoolean)
/*     */     {
/*  71 */       return arrayOfByte1;
/*     */     }
/*     */ 
/*     */     
/*  75 */     int i = getLengthOfHeader(arrayOfByte1);
/*  76 */     byte[] arrayOfByte2 = new byte[arrayOfByte1.length - i];
/*  77 */     System.arraycopy(arrayOfByte1, i, arrayOfByte2, 0, arrayOfByte2.length);
/*  78 */     return arrayOfByte2;
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
/*     */   public DLApplicationSpecific(int paramInt, ASN1EncodableVector paramASN1EncodableVector) {
/*  90 */     super(true, paramInt, getEncodedVector(paramASN1EncodableVector));
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] getEncodedVector(ASN1EncodableVector paramASN1EncodableVector) {
/*  95 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*     */     
/*  97 */     for (byte b = 0; b != paramASN1EncodableVector.size(); b++) {
/*     */ 
/*     */       
/*     */       try {
/* 101 */         byteArrayOutputStream.write(((ASN1Object)paramASN1EncodableVector.get(b)).getEncoded("DL"));
/*     */       }
/* 103 */       catch (IOException iOException) {
/*     */         
/* 105 */         throw new ASN1ParsingException("malformed object: " + iOException, iOException);
/*     */       } 
/*     */     } 
/* 108 */     return byteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 116 */     int i = 64;
/* 117 */     if (this.isConstructed)
/*     */     {
/* 119 */       i |= 0x20;
/*     */     }
/*     */     
/* 122 */     paramASN1OutputStream.writeEncoded(paramBoolean, i, this.tag, this.octets);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DLApplicationSpecific.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */