/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.util.LineOrientedOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LogOutputStream
/*     */   extends LineOrientedOutputStream
/*     */ {
/*     */   private ProjectComponent pc;
/*  38 */   private int level = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogOutputStream(ProjectComponent pc) {
/*  47 */     this.pc = pc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogOutputStream(Task task, int level) {
/*  57 */     this((ProjectComponent)task, level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LogOutputStream(ProjectComponent pc, int level) {
/*  68 */     this(pc);
/*  69 */     this.level = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processBuffer() {
/*     */     try {
/*  77 */       super.processBuffer();
/*  78 */     } catch (IOException e) {
/*     */       
/*  80 */       throw new RuntimeException("Impossible IOException caught: " + e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processLine(String line) {
/*  90 */     processLine(line, this.level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processLine(String line, int level) {
/* 100 */     this.pc.log(line, level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMessageLevel() {
/* 108 */     return this.level;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/LogOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */