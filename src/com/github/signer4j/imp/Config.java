/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IConfigPersister;
/*     */ import com.github.signer4j.IDriverSetup;
/*     */ import com.github.signer4j.IFilePath;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Environment;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.awt.Image;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Config
/*     */ {
/*     */   private static final String SIGNER4J_A3AUTO = "signer4j_a3auto";
/*     */   private static IConfigPersister config;
/*     */   private static Image icon;
/*     */   
/*     */   protected static void setup(Image image, IConfigPersister conf) {
/*  51 */     icon = (Image)Args.requireNonNull(image, "image is null");
/*  52 */     config = (IConfigPersister)Args.requireNonNull(conf, "conf is null");
/*  53 */     setupA3();
/*     */   }
/*     */   
/*     */   protected static IConfigPersister config() {
/*  57 */     return (config != null) ? config : (config = new ConfigPersister(new SignerConfig()));
/*     */   }
/*     */   
/*     */   public static Image getIcon() {
/*  61 */     return icon;
/*     */   }
/*     */   
/*     */   public static Optional<String> defaultCertificate() {
/*  65 */     return config().defaultCertificate();
/*     */   }
/*     */   
/*     */   public static Optional<String> defaultDevice() {
/*  69 */     return config().defaultDevice();
/*     */   }
/*     */   
/*     */   public static Optional<String> defaultAlias() {
/*  73 */     return config().defaultAlias();
/*     */   }
/*     */   
/*     */   public static Repository defaultRepository() {
/*  77 */     return config().defaultRepository();
/*     */   }
/*     */   
/*     */   public static void saveA1Paths(IFilePath... path) {
/*  81 */     config().saveA1Paths(path);
/*     */   }
/*     */   
/*     */   public static void saveA3Paths(IFilePath... path) {
/*  85 */     config().saveA3Paths(path);
/*     */   }
/*     */   
/*     */   public static void loadA1Paths(Consumer<IFilePath> add) {
/*  89 */     config().loadA1Paths(add);
/*     */   }
/*     */   
/*     */   public static void loadA3Paths(Consumer<IFilePath> add) {
/*  93 */     config().loadA3Paths(add);
/*     */   }
/*     */   
/*     */   public static void save(String defaultAlias) {
/*  97 */     config().save(defaultAlias);
/*     */   }
/*     */   
/*     */   public static void saveRepository(Repository repository) {
/* 101 */     config().saveRepository(repository);
/*     */   }
/*     */   
/*     */   public static void reset() {
/* 105 */     config().reset();
/*     */   }
/*     */   
/*     */   private static void setupA3() {
/* 109 */     Environment.valueFrom("signer4j_a3auto", Boolean.toString(true))
/* 110 */       .map(Strings::isTrue)
/* 111 */       .ifPresent(auto -> {
/*     */           if (!auto.booleanValue())
/*     */             return; 
/*     */           List<IFilePath> libs = new ArrayList<>();
/*     */           loadA3Paths(libs::add);
/*     */           if (!libs.isEmpty())
/*     */             return; 
/*     */           (new SmartLookupStrategy()).lookup(());
/*     */           saveA3Paths(libs.<IFilePath>toArray((IFilePath[])new FilePath[libs.size()]));
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/Config.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */