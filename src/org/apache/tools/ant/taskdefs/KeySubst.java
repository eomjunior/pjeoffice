/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.StringUtils;
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
/*     */ @Deprecated
/*     */ public class KeySubst
/*     */   extends Task
/*     */ {
/*  48 */   private File source = null;
/*  49 */   private File dest = null;
/*  50 */   private String sep = "*";
/*  51 */   private Hashtable<String, String> replacements = new Hashtable<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  58 */     log("!! KeySubst is deprecated. Use Filter + Copy instead. !!");
/*  59 */     log("Performing Substitutions");
/*  60 */     if (this.source == null || this.dest == null) {
/*  61 */       log("Source and destinations must not be null");
/*     */       return;
/*     */     } 
/*  64 */     BufferedReader br = null;
/*  65 */     BufferedWriter bw = null;
/*     */     try {
/*  67 */       br = new BufferedReader(new FileReader(this.source));
/*  68 */       this.dest.delete();
/*  69 */       bw = new BufferedWriter(new FileWriter(this.dest));
/*     */       
/*  71 */       String line = null;
/*  72 */       String newline = null;
/*  73 */       line = br.readLine();
/*  74 */       while (line != null) {
/*  75 */         if (!line.isEmpty()) {
/*  76 */           newline = replace(line, this.replacements);
/*  77 */           bw.write(newline);
/*     */         } 
/*  79 */         bw.newLine();
/*  80 */         line = br.readLine();
/*     */       } 
/*  82 */       bw.flush();
/*  83 */     } catch (IOException ioe) {
/*  84 */       log(StringUtils.getStackTrace(ioe), 0);
/*     */     } finally {
/*  86 */       FileUtils.close(bw);
/*  87 */       FileUtils.close(br);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File s) {
/*  96 */     this.source = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDest(File dest) {
/* 104 */     this.dest = dest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSep(String sep) {
/* 113 */     this.sep = sep;
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
/*     */ 
/*     */   
/*     */   public void setKeys(String keys) {
/* 129 */     if (keys != null && !keys.isEmpty()) {
/* 130 */       StringTokenizer tok = new StringTokenizer(keys, this.sep, false);
/*     */       
/* 132 */       while (tok.hasMoreTokens()) {
/* 133 */         String token = tok.nextToken().trim();
/* 134 */         StringTokenizer itok = new StringTokenizer(token, "=", false);
/*     */ 
/*     */         
/* 137 */         String name = itok.nextToken();
/* 138 */         String value = itok.nextToken();
/* 139 */         this.replacements.put(name, value);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/* 151 */       Hashtable<String, String> hash = new Hashtable<>();
/* 152 */       hash.put("VERSION", "1.0.3");
/* 153 */       hash.put("b", "ffff");
/* 154 */       System.out.println(replace("$f ${VERSION} f ${b} jj $", hash));
/*     */     }
/* 156 */     catch (Exception e) {
/* 157 */       e.printStackTrace();
/*     */     } 
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
/*     */   public static String replace(String origString, Hashtable<String, String> keys) throws BuildException {
/* 170 */     StringBuilder finalString = new StringBuilder();
/* 171 */     int index = 0;
/* 172 */     int i = 0;
/* 173 */     String key = null;
/*     */     
/* 175 */     while ((index = origString.indexOf("${", i)) > -1) {
/* 176 */       key = origString.substring(index + 2, origString.indexOf("}", index + 3));
/*     */       
/* 178 */       finalString.append(origString, i, index);
/* 179 */       if (keys.containsKey(key)) {
/* 180 */         finalString.append(keys.get(key));
/*     */       } else {
/* 182 */         finalString.append("${");
/* 183 */         finalString.append(key);
/* 184 */         finalString.append("}");
/*     */       } 
/* 186 */       i = index + 3 + key.length();
/*     */     } 
/*     */     
/* 189 */     finalString.append(origString.substring(i));
/* 190 */     return finalString.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/KeySubst.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */