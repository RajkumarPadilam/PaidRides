<?php

class DB_Functions {
 
    private $db;
 
    function __construct() {
        require_once __DIR__ . '/db_connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->db_connect();
    }
 
    // destructor
    function __destruct() {
         
    }
	
	public function REGISTER_USER($username, $phonenumber, $password, $ismale, $isRider){
		$userid=uniqid();
		$result=mysql_query("select * from LOGIN_INFO where USERNAME='$username' and PHONE='$phonenumber'") or die(mysql_error());
		if(mysql_num_rows($result)>0)
		{echo "333"; return false;}
		else
		{
			$result2=mysql_query("insert into LOGIN_INFO(USER_ID, USERNAME, PHONE, PASSWORD, ISMALE, ISRIDER) values('$userid','$username','$phonenumber','$password','$ismale','$isRider')") or die(mysql_error());
			if($result2)
			{
				$result3=mysql_query("insert into RIDER_DETAILS(RIDER_ID, USERNAME, PHONE, ISMALE, ISRIDER) values('$userid','$username','$phonenumber','$ismale','$isRider')") or die(mysql_error());
						if($result3)
					{
						return $userid;
					}
					else
					{
					echo "11111";
						return false;
					}
			}
			else
			{
				echo "222222";
				return false;
			}
		}
		
	}
	
	public function LOGIN_USER($username, $password)
	{
		//echo "select * from LOGIN_INFO where USERNAME='$username' and PASSWORD='$password'";
		$result=mysql_query("select * from LOGIN_INFO where USERNAME='$username' and PASSWORD='$password'")or die(mysql_error());
		$count=mysql_num_rows($result);
		if($count>0)
		{
			$response=mysql_fetch_array($result);
			return $response['USER_ID'];
		}
		else
		{
			return false;
		}
	}
	public  function getRiders()
	{	
			$result2=mysql_query("SELECT * FROM RIDER_DETAILS where ISRIDER=1 ") or die(mysql_error());
			$no_of_rows = mysql_num_rows($result2);
			$response=array();
			$count=$no_of_rows;
			if($no_of_rows<1)
			return false;
			
			while($no_of_rows > 0) {
            $response[$count-$no_of_rows] = mysql_fetch_array($result2);
			$no_of_rows--;
			}
			return $response;
	}
	
		public  function getDefaultRiders()
	{	
			$result2=mysql_query("SELECT * FROM DEFAULT_RIDERS") or die(mysql_error());
			$no_of_rows = mysql_num_rows($result2);
			$response=array();
			$count=$no_of_rows;
			if($no_of_rows<1)
			return false;
			
			while($no_of_rows > 0) {
         		   $response[$count-$no_of_rows] = mysql_fetch_array($result2);
			$no_of_rows--;
			}
			return $response;
	}
	
	public  function getPhoneNumber($rider_id)
	{	
			$result2=mysql_query("SELECT * FROM RIDER_DETAILS where RIDER_ID='$rider_id'") or die(mysql_error());
			$no_of_rows = mysql_num_rows($result2);
			$count=$no_of_rows;
			if($no_of_rows<1)
			return false;
			else
			return mysql_fetch_array($result2);
	}
	
	public  function getMessages()
	{	
			$result2=mysql_query("SELECT * FROM MESSAGE_BOARD ORDER BY ID DESC LIMIT 20 ") or die(mysql_error());
			$no_of_rows = mysql_num_rows($result2);
			$response=array();
			$count=$no_of_rows;
			if($no_of_rows<1)
			return false;
			
			while($no_of_rows > 0) 
			{
            $response[$count-$no_of_rows] = mysql_fetch_array($result2);
			$no_of_rows--;
			}
			return $response;
	}
	
	public  function insertMessages($rider_id,$message)
	{	
			$result2=mysql_query("insert into MESSAGE_BOARD(RIDER_ID, MESSAGE) values('$rider_id', '$message')") or die(mysql_error());
			if($result2)
			{
				return true;
			}
			else
			{
				return false;
			}
	}
	public  function getLastPersonalMessageId($receiver_id)
	{	
			$result2=mysql_query("SELECT * FROM MESSAGES where hasSEEN=0 and RECEIVER_ID='$receiver_id' ORDER BY ID DESC LIMIT 2");
			$no_of_rows = mysql_num_rows($result2);
			$count=$no_of_rows;
			
			if($no_of_rows<1)
			return false;
			
			return mysql_fetch_array($result2);
	}
	public  function getHousingMessages()
	{	
			$result2=mysql_query("SELECT * FROM HOUSING ORDER BY ID DESC LIMIT 20 ") or die(mysql_error());
			$no_of_rows = mysql_num_rows($result2);
			$response=array();
			$count=$no_of_rows;
			if($no_of_rows<1)
			return false;
			
			while($no_of_rows > 0) 
			{
            $response[$count-$no_of_rows] = mysql_fetch_array($result2);
			$no_of_rows--;
			}
			return $response;
	}
	
	public  function insertHousingMessages($rider_id,$message)
	{	
			$result2=mysql_query("insert into HOUSING(RIDER_ID, MESSAGE) values('$rider_id', '$message')") or die(mysql_error());
			if($result2)
			{
				return true;
			}
			else
			{
				return false;
			}
	}
	
	public function clearPersonalMessages($rider_id)
	{
		echo "DELETE FROM MESSAGES where RECEIVER_ID='$rider_id'";
		$result2=mysql_query("DELETE FROM MESSAGES where RECEIVER_ID='$rider_id'") or die(mysql_error());
		if($result2)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public  function getPersonalMessages($receiver_id)
	{	
			$result2=mysql_query("SELECT * FROM MESSAGES where hasSEEN=0 and RECEIVER_ID='$receiver_id' ORDER BY ID DESC LIMIT 20") or die(mysql_error());
			$no_of_rows = mysql_num_rows($result2);
			$response=array();
			$count=$no_of_rows;
			if($no_of_rows<1)
			return false;
			
			while($no_of_rows > 0) 
			{
            $response[$count-$no_of_rows] = mysql_fetch_array($result2);
			$no_of_rows--;
			}
			return $response;
	}
	
	public  function insertPersonalMessages($sender_id,$receiver_id,$message)
	{	
			echo "insert into MESSAGES(RECEIVER_ID,SENDER_ID,MESSAGE) values('$receiver_id','$sender_id','$message')";
			$result2=mysql_query("insert into MESSAGES(RECEIVER_ID,SENDER_ID,MESSAGE) values('$receiver_id','$sender_id','$message')") or die(mysql_error());
			if($result2)
			{
				return true;
			}
			else
			{
				return false;
			}
	}
	
	public  function changeMessageSeenStatus($riderid)
	{	
			$result=mysql_query("update RIDER_DETAILS set ISAVAILABLE='$isAvailable' where RIDER_ID='$riderid' ") or die(mysql_error());
			if($result)
			{
				return true;
			}
			else
			{
				return false;
			}
	}
	
	public  function changeAvailability($riderid, $isAvailable)
	{	
			$result=mysql_query("update RIDER_DETAILS set ISAVAILABLE='$isAvailable' where RIDER_ID='$riderid' ") or die(mysql_error());
			if($result)
			{
				return true;
			}
			else
			{
				return false;
			}
	}
	public  function changeRating($riderid, $rating)
	{	
			$result=mysql_query("update RIDER_DETAILS set RATING='$rating' where RIDER_ID='$riderid' ") or die(mysql_error());
			if($result)
			{
				return true;
			}
			else
			{
				return false;
			}
	}
	
}
?>