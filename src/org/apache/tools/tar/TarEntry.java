/*      */ package org.apache.tools.tar;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.Date;
/*      */ import java.util.Locale;
/*      */ import org.apache.tools.zip.ZipEncoding;
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
/*      */ public class TarEntry
/*      */   implements TarConstants
/*      */ {
/*  179 */   private String magic = "ustar\000";
/*  180 */   private String version = "00";
/*  181 */   private String name = ""; private int mode; private long userId; private long groupId; private long size;
/*  182 */   private String linkName = ""; private long modTime; private byte linkFlag; private String userName; private String groupName;
/*      */   private TarEntry() {
/*  184 */     String user = System.getProperty("user.name", "");
/*      */     
/*  186 */     if (user.length() > 31) {
/*  187 */       user = user.substring(0, 31);
/*      */     }
/*      */     
/*  190 */     this.userId = 0L;
/*  191 */     this.groupId = 0L;
/*  192 */     this.userName = user;
/*  193 */     this.groupName = "";
/*  194 */     this.file = null;
/*      */   }
/*      */   private int devMajor; private int devMinor; private boolean isExtended; private long realSize;
/*      */   private File file;
/*      */   public static final int MAX_NAMELEN = 31;
/*      */   public static final int DEFAULT_DIR_MODE = 16877;
/*      */   public static final int DEFAULT_FILE_MODE = 33188;
/*      */   public static final int MILLIS_PER_SECOND = 1000;
/*      */   
/*      */   public TarEntry(String name) {
/*  204 */     this(name, false);
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
/*      */   public TarEntry(String name, boolean preserveLeadingSlashes) {
/*  216 */     this();
/*      */     
/*  218 */     name = normalizeFileName(name, preserveLeadingSlashes);
/*  219 */     boolean isDir = name.endsWith("/");
/*      */     
/*  221 */     this.devMajor = 0;
/*  222 */     this.devMinor = 0;
/*  223 */     this.name = name;
/*  224 */     this.mode = isDir ? 16877 : 33188;
/*  225 */     this.linkFlag = isDir ? 53 : 48;
/*  226 */     this.userId = 0L;
/*  227 */     this.groupId = 0L;
/*  228 */     this.size = 0L;
/*  229 */     this.modTime = (new Date()).getTime() / 1000L;
/*  230 */     this.linkName = "";
/*  231 */     this.userName = "";
/*  232 */     this.groupName = "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarEntry(String name, byte linkFlag) {
/*  242 */     this(name);
/*  243 */     this.linkFlag = linkFlag;
/*  244 */     if (linkFlag == 76) {
/*  245 */       this.magic = "ustar  ";
/*  246 */       this.version = " \000";
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
/*      */   public TarEntry(File file) {
/*  258 */     this(file, file.getPath());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarEntry(File file, String fileName) {
/*  269 */     this();
/*      */     
/*  271 */     String normalizedName = normalizeFileName(fileName, false);
/*  272 */     this.file = file;
/*      */     
/*  274 */     this.linkName = "";
/*      */     
/*  276 */     if (file.isDirectory()) {
/*  277 */       this.mode = 16877;
/*  278 */       this.linkFlag = 53;
/*      */       
/*  280 */       int nameLength = normalizedName.length();
/*  281 */       if (nameLength == 0 || normalizedName.charAt(nameLength - 1) != '/') {
/*  282 */         this.name = normalizedName + "/";
/*      */       } else {
/*  284 */         this.name = normalizedName;
/*      */       } 
/*  286 */       this.size = 0L;
/*      */     } else {
/*  288 */       this.mode = 33188;
/*  289 */       this.linkFlag = 48;
/*  290 */       this.size = file.length();
/*  291 */       this.name = normalizedName;
/*      */     } 
/*      */     
/*  294 */     this.modTime = file.lastModified() / 1000L;
/*  295 */     this.devMajor = 0;
/*  296 */     this.devMinor = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarEntry(byte[] headerBuf) {
/*  307 */     this();
/*  308 */     parseTarHeader(headerBuf);
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
/*      */   public TarEntry(byte[] headerBuf, ZipEncoding encoding) throws IOException {
/*  322 */     this();
/*  323 */     parseTarHeader(headerBuf, encoding);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(TarEntry it) {
/*  334 */     return (it != null && getName().equals(it.getName()));
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
/*      */   public boolean equals(Object it) {
/*  346 */     return (it != null && getClass() == it.getClass() && equals((TarEntry)it));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  356 */     return getName().hashCode();
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
/*      */   public boolean isDescendent(TarEntry desc) {
/*  368 */     return desc.getName().startsWith(getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  377 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String name) {
/*  386 */     this.name = normalizeFileName(name, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMode(int mode) {
/*  395 */     this.mode = mode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getLinkFlag() {
/*  405 */     return this.linkFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLinkFlag(byte linkFlag) {
/*  415 */     this.linkFlag = linkFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getLinkName() {
/*  424 */     return this.linkName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLinkName(String link) {
/*  433 */     this.linkName = link;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int getUserId() {
/*  445 */     return (int)this.userId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUserId(int userId) {
/*  454 */     setUserId(userId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLongUserId() {
/*  464 */     return this.userId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUserId(long userId) {
/*  474 */     this.userId = userId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int getGroupId() {
/*  486 */     return (int)this.groupId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGroupId(int groupId) {
/*  495 */     setGroupId(groupId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLongGroupId() {
/*  505 */     return this.groupId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGroupId(long groupId) {
/*  515 */     this.groupId = groupId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUserName() {
/*  524 */     return this.userName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUserName(String userName) {
/*  533 */     this.userName = userName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getGroupName() {
/*  542 */     return this.groupName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGroupName(String groupName) {
/*  551 */     this.groupName = groupName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIds(int userId, int groupId) {
/*  561 */     setUserId(userId);
/*  562 */     setGroupId(groupId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNames(String userName, String groupName) {
/*  572 */     setUserName(userName);
/*  573 */     setGroupName(groupName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModTime(long time) {
/*  583 */     this.modTime = time / 1000L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModTime(Date time) {
/*  592 */     this.modTime = time.getTime() / 1000L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getModTime() {
/*  601 */     return new Date(this.modTime * 1000L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public File getFile() {
/*  610 */     return this.file;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMode() {
/*  619 */     return this.mode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getSize() {
/*  628 */     return this.size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSize(long size) {
/*  638 */     if (size < 0L) {
/*  639 */       throw new IllegalArgumentException("Size is out of range: " + size);
/*      */     }
/*  641 */     this.size = size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDevMajor() {
/*  650 */     return this.devMajor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDevMajor(int devNo) {
/*  660 */     if (devNo < 0) {
/*  661 */       throw new IllegalArgumentException("Major device number is out of range: " + devNo);
/*      */     }
/*      */     
/*  664 */     this.devMajor = devNo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDevMinor() {
/*  673 */     return this.devMinor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDevMinor(int devNo) {
/*  683 */     if (devNo < 0) {
/*  684 */       throw new IllegalArgumentException("Minor device number is out of range: " + devNo);
/*      */     }
/*      */     
/*  687 */     this.devMinor = devNo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isExtended() {
/*  697 */     return this.isExtended;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getRealSize() {
/*  706 */     return this.realSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isGNUSparse() {
/*  715 */     return (this.linkFlag == 83);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isGNULongLinkEntry() {
/*  724 */     return (this.linkFlag == 75);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isGNULongNameEntry() {
/*  733 */     return (this.linkFlag == 76);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPaxHeader() {
/*  742 */     return (this.linkFlag == 120 || this.linkFlag == 88);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isGlobalPaxHeader() {
/*  752 */     return (this.linkFlag == 103);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDirectory() {
/*  761 */     if (this.file != null) {
/*  762 */       return this.file.isDirectory();
/*      */     }
/*      */     
/*  765 */     return (this.linkFlag == 53 || getName().endsWith("/"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFile() {
/*  773 */     return (this.file != null) ? this.file.isFile() : (
/*  774 */       (this.linkFlag == 0 || this.linkFlag == 48 || !getName().endsWith("/")));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSymbolicLink() {
/*  782 */     return (this.linkFlag == 50);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLink() {
/*  790 */     return (this.linkFlag == 49);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCharacterDevice() {
/*  798 */     return (this.linkFlag == 51);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBlockDevice() {
/*  805 */     return (this.linkFlag == 52);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFIFO() {
/*  812 */     return (this.linkFlag == 54);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarEntry[] getDirectoryEntries() {
/*  822 */     if (this.file == null || !this.file.isDirectory()) {
/*  823 */       return new TarEntry[0];
/*      */     }
/*      */     
/*  826 */     String[] list = this.file.list();
/*  827 */     TarEntry[] result = new TarEntry[list.length];
/*      */     
/*  829 */     for (int i = 0; i < list.length; i++) {
/*  830 */       result[i] = new TarEntry(new File(this.file, list[i]));
/*      */     }
/*      */     
/*  833 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeEntryHeader(byte[] outbuf) {
/*      */     try {
/*  845 */       writeEntryHeader(outbuf, TarUtils.DEFAULT_ENCODING, false);
/*  846 */     } catch (IOException ex) {
/*      */       try {
/*  848 */         writeEntryHeader(outbuf, TarUtils.FALLBACK_ENCODING, false);
/*  849 */       } catch (IOException ex2) {
/*      */         
/*  851 */         throw new RuntimeException(ex2);
/*      */       } 
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
/*      */   public void writeEntryHeader(byte[] outbuf, ZipEncoding encoding, boolean starMode) throws IOException {
/*  868 */     int offset = 0;
/*      */     
/*  870 */     offset = TarUtils.formatNameBytes(this.name, outbuf, offset, 100, encoding);
/*      */     
/*  872 */     offset = writeEntryHeaderField(this.mode, outbuf, offset, 8, starMode);
/*  873 */     offset = writeEntryHeaderField(this.userId, outbuf, offset, 8, starMode);
/*      */     
/*  875 */     offset = writeEntryHeaderField(this.groupId, outbuf, offset, 8, starMode);
/*      */     
/*  877 */     offset = writeEntryHeaderField(this.size, outbuf, offset, 12, starMode);
/*  878 */     offset = writeEntryHeaderField(this.modTime, outbuf, offset, 12, starMode);
/*      */ 
/*      */     
/*  881 */     int csOffset = offset;
/*      */     
/*  883 */     for (int c = 0; c < 8; c++) {
/*  884 */       outbuf[offset++] = 32;
/*      */     }
/*      */     
/*  887 */     outbuf[offset++] = this.linkFlag;
/*  888 */     offset = TarUtils.formatNameBytes(this.linkName, outbuf, offset, 100, encoding);
/*      */     
/*  890 */     offset = TarUtils.formatNameBytes(this.magic, outbuf, offset, 6);
/*  891 */     offset = TarUtils.formatNameBytes(this.version, outbuf, offset, 2);
/*  892 */     offset = TarUtils.formatNameBytes(this.userName, outbuf, offset, 32, encoding);
/*      */     
/*  894 */     offset = TarUtils.formatNameBytes(this.groupName, outbuf, offset, 32, encoding);
/*      */     
/*  896 */     offset = writeEntryHeaderField(this.devMajor, outbuf, offset, 8, starMode);
/*      */     
/*  898 */     offset = writeEntryHeaderField(this.devMinor, outbuf, offset, 8, starMode);
/*      */ 
/*      */     
/*  901 */     while (offset < outbuf.length) {
/*  902 */       outbuf[offset++] = 0;
/*      */     }
/*      */     
/*  905 */     long chk = TarUtils.computeCheckSum(outbuf);
/*      */     
/*  907 */     TarUtils.formatCheckSumOctalBytes(chk, outbuf, csOffset, 8);
/*      */   }
/*      */ 
/*      */   
/*      */   private int writeEntryHeaderField(long value, byte[] outbuf, int offset, int length, boolean starMode) {
/*  912 */     if (!starMode && (value < 0L || value >= 1L << 3 * (length - 1)))
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  917 */       return TarUtils.formatLongOctalBytes(0L, outbuf, offset, length);
/*      */     }
/*  919 */     return TarUtils.formatLongOctalOrBinaryBytes(value, outbuf, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseTarHeader(byte[] header) {
/*      */     try {
/*  931 */       parseTarHeader(header, TarUtils.DEFAULT_ENCODING);
/*  932 */     } catch (IOException ex) {
/*      */       try {
/*  934 */         parseTarHeader(header, TarUtils.DEFAULT_ENCODING, true);
/*  935 */       } catch (IOException ex2) {
/*      */         
/*  937 */         throw new RuntimeException(ex2);
/*      */       } 
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
/*      */   public void parseTarHeader(byte[] header, ZipEncoding encoding) throws IOException {
/*  953 */     parseTarHeader(header, encoding, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void parseTarHeader(byte[] header, ZipEncoding encoding, boolean oldStyle) throws IOException {
/*  959 */     int offset = 0;
/*      */     
/*  961 */     this
/*  962 */       .name = oldStyle ? TarUtils.parseName(header, offset, 100) : TarUtils.parseName(header, offset, 100, encoding);
/*  963 */     offset += 100;
/*  964 */     this.mode = (int)TarUtils.parseOctalOrBinary(header, offset, 8);
/*  965 */     offset += 8;
/*  966 */     this.userId = (int)TarUtils.parseOctalOrBinary(header, offset, 8);
/*  967 */     offset += 8;
/*  968 */     this.groupId = (int)TarUtils.parseOctalOrBinary(header, offset, 8);
/*  969 */     offset += 8;
/*  970 */     this.size = TarUtils.parseOctalOrBinary(header, offset, 12);
/*  971 */     offset += 12;
/*  972 */     this.modTime = TarUtils.parseOctalOrBinary(header, offset, 12);
/*  973 */     offset += 12;
/*  974 */     offset += 8;
/*  975 */     this.linkFlag = header[offset++];
/*  976 */     this
/*  977 */       .linkName = oldStyle ? TarUtils.parseName(header, offset, 100) : TarUtils.parseName(header, offset, 100, encoding);
/*  978 */     offset += 100;
/*  979 */     this.magic = TarUtils.parseName(header, offset, 6);
/*  980 */     offset += 6;
/*  981 */     this.version = TarUtils.parseName(header, offset, 2);
/*  982 */     offset += 2;
/*  983 */     this
/*  984 */       .userName = oldStyle ? TarUtils.parseName(header, offset, 32) : TarUtils.parseName(header, offset, 32, encoding);
/*  985 */     offset += 32;
/*  986 */     this
/*  987 */       .groupName = oldStyle ? TarUtils.parseName(header, offset, 32) : TarUtils.parseName(header, offset, 32, encoding);
/*  988 */     offset += 32;
/*  989 */     this.devMajor = (int)TarUtils.parseOctalOrBinary(header, offset, 8);
/*  990 */     offset += 8;
/*  991 */     this.devMinor = (int)TarUtils.parseOctalOrBinary(header, offset, 8);
/*  992 */     offset += 8;
/*      */     
/*  994 */     int type = evaluateType(header);
/*  995 */     switch (type) {
/*      */       case 2:
/*  997 */         offset += 12;
/*  998 */         offset += 12;
/*  999 */         offset += 12;
/* 1000 */         offset += 4;
/* 1001 */         offset++;
/* 1002 */         offset += 96;
/* 1003 */         this.isExtended = TarUtils.parseBoolean(header, offset);
/* 1004 */         offset++;
/* 1005 */         this.realSize = TarUtils.parseOctal(header, offset, 12);
/* 1006 */         offset += 12;
/*      */         return;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1012 */     String prefix = oldStyle ? TarUtils.parseName(header, offset, 155) : TarUtils.parseName(header, offset, 155, encoding);
/*      */ 
/*      */     
/* 1015 */     if (isDirectory() && !this.name.endsWith("/")) {
/* 1016 */       this.name += "/";
/*      */     }
/* 1018 */     if (!prefix.isEmpty()) {
/* 1019 */       this.name = prefix + "/" + this.name;
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
/*      */   private static String normalizeFileName(String fileName, boolean preserveLeadingSlashes) {
/* 1031 */     String osname = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
/*      */     
/* 1033 */     if (osname != null)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/* 1038 */       if (osname.startsWith("windows")) {
/* 1039 */         if (fileName.length() > 2) {
/* 1040 */           char ch1 = fileName.charAt(0);
/* 1041 */           char ch2 = fileName.charAt(1);
/*      */           
/* 1043 */           if (ch2 == ':' && ((ch1 >= 'a' && ch1 <= 'z') || (ch1 >= 'A' && ch1 <= 'Z')))
/*      */           {
/*      */             
/* 1046 */             fileName = fileName.substring(2);
/*      */           }
/*      */         } 
/* 1049 */       } else if (osname.contains("netware")) {
/* 1050 */         int colon = fileName.indexOf(':');
/* 1051 */         if (colon != -1) {
/* 1052 */           fileName = fileName.substring(colon + 1);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/* 1057 */     fileName = fileName.replace(File.separatorChar, '/');
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1062 */     while (!preserveLeadingSlashes && fileName.startsWith("/")) {
/* 1063 */       fileName = fileName.substring(1);
/*      */     }
/* 1065 */     return fileName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int evaluateType(byte[] header) {
/* 1075 */     if (matchAsciiBuffer("ustar  ", header, 257, 6)) {
/* 1076 */       return 2;
/*      */     }
/* 1078 */     if (matchAsciiBuffer("ustar\000", header, 257, 6)) {
/* 1079 */       return 3;
/*      */     }
/* 1081 */     return 0;
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
/*      */   private static boolean matchAsciiBuffer(String expected, byte[] buffer, int offset, int length) {
/* 1095 */     byte[] buffer1 = expected.getBytes(StandardCharsets.US_ASCII);
/* 1096 */     return isEqual(buffer1, 0, buffer1.length, buffer, offset, length, false);
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
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isEqual(byte[] buffer1, int offset1, int length1, byte[] buffer2, int offset2, int length2, boolean ignoreTrailingNulls) {
/* 1116 */     int minLen = (length1 < length2) ? length1 : length2; int i;
/* 1117 */     for (i = 0; i < minLen; i++) {
/* 1118 */       if (buffer1[offset1 + i] != buffer2[offset2 + i]) {
/* 1119 */         return false;
/*      */       }
/*      */     } 
/* 1122 */     if (length1 == length2) {
/* 1123 */       return true;
/*      */     }
/* 1125 */     if (ignoreTrailingNulls) {
/* 1126 */       if (length1 > length2) {
/* 1127 */         for (i = length2; i < length1; i++) {
/* 1128 */           if (buffer1[offset1 + i] != 0) {
/* 1129 */             return false;
/*      */           }
/*      */         } 
/*      */       } else {
/* 1133 */         for (i = length1; i < length2; i++) {
/* 1134 */           if (buffer2[offset2 + i] != 0) {
/* 1135 */             return false;
/*      */           }
/*      */         } 
/*      */       } 
/* 1139 */       return true;
/*      */     } 
/* 1141 */     return false;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/tar/TarEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */