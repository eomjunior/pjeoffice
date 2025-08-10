/*    */ package META-INF.versions.9.org.bouncycastle.asn1.x9;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.asn1.ASN1Object;
/*    */ import org.bouncycastle.asn1.ASN1OctetString;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.DEROctetString;
/*    */ import org.bouncycastle.asn1.x9.X9IntegerConverter;
/*    */ import org.bouncycastle.math.ec.ECFieldElement;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class X9FieldElement
/*    */   extends ASN1Object
/*    */ {
/*    */   protected ECFieldElement f;
/* 19 */   private static X9IntegerConverter converter = new X9IntegerConverter();
/*    */ 
/*    */   
/*    */   public X9FieldElement(ECFieldElement paramECFieldElement) {
/* 23 */     this.f = paramECFieldElement;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public X9FieldElement(BigInteger paramBigInteger, ASN1OctetString paramASN1OctetString) {
/* 31 */     this((ECFieldElement)new ECFieldElement.Fp(paramBigInteger, new BigInteger(1, paramASN1OctetString.getOctets())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public X9FieldElement(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ASN1OctetString paramASN1OctetString) {
/* 39 */     this((ECFieldElement)new ECFieldElement.F2m(paramInt1, paramInt2, paramInt3, paramInt4, new BigInteger(1, paramASN1OctetString.getOctets())));
/*    */   }
/*    */ 
/*    */   
/*    */   public ECFieldElement getValue() {
/* 44 */     return this.f;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ASN1Primitive toASN1Primitive() {
/* 65 */     int i = converter.getByteLength(this.f);
/* 66 */     byte[] arrayOfByte = converter.integerToBytes(this.f.toBigInteger(), i);
/*    */     
/* 68 */     return (ASN1Primitive)new DEROctetString(arrayOfByte);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/x9/X9FieldElement.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */