/*    */ package org.apache.tools.ant.types.mappers;
/*    */ 
/*    */ import java.io.Reader;
/*    */ import java.io.StringReader;
/*    */ import java.util.Vector;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.UnsupportedAttributeException;
/*    */ import org.apache.tools.ant.filters.util.ChainReaderHelper;
/*    */ import org.apache.tools.ant.types.FilterChain;
/*    */ import org.apache.tools.ant.util.FileNameMapper;
/*    */ import org.apache.tools.ant.util.FileUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FilterMapper
/*    */   extends FilterChain
/*    */   implements FileNameMapper
/*    */ {
/*    */   private static final int BUFFER_SIZE = 8192;
/*    */   
/*    */   public void setFrom(String from) {
/* 46 */     throw new UnsupportedAttributeException("filtermapper doesn't support the \"from\" attribute.", "from");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTo(String to) {
/* 57 */     throw new UnsupportedAttributeException("filtermapper doesn't support the \"to\" attribute.", "to");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] mapFileName(String sourceFileName) {
/* 69 */     if (sourceFileName == null) {
/* 70 */       return null;
/*    */     }
/*    */     try {
/* 73 */       Reader stringReader = new StringReader(sourceFileName);
/* 74 */       ChainReaderHelper helper = new ChainReaderHelper();
/* 75 */       helper.setBufferSize(8192);
/* 76 */       helper.setPrimaryReader(stringReader);
/* 77 */       helper.setProject(getProject());
/* 78 */       Vector<FilterChain> filterChains = new Vector<>();
/* 79 */       filterChains.add(this);
/* 80 */       helper.setFilterChains(filterChains);
/* 81 */       String result = FileUtils.safeReadFully((Reader)helper.getAssembledReader());
/* 82 */       if (result.isEmpty()) {
/* 83 */         return null;
/*    */       }
/* 85 */       return new String[] { result };
/* 86 */     } catch (BuildException ex) {
/* 87 */       throw ex;
/* 88 */     } catch (Exception ex) {
/* 89 */       throw new BuildException(ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/mappers/FilterMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */