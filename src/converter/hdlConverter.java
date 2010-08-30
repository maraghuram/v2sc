package converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Stack;

import parser.IASTNode;
import parser.IParser;
import parser.ParserException;
import parser.vhdl.ASTNode;

public abstract class hdlConverter implements SCTreeConstants
{
    static public final int T_NONE = 0;
    static public final int T_VHDL = 1;
    static public final int T_VERILOG = 2;
    
    String[] m_hdlFileContents = null;
    protected PrintStream m_targetFileBuff = null;

    protected String readFile(String path) throws FileNotFoundException, IOException
    {
        File file = new File(path);
        Reader reader;
        
        reader = new FileReader(file);
        BufferedReader breader = new BufferedReader(reader);
        String s, s2 = new String();

        while((s = breader.readLine())!= null)
        {
            s2 += s + "\n";
        }
        m_hdlFileContents = s2.split("\n");
        breader.close();

        return s2;
    }
    
    protected void createFile(String path, boolean replace) throws IOException
    {
        File file = new File(path);
        if(file.exists() && !replace)
            return;
        m_targetFileBuff = new PrintStream(file);
    }
    
    public abstract void convertFile(String srcPath, String dstPath) 
                throws ParserException, FileNotFoundException, IOException;    
    public abstract void convertDir(String srcDir);    
    public abstract void addLibary(String srcDir, String libName);
}