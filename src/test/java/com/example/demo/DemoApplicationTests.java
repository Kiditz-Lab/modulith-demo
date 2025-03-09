package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

@SpringBootTest
class DemoApplicationTests {

	private final ApplicationModules modules = ApplicationModules.of(DemoApplication.class);

	@Test
	void verifyModule() {
		modules.verify();
	}

	@Test
	void writeDocumentation() {
		new Documenter(modules).writeModulesAsPlantUml().writeIndividualModulesAsPlantUml();
	}

}
