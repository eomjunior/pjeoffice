/*     */ package META-INF.versions.9.org.bouncycastle.asn1.x9;
/*     */ 
/*     */ import org.bouncycastle.asn1.ASN1Choice;
/*     */ import org.bouncycastle.asn1.ASN1Null;
/*     */ import org.bouncycastle.asn1.ASN1Object;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.x9.X9ECParameters;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class X962Parameters
/*     */   extends ASN1Object
/*     */   implements ASN1Choice
/*     */ {
/*  17 */   private ASN1Primitive params = null;
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.asn1.x9.X962Parameters getInstance(Object paramObject) {
/*  22 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.x9.X962Parameters)
/*     */     {
/*  24 */       return (org.bouncycastle.asn1.x9.X962Parameters)paramObject;
/*     */     }
/*     */     
/*  27 */     if (paramObject instanceof ASN1Primitive)
/*     */     {
/*  29 */       return new org.bouncycastle.asn1.x9.X962Parameters((ASN1Primitive)paramObject);
/*     */     }
/*     */     
/*  32 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  36 */         return new org.bouncycastle.asn1.x9.X962Parameters(ASN1Primitive.fromByteArray((byte[])paramObject));
/*     */       }
/*  38 */       catch (Exception exception) {
/*     */         
/*  40 */         throw new IllegalArgumentException("unable to parse encoded data: " + exception.getMessage());
/*     */       } 
/*     */     }
/*     */     
/*  44 */     throw new IllegalArgumentException("unknown object in getInstance()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.asn1.x9.X962Parameters getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  51 */     return getInstance(paramASN1TaggedObject.getObject());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public X962Parameters(X9ECParameters paramX9ECParameters) {
/*  57 */     this.params = paramX9ECParameters.toASN1Primitive();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public X962Parameters(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/*  63 */     this.params = (ASN1Primitive)paramASN1ObjectIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public X962Parameters(ASN1Null paramASN1Null) {
/*  69 */     this.params = (ASN1Primitive)paramASN1Null;
/*     */   }
/*     */ 
/*     */   
/*     */   private X962Parameters(ASN1Primitive paramASN1Primitive) {
/*  74 */     this.params = paramASN1Primitive;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNamedCurve() {
/*  79 */     return this.params instanceof ASN1ObjectIdentifier;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isImplicitlyCA() {
/*  84 */     return this.params instanceof ASN1Null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1Primitive getParameters() {
/*  89 */     return this.params;
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
/*     */ 
/*     */   
/*     */   public ASN1Primitive toASN1Primitive() {
/* 104 */     return this.params;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/x9/X962Parameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */