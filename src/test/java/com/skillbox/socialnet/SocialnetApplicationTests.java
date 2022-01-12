package com.skillbox.socialnet;

import org.junit.platform.suite.api.*;

@Suite
//@SelectClasses( {DefaultControllerTest.class, AccountControllerTest.class, AuthControllerTest.class, DialogControllerTest.class} )
@SelectPackages("com/skillbox/socialnet/controller")
class SocialnetApplicationTests {
}
