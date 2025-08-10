/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ExitStatusException;
/*     */ import org.apache.tools.ant.Location;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.TaskContainer;
/*     */ import org.apache.tools.ant.property.LocalProperties;
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
/*     */ public class Parallel
/*     */   extends Task
/*     */   implements TaskContainer
/*     */ {
/*     */   private static final int NUMBER_TRIES = 100;
/*     */   
/*     */   public static class TaskList
/*     */     implements TaskContainer
/*     */   {
/*  57 */     private List<Task> tasks = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addTask(Task nestedTask) {
/*  67 */       this.tasks.add(nestedTask);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  72 */   private Vector<Task> nestedTasks = new Vector<>();
/*     */ 
/*     */   
/*  75 */   private final Object semaphore = new Object();
/*     */ 
/*     */   
/*  78 */   private int numThreads = 0;
/*     */ 
/*     */   
/*  81 */   private int numThreadsPerProcessor = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   private long timeout;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean stillRunning;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean timedOut;
/*     */ 
/*     */   
/*     */   private boolean failOnAny;
/*     */ 
/*     */   
/*     */   private TaskList daemonTasks;
/*     */ 
/*     */   
/*     */   private StringBuffer exceptionMessage;
/*     */ 
/*     */   
/* 105 */   private int numExceptions = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   private Throwable firstException;
/*     */ 
/*     */ 
/*     */   
/*     */   private Location firstLocation;
/*     */ 
/*     */   
/*     */   private Integer firstExitStatus;
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDaemons(TaskList daemonTasks) {
/* 121 */     if (this.daemonTasks != null) {
/* 122 */       throw new BuildException("Only one daemon group is supported");
/*     */     }
/* 124 */     this.daemonTasks = daemonTasks;
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
/*     */   public void setPollInterval(int pollInterval) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnAny(boolean failOnAny) {
/* 145 */     this.failOnAny = failOnAny;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTask(Task nestedTask) {
/* 154 */     this.nestedTasks.addElement(nestedTask);
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
/*     */   public void setThreadsPerProcessor(int numThreadsPerProcessor) {
/* 167 */     this.numThreadsPerProcessor = numThreadsPerProcessor;
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
/*     */   public void setThreadCount(int numThreads) {
/* 182 */     this.numThreads = numThreads;
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
/*     */   public void setTimeout(long timeout) {
/* 195 */     this.timeout = timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 205 */     updateThreadCounts();
/* 206 */     if (this.numThreads == 0) {
/* 207 */       this.numThreads = this.nestedTasks.size();
/*     */     }
/* 209 */     spinThreads();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateThreadCounts() {
/* 216 */     if (this.numThreadsPerProcessor != 0) {
/* 217 */       this.numThreads = Runtime.getRuntime().availableProcessors() * this.numThreadsPerProcessor;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void processExceptions(TaskRunnable[] runnables) {
/* 223 */     if (runnables == null) {
/*     */       return;
/*     */     }
/* 226 */     for (TaskRunnable runnable : runnables) {
/* 227 */       Throwable t = runnable.getException();
/* 228 */       if (t != null) {
/* 229 */         this.numExceptions++;
/* 230 */         if (this.firstException == null) {
/* 231 */           this.firstException = t;
/*     */         }
/* 233 */         if (t instanceof BuildException && this.firstLocation == Location.UNKNOWN_LOCATION)
/*     */         {
/* 235 */           this.firstLocation = ((BuildException)t).getLocation();
/*     */         }
/* 237 */         if (t instanceof ExitStatusException && this.firstExitStatus == null) {
/*     */           
/* 239 */           ExitStatusException ex = (ExitStatusException)t;
/* 240 */           this.firstExitStatus = Integer.valueOf(ex.getStatus());
/*     */ 
/*     */           
/* 243 */           this.firstLocation = ex.getLocation();
/*     */         } 
/* 245 */         this.exceptionMessage.append(System.lineSeparator());
/* 246 */         this.exceptionMessage.append(t.getMessage());
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void spinThreads() throws BuildException {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: iconst_1
/*     */     //   2: putfield stillRunning : Z
/*     */     //   5: aload_0
/*     */     //   6: iconst_0
/*     */     //   7: putfield timedOut : Z
/*     */     //   10: iconst_0
/*     */     //   11: istore_1
/*     */     //   12: aload_0
/*     */     //   13: getfield nestedTasks : Ljava/util/Vector;
/*     */     //   16: invokevirtual stream : ()Ljava/util/stream/Stream;
/*     */     //   19: aload_0
/*     */     //   20: <illegal opcode> apply : (Lorg/apache/tools/ant/taskdefs/Parallel;)Ljava/util/function/Function;
/*     */     //   25: invokeinterface map : (Ljava/util/function/Function;)Ljava/util/stream/Stream;
/*     */     //   30: <illegal opcode> apply : ()Ljava/util/function/IntFunction;
/*     */     //   35: invokeinterface toArray : (Ljava/util/function/IntFunction;)[Ljava/lang/Object;
/*     */     //   40: checkcast [Lorg/apache/tools/ant/taskdefs/Parallel$TaskRunnable;
/*     */     //   43: astore_2
/*     */     //   44: aload_0
/*     */     //   45: getfield nestedTasks : Ljava/util/Vector;
/*     */     //   48: invokevirtual size : ()I
/*     */     //   51: istore_3
/*     */     //   52: iload_3
/*     */     //   53: aload_0
/*     */     //   54: getfield numThreads : I
/*     */     //   57: if_icmpge -> 64
/*     */     //   60: iload_3
/*     */     //   61: goto -> 68
/*     */     //   64: aload_0
/*     */     //   65: getfield numThreads : I
/*     */     //   68: istore #4
/*     */     //   70: iload #4
/*     */     //   72: anewarray org/apache/tools/ant/taskdefs/Parallel$TaskRunnable
/*     */     //   75: astore #5
/*     */     //   77: new java/lang/ThreadGroup
/*     */     //   80: dup
/*     */     //   81: ldc 'parallel'
/*     */     //   83: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   86: astore #6
/*     */     //   88: aconst_null
/*     */     //   89: astore #7
/*     */     //   91: aload_0
/*     */     //   92: getfield daemonTasks : Lorg/apache/tools/ant/taskdefs/Parallel$TaskList;
/*     */     //   95: ifnull -> 130
/*     */     //   98: aload_0
/*     */     //   99: getfield daemonTasks : Lorg/apache/tools/ant/taskdefs/Parallel$TaskList;
/*     */     //   102: invokestatic access$000 : (Lorg/apache/tools/ant/taskdefs/Parallel$TaskList;)Ljava/util/List;
/*     */     //   105: invokeinterface isEmpty : ()Z
/*     */     //   110: ifne -> 130
/*     */     //   113: aload_0
/*     */     //   114: getfield daemonTasks : Lorg/apache/tools/ant/taskdefs/Parallel$TaskList;
/*     */     //   117: invokestatic access$000 : (Lorg/apache/tools/ant/taskdefs/Parallel$TaskList;)Ljava/util/List;
/*     */     //   120: invokeinterface size : ()I
/*     */     //   125: anewarray org/apache/tools/ant/taskdefs/Parallel$TaskRunnable
/*     */     //   128: astore #7
/*     */     //   130: aload_0
/*     */     //   131: getfield semaphore : Ljava/lang/Object;
/*     */     //   134: dup
/*     */     //   135: astore #8
/*     */     //   137: monitorenter
/*     */     //   138: aload #8
/*     */     //   140: monitorexit
/*     */     //   141: goto -> 152
/*     */     //   144: astore #9
/*     */     //   146: aload #8
/*     */     //   148: monitorexit
/*     */     //   149: aload #9
/*     */     //   151: athrow
/*     */     //   152: aload_0
/*     */     //   153: getfield semaphore : Ljava/lang/Object;
/*     */     //   156: dup
/*     */     //   157: astore #8
/*     */     //   159: monitorenter
/*     */     //   160: aload #7
/*     */     //   162: ifnull -> 239
/*     */     //   165: iconst_0
/*     */     //   166: istore #9
/*     */     //   168: iload #9
/*     */     //   170: aload #7
/*     */     //   172: arraylength
/*     */     //   173: if_icmpge -> 239
/*     */     //   176: aload #7
/*     */     //   178: iload #9
/*     */     //   180: new org/apache/tools/ant/taskdefs/Parallel$TaskRunnable
/*     */     //   183: dup
/*     */     //   184: aload_0
/*     */     //   185: aload_0
/*     */     //   186: getfield daemonTasks : Lorg/apache/tools/ant/taskdefs/Parallel$TaskList;
/*     */     //   189: invokestatic access$000 : (Lorg/apache/tools/ant/taskdefs/Parallel$TaskList;)Ljava/util/List;
/*     */     //   192: iload #9
/*     */     //   194: invokeinterface get : (I)Ljava/lang/Object;
/*     */     //   199: checkcast org/apache/tools/ant/Task
/*     */     //   202: invokespecial <init> : (Lorg/apache/tools/ant/taskdefs/Parallel;Lorg/apache/tools/ant/Task;)V
/*     */     //   205: aastore
/*     */     //   206: new java/lang/Thread
/*     */     //   209: dup
/*     */     //   210: aload #6
/*     */     //   212: aload #7
/*     */     //   214: iload #9
/*     */     //   216: aaload
/*     */     //   217: invokespecial <init> : (Ljava/lang/ThreadGroup;Ljava/lang/Runnable;)V
/*     */     //   220: astore #10
/*     */     //   222: aload #10
/*     */     //   224: iconst_1
/*     */     //   225: invokevirtual setDaemon : (Z)V
/*     */     //   228: aload #10
/*     */     //   230: invokevirtual start : ()V
/*     */     //   233: iinc #9, 1
/*     */     //   236: goto -> 168
/*     */     //   239: iconst_0
/*     */     //   240: istore #9
/*     */     //   242: iconst_0
/*     */     //   243: istore #10
/*     */     //   245: iload #10
/*     */     //   247: iload #4
/*     */     //   249: if_icmpge -> 291
/*     */     //   252: aload #5
/*     */     //   254: iload #10
/*     */     //   256: aload_2
/*     */     //   257: iload #9
/*     */     //   259: iinc #9, 1
/*     */     //   262: aaload
/*     */     //   263: aastore
/*     */     //   264: new java/lang/Thread
/*     */     //   267: dup
/*     */     //   268: aload #6
/*     */     //   270: aload #5
/*     */     //   272: iload #10
/*     */     //   274: aaload
/*     */     //   275: invokespecial <init> : (Ljava/lang/ThreadGroup;Ljava/lang/Runnable;)V
/*     */     //   278: astore #11
/*     */     //   280: aload #11
/*     */     //   282: invokevirtual start : ()V
/*     */     //   285: iinc #10, 1
/*     */     //   288: goto -> 245
/*     */     //   291: aload_0
/*     */     //   292: getfield timeout : J
/*     */     //   295: lconst_0
/*     */     //   296: lcmp
/*     */     //   297: ifeq -> 315
/*     */     //   300: new org/apache/tools/ant/taskdefs/Parallel$1
/*     */     //   303: dup
/*     */     //   304: aload_0
/*     */     //   305: invokespecial <init> : (Lorg/apache/tools/ant/taskdefs/Parallel;)V
/*     */     //   308: astore #10
/*     */     //   310: aload #10
/*     */     //   312: invokevirtual start : ()V
/*     */     //   315: iload #9
/*     */     //   317: iload_3
/*     */     //   318: if_icmpge -> 409
/*     */     //   321: aload_0
/*     */     //   322: getfield stillRunning : Z
/*     */     //   325: ifeq -> 409
/*     */     //   328: iconst_0
/*     */     //   329: istore #10
/*     */     //   331: iload #10
/*     */     //   333: iload #4
/*     */     //   335: if_icmpge -> 399
/*     */     //   338: aload #5
/*     */     //   340: iload #10
/*     */     //   342: aaload
/*     */     //   343: ifnull -> 357
/*     */     //   346: aload #5
/*     */     //   348: iload #10
/*     */     //   350: aaload
/*     */     //   351: invokevirtual isFinished : ()Z
/*     */     //   354: ifeq -> 393
/*     */     //   357: aload #5
/*     */     //   359: iload #10
/*     */     //   361: aload_2
/*     */     //   362: iload #9
/*     */     //   364: iinc #9, 1
/*     */     //   367: aaload
/*     */     //   368: aastore
/*     */     //   369: new java/lang/Thread
/*     */     //   372: dup
/*     */     //   373: aload #6
/*     */     //   375: aload #5
/*     */     //   377: iload #10
/*     */     //   379: aaload
/*     */     //   380: invokespecial <init> : (Ljava/lang/ThreadGroup;Ljava/lang/Runnable;)V
/*     */     //   383: astore #11
/*     */     //   385: aload #11
/*     */     //   387: invokevirtual start : ()V
/*     */     //   390: goto -> 315
/*     */     //   393: iinc #10, 1
/*     */     //   396: goto -> 331
/*     */     //   399: aload_0
/*     */     //   400: getfield semaphore : Ljava/lang/Object;
/*     */     //   403: invokevirtual wait : ()V
/*     */     //   406: goto -> 315
/*     */     //   409: aload_0
/*     */     //   410: getfield stillRunning : Z
/*     */     //   413: ifeq -> 469
/*     */     //   416: iconst_0
/*     */     //   417: istore #10
/*     */     //   419: iload #10
/*     */     //   421: iload #4
/*     */     //   423: if_icmpge -> 461
/*     */     //   426: aload #5
/*     */     //   428: iload #10
/*     */     //   430: aaload
/*     */     //   431: ifnull -> 455
/*     */     //   434: aload #5
/*     */     //   436: iload #10
/*     */     //   438: aaload
/*     */     //   439: invokevirtual isFinished : ()Z
/*     */     //   442: ifne -> 455
/*     */     //   445: aload_0
/*     */     //   446: getfield semaphore : Ljava/lang/Object;
/*     */     //   449: invokevirtual wait : ()V
/*     */     //   452: goto -> 409
/*     */     //   455: iinc #10, 1
/*     */     //   458: goto -> 419
/*     */     //   461: aload_0
/*     */     //   462: iconst_0
/*     */     //   463: putfield stillRunning : Z
/*     */     //   466: goto -> 409
/*     */     //   469: goto -> 476
/*     */     //   472: astore #10
/*     */     //   474: iconst_1
/*     */     //   475: istore_1
/*     */     //   476: aload_0
/*     */     //   477: getfield timedOut : Z
/*     */     //   480: ifne -> 496
/*     */     //   483: aload_0
/*     */     //   484: getfield failOnAny : Z
/*     */     //   487: ifne -> 496
/*     */     //   490: aload_0
/*     */     //   491: aload #5
/*     */     //   493: invokespecial killAll : ([Lorg/apache/tools/ant/taskdefs/Parallel$TaskRunnable;)V
/*     */     //   496: aload #8
/*     */     //   498: monitorexit
/*     */     //   499: goto -> 510
/*     */     //   502: astore #12
/*     */     //   504: aload #8
/*     */     //   506: monitorexit
/*     */     //   507: aload #12
/*     */     //   509: athrow
/*     */     //   510: iload_1
/*     */     //   511: ifeq -> 524
/*     */     //   514: new org/apache/tools/ant/BuildException
/*     */     //   517: dup
/*     */     //   518: ldc 'Parallel execution interrupted.'
/*     */     //   520: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   523: athrow
/*     */     //   524: aload_0
/*     */     //   525: getfield timedOut : Z
/*     */     //   528: ifeq -> 541
/*     */     //   531: new org/apache/tools/ant/BuildException
/*     */     //   534: dup
/*     */     //   535: ldc 'Parallel execution timed out'
/*     */     //   537: invokespecial <init> : (Ljava/lang/String;)V
/*     */     //   540: athrow
/*     */     //   541: aload_0
/*     */     //   542: new java/lang/StringBuffer
/*     */     //   545: dup
/*     */     //   546: invokespecial <init> : ()V
/*     */     //   549: putfield exceptionMessage : Ljava/lang/StringBuffer;
/*     */     //   552: aload_0
/*     */     //   553: iconst_0
/*     */     //   554: putfield numExceptions : I
/*     */     //   557: aload_0
/*     */     //   558: aconst_null
/*     */     //   559: putfield firstException : Ljava/lang/Throwable;
/*     */     //   562: aload_0
/*     */     //   563: aconst_null
/*     */     //   564: putfield firstExitStatus : Ljava/lang/Integer;
/*     */     //   567: aload_0
/*     */     //   568: getstatic org/apache/tools/ant/Location.UNKNOWN_LOCATION : Lorg/apache/tools/ant/Location;
/*     */     //   571: putfield firstLocation : Lorg/apache/tools/ant/Location;
/*     */     //   574: aload_0
/*     */     //   575: aload #7
/*     */     //   577: invokespecial processExceptions : ([Lorg/apache/tools/ant/taskdefs/Parallel$TaskRunnable;)V
/*     */     //   580: aload_0
/*     */     //   581: aload_2
/*     */     //   582: invokespecial processExceptions : ([Lorg/apache/tools/ant/taskdefs/Parallel$TaskRunnable;)V
/*     */     //   585: aload_0
/*     */     //   586: getfield numExceptions : I
/*     */     //   589: iconst_1
/*     */     //   590: if_icmpne -> 623
/*     */     //   593: aload_0
/*     */     //   594: getfield firstException : Ljava/lang/Throwable;
/*     */     //   597: instanceof org/apache/tools/ant/BuildException
/*     */     //   600: ifeq -> 611
/*     */     //   603: aload_0
/*     */     //   604: getfield firstException : Ljava/lang/Throwable;
/*     */     //   607: checkcast org/apache/tools/ant/BuildException
/*     */     //   610: athrow
/*     */     //   611: new org/apache/tools/ant/BuildException
/*     */     //   614: dup
/*     */     //   615: aload_0
/*     */     //   616: getfield firstException : Ljava/lang/Throwable;
/*     */     //   619: invokespecial <init> : (Ljava/lang/Throwable;)V
/*     */     //   622: athrow
/*     */     //   623: aload_0
/*     */     //   624: getfield numExceptions : I
/*     */     //   627: iconst_1
/*     */     //   628: if_icmple -> 683
/*     */     //   631: aload_0
/*     */     //   632: getfield firstExitStatus : Ljava/lang/Integer;
/*     */     //   635: ifnonnull -> 657
/*     */     //   638: new org/apache/tools/ant/BuildException
/*     */     //   641: dup
/*     */     //   642: aload_0
/*     */     //   643: getfield exceptionMessage : Ljava/lang/StringBuffer;
/*     */     //   646: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   649: aload_0
/*     */     //   650: getfield firstLocation : Lorg/apache/tools/ant/Location;
/*     */     //   653: invokespecial <init> : (Ljava/lang/String;Lorg/apache/tools/ant/Location;)V
/*     */     //   656: athrow
/*     */     //   657: new org/apache/tools/ant/ExitStatusException
/*     */     //   660: dup
/*     */     //   661: aload_0
/*     */     //   662: getfield exceptionMessage : Ljava/lang/StringBuffer;
/*     */     //   665: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   668: aload_0
/*     */     //   669: getfield firstExitStatus : Ljava/lang/Integer;
/*     */     //   672: invokevirtual intValue : ()I
/*     */     //   675: aload_0
/*     */     //   676: getfield firstLocation : Lorg/apache/tools/ant/Location;
/*     */     //   679: invokespecial <init> : (Ljava/lang/String;ILorg/apache/tools/ant/Location;)V
/*     */     //   682: athrow
/*     */     //   683: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #257	-> 0
/*     */     //   #258	-> 5
/*     */     //   #259	-> 10
/*     */     //   #261	-> 12
/*     */     //   #262	-> 35
/*     */     //   #264	-> 44
/*     */     //   #265	-> 52
/*     */     //   #267	-> 70
/*     */     //   #268	-> 77
/*     */     //   #270	-> 88
/*     */     //   #271	-> 91
/*     */     //   #272	-> 113
/*     */     //   #275	-> 130
/*     */     //   #281	-> 138
/*     */     //   #283	-> 152
/*     */     //   #285	-> 160
/*     */     //   #286	-> 165
/*     */     //   #287	-> 176
/*     */     //   #288	-> 206
/*     */     //   #289	-> 222
/*     */     //   #290	-> 228
/*     */     //   #286	-> 233
/*     */     //   #296	-> 239
/*     */     //   #297	-> 242
/*     */     //   #298	-> 252
/*     */     //   #299	-> 264
/*     */     //   #300	-> 280
/*     */     //   #297	-> 285
/*     */     //   #303	-> 291
/*     */     //   #305	-> 300
/*     */     //   #326	-> 310
/*     */     //   #331	-> 315
/*     */     //   #332	-> 328
/*     */     //   #333	-> 338
/*     */     //   #334	-> 357
/*     */     //   #335	-> 369
/*     */     //   #336	-> 385
/*     */     //   #339	-> 390
/*     */     //   #332	-> 393
/*     */     //   #345	-> 399
/*     */     //   #349	-> 409
/*     */     //   #350	-> 416
/*     */     //   #351	-> 426
/*     */     //   #355	-> 445
/*     */     //   #356	-> 452
/*     */     //   #350	-> 455
/*     */     //   #359	-> 461
/*     */     //   #363	-> 469
/*     */     //   #361	-> 472
/*     */     //   #362	-> 474
/*     */     //   #365	-> 476
/*     */     //   #367	-> 490
/*     */     //   #369	-> 496
/*     */     //   #371	-> 510
/*     */     //   #372	-> 514
/*     */     //   #374	-> 524
/*     */     //   #375	-> 531
/*     */     //   #379	-> 541
/*     */     //   #380	-> 552
/*     */     //   #381	-> 557
/*     */     //   #382	-> 562
/*     */     //   #383	-> 567
/*     */     //   #384	-> 574
/*     */     //   #385	-> 580
/*     */     //   #387	-> 585
/*     */     //   #388	-> 593
/*     */     //   #389	-> 603
/*     */     //   #391	-> 611
/*     */     //   #393	-> 623
/*     */     //   #394	-> 631
/*     */     //   #395	-> 638
/*     */     //   #398	-> 657
/*     */     //   #399	-> 672
/*     */     //   #401	-> 683
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   222	11	10	daemonThread	Ljava/lang/Thread;
/*     */     //   168	71	9	i	I
/*     */     //   280	5	11	thread	Ljava/lang/Thread;
/*     */     //   245	46	10	i	I
/*     */     //   310	5	10	timeoutThread	Ljava/lang/Thread;
/*     */     //   385	8	11	thread	Ljava/lang/Thread;
/*     */     //   331	68	10	i	I
/*     */     //   419	42	10	i	I
/*     */     //   474	2	10	ie	Ljava/lang/InterruptedException;
/*     */     //   242	254	9	threadNumber	I
/*     */     //   0	684	0	this	Lorg/apache/tools/ant/taskdefs/Parallel;
/*     */     //   12	672	1	interrupted	Z
/*     */     //   44	640	2	runnables	[Lorg/apache/tools/ant/taskdefs/Parallel$TaskRunnable;
/*     */     //   52	632	3	numTasks	I
/*     */     //   70	614	4	maxRunning	I
/*     */     //   77	607	5	running	[Lorg/apache/tools/ant/taskdefs/Parallel$TaskRunnable;
/*     */     //   88	596	6	group	Ljava/lang/ThreadGroup;
/*     */     //   91	593	7	daemons	[Lorg/apache/tools/ant/taskdefs/Parallel$TaskRunnable;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   138	141	144	finally
/*     */     //   144	149	144	finally
/*     */     //   160	499	502	finally
/*     */     //   315	469	472	java/lang/InterruptedException
/*     */     //   502	507	502	finally
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void killAll(TaskRunnable[] running) {
/*     */     boolean oneAlive;
/* 410 */     int tries = 0;
/*     */     do {
/* 412 */       oneAlive = false;
/* 413 */       for (TaskRunnable runnable : running) {
/* 414 */         if (runnable != null && !runnable.isFinished()) {
/* 415 */           runnable.interrupt();
/* 416 */           Thread.yield();
/* 417 */           oneAlive = true;
/*     */         } 
/*     */       } 
/* 420 */       if (!oneAlive)
/* 421 */         continue;  tries++;
/* 422 */       Thread.yield();
/*     */     }
/* 424 */     while (oneAlive && tries < 100);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class TaskRunnable
/*     */     implements Runnable
/*     */   {
/*     */     private Throwable exception;
/*     */     
/*     */     private Task task;
/*     */     
/*     */     private boolean finished;
/*     */     
/*     */     private volatile Thread thread;
/*     */ 
/*     */     
/*     */     TaskRunnable(Task task) {
/* 442 */       this.task = task;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 452 */         LocalProperties.get(Parallel.this.getProject()).copy();
/* 453 */         this.thread = Thread.currentThread();
/* 454 */         this.task.perform();
/* 455 */       } catch (Throwable t) {
/* 456 */         this.exception = t;
/* 457 */         if (Parallel.this.failOnAny) {
/* 458 */           Parallel.this.stillRunning = false;
/*     */         }
/*     */       } finally {
/* 461 */         synchronized (Parallel.this.semaphore) {
/* 462 */           this.finished = true;
/* 463 */           Parallel.this.semaphore.notifyAll();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Throwable getException() {
/* 473 */       return this.exception;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isFinished() {
/* 481 */       return this.finished;
/*     */     }
/*     */     
/*     */     void interrupt() {
/* 485 */       this.thread.interrupt();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Parallel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */