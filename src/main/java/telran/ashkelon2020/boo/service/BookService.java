package telran.ashkelon2020.boo.service;

import telran.ashkelon2020.boo.dto.AuthorDto;
import telran.ashkelon2020.boo.dto.BookDto;

public interface BookService {

	boolean addBook(BookDto bookDto);
	
	BookDto findBookIsbn(String isbn);
	
	BookDto removeBookIsbn(String isbn);
	
	BookDto updateBook(String isbn, String title);
	
	Iterable<BookDto> findBooksByAuthor(String authorName);
	
	Iterable<BookDto> findBooksByPublisher(String publisherName);
	
	Iterable<AuthorDto> findBookAuthors(String isbn);
	
	Iterable<String> findPublishersByAuthor(String authorName);
	
	AuthorDto removeAuthor(String authorName);
	
}