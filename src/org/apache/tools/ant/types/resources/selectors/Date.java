/*     */ package org.apache.tools.ant.types.resources.selectors;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Locale;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.TimeComparison;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Date
/*     */   implements ResourceSelector
/*     */ {
/*     */   private static final String MILLIS_OR_DATETIME = "Either the millis or the datetime attribute must be set.";
/*  41 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*  43 */   private Long millis = null;
/*  44 */   private String dateTime = null;
/*  45 */   private String pattern = null;
/*  46 */   private TimeComparison when = TimeComparison.EQUAL;
/*  47 */   private long granularity = FILE_UTILS.getFileTimestampGranularity();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setMillis(long m) {
/*  54 */     this.millis = Long.valueOf(m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized long getMillis() {
/*  62 */     return (this.millis == null) ? -1L : this.millis.longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setDateTime(String s) {
/*  70 */     this.dateTime = s;
/*  71 */     this.millis = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getDatetime() {
/*  79 */     return this.dateTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setGranularity(long g) {
/*  87 */     this.granularity = g;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized long getGranularity() {
/*  95 */     return this.granularity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setPattern(String p) {
/* 103 */     this.pattern = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getPattern() {
/* 111 */     return this.pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setWhen(TimeComparison c) {
/* 119 */     this.when = c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized TimeComparison getWhen() {
/* 127 */     return this.when;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean isSelected(Resource r) {
/* 136 */     if (this.dateTime == null && this.millis == null) {
/* 137 */       throw new BuildException("Either the millis or the datetime attribute must be set.");
/*     */     }
/* 139 */     if (this.millis == null) {
/* 140 */       String p = (this.pattern == null) ? "MM/dd/yyyy hh:mm a" : this.pattern;
/*     */ 
/*     */       
/* 143 */       DateFormat df = (this.pattern == null) ? new SimpleDateFormat(p, Locale.US) : new SimpleDateFormat(p);
/*     */       try {
/* 145 */         long m = df.parse(this.dateTime).getTime();
/* 146 */         if (m < 0L) {
/* 147 */           throw new BuildException("Date of %s results in negative milliseconds value relative to epoch (January 1, 1970, 00:00:00 GMT).", new Object[] { this.dateTime });
/*     */         }
/*     */ 
/*     */         
/* 151 */         setMillis(m);
/* 152 */       } catch (ParseException pe) {
/* 153 */         throw new BuildException("Date of %s Cannot be parsed correctly. It should be in '%s' format.", new Object[] { this.dateTime, p });
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 158 */     return this.when.evaluate(r.getLastModified(), this.millis.longValue(), this.granularity);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/selectors/Date.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */