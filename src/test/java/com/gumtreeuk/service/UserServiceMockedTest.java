package com.gumtreeuk.service;

import com.gumtreeuk.entity.Gender;
import com.gumtreeuk.entity.User;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;

@RunWith(JUnitParamsRunner.class)
public class UserServiceMockedTest {


    UserService userService = Mockito.spy(new UserService());

    @Test
    @Parameters(method = "genderValues")
    public void testTheNumberOfPeopleOfGender(int maleNumber, int femaleNumber, Gender genderToFind, int correctNumber) {

        List<User> users = new ArrayList<User>();
        addUsers(maleNumber, Gender.Male, users);
        addUsers(femaleNumber, Gender.Female, users);

        doReturn(users).when(userService).getUsers();

        long genderNumber = userService.countByGender(genderToFind);
        assertThat(genderNumber).isEqualTo(correctNumber);
    }

    private void addUsers(int number, Gender gender, List<User> users) {
        for (int i = 0; i < number; i++) {
            users.add(new User("User", gender, 0));
        }
    }

    private Object[] genderValues() {
        return $(
                $(3, 2, Gender.Female, 2),
                $(3, 2, Gender.Male, 3),
                $(1, 0, Gender.Female, 0),
                $(1, 0, Gender.Male, 1),
                $(0, 3, Gender.Female, 3),
                $(0, 3, Gender.Male, 0)
        );
    }

    @Test
    public void testDaysBetweenTheSameUsers() {
        try {
            List<User> users = new ArrayList<User>();
            users.add(new User("Alice", Gender.Female, 1));
            users.add(new User("Bob", Gender.Male, 1));
            users.add(new User("Alice", Gender.Female, 1));

            doReturn(users).when(userService).getUsers();

            userService.getDaysBetween("Alice", "Bob");
            fail("Address book contains duplicated user names");
        } catch (IllegalArgumentException ex) {
            assertThat(ex).hasMessage("One or two users have not unique names");
        }
    }
}