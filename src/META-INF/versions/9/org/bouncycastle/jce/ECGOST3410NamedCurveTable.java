/*    */ package META-INF.versions.9.org.bouncycastle.jce;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*    */ import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
/*    */ import org.bouncycastle.asn1.x9.X9ECParameters;
/*    */ import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
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
/*    */ public class ECGOST3410NamedCurveTable
/*    */ {
/*    */   public static ECNamedCurveParameterSpec getParameterSpec(String paramString) {
/* 25 */     X9ECParameters x9ECParameters = ECGOST3410NamedCurves.getByNameX9(paramString);
/* 26 */     if (x9ECParameters == null) {
/*    */       
/*    */       try {
/*    */         
/* 30 */         x9ECParameters = ECGOST3410NamedCurves.getByOIDX9(new ASN1ObjectIdentifier(paramString));
/*    */       }
/* 32 */       catch (IllegalArgumentException illegalArgumentException) {
/*    */         
/* 34 */         return null;
/*    */       } 
/*    */     }
/*    */     
/* 38 */     if (x9ECParameters == null)
/*    */     {
/* 40 */       return null;
/*    */     }
/*    */     
/* 43 */     return new ECNamedCurveParameterSpec(paramString, x9ECParameters
/*    */         
/* 45 */         .getCurve(), x9ECParameters
/* 46 */         .getG(), x9ECParameters
/* 47 */         .getN(), x9ECParameters
/* 48 */         .getH(), x9ECParameters
/* 49 */         .getSeed());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Enumeration getNames() {
/* 59 */     return ECGOST3410NamedCurves.getNames();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/jce/ECGOST3410NamedCurveTable.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */