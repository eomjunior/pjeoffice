/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.util.JavaEnvUtils;
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
/*     */ public class GenerateKey
/*     */   extends Task
/*     */ {
/*     */   protected String alias;
/*     */   protected String keystore;
/*     */   protected String storepass;
/*     */   protected String storetype;
/*     */   protected String keypass;
/*     */   protected String sigalg;
/*     */   protected String keyalg;
/*     */   protected String saname;
/*     */   protected String dname;
/*     */   protected DistinguishedName expandedDname;
/*     */   protected int keysize;
/*     */   protected int validity;
/*     */   protected boolean verbose;
/*     */   
/*     */   public static class DnameParam
/*     */   {
/*     */     private String name;
/*     */     private String value;
/*     */     
/*     */     public void setName(String name) {
/*  53 */       this.name = name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/*  61 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setValue(String value) {
/*  69 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getValue() {
/*  77 */       return this.value;
/*     */     }
/*     */     
/*     */     public boolean isComplete() {
/*  81 */       return (this.name != null && this.value != null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class DistinguishedName
/*     */   {
/*  89 */     private List<GenerateKey.DnameParam> params = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object createParam() {
/*  96 */       GenerateKey.DnameParam param = new GenerateKey.DnameParam();
/*  97 */       this.params.add(param);
/*  98 */       return param;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Enumeration<GenerateKey.DnameParam> getParams() {
/* 106 */       return Collections.enumeration(this.params);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 118 */       return this.params.stream().map(p -> encode(p.getName()) + "=" + encode(p.getValue()))
/* 119 */         .collect(Collectors.joining(", "));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String encode(String string) {
/* 130 */       return String.join("\\,", (CharSequence[])string.split(","));
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
/*     */   public DistinguishedName createDname() throws BuildException {
/* 167 */     if (null != this.expandedDname) {
/* 168 */       throw new BuildException("DName sub-element can only be specified once.");
/*     */     }
/* 170 */     if (null != this.dname) {
/* 171 */       throw new BuildException("It is not possible to specify dname  both as attribute and element.");
/*     */     }
/*     */     
/* 174 */     this.expandedDname = new DistinguishedName();
/* 175 */     return this.expandedDname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDname(String dname) {
/* 184 */     if (null != this.expandedDname) {
/* 185 */       throw new BuildException("It is not possible to specify dname  both as attribute and element.");
/*     */     }
/*     */     
/* 188 */     this.dname = dname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSaname(String saname) {
/* 198 */     this.saname = saname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlias(String alias) {
/* 207 */     this.alias = alias;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeystore(String keystore) {
/* 216 */     this.keystore = keystore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStorepass(String storepass) {
/* 225 */     this.storepass = storepass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStoretype(String storetype) {
/* 234 */     this.storetype = storetype;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeypass(String keypass) {
/* 243 */     this.keypass = keypass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSigalg(String sigalg) {
/* 252 */     this.sigalg = sigalg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeyalg(String keyalg) {
/* 260 */     this.keyalg = keyalg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeysize(String keysize) throws BuildException {
/*     */     try {
/* 272 */       this.keysize = Integer.parseInt(keysize);
/* 273 */     } catch (NumberFormatException nfe) {
/* 274 */       throw new BuildException("KeySize attribute should be a integer");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValidity(String validity) throws BuildException {
/*     */     try {
/* 286 */       this.validity = Integer.parseInt(validity);
/* 287 */     } catch (NumberFormatException nfe) {
/* 288 */       throw new BuildException("Validity attribute should be a integer");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean verbose) {
/* 297 */     this.verbose = verbose;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 307 */     if (null == this.alias) {
/* 308 */       throw new BuildException("alias attribute must be set");
/*     */     }
/*     */     
/* 311 */     if (null == this.storepass) {
/* 312 */       throw new BuildException("storepass attribute must be set");
/*     */     }
/*     */     
/* 315 */     if (null == this.dname && null == this.expandedDname) {
/* 316 */       throw new BuildException("dname must be set");
/*     */     }
/*     */     
/* 319 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 321 */     sb.append("-genkey ");
/*     */     
/* 323 */     if (this.verbose) {
/* 324 */       sb.append("-v ");
/*     */     }
/*     */     
/* 327 */     sb.append("-alias \"");
/* 328 */     sb.append(this.alias);
/* 329 */     sb.append("\" ");
/*     */     
/* 331 */     if (null != this.dname) {
/* 332 */       sb.append("-dname \"");
/* 333 */       sb.append(this.dname);
/* 334 */       sb.append("\" ");
/*     */     } 
/*     */     
/* 337 */     if (null != this.expandedDname) {
/* 338 */       sb.append("-dname \"");
/* 339 */       sb.append(this.expandedDname);
/* 340 */       sb.append("\" ");
/*     */     } 
/*     */     
/* 343 */     if (null != this.keystore) {
/* 344 */       sb.append("-keystore \"");
/* 345 */       sb.append(this.keystore);
/* 346 */       sb.append("\" ");
/*     */     } 
/*     */     
/* 349 */     if (null != this.storepass) {
/* 350 */       sb.append("-storepass \"");
/* 351 */       sb.append(this.storepass);
/* 352 */       sb.append("\" ");
/*     */     } 
/*     */     
/* 355 */     if (null != this.storetype) {
/* 356 */       sb.append("-storetype \"");
/* 357 */       sb.append(this.storetype);
/* 358 */       sb.append("\" ");
/*     */     } 
/*     */     
/* 361 */     sb.append("-keypass \"");
/* 362 */     if (null != this.keypass) {
/* 363 */       sb.append(this.keypass);
/*     */     } else {
/* 365 */       sb.append(this.storepass);
/*     */     } 
/* 367 */     sb.append("\" ");
/*     */     
/* 369 */     if (null != this.sigalg) {
/* 370 */       sb.append("-sigalg \"");
/* 371 */       sb.append(this.sigalg);
/* 372 */       sb.append("\" ");
/*     */     } 
/*     */     
/* 375 */     if (null != this.keyalg) {
/* 376 */       sb.append("-keyalg \"");
/* 377 */       sb.append(this.keyalg);
/* 378 */       sb.append("\" ");
/*     */     } 
/*     */     
/* 381 */     if (0 < this.keysize) {
/* 382 */       sb.append("-keysize \"");
/* 383 */       sb.append(this.keysize);
/* 384 */       sb.append("\" ");
/*     */     } 
/*     */     
/* 387 */     if (0 < this.validity) {
/* 388 */       sb.append("-validity \"");
/* 389 */       sb.append(this.validity);
/* 390 */       sb.append("\" ");
/*     */     } 
/*     */     
/* 393 */     if (null != this.saname) {
/* 394 */       sb.append("-ext ");
/* 395 */       sb.append("\"san=");
/* 396 */       sb.append(this.saname);
/* 397 */       sb.append("\" ");
/*     */     } 
/*     */     
/* 400 */     log("Generating Key for " + this.alias);
/* 401 */     ExecTask cmd = new ExecTask(this);
/* 402 */     cmd.setExecutable(JavaEnvUtils.getJdkExecutable("keytool"));
/* 403 */     Commandline.Argument arg = cmd.createArg();
/* 404 */     arg.setLine(sb.toString());
/* 405 */     cmd.setFailonerror(true);
/* 406 */     cmd.setTaskName(getTaskName());
/* 407 */     cmd.execute();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/GenerateKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */