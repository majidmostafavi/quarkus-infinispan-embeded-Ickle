package infrastructure.adapter.in;

import application.dto.BookDTO;
import application.dto.BookSearchResultDTO;
import application.port.in.*;
import domain.exception.DomainException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    CreateBookUseCase createBookUseCase;

    @Inject
    GetBookUseCase getBookUseCase;

    @Inject
    UpdateBookUseCase updateBookUseCase;

    @Inject
    DeleteBookUseCase deleteBookUseCase;

    @Inject
    SearchBookUseCase searchBookUseCase;

    // ── CRUD Endpoints ────────────────────────────────────────────────────────

    @POST
    @Path("/{key}")
    public Response createBook(@PathParam("key") String key, BookDTO bookDTO) {
        try {
            BookDTO created = createBookUseCase.createBook(key, bookDTO);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (DomainException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{key}")
    public Response getBook(@PathParam("key") String key) {
        return getBookUseCase.getBook(key)
                .map(dto -> Response.ok(dto).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Book not found: " + key).build());
    }

    @GET
    public List<BookDTO> getAllBooks() {
        return getBookUseCase.getAllBooks();
    }

    @PUT
    @Path("/{key}")
    public Response updateBook(@PathParam("key") String key, BookDTO bookDTO) {
        try {
            BookDTO updated = updateBookUseCase.updateBook(key, bookDTO);
            return Response.ok(updated).build();
        } catch (DomainException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{key}")
    public Response deleteBook(@PathParam("key") String key) {
        try {
            deleteBookUseCase.deleteBook(key);
            return Response.noContent().build();
        } catch (DomainException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    // ── Search Endpoints ──────────────────────────────────────────────────────

    @GET
    @Path("/search/title")
    public BookSearchResultDTO searchByTitle(@QueryParam("q") String title) {
        return searchBookUseCase.searchByTitle(title);
    }

    @GET
    @Path("/search/author")
    public BookSearchResultDTO searchByAuthor(
            @QueryParam("firstname") String firstname,
            @QueryParam("surname") String surname) {
        return searchBookUseCase.searchByAuthor(firstname, surname);
    }

    @GET
    @Path("/search/description")
    public BookSearchResultDTO searchByDescription(@QueryParam("q") String keyword) {
        return searchBookUseCase.searchByDescription(keyword);
    }

    @GET
    @Path("/search/price")
    public BookSearchResultDTO searchByPriceRange(
            @QueryParam("min") @DefaultValue("0") float min,
            @QueryParam("max") @DefaultValue("9999") float max) {
        return searchBookUseCase.searchByPriceRange(min, max);
    }

    @GET
    @Path("/sorted/author")
    public List<BookDTO> getAllSortedByAuthor() {
        return searchBookUseCase.getAllSortedByAuthor();
    }
}
