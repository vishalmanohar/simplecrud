SimpleCrud - A simple CRUD UI library for Java Spring Applications
================================================================

* Import SimpleCrud Configuration
  Example `@Import(SimpleCrudConfiguration.class)`

* Configure Spring beans for entities that need to be CRUDed over a UI
  `
      @Bean(name = "cityCrudEntity")
      public CrudEntity cityCrudEntity(CityRepository cityRepository){
          return newCrudEntity(City.class)
                  .jpaCrudRepository(cityRepository, Long.class)
                  .model(CrudModelBuilder.jpaCrudModel()
                          .idFieldName("id")
                          .displayFields("name", "state", "country")
                          .filterFields("name", "state", "country")).build();
      }
   `

* Go to {{path}}/crud/City/list
  Thats it.
