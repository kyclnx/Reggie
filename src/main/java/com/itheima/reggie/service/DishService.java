package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.mapper.DishMapper;

public interface DishService extends IService<Dish> {


    //新增菜品，同时插入菜品对应的口味数据，需要两张表格：dish、dish_flavor
    public void saveWithFlavor(DishDto dto);
}
