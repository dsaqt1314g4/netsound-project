drop user 'netsound'@'localhost';
create user 'netsound'@'localhost' identified by 'netsound';
grant all privileges on netsounddb.* to 'netsound'@'localhost';
flush privileges;