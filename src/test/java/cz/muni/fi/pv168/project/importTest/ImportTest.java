package cz.muni.fi.pv168.project.importTest;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.export.CSVimport;
import cz.muni.fi.pv168.project.export.JsonImport;
import cz.muni.fi.pv168.project.export.service.GenericImportService;
import cz.muni.fi.pv168.project.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.project.wiring.CommonDependencyProvider;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Adam Paulen
 */
public class ImportTest {
    private static final Path PROJECT_ROOT = Paths.get(System.getProperty("project.basedir", "")).toAbsolutePath();
    private static final Path TEST_RESOURCES = PROJECT_ROOT.resolve(Path.of("src", "test", "resources"));
    private GenericImportService genericImportService;
    private DependencyProvider dependencyProvider;

    @BeforeEach
    void setUp() {
        var databaseManager = DatabaseManager.createTestInstance();
        this.dependencyProvider = new CommonDependencyProvider(databaseManager);
        this.genericImportService = new GenericImportService(dependencyProvider.getRideCrudService(),
                                                             dependencyProvider.getTemplateCrudService(),
                                                             dependencyProvider.getCategoryCrudService(),
                                                             dependencyProvider.getCurrencyCrudService(),
                                                             List.of(new CSVimport(dependencyProvider.getCurrencyCrudService()),
                                                                     new JsonImport(dependencyProvider.getCurrencyCrudService())));
    }

    @AfterEach
    void tearDown() {
        dependencyProvider.getDatabaseManager().destroySchema();
    }

    @Test
    void importEmptyCSV() {
        genericImportService.importData(TEST_RESOURCES.resolve("TestEmpty.csv").toString());

        assertThat(dependencyProvider.getRideCrudService().findAll()).isEmpty();
        assertThat(dependencyProvider.getTemplateCrudService().findAll()).isEmpty();
        assertThat(dependencyProvider.getCategoryCrudService().findAll()).isEmpty();
        assertThat(dependencyProvider.getCurrencyCrudService().findAll()).isEmpty();
    }

    @Test
    void importEmptyJSON() {
        genericImportService.importData(TEST_RESOURCES.resolve("TestEmpty.json").toString());

        assertThat(dependencyProvider.getRideCrudService().findAll()).isEmpty();
        assertThat(dependencyProvider.getTemplateCrudService().findAll()).isEmpty();
        assertThat(dependencyProvider.getCategoryCrudService().findAll()).isEmpty();
        assertThat(dependencyProvider.getCurrencyCrudService().findAll()).isEmpty();
    }

    @Test
    void importOneOfEachJSON() {
        genericImportService.importData(TEST_RESOURCES.resolve("TestOneOfEach.json").toString());

        var currency = new Currency("1", "eur", 1);
        var category = new Category("1", "cat");
        var temp = new Template("1", "1",1,currency,category,"1","1",1);
        var ride = new Ride("1", "1",1,currency,1,category,"1","1",1, LocalDate.ofYearDay(2000,1));

        assertThat(dependencyProvider.getRideCrudService().findAll()).containsExactly(ride);
        assertThat(dependencyProvider.getTemplateCrudService().findAll()).containsExactly(temp);
        assertThat(dependencyProvider.getCategoryCrudService().findAll()).containsExactly(category);
        assertThat(dependencyProvider.getCurrencyCrudService().findAll()).containsExactly(currency);
    }

    @Test
    void importOneOfEachCSV() {
        genericImportService.importData(TEST_RESOURCES.resolve("TestOneOfEach.csv").toString());

        assertThat(dependencyProvider.getRideCrudService().findAll()).hasSize(1);
        assertThat(dependencyProvider.getCategoryCrudService().findAll()).hasSize(1);
        assertThat(dependencyProvider.getCurrencyCrudService().findAll()).hasSize(1);
    }

    @Test
    void importFailCSV() {
        assertThrows(RuntimeException.class, () -> genericImportService.importData(TEST_RESOURCES.resolve("TestFail.csv").toString()));
    }

    @Test
    void importFailJSON() {
        assertThrows(RuntimeException.class, () -> genericImportService.importData(TEST_RESOURCES.resolve("TestFail.json").toString()));
    }
}
