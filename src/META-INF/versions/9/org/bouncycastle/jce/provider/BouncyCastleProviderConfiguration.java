/*     */ package META-INF.versions.9.org.bouncycastle.jce.provider;
/*     */ 
/*     */ import java.security.Permission;
/*     */ import java.security.spec.DSAParameterSpec;
/*     */ import java.security.spec.ECParameterSpec;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.crypto.spec.DHParameterSpec;
/*     */ import org.bouncycastle.crypto.CryptoServicesRegistrar;
/*     */ import org.bouncycastle.crypto.params.DHParameters;
/*     */ import org.bouncycastle.crypto.params.DSAParameters;
/*     */ import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
/*     */ import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
/*     */ import org.bouncycastle.jcajce.provider.config.ProviderConfigurationPermission;
/*     */ import org.bouncycastle.jcajce.spec.DHDomainParameterSpec;
/*     */ import org.bouncycastle.jce.spec.ECParameterSpec;
/*     */ 
/*     */ 
/*     */ 
/*     */ class BouncyCastleProviderConfiguration
/*     */   implements ProviderConfiguration
/*     */ {
/*  26 */   private static Permission BC_EC_LOCAL_PERMISSION = (Permission)new ProviderConfigurationPermission("BC", "threadLocalEcImplicitlyCa");
/*     */   
/*  28 */   private static Permission BC_EC_PERMISSION = (Permission)new ProviderConfigurationPermission("BC", "ecImplicitlyCa");
/*     */   
/*  30 */   private static Permission BC_DH_LOCAL_PERMISSION = (Permission)new ProviderConfigurationPermission("BC", "threadLocalDhDefaultParams");
/*     */   
/*  32 */   private static Permission BC_DH_PERMISSION = (Permission)new ProviderConfigurationPermission("BC", "DhDefaultParams");
/*     */   
/*  34 */   private static Permission BC_EC_CURVE_PERMISSION = (Permission)new ProviderConfigurationPermission("BC", "acceptableEcCurves");
/*     */   
/*  36 */   private static Permission BC_ADDITIONAL_EC_CURVE_PERMISSION = (Permission)new ProviderConfigurationPermission("BC", "additionalEcParameters");
/*     */ 
/*     */   
/*  39 */   private ThreadLocal ecThreadSpec = new ThreadLocal();
/*  40 */   private ThreadLocal dhThreadSpec = new ThreadLocal();
/*     */   
/*     */   private volatile ECParameterSpec ecImplicitCaParams;
/*     */   private volatile Object dhDefaultParams;
/*  44 */   private volatile Set acceptableNamedCurves = new HashSet();
/*  45 */   private volatile Map additionalECParameters = new HashMap<>();
/*     */ 
/*     */   
/*     */   void setParameter(String paramString, Object paramObject) {
/*  49 */     SecurityManager securityManager = System.getSecurityManager();
/*     */     
/*  51 */     if (paramString.equals("threadLocalEcImplicitlyCa")) {
/*     */       ECParameterSpec eCParameterSpec;
/*     */ 
/*     */       
/*  55 */       if (securityManager != null)
/*     */       {
/*  57 */         securityManager.checkPermission(BC_EC_LOCAL_PERMISSION);
/*     */       }
/*     */       
/*  60 */       if (paramObject instanceof ECParameterSpec || paramObject == null) {
/*     */         
/*  62 */         eCParameterSpec = (ECParameterSpec)paramObject;
/*     */       }
/*     */       else {
/*     */         
/*  66 */         eCParameterSpec = EC5Util.convertSpec((ECParameterSpec)paramObject);
/*     */       } 
/*     */       
/*  69 */       if (eCParameterSpec == null)
/*     */       {
/*  71 */         this.ecThreadSpec.remove();
/*     */       }
/*     */       else
/*     */       {
/*  75 */         this.ecThreadSpec.set(eCParameterSpec);
/*     */       }
/*     */     
/*  78 */     } else if (paramString.equals("ecImplicitlyCa")) {
/*     */       
/*  80 */       if (securityManager != null)
/*     */       {
/*  82 */         securityManager.checkPermission(BC_EC_PERMISSION);
/*     */       }
/*     */       
/*  85 */       if (paramObject instanceof ECParameterSpec || paramObject == null)
/*     */       {
/*  87 */         this.ecImplicitCaParams = (ECParameterSpec)paramObject;
/*     */       }
/*     */       else
/*     */       {
/*  91 */         this.ecImplicitCaParams = EC5Util.convertSpec((ECParameterSpec)paramObject);
/*     */       }
/*     */     
/*  94 */     } else if (paramString.equals("threadLocalDhDefaultParams")) {
/*     */       Object object;
/*     */ 
/*     */       
/*  98 */       if (securityManager != null)
/*     */       {
/* 100 */         securityManager.checkPermission(BC_DH_LOCAL_PERMISSION);
/*     */       }
/*     */       
/* 103 */       if (paramObject instanceof DHParameterSpec || paramObject instanceof DHParameterSpec[] || paramObject == null) {
/*     */         
/* 105 */         object = paramObject;
/*     */       }
/*     */       else {
/*     */         
/* 109 */         throw new IllegalArgumentException("not a valid DHParameterSpec");
/*     */       } 
/*     */       
/* 112 */       if (object == null)
/*     */       {
/* 114 */         this.dhThreadSpec.remove();
/*     */       }
/*     */       else
/*     */       {
/* 118 */         this.dhThreadSpec.set(object);
/*     */       }
/*     */     
/* 121 */     } else if (paramString.equals("DhDefaultParams")) {
/*     */       
/* 123 */       if (securityManager != null)
/*     */       {
/* 125 */         securityManager.checkPermission(BC_DH_PERMISSION);
/*     */       }
/*     */       
/* 128 */       if (paramObject instanceof DHParameterSpec || paramObject instanceof DHParameterSpec[] || paramObject == null)
/*     */       {
/* 130 */         this.dhDefaultParams = paramObject;
/*     */       }
/*     */       else
/*     */       {
/* 134 */         throw new IllegalArgumentException("not a valid DHParameterSpec or DHParameterSpec[]");
/*     */       }
/*     */     
/* 137 */     } else if (paramString.equals("acceptableEcCurves")) {
/*     */       
/* 139 */       if (securityManager != null)
/*     */       {
/* 141 */         securityManager.checkPermission(BC_EC_CURVE_PERMISSION);
/*     */       }
/*     */       
/* 144 */       this.acceptableNamedCurves = (Set)paramObject;
/*     */     }
/* 146 */     else if (paramString.equals("additionalEcParameters")) {
/*     */       
/* 148 */       if (securityManager != null)
/*     */       {
/* 150 */         securityManager.checkPermission(BC_ADDITIONAL_EC_CURVE_PERMISSION);
/*     */       }
/*     */       
/* 153 */       this.additionalECParameters = (Map)paramObject;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ECParameterSpec getEcImplicitlyCa() {
/* 159 */     ECParameterSpec eCParameterSpec = this.ecThreadSpec.get();
/*     */     
/* 161 */     if (eCParameterSpec != null)
/*     */     {
/* 163 */       return eCParameterSpec;
/*     */     }
/*     */     
/* 166 */     return this.ecImplicitCaParams;
/*     */   }
/*     */ 
/*     */   
/*     */   public DHParameterSpec getDHDefaultParameters(int paramInt) {
/* 171 */     Object object = this.dhThreadSpec.get();
/* 172 */     if (object == null)
/*     */     {
/* 174 */       object = this.dhDefaultParams;
/*     */     }
/*     */     
/* 177 */     if (object instanceof DHParameterSpec) {
/*     */       
/* 179 */       DHParameterSpec dHParameterSpec = (DHParameterSpec)object;
/*     */       
/* 181 */       if (dHParameterSpec.getP().bitLength() == paramInt)
/*     */       {
/* 183 */         return dHParameterSpec;
/*     */       }
/*     */     }
/* 186 */     else if (object instanceof DHParameterSpec[]) {
/*     */       
/* 188 */       DHParameterSpec[] arrayOfDHParameterSpec = (DHParameterSpec[])object;
/*     */       
/* 190 */       for (byte b = 0; b != arrayOfDHParameterSpec.length; b++) {
/*     */         
/* 192 */         if (arrayOfDHParameterSpec[b].getP().bitLength() == paramInt)
/*     */         {
/* 194 */           return arrayOfDHParameterSpec[b];
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 199 */     DHParameters dHParameters = (DHParameters)CryptoServicesRegistrar.getSizedProperty(CryptoServicesRegistrar.Property.DH_DEFAULT_PARAMS, paramInt);
/* 200 */     if (dHParameters != null)
/*     */     {
/* 202 */       return (DHParameterSpec)new DHDomainParameterSpec(dHParameters);
/*     */     }
/*     */     
/* 205 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public DSAParameterSpec getDSADefaultParameters(int paramInt) {
/* 210 */     DSAParameters dSAParameters = (DSAParameters)CryptoServicesRegistrar.getSizedProperty(CryptoServicesRegistrar.Property.DSA_DEFAULT_PARAMS, paramInt);
/* 211 */     if (dSAParameters != null)
/*     */     {
/* 213 */       return new DSAParameterSpec(dSAParameters.getP(), dSAParameters.getQ(), dSAParameters.getG());
/*     */     }
/*     */     
/* 216 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set getAcceptableNamedCurves() {
/* 221 */     return Collections.unmodifiableSet(this.acceptableNamedCurves);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map getAdditionalECParameters() {
/* 226 */     return Collections.unmodifiableMap(this.additionalECParameters);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/jce/provider/BouncyCastleProviderConfiguration.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */