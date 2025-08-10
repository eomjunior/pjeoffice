/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Set;
/*     */ import org.bouncycastle.asn1.StreamUtil;
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
/*     */ public class DLSet
/*     */   extends ASN1Set
/*     */ {
/*  56 */   private int bodyLength = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DLSet() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DLSet(ASN1Encodable paramASN1Encodable) {
/*  70 */     super(paramASN1Encodable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DLSet(ASN1EncodableVector paramASN1EncodableVector) {
/*  78 */     super(paramASN1EncodableVector, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DLSet(ASN1Encodable[] paramArrayOfASN1Encodable) {
/*  86 */     super(paramArrayOfASN1Encodable, false);
/*     */   }
/*     */ 
/*     */   
/*     */   DLSet(boolean paramBoolean, ASN1Encodable[] paramArrayOfASN1Encodable) {
/*  91 */     super(paramBoolean, paramArrayOfASN1Encodable);
/*     */   }
/*     */ 
/*     */   
/*     */   private int getBodyLength() throws IOException {
/*  96 */     if (this.bodyLength < 0) {
/*     */       
/*  98 */       int i = this.elements.length;
/*  99 */       int j = 0;
/*     */       
/* 101 */       for (byte b = 0; b < i; b++) {
/*     */         
/* 103 */         ASN1Primitive aSN1Primitive = this.elements[b].toASN1Primitive().toDLObject();
/* 104 */         j += aSN1Primitive.encodedLength();
/*     */       } 
/*     */       
/* 107 */       this.bodyLength = j;
/*     */     } 
/*     */     
/* 110 */     return this.bodyLength;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() throws IOException {
/* 115 */     int i = getBodyLength();
/*     */     
/* 117 */     return 1 + StreamUtil.calculateBodyLength(i) + i;
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
/* 130 */     if (paramBoolean)
/*     */     {
/* 132 */       paramASN1OutputStream.write(49);
/*     */     }
/*     */     
/* 135 */     ASN1OutputStream aSN1OutputStream = paramASN1OutputStream.getDLSubStream();
/*     */     
/* 137 */     int i = this.elements.length;
/* 138 */     if (this.bodyLength >= 0 || i > 16) {
/*     */       
/* 140 */       paramASN1OutputStream.writeLength(getBodyLength());
/*     */       
/* 142 */       for (byte b = 0; b < i; b++)
/*     */       {
/* 144 */         aSN1OutputStream.writePrimitive(this.elements[b].toASN1Primitive(), true);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 149 */       int j = 0;
/*     */       
/* 151 */       ASN1Primitive[] arrayOfASN1Primitive = new ASN1Primitive[i]; byte b;
/* 152 */       for (b = 0; b < i; b++) {
/*     */         
/* 154 */         ASN1Primitive aSN1Primitive = this.elements[b].toASN1Primitive().toDLObject();
/* 155 */         arrayOfASN1Primitive[b] = aSN1Primitive;
/* 156 */         j += aSN1Primitive.encodedLength();
/*     */       } 
/*     */       
/* 159 */       this.bodyLength = j;
/* 160 */       paramASN1OutputStream.writeLength(j);
/*     */       
/* 162 */       for (b = 0; b < i; b++)
/*     */       {
/* 164 */         aSN1OutputStream.writePrimitive(arrayOfASN1Primitive[b], true);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDLObject() {
/* 171 */     return (ASN1Primitive)this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DLSet.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */