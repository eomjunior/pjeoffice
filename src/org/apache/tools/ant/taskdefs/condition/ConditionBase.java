/*     */ package org.apache.tools.ant.taskdefs.condition;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.taskdefs.Available;
/*     */ import org.apache.tools.ant.taskdefs.Checksum;
/*     */ import org.apache.tools.ant.taskdefs.UpToDate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ConditionBase
/*     */   extends ProjectComponent
/*     */ {
/*  43 */   private String taskName = "condition";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   private List<Condition> conditions = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConditionBase() {
/*  54 */     this.taskName = "component";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConditionBase(String taskName) {
/*  63 */     this.taskName = taskName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int countConditions() {
/*  73 */     return this.conditions.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Enumeration<Condition> getConditions() {
/*  83 */     return Collections.enumeration(this.conditions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTaskName(String name) {
/*  94 */     this.taskName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTaskName() {
/* 104 */     return this.taskName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAvailable(Available a) {
/* 113 */     this.conditions.add(a);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addChecksum(Checksum c) {
/* 123 */     this.conditions.add(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addUptodate(UpToDate u) {
/* 133 */     this.conditions.add(u);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNot(Not n) {
/* 143 */     this.conditions.add(n);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnd(And a) {
/* 153 */     this.conditions.add(a);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOr(Or o) {
/* 163 */     this.conditions.add(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addEquals(Equals e) {
/* 173 */     this.conditions.add(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOs(Os o) {
/* 183 */     this.conditions.add(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIsSet(IsSet i) {
/* 193 */     this.conditions.add(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addHttp(Http h) {
/* 203 */     this.conditions.add(h);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSocket(Socket s) {
/* 213 */     this.conditions.add(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilesMatch(FilesMatch test) {
/* 223 */     this.conditions.add(test);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContains(Contains test) {
/* 233 */     this.conditions.add(test);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIsTrue(IsTrue test) {
/* 243 */     this.conditions.add(test);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIsFalse(IsFalse test) {
/* 253 */     this.conditions.add(test);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIsReference(IsReference i) {
/* 263 */     this.conditions.add(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIsFileSelected(IsFileSelected test) {
/* 271 */     this.conditions.add(test);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Condition c) {
/* 280 */     this.conditions.add(c);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/ConditionBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */