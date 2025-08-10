/*     */ package org.apache.tools.ant.property;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.tools.ant.BuildException;
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
/*     */ public class ResolvePropertyMap
/*     */   implements GetProperty
/*     */ {
/*  33 */   private final Set<String> seen = new HashSet<>();
/*     */ 
/*     */   
/*     */   private final ParseProperties parseProperties;
/*     */ 
/*     */   
/*     */   private final GetProperty master;
/*     */ 
/*     */   
/*     */   private Map<String, Object> map;
/*     */   
/*     */   private String prefix;
/*     */   
/*     */   private boolean prefixValues = false;
/*     */   
/*     */   private boolean expandingLHS = true;
/*     */ 
/*     */   
/*     */   public ResolvePropertyMap(Project project, GetProperty master, Collection<PropertyExpander> expanders) {
/*  52 */     this.master = master;
/*  53 */     this.parseProperties = new ParseProperties(project, expanders, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) {
/*  63 */     if (this.seen.contains(name)) {
/*  64 */       throw new BuildException("Property %s was circularly defined.", new Object[] { name });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  76 */       String fullKey = name;
/*  77 */       if (this.prefix != null && (this.expandingLHS || this.prefixValues)) {
/*  78 */         fullKey = this.prefix + name;
/*     */       }
/*     */       
/*  81 */       Object masterValue = this.master.getProperty(fullKey);
/*  82 */       if (masterValue != null)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*  87 */         return masterValue;
/*     */       }
/*     */       
/*  90 */       this.seen.add(name);
/*     */       
/*  92 */       String recursiveCallKey = name;
/*  93 */       if (this.prefix != null && !this.expandingLHS && !this.prefixValues)
/*     */       {
/*     */ 
/*     */         
/*  97 */         recursiveCallKey = this.prefix + name;
/*     */       }
/*     */       
/* 100 */       this.expandingLHS = false;
/*     */ 
/*     */       
/* 103 */       return this.parseProperties.parseProperties((String)this.map.get(recursiveCallKey));
/*     */     } finally {
/* 105 */       this.seen.remove(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void resolveAllProperties(Map<String, Object> map) {
/* 116 */     resolveAllProperties(map, null, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void resolveAllProperties(Map<String, Object> map, String prefix) {
/* 128 */     resolveAllProperties(map, null, false);
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
/*     */   public void resolveAllProperties(Map<String, Object> map, String prefix, boolean prefixValues) {
/* 143 */     this.map = map;
/* 144 */     this.prefix = prefix;
/* 145 */     this.prefixValues = prefixValues;
/*     */     
/* 147 */     for (String key : map.keySet()) {
/* 148 */       this.expandingLHS = true;
/* 149 */       Object result = getProperty(key);
/* 150 */       String value = (result == null) ? "" : result.toString();
/* 151 */       map.put(key, value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/property/ResolvePropertyMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */