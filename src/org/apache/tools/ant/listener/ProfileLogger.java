/*     */ package org.apache.tools.ant.listener;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.tools.ant.BuildEvent;
/*     */ import org.apache.tools.ant.DefaultLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProfileLogger
/*     */   extends DefaultLogger
/*     */ {
/*  34 */   private Map<Object, Date> profileData = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void targetStarted(BuildEvent event) {
/*  45 */     Date now = new Date();
/*  46 */     String name = "Target " + event.getTarget().getName();
/*  47 */     logStart(event, now, name);
/*  48 */     this.profileData.put(event.getTarget(), now);
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
/*     */   public void targetFinished(BuildEvent event) {
/*  60 */     Date start = this.profileData.remove(event.getTarget());
/*  61 */     String name = "Target " + event.getTarget().getName();
/*  62 */     logFinish(event, start, name);
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
/*     */   public void taskStarted(BuildEvent event) {
/*  74 */     String name = event.getTask().getTaskName();
/*  75 */     Date now = new Date();
/*  76 */     logStart(event, now, name);
/*  77 */     this.profileData.put(event.getTask(), now);
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
/*     */   public void taskFinished(BuildEvent event) {
/*  89 */     Date start = this.profileData.remove(event.getTask());
/*  90 */     String name = event.getTask().getTaskName();
/*  91 */     logFinish(event, start, name);
/*     */   }
/*     */   private void logFinish(BuildEvent event, Date start, String name) {
/*     */     String msg;
/*  95 */     Date now = new Date();
/*     */     
/*  97 */     if (start != null) {
/*  98 */       long diff = now.getTime() - start.getTime();
/*  99 */       msg = String.format("%n%s: finished %s (%d)", new Object[] { name, now, Long.valueOf(diff) });
/*     */     } else {
/* 101 */       msg = String.format("%n%s: finished %s (unknown duration, start not detected)", new Object[] { name, now });
/*     */     } 
/*     */     
/* 104 */     printMessage(msg, this.out, event.getPriority());
/* 105 */     log(msg);
/*     */   }
/*     */   
/*     */   private void logStart(BuildEvent event, Date start, String name) {
/* 109 */     String msg = String.format("%n%s: started %s", new Object[] { name, start });
/* 110 */     printMessage(msg, this.out, event.getPriority());
/* 111 */     log(msg);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/listener/ProfileLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */