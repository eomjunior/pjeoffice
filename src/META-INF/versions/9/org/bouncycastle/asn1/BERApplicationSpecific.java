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
/*     */ public class BERApplicationSpecific
/*     */   extends ASN1ApplicationSpecific
/*     */ {
/*     */   BERApplicationSpecific(boolean paramBoolean, int paramInt, byte[] paramArrayOfbyte) {
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
/*     */   public BERApplicationSpecific(int paramInt, ASN1Encodable paramASN1Encodable) throws IOException {
/*  31 */     this(true, paramInt, paramASN1Encodable);
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
/*     */   public BERApplicationSpecific(boolean paramBoolean, int paramInt, ASN1Encodable paramASN1Encodable) throws IOException {
/*  47 */     super((paramBoolean || paramASN1Encodable.toASN1Primitive().isConstructed()), paramInt, getEncoding(paramBoolean, paramASN1Encodable));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] getEncoding(boolean paramBoolean, ASN1Encodable paramASN1Encodable) throws IOException {
/*  53 */     byte[] arrayOfByte1 = paramASN1Encodable.toASN1Primitive().getEncoded("BER");
/*     */     
/*  55 */     if (paramBoolean)
/*     */     {
/*  57 */       return arrayOfByte1;
/*     */     }
/*     */ 
/*     */     
/*  61 */     int i = getLengthOfHeader(arrayOfByte1);
/*  62 */     byte[] arrayOfByte2 = new byte[arrayOfByte1.length - i];
/*  63 */     System.arraycopy(arrayOfByte1, i, arrayOfByte2, 0, arrayOfByte2.length);
/*  64 */     return arrayOfByte2;
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
/*     */   public BERApplicationSpecific(int paramInt, ASN1EncodableVector paramASN1EncodableVector) {
/*  76 */     super(true, paramInt, getEncodedVector(paramASN1EncodableVector));
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] getEncodedVector(ASN1EncodableVector paramASN1EncodableVector) {
/*  81 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*     */     
/*  83 */     for (byte b = 0; b != paramASN1EncodableVector.size(); b++) {
/*     */ 
/*     */       
/*     */       try {
/*  87 */         byteArrayOutputStream.write(((ASN1Object)paramASN1EncodableVector.get(b)).getEncoded("BER"));
/*     */       }
/*  89 */       catch (IOException iOException) {
/*     */         
/*  91 */         throw new ASN1ParsingException("malformed object: " + iOException, iOException);
/*     */       } 
/*     */     } 
/*  94 */     return byteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 102 */     int i = 64;
/* 103 */     if (this.isConstructed)
/*     */     {
/* 105 */       i |= 0x20;
/*     */     }
/*     */     
/* 108 */     paramASN1OutputStream.writeEncodedIndef(paramBoolean, i, this.tag, this.octets);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/BERApplicationSpecific.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */