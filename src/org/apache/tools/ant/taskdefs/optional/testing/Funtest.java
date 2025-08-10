/*     */ package org.apache.tools.ant.taskdefs.optional.testing;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.TaskAdapter;
/*     */ import org.apache.tools.ant.taskdefs.Parallel;
/*     */ import org.apache.tools.ant.taskdefs.Sequential;
/*     */ import org.apache.tools.ant.taskdefs.WaitFor;
/*     */ import org.apache.tools.ant.taskdefs.condition.Condition;
/*     */ import org.apache.tools.ant.taskdefs.condition.ConditionBase;
/*     */ import org.apache.tools.ant.util.WorkerAnt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Funtest
/*     */   extends Task
/*     */ {
/*     */   public static final String WARN_OVERRIDING = "Overriding previous definition of ";
/*     */   public static final String APPLICATION_FORCIBLY_SHUT_DOWN = "Application forcibly shut down";
/*     */   public static final String SHUTDOWN_INTERRUPTED = "Shutdown interrupted";
/*     */   public static final String SKIPPING_TESTS = "Condition failed -skipping tests";
/*     */   public static final String APPLICATION_EXCEPTION = "Application Exception";
/*     */   public static final String TEARDOWN_EXCEPTION = "Teardown Exception";
/*     */   private NestedCondition condition;
/*     */   private Parallel timedTests;
/*     */   private Sequential setup;
/*     */   private Sequential application;
/*     */   private BlockFor block;
/*     */   private Sequential tests;
/*     */   private Sequential reporting;
/*     */   private Sequential teardown;
/*     */   private long timeout;
/* 126 */   private long timeoutUnitMultiplier = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   private long shutdownTime = 10000L;
/*     */   
/* 133 */   private long shutdownUnitMultiplier = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String failureProperty;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   private String failureMessage = "Tests failed";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean failOnTeardownErrors = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BuildException testException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BuildException teardownException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BuildException applicationException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BuildException taskException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logOverride(String name, Object definition) {
/* 181 */     if (definition != null) {
/* 182 */       log("Overriding previous definition of <" + name + '>', 2);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConditionBase createCondition() {
/* 192 */     logOverride("condition", this.condition);
/* 193 */     this.condition = new NestedCondition();
/* 194 */     return this.condition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addApplication(Sequential sequence) {
/* 202 */     logOverride("application", this.application);
/* 203 */     this.application = sequence;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSetup(Sequential sequence) {
/* 211 */     logOverride("setup", this.setup);
/* 212 */     this.setup = sequence;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBlock(BlockFor sequence) {
/* 220 */     logOverride("block", this.block);
/* 221 */     this.block = sequence;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTests(Sequential sequence) {
/* 229 */     logOverride("tests", this.tests);
/* 230 */     this.tests = sequence;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReporting(Sequential sequence) {
/* 238 */     logOverride("reporting", this.reporting);
/* 239 */     this.reporting = sequence;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTeardown(Sequential sequence) {
/* 247 */     logOverride("teardown", this.teardown);
/* 248 */     this.teardown = sequence;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnTeardownErrors(boolean failOnTeardownErrors) {
/* 256 */     this.failOnTeardownErrors = failOnTeardownErrors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailureMessage(String failureMessage) {
/* 264 */     this.failureMessage = failureMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailureProperty(String failureProperty) {
/* 272 */     this.failureProperty = failureProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShutdownTime(long shutdownTime) {
/* 280 */     this.shutdownTime = shutdownTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeout(long timeout) {
/* 288 */     this.timeout = timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeoutUnit(WaitFor.Unit unit) {
/* 296 */     this.timeoutUnitMultiplier = unit.getMultiplier();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShutdownUnit(WaitFor.Unit unit) {
/* 304 */     this.shutdownUnitMultiplier = unit.getMultiplier();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BuildException getApplicationException() {
/* 313 */     return this.applicationException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BuildException getTeardownException() {
/* 321 */     return this.teardownException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BuildException getTestException() {
/* 329 */     return this.testException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BuildException getTaskException() {
/* 337 */     return this.taskException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void bind(Task task) {
/* 345 */     task.bindToOwner(this);
/* 346 */     task.init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Parallel newParallel(long parallelTimeout) {
/* 355 */     Parallel par = new Parallel();
/* 356 */     bind((Task)par);
/* 357 */     par.setFailOnAny(true);
/* 358 */     par.setTimeout(parallelTimeout);
/* 359 */     return par;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Parallel newParallel(long parallelTimeout, Task child) {
/* 369 */     Parallel par = newParallel(parallelTimeout);
/* 370 */     par.addTask(child);
/* 371 */     return par;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateTask(Task task, String role) {
/* 380 */     if (task != null && task.getProject() == null) {
/* 381 */       throw new BuildException("%s task is not bound to the project %s", new Object[] { role, task });
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
/*     */   public void execute() throws BuildException {
/* 399 */     validateTask((Task)this.setup, "setup");
/* 400 */     validateTask((Task)this.application, "application");
/* 401 */     validateTask((Task)this.tests, "tests");
/* 402 */     validateTask((Task)this.reporting, "reporting");
/* 403 */     validateTask((Task)this.teardown, "teardown");
/*     */ 
/*     */ 
/*     */     
/* 407 */     if (this.condition != null && !this.condition.eval()) {
/*     */       
/* 409 */       log("Condition failed -skipping tests");
/*     */       
/*     */       return;
/*     */     } 
/* 413 */     long timeoutMillis = this.timeout * this.timeoutUnitMultiplier;
/*     */ 
/*     */     
/* 416 */     Parallel applicationRun = newParallel(timeoutMillis);
/*     */     
/* 418 */     WorkerAnt worker = new WorkerAnt((Task)applicationRun, null);
/* 419 */     if (this.application != null) {
/* 420 */       applicationRun.addTask((Task)this.application);
/*     */     }
/*     */ 
/*     */     
/* 424 */     long testRunTimeout = 0L;
/* 425 */     Sequential testRun = new Sequential();
/* 426 */     bind((Task)testRun);
/* 427 */     if (this.block != null) {
/*     */       
/* 429 */       TaskAdapter ta = new TaskAdapter(this.block);
/* 430 */       ta.bindToOwner(this);
/* 431 */       validateTask((Task)ta, "block");
/* 432 */       testRun.addTask((Task)ta);
/*     */       
/* 434 */       testRunTimeout = this.block.calculateMaxWaitMillis();
/*     */     } 
/*     */ 
/*     */     
/* 438 */     if (this.tests != null) {
/* 439 */       testRun.addTask((Task)this.tests);
/* 440 */       testRunTimeout += timeoutMillis;
/*     */     } 
/*     */     
/* 443 */     if (this.reporting != null) {
/* 444 */       testRun.addTask((Task)this.reporting);
/* 445 */       testRunTimeout += timeoutMillis;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 450 */     this.timedTests = newParallel(testRunTimeout, (Task)testRun);
/*     */ 
/*     */     
/*     */     try {
/* 454 */       if (this.setup != null) {
/* 455 */         Parallel setupRun = newParallel(timeoutMillis, (Task)this.setup);
/* 456 */         setupRun.execute();
/*     */       } 
/*     */       
/* 459 */       worker.start();
/*     */       
/* 461 */       this.timedTests.execute();
/* 462 */     } catch (BuildException e) {
/*     */       
/* 464 */       this.testException = e;
/*     */     } finally {
/*     */       
/* 467 */       if (this.teardown != null) {
/*     */         try {
/* 469 */           Parallel teardownRun = newParallel(timeoutMillis, (Task)this.teardown);
/* 470 */           teardownRun.execute();
/* 471 */         } catch (BuildException e) {
/* 472 */           this.teardownException = e;
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 482 */       long shutdownTimeMillis = this.shutdownTime * this.shutdownUnitMultiplier;
/* 483 */       worker.waitUntilFinished(shutdownTimeMillis);
/* 484 */       if (worker.isAlive()) {
/*     */         
/* 486 */         log("Application forcibly shut down", 1);
/* 487 */         worker.interrupt();
/* 488 */         worker.waitUntilFinished(shutdownTimeMillis);
/*     */       } 
/* 490 */     } catch (InterruptedException e) {
/*     */ 
/*     */       
/* 493 */       log("Shutdown interrupted", e, 3);
/*     */     } 
/* 495 */     this.applicationException = worker.getBuildException();
/*     */ 
/*     */ 
/*     */     
/* 499 */     processExceptions();
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
/*     */   protected void processExceptions() {
/* 515 */     this.taskException = this.testException;
/*     */ 
/*     */     
/* 518 */     if (this.applicationException != null) {
/* 519 */       if (this.taskException == null || this.taskException instanceof BuildTimeoutException) {
/* 520 */         this.taskException = this.applicationException;
/*     */       } else {
/* 522 */         ignoringThrowable("Application Exception", (Throwable)this.applicationException);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 527 */     if (this.teardownException != null) {
/* 528 */       if (this.taskException == null && this.failOnTeardownErrors) {
/* 529 */         this.taskException = this.teardownException;
/*     */       } else {
/*     */         
/* 532 */         ignoringThrowable("Teardown Exception", (Throwable)this.teardownException);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 537 */     if (this.failureProperty != null && 
/* 538 */       getProject().getProperty(this.failureProperty) != null) {
/*     */       
/* 540 */       log(this.failureMessage);
/* 541 */       if (this.taskException == null) {
/* 542 */         this.taskException = new BuildException(this.failureMessage);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 548 */     if (this.taskException != null) {
/* 549 */       throw this.taskException;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void ignoringThrowable(String type, Throwable thrown) {
/* 559 */     log(type + ": " + thrown.toString(), thrown, 1);
/*     */   }
/*     */   
/*     */   private static class NestedCondition
/*     */     extends ConditionBase implements Condition {
/*     */     private NestedCondition() {}
/*     */     
/*     */     public boolean eval() {
/* 567 */       if (countConditions() != 1) {
/* 568 */         throw new BuildException("A single nested condition is required.");
/*     */       }
/*     */       
/* 571 */       return ((Condition)getConditions().nextElement()).eval();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/testing/Funtest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */