/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TimeComparison
/*     */   extends EnumeratedAttribute
/*     */ {
/*  29 */   private static final String[] VALUES = new String[] { "before", "after", "equal" };
/*     */ 
/*     */   
/*  32 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */   
/*  35 */   public static final TimeComparison BEFORE = new TimeComparison("before");
/*     */ 
/*     */   
/*  38 */   public static final TimeComparison AFTER = new TimeComparison("after");
/*     */ 
/*     */   
/*  41 */   public static final TimeComparison EQUAL = new TimeComparison("equal");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeComparison() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeComparison(String value) {
/*  54 */     setValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getValues() {
/*  62 */     return VALUES;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean evaluate(long t1, long t2) {
/*  72 */     return evaluate(t1, t2, FILE_UTILS.getFileTimestampGranularity());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean evaluate(long t1, long t2, long g) {
/*  83 */     int cmp = getIndex();
/*  84 */     if (cmp == -1) {
/*  85 */       throw new BuildException("TimeComparison value not set.");
/*     */     }
/*  87 */     if (cmp == 0) {
/*  88 */       return (t1 - g < t2);
/*     */     }
/*  90 */     if (cmp == 1) {
/*  91 */       return (t1 + g > t2);
/*     */     }
/*  93 */     return (Math.abs(t1 - t2) <= g);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int compare(long t1, long t2) {
/* 104 */     return compare(t1, t2, FILE_UTILS.getFileTimestampGranularity());
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
/*     */   public static int compare(long t1, long t2, long g) {
/* 116 */     long diff = t1 - t2;
/* 117 */     long abs = Math.abs(diff);
/* 118 */     return (abs > Math.abs(g)) ? (int)(diff / abs) : 0;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/TimeComparison.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */