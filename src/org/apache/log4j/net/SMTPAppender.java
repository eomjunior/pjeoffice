/*     */ package org.apache.log4j.net;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.util.Date;
/*     */ import java.util.Properties;
/*     */ import javax.mail.Address;
/*     */ import javax.mail.Authenticator;
/*     */ import javax.mail.BodyPart;
/*     */ import javax.mail.Message;
/*     */ import javax.mail.MessagingException;
/*     */ import javax.mail.Multipart;
/*     */ import javax.mail.PasswordAuthentication;
/*     */ import javax.mail.Session;
/*     */ import javax.mail.Transport;
/*     */ import javax.mail.internet.AddressException;
/*     */ import javax.mail.internet.InternetAddress;
/*     */ import javax.mail.internet.InternetHeaders;
/*     */ import javax.mail.internet.MimeBodyPart;
/*     */ import javax.mail.internet.MimeMessage;
/*     */ import javax.mail.internet.MimeMultipart;
/*     */ import javax.mail.internet.MimeUtility;
/*     */ import org.apache.log4j.AppenderSkeleton;
/*     */ import org.apache.log4j.Layout;
/*     */ import org.apache.log4j.helpers.CyclicBuffer;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.helpers.OptionConverter;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ import org.apache.log4j.spi.OptionHandler;
/*     */ import org.apache.log4j.spi.TriggeringEventEvaluator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SMTPAppender
/*     */   extends AppenderSkeleton
/*     */   implements UnrecognizedElementHandler
/*     */ {
/*     */   private String to;
/*     */   private String cc;
/*     */   private String bcc;
/*     */   private String from;
/*     */   private String replyTo;
/*     */   private String subject;
/*     */   private String smtpHost;
/*     */   private String smtpUsername;
/*     */   private String smtpPassword;
/*     */   private String smtpProtocol;
/* 113 */   private int smtpPort = -1;
/*     */   
/*     */   private boolean smtpDebug = false;
/*     */   private boolean checkServerIdentity = true;
/* 117 */   private int bufferSize = 512;
/*     */   
/*     */   private boolean locationInfo = false;
/*     */   private boolean sendOnClose = false;
/* 121 */   protected CyclicBuffer cb = new CyclicBuffer(this.bufferSize);
/*     */ 
/*     */   
/*     */   protected Message msg;
/*     */ 
/*     */   
/*     */   protected TriggeringEventEvaluator evaluator;
/*     */ 
/*     */ 
/*     */   
/*     */   public SMTPAppender() {
/* 132 */     this(new DefaultEvaluator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SMTPAppender(TriggeringEventEvaluator evaluator) {
/* 140 */     this.evaluator = evaluator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/* 148 */     Session session = createSession();
/* 149 */     this.msg = (Message)new MimeMessage(session);
/*     */     
/*     */     try {
/* 152 */       addressMessage(this.msg);
/* 153 */       if (this.subject != null) {
/*     */         try {
/* 155 */           this.msg.setSubject(MimeUtility.encodeText(this.subject, "UTF-8", null));
/* 156 */         } catch (UnsupportedEncodingException ex) {
/* 157 */           LogLog.error("Unable to encode SMTP subject", ex);
/*     */         } 
/*     */       }
/* 160 */     } catch (MessagingException e) {
/* 161 */       LogLog.error("Could not activate SMTPAppender options.", (Throwable)e);
/*     */     } 
/*     */     
/* 164 */     if (this.evaluator instanceof OptionHandler) {
/* 165 */       ((OptionHandler)this.evaluator).activateOptions();
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
/*     */   protected void addressMessage(Message msg) throws MessagingException {
/* 177 */     if (this.from != null) {
/* 178 */       msg.setFrom((Address)getAddress(this.from));
/*     */     } else {
/* 180 */       msg.setFrom();
/*     */     } 
/*     */ 
/*     */     
/* 184 */     if (this.replyTo != null && this.replyTo.length() > 0) {
/* 185 */       msg.setReplyTo((Address[])parseAddress(this.replyTo));
/*     */     }
/*     */     
/* 188 */     if (this.to != null && this.to.length() > 0) {
/* 189 */       msg.setRecipients(Message.RecipientType.TO, (Address[])parseAddress(this.to));
/*     */     }
/*     */ 
/*     */     
/* 193 */     if (this.cc != null && this.cc.length() > 0) {
/* 194 */       msg.setRecipients(Message.RecipientType.CC, (Address[])parseAddress(this.cc));
/*     */     }
/*     */ 
/*     */     
/* 198 */     if (this.bcc != null && this.bcc.length() > 0) {
/* 199 */       msg.setRecipients(Message.RecipientType.BCC, (Address[])parseAddress(this.bcc));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Session createSession() {
/* 210 */     Properties props = null;
/*     */     try {
/* 212 */       props = new Properties(System.getProperties());
/* 213 */     } catch (SecurityException ex) {
/* 214 */       props = new Properties();
/*     */     } 
/*     */     
/* 217 */     String prefix = "mail.smtp";
/* 218 */     if (this.smtpProtocol != null) {
/* 219 */       props.put("mail.transport.protocol", this.smtpProtocol);
/* 220 */       prefix = "mail." + this.smtpProtocol;
/*     */     } 
/*     */     
/* 223 */     if (this.checkServerIdentity)
/*     */     {
/* 225 */       props.put(prefix + ".ssl.checkserveridentity", "true");
/*     */     }
/*     */     
/* 228 */     if (this.smtpHost != null) {
/* 229 */       props.put(prefix + ".host", this.smtpHost);
/*     */     }
/* 231 */     if (this.smtpPort > 0) {
/* 232 */       props.put(prefix + ".port", String.valueOf(this.smtpPort));
/*     */     }
/*     */     
/* 235 */     Authenticator auth = null;
/* 236 */     if (this.smtpPassword != null && this.smtpUsername != null) {
/* 237 */       props.put(prefix + ".auth", "true");
/* 238 */       auth = new Authenticator() {
/*     */           protected PasswordAuthentication getPasswordAuthentication() {
/* 240 */             return new PasswordAuthentication(SMTPAppender.this.smtpUsername, SMTPAppender.this.smtpPassword);
/*     */           }
/*     */         };
/*     */     } 
/* 244 */     Session session = Session.getInstance(props, auth);
/* 245 */     if (this.smtpProtocol != null) {
/* 246 */       session.setProtocolForAddress("rfc822", this.smtpProtocol);
/*     */     }
/* 248 */     if (this.smtpDebug) {
/* 249 */       session.setDebug(this.smtpDebug);
/*     */     }
/* 251 */     return session;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LoggingEvent event) {
/* 260 */     if (!checkEntryConditions()) {
/*     */       return;
/*     */     }
/*     */     
/* 264 */     event.getThreadName();
/* 265 */     event.getNDC();
/* 266 */     event.getMDCCopy();
/* 267 */     if (this.locationInfo) {
/* 268 */       event.getLocationInformation();
/*     */     }
/* 270 */     event.getRenderedMessage();
/* 271 */     event.getThrowableStrRep();
/* 272 */     this.cb.add(event);
/* 273 */     if (this.evaluator.isTriggeringEvent(event)) {
/* 274 */       sendBuffer();
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
/*     */   protected boolean checkEntryConditions() {
/* 287 */     if (this.msg == null) {
/* 288 */       this.errorHandler.error("Message object not configured.");
/* 289 */       return false;
/*     */     } 
/*     */     
/* 292 */     if (this.evaluator == null) {
/* 293 */       this.errorHandler.error("No TriggeringEventEvaluator is set for appender [" + this.name + "].");
/* 294 */       return false;
/*     */     } 
/*     */     
/* 297 */     if (this.layout == null) {
/* 298 */       this.errorHandler.error("No layout set for appender named [" + this.name + "].");
/* 299 */       return false;
/*     */     } 
/* 301 */     return true;
/*     */   }
/*     */   
/*     */   public synchronized void close() {
/* 305 */     this.closed = true;
/* 306 */     if (this.sendOnClose && this.cb.length() > 0) {
/* 307 */       sendBuffer();
/*     */     }
/*     */   }
/*     */   
/*     */   InternetAddress getAddress(String addressStr) {
/*     */     try {
/* 313 */       return new InternetAddress(addressStr);
/* 314 */     } catch (AddressException e) {
/* 315 */       this.errorHandler.error("Could not parse address [" + addressStr + "].", (Exception)e, 6);
/* 316 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   InternetAddress[] parseAddress(String addressStr) {
/*     */     try {
/* 322 */       return InternetAddress.parse(addressStr, true);
/* 323 */     } catch (AddressException e) {
/* 324 */       this.errorHandler.error("Could not parse address [" + addressStr + "].", (Exception)e, 6);
/* 325 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTo() {
/* 333 */     return this.to;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresLayout() {
/* 341 */     return true;
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
/*     */   protected String formatBody() {
/* 354 */     StringBuilder sbuf = new StringBuilder();
/* 355 */     String t = this.layout.getHeader();
/* 356 */     if (t != null)
/* 357 */       sbuf.append(t); 
/* 358 */     int len = this.cb.length();
/* 359 */     for (int i = 0; i < len; i++) {
/*     */       
/* 361 */       LoggingEvent event = this.cb.get();
/* 362 */       sbuf.append(this.layout.format(event));
/* 363 */       if (this.layout.ignoresThrowable()) {
/* 364 */         String[] s = event.getThrowableStrRep();
/* 365 */         if (s != null) {
/* 366 */           for (int j = 0; j < s.length; j++) {
/* 367 */             sbuf.append(s[j]);
/* 368 */             sbuf.append(Layout.LINE_SEP);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 373 */     t = this.layout.getFooter();
/* 374 */     if (t != null) {
/* 375 */       sbuf.append(t);
/*     */     }
/*     */     
/* 378 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sendBuffer() {
/*     */     try {
/*     */       MimeBodyPart part;
/* 387 */       String s = formatBody();
/* 388 */       boolean allAscii = true;
/* 389 */       for (int i = 0; i < s.length() && allAscii; i++) {
/* 390 */         allAscii = (s.charAt(i) <= '');
/*     */       }
/*     */       
/* 393 */       if (allAscii) {
/* 394 */         part = new MimeBodyPart();
/* 395 */         part.setContent(s, this.layout.getContentType());
/*     */       } else {
/*     */         try {
/* 398 */           ByteArrayOutputStream os = new ByteArrayOutputStream();
/* 399 */           Writer writer = new OutputStreamWriter(MimeUtility.encode(os, "quoted-printable"), "UTF-8");
/* 400 */           writer.write(s);
/* 401 */           writer.close();
/* 402 */           InternetHeaders headers = new InternetHeaders();
/* 403 */           headers.setHeader("Content-Type", this.layout.getContentType() + "; charset=UTF-8");
/* 404 */           headers.setHeader("Content-Transfer-Encoding", "quoted-printable");
/* 405 */           part = new MimeBodyPart(headers, os.toByteArray());
/* 406 */         } catch (Exception ex) {
/* 407 */           StringBuilder sbuf = new StringBuilder(s);
/* 408 */           for (int j = 0; j < sbuf.length(); j++) {
/* 409 */             if (sbuf.charAt(j) >= '') {
/* 410 */               sbuf.setCharAt(j, '?');
/*     */             }
/*     */           } 
/* 413 */           part = new MimeBodyPart();
/* 414 */           part.setContent(sbuf.toString(), this.layout.getContentType());
/*     */         } 
/*     */       } 
/*     */       
/* 418 */       MimeMultipart mimeMultipart = new MimeMultipart();
/* 419 */       mimeMultipart.addBodyPart((BodyPart)part);
/* 420 */       this.msg.setContent((Multipart)mimeMultipart);
/*     */       
/* 422 */       this.msg.setSentDate(new Date());
/* 423 */       Transport.send(this.msg);
/* 424 */     } catch (MessagingException e) {
/* 425 */       LogLog.error("Error occured while sending e-mail notification.", (Throwable)e);
/* 426 */     } catch (RuntimeException e) {
/* 427 */       LogLog.error("Error occured while sending e-mail notification.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEvaluatorClass() {
/* 435 */     return (this.evaluator == null) ? null : this.evaluator.getClass().getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFrom() {
/* 442 */     return this.from;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReplyTo() {
/* 452 */     return this.replyTo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubject() {
/* 459 */     return this.subject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFrom(String from) {
/* 467 */     this.from = from;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReplyTo(String addresses) {
/* 477 */     this.replyTo = addresses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubject(String subject) {
/* 485 */     this.subject = subject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBufferSize(int bufferSize) {
/* 496 */     this.bufferSize = bufferSize;
/* 497 */     this.cb.resize(bufferSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSMTPHost(String smtpHost) {
/* 505 */     this.smtpHost = smtpHost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSMTPHost() {
/* 512 */     return this.smtpHost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTo(String to) {
/* 520 */     this.to = to;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 527 */     return this.bufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEvaluatorClass(String value) {
/* 537 */     this.evaluator = (TriggeringEventEvaluator)OptionConverter.instantiateByClassName(value, TriggeringEventEvaluator.class, this.evaluator);
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
/*     */   public void setLocationInfo(boolean locationInfo) {
/* 553 */     this.locationInfo = locationInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getLocationInfo() {
/* 560 */     return this.locationInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCc(String addresses) {
/* 570 */     this.cc = addresses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCc() {
/* 580 */     return this.cc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBcc(String addresses) {
/* 590 */     this.bcc = addresses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBcc() {
/* 600 */     return this.bcc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSMTPPassword(String password) {
/* 611 */     this.smtpPassword = password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSMTPUsername(String username) {
/* 622 */     this.smtpUsername = username;
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
/*     */   public void setSMTPDebug(boolean debug) {
/* 635 */     this.smtpDebug = debug;
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
/*     */   public void setCheckServerIdentity(boolean checkServerIdentity) {
/* 647 */     this.checkServerIdentity = checkServerIdentity;
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
/*     */   public boolean getCheckServerIdentity() {
/* 660 */     return this.checkServerIdentity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSMTPPassword() {
/* 670 */     return this.smtpPassword;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSMTPUsername() {
/* 680 */     return this.smtpUsername;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getSMTPDebug() {
/* 690 */     return this.smtpDebug;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setEvaluator(TriggeringEventEvaluator trigger) {
/* 700 */     if (trigger == null) {
/* 701 */       throw new NullPointerException("trigger");
/*     */     }
/* 703 */     this.evaluator = trigger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TriggeringEventEvaluator getEvaluator() {
/* 713 */     return this.evaluator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean parseUnrecognizedElement(Element element, Properties props) throws Exception {
/* 722 */     if ("triggeringPolicy".equals(element.getNodeName())) {
/* 723 */       Object triggerPolicy = DOMConfigurator.parseElement(element, props, TriggeringEventEvaluator.class);
/*     */       
/* 725 */       if (triggerPolicy instanceof TriggeringEventEvaluator) {
/* 726 */         setEvaluator((TriggeringEventEvaluator)triggerPolicy);
/*     */       }
/* 728 */       return true;
/*     */     } 
/*     */     
/* 731 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getSMTPProtocol() {
/* 741 */     return this.smtpProtocol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSMTPProtocol(String val) {
/* 751 */     this.smtpProtocol = val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getSMTPPort() {
/* 761 */     return this.smtpPort;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSMTPPort(int val) {
/* 771 */     this.smtpPort = val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean getSendOnClose() {
/* 782 */     return this.sendOnClose;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSendOnClose(boolean val) {
/* 793 */     this.sendOnClose = val;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/net/SMTPAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */