/*     */ package org.apache.tools.ant.taskdefs.optional.extension;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.util.DeweyDecimal;
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
/*     */ public final class Extension
/*     */ {
/*  46 */   public static final Attributes.Name EXTENSION_LIST = new Attributes.Name("Extension-List");
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
/*  59 */   public static final Attributes.Name OPTIONAL_EXTENSION_LIST = new Attributes.Name("Optional-Extension-List");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   public static final Attributes.Name EXTENSION_NAME = new Attributes.Name("Extension-Name");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public static final Attributes.Name SPECIFICATION_VERSION = Attributes.Name.SPECIFICATION_VERSION;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public static final Attributes.Name SPECIFICATION_VENDOR = Attributes.Name.SPECIFICATION_VENDOR;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public static final Attributes.Name IMPLEMENTATION_VERSION = Attributes.Name.IMPLEMENTATION_VERSION;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   public static final Attributes.Name IMPLEMENTATION_VENDOR = Attributes.Name.IMPLEMENTATION_VENDOR;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public static final Attributes.Name IMPLEMENTATION_URL = new Attributes.Name("Implementation-URL");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   public static final Attributes.Name IMPLEMENTATION_VENDOR_ID = new Attributes.Name("Implementation-Vendor-Id");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public static final Compatibility COMPATIBLE = new Compatibility("COMPATIBLE");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   public static final Compatibility REQUIRE_SPECIFICATION_UPGRADE = new Compatibility("REQUIRE_SPECIFICATION_UPGRADE");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   public static final Compatibility REQUIRE_VENDOR_SWITCH = new Compatibility("REQUIRE_VENDOR_SWITCH");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public static final Compatibility REQUIRE_IMPLEMENTATION_UPGRADE = new Compatibility("REQUIRE_IMPLEMENTATION_UPGRADE");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   public static final Compatibility INCOMPATIBLE = new Compatibility("INCOMPATIBLE");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String extensionName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DeweyDecimal specificationVersion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String specificationVendor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String implementationVendorID;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String implementationVendor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DeweyDecimal implementationVersion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String implementationURL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Extension[] getAvailable(Manifest manifest) {
/* 190 */     if (null == manifest) {
/* 191 */       return new Extension[0];
/*     */     }
/* 193 */     return 
/* 194 */       (Extension[])Stream.concat(Optional.<Attributes>ofNullable(manifest.getMainAttributes())
/* 195 */         .map(Stream::of).orElse(Stream.empty()), manifest
/* 196 */         .getEntries().values().stream())
/* 197 */       .map(attrs -> getExtension("", attrs)).filter(Objects::nonNull)
/* 198 */       .toArray(x$0 -> new Extension[x$0]);
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
/*     */   public static Extension[] getRequired(Manifest manifest) {
/* 211 */     return getListed(manifest, Attributes.Name.EXTENSION_LIST);
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
/*     */   public static Extension[] getOptions(Manifest manifest) {
/* 223 */     return getListed(manifest, OPTIONAL_EXTENSION_LIST);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addExtension(Extension extension, Attributes attributes) {
/* 234 */     addExtension(extension, "", attributes);
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
/*     */   public static void addExtension(Extension extension, String prefix, Attributes attributes) {
/* 249 */     attributes.putValue(prefix + EXTENSION_NAME, extension
/* 250 */         .getExtensionName());
/*     */     
/* 252 */     String specificationVendor = extension.getSpecificationVendor();
/* 253 */     if (null != specificationVendor) {
/* 254 */       attributes.putValue(prefix + SPECIFICATION_VENDOR, specificationVendor);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 259 */     DeweyDecimal specificationVersion = extension.getSpecificationVersion();
/* 260 */     if (null != specificationVersion) {
/* 261 */       attributes.putValue(prefix + SPECIFICATION_VERSION, specificationVersion
/* 262 */           .toString());
/*     */     }
/*     */ 
/*     */     
/* 266 */     String implementationVendorID = extension.getImplementationVendorID();
/* 267 */     if (null != implementationVendorID) {
/* 268 */       attributes.putValue(prefix + IMPLEMENTATION_VENDOR_ID, implementationVendorID);
/*     */     }
/*     */ 
/*     */     
/* 272 */     String implementationVendor = extension.getImplementationVendor();
/* 273 */     if (null != implementationVendor) {
/* 274 */       attributes.putValue(prefix + IMPLEMENTATION_VENDOR, implementationVendor);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 279 */     DeweyDecimal implementationVersion = extension.getImplementationVersion();
/* 280 */     if (null != implementationVersion) {
/* 281 */       attributes.putValue(prefix + IMPLEMENTATION_VERSION, implementationVersion
/* 282 */           .toString());
/*     */     }
/*     */     
/* 285 */     String implementationURL = extension.getImplementationURL();
/* 286 */     if (null != implementationURL) {
/* 287 */       attributes.putValue(prefix + IMPLEMENTATION_URL, implementationURL);
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Extension(String extensionName, String specificationVersion, String specificationVendor, String implementationVersion, String implementationVendor, String implementationVendorId, String implementationURL) {
/* 312 */     this.extensionName = extensionName;
/* 313 */     this.specificationVendor = specificationVendor;
/*     */     
/* 315 */     if (null != specificationVersion) {
/*     */       try {
/* 317 */         this.specificationVersion = new DeweyDecimal(specificationVersion);
/*     */       }
/* 319 */       catch (NumberFormatException nfe) {
/* 320 */         String error = "Bad specification version format '" + specificationVersion + "' in '" + extensionName + "'. (Reason: " + nfe + ")";
/*     */ 
/*     */         
/* 323 */         throw new IllegalArgumentException(error);
/*     */       } 
/*     */     }
/*     */     
/* 327 */     this.implementationURL = implementationURL;
/* 328 */     this.implementationVendor = implementationVendor;
/* 329 */     this.implementationVendorID = implementationVendorId;
/*     */     
/* 331 */     if (null != implementationVersion) {
/*     */       try {
/* 333 */         this.implementationVersion = new DeweyDecimal(implementationVersion);
/*     */       }
/* 335 */       catch (NumberFormatException nfe) {
/* 336 */         String error = "Bad implementation version format '" + implementationVersion + "' in '" + extensionName + "'. (Reason: " + nfe + ")";
/*     */ 
/*     */         
/* 339 */         throw new IllegalArgumentException(error);
/*     */       } 
/*     */     }
/*     */     
/* 343 */     if (null == this.extensionName) {
/* 344 */       throw new NullPointerException("extensionName property is null");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtensionName() {
/* 354 */     return this.extensionName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSpecificationVendor() {
/* 363 */     return this.specificationVendor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeweyDecimal getSpecificationVersion() {
/* 372 */     return this.specificationVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getImplementationURL() {
/* 381 */     return this.implementationURL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getImplementationVendor() {
/* 390 */     return this.implementationVendor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getImplementationVendorID() {
/* 399 */     return this.implementationVendorID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeweyDecimal getImplementationVersion() {
/* 408 */     return this.implementationVersion;
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
/*     */   public Compatibility getCompatibilityWith(Extension required) {
/* 421 */     if (!this.extensionName.equals(required.getExtensionName())) {
/* 422 */       return INCOMPATIBLE;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 427 */     DeweyDecimal requiredSpecificationVersion = required.getSpecificationVersion();
/* 428 */     if (null != requiredSpecificationVersion && (
/* 429 */       null == this.specificationVersion || 
/* 430 */       !isCompatible(this.specificationVersion, requiredSpecificationVersion))) {
/* 431 */       return REQUIRE_SPECIFICATION_UPGRADE;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 437 */     String requiredImplementationVendorID = required.getImplementationVendorID();
/* 438 */     if (null != requiredImplementationVendorID && (
/* 439 */       null == this.implementationVendorID || 
/* 440 */       !this.implementationVendorID.equals(requiredImplementationVendorID))) {
/* 441 */       return REQUIRE_VENDOR_SWITCH;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 447 */     DeweyDecimal requiredImplementationVersion = required.getImplementationVersion();
/* 448 */     if (null != requiredImplementationVersion && (
/* 449 */       null == this.implementationVersion || 
/* 450 */       !isCompatible(this.implementationVersion, requiredImplementationVersion))) {
/* 451 */       return REQUIRE_IMPLEMENTATION_UPGRADE;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 456 */     return COMPATIBLE;
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
/*     */   public boolean isCompatibleWith(Extension required) {
/* 470 */     return (COMPATIBLE == getCompatibilityWith(required));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 480 */     String format = "%s: %s%n";
/*     */     
/* 482 */     StringBuilder sb = new StringBuilder(String.format("%s: %s%n", new Object[] { EXTENSION_NAME, this.extensionName }));
/*     */ 
/*     */     
/* 485 */     if (null != this.specificationVersion) {
/* 486 */       sb.append(String.format("%s: %s%n", new Object[] { SPECIFICATION_VERSION, this.specificationVersion }));
/*     */     }
/*     */     
/* 489 */     if (null != this.specificationVendor) {
/* 490 */       sb.append(String.format("%s: %s%n", new Object[] { SPECIFICATION_VENDOR, this.specificationVendor }));
/*     */     }
/*     */     
/* 493 */     if (null != this.implementationVersion) {
/* 494 */       sb.append(String.format("%s: %s%n", new Object[] { IMPLEMENTATION_VERSION, this.implementationVersion }));
/*     */     }
/*     */     
/* 497 */     if (null != this.implementationVendorID) {
/* 498 */       sb.append(String.format("%s: %s%n", new Object[] { IMPLEMENTATION_VENDOR_ID, this.implementationVendorID }));
/*     */     }
/*     */     
/* 501 */     if (null != this.implementationVendor) {
/* 502 */       sb.append(String.format("%s: %s%n", new Object[] { IMPLEMENTATION_VENDOR, this.implementationVendor }));
/*     */     }
/*     */     
/* 505 */     if (null != this.implementationURL) {
/* 506 */       sb.append(String.format("%s: %s%n", new Object[] { IMPLEMENTATION_URL, this.implementationURL }));
/*     */     }
/*     */     
/* 509 */     return sb.toString();
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
/*     */   private boolean isCompatible(DeweyDecimal first, DeweyDecimal second) {
/* 521 */     return first.isGreaterThanOrEqual(second);
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
/*     */   private static Extension[] getListed(Manifest manifest, Attributes.Name listKey) {
/* 535 */     List<Extension> results = new ArrayList<>();
/* 536 */     Attributes mainAttributes = manifest.getMainAttributes();
/*     */     
/* 538 */     if (null != mainAttributes) {
/* 539 */       getExtension(mainAttributes, results, listKey);
/*     */     }
/*     */     
/* 542 */     manifest.getEntries().values()
/* 543 */       .forEach(attributes -> getExtension(attributes, results, listKey));
/*     */     
/* 545 */     return results.<Extension>toArray(new Extension[0]);
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
/*     */   private static void getExtension(Attributes attributes, List<Extension> required, Attributes.Name listKey) {
/* 560 */     String names = attributes.getValue(listKey);
/* 561 */     if (null == names) {
/*     */       return;
/*     */     }
/* 564 */     for (String prefix : split(names, " ")) {
/* 565 */       Extension extension = getExtension(prefix + "-", attributes);
/* 566 */       if (null != extension) {
/* 567 */         required.add(extension);
/*     */       }
/*     */     } 
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
/*     */   private static String[] split(String string, String onToken) {
/* 581 */     StringTokenizer tokenizer = new StringTokenizer(string, onToken);
/* 582 */     String[] result = new String[tokenizer.countTokens()];
/*     */     
/* 584 */     for (int i = 0; i < result.length; i++) {
/* 585 */       result[i] = tokenizer.nextToken();
/*     */     }
/*     */     
/* 588 */     return result;
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
/*     */   private static Extension getExtension(String prefix, Attributes attributes) {
/* 608 */     String nameKey = prefix + EXTENSION_NAME;
/* 609 */     String name = getTrimmedString(attributes.getValue(nameKey));
/* 610 */     if (null == name) {
/* 611 */       return null;
/*     */     }
/*     */     
/* 614 */     String specVendorKey = prefix + SPECIFICATION_VENDOR;
/*     */     
/* 616 */     String specVendor = getTrimmedString(attributes.getValue(specVendorKey));
/* 617 */     String specVersionKey = prefix + SPECIFICATION_VERSION;
/*     */     
/* 619 */     String specVersion = getTrimmedString(attributes.getValue(specVersionKey));
/*     */     
/* 621 */     String impVersionKey = prefix + IMPLEMENTATION_VERSION;
/*     */     
/* 623 */     String impVersion = getTrimmedString(attributes.getValue(impVersionKey));
/* 624 */     String impVendorKey = prefix + IMPLEMENTATION_VENDOR;
/*     */     
/* 626 */     String impVendor = getTrimmedString(attributes.getValue(impVendorKey));
/* 627 */     String impVendorIDKey = prefix + IMPLEMENTATION_VENDOR_ID;
/*     */     
/* 629 */     String impVendorId = getTrimmedString(attributes.getValue(impVendorIDKey));
/* 630 */     String impURLKey = prefix + IMPLEMENTATION_URL;
/* 631 */     String impURL = getTrimmedString(attributes.getValue(impURLKey));
/*     */     
/* 633 */     return new Extension(name, specVersion, specVendor, impVersion, impVendor, impVendorId, impURL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getTrimmedString(String value) {
/* 644 */     return (null == value) ? null : value.trim();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/extension/Extension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */