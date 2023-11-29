package types.collections.text;

import functions.functions;
import jnr.ffi.Pointer;
import org.w3c.dom.Text;
import types.boxes.STBox;

import java.sql.SQLException;

public class TestTextSet {
    public static void main(String[] args) throws SQLException {
        functions.meos_initialize("UTC");
        TextSet tset = new TextSet("{A, BB, ccc}");
        System.out.println(tset.start_element());
        //String st = tt.toString();

    }
}
