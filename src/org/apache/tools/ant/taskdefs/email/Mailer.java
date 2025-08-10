/*     */ package org.apache.tools.ant.taskdefs.email;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.util.DateUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Mailer
/*     */ {
/*  34 */   protected String host = null;
/*  35 */   protected int port = -1;
/*  36 */   protected String user = null;
/*  37 */   protected String password = null;
/*     */   
/*     */   protected boolean SSL = false;
/*     */   
/*     */   protected Message message;
/*     */   protected EmailAddress from;
/*  43 */   protected Vector<EmailAddress> replyToList = null;
/*  44 */   protected Vector<EmailAddress> toList = null;
/*  45 */   protected Vector<EmailAddress> ccList = null;
/*  46 */   protected Vector<EmailAddress> bccList = null;
/*  47 */   protected Vector<File> files = null;
/*  48 */   protected String subject = null;
/*     */   protected Task task;
/*     */   protected boolean includeFileNames = false;
/*  51 */   protected Vector<Header> headers = null;
/*     */ 
/*     */   
/*     */   private boolean ignoreInvalidRecipients = false;
/*     */ 
/*     */   
/*     */   private boolean starttls = false;
/*     */   
/*     */   private boolean portExplicitlySpecified = false;
/*     */ 
/*     */   
/*     */   public void setHost(String host) {
/*  63 */     this.host = host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPort(int port) {
/*  72 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPortExplicitlySpecified(boolean explicit) {
/*  82 */     this.portExplicitlySpecified = explicit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isPortExplicitlySpecified() {
/*  92 */     return this.portExplicitlySpecified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUser(String user) {
/* 102 */     this.user = user;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/* 112 */     this.password = password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSSL(boolean ssl) {
/* 122 */     this.SSL = ssl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnableStartTLS(boolean b) {
/* 132 */     this.starttls = b;
/*     */   }
/*     */   
/*     */   protected boolean isStartTLSEnabled() {
/* 136 */     return this.starttls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessage(Message m) {
/* 145 */     this.message = m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrom(EmailAddress from) {
/* 154 */     this.from = from;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReplyToList(Vector<EmailAddress> list) {
/* 164 */     this.replyToList = list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToList(Vector<EmailAddress> list) {
/* 173 */     this.toList = list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCcList(Vector<EmailAddress> list) {
/* 182 */     this.ccList = list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBccList(Vector<EmailAddress> list) {
/* 191 */     this.bccList = list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFiles(Vector<File> files) {
/* 200 */     this.files = files;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubject(String subject) {
/* 209 */     this.subject = subject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTask(Task task) {
/* 218 */     this.task = task;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeFileNames(boolean b) {
/* 227 */     this.includeFileNames = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHeaders(Vector<Header> v) {
/* 236 */     this.headers = v;
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
/*     */   public abstract void send() throws BuildException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreInvalidRecipients(boolean b) {
/* 258 */     this.ignoreInvalidRecipients = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldIgnoreInvalidRecipients() {
/* 268 */     return this.ignoreInvalidRecipients;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String getDate() {
/* 279 */     return DateUtils.getDateForHeader();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/email/Mailer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */