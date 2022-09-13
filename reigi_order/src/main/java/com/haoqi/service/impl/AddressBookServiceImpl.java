package com.haoqi.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haoqi.entity.AddressBook;
import com.haoqi.mapper.AddressBookMapper;
import com.haoqi.service.AddressBookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author haoqi
 * @Date 2022/7/24 - 15:59
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
