package cn.shiep.eneity;

/**
 * @Author yuanbao
 * @Date 2023/5/21
 * @Description
 */
public class Folder {
    private Integer folderID;
    private String folderName;
    private Integer parentFolderID;
    private String creationTime;
    private Integer ownerID;
    private String modificationTime;

    public Folder() {
    }

    public Folder(Integer folderID, String folderName, Integer parentFolderID, String creationTime, Integer ownerID, String modificationTime) {
        this.folderID = folderID;
        this.folderName = folderName;
        this.parentFolderID = parentFolderID;
        this.creationTime = creationTime;
        this.ownerID = ownerID;
        this.modificationTime = modificationTime;
    }

    public Integer getFolderID() {
        return folderID;
    }

    public void setFolderID(Integer folderID) {
        this.folderID = folderID;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Integer getParentFolderID() {
        return parentFolderID;
    }

    public void setParentFolderID(Integer parentFolderID) {
        this.parentFolderID = parentFolderID;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public String getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(String modificationTime) {
        this.modificationTime = modificationTime;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "folderID=" + folderID +
                ", folderName='" + folderName + '\'' +
                ", parentFolderID=" + parentFolderID +
                ", creationTime='" + creationTime + '\'' +
                ", ownerID=" + ownerID +
                ", modificationTime='" + modificationTime + '\'' +
                '}';
    }
}
