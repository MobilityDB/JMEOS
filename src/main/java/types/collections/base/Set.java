package types.collections.base;

import jnr.ffi.Pointer;
import functions.functions;
import org.locationtech.jts.io.ParseException;
import types.basic.tbool.TBoolInst;
import types.basic.tbool.TBoolSeq;
import types.basic.tbool.TBoolSeqSet;

/**
 * Abstract class that represents a set of temporal object
 *
 * @author Nidhal Mareghni
 * @since 10/09/2023
 */
public abstract class Set<T extends Object> implements Collection, Base {
    private Pointer _inner;


    /** ------------------------- Constructors ---------------------------------- */
    public Set(){}

    public Set(Pointer inner){
        this._inner = createInner(inner);
    }
    public Set(String str){
        this._inner = createStringInner(str);
    }

    public abstract Pointer get_inner();
    public abstract Pointer createInner(Pointer inner);
    public abstract Pointer createStringInner(String str);

    /* ------------------------- Output ----------------------------------------

    /** ------------------------- Conversions ----------------------------------- */

    /*
      Returns a SpanSet that contains a Span for each element in "this".
      <p>
              MEOS Functions:
                  <li>set_to_spanset</li>
      @return A new {@link SpanSet} instance
     */
    /*
    public SpanSet to_spanset(){
        return new SpanSet(functions.set_to_spanset(this._inner));
    }

     */

    /*
      Returns a span that encompasses "this".
      <p>
              MEOS Functions:
                  <li>set_span</li>
      @return A new {@link Span} instance
     */
    /*
    public Span to_span(){
        return new Span(functions.set_span(this._inner));
    }

     */

    /* ------------------------- Accessors ------------------------------------- */


    /**
     * Returns the number of elements in "this".
     * <p>
     *         MEOS Functions:
     *             <li>set_num_values</li>
     *
     * @return An {@link Integer}
     */
    public int num_elements(){
        return functions.set_num_values(this._inner);
    }

    /**
     * Returns the number of elements in "this".
     * <p>
     *         MEOS Functions:
     *             <li>set_num_values</li>
     *
     * @return An {@link Integer}
     */
    public int length(){
        return this.num_elements();
    }


    /**
     * Returns the first element in "this".
     *
     * @return A {@link T} instance
     */
    public abstract T start_element() throws ParseException;


    /**
     * Returns the last element in "this".
     *
     * @return A {@link T} instance
     */
    public abstract T end_element() throws ParseException;


    /**
     * Returns the n-th element in "this".
     * @param n the n-th element
     * @return A {@link T} instance
     */
    public T element_n(int n) throws Exception {
        if (n < 0 || n >= this.num_elements()){
            throw new Exception("Index: " + n + "out of bounds");
        }
        else {
            return null;
        }
    }


    /**
     * Return the hash representation of "this".
     * <p>
     *         MEOS Functions:
     *             <li>set_hash</li>
     * @return A new {@link Integer} instance
     */
    public long hash(){
        return functions.set_hash(this._inner);
    }



    /* ------------------------- Topological Operations ------------------------ */


    /**
     * Returns whether "this" is contained in "other".
     *
     *  <p>
     *         MEOS Functions:
     *           <ul>
     *             <li>contained_span_span</li>
     *             <li>contained_span_spanset</li>
     *             <li>contained_set_set</li>
     *             <li>contained_spanset_spanset</li>
     *           </ul>
     *
     *
     * @param other object to compare with
     * @return True if contained, False otherwise
     * @throws Exception
     */
    public boolean is_contained_in(Base other) throws Exception {
        if (other instanceof Set<?>){
            return functions.contained_set_set(this._inner, ((Set<?>) other)._inner);
        }
        else{
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Returns whether "this" contains "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>contains_set_set</li>
     *
     *
     * @param other object to compare with
     * @return True if contains, False otherwise
     * @throws Exception
     */
    public boolean contains(Base other) throws Exception {
        if (other instanceof Set<?>){
            return functions.contains_set_set(this._inner, ((Set<?>) other)._inner);
        }
        else{
            throw new Exception("Operation not supported with this type");
        }
    }

    /**
     * Returns whether "this" overlaps "other". That is, both share at least an instant
     *
     *   <p>
     *         MEOS Functions:
     *           <ul>
     *             <li>overlaps_set_set</li>
     *             <li>overlaps_span_span</li>
     *             <li>overlaps_spanset_spanset</li>
     *           </ul>
     *
     *
     * @param other object to compare with
     * @return True if overlaps, False otherwise
     * @throws Exception
     */
    public boolean overlaps(Base other) throws Exception {
        if (other instanceof Set<?>){
            return functions.overlaps_set_set(this._inner, ((Set<?>) other)._inner);
        }
        else{
            throw new Exception("Operation not supported with this type");
        }
    }


    /* ------------------------- Position Operations --------------------------- */


    /**
     * Returns whether "this" is strictly to the left of "other". That is,
     *         "this" ends before "other" starts.
     *
     *  <p>
     *         MEOS Functions:
     *           <ul>
     *             <li>left_span_span</li>
     *             <li>left_span_spanset</li>
     *           </ul>
     *
     *
     * @param other object to compare with
     * @return True if before, False otherwise
     * @throws Exception
     */
    public boolean is_left(Base other) throws Exception {
        if (other instanceof Set<?>){
            return functions.left_set_set(this._inner, ((Set<?>) other)._inner);
        }
        else{
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Returns whether "this" is to the left of "other" allowing overlap.
     *         That is, "this" ends before "other" ends (or at the same value).
     *
     *  <p>
     *         MEOS Functions:
     *             <li>overleft_span_span</li>
     *             <li>overleft_span_spanset</li>
     *
     *
     * @param other object to compare with
     * @return True if before, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_left(Base other) throws Exception {
        if (other instanceof Set<?>){
            return functions.overleft_set_set(this._inner, ((Set<?>) other)._inner);
        }
        else{
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Returns whether "this" is to the right of "other" allowing overlap. That is, "this" starts after "other"
     *         starts (or at the same value).
     *
     * <p>
     *         MEOS Functions:
     *           <ul>
     *             <li>overright_span_span</li>
     *             <li>overright_span_spanset</li>
     *           </ul>
     *
     *
     * @param other object to compare with
     * @return True if overlapping or to the right, False otherwise
     * @throws Exception
     */
    public boolean is_over_or_right(Base other) throws Exception {
        if (other instanceof Set<?>){
            return functions.overright_set_set(this._inner, ((Set<?>) other)._inner);
        }
        else{
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Returns whether "this" is strictly to the right of "other". That is, the first element in "this"
     *         is to the right "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *           <ul>
     *             <li>right_set_set</li>
     *             <li>right_span_span</li>
     *             <li>right_span_spanset</li>
     *           </ul>
     *
     * @param other object to compare with
     * @return True if right, False otherwise
     * @throws Exception
     */
    public boolean is_right(Base other) throws Exception {
        if (other instanceof Set<?>){
            return functions.right_set_set(this._inner, ((Set<?>) other)._inner);
        }
        else{
            throw new Exception("Operation not supported with this type");
        }
    }


    /* ------------------------- Distance Operations --------------------------- */

    /**
     * Returns the distance between "this" and "other".
     *
     * <p>
     *         MEOS Functions:
     *           <ul>
     *             <li>distance_set_set</li>
     *             <li>distance_span_span</li>
     *             <li>distance_spanset_span</li>
     *           </ul>
     *
     * @param other object to compare with
     * @return A {@link Float} instance
     * @throws Exception
     */
    public float distance(Base other) throws Exception {
        if (other instanceof Set<?>){
            return (float) functions.distance_set_set(this._inner, ((Set<?>) other)._inner);
        } else if (other instanceof Span<?>) {
            return (float) functions.distance_span_span(functions.set_span(this._inner), ((Span<?>) other).get_inner());
        } else if (other instanceof SpanSet<?>) {
            return (float) functions.distance_spanset_span(this._inner,((SpanSet<?>) other).get_inner());
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /* ------------------------- Set Operations -------------------------------- */



    /* ------------------------- Comparisons ----------------------------------- */


    /**
     * Returns whether "this" and "other" are equal.
     *
     *  <p>
     *         MEOS Functions:
     *             <li>set_eq</li>
     *
     * @param other object to compare with
     * @return True if equal, False otherwise
     */
    public boolean eq(Base other){
        if (other instanceof Set<?>){
            return functions.set_eq(this._inner,((Set<?>) other)._inner);
        }
        else {
            return false;
        }
    }

    /**
     * Returns whether "this" and "other" are not equal.
     *
     *  <p>
     *         MEOS Functions:
     *             <li>set_ne</li>
     *
     *
     * @param other object to compare with
     * @return True if not equal, False otherwise
     */
    public boolean notEquals(Base other){
        if (other instanceof Set<?>){
            return functions.set_ne(this._inner,((Set<?>) other)._inner);
        }
        else {
            return true;
        }
    }


    /**
     * Return whether "this" is less than "other".
     *
     *  <p>
     *         MEOS Functions:
     *             <li>set_lt</li>
     *
     * @param other object to compare with
     * @return True if less than, False otherwise
     * @throws Exception
     */
    public boolean lessThan(Base other) throws Exception {
        if (other instanceof Set<?>){
            return functions.set_lt(this._inner,((Set<?>) other)._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }



    /**
     * Return whether "this" is less than or equal to "other".
     *
     * <p>
     *         MEOS Functions:
     *             <li>set_le</li>
     *
     * @param other object to compare with
     * @return True if less than or equal, False otherwise
     * @throws Exception
     */
    public boolean lessThanOrEqual(Base other) throws Exception {
        if (other instanceof Set<?>){
            return functions.set_le(this._inner,((Set<?>) other)._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Return whether "this" is greater than "other".
     *
     *   <p>
     *
     *         MEOS Functions:
     *             <li>set_gt</li>
     *
     * @param other object to compare with
     * @return True if greater than, False otherwise
     * @throws Exception
     */
    public boolean greaterThan(Base other) throws Exception {
        if (other instanceof Set<?>){
            return functions.set_gt(this._inner,((Set<?>) other)._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }


    /**
     * Return whether "this" is greater than or equal to "other".
     *
     *  <p>
     *
     *         MEOS Functions:
     *             <li>set_ge</li>
     *
     * @param other object to compare with
     * @return True if greater than or equal, False otherwise
     * @throws Exception
     */
    public boolean greaterThanOrEqual(Base other) throws Exception {
        if (other instanceof Set<?>){
            return functions.set_ge(this._inner,((Set<?>) other)._inner);
        }
        else {
            throw new Exception("Operation not supported with this type");
        }
    }

}
