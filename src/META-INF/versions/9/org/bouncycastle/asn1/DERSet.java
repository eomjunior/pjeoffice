/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Set;
/*     */ import org.bouncycastle.asn1.DEROutputStream;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DERSet
/*     */   extends ASN1Set
/*     */ {
/*     */   public static org.bouncycastle.asn1.DERSet convert(ASN1Set paramASN1Set) {
/*  20 */     return (org.bouncycastle.asn1.DERSet)paramASN1Set.toDERObject();
/*     */   }
/*     */   
/*  23 */   private int bodyLength = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERSet() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERSet(ASN1Encodable paramASN1Encodable) {
/*  38 */     super(paramASN1Encodable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERSet(ASN1EncodableVector paramASN1EncodableVector) {
/*  47 */     super(paramASN1EncodableVector, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERSet(ASN1Encodable[] paramArrayOfASN1Encodable) {
/*  56 */     super(paramArrayOfASN1Encodable, true);
/*     */   }
/*     */ 
/*     */   
/*     */   DERSet(boolean paramBoolean, ASN1Encodable[] paramArrayOfASN1Encodable) {
/*  61 */     super(checkSorted(paramBoolean), paramArrayOfASN1Encodable);
/*     */   }
/*     */ 
/*     */   
/*     */   private int getBodyLength() throws IOException {
/*  66 */     if (this.bodyLength < 0) {
/*     */       
/*  68 */       int i = this.elements.length;
/*  69 */       int j = 0;
/*     */       
/*  71 */       for (byte b = 0; b < i; b++) {
/*     */         
/*  73 */         ASN1Primitive aSN1Primitive = this.elements[b].toASN1Primitive().toDERObject();
/*  74 */         j += aSN1Primitive.encodedLength();
/*     */       } 
/*     */       
/*  77 */       this.bodyLength = j;
/*     */     } 
/*     */     
/*  80 */     return this.bodyLength;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() throws IOException {
/*  85 */     int i = getBodyLength();
/*     */     
/*  87 */     return 1 + StreamUtil.calculateBodyLength(i) + i;
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
/* 100 */     if (paramBoolean)
/*     */     {
/* 102 */       paramASN1OutputStream.write(49);
/*     */     }
/*     */     
/* 105 */     DEROutputStream dEROutputStream = paramASN1OutputStream.getDERSubStream();
/*     */     
/* 107 */     int i = this.elements.length;
/* 108 */     if (this.bodyLength >= 0 || i > 16) {
/*     */       
/* 110 */       paramASN1OutputStream.writeLength(getBodyLength());
/*     */       
/* 112 */       for (byte b = 0; b < i; b++)
/*     */       {
/* 114 */         ASN1Primitive aSN1Primitive = this.elements[b].toASN1Primitive().toDERObject();
/* 115 */         aSN1Primitive.encode((ASN1OutputStream)dEROutputStream, true);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 120 */       int j = 0;
/*     */       
/* 122 */       ASN1Primitive[] arrayOfASN1Primitive = new ASN1Primitive[i]; byte b;
/* 123 */       for (b = 0; b < i; b++) {
/*     */         
/* 125 */         ASN1Primitive aSN1Primitive = this.elements[b].toASN1Primitive().toDERObject();
/* 126 */         arrayOfASN1Primitive[b] = aSN1Primitive;
/* 127 */         j += aSN1Primitive.encodedLength();
/*     */       } 
/*     */       
/* 130 */       this.bodyLength = j;
/* 131 */       paramASN1OutputStream.writeLength(j);
/*     */       
/* 133 */       for (b = 0; b < i; b++)
/*     */       {
/* 135 */         arrayOfASN1Primitive[b].encode((ASN1OutputStream)dEROutputStream, true);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDERObject() {
/* 142 */     return this.isSorted ? (ASN1Primitive)this : super.toDERObject();
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDLObject() {
/* 147 */     return (ASN1Primitive)this;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean checkSorted(boolean paramBoolean) {
/* 152 */     if (!paramBoolean)
/*     */     {
/* 154 */       throw new IllegalStateException("DERSet elements should always be in sorted order");
/*     */     }
/* 156 */     return paramBoolean;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DERSet.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */