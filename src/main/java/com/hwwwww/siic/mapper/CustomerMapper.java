package com.hwwwww.siic.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hwwwww.siic.vo.Customer;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author Hwwwww
 */
@Mapper
@CacheNamespace(flushInterval = 30000)
public interface CustomerMapper extends BaseMapper<Customer> {
    @Select("select c.*,b.name bed_id_name from customer c,bed b where c.bed_id = b.id")
    @ResultMap("CustomerMap")
    List<Map<String, Object>> selectCustomerbyName(QueryWrapper<Map<String, Object>> queryWrapper);

    @Select("select id,customer_name, bed_id, building_id, room_number from customer")
    @ResultMap("BedTransfer")
    List<Map<String, Object>> selectCustomerBedInfo();
}