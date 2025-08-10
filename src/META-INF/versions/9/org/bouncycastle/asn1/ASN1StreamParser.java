/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1Exception;
/*     */ import org.bouncycastle.asn1.ASN1InputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.BEROctetStringParser;
/*     */ import org.bouncycastle.asn1.BERTaggedObject;
/*     */ import org.bouncycastle.asn1.DERExternalParser;
/*     */ import org.bouncycastle.asn1.DLSetParser;
/*     */ import org.bouncycastle.asn1.DLTaggedObject;
/*     */ import org.bouncycastle.asn1.DefiniteLengthInputStream;
/*     */ import org.bouncycastle.asn1.IndefiniteLengthInputStream;
/*     */ 
/*     */ public class ASN1StreamParser {
/*     */   public ASN1StreamParser(InputStream paramInputStream) {
/*  19 */     this(paramInputStream, StreamUtil.findLimit(paramInputStream));
/*     */   }
/*     */   private final InputStream _in;
/*     */   private final int _limit;
/*     */   private final byte[][] tmpBuffers;
/*     */   
/*     */   public ASN1StreamParser(InputStream paramInputStream, int paramInt) {
/*  26 */     this._in = paramInputStream;
/*  27 */     this._limit = paramInt;
/*     */     
/*  29 */     this.tmpBuffers = new byte[11][];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1StreamParser(byte[] paramArrayOfbyte) {
/*  35 */     this(new ByteArrayInputStream(paramArrayOfbyte), paramArrayOfbyte.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ASN1Encodable readIndef(int paramInt) throws IOException {
/*  43 */     switch (paramInt) {
/*     */       
/*     */       case 8:
/*  46 */         return (ASN1Encodable)new DERExternalParser(this);
/*     */       case 4:
/*  48 */         return (ASN1Encodable)new BEROctetStringParser(this);
/*     */       case 16:
/*  50 */         return (ASN1Encodable)new BERSequenceParser(this);
/*     */       case 17:
/*  52 */         return (ASN1Encodable)new BERSetParser(this);
/*     */     } 
/*  54 */     throw new ASN1Exception("unknown BER object encountered: 0x" + Integer.toHexString(paramInt));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ASN1Encodable readImplicit(boolean paramBoolean, int paramInt) throws IOException {
/*  60 */     if (this._in instanceof IndefiniteLengthInputStream) {
/*     */       
/*  62 */       if (!paramBoolean)
/*     */       {
/*  64 */         throw new IOException("indefinite-length primitive encoding encountered");
/*     */       }
/*     */       
/*  67 */       return readIndef(paramInt);
/*     */     } 
/*     */     
/*  70 */     if (paramBoolean) {
/*     */       
/*  72 */       switch (paramInt) {
/*     */         
/*     */         case 17:
/*  75 */           return (ASN1Encodable)new DLSetParser(this);
/*     */         case 16:
/*  77 */           return (ASN1Encodable)new DLSequenceParser(this);
/*     */         case 4:
/*  79 */           return (ASN1Encodable)new BEROctetStringParser(this);
/*     */       } 
/*     */ 
/*     */     
/*     */     } else {
/*  84 */       switch (paramInt) {
/*     */         
/*     */         case 17:
/*  87 */           throw new ASN1Exception("sequences must use constructed encoding (see X.690 8.9.1/8.10.1)");
/*     */         case 16:
/*  89 */           throw new ASN1Exception("sets must use constructed encoding (see X.690 8.11.1/8.12.1)");
/*     */         case 4:
/*  91 */           return (ASN1Encodable)new DEROctetStringParser((DefiniteLengthInputStream)this._in);
/*     */       } 
/*     */     
/*     */     } 
/*  95 */     throw new ASN1Exception("implicit tagging not implemented");
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive readTaggedObject(boolean paramBoolean, int paramInt) throws IOException {
/* 100 */     if (!paramBoolean) {
/*     */ 
/*     */       
/* 103 */       DefiniteLengthInputStream definiteLengthInputStream = (DefiniteLengthInputStream)this._in;
/* 104 */       return (ASN1Primitive)new DLTaggedObject(false, paramInt, (ASN1Encodable)new DEROctetString(definiteLengthInputStream.toByteArray()));
/*     */     } 
/*     */     
/* 107 */     ASN1EncodableVector aSN1EncodableVector = readVector();
/*     */     
/* 109 */     if (this._in instanceof IndefiniteLengthInputStream)
/*     */     {
/* 111 */       return (aSN1EncodableVector.size() == 1) ? 
/* 112 */         (ASN1Primitive)new BERTaggedObject(true, paramInt, aSN1EncodableVector.get(0)) : 
/* 113 */         (ASN1Primitive)new BERTaggedObject(false, paramInt, (ASN1Encodable)BERFactory.createSequence(aSN1EncodableVector));
/*     */     }
/*     */     
/* 116 */     return (aSN1EncodableVector.size() == 1) ? 
/* 117 */       (ASN1Primitive)new DLTaggedObject(true, paramInt, aSN1EncodableVector.get(0)) : 
/* 118 */       (ASN1Primitive)new DLTaggedObject(false, paramInt, (ASN1Encodable)DLFactory.createSequence(aSN1EncodableVector));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Encodable readObject() throws IOException {
/* 124 */     int i = this._in.read();
/* 125 */     if (i == -1)
/*     */     {
/* 127 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 133 */     set00Check(false);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     int j = ASN1InputStream.readTagNumber(this._in, i);
/*     */     
/* 140 */     boolean bool = ((i & 0x20) != 0) ? true : false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     int k = ASN1InputStream.readLength(this._in, this._limit, (j == 4 || j == 16 || j == 17 || j == 8));
/*     */ 
/*     */     
/* 148 */     if (k < 0) {
/*     */       
/* 150 */       if (!bool)
/*     */       {
/* 152 */         throw new IOException("indefinite-length primitive encoding encountered");
/*     */       }
/*     */       
/* 155 */       IndefiniteLengthInputStream indefiniteLengthInputStream = new IndefiniteLengthInputStream(this._in, this._limit);
/* 156 */       org.bouncycastle.asn1.ASN1StreamParser aSN1StreamParser = new org.bouncycastle.asn1.ASN1StreamParser((InputStream)indefiniteLengthInputStream, this._limit);
/*     */       
/* 158 */       if ((i & 0x40) != 0)
/*     */       {
/* 160 */         return (ASN1Encodable)new BERApplicationSpecificParser(j, aSN1StreamParser);
/*     */       }
/*     */       
/* 163 */       if ((i & 0x80) != 0)
/*     */       {
/* 165 */         return (ASN1Encodable)new BERTaggedObjectParser(true, j, aSN1StreamParser);
/*     */       }
/*     */       
/* 168 */       return aSN1StreamParser.readIndef(j);
/*     */     } 
/*     */ 
/*     */     
/* 172 */     DefiniteLengthInputStream definiteLengthInputStream = new DefiniteLengthInputStream(this._in, k, this._limit);
/*     */     
/* 174 */     if ((i & 0x40) != 0)
/*     */     {
/* 176 */       return (ASN1Encodable)new DLApplicationSpecific(bool, j, definiteLengthInputStream.toByteArray());
/*     */     }
/*     */     
/* 179 */     if ((i & 0x80) != 0)
/*     */     {
/* 181 */       return (ASN1Encodable)new BERTaggedObjectParser(bool, j, new org.bouncycastle.asn1.ASN1StreamParser((InputStream)definiteLengthInputStream));
/*     */     }
/*     */     
/* 184 */     if (bool) {
/*     */ 
/*     */       
/* 187 */       switch (j) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 4:
/* 193 */           return (ASN1Encodable)new BEROctetStringParser(new org.bouncycastle.asn1.ASN1StreamParser((InputStream)definiteLengthInputStream));
/*     */         case 16:
/* 195 */           return (ASN1Encodable)new DLSequenceParser(new org.bouncycastle.asn1.ASN1StreamParser((InputStream)definiteLengthInputStream));
/*     */         case 17:
/* 197 */           return (ASN1Encodable)new DLSetParser(new org.bouncycastle.asn1.ASN1StreamParser((InputStream)definiteLengthInputStream));
/*     */         case 8:
/* 199 */           return (ASN1Encodable)new DERExternalParser(new org.bouncycastle.asn1.ASN1StreamParser((InputStream)definiteLengthInputStream));
/*     */       } 
/* 201 */       throw new IOException("unknown tag " + j + " encountered");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 206 */     switch (j) {
/*     */       
/*     */       case 4:
/* 209 */         return (ASN1Encodable)new DEROctetStringParser(definiteLengthInputStream);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 214 */       return (ASN1Encodable)ASN1InputStream.createPrimitiveDERObject(j, definiteLengthInputStream, this.tmpBuffers);
/*     */     }
/* 216 */     catch (IllegalArgumentException illegalArgumentException) {
/*     */       
/* 218 */       throw new ASN1Exception("corrupted stream detected", illegalArgumentException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void set00Check(boolean paramBoolean) {
/* 225 */     if (this._in instanceof IndefiniteLengthInputStream)
/*     */     {
/* 227 */       ((IndefiniteLengthInputStream)this._in).setEofOn00(paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1EncodableVector readVector() throws IOException {
/* 233 */     ASN1Encodable aSN1Encodable = readObject();
/* 234 */     if (null == aSN1Encodable)
/*     */     {
/* 236 */       return new ASN1EncodableVector(0);
/*     */     }
/*     */     
/* 239 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
/*     */     
/*     */     while (true) {
/* 242 */       if (aSN1Encodable instanceof InMemoryRepresentable) {
/*     */         
/* 244 */         aSN1EncodableVector.add((ASN1Encodable)((InMemoryRepresentable)aSN1Encodable).getLoadedObject());
/*     */       }
/*     */       else {
/*     */         
/* 248 */         aSN1EncodableVector.add((ASN1Encodable)aSN1Encodable.toASN1Primitive());
/*     */       } 
/*     */       
/* 251 */       if ((aSN1Encodable = readObject()) == null)
/* 252 */         return aSN1EncodableVector; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1StreamParser.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */