package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    void saveWithDish(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealDTO);

    void deleteBatch(List<Long> ids);

    SetmealVO getById(Long id);

    void updateWithDish(SetmealDTO setmealDTO);

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);
}
