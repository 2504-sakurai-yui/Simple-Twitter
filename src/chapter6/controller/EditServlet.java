package chapter6.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chapter6.beans.Message;
import chapter6.beans.User;
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

		//HttpSession session = request.getSession();
		//Message edit = (Message) session.getAttribute("editMessage");

		//Message message = new MessageService().select(editMessage);

		//top.jspから編集したいメッセージのidを取得
		String edit = request.getParameter("edit");

		//int intEdit = Integer.parseInt(edit);

		Message message = new Message();
		//message.setText(edit);
		new MessageService().select(message);

		request.setAttribute("text", message);

		//編集画面に遷移する
		request.getRequestDispatcher("edit.jsp").forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		HttpSession session = request.getSession();
		Message message = new Message();
		
		//新しく入力されたメッセージ取得
		String text = request.getParameter("text");
		message.setText(text);

		//loginUserからID取得
		User user = (User) session.getAttribute("loginUser");
		message.setId(user.getId());

		new MessageService().update(message);

		response.sendRedirect("./");

	}

	private Message getMessage(HttpServletRequest request) throws IOException, ServletException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		Message message = new Message();
		message.setText(request.getParameter("edit"));

		//新しく入力されたメッセージを返す
		return message;
	}

}
