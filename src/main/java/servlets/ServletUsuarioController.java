package servlets;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

import beandto.BeanDtoGraficoSalarioUser;
import dao.DAOUsuarioRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.ModelLogin;
import util.ReportUtil;

@MultipartConfig
@WebServlet(urlPatterns = { "/ServletUsuarioController" })
public class ServletUsuarioController extends ServletGenericUtil {
	private static final long serialVersionUID = 1L;

	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();

	public ServletUsuarioController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String acao = request.getParameter("acao");

		try {

			if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletar")) {
				String idUser = request.getParameter("id");

				daoUsuarioRepository.deletarUser(idUser);

				List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
				request.setAttribute("modolLogins", modelLogins);

				request.setAttribute("msg", "Excluido com sucesso!");
				request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			}

			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletarajax")) {
				String idUser = request.getParameter("id");

				daoUsuarioRepository.deletarUser(idUser);

				response.getWriter().write("Excluido com sucesso!");

			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUserAjax")) {
				String idUser = request.getParameter("nomeBusca");

				List<ModelLogin> dadosJsonUser = daoUsuarioRepository.consultaUsuarioList(idUser,
						super.getUserLogado(request));

				// response.getWriter().write("Excluido com sucesso!");

				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(dadosJsonUser);

				response.addHeader("totalPagina", ""
						+ daoUsuarioRepository.consultaUsuarioListTotalPaginacao(idUser, super.getUserLogado(request)));
				response.getWriter().write(json);

			}

			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUserAjaxPage")) {
				String idUser = request.getParameter("nomeBusca");
				String pagina = request.getParameter("pagina");

				List<ModelLogin> dadosJsonUser = daoUsuarioRepository.consultaUsuarioListOffSet(idUser,
						super.getUserLogado(request), Integer.parseInt(pagina));

				// response.getWriter().write("Excluido com sucesso!");

				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(dadosJsonUser);

				response.addHeader("totalPagina", ""
						+ daoUsuarioRepository.consultaUsuarioListTotalPaginacao(idUser, super.getUserLogado(request)));
				response.getWriter().write(json);

			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarEditar")) {
				String id = request.getParameter("id");

				ModelLogin modelLogin = daoUsuarioRepository.consultaUsuarioPorId(id, super.getUserLogado(request));

				List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
				request.setAttribute("modolLogins", modelLogins);

				request.setAttribute("msg", "Usuário em edição");
				request.setAttribute("modelLogin", modelLogin);
				request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);

			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listarUser")) {
				List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));

				request.setAttribute("msg", "Usuários carregados");
				request.setAttribute("modolLogins", modelLogins);
				request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);

			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("downloadFoto")) {
				String idUser = request.getParameter("id");

				ModelLogin modelLogin = daoUsuarioRepository.consultaUsuarioPorId(idUser, super.getUserLogado(request));
				if (modelLogin.getFotoUser() != null && !modelLogin.getFotoUser().isEmpty()) {

					response.setHeader("Content-Disposition",
							"attachment;filename=arquivo." + modelLogin.getExtensaofotouser());
					response.getOutputStream()
							.write(new Base64().decodeBase64(modelLogin.getFotoUser().split("\\,")[1]));
				}

			}

			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("paginar")) {
				Integer offset = Integer.parseInt(request.getParameter("pagina"));

				List<ModelLogin> modelLogins = daoUsuarioRepository
						.consultaUsuarioListPaginada(this.getUserLogado(request), offset);
				request.setAttribute("modolLogins", modelLogins);
				request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			}

			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("imprimirRelatorioUser")) {

				String dataInicial = request.getParameter("dataInicial");
				String dataFinal = request.getParameter("dataFinal");

				if (dataInicial == null || dataInicial.isEmpty() && dataFinal == null || dataFinal.isEmpty()) {

					request.setAttribute("listaUser",
							daoUsuarioRepository.consultaUsuarioListRel(super.getUserLogado(request)));
				} else {
					request.setAttribute("listaUser", daoUsuarioRepository
							.consultaUsuarioListRel(super.getUserLogado(request), dataInicial, dataFinal));

				}

				request.setAttribute("dataInicial", dataInicial);
				request.setAttribute("dataFinal", dataFinal);
				request.getRequestDispatcher("principal/reluser.jsp").forward(request, response);

			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("imprimirRelatorioPDF")
					|| acao.equalsIgnoreCase("imprimirRelatorioExcel")) {

				String dataInicial = request.getParameter("dataInicial");
				String dataFinal = request.getParameter("dataFinal");

				List<ModelLogin> modelLogins = null;

				if (dataInicial == null || dataInicial.isEmpty() && dataFinal == null || dataFinal.isEmpty()) {

					modelLogins = daoUsuarioRepository.consultaUsuarioListRel(super.getUserLogado(request));
				} else {
					modelLogins = daoUsuarioRepository.consultaUsuarioListRel(super.getUserLogado(request), dataInicial,
							dataFinal);

				}

				HashMap<String, Object> params = new HashMap<String, Object>();
				// params.put("{PARAM_SUB_REPORT}",
				// request.getServletContext().getRealPath("relatorio") + File.separator );

				String subReportPath = request.getServletContext().getRealPath("relatorio") + File.separator;
				params.put("PARAM_SUB_REPORT", subReportPath);

				byte[] relatorio = null;
				String extensal = "";

				if (acao.equalsIgnoreCase("imprimirRelatorioPDF")) {
					relatorio = new ReportUtil().geraRelatorioPdf(modelLogins, "rel-user-jsp", params,
							request.getServletContext());
					extensal = "pdf";
				} else if (acao.equalsIgnoreCase("imprimirRelatorioExcel")) {
					relatorio = new ReportUtil().geraRelatorioExcel(modelLogins, "rel-user-jsp", params,
							request.getServletContext());
					extensal = "xls";
				}

				response.setHeader("Content-Disposition", "attachment;filename=arquivo." + extensal);
				response.getOutputStream().write(relatorio);

			}

			else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("graficoSalario")) {

				String dataInicial = request.getParameter("dataInicial");
				String dataFinal = request.getParameter("dataFinal");

				if (dataInicial == null || dataInicial.isEmpty() && dataFinal == null || dataFinal.isEmpty()) {
					BeanDtoGraficoSalarioUser beanDtoGraficoSalarioUser = daoUsuarioRepository
							.montarGraficoMediaSalario(super.getUserLogado(request));

					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(beanDtoGraficoSalarioUser);

					response.getWriter().write(json);

				} else {
					BeanDtoGraficoSalarioUser beanDtoGraficoSalarioUser = daoUsuarioRepository
							.montarGraficoMediaSalario(super.getUserLogado(request),dataInicial, dataFinal);

					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(beanDtoGraficoSalarioUser);

					response.getWriter().write(json);
				
				}

			} else {

				List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
				request.setAttribute("modolLogins", modelLogins);
				request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);

			}

		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			String msg = "Operação realizada com sucesso!";

			String id = request.getParameter("id");
			String nome = request.getParameter("nome");
			String email = request.getParameter("email");
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");
			String perfil = request.getParameter("perfil");
			String sexo = request.getParameter("sexo");
			String cep = request.getParameter("cep");
			String logradouro = request.getParameter("logradouro");
			String bairro = request.getParameter("bairro");
			String localidade = request.getParameter("localidade");
			String uf = request.getParameter("uf");
			String numero = request.getParameter("numero");
			String dataNascimento = request.getParameter("dataNascimento");
			String rendaMensal = request.getParameter("rendamensal");

			rendaMensal = rendaMensal.replaceAll("\\,", "").replaceAll("R$ ", "");

			// rendaMensal = rendaMensal.split("\\ ")[1].replaceAll("\\.",
			// "").replaceAll("\\,", ".");

			System.out.println("Sexo recebido: " + sexo);

			ModelLogin modelLogin = new ModelLogin();
			modelLogin.setId(id != null && !id.isEmpty() ? Long.parseLong(id) : null);
			modelLogin.setNome(nome);
			modelLogin.setEmail(email);
			modelLogin.setLogin(login);
			modelLogin.setSenha(senha);
			modelLogin.setPerfil(perfil);
			modelLogin.setSexo(sexo);
			modelLogin.setCep(cep);
			modelLogin.setLogradouro(logradouro);
			modelLogin.setBairro(bairro);
			modelLogin.setLocalidade(localidade);
			modelLogin.setUf(uf);
			modelLogin.setNumero(numero);

			// modelLogin.setDataNascimento(Date.valueOf(new
			// SimpleDateFormat("yyyy-mm-dd").format(new
			// SimpleDateFormat("dd/mm/yyyy").parse(dataNascimento))));

			modelLogin.setDataNascimento(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(dataNascimento).getTime()));
			modelLogin.setRendamensal(Double.valueOf(rendaMensal));

			// if(ServletFileUpload.isMultipartContent(request)) {
			// Part part = request.getPart("fileFoto"); /* Pega foto da tela */
			// byte[] foto = IOUtils.toByteArray(part.getInputStream()); /*Converte imagem
			// para byte */
			// String imagemBase64 = new Base64().encodeBase64String(foto);
			// System.out.println(imagemBase64);

			// }

			Part part = request.getPart("fileFoto");
			if (part != null && part.getSize() > 0) {

				byte[] foto = IOUtils.toByteArray(part.getInputStream());
				String imagemBase64 = "data:image/" + part.getContentType().split("\\/")[1] + ";base64,"
						+ new Base64().encodeAsString(foto);

				modelLogin.setFotoUser(imagemBase64);
				modelLogin.setExtensaofotouser(part.getContentType().split("\\/")[1]);
			}

			if (daoUsuarioRepository.validarLogin(modelLogin.getLogin()) && modelLogin.getId() == null) {
				msg = "Já existe usuário com o mesmo login, informe um outro login";
			} else {
				if (modelLogin.isNovo()) {
					msg = "Gravado com sucesso!";
				} else {
					msg = "Atualizado com sucesso!";
				}
				modelLogin = daoUsuarioRepository.gravarUsuario(modelLogin, super.getUserLogado(request));
			}

			List<ModelLogin> modelLogins = daoUsuarioRepository.consultaUsuarioList(super.getUserLogado(request));
			request.setAttribute("modolLogins", modelLogins);

			request.setAttribute("msg", msg);
			request.setAttribute("modelLogin", modelLogin);
			request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
			request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}

	}

}
