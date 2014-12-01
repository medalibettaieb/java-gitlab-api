package org.gitlab.api.ramla;

import static org.junit.Assume.assumeNoException;

import java.io.IOException;
import java.net.ConnectException;
import java.util.UUID;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabUser;
import org.junit.Before;
import org.junit.Test;

public class TestAddUser {

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
	public void testAddUser() {
		String password = "aaaaaaaa";

		try {
			GitlabUser gitUser = _api.createUser(randVal("testUser@esprit.tn"),
					password, randVal("userName"), randVal("fullName"),
					randVal("skypeId"), randVal("linledin"),
					randVal("twitter"), "http://" + randVal("url.com"), 10,
					randVal("externuid"), randVal("externprovidername"),
					randVal("bio"), false, false, false);
			System.out.println(gitUser.getId());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String randVal(String postfix) {
		return rand + "-" + postfix;
	}

}
