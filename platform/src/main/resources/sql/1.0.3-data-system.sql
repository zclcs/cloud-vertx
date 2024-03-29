call insert_if_not_exists(database(), 'system_generator_config',
                          'server_name,author,gen_version,base_package,entity_package,ao_package,vo_package,cache_vo_package,excel_vo_package,mapper_package,mapper_xml_package,service_package,service_impl_package,controller_package,is_trim,trim_value,exclude_columns,create_at,create_by',
                          '"platform-system","zclcs","02","com.zclcs.platform.system","api.bean.entity","api.bean.ao","api.bean.vo","api.bean.cache","api.bean.excel","mapper","mapper","service","service.impl","controller","1","system_","version,tenant_id,create_at,update_at,create_by,update_by,create_name,create_date,update_name,update_date,delete_name,delete_date,deleted",now(),"system"',
                          'server_name="platform-system"');
call insert_if_not_exists(database(), 'system_generator_config',
                          'server_name,author,gen_version,base_package,entity_package,ao_package,vo_package,cache_vo_package,excel_vo_package,mapper_package,mapper_xml_package,service_package,service_impl_package,controller_package,is_trim,trim_value,exclude_columns,create_at,create_by',
                          '"test-test","zclcs","02","com.zclcs.test.test","api.bean.entity","api.bean.ao","api.bean.vo","api.bean.cache","api.bean.excel","mapper","mapper","service","service.impl","controller","1","test_","version,tenant_id,create_at,update_at,create_by,update_by,create_name,create_date,update_name,update_date,delete_name,delete_date,deleted",now(),"system"',
                          'server_name="test-test"');
call insert_if_not_exists(database(), 'system_generator_config',
                          'server_name,author,gen_version,base_package,entity_package,ao_package,vo_package,cache_vo_package,excel_vo_package,mapper_package,mapper_xml_package,service_package,service_impl_package,controller_package,is_trim,trim_value,exclude_columns,create_at,create_by',
                          '"hardware","zclcs","01","com.thsoft","api.bean.entity","api.bean.ao","api.bean.vo","","","mapper","mapper.xml","service","service.impl","controller.business","1","hard_","version,tenant_id,create_at,update_at,create_by,update_by,create_name,create_date,update_name,update_date,delete_name,delete_date,deleted",now(),"system"',
                          'server_name="hardware"');
call insert_if_not_exists(database(), 'system_dept', 'dept_code,parent_code,dept_name,order_num,create_at,create_by',
                          '"DEV","0","开发部",1.0,now(),"system"', 'dept_code="DEV"');
call insert_if_not_exists(database(), 'system_oauth_client_details',
                          'client_id,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove,create_at,create_by',
                          '"swagger","","123456","server","password,app,refresh_token,authorization_code,client_credentials","https://zclcs","",86400,86400,"","true",now(),"system"',
                          'client_id="swagger"');
call insert_if_not_exists(database(), 'system_oauth_client_details',
                          'client_id,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove,create_at,create_by',
                          '"zclcs","","123456","server","password,app,refresh_token,authorization_code,client_credentials","https://zclcs","",86400,86400,"","true",now(),"system"',
                          'client_id="zclcs"');
call insert_if_not_exists(database(), 'system_role', 'role_code,role_name,remark,create_at,create_by',
                          '"ADMIN","管理员","",now(),"system"', 'role_code="ADMIN"');
call insert_if_not_exists(database(), 'system_role', 'role_code,role_name,remark,create_at,create_by',
                          '"GUEST","查看","",now(),"system"', 'role_code="GUEST"');
call insert_if_not_exists(database(), 'system_role', 'role_code,role_name,remark,create_at,create_by',
                          '"DEVELOP","开发","",now(),"system"', 'role_code="DEVELOP"');
call insert_if_not_exists(database(), 'system_user',
                          'username,real_name,password,dept_id,status,avatar,create_at,create_by',
                          '"admin","管理员","$2a$10$LNmOtAAmYxewGYcxdwiYteFUPG1KWHYtgqwvtsEPyRxs70nT1hZF2",1,"1","default.jpg",now(),"system"',
                          'username="admin"');
call insert_if_not_exists(database(), 'system_user',
                          'username,real_name,password,dept_id,status,avatar,create_at,create_by',
                          '"guest","访客","$2a$10$63vb4uvE5bfwvP5BPHVJtObCVop2Tu/8rSW2Dqvm5MYbnBDlCNTBK",1,"1","default.jpg",now(),"system"',
                          'username="guest"');
call insert_if_not_exists(database(), 'system_user',
                          'username,real_name,password,dept_id,status,avatar,create_at,create_by',
                          '"develop","开发","$2a$10$63vb4uvE5bfwvP5BPHVJtObCVop2Tu/8rSW2Dqvm5MYbnBDlCNTBK",1,"1","default.jpg",now(),"system"',
                          'username="develop"');
call insert_if_not_exists(database(), 'system_user_data_permission', 'user_id,dept_id,create_at,create_by',
                          '(select user_id from system_user where username = "admin"),(select dept_id from system_dept where dept_code = "DEV"),now(),"system"',
                          'user_id=(select user_id from system_user where username = "admin") and dept_id=(select dept_id from system_dept where dept_code = "DEV")');
call insert_if_not_exists(database(), 'system_user_data_permission', 'user_id,dept_id,create_at,create_by',
                          '(select user_id from system_user where username = "develop"),(select dept_id from system_dept where dept_code = "DEV"),now(),"system"',
                          'user_id=(select user_id from system_user where username = "develop") and dept_id=(select dept_id from system_dept where dept_code = "DEV")');
call insert_if_not_exists(database(), 'system_user_role', 'user_id,role_id,create_at,create_by',
                          '(select user_id from system_user where username = "admin"),(select role_id from system_role where role_code = "ADMIN"),now(),"system"',
                          'user_id=(select user_id from system_user where username = "admin") and role_id=(select role_id from system_role where role_code = "ADMIN")');
call insert_if_not_exists(database(), 'system_user_role', 'user_id,role_id,create_at,create_by',
                          '(select user_id from system_user where username = "guest"),(select role_id from system_role where role_code = "GUEST"),now(),"system"',
                          'user_id=(select user_id from system_user where username = "guest") and role_id=(select role_id from system_role where role_code = "GUEST")');
call insert_if_not_exists(database(), 'system_user_role', 'user_id,role_id,create_at,create_by',
                          '(select user_id from system_user where username = "develop"),(select role_id from system_role where role_code = "DEVELOP"),now(),"system"',
                          'user_id=(select user_id from system_user where username = "develop") and role_id=(select role_id from system_role where role_code = "DEVELOP")');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM","0","系统管理","","/system","Layout","","","ant-design:setting-outlined","2","0","0","0","0","",2.0,now(),"system"',
                          'menu_code="SYSTEM"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_USER","SYSTEM","用户管理","AccountManagement","user","/cloud/system/user/index","","","ant-design:user-switch-outlined","0","0","0","0","0","",1.0,now(),"system"',
                          'menu_code="SYSTEM_USER"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_USER_DETAIL","SYSTEM","用户详情页面","AccountDetail","accountDetail/:username","/cloud/system/user/AccountDetail","","user:detail:view","ant-design:audit-outlined","0","1","1","0","0","",2.0,now(),"system"',
                          'menu_code="SYSTEM_USER_DETAIL"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_DEPT","SYSTEM","部门管理","DeptManagement","dept","/cloud/system/dept/index","","","ant-design:apartment-outlined","0","0","0","0","0","",4.0,now(),"system"',
                          'menu_code="SYSTEM_DEPT"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_USER:ADD","SYSTEM_USER","添加用户","","","","","user:add","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_USER:ADD"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_USER:UPDATE","SYSTEM_USER","修改用户","","","","","user:update","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_USER:UPDATE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_USER:DELETE","SYSTEM_USER","删除用户","","","","","user:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_USER:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_USER:RESET_PASSWORD","SYSTEM_USER","重置用户密码","","","","","user:resetPassword","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_USER:RESET_PASSWORD"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MENU","SYSTEM","菜单管理","MenuManagement","menu","/cloud/system/menu/index","","","ant-design:menu-fold-outlined","0","0","0","0","0","",3.0,now(),"system"',
                          'menu_code="SYSTEM_MENU"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MENU:ADD","SYSTEM_MENU","添加菜单","","","","","menu:add","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_MENU:ADD"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_ROLE","SYSTEM","角色管理","RoleManagement","role","/cloud/system/role/index","","","ant-design:solution-outlined","0","0","0","0","0","",2.0,now(),"system"',
                          'menu_code="SYSTEM_ROLE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MENU:UPDATE","SYSTEM_MENU","修改菜单","","","","","menu:update","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_MENU:UPDATE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MENU:DELETE","SYSTEM_MENU","删除菜单","","","","","menu:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_MENU:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_ROLE:ADD","SYSTEM_ROLE","添加角色","","","","","role:add","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_ROLE:ADD"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_ROLE:UPDATE","SYSTEM_ROLE","修改角色","","","","","role:update","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_ROLE:UPDATE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_DEPT:ADD","SYSTEM_DEPT","添加部门","","","","","dept:add","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_DEPT:ADD"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_DEPT:UPDATE","SYSTEM_DEPT","修改部门","","","","","dept:update","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_DEPT:UPDATE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_DEPT:DELETE","SYSTEM_DEPT","删除部门","","","","","dept:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_DEPT:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_ROLE:DELETE","SYSTEM_ROLE","删除角色","","","","","role:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_ROLE:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_USER:RESET_PASSWORD_MENU","SYSTEM","重置密码","ChangePassword","password","/cloud/system/password/index","","user:view","ant-design:radius-setting-outlined","0","0","0","0","0","",5.0,now(),"system"',
                          'menu_code="SYSTEM_USER:RESET_PASSWORD_MENU"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"DASHBOARD","0","首页","","/dashboard","Layout","/dashboard/analysis","","ant-design:align-left-outlined","2","0","1","1","0","",1.0,now(),"system"',
                          'menu_code="DASHBOARD"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"ANALYSIS","DASHBOARD","分析页","","analysis","/dashboard/analysis/index","","","ant-design:amazon-outlined","0","0","0","1","0","/dashboard",1.0,now(),"system"',
                          'menu_code="ANALYSIS"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"WORKBENCH","DASHBOARD","工作台","","workbench","/dashboard/workbench/index","","","ant-design:alert-filled","0","0","0","1","0","/dashboard",2.0,now(),"system"',
                          'menu_code="WORKBENCH"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GEN","0","快速开发","","/gen","Layout","","","ant-design:code-filled","2","0","0","0","0","",4.0,now(),"system"',
                          'menu_code="GEN"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GEN_CONFIG","GEN","代码生成配置管理","GenConfig","config","/cloud/generator/config/index","","gen:config","ant-design:contacts-outlined","0","0","0","0","0","",1.0,now(),"system"',
                          'menu_code="GEN_CONFIG"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GEN_GEN","GEN","代码生成","Gen","gen","/cloud/generator/gen/index","","gen:generate","ant-design:code-sandbox-outlined","0","0","0","0","0","",2.0,now(),"system"',
                          'menu_code="GEN_GEN"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GEN_CONFIG:UPDATE","GEN_CONFIG","修改","","","","","generatorConfig:update","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GEN_CONFIG:UPDATE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GEN_GEN:GEN","GEN_GEN","生成代码","","","","","gen:generate:gen","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GEN_GEN:GEN"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_DICT","SYSTEM","字典管理","Dict","dict","/cloud/system/dict/index","","","ant-design:barcode-outlined","0","0","0","0","0","",6.0,now(),"system"',
                          'menu_code="SYSTEM_DICT"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_DICT:ADD","SYSTEM_DICT","新增字典","","","","","dictItem:add","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_DICT:ADD"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_DICT:UPDATE","SYSTEM_DICT","删除字典","","","","","dictItem:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_DICT:UPDATE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_DICT:DELETE","SYSTEM_DICT","修改字典","","","","","dictItem:update","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_DICT:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_CLIENT","SYSTEM","客户端管理","Client","client","/cloud/system/client/index","","","ant-design:paper-clip-outlined","0","0","0","0","0","",7.0,now(),"system"',
                          'menu_code="SYSTEM_CLIENT"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_CLIENT:ADD","SYSTEM_CLIENT","添加","","","","","oauthClientDetails:add","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_CLIENT:ADD"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_CLIENT:UPDATE","SYSTEM_CLIENT","更新","","","","","oauthClientDetails:update","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_CLIENT:UPDATE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_CLIENT:DELETE","SYSTEM_CLIENT","删除","","","","","oauthClientDetails:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_CLIENT:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_CLIENT:DECRYPT","SYSTEM_CLIENT","获取密钥","","","","","oauthClientDetails:decrypt","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_CLIENT:DECRYPT"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY","0","网关管理","","/route","Layout","","","ant-design:gateway-outlined","2","0","0","0","0","",3.0,now(),"system"',
                          'menu_code="GATEWAY"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_ROUTE_LOG","GATEWAY","网关日志","RouteLog","log","/cloud/route/log/index","","","ant-design:login-outlined","0","0","0","0","0","",1.0,now(),"system"',
                          'menu_code="GATEWAY_ROUTE_LOG"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_RATE_LIMIT_RULE","GATEWAY","限流规则","RateLimitRule","rate/rule","/cloud/route/rate/rule/index","","","ant-design:alert-filled","0","0","0","0","0","",2.0,now(),"system"',
                          'menu_code="GATEWAY_RATE_LIMIT_RULE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_RATE_LIMIT_LOG","GATEWAY","限流日志","RateLimitLog","rate/log","/cloud/route/rate/log/index","","","ant-design:ant-design-outlined","0","0","0","0","0","",3.0,now(),"system"',
                          'menu_code="GATEWAY_RATE_LIMIT_LOG"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_BLACK_LIST","GATEWAY","黑名单管理","BlackListPage","black","/cloud/route/black/index","","","ant-design:eye-invisible-filled","0","0","0","0","0","",4.0,now(),"system"',
                          'menu_code="GATEWAY_BLACK_LIST"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_BLOCK_LOG","GATEWAY","黑名单日志","BlockLog","block","/cloud/route/block/index","","","ant-design:tablet-outlined","0","0","0","0","0","",5.0,now(),"system"',
                          'menu_code="GATEWAY_BLOCK_LOG"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_ROUTE_LOG:DELETE","GATEWAY_ROUTE_LOG","删除网关日志","","","","","routeLog:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_ROUTE_LOG:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_RATE_LIMIT_RULE:ADD","GATEWAY_RATE_LIMIT_RULE","新增限流规则","","","","","rateLimitRule:add","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_RATE_LIMIT_RULE:ADD"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_RATE_LIMIT_RULE:DELETE","GATEWAY_RATE_LIMIT_RULE","删除限流规则","","","","","rateLimitRule:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_RATE_LIMIT_RULE:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_RATE_LIMIT_RULE:UPDATE","GATEWAY_RATE_LIMIT_RULE","修改限流规则","","","","","rateLimitRule:update","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_RATE_LIMIT_RULE:UPDATE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_RATE_LIMIT_LOG:DELETE","GATEWAY_RATE_LIMIT_LOG","删除限流拦截日志","","","","","rateLimitLog:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_RATE_LIMIT_LOG:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_BLACK_LIST:ADD","GATEWAY_BLACK_LIST","新增网关黑名单","","","","","blackList:add","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_BLACK_LIST:ADD"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_BLACK_LIST:UPDATE","GATEWAY_BLACK_LIST","修改网关黑名单","","","","","blackList:update","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_BLACK_LIST:UPDATE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_BLACK_LIST:DELETE","GATEWAY_BLACK_LIST","删除网关黑名单","","","","","blackList:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_BLACK_LIST:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_BLOCK_LOG:DELETE","GATEWAY_BLOCK_LOG","删除黑名单日志","","","","","blockLog:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_BLOCK_LOG:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_USER:VIEW","SYSTEM_USER","查看用户","","","","","user:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_USER:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_ROLE:VIEW","SYSTEM_ROLE","查看角色","","","","","role:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_ROLE:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MENU:VIEW","SYSTEM_MENU","查看菜单","","","","","menu:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_MENU:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_DEPT:VIEW","SYSTEM_DEPT","查看部门","","","","","dept:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_DEPT:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_DICT:VIEW","SYSTEM_DICT","查看字典","","","","","dictItem:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_DICT:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_CLIENT:VIEW","SYSTEM_CLIENT","查看客户端","","","","","oauthClientDetails:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_CLIENT:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_ROUTE_LOG:VIEW","GATEWAY_ROUTE_LOG","查看网关日志","","","","","routeLog:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_ROUTE_LOG:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_RATE_LIMIT_RULE:VIEW","GATEWAY_RATE_LIMIT_RULE","查看限流规则","","","","","rateLimitRule:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_RATE_LIMIT_RULE:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_RATE_LIMIT_LOG:VIEW","GATEWAY_RATE_LIMIT_LOG","查看限流日志","","","","","rateLimitLog:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_RATE_LIMIT_LOG:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_BLACK_LIST:VIEW","GATEWAY_BLACK_LIST","查看黑名单","","","","","blackList:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_BLACK_LIST:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_BLOCK_LOG:VIEW","GATEWAY_BLOCK_LOG","查看黑名单日志","","","","","blockLog:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_BLOCK_LOG:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_USER:UPDATE_STATUS","SYSTEM_USER","禁用账号","","","","","user:updateStatus","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_USER:UPDATE_STATUS"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_USER:VIEW_LOG","SYSTEM_USER","查看用户操作日志","","","","","log:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_USER:VIEW_LOG"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"FILE_MANAGER","SYSTEM","文件管理","","fileManager","","","","ant-design:file-unknown-outlined","0","0","0","0","0","",8.0,now(),"system"',
                          'menu_code="FILE_MANAGER"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"FILE_MANAGER_BUCKET","FILE_MANAGER","桶","Bucket","bucket","/cloud/system/bucket/index","","","ant-design:folder-add-filled","0","0","0","0","0","",1.0,now(),"system"',
                          'menu_code="FILE_MANAGER_BUCKET"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"FILE_MANAGER_FILE","FILE_MANAGER","文件","File","file","/cloud/system/file/index","","","ant-design:file-add-filled","0","0","0","0","0","",2.0,now(),"system"',
                          'menu_code="FILE_MANAGER_FILE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"FILE_MANAGER_BUCKET:VIEW","FILE_MANAGER_BUCKET","查看桶","","","","","bucket:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="FILE_MANAGER_BUCKET:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"FILE_MANAGER_BUCKET:ADD","FILE_MANAGER_BUCKET","添加桶","","","","","bucket:add","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="FILE_MANAGER_BUCKET:ADD"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"FILE_MANAGER_BUCKET:DELETE","FILE_MANAGER_BUCKET","删除桶","","","","","bucket:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="FILE_MANAGER_BUCKET:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"FILE_MANAGER_FILE:VIEW","FILE_MANAGER_FILE","查看文件","","","","","file:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="FILE_MANAGER_FILE:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"FILE_MANAGER_FILE:DELETE","FILE_MANAGER_FILE","删除文件","","","","","file:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="FILE_MANAGER_FILE:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"FILE_MANAGER_BUCKET:UPDATE","FILE_MANAGER_BUCKET","修改桶","","","","","bucket:update","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="FILE_MANAGER_BUCKET:UPDATE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GEN_CONFIG:ADD","GEN_CONFIG","添加","","","","","generatorConfig:add","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GEN_CONFIG:ADD"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GEN_CONFIG:VIEW","GEN_CONFIG","查看","","","","","generatorConfig:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GEN_CONFIG:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GEN_CONFIG:DELETE","GEN_CONFIG","删除","","","","","generatorConfig:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GEN_CONFIG:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MONITOR","0","系统监控","","/monitor","Layout","","","ant-design:monitor-outlined","2","0","0","0","0","",5.0,now(),"system"',
                          'menu_code="SYSTEM_MONITOR"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MONITOR_REDIS","SYSTEM_MONITOR","redis控制台","RedisConsole","redis","/cloud/monitor/redis/index","","","ant-design:credit-card-twotone","0","0","0","0","0","",1.0,now(),"system"',
                          'menu_code="SYSTEM_MONITOR_REDIS"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MONITOR_REDIS:VIEW","SYSTEM_MONITOR_REDIS","查看","","","","","redis:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_MONITOR_REDIS:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MONITOR_REDIS:DELETE","SYSTEM_MONITOR_REDIS","删除","","","","","redis:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_MONITOR_REDIS:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MONITOR_MYSQL","SYSTEM_MONITOR","mysql控制台","MysqlConsole","mysql","/cloud/monitor/mysql/index","","dataBase:view","ant-design:console-sql-outlined","0","0","0","0","0","",2.0,now(),"system"',
                          'menu_code="SYSTEM_MONITOR_MYSQL"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MONITOR_NACOS","SYSTEM_MONITOR","nacos控制台","","nacos","","","nacos:view","ant-design:reconciliation-filled","0","0","0","0","0","",3.0,now(),"system"',
                          'menu_code="SYSTEM_MONITOR_NACOS"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MONITOR_NACOS_CONFIG","SYSTEM_MONITOR_NACOS","配置列表","NacosConfigPage","config","/cloud/monitor/nacos/config/index","","","ant-design:box-plot-filled","0","0","0","0","0","",1.0,now(),"system"',
                          'menu_code="SYSTEM_MONITOR_NACOS_CONFIG"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MONITOR_NACOS_SERVICE","SYSTEM_MONITOR_NACOS","服务列表","NacosServicePage","service","/cloud/monitor/nacos/service/index","","","ant-design:ci-circle-filled","0","0","0","0","0","",2.0,now(),"system"',
                          'menu_code="SYSTEM_MONITOR_NACOS_SERVICE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MONITOR_RABBITMQ","SYSTEM_MONITOR","rabbitmq控制台","","rabbitmq","","","rabbitmq:view","ant-design:pull-request-outlined","0","0","0","0","0","",5.0,now(),"system"',
                          'menu_code="SYSTEM_MONITOR_RABBITMQ"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MONITOR_RABBITMQ_EXCHANGE","SYSTEM_MONITOR_RABBITMQ","交换机","RabbitmqExchange","exchange","/cloud/monitor/rabbitmq/exchange/index","","","ant-design:node-expand-outlined","0","0","0","0","0","",1.0,now(),"system"',
                          'menu_code="SYSTEM_MONITOR_RABBITMQ_EXCHANGE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MONITOR_RABBITMQ_QUEUE","SYSTEM_MONITOR_RABBITMQ","队列","RabbitmqQueue","queue","/cloud/monitor/rabbitmq/queue/index","","","ant-design:pull-request-outlined","0","0","0","0","0","",2.0,now(),"system"',
                          'menu_code="SYSTEM_MONITOR_RABBITMQ_QUEUE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_LOGIN_LOG","SYSTEM","系统登录日志","LoginLog","loginLog","/cloud/system/loginLog/index","","","ant-design:login-outlined","0","0","0","0","0","",10.0,now(),"system"',
                          'menu_code="SYSTEM_LOGIN_LOG"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_LOGIN_LOG:DELETE","SYSTEM_LOGIN_LOG","删除登录日志","","","","","loginLog:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_LOGIN_LOG:DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"STSTEM_LOGIN_LOG:VIEW","SYSTEM_LOGIN_LOG","查看系统登录日志","","","","","loginLog:view","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="STSTEM_LOGIN_LOG:VIEW"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_USER:EXCEL","SYSTEM_USER","导出excel","","","","","user:excel","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="SYSTEM_USER:EXCEL"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_ROUTE_LOG:EXPORT","GATEWAY_ROUTE_LOG","导出网关日志","","","","","routeLog:export","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_ROUTE_LOG:EXPORT"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"GATEWAY_ROUTE_LOG:IMPORT","GATEWAY_ROUTE_LOG","导入网关日志","","","","","routeLog:import","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="GATEWAY_ROUTE_LOG:IMPORT"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"TEST","0","测试","","/test","Layout","","","ant-design:appstore-add-outlined","2","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="TEST"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"TEST_COMPANY","TEST","企业信息","Company","company","","","","ant-design:appstore-add-outlined","0","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="TEST_COMPANY"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"TEST_COMPANY_PAGE","TEST_COMPANY","分页","","","","","company:page","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="TEST_COMPANY_PAGE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"TEST_COMPANY_LIST","TEST_COMPANY","集合","","","","","company:list","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="TEST_COMPANY_LIST"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"TEST_COMPANY_ONE","TEST_COMPANY","单个","","","","","company:one","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="TEST_COMPANY_ONE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"TEST_COMPANY_ADD","TEST_COMPANY","新增","","","","","company:add","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="TEST_COMPANY_ADD"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"TEST_COMPANY_ADD:BATCH","TEST_COMPANY","批量新增","","","","","company:add:batch","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="TEST_COMPANY_ADD:BATCH"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"TEST_COMPANY_DELETE","TEST_COMPANY","删除","","","","","company:delete","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="TEST_COMPANY_DELETE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"TEST_COMPANY_UPDATE","TEST_COMPANY","修改","","","","","company:update","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="TEST_COMPANY_UPDATE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"TEST_COMPANY_UPDATE:BATCH","TEST_COMPANY","批量修改","","","","","company:update:batch","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="TEST_COMPANY_UPDATE:BATCH"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"TEST_COMPANY_CREATEORUPDATE:BATCH","TEST_COMPANY","批量新增或修改","","","","","company:createOrUpdate:batch","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="TEST_COMPANY_CREATEORUPDATE:BATCH"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"TEST_COMPANY_CREATEORUPDATE","TEST_COMPANY","新增或修改","","","","","company:createOrUpdate","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="TEST_COMPANY_CREATEORUPDATE"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"TEST_COMPANY_EXPORT","TEST_COMPANY","导出excel","","","","","company:export","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="TEST_COMPANY_EXPORT"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"TEST_COMPANY_IMPORT","TEST_COMPANY","导入excel","","","","","company:import","","1","0","0","0","0","",0.0,now(),"system"',
                          'menu_code="TEST_COMPANY_IMPORT"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MONITOR_POWER_JOB","SYSTEM_MONITOR","powerJob控制台","PowerJobJobInfo","powerJob","/cloud/monitor/powerJob/index","","powerJob:view","ant-design:mobile-outlined","0","0","0","0","0","",4.0,now(),"system"',
                          'menu_code="SYSTEM_MONITOR_POWER_JOB"');
call insert_if_not_exists(database(), 'system_menu',
                          'menu_code,parent_code,menu_name,keep_alive_name,path,component,redirect,perms,icon,type,hide_menu,ignore_keep_alive,hide_breadcrumb,hide_children_in_menu,current_active_menu,order_num,create_at,create_by',
                          '"SYSTEM_MONITOR_POWER_JOB_INSTANCE","SYSTEM_MONITOR","powerJob任务实例","PowerJobInstance","powerJobInstance/:jobId/:appId/:jobName/:jobDescription","/cloud/monitor/powerJob/instance","","powerJob:view","ant-design:aliwangwang-filled","0","1","0","0","0","",4.0,now(),"system"',
                          'menu_code="SYSTEM_MONITOR_POWER_JOB_INSTANCE"');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_USER"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_USER_DETAIL"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER_DETAIL")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_DEPT"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DEPT")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:RESET_PASSWORD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:RESET_PASSWORD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_MENU"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MENU")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_MENU:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MENU:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_ROLE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_ROLE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_MENU:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MENU:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_MENU:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MENU:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:RESET_PASSWORD_MENU"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:RESET_PASSWORD_MENU")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "DASHBOARD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "DASHBOARD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "ANALYSIS"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "ANALYSIS")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "WORKBENCH"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "WORKBENCH")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_DICT"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DICT")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_DICT:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DICT:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_DICT:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DICT:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_DICT:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DICT:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:DECRYPT"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:DECRYPT")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_MENU:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MENU:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_DICT:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DICT:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:UPDATE_STATUS"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:UPDATE_STATUS")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:VIEW_LOG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:VIEW_LOG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "FILE_MANAGER"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_LOGIN_LOG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_LOGIN_LOG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_LOGIN_LOG:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_LOGIN_LOG:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "STSTEM_LOGIN_LOG:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "STSTEM_LOGIN_LOG:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "ADMIN"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:EXCEL"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "ADMIN") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:EXCEL")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_USER"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_USER_DETAIL"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER_DETAIL")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_DEPT"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DEPT")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_MENU"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MENU")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_ROLE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_ROLE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:RESET_PASSWORD_MENU"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:RESET_PASSWORD_MENU")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "DASHBOARD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "DASHBOARD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "ANALYSIS"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "ANALYSIS")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "WORKBENCH"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "WORKBENCH")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_DICT"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DICT")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "GATEWAY"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_MENU:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MENU:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_DICT:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DICT:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:VIEW_LOG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:VIEW_LOG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "FILE_MANAGER"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "SYSTEM_LOGIN_LOG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_LOGIN_LOG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "GUEST"),(select menu_id from system_menu where menu_code = "STSTEM_LOGIN_LOG:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "GUEST") and menu_id=(select menu_id from system_menu where menu_code = "STSTEM_LOGIN_LOG:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_USER"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_USER_DETAIL"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER_DETAIL")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_DEPT"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DEPT")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:RESET_PASSWORD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:RESET_PASSWORD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MENU"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MENU")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MENU:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MENU:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_ROLE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_ROLE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MENU:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MENU:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MENU:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MENU:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:RESET_PASSWORD_MENU"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:RESET_PASSWORD_MENU")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "DASHBOARD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "DASHBOARD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "ANALYSIS"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "ANALYSIS")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "WORKBENCH"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "WORKBENCH")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GEN"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GEN")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GEN_CONFIG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GEN_CONFIG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GEN_GEN"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GEN_GEN")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GEN_CONFIG:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GEN_CONFIG:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GEN_GEN:GEN"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GEN_GEN:GEN")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_DICT"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DICT")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_DICT:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DICT:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_DICT:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DICT:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_DICT:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DICT:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:DECRYPT"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:DECRYPT")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_ROLE:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MENU:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MENU:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DEPT:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_DICT:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_DICT:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_CLIENT:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_RULE:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_RATE_LIMIT_LOG:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLACK_LIST:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_BLOCK_LOG:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:UPDATE_STATUS"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:UPDATE_STATUS")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:VIEW_LOG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:VIEW_LOG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "FILE_MANAGER"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_FILE:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "FILE_MANAGER_BUCKET:UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GEN_CONFIG:ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GEN_CONFIG:ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GEN_CONFIG:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GEN_CONFIG:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GEN_CONFIG:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GEN_CONFIG:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_REDIS"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_REDIS")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_REDIS:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_REDIS:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_REDIS:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_REDIS:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_MYSQL"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_MYSQL")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_NACOS"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_NACOS")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_NACOS_CONFIG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_NACOS_CONFIG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_NACOS_SERVICE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_NACOS_SERVICE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_RABBITMQ"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_RABBITMQ")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_RABBITMQ_EXCHANGE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_RABBITMQ_EXCHANGE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_RABBITMQ_QUEUE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_RABBITMQ_QUEUE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_LOGIN_LOG"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_LOGIN_LOG")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_LOGIN_LOG:DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_LOGIN_LOG:DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "STSTEM_LOGIN_LOG:VIEW"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "STSTEM_LOGIN_LOG:VIEW")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_USER:EXCEL"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_USER:EXCEL")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG:EXPORT"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG:EXPORT")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG:IMPORT"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "GATEWAY_ROUTE_LOG:IMPORT")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "TEST"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "TEST")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "TEST_COMPANY"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "TEST_COMPANY")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "TEST_COMPANY_PAGE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "TEST_COMPANY_PAGE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "TEST_COMPANY_LIST"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "TEST_COMPANY_LIST")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "TEST_COMPANY_ONE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "TEST_COMPANY_ONE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "TEST_COMPANY_ADD"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "TEST_COMPANY_ADD")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "TEST_COMPANY_ADD:BATCH"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "TEST_COMPANY_ADD:BATCH")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "TEST_COMPANY_DELETE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "TEST_COMPANY_DELETE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "TEST_COMPANY_UPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "TEST_COMPANY_UPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "TEST_COMPANY_UPDATE:BATCH"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "TEST_COMPANY_UPDATE:BATCH")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "TEST_COMPANY_CREATEORUPDATE:BATCH"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "TEST_COMPANY_CREATEORUPDATE:BATCH")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "TEST_COMPANY_CREATEORUPDATE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "TEST_COMPANY_CREATEORUPDATE")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "TEST_COMPANY_EXPORT"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "TEST_COMPANY_EXPORT")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "TEST_COMPANY_IMPORT"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "TEST_COMPANY_IMPORT")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_POWER_JOB"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_POWER_JOB")');
call insert_if_not_exists(database(), 'system_role_menu', 'role_id,menu_id,create_at,create_by',
                          '(select role_id from system_role where role_code = "DEVELOP"),(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_POWER_JOB_INSTANCE"),now(),"system"',
                          'role_id=(select role_id from system_role where role_code = "DEVELOP") and menu_id=(select menu_id from system_menu where menu_code = "SYSTEM_MONITOR_POWER_JOB_INSTANCE")');
