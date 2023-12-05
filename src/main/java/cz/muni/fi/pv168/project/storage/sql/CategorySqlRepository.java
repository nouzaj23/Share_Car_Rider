package cz.muni.fi.pv168.project.storage.sql;

import java.util.List;
import java.util.Optional;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.entity.CategoryEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.EntityMapper;

public class CategorySqlRepository implements Repository<Category> {

    private final DataAccessObject<CategoryEntity> categoryDao;
    private final EntityMapper<CategoryEntity, Category> categoryMapper;

    public CategorySqlRepository(
            DataAccessObject<CategoryEntity> categoryDao,
            EntityMapper<CategoryEntity, Category> categoryMapper) {
        this.categoryDao = categoryDao;
        this.categoryMapper = categoryMapper;
    }


    @Override
    public List<Category> findAll() {
        return categoryDao
                .findAll()
                .stream()
                .map(categoryMapper::mapToBusiness)
                .toList();
    }

    @Override
    public void create(Category newEntity) {
        categoryDao.create(categoryMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(Category entity) {
        var existingCategory = categoryDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Category not found, guid: " + entity.getGuid()));
        var updatedCategoryEntity = categoryMapper
                .mapExistingEntityToDatabase(entity, existingCategory.id());

        categoryDao.update(updatedCategoryEntity);
    }

    public void updateAfterRideChnaged(Ride changedRide, int distanceDelta, int countDelta) {
        var existingCategory = categoryDao.findByGuid(changedRide.getCategory().getGuid())
                .orElseThrow(() -> new DataStorageException("Category not found, guid: " + changedRide.getCategory().getGuid()));

        var updatedCategoryEntity = new CategoryEntity(existingCategory.id(), existingCategory.guid(), existingCategory.name(), existingCategory.distace() + distanceDelta, existingCategory.rides() + countDelta);

        categoryDao.update(updatedCategoryEntity);
    }

    @Override
    public void deleteByGuid(String guid) {
        categoryDao.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        categoryDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String guid) {
        return categoryDao.existsByGuid(guid);
    }

    @Override
    public Optional<Category> findByGuid(String guid) {
       return categoryDao
            .findByGuid(guid)
            .map(categoryMapper::mapToBusiness);
    }
}
