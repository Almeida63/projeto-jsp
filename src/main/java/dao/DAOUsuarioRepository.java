package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import beandto.BeanDtoGraficoSalarioUser;
import connection.SingleConnectionBanco;
import model.ModelLogin;
import model.ModelTelefone;

public class DAOUsuarioRepository {

	private Connection connection;

	public DAOUsuarioRepository() {
		connection = SingleConnectionBanco.getConnection();
	}

	public ModelLogin gravarUsuario(ModelLogin objeto, Long userLogado) throws Exception {

		if (objeto.isNovo()) { /* Grava um novo */

			String sql = "insert into model_login (login, senha, nome, email, usuario_id, perfil, sexo, cep, logradouro, bairro, localidade, uf, numero, dataNascimento,rendamensal) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, objeto.getLogin());
			statement.setString(2, objeto.getSenha());
			statement.setString(3, objeto.getNome());
			statement.setString(4, objeto.getEmail());
			statement.setLong(5, userLogado);
			statement.setString(6, objeto.getPerfil());
			statement.setString(7, objeto.getSexo());
			statement.setString(8, objeto.getCep());
			statement.setString(9, objeto.getLogradouro());
			statement.setString(10, objeto.getBairro());
			statement.setString(11, objeto.getLocalidade());
			statement.setString(12, objeto.getUf());
			statement.setString(13, objeto.getNumero());
			statement.setDate(14, objeto.getDataNascimento());
			statement.setDouble(15, objeto.getRendamensal());

			statement.execute();
			connection.commit();

			if (objeto.getFotoUser() != null && !objeto.getFotoUser().isEmpty()) {
				sql = "update model_login set fotouser =?, extensaofotouser=? where login =?";
				statement = connection.prepareStatement(sql);

				statement.setString(1, objeto.getFotoUser());
				statement.setString(2, objeto.getExtensaofotouser());
				statement.setString(3, objeto.getLogin());

				statement.execute();

				connection.commit();
			}

		} else {
			String sql = "update model_login set login=?, senha=?, nome=?, email=?, perfil=?, sexo=?, cep=?, logradouro=?, bairro=?, localidade=?, uf=?, numero=?, dataNascimento=?, rendamensal=? where id = "
					+ objeto.getId() + ";";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, objeto.getLogin());
			statement.setString(2, objeto.getSenha());
			statement.setString(3, objeto.getNome());
			statement.setString(4, objeto.getEmail());
			statement.setString(5, objeto.getPerfil());
			statement.setString(6, objeto.getSexo());
			statement.setString(7, objeto.getCep());
			statement.setString(8, objeto.getLogradouro());
			statement.setString(9, objeto.getBairro());
			statement.setString(10, objeto.getLocalidade());
			statement.setString(11, objeto.getUf());
			statement.setString(12, objeto.getNumero());
			statement.setDate(13, objeto.getDataNascimento());
			statement.setDouble(14, objeto.getRendamensal());

			statement.executeUpdate();

			connection.commit();

			if (objeto.getFotoUser() != null && !objeto.getFotoUser().isEmpty()) {
				sql = "update model_login set fotouser =?, extensaofotouser=? where login =?";
				statement = connection.prepareStatement(sql);

				statement.setString(1, objeto.getFotoUser());
				statement.setString(2, objeto.getExtensaofotouser());
				statement.setString(3, objeto.getLogin());

				statement.execute();

				connection.commit();
			}
		}

		return this.consultaUsuario(objeto.getLogin(), userLogado);

	}

	public List<ModelLogin> consultaUsuarioListPaginada(Long userLogado, Integer offset) throws SQLException {

		List<ModelLogin> retorno = new ArrayList<ModelLogin>();

		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado
				+ " order by nome offset " + offset + " limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) { /* Percorrer as linhas de resultado do SQL */
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));

			retorno.add(modelLogin);
		}

		return retorno;
	}

	public int totalPagina(Long userLogado) throws Exception {
		String sql = "select count(1) as total from model_login where usuario_id = " + userLogado;
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		resultado.next();

		Double cadastros = resultado.getDouble("total");

		Double porPagina = 5.0;

		Double pagina = cadastros / porPagina;

		Double resto = pagina % 2;

		if (resto > 0) {
			pagina++;
		}

		return pagina.intValue();
	}

	public List<ModelLogin> consultaUsuarioListRel(Long userLogado, String dataInicial, String dataFinal) throws Exception {

		List<ModelLogin> retorno = new ArrayList<ModelLogin>();

		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado + " and dataNascimento >= ? and dataNascimento <= ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setDate(1, Date.valueOf(new
				SimpleDateFormat("yyyy-mm-dd").format(new
				SimpleDateFormat("dd/mm/yyyy").parse(dataInicial))));
		statement.setDate(2, Date.valueOf(new
				SimpleDateFormat("yyyy-mm-dd").format(new
				SimpleDateFormat("dd/mm/yyyy").parse(dataFinal))));


		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) { /* Percorrer as linhas de resultado do SQL */
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
            modelLogin.setDataNascimento(resultado.getDate("dataNascimento"));
			modelLogin.setTelefones(this.listaFone(modelLogin.getId()));

			retorno.add(modelLogin);
		}

		return retorno;
	}
	
	public List<ModelLogin> consultaUsuarioListRel(Long userLogado) throws Exception {

		List<ModelLogin> retorno = new ArrayList<ModelLogin>();

		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado;
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) { /* Percorrer as linhas de resultado do SQL */
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
            modelLogin.setDataNascimento(resultado.getDate("dataNascimento"));
			modelLogin.setTelefones(this.listaFone(modelLogin.getId()));

			retorno.add(modelLogin);
		}

		return retorno;
	}

	public List<ModelLogin> consultaUsuarioList(Long userLogado) throws SQLException {

		List<ModelLogin> retorno = new ArrayList<ModelLogin>();

		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado + " limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) { /* Percorrer as linhas de resultado do SQL */
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));

			retorno.add(modelLogin);
		}

		return retorno;
	}

	public List<ModelLogin> consultaUsuarioListOffSet(String nome, Long userLogado, int offSet) throws SQLException {

		List<ModelLogin> retorno = new ArrayList<ModelLogin>();

		String sql = "select * from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id = ? offset "
				+ offSet + " limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) { /* Percorrer as linhas de resultado do SQL */
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));

			retorno.add(modelLogin);
		}

		return retorno;
	}

	public List<ModelLogin> consultaUsuarioList(String nome, Long userLogado) throws SQLException {

		List<ModelLogin> retorno = new ArrayList<ModelLogin>();

		String sql = "select * from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id = ? limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) { /* Percorrer as linhas de resultado do SQL */
			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));

			retorno.add(modelLogin);
		}

		return retorno;
	}

	public int consultaUsuarioListTotalPaginacao(String nome, Long userLogado) throws SQLException {

		String sql = "select count(1) as total from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);

		ResultSet resultado = statement.executeQuery();

		resultado.next();

		Double cadastros = resultado.getDouble("total");

		Double porPagina = 5.0;

		Double pagina = cadastros / porPagina;

		Double resto = pagina % 2;

		if (resto > 0) {
			pagina++;
		}

		return pagina.intValue();

	}

	public ModelLogin consultaUsuarioLogado(String login) throws Exception {
		ModelLogin modelLogin = new ModelLogin();

		String sql = "select * from model_login where upper(login) = upper('" + login + "')";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setUserAdmin(resultado.getBoolean("useradmin"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("dataNascimento"));
			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));

		}
		return modelLogin;

	}

	public ModelLogin consultaUsuario(String login) throws Exception {
		ModelLogin modelLogin = new ModelLogin();

		String sql = "select * from model_login where upper(login) = upper('" + login + "') and useradmin is false ";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			// modelLogin.setUserAdmin(resultado.getBoolean("useradmin"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotoUser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("dataNascimento"));
			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));
		}
		return modelLogin;

	}

	public ModelLogin consultaUsuario(String login, Long userLogado) throws Exception {
		ModelLogin modelLogin = new ModelLogin();

		String sql = "select * from model_login where upper(login) = upper('" + login
				+ "') and useradmin is false and usuario_id = " + userLogado;
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotoUser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("dataNascimento"));
			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));

		}
		return modelLogin;

	}

	public ModelLogin consultaUsuarioPorId(Long id) throws Exception {
		ModelLogin modelLogin = new ModelLogin();

		String sql = "select * from model_login where id = ? and useradmin is false";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, id);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) { /* Se tem resultado */
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotoUser"));
			modelLogin.setExtensaofotouser(resultado.getString("extensaofotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("dataNascimento"));
			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));
		}
		return modelLogin;

	}

	public ModelLogin consultaUsuarioPorId(String id, Long userLogado) throws Exception {
		ModelLogin modelLogin = new ModelLogin();

		String sql = "select * from model_login where id = ? and useradmin is false and usuario_id = ?";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(id));
		statement.setLong(2, userLogado);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) { /* Se tem resultado */
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotoUser"));
			modelLogin.setExtensaofotouser(resultado.getString("extensaofotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("dataNascimento"));
			modelLogin.setRendamensal(resultado.getDouble("rendamensal"));
		}
		return modelLogin;

	}

	public boolean validarLogin(String login) throws Exception {
		String sql = "select count(1) > 0 as existe from model_login where upper(login) = upper('" + login + "')";

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		resultado.next(); /* Pra ele entrar nos resultados do sql */
		return resultado.getBoolean("existe");
	}

	public void deletarUser(String idUser) throws SQLException {
		String sql = "delete from model_login where id = ? and useradmin is false";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(idUser));
		statement.executeUpdate();

		connection.commit();

	}

	public List<ModelTelefone> listaFone(Long idUserPai) throws Exception {
		List<ModelTelefone> list = new ArrayList<ModelTelefone>();

		String sql = "select * from telefone where usuario_pai_id = ?";

		PreparedStatement preparedStatement = connection.prepareStatement(sql);

		preparedStatement.setLong(1, idUserPai);

		ResultSet result = preparedStatement.executeQuery();

		while (result.next()) {
			ModelTelefone modelTelefone = new ModelTelefone();

			modelTelefone.setId(result.getLong("id"));
			modelTelefone.setNumero(result.getString("numero"));
			modelTelefone.setUsuario_cad_id(this.consultaUsuarioPorId(result.getLong("usuario_cad_id")));
			modelTelefone.setUsuario_pai_id(this.consultaUsuarioPorId(result.getLong("usuario_pai_id")));

			list.add(modelTelefone);
		}

		return list;

	}
	
	public BeanDtoGraficoSalarioUser montarGraficoMediaSalario(Long userLogado) throws Exception {
		
		String sql = "select avg(rendamensal) as media_salarial, perfil from model_login where usuario_id = ? group by perfil";
	
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, userLogado);
		
		
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    List<String> perfils = new ArrayList<String>();
	    List<Double> salarios = new ArrayList<Double>();
	    
	    BeanDtoGraficoSalarioUser beanDtoGraficoSalarioUser = new BeanDtoGraficoSalarioUser();	
	    
	    while(resultSet.next()) {
	    	Double media_salarial = resultSet.getDouble("media_salarial");
	    	String perfil = resultSet.getString("perfil");
	    	
	    	perfils.add(perfil);
	    	salarios.add(media_salarial);
	    	
	    }
	    
	    beanDtoGraficoSalarioUser.setPerfils(perfils);
		beanDtoGraficoSalarioUser.setSalarios(salarios);
		
		return beanDtoGraficoSalarioUser;
	}

	public BeanDtoGraficoSalarioUser montarGraficoMediaSalario(Long userLogado, String dataInicial, String dataFinal) throws Exception {
		
		String sql = "select avg(rendamensal) as media_salarial, perfil from model_login where usuario_id = ? and dataNascimento >= ? and dataNascimento <= ? group by perfil";
		
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, userLogado);
        preparedStatement.setDate(2, Date.valueOf(new
				SimpleDateFormat("yyyy-mm-dd").format(new
				SimpleDateFormat("dd/mm/yyyy").parse(dataInicial))));
        preparedStatement.setDate(3, Date.valueOf(new
				SimpleDateFormat("yyyy-mm-dd").format(new
				SimpleDateFormat("dd/mm/yyyy").parse(dataFinal))));
		
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    List<String> perfils = new ArrayList<String>();
	    List<Double> salarios = new ArrayList<Double>();
	    
	    BeanDtoGraficoSalarioUser beanDtoGraficoSalarioUser = new BeanDtoGraficoSalarioUser();	
	    
	    while(resultSet.next()) {
	    	Double media_salarial = resultSet.getDouble("media_salarial");
	    	String perfil = resultSet.getString("perfil");
	    	
	    	perfils.add(perfil);
	    	salarios.add(media_salarial);
	    	
	    }
	    
	    beanDtoGraficoSalarioUser.setPerfils(perfils);
		beanDtoGraficoSalarioUser.setSalarios(salarios);
		
		return beanDtoGraficoSalarioUser;
	}

}
