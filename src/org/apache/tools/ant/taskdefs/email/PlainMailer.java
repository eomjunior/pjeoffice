/*     */ package org.apache.tools.ant.taskdefs.email;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Objects;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.mail.MailMessage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PlainMailer
/*     */   extends Mailer
/*     */ {
/*     */   public void send() {
/*     */     try {
/*  44 */       MailMessage mailMessage = new MailMessage(this.host, this.port);
/*     */       
/*  46 */       mailMessage.from(this.from.toString());
/*     */       
/*  48 */       boolean atLeastOneRcptReached = false;
/*     */       
/*  50 */       Objects.requireNonNull(mailMessage); this.replyToList.stream().map(Object::toString).forEach(mailMessage::replyto);
/*     */       
/*  52 */       for (EmailAddress to : this.toList) {
/*     */         try {
/*  54 */           mailMessage.to(to.toString());
/*  55 */           atLeastOneRcptReached = true;
/*  56 */         } catch (IOException ex) {
/*  57 */           badRecipient(to, ex);
/*     */         } 
/*     */       } 
/*     */       
/*  61 */       for (EmailAddress cc : this.ccList) {
/*     */         try {
/*  63 */           mailMessage.cc(cc.toString());
/*  64 */           atLeastOneRcptReached = true;
/*  65 */         } catch (IOException ex) {
/*  66 */           badRecipient(cc, ex);
/*     */         } 
/*     */       } 
/*     */       
/*  70 */       for (EmailAddress bcc : this.bccList) {
/*     */         try {
/*  72 */           mailMessage.bcc(bcc.toString());
/*  73 */           atLeastOneRcptReached = true;
/*  74 */         } catch (IOException ex) {
/*  75 */           badRecipient(bcc, ex);
/*     */         } 
/*     */       } 
/*     */       
/*  79 */       if (!atLeastOneRcptReached) {
/*  80 */         throw new BuildException("Couldn't reach any recipient");
/*     */       }
/*  82 */       if (this.subject != null) {
/*  83 */         mailMessage.setSubject(this.subject);
/*     */       }
/*  85 */       mailMessage.setHeader("Date", getDate());
/*  86 */       if (this.message.getCharset() != null) {
/*  87 */         mailMessage.setHeader("Content-Type", this.message.getMimeType() + "; charset=\"" + this.message
/*  88 */             .getCharset() + "\"");
/*     */       } else {
/*  90 */         mailMessage.setHeader("Content-Type", this.message.getMimeType());
/*     */       } 
/*  92 */       if (this.headers != null) {
/*  93 */         for (Header h : this.headers) {
/*  94 */           mailMessage.setHeader(h.getName(), h.getValue());
/*     */         }
/*     */       }
/*  97 */       PrintStream out = mailMessage.getPrintStream();
/*  98 */       this.message.print(out);
/*     */       
/* 100 */       if (this.files != null) {
/* 101 */         for (File f : this.files) {
/* 102 */           attach(f, out);
/*     */         }
/*     */       }
/* 105 */       mailMessage.sendAndClose();
/* 106 */     } catch (IOException ioe) {
/* 107 */       throw new BuildException("IO error sending mail", ioe);
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
/*     */   protected void attach(File file, PrintStream out) throws IOException {
/* 121 */     if (!file.exists() || !file.canRead()) {
/* 122 */       throw new BuildException("File \"%s\" does not exist or is not readable.", new Object[] { file
/*     */             
/* 124 */             .getAbsolutePath() });
/*     */     }
/*     */     
/* 127 */     if (this.includeFileNames) {
/* 128 */       out.println();
/*     */       
/* 130 */       String filename = file.getName();
/* 131 */       int filenamelength = filename.length();
/*     */       
/* 133 */       out.println(filename);
/* 134 */       for (int star = 0; star < filenamelength; star++) {
/* 135 */         out.print('=');
/*     */       }
/* 137 */       out.println();
/*     */     } 
/*     */     
/* 140 */     int maxBuf = 1024;
/* 141 */     byte[] buf = new byte[1024];
/*     */     
/* 143 */     InputStream finstr = Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]); 
/* 144 */     try { BufferedInputStream in = new BufferedInputStream(finstr, buf.length);
/*     */       
/*     */       try { int length;
/* 147 */         while ((length = in.read(buf)) != -1) {
/* 148 */           out.write(buf, 0, length);
/*     */         }
/* 150 */         in.close(); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  if (finstr != null) finstr.close();  } catch (Throwable throwable) { if (finstr != null)
/*     */         try { finstr.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 154 */      } private void badRecipient(EmailAddress rcpt, IOException reason) { String msg = "Failed to send mail to " + rcpt;
/* 155 */     if (shouldIgnoreInvalidRecipients()) {
/* 156 */       msg = msg + " because of :" + reason.getMessage();
/* 157 */       if (this.task != null) {
/* 158 */         this.task.log(msg, 1);
/*     */       } else {
/* 160 */         System.err.println(msg);
/*     */       } 
/*     */     } else {
/* 163 */       throw new BuildException(msg, reason);
/*     */     }  }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/email/PlainMailer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */