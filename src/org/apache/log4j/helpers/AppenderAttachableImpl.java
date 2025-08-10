/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import org.apache.log4j.Appender;
/*     */ import org.apache.log4j.spi.AppenderAttachable;
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
/*     */ public class AppenderAttachableImpl
/*     */   implements AppenderAttachable
/*     */ {
/*  35 */   private final COWArrayList<Appender> appenderList = new COWArrayList<Appender>(new Appender[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAppender(Appender newAppender) {
/*  43 */     if (newAppender == null) {
/*     */       return;
/*     */     }
/*  46 */     this.appenderList.addIfAbsent(newAppender);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int appendLoopOnAppenders(LoggingEvent event) {
/*  53 */     int size = 0;
/*  54 */     Appender[] appenderArray = this.appenderList.asTypedArray();
/*  55 */     int len = appenderArray.length;
/*  56 */     for (int i = 0; i < len; i++) {
/*  57 */       appenderArray[i].doAppend(event);
/*  58 */       size++;
/*     */     } 
/*  60 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getAllAppenders() {
/*  70 */     if (this.appenderList == null) {
/*  71 */       return null;
/*     */     }
/*  73 */     return Collections.enumeration(this.appenderList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Appender getAppender(String name) {
/*  84 */     if (this.appenderList == null || name == null) {
/*  85 */       return null;
/*     */     }
/*  87 */     for (Appender appender : this.appenderList) {
/*  88 */       if (name.equals(appender.getName())) {
/*  89 */         return appender;
/*     */       }
/*     */     } 
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAttached(Appender appender) {
/* 102 */     if (this.appenderList == null || appender == null) {
/* 103 */       return false;
/*     */     }
/* 105 */     for (Appender a : this.appenderList) {
/* 106 */       if (a == appender)
/* 107 */         return true; 
/*     */     } 
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAllAppenders() {
/* 116 */     for (Appender a : this.appenderList) {
/* 117 */       a.close();
/*     */     }
/* 119 */     this.appenderList.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAppender(Appender appender) {
/* 126 */     if (appender == null || this.appenderList == null)
/*     */       return; 
/* 128 */     this.appenderList.remove(appender);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAppender(String name) {
/* 136 */     if (name == null || this.appenderList == null) {
/*     */       return;
/*     */     }
/* 139 */     for (Appender a : (Appender[])this.appenderList.asTypedArray()) {
/* 140 */       if (name.equals(a.getName())) {
/* 141 */         this.appenderList.remove(a);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/AppenderAttachableImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */