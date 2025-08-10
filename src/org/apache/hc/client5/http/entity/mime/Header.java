/*     */ package org.apache.hc.client5.http.entity.mime;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
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
/*     */ public class Header
/*     */   implements Iterable<MimeField>
/*     */ {
/*  49 */   private final List<MimeField> fields = new LinkedList<>();
/*  50 */   private final Map<String, List<MimeField>> fieldMap = new HashMap<>();
/*     */ 
/*     */   
/*     */   public void addField(MimeField field) {
/*  54 */     if (field == null) {
/*     */       return;
/*     */     }
/*  57 */     String key = field.getName().toLowerCase(Locale.ROOT);
/*  58 */     List<MimeField> values = this.fieldMap.computeIfAbsent(key, k -> new LinkedList());
/*  59 */     values.add(field);
/*  60 */     this.fields.add(field);
/*     */   }
/*     */   
/*     */   public List<MimeField> getFields() {
/*  64 */     return new ArrayList<>(this.fields);
/*     */   }
/*     */   
/*     */   public MimeField getField(String name) {
/*  68 */     if (name == null) {
/*  69 */       return null;
/*     */     }
/*  71 */     String key = name.toLowerCase(Locale.ROOT);
/*  72 */     List<MimeField> list = this.fieldMap.get(key);
/*  73 */     if (list != null && !list.isEmpty()) {
/*  74 */       return list.get(0);
/*     */     }
/*  76 */     return null;
/*     */   }
/*     */   
/*     */   public List<MimeField> getFields(String name) {
/*  80 */     if (name == null) {
/*  81 */       return null;
/*     */     }
/*  83 */     String key = name.toLowerCase(Locale.ROOT);
/*  84 */     List<MimeField> list = this.fieldMap.get(key);
/*  85 */     if (list == null || list.isEmpty()) {
/*  86 */       return Collections.emptyList();
/*     */     }
/*  88 */     return new ArrayList<>(list);
/*     */   }
/*     */   
/*     */   public int removeFields(String name) {
/*  92 */     if (name == null) {
/*  93 */       return 0;
/*     */     }
/*  95 */     String key = name.toLowerCase(Locale.ROOT);
/*  96 */     List<MimeField> removed = this.fieldMap.remove(key);
/*  97 */     if (removed == null || removed.isEmpty()) {
/*  98 */       return 0;
/*     */     }
/* 100 */     this.fields.removeAll(removed);
/* 101 */     return removed.size();
/*     */   }
/*     */   
/*     */   public void setField(MimeField field) {
/* 105 */     if (field == null) {
/*     */       return;
/*     */     }
/* 108 */     String key = field.getName().toLowerCase(Locale.ROOT);
/* 109 */     List<MimeField> list = this.fieldMap.get(key);
/* 110 */     if (list == null || list.isEmpty()) {
/* 111 */       addField(field);
/*     */       return;
/*     */     } 
/* 114 */     list.clear();
/* 115 */     list.add(field);
/* 116 */     int firstOccurrence = -1;
/* 117 */     int index = 0;
/* 118 */     for (Iterator<MimeField> it = this.fields.iterator(); it.hasNext(); index++) {
/* 119 */       MimeField f = it.next();
/* 120 */       if (f.getName().equalsIgnoreCase(field.getName())) {
/* 121 */         it.remove();
/* 122 */         if (firstOccurrence == -1) {
/* 123 */           firstOccurrence = index;
/*     */         }
/*     */       } 
/*     */     } 
/* 127 */     this.fields.add(firstOccurrence, field);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<MimeField> iterator() {
/* 132 */     return Collections.<MimeField>unmodifiableList(this.fields).iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 137 */     return this.fields.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/Header.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */