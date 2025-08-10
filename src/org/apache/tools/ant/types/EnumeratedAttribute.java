/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
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
/*     */ public abstract class EnumeratedAttribute
/*     */ {
/*     */   protected String value;
/*  43 */   private int index = -1;
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
/*     */   public abstract String[] getValues();
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
/*     */   public static EnumeratedAttribute getInstance(Class<? extends EnumeratedAttribute> clazz, String value) throws BuildException {
/*     */     EnumeratedAttribute ea;
/*  72 */     if (!EnumeratedAttribute.class.isAssignableFrom(clazz)) {
/*  73 */       throw new BuildException("You have to provide a subclass from EnumeratedAttribute as clazz-parameter.");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  78 */       ea = clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/*  79 */     } catch (Exception e) {
/*  80 */       throw new BuildException(e);
/*     */     } 
/*  82 */     ea.setValue(value);
/*  83 */     return ea;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String value) throws BuildException {
/*  92 */     int idx = indexOfValue(value);
/*  93 */     if (idx == -1) {
/*  94 */       throw new BuildException(value + " is not a legal value for this attribute");
/*     */     }
/*  96 */     this.index = idx;
/*  97 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean containsValue(String value) {
/* 106 */     return (indexOfValue(value) != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int indexOfValue(String value) {
/* 117 */     String[] values = getValues();
/* 118 */     if (values == null || value == null) {
/* 119 */       return -1;
/*     */     }
/* 121 */     for (int i = 0; i < values.length; i++) {
/* 122 */       if (value.equals(values[i])) {
/* 123 */         return i;
/*     */       }
/*     */     } 
/* 126 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getValue() {
/* 133 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getIndex() {
/* 141 */     return this.index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 151 */     return getValue();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/EnumeratedAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */