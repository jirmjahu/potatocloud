package net.potatocloud.node.utils;

import lombok.experimental.UtilityClass;
import oshi.SystemInfo;

@UtilityClass
public class HardwareUtils {

    private static final SystemInfo INFO = new SystemInfo();

    public int getCpuCores() {
        return INFO.getHardware().getProcessor().getPhysicalProcessorCount();
    }

    public int getCpuThreads() {
        return INFO.getHardware().getProcessor().getLogicalProcessorCount();
    }

    public int getRam() {
        return (int) (INFO.getHardware().getMemory().getTotal() / (1024.0 * 1024 * 1024));
    }

    public int getAvailableRam() {
        return (int) (INFO.getHardware().getMemory().getAvailable() / (1024.0 * 1024 * 1024));
    }

    public boolean isLowHardware() {
        return getCpuCores() < 4 || getRam() < 4;
    }
}
