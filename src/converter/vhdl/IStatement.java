package converter.vhdl;

/**
 * concurrent statement
 */
public interface IStatement
{
    /**
     * place in systemC module declaration(.h)
     */
    public String getDeclaration();
    
    /**
     * place in systemC module implementation(.cpp)
     */
    public String getImplements();
    
    /**
     * place in systemC module constructor
     */
    public String getInitCode();
    public String scString();
}
