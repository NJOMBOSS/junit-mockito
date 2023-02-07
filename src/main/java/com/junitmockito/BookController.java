package com.junitmockito;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/book")
public class BookController {

    @Autowired
    BookService bookService;
    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBookRecords(){
        return bookService.getAllBookRecords();
    }

    @GetMapping(value="{bookId}")
    public Book getBookById(@PathVariable(value = "bookId") Long bookId){
        return bookService.getBookById(bookId);
    }

    @PostMapping
   public Book createBookRecord(@RequestBody @Valid Book bookRecord){
        return bookService.createBookRecord(bookRecord);
    }

    @PutMapping
    public Book updateBookRecord(@RequestBody @Valid Book bookRecord) throws NotFoundException {
        return bookService.updateBookRecord(bookRecord);
    }

    @DeleteMapping(value = "{bookId}")
    public void deleteBookById(@PathVariable(value = "bookId") Long bookId) throws NotFoundException {

        bookService.deleteBookById(bookId);
    }

}
