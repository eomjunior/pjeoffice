/*    */ package META-INF.versions.9.org.bouncycastle.asn1.x9;
/*    */ 
/*    */ import org.bouncycastle.asn1.ASN1Object;
/*    */ import org.bouncycastle.asn1.ASN1OctetString;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.DEROctetString;
/*    */ import org.bouncycastle.math.ec.ECCurve;
/*    */ import org.bouncycastle.math.ec.ECPoint;
/*    */ import org.bouncycastle.util.Arrays;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class X9ECPoint
/*    */   extends ASN1Object
/*    */ {
/*    */   private final ASN1OctetString encoding;
/*    */   private ECCurve c;
/*    */   private ECPoint p;
/*    */   
/*    */   public X9ECPoint(ECPoint paramECPoint, boolean paramBoolean) {
/* 26 */     this.p = paramECPoint.normalize();
/* 27 */     this.encoding = (ASN1OctetString)new DEROctetString(paramECPoint.getEncoded(paramBoolean));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public X9ECPoint(ECCurve paramECCurve, byte[] paramArrayOfbyte) {
/* 34 */     this.c = paramECCurve;
/* 35 */     this.encoding = (ASN1OctetString)new DEROctetString(Arrays.clone(paramArrayOfbyte));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public X9ECPoint(ECCurve paramECCurve, ASN1OctetString paramASN1OctetString) {
/* 42 */     this(paramECCurve, paramASN1OctetString.getOctets());
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getPointEncoding() {
/* 47 */     return Arrays.clone(this.encoding.getOctets());
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized ECPoint getPoint() {
/* 52 */     if (this.p == null)
/*    */     {
/* 54 */       this.p = this.c.decodePoint(this.encoding.getOctets()).normalize();
/*    */     }
/*    */     
/* 57 */     return this.p;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPointCompressed() {
/* 62 */     byte[] arrayOfByte = this.encoding.getOctets();
/* 63 */     return (arrayOfByte != null && arrayOfByte.length > 0 && (arrayOfByte[0] == 2 || arrayOfByte[0] == 3));
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
/*    */   public ASN1Primitive toASN1Primitive() {
/* 76 */     return (ASN1Primitive)this.encoding;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/x9/X9ECPoint.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */