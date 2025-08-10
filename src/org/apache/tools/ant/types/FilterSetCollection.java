/*    */ package org.apache.tools.ant.types;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FilterSetCollection
/*    */ {
/* 31 */   private List<FilterSet> filterSets = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FilterSetCollection() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FilterSetCollection(FilterSet filterSet) {
/* 44 */     addFilterSet(filterSet);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addFilterSet(FilterSet filterSet) {
/* 54 */     this.filterSets.add(filterSet);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String replaceTokens(String line) {
/* 65 */     String replacedLine = line;
/* 66 */     for (FilterSet filterSet : this.filterSets) {
/* 67 */       replacedLine = filterSet.replaceTokens(replacedLine);
/*    */     }
/* 69 */     return replacedLine;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasFilters() {
/* 78 */     return this.filterSets.stream().anyMatch(FilterSet::hasFilters);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/FilterSetCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */