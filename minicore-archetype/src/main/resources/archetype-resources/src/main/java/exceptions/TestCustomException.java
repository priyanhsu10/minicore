#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.exceptions;
//Custom Exception creation as per need your application
public class TestCustomException extends  RuntimeException{
    public TestCustomException(String message) {
        super(message);
    }
}
