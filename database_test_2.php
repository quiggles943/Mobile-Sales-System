<?php
session_start();
include 'server_connection.php';	//includes the server connection file 
$token = "";

if( isset($_POST['token']))		//checks if token is passed in through post
{
	$token = $_POST["token"];	//Sets token to equal the token posted
}
else{
	http_response_code(401);
	die();
}
if( isset($_POST['tables']))	//Checks if there is a table request
{
	
	$table = $_POST['tables'];	//reads in the table request
}
else{
	//$table = array("product","image","format","invoice","invoiceitems");		//tables to look up if no table request received
	$table = array("product","format"); 	//test table (to be removed)
}

if($token != "bTe>(AQSs(Au9?9sS%&H6Pgke!LMm9,A?ZM9x"){
	http_response_code(403);
	echo "unable to access";	//if token isnt given or is not correct then it presents an error
	die();
}
else{
	http_response_code(200);
	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);		//uses variables from the server_connection.php file

	if ($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);	//stops if no connection could be created
	}
	

	$json = array();
	$database = $dbname //set the schema name to be equal to the schema in the server_connection file
	foreach ($table as &$tab){
		$final = array();
		$sql = "SELECT `COLUMN_NAME` 
				FROM `INFORMATION_SCHEMA`.`COLUMNS` 
				WHERE `TABLE_SCHEMA`='$database' 
				AND `TABLE_NAME`='$tab';";	//SQL statement to find the column names of the table
		$result = $conn->query($sql);	//runs SQL statement
		$columns = array();
		while($row = $result->fetch_assoc()) 	//for each row in the table
		{
			array_push($columns,$row['COLUMN_NAME']); //Adds the columns to an array
		}
		$sql1 = "SELECT * FROM ".$tab;	//SQL statement to select all rows from the table
		$result1 = $conn->query($sql1);	//runs SQL statement
		$posts = array();
		
		while($row = $result1->fetch_assoc()) 	//for each row in the table
		{
			
			foreach($columns as &$column)	//for each column in the row
			{
				$posts[$column] = $row[$column];	//adds the column data for that row to the key value pair for the tow
				
			}	
			$final[] = $posts;			//adds the row data to the table array	
		}
		$json[$tab] = $final;		//adds the table array to the full json array
		
		}	
		echo json_encode($json);	//converts the array to json format
		
}
?>