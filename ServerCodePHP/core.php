<?php

if (isset($_POST['TAG']) && $_POST['TAG'] != '') {
    // get tag
    $tag = $_POST['TAG'];
 
    // include db handler
    //require_once 'db_functions.php';
	require_once __DIR__ . '/db_functions.php';
    $db = new DB_Functions();
 
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);
 
 
    if ($tag == 'LOGIN') 
	{
		$name = $_POST['USERNAME'];
		$password = $_POST['PASSWORD'];
		
        $result = $db->LOGIN_USER($name,$password);
		if(!$result)
		{
			$response["success"]=0;
			$response["message"]="error in LOGGING IN";
		}
		else
		{
			$response["ID"]=$result;
			$result2 = $db->getRiders();
			if(!$result2)
			{
				$response["success"]=0;
				$response["message"]="error in fetching riders";
			}
			else
			{	$response["success"]=1;
				$response["riders"]=array();
				$length = count($result2);
				for($i=0;$i<$length;$i++)
				{
				$response["riders"][$i]=array();				
				$response["riders"][$i]["RIDER_ID"]=$result2[$i]['RIDER_ID'];
				$response["riders"][$i]["USERNAME"]=$result2[$i]['USERNAME'];
				$response["riders"][$i]["PHONE"]=$result2[$i]['PHONE'];
				$response["riders"][$i]["RATING"]=$result2[$i]['RATING'];
				$response["riders"][$i]["ISMALE"]=$result2[$i]['ISMALE'];
				$response["riders"][$i]["ISAVAILABLE"]=$result2[$i]['ISAVAILABLE'];
				}
			}
		}		
			echo json_encode($response);
	}	
	else     if ($tag == 'RATING') 
	{
		$rider_id = $_POST['RIDER_ID'];
		$rating = $_POST['RATING'];
		
        $result = $db->changeRating($rider_id,$rating);
		if(!$result)
		{
			$response["success"]=0;
			$response["message"]="error in LOGGING IN";
		}
		else
		{
			$result2 = $db->getRiders();
			if(!$result2)
			{
				$response["success"]=0;
				$response["message"]="error in fetching riders";
			}
			else
			{	$response["success"]=1;
				$response["riders"]=array();
				$length = count($result2);
				for($i=0;$i<$length;$i++)
				{
				$response["riders"][$i]=array();				
				$response["riders"][$i]["RIDER_ID"]=$result2[$i]['RIDER_ID'];
				$response["riders"][$i]["USERNAME"]=$result2[$i]['USERNAME'];
				$response["riders"][$i]["PHONE"]=$result2[$i]['PHONE'];
				$response["riders"][$i]["RATING"]=$result2[$i]['RATING'];
				$response["riders"][$i]["ISMALE"]=$result2[$i]['ISMALE'];
				$response["riders"][$i]["ISAVAILABLE"]=$result2[$i]['ISAVAILABLE'];
				}
			}
		}		
			echo json_encode($response);
	}
	else     if ($tag == 'AVAILABILITY') 
	{
		$rider_id = $_POST['RIDER_ID'];
		$availability = $_POST['AVAILABLE'];
		
        $result = $db->changeAvailability($rider_id,$availability);
		if(!$result)
		{
			$response["success"]=0;
			$response["message"]="error in LOGGING IN";
		}
		else
		{
			$result2 = $db->getRiders();
			if(!$result2)
			{
				$response["success"]=0;
				$response["message"]="error in fetching riders";
			}
			else
			{	$response["success"]=1;
				$response["riders"]=array();
				$length = count($result2);
				for($i=0;$i<$length;$i++)
				{
				$response["riders"][$i]=array();				
				$response["riders"][$i]["RIDER_ID"]=$result2[$i]['RIDER_ID'];
				$response["riders"][$i]["USERNAME"]=$result2[$i]['USERNAME'];
				$response["riders"][$i]["PHONE"]=$result2[$i]['PHONE'];
				$response["riders"][$i]["RATING"]=$result2[$i]['RATING'];
				$response["riders"][$i]["ISMALE"]=$result2[$i]['ISMALE'];
				$response["riders"][$i]["ISAVAILABLE"]=$result2[$i]['ISAVAILABLE'];
				}
			}
		}		
			echo json_encode($response);
	}
	else if ($tag == 'DETAILS') 
	{
		$result2 = $db->getRiders();
			if(!$result2)
			{
				$response["success"]=0;
				$response["message"]="error in fetching riders";
			}
			else
			{	$response["success"]=1;
				$response["riders"]=array();
				$length = count($result2);
				for($i=0;$i<$length;$i++)
				{
				$response["riders"][$i]=array();				
				$response["riders"][$i]["RIDER_ID"]=$result2[$i]['RIDER_ID'];
				$response["riders"][$i]["USERNAME"]=$result2[$i]['USERNAME'];
				$response["riders"][$i]["PHONE"]=$result2[$i]['PHONE'];
				$response["riders"][$i]["RATING"]=$result2[$i]['RATING'];
				$response["riders"][$i]["ISMALE"]=$result2[$i]['ISMALE'];
				$response["riders"][$i]["ISAVAILABLE"]=$result2[$i]['ISAVAILABLE'];
				}
			}	
			echo json_encode($response);
	}
	else if ($tag == 'DEFAULT_RIDERS') 
	{
		$result2 = $db->getDefaultRiders();
			if(!$result2)
			{
				$response["success"]=0;
				$response["message"]="error in fetching riders";
			}
			else
			{	$response["success"]=1;
				$response["default_riders"]=array();
				$length = count($result2);
				for($i=0;$i<$length;$i++)
				{
				$response["default_riders"][$i]=array();
				$response["default_riders"][$i]["USERNAME"]=$result2[$i]['USERNAME'];
				$response["default_riders"][$i]["PHONE"]=$result2[$i]['PHONE'];
				}
			}	
			echo json_encode($response);
	}
	else if ($tag == 'GET_MESSAGES') 
	{
		$result2 = $db->getMessages();
		if(!$result2)
		{
			$response["success"]=0;
		}
		else
		{	
				$response["success"]=1;
				$response["MESSAGES"]=array();
				$length = count($result2);
				for($i=0;$i<$length;$i++)
				{	
				$response["MESSAGES"][$i]=array();
				$result= $db -> getPhoneNumber($result2[$i]['RIDER_ID']);
				$response["MESSAGES"][$i]["PHONE"]=$result['PHONE'];
				$response["MESSAGES"][$i]["ISMALE"]=$result['ISMALE'];
				$response["MESSAGES"][$i]["NAME"]=$result['USERNAME'];
				$response["MESSAGES"][$i]["RIDER_ID"]=$result2[$i]['RIDER_ID'];
				$response["MESSAGES"][$i]["MESSAGE"]=$result2[$i]['MESSAGE'];
				}
		}	
			echo json_encode($response);
	}
	else if ($tag == 'INSERT_MESSAGE') 
	{
		$riderID=$_POST['RIDER_ID'];
		$message=$_POST['MESSAGE'];
		
		$result= $db->insertMessages($riderID,$message);
		
		if(!$result)
		{
			$response["success"]=0;
		}
		else
		{
			$result2 = $db->getMessages();
			if(!$result2)
			{
				$response["success"]=0;
			}
			else
			{	
					$response["success"]=1;
					$response["MESSAGES"]=array();
					$length = count($result2);
					for($i=0;$i<$length;$i++)
					{
					$response["MESSAGES"][$i]=array();
					$result= $db -> getPhoneNumber($result2[$i]['RIDER_ID']);
					$response["MESSAGES"][$i]["PHONE"]=$result['PHONE'];
					$response["MESSAGES"][$i]["ISMALE"]=$result['ISMALE'];
					$response["MESSAGES"][$i]["NAME"]=$result['USERNAME'];
					$response["MESSAGES"][$i]["RIDER_ID"]=$result2[$i]['RIDER_ID'];
					$response["MESSAGES"][$i]["MESSAGE"]=$result2[$i]['MESSAGE'];
					}
			}	
		}
			echo json_encode($response);
	}
	else if ($tag == 'GET_HOUSING_MESSAGES') 
	{
		$result2 = $db->getHousingMessages();
		if(!$result2)
		{
			$response["success"]=0;
		}
		else
		{	
				$response["success"]=1;
				$response["MESSAGES"]=array();
				$length = count($result2);
				for($i=0;$i<$length;$i++)
				{	
				$response["MESSAGES"][$i]=array();
				$result= $db -> getPhoneNumber($result2[$i]['RIDER_ID']);
				$response["MESSAGES"][$i]["PHONE"]=$result['PHONE'];
				$response["MESSAGES"][$i]["ISMALE"]=$result['ISMALE'];
				$response["MESSAGES"][$i]["NAME"]=$result['USERNAME'];
				$response["MESSAGES"][$i]["RIDER_ID"]=$result2[$i]['RIDER_ID'];
				$response["MESSAGES"][$i]["MESSAGE"]=$result2[$i]['MESSAGE'];
				}
		}	
			echo json_encode($response);
	}
	else if ($tag == 'INSERT_HOUSING_MESSAGE') 
	{
		$riderID=$_POST['RIDER_ID'];
		$message=$_POST['MESSAGE'];
		
		$result= $db->insertHousingMessages($riderID,$message);
		
		if(!$result)
		{
			$response["success"]=0;
		}
		else
		{
			$result2 = $db->getHousingMessages();
			if(!$result2)
			{
				$response["success"]=0;
			}
			else
			{	
					$response["success"]=1;
					$response["MESSAGES"]=array();
					$length = count($result2);
					for($i=0;$i<$length;$i++)
					{
					$response["MESSAGES"][$i]=array();
					$result= $db -> getPhoneNumber($result2[$i]['RIDER_ID']);
					$response["MESSAGES"][$i]["PHONE"]=$result['PHONE'];
					$response["MESSAGES"][$i]["ISMALE"]=$result['ISMALE'];
					$response["MESSAGES"][$i]["NAME"]=$result['USERNAME'];
					$response["MESSAGES"][$i]["RIDER_ID"]=$result2[$i]['RIDER_ID'];
					$response["MESSAGES"][$i]["MESSAGE"]=$result2[$i]['MESSAGE'];
					}
			}	
		}
			echo json_encode($response);
	}
	else if($tag == 'CLEAR_PERSONAL_MESSAGES')
	{
		$RIDER_ID=$_POST['RIDER_ID'];
		$result=$db->clearPersonalMessages($RIDER_ID);
		if($result)
		{
			$response["success"]=1;
		}
		else
		{
			$response["success"]=0;
		}
	}
		else if ($tag == 'GET_PERSONAL_MESSAGES') 
	{
		$rider_id= $_POST['RIDER_ID'];
		$result2 = $db->getPersonalMessages($rider_id);
		
		if(!$result2)
		{
			$response["success"]=0;
		}
		else
		{	
				$response["success"]=1;
				$response["MESSAGES"]=array();
				$length = count($result2);
				for($i=0;$i<$length;$i++)
				{	
				$response["MESSAGES"][$i]=array();
				$result= $db -> getPhoneNumber($result2[$i]['SENDER_ID']);
				$response["MESSAGES"][$i]["SENDER_ID"]=$result2[$i]['SENDER_ID'];
				$response["MESSAGES"][$i]["PHONE"]=$result['PHONE'];
				$response["MESSAGES"][$i]["ISMALE"]=$result['ISMALE'];
				$response["MESSAGES"][$i]["NAME"]=$result['USERNAME'];
				$response["MESSAGES"][$i]["MESSAGE"]=$result2[$i]['MESSAGE'];
				}
		}	
			echo json_encode($response);
	}
	else if ($tag == 'GET_LAST_PERSONAL_MESSAGE_ID') 
	{
		$rider_id= $_POST['RIDER_ID'];
		$result2 = $db->getLastPersonalMessageId($rider_id);
		
		if(!$result2)
		{
			$response["success"]=0;
		}
		else
		{	
				$response["success"]=1;
				$response["ID"]=$result2['ID'];
		}	
			echo json_encode($response);
	}
	else if ($tag == 'INSERT_PERSONAL_MESSAGE') 
	{
		$receiverID=$_POST['RECEIVER_ID'];
		$senderID=$_POST['SENDER_ID'];
		$message=$_POST['MESSAGE'];
		
		$result= $db->insertPersonalMessages($senderID,$receiverID,$message);
		
		if(!$result)
		{
			$response["success"]=0;
		}
		else
		{
			$response["success"]=1;
		}
			echo json_encode($response);
	}
    else if ($tag == 'REGISTER') 
	{
		$name = $_POST['USERNAME'];
		$password = $_POST['PASSWORD'];
		$phone = $_POST['PHONE'];
		$ismale = $_POST['ISMALE'];
		$isRider = $_POST['ISRIDER'];
		
        $result = $db->REGISTER_USER($name,$phone,$password,$ismale,$isRider);
	
		if(!$result)
		{
			$response["success"]=0;
			$response["message"]="Registration not successful";
		}
		else
		{	
			$response['ID']=$result;
			$result2 = $db->getRiders();
			if(!$result2)
			{
				$response["success"]=0;
				$response["message"]="error in fetching riders";
			}
			else
			{	$response["success"]=1;
				$response["riders"]=array();
				$length = count($result2);
				for($i=0;$i<$length;$i++)
				{
				$response["riders"][$i]=array();				
				$response["riders"][$i]["RIDER_ID"]=$result2[$i]['RIDER_ID'];
				$response["riders"][$i]["USERNAME"]=$result2[$i]['USERNAME'];
				$response["riders"][$i]["PHONE"]=$result2[$i]['PHONE'];
				$response["riders"][$i]["RATING"]=$result2[$i]['RATING'];
				$response["riders"][$i]["ISMALE"]=$result2[$i]['ISMALE'];
				$response["riders"][$i]["ISAVAILABLE"]=$result2[$i]['ISAVAILABLE'];
				}
			}
		}
		echo json_encode($response);
	}	
	else 
	{
				$response["error"] = 1;
				$response["error_msg"] = "Incorrect email or password!";
				echo json_encode($response);
	}
  } else {
		echo "$TAG";
        echo "Invalid Request";
    }
?>