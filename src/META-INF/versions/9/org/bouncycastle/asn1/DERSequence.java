/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.DEROutputStream;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ 
/*     */ public class DERSequence
/*     */   extends ASN1Sequence
/*     */ {
/*     */   public static org.bouncycastle.asn1.DERSequence convert(ASN1Sequence paramASN1Sequence) {
/*  16 */     return (org.bouncycastle.asn1.DERSequence)paramASN1Sequence.toDERObject();
/*     */   }
/*     */   
/*  19 */   private int bodyLength = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERSequence() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERSequence(ASN1Encodable paramASN1Encodable) {
/*  34 */     super(paramASN1Encodable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERSequence(ASN1EncodableVector paramASN1EncodableVector) {
/*  43 */     super(paramASN1EncodableVector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERSequence(ASN1Encodable[] paramArrayOfASN1Encodable) {
/*  52 */     super(paramArrayOfASN1Encodable);
/*     */   }
/*     */ 
/*     */   
/*     */   DERSequence(ASN1Encodable[] paramArrayOfASN1Encodable, boolean paramBoolean) {
/*  57 */     super(paramArrayOfASN1Encodable, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   private int getBodyLength() throws IOException {
/*  62 */     if (this.bodyLength < 0) {
/*     */       
/*  64 */       int i = this.elements.length;
/*  65 */       int j = 0;
/*     */       
/*  67 */       for (byte b = 0; b < i; b++) {
/*     */         
/*  69 */         ASN1Primitive aSN1Primitive = this.elements[b].toASN1Primitive().toDERObject();
/*  70 */         j += aSN1Primitive.encodedLength();
/*     */       } 
/*     */       
/*  73 */       this.bodyLength = j;
/*     */     } 
/*     */     
/*  76 */     return this.bodyLength;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() throws IOException {
/*  81 */     int i = getBodyLength();
/*     */     
/*  83 */     return 1 + StreamUtil.calculateBodyLength(i) + i;
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
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/*  96 */     if (paramBoolean)
/*     */     {
/*  98 */       paramASN1OutputStream.write(48);
/*     */     }
/*     */     
/* 101 */     DEROutputStream dEROutputStream = paramASN1OutputStream.getDERSubStream();
/*     */     
/* 103 */     int i = this.elements.length;
/* 104 */     if (this.bodyLength >= 0 || i > 16) {
/*     */       
/* 106 */       paramASN1OutputStream.writeLength(getBodyLength());
/*     */       
/* 108 */       for (byte b = 0; b < i; b++)
/*     */       {
/* 110 */         ASN1Primitive aSN1Primitive = this.elements[b].toASN1Primitive().toDERObject();
/* 111 */         aSN1Primitive.encode((ASN1OutputStream)dEROutputStream, true);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 116 */       int j = 0;
/*     */       
/* 118 */       ASN1Primitive[] arrayOfASN1Primitive = new ASN1Primitive[i]; byte b;
/* 119 */       for (b = 0; b < i; b++) {
/*     */         
/* 121 */         ASN1Primitive aSN1Primitive = this.elements[b].toASN1Primitive().toDERObject();
/* 122 */         arrayOfASN1Primitive[b] = aSN1Primitive;
/* 123 */         j += aSN1Primitive.encodedLength();
/*     */       } 
/*     */       
/* 126 */       this.bodyLength = j;
/* 127 */       paramASN1OutputStream.writeLength(j);
/*     */       
/* 129 */       for (b = 0; b < i; b++)
/*     */       {
/* 131 */         arrayOfASN1Primitive[b].encode((ASN1OutputStream)dEROutputStream, true);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDERObject() {
/* 138 */     return (ASN1Primitive)this;
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDLObject() {
/* 143 */     return (ASN1Primitive)this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERSequence.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */