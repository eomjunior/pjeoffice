/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.taskdefs.condition.Condition;
/*     */ import org.apache.tools.ant.taskdefs.condition.ConditionBase;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WaitFor
/*     */   extends ConditionBase
/*     */ {
/*     */   public static final long ONE_MILLISECOND = 1L;
/*     */   public static final long ONE_SECOND = 1000L;
/*     */   public static final long ONE_MINUTE = 60000L;
/*     */   public static final long ONE_HOUR = 3600000L;
/*     */   public static final long ONE_DAY = 86400000L;
/*     */   public static final long ONE_WEEK = 604800000L;
/*     */   public static final long DEFAULT_MAX_WAIT_MILLIS = 180000L;
/*     */   public static final long DEFAULT_CHECK_MILLIS = 500L;
/*  76 */   private long maxWait = 180000L;
/*  77 */   private long maxWaitMultiplier = 1L;
/*     */ 
/*     */ 
/*     */   
/*  81 */   private long checkEvery = 500L;
/*  82 */   private long checkEveryMultiplier = 1L;
/*     */ 
/*     */   
/*     */   private String timeoutProperty;
/*     */ 
/*     */   
/*     */   public WaitFor() {
/*  89 */     super("waitfor");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WaitFor(String taskName) {
/*  99 */     super(taskName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxWait(long time) {
/* 107 */     this.maxWait = time;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxWaitUnit(Unit unit) {
/* 115 */     this.maxWaitMultiplier = unit.getMultiplier();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCheckEvery(long time) {
/* 123 */     this.checkEvery = time;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCheckEveryUnit(Unit unit) {
/* 131 */     this.checkEveryMultiplier = unit.getMultiplier();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeoutProperty(String p) {
/* 139 */     this.timeoutProperty = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 148 */     if (countConditions() > 1)
/* 149 */       throw new BuildException("You must not nest more than one condition into %s", new Object[] {
/*     */             
/* 151 */             getTaskName()
/*     */           }); 
/* 153 */     if (countConditions() < 1)
/* 154 */       throw new BuildException("You must nest a condition into %s", new Object[] {
/* 155 */             getTaskName()
/*     */           }); 
/* 157 */     Condition c = getConditions().nextElement();
/*     */     try {
/* 159 */       long maxWaitMillis = calculateMaxWaitMillis();
/* 160 */       long checkEveryMillis = calculateCheckEveryMillis();
/* 161 */       long start = System.currentTimeMillis();
/* 162 */       long end = start + maxWaitMillis;
/*     */       
/* 164 */       while (System.currentTimeMillis() < end) {
/* 165 */         if (c.eval()) {
/* 166 */           processSuccess();
/*     */           return;
/*     */         } 
/* 169 */         Thread.sleep(checkEveryMillis);
/*     */       } 
/* 171 */     } catch (InterruptedException e) {
/* 172 */       log("Task " + getTaskName() + " interrupted, treating as timed out.");
/*     */     } 
/*     */     
/* 175 */     processTimeout();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long calculateCheckEveryMillis() {
/* 184 */     return this.checkEvery * this.checkEveryMultiplier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long calculateMaxWaitMillis() {
/* 193 */     return this.maxWait * this.maxWaitMultiplier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processSuccess() {
/* 202 */     log(getTaskName() + ": condition was met", 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processTimeout() {
/* 213 */     log(getTaskName() + ": timeout", 3);
/* 214 */     if (this.timeoutProperty != null) {
/* 215 */       getProject().setNewProperty(this.timeoutProperty, "true");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Unit
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public static final String MILLISECOND = "millisecond";
/*     */ 
/*     */     
/*     */     public static final String SECOND = "second";
/*     */ 
/*     */     
/*     */     public static final String MINUTE = "minute";
/*     */ 
/*     */     
/*     */     public static final String HOUR = "hour";
/*     */     
/*     */     public static final String DAY = "day";
/*     */     
/*     */     public static final String WEEK = "week";
/*     */     
/* 239 */     private static final String[] UNITS = new String[] { "millisecond", "second", "minute", "hour", "day", "week" };
/*     */ 
/*     */ 
/*     */     
/* 243 */     private Map<String, Long> timeTable = new HashMap<>();
/*     */ 
/*     */     
/*     */     public Unit() {
/* 247 */       this.timeTable.put("millisecond", Long.valueOf(1L));
/* 248 */       this.timeTable.put("second", Long.valueOf(1000L));
/* 249 */       this.timeTable.put("minute", Long.valueOf(60000L));
/* 250 */       this.timeTable.put("hour", Long.valueOf(3600000L));
/* 251 */       this.timeTable.put("day", Long.valueOf(86400000L));
/* 252 */       this.timeTable.put("week", Long.valueOf(604800000L));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getMultiplier() {
/* 260 */       String key = getValue().toLowerCase(Locale.ENGLISH);
/* 261 */       return ((Long)this.timeTable.get(key)).longValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 270 */       return UNITS;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/WaitFor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */