package utils;

import java.sql.SQLException;
import java.time.LocalDateTime;

import functions.functions;
import jnr.ffi.Pointer;

public class testCustomSerialization {
    public static void main(String[] args) throws SQLException{
        functions.meos_initialize("UTC");

        String period = "[2020-01-08 00:00:00+01, 2020-03-03 00:00:00+01]";

        Pointer pointer = functions.period_in(period);
        long addr = CustomSerialization.getInnerAddress(pointer);
        System.out.println(addr);

        Pointer pointer_out = CustomSerialization.constructPointer(addr);
        String value = functions.period_out(pointer_out);
        System.out.println(value);
        
        functions.meos_finalize();

    }
}
