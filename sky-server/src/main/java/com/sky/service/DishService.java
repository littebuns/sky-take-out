package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    public void saveDishFlavor(DishDTO dishDTO);

    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids) throws Exception;

    DishVO get(Long id);

    void updateDish(DishDTO dishDTO);

    List<Dish> listByCategoryId(Long categoryId);
}
