/*      */ package org.apache.tools.ant.filters;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Reader;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.taskdefs.condition.Os;
/*      */ import org.apache.tools.ant.types.EnumeratedAttribute;
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
/*      */ public final class FixCrLfFilter
/*      */   extends BaseParamFilterReader
/*      */   implements ChainableReader
/*      */ {
/*      */   private static final int DEFAULT_TAB_LENGTH = 8;
/*      */   private static final int MIN_TAB_LENGTH = 2;
/*      */   private static final int MAX_TAB_LENGTH = 80;
/*      */   private static final char CTRLZ = '\032';
/*   78 */   private int tabLength = 8;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CrLf eol;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AddAsisRemove ctrlz;
/*      */ 
/*      */ 
/*      */   
/*      */   private AddAsisRemove tabs;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean javafiles = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean fixlast = true;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean initialized = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FixCrLfFilter(Reader in) throws IOException {
/*  110 */     super(in);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  116 */     this.tabs = AddAsisRemove.ASIS;
/*  117 */     if (Os.isFamily("mac") && !Os.isFamily("unix"))
/*  118 */     { this.ctrlz = AddAsisRemove.REMOVE;
/*  119 */       setEol(CrLf.MAC); }
/*  120 */     else if (Os.isFamily("dos"))
/*  121 */     { this.ctrlz = AddAsisRemove.ASIS;
/*  122 */       setEol(CrLf.DOS); }
/*      */     else
/*  124 */     { this.ctrlz = AddAsisRemove.REMOVE;
/*  125 */       setEol(CrLf.UNIX); }  } public FixCrLfFilter() { this.tabs = AddAsisRemove.ASIS; if (Os.isFamily("mac") && !Os.isFamily("unix")) { this.ctrlz = AddAsisRemove.REMOVE; setEol(CrLf.MAC); } else if (Os.isFamily("dos")) { this.ctrlz = AddAsisRemove.ASIS; setEol(CrLf.DOS); } else { this.ctrlz = AddAsisRemove.REMOVE; setEol(CrLf.UNIX); }
/*      */      }
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
/*      */   public Reader chain(Reader rdr) {
/*      */     try {
/*  141 */       FixCrLfFilter newFilter = new FixCrLfFilter(rdr);
/*      */       
/*  143 */       newFilter.setJavafiles(getJavafiles());
/*  144 */       newFilter.setEol(getEol());
/*  145 */       newFilter.setTab(getTab());
/*  146 */       newFilter.setTablength(getTablength());
/*  147 */       newFilter.setEof(getEof());
/*  148 */       newFilter.setFixlast(getFixlast());
/*  149 */       newFilter.initInternalFilters();
/*      */       
/*  151 */       return newFilter;
/*  152 */     } catch (IOException e) {
/*  153 */       throw new BuildException(e);
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
/*      */   public AddAsisRemove getEof() {
/*  170 */     return this.ctrlz.newInstance();
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
/*      */   public CrLf getEol() {
/*  187 */     return this.eol.newInstance();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getFixlast() {
/*  196 */     return this.fixlast;
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
/*      */   public boolean getJavafiles() {
/*  211 */     return this.javafiles;
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
/*      */   public AddAsisRemove getTab() {
/*  228 */     return this.tabs.newInstance();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTablength() {
/*  237 */     return this.tabLength;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String calculateEolString(CrLf eol) {
/*  242 */     if (eol == CrLf.CR || eol == CrLf.MAC) {
/*  243 */       return "\r";
/*      */     }
/*  245 */     if (eol == CrLf.CRLF || eol == CrLf.DOS) {
/*  246 */       return "\r\n";
/*      */     }
/*      */     
/*  249 */     return "\n";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initInternalFilters() {
/*  260 */     this.in = (this.ctrlz == AddAsisRemove.REMOVE) ? new RemoveEofFilter(this.in) : this.in;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  265 */     if (this.eol != CrLf.ASIS) {
/*  266 */       this.in = new NormalizeEolFilter(this.in, calculateEolString(this.eol), getFixlast());
/*      */     }
/*      */     
/*  269 */     if (this.tabs != AddAsisRemove.ASIS) {
/*      */ 
/*      */       
/*  272 */       if (getJavafiles()) {
/*  273 */         this.in = new MaskJavaTabLiteralsFilter(this.in);
/*      */       }
/*      */       
/*  276 */       this
/*  277 */         .in = (this.tabs == AddAsisRemove.ADD) ? new AddTabFilter(this.in, getTablength()) : new RemoveTabFilter(this.in, getTablength());
/*      */     } 
/*      */     
/*  280 */     this.in = (this.ctrlz == AddAsisRemove.ADD) ? new AddEofFilter(this.in) : this.in;
/*  281 */     this.initialized = true;
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
/*      */   public synchronized int read() throws IOException {
/*  295 */     if (!this.initialized) {
/*  296 */       initInternalFilters();
/*      */     }
/*  298 */     return this.in.read();
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
/*      */   public void setEof(AddAsisRemove attr) {
/*  313 */     this.ctrlz = attr.resolve();
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
/*      */   public void setEol(CrLf attr) {
/*  329 */     this.eol = attr.resolve();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFixlast(boolean fixlast) {
/*  339 */     this.fixlast = fixlast;
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
/*      */   public void setJavafiles(boolean javafiles) {
/*  353 */     this.javafiles = javafiles;
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
/*      */   public void setTab(AddAsisRemove attr) {
/*  369 */     this.tabs = attr.resolve();
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
/*      */   public void setTablength(int tabLength) throws IOException {
/*  381 */     if (tabLength < 2 || tabLength > 80)
/*      */     {
/*  383 */       throw new IOException("tablength must be between 2 and 80");
/*      */     }
/*      */ 
/*      */     
/*  387 */     this.tabLength = tabLength;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class SimpleFilterReader
/*      */     extends Reader
/*      */   {
/*      */     private static final int PREEMPT_BUFFER_LENGTH = 16;
/*      */ 
/*      */ 
/*      */     
/*      */     private Reader in;
/*      */ 
/*      */ 
/*      */     
/*  404 */     private int[] preempt = new int[16];
/*      */     
/*  406 */     private int preemptIndex = 0;
/*      */     
/*      */     public SimpleFilterReader(Reader in) {
/*  409 */       this.in = in;
/*      */     }
/*      */     
/*      */     public void push(char c) {
/*  413 */       push(c);
/*      */     }
/*      */     
/*      */     public void push(int c) {
/*      */       try {
/*  418 */         this.preempt[this.preemptIndex++] = c;
/*  419 */       } catch (ArrayIndexOutOfBoundsException e) {
/*  420 */         int[] p2 = new int[this.preempt.length * 2];
/*  421 */         System.arraycopy(this.preempt, 0, p2, 0, this.preempt.length);
/*  422 */         this.preempt = p2;
/*  423 */         push(c);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void push(char[] cs, int start, int length) {
/*  428 */       for (int i = start + length - 1; i >= start;) {
/*  429 */         push(cs[i--]);
/*      */       }
/*      */     }
/*      */     
/*      */     public void push(char[] cs) {
/*  434 */       push(cs, 0, cs.length);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean editsBlocked() {
/*  442 */       return (this.in instanceof SimpleFilterReader && ((SimpleFilterReader)this.in).editsBlocked());
/*      */     }
/*      */     
/*      */     public int read() throws IOException {
/*  446 */       return (this.preemptIndex > 0) ? this.preempt[--this.preemptIndex] : this.in.read();
/*      */     }
/*      */     
/*      */     public void close() throws IOException {
/*  450 */       this.in.close();
/*      */     }
/*      */     
/*      */     public void reset() throws IOException {
/*  454 */       this.in.reset();
/*      */     }
/*      */     
/*      */     public boolean markSupported() {
/*  458 */       return this.in.markSupported();
/*      */     }
/*      */     
/*      */     public boolean ready() throws IOException {
/*  462 */       return this.in.ready();
/*      */     }
/*      */     
/*      */     public void mark(int i) throws IOException {
/*  466 */       this.in.mark(i);
/*      */     }
/*      */     
/*      */     public long skip(long i) throws IOException {
/*  470 */       return this.in.skip(i);
/*      */     }
/*      */     
/*      */     public int read(char[] buf) throws IOException {
/*  474 */       return read(buf, 0, buf.length);
/*      */     }
/*      */     
/*      */     public int read(char[] buf, int start, int length) throws IOException {
/*  478 */       int count = 0;
/*  479 */       int c = 0;
/*      */ 
/*      */       
/*  482 */       while (length-- > 0 && (c = read()) != -1) {
/*  483 */         buf[start++] = (char)c;
/*  484 */         count++;
/*      */       } 
/*      */       
/*  487 */       return (count == 0 && c == -1) ? -1 : count;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class MaskJavaTabLiteralsFilter
/*      */     extends SimpleFilterReader
/*      */   {
/*      */     private boolean editsBlocked = false;
/*      */     
/*      */     private static final int JAVA = 1;
/*      */     
/*      */     private static final int IN_CHAR_CONST = 2;
/*      */     
/*      */     private static final int IN_STR_CONST = 3;
/*      */     
/*      */     private static final int IN_SINGLE_COMMENT = 4;
/*      */     
/*      */     private static final int IN_MULTI_COMMENT = 5;
/*      */     private static final int TRANS_TO_COMMENT = 6;
/*      */     private static final int TRANS_FROM_MULTI = 8;
/*      */     private int state;
/*      */     
/*      */     public MaskJavaTabLiteralsFilter(Reader in) {
/*  511 */       super(in);
/*  512 */       this.state = 1;
/*      */     }
/*      */     
/*      */     public boolean editsBlocked() {
/*  516 */       return (this.editsBlocked || super.editsBlocked());
/*      */     }
/*      */     
/*      */     public int read() throws IOException {
/*  520 */       int thisChar = super.read();
/*      */       
/*  522 */       this.editsBlocked = (this.state == 2 || this.state == 3);
/*      */       
/*  524 */       switch (this.state) {
/*      */         
/*      */         case 1:
/*  527 */           switch (thisChar) {
/*      */             case 39:
/*  529 */               this.state = 2;
/*      */               break;
/*      */             case 34:
/*  532 */               this.state = 3;
/*      */               break;
/*      */             case 47:
/*  535 */               this.state = 6;
/*      */               break;
/*      */           } 
/*      */           
/*      */           break;
/*      */         
/*      */         case 2:
/*  542 */           switch (thisChar) {
/*      */             case 39:
/*  544 */               this.state = 1;
/*      */               break;
/*      */           } 
/*      */           
/*      */           break;
/*      */         
/*      */         case 3:
/*  551 */           switch (thisChar) {
/*      */             case 34:
/*  553 */               this.state = 1;
/*      */               break;
/*      */           } 
/*      */ 
/*      */           
/*      */           break;
/*      */         
/*      */         case 4:
/*  561 */           switch (thisChar) {
/*      */             case 10:
/*      */             case 13:
/*  564 */               this.state = 1;
/*      */               break;
/*      */           } 
/*      */ 
/*      */           
/*      */           break;
/*      */         
/*      */         case 5:
/*  572 */           switch (thisChar) {
/*      */             case 42:
/*  574 */               this.state = 8;
/*      */               break;
/*      */           } 
/*      */ 
/*      */           
/*      */           break;
/*      */         
/*      */         case 6:
/*  582 */           switch (thisChar) {
/*      */             case 42:
/*  584 */               this.state = 5;
/*      */               break;
/*      */             case 47:
/*  587 */               this.state = 4;
/*      */               break;
/*      */             case 39:
/*  590 */               this.state = 2;
/*      */               break;
/*      */             case 34:
/*  593 */               this.state = 3;
/*      */               break;
/*      */           } 
/*  596 */           this.state = 1;
/*      */           break;
/*      */ 
/*      */         
/*      */         case 8:
/*  601 */           switch (thisChar) {
/*      */             case 47:
/*  603 */               this.state = 1;
/*      */               break;
/*      */           } 
/*      */ 
/*      */           
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/*  612 */       return thisChar;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class NormalizeEolFilter
/*      */     extends SimpleFilterReader
/*      */   {
/*      */     private boolean previousWasEOL;
/*      */     private boolean fixLast;
/*  621 */     private int normalizedEOL = 0;
/*      */     
/*  623 */     private char[] eol = null;
/*      */     
/*      */     public NormalizeEolFilter(Reader in, String eolString, boolean fixLast) {
/*  626 */       super(in);
/*  627 */       this.eol = eolString.toCharArray();
/*  628 */       this.fixLast = fixLast;
/*      */     }
/*      */     
/*      */     public int read() throws IOException {
/*  632 */       int thisChar = super.read();
/*      */       
/*  634 */       if (this.normalizedEOL == 0) {
/*  635 */         int c, c1, c2, numEOL = 0;
/*  636 */         boolean atEnd = false;
/*  637 */         switch (thisChar) {
/*      */           case 26:
/*  639 */             c = super.read();
/*  640 */             if (c == -1) {
/*  641 */               atEnd = true;
/*  642 */               if (this.fixLast && !this.previousWasEOL) {
/*  643 */                 numEOL = 1;
/*  644 */                 push(thisChar);
/*      */               }  break;
/*      */             } 
/*  647 */             push(c);
/*      */             break;
/*      */           
/*      */           case -1:
/*  651 */             atEnd = true;
/*  652 */             if (this.fixLast && !this.previousWasEOL) {
/*  653 */               numEOL = 1;
/*      */             }
/*      */             break;
/*      */           
/*      */           case 10:
/*  658 */             numEOL = 1;
/*      */             break;
/*      */           case 13:
/*  661 */             numEOL = 1;
/*  662 */             c1 = super.read();
/*  663 */             c2 = super.read();
/*      */             
/*  665 */             if (c1 == 13 && c2 == 10)
/*      */               break; 
/*  667 */             if (c1 == 13) {
/*      */ 
/*      */               
/*  670 */               numEOL = 2;
/*  671 */               push(c2); break;
/*  672 */             }  if (c1 == 10) {
/*      */               
/*  674 */               push(c2);
/*      */               break;
/*      */             } 
/*  677 */             push(c2);
/*  678 */             push(c1);
/*      */             break;
/*      */         } 
/*      */ 
/*      */         
/*  683 */         if (numEOL > 0) {
/*  684 */           while (numEOL-- > 0) {
/*  685 */             push(this.eol);
/*  686 */             this.normalizedEOL += this.eol.length;
/*      */           } 
/*  688 */           this.previousWasEOL = true;
/*  689 */           thisChar = read();
/*  690 */         } else if (!atEnd) {
/*  691 */           this.previousWasEOL = false;
/*      */         } 
/*      */       } else {
/*  694 */         this.normalizedEOL--;
/*      */       } 
/*  696 */       return thisChar;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class AddEofFilter extends SimpleFilterReader {
/*  701 */     private int lastChar = -1;
/*      */     
/*      */     public AddEofFilter(Reader in) {
/*  704 */       super(in);
/*      */     }
/*      */     
/*      */     public int read() throws IOException {
/*  708 */       int thisChar = super.read();
/*      */ 
/*      */       
/*  711 */       if (thisChar == -1) {
/*  712 */         if (this.lastChar != 26) {
/*  713 */           this.lastChar = 26;
/*  714 */           return this.lastChar;
/*      */         } 
/*      */       } else {
/*  717 */         this.lastChar = thisChar;
/*      */       } 
/*  719 */       return thisChar;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RemoveEofFilter extends SimpleFilterReader {
/*  724 */     private int lookAhead = -1;
/*      */     
/*      */     public RemoveEofFilter(Reader in) {
/*  727 */       super(in);
/*      */       
/*      */       try {
/*  730 */         this.lookAhead = in.read();
/*  731 */       } catch (IOException e) {
/*  732 */         this.lookAhead = -1;
/*      */       } 
/*      */     }
/*      */     
/*      */     public int read() throws IOException {
/*  737 */       int lookAhead2 = super.read();
/*      */ 
/*      */       
/*  740 */       if (lookAhead2 == -1 && this.lookAhead == 26) {
/*  741 */         return -1;
/*      */       }
/*      */       
/*  744 */       int i = this.lookAhead;
/*  745 */       this.lookAhead = lookAhead2;
/*  746 */       return i;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class AddTabFilter extends SimpleFilterReader {
/*  751 */     private int columnNumber = 0;
/*      */     
/*  753 */     private int tabLength = 0;
/*      */     
/*      */     public AddTabFilter(Reader in, int tabLength) {
/*  756 */       super(in);
/*  757 */       this.tabLength = tabLength;
/*      */     }
/*      */     
/*      */     public int read() throws IOException {
/*  761 */       int countSpaces, numTabs, c = super.read();
/*      */       
/*  763 */       switch (c)
/*      */       { case 10:
/*      */         case 13:
/*  766 */           this.columnNumber = 0;
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
/*  824 */           return c;case 32: this.columnNumber++; if (!editsBlocked()) { int colNextTab = (this.columnNumber + this.tabLength - 1) / this.tabLength * this.tabLength; countSpaces = 1; numTabs = 0; while (true) { if ((c = super.read()) != -1) { switch (c) { case 32: if (++this.columnNumber == colNextTab) { numTabs++; countSpaces = 0; colNextTab += this.tabLength; continue; }  countSpaces++; continue;case 9: this.columnNumber = colNextTab; numTabs++; countSpaces = 0; colNextTab += this.tabLength; continue; }  push(c); } else { break; }  while (countSpaces-- > 0) { push(' '); this.columnNumber--; }  while (numTabs-- > 0) { push('\t'); this.columnNumber -= this.tabLength; }  c = super.read(); switch (c) { case 32: this.columnNumber++; break;case 9: this.columnNumber += this.tabLength; break; }  return c; }  } else { return c; }  while (countSpaces-- > 0) { push(' '); this.columnNumber--; }  while (numTabs-- > 0) { push('\t'); this.columnNumber -= this.tabLength; }  c = super.read(); switch (c) { case 32: this.columnNumber++; break;case 9: this.columnNumber += this.tabLength; break; }  return c;case 9: this.columnNumber = (this.columnNumber + this.tabLength - 1) / this.tabLength * this.tabLength; return c; }  this.columnNumber++; return c;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RemoveTabFilter extends SimpleFilterReader {
/*  829 */     private int columnNumber = 0;
/*      */     
/*  831 */     private int tabLength = 0;
/*      */     
/*      */     public RemoveTabFilter(Reader in, int tabLength) {
/*  834 */       super(in);
/*      */       
/*  836 */       this.tabLength = tabLength;
/*      */     }
/*      */     
/*      */     public int read() throws IOException {
/*  840 */       int width, c = super.read();
/*      */       
/*  842 */       switch (c)
/*      */       { case 10:
/*      */         case 13:
/*  845 */           this.columnNumber = 0;
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
/*  861 */           return c;case 9: width = this.tabLength - this.columnNumber % this.tabLength; if (!editsBlocked()) { for (; width > 1; width--) push(' ');  c = 32; }  this.columnNumber += width; return c; }  this.columnNumber++; return c;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class AddAsisRemove
/*      */     extends EnumeratedAttribute
/*      */   {
/*  869 */     private static final AddAsisRemove ASIS = newInstance("asis");
/*      */     
/*  871 */     private static final AddAsisRemove ADD = newInstance("add");
/*      */     
/*  873 */     private static final AddAsisRemove REMOVE = newInstance("remove");
/*      */ 
/*      */     
/*      */     public String[] getValues() {
/*  877 */       return new String[] { "add", "asis", "remove" };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/*  886 */       return (other instanceof AddAsisRemove && 
/*  887 */         getIndex() == ((AddAsisRemove)other).getIndex());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  895 */       return getIndex();
/*      */     }
/*      */     
/*      */     AddAsisRemove resolve() throws IllegalStateException {
/*  899 */       if (equals(ASIS)) {
/*  900 */         return ASIS;
/*      */       }
/*  902 */       if (equals(ADD)) {
/*  903 */         return ADD;
/*      */       }
/*  905 */       if (equals(REMOVE)) {
/*  906 */         return REMOVE;
/*      */       }
/*  908 */       throw new IllegalStateException("No replacement for " + this);
/*      */     }
/*      */ 
/*      */     
/*      */     private AddAsisRemove newInstance() {
/*  913 */       return newInstance(getValue());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static AddAsisRemove newInstance(String value) {
/*  922 */       AddAsisRemove a = new AddAsisRemove();
/*  923 */       a.setValue(value);
/*  924 */       return a;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class CrLf
/*      */     extends EnumeratedAttribute
/*      */   {
/*  932 */     private static final CrLf ASIS = newInstance("asis");
/*      */     
/*  934 */     private static final CrLf CR = newInstance("cr");
/*      */     
/*  936 */     private static final CrLf CRLF = newInstance("crlf");
/*      */     
/*  938 */     private static final CrLf DOS = newInstance("dos");
/*      */     
/*  940 */     private static final CrLf LF = newInstance("lf");
/*      */     
/*  942 */     private static final CrLf MAC = newInstance("mac");
/*      */     
/*  944 */     private static final CrLf UNIX = newInstance("unix");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String[] getValues() {
/*  951 */       return new String[] { "asis", "cr", "lf", "crlf", "mac", "unix", "dos" };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/*  960 */       return (other instanceof CrLf && getIndex() == ((CrLf)other).getIndex());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  968 */       return getIndex();
/*      */     }
/*      */     
/*      */     CrLf resolve() {
/*  972 */       if (equals(ASIS)) {
/*  973 */         return ASIS;
/*      */       }
/*  975 */       if (equals(CR) || equals(MAC)) {
/*  976 */         return CR;
/*      */       }
/*  978 */       if (equals(CRLF) || equals(DOS)) {
/*  979 */         return CRLF;
/*      */       }
/*  981 */       if (equals(LF) || equals(UNIX)) {
/*  982 */         return LF;
/*      */       }
/*  984 */       throw new IllegalStateException("No replacement for " + this);
/*      */     }
/*      */ 
/*      */     
/*      */     private CrLf newInstance() {
/*  989 */       return newInstance(getValue());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static CrLf newInstance(String value) {
/*  998 */       CrLf c = new CrLf();
/*  999 */       c.setValue(value);
/* 1000 */       return c;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/filters/FixCrLfFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */