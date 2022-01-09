package com.skillbox.socialnet;

import com.skillbox.socialnet.controller.AccountControllerTest;
import com.skillbox.socialnet.controller.AuthControllerTest;
import com.skillbox.socialnet.controller.DefaultControllerTest;
import com.skillbox.socialnet.controller.DialogControllerTest;
import org.junit.platform.suite.api.*;

@Suite
//@SelectClasses( {DefaultControllerTest.class, AccountControllerTest.class, AuthControllerTest.class, DialogControllerTest.class} )
@SelectPackages("com/skillbox/socialnet/controller")
class SocialnetApplicationTests {
}
