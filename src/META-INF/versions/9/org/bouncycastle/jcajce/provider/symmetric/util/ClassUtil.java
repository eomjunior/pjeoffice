/*    */ package META-INF.versions.9.org.bouncycastle.jcajce.provider.symmetric.util;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClassUtil
/*    */ {
/*    */   public static Class loadClass(Class paramClass, String paramString) {
/*    */     try {
/* 12 */       ClassLoader classLoader = paramClass.getClassLoader();
/*    */       
/* 14 */       if (classLoader != null)
/*    */       {
/* 16 */         return classLoader.loadClass(paramString);
/*    */       }
/*    */ 
/*    */       
/* 20 */       return AccessController.<Class<?>>doPrivileged((PrivilegedAction<Class<?>>)new Object(paramString));
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
/*    */     }
/* 38 */     catch (ClassNotFoundException classNotFoundException) {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 43 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/jcajce/provider/symmetric/util/ClassUtil.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */