package cz.muni.fi.pv168.project.storage.sql;

import java.util.List;
import java.util.Optional;

import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.entity.TemplateEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.EntityMapper;

public class TemplateSqlRepository implements Repository<Template> {

    private final DataAccessObject<TemplateEntity> templateDao;
    private final EntityMapper<TemplateEntity, Template> templateMapper;

    public TemplateSqlRepository(
            DataAccessObject<TemplateEntity> templateDao,
            EntityMapper<TemplateEntity, Template> templateMapper) {
        this.templateDao = templateDao;
        this.templateMapper = templateMapper;
    }


    @Override
    public List<Template> findAll() {
        return templateDao
                .findAll()
                .stream()
                .map(templateMapper::mapToBusiness)
                .toList();
    }

    @Override
    public void create(Template newEntity) {
        templateDao.create(templateMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(Template entity) {
        var existingTemplate = templateDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Template not found, guid: " + entity.getGuid()));
        var updatedTemplateEntity = templateMapper
                .mapExistingEntityToDatabase(entity, existingTemplate.id());

        templateDao.update(updatedTemplateEntity);
    }

    @Override
    public void deleteByGuid(String guid) {
        templateDao.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        templateDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String guid) {
        return templateDao.existsByGuid(guid);
    }

    @Override
    public Optional<Template> findByGuid(String guid) {
       return templateDao
            .findByGuid(guid)
            .map(templateMapper::mapToBusiness);
    }
}
