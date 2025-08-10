/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeServerAccess;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PjeServerAccess
/*     */   implements IPjeServerAccess
/*     */ {
/*     */   private String id;
/*     */   private String app;
/*     */   private String server;
/*     */   private String code;
/*     */   private boolean autorized;
/*     */   
/*     */   public static IPjeServerAccess fromString(List<String> members) {
/*  40 */     return new PjeServerAccess(members
/*  41 */         .get(0), members
/*  42 */         .get(1), members
/*  43 */         .get(2), 
/*  44 */         Strings.toBoolean(members.get(3), false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PjeServerAccess(String app, String server, String code) {
/*  55 */     this(app, server, code, false);
/*     */   }
/*     */   
/*     */   public PjeServerAccess(String app, String server, String code, boolean autorized) {
/*  59 */     this.app = (String)Args.requireNonNull(app, "app is null");
/*  60 */     this.server = (String)Args.requireNonNull(server, "server is null");
/*  61 */     this.code = (String)Args.requireNonNull(code, "code is null");
/*  62 */     this.autorized = autorized;
/*  63 */     this.id = app.toLowerCase() + "|" + server.toLowerCase() + "|" + code.toLowerCase();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getId() {
/*  68 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getApp() {
/*  73 */     return this.app;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getServer() {
/*  78 */     return this.server;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getCode() {
/*  83 */     return this.code;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAutorized() {
/*  88 */     return this.autorized;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String toString() {
/*  93 */     return String.format("%s;%s;%s;%s", new Object[] { this.app, this.server, this.code, Boolean.valueOf(this.autorized) });
/*     */   }
/*     */ 
/*     */   
/*     */   public final IPjeServerAccess clone(boolean allowed) {
/*  98 */     return new PjeServerAccess(this.app, this.server, this.code, allowed);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 103 */     int prime = 31;
/* 104 */     int result = 1;
/* 105 */     result = 31 * result + ((this.id == null) ? 0 : this.id.hashCode());
/* 106 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/* 111 */     if (this == obj)
/* 112 */       return true; 
/* 113 */     if (obj == null)
/* 114 */       return false; 
/* 115 */     if (getClass() != obj.getClass())
/* 116 */       return false; 
/* 117 */     PjeServerAccess other = (PjeServerAccess)obj;
/* 118 */     if (this.id == null) {
/* 119 */       if (other.id != null)
/* 120 */         return false; 
/* 121 */     } else if (!this.id.equals(other.id)) {
/* 122 */       return false;
/* 123 */     }  return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeServerAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */