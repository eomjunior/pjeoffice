/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import org.apache.log4j.helpers.ThreadLocalMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MDC
/*     */ {
/*  46 */   static final MDC mdc = new MDC();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int HT_SIZE = 7;
/*     */ 
/*     */ 
/*     */   
/*  55 */   Object tlm = new ThreadLocalMap();
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
/*     */   public static void put(String key, Object o) {
/*  68 */     if (mdc != null) {
/*  69 */       mdc.put0(key, o);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object get(String key) {
/*  80 */     if (mdc != null) {
/*  81 */       return mdc.get0(key);
/*     */     }
/*  83 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void remove(String key) {
/*  91 */     if (mdc != null) {
/*  92 */       mdc.remove0(key);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Hashtable getContext() {
/* 101 */     if (mdc != null) {
/* 102 */       return mdc.getContext0();
/*     */     }
/* 104 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clear() {
/* 114 */     if (mdc != null) {
/* 115 */       mdc.clear0();
/*     */     }
/*     */   }
/*     */   
/*     */   private void put0(String key, Object o) {
/* 120 */     if (this.tlm == null) {
/*     */       return;
/*     */     }
/* 123 */     Hashtable<Object, Object> ht = (Hashtable)((ThreadLocalMap)this.tlm).get();
/* 124 */     if (ht == null) {
/* 125 */       ht = new Hashtable<Object, Object>(7);
/* 126 */       ((ThreadLocalMap)this.tlm).set(ht);
/*     */     } 
/* 128 */     ht.put(key, o);
/*     */   }
/*     */ 
/*     */   
/*     */   private Object get0(String key) {
/* 133 */     if (this.tlm == null) {
/* 134 */       return null;
/*     */     }
/* 136 */     Hashtable ht = (Hashtable)((ThreadLocalMap)this.tlm).get();
/* 137 */     if (ht != null && key != null) {
/* 138 */       return ht.get(key);
/*     */     }
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void remove0(String key) {
/* 146 */     if (this.tlm != null) {
/* 147 */       Hashtable ht = (Hashtable)((ThreadLocalMap)this.tlm).get();
/* 148 */       if (ht != null) {
/* 149 */         ht.remove(key);
/*     */         
/* 151 */         if (ht.isEmpty()) {
/* 152 */           clear0();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Hashtable getContext0() {
/* 159 */     if (this.tlm == null) {
/* 160 */       return null;
/*     */     }
/* 162 */     return (Hashtable)((ThreadLocalMap)this.tlm).get();
/*     */   }
/*     */ 
/*     */   
/*     */   private void clear0() {
/* 167 */     if (this.tlm != null) {
/* 168 */       Hashtable ht = (Hashtable)((ThreadLocalMap)this.tlm).get();
/* 169 */       if (ht != null) {
/* 170 */         ht.clear();
/*     */       }
/*     */       
/* 173 */       ((ThreadLocalMap)this.tlm).remove();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/MDC.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */