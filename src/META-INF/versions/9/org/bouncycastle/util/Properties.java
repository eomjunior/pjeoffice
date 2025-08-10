/*     */ package META-INF.versions.9.org.bouncycastle.util;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.AccessControlException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class Properties
/*     */ {
/*  24 */   private static final ThreadLocal threadProperties = new ThreadLocal();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isOverrideSet(String paramString) {
/*     */     try {
/*  36 */       return isSetTrue(getPropertyValue(paramString));
/*     */     }
/*  38 */     catch (AccessControlException accessControlException) {
/*     */       
/*  40 */       return false;
/*     */     } 
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
/*     */   public static boolean isOverrideSetTo(String paramString, boolean paramBoolean) {
/*     */     try {
/*  55 */       String str = getPropertyValue(paramString);
/*  56 */       if (paramBoolean)
/*     */       {
/*  58 */         return isSetTrue(str);
/*     */       }
/*  60 */       return isSetFalse(str);
/*     */     }
/*  62 */     catch (AccessControlException accessControlException) {
/*     */       
/*  64 */       return false;
/*     */     } 
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
/*     */   public static boolean setThreadOverride(String paramString, boolean paramBoolean) {
/*  77 */     boolean bool = isOverrideSet(paramString);
/*     */     
/*  79 */     Map<Object, Object> map = threadProperties.get();
/*  80 */     if (map == null) {
/*     */       
/*  82 */       map = new HashMap<>();
/*     */       
/*  84 */       threadProperties.set(map);
/*     */     } 
/*     */     
/*  87 */     map.put(paramString, paramBoolean ? "true" : "false");
/*     */     
/*  89 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean removeThreadOverride(String paramString) {
/* 100 */     Map map = threadProperties.get();
/* 101 */     if (map != null) {
/*     */       
/* 103 */       String str = (String)map.remove(paramString);
/* 104 */       if (str != null) {
/*     */         
/* 106 */         if (map.isEmpty())
/*     */         {
/* 108 */           threadProperties.remove();
/*     */         }
/*     */         
/* 111 */         return "true".equals(Strings.toLowerCase(str));
/*     */       } 
/*     */     } 
/*     */     
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger asBigInteger(String paramString) {
/* 120 */     String str = getPropertyValue(paramString);
/*     */     
/* 122 */     if (str != null)
/*     */     {
/* 124 */       return new BigInteger(str);
/*     */     }
/*     */     
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<String> asKeySet(String paramString) {
/* 132 */     HashSet<String> hashSet = new HashSet();
/*     */     
/* 134 */     String str = getPropertyValue(paramString);
/*     */     
/* 136 */     if (str != null) {
/*     */       
/* 138 */       StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
/* 139 */       while (stringTokenizer.hasMoreElements())
/*     */       {
/* 141 */         hashSet.add(Strings.toLowerCase(stringTokenizer.nextToken()).trim());
/*     */       }
/*     */     } 
/*     */     
/* 145 */     return Collections.unmodifiableSet(hashSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getPropertyValue(String paramString) {
/* 150 */     String str = AccessController.<String>doPrivileged((PrivilegedAction<String>)new Object(paramString));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     if (str != null)
/*     */     {
/* 159 */       return str;
/*     */     }
/*     */     
/* 162 */     Map map = threadProperties.get();
/* 163 */     if (map != null) {
/*     */       
/* 165 */       String str1 = (String)map.get(paramString);
/* 166 */       if (str1 != null)
/*     */       {
/* 168 */         return str1;
/*     */       }
/*     */     } 
/*     */     
/* 172 */     return AccessController.<String>doPrivileged((PrivilegedAction<String>)new Object(paramString));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isSetFalse(String paramString) {
/* 183 */     if (paramString == null || paramString.length() != 5)
/*     */     {
/* 185 */       return false;
/*     */     }
/*     */     
/* 188 */     return ((paramString.charAt(0) == 'f' || paramString.charAt(0) == 'F') && (paramString
/* 189 */       .charAt(1) == 'a' || paramString.charAt(1) == 'A') && (paramString
/* 190 */       .charAt(2) == 'l' || paramString.charAt(2) == 'L') && (paramString
/* 191 */       .charAt(3) == 's' || paramString.charAt(3) == 'S') && (paramString
/* 192 */       .charAt(4) == 'e' || paramString.charAt(4) == 'E'));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isSetTrue(String paramString) {
/* 197 */     if (paramString == null || paramString.length() != 4)
/*     */     {
/* 199 */       return false;
/*     */     }
/*     */     
/* 202 */     return ((paramString.charAt(0) == 't' || paramString.charAt(0) == 'T') && (paramString
/* 203 */       .charAt(1) == 'r' || paramString.charAt(1) == 'R') && (paramString
/* 204 */       .charAt(2) == 'u' || paramString.charAt(2) == 'U') && (paramString
/* 205 */       .charAt(3) == 'e' || paramString.charAt(3) == 'E'));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/util/Properties.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */