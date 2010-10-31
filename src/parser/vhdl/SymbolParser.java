package parser.vhdl;

import java.util.ArrayList;

import parser.Token;

class SymbolParser implements VhdlTokenConstants, VhdlASTConstants, IVhdlType
{
    /**
     * parse interface list kind symbols(port, generic, supprogram parameter)
     * @param symTab current symbol table
     * @param node current node
     * @param kind symbol kind, must be port or generic
     */
    public static void parseInterface_listKind(ASTNode node, int kind, VhdlParser parser) {
        if(node == null) {
            System.err.println("parseInterface_list:null parameter!");
            return;
        }
        if(!(kind == PORT || kind == GENERIC || kind == VARIABLE)) {
            System.err.println("parseInterface_list: not port or generic or subprogram parameter");
            return;
        }
        
        ASTNode iNode = (ASTNode)node.getDescendant(ASTINTERFACE_LIST);
        
        for(int i = 0; i < iNode.getChildrenNum(); i++) {
            // interface_element --> interface_declaration --> declaration
            ASTNode decl = (ASTNode)iNode.getChild(i).getChild(0).getChild(0);
            parseVariableKind(decl, kind, parser);
        }
    }
    
    /**
     * parser variable kind symbols(variable, signal, terminal, file, constant, quantity)
     * @param symTab current symbol table
     * @param node current node
     * @param kind symbol kind
     */
    public static void parseVariableKind(ASTNode node, int kind, VhdlParser parser) {
        if(node == null) {
            System.err.println("parseVariableKind:null parameter!");
            return;
        }
        
        ASTNode iNode = (ASTNode)node.getDescendant(ASTIDENTIFIER_LIST);
        ASTNode tNode = (ASTNode)node.getDescendant(ASTSUBTYPE_INDICATION);
        ASTNode eNode = (ASTNode)node.getDescendant(ASTEXPRESSION);     // get value
        
        Symbol sym = new Symbol();
        sym.kind = kind;
        sym.type = getType(tNode);
        sym.range = getValueRange(tNode, parser);
        sym.typeRange = getTypeRange(tNode, parser);
        if(eNode != null) {
            sym.value = eNode.firstTokenImage();
        }
        
        for(int i = 0; i < iNode.getChildrenNum(); i++) {
            Symbol tmp = sym.clone();
            tmp.name = iNode.getChild(i).getName();
            node.getSymbolTable().addSymbol(tmp);
        }
    }
    
    /**
     * parse type kind(not items of record, physical, enum)
     */
    public static void parseTypeKind(ASTNode node, int kind, VhdlParser parser) {
        if(node == null) {
            System.err.println("parseTypeKind:null parameter!");
            return;
        }
        if(kind != TYPE) {
            System.err.println("parseTypeKind: not a type");
            return;
        }
        
        Symbol sym = new Symbol();
        sym.kind = kind;
        sym.name = ((ASTNode)node.getDescendant(ASTIDENTIFIER)).getName();
        ASTNode tNode = (ASTNode)node.getDescendant(ASTTYPE_DEFINITION);
        if(tNode == null) {
            // incomplete_type_declaration
            node.getSymbolTable().addSymbol(sym);
            return;
        }
        
        // full_type_declaration
        ASTNode tmpNode = (ASTNode)tNode.getDescendant(ASTRANGE);
        sym.range = getRange(tmpNode, parser);
        
        tmpNode = (ASTNode)tNode.getDescendant(ASTSUBTYPE_INDICATION);
        sym.type = getType(tmpNode);
        sym.typeRange = getTypeRange(tmpNode, parser);
        
        if((tmpNode = (ASTNode)tNode.getDescendant(ASTARRAY_TYPE_DEFINITION)) != null) {
            // array type
            ASTNode tmpNode1 = (ASTNode)tmpNode.getDescendant(ASTINDEX_CONSTRAINT);
            if(tmpNode1 != null) {
                sym.arrayRange = getRange((ASTNode)tmpNode1.getDescendant(ASTRANGE), parser);
            }
        }else if((tmpNode = (ASTNode)tNode.getDescendant(ASTRECORD_TYPE_DEFINITION)) != null) {
            // record type
            sym.type = strVhdlType[TYPE_RECORD];
        }else if((tmpNode = (ASTNode)tNode.getDescendant(ASTPHYSICAL_TYPE_DEFINITION)) != null) {
            // physical type
            //sym.type = strVhdlType[TYPE_PHYSICAL];
        }else if((tmpNode = (ASTNode)tNode.getDescendant(ASTENUMERATION_TYPE_DEFINITION)) != null) {
            // enumeration type
            //sym.type = strVhdlType[TYPE_ENUMERATION];
        }
        node.getSymbolTable().addSymbol(sym);
    }
    
    /**
     * parse subtype kind
     */
    public static void parseSubtypeKind(ASTNode node, int kind, VhdlParser parser) {
        if(node == null) {
            System.err.println("parseSubtypeKind:null parameter!");
            return;
        }
        if(kind != SUBTYPE) {
            System.err.println("parseSubtypeKind: not a subtype");
            return;
        }
        
        Symbol sym = new Symbol();
        sym.kind = kind;
        sym.name = ((ASTNode)node.getChildById(ASTIDENTIFIER)).getName();
        
        // get type and range
        ASTNode tmpNode = (ASTNode)node.getDescendant(ASTSUBTYPE_INDICATION);
        sym.type = getType(tmpNode);
        sym.range = getValueRange(tmpNode, parser);
        sym.typeRange = getTypeRange(tmpNode, parser);
        node.getSymbolTable().addSymbol(sym);
    }
    
    /**
     * parse subprogram kind
     */
    public static void parseSubprogramKind(ASTNode node, int kind, VhdlParser parser) {
        if(node == null) {
            System.err.println("parseSubprogramKind:null parameter!");
            return;
        }
        if(node.getId() != ASTSUBPROGRAM_SPECIFICATION) {
            System.err.println("parseSubprogramKind: not a subprogram node");
            return;
        }
        
        Symbol sym = new Symbol();
        sym.kind = kind;
        sym.name = node.getName();
        
        // get return type
        ASTNode tmpNode = (ASTNode)node.getChildById(ASTTYPE_MARK);
        if(tmpNode != null) {
            sym.type = tmpNode.getName();
        }
        
        tmpNode = (ASTNode)node.getDescendant(ASTINTERFACE_LIST);
        if(tmpNode == null) {
            node.getSymbolTable().getParent().addSymbol(sym);
            return;  // no parameters
        }
        
        sym.paramTypeList = new ArrayList<String>();
        
        // get parameters
        for(int i = 0; i < tmpNode.getChildrenNum(); i++) {
            ASTNode tmpNode1 = (ASTNode)((ASTNode)tmpNode.getChild(i))
                                            .getDescendant(ASTSUBTYPE_INDICATION);
            String type = getType(tmpNode1);
            ASTNode iNode = (ASTNode)node.getDescendant(ASTIDENTIFIER_LIST);
            for(int j = 0; j < iNode.getChildrenNum(); j++) {
                sym.paramTypeList.add(type);
            }
        }
        node.getSymbolTable().getParent().addSymbol(sym);   //note: use parent symbol table
    }
    
    /**
     * parse other kind(alias, attribute, group, nature, subnature, component, etc.)
     */
    public static void parseOtherKind(ASTNode node, int kind, VhdlParser parser) {
        if(node == null) {
            System.err.println("parseOtherKind:null parameter!");
            return;
        }
        Symbol sym = new Symbol();
        sym.kind = kind;
        sym.name = node.getName();
        ASTNode tmpNode = (ASTNode)node.getDescendant(ASTTYPE_MARK);
        if(tmpNode != null) {
            sym.type = tmpNode.getName();
        }
        if(kind == COMPONENT) {
            node.getSymbolTable().getParent().addSymbol(sym);
        }else {
            node.getSymbolTable().addSymbol(sym);
        }
    }
    
    /**
     * parse other kind(alias, attribute, group, nature, subnature, component, etc.)
     * specified type
     */
    public static void parseOtherKind(ASTNode node, int kind, String type, VhdlParser parser) {
        if(node == null) {
            System.err.println("parseOtherKind:null parameter!");
            return;
        }
        Symbol sym = new Symbol();
        sym.kind = kind;
        sym.type = type;
        sym.name = node.getName();
        node.getSymbolTable().addSymbol(sym);
    }
    
    /**
     * parse other kind(alias, attribute, group, nature, subnature, component, etc.)
     * specified type and value
     */
    public static void parseOtherKind(ASTNode node, int kind, String type, String value,
                            VhdlParser parser) {
        if(node == null) {
            System.err.println("parseOtherKind:null parameter!");
            return;
        }
        Symbol sym = new Symbol();
        sym.kind = kind;
        sym.type = type;
        sym.value = value;
        sym.name = node.getName();
        node.getSymbolTable().addSymbol(sym);
    }
    
    
    /**
     * get type of subtype_indication
     * @param node
     */
    private static String getType(ASTNode node) {
        if(node == null) {
            return "";
        }
        
        if(node.getId() != ASTSUBTYPE_INDICATION) {
            System.err.println("getType:not subtype_indication");
            return "";
        }
        
        ASTNode tmNode = (ASTNode)node.getChildById(ASTTYPE_MARK);
        if(tmNode == null) {
            System.err.println("no type_mark in subtype indication");
            return "";
        }
        return tmNode.getName();
    }
    
    private static String getSimpleExpression(ASTNode node) {
        String ret = "";
        if(node == null) {
            return "";
        }
        
        Token token = node.first_token;
        while(token != node.last_token) {
            ret += token.image;
            token = token.next;
        }
        ret += token.image;
        return ret;
    }
    
    private static String[] getRange(ASTNode rangeNode, VhdlParser parser) {
        String range[] = null;
        if(rangeNode == null || rangeNode.getChild(0) == null) {
            return null;
        }
        if(rangeNode.getChild(0).getId() == ASTSIMPLE_EXPRESSION) {
            range = new String[3];
            range[0] = getSimpleExpression((ASTNode)rangeNode.getChild(0));
            range[1] = ((ASTNode)rangeNode.getChild(1)).firstTokenImage();
            range[2] = getSimpleExpression((ASTNode)rangeNode.getChild(2));
        }else {
            ASTNode atribute = (ASTNode)rangeNode.getChild(0);
            ASTNode prefix = (ASTNode)atribute.getChild(0);
            ASTNode designator = (ASTNode)atribute.getChildById(ASTATTRIBUTE_DESIGNATOR);
            assert(designator.getName().equalsIgnoreCase("range"));
            Symbol sym = (Symbol)parser.getSymbol(rangeNode, prefix.getName());
            if(sym != null) {
                if(sym.typeRange == null) {
                    range = new String[3];
                    range[0] = "0";
                    range[1] = "to";
                    range[2] = prefix.getName() + ".length() - 1";
                }else {
                    range = ((Symbol)parser.getSymbol(rangeNode, 
                                        prefix.getName())).typeRange.clone();
                }
            }else {
                System.err.println("attribute name: symbol not found");
            }
        }
        return range;
    }
    
    /**
     * get value range of subtype_indication
     */
    private static String[] getValueRange(ASTNode node, VhdlParser parser) {
        if(node == null) {
            return null;
        }
        
        if(node.getId() != ASTSUBTYPE_INDICATION) {
            System.err.println("getValueRange:not subtype_indication");
            return null;
        }
        ASTNode tmNode = (ASTNode)node.getDescendant(ASTRANGE_CONSTRAINT);
        if(tmNode == null) {
            return null;
        }
        
        return getRange((ASTNode)tmNode.getChild(0), parser);
    }
    
    /**
     * get type range of subtype_indication
     */
    private static String[] getTypeRange(ASTNode node, VhdlParser parser) {
        if(node == null) {
            return null;
        }
        
        if(node.getId() != ASTSUBTYPE_INDICATION) {
            System.err.println("getTypeRange:not subtype_indication");
            return null;
        }
        ASTNode tmNode = (ASTNode)node.getDescendant(ASTINDEX_CONSTRAINT);
        if(tmNode == null) {
            return null;
        }
        tmNode = (ASTNode)tmNode.getDescendant(ASTRANGE);
        
        return getRange(tmNode, parser);
    }
}
