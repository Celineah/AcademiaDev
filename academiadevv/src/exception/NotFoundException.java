package exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String mensagem) {
        super(mensagem);
    }

    public NotFoundException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
