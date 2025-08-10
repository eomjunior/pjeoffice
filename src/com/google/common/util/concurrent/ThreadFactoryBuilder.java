/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class ThreadFactoryBuilder
/*     */ {
/*     */   @CheckForNull
/*  52 */   private String nameFormat = null; @CheckForNull
/*  53 */   private Boolean daemon = null; @CheckForNull
/*  54 */   private Integer priority = null; @CheckForNull
/*  55 */   private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null; @CheckForNull
/*  56 */   private ThreadFactory backingThreadFactory = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public ThreadFactoryBuilder setNameFormat(String nameFormat) {
/*  74 */     String unused = format(nameFormat, new Object[] { Integer.valueOf(0) });
/*  75 */     this.nameFormat = nameFormat;
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public ThreadFactoryBuilder setDaemon(boolean daemon) {
/*  87 */     this.daemon = Boolean.valueOf(daemon);
/*  88 */     return this;
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
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public ThreadFactoryBuilder setPriority(int priority) {
/* 104 */     Preconditions.checkArgument((priority >= 1), "Thread priority (%s) must be >= %s", priority, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     Preconditions.checkArgument((priority <= 10), "Thread priority (%s) must be <= %s", priority, 10);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 114 */     this.priority = Integer.valueOf(priority);
/* 115 */     return this;
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
/*     */   @CanIgnoreReturnValue
/*     */   public ThreadFactoryBuilder setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
/* 128 */     this.uncaughtExceptionHandler = (Thread.UncaughtExceptionHandler)Preconditions.checkNotNull(uncaughtExceptionHandler);
/* 129 */     return this;
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
/*     */   @CanIgnoreReturnValue
/*     */   public ThreadFactoryBuilder setThreadFactory(ThreadFactory backingThreadFactory) {
/* 143 */     this.backingThreadFactory = (ThreadFactory)Preconditions.checkNotNull(backingThreadFactory);
/* 144 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadFactory build() {
/* 155 */     return doBuild(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ThreadFactory doBuild(ThreadFactoryBuilder builder) {
/* 161 */     final String nameFormat = builder.nameFormat;
/* 162 */     final Boolean daemon = builder.daemon;
/* 163 */     final Integer priority = builder.priority;
/* 164 */     final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
/*     */ 
/*     */ 
/*     */     
/* 168 */     final ThreadFactory backingThreadFactory = (builder.backingThreadFactory != null) ? builder.backingThreadFactory : Executors.defaultThreadFactory();
/* 169 */     final AtomicLong count = (nameFormat != null) ? new AtomicLong(0L) : null;
/* 170 */     return new ThreadFactory()
/*     */       {
/*     */         public Thread newThread(Runnable runnable) {
/* 173 */           Thread thread = backingThreadFactory.newThread(runnable);
/*     */           
/* 175 */           Objects.requireNonNull(thread);
/* 176 */           if (nameFormat != null)
/*     */           {
/* 178 */             thread.setName(ThreadFactoryBuilder.format(nameFormat, new Object[] { Long.valueOf(((AtomicLong)Objects.<AtomicLong>requireNonNull(this.val$count)).getAndIncrement()) }));
/*     */           }
/* 180 */           if (daemon != null) {
/* 181 */             thread.setDaemon(daemon.booleanValue());
/*     */           }
/* 183 */           if (priority != null) {
/* 184 */             thread.setPriority(priority.intValue());
/*     */           }
/* 186 */           if (uncaughtExceptionHandler != null) {
/* 187 */             thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
/*     */           }
/* 189 */           return thread;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static String format(String format, Object... args) {
/* 195 */     return String.format(Locale.ROOT, format, args);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/ThreadFactoryBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */