/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import org.apache.tools.ant.dispatch.DispatchUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Task
/*     */   extends ProjectComponent
/*     */ {
/*     */   @Deprecated
/*     */   protected Target target;
/*     */   @Deprecated
/*     */   protected String taskName;
/*     */   @Deprecated
/*     */   protected String taskType;
/*     */   @Deprecated
/*     */   protected RuntimeConfigurable wrapper;
/*     */   private boolean invalid;
/*     */   private UnknownElement replacement;
/*     */   
/*     */   public void setOwningTarget(Target target) {
/*  95 */     this.target = target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Target getOwningTarget() {
/* 105 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTaskName(String name) {
/* 115 */     this.taskName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskName() {
/* 124 */     return this.taskName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTaskType(String type) {
/* 134 */     this.taskType = type;
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
/*     */   public void init() throws BuildException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RuntimeConfigurable getRuntimeConfigurableWrapper() {
/* 166 */     if (this.wrapper == null) {
/* 167 */       this.wrapper = new RuntimeConfigurable(this, getTaskName());
/*     */     }
/* 169 */     return this.wrapper;
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
/*     */   public void setRuntimeConfigurableWrapper(RuntimeConfigurable wrapper) {
/* 185 */     this.wrapper = wrapper;
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
/*     */   public void maybeConfigure() throws BuildException {
/* 200 */     if (this.invalid) {
/* 201 */       getReplacement();
/* 202 */     } else if (this.wrapper != null) {
/* 203 */       this.wrapper.maybeConfigure(getProject());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reconfigure() {
/* 211 */     if (this.wrapper != null) {
/* 212 */       this.wrapper.reconfigure(getProject());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleOutput(String output) {
/* 222 */     log(output, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleFlush(String output) {
/* 233 */     handleOutput(output);
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
/*     */   protected int handleInput(byte[] buffer, int offset, int length) throws IOException {
/* 250 */     return getProject().defaultInput(buffer, offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleErrorOutput(String output) {
/* 259 */     log(output, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleErrorFlush(String output) {
/* 270 */     handleErrorOutput(output);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(String msg) {
/* 279 */     log(msg, 2);
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
/*     */   public void log(String msg, int msgLevel) {
/* 291 */     if (getProject() == null) {
/* 292 */       super.log(msg, msgLevel);
/*     */     } else {
/* 294 */       getProject().log(this, msg, msgLevel);
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
/*     */   public void log(Throwable t, int msgLevel) {
/* 308 */     if (t != null) {
/* 309 */       log(t.getMessage(), t, msgLevel);
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
/*     */   public void log(String msg, Throwable t, int msgLevel) {
/* 324 */     if (getProject() == null) {
/* 325 */       super.log(msg, msgLevel);
/*     */     } else {
/* 327 */       getProject().log(this, msg, t, msgLevel);
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
/*     */   public final void perform() {
/* 341 */     if (this.invalid) {
/* 342 */       UnknownElement ue = getReplacement();
/* 343 */       Task task = ue.getTask();
/* 344 */       task.perform();
/*     */     } else {
/* 346 */       getProject().fireTaskStarted(this);
/* 347 */       Throwable reason = null;
/*     */       try {
/* 349 */         maybeConfigure();
/* 350 */         DispatchUtils.execute(this);
/* 351 */       } catch (BuildException ex) {
/* 352 */         if (ex.getLocation() == Location.UNKNOWN_LOCATION) {
/* 353 */           ex.setLocation(getLocation());
/*     */         }
/* 355 */         reason = ex;
/* 356 */         throw ex;
/* 357 */       } catch (Exception ex) {
/* 358 */         reason = ex;
/* 359 */         BuildException be = new BuildException(ex);
/* 360 */         be.setLocation(getLocation());
/* 361 */         throw be;
/* 362 */       } catch (Error ex) {
/* 363 */         reason = ex;
/* 364 */         throw ex;
/*     */       } finally {
/* 366 */         getProject().fireTaskFinished(this, reason);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void markInvalid() {
/* 376 */     this.invalid = true;
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
/*     */   protected final boolean isInvalid() {
/* 388 */     return this.invalid;
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
/*     */   private UnknownElement getReplacement() {
/* 404 */     if (this.replacement == null) {
/* 405 */       this.replacement = new UnknownElement(this.taskType);
/* 406 */       this.replacement.setProject(getProject());
/* 407 */       this.replacement.setTaskType(this.taskType);
/* 408 */       this.replacement.setTaskName(this.taskName);
/* 409 */       this.replacement.setLocation(getLocation());
/* 410 */       this.replacement.setOwningTarget(this.target);
/* 411 */       this.replacement.setRuntimeConfigurableWrapper(this.wrapper);
/* 412 */       this.wrapper.setProxy(this.replacement);
/* 413 */       replaceChildren(this.wrapper, this.replacement);
/* 414 */       this.target.replaceChild(this, this.replacement);
/* 415 */       this.replacement.maybeConfigure();
/*     */     } 
/* 417 */     return this.replacement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void replaceChildren(RuntimeConfigurable wrapper, UnknownElement parentElement) {
/* 428 */     for (RuntimeConfigurable childWrapper : Collections.<RuntimeConfigurable>list(wrapper.getChildren())) {
/* 429 */       UnknownElement childElement = new UnknownElement(childWrapper.getElementTag());
/* 430 */       parentElement.addChild(childElement);
/* 431 */       childElement.setProject(getProject());
/* 432 */       childElement.setRuntimeConfigurableWrapper(childWrapper);
/* 433 */       childWrapper.setProxy(childElement);
/* 434 */       replaceChildren(childWrapper, childElement);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskType() {
/* 444 */     return this.taskType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RuntimeConfigurable getWrapper() {
/* 453 */     return this.wrapper;
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
/*     */   public final void bindToOwner(Task owner) {
/* 469 */     setProject(owner.getProject());
/* 470 */     setOwningTarget(owner.getOwningTarget());
/* 471 */     setTaskName(owner.getTaskName());
/* 472 */     setDescription(owner.getDescription());
/* 473 */     setLocation(owner.getLocation());
/* 474 */     setTaskType(owner.getTaskType());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/Task.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */