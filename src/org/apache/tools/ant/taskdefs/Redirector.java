/*      */ package org.apache.tools.ant.taskdefs;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PipedInputStream;
/*      */ import java.io.PipedOutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.util.Arrays;
/*      */ import java.util.Vector;
/*      */ import java.util.stream.Collectors;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.ProjectComponent;
/*      */ import org.apache.tools.ant.Task;
/*      */ import org.apache.tools.ant.filters.util.ChainReaderHelper;
/*      */ import org.apache.tools.ant.types.FilterChain;
/*      */ import org.apache.tools.ant.util.ConcatFileInputStream;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.KeepAliveOutputStream;
/*      */ import org.apache.tools.ant.util.LazyFileOutputStream;
/*      */ import org.apache.tools.ant.util.LeadPipeInputStream;
/*      */ import org.apache.tools.ant.util.LineOrientedOutputStreamRedirector;
/*      */ import org.apache.tools.ant.util.NullOutputStream;
/*      */ import org.apache.tools.ant.util.OutputStreamFunneler;
/*      */ import org.apache.tools.ant.util.ReaderInputStream;
/*      */ import org.apache.tools.ant.util.TeeOutputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Redirector
/*      */ {
/*      */   private static final int STREAMPUMPER_WAIT_INTERVAL = 1000;
/*   63 */   private static final String DEFAULT_ENCODING = System.getProperty("file.encoding");
/*      */   private File[] input;
/*      */   private File[] out;
/*      */   private File[] error;
/*      */   
/*      */   private class PropertyOutputStream extends ByteArrayOutputStream {
/*      */     private final String property;
/*      */     
/*      */     PropertyOutputStream(String property) {
/*   72 */       this.property = property;
/*      */     }
/*      */     private boolean closed = false;
/*      */     
/*      */     public void close() throws IOException {
/*   77 */       synchronized (Redirector.this.outMutex) {
/*   78 */         if (!this.closed && (!Redirector.this.appendOut || !Redirector.this.appendProperties)) {
/*   79 */           Redirector.this.setPropertyFromBAOS(this, this.property);
/*   80 */           this.closed = true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean logError = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  113 */   private PropertyOutputStream baos = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  118 */   private PropertyOutputStream errorBaos = null;
/*      */ 
/*      */   
/*      */   private String outputProperty;
/*      */ 
/*      */   
/*      */   private String errorProperty;
/*      */ 
/*      */   
/*      */   private String inputString;
/*      */ 
/*      */   
/*      */   private boolean appendOut = false;
/*      */ 
/*      */   
/*      */   private boolean appendErr = false;
/*      */ 
/*      */   
/*      */   private boolean alwaysLogOut = false;
/*      */ 
/*      */   
/*      */   private boolean alwaysLogErr = false;
/*      */ 
/*      */   
/*      */   private boolean createEmptyFilesOut = true;
/*      */   
/*      */   private boolean createEmptyFilesErr = true;
/*      */   
/*      */   private final ProjectComponent managingTask;
/*      */   
/*  148 */   private OutputStream outputStream = null;
/*      */ 
/*      */   
/*  151 */   private OutputStream errorStream = null;
/*      */ 
/*      */   
/*  154 */   private InputStream inputStream = null;
/*      */ 
/*      */   
/*  157 */   private PrintStream outPrintStream = null;
/*      */ 
/*      */   
/*  160 */   private PrintStream errorPrintStream = null;
/*      */ 
/*      */   
/*      */   private Vector<FilterChain> outputFilterChains;
/*      */ 
/*      */   
/*      */   private Vector<FilterChain> errorFilterChains;
/*      */ 
/*      */   
/*      */   private Vector<FilterChain> inputFilterChains;
/*      */ 
/*      */   
/*  172 */   private String outputEncoding = DEFAULT_ENCODING;
/*      */ 
/*      */   
/*  175 */   private String errorEncoding = DEFAULT_ENCODING;
/*      */ 
/*      */   
/*  178 */   private String inputEncoding = DEFAULT_ENCODING;
/*      */ 
/*      */   
/*      */   private boolean appendProperties = true;
/*      */ 
/*      */   
/*  184 */   private final ThreadGroup threadGroup = new ThreadGroup("redirector");
/*      */ 
/*      */   
/*      */   private boolean logInputString = true;
/*      */ 
/*      */   
/*  190 */   private final Object inMutex = new Object();
/*      */ 
/*      */   
/*  193 */   private final Object outMutex = new Object();
/*      */ 
/*      */   
/*  196 */   private final Object errMutex = new Object();
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean outputIsBinary = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean discardOut = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean discardErr = false;
/*      */ 
/*      */ 
/*      */   
/*      */   public Redirector(Task managingTask) {
/*  213 */     this((ProjectComponent)managingTask);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Redirector(ProjectComponent managingTask) {
/*  224 */     this.managingTask = managingTask;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInput(File input) {
/*  234 */     (new File[1])[0] = input; setInput((input == null) ? null : new File[1]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInput(File[] input) {
/*  244 */     synchronized (this.inMutex) {
/*  245 */       if (input == null) {
/*  246 */         this.input = null;
/*      */       } else {
/*  248 */         this.input = (File[])input.clone();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInputString(String inputString) {
/*  260 */     synchronized (this.inMutex) {
/*  261 */       this.inputString = inputString;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLogInputString(boolean logInputString) {
/*  274 */     this.logInputString = logInputString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setInputStream(InputStream inputStream) {
/*  285 */     synchronized (this.inMutex) {
/*  286 */       this.inputStream = inputStream;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOutput(File out) {
/*  298 */     (new File[1])[0] = out; setOutput((out == null) ? null : new File[1]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOutput(File[] out) {
/*  309 */     synchronized (this.outMutex) {
/*  310 */       if (out == null) {
/*  311 */         this.out = null;
/*      */       } else {
/*  313 */         this.out = (File[])out.clone();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOutputEncoding(String outputEncoding) {
/*  325 */     if (outputEncoding == null) {
/*  326 */       throw new IllegalArgumentException("outputEncoding must not be null");
/*      */     }
/*      */     
/*  329 */     synchronized (this.outMutex) {
/*  330 */       this.outputEncoding = outputEncoding;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setErrorEncoding(String errorEncoding) {
/*  341 */     if (errorEncoding == null) {
/*  342 */       throw new IllegalArgumentException("errorEncoding must not be null");
/*      */     }
/*  344 */     synchronized (this.errMutex) {
/*  345 */       this.errorEncoding = errorEncoding;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInputEncoding(String inputEncoding) {
/*  356 */     if (inputEncoding == null) {
/*  357 */       throw new IllegalArgumentException("inputEncoding must not be null");
/*      */     }
/*  359 */     synchronized (this.inMutex) {
/*  360 */       this.inputEncoding = inputEncoding;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLogError(boolean logError) {
/*  373 */     synchronized (this.errMutex) {
/*  374 */       this.logError = logError;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAppendProperties(boolean appendProperties) {
/*  387 */     synchronized (this.outMutex) {
/*  388 */       this.appendProperties = appendProperties;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setError(File error) {
/*  399 */     (new File[1])[0] = error; setError((error == null) ? null : new File[1]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setError(File[] error) {
/*  409 */     synchronized (this.errMutex) {
/*  410 */       if (error == null) {
/*  411 */         this.error = null;
/*      */       } else {
/*  413 */         this.error = (File[])error.clone();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOutputProperty(String outputProperty) {
/*  425 */     if (outputProperty == null || 
/*  426 */       !outputProperty.equals(this.outputProperty)) {
/*  427 */       synchronized (this.outMutex) {
/*  428 */         this.outputProperty = outputProperty;
/*  429 */         this.baos = null;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAppend(boolean append) {
/*  443 */     synchronized (this.outMutex) {
/*  444 */       this.appendOut = append;
/*      */     } 
/*  446 */     synchronized (this.errMutex) {
/*  447 */       this.appendErr = append;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDiscardOutput(boolean discard) {
/*  463 */     synchronized (this.outMutex) {
/*  464 */       this.discardOut = discard;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDiscardError(boolean discard) {
/*  480 */     synchronized (this.errMutex) {
/*  481 */       this.discardErr = discard;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAlwaysLog(boolean alwaysLog) {
/*  495 */     synchronized (this.outMutex) {
/*  496 */       this.alwaysLogOut = alwaysLog;
/*      */     } 
/*  498 */     synchronized (this.errMutex) {
/*  499 */       this.alwaysLogErr = alwaysLog;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCreateEmptyFiles(boolean createEmptyFiles) {
/*  511 */     synchronized (this.outMutex) {
/*  512 */       this.createEmptyFilesOut = createEmptyFiles;
/*      */     } 
/*  514 */     synchronized (this.outMutex) {
/*  515 */       this.createEmptyFilesErr = createEmptyFiles;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setErrorProperty(String errorProperty) {
/*  526 */     synchronized (this.errMutex) {
/*  527 */       if (errorProperty == null || 
/*  528 */         !errorProperty.equals(this.errorProperty)) {
/*  529 */         this.errorProperty = errorProperty;
/*  530 */         this.errorBaos = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInputFilterChains(Vector<FilterChain> inputFilterChains) {
/*  542 */     synchronized (this.inMutex) {
/*  543 */       this.inputFilterChains = inputFilterChains;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOutputFilterChains(Vector<FilterChain> outputFilterChains) {
/*  554 */     synchronized (this.outMutex) {
/*  555 */       this.outputFilterChains = outputFilterChains;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setErrorFilterChains(Vector<FilterChain> errorFilterChains) {
/*  566 */     synchronized (this.errMutex) {
/*  567 */       this.errorFilterChains = errorFilterChains;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBinaryOutput(boolean b) {
/*  582 */     this.outputIsBinary = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setPropertyFromBAOS(ByteArrayOutputStream baos, String propertyName) {
/*  595 */     BufferedReader in = new BufferedReader(new StringReader(Execute.toString(baos)));
/*  596 */     this.managingTask.getProject().setNewProperty(propertyName, in
/*  597 */         .lines().collect(Collectors.joining(System.lineSeparator())));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void createStreams() {
/*  606 */     synchronized (this.outMutex) {
/*  607 */       outStreams();
/*  608 */       if (this.alwaysLogOut || this.outputStream == null) {
/*  609 */         LogOutputStream logOutputStream = new LogOutputStream(this.managingTask, 2);
/*      */         
/*  611 */         this
/*  612 */           .outputStream = (this.outputStream == null) ? (OutputStream)logOutputStream : (OutputStream)new TeeOutputStream((OutputStream)logOutputStream, this.outputStream);
/*      */       } 
/*      */       
/*  615 */       if ((this.outputFilterChains != null && this.outputFilterChains.size() > 0) || 
/*  616 */         !this.outputEncoding.equalsIgnoreCase(this.inputEncoding)) {
/*      */         try {
/*  618 */           ChainReaderHelper.ChainReader chainReader; LeadPipeInputStream snk = new LeadPipeInputStream();
/*  619 */           snk.setManagingComponent(this.managingTask);
/*      */           
/*  621 */           LeadPipeInputStream leadPipeInputStream1 = snk;
/*      */           
/*  623 */           Reader reader = new InputStreamReader((InputStream)leadPipeInputStream1, this.inputEncoding);
/*      */ 
/*      */           
/*  626 */           if (this.outputFilterChains != null && this.outputFilterChains
/*  627 */             .size() > 0) {
/*  628 */             ChainReaderHelper helper = new ChainReaderHelper();
/*  629 */             helper.setProject(this.managingTask.getProject());
/*  630 */             helper.setPrimaryReader(reader);
/*  631 */             helper.setFilterChains(this.outputFilterChains);
/*  632 */             chainReader = helper.getAssembledReader();
/*      */           } 
/*  634 */           ReaderInputStream readerInputStream = new ReaderInputStream((Reader)chainReader, this.outputEncoding);
/*      */           
/*  636 */           Thread t = new Thread(this.threadGroup, new StreamPumper((InputStream)readerInputStream, this.outputStream, true), "output pumper");
/*      */           
/*  638 */           t.setPriority(10);
/*  639 */           this.outputStream = new PipedOutputStream((PipedInputStream)snk);
/*  640 */           t.start();
/*  641 */         } catch (IOException eyeOhEx) {
/*  642 */           throw new BuildException("error setting up output stream", eyeOhEx);
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  648 */     synchronized (this.errMutex) {
/*  649 */       errorStreams();
/*  650 */       if (this.alwaysLogErr || this.errorStream == null) {
/*  651 */         LogOutputStream logOutputStream = new LogOutputStream(this.managingTask, 1);
/*      */         
/*  653 */         this
/*  654 */           .errorStream = (this.errorStream == null) ? (OutputStream)logOutputStream : (OutputStream)new TeeOutputStream((OutputStream)logOutputStream, this.errorStream);
/*      */       } 
/*      */       
/*  657 */       if ((this.errorFilterChains != null && this.errorFilterChains.size() > 0) || 
/*  658 */         !this.errorEncoding.equalsIgnoreCase(this.inputEncoding)) {
/*      */         try {
/*  660 */           ChainReaderHelper.ChainReader chainReader; LeadPipeInputStream snk = new LeadPipeInputStream();
/*  661 */           snk.setManagingComponent(this.managingTask);
/*      */           
/*  663 */           LeadPipeInputStream leadPipeInputStream1 = snk;
/*      */           
/*  665 */           Reader reader = new InputStreamReader((InputStream)leadPipeInputStream1, this.inputEncoding);
/*      */ 
/*      */           
/*  668 */           if (this.errorFilterChains != null && this.errorFilterChains
/*  669 */             .size() > 0) {
/*  670 */             ChainReaderHelper helper = new ChainReaderHelper();
/*  671 */             helper.setProject(this.managingTask.getProject());
/*  672 */             helper.setPrimaryReader(reader);
/*  673 */             helper.setFilterChains(this.errorFilterChains);
/*  674 */             chainReader = helper.getAssembledReader();
/*      */           } 
/*  676 */           ReaderInputStream readerInputStream = new ReaderInputStream((Reader)chainReader, this.errorEncoding);
/*      */           
/*  678 */           Thread t = new Thread(this.threadGroup, new StreamPumper((InputStream)readerInputStream, this.errorStream, true), "error pumper");
/*      */           
/*  680 */           t.setPriority(10);
/*  681 */           this.errorStream = new PipedOutputStream((PipedInputStream)snk);
/*  682 */           t.start();
/*  683 */         } catch (IOException eyeOhEx) {
/*  684 */           throw new BuildException("error setting up error stream", eyeOhEx);
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  690 */     synchronized (this.inMutex) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  695 */       if (this.input != null && this.input.length > 0) {
/*  696 */         this.managingTask
/*  697 */           .log("Redirecting input from file" + (
/*  698 */             (this.input.length == 1) ? "" : "s"), 3);
/*      */         
/*      */         try {
/*  701 */           this.inputStream = (InputStream)new ConcatFileInputStream(this.input);
/*  702 */         } catch (IOException eyeOhEx) {
/*  703 */           throw new BuildException(eyeOhEx);
/*      */         } 
/*  705 */         ((ConcatFileInputStream)this.inputStream).setManagingComponent(this.managingTask);
/*  706 */       } else if (this.inputString != null) {
/*  707 */         StringBuilder buf = new StringBuilder("Using input ");
/*  708 */         if (this.logInputString) {
/*  709 */           buf.append('"').append(this.inputString).append('"');
/*      */         } else {
/*  711 */           buf.append("string");
/*      */         } 
/*  713 */         this.managingTask.log(buf.toString(), 3);
/*  714 */         this.inputStream = new ByteArrayInputStream(this.inputString.getBytes());
/*      */       } 
/*      */       
/*  717 */       if (this.inputStream != null && this.inputFilterChains != null && this.inputFilterChains
/*  718 */         .size() > 0) {
/*  719 */         ChainReaderHelper helper = new ChainReaderHelper();
/*  720 */         helper.setProject(this.managingTask.getProject());
/*      */         try {
/*  722 */           helper.setPrimaryReader(new InputStreamReader(this.inputStream, this.inputEncoding));
/*      */         }
/*  724 */         catch (IOException eyeOhEx) {
/*  725 */           throw new BuildException("error setting up input stream", eyeOhEx);
/*      */         } 
/*      */         
/*  728 */         helper.setFilterChains(this.inputFilterChains);
/*  729 */         this
/*  730 */           .inputStream = (InputStream)new ReaderInputStream((Reader)helper.getAssembledReader(), this.inputEncoding);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void outStreams() {
/*  737 */     boolean haveOutputFiles = (this.out != null && this.out.length > 0);
/*  738 */     if (this.discardOut) {
/*  739 */       if (haveOutputFiles || this.outputProperty != null) {
/*  740 */         throw new BuildException("Cant discard output when output or outputProperty are set");
/*      */       }
/*      */       
/*  743 */       this.managingTask.log("Discarding output", 3);
/*  744 */       this.outputStream = (OutputStream)NullOutputStream.INSTANCE;
/*      */       return;
/*      */     } 
/*  747 */     if (haveOutputFiles) {
/*      */       
/*  749 */       String logHead = "Output " + (this.appendOut ? "appended" : "redirected") + " to ";
/*  750 */       this.outputStream = foldFiles(this.out, logHead, 3, this.appendOut, this.createEmptyFilesOut);
/*      */     } 
/*      */     
/*  753 */     if (this.outputProperty != null) {
/*  754 */       if (this.baos == null) {
/*  755 */         this.baos = new PropertyOutputStream(this.outputProperty);
/*  756 */         this.managingTask.log("Output redirected to property: " + this.outputProperty, 3);
/*      */       } 
/*      */ 
/*      */       
/*  760 */       KeepAliveOutputStream keepAliveOutputStream = new KeepAliveOutputStream(this.baos);
/*  761 */       this
/*  762 */         .outputStream = (this.outputStream == null) ? (OutputStream)keepAliveOutputStream : (OutputStream)new TeeOutputStream(this.outputStream, (OutputStream)keepAliveOutputStream);
/*      */     } else {
/*  764 */       this.baos = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void errorStreams() {
/*  769 */     boolean haveErrorFiles = (this.error != null && this.error.length > 0);
/*  770 */     if (this.discardErr) {
/*  771 */       if (haveErrorFiles || this.errorProperty != null || this.logError) {
/*  772 */         throw new BuildException("Cant discard error output when error, errorProperty or logError are set");
/*      */       }
/*      */       
/*  775 */       this.managingTask.log("Discarding error output", 3);
/*  776 */       this.errorStream = (OutputStream)NullOutputStream.INSTANCE;
/*      */       return;
/*      */     } 
/*  779 */     if (haveErrorFiles) {
/*      */       
/*  781 */       String logHead = "Error " + (this.appendErr ? "appended" : "redirected") + " to ";
/*  782 */       this.errorStream = foldFiles(this.error, logHead, 3, this.appendErr, this.createEmptyFilesErr);
/*      */     }
/*  784 */     else if (!this.logError && this.outputStream != null && this.errorProperty == null) {
/*  785 */       long funnelTimeout = 0L;
/*  786 */       OutputStreamFunneler funneler = new OutputStreamFunneler(this.outputStream, 0L);
/*      */       
/*      */       try {
/*  789 */         this.outputStream = funneler.getFunnelInstance();
/*  790 */         this.errorStream = funneler.getFunnelInstance();
/*  791 */         if (!this.outputIsBinary) {
/*  792 */           this.outputStream = (OutputStream)new LineOrientedOutputStreamRedirector(this.outputStream);
/*  793 */           this.errorStream = (OutputStream)new LineOrientedOutputStreamRedirector(this.errorStream);
/*      */         } 
/*  795 */       } catch (IOException eyeOhEx) {
/*  796 */         throw new BuildException("error splitting output/error streams", eyeOhEx);
/*      */       } 
/*      */     } 
/*      */     
/*  800 */     if (this.errorProperty != null) {
/*  801 */       if (this.errorBaos == null) {
/*  802 */         this.errorBaos = new PropertyOutputStream(this.errorProperty);
/*  803 */         this.managingTask.log("Error redirected to property: " + this.errorProperty, 3);
/*      */       } 
/*      */ 
/*      */       
/*  807 */       KeepAliveOutputStream keepAliveOutputStream = new KeepAliveOutputStream(this.errorBaos);
/*  808 */       this
/*  809 */         .errorStream = (this.error == null || this.error.length == 0) ? (OutputStream)keepAliveOutputStream : (OutputStream)new TeeOutputStream(this.errorStream, (OutputStream)keepAliveOutputStream);
/*      */     } else {
/*  811 */       this.errorBaos = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ExecuteStreamHandler createHandler() throws BuildException {
/*  825 */     createStreams();
/*  826 */     boolean nonBlockingRead = (this.input == null && this.inputString == null);
/*  827 */     return new PumpStreamHandler(getOutputStream(), getErrorStream(), 
/*  828 */         getInputStream(), nonBlockingRead);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleOutput(String output) {
/*  838 */     synchronized (this.outMutex) {
/*  839 */       if (this.outPrintStream == null) {
/*  840 */         this.outPrintStream = new PrintStream(this.outputStream);
/*      */       }
/*  842 */       this.outPrintStream.print(output);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int handleInput(byte[] buffer, int offset, int length) throws IOException {
/*  863 */     synchronized (this.inMutex) {
/*  864 */       if (this.inputStream == null) {
/*  865 */         return this.managingTask.getProject().defaultInput(buffer, offset, length);
/*      */       }
/*      */       
/*  868 */       return this.inputStream.read(buffer, offset, length);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleFlush(String output) {
/*  880 */     synchronized (this.outMutex) {
/*  881 */       if (this.outPrintStream == null) {
/*  882 */         this.outPrintStream = new PrintStream(this.outputStream);
/*      */       }
/*  884 */       this.outPrintStream.print(output);
/*  885 */       this.outPrintStream.flush();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleErrorOutput(String output) {
/*  896 */     synchronized (this.errMutex) {
/*  897 */       if (this.errorPrintStream == null) {
/*  898 */         this.errorPrintStream = new PrintStream(this.errorStream);
/*      */       }
/*  900 */       this.errorPrintStream.print(output);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleErrorFlush(String output) {
/*  911 */     synchronized (this.errMutex) {
/*  912 */       if (this.errorPrintStream == null) {
/*  913 */         this.errorPrintStream = new PrintStream(this.errorStream);
/*      */       }
/*  915 */       this.errorPrintStream.print(output);
/*  916 */       this.errorPrintStream.flush();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public OutputStream getOutputStream() {
/*  927 */     synchronized (this.outMutex) {
/*  928 */       return this.outputStream;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public OutputStream getErrorStream() {
/*  939 */     synchronized (this.errMutex) {
/*  940 */       return this.errorStream;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InputStream getInputStream() {
/*  951 */     synchronized (this.inMutex) {
/*  952 */       return this.inputStream;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void complete() throws IOException {
/*  967 */     System.out.flush();
/*  968 */     System.err.flush();
/*      */     
/*  970 */     synchronized (this.inMutex) {
/*  971 */       if (this.inputStream != null) {
/*  972 */         this.inputStream.close();
/*      */       }
/*      */     } 
/*      */     
/*  976 */     synchronized (this.outMutex) {
/*  977 */       this.outputStream.flush();
/*  978 */       this.outputStream.close();
/*      */     } 
/*      */     
/*  981 */     synchronized (this.errMutex) {
/*  982 */       this.errorStream.flush();
/*  983 */       this.errorStream.close();
/*      */     } 
/*      */ 
/*      */     
/*  987 */     synchronized (this) {
/*  988 */       while (this.threadGroup.activeCount() > 0) {
/*      */         try {
/*  990 */           this.managingTask.log("waiting for " + this.threadGroup.activeCount() + " Threads:", 4);
/*      */           
/*  992 */           Thread[] thread = new Thread[this.threadGroup.activeCount()];
/*  993 */           this.threadGroup.enumerate(thread);
/*  994 */           for (int i = 0; i < thread.length && thread[i] != null; i++) {
/*      */             try {
/*  996 */               this.managingTask.log(thread[i].toString(), 4);
/*      */             }
/*  998 */             catch (NullPointerException nullPointerException) {}
/*      */           } 
/*      */ 
/*      */           
/* 1002 */           wait(1000L);
/* 1003 */         } catch (InterruptedException eyeEx) {
/* 1004 */           Thread[] thread = new Thread[this.threadGroup.activeCount()];
/* 1005 */           this.threadGroup.enumerate(thread);
/* 1006 */           for (int i = 0; i < thread.length && thread[i] != null; i++) {
/* 1007 */             thread[i].interrupt();
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1013 */     setProperties();
/*      */     
/* 1015 */     synchronized (this.inMutex) {
/* 1016 */       this.inputStream = null;
/*      */     } 
/* 1018 */     synchronized (this.outMutex) {
/* 1019 */       this.outputStream = null;
/* 1020 */       this.outPrintStream = null;
/*      */     } 
/* 1022 */     synchronized (this.errMutex) {
/* 1023 */       this.errorStream = null;
/* 1024 */       this.errorPrintStream = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProperties() {
/* 1033 */     synchronized (this.outMutex) {
/* 1034 */       FileUtils.close(this.baos);
/*      */     } 
/* 1036 */     synchronized (this.errMutex) {
/* 1037 */       FileUtils.close(this.errorBaos);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private OutputStream foldFiles(File[] file, String logHead, int loglevel, boolean append, boolean createEmptyFiles) {
/* 1043 */     LazyFileOutputStream lazyFileOutputStream = new LazyFileOutputStream(file[0], append, createEmptyFiles);
/*      */ 
/*      */     
/* 1046 */     this.managingTask.log(logHead + file[0], loglevel);
/* 1047 */     char[] c = new char[logHead.length()];
/* 1048 */     Arrays.fill(c, ' ');
/* 1049 */     String indent = new String(c);
/*      */     
/* 1051 */     for (int i = 1; i < file.length; i++) {
/* 1052 */       this.outputStream = (OutputStream)new TeeOutputStream(this.outputStream, (OutputStream)new LazyFileOutputStream(file[i], append, createEmptyFiles));
/*      */       
/* 1054 */       this.managingTask.log(indent + file[i], loglevel);
/*      */     } 
/* 1056 */     return (OutputStream)lazyFileOutputStream;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Redirector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */