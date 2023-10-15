package cz.muni.fi.pv168.project.data;

import cz.muni.fi.pv168.project.model.Category;

import java.util.List;

public class TestDataGenerator {
    public static final List<Category> CATEGORIES = List.of(
            new Category("Business trip"),
            new Category("Personal trip")
    );
}
