/*    */ package com.itextpdf.text.pdf.fonts.cmaps;
/*    */ 
/*    */ import com.itextpdf.text.error_messages.MessageLocalization;
/*    */ import com.itextpdf.text.io.RandomAccessSourceFactory;
/*    */ import com.itextpdf.text.io.StreamUtil;
/*    */ import com.itextpdf.text.pdf.PRTokeniser;
/*    */ import com.itextpdf.text.pdf.RandomAccessFileOrArray;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
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
/*    */ public class CidResource
/*    */   implements CidLocation
/*    */ {
/*    */   public PRTokeniser getLocation(String location) throws IOException {
/* 63 */     String fullName = "com/itextpdf/text/pdf/fonts/cmaps/" + location;
/* 64 */     InputStream inp = StreamUtil.getResourceStream(fullName);
/* 65 */     if (inp == null)
/* 66 */       throw new IOException(MessageLocalization.getComposedMessage("the.cmap.1.was.not.found", new Object[] { fullName })); 
/* 67 */     return new PRTokeniser(new RandomAccessFileOrArray((new RandomAccessSourceFactory()).createSource(inp)));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/fonts/cmaps/CidResource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */