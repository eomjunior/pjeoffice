/*     */ package org.apache.tools.ant.util.facade;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Path;
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
/*     */ public class FacadeTaskHelper
/*     */ {
/*  42 */   private List<ImplementationSpecificArgument> args = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String userChoice;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String magicValue;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String defaultValue;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Path implementationClasspath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FacadeTaskHelper(String defaultValue) {
/*  69 */     this(defaultValue, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FacadeTaskHelper(String defaultValue, String magicValue) {
/*  79 */     this.defaultValue = defaultValue;
/*  80 */     this.magicValue = magicValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMagicValue(String magicValue) {
/*  88 */     this.magicValue = magicValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImplementation(String userChoice) {
/*  96 */     this.userChoice = userChoice;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getImplementation() {
/* 104 */     return (this.userChoice != null) ? this.userChoice : (
/* 105 */       (this.magicValue != null) ? this.magicValue : 
/* 106 */       this.defaultValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExplicitChoice() {
/* 114 */     return this.userChoice;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addImplementationArgument(ImplementationSpecificArgument arg) {
/* 122 */     this.args.add(arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getArgs() {
/* 131 */     String implementation = getImplementation();
/* 132 */     return (String[])this.args.stream().map(arg -> arg.getParts(implementation))
/* 133 */       .filter(Objects::nonNull).flatMap(Stream::of)
/* 134 */       .toArray(x$0 -> new String[x$0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasBeenSet() {
/* 144 */     return (this.userChoice != null || this.magicValue != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getImplementationClasspath(Project project) {
/* 155 */     if (this.implementationClasspath == null) {
/* 156 */       this.implementationClasspath = new Path(project);
/*     */     }
/* 158 */     return this.implementationClasspath;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/facade/FacadeTaskHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */