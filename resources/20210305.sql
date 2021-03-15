update auth_menu_inf set MENU_NAME='校内执勤管理' where MENU_NAME='教师巡查管理';
update auth_menu_inf set MENU_NAME='校外执勤管理' where MENU_NAME='护校队巡查管理';

update t_inspection_message  set title = REPLACE(title,'教师执勤','校内执勤');
update t_inspection_message  set title = REPLACE(title,'护校队巡查','校外执勤');

update t_notice_mx set template_name = REPLACE(template_name,'教师执勤','校内执勤');
update t_notice_mx set template_name = REPLACE(template_name,'护校队巡查','校外执勤');

update t_inspection_template set name  = REPLACE(name,'教师执勤','校内执勤');
update t_inspection_template set name  = REPLACE(name,'护校队巡查','校外执勤');

update auth_user_inf set template_name = REPLACE(template_name,'教师执勤','校内执勤');
update auth_user_inf set template_name = REPLACE(template_name,'护校队巡查','校外执勤');