/*    */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.spec;
/*    */ 
/*    */ import java.security.spec.AlgorithmParameterSpec;
/*    */ import org.bouncycastle.pqc.crypto.qtesla.QTESLASecurityCategory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QTESLAParameterSpec
/*    */   implements AlgorithmParameterSpec
/*    */ {
/* 17 */   public static final String PROVABLY_SECURE_I = QTESLASecurityCategory.getName(5);
/* 18 */   public static final String PROVABLY_SECURE_III = QTESLASecurityCategory.getName(6);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private String securityCategory;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public QTESLAParameterSpec(String paramString) {
/* 29 */     this.securityCategory = paramString;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSecurityCategory() {
/* 39 */     return this.securityCategory;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/spec/QTESLAParameterSpec.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */