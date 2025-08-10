/*     */ package org.apache.tools.ant.taskdefs.cvslib;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.tools.ant.taskdefs.AbstractCvsTask;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CvsVersion
/*     */   extends AbstractCvsTask
/*     */ {
/*     */   static final long VERSION_1_11_2 = 11102L;
/*     */   static final long MULTIPLY = 100L;
/*     */   private String clientVersion;
/*     */   private String serverVersion;
/*     */   private String clientVersionProperty;
/*     */   private String serverVersionProperty;
/*     */   
/*     */   public String getClientVersion() {
/*  57 */     return this.clientVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServerVersion() {
/*  65 */     return this.serverVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientVersionProperty(String clientVersionProperty) {
/*  73 */     this.clientVersionProperty = clientVersionProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServerVersionProperty(String serverVersionProperty) {
/*  81 */     this.serverVersionProperty = serverVersionProperty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsCvsLogWithSOption() {
/*  89 */     if (this.serverVersion == null) {
/*  90 */       return false;
/*     */     }
/*  92 */     StringTokenizer tokenizer = new StringTokenizer(this.serverVersion, ".");
/*  93 */     long counter = 10000L;
/*  94 */     long version = 0L;
/*  95 */     while (tokenizer.hasMoreTokens()) {
/*  96 */       String s = tokenizer.nextToken();
/*     */       int i;
/*  98 */       for (i = 0; i < s.length() && 
/*  99 */         Character.isDigit(s.charAt(i)); i++);
/*     */ 
/*     */ 
/*     */       
/* 103 */       String s2 = s.substring(0, i);
/* 104 */       version += counter * Long.parseLong(s2);
/* 105 */       if (counter == 1L) {
/*     */         break;
/*     */       }
/* 108 */       counter /= 100L;
/*     */     } 
/* 110 */     return (version >= 11102L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 118 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 119 */     setOutputStream(bos);
/* 120 */     ByteArrayOutputStream berr = new ByteArrayOutputStream();
/* 121 */     setErrorStream(berr);
/* 122 */     setCommand("version");
/* 123 */     super.execute();
/* 124 */     String output = bos.toString();
/* 125 */     log("Received version response \"" + output + "\"", 4);
/*     */     
/* 127 */     StringTokenizer st = new StringTokenizer(output);
/* 128 */     boolean client = false;
/* 129 */     boolean server = false;
/* 130 */     String cvs = null;
/* 131 */     String cachedVersion = null;
/* 132 */     boolean haveReadAhead = false;
/* 133 */     while (haveReadAhead || st.hasMoreTokens()) {
/* 134 */       String currentToken = haveReadAhead ? cachedVersion : st.nextToken();
/* 135 */       haveReadAhead = false;
/* 136 */       if ("Client:".equals(currentToken)) {
/* 137 */         client = true;
/* 138 */       } else if ("Server:".equals(currentToken)) {
/* 139 */         server = true;
/* 140 */       } else if (currentToken.startsWith("(CVS") && currentToken
/* 141 */         .endsWith(")")) {
/* 142 */         cvs = (currentToken.length() == 5) ? "" : (" " + currentToken);
/*     */       } 
/* 144 */       if (!client && !server && cvs != null && cachedVersion == null && st
/* 145 */         .hasMoreTokens()) {
/* 146 */         cachedVersion = st.nextToken();
/* 147 */         haveReadAhead = true; continue;
/* 148 */       }  if (client && cvs != null) {
/* 149 */         if (st.hasMoreTokens()) {
/* 150 */           this.clientVersion = st.nextToken() + cvs;
/*     */         }
/* 152 */         client = false;
/* 153 */         cvs = null; continue;
/* 154 */       }  if (server && cvs != null) {
/* 155 */         if (st.hasMoreTokens()) {
/* 156 */           this.serverVersion = st.nextToken() + cvs;
/*     */         }
/* 158 */         server = false;
/* 159 */         cvs = null; continue;
/* 160 */       }  if ("(client/server)".equals(currentToken) && cvs != null && cachedVersion != null && !client && !server) {
/*     */ 
/*     */         
/* 163 */         client = server = true;
/* 164 */         this.clientVersion = this.serverVersion = cachedVersion + cvs;
/* 165 */         cachedVersion = cvs = null;
/*     */       } 
/*     */     } 
/* 168 */     if (this.clientVersionProperty != null) {
/* 169 */       getProject().setNewProperty(this.clientVersionProperty, this.clientVersion);
/*     */     }
/* 171 */     if (this.serverVersionProperty != null)
/* 172 */       getProject().setNewProperty(this.serverVersionProperty, this.serverVersion); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/cvslib/CvsVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */