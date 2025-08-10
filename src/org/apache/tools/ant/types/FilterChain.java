/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.filters.ChainableReader;
/*     */ import org.apache.tools.ant.filters.ClassConstants;
/*     */ import org.apache.tools.ant.filters.EscapeUnicode;
/*     */ import org.apache.tools.ant.filters.ExpandProperties;
/*     */ import org.apache.tools.ant.filters.HeadFilter;
/*     */ import org.apache.tools.ant.filters.LineContains;
/*     */ import org.apache.tools.ant.filters.LineContainsRegExp;
/*     */ import org.apache.tools.ant.filters.PrefixLines;
/*     */ import org.apache.tools.ant.filters.ReplaceTokens;
/*     */ import org.apache.tools.ant.filters.StripJavaComments;
/*     */ import org.apache.tools.ant.filters.StripLineBreaks;
/*     */ import org.apache.tools.ant.filters.StripLineComments;
/*     */ import org.apache.tools.ant.filters.SuffixLines;
/*     */ import org.apache.tools.ant.filters.TabsToSpaces;
/*     */ import org.apache.tools.ant.filters.TailFilter;
/*     */ import org.apache.tools.ant.filters.TokenFilter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FilterChain
/*     */   extends DataType
/*     */ {
/*  49 */   private Vector<Object> filterReaders = new Vector();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilterReader(AntFilterReader filterReader) {
/*  57 */     if (isReference()) {
/*  58 */       throw noChildrenAllowed();
/*     */     }
/*  60 */     setChecked(false);
/*  61 */     this.filterReaders.addElement(filterReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector<Object> getFilterReaders() {
/*  70 */     if (isReference()) {
/*  71 */       return getRef().getFilterReaders();
/*     */     }
/*  73 */     dieOnCircularReference();
/*  74 */     return this.filterReaders;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClassConstants(ClassConstants classConstants) {
/*  83 */     if (isReference()) {
/*  84 */       throw noChildrenAllowed();
/*     */     }
/*  86 */     setChecked(false);
/*  87 */     this.filterReaders.addElement(classConstants);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExpandProperties(ExpandProperties expandProperties) {
/*  96 */     if (isReference()) {
/*  97 */       throw noChildrenAllowed();
/*     */     }
/*  99 */     setChecked(false);
/* 100 */     this.filterReaders.addElement(expandProperties);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addHeadFilter(HeadFilter headFilter) {
/* 109 */     if (isReference()) {
/* 110 */       throw noChildrenAllowed();
/*     */     }
/* 112 */     setChecked(false);
/* 113 */     this.filterReaders.addElement(headFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLineContains(LineContains lineContains) {
/* 122 */     if (isReference()) {
/* 123 */       throw noChildrenAllowed();
/*     */     }
/* 125 */     setChecked(false);
/* 126 */     this.filterReaders.addElement(lineContains);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLineContainsRegExp(LineContainsRegExp lineContainsRegExp) {
/* 136 */     if (isReference()) {
/* 137 */       throw noChildrenAllowed();
/*     */     }
/* 139 */     setChecked(false);
/* 140 */     this.filterReaders.addElement(lineContainsRegExp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPrefixLines(PrefixLines prefixLines) {
/* 149 */     if (isReference()) {
/* 150 */       throw noChildrenAllowed();
/*     */     }
/* 152 */     setChecked(false);
/* 153 */     this.filterReaders.addElement(prefixLines);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSuffixLines(SuffixLines suffixLines) {
/* 163 */     if (isReference()) {
/* 164 */       throw noChildrenAllowed();
/*     */     }
/* 166 */     setChecked(false);
/* 167 */     this.filterReaders.addElement(suffixLines);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReplaceTokens(ReplaceTokens replaceTokens) {
/* 176 */     if (isReference()) {
/* 177 */       throw noChildrenAllowed();
/*     */     }
/* 179 */     setChecked(false);
/* 180 */     this.filterReaders.addElement(replaceTokens);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addStripJavaComments(StripJavaComments stripJavaComments) {
/* 190 */     if (isReference()) {
/* 191 */       throw noChildrenAllowed();
/*     */     }
/* 193 */     setChecked(false);
/* 194 */     this.filterReaders.addElement(stripJavaComments);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addStripLineBreaks(StripLineBreaks stripLineBreaks) {
/* 204 */     if (isReference()) {
/* 205 */       throw noChildrenAllowed();
/*     */     }
/* 207 */     setChecked(false);
/* 208 */     this.filterReaders.addElement(stripLineBreaks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addStripLineComments(StripLineComments stripLineComments) {
/* 218 */     if (isReference()) {
/* 219 */       throw noChildrenAllowed();
/*     */     }
/* 221 */     setChecked(false);
/* 222 */     this.filterReaders.addElement(stripLineComments);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTabsToSpaces(TabsToSpaces tabsToSpaces) {
/* 231 */     if (isReference()) {
/* 232 */       throw noChildrenAllowed();
/*     */     }
/* 234 */     setChecked(false);
/* 235 */     this.filterReaders.addElement(tabsToSpaces);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTailFilter(TailFilter tailFilter) {
/* 244 */     if (isReference()) {
/* 245 */       throw noChildrenAllowed();
/*     */     }
/* 247 */     setChecked(false);
/* 248 */     this.filterReaders.addElement(tailFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addEscapeUnicode(EscapeUnicode escapeUnicode) {
/* 258 */     if (isReference()) {
/* 259 */       throw noChildrenAllowed();
/*     */     }
/* 261 */     setChecked(false);
/* 262 */     this.filterReaders.addElement(escapeUnicode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTokenFilter(TokenFilter tokenFilter) {
/* 272 */     if (isReference()) {
/* 273 */       throw noChildrenAllowed();
/*     */     }
/* 275 */     setChecked(false);
/* 276 */     this.filterReaders.addElement(tokenFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDeleteCharacters(TokenFilter.DeleteCharacters filter) {
/* 286 */     if (isReference()) {
/* 287 */       throw noChildrenAllowed();
/*     */     }
/* 289 */     setChecked(false);
/* 290 */     this.filterReaders.addElement(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContainsRegex(TokenFilter.ContainsRegex filter) {
/* 300 */     if (isReference()) {
/* 301 */       throw noChildrenAllowed();
/*     */     }
/* 303 */     setChecked(false);
/* 304 */     this.filterReaders.addElement(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReplaceRegex(TokenFilter.ReplaceRegex filter) {
/* 313 */     if (isReference()) {
/* 314 */       throw noChildrenAllowed();
/*     */     }
/* 316 */     setChecked(false);
/* 317 */     this.filterReaders.addElement(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTrim(TokenFilter.Trim filter) {
/* 327 */     if (isReference()) {
/* 328 */       throw noChildrenAllowed();
/*     */     }
/* 330 */     setChecked(false);
/* 331 */     this.filterReaders.addElement(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReplaceString(TokenFilter.ReplaceString filter) {
/* 342 */     if (isReference()) {
/* 343 */       throw noChildrenAllowed();
/*     */     }
/* 345 */     setChecked(false);
/* 346 */     this.filterReaders.addElement(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addIgnoreBlank(TokenFilter.IgnoreBlank filter) {
/* 357 */     if (isReference()) {
/* 358 */       throw noChildrenAllowed();
/*     */     }
/* 360 */     setChecked(false);
/* 361 */     this.filterReaders.addElement(filter);
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
/*     */   public void setRefid(Reference r) throws BuildException {
/* 377 */     if (!this.filterReaders.isEmpty()) {
/* 378 */       throw tooManyAttributes();
/*     */     }
/* 380 */     super.setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(ChainableReader filter) {
/* 391 */     if (isReference()) {
/* 392 */       throw noChildrenAllowed();
/*     */     }
/* 394 */     setChecked(false);
/* 395 */     this.filterReaders.addElement(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 401 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 404 */     if (isReference()) {
/* 405 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 407 */       for (Object o : this.filterReaders) {
/* 408 */         if (o instanceof DataType) {
/* 409 */           pushAndInvokeCircularReferenceCheck((DataType)o, stk, p);
/*     */         }
/*     */       } 
/* 412 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private FilterChain getRef() {
/* 417 */     return getCheckedRef(FilterChain.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/FilterChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */