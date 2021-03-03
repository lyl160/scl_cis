ALTER TABLE `t_im_read_logs`
    ADD COLUMN `type` int(255) NULL DEFAULT NULL COMMENT 'type 类型：1校务巡查反馈、2一周综述、3校园大事记、4教师执勤、5护校队巡查、6后勤巡查反馈' AFTER `school_id`;

update t_im_read_logs aa
set aa.type = (select bb.type from t_inspection_message bb where bb.id = aa.message_id);