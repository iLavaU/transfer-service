package com.somecompany.transferservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.somecompany.transferservice.dto.OwnerDto;
import com.somecompany.transferservice.dto.request.AccountDto;
import com.somecompany.transferservice.dto.request.MakeTransferRequestDto;
import com.somecompany.transferservice.dto.response.AccountCreationDto;
import com.somecompany.transferservice.dto.response.BaseResponseDto;
import com.somecompany.transferservice.dto.response.OwnerCreationDto;
import com.somecompany.transferservice.model.Account;
import com.somecompany.transferservice.repository.AccountRepository;
import com.somecompany.transferservice.service.impl.GetAccountByUuidUseCase;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.math.BigDecimal;
import java.util.Objects;

import static com.somecompany.transferservice.TestDataUtil.ownerCreationReq1;
import static com.somecompany.transferservice.TestDataUtil.ownerCreationReq2;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DisabledIf("true")
@ActiveProfiles("test")
public class IntegrationTestsV1 {

    @LocalServerPort
    private Integer port;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withCopyFileToContainer(
                    MountableFile.forClasspathResource("/scripts/db-table-creation-script.sql"),
                    "/docker-entrypoint-initdb.d/"
            )
            .withDatabaseName("financedb")
            .withUsername("postgres")
            .withPassword("postgres")
            .withExposedPorts(5432);

    @Container
    private static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    @Container
    private static final MockServerContainer mockOERContainer = new MockServerContainer(
            DockerImageName.parse("mockserver/mockserver:5.15.0")
    );

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GetAccountByUuidUseCase getAccountByUuidUseCase;
    @Autowired
    private AccountRepository accountRepository;
    private RestClient restClient;

    private static MockServerClient mockServerClient;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                () -> String.format("jdbc:postgresql://localhost:%d/%s?user=%s&password=%s",
                        postgres.getFirstMappedPort(),
                        postgres.getDatabaseName(),
                        postgres.getUsername(),
                        postgres.getPassword()));
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.kafka.producer.retries", ()->0);
        registry.add("kafka.producer.pending-transfer.topic", ()->"test-topic");
        registry.add("kafka.consumer.pending-transfer.groupid", ()->"test-group-id");

        registry.add("open.exchange.rates.api.appID", ()->"test-appid");
        registry.add("open.exchange.rates.api.url", ()->"https://openexchangerates.org/api");
    }

    @BeforeAll
    static void beforeAll() {
        mockServerClient =
                new MockServerClient(
                        mockOERContainer.getHost(),
                        mockOERContainer.getServerPort()
                );
    }

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder().baseUrl(String.format("http://localhost:%s", port)).build();
        RestAssured.baseURI = "http://localhost:" + port;
        mockServerClient.reset();
    }

    @Test
    void testOwnerCreation() {
        given()
                .contentType("application/json")
                .body(ownerCreationReq2)
        .when()
                .post("/owner/create")
        .then()
                .statusCode(201)
                .contentType("application/json");
    }

    @Test
    void testFull() throws JsonProcessingException {
        ResponseEntity<BaseResponseDto<OwnerDto>> ownerCreationRes1 = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/owner/create").build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.readValue(ownerCreationReq1, OwnerCreationDto.class))
                .retrieve().toEntity(new ParameterizedTypeReference<>() {});


        ResponseEntity<BaseResponseDto<OwnerDto>> ownerCreationRes2 = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/owner/create").build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.readValue(ownerCreationReq2, OwnerCreationDto.class))
                .retrieve().toEntity(new ParameterizedTypeReference<>() {});

        ResponseEntity<BaseResponseDto<AccountDto>> accountCreationRes1 = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/account/create").build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AccountCreationDto(Objects.requireNonNull(ownerCreationRes1.getBody()).getData().uuid(), "USD"))
                .retrieve().toEntity(new ParameterizedTypeReference<>() {});

        ResponseEntity<BaseResponseDto<AccountDto>> accountCreationRes2 = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/account/create").build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AccountCreationDto(Objects.requireNonNull(ownerCreationRes2.getBody()).getData().uuid(), "USD"))
                .retrieve().toEntity(new ParameterizedTypeReference<>() {});

        Account account1 = getAccountByUuidUseCase.execute(Objects.requireNonNull(accountCreationRes1.getBody()).getData().uuid());
        Account account2 = getAccountByUuidUseCase.execute(Objects.requireNonNull(accountCreationRes2.getBody()).getData().uuid());
        account1.setBalance(BigDecimal.valueOf(100));
        account2.setBalance(BigDecimal.valueOf(100));
        accountRepository.save(account1);
        accountRepository.save(account2);

        MakeTransferRequestDto makeTransferRequestDto = new MakeTransferRequestDto();
        makeTransferRequestDto.setAmount(BigDecimal.valueOf(1));
        makeTransferRequestDto.setOriginAccountUUID(account1.getUuid());
        makeTransferRequestDto.setRecipientAccountUUID(account2.getUuid());

        ResponseEntity<BaseResponseDto<AccountDto>> l = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/transfer/v1").build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(makeTransferRequestDto)
                .retrieve().toEntity(new ParameterizedTypeReference<>() {});

        Account modifiedAccount1 = getAccountByUuidUseCase.execute(Objects.requireNonNull(accountCreationRes1.getBody()).getData().uuid());
        Account modifiedAccount2 = getAccountByUuidUseCase.execute(Objects.requireNonNull(accountCreationRes2.getBody()).getData().uuid());


        assertEquals(ownerCreationRes1.getStatusCode().value(), 201);
        assertNotNull(ownerCreationRes1.getBody());
        assertEquals(ownerCreationRes2.getStatusCode().value(), 201);
        assertNotNull(ownerCreationRes2.getBody());

        assertEquals(modifiedAccount1.getBalance(), BigDecimal.valueOf(99));
        assertEquals(modifiedAccount2.getBalance(), BigDecimal.valueOf(101));
    }
}
