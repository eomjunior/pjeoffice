/*     */ package br.jus.cnj.pje.office.core.imp.shell;
/*     */ 
/*     */ import br.jus.cnj.pje.office.shell.ShellExtension;
/*     */ import br.jus.cnj.pje.office.task.imp.PjeSignMode;
/*     */ import br.jus.cnj.pje.office.task.imp.PjeTaskReader;
/*     */ import br.jus.cnj.pje.office.task.imp.StandardSignature;
/*     */ import com.github.signer4j.imp.SignatureAlgorithm;
/*     */ import com.github.signer4j.imp.SignatureType;
/*     */ import com.github.utils4j.imp.Directory;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ShellExtensionLauncher
/*     */ {
/*     */   public static List<File> mp4Split90(List<File> files) {
/*  48 */     return mp4SplitBySize(files, 90);
/*     */   }
/*     */   
/*     */   public static List<File> mp4SplitNSize(List<File> files) {
/*  52 */     return mp4SplitBySize(files, 0);
/*     */   }
/*     */   
/*     */   public static List<File> mp4SplitNTime(List<File> files) {
/*  56 */     return mp4SplitByDuration(files, 0);
/*     */   }
/*     */   
/*     */   public static List<File> signAtSameFolder(List<File> files) {
/*  60 */     return sign(files, "samefolder");
/*     */   }
/*     */   
/*     */   public static List<File> signAtNewFolder(List<File> files) {
/*  64 */     return sign(files, "newfolder");
/*     */   }
/*     */   
/*     */   public static List<File> signAtOtherFolder(List<File> files) {
/*  68 */     return sign(files, "selectfolder");
/*     */   }
/*     */   
/*     */   public static List<File> pdfSplit10(List<File> files) {
/*  72 */     return pdfSplitBySize(files, 10);
/*     */   }
/*     */   
/*     */   public static List<File> pdfSplitN(List<File> files) {
/*  76 */     return pdfSplitBySize(files, 0);
/*     */   }
/*     */   
/*     */   public static List<File> pdfSplitCount1(List<File> files) {
/*  80 */     return pdfSplitByCount(files, 1);
/*     */   }
/*     */   
/*     */   public static List<File> pdfSplitCountN(List<File> files) {
/*  84 */     return pdfSplitByCount(files, 0);
/*     */   }
/*     */   
/*     */   public static List<File> pdfSplitByP(List<File> files) {
/*  88 */     return pdfSplitByParity(files, true);
/*     */   }
/*     */   
/*     */   public static List<File> pdfSplitByI(List<File> files) {
/*  92 */     return pdfSplitByParity(files, false);
/*     */   }
/*     */   
/*     */   public static List<File> sign(List<File> files, String sendTo) {
/*  96 */     return signFile(files, sendTo);
/*     */   }
/*     */   
/*     */   public static List<File> pdfSplitByPages(List<File> pdfs) {
/* 100 */     List<File> taskFiles = new ArrayList<>(pdfs.size());
/* 101 */     pdfs.forEach(f -> ShellExtension.main(taskFiles::add, new String[] { PjeTaskReader.PDF_SPLIT_BY_PAGES.getId(), Directory.stringPath(f) }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     return taskFiles;
/*     */   }
/*     */   
/*     */   public static List<File> pdfJoin(List<File> pdfs) {
/* 112 */     List<File> taskFiles = new ArrayList<>(pdfs.size());
/* 113 */     pdfs.forEach(f -> ShellExtension.main(taskFiles::add, new String[] { PjeTaskReader.PDF_JOIN.getId(), Directory.stringPath(f) }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     return taskFiles;
/*     */   }
/*     */   
/*     */   public static List<File> mp4Slice(List<File> files) {
/* 124 */     List<File> taskFiles = new ArrayList<>(files.size());
/* 125 */     files.forEach(f -> ShellExtension.main(taskFiles::add, new String[] { PjeTaskReader.VIDEO_SPLIT_BY_SLICE.getId(), Directory.stringPath(f) }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     return taskFiles;
/*     */   }
/*     */   
/*     */   public static List<File> mp4Audio(List<File> files) {
/* 136 */     List<File> taskFiles = new ArrayList<>(files.size());
/* 137 */     files.forEach(f -> ShellExtension.main(taskFiles::add, new String[] { PjeTaskReader.VIDEO_EXTRACT_AUDIO.getId(), Directory.stringPath(f) }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     return taskFiles;
/*     */   }
/*     */   
/*     */   public static List<File> mp4Webm(List<File> files) {
/* 148 */     List<File> taskFiles = new ArrayList<>(files.size());
/* 149 */     files.forEach(f -> ShellExtension.main(taskFiles::add, new String[] { PjeTaskReader.VIDEO_CONVERT_WEBM.getId(), Directory.stringPath(f) }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 156 */     return taskFiles;
/*     */   }
/*     */   
/*     */   public static List<File> mp4Optimize(List<File> files) {
/* 160 */     List<File> taskFiles = new ArrayList<>(files.size());
/* 161 */     files.forEach(f -> ShellExtension.main(taskFiles::add, new String[] { PjeTaskReader.VIDEO_OPTIMIZE.getId(), Directory.stringPath(f) }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 168 */     return taskFiles;
/*     */   }
/*     */   
/*     */   private static List<File> signFile(List<File> files, String sendTo) {
/* 172 */     List<File> taskFiles = new ArrayList<>(files.size());
/* 173 */     files.forEach(f -> ShellExtension.main(taskFiles::add, new String[] { PjeTaskReader.CNJ_ASSINADOR.getId(), Directory.stringPath(f), sendTo, PjeSignMode.DEFINIDO.getKey(), StandardSignature.CADES.getKey(), SignatureType.ATTACHED.getKey(), SignatureAlgorithm.SHA256withRSA.getName() }));
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
/* 185 */     return taskFiles;
/*     */   }
/*     */   
/*     */   private static List<File> pdfSplitBySize(List<File> files, int size) {
/* 189 */     List<File> taskFiles = new ArrayList<>(files.size());
/* 190 */     files.forEach(f -> ShellExtension.main(taskFiles::add, new String[] { PjeTaskReader.PDF_SPLIT_BY_SIZE.getId(), Directory.stringPath(f), Integer.toString(size) }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     return taskFiles;
/*     */   }
/*     */   
/*     */   private static List<File> pdfSplitByCount(List<File> files, int count) {
/* 202 */     List<File> taskFiles = new ArrayList<>(files.size());
/* 203 */     files.forEach(f -> ShellExtension.main(taskFiles::add, new String[] { PjeTaskReader.PDF_SPLIT_BY_COUNT.getId(), Directory.stringPath(f), Integer.toString(count) }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     return taskFiles;
/*     */   }
/*     */   
/*     */   private static List<File> pdfSplitByParity(List<File> files, boolean parity) {
/* 215 */     List<File> taskFiles = new ArrayList<>(files.size());
/* 216 */     files.forEach(f -> ShellExtension.main(taskFiles::add, new String[] { PjeTaskReader.PDF_SPLIT_BY_PARITY.getId(), Directory.stringPath(f), Boolean.toString(parity) }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 224 */     return taskFiles;
/*     */   }
/*     */   
/*     */   private static List<File> mp4SplitBySize(List<File> files, int size) {
/* 228 */     List<File> taskFiles = new ArrayList<>(files.size());
/* 229 */     files.forEach(f -> ShellExtension.main(taskFiles::add, new String[] { PjeTaskReader.VIDEO_SPLIT_BY_SIZE.getId(), Directory.stringPath(f), Integer.toString(size) }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 237 */     return taskFiles;
/*     */   }
/*     */   
/*     */   private static List<File> mp4SplitByDuration(List<File> files, int duration) {
/* 241 */     List<File> taskFiles = new ArrayList<>(files.size());
/* 242 */     files.forEach(f -> ShellExtension.main(taskFiles::add, new String[] { PjeTaskReader.VIDEO_SPLIT_BY_DURATION.getId(), Directory.stringPath(f), Integer.toString(duration) }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 250 */     return taskFiles;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/shell/ShellExtensionLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */