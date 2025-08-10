/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.condition.Condition;
/*     */ import org.apache.tools.ant.types.Comparison;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.types.resources.Resources;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.PropertyOutputStream;
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
/*     */ public class Length
/*     */   extends Task
/*     */   implements Condition
/*     */ {
/*     */   private static final String ALL = "all";
/*     */   private static final String EACH = "each";
/*     */   private static final String STRING = "string";
/*     */   private static final String LENGTH_REQUIRED = "Use of the Length condition requires that the length attribute be set.";
/*     */   private String property;
/*     */   private String string;
/*     */   private Boolean trim;
/*  56 */   private String mode = "all";
/*  57 */   private Comparison when = Comparison.EQUAL;
/*     */ 
/*     */   
/*     */   private Long length;
/*     */   
/*     */   private Resources resources;
/*     */ 
/*     */   
/*     */   public synchronized void setProperty(String property) {
/*  66 */     this.property = property;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setResource(Resource resource) {
/*  74 */     add((ResourceCollection)resource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setFile(File file) {
/*  82 */     add((ResourceCollection)new FileResource(file));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void add(FileSet fs) {
/*  90 */     add((ResourceCollection)fs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void add(ResourceCollection c) {
/*  99 */     if (c == null) {
/*     */       return;
/*     */     }
/* 102 */     this.resources = (this.resources == null) ? new Resources() : this.resources;
/* 103 */     this.resources.add(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setLength(long ell) {
/* 111 */     this.length = Long.valueOf(ell);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setWhen(When w) {
/* 120 */     setWhen(w);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setWhen(Comparison c) {
/* 130 */     this.when = c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setMode(FileMode m) {
/* 138 */     this.mode = m.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setString(String string) {
/* 146 */     this.string = string;
/* 147 */     this.mode = "string";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setTrim(boolean trim) {
/* 155 */     this.trim = Boolean.valueOf(trim);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getTrim() {
/* 163 */     return Boolean.TRUE.equals(this.trim);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 171 */     validate();
/*     */ 
/*     */     
/* 174 */     OutputStream out = (this.property == null) ? (OutputStream)new LogOutputStream(this, 2) : (OutputStream)new PropertyOutputStream(getProject(), this.property);
/* 175 */     PrintStream ps = new PrintStream(out);
/*     */     
/* 177 */     switch (this.mode) {
/*     */       case "string":
/* 179 */         ps.print(getLength(this.string, getTrim()));
/* 180 */         ps.close();
/*     */         break;
/*     */       case "each":
/* 183 */         handleResources(new EachHandler(ps));
/*     */         break;
/*     */       case "all":
/* 186 */         handleResources(new AllHandler(ps));
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean eval() {
/*     */     Long ell;
/* 198 */     validate();
/* 199 */     if (this.length == null) {
/* 200 */       throw new BuildException("Use of the Length condition requires that the length attribute be set.");
/*     */     }
/*     */     
/* 203 */     if ("string".equals(this.mode)) {
/* 204 */       ell = Long.valueOf(getLength(this.string, getTrim()));
/*     */     } else {
/* 206 */       AccumHandler h = new AccumHandler();
/* 207 */       handleResources(h);
/* 208 */       ell = Long.valueOf(h.getAccum());
/*     */     } 
/* 210 */     return this.when.evaluate(ell.compareTo(this.length));
/*     */   }
/*     */   
/*     */   private void validate() {
/* 214 */     if (this.string != null) {
/* 215 */       if (this.resources != null) {
/* 216 */         throw new BuildException("the string length function is incompatible with the file/resource length function");
/*     */       }
/*     */       
/* 219 */       if (!"string".equals(this.mode)) {
/* 220 */         throw new BuildException("the mode attribute is for use with the file/resource length function");
/*     */       }
/*     */     }
/* 223 */     else if (this.resources != null) {
/* 224 */       if (!"each".equals(this.mode) && !"all".equals(this.mode)) {
/* 225 */         throw new BuildException("invalid mode setting for file/resource length function: \"" + this.mode + "\"");
/*     */       }
/*     */ 
/*     */       
/* 229 */       if (this.trim != null) {
/* 230 */         throw new BuildException("the trim attribute is for use with the string length function only");
/*     */       }
/*     */     } else {
/*     */       
/* 234 */       throw new BuildException("you must set either the string attribute or specify one or more files using the file attribute or nested resource collections");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleResources(Handler h) {
/* 240 */     for (Resource r : this.resources) {
/* 241 */       if (!r.isExists()) {
/* 242 */         log(r + " does not exist", 1);
/*     */       }
/* 244 */       if (r.isDirectory()) {
/* 245 */         log(r + " is a directory; length may not be meaningful", 1);
/*     */       }
/* 247 */       h.handle(r);
/*     */     } 
/* 249 */     h.complete();
/*     */   }
/*     */   
/*     */   private static long getLength(String s, boolean t) {
/* 253 */     return (t ? s.trim() : s).length();
/*     */   }
/*     */   
/*     */   public static class FileMode
/*     */     extends EnumeratedAttribute {
/* 258 */     static final String[] MODES = new String[] { "each", "all" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 266 */       return MODES;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class When
/*     */     extends Comparison {}
/*     */ 
/*     */   
/*     */   private abstract class Handler
/*     */   {
/*     */     private PrintStream ps;
/*     */ 
/*     */     
/*     */     Handler(PrintStream ps) {
/* 282 */       this.ps = ps;
/*     */     }
/*     */     
/*     */     protected PrintStream getPs() {
/* 286 */       return this.ps;
/*     */     }
/*     */     
/*     */     protected abstract void handle(Resource param1Resource);
/*     */     
/*     */     void complete() {
/* 292 */       FileUtils.close(this.ps);
/*     */     }
/*     */   }
/*     */   
/*     */   private class EachHandler extends Handler {
/*     */     EachHandler(PrintStream ps) {
/* 298 */       super(ps);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void handle(Resource r) {
/* 303 */       getPs().print(r.toString());
/* 304 */       getPs().print(" : ");
/*     */       
/* 306 */       long size = r.getSize();
/* 307 */       if (size == -1L) {
/* 308 */         getPs().println("unknown");
/*     */       } else {
/* 310 */         getPs().println(size);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class AccumHandler extends Handler {
/* 316 */     private long accum = 0L;
/*     */     
/*     */     AccumHandler() {
/* 319 */       super(null);
/*     */     }
/*     */     
/*     */     protected AccumHandler(PrintStream ps) {
/* 323 */       super(ps);
/*     */     }
/*     */     
/*     */     protected long getAccum() {
/* 327 */       return this.accum;
/*     */     }
/*     */ 
/*     */     
/*     */     protected synchronized void handle(Resource r) {
/* 332 */       long size = r.getSize();
/* 333 */       if (size == -1L) {
/* 334 */         Length.this.log("Size unknown for " + r.toString(), 1);
/*     */       } else {
/* 336 */         this.accum += size;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class AllHandler extends AccumHandler {
/*     */     AllHandler(PrintStream ps) {
/* 343 */       super(ps);
/*     */     }
/*     */ 
/*     */     
/*     */     void complete() {
/* 348 */       getPs().print(getAccum());
/* 349 */       super.complete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Length.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */