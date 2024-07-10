package com.example.demo.student;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;

// import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

  @Mock
  private StudentRepository studentRepository;

  // private AutoCloseable autoCloseable;
  private StudentService studentService;

  @BeforeEach
  void setup() {
    // autoCloseable = MockitoAnnotations.openMocks(this);
    studentService = new StudentService(studentRepository);

  }

  // @AfterEach
  // void tearDown() throws Exception {
  // autoCloseable.close();
  // }

  @Test
  void getAllStudents() {
    // When
    studentService.getAllStudents();

    // Then
    verify(studentRepository).findAll();
  }

  @Test
  void addStudent() {
    // Given
    Student student = new Student("Vanessa", "vanessa@test.com", Gender.FEMALE);

    // When
    studentService.addStudent(student);

    // Then
    ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
    verify(studentRepository).save(studentArgumentCaptor.capture());

    Student capturedStudent = studentArgumentCaptor.getValue();
    assertThat(capturedStudent).isEqualTo(student);
  }

  @Test
  void willThrowWhenEmailIsTaken() {
    // Given
    Student student = new Student("Vanessa", "vanessa@test.com", Gender.FEMALE);
    given(studentRepository.selectExistsEmail(student.getEmail())).willReturn(true);

    // Then
    assertThatThrownBy(() -> studentService.addStudent(student))
        .isInstanceOf(BadRequestException.class)
        .hasMessageContaining("Email " + student.getEmail() + " taken");

    verify(studentRepository, never()).save(student);
  }

  @Test
  void deleteStudent() {
    // Given
    Long studentId = 1L;
    given(studentRepository.existsById(studentId)).willReturn(true);

    // When
    studentService.deleteStudent(studentId);

    /// Then
    verify(studentRepository).deleteById(studentId);

  }

  @Test
  // @Disabled
  void willThrowStudentDoesNotExist() {
    // Given
    Long studentId = 1L;
    given(studentRepository.existsById(studentId)).willReturn(false);

    /// Then
    assertThatThrownBy(() -> studentService.deleteStudent(studentId))
        .isInstanceOf(StudentNotFoundException.class)
        .hasMessageContaining("Student with id " + studentId + " does not exists");

    verify(studentRepository, never()).deleteById(studentId);
  }

}
