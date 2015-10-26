package jpm.mimacom.tasks.portlet;

import java.io.IOException;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import jpm.mimacom.tasks.app.ComponentManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TasksViewer extends GenericPortlet {

	final static Logger LOGGER = LogManager.getLogger(TasksViewer.class);

	public TasksViewer() {
	}

	@Override
	protected void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		final ResourceURL url = response.createResourceURL();
		request.setAttribute("resourceURL", url);
		getPortletContext().getRequestDispatcher("/WEB-INF/jsp/tasks-view.jsp")
				.include(request, response);
		// super.doView(request, response);
	}

	@Override
	public void serveResource(ResourceRequest arg0, ResourceResponse arg1)
			throws PortletException, IOException {

		final Map<?, ?> userInfo = (Map<?, ?>) arg0
				.getAttribute(PortletRequest.USER_INFO);
		final String userLoginId = (String) userInfo.get("user.login.id");

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("TasksViewer.serveResource: {cmd="
					+ arg0.getParameter("cmd") + ", data="
					+ arg0.getParameter("data") + "}");
		}

		arg1.setContentType("application/json");
		ComponentManager
				.getInstance()
				.getTasksResources()
				.dispatch(userLoginId, arg0.getParameter("cmd"),
						arg0.getParameter("data"),
						arg1.getPortletOutputStream());
		arg1.getPortletOutputStream().flush();

	}

}
