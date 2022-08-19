package com.boy.softwarecrm.pojo;

import com.wen.releasedao.core.annotation.IdField;
import com.wen.releasedao.core.annotation.TableName;
import com.wen.releasedao.core.enums.IdTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 客户 实体类
 * @author calwen
 * @since 2022/8/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("client")
public class Client {
    @IdField(idType = IdTypeEnum.AUTO)
    private Integer id;
    private String name;
    private String phone;
    private String address;
    private Integer level;
    private Integer creditLevel;
    private String manager;
    private String legalPerson;
    private String loseReason;
}
