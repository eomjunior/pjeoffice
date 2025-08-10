/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.apache.tools.ant.BuildException;
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
/*     */ public class Assertions
/*     */   extends DataType
/*     */   implements Cloneable
/*     */ {
/*     */   private Boolean enableSystemAssertions;
/*  78 */   private ArrayList<BaseAssertion> assertionList = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addEnable(EnabledAssertion assertion) {
/*  86 */     checkChildrenAllowed();
/*  87 */     this.assertionList.add(assertion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDisable(DisabledAssertion assertion) {
/*  95 */     checkChildrenAllowed();
/*  96 */     this.assertionList.add(assertion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnableSystemAssertions(Boolean enableSystemAssertions) {
/* 106 */     checkAttributesAllowed();
/* 107 */     this.enableSystemAssertions = enableSystemAssertions;
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
/*     */   public void setRefid(Reference ref) {
/* 120 */     if (!this.assertionList.isEmpty() || this.enableSystemAssertions != null) {
/* 121 */       throw tooManyAttributes();
/*     */     }
/* 123 */     super.setRefid(ref);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Assertions getFinalReference() {
/* 131 */     if (getRefid() == null) {
/* 132 */       return this;
/*     */     }
/* 134 */     Object o = getRefid().getReferencedObject(getProject());
/* 135 */     if (!(o instanceof Assertions)) {
/* 136 */       throw new BuildException("reference is of wrong type");
/*     */     }
/* 138 */     return (Assertions)o;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 146 */     Assertions clause = getFinalReference();
/* 147 */     return clause.getFinalSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getFinalSize() {
/* 156 */     return this.assertionList.size() + ((this.enableSystemAssertions != null) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyAssertions(List<String> commandList) {
/* 165 */     getProject().log("Applying assertions", 4);
/* 166 */     Assertions clause = getFinalReference();
/*     */     
/* 168 */     if (Boolean.TRUE.equals(clause.enableSystemAssertions)) {
/* 169 */       getProject().log("Enabling system assertions", 4);
/* 170 */       commandList.add("-enablesystemassertions");
/* 171 */     } else if (Boolean.FALSE.equals(clause.enableSystemAssertions)) {
/* 172 */       getProject().log("disabling system assertions", 4);
/* 173 */       commandList.add("-disablesystemassertions");
/*     */     } 
/*     */ 
/*     */     
/* 177 */     for (BaseAssertion assertion : clause.assertionList) {
/* 178 */       String arg = assertion.toCommand();
/* 179 */       getProject().log("adding assertion " + arg, 4);
/* 180 */       commandList.add(arg);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyAssertions(CommandlineJava command) {
/* 189 */     Assertions clause = getFinalReference();
/*     */     
/* 191 */     if (Boolean.TRUE.equals(clause.enableSystemAssertions)) {
/* 192 */       addVmArgument(command, "-enablesystemassertions");
/* 193 */     } else if (Boolean.FALSE.equals(clause.enableSystemAssertions)) {
/* 194 */       addVmArgument(command, "-disablesystemassertions");
/*     */     } 
/*     */ 
/*     */     
/* 198 */     for (BaseAssertion assertion : clause.assertionList) {
/* 199 */       String arg = assertion.toCommand();
/* 200 */       addVmArgument(command, arg);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyAssertions(ListIterator<String> commandIterator) {
/* 210 */     getProject().log("Applying assertions", 4);
/* 211 */     Assertions clause = getFinalReference();
/*     */     
/* 213 */     if (Boolean.TRUE.equals(clause.enableSystemAssertions)) {
/* 214 */       getProject().log("Enabling system assertions", 4);
/* 215 */       commandIterator.add("-enablesystemassertions");
/* 216 */     } else if (Boolean.FALSE.equals(clause.enableSystemAssertions)) {
/* 217 */       getProject().log("disabling system assertions", 4);
/* 218 */       commandIterator.add("-disablesystemassertions");
/*     */     } 
/*     */ 
/*     */     
/* 222 */     for (BaseAssertion assertion : clause.assertionList) {
/* 223 */       String arg = assertion.toCommand();
/* 224 */       getProject().log("adding assertion " + arg, 4);
/* 225 */       commandIterator.add(arg);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addVmArgument(CommandlineJava command, String arg) {
/* 236 */     Commandline.Argument argument = command.createVmArgument();
/* 237 */     argument.setValue(arg);
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
/*     */   public Object clone() throws CloneNotSupportedException {
/* 249 */     Assertions that = (Assertions)super.clone();
/* 250 */     that.assertionList = new ArrayList<>(this.assertionList);
/* 251 */     return that;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class BaseAssertion
/*     */   {
/*     */     private String packageName;
/*     */ 
/*     */     
/*     */     private String className;
/*     */ 
/*     */ 
/*     */     
/*     */     public void setClass(String className) {
/* 266 */       this.className = className;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setPackage(String packageName) {
/* 274 */       this.packageName = packageName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected String getClassName() {
/* 283 */       return this.className;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected String getPackageName() {
/* 292 */       return this.packageName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract String getCommandPrefix();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toCommand() {
/* 308 */       if (getPackageName() != null && getClassName() != null) {
/* 309 */         throw new BuildException("Both package and class have been set");
/*     */       }
/* 311 */       StringBuilder command = new StringBuilder(getCommandPrefix());
/*     */       
/* 313 */       if (getPackageName() != null) {
/*     */         
/* 315 */         command.append(':');
/* 316 */         command.append(getPackageName());
/* 317 */         if (!command.toString().endsWith("..."))
/*     */         {
/* 319 */           command.append("...");
/*     */         }
/* 321 */       } else if (getClassName() != null) {
/*     */         
/* 323 */         command.append(':');
/* 324 */         command.append(getClassName());
/*     */       } 
/* 326 */       return command.toString();
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
/*     */   public static class EnabledAssertion
/*     */     extends BaseAssertion
/*     */   {
/*     */     public String getCommandPrefix() {
/* 341 */       return "-ea";
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
/*     */   public static class DisabledAssertion
/*     */     extends BaseAssertion
/*     */   {
/*     */     public String getCommandPrefix() {
/* 356 */       return "-da";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/Assertions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */