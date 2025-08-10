/*    */ package META-INF.versions.9.org.bouncycastle.crypto;
/*    */ 
/*    */ import java.security.Permission;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ public class CryptoServicesPermission
/*    */   extends Permission
/*    */ {
/*    */   public static final String GLOBAL_CONFIG = "globalConfig";
/*    */   public static final String THREAD_LOCAL_CONFIG = "threadLocalConfig";
/*    */   public static final String DEFAULT_RANDOM = "defaultRandomConfig";
/* 28 */   private final Set<String> actions = new HashSet<>();
/*    */ 
/*    */   
/*    */   public CryptoServicesPermission(String paramString) {
/* 32 */     super(paramString);
/*    */     
/* 34 */     this.actions.add(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean implies(Permission paramPermission) {
/* 39 */     if (paramPermission instanceof org.bouncycastle.crypto.CryptoServicesPermission) {
/*    */       
/* 41 */       org.bouncycastle.crypto.CryptoServicesPermission cryptoServicesPermission = (org.bouncycastle.crypto.CryptoServicesPermission)paramPermission;
/*    */       
/* 43 */       if (getName().equals(cryptoServicesPermission.getName()))
/*    */       {
/* 45 */         return true;
/*    */       }
/*    */       
/* 48 */       if (this.actions.containsAll(cryptoServicesPermission.actions))
/*    */       {
/* 50 */         return true;
/*    */       }
/*    */     } 
/*    */     
/* 54 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 59 */     if (paramObject instanceof org.bouncycastle.crypto.CryptoServicesPermission) {
/*    */       
/* 61 */       org.bouncycastle.crypto.CryptoServicesPermission cryptoServicesPermission = (org.bouncycastle.crypto.CryptoServicesPermission)paramObject;
/*    */       
/* 63 */       if (this.actions.equals(cryptoServicesPermission.actions))
/*    */       {
/* 65 */         return true;
/*    */       }
/*    */     } 
/*    */     
/* 69 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 74 */     return this.actions.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getActions() {
/* 79 */     return this.actions.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/crypto/CryptoServicesPermission.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */