package com.junitmockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    Book RECORD_1 = new Book(1L, "Atomic Habits","How to build better habits",5);
    Book RECORD_2 = new Book(2L, "Thinking Fast and Slow","How to create good mental models about thinking",4);
    Book RECORD_3 = new Book(3L, "Grokking Algorithms","Learn algorithms the fun way",5);

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks( this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void getAllRecords_success() throws Exception{
        List<Book> records = new ArrayList<>(Arrays.asList(RECORD_1,RECORD_2,RECORD_3));

        Mockito.when(bookService.getAllBookRecords()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name").value("Grokking Algorithms"));

    }

    @Test
    public void getBookById_success() throws Exception{
        Mockito.when(bookService.getBookById(RECORD_1.getBookId())).thenReturn(RECORD_1);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", notNullValue()))
                        .andExpect(jsonPath("$.name").value("Atomic Habits"));
    }

    @Test
    public void createRecord_success() throws Exception{
        Book record = Book.builder()
                .bookId(4L)
                .name("Introduction to C")
                .summary("The name but longer")
                .rating(5)
                .build();

        Mockito.when(bookService.createBookRecord(record)).thenReturn(record);

        String content = objectWriter.writeValueAsString(record);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name").value("Introduction to C"));

    }

    @Test
    public void updateBookRecord_success() throws Exception{
        Book updateRecord = Book.builder()
                .bookId(1L)
                .name("Updated Book Name")
                .summary("Updated Summary")
                .rating(1)
                .build();

       Mockito.when(bookService.getBookById(RECORD_1.getBookId())).thenReturn(RECORD_1);
        Mockito.when(bookService.createBookRecord(updateRecord)).thenReturn(updateRecord);

        String updateContent = objectWriter.writeValueAsString(updateRecord);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updateContent);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
               .andExpect(jsonPath("$.name").value("Updated Book Name"));

    }

    @Test
    public void deleteBookById_success() throws Exception{
        //Mockito.when(bookRepository.findById(RECORD_2.getBookId())).thenReturn(java.util.Optional.ofNullable(RECORD_2));
        Mockito.when(bookService.getBookById(RECORD_2.getBookId())).thenReturn(RECORD_2);
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/book/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
 }

}
