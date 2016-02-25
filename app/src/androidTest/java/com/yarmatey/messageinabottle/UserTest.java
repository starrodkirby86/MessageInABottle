package com.yarmatey.messageinabottle;

import android.test.AndroidTestCase;

import com.yarmatey.messageinabottle.login.User;

/**
 * Created by jason on 2/23/16.
 */
public class UserTest extends AndroidTestCase {


    User userTest;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        userTest = new User();
    }

    public void testUserName() throws Exception {
        String name = "Davy Jones";
        userTest.setUsername(name);
        assertEquals(name, userTest.getUsername());
    }

    public void testUserEmail() throws Exception {
        String email = "messageinabottle@yarmatey.com";
        userTest.setEmail(email);
        assertEquals(email, userTest.getEmail());
    }



    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
