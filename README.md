#Trending News Project

This is a project that will display current trending topics.
There are 3 end points for the localhost
1.	The home page gives details of the national news, how frequent are the trends pattern of the trends and the centre of focus of the trend.
1.	The /national page is same as home page.
1.	The news/{id} page - The page gives info about trending topics across various categories. Various ids are - 
	1.	0:- General
	1.	1:- National
	1.	2:- World
	1.	3:- Business
	1.	4:- Technology
	1.	5:- Entertainment
	1.	6:- Sports

##Setup

Make a file PersonalConstants.java in constants package and have following constants.
*	DBNAME - Name of the database
*	DBUSER - Username of MySQL
*	DBPASS - Password of MySQL
*	GOOGLE_API_KEY - API Key for Google Maps

Before using for the first time initialize the database by using file database_init.sql