package cz.muni.fi.pv168.project.export;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.export.service.GenericExportService;
import cz.muni.fi.pv168.project.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.project.wiring.CommonDependencyProvider;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExportTest {

    private static final Path PROJECT_ROOT = Paths.get(System.getProperty("project.basedir", "")).toAbsolutePath();
    private static final Path TEST_RESOURCES = PROJECT_ROOT.resolve(Path.of("src", "test", "resources"));

    private final Path exportFilePathCSV = TEST_RESOURCES
            .resolve(Instant.now().toString().replace(':', '_') + ".csv");

    private final Path exportFilePathJSON = TEST_RESOURCES
            .resolve(Instant.now().toString().replace(':', '_') + ".json");

    private GenericExportService genericExportService;
    private DependencyProvider dependencyProvider;

    @BeforeEach
    void setUp() {
        var databaseManager = DatabaseManager.createTestInstance();
        this.dependencyProvider = new CommonDependencyProvider(databaseManager);
        this.genericExportService = new GenericExportService(new TestImplPanel(), new TestImplPanel(), new TestImplPanel(),
                                                             new TestEntityModel(), new TestEntityModel(), new TestEntityModel(),
                                                                dependencyProvider, List.of(new CSVexport(), new JsonExport()));
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(exportFilePathCSV)) {
            Files.delete(exportFilePathCSV);
        }
        if (Files.exists(exportFilePathJSON)) {
            Files.delete(exportFilePathJSON);
        }
        dependencyProvider.getDatabaseManager().destroySchema();
    }

    @Test
    void exportEmptyCSV() throws IOException {
        genericExportService.exportData(exportFilePathCSV.toString());

        assertExportedContent("", exportFilePathCSV);
    }

    @Test
    void exportOneOfEachCSV() throws IOException {
        createOneOfEach();
        genericExportService.exportData(exportFilePathCSV.toString());

        assertExportedContent("1,1,eur,1.0,1.0,cat,1,1,1,01 01 00,1", exportFilePathCSV);
    }

    @Test
    void exportOneOfEachJSON() throws IOException {
        createOneOfEach();
        genericExportService.exportData(exportFilePathJSON.toString());

        assertExportedContent("""
                {
                  "rides" : [ {
                    "guid" : "1",
                    "name" : "1",
                    "passengers" : 1,
                    "currency" : {
                      "guid" : "1",
                      "code" : "eur",
                      "conversionRatio" : 1.0
                    },
                    "category" : {
                      "guid" : "1",
                      "name" : "cat"
                    },
                    "from" : "1",
                    "to" : "1",
                    "distance" : 1,
                    "hours" : 0.0,
                    "date" : "2000-01-01",
                    "fuelExpenses" : 1.0
                  } ],
                  "categories" : [ {
                    "guid" : "1",
                    "name" : "cat"
                  } ],
                  "templates" : [ {
                    "guid" : "1",
                    "name" : "1",
                    "passengers" : 1,
                    "currency" : {
                      "guid" : "1",
                      "code" : "eur",
                      "conversionRatio" : 1.0
                    },
                    "category" : {
                      "guid" : "1",
                      "name" : "cat"
                    },
                    "from" : "1",
                    "to" : "1",
                    "distance" : 1,
                    "hours" : 0.0
                  } ],
                  "currencies" : [ {
                    "guid" : "1",
                    "code" : "eur",
                    "conversionRatio" : 1.0
                  } ]
                }
                """, exportFilePathJSON);
    }

    @Test
    void exportEmptyJSON() throws IOException {
        genericExportService.exportData(exportFilePathJSON.toString());

        assertExportedContent("""
                {
                  "rides" : [ ],
                  "categories" : [ ],
                  "templates" : [ ],
                  "currencies" : [ ]
                }
                """, exportFilePathJSON);
    }

    private void assertExportedContent(String expectedContent, Path path) throws IOException {
        assertThat(Files.readString(path))
                .isEqualToIgnoringNewLines(expectedContent);
    }

    private void createOneOfEach(){
        var currency = new Currency("1", "eur", 1);
        var category = new Category("1", "cat");
        var temp = new Template("1", "1",1,currency,category,"1","1",1);
        var ride= new Ride("1", "1",1,currency,1,category,"1","1",1,LocalDate.ofYearDay(2000,1));
        dependencyProvider.getCurrencyCrudService().create(currency);
        dependencyProvider.getCategoryCrudService().create(category);
        dependencyProvider.getRideCrudService().create(ride);
        dependencyProvider.getTemplateCrudService().create(temp);
    }
}
