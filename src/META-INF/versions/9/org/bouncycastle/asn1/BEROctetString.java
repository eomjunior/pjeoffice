/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
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
/*     */ public class BEROctetString
/*     */   extends ASN1OctetString
/*     */ {
/*     */   private static final int DEFAULT_CHUNK_SIZE = 1000;
/*     */   private final int chunkSize;
/*     */   private final ASN1OctetString[] octs;
/*     */   
/*     */   private static byte[] toBytes(ASN1OctetString[] paramArrayOfASN1OctetString) {
/*  36 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*     */     
/*  38 */     for (byte b = 0; b != paramArrayOfASN1OctetString.length; b++) {
/*     */ 
/*     */       
/*     */       try {
/*  42 */         byteArrayOutputStream.write(paramArrayOfASN1OctetString[b].getOctets());
/*     */       }
/*  44 */       catch (IOException iOException) {
/*     */         
/*  46 */         throw new IllegalArgumentException("exception converting octets " + iOException.toString());
/*     */       } 
/*     */     } 
/*     */     
/*  50 */     return byteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BEROctetString(byte[] paramArrayOfbyte) {
/*  60 */     this(paramArrayOfbyte, 1000);
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
/*     */   public BEROctetString(ASN1OctetString[] paramArrayOfASN1OctetString) {
/*  72 */     this(paramArrayOfASN1OctetString, 1000);
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
/*     */   public BEROctetString(byte[] paramArrayOfbyte, int paramInt) {
/*  84 */     this(paramArrayOfbyte, null, paramInt);
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
/*     */   public BEROctetString(ASN1OctetString[] paramArrayOfASN1OctetString, int paramInt) {
/*  98 */     this(toBytes(paramArrayOfASN1OctetString), paramArrayOfASN1OctetString, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   private BEROctetString(byte[] paramArrayOfbyte, ASN1OctetString[] paramArrayOfASN1OctetString, int paramInt) {
/* 103 */     super(paramArrayOfbyte);
/* 104 */     this.octs = paramArrayOfASN1OctetString;
/* 105 */     this.chunkSize = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getObjects() {
/* 115 */     if (this.octs == null)
/*     */     {
/* 117 */       return (Enumeration)new Object(this);
/*     */     }
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
/* 141 */     return (Enumeration)new Object(this);
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
/*     */   boolean isConstructed() {
/* 163 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int encodedLength() throws IOException {
/* 169 */     int i = 0;
/* 170 */     for (Enumeration<ASN1Encodable> enumeration = getObjects(); enumeration.hasMoreElements();)
/*     */     {
/* 172 */       i += ((ASN1Encodable)enumeration.nextElement()).toASN1Primitive().encodedLength();
/*     */     }
/*     */     
/* 175 */     return 2 + i + 2;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 180 */     paramASN1OutputStream.writeEncodedIndef(paramBoolean, 36, getObjects());
/*     */   }
/*     */ 
/*     */   
/*     */   static org.bouncycastle.asn1.BEROctetString fromSequence(ASN1Sequence paramASN1Sequence) {
/* 185 */     int i = paramASN1Sequence.size();
/* 186 */     ASN1OctetString[] arrayOfASN1OctetString = new ASN1OctetString[i];
/* 187 */     for (byte b = 0; b < i; b++)
/*     */     {
/* 189 */       arrayOfASN1OctetString[b] = ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(b));
/*     */     }
/* 191 */     return new org.bouncycastle.asn1.BEROctetString(arrayOfASN1OctetString);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/BEROctetString.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */