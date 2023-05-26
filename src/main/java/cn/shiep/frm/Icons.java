package cn.shiep.frm;

/**
 * @Author yuanbao
 * @Date 2023/5/22
 * @Description
 */
public enum Icons {
    UPLOAD("src/main/Icons/upload.png"),
    ZIP("src/main/icons/zip (1).png"),
    NEWDIR("src/main/icons/文件夹 (1).png"),
    TXT("src/main/icons/Txt.png"),
    DOWNLOAD("src/main/icons/download.png"),
    DOC("src/main/icons/docx.png"),
    MUSIC("src/main/icons/音乐 (1).png"),
    NONE("src/main/icons/问号.png"),
    EXCEL("src/main/icons/excel2.png"),
    PPT("src/main/icons/ppt.png"),
    VIDEO("src/main/icons/视频直播.png"),
    IMAGE("src/main/icons/图片_填充.png"),
    ;

    private final String src;

    Icons(String src) {
        this.src = src;
    }

    public String getSrc() {
        return src;
    }
}
