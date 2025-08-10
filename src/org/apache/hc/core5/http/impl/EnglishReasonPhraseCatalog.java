/*     */ package org.apache.hc.core5.http.impl;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ReasonPhraseCatalog;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class EnglishReasonPhraseCatalog
/*     */   implements ReasonPhraseCatalog
/*     */ {
/*  53 */   public static final EnglishReasonPhraseCatalog INSTANCE = new EnglishReasonPhraseCatalog();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReason(int status, Locale loc) {
/*  75 */     Args.checkRange(status, 100, 599, "Unknown category for status code");
/*  76 */     int category = status / 100;
/*  77 */     int subcode = status - 100 * category;
/*     */     
/*  79 */     String reason = null;
/*  80 */     if ((REASON_PHRASES[category]).length > subcode) {
/*  81 */       reason = REASON_PHRASES[category][subcode];
/*     */     }
/*     */     
/*  84 */     return reason;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  89 */   private static final String[][] REASON_PHRASES = new String[][] { null, new String[4], new String[27], new String[9], new String[52], new String[12] };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void setReason(int status, String reason) {
/* 108 */     int category = status / 100;
/* 109 */     int subcode = status - 100 * category;
/* 110 */     REASON_PHRASES[category][subcode] = reason;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 119 */     setReason(200, "OK");
/*     */     
/* 121 */     setReason(201, "Created");
/*     */     
/* 123 */     setReason(202, "Accepted");
/*     */     
/* 125 */     setReason(204, "No Content");
/*     */     
/* 127 */     setReason(301, "Moved Permanently");
/*     */     
/* 129 */     setReason(302, "Moved Temporarily");
/*     */     
/* 131 */     setReason(304, "Not Modified");
/*     */     
/* 133 */     setReason(400, "Bad Request");
/*     */     
/* 135 */     setReason(401, "Unauthorized");
/*     */     
/* 137 */     setReason(403, "Forbidden");
/*     */     
/* 139 */     setReason(404, "Not Found");
/*     */     
/* 141 */     setReason(500, "Internal Server Error");
/*     */     
/* 143 */     setReason(501, "Not Implemented");
/*     */     
/* 145 */     setReason(502, "Bad Gateway");
/*     */     
/* 147 */     setReason(503, "Service Unavailable");
/*     */ 
/*     */     
/* 150 */     setReason(100, "Continue");
/*     */     
/* 152 */     setReason(307, "Temporary Redirect");
/*     */     
/* 154 */     setReason(405, "Method Not Allowed");
/*     */     
/* 156 */     setReason(409, "Conflict");
/*     */     
/* 158 */     setReason(412, "Precondition Failed");
/*     */     
/* 160 */     setReason(413, "Request Too Long");
/*     */     
/* 162 */     setReason(414, "Request-URI Too Long");
/*     */     
/* 164 */     setReason(415, "Unsupported Media Type");
/*     */     
/* 166 */     setReason(300, "Multiple Choices");
/*     */     
/* 168 */     setReason(303, "See Other");
/*     */     
/* 170 */     setReason(305, "Use Proxy");
/*     */     
/* 172 */     setReason(402, "Payment Required");
/*     */     
/* 174 */     setReason(406, "Not Acceptable");
/*     */     
/* 176 */     setReason(407, "Proxy Authentication Required");
/*     */     
/* 178 */     setReason(408, "Request Timeout");
/*     */ 
/*     */     
/* 181 */     setReason(101, "Switching Protocols");
/*     */     
/* 183 */     setReason(203, "Non Authoritative Information");
/*     */     
/* 185 */     setReason(205, "Reset Content");
/*     */     
/* 187 */     setReason(206, "Partial Content");
/*     */     
/* 189 */     setReason(504, "Gateway Timeout");
/*     */     
/* 191 */     setReason(505, "Http Version Not Supported");
/*     */     
/* 193 */     setReason(410, "Gone");
/*     */     
/* 195 */     setReason(411, "Length Required");
/*     */     
/* 197 */     setReason(416, "Requested Range Not Satisfiable");
/*     */     
/* 199 */     setReason(417, "Expectation Failed");
/*     */     
/* 201 */     setReason(421, "Misdirected Request");
/*     */ 
/*     */ 
/*     */     
/* 205 */     setReason(102, "Processing");
/*     */     
/* 207 */     setReason(207, "Multi-Status");
/*     */     
/* 209 */     setReason(208, "Already Reported");
/*     */     
/* 211 */     setReason(226, "IM Used");
/*     */     
/* 213 */     setReason(422, "Unprocessable Entity");
/*     */     
/* 215 */     setReason(419, "Insufficient Space On Resource");
/*     */     
/* 217 */     setReason(420, "Method Failure");
/*     */     
/* 219 */     setReason(423, "Locked");
/*     */     
/* 221 */     setReason(507, "Insufficient Storage");
/*     */     
/* 223 */     setReason(508, "Loop Detected");
/*     */     
/* 225 */     setReason(510, "Not Extended");
/*     */     
/* 227 */     setReason(424, "Failed Dependency");
/*     */     
/* 229 */     setReason(425, "Too Early");
/*     */     
/* 231 */     setReason(426, "Upgrade Required");
/*     */ 
/*     */ 
/*     */     
/* 235 */     setReason(428, "Precondition Required");
/*     */     
/* 237 */     setReason(429, "Too Many Requests");
/*     */     
/* 239 */     setReason(431, "Request Header Fields Too Large");
/*     */     
/* 241 */     setReason(511, "Network Authentication Required");
/*     */ 
/*     */ 
/*     */     
/* 245 */     setReason(103, "Early Hints");
/*     */ 
/*     */     
/* 248 */     setReason(308, "Permanent Redirect");
/*     */ 
/*     */     
/* 251 */     setReason(451, "Unavailable For Legal Reasons");
/*     */ 
/*     */     
/* 254 */     setReason(506, "Variant Also Negotiates");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/EnglishReasonPhraseCatalog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */