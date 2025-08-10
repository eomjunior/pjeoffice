package org.apache.hc.core5.pool;

import org.apache.hc.core5.io.ModalCloseable;

public interface ManagedConnPool<T, C extends ModalCloseable> extends ConnPool<T, C>, ConnPoolControl<T>, ModalCloseable {}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/pool/ManagedConnPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */