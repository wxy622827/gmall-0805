package com.atguigu.gmall.pms.service.impl;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.pms.dao.CategoryDao;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.service.CategoryService;
import com.atguigu.gmall.pms.vo.CategoryVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryDao categoryDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public List<CategoryEntity> queryCategoriesByLevelOrPid(Integer level, Long pid) {

        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();

        // 分类层级查询
        if (level != 0) {
            wrapper.eq("cat_level", level);
        }

        // 父id查询
        if (pid != null) {
            wrapper.eq("parent_cid", pid);
        }

        return this.categoryDao.selectList(wrapper);
    }

//    @Override
//    public List<CategoryVO> queryCategoriesWithSub(Long pid) {
//
//         //根据以及分类的id查询二级分类
//        //同一张表做关联，a的id是b的父id   a.parent_id=1
//      return  this.categoryDao.queryCategoriesWithSub(pid);
//
//    }
    @Override
    public List<CategoryVO> queryCategoriesWithSub(Long pid) {

        return this.categoryDao.queryCategoriesWithSub(pid);
    }

}