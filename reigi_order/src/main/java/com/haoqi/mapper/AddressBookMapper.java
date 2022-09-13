package com.haoqi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haoqi.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author haoqi
 * @Date 2022/7/24 - 15:59
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
