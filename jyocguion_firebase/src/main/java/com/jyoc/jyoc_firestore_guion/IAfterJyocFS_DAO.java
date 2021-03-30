package com.jyoc.jyoc_firestore_guion;

import java.util.List;

public interface IAfterJyocFS_DAO<T>{

    void afterReadAllElements(List<T> c);

    void afterReadELementByEquality(T c);

    void afterAddElement(boolean result);
    
    void afterDeleteElements(int numElementsDeleted);
    
}
