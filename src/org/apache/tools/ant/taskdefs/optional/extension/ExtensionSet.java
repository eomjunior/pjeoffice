/*     */ package org.apache.tools.ant.taskdefs.optional.extension;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.DataType;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Reference;
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
/*     */ public class ExtensionSet
/*     */   extends DataType
/*     */ {
/*  42 */   private final List<ExtensionAdapter> extensions = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   private final List<FileSet> extensionsFilesets = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExtension(ExtensionAdapter extensionAdapter) {
/*  55 */     if (isReference()) {
/*  56 */       throw noChildrenAllowed();
/*     */     }
/*  58 */     setChecked(false);
/*  59 */     this.extensions.add(extensionAdapter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLibfileset(LibFileSet fileSet) {
/*  68 */     if (isReference()) {
/*  69 */       throw noChildrenAllowed();
/*     */     }
/*  71 */     setChecked(false);
/*  72 */     this.extensionsFilesets.add(fileSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet fileSet) {
/*  81 */     if (isReference()) {
/*  82 */       throw noChildrenAllowed();
/*     */     }
/*  84 */     setChecked(false);
/*  85 */     this.extensionsFilesets.add(fileSet);
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
/*     */   public Extension[] toExtensions(Project proj) throws BuildException {
/*  97 */     if (isReference()) {
/*  98 */       return getRef().toExtensions(proj);
/*     */     }
/* 100 */     dieOnCircularReference();
/* 101 */     List<Extension> extensionsList = ExtensionUtil.toExtensions(this.extensions);
/* 102 */     ExtensionUtil.extractExtensions(proj, extensionsList, this.extensionsFilesets);
/* 103 */     return extensionsList.<Extension>toArray(new Extension[0]);
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
/*     */   public void setRefid(Reference reference) throws BuildException {
/* 119 */     if (!this.extensions.isEmpty() || !this.extensionsFilesets.isEmpty()) {
/* 120 */       throw tooManyAttributes();
/*     */     }
/* 122 */     super.setRefid(reference);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 128 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 131 */     if (isReference()) {
/* 132 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 134 */       for (ExtensionAdapter extensionAdapter : this.extensions) {
/* 135 */         pushAndInvokeCircularReferenceCheck(extensionAdapter, stk, p);
/*     */       }
/* 137 */       for (FileSet fileSet : this.extensionsFilesets) {
/* 138 */         pushAndInvokeCircularReferenceCheck((DataType)fileSet, stk, p);
/*     */       }
/* 140 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private ExtensionSet getRef() {
/* 145 */     return (ExtensionSet)getCheckedRef(ExtensionSet.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 154 */     return "ExtensionSet" + Arrays.<Extension>asList(toExtensions(getProject()));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/extension/ExtensionSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */