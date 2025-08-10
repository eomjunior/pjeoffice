/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.file.Files;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.types.resources.Union;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.StreamUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Replace
/*     */   extends MatchingTask
/*     */ {
/*  62 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*  64 */   private File sourceFile = null;
/*  65 */   private NestedString token = null;
/*  66 */   private NestedString value = new NestedString();
/*     */   
/*  68 */   private Resource propertyResource = null;
/*  69 */   private Resource replaceFilterResource = null;
/*  70 */   private Properties properties = null;
/*  71 */   private List<Replacefilter> replacefilters = new ArrayList<>();
/*     */   
/*  73 */   private File dir = null;
/*     */   
/*     */   private int fileCount;
/*     */   
/*     */   private int replaceCount;
/*     */   
/*     */   private boolean summary = false;
/*  80 */   private String encoding = null;
/*     */   
/*     */   private Union resources;
/*     */   
/*     */   private boolean preserveLastModified = false;
/*     */   
/*     */   private boolean failOnNoReplacements = false;
/*     */ 
/*     */   
/*     */   public class NestedString
/*     */   {
/*     */     private boolean expandProperties = false;
/*     */     
/*  93 */     private StringBuffer buf = new StringBuffer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setExpandProperties(boolean b) {
/* 107 */       this.expandProperties = b;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addText(String val) {
/* 116 */       this.buf.append(val);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getText() {
/* 123 */       String s = this.buf.toString();
/* 124 */       return this.expandProperties ? Replace.this.getProject().replaceProperties(s) : s;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class Replacefilter
/*     */   {
/*     */     private Replace.NestedString token;
/*     */     
/*     */     private Replace.NestedString value;
/*     */     
/*     */     private String replaceValue;
/*     */     private String property;
/*     */     private StringBuffer inputBuffer;
/* 138 */     private StringBuffer outputBuffer = new StringBuffer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void validate() throws BuildException {
/* 146 */       if (this.token == null) {
/* 147 */         throw new BuildException("token is a mandatory for replacefilter.");
/*     */       }
/*     */ 
/*     */       
/* 151 */       if (this.token.getText().isEmpty()) {
/* 152 */         throw new BuildException("The token must not be an empty string.");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 157 */       if (this.value != null && this.property != null) {
/* 158 */         throw new BuildException("Either value or property can be specified, but a replacefilter element cannot have both.");
/*     */       }
/*     */ 
/*     */       
/* 162 */       if (this.property != null) {
/*     */         
/* 164 */         if (Replace.this.propertyResource == null) {
/* 165 */           throw new BuildException("The replacefilter's property attribute can only be used with the replacetask's propertyFile/Resource attribute.");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 170 */         if (Replace.this.properties == null || Replace.this
/* 171 */           .properties.getProperty(this.property) == null) {
/* 172 */           throw new BuildException("property \"%s\" was not found in %s", new Object[] { this.property, 
/*     */                 
/* 174 */                 Replace.access$000(this.this$0).getName() });
/*     */         }
/*     */       } 
/*     */       
/* 178 */       this.replaceValue = getReplaceValue();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getReplaceValue() {
/* 186 */       if (this.property != null) {
/* 187 */         return Replace.this.properties.getProperty(this.property);
/*     */       }
/* 189 */       if (this.value != null) {
/* 190 */         return this.value.getText();
/*     */       }
/* 192 */       if (Replace.this.value != null) {
/* 193 */         return Replace.this.value.getText();
/*     */       }
/*     */       
/* 196 */       return "";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setToken(String t) {
/* 204 */       createReplaceToken().addText(t);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getToken() {
/* 212 */       return this.token.getText();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setValue(String value) {
/* 221 */       createReplaceValue().addText(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getValue() {
/* 229 */       return this.value.getText();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setProperty(String property) {
/* 238 */       this.property = property;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getProperty() {
/* 247 */       return this.property;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Replace.NestedString createReplaceToken() {
/* 256 */       if (this.token == null) {
/* 257 */         this.token = new Replace.NestedString();
/*     */       }
/* 259 */       return this.token;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Replace.NestedString createReplaceValue() {
/* 268 */       if (this.value == null) {
/* 269 */         this.value = new Replace.NestedString();
/*     */       }
/* 271 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     StringBuffer getOutputBuffer() {
/* 280 */       return this.outputBuffer;
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
/*     */     void setInputBuffer(StringBuffer input) {
/* 292 */       this.inputBuffer = input;
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
/*     */     boolean process() {
/* 304 */       String t = getToken();
/* 305 */       if (this.inputBuffer.length() > t.length()) {
/* 306 */         int pos = replace();
/* 307 */         pos = Math.max(this.inputBuffer.length() - t.length(), pos);
/* 308 */         this.outputBuffer.append(this.inputBuffer.substring(0, pos));
/* 309 */         this.inputBuffer.delete(0, pos);
/* 310 */         return true;
/*     */       } 
/* 312 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void flush() {
/* 321 */       replace();
/* 322 */       this.outputBuffer.append(this.inputBuffer);
/* 323 */       this.inputBuffer.delete(0, this.inputBuffer.length());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int replace() {
/* 332 */       String t = getToken();
/* 333 */       int found = this.inputBuffer.indexOf(t);
/* 334 */       int pos = -1;
/* 335 */       int tokenLength = t.length();
/* 336 */       int replaceValueLength = this.replaceValue.length();
/* 337 */       while (found >= 0) {
/* 338 */         this.inputBuffer.replace(found, found + tokenLength, this.replaceValue);
/* 339 */         pos = found + replaceValueLength;
/* 340 */         found = this.inputBuffer.indexOf(t, pos);
/* 341 */         ++Replace.this.replaceCount;
/*     */       } 
/* 343 */       return pos;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class FileInput
/*     */     implements AutoCloseable
/*     */   {
/*     */     private static final int BUFF_SIZE = 4096;
/*     */ 
/*     */     
/*     */     private StringBuffer outputBuffer;
/*     */ 
/*     */     
/*     */     private final InputStream is;
/*     */     
/*     */     private Reader reader;
/*     */     
/*     */     private char[] buffer;
/*     */ 
/*     */     
/*     */     FileInput(File source) throws IOException {
/* 366 */       this.outputBuffer = new StringBuffer();
/* 367 */       this.buffer = new char[4096];
/* 368 */       this.is = Files.newInputStream(source.toPath(), new java.nio.file.OpenOption[0]);
/*     */       try {
/* 370 */         this
/*     */           
/* 372 */           .reader = new BufferedReader((Replace.this.encoding != null) ? new InputStreamReader(this.is, Replace.this.encoding) : new InputStreamReader(this.is));
/*     */       } finally {
/* 374 */         if (this.reader == null) {
/* 375 */           this.is.close();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     StringBuffer getOutputBuffer() {
/* 386 */       return this.outputBuffer;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean readChunk() throws IOException {
/* 395 */       int bufferLength = this.reader.read(this.buffer);
/* 396 */       if (bufferLength < 0) {
/* 397 */         return false;
/*     */       }
/* 399 */       this.outputBuffer.append(new String(this.buffer, 0, bufferLength));
/* 400 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 409 */       this.is.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class FileOutput
/*     */     implements AutoCloseable
/*     */   {
/*     */     private StringBuffer inputBuffer;
/*     */ 
/*     */     
/*     */     private final OutputStream os;
/*     */ 
/*     */     
/*     */     private Writer writer;
/*     */ 
/*     */ 
/*     */     
/*     */     FileOutput(File out) throws IOException {
/* 430 */       this.os = Files.newOutputStream(out.toPath(), new java.nio.file.OpenOption[0]);
/*     */       try {
/* 432 */         this
/*     */           
/* 434 */           .writer = new BufferedWriter((Replace.this.encoding != null) ? new OutputStreamWriter(this.os, Replace.this.encoding) : new OutputStreamWriter(this.os));
/*     */       } finally {
/* 436 */         if (this.writer == null) {
/* 437 */           this.os.close();
/*     */         }
/*     */       } 
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
/*     */     void setInputBuffer(StringBuffer input) {
/* 451 */       this.inputBuffer = input;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean process() throws IOException {
/* 462 */       this.writer.write(this.inputBuffer.toString());
/* 463 */       this.inputBuffer.delete(0, this.inputBuffer.length());
/* 464 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void flush() throws IOException {
/* 472 */       process();
/* 473 */       this.writer.flush();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 482 */       this.os.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 493 */     List<Replacefilter> savedFilters = new ArrayList<>(this.replacefilters);
/*     */     
/* 495 */     Properties savedProperties = (this.properties == null) ? null : (Properties)this.properties.clone();
/*     */     
/* 497 */     if (this.token != null) {
/*     */ 
/*     */ 
/*     */       
/* 501 */       StringBuilder val = new StringBuilder(this.value.getText());
/* 502 */       stringReplace(val, "\r\n", "\n");
/* 503 */       stringReplace(val, "\n", System.lineSeparator());
/* 504 */       StringBuilder tok = new StringBuilder(this.token.getText());
/* 505 */       stringReplace(tok, "\r\n", "\n");
/* 506 */       stringReplace(tok, "\n", System.lineSeparator());
/* 507 */       Replacefilter firstFilter = createPrimaryfilter();
/* 508 */       firstFilter.setToken(tok.toString());
/* 509 */       firstFilter.setValue(val.toString());
/*     */     } 
/*     */     
/*     */     try {
/* 513 */       if (this.replaceFilterResource != null) {
/* 514 */         Properties properties = getProperties(this.replaceFilterResource);
/* 515 */         StreamUtils.iteratorAsStream(getOrderedIterator(properties)).forEach(tok -> {
/*     */               Replacefilter replaceFilter = createReplacefilter();
/*     */               
/*     */               replaceFilter.setToken(tok);
/*     */               replaceFilter.setValue(properties.getProperty(tok));
/*     */             });
/*     */       } 
/* 522 */       validateAttributes();
/*     */       
/* 524 */       if (this.propertyResource != null) {
/* 525 */         this.properties = getProperties(this.propertyResource);
/*     */       }
/*     */       
/* 528 */       validateReplacefilters();
/* 529 */       this.fileCount = 0;
/* 530 */       this.replaceCount = 0;
/*     */       
/* 532 */       if (this.sourceFile != null) {
/* 533 */         processFile(this.sourceFile);
/*     */       }
/*     */       
/* 536 */       if (this.dir != null) {
/* 537 */         DirectoryScanner ds = getDirectoryScanner(this.dir);
/* 538 */         for (String src : ds.getIncludedFiles()) {
/* 539 */           File file = new File(this.dir, src);
/* 540 */           processFile(file);
/*     */         } 
/*     */       } 
/*     */       
/* 544 */       if (this.resources != null) {
/* 545 */         for (Resource r : this.resources) {
/* 546 */           processFile(((FileProvider)r.as(FileProvider.class)).getFile());
/*     */         }
/*     */       }
/*     */       
/* 550 */       if (this.summary) {
/* 551 */         log("Replaced " + this.replaceCount + " occurrences in " + this.fileCount + " files.", 2);
/*     */       }
/*     */       
/* 554 */       if (this.failOnNoReplacements && this.replaceCount == 0) {
/* 555 */         throw new BuildException("didn't replace anything");
/*     */       }
/*     */     } finally {
/* 558 */       this.replacefilters = savedFilters;
/* 559 */       this.properties = savedProperties;
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
/*     */   public void validateAttributes() throws BuildException {
/* 571 */     if (this.sourceFile == null && this.dir == null && this.resources == null) {
/* 572 */       throw new BuildException("Either the file or the dir attribute or nested resources must be specified", 
/*     */           
/* 574 */           getLocation());
/*     */     }
/* 576 */     if (this.propertyResource != null && !this.propertyResource.isExists()) {
/* 577 */       throw new BuildException("Property file " + this.propertyResource
/* 578 */           .getName() + " does not exist.", 
/* 579 */           getLocation());
/*     */     }
/* 581 */     if (this.token == null && this.replacefilters.isEmpty()) {
/* 582 */       throw new BuildException("Either token or a nested replacefilter must be specified", 
/*     */           
/* 584 */           getLocation());
/*     */     }
/* 586 */     if (this.token != null && this.token.getText().isEmpty()) {
/* 587 */       throw new BuildException("The token attribute must not be an empty string.", 
/*     */           
/* 589 */           getLocation());
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
/*     */   public void validateReplacefilters() throws BuildException {
/* 601 */     this.replacefilters.forEach(Replacefilter::validate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties getProperties(File propertyFile) throws BuildException {
/* 611 */     return getProperties((Resource)new FileResource(getProject(), propertyFile));
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
/*     */   public Properties getProperties(Resource propertyResource) throws BuildException {
/* 623 */     Properties props = new Properties();
/*     */     
/* 625 */     try { InputStream in = propertyResource.getInputStream(); 
/* 626 */       try { props.load(in);
/* 627 */         if (in != null) in.close();  } catch (Throwable throwable) { if (in != null) try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 628 */     { throw new BuildException("Property resource (%s) cannot be loaded.", new Object[] { propertyResource
/* 629 */             .getName() }); }
/*     */     
/* 631 */     return props;
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
/*     */   private void processFile(File src) throws BuildException {
/* 643 */     if (!src.exists()) {
/* 644 */       throw new BuildException("Replace: source file " + src.getPath() + " doesn't exist", 
/* 645 */           getLocation());
/*     */     }
/*     */     
/* 648 */     int repCountStart = this.replaceCount;
/* 649 */     logFilterChain(src.getPath());
/*     */     
/*     */     try {
/* 652 */       File temp = FILE_UTILS.createTempFile(getProject(), "rep", ".tmp", src
/* 653 */           .getParentFile(), false, true);
/*     */       try {
/* 655 */         FileInput in = new FileInput(src); 
/* 656 */         try { FileOutput out = new FileOutput(temp); 
/* 657 */           try { out.setInputBuffer(buildFilterChain(in.getOutputBuffer()));
/*     */             
/* 659 */             while (in.readChunk()) {
/* 660 */               if (processFilterChain()) {
/* 661 */                 out.process();
/*     */               }
/*     */             } 
/*     */             
/* 665 */             flushFilterChain();
/*     */             
/* 667 */             out.flush();
/* 668 */             out.close(); } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  in.close(); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 669 */          boolean changes = (this.replaceCount != repCountStart);
/* 670 */         if (changes) {
/* 671 */           this.fileCount++;
/* 672 */           long origLastModified = src.lastModified();
/* 673 */           FILE_UTILS.rename(temp, src);
/* 674 */           if (this.preserveLastModified) {
/* 675 */             FILE_UTILS.setFileLastModified(src, origLastModified);
/*     */           }
/*     */         } 
/*     */       } finally {
/* 679 */         if (temp.isFile() && !temp.delete()) {
/* 680 */           temp.deleteOnExit();
/*     */         }
/*     */       } 
/* 683 */     } catch (IOException ioe) {
/* 684 */       throw new BuildException("IOException in " + src + " - " + ioe
/* 685 */           .getClass().getName() + ":" + ioe
/* 686 */           .getMessage(), ioe, getLocation());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void flushFilterChain() {
/* 694 */     this.replacefilters.forEach(Replacefilter::flush);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean processFilterChain() {
/* 702 */     return this.replacefilters.stream().allMatch(Replacefilter::process);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StringBuffer buildFilterChain(StringBuffer inputBuffer) {
/* 712 */     StringBuffer buf = inputBuffer;
/* 713 */     for (Replacefilter filter : this.replacefilters) {
/* 714 */       filter.setInputBuffer(buf);
/* 715 */       buf = filter.getOutputBuffer();
/*     */     } 
/* 717 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void logFilterChain(String filename) {
/* 725 */     this.replacefilters
/* 726 */       .forEach(filter -> log("Replacing in " + filename + ": " + filter.getToken() + " --> " + filter.getReplaceValue(), 3));
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
/*     */   public void setFile(File file) {
/* 738 */     this.sourceFile = file;
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
/*     */   public void setSummary(boolean summary) {
/* 750 */     this.summary = summary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReplaceFilterFile(File replaceFilterFile) {
/* 761 */     setReplaceFilterResource((Resource)new FileResource(getProject(), replaceFilterFile));
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
/*     */   public void setReplaceFilterResource(Resource replaceFilter) {
/* 773 */     this.replaceFilterResource = replaceFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDir(File dir) {
/* 782 */     this.dir = dir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToken(String token) {
/* 792 */     createReplaceToken().addText(token);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String value) {
/* 801 */     createReplaceValue().addText(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/* 811 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NestedString createReplaceToken() {
/* 819 */     if (this.token == null) {
/* 820 */       this.token = new NestedString();
/*     */     }
/* 822 */     return this.token;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NestedString createReplaceValue() {
/* 830 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPropertyFile(File propertyFile) {
/* 840 */     setPropertyResource((Resource)new FileResource(propertyFile));
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
/*     */   public void setPropertyResource(Resource propertyResource) {
/* 853 */     this.propertyResource = propertyResource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Replacefilter createReplacefilter() {
/* 861 */     Replacefilter filter = new Replacefilter();
/* 862 */     this.replacefilters.add(filter);
/* 863 */     return filter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfigured(ResourceCollection rc) {
/* 873 */     if (!rc.isFilesystemOnly()) {
/* 874 */       throw new BuildException("only filesystem resources are supported");
/*     */     }
/* 876 */     if (this.resources == null) {
/* 877 */       this.resources = new Union();
/*     */     }
/* 879 */     this.resources.add(rc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreserveLastModified(boolean b) {
/* 890 */     this.preserveLastModified = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnNoReplacements(boolean b) {
/* 900 */     this.failOnNoReplacements = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Replacefilter createPrimaryfilter() {
/* 909 */     Replacefilter filter = new Replacefilter();
/* 910 */     this.replacefilters.add(0, filter);
/* 911 */     return filter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void stringReplace(StringBuilder str, String str1, String str2) {
/* 922 */     int found = str.indexOf(str1);
/* 923 */     int str1Length = str1.length();
/* 924 */     int str2Length = str2.length();
/* 925 */     while (found >= 0) {
/* 926 */       str.replace(found, found + str1Length, str2);
/* 927 */       found = str.indexOf(str1, found + str2Length);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Iterator<String> getOrderedIterator(Properties props) {
/* 938 */     List<String> keys = new ArrayList<>(props.stringPropertyNames());
/* 939 */     keys.sort(Comparator.<String>comparingInt(String::length).reversed());
/* 940 */     return keys.iterator();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Replace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */