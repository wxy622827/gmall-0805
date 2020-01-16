package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.CategoryEntity;
import java.util.List;
import lombok.Data;

/**
 * @author wxy
 * @create 2020-01-13 18:47
 */
@Data
public class CategoryVO extends CategoryEntity {
    private List<CategoryEntity> subs;
}
