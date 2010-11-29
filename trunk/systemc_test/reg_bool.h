/*
 * reg_bool: inheritance of sc_signal and sc_concat_bool
 * operate the same as bool, but can be used as signal
 */

#ifndef __REG_BOOL_H__
#define __REG_BOOL_H__

#include <systemc.h>

using namespace sc_dt;

class reg_bool : public sc_signal<bool>
{
public:

    // constructors

    reg_bool()
        : sc_signal<bool>()
    { }

    reg_bool( bool v )
        : sc_signal<bool>()
    { sc_signal<bool>::m_cur_val = v; }

    reg_bool( const reg_bool& a )
        : sc_signal<bool>()
    { sc_signal<bool>::m_cur_val = a.read(); }


    // assignment operators

    reg_bool& operator = ( bool v )
    { sc_signal<bool>::write(v); return *this; }

    reg_bool& operator = ( uint_type v )
    { sc_signal<bool>::write((bool)v); return *this; }

    reg_bool& operator = ( unsigned long a )
    { sc_signal<bool>::write((bool)a); return *this; }

    reg_bool& operator = ( long a )
    { sc_signal<bool>::write((bool)a); return *this; }

    reg_bool& operator = ( unsigned int a )
    { sc_signal<bool>::write((bool)a); return *this; }

    reg_bool& operator = ( int a )
    { sc_signal<bool>::write((bool)a); return *this; }

    reg_bool& operator = ( int64 a )
    { sc_signal<bool>::write((bool)a); return *this; }

    reg_bool& operator = ( double a )
    { sc_signal<bool>::write((bool)a); return *this; }

    // bitwise assignment operators

    reg_bool& operator &= ( uint_type v )
    { int value = sc_signal<bool>::read();
        sc_signal<bool>::write(value&v); return *this; }

    reg_bool& operator |= ( uint_type v )
    { int value = sc_signal<bool>::read();
        sc_signal<bool>::write(value|v); return *this; }

    reg_bool& operator ^= ( uint_type v )
    { int value = sc_signal<bool>::read();
        sc_signal<bool>::write(value^v); return *this; }

    bool operator ! ()
    { return !sc_signal<bool>::read(); }

    // relational operators
    friend bool operator == ( const reg_bool& a, const reg_bool& b )
    { return a.read() == b.read(); }

    friend bool operator == ( const reg_bool& a, const int& b )
    { return a.read() == b; }

    friend bool operator == ( const int& a, const reg_bool& b )
    { return a == b.read(); }

    friend bool operator != ( const reg_bool& a, const reg_bool& b )
    { return a.read() != b.read(); }

    friend bool operator <  ( const reg_bool& a, const reg_bool& b )
    { return a.read() < b.read(); }

    friend bool operator <= ( const reg_bool& a, const reg_bool& b )
    { return a.read() <= b.read(); }

    friend bool operator >  ( const reg_bool& a, const reg_bool& b )
    { return a.read() > b.read(); }

    friend bool operator >= ( const reg_bool& a, const reg_bool& b )
    { return a.read() >= b.read(); }

    int to_int() const
    { return (int)sc_signal<bool>::read(); }
};

inline
const
sc_dt::sc_concatref& operator , (const reg_bool& a, const reg_bool& b)
{
    const sc_dt::sc_concat_bool* a_p;      // Proxy for boolean value.
    const sc_dt::sc_concat_bool* b_p;      // Proxy for boolean value.
    sc_dt::sc_concatref*         result_p; // Proxy for the concatenation.

    a_p = sc_dt::sc_concat_bool::allocate(a.read());
    b_p = sc_dt::sc_concat_bool::allocate(b.read());
    result_p = sc_dt::sc_concatref::m_pool.allocate();
    result_p->initialize( *a_p, *b_p );
    return *result_p;
}

#endif /* __REG_BOOL_H__ */
