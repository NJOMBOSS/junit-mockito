package com.junitmockito;

import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.aspectj.util.LangUtil.isEmpty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;


    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenFindAll_thenReturnBooklist(){

        // given
        Book book = Book.builder()
                .bookId(1L)
                .name("Atomic Habits")
                .summary("How to build better habits")
                .rating(5)
                .build();
        List<Book> expectedBooks = Arrays.asList(book);

        doReturn(expectedBooks).when(bookRepository).findAll();

        // when
        List<Book> actualBooks = bookService.getAllBookRecords();

        // then
        assertThat(actualBooks).isEqualTo(expectedBooks);
    }

    @Test
    public void whenFindAll_thenReturnEmpty(){

        // given

        List<Book> expectedBooks = Arrays.asList();

        doReturn(expectedBooks).when(bookRepository).findAll();

        // when
        List<Book> actualBooks = bookService.getAllBookRecords();

        // then
        assertThat(actualBooks).isEqualTo(expectedBooks);
    }

    @Test
    public void createBookTest_thenSucced() {

        Book book  = new Book(1L, "Thinking Fast and Slow","How to create good mental models about thinking",4);

        bookService.createBookRecord(book);

        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void createBookTest_thenUnsucced() {
        Book book1  = new Book(1L, "Thinking Fast and Slow","How to create good mental models about thinking",4);
        Book book2  = new Book(2L, "Thinking Fast and Slow","How to create good mental models about thinking",4);

      when(bookRepository.findByName("Thinking Fast and Slow")).thenReturn(Optional.of(book1));
         bookService.createBookRecord(book2);
     /*     verify(bookRepository).findByName("Thinking Fast and Slow");*/
    }

    @Test
    public void getBookByIdTest(){

       Optional<Book>  books = Optional.of(new Book(1L, "Atomic Habits", "How to build better habits", 5));

        when(bookRepository.findById(1L)).thenReturn(books);

        Book book = bookService.getBookById(1L);

        assertEquals("Atomic Habits", book.getName());
        assertEquals("How to build better habits", book.getSummary());
        assertEquals(5, book.getRating());
    }



    @Test
    public void updateBookTest()throws NotFoundException {

        Book  book = new Book(1L, "Atomic Habits", "How to build better habits", 5);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.updateBookRecord(book);
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(book);


    }




    @Test
    public void deleteBookTest() throws NotFoundException {

        Book  book = new Book(1L, "Atomic Habits", "How to build better habits", 5);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBookById(1L);
        verify(bookRepository).deleteById(1L);

    }

}
