package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet{

	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
	public EditServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		HttpSession session = request.getSession();

		//top.jspから編集したいメッセージidを取得
		String editId = request.getParameter("editId");

		if (StringUtils.isBlank(editId) || editId.matches("^[^0-9]+$")) {
			session.setAttribute("errorMessages", "不正なパラメータが入力されました");
			response.sendRedirect("./");
			return;
		}

		int intEditId = Integer.parseInt(editId);

		Message message = new MessageService().select(intEditId);

		if (message == null) {
			session.setAttribute("errorMessages", "不正なパラメータが入力されました");
			response.sendRedirect("./");
			return;
		}

		request.setAttribute("message", message);

		//編集画面に遷移する
		request.getRequestDispatcher("edit.jsp").forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		Message message = getMessage(request);
		List<String> errorMessages = new ArrayList<String>();

		if (!isValid(message, errorMessages)) {
			request.setAttribute("errorMessages", errorMessages);
			request.setAttribute("message", message);
			request.getRequestDispatcher("edit.jsp").forward(request, response);
			return;
		}

		new MessageService().update(message);

		response.sendRedirect("./");

	}

	//新しく入力されたメッセージ情報取得
	private Message getMessage(HttpServletRequest request) throws IOException, ServletException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		Message message = new Message();
		message.setId(Integer.parseInt(request.getParameter("id")));
		message.setText(request.getParameter("text"));

		//新しく入力されたメッセージを返す
		return message;
	}

	private boolean isValid(Message message, List<String> errorMessages) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		String text = message.getText();

		if (!StringUtils.isEmpty(text) && (140 < text.length())) {
			errorMessages.add("140文字以下で入力してください");
		}

		if (StringUtils.isBlank(text)) {
			errorMessages.add("入力してください");
		}

		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}

}
