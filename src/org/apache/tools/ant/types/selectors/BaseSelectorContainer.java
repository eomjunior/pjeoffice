/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Stack;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.DataType;
/*     */ import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseSelectorContainer
/*     */   extends BaseSelector
/*     */   implements SelectorContainer
/*     */ {
/*  42 */   private List<FileSelector> selectorsList = Collections.synchronizedList(new ArrayList<>());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSelectors() {
/*  49 */     dieOnCircularReference();
/*  50 */     return !this.selectorsList.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int selectorCount() {
/*  58 */     dieOnCircularReference();
/*  59 */     return this.selectorsList.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSelector[] getSelectors(Project p) {
/*  68 */     dieOnCircularReference();
/*  69 */     return this.selectorsList.<FileSelector>toArray(new FileSelector[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<FileSelector> selectorElements() {
/*  77 */     dieOnCircularReference();
/*  78 */     return Collections.enumeration(this.selectorsList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  89 */     dieOnCircularReference();
/*  90 */     return this.selectorsList.stream().map(Object::toString)
/*  91 */       .collect(Collectors.joining(", "));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendSelector(FileSelector selector) {
/* 100 */     this.selectorsList.add(selector);
/* 101 */     setChecked(false);
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
/*     */   public void validate() {
/* 121 */     verifySettings();
/* 122 */     dieOnCircularReference();
/* 123 */     String errmsg = getError();
/* 124 */     if (errmsg != null) {
/* 125 */       throw new BuildException(errmsg);
/*     */     }
/* 127 */     Objects.requireNonNull(BaseSelector.class);
/* 128 */     Objects.requireNonNull(BaseSelector.class); this.selectorsList.stream().filter(BaseSelector.class::isInstance).map(BaseSelector.class::cast).forEach(BaseSelector::validate);
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
/*     */   public abstract boolean isSelected(File paramFile1, String paramString, File paramFile2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSelector(SelectSelector selector) {
/* 151 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnd(AndSelector selector) {
/* 159 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOr(OrSelector selector) {
/* 167 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNot(NotSelector selector) {
/* 175 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNone(NoneSelector selector) {
/* 183 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMajority(MajoritySelector selector) {
/* 191 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDate(DateSelector selector) {
/* 199 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSize(SizeSelector selector) {
/* 207 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilename(FilenameSelector selector) {
/* 215 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCustom(ExtendSelector selector) {
/* 223 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContains(ContainsSelector selector) {
/* 231 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPresent(PresentSelector selector) {
/* 239 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDepth(DepthSelector selector) {
/* 247 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDepend(DependSelector selector) {
/* 255 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDifferent(DifferentSelector selector) {
/* 263 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addType(TypeSelector selector) {
/* 271 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContainsRegexp(ContainsRegexpSelector selector) {
/* 279 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addModified(ModifiedSelector selector) {
/* 288 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */   
/*     */   public void addReadable(ReadableSelector r) {
/* 292 */     appendSelector(r);
/*     */   }
/*     */   
/*     */   public void addWritable(WritableSelector w) {
/* 296 */     appendSelector(w);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExecutable(ExecutableSelector e) {
/* 304 */     appendSelector(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSymlink(SymlinkSelector e) {
/* 312 */     appendSelector(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOwnedBy(OwnedBySelector o) {
/* 320 */     appendSelector(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPosixGroup(PosixGroupSelector o) {
/* 328 */     appendSelector(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPosixPermissions(PosixPermissionsSelector o) {
/* 336 */     appendSelector(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileSelector selector) {
/* 345 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 350 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 353 */     if (isReference()) {
/* 354 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 356 */       for (FileSelector fileSelector : this.selectorsList) {
/* 357 */         if (fileSelector instanceof DataType) {
/* 358 */           pushAndInvokeCircularReferenceCheck((DataType)fileSelector, stk, p);
/*     */         }
/*     */       } 
/* 361 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/BaseSelectorContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */