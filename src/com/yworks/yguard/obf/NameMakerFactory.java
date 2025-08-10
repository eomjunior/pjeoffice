/*     */ package com.yworks.yguard.obf;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public abstract class NameMakerFactory
/*     */ {
/*  21 */   private static NameMakerFactory instance = new DefaultNameMakerFactory();
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
/*     */   public static NameMakerFactory getInstance() {
/*  36 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInstance(NameMakerFactory _instance) {
/*  46 */     instance = _instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract NameMaker getPackageNameMaker(String[] paramArrayOfString, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract NameMaker getClassNameMaker(String[] paramArrayOfString, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract NameMaker getInnerClassNameMaker(String[] paramArrayOfString, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract NameMaker getMethodNameMaker(String[] paramArrayOfString, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract NameMaker getFieldNameMaker(String[] paramArrayOfString, String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class DefaultNameMakerFactory
/*     */     extends NameMakerFactory
/*     */   {
/*  99 */     private Map classNameMap = new HashMap<>();
/* 100 */     private Map fieldNameMap = new HashMap<>();
/* 101 */     private Map methodNameMap = new HashMap<>();
/* 102 */     private Map innerClassNameMap = new HashMap<>();
/* 103 */     private Map packageNameMap = new HashMap<>();
/*     */ 
/*     */     
/*     */     public NameMaker getClassNameMaker(String[] reservedNames, String fqClassName) {
/* 107 */       NameMaker res = (NameMaker)this.classNameMap.get(fqClassName);
/* 108 */       if (res == null) {
/* 109 */         res = createClassNameMaker(reservedNames, fqClassName);
/* 110 */         this.classNameMap.put(fqClassName, res);
/*     */       } 
/* 112 */       return res;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected NameMaker createClassNameMaker(String[] reservedNames, String fqClassName) {
/* 123 */       return new KeywordNameMaker(reservedNames);
/*     */     }
/*     */ 
/*     */     
/*     */     public NameMaker getFieldNameMaker(String[] reservedNames, String fqClassName) {
/* 128 */       NameMaker res = (NameMaker)this.fieldNameMap.get(fqClassName);
/* 129 */       if (res == null) {
/* 130 */         res = createFieldNameMaker(reservedNames, fqClassName);
/* 131 */         this.fieldNameMap.put(fqClassName, res);
/*     */       } 
/* 133 */       return res;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected NameMaker createFieldNameMaker(String[] reservedNames, String fqClassName) {
/* 144 */       return new KeywordNameMaker(reservedNames, false, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public NameMaker getInnerClassNameMaker(String[] reservedNames, String fqInnerClassName) {
/* 149 */       NameMaker res = (NameMaker)this.innerClassNameMap.get(fqInnerClassName);
/* 150 */       if (res == null) {
/* 151 */         res = createInnerClassNameMaker(reservedNames, fqInnerClassName);
/* 152 */         this.innerClassNameMap.put(fqInnerClassName, res);
/*     */       } 
/* 154 */       return res;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected NameMaker createInnerClassNameMaker(final String[] reservedNames, String fqInnerClassName) {
/* 165 */       final NameMaker inner = new KeywordNameMaker(null);
/*     */       
/* 167 */       return new NameMaker() { public String nextName(String sig) {
/*     */             String name;
/*     */             do {
/* 170 */               name = '_' + inner.nextName(sig);
/* 171 */             } while (reservedNames != null && Tools.isInArray(name, reservedNames));
/* 172 */             return name;
/*     */           } }
/*     */         ;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public NameMaker getMethodNameMaker(String[] reservedNames, String fqClassName) {
/* 181 */       NameMaker res = (NameMaker)this.methodNameMap.get(fqClassName);
/* 182 */       if (res == null) {
/* 183 */         res = createMethodNameMaker(reservedNames, fqClassName);
/* 184 */         this.methodNameMap.put(fqClassName, res);
/*     */       } 
/* 186 */       return res;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected NameMaker createMethodNameMaker(String[] reservedNames, String fqClassName) {
/* 197 */       return new KeywordNameMaker(reservedNames, false, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public NameMaker getPackageNameMaker(String[] reservedNames, String packageName) {
/* 202 */       NameMaker res = (NameMaker)this.packageNameMap.get(packageName);
/* 203 */       if (res == null) {
/* 204 */         res = createPackageNameMaker(reservedNames, packageName);
/* 205 */         this.packageNameMap.put(packageName, res);
/*     */       } 
/* 207 */       return res;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected NameMaker createPackageNameMaker(String[] reservedNames, String packageName) {
/* 218 */       return new KeywordNameMaker(reservedNames);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 222 */       return "default";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/NameMakerFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */