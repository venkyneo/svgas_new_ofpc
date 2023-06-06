insert into alarmgroup(alarmgroup) values('NONE'),('TANK_LEVEL'),('TANK_PRESSURE'),('TANK_TEMPERATURE'),('PUMP1'),('PUMP2'),('PUMP3'),('PUMP4'),('PUMP5'),('PUMP6'),('PUMP7'),('PUMP8'),('VEROMETER1'),('VEROMETER2'),('VEROMETER3'),('VEROMETER4'),('VEROMETER5'),('VEROMETER6'),('VEROMETER7'),('VEROMETER8')
insert into categorytype(category) values('NORMAL_REPORT'),('ALARM_REPORT'),('SET_POINT'),('OTHERS')
insert into viewcategory(viewcategoryname) values('DASHBOARD'),('MIMIC'),('CONFIG')
insert into alarmtype(atypename) values('NONE'),('HIGH'),('LOW')
insert into pcode(pcodename, main_category, view_category, alarm_group) values('START_STOP',4,1,1),('REAL_FLOW_RATE',1,1,1),('FIXED_FLOW_RATE',1,1,1),('CONCENTRATION',1,1,1),('REAMAINING_TIME',4,1,1),('PERIOD',4,1,1),('TOTAL_INJECTION',4,1,1),('TANK_LEVEL',1,2,1),('TANK_PRESSURE',1,2,1),('TANK_TEMPERATURE',1,2,1),('TANK_LEVEL_HIGH_ALARM',2,1,1),('TANK_LEVEL_LOW_ALARM',2,1,1),('TANK_LEVEL_HIGH_ALARM_SETPOINT',4,3,3),('TANKLEVEL_LOW_ALARM_SETPOINT',4,3,3),('VEROMETER_1_LEVEL',4,2,1),('VEROMETER_2_LEVEL',4,2,1),('VEROMETER_3_LEVEL',4,2,1),('VEROMETER_4_LEVEL',4,2,1),('VEROMETER_5_LEVEL',4,2,1),('VEROMETER_6_LEVEL',4,2,1),('VEROMETER_7_LEVEL',4,2,1),('VEROMETER_8_LEVEL',4,2,1),('PUMP_1_COUNTER',4,2,1),('PUMP_2_COUNTER',4,2,1),('PUMP_3_COUNTER',4,2,1),('PUMP_4_COUNTER',4,2,1),('PUMP_5_COUNTER',4,2,1),('PUMP_6_COUNTER',4,2,1),('PUMP_7_COUNTER',4,2,1),('PUMP_8_COUNTER',4,2,1),('PUMP_1_LAST_INJECTION',4,2,1),('PUMP_2_LAST_INJECTION',4,2,1),('PUMP_3_LAST_INJECTION',4,2,1),('PUMP_4_LAST_INJECTION',4,2,1),('PUMP_5_LAST_INJECTION',4,2,1),('PUMP_6_LAST_INJECTION',4,2,1),('PUMP_7_LAST_INJECTION',4,2,1),('PUMP_8_LAST_INJECTION',4,2,1),('PUMP_1_HIGH_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_2_HIGH_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_3_HIGH_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_4_HIGH_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_5_HIGH_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_6_HIGH_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_7_HIGH_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_8_HIGH_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_1_LOW_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_2_LOW_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_3_LOW_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_4_LOW_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_5_LOW_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_6_LOW_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_7_LOW_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_8_LOW_FLOW_ALARM_SETPOINT',3,3,1),('PUMP_1_HIGH_ALARM',2,1,5),('PUMP_2_HIGH_ALARM',2,1,6),('PUMP_3_HIGH_ALARM',2,1,7),('PUMP_4_HIGH_ALARM',2,1,8),('PUMP_5_HIGH_ALARM',2,1,9),('PUMP_6_HIGH_ALARM',2,1,10),('PUMP_7_HIGH_ALARM',2,1,11),('PUMP_8_HIGH_ALARM',2,1,12),('PUMP_1_LOW_ALARM',2,1,5),('PUMP_2_LOW_ALARM',2,1,6),('PUMP_3_LOW_ALARM',2,1,7),('PUMP_4_LOW_ALARM',2,1,8),('PUMP_5_LOW_ALARM',2,1,9),('PUMP_6_LOW_ALARM',2,1,10),('PUMP_7_LOW_ALARM',2,1,11),('PUMP_8_LOW_ALARM',2,1,12)
insert into roles(name) values('ROLE_USER'),('ROLE_MODERATOR'),('ROLE_ADMIN')
insert into users(email,password,username) values('admin@admin.com','admin@123','svgasadmin')
insert into user_roles(user_id,role_id) values(1,3),(1,2),(1,1)