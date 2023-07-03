package function;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;

public class functions {
    public interface MeosLibrary {
        functions.MeosLibrary INSTANCE = LibraryLoader.create(functions.MeosLibrary.class).load("meos");
        functions.MeosLibrary meos = functions.MeosLibrary.INSTANCE;