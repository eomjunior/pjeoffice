/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ASN1ApplicationSpecific
/*     */   extends ASN1Primitive
/*     */ {
/*     */   protected final boolean isConstructed;
/*     */   protected final int tag;
/*     */   protected final byte[] octets;
/*     */   
/*     */   ASN1ApplicationSpecific(boolean paramBoolean, int paramInt, byte[] paramArrayOfbyte) {
/*  23 */     this.isConstructed = paramBoolean;
/*  24 */     this.tag = paramInt;
/*  25 */     this.octets = Arrays.clone(paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1ApplicationSpecific getInstance(Object paramObject) {
/*  36 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.ASN1ApplicationSpecific)
/*     */     {
/*  38 */       return (org.bouncycastle.asn1.ASN1ApplicationSpecific)paramObject;
/*     */     }
/*  40 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  44 */         return getInstance(ASN1Primitive.fromByteArray((byte[])paramObject));
/*     */       }
/*  46 */       catch (IOException iOException) {
/*     */         
/*  48 */         throw new IllegalArgumentException("Failed to construct object from byte[]: " + iOException.getMessage());
/*     */       } 
/*     */     }
/*     */     
/*  52 */     throw new IllegalArgumentException("unknown object in getInstance: " + paramObject.getClass().getName());
/*     */   }
/*     */ 
/*     */   
/*     */   protected static int getLengthOfHeader(byte[] paramArrayOfbyte) {
/*  57 */     int i = paramArrayOfbyte[1] & 0xFF;
/*     */     
/*  59 */     if (i == 128)
/*     */     {
/*  61 */       return 2;
/*     */     }
/*     */     
/*  64 */     if (i > 127) {
/*     */       
/*  66 */       int j = i & 0x7F;
/*     */ 
/*     */       
/*  69 */       if (j > 4)
/*     */       {
/*  71 */         throw new IllegalStateException("DER length more than 4 bytes: " + j);
/*     */       }
/*     */       
/*  74 */       return j + 2;
/*     */     } 
/*     */     
/*  77 */     return 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstructed() {
/*  87 */     return this.isConstructed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getContents() {
/*  97 */     return Arrays.clone(this.octets);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getApplicationTag() {
/* 107 */     return this.tag;
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
/*     */   public ASN1Primitive getObject() throws IOException {
/* 119 */     return ASN1Primitive.fromByteArray(getContents());
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
/*     */   public ASN1Primitive getObject(int paramInt) throws IOException {
/* 132 */     if (paramInt >= 31)
/*     */     {
/* 134 */       throw new IOException("unsupported tag number");
/*     */     }
/*     */     
/* 137 */     byte[] arrayOfByte1 = getEncoded();
/* 138 */     byte[] arrayOfByte2 = replaceTagNumber(paramInt, arrayOfByte1);
/*     */     
/* 140 */     if ((arrayOfByte1[0] & 0x20) != 0)
/*     */     {
/* 142 */       arrayOfByte2[0] = (byte)(arrayOfByte2[0] | 0x20);
/*     */     }
/*     */     
/* 145 */     return ASN1Primitive.fromByteArray(arrayOfByte2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int encodedLength() throws IOException {
/* 151 */     return StreamUtil.calculateTagLength(this.tag) + StreamUtil.calculateBodyLength(this.octets.length) + this.octets.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 159 */     int i = 64;
/* 160 */     if (this.isConstructed)
/*     */     {
/* 162 */       i |= 0x20;
/*     */     }
/*     */     
/* 165 */     paramASN1OutputStream.writeEncoded(paramBoolean, i, this.tag, this.octets);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 171 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.ASN1ApplicationSpecific))
/*     */     {
/* 173 */       return false;
/*     */     }
/*     */     
/* 176 */     org.bouncycastle.asn1.ASN1ApplicationSpecific aSN1ApplicationSpecific = (org.bouncycastle.asn1.ASN1ApplicationSpecific)paramASN1Primitive;
/*     */     
/* 178 */     return (this.isConstructed == aSN1ApplicationSpecific.isConstructed && this.tag == aSN1ApplicationSpecific.tag && 
/*     */       
/* 180 */       Arrays.areEqual(this.octets, aSN1ApplicationSpecific.octets));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 185 */     return (this.isConstructed ? 1 : 0) ^ this.tag ^ Arrays.hashCode(this.octets);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] replaceTagNumber(int paramInt, byte[] paramArrayOfbyte) throws IOException {
/* 191 */     int i = paramArrayOfbyte[0] & 0x1F;
/* 192 */     byte b = 1;
/*     */ 
/*     */ 
/*     */     
/* 196 */     if (i == 31) {
/*     */       
/* 198 */       int j = paramArrayOfbyte[b++] & 0xFF;
/*     */ 
/*     */ 
/*     */       
/* 202 */       if ((j & 0x7F) == 0)
/*     */       {
/* 204 */         throw new IOException("corrupted stream - invalid high tag number found");
/*     */       }
/*     */       
/* 207 */       while ((j & 0x80) != 0)
/*     */       {
/* 209 */         j = paramArrayOfbyte[b++] & 0xFF;
/*     */       }
/*     */     } 
/*     */     
/* 213 */     byte[] arrayOfByte = new byte[paramArrayOfbyte.length - b + 1];
/*     */     
/* 215 */     System.arraycopy(paramArrayOfbyte, b, arrayOfByte, 1, arrayOfByte.length - 1);
/*     */     
/* 217 */     arrayOfByte[0] = (byte)paramInt;
/*     */     
/* 219 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 224 */     StringBuffer stringBuffer = new StringBuffer();
/* 225 */     stringBuffer.append("[");
/* 226 */     if (isConstructed())
/*     */     {
/* 228 */       stringBuffer.append("CONSTRUCTED ");
/*     */     }
/* 230 */     stringBuffer.append("APPLICATION ");
/* 231 */     stringBuffer.append(Integer.toString(getApplicationTag()));
/* 232 */     stringBuffer.append("]");
/*     */     
/* 234 */     if (this.octets != null) {
/*     */       
/* 236 */       stringBuffer.append(" #");
/* 237 */       stringBuffer.append(Hex.toHexString(this.octets));
/*     */     }
/*     */     else {
/*     */       
/* 241 */       stringBuffer.append(" #null");
/*     */     } 
/* 243 */     stringBuffer.append(" ");
/* 244 */     return stringBuffer.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1ApplicationSpecific.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */