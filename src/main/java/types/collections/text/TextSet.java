package types.collections.text;

import jnr.ffi.Pointer;
import types.collections.base.Base;
import types.collections.base.Set;
import functions.functions;


/**
 * Class for representing a set of text values.
 * <p>
 *     "TextSet" objects can be created with a single argument of type string as
 *     in MobilityDB.
 *  <p>
 *         >>> TextSet(string='{a, b, c, def}')
 * <p>
 *     Another possibility is to create a ``TextSet`` object from a list of strings.
 * <p>
 *         >>> TextSet(elements=['a', 'b', 'c', 'def'])
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public class TextSet extends Set<String> {
    private Pointer _inner;


    /** ------------------------- Constructors ---------------------------------- */

    public TextSet(){
        super();
    }

    public TextSet(String str){
        super(str);
        _inner = functions.textset_in(str);
    }

    public TextSet(Pointer inner){
        super(inner);
        _inner = inner;
    }
    @Override
    public Pointer createStringInner(String str){
        return functions.textset_in(str);
    }

    @Override
    public Pointer createInner(Pointer inner){
        return _inner;
    }


    public String as_hexwkb(){
        String pr = functions.set_as_hexwkb(this.get_inner(),(byte) -1);
        return pr;
    }


    /* ------------------------- Output ---------------------------------------- */

    /**
     * Return the string representation of the content of ``self``.
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>textset_out</li>
     *
     * @return A new {@link String} instance
     */
    public String toString(){
        return functions.textset_out(this._inner);
    }


    /** ------------------------- Accessors ------------------------------------- */

    public Pointer get_inner(){
        return this._inner;
    }

    /**
     * Returns the first element in "this".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>textset_start_value</li>
     *
     * @return A {@link String} instance
     */
    public String start_element() {
        return functions.text2cstring(functions.textset_start_value(this._inner));
    }

    /**
     * Returns the last element in "this".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>textset_end_value</li>
     *
     * @return A {@link String} instance
     */
    public String end_element(){
        return functions.text2cstring(functions.textset_end_value(this._inner));
    }

    /**
     * Returns the "n"-th element in "this".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>textset_value_n</li>
     *
     * @param n The 0-based index of the element to return.
     * @return A {@link String} instance
     * @throws Exception
     */
    public String element_n(int n) throws Exception {
        super.element_n(n);
        return functions.text2cstring(functions.textset_value_n(this._inner,n));
    }

    /* ------------------------- Topological Operations -------------------------------- */

    /**
     * Returns whether "this" contains "content".
     *
     *   <p>
     *
     *         MEOS Functions:
     *             <li>contains_set_set</li>
     *             <li>contains_textset_text</li>
     *
     * @param other object to compare with
     * @return True if contains, False otherwise
     * @throws Exception
     */
    public boolean contains(Object other) throws Exception {
        if (other instanceof String){
            TextSet tset = new TextSet((String)other);
            return functions.contains_textset_text(this._inner,tset._inner);
        }
        else {
            return super.contains((Base)other);
        }
    }


    /* ------------------------- Transformations -------------------------------- */


    /**
     * Returns a new textset that is the result of appling uppercase to "this"
     *
     * <p>
     *         MEOS Functions:
     *             <li>textset_lower</li>
     *
     * @return A new {@link TextSet} instance
     */
    public TextSet lowercase(){
        return new TextSet(functions.textset_lower(this._inner));
    }

    /**
     * Returns a new textset that is the result of appling uppercase to "this"
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>textset_upper</li>
     *
     * @return A new {@link TextSet} instance
     */
    public TextSet uppercase(){
        return new TextSet(functions.textset_upper(this._inner));
    }



    /* ------------------------- Set Operations -------------------------------- */


    /**
     * Returns the intersection of "this" and "other".
     *
     * <p>
     *         MEOS Functions:
     *             <li>intersection_textset_text</li>
     *             <li>intersection_set_set</li>
     * @param ts A {@link TextSet} or {@link String} instance
     * @return An object of the same type as "other" or null if the intersection is empty.
     */
    public TextSet intersection(TextSet ts){
        return new TextSet(functions.intersection_set_set(this._inner, ts._inner));
    }



    /**
     * Returns the difference of "this" and "other".
     *
     * <p>
     *
     *         MEOS Functions:
     *             <li>minus_textset_text</li>
     *             <li>minus_set_set</li>
     *
     * @param other A {@link TextSet} or {@link String} instance
     * @return A {@link TextSet} instance or "null" if the difference is empty.
     */
    public TextSet minus(Object other){
        if (other instanceof String){
            TextSet tmptxt = new TextSet((String) other);
            return new TextSet(functions.minus_textset_text(this._inner, tmptxt._inner));
        }
        else if (other instanceof TextSet){
            return new TextSet(functions.minus_set_set(this._inner,((TextSet) other)._inner));
        }
        else{
            return null;
        }
    }


    /**
     * Returns the union of "this" and "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>union_textset_text</li>
     *             <li>union_set_set</li>
     *
     * @param other A :class:`TextSet` or :class:`str` instance
     * @return A :class:`TextSet` instance.
     */
    public TextSet union(Object other){
        if (other instanceof String){
            TextSet tmptxt = new TextSet((String) other);
            return new TextSet(functions.union_textset_text(this._inner, tmptxt._inner));
        }
        else if (other instanceof TextSet){
            return new TextSet(functions.union_set_set(this._inner,((TextSet) other)._inner));
        }
        else{
            return null;
        }
    }





}
