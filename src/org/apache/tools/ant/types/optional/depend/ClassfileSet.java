/*     */ package org.apache.tools.ant.types.optional.depend;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.DataType;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.util.StringUtils;
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
/*     */ public class ClassfileSet
/*     */   extends FileSet
/*     */ {
/*  44 */   private List<String> rootClasses = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   private List<FileSet> rootFileSets = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassfileSet() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ClassRoot
/*     */   {
/*     */     private String rootClass;
/*     */ 
/*     */ 
/*     */     
/*     */     public void setClassname(String name) {
/*  64 */       this.rootClass = name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getClassname() {
/*  73 */       return this.rootClass;
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
/*     */   protected ClassfileSet(ClassfileSet s) {
/*  89 */     super(s);
/*  90 */     this.rootClasses.addAll(s.rootClasses);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addRootFileset(FileSet rootFileSet) {
/* 101 */     this.rootFileSets.add(rootFileSet);
/* 102 */     setChecked(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRootClass(String rootClass) {
/* 111 */     this.rootClasses.add(rootClass);
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
/*     */   public DirectoryScanner getDirectoryScanner(Project p) {
/* 123 */     if (isReference()) {
/* 124 */       return getRef(p).getDirectoryScanner(p);
/*     */     }
/* 126 */     dieOnCircularReference(p);
/* 127 */     DirectoryScanner parentScanner = super.getDirectoryScanner(p);
/* 128 */     DependScanner scanner = new DependScanner(parentScanner);
/* 129 */     Vector<String> allRootClasses = new Vector<>(this.rootClasses);
/* 130 */     for (FileSet additionalRootSet : this.rootFileSets) {
/*     */       
/* 132 */       DirectoryScanner additionalScanner = additionalRootSet.getDirectoryScanner(p);
/* 133 */       for (String file : additionalScanner.getIncludedFiles()) {
/* 134 */         if (file.endsWith(".class")) {
/* 135 */           String classFilePath = StringUtils.removeSuffix(file, ".class");
/*     */           
/* 137 */           String className = classFilePath.replace('/', '.').replace('\\', '.');
/* 138 */           allRootClasses.addElement(className);
/*     */         } 
/*     */       } 
/* 141 */       scanner.addBasedir(additionalRootSet.getDir(p));
/*     */     } 
/* 143 */     scanner.setBasedir(getDir(p));
/* 144 */     scanner.setRootClasses(allRootClasses);
/* 145 */     scanner.scan();
/* 146 */     return scanner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredRoot(ClassRoot root) {
/* 155 */     this.rootClasses.add(root.getClassname());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 165 */     return new ClassfileSet(isReference() ? getRef() : this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) {
/* 170 */     if (isChecked()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 175 */     super.dieOnCircularReference(stk, p);
/*     */     
/* 177 */     if (!isReference()) {
/* 178 */       for (FileSet additionalRootSet : this.rootFileSets) {
/* 179 */         pushAndInvokeCircularReferenceCheck((DataType)additionalRootSet, stk, p);
/*     */       }
/* 181 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private ClassfileSet getRef() {
/* 186 */     return (ClassfileSet)getCheckedRef(ClassfileSet.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/optional/depend/ClassfileSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */