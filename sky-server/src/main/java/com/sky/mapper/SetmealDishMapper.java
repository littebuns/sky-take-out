package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    @Select("select count(1) from setmeal_dish where dish_id = #{dishId}")
    Long countByDishId(Long id);

    List<Long> getSetmealByDishIds(List<Long> dishIds);


    void batchInsert(List<SetmealDish> setmealDishes);


    void deleteBatchBySetmealIds(List<Long> setmealIds);
}
