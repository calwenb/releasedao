package com.wen.softwarecrm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author Administrator
 * @create 2022/7/18 17:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class ResultVO<T> implements Serializable {
    private Integer code;
    private T data;
    private String message;
}
