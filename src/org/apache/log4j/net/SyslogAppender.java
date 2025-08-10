/*     */ package org.apache.log4j.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.Writer;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import org.apache.log4j.AppenderSkeleton;
/*     */ import org.apache.log4j.Layout;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.apache.log4j.helpers.SyslogQuietWriter;
/*     */ import org.apache.log4j.helpers.SyslogWriter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SyslogAppender
/*     */   extends AppenderSkeleton
/*     */ {
/*     */   public static final int LOG_KERN = 0;
/*     */   public static final int LOG_USER = 8;
/*     */   public static final int LOG_MAIL = 16;
/*     */   public static final int LOG_DAEMON = 24;
/*     */   public static final int LOG_AUTH = 32;
/*     */   public static final int LOG_SYSLOG = 40;
/*     */   public static final int LOG_LPR = 48;
/*     */   public static final int LOG_NEWS = 56;
/*     */   public static final int LOG_UUCP = 64;
/*     */   public static final int LOG_CRON = 72;
/*     */   public static final int LOG_AUTHPRIV = 80;
/*     */   public static final int LOG_FTP = 88;
/*     */   public static final int LOG_LOCAL0 = 128;
/*     */   public static final int LOG_LOCAL1 = 136;
/*     */   public static final int LOG_LOCAL2 = 144;
/*     */   public static final int LOG_LOCAL3 = 152;
/*     */   public static final int LOG_LOCAL4 = 160;
/*     */   public static final int LOG_LOCAL5 = 168;
/*     */   public static final int LOG_LOCAL6 = 176;
/*     */   public static final int LOG_LOCAL7 = 184;
/*     */   protected static final int SYSLOG_HOST_OI = 0;
/*     */   protected static final int FACILITY_OI = 1;
/*     */   static final String TAB = "    ";
/*  97 */   int syslogFacility = 8;
/*     */ 
/*     */ 
/*     */   
/*     */   String facilityStr;
/*     */ 
/*     */ 
/*     */   
/*     */   boolean facilityPrinting = false;
/*     */ 
/*     */   
/*     */   SyslogQuietWriter sqw;
/*     */ 
/*     */   
/*     */   String syslogHost;
/*     */ 
/*     */   
/*     */   private boolean header = false;
/*     */ 
/*     */   
/* 117 */   private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm:ss ", Locale.ENGLISH);
/*     */ 
/*     */ 
/*     */   
/*     */   private String localHostname;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean layoutHeaderChecked = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SyslogAppender() {
/* 131 */     initSyslogFacilityStr();
/*     */   }
/*     */   
/*     */   public SyslogAppender(Layout layout, int syslogFacility) {
/* 135 */     this.layout = layout;
/* 136 */     this.syslogFacility = syslogFacility;
/* 137 */     initSyslogFacilityStr();
/*     */   }
/*     */   
/*     */   public SyslogAppender(Layout layout, String syslogHost, int syslogFacility) {
/* 141 */     this(layout, syslogFacility);
/* 142 */     setSyslogHost(syslogHost);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void close() {
/* 151 */     this.closed = true;
/* 152 */     if (this.sqw != null) {
/*     */       try {
/* 154 */         if (this.layoutHeaderChecked && this.layout != null && this.layout.getFooter() != null) {
/* 155 */           sendLayoutMessage(this.layout.getFooter());
/*     */         }
/* 157 */         this.sqw.close();
/* 158 */         this.sqw = null;
/* 159 */       } catch (InterruptedIOException e) {
/* 160 */         Thread.currentThread().interrupt();
/* 161 */         this.sqw = null;
/* 162 */       } catch (IOException e) {
/* 163 */         this.sqw = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void initSyslogFacilityStr() {
/* 169 */     this.facilityStr = getFacilityString(this.syslogFacility);
/*     */     
/* 171 */     if (this.facilityStr == null) {
/* 172 */       System.err.println("\"" + this.syslogFacility + "\" is an unknown syslog facility. Defaulting to \"USER\".");
/* 173 */       this.syslogFacility = 8;
/* 174 */       this.facilityStr = "user:";
/*     */     } else {
/* 176 */       this.facilityStr += ":";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getFacilityString(int syslogFacility) {
/* 185 */     switch (syslogFacility) {
/*     */       case 0:
/* 187 */         return "kern";
/*     */       case 8:
/* 189 */         return "user";
/*     */       case 16:
/* 191 */         return "mail";
/*     */       case 24:
/* 193 */         return "daemon";
/*     */       case 32:
/* 195 */         return "auth";
/*     */       case 40:
/* 197 */         return "syslog";
/*     */       case 48:
/* 199 */         return "lpr";
/*     */       case 56:
/* 201 */         return "news";
/*     */       case 64:
/* 203 */         return "uucp";
/*     */       case 72:
/* 205 */         return "cron";
/*     */       case 80:
/* 207 */         return "authpriv";
/*     */       case 88:
/* 209 */         return "ftp";
/*     */       case 128:
/* 211 */         return "local0";
/*     */       case 136:
/* 213 */         return "local1";
/*     */       case 144:
/* 215 */         return "local2";
/*     */       case 152:
/* 217 */         return "local3";
/*     */       case 160:
/* 219 */         return "local4";
/*     */       case 168:
/* 221 */         return "local5";
/*     */       case 176:
/* 223 */         return "local6";
/*     */       case 184:
/* 225 */         return "local7";
/*     */     } 
/* 227 */     return null;
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
/*     */   public static int getFacility(String facilityName) {
/* 243 */     if (facilityName != null) {
/* 244 */       facilityName = facilityName.trim();
/*     */     }
/* 246 */     if ("KERN".equalsIgnoreCase(facilityName))
/* 247 */       return 0; 
/* 248 */     if ("USER".equalsIgnoreCase(facilityName))
/* 249 */       return 8; 
/* 250 */     if ("MAIL".equalsIgnoreCase(facilityName))
/* 251 */       return 16; 
/* 252 */     if ("DAEMON".equalsIgnoreCase(facilityName))
/* 253 */       return 24; 
/* 254 */     if ("AUTH".equalsIgnoreCase(facilityName))
/* 255 */       return 32; 
/* 256 */     if ("SYSLOG".equalsIgnoreCase(facilityName))
/* 257 */       return 40; 
/* 258 */     if ("LPR".equalsIgnoreCase(facilityName))
/* 259 */       return 48; 
/* 260 */     if ("NEWS".equalsIgnoreCase(facilityName))
/* 261 */       return 56; 
/* 262 */     if ("UUCP".equalsIgnoreCase(facilityName))
/* 263 */       return 64; 
/* 264 */     if ("CRON".equalsIgnoreCase(facilityName))
/* 265 */       return 72; 
/* 266 */     if ("AUTHPRIV".equalsIgnoreCase(facilityName))
/* 267 */       return 80; 
/* 268 */     if ("FTP".equalsIgnoreCase(facilityName))
/* 269 */       return 88; 
/* 270 */     if ("LOCAL0".equalsIgnoreCase(facilityName))
/* 271 */       return 128; 
/* 272 */     if ("LOCAL1".equalsIgnoreCase(facilityName))
/* 273 */       return 136; 
/* 274 */     if ("LOCAL2".equalsIgnoreCase(facilityName))
/* 275 */       return 144; 
/* 276 */     if ("LOCAL3".equalsIgnoreCase(facilityName))
/* 277 */       return 152; 
/* 278 */     if ("LOCAL4".equalsIgnoreCase(facilityName))
/* 279 */       return 160; 
/* 280 */     if ("LOCAL5".equalsIgnoreCase(facilityName))
/* 281 */       return 168; 
/* 282 */     if ("LOCAL6".equalsIgnoreCase(facilityName))
/* 283 */       return 176; 
/* 284 */     if ("LOCAL7".equalsIgnoreCase(facilityName)) {
/* 285 */       return 184;
/*     */     }
/* 287 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private void splitPacket(String header, String packet) {
/* 292 */     int byteCount = (packet.getBytes()).length;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 298 */     if (byteCount <= 1019) {
/* 299 */       this.sqw.write(packet);
/*     */     } else {
/* 301 */       int split = header.length() + (packet.length() - header.length()) / 2;
/* 302 */       splitPacket(header, packet.substring(0, split) + "...");
/* 303 */       splitPacket(header, header + "..." + packet.substring(split));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void append(LoggingEvent event) {
/*     */     String packet;
/* 309 */     if (!isAsSevereAsThreshold((Priority)event.getLevel())) {
/*     */       return;
/*     */     }
/*     */     
/* 313 */     if (this.sqw == null) {
/* 314 */       this.errorHandler.error("No syslog host is set for SyslogAppedender named \"" + this.name + "\".");
/*     */       
/*     */       return;
/*     */     } 
/* 318 */     if (!this.layoutHeaderChecked) {
/* 319 */       if (this.layout != null && this.layout.getHeader() != null) {
/* 320 */         sendLayoutMessage(this.layout.getHeader());
/*     */       }
/* 322 */       this.layoutHeaderChecked = true;
/*     */     } 
/*     */     
/* 325 */     String hdr = getPacketHeader(event.timeStamp);
/*     */     
/* 327 */     if (this.layout == null) {
/* 328 */       packet = String.valueOf(event.getMessage());
/*     */     } else {
/* 330 */       packet = this.layout.format(event);
/*     */     } 
/* 332 */     if (this.facilityPrinting || hdr.length() > 0) {
/* 333 */       StringBuilder buf = new StringBuilder(hdr);
/* 334 */       if (this.facilityPrinting) {
/* 335 */         buf.append(this.facilityStr);
/*     */       }
/* 337 */       buf.append(packet);
/* 338 */       packet = buf.toString();
/*     */     } 
/*     */     
/* 341 */     this.sqw.setLevel(event.getLevel().getSyslogEquivalent());
/*     */ 
/*     */ 
/*     */     
/* 345 */     if (packet.length() > 256) {
/* 346 */       splitPacket(hdr, packet);
/*     */     } else {
/* 348 */       this.sqw.write(packet);
/*     */     } 
/*     */     
/* 351 */     if (this.layout == null || this.layout.ignoresThrowable()) {
/* 352 */       String[] s = event.getThrowableStrRep();
/* 353 */       if (s != null) {
/* 354 */         for (int i = 0; i < s.length; i++) {
/* 355 */           if (s[i].startsWith("\t")) {
/* 356 */             this.sqw.write(hdr + "    " + s[i].substring(1));
/*     */           } else {
/* 358 */             this.sqw.write(hdr + s[i]);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/* 369 */     if (this.header) {
/* 370 */       getLocalHostname();
/*     */     }
/* 372 */     if (this.layout != null && this.layout.getHeader() != null) {
/* 373 */       sendLayoutMessage(this.layout.getHeader());
/*     */     }
/* 375 */     this.layoutHeaderChecked = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresLayout() {
/* 385 */     return true;
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
/*     */   public void setSyslogHost(String syslogHost) {
/* 397 */     this.sqw = new SyslogQuietWriter((Writer)new SyslogWriter(syslogHost), this.syslogFacility, this.errorHandler);
/*     */     
/* 399 */     this.syslogHost = syslogHost;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSyslogHost() {
/* 406 */     return this.syslogHost;
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
/*     */   public void setFacility(String facilityName) {
/* 421 */     if (facilityName == null) {
/*     */       return;
/*     */     }
/* 424 */     this.syslogFacility = getFacility(facilityName);
/* 425 */     if (this.syslogFacility == -1) {
/* 426 */       System.err.println("[" + facilityName + "] is an unknown syslog facility. Defaulting to [USER].");
/* 427 */       this.syslogFacility = 8;
/*     */     } 
/*     */     
/* 430 */     initSyslogFacilityStr();
/*     */ 
/*     */     
/* 433 */     if (this.sqw != null) {
/* 434 */       this.sqw.setSyslogFacility(this.syslogFacility);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFacility() {
/* 442 */     return getFacilityString(this.syslogFacility);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFacilityPrinting(boolean on) {
/* 451 */     this.facilityPrinting = on;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFacilityPrinting() {
/* 458 */     return this.facilityPrinting;
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
/*     */   public final boolean getHeader() {
/* 470 */     return this.header;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setHeader(boolean val) {
/* 480 */     this.header = val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getLocalHostname() {
/* 490 */     if (this.localHostname == null) {
/*     */       try {
/* 492 */         InetAddress addr = InetAddress.getLocalHost();
/* 493 */         this.localHostname = addr.getHostName();
/* 494 */       } catch (UnknownHostException uhe) {
/* 495 */         this.localHostname = "UNKNOWN_HOST";
/*     */       } 
/*     */     }
/* 498 */     return this.localHostname;
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
/*     */   private String getPacketHeader(long timeStamp) {
/* 510 */     if (this.header) {
/* 511 */       StringBuilder buf = new StringBuilder(this.dateFormat.format(new Date(timeStamp)));
/*     */       
/* 513 */       if (buf.charAt(4) == '0') {
/* 514 */         buf.setCharAt(4, ' ');
/*     */       }
/* 516 */       buf.append(getLocalHostname());
/* 517 */       buf.append(' ');
/* 518 */       return buf.toString();
/*     */     } 
/* 520 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendLayoutMessage(String msg) {
/* 529 */     if (this.sqw != null) {
/* 530 */       String packet = msg;
/* 531 */       String hdr = getPacketHeader((new Date()).getTime());
/* 532 */       if (this.facilityPrinting || hdr.length() > 0) {
/* 533 */         StringBuilder buf = new StringBuilder(hdr);
/* 534 */         if (this.facilityPrinting) {
/* 535 */           buf.append(this.facilityStr);
/*     */         }
/* 537 */         buf.append(msg);
/* 538 */         packet = buf.toString();
/*     */       } 
/* 540 */       this.sqw.setLevel(6);
/* 541 */       this.sqw.write(packet);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/net/SyslogAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */