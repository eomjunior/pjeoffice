/*    */ package org.apache.log4j.net;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InvalidClassException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectStreamClass;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class HardenedObjectInputStream
/*    */   extends ObjectInputStream
/*    */ {
/*    */   static final String ARRAY_CLASS_PREFIX = "[L";
/*    */   final List<String> whitelistedClassNames;
/* 46 */   static final String[] JAVA_PACKAGES = new String[] { "java.lang", "java.util", "[Ljava.lang" };
/*    */   
/*    */   public HardenedObjectInputStream(InputStream in, String[] whilelist) throws IOException {
/* 49 */     super(in);
/*    */     
/* 51 */     this.whitelistedClassNames = new ArrayList<String>();
/* 52 */     if (whilelist != null) {
/* 53 */       for (int i = 0; i < whilelist.length; i++) {
/* 54 */         this.whitelistedClassNames.add(whilelist[i]);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public HardenedObjectInputStream(InputStream in, List<String> whitelist) throws IOException {
/* 60 */     super(in);
/*    */     
/* 62 */     this.whitelistedClassNames = new ArrayList<String>();
/* 63 */     this.whitelistedClassNames.addAll(whitelist);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Class<?> resolveClass(ObjectStreamClass anObjectStreamClass) throws IOException, ClassNotFoundException {
/* 69 */     String incomingClassName = anObjectStreamClass.getName();
/*    */     
/* 71 */     if (!isWhitelisted(incomingClassName)) {
/* 72 */       throw new InvalidClassException("Unauthorized deserialization attempt", incomingClassName);
/*    */     }
/*    */     
/* 75 */     return super.resolveClass(anObjectStreamClass);
/*    */   }
/*    */   
/*    */   private boolean isWhitelisted(String incomingClassName) {
/* 79 */     for (int i = 0; i < JAVA_PACKAGES.length; i++) {
/* 80 */       if (incomingClassName.startsWith(JAVA_PACKAGES[i]))
/* 81 */         return true; 
/*    */     } 
/* 83 */     for (String whiteListed : this.whitelistedClassNames) {
/* 84 */       if (incomingClassName.equals(whiteListed))
/* 85 */         return true; 
/*    */     } 
/* 87 */     return false;
/*    */   }
/*    */   
/*    */   protected void addToWhitelist(List<String> additionalAuthorizedClasses) {
/* 91 */     this.whitelistedClassNames.addAll(additionalAuthorizedClasses);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/net/HardenedObjectInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */