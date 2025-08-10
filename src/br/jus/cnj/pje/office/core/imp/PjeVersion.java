/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.shell.ShellExtension;
/*    */ import com.github.utils4j.IConstants;
/*    */ import com.github.utils4j.imp.Jvms;
/*    */ import com.github.utils4j.imp.SemanticVersion;
/*    */ import java.io.IOException;
/*    */ import org.json.JSONObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class PjeVersion
/*    */   extends SemanticVersion
/*    */ {
/* 50 */   public static final PjeVersion CURRENT = from(2, 5, 16);
/*    */   
/*    */   private PjeVersion(int major, int minor, int patch) {
/* 53 */     super(major, minor, patch);
/*    */   }
/*    */   
/*    */   public final byte[] aboutBytes(String model) {
/* 57 */     return about(model).getBytes(IConstants.DEFAULT_CHARSET);
/*    */   }
/*    */   
/*    */   public final String about(String model) {
/* 61 */     JSONObject json = new JSONObject();
/* 62 */     json.put("versao", toString());
/* 63 */     json.put("success", true);
/* 64 */     json.put("modelo", model);
/* 65 */     json.put("home.config", ShellExtension.HOME_CONFIG_FOLDER);
/* 66 */     json.put("os.native.arch", Jvms.isNative64bits() ? "64 bits" : "32 bits");
/* 67 */     System.getProperties().forEach((k, v) -> json.put(k.toString(), v.toString()));
/* 68 */     return json.toString();
/*    */   }
/*    */   
/*    */   public final String getEdition() {
/* 72 */     return "Pro";
/*    */   }
/*    */   
/*    */   public static PjeVersion from(int major, int minor, int patch) {
/* 76 */     return new PjeVersion(major, minor, patch);
/*    */   }
/*    */   
/*    */   public static PjeVersion from(String version) throws IOException {
/* 80 */     SemanticVersion v = SemanticVersion.from(version);
/* 81 */     return from(v.major(), v.minor(), v.patch());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */