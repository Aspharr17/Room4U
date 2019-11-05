<?php
	$con = mysqli_connect("localhost","id8908548_root","ghostrecon","id8908548_users");

	$usuario	= $_REQUEST["usuario"];

	$statement 	= mysqli_prepare($con,"SELECT * FROM user WHERE usuario = ?");
	mysqli_stmt_bind_param($statement, "s", $usuario);
	mysqli_stmt_execute($statement);

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $idusuario, $nombre, $usuario, $clave, $telefono,$guardado);

	$response = array();
	$response["success"] = false;

	while(mysqli_stmt_fetch($statement)){
		$response["success"] = true;
		$response["nombre"] = $nombre;
		$response["usuario"] = $usuario;
		$response["clave"] = $clave;
		$response["telefono"] = $telefono;
		$response["guardado"] = $guardado;
	}
	$array = explode(",", $guardado);

	echo json_encode($guardado);