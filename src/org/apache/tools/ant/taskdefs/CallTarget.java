/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.PropertySet;
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
/*     */ public class CallTarget
/*     */   extends Task
/*     */ {
/*     */   private Ant callee;
/*     */   private boolean inheritAll = true;
/*     */   private boolean inheritRefs = false;
/*     */   private boolean targetSet = false;
/*     */   
/*     */   public void setInheritAll(boolean inherit) {
/*  68 */     this.inheritAll = inherit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInheritRefs(boolean inheritRefs) {
/*  77 */     this.inheritRefs = inheritRefs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/*  85 */     this.callee = new Ant(this);
/*  86 */     this.callee.init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  95 */     if (this.callee == null) {
/*  96 */       init();
/*     */     }
/*  98 */     if (!this.targetSet) {
/*  99 */       throw new BuildException("Attribute target or at least one nested target is required.", 
/*     */           
/* 101 */           getLocation());
/*     */     }
/* 103 */     this.callee.setAntfile(getProject().getProperty("ant.file"));
/* 104 */     this.callee.setInheritAll(this.inheritAll);
/* 105 */     this.callee.setInheritRefs(this.inheritRefs);
/* 106 */     this.callee.execute();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Property createParam() {
/* 114 */     if (this.callee == null) {
/* 115 */       init();
/*     */     }
/* 117 */     return this.callee.createProperty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReference(Ant.Reference r) {
/* 127 */     if (this.callee == null) {
/* 128 */       init();
/*     */     }
/* 130 */     this.callee.addReference(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPropertyset(PropertySet ps) {
/* 139 */     if (this.callee == null) {
/* 140 */       init();
/*     */     }
/* 142 */     this.callee.addPropertyset(ps);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(String target) {
/* 150 */     if (this.callee == null) {
/* 151 */       init();
/*     */     }
/* 153 */     this.callee.setTarget(target);
/* 154 */     this.targetSet = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredTarget(Ant.TargetElement t) {
/* 163 */     if (this.callee == null) {
/* 164 */       init();
/*     */     }
/* 166 */     this.callee.addConfiguredTarget(t);
/* 167 */     this.targetSet = true;
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
/*     */   public void handleOutput(String output) {
/* 179 */     if (this.callee != null) {
/* 180 */       this.callee.handleOutput(output);
/*     */     } else {
/* 182 */       super.handleOutput(output);
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
/*     */   public int handleInput(byte[] buffer, int offset, int length) throws IOException {
/* 202 */     if (this.callee != null) {
/* 203 */       return this.callee.handleInput(buffer, offset, length);
/*     */     }
/* 205 */     return super.handleInput(buffer, offset, length);
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
/*     */   public void handleFlush(String output) {
/* 217 */     if (this.callee != null) {
/* 218 */       this.callee.handleFlush(output);
/*     */     } else {
/* 220 */       super.handleFlush(output);
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
/*     */   public void handleErrorOutput(String output) {
/* 234 */     if (this.callee != null) {
/* 235 */       this.callee.handleErrorOutput(output);
/*     */     } else {
/* 237 */       super.handleErrorOutput(output);
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
/*     */   public void handleErrorFlush(String output) {
/* 250 */     if (this.callee != null) {
/* 251 */       this.callee.handleErrorFlush(output);
/*     */     } else {
/* 253 */       super.handleErrorFlush(output);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/CallTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */