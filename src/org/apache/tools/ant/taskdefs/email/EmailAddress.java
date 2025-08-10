/*     */ package org.apache.tools.ant.taskdefs.email;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EmailAddress
/*     */ {
/*     */   private String name;
/*     */   private String address;
/*     */   
/*     */   public EmailAddress() {}
/*     */   
/*     */   public EmailAddress(String email) {
/*  50 */     int minLen = 9;
/*  51 */     int len = email.length();
/*     */ 
/*     */     
/*  54 */     if (len > 9 && (
/*  55 */       email.charAt(0) == '<' || email.charAt(1) == '<') && (email
/*  56 */       .charAt(len - 1) == '>' || email.charAt(len - 2) == '>')) {
/*  57 */       this.address = trim(email, true);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  62 */     int paramDepth = 0;
/*  63 */     int start = 0;
/*  64 */     int end = 0;
/*  65 */     int nStart = 0;
/*  66 */     int nEnd = 0;
/*     */     
/*  68 */     for (int i = 0; i < len; i++) {
/*  69 */       char c = email.charAt(i);
/*  70 */       if (c == '(') {
/*  71 */         paramDepth++;
/*  72 */         if (start == 0) {
/*  73 */           end = i;
/*  74 */           nStart = i + 1;
/*     */         } 
/*  76 */       } else if (c == ')') {
/*  77 */         paramDepth--;
/*  78 */         if (end == 0) {
/*  79 */           start = i + 1;
/*  80 */           nEnd = i;
/*     */         } 
/*  82 */       } else if (paramDepth == 0 && c == '<') {
/*  83 */         if (start == 0) {
/*  84 */           nEnd = i;
/*     */         }
/*  86 */         start = i + 1;
/*  87 */       } else if (paramDepth == 0 && c == '>') {
/*  88 */         end = i;
/*  89 */         if (end != len - 1) {
/*  90 */           nStart = i + 1;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  96 */     if (end == 0) {
/*  97 */       end = len;
/*     */     }
/*     */     
/* 100 */     if (nEnd == 0) {
/* 101 */       nEnd = len;
/*     */     }
/*     */ 
/*     */     
/* 105 */     this.address = trim(email.substring(start, end), true);
/* 106 */     this.name = trim(email.substring(nStart, nEnd), false);
/*     */ 
/*     */ 
/*     */     
/* 110 */     if (this.name.length() + this.address.length() > len) {
/* 111 */       this.name = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String trim(String t, boolean trimAngleBrackets) {
/* 120 */     int start = 0;
/* 121 */     int end = t.length();
/*     */     
/*     */     while (true) {
/* 124 */       boolean trim = false;
/* 125 */       if (t.charAt(end - 1) == ')' || (t
/* 126 */         .charAt(end - 1) == '>' && trimAngleBrackets) || (t
/* 127 */         .charAt(end - 1) == '"' && t.charAt(end - 2) != '\\') || t
/* 128 */         .charAt(end - 1) <= ' ') {
/* 129 */         trim = true;
/* 130 */         end--;
/*     */       } 
/* 132 */       if (t.charAt(start) == '(' || (t
/* 133 */         .charAt(start) == '<' && trimAngleBrackets) || t
/* 134 */         .charAt(start) == '"' || t
/* 135 */         .charAt(start) <= ' ') {
/* 136 */         trim = true;
/* 137 */         start++;
/*     */       } 
/* 139 */       if (!trim) {
/* 140 */         return t.substring(start, end);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 149 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAddress(String address) {
/* 158 */     this.address = address;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 168 */     if (this.name == null) {
/* 169 */       return this.address;
/*     */     }
/* 171 */     return this.name + " <" + this.address + ">";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAddress() {
/* 180 */     return this.address;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 189 */     return this.name;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/email/EmailAddress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */