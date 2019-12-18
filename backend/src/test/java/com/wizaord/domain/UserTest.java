package com.wizaord.domain;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@QuarkusTest
public class UserTest {

    @AfterEach
    public void cleanDatabase() {
        User.deleteAll();
    }

    @Test
    public void persistenceInMongoWork() {
        // given
        User user = new User("username", "email", "password");

        // when
        user.persist();

        // then
        List<PanacheMongoEntityBase> users = User.listAll();
        assertThat(users).isNotEmpty().hasSize(1);
    }
}
