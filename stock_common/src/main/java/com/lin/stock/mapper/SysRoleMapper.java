package com.lin.stock.mapper;

import com.lin.stock.pojo.entity.SysRole;

/**
* @author linzh
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2024-04-19 22:52:19
* @Entity com.lin.stock.pojo.entity.SysRole
*/
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

}
