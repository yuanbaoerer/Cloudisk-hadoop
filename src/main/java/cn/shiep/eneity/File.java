package cn.shiep.eneity;

/**
 * @Author yuanbao
 * @Date 2023/5/21
 * @Description
 */
public class File {
    private Integer fileID;
    private String fileName;
    private String filePath;
    private long fileSize;
    private String creationTime;
    private String modificationTime;
    private Integer folderID;
    private Integer ownerID;
    //扩展名（文件后缀）
    private String extension;
    //权限
    private String authority;

    private String operationTime;

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getFileRealName() {
        return fileRealName;
    }

    public void setFileRealName(String fileRealName) {
        this.fileRealName = fileRealName;
    }

    private String fileRealName;

    public File() {
    }

    public File(Integer fileID, String fileName, String filePath, long fileSize, String creationTime, String modificationTime, Integer folderID, Integer ownerID, String extension, String authority, String operationTime, String fileRealName) {
        this.fileID = fileID;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.creationTime = creationTime;
        this.modificationTime = modificationTime;
        this.folderID = folderID;
        this.ownerID = ownerID;
        this.extension = extension;
        this.authority = authority;
        this.operationTime = operationTime;
        this.fileRealName = fileRealName;
    }

    public Integer getFileID() {
        return fileID;
    }

    public void setFileID(Integer fileID) {
        this.fileID = fileID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getmodificationTime() {
        return modificationTime;
    }

    public void setmodificationTime(String modificationTime) {
        this.modificationTime = modificationTime;
    }

    public Integer getFolderID() {
        return folderID;
    }

    public void setFolderID(Integer folderID) {
        this.folderID = folderID;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "File{" +
                "fileID=" + fileID +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", modificationTime='" + modificationTime + '\'' +
                ", folderID=" + folderID +
                ", ownerID=" + ownerID +
                ", extension='" + extension + '\'' +
                ", authority='" + authority + '\'' +
                ", operationTime='" + operationTime + '\'' +
                ", fileRealName='" + fileRealName + '\'' +
                '}';
    }
}
