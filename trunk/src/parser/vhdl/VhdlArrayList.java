package parser.vhdl;

import java.util.ArrayList;
import java.util.Collection;

import parser.INameObject;

/**
 * no reduplicated elements ArrayList
 */
public class VhdlArrayList<E extends INameObject> extends ArrayList<E>
{
    private static final long serialVersionUID = 6099154330118133178L;
    
    @Override
    public boolean add(E e)
    {
        if(e == null) {
            return false;
        }

        for(int i = 0; i < size(); i++) {
            if(e.equals(get(i))) {
                return false;
            }
        }
        return super.add(e);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        if(c == null) {
            return false;
        }
        
        Object[] eArray = c.toArray();

        for(int i = 0; i < eArray.length; i++) {
            add((E)eArray[i]);
        }
        return true;
    }
    
    public boolean addAll(E[] c)
    {
        if(c == null) {
            return false;
        }
        for(int i = 0; i < c.length; i++) {
            add(c[i]);
        }
        return true;
    }
    
    public E get(String name)
    {
        if(name == null || name.isEmpty()) {
            return null;
        }
        for(int i = 0; i < size(); i++) {
            if(name.equalsIgnoreCase(get(i).getName())) {
                return get(i);
            }
        }
        return null;
    }
}