/*    */ package META-INF.versions.9.org.bouncycastle.asn1.rosstandart;
/*    */ 
/*    */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*    */ 
/*    */ public interface RosstandartObjectIdentifiers
/*    */ {
/*  7 */   public static final ASN1ObjectIdentifier rosstandart = new ASN1ObjectIdentifier("1.2.643.7");
/*    */   
/*  9 */   public static final ASN1ObjectIdentifier id_tc26 = rosstandart.branch("1");
/*    */   
/* 11 */   public static final ASN1ObjectIdentifier id_tc26_gost_3411_12_256 = id_tc26.branch("1.2.2");
/*    */   
/* 13 */   public static final ASN1ObjectIdentifier id_tc26_gost_3411_12_512 = id_tc26.branch("1.2.3");
/*    */   
/* 15 */   public static final ASN1ObjectIdentifier id_tc26_hmac_gost_3411_12_256 = id_tc26.branch("1.4.1");
/*    */   
/* 17 */   public static final ASN1ObjectIdentifier id_tc26_hmac_gost_3411_12_512 = id_tc26.branch("1.4.2");
/*    */   
/* 19 */   public static final ASN1ObjectIdentifier id_tc26_gost_3410_12_256 = id_tc26.branch("1.1.1");
/*    */   
/* 21 */   public static final ASN1ObjectIdentifier id_tc26_gost_3410_12_512 = id_tc26.branch("1.1.2");
/*    */   
/* 23 */   public static final ASN1ObjectIdentifier id_tc26_signwithdigest_gost_3410_12_256 = id_tc26.branch("1.3.2");
/*    */   
/* 25 */   public static final ASN1ObjectIdentifier id_tc26_signwithdigest_gost_3410_12_512 = id_tc26.branch("1.3.3");
/*    */   
/* 27 */   public static final ASN1ObjectIdentifier id_tc26_agreement = id_tc26.branch("1.6");
/*    */   
/* 29 */   public static final ASN1ObjectIdentifier id_tc26_agreement_gost_3410_12_256 = id_tc26_agreement.branch("1");
/*    */   
/* 31 */   public static final ASN1ObjectIdentifier id_tc26_agreement_gost_3410_12_512 = id_tc26_agreement.branch("2");
/*    */   
/* 33 */   public static final ASN1ObjectIdentifier id_tc26_gost_3410_12_256_paramSet = id_tc26.branch("2.1.1");
/*    */   
/* 35 */   public static final ASN1ObjectIdentifier id_tc26_gost_3410_12_256_paramSetA = id_tc26_gost_3410_12_256_paramSet.branch("1");
/*    */   
/* 37 */   public static final ASN1ObjectIdentifier id_tc26_gost_3410_12_512_paramSet = id_tc26.branch("2.1.2");
/*    */   
/* 39 */   public static final ASN1ObjectIdentifier id_tc26_gost_3410_12_512_paramSetA = id_tc26_gost_3410_12_512_paramSet.branch("1");
/*    */   
/* 41 */   public static final ASN1ObjectIdentifier id_tc26_gost_3410_12_512_paramSetB = id_tc26_gost_3410_12_512_paramSet.branch("2");
/*    */   
/* 43 */   public static final ASN1ObjectIdentifier id_tc26_gost_3410_12_512_paramSetC = id_tc26_gost_3410_12_512_paramSet.branch("3");
/*    */   
/* 45 */   public static final ASN1ObjectIdentifier id_tc26_gost_28147_param_Z = id_tc26.branch("2.5.1.1");
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/rosstandart/RosstandartObjectIdentifiers.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */