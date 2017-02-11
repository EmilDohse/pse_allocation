package data;

import exception.DataException;

@FunctionalInterface
public interface Transaction {

    public void transact() throws DataException;

}
