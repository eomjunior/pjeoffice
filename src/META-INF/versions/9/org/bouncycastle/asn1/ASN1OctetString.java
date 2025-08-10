/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1OctetStringParser;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.BEROctetString;
/*     */ import org.bouncycastle.asn1.DEROctetString;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.Strings;
/*     */ import org.bouncycastle.util.encoders.Hex;
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
/*     */ public abstract class ASN1OctetString
/*     */   extends ASN1Primitive
/*     */   implements ASN1OctetStringParser
/*     */ {
/*     */   byte[] string;
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1OctetString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/* 118 */     if (paramBoolean) {
/*     */       
/* 120 */       if (!paramASN1TaggedObject.isExplicit())
/*     */       {
/* 122 */         throw new IllegalArgumentException("object implicit - explicit expected.");
/*     */       }
/*     */       
/* 125 */       return getInstance(paramASN1TaggedObject.getObject());
/*     */     } 
/*     */     
/* 128 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     if (paramASN1TaggedObject.isExplicit()) {
/*     */       
/* 136 */       org.bouncycastle.asn1.ASN1OctetString aSN1OctetString = getInstance(aSN1Primitive);
/*     */       
/* 138 */       if (paramASN1TaggedObject instanceof org.bouncycastle.asn1.BERTaggedObject)
/*     */       {
/* 140 */         return (org.bouncycastle.asn1.ASN1OctetString)new BEROctetString(new org.bouncycastle.asn1.ASN1OctetString[] { aSN1OctetString });
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 145 */       return (org.bouncycastle.asn1.ASN1OctetString)(new BEROctetString(new org.bouncycastle.asn1.ASN1OctetString[] { aSN1OctetString })).toDLObject();
/*     */     } 
/*     */     
/* 148 */     if (aSN1Primitive instanceof org.bouncycastle.asn1.ASN1OctetString) {
/*     */       
/* 150 */       org.bouncycastle.asn1.ASN1OctetString aSN1OctetString = (org.bouncycastle.asn1.ASN1OctetString)aSN1Primitive;
/*     */       
/* 152 */       if (paramASN1TaggedObject instanceof org.bouncycastle.asn1.BERTaggedObject)
/*     */       {
/* 154 */         return aSN1OctetString;
/*     */       }
/*     */       
/* 157 */       return (org.bouncycastle.asn1.ASN1OctetString)aSN1OctetString.toDLObject();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     if (aSN1Primitive instanceof ASN1Sequence) {
/*     */       
/* 165 */       ASN1Sequence aSN1Sequence = (ASN1Sequence)aSN1Primitive;
/*     */       
/* 167 */       if (paramASN1TaggedObject instanceof org.bouncycastle.asn1.BERTaggedObject)
/*     */       {
/* 169 */         return (org.bouncycastle.asn1.ASN1OctetString)BEROctetString.fromSequence(aSN1Sequence);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 174 */       return (org.bouncycastle.asn1.ASN1OctetString)BEROctetString.fromSequence(aSN1Sequence).toDLObject();
/*     */     } 
/*     */     
/* 177 */     throw new IllegalArgumentException("unknown object in getInstance: " + paramASN1TaggedObject.getClass().getName());
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
/*     */   public static org.bouncycastle.asn1.ASN1OctetString getInstance(Object paramObject) {
/* 189 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.ASN1OctetString)
/*     */     {
/* 191 */       return (org.bouncycastle.asn1.ASN1OctetString)paramObject;
/*     */     }
/* 193 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/* 197 */         return getInstance(fromByteArray((byte[])paramObject));
/*     */       }
/* 199 */       catch (IOException iOException) {
/*     */         
/* 201 */         throw new IllegalArgumentException("failed to construct OCTET STRING from byte[]: " + iOException.getMessage());
/*     */       } 
/*     */     }
/* 204 */     if (paramObject instanceof ASN1Encodable) {
/*     */       
/* 206 */       ASN1Primitive aSN1Primitive = ((ASN1Encodable)paramObject).toASN1Primitive();
/*     */       
/* 208 */       if (aSN1Primitive instanceof org.bouncycastle.asn1.ASN1OctetString)
/*     */       {
/* 210 */         return (org.bouncycastle.asn1.ASN1OctetString)aSN1Primitive;
/*     */       }
/*     */     } 
/*     */     
/* 214 */     throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1OctetString(byte[] paramArrayOfbyte) {
/* 225 */     if (paramArrayOfbyte == null)
/*     */     {
/* 227 */       throw new NullPointerException("'string' cannot be null");
/*     */     }
/* 229 */     this.string = paramArrayOfbyte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getOctetStream() {
/* 239 */     return new ByteArrayInputStream(this.string);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1OctetStringParser parser() {
/* 249 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getOctets() {
/* 259 */     return this.string;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 264 */     return Arrays.hashCode(getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 270 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.ASN1OctetString))
/*     */     {
/* 272 */       return false;
/*     */     }
/*     */     
/* 275 */     org.bouncycastle.asn1.ASN1OctetString aSN1OctetString = (org.bouncycastle.asn1.ASN1OctetString)paramASN1Primitive;
/*     */     
/* 277 */     return Arrays.areEqual(this.string, aSN1OctetString.string);
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1Primitive getLoadedObject() {
/* 282 */     return toASN1Primitive();
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDERObject() {
/* 287 */     return (ASN1Primitive)new DEROctetString(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDLObject() {
/* 292 */     return (ASN1Primitive)new DEROctetString(this.string);
/*     */   }
/*     */ 
/*     */   
/*     */   abstract void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException;
/*     */   
/*     */   public String toString() {
/* 299 */     return "#" + Strings.fromByteArray(Hex.encode(this.string));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1OctetString.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */