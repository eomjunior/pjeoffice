/*     */ package org.apache.tools.mail;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MailMessage
/*     */ {
/*     */   public static final String DEFAULT_HOST = "localhost";
/*     */   public static final int DEFAULT_PORT = 25;
/*     */   private String host;
/* 111 */   private int port = 25;
/*     */ 
/*     */   
/*     */   private String from;
/*     */ 
/*     */   
/* 117 */   private final Vector<String> replyto = new Vector<>();
/*     */ 
/*     */   
/* 120 */   private final Vector<String> to = new Vector<>();
/*     */ 
/*     */   
/* 123 */   private final Vector<String> cc = new Vector<>();
/*     */ 
/*     */   
/* 126 */   private final Map<String, String> headers = new LinkedHashMap<>();
/*     */   
/*     */   private MailPrintStream out;
/*     */   
/*     */   private SmtpResponseReader in;
/*     */   
/*     */   private Socket socket;
/*     */   
/*     */   private static final int OK_READY = 220;
/*     */   
/*     */   private static final int OK_HELO = 250;
/*     */   
/*     */   private static final int OK_FROM = 250;
/*     */   
/*     */   private static final int OK_RCPT_1 = 250;
/*     */   
/*     */   private static final int OK_RCPT_2 = 251;
/*     */   
/*     */   private static final int OK_DATA = 354;
/*     */   
/*     */   private static final int OK_DOT = 250;
/*     */   private static final int OK_QUIT = 221;
/*     */   
/*     */   public MailMessage() throws IOException {
/* 150 */     this("localhost", 25);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MailMessage(String host) throws IOException {
/* 161 */     this(host, 25);
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
/*     */   public MailMessage(String host, int port) throws IOException {
/* 173 */     this.port = port;
/* 174 */     this.host = host;
/* 175 */     connect();
/* 176 */     sendHelo();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPort(int port) {
/* 185 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void from(String from) throws IOException {
/* 195 */     sendFrom(from);
/* 196 */     this.from = from;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replyto(String rto) {
/* 207 */     this.replyto.addElement(rto);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void to(String to) throws IOException {
/* 218 */     sendRcpt(to);
/* 219 */     this.to.addElement(to);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cc(String cc) throws IOException {
/* 230 */     sendRcpt(cc);
/* 231 */     this.cc.addElement(cc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bcc(String bcc) throws IOException {
/* 242 */     sendRcpt(bcc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubject(String subj) {
/* 252 */     setHeader("Subject", subj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHeader(String name, String value) {
/* 263 */     this.headers.put(name, value);
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
/*     */   public PrintStream getPrintStream() throws IOException {
/* 277 */     setFromHeader();
/* 278 */     setReplyToHeader();
/* 279 */     setToHeader();
/* 280 */     setCcHeader();
/* 281 */     setHeader("X-Mailer", "org.apache.tools.mail.MailMessage (ant.apache.org)");
/*     */     
/* 283 */     sendData();
/* 284 */     flushHeaders();
/* 285 */     return this.out;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void setFromHeader() {
/* 291 */     setHeader("From", this.from);
/*     */   }
/*     */ 
/*     */   
/*     */   void setReplyToHeader() {
/* 296 */     if (!this.replyto.isEmpty()) {
/* 297 */       setHeader("Reply-To", vectorToList(this.replyto));
/*     */     }
/*     */   }
/*     */   
/*     */   void setToHeader() {
/* 302 */     if (!this.to.isEmpty()) {
/* 303 */       setHeader("To", vectorToList(this.to));
/*     */     }
/*     */   }
/*     */   
/*     */   void setCcHeader() {
/* 308 */     if (!this.cc.isEmpty()) {
/* 309 */       setHeader("Cc", vectorToList(this.cc));
/*     */     }
/*     */   }
/*     */   
/*     */   String vectorToList(Vector<String> v) {
/* 314 */     return String.join(", ", (Iterable)v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void flushHeaders() throws IOException {
/* 322 */     this.headers.forEach((k, v) -> this.out.printf("%s: %s%n", new Object[] { k, v }));
/* 323 */     this.out.println();
/* 324 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendAndClose() throws IOException {
/*     */     try {
/* 335 */       sendDot();
/* 336 */       sendQuit();
/*     */     } finally {
/* 338 */       disconnect();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static String sanitizeAddress(String s) {
/* 345 */     int paramDepth = 0;
/* 346 */     int start = 0;
/* 347 */     int end = 0;
/* 348 */     int len = s.length();
/*     */     
/* 350 */     for (int i = 0; i < len; i++) {
/* 351 */       char c = s.charAt(i);
/* 352 */       if (c == '(') {
/* 353 */         paramDepth++;
/* 354 */         if (start == 0) {
/* 355 */           end = i;
/*     */         }
/* 357 */       } else if (c == ')') {
/* 358 */         paramDepth--;
/* 359 */         if (end == 0) {
/* 360 */           start = i + 1;
/*     */         }
/* 362 */       } else if (paramDepth == 0 && c == '<') {
/* 363 */         start = i + 1;
/* 364 */       } else if (paramDepth == 0 && c == '>') {
/* 365 */         end = i;
/*     */       } 
/*     */     } 
/*     */     
/* 369 */     if (end == 0) {
/* 370 */       end = len;
/*     */     }
/*     */     
/* 373 */     return s.substring(start, end);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void connect() throws IOException {
/* 379 */     this.socket = new Socket(this.host, this.port);
/* 380 */     this
/* 381 */       .out = new MailPrintStream(new BufferedOutputStream(this.socket.getOutputStream()));
/* 382 */     this.in = new SmtpResponseReader(this.socket.getInputStream());
/* 383 */     getReady();
/*     */   }
/*     */   
/*     */   void getReady() throws IOException {
/* 387 */     String response = this.in.getResponse();
/* 388 */     int[] ok = { 220 };
/* 389 */     if (!isResponseOK(response, ok)) {
/* 390 */       throw new IOException("Didn't get introduction from server: " + response);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void sendHelo() throws IOException {
/* 396 */     String local = InetAddress.getLocalHost().getCanonicalHostName();
/* 397 */     int[] ok = { 250 };
/* 398 */     send("HELO " + local, ok);
/*     */   }
/*     */   
/*     */   void sendFrom(String from) throws IOException {
/* 402 */     int[] ok = { 250 };
/* 403 */     send("MAIL FROM: <" + sanitizeAddress(from) + ">", ok);
/*     */   }
/*     */   
/*     */   void sendRcpt(String rcpt) throws IOException {
/* 407 */     int[] ok = { 250, 251 };
/* 408 */     send("RCPT TO: <" + sanitizeAddress(rcpt) + ">", ok);
/*     */   }
/*     */   
/*     */   void sendData() throws IOException {
/* 412 */     int[] ok = { 354 };
/* 413 */     send("DATA", ok);
/*     */   }
/*     */   
/*     */   void sendDot() throws IOException {
/* 417 */     int[] ok = { 250 };
/* 418 */     send("\r\n.", ok);
/*     */   }
/*     */   
/*     */   void sendQuit() throws IOException {
/* 422 */     int[] ok = { 221 };
/*     */     try {
/* 424 */       send("QUIT", ok);
/* 425 */     } catch (IOException e) {
/* 426 */       throw new ErrorInQuitException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   void send(String msg, int[] ok) throws IOException {
/* 431 */     this.out.rawPrint(msg + "\r\n");
/* 432 */     String response = this.in.getResponse();
/* 433 */     if (!isResponseOK(response, ok)) {
/* 434 */       throw new IOException("Unexpected reply to command: " + msg + ": " + response);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isResponseOK(String response, int[] ok) {
/* 441 */     for (int status : ok) {
/* 442 */       if (response.startsWith("" + status)) {
/* 443 */         return true;
/*     */       }
/*     */     } 
/* 446 */     return false;
/*     */   }
/*     */   
/*     */   void disconnect() throws IOException {
/* 450 */     if (this.out != null) {
/* 451 */       this.out.close();
/*     */     }
/* 453 */     if (this.in != null) {
/*     */       try {
/* 455 */         this.in.close();
/* 456 */       } catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */     
/* 460 */     if (this.socket != null)
/*     */       try {
/* 462 */         this.socket.close();
/* 463 */       } catch (IOException iOException) {} 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/mail/MailMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */