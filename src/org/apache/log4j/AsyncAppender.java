/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.log4j.helpers.AppenderAttachableImpl;
/*     */ import org.apache.log4j.helpers.LogLog;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncAppender
/*     */   extends AppenderSkeleton
/*     */   implements AppenderAttachable
/*     */ {
/*     */   public static final int DEFAULT_BUFFER_SIZE = 128;
/*  65 */   private final List buffer = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   private final Map discardMap = new HashMap<Object, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   private int bufferSize = 128;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AppenderAttachableImpl aai;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AppenderAttachableImpl appenders;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Thread dispatcher;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean locationInfo = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean blocking = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncAppender() {
/* 104 */     this.appenders = new AppenderAttachableImpl();
/*     */ 
/*     */ 
/*     */     
/* 108 */     this.aai = this.appenders;
/*     */     
/* 110 */     this.dispatcher = new Thread(new Dispatcher(this, this.buffer, this.discardMap, this.appenders));
/*     */ 
/*     */ 
/*     */     
/* 114 */     this.dispatcher.setDaemon(true);
/*     */ 
/*     */ 
/*     */     
/* 118 */     this.dispatcher.setName("AsyncAppender-Dispatcher-" + this.dispatcher.getName());
/* 119 */     this.dispatcher.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAppender(Appender newAppender) {
/* 128 */     synchronized (this.appenders) {
/* 129 */       this.appenders.addAppender(newAppender);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LoggingEvent event) {
/* 141 */     if (this.dispatcher == null || !this.dispatcher.isAlive() || this.bufferSize <= 0) {
/* 142 */       synchronized (this.appenders) {
/* 143 */         this.appenders.appendLoopOnAppenders(event);
/*     */       } 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 151 */     event.getNDC();
/* 152 */     event.getThreadName();
/*     */     
/* 154 */     event.getMDCCopy();
/* 155 */     if (this.locationInfo) {
/* 156 */       event.getLocationInformation();
/*     */     }
/* 158 */     event.getRenderedMessage();
/* 159 */     event.getThrowableStrRep();
/*     */     
/* 161 */     synchronized (this.buffer) {
/*     */       while (true) {
/* 163 */         int previousSize = this.buffer.size();
/*     */         
/* 165 */         if (previousSize < this.bufferSize) {
/* 166 */           this.buffer.add(event);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 173 */           if (previousSize == 0) {
/* 174 */             this.buffer.notifyAll();
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 187 */         boolean discard = true;
/* 188 */         if (this.blocking && !Thread.interrupted() && Thread.currentThread() != this.dispatcher) {
/*     */           try {
/* 190 */             this.buffer.wait();
/* 191 */             discard = false;
/* 192 */           } catch (InterruptedException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 197 */             Thread.currentThread().interrupt();
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 205 */         if (discard) {
/* 206 */           String loggerName = event.getLoggerName();
/* 207 */           DiscardSummary summary = (DiscardSummary)this.discardMap.get(loggerName);
/*     */           
/* 209 */           if (summary == null) {
/* 210 */             summary = new DiscardSummary(event);
/* 211 */             this.discardMap.put(loggerName, summary); break;
/*     */           } 
/* 213 */           summary.add(event);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
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
/*     */   public void close() {
/* 231 */     synchronized (this.buffer) {
/* 232 */       this.closed = true;
/* 233 */       this.buffer.notifyAll();
/*     */     } 
/*     */     
/*     */     try {
/* 237 */       this.dispatcher.join();
/* 238 */     } catch (InterruptedException e) {
/* 239 */       Thread.currentThread().interrupt();
/*     */       
/* 241 */       LogLog.error("Got an InterruptedException while waiting for the dispatcher to finish.", e);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     synchronized (this.appenders) {
/* 248 */       Enumeration iter = this.appenders.getAllAppenders();
/*     */       
/* 250 */       if (iter != null) {
/* 251 */         while (iter.hasMoreElements()) {
/* 252 */           Object next = iter.nextElement();
/*     */           
/* 254 */           if (next instanceof Appender) {
/* 255 */             ((Appender)next).close();
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getAllAppenders() {
/* 268 */     synchronized (this.appenders) {
/* 269 */       return this.appenders.getAllAppenders();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Appender getAppender(String name) {
/* 280 */     synchronized (this.appenders) {
/* 281 */       return this.appenders.getAppender(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getLocationInfo() {
/* 291 */     return this.locationInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAttached(Appender appender) {
/* 301 */     synchronized (this.appenders) {
/* 302 */       return this.appenders.isAttached(appender);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresLayout() {
/* 310 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAllAppenders() {
/* 317 */     synchronized (this.appenders) {
/* 318 */       this.appenders.removeAllAppenders();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAppender(Appender appender) {
/* 328 */     synchronized (this.appenders) {
/* 329 */       this.appenders.removeAppender(appender);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAppender(String name) {
/* 339 */     synchronized (this.appenders) {
/* 340 */       this.appenders.removeAppender(name);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocationInfo(boolean flag) {
/* 359 */     this.locationInfo = flag;
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
/*     */   public void setBufferSize(int size) {
/* 374 */     if (size < 0) {
/* 375 */       throw new NegativeArraySizeException("size");
/*     */     }
/*     */     
/* 378 */     synchronized (this.buffer) {
/*     */ 
/*     */ 
/*     */       
/* 382 */       this.bufferSize = (size < 1) ? 1 : size;
/* 383 */       this.buffer.notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 393 */     return this.bufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlocking(boolean value) {
/* 404 */     synchronized (this.buffer) {
/* 405 */       this.blocking = value;
/* 406 */       this.buffer.notifyAll();
/*     */     } 
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
/*     */   public boolean getBlocking() {
/* 419 */     return this.blocking;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class DiscardSummary
/*     */   {
/*     */     private LoggingEvent maxEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int count;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DiscardSummary(LoggingEvent event) {
/* 442 */       this.maxEvent = event;
/* 443 */       this.count = 1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(LoggingEvent event) {
/* 452 */       if (event.getLevel().toInt() > this.maxEvent.getLevel().toInt()) {
/* 453 */         this.maxEvent = event;
/*     */       }
/*     */       
/* 456 */       this.count++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LoggingEvent createEvent() {
/* 465 */       String msg = MessageFormat.format("Discarded {0} messages due to full event buffer including: {1}", new Object[] { new Integer(this.count), this.maxEvent
/* 466 */             .getMessage() });
/*     */       
/* 468 */       return new LoggingEvent("org.apache.log4j.AsyncAppender.DONT_REPORT_LOCATION", 
/* 469 */           Logger.getLogger(this.maxEvent.getLoggerName()), this.maxEvent.getLevel(), msg, null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Dispatcher
/*     */     implements Runnable
/*     */   {
/*     */     private final AsyncAppender parent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final List buffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Map discardMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final AppenderAttachableImpl appenders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Dispatcher(AsyncAppender parent, List buffer, Map discardMap, AppenderAttachableImpl appenders) {
/* 508 */       this.parent = parent;
/* 509 */       this.buffer = buffer;
/* 510 */       this.appenders = appenders;
/* 511 */       this.discardMap = discardMap;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 518 */       boolean isActive = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 527 */         while (isActive) {
/* 528 */           LoggingEvent[] events = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 534 */           synchronized (this.buffer) {
/* 535 */             int bufferSize = this.buffer.size();
/* 536 */             isActive = !this.parent.closed;
/*     */             
/* 538 */             while (bufferSize == 0 && isActive) {
/* 539 */               this.buffer.wait();
/* 540 */               bufferSize = this.buffer.size();
/* 541 */               isActive = !this.parent.closed;
/*     */             } 
/*     */             
/* 544 */             if (bufferSize > 0) {
/* 545 */               events = new LoggingEvent[bufferSize + this.discardMap.size()];
/* 546 */               this.buffer.toArray((Object[])events);
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 551 */               int index = bufferSize;
/*     */               
/* 553 */               for (Iterator<AsyncAppender.DiscardSummary> iter = this.discardMap.values().iterator(); iter.hasNext();) {
/* 554 */                 events[index++] = ((AsyncAppender.DiscardSummary)iter.next()).createEvent();
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 560 */               this.buffer.clear();
/* 561 */               this.discardMap.clear();
/*     */ 
/*     */ 
/*     */               
/* 565 */               this.buffer.notifyAll();
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 572 */           if (events != null) {
/* 573 */             for (int i = 0; i < events.length; i++) {
/* 574 */               synchronized (this.appenders) {
/* 575 */                 this.appenders.appendLoopOnAppenders(events[i]);
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/* 580 */       } catch (InterruptedException ex) {
/* 581 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/AsyncAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */