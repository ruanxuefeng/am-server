package com.am.server.api.upload.enumerate;

/**
 * 文件类型
 *
 * @author 阮雪峰
 * @date 2018/7/24 9:57
 */
public enum FileType {
    /**
     * 头像
     */
    avatar("/avatar"),
    ;

    /**
     * 文件夹
     */
    private final String folder;

    FileType(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }
}
