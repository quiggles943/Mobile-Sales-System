<?php
session_start();
include 'server_connection.php';	//includes the server connection file 
$token = "";
if( isset($_POST['token']))		//checks if token is passed in through post
{
	$token = $_POST["token"];	//Sets token to equal the token posted
}
if($token != "bTe>(AQSs(Au9?9sS%&H6Pgke!LMm9,A?ZM9x"){
	echo "unable to access";	//if token isnt given or is not correct then it presents an error
}
else{

	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);		//uses variables from the server_connection.php file

	if ($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);	//stops if no connection could be created
	}
	
	$table = "product";		//table to look up
	
	$sql = "SELECT * FROM ".$table;	//SQL statement
	$result = $conn->query($sql);	//runs SQL statement

	$posts = array();

	while($row = $result->fetch_assoc()) 	//for each row in the table
	{ 
		//sets variables equal to the column value for that row
		$format=$row['Format']; 
		$imageID=$row['ImageID']; 
		$prodDesc=$row['ProdDesc']; 
		$price=$row['Price']; 

		$posts[] = array('Format'=> $format, 'ImageId'=> $imageID, 'ProdDesc'=> $prodDesc, 'Price'=> $price);	//adds the row to the array
	} 
	echo json_encode($posts);	//returns the JSON encoded data from the table
}
?>