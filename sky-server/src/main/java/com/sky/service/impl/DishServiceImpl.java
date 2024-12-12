package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional
    public void saveDishFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
        }
        if (!flavors.isEmpty()) {
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> dishPage = dishMapper.page(dishPageQueryDTO);
        PageResult pageResult = new PageResult();
        pageResult.setRecords(dishPage.getResult());
        pageResult.setTotal(pageResult.getTotal());
        return pageResult;
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) throws Exception {

        for (Long id : ids) {
            Dish dish = dishMapper.get(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //查看菜品是否和套餐关联
        List<Long> setmealByDishIds = setmealDishMapper.getSetmealByDishIds(ids);
        if (!setmealByDishIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        for (Long id : ids) {
            dishMapper.delete(id);
            //删除和菜品关联的口味
            dishFlavorMapper.deleteByDishId(id);
        }

        dishMapper.deleteBatch(ids);
        dishFlavorMapper.deleteBatchByDishIds(ids);


    }

    @Override
    public DishVO get(Long id) {
        DishVO dishVO = dishMapper.getById(id);
        //获取口味列表
        List<DishFlavor> dishFlavors = dishFlavorMapper.listByDashId(id);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    @Override
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //更新菜品信息
        dishMapper.update(dish);
        //删除原有的口味信息
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        //更新口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dish.getId());
        }
        if (!flavors.isEmpty()) {
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    @Override
    public List<Dish> listByCategoryId(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

}
