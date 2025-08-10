/*    */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.spec;
/*    */ 
/*    */ import java.security.spec.AlgorithmParameterSpec;
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
/*    */ 
/*    */ 
/*    */ public class XMSSParameterSpec
/*    */   implements AlgorithmParameterSpec
/*    */ {
/*    */   public static final String SHA256 = "SHA256";
/*    */   public static final String SHA512 = "SHA512";
/*    */   public static final String SHAKE128 = "SHAKE128";
/*    */   public static final String SHAKE256 = "SHAKE256";
/* 31 */   public static final org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec SHA2_10_256 = new org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec(10, "SHA256");
/* 32 */   public static final org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec SHA2_16_256 = new org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec(16, "SHA256");
/* 33 */   public static final org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec SHA2_20_256 = new org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec(20, "SHA256");
/* 34 */   public static final org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec SHAKE_10_256 = new org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec(10, "SHAKE128");
/* 35 */   public static final org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec SHAKE_16_256 = new org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec(16, "SHAKE128");
/* 36 */   public static final org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec SHAKE_20_256 = new org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec(20, "SHAKE128");
/*    */   
/* 38 */   public static final org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec SHA2_10_512 = new org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec(10, "SHA512");
/* 39 */   public static final org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec SHA2_16_512 = new org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec(16, "SHA512");
/* 40 */   public static final org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec SHA2_20_512 = new org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec(20, "SHA512");
/* 41 */   public static final org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec SHAKE_10_512 = new org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec(10, "SHAKE256");
/* 42 */   public static final org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec SHAKE_16_512 = new org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec(16, "SHAKE256");
/* 43 */   public static final org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec SHAKE_20_512 = new org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec(20, "SHAKE256");
/*    */   
/*    */   private final int height;
/*    */   
/*    */   private final String treeDigest;
/*    */   
/*    */   public XMSSParameterSpec(int paramInt, String paramString) {
/* 50 */     this.height = paramInt;
/* 51 */     this.treeDigest = paramString;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTreeDigest() {
/* 56 */     return this.treeDigest;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 61 */     return this.height;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/spec/XMSSParameterSpec.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */