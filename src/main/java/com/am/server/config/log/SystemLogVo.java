package com.am.server.config.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author 阮雪峰
 * @date 2020年6月8日22:29:48
 */
@Getter
public class SystemLogVo {
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime dateTime;
    private final Integer level;
    private final String levelName;
    private final String message;

    public SystemLogVo(LocalDateTime dateTime, Integer level, String levelName, String message) {
        this.dateTime = dateTime;
        this.level = level;
        this.levelName = levelName;
        this.message = message;
    }
}
