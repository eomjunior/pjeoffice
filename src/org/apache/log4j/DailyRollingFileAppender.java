/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DailyRollingFileAppender
/*     */   extends FileAppender
/*     */ {
/*     */   static final int TOP_OF_TROUBLE = -1;
/*     */   static final int TOP_OF_MINUTE = 0;
/*     */   static final int TOP_OF_HOUR = 1;
/*     */   static final int HALF_DAY = 2;
/*     */   static final int TOP_OF_DAY = 3;
/*     */   static final int TOP_OF_WEEK = 4;
/*     */   static final int TOP_OF_MONTH = 5;
/* 156 */   private String datePattern = "'.'yyyy-MM-dd";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String scheduledFilename;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   private long nextCheck = System.currentTimeMillis() - 1L;
/*     */   
/* 173 */   Date now = new Date();
/*     */   
/*     */   SimpleDateFormat sdf;
/*     */   
/* 177 */   RollingCalendar rc = new RollingCalendar();
/*     */   
/* 179 */   int checkPeriod = -1;
/*     */ 
/*     */   
/* 182 */   static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DailyRollingFileAppender() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DailyRollingFileAppender(Layout layout, String filename, String datePattern) throws IOException {
/* 197 */     super(layout, filename, true);
/* 198 */     this.datePattern = datePattern;
/* 199 */     activateOptions();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDatePattern(String pattern) {
/* 207 */     this.datePattern = pattern;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDatePattern() {
/* 212 */     return this.datePattern;
/*     */   }
/*     */   
/*     */   public void activateOptions() {
/* 216 */     super.activateOptions();
/* 217 */     if (this.datePattern != null && this.fileName != null) {
/* 218 */       this.now.setTime(System.currentTimeMillis());
/* 219 */       this.sdf = new SimpleDateFormat(this.datePattern);
/* 220 */       int type = computeCheckPeriod();
/* 221 */       printPeriodicity(type);
/* 222 */       this.rc.setType(type);
/* 223 */       File file = new File(this.fileName);
/* 224 */       this.scheduledFilename = this.fileName + this.sdf.format(new Date(file.lastModified()));
/*     */     } else {
/*     */       
/* 227 */       LogLog.error("Either File or DatePattern options are not set for appender [" + this.name + "].");
/*     */     } 
/*     */   }
/*     */   
/*     */   void printPeriodicity(int type) {
/* 232 */     switch (type) {
/*     */       case 0:
/* 234 */         LogLog.debug("Appender [" + this.name + "] to be rolled every minute.");
/*     */         return;
/*     */       case 1:
/* 237 */         LogLog.debug("Appender [" + this.name + "] to be rolled on top of every hour.");
/*     */         return;
/*     */       case 2:
/* 240 */         LogLog.debug("Appender [" + this.name + "] to be rolled at midday and midnight.");
/*     */         return;
/*     */       case 3:
/* 243 */         LogLog.debug("Appender [" + this.name + "] to be rolled at midnight.");
/*     */         return;
/*     */       case 4:
/* 246 */         LogLog.debug("Appender [" + this.name + "] to be rolled at start of week.");
/*     */         return;
/*     */       case 5:
/* 249 */         LogLog.debug("Appender [" + this.name + "] to be rolled at start of every month.");
/*     */         return;
/*     */     } 
/* 252 */     LogLog.warn("Unknown periodicity for appender [" + this.name + "].");
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
/*     */   int computeCheckPeriod() {
/* 266 */     RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone, Locale.getDefault());
/*     */     
/* 268 */     Date epoch = new Date(0L);
/* 269 */     if (this.datePattern != null) {
/* 270 */       for (int i = 0; i <= 5; i++) {
/* 271 */         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.datePattern);
/* 272 */         simpleDateFormat.setTimeZone(gmtTimeZone);
/* 273 */         String r0 = simpleDateFormat.format(epoch);
/* 274 */         rollingCalendar.setType(i);
/* 275 */         Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
/* 276 */         String r1 = simpleDateFormat.format(next);
/*     */         
/* 278 */         if (r0 != null && r1 != null && !r0.equals(r1)) {
/* 279 */           return i;
/*     */         }
/*     */       } 
/*     */     }
/* 283 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void rollOver() throws IOException {
/* 292 */     if (this.datePattern == null) {
/* 293 */       this.errorHandler.error("Missing DatePattern option in rollOver().");
/*     */       
/*     */       return;
/*     */     } 
/* 297 */     String datedFilename = this.fileName + this.sdf.format(this.now);
/*     */ 
/*     */ 
/*     */     
/* 301 */     if (this.scheduledFilename.equals(datedFilename)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 306 */     closeFile();
/*     */     
/* 308 */     File target = new File(this.scheduledFilename);
/* 309 */     if (target.exists()) {
/* 310 */       target.delete();
/*     */     }
/*     */     
/* 313 */     File file = new File(this.fileName);
/* 314 */     boolean result = file.renameTo(target);
/* 315 */     if (result) {
/* 316 */       LogLog.debug(this.fileName + " -> " + this.scheduledFilename);
/*     */     } else {
/* 318 */       LogLog.error("Failed to rename [" + this.fileName + "] to [" + this.scheduledFilename + "].");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 324 */       setFile(this.fileName, true, this.bufferedIO, this.bufferSize);
/* 325 */     } catch (IOException e) {
/* 326 */       this.errorHandler.error("setFile(" + this.fileName + ", true) call failed.");
/*     */     } 
/* 328 */     this.scheduledFilename = datedFilename;
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
/*     */   protected void subAppend(LoggingEvent event) {
/* 340 */     long n = System.currentTimeMillis();
/* 341 */     if (n >= this.nextCheck) {
/* 342 */       this.now.setTime(n);
/* 343 */       this.nextCheck = this.rc.getNextCheckMillis(this.now);
/*     */       try {
/* 345 */         rollOver();
/* 346 */       } catch (IOException ioe) {
/* 347 */         if (ioe instanceof java.io.InterruptedIOException) {
/* 348 */           Thread.currentThread().interrupt();
/*     */         }
/* 350 */         LogLog.error("rollOver() failed.", ioe);
/*     */       } 
/*     */     } 
/* 353 */     super.subAppend(event);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/DailyRollingFileAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */