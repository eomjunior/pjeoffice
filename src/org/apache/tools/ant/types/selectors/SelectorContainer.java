package org.apache.tools.ant.types.selectors;

import java.util.Enumeration;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;

public interface SelectorContainer {
  boolean hasSelectors();
  
  int selectorCount();
  
  FileSelector[] getSelectors(Project paramProject);
  
  Enumeration<FileSelector> selectorElements();
  
  void appendSelector(FileSelector paramFileSelector);
  
  void addSelector(SelectSelector paramSelectSelector);
  
  void addAnd(AndSelector paramAndSelector);
  
  void addOr(OrSelector paramOrSelector);
  
  void addNot(NotSelector paramNotSelector);
  
  void addNone(NoneSelector paramNoneSelector);
  
  void addMajority(MajoritySelector paramMajoritySelector);
  
  void addDate(DateSelector paramDateSelector);
  
  void addSize(SizeSelector paramSizeSelector);
  
  void addFilename(FilenameSelector paramFilenameSelector);
  
  void addCustom(ExtendSelector paramExtendSelector);
  
  void addContains(ContainsSelector paramContainsSelector);
  
  void addPresent(PresentSelector paramPresentSelector);
  
  void addDepth(DepthSelector paramDepthSelector);
  
  void addDepend(DependSelector paramDependSelector);
  
  void addContainsRegexp(ContainsRegexpSelector paramContainsRegexpSelector);
  
  void addType(TypeSelector paramTypeSelector);
  
  void addDifferent(DifferentSelector paramDifferentSelector);
  
  void addModified(ModifiedSelector paramModifiedSelector);
  
  void add(FileSelector paramFileSelector);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/SelectorContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */