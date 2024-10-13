package com.redhat.training.camel.database;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.stereotype.Component;

import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;

@Component
public class JpaRoutePersistenceUnit implements PersistenceUnitInfo {
    @Override
    public String getPersistenceUnitName() {
        return "jpaRoute";
    }

    @Override
    public String getPersistenceProviderClassName() {
        return org.hibernate.jpa.HibernatePersistenceProvider.class.getName();
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return PersistenceUnitTransactionType.RESOURCE_LOCAL;
    }

    @Override
    public DataSource getJtaDataSource() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setURL("jdbc:postgresql://localhost:5432/items");
        ds.setUser("developer");
        ds.setPassword("developer");
        return ds;
    }

    @Override
    public DataSource getNonJtaDataSource() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setURL("jdbc:postgresql://localhost:5432/items");
        ds.setUser("developer");
        ds.setPassword("developer");
        return ds;
    }

    @Override
    public List<String> getMappingFileNames() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMappingFileNames'");
    }

    @Override
    public List<URL> getJarFileUrls() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getJarFileUrls'");
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPersistenceUnitRootUrl'");
    }

    @Override
    public List<String> getManagedClassNames() {
        return List.of(com.redhat.training.camel.database.Item.class.getName());
    }

    @Override
    public boolean excludeUnlistedClasses() {
        return false;
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSharedCacheMode'");
    }

    @Override
    public ValidationMode getValidationMode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getValidationMode'");
    }

    @Override
    public Properties getProperties() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProperties'");
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPersistenceXMLSchemaVersion'");
    }

    @Override
    public ClassLoader getClassLoader() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getClassLoader'");
    }

    @Override
    public void addTransformer(ClassTransformer transformer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addTransformer'");
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNewTempClassLoader'");
    }
}
