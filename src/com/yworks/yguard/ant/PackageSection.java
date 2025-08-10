/*     */ package com.yworks.yguard.ant;
/*     */ 
/*     */ import com.yworks.common.ant.ZipScannerTool;
/*     */ import com.yworks.yguard.ObfuscatorTask;
/*     */ import com.yworks.yguard.obf.YGuardRule;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.PatternSet;
/*     */ import org.apache.tools.ant.types.ZipFileSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PackageSection
/*     */   implements Mappable
/*     */ {
/*     */   private String name;
/*     */   private String mapTo;
/*  28 */   protected List patternSets = new ArrayList(5);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean allowMatchAllPatternSet = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredPatternSet(PatternSet ps) {
/*  41 */     this.patternSets.add(ps);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  50 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addEntries(Collection<YGuardRule> entries, ZipFileSet zf) throws IOException {
/*  61 */     Project project = zf.getProject();
/*  62 */     Set<String> packages = new HashSet();
/*  63 */     if (this.name != null) {
/*  64 */       packages.add(ObfuscatorTask.toNativeClass(this.name));
/*     */     }
/*  66 */     for (Iterator<PatternSet> it = this.patternSets.iterator(); it.hasNext(); ) {
/*  67 */       PatternSet ps = it.next();
/*  68 */       DirectoryScanner scanner = zf.getDirectoryScanner(project);
/*  69 */       scanner.setIncludes(ObfuscatorTask.toNativePattern(ps.getIncludePatterns(project)));
/*  70 */       scanner.setExcludes(ObfuscatorTask.toNativePattern(ps.getExcludePatterns(project)));
/*  71 */       String[] matches = ZipScannerTool.getMatches(zf, scanner);
/*  72 */       for (int i = 0; i < matches.length; i++) {
/*  73 */         String match = matches[i];
/*  74 */         int slashIndex = match.lastIndexOf('/');
/*  75 */         if (match.endsWith(".class") || (match.endsWith("/") && slashIndex > 0)) {
/*  76 */           match = match.substring(0, slashIndex);
/*  77 */           packages.add(match);
/*     */         } 
/*     */       } 
/*     */     } 
/*  81 */     if (this.patternSets.isEmpty() && this.allowMatchAllPatternSet && this.name == null) {
/*  82 */       DirectoryScanner scanner = zf.getDirectoryScanner(project);
/*  83 */       scanner.setIncludes(new String[] { "**/*.class" });
/*  84 */       scanner.setExcludes(new String[0]);
/*  85 */       String[] matches = ZipScannerTool.getMatches(zf, scanner);
/*  86 */       for (int i = 0; i < matches.length; i++) {
/*  87 */         String match = matches[i];
/*  88 */         int slashIndex = match.lastIndexOf('/');
/*  89 */         if (match.endsWith(".class") || (match.endsWith("/") && slashIndex > 0)) {
/*  90 */           match = match.substring(0, slashIndex);
/*  91 */           packages.add(match);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  96 */     for (Iterator<String> iterator = packages.iterator(); iterator.hasNext(); ) {
/*  97 */       String pack = iterator.next();
/*  98 */       YGuardRule rule = new YGuardRule(11, pack);
/*  99 */       entries.add(rule);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMap(String mapTo) {
/* 109 */     this.mapTo = mapTo;
/*     */   }
/*     */   
/*     */   public void addMapEntries(Collection<YGuardRule> entries) {
/* 113 */     YGuardRule entry = new YGuardRule(4, ObfuscatorTask.toNativeClass(this.name));
/* 114 */     entry.obfName = ObfuscatorTask.toNativeClass(this.mapTo);
/* 115 */     entries.add(entry);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/ant/PackageSection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */