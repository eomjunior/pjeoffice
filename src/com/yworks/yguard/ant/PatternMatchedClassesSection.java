/*    */ package com.yworks.yguard.ant;
/*    */ 
/*    */ import com.yworks.common.ant.ZipScannerTool;
/*    */ import com.yworks.yguard.ObfuscatorTask;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.apache.tools.ant.DirectoryScanner;
/*    */ import org.apache.tools.ant.Project;
/*    */ import org.apache.tools.ant.types.PatternSet;
/*    */ import org.apache.tools.ant.types.ZipFileSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PatternMatchedClassesSection
/*    */ {
/* 27 */   protected List patternSets = new ArrayList(5);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   protected final Map properties = new HashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean allowMatchAllPatternSet = false;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addConfiguredPatternSet(PatternSet ps) {
/* 45 */     this.patternSets.add(ps);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addConfiguredProperty(Property p) {
/* 54 */     this.properties.put(p.getName(), p.getValue());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List getPatternSets() {
/* 63 */     return this.patternSets;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addEntries(Collection entries, ZipFileSet zf) throws IOException {
/* 74 */     Project project = zf.getProject();
/* 75 */     for (Iterator<PatternSet> it = this.patternSets.iterator(); it.hasNext(); ) {
/*    */       
/* 77 */       PatternSet ps = it.next();
/* 78 */       DirectoryScanner scanner = zf.getDirectoryScanner(project);
/* 79 */       scanner.setIncludes(ObfuscatorTask.toNativePattern(ps.getIncludePatterns(project)));
/* 80 */       scanner.setExcludes(ObfuscatorTask.toNativePattern(ps.getExcludePatterns(project)));
/* 81 */       String[] matches = ZipScannerTool.getMatches(zf, scanner);
/* 82 */       for (int i = 0; i < matches.length; i++) {
/* 83 */         String match = matches[i];
/* 84 */         if (match.endsWith(".class")) {
/* 85 */           match = match.substring(0, match.length() - 6);
/* 86 */           addEntries(entries, match);
/*    */         } 
/*    */       } 
/*    */     } 
/* 90 */     if (this.patternSets.isEmpty() && this.allowMatchAllPatternSet) {
/* 91 */       DirectoryScanner scanner = zf.getDirectoryScanner(project);
/* 92 */       scanner.setIncludes(new String[] { "**/*.class" });
/* 93 */       scanner.setExcludes(new String[0]);
/* 94 */       String[] matches = ZipScannerTool.getMatches(zf, scanner);
/* 95 */       for (int i = 0; i < matches.length; i++) {
/* 96 */         String match = matches[i];
/* 97 */         if (match.endsWith(".class")) {
/* 98 */           match = match.substring(0, match.length() - 6);
/* 99 */           addEntries(entries, match);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void addEntries(Collection paramCollection, String paramString);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/ant/PatternMatchedClassesSection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */