package dk.emad.humidsensor;

import dk.emad.humidsensor.service.PersistinatorService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AppTests {

	@MockBean
	private PersistinatorService pst;

	@Test
	void contextLoads() {
	}

}
