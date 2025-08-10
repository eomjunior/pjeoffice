/*     */ package org.apache.tools.ant.property;
/*     */ 
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import org.apache.tools.ant.Project;
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
/*     */ public class ParseProperties
/*     */   implements ParseNextProperty
/*     */ {
/*     */   private final Project project;
/*     */   private final GetProperty getProperty;
/*     */   private final Collection<PropertyExpander> expanders;
/*     */   
/*     */   public ParseProperties(Project project, Collection<PropertyExpander> expanders, GetProperty getProperty) {
/*  44 */     this.project = project;
/*  45 */     this.expanders = expanders;
/*  46 */     this.getProperty = getProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Project getProject() {
/*  55 */     return this.project;
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
/*     */   public Object parseProperties(String value) {
/*  95 */     if (value == null || value.isEmpty()) {
/*  96 */       return value;
/*     */     }
/*  98 */     int len = value.length();
/*  99 */     ParsePosition pos = new ParsePosition(0);
/* 100 */     Object o = parseNextProperty(value, pos);
/* 101 */     if (o != null && pos.getIndex() >= len) {
/* 102 */       return o;
/*     */     }
/* 104 */     StringBuilder sb = new StringBuilder(len * 2);
/* 105 */     if (o == null) {
/* 106 */       sb.append(value.charAt(pos.getIndex()));
/* 107 */       pos.setIndex(pos.getIndex() + 1);
/*     */     } else {
/* 109 */       sb.append(o);
/*     */     } 
/* 111 */     while (pos.getIndex() < len) {
/* 112 */       o = parseNextProperty(value, pos);
/* 113 */       if (o == null) {
/* 114 */         sb.append(value.charAt(pos.getIndex()));
/* 115 */         pos.setIndex(pos.getIndex() + 1); continue;
/*     */       } 
/* 117 */       sb.append(o);
/*     */     } 
/*     */     
/* 120 */     return sb.toString();
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
/*     */   
/*     */   public boolean containsProperties(String value) {
/* 134 */     if (value == null) {
/* 135 */       return false;
/*     */     }
/* 137 */     int len = value.length();
/* 138 */     for (ParsePosition pos = new ParsePosition(0); pos.getIndex() < len; ) {
/* 139 */       if (parsePropertyName(value, pos) != null) {
/* 140 */         return true;
/*     */       }
/* 142 */       pos.setIndex(pos.getIndex() + 1);
/*     */     } 
/* 144 */     return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object parseNextProperty(String value, ParsePosition pos) {
/* 164 */     int start = pos.getIndex();
/*     */     
/* 166 */     if (start > value.length())
/*     */     {
/*     */       
/* 169 */       return null;
/*     */     }
/*     */     
/* 172 */     String propertyName = parsePropertyName(value, pos);
/* 173 */     if (propertyName != null) {
/* 174 */       Object result = getProperty(propertyName);
/* 175 */       if (result != null) {
/* 176 */         return result;
/*     */       }
/* 178 */       if (this.project != null) {
/* 179 */         this.project.log("Property \"" + propertyName + "\" has not been set", 3);
/*     */       }
/*     */ 
/*     */       
/* 183 */       return value.substring(start, pos.getIndex());
/*     */     } 
/* 185 */     return null;
/*     */   }
/*     */   
/*     */   private String parsePropertyName(String value, ParsePosition pos) {
/* 189 */     return this.expanders.stream()
/* 190 */       .map(xp -> xp.parsePropertyName(value, pos, this))
/* 191 */       .filter(Objects::nonNull).findFirst().orElse(null);
/*     */   }
/*     */   
/*     */   private Object getProperty(String propertyName) {
/* 195 */     return this.getProperty.getProperty(propertyName);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/property/ParseProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */