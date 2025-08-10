/*     */ package com.yworks.yguard.obf;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
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
/*     */ public class Pk
/*     */   extends PkCl
/*     */ {
/*  23 */   private Hashtable pks = new Hashtable<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Pk createRoot(ClassTree classTree) {
/*  34 */     return new Pk(classTree);
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
/*     */   public Pk(ClassTree classTree) {
/*  46 */     this((TreeItem)null, "");
/*  47 */     this.classTree = classTree;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pk(TreeItem parent, String name) {
/*  58 */     super(parent, name);
/*  59 */     if (parent == null && !name.equals(""))
/*     */     {
/*  61 */       throw new IllegalArgumentException("Internal error: only the default package has no parent");
/*     */     }
/*  63 */     if (parent != null && name.equals(""))
/*     */     {
/*  65 */       throw new IllegalArgumentException("Internal error: the default package cannot have a parent");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pk getPackage(String name) {
/*  75 */     return (Pk)this.pks.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getPackageEnum() {
/*  82 */     return this.pks.elements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPackageCount() {
/*  89 */     return this.pks.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pk addPackage(String name) {
/*  99 */     Pk pk = getPackage(name);
/* 100 */     if (pk == null) {
/*     */       
/* 102 */       pk = new Pk(this, name);
/* 103 */       this.pks.put(name, pk);
/*     */     } 
/* 105 */     return pk;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Cl addClass(Object[] classInfo) {
/* 111 */     return addClass(false, classInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Cl addPlaceholderClass(String name) {
/* 117 */     return addPlaceholderClass(false, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void generateNames() {
/* 123 */     super.generateNames();
/* 124 */     generateNames(this.pks);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/Pk.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */