/*     */ package org.apache.log4j.xml;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Set;
/*     */ import org.apache.log4j.Layout;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLLayout
/*     */   extends Layout
/*     */ {
/*  69 */   private final int DEFAULT_SIZE = 256;
/*  70 */   private final int UPPER_LIMIT = 2048;
/*     */   
/*  72 */   private StringBuffer buf = new StringBuffer(256);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean locationInfo = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean properties = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocationInfo(boolean flag) {
/*  88 */     this.locationInfo = flag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getLocationInfo() {
/*  95 */     return this.locationInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperties(boolean flag) {
/* 105 */     this.properties = flag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getProperties() {
/* 115 */     return this.properties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String format(LoggingEvent event) {
/* 130 */     if (this.buf.capacity() > 2048) {
/* 131 */       this.buf = new StringBuffer(256);
/*     */     } else {
/* 133 */       this.buf.setLength(0);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 138 */     this.buf.append("<log4j:event logger=\"");
/* 139 */     this.buf.append(Transform.escapeTags(event.getLoggerName()));
/* 140 */     this.buf.append("\" timestamp=\"");
/* 141 */     this.buf.append(event.timeStamp);
/* 142 */     this.buf.append("\" level=\"");
/* 143 */     this.buf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
/* 144 */     this.buf.append("\" thread=\"");
/* 145 */     this.buf.append(Transform.escapeTags(event.getThreadName()));
/* 146 */     this.buf.append("\">\r\n");
/*     */     
/* 148 */     this.buf.append("<log4j:message><![CDATA[");
/*     */ 
/*     */     
/* 151 */     Transform.appendEscapingCDATA(this.buf, event.getRenderedMessage());
/* 152 */     this.buf.append("]]></log4j:message>\r\n");
/*     */     
/* 154 */     String ndc = event.getNDC();
/* 155 */     if (ndc != null) {
/* 156 */       this.buf.append("<log4j:NDC><![CDATA[");
/* 157 */       Transform.appendEscapingCDATA(this.buf, ndc);
/* 158 */       this.buf.append("]]></log4j:NDC>\r\n");
/*     */     } 
/*     */     
/* 161 */     String[] s = event.getThrowableStrRep();
/* 162 */     if (s != null) {
/* 163 */       this.buf.append("<log4j:throwable><![CDATA[");
/* 164 */       for (int i = 0; i < s.length; i++) {
/* 165 */         Transform.appendEscapingCDATA(this.buf, s[i]);
/* 166 */         this.buf.append("\r\n");
/*     */       } 
/* 168 */       this.buf.append("]]></log4j:throwable>\r\n");
/*     */     } 
/*     */     
/* 171 */     if (this.locationInfo) {
/* 172 */       LocationInfo locationInfo = event.getLocationInformation();
/* 173 */       this.buf.append("<log4j:locationInfo class=\"");
/* 174 */       this.buf.append(Transform.escapeTags(locationInfo.getClassName()));
/* 175 */       this.buf.append("\" method=\"");
/* 176 */       this.buf.append(Transform.escapeTags(locationInfo.getMethodName()));
/* 177 */       this.buf.append("\" file=\"");
/* 178 */       this.buf.append(Transform.escapeTags(locationInfo.getFileName()));
/* 179 */       this.buf.append("\" line=\"");
/* 180 */       this.buf.append(locationInfo.getLineNumber());
/* 181 */       this.buf.append("\"/>\r\n");
/*     */     } 
/*     */     
/* 184 */     if (this.properties) {
/* 185 */       Set keySet = event.getPropertyKeySet();
/* 186 */       if (keySet.size() > 0) {
/* 187 */         this.buf.append("<log4j:properties>\r\n");
/* 188 */         Object[] keys = keySet.toArray();
/* 189 */         Arrays.sort(keys);
/* 190 */         for (int i = 0; i < keys.length; i++) {
/* 191 */           String key = keys[i].toString();
/* 192 */           Object val = event.getMDC(key);
/* 193 */           if (val != null) {
/* 194 */             this.buf.append("<log4j:data name=\"");
/* 195 */             this.buf.append(Transform.escapeTags(key));
/* 196 */             this.buf.append("\" value=\"");
/* 197 */             this.buf.append(Transform.escapeTags(String.valueOf(val)));
/* 198 */             this.buf.append("\"/>\r\n");
/*     */           } 
/*     */         } 
/* 201 */         this.buf.append("</log4j:properties>\r\n");
/*     */       } 
/*     */     } 
/*     */     
/* 205 */     this.buf.append("</log4j:event>\r\n\r\n");
/*     */     
/* 207 */     return this.buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean ignoresThrowable() {
/* 215 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/xml/XMLLayout.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */