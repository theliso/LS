package pt.isel.ls.Parser;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface MySupplier {
    Connection get() throws SQLException;
}
