/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IConfig;
/*     */ import com.github.signer4j.IConfigPersister;
/*     */ import com.github.signer4j.IFilePath;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Dates;
/*     */ import com.github.utils4j.imp.Jvms;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.function.Predicates;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Properties;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigPersister
/*     */   implements IConfigPersister
/*     */ {
/*  58 */   protected static final Logger LOGGER = LoggerFactory.getLogger(ConfigPersister.class);
/*     */   
/*     */   protected static final char LIST_DELIMITER = '|';
/*     */   
/*     */   private static final String CERTIFICATE_A1_LIST = "list.a1";
/*     */   
/*     */   private static final String CERTIFICATE_A3_LIST = "list.a3";
/*     */   
/*     */   private static final String DEFAULT_CERTIFICATE = "default.certificate";
/*     */   
/*     */   private static final String DEFAULT_REPOSITORY = "default.repository";
/*     */   
/*     */   private final IConfig config;
/*     */   
/*     */   public ConfigPersister() {
/*  73 */     this(new SignerConfig());
/*     */   }
/*     */   
/*     */   public ConfigPersister(IConfig config) {
/*  77 */     this.config = (IConfig)Args.requireNonNull(config, "config is null");
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/*  82 */     this.config.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void loadA1Paths(Consumer<IFilePath> add) {
/*  87 */     load(add, "list.a1", Predicates.all());
/*     */   }
/*     */ 
/*     */   
/*     */   public final void loadA3Paths(Consumer<IFilePath> add) {
/*  92 */     load(add, "list.a3", Predicates.all());
/*     */   }
/*     */ 
/*     */   
/*     */   public final void saveA3Paths(IFilePath... path) {
/*  97 */     put(p -> "", "list.a3", (Object[])path);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void save(String device) {
/* 102 */     put(p -> "", "default.certificate", (Object[])Strings.toArray(new String[] { device }));
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveRepository(Repository repository) {
/* 107 */     if (!Jvms.isWindows() || repository == null)
/* 108 */       repository = Repository.NATIVE; 
/* 109 */     put(p -> "", "default.repository", (Object[])Strings.toArray(new String[] { repository.getName() }));
/*     */   }
/*     */ 
/*     */   
/*     */   public final void saveA1Paths(IFilePath... path) {
/* 114 */     put(p -> "", "list.a1", (Object[])path);
/*     */   }
/*     */   
/*     */   private void load(Consumer<IFilePath> add, String param, Predicate<File> filter) {
/* 118 */     Properties properties = new Properties();
/* 119 */     if (!open(properties))
/*     */       return; 
/* 121 */     List<String> pathList = Strings.split(properties.getProperty(param, ""), '|');
/* 122 */     for (String path : pathList) {
/* 123 */       File p = Paths.get(path, new String[0]).toFile();
/* 124 */       if (p.exists() && p.isFile() && filter.test(p)) {
/* 125 */         add.accept(new FilePath(p.toPath()));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> defaultAlias() {
/* 132 */     Properties properties = new Properties();
/* 133 */     if (!open(properties))
/* 134 */       return Optional.empty(); 
/* 135 */     return Strings.optional(properties.getProperty("default.certificate"));
/*     */   }
/*     */ 
/*     */   
/*     */   public Repository defaultRepository() {
/* 140 */     if (!Jvms.isWindows())
/* 141 */       return Repository.NATIVE; 
/* 142 */     Properties properties = new Properties();
/* 143 */     if (!open(properties))
/* 144 */       return Repository.NATIVE; 
/* 145 */     return Repository.from(properties.getProperty("default.repository", Repository.NATIVE.getName()));
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> defaultDevice() {
/* 150 */     Properties properties = new Properties();
/* 151 */     if (!open(properties))
/* 152 */       return Optional.empty(); 
/* 153 */     return get(properties, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> defaultCertificate() {
/* 158 */     Properties properties = new Properties();
/* 159 */     if (!open(properties))
/* 160 */       return Optional.empty(); 
/* 161 */     return get(properties, 1);
/*     */   }
/*     */   
/*     */   protected final boolean open(Properties properties) {
/* 165 */     try (FileInputStream input = new FileInputStream(this.config.getConfigFile())) {
/* 166 */       properties.load(input);
/* 167 */     } catch (IOException e) {
/* 168 */       LOGGER.warn("Não foi possível ler os arquivos de configuração", e);
/* 169 */       return false;
/*     */     } 
/* 171 */     return true;
/*     */   }
/*     */   
/*     */   protected final void put(Function<Properties, String> start, String paramName, Object[] access) {
/* 175 */     Properties properties = new Properties();
/* 176 */     if (!open(properties))
/*     */       return; 
/* 178 */     String output = start.apply(properties);
/* 179 */     for (Object sa : access) {
/* 180 */       if (sa != null)
/*     */       {
/* 182 */         output = output + (output.isEmpty() ? "" : (String)Character.valueOf('|')) + sa.toString(); } 
/*     */     } 
/* 184 */     toDisk(properties, paramName, output);
/*     */   }
/*     */   
/*     */   protected final void remove(Object access, String paramName) {
/* 188 */     Properties properties = new Properties();
/* 189 */     if (!open(properties))
/*     */       return; 
/* 191 */     String output = Strings.trim(properties.getProperty(paramName, ""));
/* 192 */     List<String> servers = Strings.split(output, '|');
/* 193 */     String accessText = access.toString();
/* 194 */     servers.removeIf(s -> accessText.equalsIgnoreCase(s));
/* 195 */     output = Strings.merge(servers, '|');
/* 196 */     toDisk(properties, paramName, output);
/*     */   }
/*     */   
/*     */   private void toDisk(Properties properties, String paramName, String output) {
/* 200 */     properties.setProperty(paramName, output);
/* 201 */     try (FileOutputStream out = new FileOutputStream(this.config.getConfigFile())) {
/* 202 */       properties.store(out, "Salvo em " + Dates.stringNow());
/* 203 */     } catch (IOException e) {
/* 204 */       LOGGER.warn("Não foi possível salvar o arquivo de configuração", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Optional<String> get(Properties properties, int index) {
/* 209 */     List<String> members = Strings.split(properties.getProperty("default.certificate", ""), ':');
/* 210 */     if (members.size() != 2)
/* 211 */       return Optional.empty(); 
/* 212 */     return Optional.of(members.get(index));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/ConfigPersister.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */