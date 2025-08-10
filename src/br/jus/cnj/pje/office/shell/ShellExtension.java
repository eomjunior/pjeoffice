/*     */ package br.jus.cnj.pje.office.shell;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Optional;
/*     */ import java.util.Properties;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import javax.swing.JOptionPane;
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
/*     */ public class ShellExtension
/*     */ {
/*     */   private static final String APP = "pjeoffice-pro";
/*     */   private static Path TEMP_WATCHING;
/*  52 */   private static final Path HOME = Paths.get(System.getProperty("user.home"), new String[0]);
/*     */   
/*  54 */   public static final Path HOME_CONFIG_FOLDER = HOME.resolve(".pjeoffice-pro");
/*     */   
/*  56 */   public static final Path HOME_CONFIG_FILE = HOME_CONFIG_FOLDER.resolve("pjeoffice-pro.config");
/*     */   
/*  58 */   public static final Path HOME_WATCHING = HOME_CONFIG_FOLDER.resolve("watching");
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String... args) {
/*  63 */     main(null, args);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(Consumer<File> consumer, String... args) {
/*  68 */     Optional<Task> otask = Task.from(Strings.at(args, 0));
/*  69 */     if (!otask.isPresent()) {
/*     */       return;
/*     */     }
/*     */     
/*  73 */     Path consumeWatching = HOME_WATCHING;
/*     */     
/*  75 */     if (consumer != null && 
/*  76 */       TEMP_WATCHING == null) {
/*     */       try {
/*  78 */         consumeWatching = TEMP_WATCHING = Files.createTempDirectory("consumer-watching", (FileAttribute<?>[])new FileAttribute[0]);
/*  79 */       } catch (IOException e) {
/*  80 */         consumer = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*  85 */     if (consumer == null) {
/*  86 */       consumeWatching = HOME_WATCHING;
/*  87 */       consumer = (f -> {
/*     */         
/*     */         });
/*  90 */     }  String filePath = Strings.at(args, 1);
/*     */     
/*  92 */     File input = new File(filePath);
/*  93 */     if (!input.exists()) {
/*  94 */       String newName = Strings.sanitize(filePath);
/*  95 */       File newFile = new File(newName);
/*  96 */       if (!newFile.exists()) {
/*  97 */         showMessage("Arquivo '" + input.getName() + "' formado por caracteres inválidos.\nTente renomeá-lo!");
/*     */         return;
/*     */       } 
/* 100 */       input = newFile;
/*     */     } 
/*     */     
/* 103 */     File lock = HOME_WATCHING.resolve(".lock").toFile();
/*     */     
/* 105 */     if (!lock.exists()) {
/*     */       
/* 107 */       File slock = HOME_WATCHING.resolve(".slock").toFile();
/*     */       
/* 109 */       if (!createLock(slock)) {
/*     */         
/* 111 */         if (!slock.exists()) {
/*     */           return;
/*     */         }
/* 114 */         boolean timeout = waitFor(slock::exists);
/*     */         
/* 116 */         if (timeout) {
/* 117 */           slock.delete();
/* 118 */           slock.deleteOnExit();
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */       } else {
/* 124 */         try (RandomAccessFile alock = new RandomAccessFile(slock, "r")) {
/*     */           
/* 126 */           Optional<Path> pjeOfficeHomeOpt = Optional.empty();
/* 127 */           String homeEnv = System.getenv("pjeoffice_home");
/*     */           
/* 129 */           if (homeEnv != null) {
/* 130 */             Path home = Paths.get(homeEnv, new String[0]);
/* 131 */             if (Files.exists(home, new java.nio.file.LinkOption[0])) {
/* 132 */               pjeOfficeHomeOpt = Optional.of(home);
/*     */             }
/*     */           } 
/*     */           
/* 136 */           if (!pjeOfficeHomeOpt.isPresent()) {
/* 137 */             Path home = Paths.get(ShellExtension.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().getParent();
/* 138 */             if (!Files.exists(home, new java.nio.file.LinkOption[0])) {
/* 139 */               throw new Exception("PJeOffice Offline");
/*     */             }
/* 141 */             pjeOfficeHomeOpt = Optional.of(home);
/*     */           } 
/*     */           
/* 144 */           Path pjeOfficeHome = pjeOfficeHomeOpt.get();
/*     */           
/* 146 */           (new ProcessBuilder(new String[] { pjeOfficeHome
/* 147 */                 .resolve("pjeoffice-pro.exe")
/* 148 */                 .toFile()
/* 149 */                 .getCanonicalPath()
/*     */               
/* 151 */               })).directory(pjeOfficeHome.toFile())
/* 152 */             .start();
/*     */           
/* 154 */           boolean timeout = waitFor(() -> Boolean.valueOf(!lock.exists()));
/*     */           
/* 156 */           if (timeout) {
/* 157 */             throw new Exception("PJeOffice Offline");
/*     */           }
/*     */         }
/* 160 */         catch (Exception e) {
/* 161 */           showMessage("PJeOffice não está aberto/em execução. ");
/*     */           return;
/*     */         } finally {
/* 164 */           slock.delete();
/* 165 */           slock.deleteOnExit();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 170 */     String inputPath = "", outputPath = inputPath;
/*     */     
/*     */     try {
/* 173 */       inputPath = input.getCanonicalPath();
/*     */       
/* 175 */       Task task = otask.get();
/* 176 */       Properties p = new Properties();
/* 177 */       p.put("task", task.getId());
/* 178 */       p.put("arquivo", inputPath);
/* 179 */       task.echo(args, p);
/*     */       
/* 181 */       File output = consumeWatching.resolve(task.getId() + "." + input.getName() + ".task").toFile();
/* 182 */       outputPath = output.getAbsolutePath();
/*     */       
/* 184 */       try (FileOutputStream out = new FileOutputStream(output)) {
/* 185 */         p.store(out, (String)null);
/*     */       } 
/*     */       
/* 188 */       consumer.accept(output);
/*     */     }
/* 190 */     catch (Exception e) {
/* 191 */       String detail = "<html><body><h3>Não foi possível realizar a operação!</h3>";
/*     */       
/* 193 */       if (isInvalidPathLength(inputPath, outputPath)) {
/* 194 */         detail = detail + "<p>O caminho/nome do arquivo ultrapassa 256 caracteres.<br><br>Tente diminuir o tamanho do nome do arquivo ou hierarquia de pastas!</p>";
/*     */       }
/*     */       
/* 197 */       detail = detail + "</body></html>";
/* 198 */       showMessage(detail);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isInvalidPathLength(String path) {
/* 203 */     return (path == null || path.length() == 0 || path.length() >= 255);
/*     */   }
/*     */   
/*     */   private static boolean isInvalidPathLength(String inputPath, String outputPath) {
/* 207 */     return (isInvalidPathLength(inputPath) || isInvalidPathLength(outputPath));
/*     */   }
/*     */   
/*     */   private static void showMessage(String message) {
/* 211 */     JOptionPane.showMessageDialog(null, message);
/*     */   }
/*     */   
/*     */   private static boolean createLock(File file) {
/*     */     try {
/* 216 */       file.getParentFile().mkdirs();
/* 217 */       return file.createNewFile();
/* 218 */     } catch (IOException e) {
/* 219 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean waitFor(Supplier<Boolean> waitCondition) {
/* 224 */     long start = System.currentTimeMillis();
/* 225 */     boolean timeout = false;
/*     */     
/*     */     do {
/*     */       try {
/* 229 */         Thread.sleep(100L);
/* 230 */       } catch (InterruptedException e) {
/*     */       
/*     */       } finally {
/* 233 */         timeout = (System.currentTimeMillis() - start >= 15000L);
/*     */       } 
/* 235 */     } while (((Boolean)waitCondition.get()).booleanValue() && !timeout);
/*     */     
/* 237 */     return timeout;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/shell/ShellExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */