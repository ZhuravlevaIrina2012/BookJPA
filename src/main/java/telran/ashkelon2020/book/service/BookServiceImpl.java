package telran.ashkelon2020.book.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telran.ashkelon2020.book.dao.AuthorRepository;
import telran.ashkelon2020.book.dao.BookRepository;
import telran.ashkelon2020.book.dao.PublisherRepository;
import telran.ashkelon2020.book.dto.AuthorDto;
import telran.ashkelon2020.book.dto.BookDto;
import telran.ashkelon2020.book.exceptions.EntityNotFoundException;
import telran.ashkelon2020.book.exceptions.ForbiddenException;
import telran.ashkelon2020.book.model.Author;
import telran.ashkelon2020.book.model.Book;
import telran.ashkelon2020.book.model.Publisher;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	AuthorRepository authorRepository;
	
	@Autowired
	PublisherRepository publisherRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	@Transactional
	public boolean addBook(BookDto bookDto) {
		if (bookRepository.existsById(bookDto.getIsbn())) {
			return false;
		}
		//Publisher
		String publisherName = bookDto.getPublisher();
		Publisher publisher = publisherRepository.findById(publisherName)
				.orElse(publisherRepository.save(new Publisher(publisherName)));
		//Author
		Set<Author> authors = bookDto.getAuthors().stream()
				.map(a -> authorRepository.findById(a.getName()).orElse(authorRepository.save(new Author(a.getName(), a.getBirthDate()))))
				.collect(Collectors.toSet());
		Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher);
		bookRepository.save(book);
		return true;
	}

	@Override
	public BookDto findBookIsbn(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(() -> new EntityNotFoundException());
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto removeBookIsbn(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(() -> new EntityNotFoundException());
		bookRepository.deleteById(isbn);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto updateBook(String isbn, String title) {
		Book book = bookRepository.findById(isbn).orElseThrow(() -> new EntityNotFoundException());
		book.setTitle(title);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	public Iterable<BookDto> findBooksByAuthor(String authorName) {
		return bookRepository.findBookByAuthorsName(authorName).stream()
						.map(b -> modelMapper.map(b, BookDto.class))
						.collect(Collectors.toList());
	}

	@Override
	public Iterable<BookDto> findBooksByPublisher(String publisherName) {
		return bookRepository.findBookByPublisher(publisherName).stream()
						.map(b -> modelMapper.map(b, BookDto.class))
						.collect(Collectors.toList());
	}

	@Override
	public Iterable<AuthorDto> findBookAuthors(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(() -> new EntityNotFoundException());
		return book.getAuthors().stream()
				.map(a -> modelMapper.map(a, AuthorDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<String> findPublishersByAuthor(String authorName) {
		return bookRepository.findBookByAuthorsName(authorName).stream()
					.map(b -> b.getPublisher().getPublisherName())
					.distinct()
					.collect(Collectors.toList());
						
	}
	
	@Override
	@Transactional
	public AuthorDto removeAuthor(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(() -> new EntityNotFoundException());
		if (!bookRepository.findBookByAuthorsName(authorName).isEmpty()) {
			throw new ForbiddenException();
		}
		authorRepository.deleteById(authorName);
		return modelMapper.map(author, AuthorDto.class);
	}

}
