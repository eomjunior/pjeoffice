/*     */ package META-INF.versions.9.org.bouncycastle.jcajce.provider.config;
/*     */ 
/*     */ import java.security.BasicPermission;
/*     */ import java.security.Permission;
/*     */ import java.util.StringTokenizer;
/*     */ import org.bouncycastle.util.Strings;
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
/*     */ 
/*     */ 
/*     */ public class ProviderConfigurationPermission
/*     */   extends BasicPermission
/*     */ {
/*     */   private static final int THREAD_LOCAL_EC_IMPLICITLY_CA = 1;
/*     */   private static final int EC_IMPLICITLY_CA = 2;
/*     */   private static final int THREAD_LOCAL_DH_DEFAULT_PARAMS = 4;
/*     */   private static final int DH_DEFAULT_PARAMS = 8;
/*     */   private static final int ACCEPTABLE_EC_CURVES = 16;
/*     */   private static final int ADDITIONAL_EC_PARAMETERS = 32;
/*     */   private static final int ALL = 63;
/*     */   private static final String THREAD_LOCAL_EC_IMPLICITLY_CA_STR = "threadlocalecimplicitlyca";
/*     */   private static final String EC_IMPLICITLY_CA_STR = "ecimplicitlyca";
/*     */   private static final String THREAD_LOCAL_DH_DEFAULT_PARAMS_STR = "threadlocaldhdefaultparams";
/*     */   private static final String DH_DEFAULT_PARAMS_STR = "dhdefaultparams";
/*     */   private static final String ACCEPTABLE_EC_CURVES_STR = "acceptableeccurves";
/*     */   private static final String ADDITIONAL_EC_PARAMETERS_STR = "additionalecparameters";
/*     */   private static final String ALL_STR = "all";
/*     */   private final String actions;
/*     */   private final int permissionMask;
/*     */   
/*     */   public ProviderConfigurationPermission(String paramString) {
/*  54 */     super(paramString);
/*  55 */     this.actions = "all";
/*  56 */     this.permissionMask = 63;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProviderConfigurationPermission(String paramString1, String paramString2) {
/*  61 */     super(paramString1, paramString2);
/*  62 */     this.actions = paramString2;
/*  63 */     this.permissionMask = calculateMask(paramString2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int calculateMask(String paramString) {
/*  69 */     StringTokenizer stringTokenizer = new StringTokenizer(Strings.toLowerCase(paramString), " ,");
/*  70 */     int i = 0;
/*     */     
/*  72 */     while (stringTokenizer.hasMoreTokens()) {
/*     */       
/*  74 */       String str = stringTokenizer.nextToken();
/*     */       
/*  76 */       if (str.equals("threadlocalecimplicitlyca")) {
/*     */         
/*  78 */         i |= 0x1; continue;
/*     */       } 
/*  80 */       if (str.equals("ecimplicitlyca")) {
/*     */         
/*  82 */         i |= 0x2; continue;
/*     */       } 
/*  84 */       if (str.equals("threadlocaldhdefaultparams")) {
/*     */         
/*  86 */         i |= 0x4; continue;
/*     */       } 
/*  88 */       if (str.equals("dhdefaultparams")) {
/*     */         
/*  90 */         i |= 0x8; continue;
/*     */       } 
/*  92 */       if (str.equals("acceptableeccurves")) {
/*     */         
/*  94 */         i |= 0x10; continue;
/*     */       } 
/*  96 */       if (str.equals("additionalecparameters")) {
/*     */         
/*  98 */         i |= 0x20; continue;
/*     */       } 
/* 100 */       if (str.equals("all"))
/*     */       {
/* 102 */         i |= 0x3F;
/*     */       }
/*     */     } 
/*     */     
/* 106 */     if (i == 0)
/*     */     {
/* 108 */       throw new IllegalArgumentException("unknown permissions passed to mask");
/*     */     }
/*     */     
/* 111 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getActions() {
/* 116 */     return this.actions;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean implies(Permission paramPermission) {
/* 122 */     if (!(paramPermission instanceof org.bouncycastle.jcajce.provider.config.ProviderConfigurationPermission))
/*     */     {
/* 124 */       return false;
/*     */     }
/*     */     
/* 127 */     if (!getName().equals(paramPermission.getName()))
/*     */     {
/* 129 */       return false;
/*     */     }
/*     */     
/* 132 */     org.bouncycastle.jcajce.provider.config.ProviderConfigurationPermission providerConfigurationPermission = (org.bouncycastle.jcajce.provider.config.ProviderConfigurationPermission)paramPermission;
/*     */     
/* 134 */     return ((this.permissionMask & providerConfigurationPermission.permissionMask) == providerConfigurationPermission.permissionMask);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 140 */     if (paramObject == this)
/*     */     {
/* 142 */       return true;
/*     */     }
/*     */     
/* 145 */     if (paramObject instanceof org.bouncycastle.jcajce.provider.config.ProviderConfigurationPermission) {
/*     */       
/* 147 */       org.bouncycastle.jcajce.provider.config.ProviderConfigurationPermission providerConfigurationPermission = (org.bouncycastle.jcajce.provider.config.ProviderConfigurationPermission)paramObject;
/*     */       
/* 149 */       return (this.permissionMask == providerConfigurationPermission.permissionMask && getName().equals(providerConfigurationPermission.getName()));
/*     */     } 
/*     */     
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 157 */     return getName().hashCode() + this.permissionMask;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/jcajce/provider/config/ProviderConfigurationPermission.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */