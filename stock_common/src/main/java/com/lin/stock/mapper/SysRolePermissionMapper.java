package com.lin.stock.mapper;

import com.lin.stock.pojo.entity.SysRolePermission;

/**
* @author linzh
* @description 针对表【sys_role_permission(角色权限表)】的数据库操作Mapper
* @createDate 2024-04-19 22:52:19
* @Entity com.lin.stock.pojo.entity.SysRolePermission
*/
public interface SysRolePermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

}
