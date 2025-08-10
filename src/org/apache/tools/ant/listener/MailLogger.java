/*     */ package org.apache.tools.ant.listener;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.BuildEvent;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DefaultLogger;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.taskdefs.email.EmailAddress;
/*     */ import org.apache.tools.ant.taskdefs.email.Mailer;
/*     */ import org.apache.tools.ant.taskdefs.email.Message;
/*     */ import org.apache.tools.ant.util.ClasspathUtils;
/*     */ import org.apache.tools.ant.util.DateUtils;
/*     */ import org.apache.tools.ant.util.FileUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MailLogger
/*     */   extends DefaultLogger
/*     */ {
/*     */   private static final String DEFAULT_MIME_TYPE = "text/plain";
/* 101 */   private StringBuffer buffer = new StringBuffer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildFinished(BuildEvent event) {
/* 110 */     super.buildFinished(event);
/*     */     
/* 112 */     Project project = event.getProject();
/* 113 */     Map<String, Object> properties = project.getProperties();
/*     */ 
/*     */ 
/*     */     
/* 117 */     Properties fileProperties = new Properties();
/* 118 */     String filename = (String)properties.get("MailLogger.properties.file");
/* 119 */     if (filename != null) {
/* 120 */       InputStream is = null;
/*     */       try {
/* 122 */         is = Files.newInputStream(Paths.get(filename, new String[0]), new java.nio.file.OpenOption[0]);
/* 123 */         fileProperties.load(is);
/* 124 */       } catch (IOException iOException) {
/*     */       
/*     */       } finally {
/* 127 */         FileUtils.close(is);
/*     */       } 
/*     */     } 
/*     */     
/* 131 */     fileProperties.stringPropertyNames()
/* 132 */       .forEach(key -> properties.put(key, project.replaceProperties(fileProperties.getProperty(key))));
/*     */     
/* 134 */     boolean success = (event.getException() == null);
/* 135 */     String prefix = success ? "success" : "failure";
/*     */     
/*     */     try {
/* 138 */       boolean notify = Project.toBoolean(getValue(properties, prefix + ".notify", "on"));
/*     */ 
/*     */       
/* 141 */       if (!notify) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 164 */       Values values = (new Values()).mailhost(getValue(properties, "mailhost", "localhost")).port(Integer.parseInt(getValue(properties, "port", String.valueOf(25)))).user(getValue(properties, "user", "")).password(getValue(properties, "password", "")).ssl(Project.toBoolean(getValue(properties, "ssl", "off"))).starttls(Project.toBoolean(getValue(properties, "starttls.enable", "off"))).from(getValue(properties, "from", null)).replytoList(getValue(properties, "replyto", "")).toList(getValue(properties, prefix + ".to", null)).toCcList(getValue(properties, prefix + ".cc", "")).toBccList(getValue(properties, prefix + ".bcc", "")).mimeType(getValue(properties, "mimeType", "text/plain")).charset(getValue(properties, "charset", "")).body(getValue(properties, prefix + ".body", "")).subject(getValue(properties, prefix + ".subject", 
/*     */             
/* 166 */             success ? "Build Success" : "Build Failure"));
/* 167 */       if (values.user().isEmpty() && values
/* 168 */         .password().isEmpty() && 
/* 169 */         !values.ssl() && !values.starttls()) {
/* 170 */         sendMail(values, this.buffer.substring(0));
/*     */       } else {
/* 172 */         sendMimeMail(event
/* 173 */             .getProject(), values, this.buffer.substring(0));
/*     */       } 
/* 175 */     } catch (Exception e) {
/* 176 */       System.out.println("MailLogger failed to send e-mail!");
/* 177 */       e.printStackTrace(System.err);
/*     */     } 
/*     */   }
/*     */   private static class Values { private String mailhost; private int port; private String user; private String password; private boolean ssl; private String from; private String replytoList;
/*     */     private String toList;
/*     */     
/*     */     public String mailhost() {
/* 184 */       return this.mailhost;
/*     */     } private String toCcList; private String toBccList; private String subject; private String charset; private String mimeType; private String body; private boolean starttls; private Values() {}
/*     */     public Values mailhost(String mailhost) {
/* 187 */       this.mailhost = mailhost;
/* 188 */       return this;
/*     */     }
/*     */     
/*     */     public int port() {
/* 192 */       return this.port;
/*     */     }
/*     */     public Values port(int port) {
/* 195 */       this.port = port;
/* 196 */       return this;
/*     */     }
/*     */     
/*     */     public String user() {
/* 200 */       return this.user;
/*     */     }
/*     */     public Values user(String user) {
/* 203 */       this.user = user;
/* 204 */       return this;
/*     */     }
/*     */     
/*     */     public String password() {
/* 208 */       return this.password;
/*     */     }
/*     */     public Values password(String password) {
/* 211 */       this.password = password;
/* 212 */       return this;
/*     */     }
/*     */     
/*     */     public boolean ssl() {
/* 216 */       return this.ssl;
/*     */     }
/*     */     public Values ssl(boolean ssl) {
/* 219 */       this.ssl = ssl;
/* 220 */       return this;
/*     */     }
/*     */     
/*     */     public String from() {
/* 224 */       return this.from;
/*     */     }
/*     */     public Values from(String from) {
/* 227 */       this.from = from;
/* 228 */       return this;
/*     */     }
/*     */     
/*     */     public String replytoList() {
/* 232 */       return this.replytoList;
/*     */     }
/*     */     public Values replytoList(String replytoList) {
/* 235 */       this.replytoList = replytoList;
/* 236 */       return this;
/*     */     }
/*     */     
/*     */     public String toList() {
/* 240 */       return this.toList;
/*     */     }
/*     */     public Values toList(String toList) {
/* 243 */       this.toList = toList;
/* 244 */       return this;
/*     */     }
/*     */     
/*     */     public String toCcList() {
/* 248 */       return this.toCcList;
/*     */     }
/*     */     public Values toCcList(String toCcList) {
/* 251 */       this.toCcList = toCcList;
/* 252 */       return this;
/*     */     }
/*     */     
/*     */     public String toBccList() {
/* 256 */       return this.toBccList;
/*     */     }
/*     */     public Values toBccList(String toBccList) {
/* 259 */       this.toBccList = toBccList;
/* 260 */       return this;
/*     */     }
/*     */     
/*     */     public String subject() {
/* 264 */       return this.subject;
/*     */     }
/*     */     public Values subject(String subject) {
/* 267 */       this.subject = subject;
/* 268 */       return this;
/*     */     }
/*     */     
/*     */     public String charset() {
/* 272 */       return this.charset;
/*     */     }
/*     */     public Values charset(String charset) {
/* 275 */       this.charset = charset;
/* 276 */       return this;
/*     */     }
/*     */     
/*     */     public String mimeType() {
/* 280 */       return this.mimeType;
/*     */     }
/*     */     public Values mimeType(String mimeType) {
/* 283 */       this.mimeType = mimeType;
/* 284 */       return this;
/*     */     }
/*     */     
/*     */     public String body() {
/* 288 */       return this.body;
/*     */     }
/*     */     public Values body(String body) {
/* 291 */       this.body = body;
/* 292 */       return this;
/*     */     }
/*     */     
/*     */     public boolean starttls() {
/* 296 */       return this.starttls;
/*     */     }
/*     */     public Values starttls(boolean starttls) {
/* 299 */       this.starttls = starttls;
/* 300 */       return this;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void log(String message) {
/* 311 */     this.buffer.append(message).append(System.lineSeparator());
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
/*     */   private String getValue(Map<String, Object> properties, String name, String defaultValue) {
/* 327 */     String propertyName = "MailLogger." + name;
/* 328 */     String value = (String)properties.get(propertyName);
/*     */     
/* 330 */     if (value == null) {
/* 331 */       value = defaultValue;
/*     */     }
/*     */     
/* 334 */     if (value == null) {
/* 335 */       throw new RuntimeException("Missing required parameter: " + propertyName);
/*     */     }
/*     */     
/* 338 */     return value;
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
/*     */   private void sendMail(Values values, String message) throws IOException {
/* 350 */     MailMessage mailMessage = new MailMessage(values.mailhost(), values.port());
/* 351 */     mailMessage.setHeader("Date", DateUtils.getDateForHeader());
/*     */     
/* 353 */     mailMessage.from(values.from());
/* 354 */     if (!values.replytoList().isEmpty()) {
/*     */       
/* 356 */       StringTokenizer stringTokenizer = new StringTokenizer(values.replytoList(), ", ", false);
/* 357 */       while (stringTokenizer.hasMoreTokens()) {
/* 358 */         mailMessage.replyto(stringTokenizer.nextToken());
/*     */       }
/*     */     } 
/* 361 */     StringTokenizer t = new StringTokenizer(values.toList(), ", ", false);
/* 362 */     while (t.hasMoreTokens()) {
/* 363 */       mailMessage.to(t.nextToken());
/*     */     }
/*     */     
/* 366 */     mailMessage.setSubject(values.subject());
/*     */     
/* 368 */     if (values.charset().isEmpty()) {
/* 369 */       mailMessage.setHeader("Content-Type", values.mimeType());
/*     */     } else {
/* 371 */       mailMessage.setHeader("Content-Type", values.mimeType() + "; charset=\"" + values
/* 372 */           .charset() + "\"");
/*     */     } 
/*     */     
/* 375 */     PrintStream ps = mailMessage.getPrintStream();
/* 376 */     ps.println(values.body().isEmpty() ? message : values.body());
/*     */     
/* 378 */     mailMessage.sendAndClose();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendMimeMail(Project project, Values values, String message) {
/* 387 */     Mailer mailer = null;
/*     */     try {
/* 389 */       mailer = (Mailer)ClasspathUtils.newInstance("org.apache.tools.ant.taskdefs.email.MimeMailer", MailLogger.class
/*     */           
/* 391 */           .getClassLoader(), Mailer.class);
/* 392 */     } catch (BuildException e) {
/* 393 */       Throwable t = (e.getCause() == null) ? (Throwable)e : e.getCause();
/* 394 */       log("Failed to initialise MIME mail: " + t.getMessage());
/*     */       
/*     */       return;
/*     */     } 
/* 398 */     Vector<EmailAddress> replyToList = splitEmailAddresses(values.replytoList());
/* 399 */     mailer.setHost(values.mailhost());
/* 400 */     mailer.setPort(values.port());
/* 401 */     mailer.setUser(values.user());
/* 402 */     mailer.setPassword(values.password());
/* 403 */     mailer.setSSL(values.ssl());
/* 404 */     mailer.setEnableStartTLS(values.starttls());
/*     */     
/* 406 */     Message mymessage = new Message(!values.body().isEmpty() ? values.body() : message);
/* 407 */     mymessage.setProject(project);
/* 408 */     mymessage.setMimeType(values.mimeType());
/* 409 */     if (!values.charset().isEmpty()) {
/* 410 */       mymessage.setCharset(values.charset());
/*     */     }
/* 412 */     mailer.setMessage(mymessage);
/* 413 */     mailer.setFrom(new EmailAddress(values.from()));
/* 414 */     mailer.setReplyToList(replyToList);
/* 415 */     Vector<EmailAddress> toList = splitEmailAddresses(values.toList());
/* 416 */     mailer.setToList(toList);
/* 417 */     Vector<EmailAddress> toCcList = splitEmailAddresses(values.toCcList());
/* 418 */     mailer.setCcList(toCcList);
/* 419 */     Vector<EmailAddress> toBccList = splitEmailAddresses(values.toBccList());
/* 420 */     mailer.setBccList(toBccList);
/* 421 */     mailer.setFiles(new Vector());
/* 422 */     mailer.setSubject(values.subject());
/* 423 */     mailer.setHeaders(new Vector());
/* 424 */     mailer.send();
/*     */   }
/*     */   
/*     */   private Vector<EmailAddress> splitEmailAddresses(String listString) {
/* 428 */     return (Vector<EmailAddress>)Stream.<String>of(listString.split(",")).map(EmailAddress::new)
/* 429 */       .collect(Collectors.toCollection(Vector::new));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/listener/MailLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */