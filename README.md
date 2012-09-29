# jFactory

jFactory is a factory library for Java, inspired by the great [factory_girl](https://github.com/thoughtbot/factory_girl) by Thoughtbot.

jFactory can be used to easily define factories for your model objects.
It has a notion of building and creating objects, where creating objects means saving objects to a database after building them.

jFactory is database/persistence layer agnostic, support for databases can easily provided by subclassing the PersistableObjectFactory class.
Currently only MongoDB is supported through the [Morphia](http://code.google.com/p/morphia/) POJO mapper.

# Usage

## Defining factories

```java
public class ArticleFactory extends ObjectFactory<Article> {

    public ArticleFactory() {
        super(Article.class);
    }

    @Override
    protected void define() {
        property("guid", "http://pwiddershoven.nl/blog/2011/01/05/airplayer.html");
        property("title", "Airplayer");

        trait(new Trait("unpublished") {
            public void apply() {
                property("state", Article.State.UNPUBLISHED);
            }
        });

        sequence("id", new Sequence() {
            public Object apply(int n) {
                return n;
            }
        });
    }
    
    @AfterFactoryBuild
    public void afterBuild(Article article) {
        System.out.println(article);
    }
}
```

```java
public class BookFactory extends ObjectFactory<Book> {

    public BookFactory() {
        super(Book.class);
    }

    @Override
    protected void define() {
        constructWith("Enterprise Integration Patterns", "Gregor Hohpe");
        
        // lazy values are evaluated when an object is created through the factory
        // regular values are evaluated when the factory is constructed
        property("createdAt", new LazyValue() {
            @Override
            public Object evaluate() {
                return new Date();
            }
        });
    }
}
```

## Using factories

Factories can be either called directly:

```java
Book book = new BookFactory().build("title", "My Book");
Article article = new ArticleFactory("unpublished").build();
```

Or through the provided Factory class. In this case you have to configure which packages to scan for Factory classes.

```java
// add package to scan for factory classes, so jFactory can find the factories
Factory.addFactoryScanPackage("nl.pwiddershoven.jfactory.factories");

Article article1 = build(Article.class);
Article article2 = build(Article.class, "unpublished", "title", "Some title");
Article article2 = build(Article.class, "author", "Pascal");
````

For more examples, see [FactoryTest.java](https://github.com/PascalW/jFactory/blob/master/core/src/test/java/nl/pwiddershoven/jfactory/FactoryTest.java).

## Persistence

jFactory has the notion of "persistable objects", meaning you can create factories which will automatically persist the created objects through some persistence layer.
jFactory has a flexible architecture, allowing you to easily define custom abstract factories for your favorite persistence layer.
jFactory comes with support for persitence through the [Morphia](http://code.google.com/p/morphia/) Object Mapper.

Defining a factory for a Morphia entity is easy and just like defining a regular Factory:

```java
public class ArticleFactory extends MorphiaBackedPersistableObjectFactory<PersistableArticle> {

    public ArticleFactory() {
        super(PersistableArticle.class);
    }

    @Override
    protected void define() {
        property("id", new ObjectId());
        property("guid", "http://pwiddershoven.nl");
        property("title", "Test");
    }
    
    @AfterFactoryCreate
    public void afterCreate(PersistableArticle article) {
        //do something with the article
    }
}
```

The only difference is the factory class you inherit from must be a `PersistableObjectFactory` like the `MorphiaBackedPersistableObjectFactory`.
PeristableObjectFactories behave like regular factories, but add a `create` method allowing you the build and persist an object.

```java
//first, set connection details so the object factory know where to persist your objects
MorphiaBackedPersistableObjectFactory.setDatastore(datastore);

//using `build` an object is only constructed like when using a regular factory
PersistableArticle article1 = build(PersistableArticle.class, "title", "test");

//using `create` to create an object will cause the object to be persisted too
PersistableArticle article2 = create(PersistableArticle.class, "title", "test");
```

### Custom abstract persistable factories

Definign a custom abstract factory for some persistence layer is easy, see for example the `MorphiaBackedPersistableObjectFactory` implementation:

```java
public abstract class MorphiaBackedPersistableObjectFactory<T> extends PersistableObjectFactory<T> {

    private static Datastore datastore;

    public static void setDatastore(Datastore datastore) {
        MorphiaBackedPersistableObjectFactory.datastore = datastore;
    }

    public MorphiaBackedPersistableObjectFactory(Class<T> factoryClass) {
        super(factoryClass);
    }

    @Override
    protected void persist(T object) {
        datastore.save(object);
    }
}
```

Basically, you only have to define a method that can persist built objects.
PersistableObjectFactories should also allow to set some connection details, like the `setDatastore` method in the example above.