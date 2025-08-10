/*     */ package com.yworks.common.ant;
/*     */ 
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.PatternSet;
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
/*     */ public abstract class PatternMatchedSection
/*     */ {
/*     */   public enum Access
/*     */   {
/*  26 */     PUBLIC,
/*     */ 
/*     */ 
/*     */     
/*  30 */     PROTECTED,
/*     */ 
/*     */ 
/*     */     
/*  34 */     FRIENDLY,
/*     */ 
/*     */ 
/*     */     
/*  38 */     PRIVATE,
/*     */ 
/*     */ 
/*     */     
/*  42 */     NONE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isAccessLevel(Access level) {
/*  52 */       if (equals(NONE) && !level.equals(NONE)) return false; 
/*  53 */       return (compareTo(level) >= 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isAccessLevel(int asmAccess) {
/*  63 */       return isAccessLevel(valueOf(asmAccess));
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
/*  87 */   private Access access = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<TypePatternSet.Type> types;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<TypePatternSet.Type, PatternSet> patternSets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypePatternSet createPatternSet() {
/* 105 */     TypePatternSet typePatternSet = new TypePatternSet();
/* 106 */     addPatternSet(typePatternSet, typePatternSet.getType());
/* 107 */     return typePatternSet;
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
/*     */   public void addPatternSet(PatternSet ps, TypePatternSet.Type type) {
/* 122 */     if (null == this.patternSets) {
/* 123 */       this.patternSets = new EnumMap<>(TypePatternSet.Type.class);
/*     */     }
/*     */ 
/*     */     
/* 127 */     if (null != this.patternSets.get(type)) {
/* 128 */       PatternSet existing = this.patternSets.get(type);
/* 129 */       ps.addConfiguredPatternset(existing);
/*     */     } 
/* 131 */     this.patternSets.put(type, ps);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternSet getPatternSet(TypePatternSet.Type type) {
/* 141 */     if (null != this.patternSets) {
/* 142 */       return this.patternSets.get(type);
/*     */     }
/* 144 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccess(String access) {
/* 154 */     this.access = Access.valueOf(access.toUpperCase());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Access getAccess() {
/* 163 */     return this.access;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Access accessValue(String accessString) {
/* 173 */     Access access = null;
/* 174 */     if (accessString.trim().equals("")) {
/* 175 */       throw new BuildException("You specified an empty access modifier.");
/*     */     }
/*     */     try {
/* 178 */       access = Access.valueOf(accessString.trim().toUpperCase());
/* 179 */     } catch (IllegalArgumentException e) {
/* 180 */       throw new BuildException("Illegal access modifier: " + accessString);
/*     */     } 
/* 182 */     return access;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/common/ant/PatternMatchedSection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */