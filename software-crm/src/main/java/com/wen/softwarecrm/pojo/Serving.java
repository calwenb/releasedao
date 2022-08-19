package com.wen.softwarecrm.pojo;

import com.wen.releasedao.core.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("serving")
public class Serving {
    private Integer id;
    private String userName;
    private String title;
    private String content;
    private String type;
    private String createName;
    private Date createTime;
}
