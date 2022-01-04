package com.skillbox.socialnet;

import com.skillbox.socialnet.controller.AccountControllerTest;
import com.skillbox.socialnet.controller.AuthControllerTest;
import com.skillbox.socialnet.controller.DialogControllerTest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({AccountControllerTest.class, AuthControllerTest.class, DialogControllerTest.class} )
class SocialnetApplicationTests {
}
