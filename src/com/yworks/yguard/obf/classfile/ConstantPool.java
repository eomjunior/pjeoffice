/*     */ package com.yworks.yguard.obf.classfile;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConstantPool
/*     */ {
/*     */   private ClassFile myClassFile;
/*     */   private Vector pool;
/*     */   
/*     */   public ConstantPool(ClassFile classFile, CpInfo[] cpInfo) {
/*  41 */     this.myClassFile = classFile;
/*  42 */     int length = cpInfo.length;
/*  43 */     this.pool = new Vector(length);
/*  44 */     this.pool.setSize(length);
/*  45 */     for (int i = 0; i < length; i++)
/*     */     {
/*  47 */       this.pool.setElementAt(cpInfo[i], i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration elements() {
/*  58 */     return this.pool.elements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*  68 */     return this.pool.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CpInfo getCpEntry(int i) {
/*  79 */     if (i < this.pool.size())
/*     */     {
/*  81 */       return this.pool.elementAt(i);
/*     */     }
/*  83 */     throw new IndexOutOfBoundsException("Constant Pool index out of range.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateRefCount() {
/*  92 */     walkPool(new PoolAction() { public void defaultAction(CpInfo cpInfo) {
/*  93 */             cpInfo.resetRefCount();
/*     */           } }
/*     */       );
/*     */     
/*  97 */     this.myClassFile.markUtf8Refs(this);
/*     */ 
/*     */     
/* 100 */     this.myClassFile.markNTRefs(this);
/*     */ 
/*     */     
/* 103 */     walkPool(new PoolAction() { public void utf8Action(Utf8CpInfo cpInfo) {
/* 104 */             if (cpInfo.getRefCount() == 0) cpInfo.clearString();
/*     */           
/*     */           } }
/*     */       );
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void incRefCount(int i) {
/* 115 */     CpInfo cpInfo = this.pool.elementAt(i);
/* 116 */     if (cpInfo != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 123 */       cpInfo.incRefCount();
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
/*     */   public int remapUtf8To(String newString, int oldIndex) {
/* 136 */     decRefCount(oldIndex);
/* 137 */     return addUtf8Entry(newString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decRefCount(int i) {
/* 147 */     CpInfo cpInfo = this.pool.elementAt(i);
/* 148 */     if (cpInfo != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 155 */       cpInfo.decRefCount();
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
/*     */   public int addEntry(CpInfo entry) {
/* 167 */     int oldLength = this.pool.size();
/* 168 */     this.pool.setSize(oldLength + 1);
/* 169 */     this.pool.setElementAt(entry, oldLength);
/* 170 */     return oldLength;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int addUtf8Entry(String s) {
/*     */     int i;
/* 177 */     for (i = 0; i < this.pool.size(); i++) {
/*     */       
/* 179 */       Object o = this.pool.elementAt(i);
/* 180 */       if (o instanceof Utf8CpInfo) {
/*     */         
/* 182 */         Utf8CpInfo entry = (Utf8CpInfo)o;
/* 183 */         if (entry.getString().equals(s)) {
/*     */           
/* 185 */           entry.incRefCount();
/* 186 */           return i;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 192 */     for (i = 0; i < this.pool.size(); i++) {
/*     */       
/* 194 */       Object o = this.pool.elementAt(i);
/* 195 */       if (o instanceof Utf8CpInfo) {
/*     */         
/* 197 */         Utf8CpInfo entry = (Utf8CpInfo)o;
/* 198 */         if (entry.getRefCount() == 0) {
/*     */           
/* 200 */           entry.setString(s);
/* 201 */           entry.incRefCount();
/* 202 */           return i;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 208 */     return addEntry(new Utf8CpInfo(s));
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
/*     */   class PoolAction
/*     */   {
/*     */     public void utf8Action(Utf8CpInfo cpInfo) {
/* 222 */       defaultAction(cpInfo);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void defaultAction(CpInfo cpInfo) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void walkPool(PoolAction pa) {
/* 235 */     for (Enumeration enumeration = this.pool.elements(); enumeration.hasMoreElements(); ) {
/*     */       
/* 237 */       Object o = enumeration.nextElement();
/* 238 */       if (o instanceof Utf8CpInfo) {
/*     */         
/* 240 */         pa.utf8Action((Utf8CpInfo)o); continue;
/*     */       } 
/* 242 */       if (o instanceof CpInfo)
/*     */       {
/* 244 */         pa.defaultAction((CpInfo)o);
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/obf/classfile/ConstantPool.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */