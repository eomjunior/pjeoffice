/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import org.apache.hc.core5.http.HeaderElement;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ public class BasicHeaderElement
/*     */   implements HeaderElement
/*     */ {
/*  41 */   private static final NameValuePair[] EMPTY_NAME_VALUE_PAIR_ARRAY = new NameValuePair[0];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String name;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String value;
/*     */ 
/*     */ 
/*     */   
/*     */   private final NameValuePair[] parameters;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHeaderElement(String name, String value, NameValuePair[] parameters) {
/*  60 */     this.name = (String)Args.notNull(name, "Name");
/*  61 */     this.value = value;
/*  62 */     if (parameters != null) {
/*  63 */       this.parameters = parameters;
/*     */     } else {
/*  65 */       this.parameters = EMPTY_NAME_VALUE_PAIR_ARRAY;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHeaderElement(String name, String value) {
/*  76 */     this(name, value, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  81 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  86 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public NameValuePair[] getParameters() {
/*  91 */     return (NameValuePair[])this.parameters.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getParameterCount() {
/*  96 */     return this.parameters.length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NameValuePair getParameter(int index) {
/* 102 */     return this.parameters[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public NameValuePair getParameterByName(String name) {
/* 107 */     Args.notNull(name, "Name");
/* 108 */     NameValuePair found = null;
/* 109 */     for (NameValuePair current : this.parameters) {
/* 110 */       if (current.getName().equalsIgnoreCase(name)) {
/* 111 */         found = current;
/*     */         break;
/*     */       } 
/*     */     } 
/* 115 */     return found;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 120 */     StringBuilder buffer = new StringBuilder();
/* 121 */     buffer.append(this.name);
/* 122 */     if (this.value != null) {
/* 123 */       buffer.append("=");
/* 124 */       buffer.append(this.value);
/*     */     } 
/* 126 */     for (NameValuePair parameter : this.parameters) {
/* 127 */       buffer.append("; ");
/* 128 */       buffer.append(parameter);
/*     */     } 
/* 130 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BasicHeaderElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */