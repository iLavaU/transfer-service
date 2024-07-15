package com.somecompany.transferservice.service;

import com.somecompany.transferservice.TestDataUtil;
import com.somecompany.transferservice.dto.owner.OwnerCreationDto;
import com.somecompany.transferservice.mapper.OwnerMapperImpl;
import com.somecompany.transferservice.model.Owner;
import com.somecompany.transferservice.repository.OwnerRepository;
import com.somecompany.transferservice.service.impl.CreateOwnerUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateOwnerUseCaseTest {

    @Mock
    OwnerRepository ownerRepository;
    OwnerMapperImpl ownerMapper = new OwnerMapperImpl();
    CreateOwnerUseCase createOwnerUseCase;

    TestDataUtil testDataUtil;

    @BeforeEach
    public void setUp() {
        createOwnerUseCase = new CreateOwnerUseCase(ownerRepository, ownerMapper);
        testDataUtil = new TestDataUtil();
    }

    @Test
    public void createOwnerSucceed() {
        OwnerCreationDto creationDto = new OwnerCreationDto(
                "Julian",
                "Alvarez",
                "julian.alvarez@test.com"
        );
        when(ownerRepository.save(argThat(owner ->
                owner.getFirstName().equals("Julian") &&
                owner.getLastName().equals("Alvarez") &&
                owner.getEmail().equals("julian.alvarez@test.com")))).thenReturn(testDataUtil.TEST_OWNER_1);
        Owner owner = createOwnerUseCase.execute(creationDto);
        assertEquals(owner, testDataUtil.TEST_OWNER_1);
    }
}
