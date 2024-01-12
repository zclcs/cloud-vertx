call insert_if_not_exists(database(), 'system_rate_limit_rule',
                          'request_uri,request_method,limit_from,limit_to,rate_limit_count,interval_sec,rule_status,create_at,create_by',
                          '"/system/login/token/byUsername","POST","00:00:00","23:59:59","100","1","1",now(),"system"',
                          'request_uri="/system/login/token/byUsername" and request_method="POST"');
call insert_if_not_exists(database(), 'system_rate_limit_rule',
                          'request_uri,request_method,limit_from,limit_to,rate_limit_count,interval_sec,rule_status,create_at,create_by',
                          '"/system/login/token/byMobile","POST","00:00:00","23:59:59","100","1","1",now(),"system"',
                          'request_uri="/system/login/token/byMobile" and request_method="POST"');
