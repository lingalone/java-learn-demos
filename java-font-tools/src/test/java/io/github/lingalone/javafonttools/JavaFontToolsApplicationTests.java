package io.github.lingalone.javafonttools;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.java2d.cmm.CMMServiceProvider;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JavaFontToolsApplicationTests {

	@Test
	public void contextLoads() {
		CMMServiceProvider.class.getClass();
	}

}
