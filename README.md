SimpleCrud - A simple CRUD UI library for Java Spring Applications
================================================================

* Import SimpleCrud Configuration  
  Example `@Import(SimpleCrudConfiguration.class)`

* Configure Spring beans for entities that need to be CRUDed over a UI

  ```
      @Bean(name = "cityCrudEntity")
      public CrudEntity cityCrudEntity(CityRepository cityRepository){
          return newCrudEntity(City.class)
                  .jpaCrudRepository(cityRepository, Long.class)
                  .model(CrudModelBuilder.jpaCrudModel()
                          .idFieldName("id")
                          .displayFields("name", "state", "country")
                          .filterFields("name", "state", "country")).build();
      }
   ```

* Go to `{{path}}/crud/City/list`
  Thats it.

### Sample project
[simplecrud-sample](https://github.com/vishalmanohar/simplecrud-sample)


### Screenshots

* Listing page http://localhost:8080/crud/City/list

  ![Screenshot](https://github.com/vishalmanohar/simplecrud-sample/blob/master/screenshots/simple-crud-list.png?raw=true)
  
* Add or Edit UI

  ![Screenshot](https://github.com/vishalmanohar/simplecrud-sample/blob/master/screenshots/simplecrud-edit.png?raw=true)
