package it.mikedmc.model;

public class Processo {
    private boolean attivo;
    private String name;
    private String processName;
    private String pid;
    private String memoryUsagePercentage;
    private int physicalMemory;
    private String startTime;

    @Override
    public String toString() {
        return "Processo{" +
                "attivo=" + attivo +
                ", name='" + name + '\'' +
                ", processName='" + processName + '\'' +
                ", pid='" + pid + '\'' +
                ", memoryUsagePercentage=" + memoryUsagePercentage +
                ", physicalMemory=" + physicalMemory +
                ", startTime='" + startTime + '\'' +
                '}';
    }

    public Processo (){}

    public Processo(String name, String processName, String pid, String memoryUsagePercentage, int physicalMemory, String startTime) {
        this.name = name;
        this.processName = processName;
        this.pid = pid;
        this.memoryUsagePercentage = memoryUsagePercentage;
        this.physicalMemory = physicalMemory;
        this.startTime = startTime;
    }

    public Processo(boolean attivo, String name, String processName, String pid, String memoryUsagePercentage, int physicalMemory, String startTime) {
        this.setAttivo(attivo);
        this.name = name;
        this.processName = processName;
        this.pid = pid;
        this.memoryUsagePercentage = memoryUsagePercentage;
        this.physicalMemory = physicalMemory;
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getMemoryUsagePercentage() {
        return memoryUsagePercentage;
    }
    public void setMemoryUsagePercentage(String memoryUsagePercentage) {
        this.memoryUsagePercentage = memoryUsagePercentage;
    }
    public int getPhysicalMemory() {
        return physicalMemory;
    }
    public void setPhysicalMemory(int physicalMemory) {
        this.physicalMemory = physicalMemory;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }

}