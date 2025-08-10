/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IConfig;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
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
/*    */ public class SignerConfig
/*    */   implements IConfig
/*    */ {
/* 40 */   private static final Path HOME = Paths.get(System.getProperty("user.home"), new String[0]);
/*    */   
/*    */   private final File config;
/*    */   
/*    */   public SignerConfig() {
/* 45 */     this("signer4j");
/*    */   }
/*    */   
/*    */   public SignerConfig(String configName) {
/* 49 */     this(HOME.resolve("." + configName).resolve(configName + ".config").toFile());
/*    */   }
/*    */   
/*    */   public SignerConfig(File configFile) {
/* 53 */     Args.requireNonNull(configFile, "config is null");
/* 54 */     configFile.getParentFile().mkdirs();
/* 55 */     this.config = configFile;
/*    */   }
/*    */ 
/*    */   
/*    */   public void reset() {
/* 60 */     this.config.delete();
/*    */   }
/*    */ 
/*    */   
/*    */   public File getConfigFile() throws IOException {
/* 65 */     if (!this.config.exists() && !this.config.createNewFile()) {
/* 66 */       throw new IOException("Não foi possível criar o arquivo de configuração em: " + this.config.getCanonicalPath());
/*    */     }
/* 68 */     return this.config;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/SignerConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */