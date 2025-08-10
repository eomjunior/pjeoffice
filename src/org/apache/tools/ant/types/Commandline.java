/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Commandline
/*     */   implements Cloneable
/*     */ {
/*  56 */   private static final boolean IS_WIN_9X = Os.isFamily("win9x");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   private List<Argument> arguments = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private String executable = null;
/*     */   
/*  68 */   protected static final String DISCLAIMER = String.format("%nThe ' characters around the executable and arguments are%nnot part of the command.%n", new Object[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Commandline(String toProcess) {
/*  78 */     String[] tmp = translateCommandline(toProcess);
/*  79 */     if (tmp != null && tmp.length > 0) {
/*  80 */       setExecutable(tmp[0]);
/*  81 */       for (int i = 1; i < tmp.length; i++) {
/*  82 */         createArgument().setValue(tmp[i]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Argument
/*     */     extends ProjectComponent
/*     */   {
/*     */     private String[] parts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     private String prefix = "";
/* 102 */     private String suffix = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setValue(String value) {
/* 110 */       this.parts = new String[] { value };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setLine(String line) {
/* 119 */       if (line == null) {
/*     */         return;
/*     */       }
/* 122 */       this.parts = Commandline.translateCommandline(line);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setPath(Path value) {
/* 133 */       this.parts = new String[] { value.toString() };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setPathref(Reference value) {
/* 144 */       Path p = new Path(getProject());
/* 145 */       p.setRefid(value);
/* 146 */       this.parts = new String[] { p.toString() };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setFile(File value) {
/* 156 */       this.parts = new String[] { value.getAbsolutePath() };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setPrefix(String prefix) {
/* 167 */       this.prefix = (prefix != null) ? prefix : "";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setSuffix(String suffix) {
/* 178 */       this.suffix = (suffix != null) ? suffix : "";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void copyFrom(Argument other) {
/* 189 */       this.parts = other.parts;
/* 190 */       this.prefix = other.prefix;
/* 191 */       this.suffix = other.suffix;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getParts() {
/* 199 */       if (this.parts == null || this.parts.length == 0 || (this.prefix.isEmpty() && this.suffix.isEmpty())) {
/* 200 */         return this.parts;
/*     */       }
/* 202 */       String[] fullParts = new String[this.parts.length];
/* 203 */       for (int i = 0; i < fullParts.length; i++) {
/* 204 */         fullParts[i] = this.prefix + this.parts[i] + this.suffix;
/*     */       }
/* 206 */       return fullParts;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class Marker
/*     */   {
/*     */     private int position;
/*     */ 
/*     */ 
/*     */     
/* 219 */     private int realPos = -1;
/* 220 */     private String prefix = "";
/* 221 */     private String suffix = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Marker(int position) {
/* 228 */       this.position = position;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getPosition() {
/* 239 */       if (this.realPos == -1) {
/* 240 */         this
/*     */           
/* 242 */           .realPos = ((Commandline.this.executable == null) ? 0 : 1) + (int)Commandline.this.arguments.stream().limit(this.position).map(Commandline.Argument::getParts).flatMap(Stream::of).count();
/*     */       }
/* 244 */       return this.realPos;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setPrefix(String prefix) {
/* 254 */       this.prefix = (prefix != null) ? prefix : "";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getPrefix() {
/* 264 */       return this.prefix;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setSuffix(String suffix) {
/* 274 */       this.suffix = (suffix != null) ? suffix : "";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getSuffix() {
/* 284 */       return this.suffix;
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
/*     */   public Argument createArgument() {
/* 300 */     return createArgument(false);
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
/*     */   public Argument createArgument(boolean insertAtStart) {
/* 314 */     Argument argument = new Argument();
/* 315 */     if (insertAtStart) {
/* 316 */       this.arguments.add(0, argument);
/*     */     } else {
/* 318 */       this.arguments.add(argument);
/*     */     } 
/* 320 */     return argument;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExecutable(String executable) {
/* 329 */     setExecutable(executable, true);
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
/*     */   public void setExecutable(String executable, boolean translateFileSeparator) {
/* 342 */     if (executable == null || executable.isEmpty()) {
/*     */       return;
/*     */     }
/* 345 */     this
/*     */       
/* 347 */       .executable = translateFileSeparator ? executable.replace('/', File.separatorChar).replace('\\', File.separatorChar) : executable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExecutable() {
/* 355 */     return this.executable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addArguments(String[] line) {
/* 363 */     for (String argument : line) {
/* 364 */       createArgument().setValue(argument);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getCommandline() {
/* 373 */     List<String> commands = new LinkedList<>();
/* 374 */     addCommandToList(commands.listIterator());
/* 375 */     return commands.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCommandToList(ListIterator<String> list) {
/* 384 */     if (this.executable != null) {
/* 385 */       list.add(this.executable);
/*     */     }
/* 387 */     addArgumentsToList(list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getArguments() {
/* 396 */     List<String> result = new ArrayList<>(this.arguments.size() * 2);
/* 397 */     addArgumentsToList(result.listIterator());
/* 398 */     return result.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addArgumentsToList(ListIterator<String> list) {
/* 407 */     for (Argument arg : this.arguments) {
/* 408 */       String[] s = arg.getParts();
/* 409 */       if (s != null) {
/* 410 */         for (String value : s) {
/* 411 */           list.add(value);
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 423 */     return toString(getCommandline());
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
/*     */   public static String quoteArgument(String argument) {
/* 438 */     if (argument.contains("\"")) {
/* 439 */       if (argument.contains("'")) {
/* 440 */         throw new BuildException("Can't handle single and double quotes in same argument");
/*     */       }
/*     */       
/* 443 */       return '\'' + argument + '\'';
/*     */     } 
/* 445 */     if (argument.contains("'") || argument.contains(" ") || (IS_WIN_9X && argument
/*     */       
/* 447 */       .contains(";"))) {
/* 448 */       return '"' + argument + '"';
/*     */     }
/* 450 */     return argument;
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
/*     */   public static String toString(String[] line) {
/* 462 */     if (line == null || line.length == 0) {
/* 463 */       return "";
/*     */     }
/*     */     
/* 466 */     StringBuilder result = new StringBuilder();
/* 467 */     for (String argument : line) {
/* 468 */       if (result.length() > 0) {
/* 469 */         result.append(' ');
/*     */       }
/* 471 */       result.append(quoteArgument(argument));
/*     */     } 
/* 473 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] translateCommandline(String toProcess) {
/* 483 */     if (toProcess == null || toProcess.isEmpty())
/*     */     {
/* 485 */       return new String[0];
/*     */     }
/*     */ 
/*     */     
/* 489 */     int normal = 0;
/* 490 */     int inQuote = 1;
/* 491 */     int inDoubleQuote = 2;
/* 492 */     int state = 0;
/* 493 */     StringTokenizer tok = new StringTokenizer(toProcess, "\"' ", true);
/* 494 */     ArrayList<String> result = new ArrayList<>();
/* 495 */     StringBuilder current = new StringBuilder();
/* 496 */     boolean lastTokenHasBeenQuoted = false;
/*     */     
/* 498 */     while (tok.hasMoreTokens()) {
/* 499 */       String nextTok = tok.nextToken();
/* 500 */       switch (state) {
/*     */         case 1:
/* 502 */           if ("'".equals(nextTok)) {
/* 503 */             lastTokenHasBeenQuoted = true;
/* 504 */             state = 0; continue;
/*     */           } 
/* 506 */           current.append(nextTok);
/*     */           continue;
/*     */         
/*     */         case 2:
/* 510 */           if ("\"".equals(nextTok)) {
/* 511 */             lastTokenHasBeenQuoted = true;
/* 512 */             state = 0; continue;
/*     */           } 
/* 514 */           current.append(nextTok);
/*     */           continue;
/*     */       } 
/*     */       
/* 518 */       if ("'".equals(nextTok)) {
/* 519 */         state = 1;
/* 520 */       } else if ("\"".equals(nextTok)) {
/* 521 */         state = 2;
/* 522 */       } else if (" ".equals(nextTok)) {
/* 523 */         if (lastTokenHasBeenQuoted || current.length() > 0) {
/* 524 */           result.add(current.toString());
/* 525 */           current.setLength(0);
/*     */         } 
/*     */       } else {
/* 528 */         current.append(nextTok);
/*     */       } 
/* 530 */       lastTokenHasBeenQuoted = false;
/*     */     } 
/*     */ 
/*     */     
/* 534 */     if (lastTokenHasBeenQuoted || current.length() > 0) {
/* 535 */       result.add(current.toString());
/*     */     }
/* 537 */     if (state == 1 || state == 2) {
/* 538 */       throw new BuildException("unbalanced quotes in " + toProcess);
/*     */     }
/* 540 */     return result.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 549 */     return (getCommandline()).length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 559 */       Commandline c = (Commandline)super.clone();
/* 560 */       c.arguments = new ArrayList<>(this.arguments);
/* 561 */       return c;
/* 562 */     } catch (CloneNotSupportedException e) {
/* 563 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 571 */     this.executable = null;
/* 572 */     this.arguments.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearArgs() {
/* 580 */     this.arguments.clear();
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
/*     */   public Marker createMarker() {
/* 592 */     return new Marker(this.arguments.size());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String describeCommand() {
/* 602 */     return describeCommand(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String describeArguments() {
/* 612 */     return describeArguments(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String describeCommand(Commandline line) {
/* 623 */     return describeCommand(line.getCommandline());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String describeArguments(Commandline line) {
/* 634 */     return describeArguments(line.getArguments());
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
/*     */   public static String describeCommand(String[] args) {
/* 648 */     if (args == null || args.length == 0) {
/* 649 */       return "";
/*     */     }
/* 651 */     StringBuilder buf = (new StringBuilder("Executing '")).append(args[0]).append("'");
/* 652 */     if (args.length > 1) {
/* 653 */       buf.append(" with ");
/* 654 */       buf.append(describeArguments(args, 1));
/*     */     } else {
/* 656 */       buf.append(DISCLAIMER);
/*     */     } 
/* 658 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String describeArguments(String[] args) {
/* 669 */     return describeArguments(args, 0);
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
/*     */   protected static String describeArguments(String[] args, int offset) {
/* 683 */     if (args == null || args.length <= offset) {
/* 684 */       return "";
/*     */     }
/* 686 */     StringBuilder buf = new StringBuilder();
/* 687 */     buf.append(String.format("argument%s:%n", new Object[] { (args.length > offset) ? "s" : "" }));
/* 688 */     for (int i = offset; i < args.length; i++) {
/* 689 */       buf.append(String.format("'%s'%n", new Object[] { args[i] }));
/*     */     } 
/* 691 */     buf.append(DISCLAIMER);
/* 692 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Argument> iterator() {
/* 701 */     return this.arguments.iterator();
/*     */   }
/*     */   
/*     */   public Commandline() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/Commandline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */