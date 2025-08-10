/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1Exception;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.ASN1Set;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.BEROctetString;
/*     */ import org.bouncycastle.asn1.BERSequence;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ 
/*     */ 
/*     */ public class BERTaggedObject
/*     */   extends ASN1TaggedObject
/*     */ {
/*     */   public BERTaggedObject(int paramInt, ASN1Encodable paramASN1Encodable) {
/*  22 */     super(true, paramInt, paramASN1Encodable);
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
/*     */   public BERTaggedObject(boolean paramBoolean, int paramInt, ASN1Encodable paramASN1Encodable) {
/*  35 */     super(paramBoolean, paramInt, paramASN1Encodable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BERTaggedObject(int paramInt) {
/*  45 */     super(false, paramInt, (ASN1Encodable)new BERSequence());
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/*  50 */     return (this.explicit || this.obj.toASN1Primitive().isConstructed());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int encodedLength() throws IOException {
/*  56 */     ASN1Primitive aSN1Primitive = this.obj.toASN1Primitive();
/*  57 */     int i = aSN1Primitive.encodedLength();
/*     */     
/*  59 */     if (this.explicit)
/*     */     {
/*  61 */       return StreamUtil.calculateTagLength(this.tagNo) + StreamUtil.calculateBodyLength(i) + i;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  66 */     i--;
/*     */     
/*  68 */     return StreamUtil.calculateTagLength(this.tagNo) + i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/*  74 */     paramASN1OutputStream.writeTag(paramBoolean, 160, this.tagNo);
/*  75 */     paramASN1OutputStream.write(128);
/*     */     
/*  77 */     if (!this.explicit) {
/*     */       Enumeration enumeration;
/*     */       
/*  80 */       if (this.obj instanceof ASN1OctetString) {
/*     */         
/*  82 */         if (this.obj instanceof BEROctetString)
/*     */         {
/*  84 */           enumeration = ((BEROctetString)this.obj).getObjects();
/*     */         }
/*     */         else
/*     */         {
/*  88 */           ASN1OctetString aSN1OctetString = (ASN1OctetString)this.obj;
/*  89 */           BEROctetString bEROctetString = new BEROctetString(aSN1OctetString.getOctets());
/*  90 */           enumeration = bEROctetString.getObjects();
/*     */         }
/*     */       
/*  93 */       } else if (this.obj instanceof ASN1Sequence) {
/*     */         
/*  95 */         enumeration = ((ASN1Sequence)this.obj).getObjects();
/*     */       }
/*  97 */       else if (this.obj instanceof ASN1Set) {
/*     */         
/*  99 */         enumeration = ((ASN1Set)this.obj).getObjects();
/*     */       }
/*     */       else {
/*     */         
/* 103 */         throw new ASN1Exception("not implemented: " + this.obj.getClass().getName());
/*     */       } 
/*     */       
/* 106 */       paramASN1OutputStream.writeElements(enumeration);
/*     */     }
/*     */     else {
/*     */       
/* 110 */       paramASN1OutputStream.writePrimitive(this.obj.toASN1Primitive(), true);
/*     */     } 
/*     */     
/* 113 */     paramASN1OutputStream.write(0);
/* 114 */     paramASN1OutputStream.write(0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/BERTaggedObject.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */