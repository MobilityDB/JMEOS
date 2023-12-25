package types.collections.text;

import functions.functions;

import java.sql.SQLException;

public class TestTextSet {
    public static void main(String[] args) throws SQLException {
        functions.meos_initialize("UTC");
        TextSet tset = new TextSet("{A, BB, ccc}");
        System.out.println(tset.start_element());
        //String st = tt.toString();

    }
}
