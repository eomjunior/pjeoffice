/*     */ package com.yworks.yguard;
/*     */ 
/*     */ import com.yworks.common.ShrinkBag;
/*     */ import com.yworks.common.ant.AttributesSection;
/*     */ import com.yworks.common.ant.Exclude;
/*     */ import com.yworks.common.ant.YGuardBaseTask;
/*     */ import com.yworks.logging.Logger;
/*     */ import com.yworks.yguard.obf.Cl;
/*     */ import com.yworks.yshrink.ant.ShrinkTask;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class YGuardTask
/*     */   extends YGuardBaseTask
/*     */ {
/*  26 */   protected List<YGuardBaseTask> subTasks = new ArrayList<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  31 */     super.execute();
/*     */ 
/*     */     
/*  34 */     if (null != this.pairs) {
/*  35 */       for (ShrinkBag pair : this.pairs) {
/*  36 */         for (YGuardBaseTask subTask : this.subTasks) {
/*  37 */           subTask.addConfiguredInOutPair(pair);
/*     */         }
/*     */       } 
/*     */     } else {
/*  41 */       throw new BuildException("No inoutpairs given. At least one inoutpair has to be specified.");
/*     */     } 
/*     */ 
/*     */     
/*  45 */     if (null != this.resourceClassPath) {
/*  46 */       for (YGuardBaseTask subTask : this.subTasks) {
/*  47 */         subTask.setResourceClassPath(this.resourceClassPath);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*  52 */     if (null != this.attributesSections) {
/*  53 */       for (YGuardBaseTask subTask : this.subTasks) {
/*  54 */         subTask.addAttributesSections(this.attributesSections);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     Collections.sort(this.subTasks, new Comparator<YGuardBaseTask>()
/*     */         {
/*     */           public int compare(YGuardBaseTask o1, YGuardBaseTask o2)
/*     */           {
/*  75 */             if (o1 instanceof ShrinkTask) {
/*  76 */               return 0;
/*     */             }
/*  78 */             return 1;
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     int taskNum = 0;
/*  86 */     File[] outFiles = new File[this.pairs.size()];
/*  87 */     File[] tempFiles = new File[this.pairs.size()];
/*     */     
/*  89 */     for (YGuardBaseTask subTask : this.subTasks) {
/*     */       
/*  91 */       for (int i = 0; i < this.pairs.size(); i++) {
/*  92 */         ShrinkBag pair = this.pairs.get(i);
/*     */         
/*  94 */         if (0 == taskNum) {
/*  95 */           outFiles[i] = pair.getOut();
/*     */         } else {
/*  97 */           if (taskNum > 1) {
/*  98 */             pair.getIn().delete();
/*     */           }
/* 100 */           pair.setIn(pair.getOut());
/*     */         } 
/*     */         
/* 103 */         if (taskNum == this.subTasks.size() - 1) {
/* 104 */           pair.setOut(outFiles[i]);
/*     */         } else {
/* 106 */           File tempFile = getTempFile(pair.getOut());
/* 107 */           tempFiles[i] = tempFile;
/* 108 */           pair.setOut(tempFile);
/*     */         } 
/*     */         
/* 111 */         if (taskNum > 1) {
/* 112 */           tempFiles[taskNum * (this.pairs.size() - 1) + i].delete();
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 118 */       subTask.execute();
/*     */       
/* 120 */       taskNum++;
/*     */     } 
/*     */     
/* 123 */     if (this.subTasks.size() > 1) {
/* 124 */       for (File tempFile : tempFiles) {
/* 125 */         tempFile.delete();
/*     */       }
/*     */     }
/*     */     
/* 129 */     Cl.setClassResolver(null);
/*     */   }
/*     */ 
/*     */   
/*     */   private File getTempFile(File origFile) {
/*     */     try {
/* 135 */       File folder = new File(origFile.getParent());
/* 136 */       if (folder.exists()) {
/* 137 */         File tempFile = File.createTempFile("yguard_temp_", ".jar", folder);
/* 138 */         tempFile.deleteOnExit();
/* 139 */         return tempFile;
/*     */       } 
/* 141 */       System.out.println("could not create temp file for " + origFile + " - parent folder does not exist: " + folder);
/* 142 */       return null;
/*     */     
/*     */     }
/* 145 */     catch (IOException e) {
/* 146 */       Logger.err("could not create temp file for " + origFile, e);
/* 147 */       throw new BuildException("could not create temp file for " + origFile);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShrinkTask createShrink() {
/* 157 */     ShrinkTask shrinkTask = newShrinkTask(true);
/* 158 */     configureSubTask((Task)shrinkTask);
/* 159 */     this.subTasks.add(shrinkTask);
/* 160 */     return shrinkTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ShrinkTask newShrinkTask(boolean mode) {
/* 170 */     return new ShrinkTask(mode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObfuscatorTask createRename() {
/* 179 */     ObfuscatorTask obfuscatorTask = newObfuscatorTask(true);
/* 180 */     configureSubTask((Task)obfuscatorTask);
/* 181 */     this.subTasks.add(obfuscatorTask);
/* 182 */     return obfuscatorTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ObfuscatorTask newObfuscatorTask(boolean mode) {
/* 192 */     return new ObfuscatorTask(mode);
/*     */   }
/*     */   
/*     */   private void configureSubTask(Task task) {
/* 196 */     task.setProject(getProject());
/* 197 */     task.setOwningTarget(getOwningTarget());
/* 198 */     task.setTaskName(getTaskName());
/* 199 */     task.setLocation(getLocation());
/* 200 */     task.setDescription(getDescription());
/* 201 */     task.init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObfuscatorTask createObfuscate() {
/* 210 */     return createRename();
/*     */   }
/*     */   
/*     */   public Exclude createKeep() {
/* 214 */     throw new BuildException("The keep element is allowed only in nested subtasks of the yguard task.");
/*     */   }
/*     */   
/*     */   public void addAttributesSections(List<AttributesSection> attributesSections) {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/YGuardTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */