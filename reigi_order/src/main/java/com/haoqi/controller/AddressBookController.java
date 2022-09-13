package com.haoqi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.haoqi.common.BaseContext;
import com.haoqi.common.R;
import com.haoqi.entity.AddressBook;
import com.haoqi.service.impl.AddressBookServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author haoqi
 * @Date 2022/7/25 - 14:20
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookServiceImpl addressBookService;

    //设置默认地址
    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        log.info("/default: addressBook{}",addressBook);
        LambdaUpdateWrapper<AddressBook> queryWrapper = new LambdaUpdateWrapper<>();
        //根据当前登录用户id去匹配地址薄地址
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());

        //设置当前默认地址状态为 0  （0不是默认地址）
        queryWrapper.set(AddressBook::getIsDefault,0);  //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update(queryWrapper);    //执行sql

        //把当前要改的这条数据的地址状态改成1（1默认地址）
        addressBook.setIsDefault(1);    //SQL:update address_book set is_default = 1 where id = ?
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }
    //查询默认地址
    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        //根据当前登录的UserID来查
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        //条件查询，查地址状态为1的数据
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }

    //新增地址
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        log.info("/save:addressBook{}",addressBook);
        //设置userId，知道当前这个地址是谁的
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);

        return R.success(addressBook);
    }
    //查询全部地址
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook){
        log.info("/list:addressBook{}",addressBook);
        addressBook.setUserId(BaseContext.getCurrentId());
        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBook.getUserId() != null,AddressBook::getUserId,addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        List<AddressBook> list = addressBookService.list(queryWrapper);
        return R.success(list);
    }

    //根据id查询地址
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null){
            return R.success(addressBook);
        }else {
            return R.error("没有找到该对象！");
        }
    }

    //根据id删除地址
    @DeleteMapping
    public R<String> delete(@RequestParam("id") Long id){
        if(id==null){
            return R.error("请求异常");
        }
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getId,id).eq(AddressBook::getUserId,BaseContext.getCurrentId());
        addressBookService.remove(queryWrapper);
//        addressBookService.removeById(id); //直接使用这个removeById不太严谨
        return R.success("删除成功！");
    }

    //修改地址
    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return R.success("修改成功！");
    }



}
