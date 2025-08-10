/*     */ package org.apache.log4j.chainsaw;
/*     */ 
/*     */ import org.apache.log4j.Priority;
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
/*     */ class EventDetails
/*     */ {
/*     */   private final long mTimeStamp;
/*     */   private final Priority mPriority;
/*     */   private final String mCategoryName;
/*     */   private final String mNDC;
/*     */   private final String mThreadName;
/*     */   private final String mMessage;
/*     */   private final String[] mThrowableStrRep;
/*     */   private final String mLocationDetails;
/*     */   
/*     */   EventDetails(long aTimeStamp, Priority aPriority, String aCategoryName, String aNDC, String aThreadName, String aMessage, String[] aThrowableStrRep, String aLocationDetails) {
/*  62 */     this.mTimeStamp = aTimeStamp;
/*  63 */     this.mPriority = aPriority;
/*  64 */     this.mCategoryName = aCategoryName;
/*  65 */     this.mNDC = aNDC;
/*  66 */     this.mThreadName = aThreadName;
/*  67 */     this.mMessage = aMessage;
/*  68 */     this.mThrowableStrRep = aThrowableStrRep;
/*  69 */     this.mLocationDetails = aLocationDetails;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   EventDetails(LoggingEvent aEvent) {
/*  79 */     this(aEvent.timeStamp, (Priority)aEvent.getLevel(), aEvent.getLoggerName(), aEvent.getNDC(), aEvent.getThreadName(), aEvent
/*  80 */         .getRenderedMessage(), aEvent.getThrowableStrRep(), 
/*  81 */         (aEvent.getLocationInformation() == null) ? null : (aEvent.getLocationInformation()).fullInfo);
/*     */   }
/*     */ 
/*     */   
/*     */   long getTimeStamp() {
/*  86 */     return this.mTimeStamp;
/*     */   }
/*     */ 
/*     */   
/*     */   Priority getPriority() {
/*  91 */     return this.mPriority;
/*     */   }
/*     */ 
/*     */   
/*     */   String getCategoryName() {
/*  96 */     return this.mCategoryName;
/*     */   }
/*     */ 
/*     */   
/*     */   String getNDC() {
/* 101 */     return this.mNDC;
/*     */   }
/*     */ 
/*     */   
/*     */   String getThreadName() {
/* 106 */     return this.mThreadName;
/*     */   }
/*     */ 
/*     */   
/*     */   String getMessage() {
/* 111 */     return this.mMessage;
/*     */   }
/*     */ 
/*     */   
/*     */   String getLocationDetails() {
/* 116 */     return this.mLocationDetails;
/*     */   }
/*     */ 
/*     */   
/*     */   String[] getThrowableStrRep() {
/* 121 */     return this.mThrowableStrRep;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/chainsaw/EventDetails.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */