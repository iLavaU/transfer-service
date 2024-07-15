package com.somecompany.transferservice.service;

import com.somecompany.transferservice.TestDataUtil;
import com.somecompany.transferservice.exception.OwnerNotFoundException;
import com.somecompany.transferservice.model.Owner;
import com.somecompany.transferservice.repository.OwnerRepository;
import com.somecompany.transferservice.service.impl.GetOwnerByUuidUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetOwnerByUuidUseCaseTest {

    @Mock
    OwnerRepository ownerRepository;
    @InjectMocks
    GetOwnerByUuidUseCase getOwnerByUuidUseCase;
    TestDataUtil testDataUtil;

    @BeforeEach
    void setUp() {
        testDataUtil = new TestDataUtil();
    }

    @Test
    void getOwnerByUuidSuccess() {
        when(ownerRepository.findByUuid(testDataUtil.OWNER_1_UUID)).thenReturn(Optional.of(testDataUtil.TEST_OWNER_1));
        Owner owner = getOwnerByUuidUseCase.execute(testDataUtil.OWNER_1_UUID);
        assertEquals(testDataUtil.TEST_OWNER_1, owner);
    }

    @Test
    void getOwnerByUuidFailed() {
        when(ownerRepository.findByUuid(testDataUtil.OWNER_1_UUID)).thenReturn(Optional.empty());
        OwnerNotFoundException exception = assertThrows(OwnerNotFoundException.class, () -> getOwnerByUuidUseCase.execute(testDataUtil.OWNER_1_UUID));
        assertEquals(String.format("Owner with uuid %s not found.", testDataUtil.OWNER_1_UUID), exception.getMessage());
    }
}
