package org.gitlab.api.ramla;

import static org.junit.Assume.assumeNoException;

import java.io.IOException;
import java.net.ConnectException;
import java.util.UUID;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabAccessLevel;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.junit.Before;
import org.junit.Test;

public class TestAssignUserToAdminProject {

	GitlabAPI _api;

	private static final String TEST_URL = System.getProperty("TEST_URL",
			"http://localhost");
	private static final String TEST_TOKEN = System.getProperty("TEST_TOKEN",
			"eaawRj5Lk5nG_sMK48em");

	String rand = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

	@Before
	public void setup() throws IOException {
		_api = GitlabAPI.connect(TEST_URL, TEST_TOKEN);
		try {
			_api.dispatch().with("login", "INVALID").with("password", rand)
					.to("session", GitlabUser.class);
		} catch (ConnectException e) {
			assumeNoException("GITLAB not running on '" + TEST_URL
					+ "', skipping...", e);
		} catch (IOException e) {
			final String message = e.getMessage();
			if (!message.equals("{\"message\":\"401 Unauthorized\"}")) {
				throw new AssertionError("Expected an unauthorized message", e);
			}
		}
	}

	@Test
	public void testAffUserProj() throws IOException {

		GitlabProject gitlabProject=_api.createProject("first1");
		
		String username = "8f36728d-userName";
		GitlabUser gitlabUser = _api.getUserViaSudo(username);
		
		
		
		GitlabAccessLevel accessLevel=GitlabAccessLevel.Developer;
		_api.addProjectMember(gitlabProject, gitlabUser, accessLevel);
		System.out.println(gitlabProject.getSshUrl());

	}

	private String randVal(String postfix) {
		return rand + "-" + postfix;
	}

}
