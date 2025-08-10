/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.util.Date;
/*     */ import org.apache.log4j.helpers.Transform;
/*     */ import org.apache.log4j.spi.LocationInfo;
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
/*     */ public class HTMLLayout
/*     */   extends Layout
/*     */ {
/*  35 */   protected final int BUF_SIZE = 256;
/*  36 */   protected final int MAX_CAPACITY = 1024;
/*     */   
/*  38 */   static String TRACE_PREFIX = "<br>&nbsp;&nbsp;&nbsp;&nbsp;";
/*     */ 
/*     */   
/*  41 */   private StringBuffer sbuf = new StringBuffer(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String LOCATION_INFO_OPTION = "LocationInfo";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String TITLE_OPTION = "Title";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean locationInfo = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   String title = "Log4J Log Messages";
/*     */ 
/*     */ 
/*     */ 
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
/*  81 */     this.locationInfo = flag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getLocationInfo() {
/*  88 */     return this.locationInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTitle(String title) {
/*  99 */     this.title = title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTitle() {
/* 106 */     return this.title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 113 */     return "text/html";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public String format(LoggingEvent event) {
/* 124 */     if (this.sbuf.capacity() > 1024) {
/* 125 */       this.sbuf = new StringBuffer(256);
/*     */     } else {
/* 127 */       this.sbuf.setLength(0);
/*     */     } 
/*     */     
/* 130 */     this.sbuf.append(Layout.LINE_SEP + "<tr>" + Layout.LINE_SEP);
/*     */     
/* 132 */     this.sbuf.append("<td>");
/* 133 */     this.sbuf.append(event.timeStamp - LoggingEvent.getStartTime());
/* 134 */     this.sbuf.append("</td>" + Layout.LINE_SEP);
/*     */     
/* 136 */     String escapedThread = Transform.escapeTags(event.getThreadName());
/* 137 */     this.sbuf.append("<td title=\"" + escapedThread + " thread\">");
/* 138 */     this.sbuf.append(escapedThread);
/* 139 */     this.sbuf.append("</td>" + Layout.LINE_SEP);
/*     */     
/* 141 */     this.sbuf.append("<td title=\"Level\">");
/* 142 */     if (event.getLevel().equals(Level.DEBUG)) {
/* 143 */       this.sbuf.append("<font color=\"#339933\">");
/* 144 */       this.sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
/* 145 */       this.sbuf.append("</font>");
/* 146 */     } else if (event.getLevel().isGreaterOrEqual(Level.WARN)) {
/* 147 */       this.sbuf.append("<font color=\"#993300\"><strong>");
/* 148 */       this.sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
/* 149 */       this.sbuf.append("</strong></font>");
/*     */     } else {
/* 151 */       this.sbuf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
/*     */     } 
/* 153 */     this.sbuf.append("</td>" + Layout.LINE_SEP);
/*     */     
/* 155 */     String escapedLogger = Transform.escapeTags(event.getLoggerName());
/* 156 */     this.sbuf.append("<td title=\"" + escapedLogger + " category\">");
/* 157 */     this.sbuf.append(escapedLogger);
/* 158 */     this.sbuf.append("</td>" + Layout.LINE_SEP);
/*     */     
/* 160 */     if (this.locationInfo) {
/* 161 */       LocationInfo locInfo = event.getLocationInformation();
/* 162 */       this.sbuf.append("<td>");
/* 163 */       this.sbuf.append(Transform.escapeTags(locInfo.getFileName()));
/* 164 */       this.sbuf.append(':');
/* 165 */       this.sbuf.append(locInfo.getLineNumber());
/* 166 */       this.sbuf.append("</td>" + Layout.LINE_SEP);
/*     */     } 
/*     */     
/* 169 */     this.sbuf.append("<td title=\"Message\">");
/* 170 */     this.sbuf.append(Transform.escapeTags(event.getRenderedMessage()));
/* 171 */     this.sbuf.append("</td>" + Layout.LINE_SEP);
/* 172 */     this.sbuf.append("</tr>" + Layout.LINE_SEP);
/*     */     
/* 174 */     if (event.getNDC() != null) {
/* 175 */       this.sbuf.append("<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : xx-small;\" colspan=\"6\" title=\"Nested Diagnostic Context\">");
/*     */       
/* 177 */       this.sbuf.append("NDC: " + Transform.escapeTags(event.getNDC()));
/* 178 */       this.sbuf.append("</td></tr>" + Layout.LINE_SEP);
/*     */     } 
/*     */     
/* 181 */     String[] s = event.getThrowableStrRep();
/* 182 */     if (s != null) {
/* 183 */       this.sbuf.append("<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : xx-small;\" colspan=\"6\">");
/* 184 */       appendThrowableAsHTML(s, this.sbuf);
/* 185 */       this.sbuf.append("</td></tr>" + Layout.LINE_SEP);
/*     */     } 
/*     */     
/* 188 */     return this.sbuf.toString();
/*     */   }
/*     */   
/*     */   void appendThrowableAsHTML(String[] s, StringBuffer sbuf) {
/* 192 */     if (s != null) {
/* 193 */       int len = s.length;
/* 194 */       if (len == 0)
/*     */         return; 
/* 196 */       sbuf.append(Transform.escapeTags(s[0]));
/* 197 */       sbuf.append(Layout.LINE_SEP);
/* 198 */       for (int i = 1; i < len; i++) {
/* 199 */         sbuf.append(TRACE_PREFIX);
/* 200 */         sbuf.append(Transform.escapeTags(s[i]));
/* 201 */         sbuf.append(Layout.LINE_SEP);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHeader() {
/* 210 */     StringBuilder sbuf = new StringBuilder();
/* 211 */     sbuf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" + Layout.LINE_SEP);
/*     */ 
/*     */     
/* 214 */     sbuf.append("<html>" + Layout.LINE_SEP);
/* 215 */     sbuf.append("<head>" + Layout.LINE_SEP);
/* 216 */     sbuf.append("<title>" + this.title + "</title>" + Layout.LINE_SEP);
/* 217 */     sbuf.append("<style type=\"text/css\">" + Layout.LINE_SEP);
/* 218 */     sbuf.append("<!--" + Layout.LINE_SEP);
/* 219 */     sbuf.append("body, table {font-family: arial,sans-serif; font-size: x-small;}" + Layout.LINE_SEP);
/* 220 */     sbuf.append("th {background: #336699; color: #FFFFFF; text-align: left;}" + Layout.LINE_SEP);
/* 221 */     sbuf.append("-->" + Layout.LINE_SEP);
/* 222 */     sbuf.append("</style>" + Layout.LINE_SEP);
/* 223 */     sbuf.append("</head>" + Layout.LINE_SEP);
/* 224 */     sbuf.append("<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">" + Layout.LINE_SEP);
/* 225 */     sbuf.append("<hr size=\"1\" noshade>" + Layout.LINE_SEP);
/* 226 */     sbuf.append("Log session start time " + new Date() + "<br>" + Layout.LINE_SEP);
/* 227 */     sbuf.append("<br>" + Layout.LINE_SEP);
/* 228 */     sbuf.append("<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">" + Layout.LINE_SEP);
/*     */     
/* 230 */     sbuf.append("<tr>" + Layout.LINE_SEP);
/* 231 */     sbuf.append("<th>Time</th>" + Layout.LINE_SEP);
/* 232 */     sbuf.append("<th>Thread</th>" + Layout.LINE_SEP);
/* 233 */     sbuf.append("<th>Level</th>" + Layout.LINE_SEP);
/* 234 */     sbuf.append("<th>Category</th>" + Layout.LINE_SEP);
/* 235 */     if (this.locationInfo) {
/* 236 */       sbuf.append("<th>File:Line</th>" + Layout.LINE_SEP);
/*     */     }
/* 238 */     sbuf.append("<th>Message</th>" + Layout.LINE_SEP);
/* 239 */     sbuf.append("</tr>" + Layout.LINE_SEP);
/* 240 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFooter() {
/* 247 */     StringBuilder sbuf = new StringBuilder();
/* 248 */     sbuf.append("</table>" + Layout.LINE_SEP);
/* 249 */     sbuf.append("<br>" + Layout.LINE_SEP);
/* 250 */     sbuf.append("</body></html>");
/* 251 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ignoresThrowable() {
/* 259 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/HTMLLayout.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */