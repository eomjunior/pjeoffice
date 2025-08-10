/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.util.FileUtils;
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
/*     */ public class TokenizedPath
/*     */ {
/*  38 */   public static final TokenizedPath EMPTY_PATH = new TokenizedPath("", new String[0]);
/*     */ 
/*     */ 
/*     */   
/*  42 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*  44 */   private static final boolean[] CS_SCAN_ONLY = new boolean[] { true };
/*     */   
/*  46 */   private static final boolean[] CS_THEN_NON_CS = new boolean[] { true, false };
/*     */ 
/*     */   
/*     */   private final String path;
/*     */ 
/*     */   
/*     */   private final String[] tokenizedPath;
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenizedPath(String path) {
/*  57 */     this(path, SelectorUtils.tokenizePathAsArray(path));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenizedPath(TokenizedPath parent, String child) {
/*  67 */     if (!parent.path.isEmpty() && parent.path
/*  68 */       .charAt(parent.path.length() - 1) != File.separatorChar) {
/*  69 */       parent.path += File.separatorChar + child;
/*     */     } else {
/*  71 */       parent.path += child;
/*     */     } 
/*  73 */     this.tokenizedPath = new String[parent.tokenizedPath.length + 1];
/*  74 */     System.arraycopy(parent.tokenizedPath, 0, this.tokenizedPath, 0, parent.tokenizedPath.length);
/*     */     
/*  76 */     this.tokenizedPath[parent.tokenizedPath.length] = child;
/*     */   }
/*     */ 
/*     */   
/*     */   TokenizedPath(String path, String[] tokens) {
/*  81 */     this.path = path;
/*  82 */     this.tokenizedPath = tokens;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  90 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int depth() {
/*  98 */     return this.tokenizedPath.length;
/*     */   }
/*     */ 
/*     */   
/*     */   String[] getTokens() {
/* 103 */     return this.tokenizedPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File findFile(File base, boolean cs) {
/* 115 */     String[] tokens = this.tokenizedPath;
/* 116 */     if (FileUtils.isAbsolutePath(this.path)) {
/* 117 */       if (base == null) {
/* 118 */         String[] s = FILE_UTILS.dissect(this.path);
/* 119 */         base = new File(s[0]);
/* 120 */         tokens = SelectorUtils.tokenizePathAsArray(s[1]);
/*     */       } else {
/* 122 */         File f = FILE_UTILS.normalize(this.path);
/* 123 */         String s = FILE_UTILS.removeLeadingPath(base, f);
/* 124 */         if (s.equals(f.getAbsolutePath()))
/*     */         {
/*     */           
/* 127 */           return null;
/*     */         }
/* 129 */         tokens = SelectorUtils.tokenizePathAsArray(s);
/*     */       } 
/*     */     }
/* 132 */     return findFile(base, tokens, cs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSymlink(File base) {
/* 142 */     for (String token : this.tokenizedPath) {
/*     */       Path pathToTraverse;
/* 144 */       if (base == null) {
/* 145 */         pathToTraverse = Paths.get(token, new String[0]);
/*     */       } else {
/* 147 */         pathToTraverse = Paths.get(base.toPath().toString(), new String[] { token });
/*     */       } 
/* 149 */       if (Files.isSymbolicLink(pathToTraverse)) {
/* 150 */         return true;
/*     */       }
/* 152 */       base = new File(base, token);
/*     */     } 
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 163 */     return (o instanceof TokenizedPath && this.path
/* 164 */       .equals(((TokenizedPath)o).path));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 169 */     return this.path.hashCode();
/*     */   }
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
/*     */   private static File findFile(File base, String[] pathElements, boolean cs) {
/* 183 */     for (String pathElement : pathElements) {
/* 184 */       if (!base.isDirectory()) {
/* 185 */         return null;
/*     */       }
/* 187 */       String[] files = base.list();
/* 188 */       if (files == null) {
/* 189 */         throw new BuildException("IO error scanning directory %s", new Object[] { base
/* 190 */               .getAbsolutePath() });
/*     */       }
/* 192 */       boolean found = false;
/* 193 */       boolean[] matchCase = cs ? CS_SCAN_ONLY : CS_THEN_NON_CS;
/* 194 */       for (int i = 0; !found && i < matchCase.length; i++) {
/* 195 */         for (int j = 0; !found && j < files.length; j++) {
/* 196 */           if (matchCase[i] ? files[j]
/* 197 */             .equals(pathElement) : files[j]
/* 198 */             .equalsIgnoreCase(pathElement)) {
/* 199 */             base = new File(base, files[j]);
/* 200 */             found = true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 204 */       if (!found) {
/* 205 */         return null;
/*     */       }
/*     */     } 
/* 208 */     return (pathElements.length == 0 && !base.isDirectory()) ? null : base;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenizedPattern toPattern() {
/* 218 */     return new TokenizedPattern(this.path, this.tokenizedPath);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/TokenizedPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */