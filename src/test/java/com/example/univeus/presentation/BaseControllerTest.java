package com.example.univeus.presentation;

import com.example.univeus.common.TestAuthConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

@WebMvcTest
@Import(TestAuthConfig.class)
public abstract class BaseControllerTest {
}
