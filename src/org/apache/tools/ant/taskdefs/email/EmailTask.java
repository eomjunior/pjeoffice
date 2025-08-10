/*     */ package org.apache.tools.ant.taskdefs.email;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.util.ClasspathUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EmailTask
/*     */   extends Task
/*     */ {
/*     */   private static final int SMTP_PORT = 25;
/*     */   public static final String AUTO = "auto";
/*     */   public static final String MIME = "mime";
/*     */   public static final String UU = "uu";
/*     */   public static final String PLAIN = "plain";
/*     */   
/*     */   public static class Encoding
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public String[] getValues() {
/*  64 */       return new String[] { "auto", "mime", "uu", "plain" };
/*     */     }
/*     */   }
/*     */   
/*  68 */   private String encoding = "auto";
/*     */   
/*  70 */   private String host = "localhost";
/*  71 */   private Integer port = null;
/*     */   
/*  73 */   private String subject = null;
/*     */   
/*  75 */   private Message message = null;
/*     */   
/*     */   private boolean failOnError = true;
/*     */   private boolean includeFileNames = false;
/*  79 */   private String messageMimeType = null;
/*     */   
/*     */   private String messageFileInputEncoding;
/*     */   
/*  83 */   private EmailAddress from = null;
/*     */   
/*  85 */   private Vector<EmailAddress> replyToList = new Vector<>();
/*     */   
/*  87 */   private Vector<EmailAddress> toList = new Vector<>();
/*     */   
/*  89 */   private Vector<EmailAddress> ccList = new Vector<>();
/*     */   
/*  91 */   private Vector<EmailAddress> bccList = new Vector<>();
/*     */ 
/*     */   
/*  94 */   private Vector<Header> headers = new Vector<>();
/*     */ 
/*     */   
/*  97 */   private Path attachments = null;
/*     */   
/*  99 */   private String charset = null;
/*     */   
/* 101 */   private String user = null;
/*     */   
/* 103 */   private String password = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean ssl = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean starttls = false;
/*     */ 
/*     */   
/*     */   private boolean ignoreInvalidRecipients = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUser(String user) {
/* 119 */     this.user = user;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/* 129 */     this.password = password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSSL(boolean ssl) {
/* 139 */     this.ssl = ssl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnableStartTLS(boolean b) {
/* 150 */     this.starttls = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(Encoding encoding) {
/* 159 */     this.encoding = encoding.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMailport(int port) {
/* 168 */     this.port = Integer.valueOf(port);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMailhost(String host) {
/* 177 */     this.host = host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubject(String subject) {
/* 186 */     this.subject = subject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessage(String message) {
/* 195 */     if (this.message != null) {
/* 196 */       throw new BuildException("Only one message can be sent in an email");
/*     */     }
/*     */     
/* 199 */     this.message = new Message(message);
/* 200 */     this.message.setProject(getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageFile(File file) {
/* 209 */     if (this.message != null) {
/* 210 */       throw new BuildException("Only one message can be sent in an email");
/*     */     }
/*     */     
/* 213 */     this.message = new Message(file);
/* 214 */     this.message.setProject(getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageMimeType(String type) {
/* 224 */     this.messageMimeType = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMessage(Message message) throws BuildException {
/* 234 */     if (this.message != null) {
/* 235 */       throw new BuildException("Only one message can be sent in an email");
/*     */     }
/*     */     
/* 238 */     this.message = message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFrom(EmailAddress address) {
/* 247 */     if (this.from != null) {
/* 248 */       throw new BuildException("Emails can only be from one address");
/*     */     }
/* 250 */     this.from = address;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrom(String address) {
/* 259 */     if (this.from != null) {
/* 260 */       throw new BuildException("Emails can only be from one address");
/*     */     }
/* 262 */     this.from = new EmailAddress(address);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReplyTo(EmailAddress address) {
/* 272 */     this.replyToList.add(address);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReplyTo(String address) {
/* 282 */     this.replyToList.add(new EmailAddress(address));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTo(EmailAddress address) {
/* 291 */     this.toList.add(address);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToList(String list) {
/* 300 */     StringTokenizer tokens = new StringTokenizer(list, ",");
/*     */     
/* 302 */     while (tokens.hasMoreTokens()) {
/* 303 */       this.toList.add(new EmailAddress(tokens.nextToken()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCc(EmailAddress address) {
/* 313 */     this.ccList.add(address);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCcList(String list) {
/* 322 */     StringTokenizer tokens = new StringTokenizer(list, ",");
/*     */     
/* 324 */     while (tokens.hasMoreTokens()) {
/* 325 */       this.ccList.add(new EmailAddress(tokens.nextToken()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBcc(EmailAddress address) {
/* 335 */     this.bccList.add(address);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBccList(String list) {
/* 344 */     StringTokenizer tokens = new StringTokenizer(list, ",");
/*     */     
/* 346 */     while (tokens.hasMoreTokens()) {
/* 347 */       this.bccList.add(new EmailAddress(tokens.nextToken()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnError(boolean failOnError) {
/* 357 */     this.failOnError = failOnError;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFiles(String filenames) {
/* 366 */     StringTokenizer t = new StringTokenizer(filenames, ", ");
/*     */     
/* 368 */     while (t.hasMoreTokens()) {
/* 369 */       createAttachments()
/* 370 */         .add((ResourceCollection)new FileResource(getProject().resolveFile(t.nextToken())));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet fs) {
/* 380 */     createAttachments().add((ResourceCollection)fs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createAttachments() {
/* 390 */     if (this.attachments == null) {
/* 391 */       this.attachments = new Path(getProject());
/*     */     }
/* 393 */     return this.attachments.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Header createHeader() {
/* 401 */     Header h = new Header();
/* 402 */     this.headers.add(h);
/* 403 */     return h;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludefilenames(boolean includeFileNames) {
/* 413 */     this.includeFileNames = includeFileNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getIncludeFileNames() {
/* 422 */     return this.includeFileNames;
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
/*     */   public void setIgnoreInvalidRecipients(boolean b) {
/* 436 */     this.ignoreInvalidRecipients = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 444 */     Message savedMessage = this.message;
/*     */     
/*     */     try {
/* 447 */       Mailer mailer = null;
/*     */ 
/*     */       
/* 450 */       boolean autoFound = false;
/*     */       
/* 452 */       if ("mime".equals(this.encoding) || ("auto"
/* 453 */         .equals(this.encoding) && !autoFound)) {
/*     */         
/*     */         try {
/*     */           
/* 457 */           Class.forName("javax.activation.DataHandler");
/* 458 */           Class.forName("javax.mail.internet.MimeMessage");
/*     */           
/* 460 */           mailer = (Mailer)ClasspathUtils.newInstance("org.apache.tools.ant.taskdefs.email.MimeMailer", EmailTask.class
/*     */               
/* 462 */               .getClassLoader(), Mailer.class);
/* 463 */           autoFound = true;
/*     */           
/* 465 */           log("Using MIME mail", 3);
/* 466 */         } catch (BuildException e) {
/* 467 */           logBuildException("Failed to initialise MIME mail: ", e);
/*     */         } 
/*     */       }
/*     */       
/* 471 */       if (!autoFound && (this.user != null || this.password != null) && ("uu"
/* 472 */         .equals(this.encoding) || "plain".equals(this.encoding))) {
/* 473 */         throw new BuildException("SMTP auth only possible with MIME mail");
/*     */       }
/*     */       
/* 476 */       if (!autoFound && (this.ssl || this.starttls) && ("uu"
/* 477 */         .equals(this.encoding) || "plain".equals(this.encoding))) {
/* 478 */         throw new BuildException("SSL and STARTTLS only possible with MIME mail");
/*     */       }
/*     */ 
/*     */       
/* 482 */       if ("uu".equals(this.encoding) || ("auto"
/* 483 */         .equals(this.encoding) && !autoFound)) {
/*     */         try {
/* 485 */           mailer = (Mailer)ClasspathUtils.newInstance("org.apache.tools.ant.taskdefs.email.UUMailer", EmailTask.class
/*     */               
/* 487 */               .getClassLoader(), Mailer.class);
/* 488 */           autoFound = true;
/* 489 */           log("Using UU mail", 3);
/* 490 */         } catch (BuildException e) {
/* 491 */           logBuildException("Failed to initialise UU mail: ", e);
/*     */         } 
/*     */       }
/*     */       
/* 495 */       if ("plain".equals(this.encoding) || ("auto"
/* 496 */         .equals(this.encoding) && !autoFound)) {
/* 497 */         mailer = new PlainMailer();
/* 498 */         autoFound = true;
/* 499 */         log("Using plain mail", 3);
/*     */       } 
/*     */       
/* 502 */       if (mailer == null) {
/* 503 */         throw new BuildException("Failed to initialise encoding: %s", new Object[] { this.encoding });
/*     */       }
/*     */ 
/*     */       
/* 507 */       if (this.message == null) {
/* 508 */         this.message = new Message();
/* 509 */         this.message.setProject(getProject());
/*     */       } 
/*     */       
/* 512 */       if (this.from == null || this.from.getAddress() == null) {
/* 513 */         throw new BuildException("A from element is required");
/*     */       }
/*     */       
/* 516 */       if (this.toList.isEmpty() && this.ccList.isEmpty() && this.bccList.isEmpty()) {
/* 517 */         throw new BuildException("At least one of to, cc or bcc must be supplied");
/*     */       }
/*     */ 
/*     */       
/* 521 */       if (this.messageMimeType != null) {
/* 522 */         if (this.message.isMimeTypeSpecified()) {
/* 523 */           throw new BuildException("The mime type can only be specified in one location");
/*     */         }
/*     */         
/* 526 */         this.message.setMimeType(this.messageMimeType);
/*     */       } 
/*     */       
/* 529 */       if (this.charset != null) {
/* 530 */         if (this.message.getCharset() != null) {
/* 531 */           throw new BuildException("The charset can only be specified in one location");
/*     */         }
/*     */         
/* 534 */         this.message.setCharset(this.charset);
/*     */       } 
/* 536 */       this.message.setInputEncoding(this.messageFileInputEncoding);
/*     */ 
/*     */       
/* 539 */       Vector<File> files = new Vector<>();
/*     */       
/* 541 */       if (this.attachments != null) {
/* 542 */         for (Resource r : this.attachments) {
/* 543 */           files.add(((FileProvider)r.as(FileProvider.class)).getFile());
/*     */         }
/*     */       }
/*     */       
/* 547 */       log("Sending email: " + this.subject, 2);
/* 548 */       log("From " + this.from, 3);
/* 549 */       log("ReplyTo " + this.replyToList, 3);
/* 550 */       log("To " + this.toList, 3);
/* 551 */       log("Cc " + this.ccList, 3);
/* 552 */       log("Bcc " + this.bccList, 3);
/*     */ 
/*     */       
/* 555 */       mailer.setHost(this.host);
/* 556 */       if (this.port != null) {
/* 557 */         mailer.setPort(this.port.intValue());
/* 558 */         mailer.setPortExplicitlySpecified(true);
/*     */       } else {
/* 560 */         mailer.setPort(25);
/* 561 */         mailer.setPortExplicitlySpecified(false);
/*     */       } 
/* 563 */       mailer.setUser(this.user);
/* 564 */       mailer.setPassword(this.password);
/* 565 */       mailer.setSSL(this.ssl);
/* 566 */       mailer.setEnableStartTLS(this.starttls);
/* 567 */       mailer.setMessage(this.message);
/* 568 */       mailer.setFrom(this.from);
/* 569 */       mailer.setReplyToList(this.replyToList);
/* 570 */       mailer.setToList(this.toList);
/* 571 */       mailer.setCcList(this.ccList);
/* 572 */       mailer.setBccList(this.bccList);
/* 573 */       mailer.setFiles(files);
/* 574 */       mailer.setSubject(this.subject);
/* 575 */       mailer.setTask(this);
/* 576 */       mailer.setIncludeFileNames(this.includeFileNames);
/* 577 */       mailer.setHeaders(this.headers);
/* 578 */       mailer.setIgnoreInvalidRecipients(this.ignoreInvalidRecipients);
/*     */ 
/*     */       
/* 581 */       mailer.send();
/*     */ 
/*     */       
/* 584 */       int count = files.size();
/*     */       
/* 586 */       log("Sent email with " + count + " attachment" + (
/* 587 */           (count == 1) ? "" : "s"), 2);
/* 588 */     } catch (BuildException e) {
/* 589 */       logBuildException("Failed to send email: ", e);
/* 590 */       if (this.failOnError) {
/* 591 */         throw e;
/*     */       }
/* 593 */     } catch (Exception e) {
/* 594 */       log("Failed to send email: " + e.getMessage(), 1);
/* 595 */       if (this.failOnError) {
/* 596 */         throw new BuildException(e);
/*     */       }
/*     */     } finally {
/* 599 */       this.message = savedMessage;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void logBuildException(String reason, BuildException e) {
/* 604 */     Throwable t = (e.getCause() == null) ? (Throwable)e : e.getCause();
/* 605 */     log(reason + t.getMessage(), 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharset(String charset) {
/* 616 */     this.charset = charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCharset() {
/* 626 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageFileInputEncoding(String encoding) {
/* 636 */     this.messageFileInputEncoding = encoding;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/email/EmailTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */