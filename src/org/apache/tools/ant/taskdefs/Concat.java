/*      */ package org.apache.tools.ant.taskdefs;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.io.Writer;
/*      */ import java.nio.file.Files;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.Vector;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.ProjectComponent;
/*      */ import org.apache.tools.ant.Task;
/*      */ import org.apache.tools.ant.filters.util.ChainReaderHelper;
/*      */ import org.apache.tools.ant.types.FileList;
/*      */ import org.apache.tools.ant.types.FileSet;
/*      */ import org.apache.tools.ant.types.FilterChain;
/*      */ import org.apache.tools.ant.types.Path;
/*      */ import org.apache.tools.ant.types.Resource;
/*      */ import org.apache.tools.ant.types.ResourceCollection;
/*      */ import org.apache.tools.ant.types.resources.FileResource;
/*      */ import org.apache.tools.ant.types.resources.Intersect;
/*      */ import org.apache.tools.ant.types.resources.LogOutputResource;
/*      */ import org.apache.tools.ant.types.resources.Resources;
/*      */ import org.apache.tools.ant.types.resources.Restrict;
/*      */ import org.apache.tools.ant.types.resources.StringResource;
/*      */ import org.apache.tools.ant.types.resources.selectors.Exists;
/*      */ import org.apache.tools.ant.types.resources.selectors.Not;
/*      */ import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
/*      */ import org.apache.tools.ant.types.selectors.SelectorUtils;
/*      */ import org.apache.tools.ant.util.ConcatResourceInputStream;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.ReaderInputStream;
/*      */ import org.apache.tools.ant.util.ResourceUtils;
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
/*      */ public class Concat
/*      */   extends Task
/*      */   implements ResourceCollection
/*      */ {
/*      */   private static final int BUFFER_SIZE = 8192;
/*   83 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*      */   
/*   85 */   private static final ResourceSelector EXISTS = (ResourceSelector)new Exists(); private Resource dest; private boolean append; private String encoding; private String outputEncoding; private boolean binary;
/*   86 */   private static final ResourceSelector NOT_EXISTS = (ResourceSelector)new Not(EXISTS);
/*      */   private boolean filterBeforeConcat;
/*      */   private StringBuffer textBuffer;
/*      */   private Resources rc;
/*      */   private Vector<FilterChain> filterChains;
/*      */   
/*   92 */   public static class TextElement extends ProjectComponent { private String value = "";
/*      */     private boolean trimLeading = false;
/*      */     private boolean trim = false;
/*      */     private boolean filtering = true;
/*   96 */     private String encoding = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setFiltering(boolean filtering) {
/*  106 */       this.filtering = filtering;
/*      */     }
/*      */ 
/*      */     
/*      */     private boolean getFiltering() {
/*  111 */       return this.filtering;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setEncoding(String encoding) {
/*  120 */       this.encoding = encoding;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setFile(File file) throws BuildException {
/*  131 */       if (!file.exists()) {
/*  132 */         throw new BuildException("File %s does not exist.", new Object[] { file });
/*      */       }
/*      */       
/*  135 */       BufferedReader reader = null;
/*      */       try {
/*  137 */         if (this.encoding == null) {
/*  138 */           reader = new BufferedReader(new FileReader(file));
/*      */         } else {
/*      */           
/*  141 */           reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]), this.encoding));
/*      */         } 
/*      */         
/*  144 */         this.value = FileUtils.safeReadFully(reader);
/*  145 */       } catch (IOException ex) {
/*  146 */         throw new BuildException(ex);
/*      */       } finally {
/*  148 */         FileUtils.close(reader);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addText(String value) {
/*  157 */       this.value += getProject().replaceProperties(value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setTrimLeading(boolean strip) {
/*  165 */       this.trimLeading = strip;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setTrim(boolean trim) {
/*  173 */       this.trim = trim;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getValue() {
/*  180 */       if (this.value == null) {
/*  181 */         this.value = "";
/*      */       }
/*  183 */       if (this.value.trim().isEmpty()) {
/*  184 */         this.value = "";
/*      */       }
/*  186 */       if (this.trimLeading) {
/*  187 */         StringBuilder b = new StringBuilder();
/*  188 */         boolean startOfLine = true;
/*  189 */         for (char ch : this.value.toCharArray()) {
/*  190 */           if (startOfLine) {
/*  191 */             if (ch == ' ' || ch == '\t') {
/*      */               continue;
/*      */             }
/*  194 */             startOfLine = false;
/*      */           } 
/*  196 */           b.append(ch);
/*  197 */           if (ch == '\n' || ch == '\r')
/*  198 */             startOfLine = true; 
/*      */           continue;
/*      */         } 
/*  201 */         this.value = b.toString();
/*      */       } 
/*  203 */       if (this.trim) {
/*  204 */         this.value = this.value.trim();
/*      */       }
/*  206 */       return this.value;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class LastLineFixingReader
/*      */     extends Reader
/*      */   {
/*      */     private final Reader reader;
/*      */ 
/*      */ 
/*      */     
/*  220 */     private int lastPos = 0;
/*  221 */     private final char[] lastChars = new char[Concat.this.eolString.length()];
/*      */     private boolean needAddSeparator = false;
/*      */     
/*      */     private LastLineFixingReader(Reader reader) {
/*  225 */       this.reader = reader;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int read() throws IOException {
/*  237 */       if (this.needAddSeparator) {
/*  238 */         if (this.lastPos >= Concat.this.eolString.length()) {
/*  239 */           return -1;
/*      */         }
/*  241 */         return Concat.this.eolString.charAt(this.lastPos++);
/*      */       } 
/*      */       
/*  244 */       int ch = this.reader.read();
/*  245 */       if (ch == -1) {
/*  246 */         if (isMissingEndOfLine()) {
/*  247 */           this.needAddSeparator = true;
/*  248 */           this.lastPos = 1;
/*  249 */           return Concat.this.eolString.charAt(0);
/*      */         } 
/*      */       } else {
/*  252 */         addLastChar((char)ch);
/*  253 */         return ch;
/*      */       } 
/*  255 */       return -1;
/*      */     }
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
/*      */     public int read(char[] cbuf, int off, int len) throws IOException {
/*  270 */       int amountRead = 0;
/*      */       label31: while (true) {
/*  272 */         while (this.needAddSeparator) {
/*  273 */           if (this.lastPos >= Concat.this.eolString.length()) {
/*      */             break label31;
/*      */           }
/*  276 */           cbuf[off] = Concat.this.eolString.charAt(this.lastPos++);
/*  277 */           len--;
/*  278 */           off++;
/*  279 */           amountRead++;
/*  280 */           if (len == 0) {
/*  281 */             return amountRead;
/*      */           }
/*      */         } 
/*      */         
/*  285 */         int nRead = this.reader.read(cbuf, off, len);
/*  286 */         if (nRead == -1 || nRead == 0) {
/*  287 */           if (isMissingEndOfLine()) {
/*  288 */             this.needAddSeparator = true;
/*  289 */             this.lastPos = 0;
/*      */             continue;
/*      */           } 
/*      */           break;
/*      */         } 
/*  294 */         int i = nRead;
/*  295 */         for (; i > nRead - this.lastChars.length; 
/*  296 */           i--) {
/*  297 */           if (i <= 0) {
/*      */             break;
/*      */           }
/*  300 */           addLastChar(cbuf[off + i - 1]);
/*      */         } 
/*  302 */         len -= nRead;
/*  303 */         off += nRead;
/*  304 */         amountRead += nRead;
/*  305 */         if (len == 0) {
/*  306 */           return amountRead;
/*      */         }
/*      */       } 
/*      */       
/*  310 */       if (amountRead == 0) {
/*  311 */         return -1;
/*      */       }
/*  313 */       return amountRead;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void close() throws IOException {
/*  321 */       this.reader.close();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void addLastChar(char ch) {
/*  329 */       System.arraycopy(this.lastChars, 1, this.lastChars, 0, this.lastChars.length - 2 + 1);
/*  330 */       this.lastChars[this.lastChars.length - 1] = ch;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean isMissingEndOfLine() {
/*  338 */       for (int i = 0; i < this.lastChars.length; i++) {
/*  339 */         if (this.lastChars[i] != Concat.this.eolString.charAt(i)) {
/*  340 */           return true;
/*      */         }
/*      */       } 
/*  343 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class MultiReader<S>
/*      */     extends Reader
/*      */   {
/*  353 */     private Reader reader = null;
/*      */     
/*      */     private Iterator<S> readerSources;
/*      */     private Concat.ReaderFactory<S> factory;
/*      */     private final boolean filterBeforeConcat;
/*      */     
/*      */     private MultiReader(Iterator<S> readerSources, Concat.ReaderFactory<S> factory, boolean filterBeforeConcat) {
/*  360 */       this.readerSources = readerSources;
/*  361 */       this.factory = factory;
/*  362 */       this.filterBeforeConcat = filterBeforeConcat;
/*      */     }
/*      */     
/*      */     private Reader getReader() throws IOException {
/*  366 */       if (this.reader == null && this.readerSources.hasNext()) {
/*  367 */         this.reader = this.factory.getReader(this.readerSources.next());
/*  368 */         if (isFixLastLine()) {
/*  369 */           this.reader = new Concat.LastLineFixingReader(this.reader);
/*      */         }
/*  371 */         if (this.filterBeforeConcat) {
/*  372 */           this.reader = Concat.this.getFilteredReader(this.reader);
/*      */         }
/*      */       } 
/*  375 */       return this.reader;
/*      */     }
/*      */     
/*      */     private void nextReader() throws IOException {
/*  379 */       close();
/*  380 */       this.reader = null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int read() throws IOException {
/*  392 */       while (getReader() != null) {
/*  393 */         int ch = getReader().read();
/*  394 */         if (ch == -1) {
/*  395 */           nextReader(); continue;
/*      */         } 
/*  397 */         return ch;
/*      */       } 
/*      */       
/*  400 */       return -1;
/*      */     }
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
/*      */     public int read(char[] cbuf, int off, int len) throws IOException {
/*  415 */       int amountRead = 0;
/*  416 */       while (getReader() != null) {
/*  417 */         int nRead = getReader().read(cbuf, off, len);
/*  418 */         if (nRead == -1 || nRead == 0) {
/*  419 */           nextReader(); continue;
/*      */         } 
/*  421 */         len -= nRead;
/*  422 */         off += nRead;
/*  423 */         amountRead += nRead;
/*  424 */         if (len == 0) {
/*  425 */           return amountRead;
/*      */         }
/*      */       } 
/*      */       
/*  429 */       if (amountRead == 0) {
/*  430 */         return -1;
/*      */       }
/*  432 */       return amountRead;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void close() throws IOException {
/*  440 */       if (this.reader != null) {
/*  441 */         this.reader.close();
/*      */       }
/*      */     }
/*      */     
/*      */     private boolean isFixLastLine() {
/*  446 */       return (Concat.this.fixLastLine && Concat.this.textBuffer == null);
/*      */     }
/*      */   }
/*      */   
/*      */   private final class ConcatResource extends Resource {
/*      */     private ResourceCollection c;
/*      */     
/*      */     private ConcatResource(ResourceCollection c) {
/*  454 */       this.c = c;
/*      */     } public InputStream getInputStream() {
/*      */       Reader resourceReader;
/*      */       Reader rdr;
/*  458 */       if (Concat.this.binary) {
/*  459 */         ConcatResourceInputStream result = new ConcatResourceInputStream(this.c);
/*  460 */         result.setManagingComponent((ProjectComponent)this);
/*  461 */         return (InputStream)result;
/*      */       } 
/*      */       
/*  464 */       if (Concat.this.filterBeforeConcat) {
/*      */         
/*  466 */         resourceReader = new Concat.MultiReader(this.c.iterator(), Concat.this.resourceReaderFactory, true);
/*      */       } else {
/*  468 */         resourceReader = Concat.this.getFilteredReader(new Concat.MultiReader(this.c
/*  469 */               .iterator(), Concat.this.resourceReaderFactory, false));
/*      */       } 
/*      */       
/*  472 */       if (Concat.this.header == null && Concat.this.footer == null) {
/*  473 */         rdr = resourceReader;
/*      */       } else {
/*  475 */         int readerCount = 1;
/*  476 */         if (Concat.this.header != null) {
/*  477 */           readerCount++;
/*      */         }
/*  479 */         if (Concat.this.footer != null) {
/*  480 */           readerCount++;
/*      */         }
/*  482 */         Reader[] readers = new Reader[readerCount];
/*  483 */         int pos = 0;
/*  484 */         if (Concat.this.header != null) {
/*  485 */           readers[pos] = new StringReader(Concat.this.header.getValue());
/*  486 */           if (Concat.this.header.getFiltering()) {
/*  487 */             readers[pos] = Concat.this.getFilteredReader(readers[pos]);
/*      */           }
/*  489 */           pos++;
/*      */         } 
/*  491 */         readers[pos++] = resourceReader;
/*  492 */         if (Concat.this.footer != null) {
/*  493 */           readers[pos] = new StringReader(Concat.this.footer.getValue());
/*  494 */           if (Concat.this.footer.getFiltering()) {
/*  495 */             readers[pos] = Concat.this.getFilteredReader(readers[pos]);
/*      */           }
/*      */         } 
/*      */         
/*  499 */         rdr = new Concat.MultiReader(Arrays.<Reader>asList(readers).iterator(), Concat.this.identityReaderFactory, false);
/*      */       } 
/*  501 */       return (Concat.this.outputEncoding == null) ? (InputStream)new ReaderInputStream(rdr) : 
/*  502 */         (InputStream)new ReaderInputStream(rdr, Concat.this.outputEncoding);
/*      */     }
/*      */     
/*      */     public String getName() {
/*  506 */       return (Concat.this.resourceName == null) ? (
/*  507 */         "concat (" + this.c + ")") : Concat.this.resourceName;
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
/*      */   private boolean forceOverwrite = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean force = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private TextElement footer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private TextElement header;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean fixLastLine = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String eolString;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  568 */   private Writer outputWriter = null;
/*      */   
/*      */   private boolean ignoreEmpty = true;
/*      */   
/*      */   private String resourceName;
/*      */ 
/*      */   
/*  575 */   private ReaderFactory<Resource> resourceReaderFactory = new ReaderFactory<Resource>()
/*      */     {
/*      */       public Reader getReader(Resource o) throws IOException {
/*  578 */         InputStream is = o.getInputStream();
/*  579 */         return new BufferedReader((Concat.this.encoding == null) ? 
/*  580 */             new InputStreamReader(is) : 
/*  581 */             new InputStreamReader(is, Concat.this.encoding));
/*      */       }
/*      */     }; private ReaderFactory<Reader> identityReaderFactory;
/*      */   public Concat() {
/*  585 */     this.identityReaderFactory = (o -> o);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  591 */     reset();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void reset() {
/*  598 */     this.append = false;
/*  599 */     this.forceOverwrite = true;
/*  600 */     this.dest = null;
/*  601 */     this.encoding = null;
/*  602 */     this.outputEncoding = null;
/*  603 */     this.fixLastLine = false;
/*  604 */     this.filterChains = null;
/*  605 */     this.footer = null;
/*  606 */     this.header = null;
/*  607 */     this.binary = false;
/*  608 */     this.outputWriter = null;
/*  609 */     this.textBuffer = null;
/*  610 */     this.eolString = System.lineSeparator();
/*  611 */     this.rc = null;
/*  612 */     this.ignoreEmpty = true;
/*  613 */     this.force = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDestfile(File destinationFile) {
/*  623 */     setDest((Resource)new FileResource(destinationFile));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDest(Resource dest) {
/*  632 */     this.dest = dest;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAppend(boolean append) {
/*  643 */     this.append = append;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEncoding(String encoding) {
/*  652 */     this.encoding = encoding;
/*  653 */     if (this.outputEncoding == null) {
/*  654 */       this.outputEncoding = encoding;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOutputEncoding(String outputEncoding) {
/*  664 */     this.outputEncoding = outputEncoding;
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
/*      */   @Deprecated
/*      */   public void setForce(boolean forceOverwrite) {
/*  677 */     this.forceOverwrite = forceOverwrite;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOverwrite(boolean forceOverwrite) {
/*  688 */     setForce(forceOverwrite);
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
/*      */   public void setForceReadOnly(boolean f) {
/*  700 */     this.force = f;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIgnoreEmpty(boolean ignoreEmpty) {
/*  711 */     this.ignoreEmpty = ignoreEmpty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResourceName(String resourceName) {
/*  720 */     this.resourceName = resourceName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createPath() {
/*  731 */     Path path = new Path(getProject());
/*  732 */     add((ResourceCollection)path);
/*  733 */     return path;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFileset(FileSet set) {
/*  741 */     add((ResourceCollection)set);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFilelist(FileList list) {
/*  749 */     add((ResourceCollection)list);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(ResourceCollection c) {
/*  758 */     synchronized (this) {
/*  759 */       if (this.rc == null) {
/*  760 */         this.rc = new Resources();
/*  761 */         this.rc.setProject(getProject());
/*  762 */         this.rc.setCache(true);
/*      */       } 
/*      */     } 
/*  765 */     this.rc.add(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFilterChain(FilterChain filterChain) {
/*  774 */     if (this.filterChains == null) {
/*  775 */       this.filterChains = new Vector<>();
/*      */     }
/*  777 */     this.filterChains.addElement(filterChain);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addText(String text) {
/*  785 */     if (this.textBuffer == null)
/*      */     {
/*      */       
/*  788 */       this.textBuffer = new StringBuffer(text.length());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  793 */     this.textBuffer.append(text);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addHeader(TextElement headerToAdd) {
/*  802 */     this.header = headerToAdd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFooter(TextElement footerToAdd) {
/*  811 */     this.footer = footerToAdd;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFixLastLine(boolean fixLastLine) {
/*  822 */     this.fixLastLine = fixLastLine;
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
/*      */   public void setEol(FixCRLF.CrLf crlf) {
/*  834 */     String s = crlf.getValue();
/*  835 */     if ("cr".equals(s) || "mac".equals(s)) {
/*  836 */       this.eolString = "\r";
/*  837 */     } else if ("lf".equals(s) || "unix".equals(s)) {
/*  838 */       this.eolString = "\n";
/*  839 */     } else if ("crlf".equals(s) || "dos".equals(s)) {
/*  840 */       this.eolString = "\r\n";
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWriter(Writer outputWriter) {
/*  851 */     this.outputWriter = outputWriter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBinary(boolean binary) {
/*  862 */     this.binary = binary;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFilterBeforeConcat(boolean filterBeforeConcat) {
/*  873 */     this.filterBeforeConcat = filterBeforeConcat;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void execute() {
/*  881 */     validate();
/*  882 */     if (this.binary && this.dest == null) {
/*  883 */       throw new BuildException("dest|destfile attribute is required for binary concatenation");
/*      */     }
/*      */     
/*  886 */     ResourceCollection c = getResources();
/*  887 */     if (isUpToDate(c)) {
/*  888 */       log(this.dest + " is up-to-date.", 3);
/*      */       return;
/*      */     } 
/*  891 */     if (c.isEmpty() && this.ignoreEmpty) {
/*      */       return;
/*      */     }
/*      */     
/*      */     try {
/*  896 */       ResourceUtils.copyResource(new ConcatResource(c), (this.dest == null) ? 
/*  897 */           (Resource)new LogOutputResource((ProjectComponent)this, 1) : 
/*  898 */           this.dest, null, null, true, false, this.append, null, null, 
/*      */           
/*  900 */           getProject(), this.force);
/*  901 */     } catch (IOException e) {
/*  902 */       throw new BuildException("error concatenating content to " + this.dest, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator<Resource> iterator() {
/*  912 */     validate();
/*  913 */     return 
/*  914 */       Collections.<Resource>singletonList(new ConcatResource(getResources()))
/*  915 */       .iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  924 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFilesystemOnly() {
/*  933 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void validate() {
/*  942 */     sanitizeText();
/*      */ 
/*      */     
/*  945 */     if (this.binary) {
/*  946 */       if (this.textBuffer != null) {
/*  947 */         throw new BuildException("Nested text is incompatible with binary concatenation");
/*      */       }
/*      */       
/*  950 */       if (this.encoding != null || this.outputEncoding != null) {
/*  951 */         throw new BuildException("Setting input or output encoding is incompatible with binary concatenation");
/*      */       }
/*      */       
/*  954 */       if (this.filterChains != null) {
/*  955 */         throw new BuildException("Setting filters is incompatible with binary concatenation");
/*      */       }
/*      */       
/*  958 */       if (this.fixLastLine) {
/*  959 */         throw new BuildException("Setting fixlastline is incompatible with binary concatenation");
/*      */       }
/*      */       
/*  962 */       if (this.header != null || this.footer != null) {
/*  963 */         throw new BuildException("Nested header or footer is incompatible with binary concatenation");
/*      */       }
/*      */     } 
/*      */     
/*  967 */     if (this.dest != null && this.outputWriter != null) {
/*  968 */       throw new BuildException("Cannot specify both a destination resource and an output writer");
/*      */     }
/*      */ 
/*      */     
/*  972 */     if (this.rc == null && this.textBuffer == null)
/*      */     {
/*  974 */       throw new BuildException("At least one resource must be provided, or some text.");
/*      */     }
/*      */     
/*  977 */     if (this.rc != null && this.textBuffer != null)
/*      */     {
/*      */       
/*  980 */       throw new BuildException("Cannot include inline text when using resources.");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResourceCollection getResources() {
/*  989 */     if (this.rc == null) {
/*  990 */       return (ResourceCollection)new StringResource(getProject(), this.textBuffer.toString());
/*      */     }
/*  992 */     if (this.dest != null) {
/*  993 */       Intersect checkDestNotInSources = new Intersect();
/*  994 */       checkDestNotInSources.setProject(getProject());
/*  995 */       checkDestNotInSources.add((ResourceCollection)this.rc);
/*  996 */       checkDestNotInSources.add((ResourceCollection)this.dest);
/*  997 */       if (checkDestNotInSources.size() > 0) {
/*  998 */         throw new BuildException("Destination resource %s was specified as an input resource.", new Object[] { this.dest });
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1003 */     Restrict noexistRc = new Restrict();
/* 1004 */     noexistRc.add(NOT_EXISTS);
/* 1005 */     noexistRc.add((ResourceCollection)this.rc);
/* 1006 */     for (Resource r : noexistRc) {
/* 1007 */       log(r + " does not exist.", 0);
/*      */     }
/* 1009 */     Restrict result = new Restrict();
/* 1010 */     result.add(EXISTS);
/* 1011 */     result.add((ResourceCollection)this.rc);
/* 1012 */     return (ResourceCollection)result;
/*      */   }
/*      */   
/*      */   private boolean isUpToDate(ResourceCollection c) {
/* 1016 */     return (this.dest != null && !this.forceOverwrite && c
/* 1017 */       .stream().noneMatch(r -> SelectorUtils.isOutOfDate(r, this.dest, FILE_UTILS.getFileTimestampGranularity())));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void sanitizeText() {
/* 1027 */     if (this.textBuffer != null && this.textBuffer.toString().trim().isEmpty()) {
/* 1028 */       this.textBuffer = null;
/*      */     }
/*      */   }
/*      */   
/*      */   private Reader getFilteredReader(Reader r) {
/* 1033 */     if (this.filterChains == null) {
/* 1034 */       return r;
/*      */     }
/* 1036 */     ChainReaderHelper helper = new ChainReaderHelper();
/* 1037 */     helper.setBufferSize(8192);
/* 1038 */     helper.setPrimaryReader(r);
/* 1039 */     helper.setFilterChains(this.filterChains);
/* 1040 */     helper.setProject(getProject());
/*      */     
/* 1042 */     return (Reader)helper.getAssembledReader();
/*      */   }
/*      */   
/*      */   private static interface ReaderFactory<S> {
/*      */     Reader getReader(S param1S) throws IOException;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Concat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */