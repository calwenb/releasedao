package com.wen.softwarecrm.pojo;

import com.wen.releasedao.core.annotation.ColumnId;
import com.wen.releasedao.core.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("address")
public class Address {
    @ColumnId
    private Integer id;
    private Integer userId;
    private String address;
}
