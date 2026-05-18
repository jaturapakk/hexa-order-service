package com.order;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class RealtimeOrderApplicationTest {

    @Test
    void main() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(RealtimeOrderApplication.class, new String[]{}))
                    .thenReturn(null);

            RealtimeOrderApplication.main(new String[]{});

            mocked.verify(() -> SpringApplication.run(RealtimeOrderApplication.class, new String[]{}));
        }
    }
}
