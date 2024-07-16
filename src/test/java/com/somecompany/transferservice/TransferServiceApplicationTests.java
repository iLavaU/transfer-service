package com.somecompany.transferservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"OPEN.EXCHANGE.RATES.API.APPID=testappid",
		"OPEN.EXCHANGE.RATES.API.URL=https://openexchangerates.org/api",
		"DB_HOST_NAME=localhost",
		"DB_PORT_NUMBER=5432",
		"DB_NAME=test",
		"DB_USERNAME=test",
		"DB_PASSWORD=test"
})
class TransferServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
