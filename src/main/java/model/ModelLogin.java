package model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ModelLogin implements Serializable {

	private static final long serialVersionUID = 2472796871797376756L;

	private Long id;
	private String nome;
	private String email;
	private String login;
	private String senha;
	private String sexo;
	private String fotoUser;
	private String extensaofotouser;
	
	private String cep;
	private String localidade;
	private String bairro;
	private String uf;
	private String logradouro;
	private String numero;
	private Date dataNascimento;
	private Double rendamensal;
	
	private List<ModelTelefone> telefones = new ArrayList<ModelTelefone>();
	
	public String getFotoUser() {
		return fotoUser;
	}

	public void setFotoUser(String fotoUser) {
		this.fotoUser = fotoUser;
	}

	public String getExtensaofotouser() {
		return extensaofotouser;
	}

	public void setExtensaofotouser(String extensaofotouser) {
		this.extensaofotouser = extensaofotouser;
	}

	private boolean userAdmin;
	
	private String perfil;
	
	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public boolean getUserAdmin() {
		return userAdmin;
	}

	public void setUserAdmin(boolean userAdmin) {
		this.userAdmin = userAdmin;
	}

	public boolean isNovo() {
		
		if(this.id == null) {
			return true; /*Inserir um novo */
		}else if(this.id != null && this.id > 0){
			return false; /*Atualizar*/
		}
		
		return id == null;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Double getRendamensal() {
		return rendamensal;
	}

	public void setRendamensal(Double rendamensal) {
		this.rendamensal = rendamensal;
	}

	public List<ModelTelefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<ModelTelefone> telefones) {
		this.telefones = telefones;
	}
	
	public String getMostraTelefoneRel() {
		
		String fone = "Telefone:\n\n";
		
		for(ModelTelefone modelTelefone : telefones) {
			fone += modelTelefone.getNumero() + "\n";
		}
		
		return fone;
	}
	
	
	
	
	
	
	
	
	

}
