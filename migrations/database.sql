-- orders
CREATE TABLE orders (
	id bigint not null,
	user_id bigint not null,
	external_id BIGINT DEFAULT NULL , -- buy side 传入的订单id,也可能不传,如果传了需要唯一
	symbol varchar(20) not null,
	action varchar(10) not null,
	order_type varchar(20) not null,
	limit_price double,
	stop_price double ,
	total_quantity int not null,
	filled_quantity int not null DEFAULT 0,
	avg_fill_price double,
	time_in_force varchar(20) not null,
	expired_at DATETIME,
	status VARCHAR(20) NOT NULL ,
	message VARCHAR(200),

	commission DOUBLE,

	-- 更改/取消
	cancel_status TINYINT DEFAULT 0,
	replace_status TINYINT DEFAULT 0,
	replace_request VARCHAR(500),

	source VARCHAR(100),

	updated_at datetime(3) not null,
	created_at datetime(3) not null,
	primary key(id),
	UNIQUE key(user_id, external_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE order_histories (
	id bigint auto_increment not null,
	order_id bigint not null,
	status varchar(20) not null,
	content TEXT NOT NULL ,

	updated_at datetime(3) not null,
	created_at datetime(3) not null,
	primary key(id),
	KEY (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# CREATE TABLE trade_orders (
# 	id BIGINT NOT NULL AUTO_INCREMENT,
# 	order_id BIGINT NOT NULL ,
# 	exchange VARCHAR(10) NOT NULL ,
# 	symbol VARCHAR(20) NOT NULL ,
# 	side VARCHAR(5) NOT NULL ,
# 	order_type VARCHAR(20) NOT NULL ,
# 	total_quantity INT NOT NULL ,
# 	filled_quantity INT NOT NULL DEFAULT 0,
# 	avg_fill_price DOUBLE,
# 	time_in_force VARCHAR(20) NOT NULL ,
# 	status VARCHAR(20) NOT NULL ,
#
# 	updated_at datetime(3) not null,
# 	created_at datetime(3) not null,
# 	primary key(id),
# 	KEY (user_order_id)
# );

CREATE TABLE executions (
	id bigint not null auto_increment,
	order_id bigint not null,
	channel TINYINT NOT NULL ,
	report_id VARCHAR(20) NOT NULL ,
	exec_trans_type TINYINT NOT NULL ,
	exec_type CHAR(1) NOT NULL ,
	exchange VARCHAR(20) NOT NULL,

	order_status CHAR(1) NOT NULL ,
	reject_reason VARCHAR(200),
	symbol VARCHAR(20) NOT NULL ,
	side TINYINT NOT NULL ,

	last_filled INT,
	last_price DOUBLE,

	cumulative INT NOT NULL ,
	remaining INT NOT NULL,
	avg_price DOUBLE NOT NULL ,

	commision DOUBLE,
	message TEXT,

	updated_at datetime not null,
	created_at datetime not null,                                                                                                                                    v
	primary key(id),
	KEY (order_id),
	UNIQUE (channel, exec_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE execution_settlement (
	id BIGINT NOT NULL AUTO_INCREMENT,
	exec_id BIGINT NOT NULL ,

	status varchar(20) not null, -- new, unsettled, settle
	traded_at datetime not null,
	settled_at datetime not null,

	quantity int not null,
	price double not null,
	pnl double not null,
	commision double not null,
	fee double not null, 	-- 相关费用： https://www.interactivebrokers.com/cn/index.php?f=commission&p=stocks2
	-- 交易所交易所费用、清算费用（每股）、交易费用 0.0000184 * 总交易值
	-- 经过隔夜的定单将被视为一个新的定单，用于确定定单的最低数量 https://www.interactivebrokers.com/cn/index.php?f=commission&p=stocks2
	code VARCHAR(20) NOT NULL ,-- P：部分执行 R:股息再投资 T:转账 C:平仓 Ca：取消

	PRIMARY KEY (id)
);

-- 持仓变动, FIFO。要能清楚计算所有持仓；哪些股票/资金尚未结算；监护者的持仓和资金，并内部生成记录与之对账
-- 要结算之后才能
-- 用于计算哪些持仓的股票是 1. 可以分红的; 2. 需要计息的. 3.
CREATE TABLE positions (
	id bigint auto_increment not null,
	symbol varchar(20) not null,

	total_quantity int not null,
	open_quantity int not null,
	close_quantity int not null,

	status varchar(20) not null,  -- open/close/
	action_type varchar(20) not null, -- open/close/

	trade_id bigint not null,
	order_id bigint not null,
	traded_at datetime not null,
	settled_at datetime not null,

	created_at datetime not null,
	updated_at datetime not null,
	primary key(id),
	key (trade_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 持仓汇总
CREATE TABLE position_summarys (
	id bigint auto_increment not null,
	user_id bigint not null,
	symbol varchar(20) not null,
	settled_positions int not null,
	unsettled_positions int not null,
	avg_cost double not null, -- 怎么计算？跟买卖顺序有关, 用于计算未实现的收益

	created_at datetime not null,
	updated_at datetime not null,
	primary key(id),
	unique key (user_id, symbol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- double entry bookkeeping(ownership vs location)
CREATE TABLE accounts (
	id BIGINT NOT NULL AUTO_INCREMENT,
	created_at datetime not null,
	updated_at datetime not null,
	primary key(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE account_summarys (
	id BIGINT NOT NULL AUTO_INCREMENT,
	created_at datetime not null,
	updated_at datetime not null,
	primary key(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 交易系统（front office) & 结算系统 (for back office)

-- static data

-- 股票代码变化？
CREATE TABLE corporate_actions (
	id bigint auto_increment not null,

	symbol varchar(20) not null,
	action_type varchar(20) not null,

	-- 相关日期和元数据
	created_at datetime not null,
	updated_at datetime not null,
	primary key(id),
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 股票数据 common share/prefered share, ISIN, cusip
-- contract

-- 数据抓取项目，获取关键数据和信息

-- trade life cycle


