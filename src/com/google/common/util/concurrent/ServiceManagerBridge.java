package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.J2ktIncompatible;
import com.google.common.collect.ImmutableMultimap;

@ElementTypesAreNonnullByDefault
@J2ktIncompatible
@GwtIncompatible
interface ServiceManagerBridge {
  ImmutableMultimap<Service.State, Service> servicesByState();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/ServiceManagerBridge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */