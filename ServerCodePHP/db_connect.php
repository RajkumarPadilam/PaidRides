<?php
class DB_CONNECT{

	function __construct(){
	//$this-> db_connect();
	}
	
	function __destruct(){
	$this-> db_close();
	}

	function db_connect(){
	
		//require_once 'config.php';
		require_once __DIR__ . '/db_config.php';
		$con=mysql_connect(DB_SERVER,DB_USER,DB_PASSWORD)
		or die(mysql_error());

		$db=mysql_select_db(DB_DATABASE) or die(mysql_error());
		return $con;
	}
	
	function db_close(){
	mysql_close();
	}

}

?>