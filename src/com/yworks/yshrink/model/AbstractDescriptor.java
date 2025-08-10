/*     */ package com.yworks.yshrink.model;
/*     */ 
/*     */ import com.yworks.logging.Logger;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDescriptor
/*     */ {
/*     */   private boolean isEntryPoint;
/*     */   private boolean isReachable;
/*  22 */   private List<AnnotationUsage> annotations = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object node;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int access;
/*     */ 
/*     */ 
/*     */   
/*     */   protected File sourceJar;
/*     */ 
/*     */   
/*  37 */   private static final Pattern CLASS_PATTERN = Pattern.compile("L(.*);");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractDescriptor(int access, File sourceJar) {
/*  46 */     this.access = access;
/*  47 */     this.sourceJar = sourceJar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationUsage addAnnotation(String annotationName) {
/*  57 */     Matcher matcher = CLASS_PATTERN.matcher(annotationName);
/*  58 */     if (matcher.matches()) {
/*  59 */       AnnotationUsage usage = new AnnotationUsage(matcher.group(1));
/*  60 */       this.annotations.add(usage);
/*  61 */       return usage;
/*     */     } 
/*  63 */     Logger.warn("Unexpected annotation name: " + annotationName);
/*  64 */     return new AnnotationUsage(annotationName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<AnnotationUsage> getAnnotations() {
/*  74 */     return this.annotations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getNode() {
/*  83 */     return this.node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNode(Object node) {
/*  92 */     this.node = node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEntryPoint() {
/* 101 */     return this.isEntryPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntryPoint(boolean entryPoint) {
/* 110 */     this.isEntryPoint = entryPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReachable(boolean reachable) {
/* 119 */     this.isReachable = reachable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAccess() {
/* 128 */     return this.access;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSynthetic() {
/* 137 */     return ((this.access & 0x1000) == 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/* 146 */     return ((0x400 & this.access) == 1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getSourceJar() {
/* 155 */     return this.sourceJar;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/model/AbstractDescriptor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */