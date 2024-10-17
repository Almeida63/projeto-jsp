<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<title>Curso JSP</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
<style type="text/css">
form {
	position: absolute;
	top: 40%;
	left: 33%;
	right: 33%;
}

h5 {
	position: absolute;
	top: 30%;
	left: 33%;
}

.msg {
	position: absolute;
	top: 70%;
	left: 33%;
	font-size: 15px;
	color: red;
}

.btn-submit {
	margin-top: 20px;
	display: block;
	width: 100%;
}
</style>
</head>
<body>
	<h5>Bem vindo ao curso de JSP</h5>
	<form action="<%= request.getContextPath() %>/ServletLogin" method="post" class="needs-validation"
		novalidate>
		<input type="hidden" value="<%=request.getParameter("url")%>"
			name="url">

		<div class="mb-3">
			<label class="form-label">Login:</label> <input class="form-control"
				name="login" type="text" required>
			<div class="invalid-feedback">Campo obrigatório!</div>
			<div class="valid-feedback">Ok!</div>
		</div>

		<div class="mb-3">
			<label class="form-label">Senha:</label> <input class="form-control"
				name="senha" type="password" required>
			<div class="invalid-feedback">Campo obrigatório!</div>
			<div class="valid-feedback">Ok!</div>
		</div>

		<input type="submit" value="Acessar" class="btn btn-primary mb-3">
	</form>

	<h5 class="msg">${msg}</h5>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
		integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
		crossorigin="anonymous"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
		integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
		crossorigin="anonymous"></script>
	<script>
	// Example starter JavaScript for disabling form submissions if there are invalid fields
	
	(function()  {
	  'use strict'

	  // Fetch all the forms we want to apply custom Bootstrap validation styles to
	  const forms = document.querySelectorAll('.needs-validation')

	  // Loop over them and prevent submission
	  Array.from(forms).forEach(form => {
	    form.addEventListener('submit', event => {
	      if (!form.checkValidity()) {
	        event.preventDefault()
	        event.stopPropagation()
	      }

	      form.classList.add('was-validated')
	    }, false)
	  })
	})()
	
	
	
	</script>
</body>
</html>
