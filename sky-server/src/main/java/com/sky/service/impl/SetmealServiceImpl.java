package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //保存套餐本体
        setmealMapper.insert(setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal.getId());
        }
        //保存套餐和菜品的关联信息
        setmealDishMapper.batchInsert(setmealDishes);

    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> setmeals = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(setmeals.getTotal(), setmeals.getResult());
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //起售的套餐无法删除
        List<Setmeal> setmeals = setmealMapper.listByIds(ids);
        boolean match = setmeals.stream().anyMatch(setmeal -> setmeal.getStatus() == 1);
        if(match){
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        //删除套餐
        setmealMapper.deleteBatch(ids);
        //删除套餐和菜品的关联信息
        setmealDishMapper.deleteBatchBySetmealIds(ids);
    }

}
