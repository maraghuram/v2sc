/**
 * 
 * This file is based on the VHDL parser originally developed by
 * (c) 1997 Christoph Grimm,
 * J.W. Goethe-University Frankfurt
 * Department for computer engineering
 *
 **/
package parser.vhdl;

import java.util.ArrayList;

public class SymbolTable
{
    /**
     * Hierarchie of symbol tables is stored using this variable. The upper
     * symbol table is the enclosing architecture, entity, ... scope.
     */
    SymbolTable parentTable = null;
    
    /**
     * arraylist, where the symbols are stored.
     */
    ArrayList<Symbol> symbols = new ArrayList<Symbol>();
    
    public SymbolTable() {}
    public SymbolTable(SymbolTable upsym) {
        parentTable = upsym;
    }

    /**
     * Add an identifier to symbol table
     */
    public void addSymbol(Symbol s) {
        symbols.add(s);
    }

    /**
     * Get a symbol from the symbol table
     */
    public Symbol getSymbol(String identifier) {
        int i;
        for (i = 0; i < symbols.size(); i++) {
            if (identifier.compareTo(symbols.get(i).name) == 0)
                return symbols.get(i);
        }
        return parentTable.getSymbol(identifier);
    }
    
    public SymbolTable getParentTable() {
        return parentTable;
    }
    
    public void setParentTable(SymbolTable sym) {
        parentTable = sym;
    }
}