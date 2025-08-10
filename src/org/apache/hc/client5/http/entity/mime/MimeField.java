/*     */ package org.apache.hc.client5.http.entity.mime;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.NameValuePair;
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
/*     */ public class MimeField
/*     */ {
/*     */   private final String name;
/*     */   private final String value;
/*     */   private final List<NameValuePair> parameters;
/*     */   
/*     */   public MimeField(String name, String value) {
/*  49 */     this.name = name;
/*  50 */     this.value = value;
/*  51 */     this.parameters = Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeField(String name, String value, List<NameValuePair> parameters) {
/*  58 */     this.name = name;
/*  59 */     this.value = value;
/*  60 */     this
/*  61 */       .parameters = (parameters != null) ? Collections.<NameValuePair>unmodifiableList(new ArrayList<>(parameters)) : Collections.<NameValuePair>emptyList();
/*     */   }
/*     */   
/*     */   public MimeField(MimeField from) {
/*  65 */     this(from.name, from.value, from.parameters);
/*     */   }
/*     */   
/*     */   public String getName() {
/*  69 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  76 */     return this.value;
/*     */   }
/*     */   
/*     */   public String getBody() {
/*  80 */     StringBuilder sb = new StringBuilder();
/*  81 */     sb.append(this.value);
/*  82 */     for (int i = 0; i < this.parameters.size(); i++) {
/*  83 */       NameValuePair parameter = this.parameters.get(i);
/*  84 */       sb.append("; ");
/*  85 */       sb.append(parameter.getName());
/*  86 */       sb.append("=\"");
/*  87 */       String v = parameter.getValue();
/*  88 */       for (int n = 0; n < v.length(); n++) {
/*  89 */         char ch = v.charAt(n);
/*  90 */         if (ch == '"' || ch == '\\') {
/*  91 */           sb.append("\\");
/*     */         }
/*  93 */         sb.append(ch);
/*     */       } 
/*  95 */       sb.append("\"");
/*     */     } 
/*  97 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public List<NameValuePair> getParameters() {
/* 101 */     return this.parameters;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 106 */     StringBuilder buffer = new StringBuilder();
/* 107 */     buffer.append(this.name);
/* 108 */     buffer.append(": ");
/* 109 */     buffer.append(getBody());
/* 110 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/MimeField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */