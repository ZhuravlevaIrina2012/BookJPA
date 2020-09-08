package telran.ashkelon2020.book.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import telran.ashkelon2020.book.model.Author;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookDto {
	String isbn;
	String title;
	@Singular
	Set<Author> authors;
	String publisher;
}
