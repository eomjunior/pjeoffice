/*     */ package org.apache.log4j.chainsaw;
/*     */ 
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class XMLFileHandler
/*     */   extends DefaultHandler
/*     */ {
/*     */   private static final String TAG_EVENT = "log4j:event";
/*     */   private static final String TAG_MESSAGE = "log4j:message";
/*     */   private static final String TAG_NDC = "log4j:NDC";
/*     */   private static final String TAG_THROWABLE = "log4j:throwable";
/*     */   private static final String TAG_LOCATION_INFO = "log4j:locationInfo";
/*     */   private final MyTableModel mModel;
/*     */   private int mNumEvents;
/*     */   private long mTimeStamp;
/*     */   private Level mLevel;
/*     */   private String mCategoryName;
/*     */   private String mNDC;
/*     */   private String mThreadName;
/*     */   private String mMessage;
/*     */   private String[] mThrowableStrRep;
/*     */   private String mLocationDetails;
/*  66 */   private final StringBuffer mBuf = new StringBuffer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   XMLFileHandler(MyTableModel aModel) {
/*  74 */     this.mModel = aModel;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDocument() throws SAXException {
/*  79 */     this.mNumEvents = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void characters(char[] aChars, int aStart, int aLength) {
/*  84 */     this.mBuf.append(String.valueOf(aChars, aStart, aLength));
/*     */   }
/*     */ 
/*     */   
/*     */   public void endElement(String aNamespaceURI, String aLocalName, String aQName) {
/*  89 */     if ("log4j:event".equals(aQName)) {
/*  90 */       addEvent();
/*  91 */       resetData();
/*  92 */     } else if ("log4j:NDC".equals(aQName)) {
/*  93 */       this.mNDC = this.mBuf.toString();
/*  94 */     } else if ("log4j:message".equals(aQName)) {
/*  95 */       this.mMessage = this.mBuf.toString();
/*  96 */     } else if ("log4j:throwable".equals(aQName)) {
/*  97 */       StringTokenizer st = new StringTokenizer(this.mBuf.toString(), "\n\t");
/*  98 */       this.mThrowableStrRep = new String[st.countTokens()];
/*  99 */       if (this.mThrowableStrRep.length > 0) {
/* 100 */         this.mThrowableStrRep[0] = st.nextToken();
/* 101 */         for (int i = 1; i < this.mThrowableStrRep.length; i++) {
/* 102 */           this.mThrowableStrRep[i] = "\t" + st.nextToken();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void startElement(String aNamespaceURI, String aLocalName, String aQName, Attributes aAtts) {
/* 110 */     this.mBuf.setLength(0);
/*     */     
/* 112 */     if ("log4j:event".equals(aQName)) {
/* 113 */       this.mThreadName = aAtts.getValue("thread");
/* 114 */       this.mTimeStamp = Long.parseLong(aAtts.getValue("timestamp"));
/* 115 */       this.mCategoryName = aAtts.getValue("logger");
/* 116 */       this.mLevel = Level.toLevel(aAtts.getValue("level"));
/* 117 */     } else if ("log4j:locationInfo".equals(aQName)) {
/* 118 */       this
/* 119 */         .mLocationDetails = aAtts.getValue("class") + "." + aAtts.getValue("method") + "(" + aAtts.getValue("file") + ":" + aAtts.getValue("line") + ")";
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   int getNumEvents() {
/* 125 */     return this.mNumEvents;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addEvent() {
/* 134 */     this.mModel.addEvent(new EventDetails(this.mTimeStamp, (Priority)this.mLevel, this.mCategoryName, this.mNDC, this.mThreadName, this.mMessage, this.mThrowableStrRep, this.mLocationDetails));
/*     */     
/* 136 */     this.mNumEvents++;
/*     */   }
/*     */ 
/*     */   
/*     */   private void resetData() {
/* 141 */     this.mTimeStamp = 0L;
/* 142 */     this.mLevel = null;
/* 143 */     this.mCategoryName = null;
/* 144 */     this.mNDC = null;
/* 145 */     this.mThreadName = null;
/* 146 */     this.mMessage = null;
/* 147 */     this.mThrowableStrRep = null;
/* 148 */     this.mLocationDetails = null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/chainsaw/XMLFileHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */