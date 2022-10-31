package com.wen.releasedao.core.vo;

import lombok.Data;

/**
 * @author calwen
 * @since 2022/10/31
 */
@Data
public class PageRequest {
    /**
     * 分页页码
     */
//    @NotNull(message = "分页页码字段pageNum为必传参数")
    private Integer page;

    /**
     * 分页大小
     */
//    @NotNull(message = "分页大小字段pageSize为必传参数")
//    @Max(value = 100, message = "分页大小不能大于500")
    private Integer size;
}
