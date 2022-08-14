package com.boy.softwarecrm.pojo;

import com.wen.releasedao.core.annotation.IdField;
import com.wen.releasedao.core.annotation.TableName;
import com.wen.releasedao.core.enums.IdTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * User实体类
 *
 * @author calwen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User implements Serializable {
    @IdField(idType = IdTypeEnum.AUTO)
    private Integer id;
    private String userName;
    private String loginName;
    private String password;
    private Integer userType;
    private String phoneNumber;
    private String email;
    private Date registerTime;

}
