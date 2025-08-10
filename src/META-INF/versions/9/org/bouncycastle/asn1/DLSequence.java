/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ 
/*     */ public class DLSequence extends ASN1Sequence {
/*  11 */   private int bodyLength = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DLSequence() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DLSequence(ASN1Encodable paramASN1Encodable) {
/*  26 */     super(paramASN1Encodable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DLSequence(ASN1EncodableVector paramASN1EncodableVector) {
/*  35 */     super(paramASN1EncodableVector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DLSequence(ASN1Encodable[] paramArrayOfASN1Encodable) {
/*  44 */     super(paramArrayOfASN1Encodable);
/*     */   }
/*     */ 
/*     */   
/*     */   DLSequence(ASN1Encodable[] paramArrayOfASN1Encodable, boolean paramBoolean) {
/*  49 */     super(paramArrayOfASN1Encodable, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   private int getBodyLength() throws IOException {
/*  54 */     if (this.bodyLength < 0) {
/*     */       
/*  56 */       int i = this.elements.length;
/*  57 */       int j = 0;
/*     */       
/*  59 */       for (byte b = 0; b < i; b++) {
/*     */         
/*  61 */         ASN1Primitive aSN1Primitive = this.elements[b].toASN1Primitive().toDLObject();
/*  62 */         j += aSN1Primitive.encodedLength();
/*     */       } 
/*     */       
/*  65 */       this.bodyLength = j;
/*     */     } 
/*     */     
/*  68 */     return this.bodyLength;
/*     */   }
/*     */ 
/*     */   
/*     */   int encodedLength() throws IOException {
/*  73 */     int i = getBodyLength();
/*     */     
/*  75 */     return 1 + StreamUtil.calculateBodyLength(i) + i;
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
/*  88 */     if (paramBoolean)
/*     */     {
/*  90 */       paramASN1OutputStream.write(48);
/*     */     }
/*     */     
/*  93 */     ASN1OutputStream aSN1OutputStream = paramASN1OutputStream.getDLSubStream();
/*     */     
/*  95 */     int i = this.elements.length;
/*  96 */     if (this.bodyLength >= 0 || i > 16) {
/*     */       
/*  98 */       paramASN1OutputStream.writeLength(getBodyLength());
/*     */       
/* 100 */       for (byte b = 0; b < i; b++)
/*     */       {
/* 102 */         aSN1OutputStream.writePrimitive(this.elements[b].toASN1Primitive(), true);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 107 */       int j = 0;
/*     */       
/* 109 */       ASN1Primitive[] arrayOfASN1Primitive = new ASN1Primitive[i]; byte b;
/* 110 */       for (b = 0; b < i; b++) {
/*     */         
/* 112 */         ASN1Primitive aSN1Primitive = this.elements[b].toASN1Primitive().toDLObject();
/* 113 */         arrayOfASN1Primitive[b] = aSN1Primitive;
/* 114 */         j += aSN1Primitive.encodedLength();
/*     */       } 
/*     */       
/* 117 */       this.bodyLength = j;
/* 118 */       paramASN1OutputStream.writeLength(j);
/*     */       
/* 120 */       for (b = 0; b < i; b++)
/*     */       {
/* 122 */         aSN1OutputStream.writePrimitive(arrayOfASN1Primitive[b], true);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDLObject() {
/* 129 */     return (ASN1Primitive)this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/DLSequence.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */