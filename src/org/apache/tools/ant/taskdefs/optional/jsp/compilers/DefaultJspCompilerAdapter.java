/*     */ package org.apache.tools.ant.taskdefs.optional.jsp.compilers;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Vector;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.taskdefs.optional.jsp.JspC;
/*     */ import org.apache.tools.ant.types.CommandlineJava;
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
/*     */ public abstract class DefaultJspCompilerAdapter
/*     */   implements JspCompilerAdapter
/*     */ {
/*     */   protected JspC owner;
/*     */   
/*     */   protected void logAndAddFilesToCompile(JspC jspc, Vector<String> compileList, CommandlineJava cmd) {
/*  48 */     jspc.log("Compilation " + cmd.describeJavaCommand(), 3);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  54 */     String niceSourceList = compileList.stream().peek(arg -> cmd.createArgument().setValue(arg)).map(arg -> String.format("    %s%n", new Object[] { arg })).collect(Collectors.joining(""));
/*  55 */     jspc.log(String.format("File%s to be compiled:%n%s", new Object[] {
/*  56 */             (compileList.size() == 1) ? "" : "s", niceSourceList
/*     */           }), 3);
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
/*     */   public void setJspc(JspC owner) {
/*  74 */     this.owner = owner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JspC getJspc() {
/*  81 */     return this.owner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addArg(CommandlineJava cmd, String argument) {
/*  90 */     if (argument != null && !argument.isEmpty()) {
/*  91 */       cmd.createArgument().setValue(argument);
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
/*     */   protected void addArg(CommandlineJava cmd, String argument, String value) {
/* 103 */     if (value != null) {
/* 104 */       cmd.createArgument().setValue(argument);
/* 105 */       cmd.createArgument().setValue(value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addArg(CommandlineJava cmd, String argument, File file) {
/* 116 */     if (file != null) {
/* 117 */       cmd.createArgument().setValue(argument);
/* 118 */       cmd.createArgument().setFile(file);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean implementsOwnDependencyChecking() {
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Project getProject() {
/* 137 */     return getJspc().getProject();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/jsp/compilers/DefaultJspCompilerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */