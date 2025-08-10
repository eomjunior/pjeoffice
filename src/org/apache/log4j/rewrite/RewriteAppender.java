/*     */ package org.apache.log4j.rewrite;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import org.apache.log4j.Appender;
/*     */ import org.apache.log4j.AppenderSkeleton;
/*     */ import org.apache.log4j.helpers.AppenderAttachableImpl;
/*     */ import org.apache.log4j.spi.AppenderAttachable;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ import org.apache.log4j.spi.OptionHandler;
/*     */ import org.apache.log4j.xml.DOMConfigurator;
/*     */ import org.apache.log4j.xml.UnrecognizedElementHandler;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RewriteAppender
/*     */   extends AppenderSkeleton
/*     */   implements AppenderAttachable, UnrecognizedElementHandler
/*     */ {
/*     */   private RewritePolicy policy;
/*  49 */   private final AppenderAttachableImpl appenders = new AppenderAttachableImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void append(LoggingEvent event) {
/*  56 */     LoggingEvent rewritten = event;
/*  57 */     if (this.policy != null) {
/*  58 */       rewritten = this.policy.rewrite(event);
/*     */     }
/*  60 */     if (rewritten != null) {
/*  61 */       synchronized (this.appenders) {
/*  62 */         this.appenders.appendLoopOnAppenders(rewritten);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAppender(Appender newAppender) {
/*  73 */     synchronized (this.appenders) {
/*  74 */       this.appenders.addAppender(newAppender);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getAllAppenders() {
/*  84 */     synchronized (this.appenders) {
/*  85 */       return this.appenders.getAllAppenders();
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
/*  96 */     synchronized (this.appenders) {
/*  97 */       return this.appenders.getAppender(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 106 */     this.closed = true;
/*     */ 
/*     */ 
/*     */     
/* 110 */     synchronized (this.appenders) {
/* 111 */       Enumeration iter = this.appenders.getAllAppenders();
/*     */       
/* 113 */       if (iter != null) {
/* 114 */         while (iter.hasMoreElements()) {
/* 115 */           Object next = iter.nextElement();
/*     */           
/* 117 */           if (next instanceof Appender) {
/* 118 */             ((Appender)next).close();
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
/*     */   
/*     */   public boolean isAttached(Appender appender) {
/* 132 */     synchronized (this.appenders) {
/* 133 */       return this.appenders.isAttached(appender);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresLayout() {
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAllAppenders() {
/* 148 */     synchronized (this.appenders) {
/* 149 */       this.appenders.removeAllAppenders();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAppender(Appender appender) {
/* 159 */     synchronized (this.appenders) {
/* 160 */       this.appenders.removeAppender(appender);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAppender(String name) {
/* 170 */     synchronized (this.appenders) {
/* 171 */       this.appenders.removeAppender(name);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setRewritePolicy(RewritePolicy rewritePolicy) {
/* 176 */     this.policy = rewritePolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean parseUnrecognizedElement(Element element, Properties props) throws Exception {
/* 183 */     String nodeName = element.getNodeName();
/* 184 */     if ("rewritePolicy".equals(nodeName)) {
/* 185 */       Object rewritePolicy = DOMConfigurator.parseElement(element, props, RewritePolicy.class);
/*     */       
/* 187 */       if (rewritePolicy != null) {
/* 188 */         if (rewritePolicy instanceof OptionHandler) {
/* 189 */           ((OptionHandler)rewritePolicy).activateOptions();
/*     */         }
/* 191 */         setRewritePolicy((RewritePolicy)rewritePolicy);
/*     */       } 
/* 193 */       return true;
/*     */     } 
/* 195 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/rewrite/RewriteAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */