CREATE TABLE article (
	title VARCHAR(500),
	time TIMESTAMP,
	content TEXT,
	link VARCHAR(500)
	);
	
CREATE TABLE business (
	title VARCHAR(500),
	time TIMESTAMP
	);
	
CREATE TABLE entertainment (
	title VARCHAR(500),
	time TIMESTAMP
	);
	
CREATE TABLE general (
	title VARCHAR(500),
	time TIMESTAMP
	);
	
CREATE TABLE political (
	title VARCHAR(500),
	time TIMESTAMP
	);
	
CREATE TABLE sports (
	title VARCHAR(500),
	time TIMESTAMP
	);
	
CREATE TABLE technology (
	title VARCHAR(500),
	time TIMESTAMP
	);
	
CREATE TABLE world (
	title VARCHAR(500),
	time TIMESTAMP
	);
	
CREATE TABLE national (keywords VARCHAR(40));

CREATE TABLE keywords (title VARCHAR(40));

CREATE TABLE nationalnews (news VARCHAR(500));

CREATE TABLE trends (
	trend VARCHAR(40),
	last_6_hrs INTEGER,
	last_12_hrs INTEGER,
	last_1_day INTEGER,
	last_2_days INTEGER
	);
	
ALTER TABLE article ADD UNIQUE (link);