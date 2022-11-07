package com.wen.softwarecrm.pojo;

import com.wen.releasedao.core.annotation.Column;
import com.wen.releasedao.core.annotation.ColumnId;
import com.wen.releasedao.core.annotation.ColumnJoin;
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
    @ColumnId(idType = IdTypeEnum.AUTO)
    private Integer id;
    private String userName;
    private String loginName;
    private String password;
    private Integer userType;
    private String phoneNumber;
    private String email;
    private Date registerTime;
    @ColumnJoin
    @Column(exist = false)
    private Address address;
}
