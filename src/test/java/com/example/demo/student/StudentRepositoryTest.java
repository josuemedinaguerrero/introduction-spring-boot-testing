package com.example.demo.student;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class StudentRepositoryTest {

  @Autowired
  private StudentRepository studentRepository;

  @AfterEach
  void tearDown() {
    studentRepository.deleteAll();
  }

  @Test
  void isShouldCheckWhenStudentEmailExists() {
    // given
    String email = "vanessa@test.com";
    Student student = new Student("Vanessa", email, Gender.FEMALE);
    studentRepository.save(student);

    // when
    boolean expected = studentRepository.selectExistsEmail(email);

    // then
    assertThat(expected).isTrue();
  }

  @Test
  void isShouldCheckWhenStudentEmailDoesNotExists() {
    // given
    String email = "vanessa@test.com";

    // when
    boolean expected = studentRepository.selectExistsEmail(email);

    // then
    assertThat(expected).isFalse();
  }

}
