/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
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
/*     */ 
/*     */ 
/*     */ public abstract class AbstractSelectorContainer
/*     */   extends DataType
/*     */   implements Cloneable, SelectorContainer
/*     */ {
/*  44 */   private List<FileSelector> selectorsList = Collections.synchronizedList(new ArrayList<>());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSelectors() {
/*  52 */     if (isReference()) {
/*  53 */       return getRef().hasSelectors();
/*     */     }
/*  55 */     dieOnCircularReference();
/*  56 */     return !this.selectorsList.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int selectorCount() {
/*  64 */     if (isReference()) {
/*  65 */       return getRef().selectorCount();
/*     */     }
/*  67 */     dieOnCircularReference();
/*  68 */     return this.selectorsList.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSelector[] getSelectors(Project p) {
/*  77 */     if (isReference()) {
/*  78 */       return getRef(p).getSelectors(p);
/*     */     }
/*  80 */     dieOnCircularReference(p);
/*  81 */     return this.selectorsList.<FileSelector>toArray(new FileSelector[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<FileSelector> selectorElements() {
/*  89 */     if (isReference()) {
/*  90 */       return getRef().selectorElements();
/*     */     }
/*  92 */     dieOnCircularReference();
/*  93 */     return Collections.enumeration(this.selectorsList);
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
/* 104 */     return this.selectorsList.stream().map(Object::toString)
/* 105 */       .collect(Collectors.joining(", "));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendSelector(FileSelector selector) {
/* 114 */     if (isReference()) {
/* 115 */       throw noChildrenAllowed();
/*     */     }
/* 117 */     this.selectorsList.add(selector);
/* 118 */     setChecked(false);
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
/* 138 */     if (isReference()) {
/* 139 */       getRef().validate();
/*     */     }
/* 141 */     dieOnCircularReference();
/* 142 */     Objects.requireNonNull(BaseSelector.class);
/* 143 */     Objects.requireNonNull(BaseSelector.class); this.selectorsList.stream().filter(BaseSelector.class::isInstance).map(BaseSelector.class::cast).forEach(BaseSelector::validate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSelector(SelectSelector selector) {
/* 154 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnd(AndSelector selector) {
/* 162 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOr(OrSelector selector) {
/* 170 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNot(NotSelector selector) {
/* 178 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNone(NoneSelector selector) {
/* 186 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMajority(MajoritySelector selector) {
/* 194 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDate(DateSelector selector) {
/* 202 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSize(SizeSelector selector) {
/* 210 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFilename(FilenameSelector selector) {
/* 218 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCustom(ExtendSelector selector) {
/* 226 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContains(ContainsSelector selector) {
/* 234 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPresent(PresentSelector selector) {
/* 242 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDepth(DepthSelector selector) {
/* 250 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDepend(DependSelector selector) {
/* 258 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDifferent(DifferentSelector selector) {
/* 266 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addType(TypeSelector selector) {
/* 274 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContainsRegexp(ContainsRegexpSelector selector) {
/* 282 */     appendSelector(selector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addModified(ModifiedSelector selector) {
/* 291 */     appendSelector((FileSelector)selector);
/*     */   }
/*     */   
/*     */   public void addReadable(ReadableSelector r) {
/* 295 */     appendSelector(r);
/*     */   }
/*     */   
/*     */   public void addWritable(WritableSelector w) {
/* 299 */     appendSelector(w);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExecutable(ExecutableSelector e) {
/* 307 */     appendSelector(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSymlink(SymlinkSelector e) {
/* 315 */     appendSelector(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addOwnedBy(OwnedBySelector o) {
/* 323 */     appendSelector(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPosixGroup(PosixGroupSelector o) {
/* 331 */     appendSelector(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPosixPermissions(PosixPermissionsSelector o) {
/* 339 */     appendSelector(o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(FileSelector selector) {
/* 348 */     appendSelector(selector);
/*     */   }
/*     */   
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) {
/* 352 */     if (isChecked()) {
/*     */       return;
/*     */     }
/* 355 */     if (isReference()) {
/* 356 */       super.dieOnCircularReference(stk, p);
/*     */     } else {
/* 358 */       for (FileSelector fileSelector : this.selectorsList) {
/* 359 */         if (fileSelector instanceof DataType) {
/* 360 */           pushAndInvokeCircularReferenceCheck((DataType)fileSelector, stk, p);
/*     */         }
/*     */       } 
/* 363 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Object clone() {
/* 372 */     if (isReference()) {
/* 373 */       return getRef().clone();
/*     */     }
/*     */     
/*     */     try {
/* 377 */       AbstractSelectorContainer sc = (AbstractSelectorContainer)super.clone();
/* 378 */       sc.selectorsList = new Vector<>(this.selectorsList);
/* 379 */       return sc;
/* 380 */     } catch (CloneNotSupportedException e) {
/* 381 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private AbstractSelectorContainer getRef(Project p) {
/* 387 */     return (AbstractSelectorContainer)getCheckedRef(AbstractSelectorContainer.class, getDataTypeName(), p);
/*     */   }
/*     */   
/*     */   private AbstractSelectorContainer getRef() {
/* 391 */     return (AbstractSelectorContainer)getCheckedRef(AbstractSelectorContainer.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/AbstractSelectorContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */